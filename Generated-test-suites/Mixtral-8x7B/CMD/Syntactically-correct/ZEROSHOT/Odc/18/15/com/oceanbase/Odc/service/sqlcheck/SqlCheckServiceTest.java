package com.oceanbase.odc.service.sqlcheck;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@SuppressWarnings("unused")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SkipAuthorize("inside connect session")
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.emptyList());

        sqlCheckService.check(session, req);

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithEnvAndDbName() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();

        Environment env = new Environment();
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("test_rule", RuleType.SQL_CHECK));
        rule.setEnabled(true);
        rule.setLevel("ERROR");
        rules.add(rule);

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        sqlCheckService.check(environmentId, databaseName, sqls, config);

        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(env.getRulesetId(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = mock(ConnectionSession.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        DialectType dialectType = DialectType.MYSQL;

        when(session.getDialectType()).thenReturn(dialectType);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(jdbcOperations);

        sqlCheckService.getRules(rules, session);

        verify(session, times(1)).getDialectType();
        verify(session, times(1)).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbcOperations() {
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);

        sqlCheckService.getRules(rules, dialectType, jdbcOperations);

        verify(jdbcOperations, times(0)).query(anyString(), any(PreparedStatementSetter.class),
                any(ResultSetExtractor.class));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();

        sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        verify(rules, times(0)).forEach(any(Rule::setLevel));
    }
}