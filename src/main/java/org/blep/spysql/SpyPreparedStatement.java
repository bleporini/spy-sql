package org.blep.spysql;

import lombok.Delegate;
import lombok.NonNull;
import net.jcip.annotations.NotThreadSafe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author blep
 */
@NotThreadSafe
public class SpyPreparedStatement implements PreparedStatement{

    private final Collection<SqlListener> listeners;

    private final String sql;

    private int batchSize=0;

    public SpyPreparedStatement(Collection<SqlListener> listeners, String sql, PreparedStatement delegate
                                ) {
        this.listeners = listeners;
        this.sql = sql;
        this.delegate = delegate;
        this.statementDelegate = new SpyStatement(listeners,delegate);
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        final ResultSet res = delegate.executeQuery();
        notifyListeners();
        return res;
    }

    @Override
    public int executeUpdate() throws SQLException {
        final int res = delegate.executeUpdate();
        notifyListeners();
        return res;
    }

    @Override
    public boolean execute() throws SQLException {
        final boolean res = delegate.execute();
        notifyListeners();
        return res;
    }

    @Override
    public void addBatch() throws SQLException {
        delegate.addBatch();
        batchSize++;
    }

    @Override
    public void clearBatch() throws SQLException {
        batchSize=0;
    }

    @Override
    public int[] executeBatch() throws SQLException {
        final int[] res = delegate.executeBatch();
        for(int i = 0 ; i<batchSize; i++)
            notifyListeners();
        batchSize=0;
        return res;
    }

    @NonNull
    @Delegate(excludes = {SpyStatement.class,Excludes.class, Excludes2.class})
    private final PreparedStatement delegate;

    @Delegate(excludes = Excludes2.class)
    private final SpyStatement statementDelegate;



    private interface Excludes{
        ResultSet executeQuery() throws SQLException;
        int executeUpdate() throws SQLException;
        boolean execute() throws SQLException;
        void addBatch() throws SQLException;

    }

    private interface Excludes2{
        void clearBatch() throws SQLException;
        int[] executeBatch() throws SQLException;
    }

    private void notifyListeners(){
        for (SqlListener listener : listeners) {
            listener.queryExecuted(sql);
        }
    }
}
