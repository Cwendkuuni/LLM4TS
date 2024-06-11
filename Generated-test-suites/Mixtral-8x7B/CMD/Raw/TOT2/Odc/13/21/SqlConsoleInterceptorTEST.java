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
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.session.ConnectionSessionContext;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResultWithViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class SqlConsoleInterceptorTest {

 @MockBean
 private AuthenticationFacade authenticationFacade;

 @MockBean
 private ConnectionSessionUtil connectionSessionUtil;

 @MockBean
 private RuleService ruleService;

 @MockBean
 private SqlConsoleRuleService sqlConsoleRuleService;

 @Autowired
 private SqlConsoleInterceptor sqlConsoleInterceptor;

 private ConnectionSession connectionSession;
 private SqlAsyncExecuteReq sqlAsyncExecuteReq;
 private SqlAsyncExecuteResp sqlAsyncExecuteResp;
 private ConnectionSessionContext connectionSessionContext;

 @BeforeEach
 public void setup() {
 connectionSession = mock(ConnectionSession.class);
 sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
 sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
 connectionSessionContext = mock(ConnectionSessionContext.class);
 }

 @Test
 public void testDoPreHandle_happyPath() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.getProperties(anyLong(), any(), any(), any())).thenReturn(Optional.of(10));

 sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession,
 connectionSessionContext);

 verify(connectionSessionContext, times(1)).put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
 }

 @Test
 public void testDoPreHandle_needSqlConsoleCheck() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.getProperties(anyLong(), any(), any(), any())).thenReturn(Optional.of(10));

 sqlAsyncExecuteReq.setNeedSqlConsoleCheck(true);

 sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession,
 connectionSessionContext);

 verify(connectionSessionContext, times(1)).put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
 }

 @Test
 public void testDoPreHandle_exception() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenThrow(new RuntimeException());

 sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession,
 connectionSessionContext);

 verify(connectionSessionContext, times(1)).put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
 }

 @Test
 public void testDoAfterCompletion_success() {
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, connectionSessionContext);

 verify(connectionSessionContext, times(1)).putAllowExport(true);
 }

 @Test
 public void testDoAfterCompletion_failure() {
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.FAILURE);

 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, connectionSessionContext);

 verify(connectionSessionContext, times(1)).putAllowExport(false);
 }

 @Test
 public void testGetExecuteStageName() {
 Assertions.assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
 }

 @Test
 public void testIsIndividualTeam() {
 when(authenticationFacade.currentUser()).thenReturn(mock(com.oceanbase.odc.service.iam.auth.User.class));
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

 Assertions.assertTrue(sqlConsoleInterceptor.isIndividualTeam());
 }

 @Test
 public void testIsIndividualTeam_notIndividual() {
 when(authenticationFacade.currentUser()).thenReturn(mock(com.oceanbase.odc.service.iam.auth.User.class));
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

 Assertions.assertFalse(sqlConsoleInterceptor.isIndividualTeam());
 }

 @Test
 public void testDetermineSqlType_success() {
 String sql = "SELECT * FROM users";
 AbstractSyntaxTree abstractSyntaxTree = mock(AbstractSyntaxTree.class);
 BasicResult basicResult = new BasicResult();
 basicResult.setSqlType(SqlType.SELECT);

 when(AbstractSyntaxTreeFactories.getAstFactory(any(), anyInt())
 .buildAbstractSyntaxTree(anyString())).thenReturn(abstractSyntaxTree);
 when(abstractSyntaxTree.getParseResult()).thenReturn(basicResult);

 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setOriginalSql(sql);

 SqlExecuteResultWithViolation sqlExecuteResultWithViolation = sqlConsoleInterceptor.determineSqlType(sqlTuple,
 DialectType.MYSQL);

 Assertions.assertEquals(SqlType.SELECT, sqlExecuteResultWithViolation.getSqlType());
 Assertions.assertNull(sqlExecuteResultWithViolation.getSyntaxError());
 }

 @Test
 public void testDetermineSqlType_syntaxError() {
 String sql = "SELECT * FROM usrs";
 AbstractSyntaxTree abstractSyntaxTree = mock(AbstractSyntaxTree.class);
 BasicResult basicResult = new BasicResult();
 basicResult.setSyntaxError(true);

 when(AbstractSyntaxTreeFactories.getAstFactory(any(), anyInt())
 .buildAbstractSyntaxTree(anyString())).thenReturn(abstractSyntaxTree);
 when(abstractSyntaxTree.getParseResult()).thenReturn(basicResult);

 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setOriginalSql(sql);

 SqlExecuteResultWithViolation sqlExecuteResultWithViolation = sqlConsoleInterceptor.determineSqlType(sqlTuple,
 DialectType.MYSQL);

 Assertions.assertEquals(SqlType.UNKNOWN, sqlExecuteResultWithViolation.getSqlType());
 Assertions.assertTrue(sqlExecuteResultWithViolation.getSyntaxError());
 }
}