/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openejb.assembler.classic;

import java.io.File;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import javax.naming.Context;

import org.apache.openejb.persistence.PersistenceClassLoaderHandler;
import org.apache.openejb.persistence.PersistenceUnitInfoImpl;
import org.apache.openejb.spi.ContainerSystem;
import org.apache.openejb.loader.SystemInstance;

public class PersistenceBuilder {
    public static final String PROVIDER_PROP = "javax.persistence.provider";

    public static final String TRANSACTIONTYPE_PROP = "javax.persistence.transactionType";

    public static final String JTADATASOURCE_PROP = "javax.persistence.jtaDataSource";

    public static final String NON_JTADATASOURCE_PROP = "javax.persistence.nonJtaDataSource";

    private static final String DEFAULT_PERSISTENCE_PROVIDER = "org.apache.openjpa.persistence.PersistenceProviderImpl";

    /**
     * External handler which handles adding a runtime ClassTransformer to the classloader.
     */
    private final PersistenceClassLoaderHandler persistenceClassLoaderHandler;

    /**
     * If set, overrides the persistence provider class name in the persistence.xml.
     */
    private String providerEnv;

    /**
     * If set, overrides the transaction type in the persistence.xml.
     */
    private String transactionTypeEnv;

    /**
     * If set, overrides the jta data source class name in the persistence.xml.
     */
    private String jtaDataSourceEnv;

    /**
     * If set, overrides the non-jta data source class name in the persistence.xml.
     */
    private String nonJtaDataSourceEnv;

    public PersistenceBuilder(PersistenceClassLoaderHandler persistenceClassLoaderHandler) {
        loadSystemProps();
        this.persistenceClassLoaderHandler = persistenceClassLoaderHandler;
    }

    private void loadSystemProps() {
        providerEnv = System.getProperty(PROVIDER_PROP);
        transactionTypeEnv = System.getProperty(TRANSACTIONTYPE_PROP);
        jtaDataSourceEnv = System.getProperty(JTADATASOURCE_PROP);
        nonJtaDataSourceEnv = System.getProperty(NON_JTADATASOURCE_PROP);
    }

    public EntityManagerFactory createEntityManagerFactory(PersistenceUnitInfo info, ClassLoader classLoader) throws Exception {
        PersistenceUnitInfoImpl unitInfo = new PersistenceUnitInfoImpl(persistenceClassLoaderHandler);

        // Persistence Unit Name
        unitInfo.setPersistenceUnitName(info.name);

        // Persistence Provider Class Name
        if (providerEnv != null) {
            unitInfo.setPersistenceProviderClassName(providerEnv);
        } else {
            unitInfo.setPersistenceProviderClassName(info.provider);
        }

        // ClassLoader
        unitInfo.setClassLoader(classLoader);

        // Exclude Unlisted Classes
        unitInfo.setExcludeUnlistedClasses(info.excludeUnlistedClasses);

        // Jar File Urls
        unitInfo.setJarFileUrls(info.jarFiles);

        // JTA Datasource
        String dataSource = info.jtaDataSource;
        if (jtaDataSourceEnv != null) dataSource = jtaDataSourceEnv;
        if (dataSource != null) {
            if (System.getProperty("duct tape") == null){
                Context context = SystemInstance.get().getComponent(ContainerSystem.class).getJNDIContext();
                DataSource jtaDataSource = (DataSource) context.lookup(dataSource);
                unitInfo.setJtaDataSource(jtaDataSource);
            }
        }

        // Managed Class Names
        unitInfo.setManagedClassNames(info.classes);

        // Mapping File Names
        unitInfo.setMappingFileNames(info.mappingFiles);

        // Handle Properties
        unitInfo.setProperties(info.properties);

        // Persistence Unit Transaction Type
        if (transactionTypeEnv != null) {
            try {
                // Override with sys vars
                PersistenceUnitTransactionType type = Enum.valueOf(PersistenceUnitTransactionType.class, transactionTypeEnv.toUpperCase());
                unitInfo.setTransactionType(type);
            } catch (IllegalArgumentException e) {
                throw (IllegalArgumentException)(new IllegalArgumentException("Unknown " + TRANSACTIONTYPE_PROP + ", valid options are " + PersistenceUnitTransactionType.JTA + " or " + PersistenceUnitTransactionType.RESOURCE_LOCAL).initCause(e));
            }
        } else {
            PersistenceUnitTransactionType type = Enum.valueOf(PersistenceUnitTransactionType.class, info.transactionType);
            unitInfo.setTransactionType(type);
        }

        // Non JTA Datasource
        String nonJta = info.nonJtaDataSource;
        if (nonJtaDataSourceEnv != null) nonJta = nonJtaDataSourceEnv;
        if (nonJta != null) {
            if (System.getProperty("duct tape") == null){
                Context context = SystemInstance.get().getComponent(ContainerSystem.class).getJNDIContext();
                DataSource nonJtaDataSource = (DataSource) context.lookup(nonJta);
                unitInfo.setNonJtaDataSource(nonJtaDataSource);
            }
        }

        // Persistence Unit Root Url
        unitInfo.setPersistenceUnitRootUrl(new File(info.persistenceUnitRootUrl).toURL());

        // create the persistence provider
        String persistenceProviderClassName = unitInfo.getPersistenceProviderClassName();
        if (persistenceProviderClassName == null) {
            persistenceProviderClassName = DEFAULT_PERSISTENCE_PROVIDER;
        }
        Class clazz = classLoader.loadClass(persistenceProviderClassName);
        PersistenceProvider persistenceProvider = (PersistenceProvider) clazz.newInstance();

        // Create entity manager factory
        EntityManagerFactory emf = persistenceProvider.createContainerEntityManagerFactory(unitInfo, new HashMap());
        return emf;
    }
}
