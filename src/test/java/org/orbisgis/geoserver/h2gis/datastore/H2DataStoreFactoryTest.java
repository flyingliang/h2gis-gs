/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.orbisgis.geoserver.h2gis.datastore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.DataStore;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.h2.tools.Server;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;


/**
 * 
 *
 * @source $URL$
 */
public class H2DataStoreFactoryTest  {
    H2GISDataStoreFactory factory;
    HashMap params;
    
    @Before
    protected void setUp() throws Exception {
        factory = new H2GISDataStoreFactory();
        params = new HashMap();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "h2gis");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "h2gis");
        
    }

    public void testCanProcess() throws Exception {
        assertFalse(factory.canProcess(Collections.EMPTY_MAP));
        assertTrue(factory.canProcess(params));
    }
    
    public void testCreateDataStore() throws Exception {
        JDBCDataStore ds = factory.createDataStore( params );
        assertNotNull( ds );
        assertTrue(ds.getDataSource() instanceof ManageableDataSource);
    }

    public void testCreateDataStoreMVCC() throws Exception {
        Map clonedParams = new HashMap(params);
        clonedParams.put(H2GISDataStoreFactory.MVCC.key, true);
        JDBCDataStore ds = factory.createDataStore(clonedParams);
        assertNotNull(ds);
        final DataSource source = ds.getDataSource();
        assertNotNull(source);
        final DataSource wrapped = source.unwrap(DataSource.class);
        assertNotNull(wrapped);
        if (wrapped instanceof BasicDataSource) {
            final BasicDataSource basicSource = (BasicDataSource) wrapped;
            final String url = basicSource.getUrl();
            assertTrue(url.contains("MVCC=true"));
        }
    }

    public void testTCP() throws Exception {
        HashMap params = new HashMap();
        params.put(H2GISDataStoreFactory.HOST.key, "localhost");
        params.put(H2GISDataStoreFactory.DATABASE.key, "geotools");
        params.put(H2GISDataStoreFactory.USER.key, "h2gis");
        params.put(H2GISDataStoreFactory.PASSWD.key, "h2gis");
        
        DataStore ds = factory.createDataStore(params);
        try {
            ds.getTypeNames();
            //fail("Should not have made a connection.");
        }
        catch(Exception ok) {}
        
        Server server = Server.createTcpServer(new String[]{"-baseDir", "target"});
        server.start();
        try {
            while(!server.isRunning(false)) {
                Thread.sleep(100);
            }
            
            ds = factory.createDataStore(params);
            ds.getTypeNames();
        }
        finally {
            server.shutdown();
        }
    }
}