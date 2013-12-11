package org.blep.spysql;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.blep.spysql.TestUtils.SQL_INF_SCHEMA;
import static org.fest.assertions.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.blep.spysql.TestUtils.buildSpyDatasource;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * User: blep
 * Date: 09/12/13
 * Time: 09:05
 */

public class SpyStatementTest {

    private SpyDatasource ds;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        ds = buildSpyDatasource();
        conn = ds.getConnection();

        final Statement statement = conn.createStatement();
        statement.execute("create table if not exists test (name varchar(255))");
        statement.execute("insert into test values ('bali balo')");

    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    private interface Job{
        void doIt(Statement statement) throws SQLException;
    }

    private void testListener(Job toDo) throws SQLException {
        Statement statement = null;
        try{
            final SqlListener mock = mock(SqlListener.class);
            ds.addListener(mock);
            statement = conn.createStatement();
            toDo.doIt(statement);
            verify(mock).queryExecuted(anyString());
        }finally {
            statement.close();
        }

    }

    @Test
    public void should_notify_listener_execute_query() throws Exception {
        testListener(new Job() {
            @Override
            public void doIt(Statement statement) throws SQLException {
                statement.executeQuery("select * from test");
            }
        });
    }

    @Test
    public void should_notify_listener_with_execute() throws Exception {
        testListener(new Job() {
            @Override
            public void doIt(Statement statement) throws SQLException {
                statement.execute(SQL_INF_SCHEMA);
            }
        });
    }



}
