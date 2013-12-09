package org.blep.spysql;

/**
 * @author blep
 *         Date: 07/12/13
 *         Time: 09:10
 */
public interface SqlListener {
    void queryExecuted(String sql);
}
