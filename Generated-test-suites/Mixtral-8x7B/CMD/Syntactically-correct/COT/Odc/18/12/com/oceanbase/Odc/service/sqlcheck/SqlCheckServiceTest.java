import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;
    private SqlCheckReq req;
    private List<Rule> rules;

    @Before
    public void setUp() {
        session = mock(ConnectionSession.class);
        req = mock(SqlCheckReq.class);

        rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata());
        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata());
        rules.add(rule1);
        rules.add(rule2);
    }

    @Test
    public void testCheck_NoRules() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheck_EmptySql() {
        List<Rule> filteredRules = sqlCheckService.getRules(rules, session);
        assertEquals(Collections.emptyList(), filteredRules);
    }

    @Test
    public void testCheck_ValidViolations() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertFalse(sqlCheckRules.isEmpty());

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "DELIMITER", sqlCheckRules);
        String validScriptContent = "SELECT * FROM table;";
        List<CheckViolation> checkViolations = sqlChecker.check(validScriptContent);
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testCheck_InvalidEnvironmentId() {
        Long environmentId = -1L;
        assertThrows(IllegalArgumentException.class, () -> sqlCheckService.check(environmentId, "",
                Collections.emptyList(), mock(ConnectionConfig.class)));
    }

    @Test
    public void testCheck_EmptySqls() {
        Long environmentId = 1L;
        assertThrows(IllegalArgumentException.class,
                () -> sqlCheckService.check(environmentId, "", Collections.emptyList(), mock(ConnectionConfig.class)));
    }

    @Test
    public void testGetRules_EmptyRules() {
        List<Rule> filteredRules = sqlCheckService.getRules(Collections.emptyList(), session);
        assertEquals(Collections.emptyList(), filteredRules);
    }

    @Test
    public void testGetRules_EmptyJdbcOperations() {
        when(session.getSyncJdbcExecutor(anyString())).thenReturn(null);
        List<Rule> filteredRules = sqlCheckService.getRules(rules, session);
        assertEquals(Collections.emptyList(), filteredRules);
    }

    @Test
    public void testFullFillRiskLevel_EmptyViolations() {
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());
        assertEquals(0, violatedRules.size());
    }

    @Test
    public void testFullFillRiskLevel_ValidViolations() {
        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(new CheckViolation());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
        assertEquals(1, violatedRules.size());
    }
}