package org.blep.spysql;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * User: blep
 * Date: 09/12/13
 * Time: 09:02
 */

public class TestUtils {
    public static final String SQL_INF_SCHEMA = "select * from  information_schema.users";

    public static SpyDatasource buildSpyDatasource() {
        final BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.h2.Driver");
        basicDataSource.setUrl("jdbc:h2:mem:sample");
        basicDataSource.setUsername("sa");
        basicDataSource.setPassword("");

        final SpyDatasource spyDs = new SpyDatasource();
        spyDs.setDelegate(basicDataSource);
        return spyDs;
    }
}
