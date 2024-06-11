import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
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
 @Mock
 private AbstractSyntaxTree abstractSyntaxTree;
 @Mock
 private AbstractSyntaxTreeFactories abstractSyntaxTreeFactories;

 private SqlConsoleInterceptor sqlConsoleInterceptor;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 sqlConsoleInterceptor = new SqlConsoleInterceptor();
 ReflectionTestUtils.setField(sqlConsoleInterceptor, "authenticationFacade", authenticationFacade);
 ReflectionTestUtils.setField(sqlConsoleInterceptor, "connectionSessionUtil", connectionSessionUtil);
 ReflectionTestUtils.setField(sqlConsoleInterceptor, "ruleService", ruleService);
 ReflectionTestUtils.setField(sqlConsoleInterceptor, "sqlConsoleRuleService", sqlConsoleRuleService);
 ReflectionTestUtils.setField(sqlConsoleInterceptor, "abstractSyntaxTreeFactories", abstractSyntaxTreeFactories);
 }

 @Test
 public void testDoPreHandle_whenRuleSetIdIsNull_returnsTrue() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);

 SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);

 Assertions.assertTrue(result);
 }

 @Test
 public void testDoPreHandle_whenIsIndividualTeamIsTrue_returnsTrue() {
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);

 SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);

 Assertions.assertTrue(result);
 }

 @Test
 public void testDoPreHandle_whenContextContainsNeedSqlConsoleCheckFalse_returnsTrue() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.ORGANIZATION);
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

 Map<String, Object> context = new HashMap<>();
 context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

 SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();

 boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);

 Assertions.assertTrue(result);
 }

 @Test
 public void testDoPreHandle_whenSqlConsoleInterceptedIsTrue_returnsTrue() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.ORGANIZATION);
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

 Map<String, Object> context = new HashMap<>();

 SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
 sqlAsyncExecuteResp.setSqls(List.of(new SqlTuplesWithViolation()));

 boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);

 Assertions.assertTrue(result);
 Assertions.assertEquals(sqlAsyncExecuteResp,
 context.get(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED));
 }

 @Test
 public void testDoPreHandle_whenQueryLimitIsPresent_setsQueryLimit() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.ORGANIZATION);
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

 Map<String, Object> context = new HashMap<>();

 SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();

 when(sqlConsoleRuleService.getProperties(eq(1L), any(SqlConsoleRules.class), any(DialectType.class), any()))
 .thenReturn(Optional.of(10));

 boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);

 Assertions.assertTrue(result);
 Assertions.assertEquals(10, sqlAsyncExecuteReq.getQueryLimit().intValue());
 }

 @Test
 public void testDoPreHandle_whenAllowExecuteIsFalse_setsViolatedRules() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.ORGANIZATION);
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

 Map<String, Object> context = new HashMap<>();

 SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
 sqlAsyncExecuteResp.setSqls(List.of(new SqlTuplesWithViolation()));

 when(sqlConsoleRuleService.getProperties(eq(1L), any(SqlConsoleRules.class), any(DialectType.class), any()))
 .thenReturn(Optional.of(10));
 when(ruleService.getByRulesetIdAndName(eq(1L), any(String.class)))
 .thenReturn(Optional.of(new Rule()));
 when(sqlConsoleRuleService.getListProperties(eq(1L), any(SqlConsoleRules.class), any(DialectType.class), any()))
 .thenReturn(Optional.of(List.of("CREATE_PL")));

 boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);

 Assertions.assertFalse(result);
 Assertions.assertEquals(1, sqlAsyncExecuteResp.getSqls().get(0).getViolatedRules().size());
 }

 @Test
 public void testDoAfterCompletion_whenResponseStatusIsSuccess_setsAllowExportAndEditable() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.ORGANIZATION);
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

 Assertions.assertTrue(sqlExecuteResult.getAllowExport());
 Assertions.assertFalse(sqlExecuteResult.getResultSetMetaData().isEditable());
 }

 @Test
 public void testDoAfterCompletion_whenResponseStatusIsNotSuccess_doesNotSetAllowExportAndEditable() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.ORGANIZATION);
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.FAILED);

 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

 Assertions.assertFalse(sqlExecuteResult.getAllowExport());
 Assertions.assertNull(sqlExecuteResult.getResultSetMetaData());
 }

 @Test
 public void testDetermineSqlType_whenSqlTypeIsUnknown_returnsBasicResultWithSqlTypeOthers() {
 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setOriginalSql("SELECT * FROM users");

 BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

 Assertions.assertEquals(SqlType.OTHERS, basicResult.getSqlType());
 }

 @Test
 public void testDetermineSqlType_whenSyntaxErrorIsTrue_returnsBasicResultWithSyntaxErrorTrue() {
 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setOriginalSql("SELECT * FROM users");

 BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

 Assertions.assertFalse(basicResult.getSyntaxError());

 sqlTuple.setSyntaxError(true);

 basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

 Assertions.assertTrue(basicResult.getSyntaxError());
 }

}