package org.blep.spysql;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * User: blep
 * Date: 09/12/13
 * Time: 09:02
 */

public class TestUtils {
    public static final String SQL_INF_SCHEMA = "select * from  information_schema.users";
    public static final String INSERT_TEST="insert into test(name) values ('bali balo')";
    public static final String CREATE_TABLE_TEST = "create table if not exists test (id identity, name varchar(255))";
    public static final String SELECT_TEST = "select * from test";


    public static SpyDataSource buildSpyDatasource() {
        final BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.h2.Driver");
        basicDataSource.setUrl("jdbc:h2:mem:sample");
        basicDataSource.setUsername("sa");
        basicDataSource.setPassword("");

        final SpyDataSource spyDs = new SpyDataSource(basicDataSource);
        return spyDs;
    }
}
