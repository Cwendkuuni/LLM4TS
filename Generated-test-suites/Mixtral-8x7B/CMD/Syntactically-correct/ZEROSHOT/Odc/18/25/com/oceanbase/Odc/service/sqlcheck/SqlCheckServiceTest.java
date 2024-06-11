/**
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an AS IS BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

/**
 * {@link SqlCheckServiceTest}
 *
 * @author yh263208
 * @date 2022-11-28 11:01
 * @since ODC_release_4.1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;

    private SqlCheckReq sqlCheckReq;

    @Before
    public void setUp() {
        session = mock(ConnectionSession.class);
        sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM users WHERE id = 1;");
    }

    @Test
    public void testCheck() {
        Long ruleSetId = 1L;
        List<Rule> rules = createRules(ruleSetId);
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules, session);
        when(sqlCheckService.getRules(anyList(), eq(session))).thenReturn(sqlCheckRules);

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(1, checkResults.size());
        assertNotNull(checkResults.get(0).getViolations());
        assertEquals(0, checkResults.get(0).getViolations().size());
    }

    @Test
    public void testCheckEmptySql() {
        sqlCheckReq.setScriptContent("");
        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckEmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckEnvironment() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users WHERE id = 1;", 0, 60));
        ConnectionConfig config = new ConnectionConfig();

        Environment environment = new Environment();
        environment.setRulesetId(1L);

        when(environmentService.detail(eq(environmentId))).thenReturn(environment);

        List<Rule> rules = createRules(environment.getRulesetId());
        when(ruleService.list(eq(environment.getRulesetId()), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(new SingleConnectionDataSource());
        when(factory.resetSchema(any(Function.class))).thenReturn(factory);

        DefaultSqlChecker sqlChecker = mock(DefaultSqlChecker.class);
        when(sqlChecker.check(anyList(), any(SqlCheckContext.class))).thenReturn(Collections.emptyList());

        when(sqlCheckService.getRules(anyList(), eq(config.getDialectType()), any(JdbcOperations.class)))
                .thenReturn(Collections.emptyList());

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertTrue(CollectionUtils.isEmpty(checkViolations));
    }

    private List<SqlCheckRule> createSqlCheckRules(List<Rule> rules, ConnectionSession session) {
        DialectType dialectType = DialectType.MYSQL;
        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(dialectType, new JdbcTemplate(new SingleConnectionDataSource()));
        return rules.stream().filter(rule -> {
            RuleMetadata metadata = rule.getMetadata();
            if (metadata == null || !Boolean.TRUE.equals(rule.getEnabled())) {
                return false;
            }
            return Objects.equals(metadata.getType(), RuleType.SQL_CHECK);
        }).map(rule -> {
            try {
                return SqlCheckRules.createByRule(candidates, dialectType, rule);
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<Rule> createRules(Long ruleSetId) {
        List<Rule> rules = new ArrayList<>();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("type", RuleType.SQL_CHECK);
        rules.add(createRule(ruleSetId, "rule1", metadata));
        rules.add(createRule(ruleSetId, "rule2", metadata));
        return rules;
    }

    private Rule createRule(Long ruleSetId, String name, Map<String, Object> metadata) {
        Rule rule = new Rule();
        rule.setId(1L);
        rule.setRuleSetId(ruleSetId);
        rule.setName(name);
        rule.setEnabled(true);
        rule.setLevel(1);
        RuleMetadata ruleMetadata = new RuleMetadata();
        ruleMetadata.setName(name);
        ruleMetadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(ruleMetadata);
        return rule;
    }
}