package glengineer.agents.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.Agent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GapAgent;
import glengineer.agents.TemporaryGapAgent;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent agent1;
 private Agent agent2;
 private Agent agent3;
 private Agent agentGap;
 private Settings settings;
 private List<Agent> expectedChildren;

 @Before
 public void setup() {
 groupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 throw new UnsupportedOperationException();
 }
 };
 agent1 = new AgentSetter().createAgentWithFirstName("John").createAgentWithLastName("Doe");
 agent2 = new AgentSetter().createAgentWithFirstName("Jane").createAgentWithLastName("Doe");
 agent3 = new AgentSetter().createAgentWithFirstName("Bob").createAgentWithLastName("Smith");
 agentGap = new GapAgent(1);
 settings = new Settings();
 }

 @Test
 public void testGetFirstName() {
 assertEquals(agent1.getFirstName(), groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals(agent1.getLastName(), groupAgent.getLastName());
 }

 @Test
 public void testIsGroupWithCorrectNames() {
 assertTrue(groupAgent.isGroup(agent1.getFirstName(), agent1.getLastName()));
 }

 @Test
 public void testIsGroupWithIncorrectNames() {
 assertFalse(groupAgent.isGroup("John", "Smith"));
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(agent1);
 groupAgent.addAgent(agent2);
 List<Agent> actualChildren = groupAgent.getChildren();
 expectedChildren = new ArrayList<>();
 expectedChildren.add(agent1);
 expectedChildren.add(agent2);
 assertEquals(expectedChildren, actualChildren);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(groupAgent.getComponent("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithNonExistingFirstNameLastName() {
 groupAgent.getGroup("John", "Smith");
 }

 @Test
 public void testGetGroupWithExistingFirstNameLastName() {
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 throw new UnsupportedOperationException();
 }
 };
 agent1 = new AgentSetter().createAgentWithFirstName("John").createAgentWithLastName("Doe");
 agent2 = new AgentSetter().createAgentWithFirstName("Jane").createAgentWithLastName("Doe");
 groupAgent2.addAgent(agent1);
 groupAgent2.addAgent(agent2);
 assertEquals(agent1, groupAgent2.getGroup("John", "Doe"));
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent groupAgent3 = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 throw new UnsupportedOperationException();
 }
 };
 agent1 = new AgentSetter().createAgentWithFirstName("John").createAgentWithLastName("Doe");
 agent2 = new AgentSetter().createAgentWithFirstName("Jane").createAgentWithLastName("Doe");groupAgent3.addAgent(agent1); groupAgent3.addAgent(agent2); GroupAgent groupAgent4 = new GroupAgent() { @Override public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) { throw new UnsupportedOperationException(); } }; agent3 = new AgentSetter().createAgentWithFirstName("Bob").createAgentWithLastName("Smith"); groupAgent4.addAgent(agent3); groupAgent3.addAgent(groupAgent4); assertEquals(groupAgent4, groupAgent3.findDependingGroupByNames("Bob", "Smith")); }

 @Test public void testFindDependingParallelGroupByNames() { GroupAgent groupAgent5 = new GroupAgent() { @Override public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) { throw new UnsupportedOperationException(); } }; GroupAgent parallelGroupAgent1 = new GroupAgent() { @Override public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) { throw new UnsupportedOperationException(); } }; GroupAgent parallelGroupAgent2 = new GroupAgent() { @Override public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) { throw new UnsupportedOperationException(); } }; parallelGroupAgent1.addAgent(parallelGroupAgent2); groupAgent5.addAgent(parallelGroupAgent1); assertEquals(parallelGroupAgent1, groupAgent5.findDependingParallelGroupByNames("ParallelGroupName1", "ParallelGroupName2")); }

 @Test public void testFindDependingSequentialGroupByNames() { GroupAgent groupAgent6 = new GroupAgent() { @Override public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) { throw new UnsupportedOperationException(); } }; GroupAgent sequentialGroupAgent1 = new GroupAgent() { @Override public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) { throw new UnsupportedOperationException(); } }; GroupAgent sequentialGroupAgent2 = new GroupAgent() { @Override public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) { throw new UnsupportedOperationException(); } }; sequentialGroupAgent1.addAgent(sequentialGroupAgent2); groupAgent6.addAgent(sequentialGroupAgent1); assertEquals(sequentialGroupAgent1, groupAgent6.findDependingSequentialGroupByNames("SequentialGroupName1", "SequentialGroupName2")); }

 @Test public void testRemoveGapsFromParallelGroups() { groupAgent.addAgent(agent1); groupAgent.addAgent(agent2); groupAgent.addAgent(agent3); groupAgent.addAgent(agentGap); groupAgent.removeGapsFromParallelGroups(); List<Agent> actualChildren = groupAgent.getChildren(); expectedChildren = new ArrayList<>(); expectedChildren.add(agent1); expectedChildren.add(agent2); expectedChildren.add(agent3); assertEquals(expectedChildren, actualChildren); }

 @Test public void testReplaceTemporaryGapsByRealGaps() { GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps(); map.put((TemporaryGapAgent)agentGap, new GapAgent(1)); groupAgent.replaceTemporaryGapsByRealGaps(map); List<Agent> actualChildren = groupAgent.getChildren(); expectedChildren = new ArrayList<>(); expectedChildren.add(agent1); expectedChildren.add(agent2); expectedChildren.add(agentGap); expectedChildren.add(agent3); assertEquals(expectedChildren, actualChildren); }

 @Test public void testAddGapBefore() { groupAgent.addAgent(agent1); groupAgent.addAgent(agent2); groupAgent.addAgent(agentGap); groupAgent.addGapBefore(agentGap, agent1); List<Agent> actualChildren = groupAgent.getChildren(); expectedChildren = new ArrayList<>(); expectedChildren.add(agentGap); expectedChildren.add(agent1); expectedChildren.add(agent2); assertEquals(expectedChildren, actualChildren); }

 @Test public void testAddGapAfter() { groupAgent.addAgent(agent1); groupAgent.addAgent(agent2); groupAgent.addAgent(agentGap); groupAgent.addGapAfter(agentGap, agent2); List<Agent> actualChildren = groupAgent.getChildren(); expectedChildren = new ArrayList<>(); expectedChildren.add(agent1); expectedChildren.add(agent2); expectedChildren.add(agentGap); assertEquals(expectedChildren, actualChildren); }

 @Test public void testFunctionsOnGroup() { FunctionsOnGroupAndElement functions = new GroupAgent.FunctionsOnGroupAndElementImplementation(groupAgent, agent1); functions.addPrecedingGap(1); FunctionsOnGroupAndElement functions2 = new GroupAgent.FunctionsOnGroupAndElementImplementation(groupAgent, agent2); functions2.addFollowingGap(2); assertEquals(3, groupAgent.getNumberOfChildren()); List<Agent> actualChildren = groupAgent.getChildren(); Agent expectedFirstChild = agent1; expectedChildren.add(0, new GapAgent(1)); expectedChildren.set(1, expectedFirstChild); expectedChildren.add(agent2); expectedChildren.add(new GapAgent(2)); assertEquals(expectedChildren, actualChildren); }
}