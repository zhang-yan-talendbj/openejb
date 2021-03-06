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
package org.apache.openejb.test.entity.cmr.onetomany;

import java.util.Set;
import javax.ejb.EJBLocalObject;

/**
 * @version $Revision: 451417 $ $Date: 2006-09-29 13:13:22 -0700 (Fri, 29 Sep 2006) $
 */
public interface ArtistLocal extends EJBLocalObject {
    // CMP
    public Integer getId();
    public void setId(Integer id);

    public String getName();
    public void setName(String name);
    
    // CMR
    public Set<SongLocal> getPerformed();
    public void setPerformed(Set<SongLocal> performed);

    public Set<SongLocal> getComposed();
    public void setComposed(Set<SongLocal> composed);
}
