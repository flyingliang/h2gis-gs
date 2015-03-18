/*
 * h2gis-gs is an extension to geoserver to connect H2GIS a spatial library 
 * that brings spatial support to the H2 Java database.
 *
 * h2gis-gs  is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2014-2015 IRSTV (FR CNRS 2488)
 *
 * h2gis-gs  is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * h2gis-gs  is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * h2gis-gs. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.geoserver.h2gis.datastore;

import com.vividsolutions.jts.io.WKTReader;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Erwan Bocher
 */
public abstract class H2GISDBTestSetUp {
    
    private static final String DB_NAME = "H2GISDBTest";
    
    private H2GISDataStoreFactory factory;
    private HashMap params;
    public JDBCDataStore ds;
    public WKTReader wKTReader;
    

    @Before
    public void setDatabase() throws Exception {
        factory = new H2GISDataStoreFactory();
        factory.setBaseDirectory(new File(getDataBasePath(DB_NAME)));
        params = new HashMap();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/h2gis");
        params.put(JDBCDataStoreFactory.DATABASE.key, "h2gis");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "h2gis");
        ds = factory.createDataStore( params );
        wKTReader = new WKTReader();
    }
    
    private static String getDataBasePath(String dbName) {
        if (dbName.startsWith("file://")) {
            return new File(URI.create(dbName)).getAbsolutePath();
        } else {
            return new File("target/test-resources/dbH2" + dbName).getAbsolutePath();
        }
    }

    @After
    public void tearDownDatabase() throws Exception {
        ds.getDataSource().getConnection().close();
    }
    
    @Test
    public void test_CreateDataStore() throws Exception {
           assertTrue(true);
    }
}
