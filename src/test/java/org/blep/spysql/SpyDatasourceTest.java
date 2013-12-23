package org.blep.spysql;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.fest.assertions.Assertions.assertThat;
import static org.blep.spysql.TestUtils.*;

/**
 * @author blep
 *         Date: 08/12/13
 *         Time: 06:49
 */
public class SpyDataSourceTest {
    private SpyDataSource datasource;

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

    @Test
    public void should_be_thread_safe() throws Exception {
        final ExecutorService service = newFixedThreadPool(getRuntime().availableProcessors());
        final int total = 100000;
        final Runnable action = new Runnable() {
            @Override
            public void run() {
                datasource.addListener(new SqlCounter());
            }
        };
        for (int i = 0; i < total; i++) {
            service.execute(action);
        }
        service.shutdown();
        service.awaitTermination(10, SECONDS);

        final Collection listeners = getPrivateListeners();

        assertThat(listeners.size()).isEqualTo(total);
    }

    @Test
    public void should_remove_listener() throws Exception {
        final SqlCounter listener1 = new SqlCounter();
        final SqlCounter listener2 = new SqlCounter();

        final Collection listeners = getPrivateListeners();
        assertThat(listeners).hasSize(0);

        datasource.addListener(listener1);
        datasource.addListener(listener2);

        assertThat(listeners).hasSize(2);
        assertThat(listeners).contains(listener1, listener2);

        datasource.removeListener(listener1);
        assertThat(listeners).contains(listener2);
        assertThat(listeners).hasSize(1);

    }

    private Collection getPrivateListeners() throws NoSuchFieldException, IllegalAccessException {
        final Field field = SpyDataSource.class.getDeclaredField("listeners");
        field.setAccessible(true);
        return (Collection) field.get(datasource);
    }
}
