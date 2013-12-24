package io.blep.spysql;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;
import static java.sql.Statement.NO_GENERATED_KEYS;
import static io.blep.spysql.TestUtils.SQL_INF_SCHEMA;
import static io.blep.spysql.TestUtils.buildSpyDatasource;
import static org.fest.assertions.Assertions.assertThat;


/**
 * User: blep
 * Date: 09/12/13
 * Time: 09:15
 */

public class SpyConnectionTest {

    private Connection conn;

    @Before
    public void setUp() throws Exception {
        conn = buildSpyDatasource().getConnection();
    }

    @Test
    public void shloud_create_a_spy_statement() throws Exception {
        assertThat(conn.createStatement()).isInstanceOf(SpyStatement.class);

        assertThat(conn.createStatement(TYPE_FORWARD_ONLY,CONCUR_READ_ONLY)
        ).isInstanceOf(SpyStatement.class);

        assertThat(conn.createStatement(
                TYPE_FORWARD_ONLY, CONCUR_READ_ONLY, HOLD_CURSORS_OVER_COMMIT)
        ).isInstanceOf(SpyStatement.class);
    }

    @Test
    public void should_prepare_a_spy_prepared_statement() throws Exception {
        assertThat(conn.prepareStatement(SQL_INF_SCHEMA))
                .isInstanceOf(SpyPreparedStatement.class);

        assertThat(
            conn.prepareStatement(SQL_INF_SCHEMA,
                    NO_GENERATED_KEYS)
        ).isInstanceOf(SpyPreparedStatement.class);

        assertThat(
            conn.prepareStatement(SQL_INF_SCHEMA,
                    new int[]{})
        ).isInstanceOf(SpyPreparedStatement.class);

        assertThat(
                conn.prepareStatement(SQL_INF_SCHEMA,
                        TYPE_FORWARD_ONLY, CONCUR_READ_ONLY)
        ).isInstanceOf(SpyPreparedStatement.class);

        assertThat(
                conn.prepareStatement(SQL_INF_SCHEMA,
                        TYPE_FORWARD_ONLY, CONCUR_READ_ONLY, HOLD_CURSORS_OVER_COMMIT)
        ).isInstanceOf(SpyPreparedStatement.class);

        assertThat(
                conn.prepareStatement(SQL_INF_SCHEMA,
                        new String[]{})
        ).isInstanceOf(SpyPreparedStatement.class);


    }

}
