import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Component
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private ConnectionSessionUtil connectionSessionUtil;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_allowed() {
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(authenticationFacade.currentUser()).thenReturn(new AuthenticationFacade.User("user", OrganizationType.INDIVIDUAL));

        boolean result = sqlConsoleInterceptor.doPreHandle(req, resp, connectionSession, context);

        assertTrue(result);
        verify(sqlConsoleRuleService, never()).getProperties(anyLong(), any(), any(), any());
        verify(ruleService, never()).getByRulesetIdAndName(anyLong(), any());
    }

    @Test
    public void testDoPreHandle_forbidden() {
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(authenticationFacade.currentUser()).thenReturn(new AuthenticationFacade.User("user", OrganizationType.TEAM));
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), any()))
                .thenReturn(Optional.of(100));

        boolean result = sqlConsoleInterceptor.doPreHandle(req, resp, connectionSession, context);

        assertFalse(result);
        verify(ruleService, times(4)).getByRulesetIdAndName(eq(1L), any());
    }

    @Test
    public void testDoAfterCompletion_notForbidden() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);

        sqlConsoleInterceptor.doAfterCompletion(result, connectionSession, new HashMap<>());

        assertTrue(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_forbidden() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(result, connectionSession, new HashMap<>());

        assertFalse(result.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam_individual() {
        AuthenticationFacade.User user = new AuthenticationFacade.User("user", OrganizationType.INDIVIDUAL);

        boolean result = sqlConsoleInterceptor.isIndividualTeam(user);

        assertTrue(result);
    }

    @Test
    public void testIsIndividualTeam_team() {
        AuthenticationFacade.User user = new AuthenticationFacade.User("user", OrganizationType.TEAM);

        boolean result = sqlConsoleInterceptor.isIndividualTeam(user);

        assertFalse(result);
    }

    @Test
    public void testDetermineSqlType_success() throws Exception {
        String sql = "SELECT * FROM users;";
        SqlTuple sqlTuple = new SqlTuple("1", sql, 0, null);
        AbstractSyntaxTree ast = new AbstractSyntaxTree(sql, AbstractSyntaxTreeFactories.getAstFactory(DialectType.MYSQL, 0));
        sqlTuple.setAst(ast);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.SELECT, result.getSqlType());
        assertFalse(result.isSyntaxError());
    }

    @Test
    public void testDetermineSqlType_error() throws Exception {
        String sql = "SELECT * FROM users;";
        SqlTuple sqlTuple = new SqlTuple("1", sql, 0, null);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.UNKNOWN, result.getSqlType());
        assertTrue(result.isSyntaxError());
    }

}