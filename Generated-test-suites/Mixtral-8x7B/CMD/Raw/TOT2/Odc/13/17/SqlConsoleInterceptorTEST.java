import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuple;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@Component
public class SqlConsoleInterceptorTest {

 @Mock
 private AuthenticationFacade authenticationFacade;

 @Mock
 private RuleService ruleService;

 @Mock
 private SqlConsoleRuleService sqlConsoleRuleService;

 @InjectMocks
 private SqlConsoleInterceptor sqlConsoleInterceptor;

 private SqlAsyncExecuteResp sqlAsyncExecuteResp;
 private SqlExecuteResult sqlExecuteResult;
 private SqlTuple sqlTuple;
 private AbstractSyntaxTree abstractSyntaxTree;
 private BasicResult basicResult;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.openMocks(this);
 sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
 sqlExecuteResult = new SqlExecuteResult();
 sqlTuple = new SqlTuple();
 abstractSyntaxTree = Mockito.mock(AbstractSyntaxTree.class);
 basicResult = new BasicResult(SqlType.SELECT);
 }

 // Test doPreHandle method with positive case
 @Test
 public void testDoPreHandlePositive() {
 Map<String, Object> context = new HashMap<>();
 when(authenticationFacade.currentUser()).thenReturn(getUser());
 when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.empty());
 when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(), any())).thenReturn(Optional.empty());
 sqlConsoleInterceptor.doPreHandle(null, sqlAsyncExecuteResp, null, context);
 Assertions.assertFalse(context.containsKey(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK));
 Assertions.assertFalse(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
 }

 // Test handle method with positive case
 @Test
 public void testHandlePositive() {
 Long ruleSetId = 1L;
 sqlTuple.setSqlId("1");
 when(ConnectionSessionUtil.getRuleSetId(null)).thenReturn(ruleSetId);
 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, any(), any()))
 .thenReturn(Optional.of(10));
 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, any(), any()))
 .thenReturn(Optional.of(1));
 when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
 .thenReturn(Optional.empty());
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, any()))
 .thenReturn(false);
 sqlAsyncExecuteResp.setSqls(getSqlTuples());
 sqlConsoleInterceptor.handle(sqlAsyncExecuteResp, sqlExecuteResult, null, new HashMap<>());
 Assertions.assertTrue(sqlExecuteResult.isAllowExport());
 }

 // Test determineSqlType method with SyntaxErrorException
 @Test
 public void testDetermineSqlTypeSyntaxError() throws SyntaxErrorException {
 sqlTuple.setOriginalSql("INVALID SQL");
 when(AbstractSyntaxTreeFactories.getAstFactory(any(), anyInt())).thenThrow(SyntaxErrorException.class);
 Assertions.assertEquals(SqlType.UNKNOWN, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL).getSqlType());
 }

 // Test doAfterCompletion with not allowed export
 @Test
 public void testDoAfterCompletionNotAllowedExport() {
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 when(authenticationFacade.currentUser()).thenReturn(getUser());
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, any())).thenReturn(true);
 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, null, new HashMap<>());
 Assertions.assertFalse(sqlExecuteResult.isAllowExport());
 }

 // Test isIndividualTeam method with Individual OrganizationType
 @Test
 public void testIsIndividualTeamTrue() {
 when(authenticationFacade.currentUser()).thenReturn(getIndividualUser());
 Assertions.assertTrue(sqlConsoleInterceptor.isIndividualTeam());
 }

 // Helper methods
 private List<SqlTuple> getSqlTuples() {
 List<SqlTuple> sqlTuples = new ArrayList<>();
 sqlTuples.add(sqlTuple);
 return sqlTuples;
 }

 private User getUser() {
 User user = new User();
 user.setOrganizationType(OrganizationType.TEAM);
 return user;
 }

 private User getIndividualUser() {
 User user = new User();
 user.setOrganizationType(OrganizationType.INDIVIDUAL);
 return user;
 }

 static class User {
 OrganizationType organizationType;

 public OrganizationType getOrganizationType() {
 return organizationType;
 }

 public void setOrganizationType(OrganizationType organizationType) {
 this.organizationType = organizationType;
 }
 }
}