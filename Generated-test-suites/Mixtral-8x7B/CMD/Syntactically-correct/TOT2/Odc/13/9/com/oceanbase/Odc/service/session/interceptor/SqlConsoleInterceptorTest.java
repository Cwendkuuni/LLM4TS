import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
class TestAuthenticationFacade {

    OrganizationType organizationType = OrganizationType.INDIVIDUAL;

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }
}

@Component
class TestRuleService {

    public Optional<Rule> getByRulesetIdAndName(Long ruleSetId, String ruleName) {
        return Optional.empty();
    }
}

@Component
class TestSqlConsoleRuleService {

    public Optional<Integer> getProperties(Long ruleSetId, String propertyName, DialectType dialectType,
            Class<Integer> clazz) {
        return Optional.empty();
    }

    public Optional<List<String>> getListProperties(Long ruleSetId, String propertyName, DialectType dialectType,
            Class<String> clazz) {
        return Optional.empty();
    }

    public boolean isForbidden(String propertyName, ConnectionSession session) {
        return false;
    }

    public List<Rule> getByRulesetIdAndName(Long ruleSetId, String ruleName) {
        return new ArrayList<>();
    }
}

@Configuration
class TestContextConfig {

    @Bean
    @Primary
    public AuthenticationFacade authenticationFacade() {
        return new TestAuthenticationFacade();
    }

    @Bean
    @Primary
    public RuleService ruleService() {
        return new TestRuleService();
    }

    @Bean
    @Primary
    public SqlConsoleRuleService sqlConsoleRuleService() {
        return new TestSqlConsoleRuleService();
    }
}

@Component
class TestConnectionSessionUtil {

    public static Long getRuleSetId(ConnectionSession connectionSession) {
        return 1L;
    }

    public static boolean isIndividualTeam() {
        return true;
    }
}

public class SqlConsoleInterceptorTest {

    private static final String SQL_1 = "SELECT * FROM users";
    private static final String SQL_2 = "CREATE PROCEDURE get_users() BEGIN SELECT * FROM users; END";
    private static final String SQL_3 = "INSERT INTO users (name, email) VALUES ('test', 'test@example.com')";

    @Autowired
    private ApplicationContext applicationContext;

    private BaseTimeConsumingInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        applicationContext.getBeansOfType(BaseTimeConsumingInterceptor.class).values().forEach(interceptor -> {
            if (interceptor instanceof SqlConsoleInterceptor) {
                sqlConsoleInterceptor = interceptor;
                sqlConsoleInterceptor.setAuthenticationFacade(mockAuthenticationFacade());
                sqlConsoleInterceptor.setRuleService(mockRuleService());
                sqlConsoleInterceptor.setSqlConsoleRuleService(mockSqlConsoleRuleService());
                sqlConsoleInterceptor.setConnectionSessionUtil(mockConnectionSessionUtil());
            }
        });
    }

    private AuthenticationFacade mockAuthenticationFacade() {
        TestAuthenticationFacade authenticationFacade = mock(TestAuthenticationFacade.class);
        when(authenticationFacade.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        return authenticationFacade;
    }

    private RuleService mockRuleService() {
        TestRuleService ruleService = mock(TestRuleService.class);
        doNothing().when(ruleService).getByRulesetIdAndName(anyLong(), anyString());
        return ruleService;
    }

    private SqlConsoleRuleService mockSqlConsoleRuleService() {
        TestSqlConsoleRuleService sqlConsoleRuleService = mock(TestSqlConsoleRuleService.class);
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), any()))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), any(DialectType.class), any()))
                .thenReturn(Optional.of(List.of("SELECT", "INSERT")));
        when(sqlConsoleRuleService.isForbidden(anyString(), any()))
                .thenReturn(false);
        return sqlConsoleRuleService;
    }

    private ConnectionSessionUtil mockConnectionSessionUtil() {
        return new TestConnectionSessionUtil();
    }

    @Test
    public void testDoPreHandle_noRuleSetId() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple(1L, SQL_1))));
        Map<String, Object> context = new HashMap<>();

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, new SqlAsyncExecuteResp(), new ConnectionSession(),
                context));
        assertFalse(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_individualTeam() {
        TestAuthenticationFacade authenticationFacade = mock(TestAuthenticationFacade.class);
        when(authenticationFacade.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple(1L, SQL_1))));
        Map<String, Object> context = new HashMap<>();

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, new SqlAsyncExecuteResp(), new ConnectionSession(),
                context));
        assertFalse(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_allowAllSqlTypes() {
        TestSqlConsoleRuleService sqlConsoleRuleService = mock(TestSqlConsoleRuleService.class);
        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), any(DialectType.class), any()))
                .thenReturn(Optional.of(List.of("SELECT", "INSERT", "CREATE")));
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple(1L, SQL_1)),
                new SqlTuplesWithViolation(new SqlTuple(2L, SQL_2)),
                new SqlTuplesWithViolation(new SqlTuple(3L, SQL_3))));
        Map<String, Object> context = new HashMap<>();

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, new SqlAsyncExecuteResp(), new ConnectionSession(),
                context));
        assertFalse(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_disallowCreateProcedure() {
        TestSqlConsoleRuleService sqlConsoleRuleService = mock(TestSqlConsoleRuleService.class);
        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), any(DialectType.class), any()))
                .thenReturn(Optional.of(List.of("SELECT", "INSERT")));
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()), any()))
                .thenReturn(true);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple(1L, SQL_1)),
                new SqlTuplesWithViolation(new SqlTuple(2L, SQL_2)),
                new SqlTuplesWithViolation(new SqlTuple(3L, SQL_3))));
        Map<String, Object> context = new HashMap<>();

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, new SqlAsyncExecuteResp(), new ConnectionSession(),
                context));
        assertTrue(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_exceedMaxReturnRows() {
        TestSqlConsoleRuleService sqlConsoleRuleService = mock(TestSqlConsoleRuleService.class);
        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), any(DialectType.class), any()))
                .thenReturn(Optional.of(List.of("SELECT", "INSERT")));
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()), any()))
                .thenReturn(false);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple(1L, SQL_1)),
                new SqlTuplesWithViolation(new SqlTuple(2L, SQL_2)),
                new SqlTuplesWithViolation(new SqlTuple(3L, SQL_3))));
        request.setQueryLimit(9);
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, new SqlAsyncExecuteResp(), new ConnectionSession(),
                context));
        assertTrue(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoAfterCompletion_forbiddenEditResultSet() {
        TestSqlConsoleRuleService sqlConsoleRuleService = mock(TestSqlConsoleRuleService.class);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET.getRuleName()), any()))
                .thenReturn(true);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlExecuteResult.setResultSetMetaData(new SqlExecuteResult.ResultSetMetaData(true, true));

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, new ConnectionSession(), new HashMap<>());

        assertFalse(sqlExecuteResult.getResultSetMetaData().isEditable());
    }

    @Test
    public void testDoAfterCompletion_forbiddenExportResultSet() {
        TestSqlConsoleRuleService sqlConsoleRuleService = mock(TestSqlConsoleRuleService.class);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET.getRuleName()), any()))
                .thenReturn(true);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, new ConnectionSession(), new HashMap<>());

        assertFalse(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }
}