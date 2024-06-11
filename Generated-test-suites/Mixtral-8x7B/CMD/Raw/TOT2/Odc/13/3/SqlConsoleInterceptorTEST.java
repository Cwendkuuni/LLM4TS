import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.authenticationFacade = authenticationFacade;
        sqlConsoleInterceptor.ruleService = ruleService;
        sqlConsoleInterceptor.sqlConsoleRuleService = sqlConsoleRuleService;
    }

    @Test
    public void testDoPreHandle_happyPath() {
        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(getUser());
        when(ruleService.getByRulesetIdAndName(1L, "ALLOW_SQL_TYPES")).thenReturn(getRule());

        boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context);

        assertEquals(true, result);
    }

    @Test
    public void testDoPreHandle_forbiddenToCreatePl() {
        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(getUser());
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(true);

        boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context);

        assertEquals(false, result);
    }

    @Test
    public void testDoPreHandle_forbiddenSqlTypes() {
        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(getUser());
        when(ruleService.getByRulesetIdAndName(1L, "ALLOW_SQL_TYPES"))
                .thenReturn(Optional.of(getRuleWithForbiddenSqlTypes()));

        boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context);

        assertEquals(false, result);
    }

    @Test
    public void testDoPreHandle_exceedMaxExecuteSqls() {
        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(getSqlTuplesWithViolationList(3));
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(getUser());
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(2));

        boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context);

        assertEquals(false, result);
    }

    @Test
    public void testDoPreHandle_exceedMaxReturnRows() {
        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(getSqlTuplesWithViolationList(1));
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(getUser());
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));

        boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context);

        assertEquals(false, result);
    }

    @Test
    public void testDoAfterCompletion_notAllowedEditResultset() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlExecuteResult.setResultSetMetaData(getResultSetMetaData());
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(getUser());
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        assertEquals(false, sqlExecuteResult.getResultSetMetaData().isEditable());
    }

    @Test
    public void testDoAfterCompletion_notAllowedExportResultset() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(getUser());
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        assertEquals(false, sqlExecuteResult.getAllowExport());
    }

    private User getUser() {
        User user = new User();
        user.setOrganizationType(OrganizationType.ORGANIZATION);
        return user;
    }

    private Rule getRule() {
        Rule rule = new Rule();
        rule.setProperties(getProperties());
        return rule;
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.add(new PropertyMetadata("ALLOW_SQL_TYPES", "SELECT, INSERT, UPDATE, DELETE"));
        return properties;
    }

    private Rule getRuleWithForbiddenSqlTypes() {
        Rule rule = new Rule();
        rule.setProperties(getPropertiesWithForbiddenSqlTypes());
        return rule;
    }

    private Properties getPropertiesWithForbiddenSqlTypes() {
        Properties properties = new Properties();
        properties.add(new PropertyMetadata("ALLOW_SQL_TYPES", "SELECT, INSERT, UPDATE"));
        return properties;
    }

    private List<SqlTuplesWithViolation> getSqlTuplesWithViolationList(int times) {
        List<SqlTuplesWithViolation> sqlTuplesWithViolationList = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            sqlTuplesWithViolationList.add(getSqlTuplesWithViolation());
        }
        return sqlTuplesWithViolationList;
    }

    private SqlTuplesWithViolation getSqlTuplesWithViolation() {
        SqlTuplesWithViolation sqlTuplesWithViolation = new SqlTuplesWithViolation();
        sqlTuplesWithViolation.setSqlTuple(getSqlTuple());
        sqlTuplesWithViolation.setViolatedRules(getRuleViolationList());
        return sqlTuplesWithViolation;
    }

    private SqlTuple getSqlTuple() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM users");
        return sqlTuple;
    }

    private List<RuleViolation> getRuleViolationList() {
        RuleViolation ruleViolation = new RuleViolation();
        ruleViolation.setLevel(RuleViolation.Level.ERROR);
        List<RuleViolation> ruleViolationList = new ArrayList<>();
        ruleViolationList.add(ruleViolation);
        return ruleViolationList;
    }

    private ResultSetMetaData getResultSetMetaData() {
        ResultSetMetaData resultSetMetaData = new ResultSetMetaData();
        resultSetMetaData.setEditable(true);
        return resultSetMetaData;
    }
}