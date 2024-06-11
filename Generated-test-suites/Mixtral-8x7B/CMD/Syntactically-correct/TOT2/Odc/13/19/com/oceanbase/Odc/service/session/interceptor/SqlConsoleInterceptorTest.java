import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.result.BasicResult;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.profiles.active=test"})
public class SqlConsoleInterceptorTest {

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Autowired
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        reset(authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_typicalUseCase() {
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        req.setSql("SELECT * FROM users;");
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        resp.setSqls(Arrays.asList(new SqlTuplesWithViolation("test_sql_id", "SELECT * FROM users;")));

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new User("test_user", OrganizationType.TEAM));
        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any())).thenReturn(Optional.empty());
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);

        boolean result = sqlConsoleInterceptor.doPreHandle(req, resp, session, context);

        Assertions.assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ruleSetIdIsNull() {
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        req.setSql("SELECT * FROM users;");
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        resp.setSqls(Arrays.asList(new SqlTuplesWithViolation("test_sql_id", "SELECT * FROM users;")));

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new User("test_user", OrganizationType.TEAM));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        boolean result = sqlConsoleInterceptor.doPreHandle(req, resp, session, context);

        Assertions.assertTrue(result);
    }

    @Test
    public void testDoPreHandle_individualTeam() {
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        req.setSql("SELECT * FROM users;");
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        resp.setSqls(Arrays.asList(new SqlTuplesWithViolation("test_sql_id", "SELECT * FROM users;")));

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new User("test_user", OrganizationType.INDIVIDUAL));

        boolean result = sqlConsoleInterceptor.doPreHandle(req, resp, session, context);

        Assertions.assertTrue(result);
    }

    @Test
    public void testDoPreHandle_needSqlConsoleCheckIsFalse() {
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        req.setSql("SELECT * FROM users;");
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        resp.setSqls(Arrays.asList(new SqlTuplesWithViolation("test_sql_id", "SELECT * FROM users;")));

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new User("test_user", OrganizationType.TEAM));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        boolean result = sqlConsoleInterceptor.doPreHandle(req, resp, session, context);

        Assertions.assertTrue(result);
    }

    @Test
    public void testDoPreHandle_maxExecuteSqlsForbidden() {
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        req.setSql("SELECT * FROM users; SELECT * FROM users2;");
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        resp.setSqls(Arrays.asList(
                new SqlTuplesWithViolation("test_sql_id1", "SELECT * FROM users;"),
                new SqlTuplesWithViolation("test_sql_id2", "SELECT * FROM users2;")
        ));

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new User("test_user", OrganizationType.TEAM));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), any()))
                .thenReturn(Optional.of(2));
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);

        boolean result = sqlConsoleInterceptor.doPreHandle(req, resp, session, context);

        Assertions.assertFalse(result);
        verify(sqlConsoleRuleService, times(1)).getProperties(eq(1L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), any());
        verify(ruleService, times(1)).getByRulesetIdAndName(eq(1L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()));
    }

    @Test
    public void testDoPreHandle_notAllowedCreatePlForbidden() {
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        req.setSql("CREATE PROCEDURE test_proc AS SELECT * FROM users;");
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        resp.setSqls(Arrays.asList(new SqlTuplesWithViolation("test_sql_id", req.getSql())));

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new User("test_user", OrganizationType.TEAM));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), any()))
                .thenReturn(Optional.empty());
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL), any()))
                .thenReturn(true);
        when(ruleService.getByRulesetIdAndName(eq(1L), eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName())))
                .thenReturn(Optional.of(new Rule()));

        boolean result = sqlConsoleInterceptor.doPreHandle(req, resp, session, context);

        Assertions.assertFalse(result);
    }

    @Test
    public void testDoPreHandle_allowSqlTypesForbidden() {
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        req.setSql("SELECT * FROM users;");
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        resp.setSqls(Arrays.asList(new SqlTuplesWithViolation("test_sql_id", req.getSql())));

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new User("test_user", OrganizationType.TEAM));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), any()))
                .thenReturn(Optional.empty());
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL), any()))
                .thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(eq(1L), eq(SqlConsoleRules.ALLOW_SQL_TYPES), any(), any()))
                .thenReturn(Optional.of(Arrays.asList("CREATE")));

        boolean result = sqlConsoleInterceptor.doPreHandle(req, resp, session, context);

        Assertions.assertFalse(result);
    }

    @Test
    public void testDoPreHandle_syntaxError() {
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        req.setSql("SELECT * FROM uusers;");
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        resp.setSqls(Arrays.asList(new SqlTuplesWithViolation("test_sql_id", req.getSql())));

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new User("test_user", OrganizationType.TEAM));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), any()))
                .thenReturn(Optional.empty());
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL), any()))
                .thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(eq(1L), eq(SqlConsoleRules.ALLOW_SQL_TYPES), any(), any()))
                .thenReturn(Optional.of(Arrays.asList("SELECT")));

        boolean result = sqlConsoleInterceptor.doPreHandle(req, resp, session, context);

        Assertions.assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion_notAllowedEditResultsetForbidden() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setResultSetMetaData(new ResultSetMetaData(true));

        ConnectionSession session = mock(ConnectionSession.class);
        when(authenticationFacade.currentUser()).thenReturn(new User("test_user", OrganizationType.TEAM));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET), any()))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(result, session, new HashMap<>());

        Assertions.assertFalse(result.getResultSetMetaData().isEditable());
    }

    @Test
    public void testDoAfterCompletion_notAllowedExportForbidden() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);

        ConnectionSession session = mock(ConnectionSession.class);
        when(authenticationFacade.currentUser()).thenReturn(new User("test_user", OrganizationType.TEAM));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET), any()))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(result, session, new HashMap<>());

        Assertions.assertFalse(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_successful() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);

        ConnectionSession session = mock(ConnectionSession.class);
        when(authenticationFacade.currentUser()).thenReturn(new User("test_user", OrganizationType.TEAM));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET), any()))
                .thenReturn(false);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET), any()))
                .thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(result, session, new HashMap<>());

        Assertions.assertTrue(result.getResultSetMetaData().isEditable());
        Assertions.assertTrue(result.isAllowExport());
    }

    private class User {
        private final String name;
        private final OrganizationType organizationType;

        private User(String name, OrganizationType organizationType) {
            this.name = name;
            this.organizationType = organizationType;
        }

        public String getName() {
            return name;
        }

        public OrganizationType getOrganizationType() {
            return organizationType;
        }
    }

    private class ResultSetMetaData {
        private boolean editable;

        private ResultSetMetaData(boolean editable) {
            this.editable = editable;
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }
    }
}