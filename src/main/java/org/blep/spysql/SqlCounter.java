package org.blep.spysql;

import lombok.Getter;
import net.jcip.annotations.NotThreadSafe;

/**
 *
 * User: blep
 * Date: 09/12/13
 * Time: 09:07
 */
@NotThreadSafe
public class SqlCounter implements SqlListener {
    @Getter
    private int count=0;

    @Override
    public void queryExecuted(String sql) {
        count++;
    }

    public void reset(){
        count = 0;
    }
}
