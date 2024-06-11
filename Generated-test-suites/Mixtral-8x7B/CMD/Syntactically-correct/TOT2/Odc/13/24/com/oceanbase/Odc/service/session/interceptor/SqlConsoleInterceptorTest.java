import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.core.sql.parser.result.BasicResult;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.model.SqlTuple;
import com.oceanbase.odc.service.shared.constant.DialectType;
import com.oceanbase.odc.service.shared.constant.OrganizationType;
import org.junit.jupiter.api.*;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
class SqlConsoleInterceptorTestDummy {

    @Bean
    public AuthenticationFacade authenticationFacade() {
        AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
        when(authenticationFacade.currentUser()).thenReturn(new User(OrganizationType.INDIVIDUAL));
        return authenticationFacade;
    }

    @Bean
    public RuleService ruleService() {
        RuleService ruleService = mock(RuleService.class);
        when(ruleService.getByRulesetIdAndName(any(), any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
        return ruleService;
    }

    @Bean
    public SqlConsoleRuleService sqlConsoleRuleService() {
        SqlConsoleRuleService sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        when(sqlConsoleRuleService.getProperties(any(), any(), any(), any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(sqlConsoleRuleService.getListProperties(any(), any(), any(), any()))
                .thenAnswer(invocation -> Optional.of(Arrays.asList(invocation.getArgument(2).toString())));
        when(sqlConsoleRuleService.isForbidden(any(), any()))
                .thenAnswer(invocation -> false);
        return sqlConsoleRuleService;
    }

    @Bean
    public BaseTimeConsumingInterceptor baseTimeConsumingInterceptor() {
        return new BaseTimeConsumingInterceptor();
    }

}

@Configuration
class TestContextConfiguration {

    @Autowired
    private SqlConsoleInterceptorTestDummy sqlConsoleInterceptorTestDummy;

    @Bean
    public SqlConsoleInterceptor sqlConsoleInterceptor() {
        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(sqlConsoleInterceptorTestDummy.authenticationFacade());
        sqlConsoleInterceptor.setRuleService(sqlConsoleInterceptorTestDummy.ruleService());
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleInterceptorTestDummy.sqlConsoleRuleService());
        sqlConsoleInterceptor.setBaseTimeConsumingInterceptor(sqlConsoleInterceptorTestDummy.baseTimeConsumingInterceptor());
        return sqlConsoleInterceptor;
    }
}

@Component
class User {

    private final OrganizationType organizationType;

    public User(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }

    public OrganizationType getOrganizationType() {
        return organizationType;
    }
}

class SqlConsoleInterceptorTest {

    @Autowired
    private ApplicationContext applicationContext;

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @BeforeEach
    public void setUp() {
        sqlConsoleInterceptor = applicationContext.getBean(SqlConsoleInterceptor.class);
    }

    private void testDoPreHandle(String sql, List<SqlTuple> sqlTuples, boolean allowExecute) {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(Arrays.asList(new SqlTuplesWithViolation(sql, sqlTuples)));
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        when(session.getRuleSetId()).thenReturn(1L);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertEquals(allowExecute, result);
    }

    @Test
    public void testDoPreHandle_NoRuleSet() {
        testDoPreHandle("SELECT 1", Arrays.asList(), true);
    }

    @Test
    public void testDoPreHandle_NoCheck() {
        testDoPreHandle("SELECT 1", Arrays.asList(), true);
    }

    @Test
    public void testDoPreHandle_AlwaysAllow() {
        testDoPreHandle("CREATE TABLE t (i INT)",
                Arrays.asList(createSqlTuple("CREATE TABLE t (i INT)", SqlType.DDL)), true);
    }

    @Test
    public void testDoPreHandle_ForbiddenCreatePl() {
        sqlConsoleInterceptor.setForbiddenCreatePl(true);
        testDoPreHandle("CREATE PROCEDURE p() BEGIN SELECT 1 END",
                Arrays.asList(createSqlTuple("CREATE PROCEDURE p() BEGIN SELECT 1 END", SqlType.DDL)), false);
    }

    @Test
    public void testDoPreHandle_AllowSqlTypes() {
        when(sqlConsoleRuleService.getListProperties(any(), any(), any(), any()))
                .thenAnswer(invocation -> Optional.of(Arrays.asList(invocation.getArgument(2).toString())));
        sqlConsoleInterceptor.setForbiddenCreatePl(false);
        testDoPreHandle("SELECT 1", Arrays.asList(createSqlTuple("SELECT 1", SqlType.SELECT)), true);
        testDoPreHandle("INSERT INTO t VALUES (1)",
                Arrays.asList(createSqlTuple("INSERT INTO t VALUES (1)", SqlType.INSERT)), true);
        testDoPreHandle("SELECT 1; DROP TABLE t",
                Arrays.asList(createSqlTuple("SELECT 1", SqlType.SELECT),
                        createSqlTuple("DROP TABLE t", SqlType.DROP)), false);
    }

    @Test
    public void testDoPreHandle_MaxExecuteSqls() {
        sqlConsoleInterceptor.setMaxExecuteSqls(1);
        testDoPreHandle("SELECT 1; SELECT 2",
                Arrays.asList(createSqlTuple("SELECT 1", SqlType.SELECT),
                        createSqlTuple("SELECT 2", SqlType.SELECT)), false);
    }

    @Test
    public void testDoPreHandle_MaxReturnRows() {
        sqlConsoleInterceptor.setMaxReturnRows(2);
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setResultSetMetaData(createResultSetMetaData(3));
        result.setResultSetRows(Arrays.asList(createTuple("1"), createTuple("2"), createTuple("3")));
        sqlConsoleInterceptor.doAfterCompletion(result, mock(ConnectionSession.class), new HashMap<>());
        assertFalse(result.isAllowExport());
    }

    private AbstractSyntaxTree.ParseResult createSqlTuple(String sql, SqlType sqlType) {
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(ast.getParseResult()).thenReturn(new AbstractSyntaxTree.ParseResult(sqlType));
        return new SqlTuple(1, sql, ast);
    }

    private Map<String, Integer> createResultSetMetaData(int columnCount) {
        Map<String, Integer> resultSetMetaData = new HashMap<>();
        for (int i = 0; i < columnCount; i++) {
            resultSetMetaData.put("column" + i, i);
        }
        return resultSetMetaData;
    }

    private SqlTuple createTuple(Object value) {
        return new SqlTuple(1, "query", mock(AbstractSyntaxTree.class), null,
                mock(com.oceanbase.odc.core.sql.parser.result.ResultSet.class), null,
                new Object[]{value});
    }

    private int getOrder() {
        return sqlConsoleInterceptor.getOrder();
    }

}