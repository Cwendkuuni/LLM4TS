import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@Component
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
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser()).thenReturn(mock(com.oceanbase.odc.core.session.User.class));

        AtomicBoolean sqlConsoleIntercepted = new AtomicBoolean(false);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(100));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(2));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(List.of(SqlType.SELECT.name())));

        sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
        assertEquals(sqlConsoleIntercepted.get(), context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser()).thenReturn(mock(com.oceanbase.odc.core.session.User.class));

        AtomicBoolean allowExecute = new AtomicBoolean(true);

        List<SqlTuple> sqlTuples = createSqlTuples();

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(100));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(2));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(List.of(SqlType.SELECT.name())));

        sqlConsoleInterceptor.handle(request, response, session, context);

        assertFalse(allowExecute.get());
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser()).thenReturn(mock(com.oceanbase.odc.core.session.User.class));

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertEquals(SqlExecuteStatus.SUCCESS, response.getStatus());
    }

    private List<SqlTuple> createSqlTuples() {
        return List.of(new SqlTuple("SELECT * FROM table1", "1", new int[0], "originalSql",
                AbstractSyntaxTreeFactories.getAstFactory(DialectType.MYSQL, 0)));
    }
}