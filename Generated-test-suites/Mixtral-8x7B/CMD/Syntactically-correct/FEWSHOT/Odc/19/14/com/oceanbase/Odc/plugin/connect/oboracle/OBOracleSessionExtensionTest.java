package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@org.powermock.modules.junit4.PowerMockRunner
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn("currentSchema");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("currentSchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getClass().getName()).thenReturn("com.oceanbase.jdbc.OceanBaseConnection");
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
                .thenThrow(new RuntimeException());
        PowerMockito.whenNew(OceanBaseConnection.class).withAnyArguments().thenReturn(mock(OceanBaseConnection.class));
        when(mock(OceanBaseConnection.class).getServerThreadId()).thenReturn(789012);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("789012", connectionId);
    }

    @Test
    public void testGetConnectionIdNoException() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getClass().getName()).thenReturn("com.oceanbase.jdbc.OceanBaseConnection");
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
                .thenReturn("123456");
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("123456", connectionId);
    }

    @Test
    public void testGetConnectionIdNotOceanBaseConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getClass().getName()).thenReturn("java.sql.Connection");
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNull(connectionId);
    }
}