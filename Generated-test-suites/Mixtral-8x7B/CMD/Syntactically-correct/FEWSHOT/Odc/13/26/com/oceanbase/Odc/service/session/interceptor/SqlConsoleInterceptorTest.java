package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
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

@Component
public class SqlConsoleInterceptorTest {

    private static final String TEST_SQL1 = "SELECT * FROM table1";
    private static final String TEST_SQL2 = "INSERT INTO table1 VALUES (1, 'test')";
    private static final String TEST_SQL3 = "CREATE PL FUNCTION myFunc() RETURNS INT BEGIN RETURN 1; END";

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private ConnectionSession session;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private Map<String, Object> context;

    @Before
    public void setup() {
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.authenticationFacade = authenticationFacade;
        sqlConsoleInterceptor.ruleService = ruleService;
        sqlConsoleInterceptor.sqlConsoleRuleService = sqlConsoleRuleService;

        session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        request = new SqlAsyncExecuteReq();
        request.setSqls(new ArrayList<>());
        request.getSqls().add(new SqlTuplesWithViolation(TEST_SQL1, null));
        request.getSqls().add(new SqlTuplesWithViolation(TEST_SQL2, null));

        response = new SqlAsyncExecuteResp();
        response.setSqls(request.getSqls());

        context = new HashMap<>();

        AbstractSyntaxTreeFactories.init(session.getDialectType());
    }

    @Test
    public void testDoPreHandle_allowed() {
        when(authenticationFacade.currentUser()).thenReturn(new User("testUser", OrganizationType.INDIVIDUAL));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, session.getDialectType(),
                Integer.class)).thenReturn(Optional.of(100));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(new Rule()));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(false);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_disallowed() {
        when(authenticationFacade.currentUser()).thenReturn(new User("testUser", OrganizationType.INDIVIDUAL));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, session.getDialectType(),
                Integer.class)).thenReturn(Optional.of(100));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(new Rule()));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(true);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion_allowed() {
        when(authenticationFacade.currentUser()).thenReturn(new User("testUser", OrganizationType.INDIVIDUAL));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(false);

        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setResultSetMetaData(new ResultSetMetaData(true, false));

        sqlConsoleInterceptor.doAfterCompletion(result, session, context);

        assertTrue(result.getResultSetMetaData().isEditable());
    }

    @Test
    public void testDoAfterCompletion_disallowed() {
        when(authenticationFacade.currentUser()).thenReturn(new User("testUser", OrganizationType.INDIVIDUAL));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(true);

        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setResultSetMetaData(new ResultSetMetaData(true, false));

        sqlConsoleInterceptor.doAfterCompletion(result, session, context);

        assertFalse(result.getResultSetMetaData().isEditable());
    }

    @Test
    public void testIsIndividualTeam() {
        when(authenticationFacade.currentUser()).thenReturn(new User("testUser", OrganizationType.INDIVIDUAL));

        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType_valid() throws SyntaxErrorException {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(TEST_SQL1, session.getDialectType());

        assertEquals(SqlType.SELECT, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_syntaxError() throws SyntaxErrorException {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        BasicResult result = sqlConsoleInterceptor.determineSqlType("SELECT * FROM ta", session.getDialectType());

        assertTrue(result.getSyntaxError());
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

}

class User {
    private String userName;
    private OrganizationType organizationType;

    public User(String userName, OrganizationType organizationType) {
        this.userName = userName;
        this.organizationType = organizationType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }

}

class ResultSetMetaData {
    private boolean editable;
    private boolean scrollable;

    public ResultSetMetaData(boolean editable, boolean scrollable) {
        this.editable = editable;
        this.scrollable = scrollable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

}