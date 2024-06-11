import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class SqlConsoleInterceptorTest {

 @MockBean
 private AuthenticationFacade authenticationFacade;

 @MockBean
 private RuleService ruleService;

 @MockBean
 private SqlConsoleRuleService sqlConsoleRuleService;

 @Autowired
 private BaseTimeConsumingInterceptor sqlConsoleInterceptor;

 private SqlAsyncExecuteReq req;
 private SqlAsyncExecuteResp resp;
 private ConnectionSession session;
 private Map<String, Object> context;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.openMocks(this);
 req = new SqlAsyncExecuteReq();
 resp = new SqlAsyncExecuteResp();
 session = mock(ConnectionSession.class);
 context = new HashMap<>();
 }

 @Test
 public void testDoPreHandle_noRuleSetId_returnsTrue() {
 ConnectionSessionUtil.setRuleSetId(session, null);
 context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
 Assertions.assertTrue(sqlConsoleInterceptor.doPreHandle(req, resp, session, context));
 }

 @Test
 public void testDoPreHandle_individualTeam_returnsTrue() {
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);
 context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
 Assertions.assertTrue(sqlConsoleInterceptor.doPreHandle(req, resp, session, context));
 }

 @Test
 public void testDoPreHandle_noNeedSqlConsoleCheck_returnsTrue() {
 ConnectionSessionUtil.setRuleSetId(session, 1L);
 context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
 Assertions.assertTrue(sqlConsoleInterceptor.doPreHandle(req, resp, session, context));
 }

 @Test
 public void testDoPreHandle_ruleSetIdNull_forbiddenCreatePl_returnsFalse() {
 ConnectionSessionUtil.setRuleSetId(session, 1L);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
 .thenReturn(true);
 context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
 Assertions.assertFalse(sqlConsoleInterceptor.doPreHandle(req, resp, session, context));
 }

 // Add more test cases based on different scenarios

 @Test
 public void testDoAfterCompletion_success_individualTeam_allowExportTrue() {
 ConnectionSessionUtil.setRuleSetId(session, null);
 SqlExecuteResult result = new SqlExecuteResult();
 result.setStatus(SqlExecuteStatus.SUCCESS);
 sqlConsoleInterceptor.doAfterCompletion(result, session, context);
 Assertions.assertTrue(result.isAllowExport());
 }

 @Test
 public void testDoAfterCompletion_success_ruleSetIdPresent_notAllowedExport_allowExportFalse() {
 ConnectionSessionUtil.setRuleSetId(session, 1L);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
 .thenReturn(true);
 SqlExecuteResult result = new SqlExecuteResult();
 result.setStatus(SqlExecuteStatus.SUCCESS);
 sqlConsoleInterceptor.doAfterCompletion(result, session, context);
 Assertions.assertFalse(result.isAllowExport());
 }

 // Add more test cases for doAfterCompletion
}