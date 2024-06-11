import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Component
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.authenticationFacade = authenticationFacade;
        sqlConsoleInterceptor.ruleService = ruleService;
        sqlConsoleInterceptor.sqlConsoleRuleService = sqlConsoleRuleService;
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsNull() {
        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context);

        Assert.assertTrue(result);
        verify(sqlConsoleRuleService, never()).getProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, never()).getListProperties(anyLong(), any(), any(), any());
        verify(ruleService, never()).getByRulesetIdAndName(anyLong(), any());
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsNotNull() {
        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setDialectType(DialectType.MYSQL);
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);

        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(10));

        when(sqlConsoleRuleService.getListProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(List.of("SELECT", "INSERT")));

        ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())
                .ifPresent(rule -> {
                    RuleViolation violation = new RuleViolation();
                    violation.setLevel(rule.getLevel());
                    violation.setLocalizedMessage("Max execute SQLs violation");
                    rule.setViolation(violation);
                });

        AbstractSyntaxTree ast = Mockito.mock(AbstractSyntaxTree.class);
        when(ast.getParseResult()).thenReturn(new BasicResult(SqlType.SELECT));

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(ast);
        sqlTuple.setOriginalSql("SELECT * FROM users");
        sqlTuple.setOffset(0);

        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        sqls.add(new SqlTuplesWithViolation(sqlTuple, List.of()));

        sqlAsyncExecuteResp.setSqls(sqls);

        boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context);

        Assert.assertTrue(result);
        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, times(1)).getListProperties(anyLong(), any(), any(), any());
        verify(ruleService, times(1)).getByRulesetIdAndName(anyLong(), any());
    }

    @Test
    public void testDoAfterCompletion_whenResponseStatusIsSuccess() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setOrganizationType(OrganizationType.INDIVIDUAL);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, new HashMap<>());

        Assert.assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_whenResponseStatusIsNotSuccess() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.ERROR);
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setOrganizationType(OrganizationType.INDIVIDUAL);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, new HashMap<>());

        Assert.assertFalse(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam_whenOrganizationTypeIsIndividual() {
        when(authenticationFacade.currentUser()).thenReturn(new org.oceanbase.odc.service.iam.auth.User("user", null,
                OrganizationType.INDIVIDUAL));

        Assert.assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testIsIndividualTeam_whenOrganizationTypeIsNotIndividual() {
        when(authenticationFacade.currentUser()).thenReturn(new org.oceanbase.odc.service.iam.auth.User("user", null,
                OrganizationType.ORGANIZATION));

        Assert.assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType_whenSyntaxErrorIsTrue() throws SyntaxErrorException {
        AbstractSyntaxTree ast = Mockito.mock(AbstractSyntaxTree.class);
        when(ast.getParseResult()).thenThrow(new SyntaxErrorException(new Exception()));

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM users");
        sqlTuple.setOffset(0);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        Assert.assertNotNull(result);
        Assert.assertEquals(SqlType.UNKNOWN, result.getSqlType());
        Assert.assertTrue(result.isSyntaxError());
    }

    @Test
    public void testDetermineSqlType_whenSyntaxErrorIsFalse() throws SyntaxErrorException {
        AbstractSyntaxTree ast = Mockito.mock(AbstractSyntaxTree.class);
        when(ast.getParseResult()).thenReturn(new BasicResult(SqlType.SELECT));

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM users");
        sqlTuple.setOffset(0);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        Assert.assertNotNull(result);
        Assert.assertEquals(SqlType.SELECT, result.getSqlType());
        Assert.assertFalse(result.isSyntaxError());
    }

}