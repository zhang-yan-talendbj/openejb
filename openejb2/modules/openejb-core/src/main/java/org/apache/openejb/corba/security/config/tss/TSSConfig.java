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
package org.apache.openejb.corba.security.config.tss;

import java.io.Serializable;
import javax.net.ssl.SSLSession;
import javax.security.auth.Subject;

import org.omg.CORBA.ORB;
import org.omg.CSI.EstablishContext;
import org.omg.IOP.Codec;
import org.omg.IOP.TaggedComponent;

import org.apache.geronimo.security.deploy.DefaultPrincipal;

import org.apache.openejb.corba.security.SASException;


/**
 * @version $Rev$ $Date$
 */
public class TSSConfig implements Serializable {

    private boolean inherit;
    private DefaultPrincipal defaultPrincipal;
    private TSSTransportMechConfig transport_mech;
    private final TSSCompoundSecMechListConfig mechListConfig = new TSSCompoundSecMechListConfig();

    public boolean isInherit() {
        return inherit;
    }

    public void setInherit(boolean inherit) {
        this.inherit = inherit;
    }

    public DefaultPrincipal getDefaultPrincipal() {
        return defaultPrincipal;
    }

    public void setDefaultPrincipal(DefaultPrincipal defaultPrincipal) {
        this.defaultPrincipal = defaultPrincipal;
    }

    public TSSTransportMechConfig getTransport_mech() {
        return transport_mech;
    }

    public void setTransport_mech(TSSTransportMechConfig transport_mech) {
        this.transport_mech = transport_mech;
    }

    public TSSCompoundSecMechListConfig getMechListConfig() {
        return mechListConfig;
    }

    public TaggedComponent generateIOR(ORB orb, Codec codec) throws Exception {
        return mechListConfig.encodeIOR(orb, codec);
    }

    public Subject check(SSLSession session, EstablishContext msg) throws SASException {

        Subject transportSubject = transport_mech.check(session);
        
        Subject mechSubject = mechListConfig.check(msg);
        if (mechSubject != null) return mechSubject;

        return transportSubject;
    }
}
