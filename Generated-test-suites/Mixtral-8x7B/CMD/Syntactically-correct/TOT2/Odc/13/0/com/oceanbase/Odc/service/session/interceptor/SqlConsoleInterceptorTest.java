import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.result.BasicResult;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.mockito.Mockito.when;

@Component
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsNull_thenReturnTrue() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = authenticationFacade;
        when(authenticationFacade.currentUser()).thenReturn(new MockUser(OrganizationType.ORGANIZATION));

        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(new ArrayList<>());
        Map<String, Object> context = new HashMap<>();

        ConnectionSession session = new ConnectionSession();

        assertTrue(interceptor.doPreHandle(new SqlAsyncExecuteReq(), sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsNotNullAndNotIndividualTeam_thenReturnTrue() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = authenticationFacade;
        when(authenticationFacade.currentUser()).thenReturn(new MockUser(OrganizationType.ORGANIZATION));

        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(new ArrayList<>());
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);

        assertTrue(interceptor.doPreHandle(new SqlAsyncExecuteReq(), sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsNotNullAndIndividualTeam_thenReturnTrue() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = authenticationFacade;
        when(authenticationFacade.currentUser()).thenReturn(new MockUser(OrganizationType.INDIVIDUAL));

        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(new ArrayList<>());
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);

        assertTrue(interceptor.doPreHandle(new SqlAsyncExecuteReq(), sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsNotNullAndNotIndividualTeamAndQueryLimitIsPresent_thenReturnTrue() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = authenticationFacade;
        when(authenticationFacade.currentUser()).thenReturn(new MockUser(OrganizationType.ORGANIZATION));

        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(new ArrayList<>());
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setDialectType(DialectType.MYSQL);

        when(interceptor.sqlConsoleRuleService.getProperties(anyLong(), any(), any()))
                .thenReturn(Optional.of(2L));
        when(interceptor.sqlConsoleRuleService.getProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(2));

        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        sqlAsyncExecuteReq.setQueryLimit(1L);

        assertTrue(interceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsNotNullAndNotIndividualTeamAndQueryLimitIsNotPresent_thenReturnFalse() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = authenticationFacade;
        when(authenticationFacade.currentUser()).thenReturn(new MockUser(OrganizationType.ORGANIZATION));

        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(new ArrayList<>());
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);
        session.setDialectType(DialectType.MYSQL);

        when(interceptor.sqlConsoleRuleService.getProperties(anyLong(), any(), any()))
                .thenReturn(Optional.of(2L));
        when(interceptor.sqlConsoleRuleService.getProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(2));

        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();

        assertFalse(interceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testDoAfterCompletion_whenSqlExecuteStatusIsSuccessAndIndividualTeam_thenReturnTrue() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = authenticationFacade;
        when(authenticationFacade.currentUser()).thenReturn(new MockUser(OrganizationType.INDIVIDUAL));

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);

        when(interceptor.isIndividualTeam()).thenReturn(true);

        interceptor.doAfterCompletion(sqlExecuteResult, session, new HashMap<>());

        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_whenSqlExecuteStatusIsSuccessAndNotIndividualTeam_thenReturnFalse() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = authenticationFacade;
        when(authenticationFacade.currentUser()).thenReturn(new MockUser(OrganizationType.ORGANIZATION));

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);

        when(interceptor.isIndividualTeam()).thenReturn(false);

        interceptor.doAfterCompletion(sqlExecuteResult, session, new HashMap<>());

        assertFalse(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_whenSqlExecuteStatusIsFailure_thenReturnFalse() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = authenticationFacade;
        when(authenticationFacade.currentUser()).thenReturn(new MockUser(OrganizationType.ORGANIZATION));

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.FAILURE);

        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(1L);

        interceptor.doAfterCompletion(sqlExecuteResult, session, new HashMap<>());

        assertFalse(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam_whenOrganizationTypeIsIndividual_thenReturnTrue() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = authenticationFacade;
        when(authenticationFacade.currentUser()).thenReturn(new MockUser(OrganizationType.INDIVIDUAL));

        assertTrue(interceptor.isIndividualTeam());
    }

    @Test
    public void testIsIndividualTeam_whenOrganizationTypeIsOrganization_thenReturnFalse() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = authenticationFacade;
        when(authenticationFacade.currentUser()).thenReturn(new MockUser(OrganizationType.ORGANIZATION));

        assertFalse(interceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType_whenAstIsNull_thenReturnBasicResultWithSqlTypeUnknown() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        BasicResult basicResult = interceptor.determineSqlType(new SqlTuple(), DialectType.MYSQL);

        assertEquals(SqlType.UNKNOWN, basicResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_whenAstIsNotNull_thenReturnBasicResultWithCorrectSqlType() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

        BasicResult basicResult = interceptor.determineSqlType(new SqlTuple(null, SQL, null), DialectType.MYSQL);

        assertEquals(SqlType.DDL, basicResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_whenSyntaxErrorOccurs_thenReturnBasicResultWithSyntaxErrorTrue() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

        BasicResult basicResult = interceptor.determineSqlType(new SqlTuple(null, "Invalid SQL", null),
                DialectType.MYSQL);

        assertTrue(basicResult.isSyntaxError());
    }

    private class MockUser {

        private OrganizationType organizationType;

        public MockUser(OrganizationType organizationType) {
            this.organizationType = organizationType;
        }

        public OrganizationType getOrganizationType() {
            return organizationType;
        }

        public void setOrganizationType(OrganizationType organizationType) {
            this.organizationType = organizationType;
        }

    }

}