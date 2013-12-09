package org.blep.spysql;

import lombok.AllArgsConstructor;
import lombok.Delegate;
import lombok.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author blep
 *         Date: 07/12/13
 *         Time: 08:50
 */
@AllArgsConstructor
public class SpyStatement implements Statement {
    @NonNull
    private Collection<SqlListener> listeners;

    private final List<String> batched = new ArrayList<>();

    @NonNull
    @Delegate(excludes = Exclude.class)
    private final Statement delegate;

    private interface Exclude{
        ResultSet executeQuery(String sql) throws SQLException;
        int executeUpdate(String sql) throws SQLException;
        boolean execute(String sql) throws SQLException;
        void addBatch(String sql) throws SQLException;
        void clearBatch() throws SQLException;
        int[] executeBatch() throws SQLException;
        int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException;
        int executeUpdate(String sql, int columnIndexes[]) throws SQLException;
        int executeUpdate(String sql, String columnNames[]) throws SQLException;
        boolean execute(String sql, int autoGeneratedKeys) throws SQLException;
        boolean execute(String sql, int columnIndexes[]) throws SQLException;
        boolean execute(String sql, String columnNames[]) throws SQLException;

    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        final ResultSet resultSet = delegate.executeQuery(sql);
        notifyListeners(sql);
        return resultSet;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        final int i = delegate.executeUpdate(sql);
        notifyListeners(sql);
        return i;
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        final boolean res = delegate.execute(sql);
        notifyListeners(sql);
        return res;
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        batched.add(sql);
        delegate.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        batched.clear();
        delegate.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        final int[] res = delegate.executeBatch();
        for (String sql : batched) {
            notifyListeners(sql);
        }
        batched.clear();
        return res;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        final int res = delegate.executeUpdate(sql, autoGeneratedKeys);
        notifyListeners(sql);
        return res;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        final int res = delegate.executeUpdate(sql, columnIndexes);
        notifyListeners(sql);
        return res;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        final int res = delegate.executeUpdate(sql, columnNames);
        notifyListeners(sql);
        return res;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        final boolean res = delegate.execute(sql, autoGeneratedKeys);
        notifyListeners(sql);
        return res;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        final boolean res = delegate.execute(sql, columnIndexes);
        notifyListeners(sql);
        return res;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        final boolean res = delegate.execute(sql, columnNames);
        notifyListeners(sql);
        return res;
    }

    private void notifyListeners(String sql){
        for (SqlListener listener : listeners) {
            listener.queryExecuted(sql);
        }
    }
}
