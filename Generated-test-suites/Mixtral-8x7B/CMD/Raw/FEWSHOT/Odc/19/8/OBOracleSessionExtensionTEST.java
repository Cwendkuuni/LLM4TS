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

    private static final String CONNECTION_ID = "123456";
    private static final String SCHEMA_NAME = "test_schema";
    private static final String CURRENT_SCHEMA = "current_schema";
    private static final String QUOTED_SCHEMA = "\"test_schema\"";
    private static final String ALTER_SQL = "ALTER SESSION SET CURRENT_SCHEMA=\"" + SCHEMA_NAME + "\"";
    private static final String QUERY_SQL = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";

    private Connection mockConnection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        mockConnection = mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123";
        obOracleSessionExtension.killQuery(mockConnection, connectionId);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(mockConnection);

        PowerMockito.verifyStatic();
        JdbcOperationsUtil.getJdbcOperations(mockConnection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).update(ALTER_SQL)).thenReturn(1);

        obOracleSessionExtension.switchSchema(mockConnection, SCHEMA_NAME);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        String currentSchema = JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(QUERY_SQL,
                String.class);
        assertEquals(CURRENT_SCHEMA, currentSchema);

        PowerMockito.verifyStatic();
        JdbcOperationsUtil.getJdbcOperations(mockConnection).update(ALTER_SQL);
    }

    @Test
    public void testSwitchSchemaNoChange() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(QUERY_SQL, String.class))
                .thenReturn(SCHEMA_NAME);

        obOracleSessionExtension.switchSchema(mockConnection, SCHEMA_NAME);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class, PowerMockito.times(0));
        JdbcOperationsUtil.getJdbcOperations(mockConnection).update(ALTER_SQL);
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(QUERY_SQL, String.class))
                .thenReturn(CURRENT_SCHEMA);

        String result = obOracleSessionExtension.getCurrentSchema(mockConnection);

        assertEquals(CURRENT_SCHEMA, result);

        PowerMockito.verifyStatic();
        JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(QUERY_SQL, String.class);
    }

    @Test
    public void testGetConnectionId() {
        when(mockConnection instanceof OceanBaseConnection)
                .thenReturn(true);
        when(((OceanBaseConnection) mockConnection).getServerThreadId())
                .thenReturn(Long.parseLong(CONNECTION_ID));

        String result = obOracleSessionExtension.getConnectionId(mockConnection);

        assertEquals(CONNECTION_ID, result);

        PowerMockito.verifyStatic();
        String querySql = "select userenv('sessionid') from dual";
        JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(querySql, String.class);

        PowerMockito.verifyStatic(StringUtils.class);
        StringUtils.quoteOracleIdentifier(SCHEMA_NAME);

        PowerMockito
                .verifyStatic(JdbcOperationsUtil.class, PowerMockito.times(0))
                .getJdbcOperations(mockConnection);
    }

    @Test
    public void testGetConnectionIdFallback() {
        when(mockConnection instanceof OceanBaseConnection)
                .thenReturn(false);

        String result = obOracleSessionExtension.getConnectionId(mockConnection);

        assertNull(result);

        PowerMockito.verifyStatic();
        String querySql = "select userenv('sessionid') from dual";
        JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(querySql, String.class);

        PowerMockito.verifyStatic(StringUtils.class, PowerMockito.times(0));
        StringUtils.quoteOracleIdentifier(SCHEMA_NAME);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class, PowerMockito.times(0))
                .getJdbcOperations(mockConnection);
    }
}