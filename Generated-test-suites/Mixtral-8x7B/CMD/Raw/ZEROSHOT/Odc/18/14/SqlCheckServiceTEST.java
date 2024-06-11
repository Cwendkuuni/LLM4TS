package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Spy
    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Test
    public void testCheck_withEmptyScriptContent_returnsEmptyList() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("");

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals(0, result.size());
    }

    @Test
    public void testCheck_withNullScriptContent_returnsEmptyList() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(null);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals(0, result.size());
    }

    @Test
    public void testCheck_withEnabledRules_returnsCheckResultList() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = createDummyRules(10, true);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertFalse(result.isEmpty());
        assertEquals(10, result.size());
    }

    @Test
    public void testCheck_withDisabledRules_returnsEmptyList() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = createDummyRules(10, false);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheck_withNonEmptySqlsList_returnsCheckViolationsList() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = createDummyOffsetStrings(5);
        ConnectionConfig config = new ConnectionConfig();

        Environment env = new Environment();
        env.setRulesetId(2L);
        when(environmentService.detail(environmentId)).thenReturn(env);

        List<Rule> rules = createDummyRules(10, true);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(new SingleConnectionDataSource());

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertFalse(result.isEmpty());
        assertEquals(50, result.size());
    }

    @Test
    public void testGetRules_withConnectionSession_returnsSqlCheckRulesList() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ArgumentMatchers.anyString())).thenReturn(mock(JdbcOperations.class));

        List<Rule> rules = createDummyRules(10, true);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertEquals(10, sqlCheckRules.size());
    }

    @Test
    public void testGetRules_withConnectionConfig_returnsSqlCheckRulesList() {
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);

        List<Rule> rules = createDummyRules(10, true);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);

        assertEquals(10, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel_withViolations_setsRuleLevel() {
        List<Rule> rules = createDummyRules(10, true);
        List<CheckViolation> violations = createDummyCheckViolations(5);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(50, result.size());
        for (Rule rule : result) {
            assertNotNull(rule.getLevel());
        }
    }

    private List<Rule> createDummyRules(int size, boolean enabled) {
        List<Rule> rules = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Rule rule = new Rule();
            rule.setEnabled(enabled);
            RuleMetadata metadata = new RuleMetadata();
            metadata.setType(RuleType.SQL_CHECK);
            metadata.setName("RULE_" + i);
            rule.setMetadata(metadata);
            rules.add(rule);
        }
        return rules;
    }

    private List<OffsetString> createDummyOffsetStrings(int size) {
        List<OffsetString> sqls = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            sqls.add(new OffsetString("SELECT * FROM users;", i));
        }
        return sqls;
    }

    private List<CheckViolation> createDummyCheckViolations(int size) {
        List<CheckViolation> violations = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            CheckViolation violation = new CheckViolation();
            violation.setType(SqlCheckRules.DUMMY_RULE);
            violations.add(violation);
        }
        return violations;
    }
}