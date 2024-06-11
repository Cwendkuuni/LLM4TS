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

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private SessionExtensionPoint obOracleSessionExtension;
    private Connection connection;
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
        jdbcOperationsUtil = mock(JdbcOperationsUtil.class);

        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);

        String expectedQuery = "KILL QUERY " + connectionId;
        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection).execute(expectedQuery);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "different_schema";

        when(jdbcOperationsUtil.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL",
                String.class)).thenReturn(currentSchema);

        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection).update(alterSql);
    }

    @Test
    public void testSwitchSchema_whenSameSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = schemaName;

        when(jdbcOperationsUtil.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL",
                String.class)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class, PowerMockito.never());
        JdbcOperationsUtil.getJdbcOperations(connection).update(PowerMockito.anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        when(jdbcOperationsUtil.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL",
                String.class)).thenReturn("current_schema");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals("current_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(jdbcOperationsUtil.queryForObject("select userenv('sessionid') from dual", String.class))
                .thenReturn("123456");

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertEquals("123456", connectionId);
    }

    @Test
    public void testGetConnectionId_whenConnectionIsOceanBaseConnection() throws SQLException {
        long threadId = 789012;
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(threadId);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(String.valueOf(threadId), connectionId);
    }

    @Test
    public void testGetConnectionId_whenQueryForObjectFails() {
        when(connection instanceof OceanBaseConnection).thenReturn(false);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertNull(connectionId);
    }
}