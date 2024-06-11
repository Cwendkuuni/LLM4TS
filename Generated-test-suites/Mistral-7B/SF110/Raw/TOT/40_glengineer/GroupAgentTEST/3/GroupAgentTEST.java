import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.GroupAgentTestUtils;
import glengineer.agents.Agent;
import glengineer.agents.ComponentAgent;
import glengineer.agents.GroupAgent;
import glengineer.agents.ParallelGroupAgent;
import glengineer.agents.SequentialGroupAgent;
import glengineer.agents.GapAgent;
import glengineer.agents.TemporaryGapAgent;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent testAgent1;
 private Agent testAgent2;
 private Agent testAgent3;
 private Agent testComponentAgent;
 private GroupAgent testGroupAgent1;
 private GroupAgent testGroupAgent2;
 private ParallelGroupAgent testParallelGroupAgent;
 private SequentialGroupAgent testSequentialGroupAgent;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return GroupAgentTestUtils.createTestGroupContent();
 }

 @Override
 FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };

 testAgent1 = new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };

 testAgent2 = new Agent() {
 @Override
 public String getFirstName() {
 return "Jane";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };

 testAgent3 = new Agent() {
 @Override
 public String getFirstName() {
 return null;
 }

 @Override
 public String getLastName() {
 return null;
 }
 };

 testComponentAgent = new ComponentAgent() {
 @Override
 public String getName() {
 return "testComponent";
 }
 };

 testGroupAgent1 = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return GroupAgentTestUtils.createTestGroupContent();
 }

 @Override
 FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testGroupAgent1.addAgent(testAgent1);
 testGroupAgent1.addAgent(testAgent2);

 testGroupAgent2 = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return GroupAgentTestUtils.createTestGroupContent();
 }

 @Override
 FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testGroupAgent2.addAgent(testAgent2);
 testGroupAgent2.addAgent(testAgent3);

 testParallelGroupAgent = new ParallelGroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return GroupAgentTestUtils.createTestGroupContent();
 }

 @Override
 FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testParallelGroupAgent.addAgent(testAgent1);
 testParallelGroupAgent.addAgent(testAgent2);

 testSequentialGroupAgent = new SequentialGroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return GroupAgentTestUtils.createTestGroupContent();
 }

 @Override
 FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testSequentialGroupAgent.addAgent(testAgent1);
 testSequentialGroupAgent.addAgent(testAgent2);

 groupAgent.addAgent(testGroupAgent1);
 groupAgent.addAgent(testGroupAgent2);
 groupAgent.addAgent(testParallelGroupAgent);
 groupAgent.addAgent(testSequentialGroupAgent);
 }

 @Test
 public void testGetFirstName() {
 assertEquals("John", groupAgent.getChildren().get(0).getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("Doe", groupAgent.getChildren().get(0).getLastName());
 }

 @Test
 public void testGetFirstNameEmptyList() {
 assertNull(groupAgent.getChildren().get(0).getFirstName());
 }

 @Test
 public void testGetLastNameEmptyList() {
 assertNull(groupAgent.getChildren().get(0).getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("testComponent"));
 }

 @Test
 public void testIsGroup() {
 assertFalse(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGroupSameInstance() {
 assertTrue(groupAgent.isGroup(testGroupAgent1.getFirstName(), testGroupAgent1.getLastName()));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGroup(testGroupAgent1.getFirstName(), testGroupAgent1.getLastName()));
 }

 @Test
 public void testEquals() {
 Agent agent = new Agent() {
 @Override
 public String getFirstName() {
 return testGroupAgent1.getFirstName();
 }

 @Override
 public String getLastName() {
 return testGroupAgent1.getLastName();
 }
 };
 assertTrue(groupAgent.equals(testGroupAgent1));
 assertFalse(groupAgent.equals(testAgent1));
 }

 @Test
 public void testAddAgent() {
 Agent agent = new Agent() {
 @Override
 public String getFirstName() {
 return "test";
 }

 @Override
 public String getLastName() {
 return "agent";
 }
 };
 groupAgent.addAgent(agent);
 assertEquals(4, groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(4, children.size());
 assertEquals(testGroupAgent1, children.get(0));
 assertEquals(testGroupAgent2, children.get(1));
 assertEquals(testParallelGroupAgent, children.get(2));
 assertEquals(testSequentialGroupAgent, children.get(3));
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(4, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testFindDependingComponentByName() {
 assertEquals(testComponentAgent, groupAgent.findDependingComponentByName("testComponent"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameNotFound() {
 groupAgent.findDependingComponentByName("nonExistentComponent");
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertEquals(testGroupAgent1, groupAgent.findDependingGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNotFound() {
 groupAgent.findDependingGroupByNames("nonExistentFirstName", "nonExistentLastName");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertEquals(testParallelGroupAgent, groupAgent.findDependingParallelGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesNotFound() {
 groupAgent.findDependingParallelGroupByNames("nonExistentFirstName", "nonExistentLastName");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertEquals(testSequentialGroupAgent, groupAgent.findDependingSequentialGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesNotFound() {
 groupAgent.findDependingSequentialGroupByNames("nonExistentFirstName", "nonExistentLastName");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.removeGapsFromParallelGroups();
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
 map.put(testParallelGroupAgent.getGapsType(), new GapAgent(100));
 map.put(testSequentialGroupAgent.getGapsType(), new GapAgent(150));
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 }

 @Test
 public void testAddContentToGroup() {
 GroupAgent testParentGroupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return GroupAgentTestUtils.createTestParentGroupContent();
 }

 @Override
 FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent.addContentToGroup(testParentGroupAgent, new GroupCreatorAndElementAdder() {
 @Override
 public void addGroupToGroup(GroupLayout.Group parent, GroupLayout.Group child) {
 // no-op
 }

 @Override
 public void addElementToGroup(GroupLayout.Group parent, Component component) {
 // no-op
 }
 });
 }

 @Test
 public void testAddGapBefore() {
 GapAgent gap = new GapAgent(50);
 groupAgent.addAgent(testAgent1);
 groupAgent.addGapBefore(gap, testAgent1);
 assertEquals(2, groupAgent.getChildren().size());
 assertEquals(testAgent1, groupAgent.getChildren().get(0));
 assertEquals(gap, groupAgent.getChildren().get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapBeforeAgentNotFound() {
 groupAgent.addAgent(testAgent1);
 groupAgent.addGapBefore(new GapAgent(50), testAgent2);
 }

 @Test
 public void testAddGapAfter() {
 GapAgent gap = new GapAgent(50);
 groupAgent.addAgent(testAgent1);
 groupAgent.addAgent(testAgent2);
 groupAgent.addGapAfter(gap, testAgent1);
 assertEquals(3, groupAgent.getChildren().size());
 assertEquals(testAgent1, groupAgent.getChildren().get(0));
 assertEquals(gap, groupAgent.getChildren().get(1));
 assertEquals(testAgent2, groupAgent.getChildren().get(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapAfterAgentNotFound() {
 groupAgent.addAgent(testAgent1);
 groupAgent.addAgent(testAgent2);
 groupAgent.addGapAfter(new GapAgent(50), testAgent3);
 }
}