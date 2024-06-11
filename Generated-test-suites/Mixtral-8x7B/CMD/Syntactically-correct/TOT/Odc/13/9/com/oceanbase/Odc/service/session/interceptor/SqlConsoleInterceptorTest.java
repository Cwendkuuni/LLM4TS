import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.result.BasicResult;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuple;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SqlConsoleInterceptorTest {

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private BaseTimeConsumingInterceptor sqlConsoleInterceptor;

    @BeforeEach
    public void setUp() {
        Mockito.reset(authenticationFacade);
    }

    @Test
    public void testDoPreHandle_noRuleSetId_returnsTrue() {
        ConnectionSession session = new ConnectionSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        HashMap<String, Object> context = new HashMap<>();

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        assertFalse(context.containsKey(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK));
        assertFalse(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_individualTeam_returnsTrue() {
        ConnectionSession session = new ConnectionSession();
        session.setOrganizationType(OrganizationType.INDIVIDUAL);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        HashMap<String, Object> context = new HashMap<>();

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        assertFalse(context.containsKey(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK));
        assertFalse(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_needSqlConsoleCheckFalse_returnsTrue() {
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setDialectType(DialectType.MYSQL);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        HashMap<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        assertFalse(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_maxExecuteSqlsViolation_returnsFalse() {
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setDialectType(DialectType.MYSQL);
        session.setOrganizationType(OrganizationType.COMPANY);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(createSqlTuples(3));

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(request.getSqls());

        HashMap<String, Object> context = new HashMap<>();
        Mockito.when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.COMPANY));
        Mockito.when(ruleService.getByRulesetIdAndName(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(createRule(SqlConsoleRules.MAX_EXECUTE_SQLS));
        Mockito.when(sqlConsoleRuleService.getProperties(Mockito.anyLong(), Mockito.anyString(),
                Mockito.any(DialectType.class), Mockito.any()))
                .thenReturn(Optional.of(5));

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        assertTrue(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_notAllowedCreatePl_returnsFalse() {
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setDialectType(DialectType.MYSQL);
        session.setOrganizationType(OrganizationType.COMPANY);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(createSqlTuples(1, "CREATE PROCEDURE test_proc ..."));

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(request.getSqls());

        HashMap<String, Object> context = new HashMap<>();
        Mockito.when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.COMPANY));
        Mockito.when(ruleService.getByRulesetIdAndName(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(createRule(SqlConsoleRules.NOT_ALLOWED_CREATE_PL));
        Mockito.when(sqlConsoleRuleService.isForbidden(Mockito.anyString(), Mockito.any(ConnectionSession.class)))
                .thenReturn(true);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        assertTrue(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_allowedSqlTypesViolation_returnsFalse() {
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setDialectType(DialectType.MYSQL);
        session.setOrganizationType(OrganizationType.COMPANY);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(createSqlTuples(1, "SELECT * FROM dual", SqlType.DDL));

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(request.getSqls());

        HashMap<String, Object> context = new HashMap<>();
        Mockito.when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.COMPANY));
        Mockito.when(ruleService.getByRulesetIdAndName(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(createRule(SqlConsoleRules.ALLOW_SQL_TYPES));
        Mockito.when(sqlConsoleRuleService.getListProperties(Mockito.anyLong(), Mockito.anyString(),
                Mockito.any(DialectType.class), Mockito.any()))
                .thenReturn(Optional.of(List.of("SELECT", "INSERT", "UPDATE", "DELETE")));

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        assertTrue(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_success() {
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setDialectType(DialectType.MYSQL);
        session.setOrganizationType(OrganizationType.COMPANY);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(createSqlTuples(1, "SELECT * FROM dual", SqlType.SELECT));

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(request.getSqls());

        HashMap<String, Object> context = new HashMap<>();
        Mockito.when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.COMPANY));
        Mockito.when(ruleService.getByRulesetIdAndName(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(createRule(SqlConsoleRules.ALLOW_SQL_TYPES));
        Mockito.when(sqlConsoleRuleService.getListProperties(Mockito.anyLong(), Mockito.anyString(),
                Mockito.any(DialectType.class), Mockito.any()))
                .thenReturn(Optional.of(List.of("SELECT", "INSERT", "UPDATE", "DELETE")));

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        assertFalse(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoAfterCompletion_noRuleSetId_returnsTrue() {
        ConnectionSession session = new ConnectionSession();
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setResultSetMetaData(new BasicResult());

        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        assertEquals(SqlExecuteStatus.SUCCESS, response.getStatus());
        assertTrue(response.getResultSetMetaData().isEditable());
        assertTrue(response.getAllowExport());
    }

    @Test
    public void testDoAfterCompletion_notAllowedEditResultset_returnsFalse() {
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setOrganizationType(OrganizationType.COMPANY);

        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setResultSetMetaData(new BasicResult());

        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        assertEquals(SqlExecuteStatus.SUCCESS, response.getStatus());
        assertFalse(response.getResultSetMetaData().isEditable());
        assertTrue(response.getAllowExport());
    }

    @Test
    public void testDoAfterCompletion_notAllowedExportResultset_returnsFalse() {
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setDialectType(DialectType.MYSQL);
        session.setOrganizationType(OrganizationType.COMPANY);

        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setResultSetMetaData(new BasicResult());

        Mockito.when(sqlConsoleRuleService.isForbidden(Mockito.anyString(), Mockito.any(ConnectionSession.class)))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        assertEquals(SqlExecuteStatus.SUCCESS, response.getStatus());
        assertTrue(response.getResultSetMetaData().isEditable());
        assertFalse(response.getAllowExport());
    }

    @Test
    public void testDoAfterCompletion_success() {
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setDialectType(DialectType.MYSQL);
        session.setOrganizationType(OrganizationType.COMPANY);

        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setResultSetMetaData(new BasicResult());

        Mockito.when(sqlConsoleRuleService.isForbidden(Mockito.anyString(), Mockito.any(ConnectionSession.class)))
                .thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        assertEquals(SqlExecuteStatus.SUCCESS, response.getStatus());
        assertTrue(response.getResultSetMetaData().isEditable());
        assertTrue(response.getAllowExport());
    }

    private List<SqlTuple> createSqlTuples(int count, String... sqlsAndTypes) {
        List<SqlTuple> sqlTuples = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            SqlTuple sqlTuple = new SqlTuple();
            sqlTuple.setOriginalSql(sqlsAndTypes[i * 2]);
            sqlTuple.setSqlType(sqlsAndTypes[i * 2 + 1] == "DDL" ? SqlType.DDL : SqlType.SELECT);
            sqlTuple.setAst(null);
            sqlTuples.add(sqlTuple);
        }
        return sqlTuples;
    }

    private Rule createRule(SqlConsoleRules sqlConsoleRules) {
        Rule rule = new Rule();
        rule.setRuleName(sqlConsoleRules.getRuleName());
        rule.setLevel(sqlConsoleRules.getLevel());
        rule.setProperties(new HashMap<>());
        rule.getProperties().put(sqlConsoleRules.getMetadata().getPropertyMetadatas().get(0).getName(), "20");
        return rule;
    }

    private OrganizationType.User createUser(OrganizationType organizationType) {
        OrganizationType.User user = Mockito.mock(OrganizationType.User.class);
        Mockito.when(user.getOrganizationType()).thenReturn(organizationType);
        return user;
    }
}