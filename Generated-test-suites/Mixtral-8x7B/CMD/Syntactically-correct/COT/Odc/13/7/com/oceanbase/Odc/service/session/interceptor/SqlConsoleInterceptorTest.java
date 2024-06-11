import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

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
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

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
    }

    @Test
    public void testDoPreHandle_NoRuleSetId_ReturnsTrue() {
        ConnectionSession session = new ConnectionSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_IsIndividualTeam_ReturnsTrue() {
        ConnectionSession session = new ConnectionSession();
        session.setOrganizationType(OrganizationType.INDIVIDUAL);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_NoNeedSqlConsoleCheck_ReturnsTrue() {
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_AllowedToExecute_ReturnsTrue() {
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(100));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MYSQL, String.class))
                .thenReturn(Optional.of(Arrays.asList(SqlType.SELECT.name(), SqlType.INSERT.name())));

        List<SqlTuplesWithViolation> sqlTuples = Collections.singletonList(
                new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM table"),
                        Collections.emptyList()));
        response.setSqls(sqlTuples);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_NotAllowedToExecute_ReturnsFalse() {
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(100));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(true);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MYSQL, String.class))
                .thenReturn(Optional.of(Arrays.asList(SqlType.SELECT.name(), SqlType.INSERT.name())));

        List<SqlTuplesWithViolation> sqlTuples = Collections.singletonList(
                new SqlTuplesWithViolation(new SqlTuple("CREATE PROCEDURE procedure_name() BEGIN END"),
                        Collections.emptyList()));
        response.setSqls(sqlTuples);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertFalse(result);
    }

    // Add more test cases for different scenarios

    @Test
    public void testGetExecuteStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testIsIndividualTeam() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.INDIVIDUAL);

        assertTrue(sqlConsoleInterceptor.isIndividualTeam());

        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);

        assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }

    // Add more test cases for other methods
}