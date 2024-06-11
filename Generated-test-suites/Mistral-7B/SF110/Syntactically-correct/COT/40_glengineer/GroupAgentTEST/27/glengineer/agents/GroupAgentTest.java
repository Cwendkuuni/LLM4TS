package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.GroupAgent;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.types.ComponentAgent;
import glengineer.agents.types.GapAgent;
import glengineer.agents.types.GroupAgent;
import glengineer.agents.types.ParallelGroupAgent;
import glengineer.agents.types.SequentialGroupAgent;
import glengineer.agents.types.TemporaryGapAgent;
import glengineer.exceptions.IllegalArgumentException;
import glengineer.exceptions.NoSuchElementException;
import glengineer.layout.GroupLayout.Group;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent agent1, agent2, agent3, agent4, agent5, agent6, agent7, agent8, agent9, agent10, gap1, gap2;
 private Settings settings;
 private GroupLayoutEngineer.TemporaryGapsToGaps map;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 settings = new Settings();

 agent1 = new AgentSetter().setFirstName("John").setLastName("Doe").createAgent();
 agent2 = new AgentSetter().setFirstName("Jane").setLastName("Doe").createAgent();
 agent3 = new AgentSetter().setFirstName("Bob").setLastName("Smith").createAgent();
 agent4 = new AgentSetter().setFirstName("Alice").setLastName("Williams").createAgent();
 agent5 = new AgentSetter().setFirstName("Mike").setLastName("Johnson").createAgent();
 agent6 = new AgentSetter().setFirstName("Eve").setLastName("Miller").createAgent();
 agent7 = new AgentSetter().setFirstName("Tom").setLastName("Anderson").createAgent();
 agent8 = new AgentSetter().setFirstName("Mary").setLastName("Davis").createAgent();
 agent9 = new AgentSetter().setFirstName("David").setLastName("Brown").createAgent();
 agent10 = new AgentSetter().setFirstName("Sara").setLastName("Garcia").createAgent();
 gap1 = new GapAgent();
 gap2 = new GapAgent();

 groupAgent.addAgent(agent1);
 groupAgent.addAgent(agent2);
 groupAgent.addAgent(agent3);
 groupAgent.addAgent(agent4);
 groupAgent.addAgent(agent5);
 groupAgent.addAgent(agent6);
 groupAgent.addAgent(agent7);
 groupAgent.addAgent(agent8);
 groupAgent.addAgent(agent9);
 groupAgent.addAgent(agent10);
 }

 @Test
 public void testGetFirstName() {
 assertEquals("John", groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("Doe", groupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("componentName"));
 }

 @Test
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup("Doe", "John"));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent agent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 agent.addAgent(agent1);
 agent.addAgent(agent2);
 assertTrue(groupAgent.equals(agent));
 }

 @Test
 public void testAddAgent() {
 assertThrows(IllegalArgumentException.class, () -> {
 groupAgent.addAgent(null);
 });

 groupAgent.addAgent(agent1);
 List<Agent> children = groupAgent.getChildren();
 assertEquals("John Doe", children.get(0).getFullName());
 assertEquals("Jane Doe", children.get(1).getFullName());
 assertEquals("Bob Smith", children.get(2).getFullName());
 assertEquals("Alice Williams", children.get(3).getFullName());
 assertEquals("Mike Johnson", children.get(4).getFullName());
 assertEquals("Eve Miller", children.get(5).getFullName());
 assertEquals("Tom Anderson", children.get(6).getFullName());
 assertEquals("Mary Davis", children.get(7).getFullName());
 assertEquals("David Brown", children.get(8).getFullName());
 assertEquals("Sara Garcia", children.get(9).getFullName());
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(10, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(10, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertEquals(agent1, groupAgent.getComponent("componentName"));
 }

 @Test
 public void testGetGroup() {
 GroupAgent group = (GroupAgent) agent1;
 groupAgent.addAgent(group);
 assertEquals(group1, groupAgent.getGroup("Doe", "John"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNotFound() {
 groupAgent.getGroup("notFoundName", "notFoundLastName");
 }

 @Test
 public void testFindDependingComponentByName() {
 assertEquals(agent1, groupAgent.findDependingComponentByName("componentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameNotFound() {
 groupAgent.findDependingComponentByName("notFoundName");
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent group = (GroupAgent) agent1;
 groupAgent.addAgent(group);
 assertEquals(group1, groupAgent.findDependingGroupByNames("Doe", "John"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNotFound() {
 groupAgent.findDependingGroupByNames("notFoundName1", "notFoundName2");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 ParallelGroupAgent parallelGroup = (ParallelGroupAgent) agent1;
 groupAgent.addAgent(parallelGroup);
 assertEquals(parallelGroup1, groupAgent.findDependingParallelGroupByNames("Doe", "John"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesNotFound() {
 groupAgent.findDependingParallelGroupByNames("notFoundName1", "notFoundName2");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 SequentialGroupAgent sequentialGroup = (SequentialGroupAgent) agent1;
 groupAgent.addAgent(sequentialGroup);
 assertEquals(sequentialGroup1, groupAgent.findDependingSequentialGroupByNames("Doe", "John"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesNotFound() {
 groupAgent.findDependingSequentialGroupByNames("notFoundName1", "notFoundName2");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.removeGapsFromParallelGroups();
 }

 @Test
 public void testPassTheCallToSubgroups() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 List<Agent> children = new LinkedList<>();
 groupAgent.addAgent(children);
 GroupAgent childAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 children.add(childAgent);
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(0, childAgent.getNumberOfChildren());
 }

 @Test
 public void testRemoveAllGapsFromParallelSubgroups() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 ParallelGroupAgent parallelGroup = new ParallelGroupAgent();
 GroupAgent group1 = new GroupAgent();
 group1.addAgent(gap1);
 parallelGroup.addAgent(group1);
 groupAgent.addAgent(parallelGroup);
 parallelGroup.removeAllGaps();
 assertEquals(0, parallelGroup.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReplaceParallelSubgroupsWithGapsByTheirSingleGaps_invalidType() {
 groupAgent.replaceParallelSubgroupsWithGapsByTheirSingleGaps();
 }

 @Test
 public void testReplaceParallelSubgroupsWithGapsByTheirSingleGaps() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 ParallelGroupAgent parallelGroup1 = new ParallelGroupAgent();
 parallelGroup1.addAgent(gap1);
 GroupAgent group1 = new GroupAgent();
 group1.addAgent(agent1);
 groupAgent.addAgent(parallelGroup1);
 groupAgent.addAgent(group1);
 parallelGroup1.replaceParallelSubgroupsWithGapsByTheirSingleGaps();
 assertEquals("John Doe", parallelGroup1.getChildren().get(0).getFullName());
 }

 @Test
 public void testReplaceContinuousSequencesOfGapsBySingleGaps() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent.addAgent(agent1);
 groupAgent.addAgent(gap1);
 groupAgent.addAgent(agent2);
 groupAgent.addAgent(gap1);
 groupAgent.addAgent(agent3);
 groupAgent.replaceContinuousSequencesOfGapsBySingleGaps();
 assertEquals("John Doe", groupAgent.getChildren().get(0).getFullName());
 assertEquals("Mike Johnson", groupAgent.getChildren().get(2).getFullName());
 }

 @Test
 public void testReplaceGroupsWithSingleElementsByThatElements() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 GroupAgent group1 = new GroupAgent();
 group1.addAgent(agent1);
 groupAgent.addAgent(group1);
 groupAgent.addAgent(agent2);
 groupAgent.replaceGroupsWithSingleElementsByThatElements();
 assertEquals("John Doe", groupAgent.getChildren().get(0).getFullName());
 assertEquals("Mike Johnson", groupAgent.getChildren().get(2).getFullName());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent.addAgent(agent1);
 groupAgent.addAgent(agent2);
 groupAgent.addAgent(agent3);
 map = new GroupLayoutEngineer.TemporaryGapsToGaps();
 map.put((TemporaryGapAgent) agent2, gap1);
 map.put((TemporaryGapAgent) agent3, gap2);
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 assertEquals("John Doe", groupAgent.getChildren().get(0).getFullName());
 assertEquals("GapAgent", groupAgent.getChildren().get(1).getClass().getSimpleName());
 assertEquals("GapAgent", groupAgent.getChildren().get(2).getClass().getSimpleName());
 }

 @Test
 public void testAddPrecedingGap() {
 FunctionsOnGroupAndElementImplementation functions = new FunctionsOnGroupAndElementImplementation(groupAgent, agent1);
 functions.addPrecedingGap(10);
 assertEquals(11, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testAddPrecedingGapWithMinPrefMax() {
 FunctionsOnGroupAndElementImplementation functions = new FunctionsOnGroupAndElementImplementation(groupAgent, agent1);
 functions.addPrecedingGap(5, 5, 10);
 assertEquals(12, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testAddFollowingGap() {
 FunctionsOnGroupAndElementImplementation functions = new FunctionsOnGroupAndElementImplementation(groupAgent, agent10);
 functions.addFollowingGap(10);
 assertEquals(11, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testAddFollowingGapWithMinPrefMax() {
 FunctionsOnGroupAndElementImplementation functions = new FunctionsOnGroupAndElementImplementation(groupAgent, agent10);
 functions.addFollowingGap(5, 5, 10);
 assertEquals(12, groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPrecedingGapWithInvalidAgent() {
 FunctionsOnGroupAndElementImplementation functions = new FunctionsOnGroupAndElementImplementation(groupAgent, null);
 functions.addPrecedingGap(5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddFollowingGapWithInvalidAgent() {
 FunctionsOnGroupAndElementImplementation functions = new FunctionsOnGroupAndElementImplementation(groupAgent, null);
 functions.addFollowingGap(5);
 }

 @Test(expected = NoSuchElementException.class)
 public void testAddPrecedingGapWithNotFoundAgent() {
 FunctionsOnGroupAndElementImplementation functions = new FunctionsOnGroupAndElementImplementation(groupAgent, null);
 functions.addPrecedingGap(5, agent11, agent1);
 }

 @Test(expected = NoSuchElementException.class)
 public void testAddFollowingGapWithNotFoundAgent() {
 FunctionsOnGroupAndElementImplementation functions = new FunctionsOnGroupAndElementImplementation(groupAgent, null);
 functions.addFollowingGap(5, agent1, agent11);
 }
}