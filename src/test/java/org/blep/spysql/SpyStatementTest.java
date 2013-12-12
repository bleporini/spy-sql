package org.blep.spysql;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.blep.spysql.TestUtils.*;
import static org.fest.assertions.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        conn.setAutoCommit(true);
        final Statement statement = conn.createStatement();
        statement.execute(CREATE_TABLE_TEST);
        statement.execute(INSERT_TEST);

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
    public void should_notify_listener_execute_update() throws Exception {
        testListener(new Job() {
            @Override
            public void doIt(Statement statement) throws SQLException {
                statement.executeUpdate(INSERT_TEST);
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

    @Test
    public void batchTest() throws Exception {
        final SqlCounter counter = new SqlCounter();
        ds.addListener(counter);

        Statement statement = conn.createStatement();
        statement.addBatch(INSERT_TEST);
        assertThat(counter.getCount()).isEqualTo(0);

        statement.clearBatch();
        assertThat(counter.getCount()).isEqualTo(0);

        statement.addBatch(INSERT_TEST);
        statement.addBatch(INSERT_TEST);
        assertThat(counter.getCount()).isEqualTo(0);

        statement.executeBatch();

        assertThat(counter.getCount()).isEqualTo(2);
    }

    @Test
    public void should_executeUpdate_and_return_id() throws Exception {
        testListener(new Job() {
            @Override
            public void doIt(Statement statement) throws SQLException {
                statement.executeUpdate(INSERT_TEST, RETURN_GENERATED_KEYS);

                final ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                final long id = keys.getLong(1);
                System.out.println("id = " + id);
            }
        });
    }

    @Test
    public void should_executeUpdate_and_return_id2() throws Exception {
        testListener(new Job() {
            @Override
            public void doIt(Statement statement) throws SQLException {
                statement.executeUpdate(INSERT_TEST, new int[]{1});

                final ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                final long id = keys.getLong(1);
                System.out.println("id = " + id);
            }
        });
    }

    @Test
    public void should_executeUpdate_and_return_id3() throws Exception {
        testListener(new Job() {
            @Override
            public void doIt(Statement statement) throws SQLException {
                statement.executeUpdate(INSERT_TEST, new String[]{"id"});

                final ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                final long id = keys.getLong(1);
                System.out.println("id = " + id);
            }
        });
    }

    @Test
    public void should_execute_and_return_id() throws Exception {
        testListener(new Job() {
            @Override
            public void doIt(Statement statement) throws SQLException {
                statement.execute(INSERT_TEST, RETURN_GENERATED_KEYS);

                final ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                final long id = keys.getLong(1);
                System.out.println("id = " + id);
            }
        });
    }

    @Test
    public void should_execute_with_col_names() throws Exception {
        testListener(new Job() {
            @Override
            public void doIt(Statement statement) throws SQLException {
                statement.execute(INSERT_TEST, new String[]{"id"});

                final ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                final long id = keys.getLong(1);
                System.out.println("id = " + id);
            }
        });
    }

    @Test
    public void should_execute_with_col_indexes() throws Exception {
        testListener(new Job() {
            @Override
            public void doIt(Statement statement) throws SQLException {
                statement.execute(INSERT_TEST, new int[]{0});

                final ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                final long id = keys.getLong(1);
                System.out.println("id = " + id);
            }
        });
    }
}
