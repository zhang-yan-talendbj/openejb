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
package org.openejb.test.stateful;

import org.openejb.test.TestFailureException;
import org.openejb.test.TestManager;

/**
 * [4] Should be run as the fourth test suite of the StatefulTestClients
 */
public class StatefulJndiEncTests extends StatefulTestClient {

    protected EncStatefulHome ejbHome;
    protected EncStatefulObject ejbObject;

    public StatefulJndiEncTests() {
        super("JNDI_ENC.");
    }

    protected void setUp() throws Exception {
        super.setUp();
        Object obj = initialContext.lookup("client/tests/stateful/EncBean");
        ejbHome = (EncStatefulHome) javax.rmi.PortableRemoteObject.narrow(obj, EncStatefulHome.class);
        ejbObject = ejbHome.create("Enc Bean");
        
        /*[2] Create database table */
        TestManager.getDatabase().createEntityTable();
        TestManager.getDatabase().createEntityExplicitePKTable();
    }

    /**
     * Tears down the fixture, for example, close a network connection.
     * This method is called after a test is executed.
     */
    protected void tearDown() throws Exception {
        try {
            /*[1] Drop database table */
            TestManager.getDatabase().dropEntityTable();
            TestManager.getDatabase().dropEntityExplicitePKTable();
        } catch (Exception e) {
            throw e;
        } finally {
            super.tearDown();
        }
    }

    public void test01_lookupStringEntry() {
        try {
            ejbObject.lookupStringEntry();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test02_lookupDoubleEntry() {
        try {
            ejbObject.lookupDoubleEntry();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test03_lookupLongEntry() {
        try {
            ejbObject.lookupLongEntry();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test04_lookupFloatEntry() {
        try {
            ejbObject.lookupFloatEntry();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test05_lookupIntegerEntry() {
        try {
            ejbObject.lookupIntegerEntry();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test06_lookupShortEntry() {
        try {
            ejbObject.lookupShortEntry();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test07_lookupBooleanEntry() {
        try {
            ejbObject.lookupBooleanEntry();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test08_lookupByteEntry() {
        try {
            ejbObject.lookupByteEntry();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test09_lookupEntityBean() {
        try {
            ejbObject.lookupEntityBean();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test10_lookupStatefulBean() {
        try {
            ejbObject.lookupStatefulBean();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test11_lookupStatelessBean() {
        try {
            ejbObject.lookupStatelessBean();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test12_lookupResource() {
        try {
            ejbObject.lookupResource();
        } catch (TestFailureException e) {
            throw e.error;
        } catch (Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

}
