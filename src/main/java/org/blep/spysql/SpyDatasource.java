package org.blep.spysql;

import lombok.Delegate;
import lombok.Setter;
import net.jcip.annotations.ThreadSafe;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This datasource implementation must be exclusively used for testing purposes.
 * Do not use for production.
 *
 * @author blep
 */
@ThreadSafe
public class SpyDatasource implements DataSource{

    private final Collection<SqlListener> listeners = new CopyOnWriteArrayList<>();

    public SpyDatasource addListener(SqlListener listener) {
        listeners.add(listener);
        return this;
    }

    public SpyDatasource removeListener(SqlListener listener) {
        listeners.remove(listener);
        return this;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new SpyConnection(listeners,delegate.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new SpyConnection(listeners,delegate.getConnection(username, password));
    }

    private interface Excludes{
        Connection getConnection()        throws SQLException;
        Connection getConnection(String username, String password)        throws SQLException;
    }

    @Setter
    @Delegate(excludes = Excludes.class)
    private DataSource delegate;


}
