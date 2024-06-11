import com.oceanbase.odc.core.sql.parse.AstFactory;
import com.oceanbase.odc.core.sql.parse.AstFactoryImpl;
import com.oceanbase.odc.core.sql.parse.ParseResult;
import com.oceanbase.odc.core.sql.parse.SyntaxErrorException;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.SqlType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.OrganizationType;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.ConnectionSession;
import com.oceanbase.odc.service.session.model.ConnectionSessionUtil;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuple;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private ConnectionSessionUtil connectionSessionUtil;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Mock
    private RuleService ruleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AstFactory astFactory = new AstFactoryImpl();
        when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(authenticationFacade.currentUser()).thenReturn(createUser());
    }

    @Test
    public void testDoPreHandleAllowExecute() {
        when(sqlConsoleRuleService.getProperties(any(), any(), any()))
                .thenReturn(Optional.of(0));
        when(sqlConsoleRuleService.getListProperties(any(), any(), any(), any()))
                .thenReturn(Optional.of(Collections.singletonList("SELECT")));

        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        sqlAsyncExecuteReq.setSqls(Collections.singletonList(createSqlTuple("SELECT * FROM users")));

        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(Collections.singletonList(new SqlTuplesWithViolation(createSqlTuple("SELECT * FROM users"),
                Collections.emptyList())));

        Map<String, Object> context = new HashMap<>();

        assertTrue(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));
        assertTrue((boolean) context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandleForbidCreatePl() {
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(true);

        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        sqlAsyncExecuteReq.setSqls(Collections.singletonList(createSqlTuple("CREATE PROCEDURE test()")));

        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(Collections.singletonList(new SqlTuplesWithViolation(createSqlTuple("CREATE PROCEDURE test()"),
                Collections.singletonList(createRule("NOT_ALLOWED_CREATE_PL")))));

        Map<String, Object> context = new HashMap<>();

        assertFalse(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));
        assertTrue((boolean) context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandleForbidExport() {
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(true);

        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        sqlAsyncExecuteReq.setSqls(Collections.singletonList(createSqlTuple("SELECT * FROM users")));

        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(Collections.singletonList(new SqlTuplesWithViolation(createSqlTuple("SELECT * FROM users"),
                Collections.emptyList())));

        Map<String, Object> context = new HashMap<>();

        assertTrue(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));
        assertFalse((boolean) context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoAfterCompletionAllowExport() {
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setResultSetMetaData(new SqlExecuteResult.ResultSetMetaData(true));

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletionForbidExport() {
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(true);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setResultSetMetaData(new SqlExecuteResult.ResultSetMetaData(true));

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionConnectionSession, new HashMap<>());

        assertFalse(sqlExecuteResult.isAllowExport());
    }

    private User createUser() {
        User user = new User();
        user.setOrganizationType(OrganizationType.INDIVIDUAL);
        return user;
    }

    private SqlTuple createSqlTuple(String sql) {
        return new SqlTuple(sql, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    private Rule createRule(String ruleName) {
        RuleViolation violation = new RuleViolation();
        violation.setLevel("ERROR");
        violation.setLocalizedMessage("Rule " + ruleName + " is forbidden.");

        Rule rule = new Rule();
        rule.setName(ruleName);
        rule.setViolation(violation);

        return rule;
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

class SqlTuplesWithViolation {
    private SqlTuple sqlTuple;
    private List<RuleViolation> violatedRules;

    public SqlTuplesWithViolation(SqlTuple sqlTuple, List<RuleViolation> violatedRules) {
        this.sqlTuple = sqlTuple;
        this.violatedRules = violatedRules;
    }

    public SqlTuple getSqlTuple() {
        return sqlTuple;
    }

    public List<RuleViolation> getViolatedRules() {
        return violatedRules;
    }
}

class RuleViolation {
    private String level;
    private String localizedMessage;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }
}

class Rule {
    private String name;
    private RuleViolation violation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RuleViolation getViolation() {
        return violation;
    }

    public void setViolation(RuleViolation violation) {
        this.violation = violation;
    }
}

class ParseResult {
    private SqlType sqlType;
    private boolean syntaxError;

    public ParseResult(SqlType sqlType, boolean syntaxError) {
        this.sqlType = sqlType;
        this.syntaxError = syntaxError;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public void setSqlType(SqlType sqlType) {
        this.sqlType = sqlType;
    }

    public boolean getSyntaxError() {
        return syntaxError;
    }

    public void setSyntaxError(boolean syntaxError) {
        this.syntaxError = syntaxError;
    }
}

class AbstractSyntaxTree {
    public ParseResult getParseResult() throws SyntaxErrorException {
        return null;
    }
}

class AstFactory {
    public AbstractSyntaxTree getAstFactory(DialectType dialectType, int i) {
        return null;
    }
}

enum DialectType {
    MYSQL,
    POSTGRESQL
}

enum SqlType {
    SELECT,
    INSERT,
    UPDATE,
    DELETE,
    CREATE,
    ALTER,
    DROP,
    UNKNOWN,
    OTHERS
}

class BaseTimeConsumingInterceptor {
    protected String getExecuteStageName() {
        return null;
    }

    public int getOrder() {
        return 0;
    }
}