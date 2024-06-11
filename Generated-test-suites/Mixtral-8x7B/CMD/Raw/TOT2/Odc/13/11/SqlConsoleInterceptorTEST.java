import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class SqlConsoleInterceptorTest {

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    private BaseTimeConsumingInterceptor.Context context;
    private SqlAsyncExecuteReq sqlAsyncExecuteReq;
    private SqlAsyncExecuteResp sqlAsyncExecuteResp;

    @BeforeEach
    public void setup() {
        context = new BaseTimeConsumingInterceptor.Context();
        sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        sqlAsyncExecuteResp = new SqlAsyncExecuteResp();

        Mockito.when(authenticationFacade.currentUser()).thenReturn(getUser());
    }

    @Test
    public void testDoPreHandle_noRuleSetId() {
        sqlAsyncExecuteReq.setSqls(List.of(getSqlTuple()));
        sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, getConnectionSession(), context);

        Assertions.assertFalse(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_individualTeam() {
        setupMocksForIndividualTeam();

        sqlAsyncExecuteReq.setSqls(List.of(getSqlTuple()));
        sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, getConnectionSession(), context);

        Assertions.assertFalse(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_allowExecute() {
        setupMocksForRuleSet();

        sqlAsyncExecuteReq.setSqls(List.of(getSqlTuple()));
        sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, getConnectionSession(), context);

        Assertions.assertTrue(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_forbiddenToCreatePl() {
        setupMocksForRuleSet_forbiddenToCreatePl();

        sqlAsyncExecuteReq.setSqls(List.of(getSqlTuple()));
        sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, getConnectionSession(), context);

        Assertions.assertFalse(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoAfterCompletion_failure() {
        setupMocksForRuleSet();

        sqlAsyncExecuteResp.setStatus(SqlExecuteStatus.FAILURE);
        sqlConsoleInterceptor.doAfterCompletion(getSqlExecuteResult(), getConnectionSession(), context);

        Assertions.assertNull(context.get("allowExport"));
    }

    @Test
    public void testDoAfterCompletion_individualTeam() {
        setupMocksForIndividualTeam();

        sqlConsoleInterceptor.doAfterCompletion(getSqlExecuteResult(), getConnectionSession(), context);

        Assertions.assertTrue(context.get("allowExport"));
    }

    @Test
    public void testDoAfterCompletion_notAllowedEditResultSet() {
        setupMocksForRuleSet_notAllowedEditResultSet();

        sqlConsoleInterceptor.doAfterCompletion(getSqlExecuteResult(), getConnectionSession(), context);

        Assertions.assertFalse(context.get("allowExport"));
    }

    @Test
    public void testDoAfterCompletion_allowedExportResultSet() {
        setupMocksForRuleSet_allowedExportResultSet();

        sqlConsoleInterceptor.doAfterCompletion(getSqlExecuteResult(), getConnectionSession(), context);

        Assertions.assertTrue(context.get("allowExport"));
    }

    private ConnectionSession getConnectionSession() {
        ConnectionSession connectionSession = new ConnectionSession();
        connectionSession.setDialectType(DialectType.MYSQL);
        connectionSession.setOrganizationType(OrganizationType.ORGANIZATION);
        connectionSession.setRuleSetId(1L);
        return connectionSession;
    }

    private SqlExecuteResult getSqlExecuteResult() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setResultSetMetaData(new HashMap<>());
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        return sqlExecuteResult;
    }

    private SqlTuple getSqlTuple() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOffset(0);
        sqlTuple.setOriginalSql("SELECT * FROM table");

        AbstractSyntaxTree ast = new AbstractSyntaxTreeFactories().getAstFactory(DialectType.MYSQL, 0)
                .create(sqlTuple.getOriginalSql());
        sqlTuple.setAst(ast);

        return sqlTuple;
    }

    private void setupMocksForRuleSet() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("max_return_rows", 1000);
        properties.put("max_execute_sqls", 10);

        Mockito.when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MYSQL, Integer.class)).thenReturn(Optional.of(1000));
        Mockito.when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MYSQL, Integer.class)).thenReturn(Optional.of(10));
        Mockito.when(sqlConsoleRuleService.getRuleSetProperties(1L, DialectType.MYSQL)).thenReturn(properties);
    }

    private void setupMocksForRuleSet_forbiddenToCreatePl() {
        setupMocksForRuleSet();

        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, getConnectionSession()))
                .thenReturn(true);
    }

    private void setupMocksForRuleSet_notAllowedEditResultSet() {
        setupMocksForRuleSet();

        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET,
                getConnectionSession())).thenReturn(true);
    }

    private void setupMocksForRuleSet_allowedExportResultSet() {
        setupMocksForRuleSet();

        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET,
                getConnectionSession())).thenReturn(false);
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET,
                getConnectionSession())).thenReturn(false);
    }

    private void setupMocksForIndividualTeam() {
        Mockito.when(authenticationFacade.currentUser()).thenReturn(getUser(OrganizationType.INDIVIDUAL));
    }

    private User getUser() {
        return getUser(OrganizationType.ORGANIZATION);
    }

    private User getUser(OrganizationType organizationType) {
        User user = new User();
        user.setOrganizationType(organizationType);
        return user;
    }
}

class User {

    private OrganizationType organizationType;

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }
}