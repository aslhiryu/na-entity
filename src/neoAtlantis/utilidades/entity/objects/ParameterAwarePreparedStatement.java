package neoAtlantis.utilidades.entity.objects;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class ParameterAwarePreparedStatement implements PreparedStatement {
    private final PreparedStatement delegate;
    private final Map<Integer,Object> parameters;

    public ParameterAwarePreparedStatement(PreparedStatement delegate) {
        this.delegate = delegate;
        this.parameters = new HashMap<>();
    }

    public Map<Integer,Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return delegate.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        return this.delegate.executeUpdate();
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        this.delegate.setString(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setNull(int parameterIndex, int x) throws SQLException {
        this.delegate.setNull(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        this.delegate.setBoolean(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        this.delegate.setByte(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        this.delegate.setShort(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        this.delegate.setInt(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        this.delegate.setLong(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        this.delegate.setFloat(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        this.delegate.setDouble(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        this.delegate.setBigDecimal(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        this.delegate.setBytes(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        this.delegate.setDate(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        this.delegate.setTime(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        this.delegate.setTimestamp(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int i) throws SQLException {
        this.delegate.setAsciiStream(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int i) throws SQLException {
        this.delegate.setUnicodeStream(parameterIndex, x, i);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int i) throws SQLException {
        this.delegate.setBinaryStream(parameterIndex, x, i);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void clearParameters() throws SQLException {
        this.parameters.clear();
        this.delegate.clearParameters();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int i) throws SQLException {
        this.delegate.setObject(parameterIndex, x, i);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        this.delegate.setObject(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, int i) throws SQLException {
        this.delegate.setCharacterStream(parameterIndex, x, i);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        this.delegate.setRef(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        this.delegate.setBlob(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        this.delegate.setClob(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        this.delegate.setArray(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar c) throws SQLException {
        this.delegate.setDate(parameterIndex, x, c);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar c) throws SQLException {
        this.delegate.setTime(parameterIndex, x, c);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar c) throws SQLException {
        this.delegate.setTimestamp(parameterIndex, x, c);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setNull(int parameterIndex, int x, String c) throws SQLException {
        this.delegate.setNull(parameterIndex, x, c);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        this.delegate.setURL(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        this.delegate.setRowId(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String x) throws SQLException {
        this.delegate.setNString(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x, long l) throws SQLException {
        this.delegate.setNCharacterStream(parameterIndex, x, l);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setNClob(int parameterIndex, NClob x) throws SQLException {
        this.delegate.setNClob(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Reader x, long l) throws SQLException {
        this.delegate.setClob(parameterIndex, x, l);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x, long l) throws SQLException {
        this.delegate.setBlob(parameterIndex, x, l);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setNClob(int parameterIndex, Reader x, long l) throws SQLException {
        this.delegate.setNClob(parameterIndex, x, l);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML x) throws SQLException {
        this.delegate.setSQLXML(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int i1, int i2) throws SQLException {
        this.delegate.setObject(parameterIndex, x, i1, i2);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long l) throws SQLException {
        this.delegate.setAsciiStream(parameterIndex, x, l);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long l) throws SQLException {
        this.delegate.setBinaryStream(parameterIndex, x, l);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, long l) throws SQLException {
        this.delegate.setCharacterStream(parameterIndex, x, l);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        this.delegate.setAsciiStream(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        this.delegate.setBinaryStream(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x) throws SQLException {
        this.delegate.setCharacterStream(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x) throws SQLException {
        this.delegate.setNCharacterStream(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Reader x) throws SQLException {
        this.delegate.setClob(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x) throws SQLException {
        this.delegate.setBlob(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public void setNClob(int parameterIndex, Reader x) throws SQLException {
        this.delegate.setNClob(parameterIndex, x);
        this.parameters.put(parameterIndex, x);
    }

    @Override
    public boolean execute() throws SQLException {
        return this.delegate.execute();
    }

    @Override
    public void addBatch() throws SQLException {
        this.delegate.addBatch();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.delegate.getMetaData();
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return this.delegate.getParameterMetaData();
    }

    @Override
    public ResultSet executeQuery(String string) throws SQLException {
        return this.delegate.executeQuery(string);
    }

    @Override
    public int executeUpdate(String string) throws SQLException {
        return this.delegate.executeUpdate(string);
    }

    @Override
    public void close() throws SQLException {
        this.delegate.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return this.delegate.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int i) throws SQLException {
        this.delegate.setMaxFieldSize(i);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return this.delegate.getMaxRows();
    }

    @Override
    public void setMaxRows(int i) throws SQLException {
        this.delegate.setMaxRows(i);
    }

    @Override
    public void setEscapeProcessing(boolean bln) throws SQLException {
        this.delegate.setEscapeProcessing(bln);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return this.delegate.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int i) throws SQLException {
        this.delegate.setQueryTimeout(i);
    }

    @Override
    public void cancel() throws SQLException {
        this.delegate.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.delegate.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.delegate.clearWarnings();
    }

    @Override
    public void setCursorName(String string) throws SQLException {
        this.delegate.setCursorName(string);
    }

    @Override
    public boolean execute(String string) throws SQLException {
        return this.delegate.execute(string);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return this.delegate.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return this.delegate.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return this.delegate.getMoreResults();
    }

    @Override
    public void setFetchDirection(int i) throws SQLException {
        this.delegate.setFetchDirection(i);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return this.delegate.getFetchDirection();
    }

    @Override
    public void setFetchSize(int i) throws SQLException {
        this.delegate.setFetchSize(i);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return this.delegate.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return this.delegate.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return this.delegate.getResultSetType();
    }

    @Override
    public void addBatch(String string) throws SQLException {
        this.delegate.addBatch(string);
    }

    @Override
    public void clearBatch() throws SQLException {
        this.delegate.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return this.delegate.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.delegate.getConnection();
    }

    @Override
    public boolean getMoreResults(int i) throws SQLException {
        return this.delegate.getMoreResults(i);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return this.delegate.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String string, int i) throws SQLException {
        return this.delegate.executeUpdate(string, i);
    }

    @Override
    public int executeUpdate(String string, int[] ints) throws SQLException {
        return this.delegate.executeUpdate(string, ints);
    }

    @Override
    public int executeUpdate(String string, String[] strings) throws SQLException {
        return this.delegate.executeUpdate(string, strings);
    }

    @Override
    public boolean execute(String string, int i) throws SQLException {
        return this.delegate.execute(string, i);
    }

    @Override
    public boolean execute(String string, int[] ints) throws SQLException {
        return this.delegate.execute(string, ints);
    }

    @Override
    public boolean execute(String string, String[] strings) throws SQLException {
        return this.delegate.execute(string, strings);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return this.delegate.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.delegate.isClosed();
    }

    @Override
    public void setPoolable(boolean bln) throws SQLException {
        this.delegate.setPoolable(bln);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return this.delegate.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        this.delegate.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return this.delegate.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> type) throws SQLException {
        return this.delegate.unwrap(type);
    }

    @Override
    public boolean isWrapperFor(Class<?> type) throws SQLException {
        return this.delegate.isWrapperFor(type);
    }
}