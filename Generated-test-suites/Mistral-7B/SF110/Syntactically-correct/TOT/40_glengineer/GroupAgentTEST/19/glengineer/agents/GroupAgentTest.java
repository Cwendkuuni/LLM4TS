package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.test.dummies.DummyAgent;
import glengineer.agents.test.dummies.DummyComponentAgent;
import glengineer.agents.test.dummies.DummyGroupAgent;
import glengineer.agents.test.dummies.DummyParallelGroupAgent;
import glengineer.agents.test.dummies.DummySequentialGroupAgent;
import glengineer.agents.test.dummies.DummyTemporaryGapAgent;
import glengineer.agents.test.dummies.DummyGapAgent;
import glengineer.agents.test.dummies.DummySettings;
import glengineer.exceptions.IllegalArgumentException;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Settings settings;
 private List<Agent> childrenAgents;
 private List<Agent> expectedChildren;

 @Before
 public void setUp() {
 groupAgent = new DummyGroupAgent();
 settings = new DummySettings();
 childrenAgents = new LinkedList<Agent>();
 expectedChildren = new ArrayList<Agent>();
 expectedChildren.add(new DummyComponentAgent());
 expectedChildren.add(new DummyGroupAgent());
 expectedChildren.add(new DummyAgent());
 }

 @Test
 public void testGetFirstName() {
 Agent agent = new DummyComponentAgent();
 childrenAgents.add(agent);
 groupAgent.setChildren(childrenAgents);
 String result = groupAgent.getFirstName();
 assertEquals(agent.getFirstName(), result);
 }

 @Test
 public void testGetLastName() {
 Agent agent = new DummyAgent();
 childrenAgents.add(agent);
 groupAgent.setChildren(childrenAgents);
 String result = groupAgent.getLastName();
 assertEquals(agent.getLastName(), result);
 }

 @Test
 public void testIsComponent() {
 Agent componentAgent = new DummyComponentAgent();
 groupAgent.setChildren(childrenAgents);
 childrenAgents.add(componentAgent);
 assertFalse(groupAgent.isComponent("nonExistentComponentName"));
 assertTrue(componentAgent.isComponent("componentName"));
 }

 @Test
 public void testIsGroup() {
 Agent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 childrenAgents.add(groupAgent);
 assertFalse(groupAgent.isGroup("nonExistentFirstName", "nonExistentLastName"));
 assertTrue(groupAgent.isGroup("firstName", "lastName"));
 }

 @Test
 public void testIsGap() {
 Agent gapAgent = new DummyGapAgent();
 groupAgent.setChildren(childrenAgents);
 childrenAgents.add(gapAgent);
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 Agent agent1 = new DummyGroupAgent();
 Agent agent2 = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 childrenAgents.add(agent1);
 childrenAgents.add(agent2);
 assertFalse(groupAgent.equals(agent2));
 groupAgent.setChildren(Collections.singletonList(agent1));
 assertTrue(groupAgent.equals(agent1));
 }

 @Test
 public void testAddAgent() {
 Agent agentToAdd = new DummyAgent();
 groupAgent.addAgent(agentToAdd);
 assertTrue(groupAgent.getChildren().contains(agentToAdd));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetChildren() {
 Agent agent = new DummyAgent();
 childrenAgents.add(agent);
 groupAgent.setChildren(childrenAgents);
 List<Agent> result = groupAgent.getChildren();
 assertEquals(expectedChildren, result);
 }

 @Test
 public void testGetNumberOfChildren() {
 Agent agent = new DummyAgent();
 childrenAgents.add(agent);
 groupAgent.setChildren(childrenAgents);
 int result = groupAgent.getNumberOfChildren();
 assertEquals(expectedChildren.size(), result);
 }

 @Test
 public void testGetComponent() {
 Agent componentAgent = new DummyComponentAgent();
 childrenAgents.add(componentAgent);
 groupAgent.setChildren(childrenAgents);
 ComponentAgent result = groupAgent.getComponent("componentName");
 assertEquals(componentAgent, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWithNonExistentName() {
 String nonExistentName = "nonExistentName";
 groupAgent.setChildren(childrenAgents);
 try {
 groupAgent.getComponent(nonExistentName);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetGroup() {
 Agent groupAgentToFind = new DummyGroupAgent();
 childrenAgents.add(groupAgentToFind);
 groupAgent.setChildren(childrenAgents);
 GroupAgent result = groupAgent.getGroup("firstName", "lastName");
 assertEquals(groupAgentToFind, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithNonExistentFirstName() {
 String nonExistentFirstName = "nonExistentFirstName";
 Agent groupAgentToFind = new DummyGroupAgent();
 childrenAgents.add(groupAgentToFind);
 groupAgent.setChildren(childrenAgents);
 try {
 groupAgent.getGroup(nonExistentFirstName, "lastName");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithNonExistentLastName() {
 String firstName = "firstName";
 Agent groupAgentToFind = new DummyGroupAgent();
 childrenAgents.add(groupAgentToFind);
 groupAgent.setChildren(childrenAgents);
 try {
 groupAgent.getGroup("firstName", "nonExistentLastName");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFindDependingComponentByName() {
 Agent componentAgent = new DummyComponentAgent();
 childrenAgents.add(componentAgent);
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 ComponentAgent result = groupAgent.findDependingComponentByName("componentName");
 assertEquals(componentAgent, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWithNonExistentName() {
 String nonExistentName = "nonExistentName";
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 try {
 groupAgent.findDependingComponentByName(nonExistentName);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFindDependingGroupByNames() {
 Agent groupAgentToFind = new DummyGroupAgent();
 childrenAgents.add(groupAgentToFind);
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 GroupAgent result = groupAgent.findDependingGroupByNames("firstName", "lastName");
 assertEquals(groupAgentToFind, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWithNonExistentFirstName() {
 String nonExistentFirstName = "nonExistentFirstName";
 Agent groupAgentToFind = new DummyGroupAgent();
 childrenAgents.add(groupAgentToFind);
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 try {
 groupAgent.findDependingGroupByNames(nonExistentFirstName, "lastName");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWithNonExistentLastName() {
 String firstName = "firstName";
 Agent groupAgentToFind = new DummyGroupAgent();
 childrenAgents.add(groupAgentToFind);
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 try {
 groupAgent.findDependingGroupByNames(firstName, "nonExistentLastName");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 ParallelGroupAgent parallelGroupAgentToFind = new DummyParallelGroupAgent();
 childrenAgents.add(parallelGroupAgentToFind);
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 ParallelGroupAgent result = groupAgent.findDependingParallelGroupByNames("firstName", "lastName");
 assertEquals(parallelGroupAgentToFind, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWithNonExistentFirstName() {
 String nonExistentFirstName = "nonExistentFirstName";
 ParallelGroupAgent parallelGroupAgentToFind = new DummyParallelGroupAgent();
 childrenAgents.add(parallelGroupAgentToFind);
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 try {
 groupAgent.findDependingParallelGroupByNames(nonExistentFirstName, "lastName");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWithNonExistentLastName() {
 String firstName = "firstName";
 ParallelGroupAgent parallelGroupAgentToFind = new DummyParallelGroupAgent();
 childrenAgents.add(parallelGroupAgentToFind);
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 try {
 groupAgent.findDependingParallelGroupByNames(firstName, "nonExistentLastName");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 SequentialGroupAgent sequentialGroupAgentToFind = new DummySequentialGroupAgent();
 childrenAgents.add(sequentialGroupAgentToFind);
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 SequentialGroupAgent result = groupAgent.findDependingSequentialGroupByNames("firstName", "lastName");
 assertEquals(sequentialGroupAgentToFind, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWithNonExistentFirstName() {
 String nonExistentFirstName = "nonExistentFirstName";
 SequentialGroupAgent sequentialGroupAgentToFind = new DummySequentialGroupAgent();
 childrenAgents.add(sequentialGroupAgentToFind);
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 try {
 groupAgent.findDependingSequentialGroupByNames(nonExistentFirstName, "lastName");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWithNonExistentLastName() {
 String firstName = "firstName";
 SequentialGroupAgent sequentialGroupAgentToFind = new DummySequentialGroupAgent();
 childrenAgents.add(sequentialGroupAgentToFind);
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.setChildren(childrenAgents);
 try {
 groupAgent.findDependingSequentialGroupByNames(firstName, "nonExistentLastName");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent groupAgent = new DummyGroupAgent();
 groupAgent.removeGapsFromParallelGroups();
 assertTrue(groupAgent.getChildren().isEmpty());
 }

 @Test
 public void testPassTheCallToSubgroups() {
 GroupAgent groupAgent = new DummyGroupAgent();
 GroupAgent subGroupAgent = new DummyGroupAgent();
 groupAgent.addAgent(subGroupAgent);
 groupAgent.removeGapsFromParallelGroups();
 assertTrue(subGroupAgent.getChildren().isEmpty());
 }

 @Test
 public void testRemoveAllGapsFromParallelSubgroups() {
 GroupAgent groupAgent = new DummyGroupAgent();
 ParallelGroupAgent parallelGroupAgent = new DummyParallelGroupAgent();
 groupAgent.addAgent(parallelGroupAgent);
 parallelGroupAgent.addAgent(new DummyComponentAgent());
 parallelGroupAgent.addAgent(new DummyAgent());
 parallelGroupAgent.addAgent(new DummyGapAgent());
 groupAgent.removeAllGaps();
 assertTrue(parallelGroupAgent.getChildren().size() == 2);
 }

 @Test
 public void testReplaceParallelSubgroupsWithGapsByTheirSingleGaps() {
 GroupAgent groupAgent = new DummyGroupAgent();
 ParallelGroupAgent parallelGroupAgent = new DummyParallelGroupAgent();
 parallelGroupAgent.setGapsType(new DummyGapAgent());
 groupAgent.addAgent(parallelGroupAgent);
 groupAgent.replaceParallelSubgroupsWithGapsByTheirSingleGaps();
 assertNotNull(groupAgent.findChildByClass(GapAgent.class));
 }

 @Test
 public void testReplaceContinuousSequencesOfGapsBySingleGaps() {
 GroupAgent groupAgent = new DummyGroupAgent();
 List<Agent> children = new ArrayList<Agent>();
 children.add(new DummyAgent());
 children.add(new DummyGapAgent());
 children.add(new DummyAgent());
 children.add(new DummyGapAgent());
 children.add(new DummyAgent());
 children.add(new DummyAgent());
 children.add(new DummyGapAgent());
 children.add(new DummyAgent());
 children.add(new DummyAgent());
 groupAgent.setChildren(children);
 groupAgent.replaceContinuousSequencesOfGapsBySingleGaps();
 assertTrue(groupAgent.getChildren().size() == 9);
 }

 @Test
 public void testReplaceGroupsWithSingleElementsByThatElements() {
 GroupAgent groupAgent = new DummyGroupAgent();
 GroupAgent singleElementGroupAgent = new DummyGroupAgent();
 singleElementGroupAgent.addAgent(new DummyAgent());
 groupAgent.addAgent(singleElementGroupAgent);
 groupAgent.replaceGroupsWithSingleElementsByThatElements();
 assertTrue(groupAgent.getChildren().size() == 1);
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgent groupAgent = new DummyGroupAgent();
 List<Agent> children = new ArrayList<Agent>();
 children.add(new DummyComponentAgent());
 children.add(new DummyTemporaryGapAgent());
 children.add(new DummyAgent());
 groupAgent.setChildren(children);
 GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
 map.addMapping(DummyTemporaryGapAgent.class, new DummyGapAgent());
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 assertTrue(groupAgent.getChildren().size() == 3);
 }

 @Test
 public void testAddPrecedingGap() {
 GroupAgent groupAgent = new DummyGroupAgent();
 ComponentAgent componentAgent = new DummyComponentAgent();
 groupAgent.addAgent(componentAgent);
 groupAgent.addPrecedingGap(new DummyGapAgent(10), componentAgent);
 assertEquals(11, groupAgent.getNumberOfChildren());
 Agent gapAgent = groupAgent.findChildByClass(GapAgent.class);
 assertEquals(10, gapAgent.getSize());
 }

 @Test
 public void testAddFollowingGap() {
 GroupAgent groupAgent = new DummyGroupAgent();
 ComponentAgent componentAgent = new DummyComponentAgent();
 groupAgent.addAgent(componentAgent);
 groupAgent.addFollowingGap(new DummyGapAgent(10), componentAgent);
 assertEquals(12, groupAgent.getNumberOfChildren());
 Agent gapAgent = groupAgent.findChildByClass(GapAgent.class);
 assertEquals(10, gapAgent.getSize());
 }
}