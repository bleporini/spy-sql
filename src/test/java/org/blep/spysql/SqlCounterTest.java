package org.blep.spysql;

import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author blep
 */
public class SqlCounterTest {

    @Test
    public void should_reset() throws Exception {
        final SqlCounter counter = new SqlCounter();

        assertThat(counter.getCount()).isEqualTo(0);

        counter.queryExecuted("bali balo");
        assertThat(counter.getCount()).isEqualTo(1);

        counter.reset();
        assertThat(counter.getCount()).isEqualTo(0);
    }

    @Test
    public void should_be_thread_safe() throws Exception {
        final ExecutorService service = newFixedThreadPool(getRuntime().availableProcessors());
        final SqlCounter counter = new SqlCounter();
        final int total = 10000000;
        final Runnable action = new Runnable() {
            @Override
            public void run() {
                counter.queryExecuted("bali balo");
            }
        };
        for (int i = 0; i < total; i++) {
            service.execute(action);
        }
        service.shutdown();
        service.awaitTermination(10, SECONDS);

        assertThat(counter.getCount()).isEqualTo(total);

    }
}
