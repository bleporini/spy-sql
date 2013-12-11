package org.blep.spysql;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.blep.spysql.TestUtils.*;

/**
 * @author blep
 *         Date: 08/12/13
 *         Time: 06:49
 */
public class SpyDatasourceTest {
    private SpyDatasource datasource;

    @Before
    public void setUp() throws Exception {
        datasource =  buildSpyDatasource();
    }



    @Test
    public void should_create_datasource() throws Exception {
        assertThat(datasource).isNotNull();
    }

    @Test
    public void should_create_a_spy_connection() throws Exception {
        assertThat(datasource.getConnection()).isInstanceOf(SpyConnection.class);
    }
}
