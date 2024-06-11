import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.result.BasicResult;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class SqlConsoleInterceptorTest {

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

    @Autowired
    private BaseTimeConsumingInterceptor baseTimeConsumingInterceptor;

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setConnectionSessionUtil(connectionSessionUtil);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
        sqlConsoleInterceptor.setBaseTimeConsumingInterceptor(baseTimeConsumingInterceptor);
    }

    @Test
    public void testDoPreHandle_NoRuleSetId_ReturnsTrue() {
        Mockito.when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);

        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(new ArrayList<>());
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.doPreHandle(new SqlAsyncExecuteReq(), sqlAsyncExecuteResp,
                connectionSession, context);

        Assertions.assertTrue(result, "Method should return true if there is no rule set ID");
        verify(authenticationFacade, times(0)).currentUser();
        verify(ruleService, times(0)).getByRulesetIdAndName(anyLong(), anyString());
        verify(sqlConsoleRuleService, times(0)).getProperties(anyLong(), anyString(), any(), any());
        verify(sqlConsoleRuleService, times(0)).isForbidden(anyString(), any());
        verify(baseTimeConsumingInterceptor, times(0)).getExecuteStageName();
    }

    @Test
    public void testDoPreHandle_IndividualTeam_ReturnsTrue() {
        Mockito.when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        Mockito.when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.INDIVIDUAL);

        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(new ArrayList<>());
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.doPreHandle(new SqlAsyncExecuteReq(), sqlAsyncExecuteResp,
                connectionSession, context);

        Assertions.assertTrue(result, "Method should return true if the user is from an individual team");
        verify(authenticationFacade, times(1)).currentUser();
        verify(ruleService, times(0)).getByRulesetIdAndName(anyLong(), anyString());
        verify(sqlConsoleRuleService, times(0)).getProperties(anyLong(), anyString(), any(), any());
        verify(sqlConsoleRuleService, times(0)).isForbidden(anyString(), any());
        verify(baseTimeConsumingInterceptor, times(0)).getExecuteStageName();
    }

    @Test
    public void testDoPreHandle_RuleSetIdAndQueryLimit_UpdatesQueryLimit() {
        Mockito.when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        Mockito.when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);

        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        sqlAsyncExecuteReq.setQueryLimit(10);

        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(Arrays.asList(
                new SqlTuplesWithViolation(new SqlTuple("select * from table1", "", ""),
                        new ArrayList<>(), "", "")
        ));

        Map<String, Object> context = new HashMap<>();

        sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);

        Assertions.assertEquals(5, sqlAsyncExecuteReq.getQueryLimit(),
                "Method should update query limit if there is a rule set ID and query limit");
        verify(authenticationFacade, times(1)).currentUser();
        verify(ruleService, times(1)).getByRulesetIdAndName(eq(1L), eq(SqlConsoleRules.MAX_RETURN_ROWS.getRuleName()));
        verify(ruleService, times(1)).getByRulesetIdAndName(eq(1L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()));
        verify(sqlConsoleRuleService, times(1)).getProperties(eq(1L), eq(SqlConsoleRules.MAX_RETURN_ROWS.getRuleName()),
                any(), any());
        verify(sqlConsoleRuleService, times(1)).getProperties(eq(1L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()),
                any(), any());
        verify(sqlConsoleRuleService, times(1)).isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()),
                any());
        verify(baseTimeConsumingInterceptor, times(1)).getExecuteStageName();
    }

    @Test
    public void testDoAfterCompletion_NotAllowedEditResultset_NoEditableMetadata() {
        Mockito.when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        Mockito.when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET.getRuleName(),
                connectionSession)).thenReturn(true);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setResultSetMetaData(new BasicResult(SqlType.QUERY, "", "", "", "", "", null, null));

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

        Assertions.assertFalse(sqlExecuteResult.getResultSetMetaData().isEditable(),
                "Result set metadata should not be editable if not allowed by rule");
    }

    @Test
    public void testDoAfterCompletion_NotAllowedEditResultset_NullMetadata() {
        Mockito.when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        Mockito.when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET.getRuleName(),
                connectionSession)).thenReturn(true);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

        Assertions.assertNull(sqlExecuteResult.getResultSetMetaData(),
                "Result set metadata should be null if not allowed by rule and not present");
    }

    @Test
    public void testDoAfterCompletion_AllowedEditResultset_EditableMetadata() {
        Mockito.when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        Mockito.when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET.getRuleName(),
                connectionSession)).thenReturn(false);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setResultSetMetaData(new BasicResult(SqlType.QUERY, "", "", "", "", "", null, null));

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

        Assertions.assertTrue(sqlExecuteResult.getResultSetMetaData().isEditable(),
                "Result set metadata should be editable if allowed by rule");
    }

    @Test
    public void testDoAfterCompletion_NotAllowedExport_FalseAllowExport() {
        Mockito.when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        Mockito.when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET.getRuleName(),
                connectionSession)).thenReturn(true);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

        Assertions.assertFalse(sqlExecuteResult.isAllowExport(),
                "Export should be disallowed if not allowed by rule");
    }

    @Test
    public void testDoAfterCompletion_AllowedExport_TrueAllowExport() {
        Mockito.when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        Mockito.when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET.getRuleName(),
                connectionSession)).thenReturn(false);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

        Assertions.assertTrue(sqlExecuteResult.isAllowExport(),
                "Export should be allowed if allowed by rule");
    }
}