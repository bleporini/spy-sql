package io.blep.spysql;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * User: blep
 */
@ThreadSafe
public class SqlCounter implements SqlListener {
    private final AtomicInteger count=new AtomicInteger(0);

    @Override
    public void queryExecuted(String sql) {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.intValue();
    }

    public void reset(){
        count.set(0);
    }
}
