package org.blep.spysql;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.fest.assertions.Assert.*;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author blep
 *         Date: 08/12/13
 *         Time: 06:49
 */
public class SpyDatasourceTest {
    private SpyDatasource datasource;

    @Before
    public void setUp() throws Exception {
        final BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.h2.Driver");
        basicDataSource.setUrl("jdbc:h2:mem:sample");
        basicDataSource.setUsername("sa");
        basicDataSource.setPassword("");

        datasource = new SpyDatasource();
        datasource.setDelegate(basicDataSource);
    }

    @Test
    public void should_create_datasource() throws Exception {
        assertThat(datasource).isNotNull();

    }
}
