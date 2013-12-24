package io.blep.spysql;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import static io.blep.spysql.TestUtils.*;
import static org.fest.assertions.Assertions.assertThat;

public class SpyPreparedStatementTest {

    private SpyDataSource ds;
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


    @Test
    public void should_notify_executeQuery() throws Exception {
        final SqlCounter counter = new SqlCounter();
        ds.addListener(counter);

        final PreparedStatement statement = conn.prepareStatement(SELECT_TEST);
        assertThat(counter.getCount()).isEqualTo(0);

        statement.executeQuery();
        assertThat(counter.getCount()).isEqualTo(1);

    }

    @Test
    public void should_notify_executeUpdate() throws Exception {
        final SqlCounter counter = new SqlCounter();
        ds.addListener(counter);

        final PreparedStatement statement = conn.prepareStatement(INSERT_TEST);
        assertThat(counter.getCount()).isEqualTo(0);

        statement.executeUpdate();
        assertThat(counter.getCount()).isEqualTo(1);
    }

    @Test
    public void should_notify_execute() throws Exception {
        final SqlCounter counter = new SqlCounter();
        ds.addListener(counter);

        final PreparedStatement statement = conn.prepareStatement(INSERT_TEST);
        assertThat(counter.getCount()).isEqualTo(0);

        statement.execute();
        assertThat(counter.getCount()).isEqualTo(1);
    }

    @Test
    public void batchTest() throws Exception {
        final SqlCounter counter = new SqlCounter();
        ds.addListener(counter);

        PreparedStatement statement = conn.prepareStatement(INSERT_TEST);
        assertThat(counter.getCount()).isEqualTo(0);

        statement.addBatch();
        assertThat(counter.getCount()).isEqualTo(0);

        statement.clearBatch();
        assertThat(counter.getCount()).isEqualTo(0);

        statement.addBatch();
        statement.addBatch();
        assertThat(counter.getCount()).isEqualTo(0);

        statement.executeBatch();

        assertThat(counter.getCount()).isEqualTo(2);

        statement.executeBatch();
        assertThat(counter.getCount()).isEqualTo(2);
    }
}
