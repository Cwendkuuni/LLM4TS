package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
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

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
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

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Validated
@SkipAuthorize("inside connect session")
public class SqlCheckService {

@Autowired
private RuleService ruleService;

@Autowired
private EnvironmentService environmentService;

@InjectMocks
private SqlCheckService sqlCheckService;

@Mock
private RuleService mockRuleService;

@Mock
private EnvironmentService mockEnvironmentService;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testCheck() {
ConnectionSession session = mock(ConnectionSession.class);
SqlCheckReq req = new SqlCheckReq();
when(session.getDialectType()).thenReturn(DialectType.MYSQL);
when(mockRuleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
.thenReturn(Collections.singletonList(getRule()));
List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.singletonList(getRule()), session);
assertNotNull(sqlCheckRules);
assertEquals(1, sqlCheckRules.size());
}

@Test
public void testCheckWithEmptyRuleList() {
ConnectionSession session = mock(ConnectionSession.class);
SqlCheckReq req = new SqlCheckReq();
when(session.getDialectType()).thenReturn(DialectType.MYSQL);
when(mockRuleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
.thenReturn(Collections.emptyList());
List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);
assertNotNull(sqlCheckRules);
assertEquals(0, sqlCheckRules.size());
}

@Test
public void testCheckWithNullRuleList() {
ConnectionSession session = mock(ConnectionSession.class);
SqlCheckReq req = new SqlCheckReq();
when(session.getDialectType()).thenReturn(DialectType.MYSQL);
when(mockRuleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
.thenReturn(null);
List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(null, session);
assertNull(sqlCheckRules);
}

@Test
public void testCheckConnectionConfig() {
Environment env = new Environment();
env.setRulesetId(1L);
ConnectionConfig config = new ConnectionConfig();
config.setDialectType(DialectType.MYSQL);
List<OffsetString> sqls = new ArrayList<>();
sqls.add(new OffsetString("SELECT * FROM TABLE", 0, 10));
when(mockEnvironmentService.detail(eq(1L)))
.thenReturn(env);
when(mockRuleService.list(eq(1L), any(QueryRuleMetadataParams.class)))
.thenReturn(Collections.singletonList(getRule()));
JdbcOperations jdbcOperations = mock(JdbcOperations.class);
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setJdbcOperations(jdbcOperations);
when(new OBConsoleDataSourceFactory(config, true, false)
.getDataSource())
.thenReturn(dataSource);
List<CheckViolation> checkViolations = sqlCheckService.check(1L, "dbname", sqls, config);
assertNotNull(checkViolations);
assertEquals(0, checkViolations.size());
}

@Test
public void testGetRulesWithEmptyRuleList() {
ConnectionSession session = mock(ConnectionSession.class);
List<Rule> rules = Collections.emptyList();
List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
assertNotNull(sqlCheckRules);
assertEquals(0, sqlCheckRules.size());
}

@Test
public void testGetRulesWithNullRuleList() {
ConnectionSession session = mock(ConnectionSession.class);
List<Rule> rules = null;
List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
assertNull(sqlCheckRules);
}

@Test
public void testFullFillRiskLevel() {
List<Rule> rules = Collections.singletonList(getRule());
List<CheckViolation> violations = Collections.singletonList(getCheckViolation());
List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
assertNotNull(violatedRules);
assertEquals(1, violatedRules.size());
assertEquals(RuleViolation.fromCheckViolation(getCheckViolation()),
violatedRules.get(0).getViolation());
}

@Test
public void testFullFillRiskLevelWithEmptyViolations() {
List<Rule> rules = Collections.singletonList(getRule());
List<CheckViolation> violations = Collections.emptyList();
List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
assertNotNull(violatedRules);
assertEquals(0, violatedRules.size());
}

@Test
public void testFullFillRiskLevelWithNullViolations() {
List<Rule> rules = Collections.singletonList(getRule());
List<CheckViolation> violations = null;
List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
assertNull(violatedRules);
}

private Rule getRule() {
Rule rule = new Rule();
rule.setMetadata(getRuleMetadata());
rule.setEnabled(true);
return rule;
}

private RuleMetadata getRuleMetadata() {
RuleMetadata metadata = new RuleMetadata();
metadata.setName("test_rule");
metadata.setType(RuleType.SQL_CHECK);
return metadata;
}

private CheckViolation getCheckViolation() {
CheckViolation violation = new CheckViolation();
violation.setType(getRuleType());
return violation;
}

private RuleType getRuleType() {
RuleType ruleType = new RuleType();
ruleType.setName("test_rule_type");
return ruleType;
}
}