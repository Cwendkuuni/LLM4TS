package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.Agent;
import glengineer.agents.ComponentAgent;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.Settings;
import glengineer.agents.GapAgent;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.test.mocks.MockComponentAgent;
import glengineer.agents.test.mocks.MockGroupAgent;
import glengineer.agents.test.mocks.MockTemporaryGapAgent;
import glengineer.agents.test.mocks.MockParallelGroupAgent;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private AgentSetter agentSetter;
 private Settings settings;
 private ComponentAgent componentAgent;
 private GroupAgent groupAgentWithComponent;
 private GroupAgent groupAgentWithEmptyList;
 private GroupAgent groupAgentWithTwoAgents;
 private GroupAgent groupAgentWithTwoComponents;
 private GroupAgent groupAgentWithSameFirstNameLastName;
 private GroupAgent groupAgentWithNullFirstNameLastName;
 private GroupAgent groupAgentWithNullChild;
 private GroupAgent groupAgentWithDuplicateChild;
 private GroupAgent groupAgentWithTemporaryGap;
 private ParallelGroupAgent parallelGroupAgent;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 agentSetter = new AgentSetter();
 settings = new Settings();
 componentAgent = new MockComponentAgent();
 groupAgentWithComponent = new MockGroupAgent(componentAgent);
 groupAgentWithEmptyList = new MockGroupAgent(null);
 groupAgentWithTwoAgents = new MockGroupAgent(new MockComponentAgent(), new MockComponentAgent());
 groupAgentWithTwoComponents = new MockGroupAgent(componentAgent, componentAgent);
 groupAgentWithSameFirstNameLastName = new MockGroupAgent("John", "Doe", componentAgent, componentAgent);
 groupAgentWithNullFirstNameLastName = new MockGroupAgent(null, "Doe", componentAgent, componentAgent);
 groupAgentWithNullChild = new MockGroupAgent(this, null);
 groupAgentWithDuplicateChild = new MockGroupAgent(this, componentAgent);
 groupAgentWithTemporaryGap = new MockGroupAgent(this, new MockTemporaryGapAgent(20));
 parallelGroupAgent = new MockParallelGroupAgent(componentAgent, componentAgent);
 }

 @Test
 public void testGetFirstName() {
 assertEquals(componentAgent.getFirstName(), groupAgentWithComponent.getFirstName());
 assertEquals(null, groupAgentWithEmptyList.getFirstName());
 assertEquals(null, groupAgentWithNullFirstNameLastName.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals(componentAgent.getLastName(), groupAgentWithComponent.getLastName());
 assertEquals(null, groupAgentWithEmptyList.getLastName());
 assertEquals("Doe", groupAgentWithNullFirstNameLastName.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("componentName"));
 assertTrue(componentAgent.isComponent("componentName"));
 }

 @Test
 public void testIsGroup() {
 assertFalse(groupAgent.isGroup("firstName", "lastName"));
 assertTrue(groupAgentWithComponent.isGroup("John", "Doe"));
 assertFalse(groupAgentWithEmptyList.isGroup("firstName", "lastName"));
 assertFalse(groupAgentWithNullFirstNameLastName.isGroup(null, "lastName"));
 assertFalse(groupAgentWithNullFirstNameLastName.isGroup("firstName", null));
 assertFalse(groupAgentWithNullFirstNameLastName.isGroup(null, null));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 assertTrue(groupAgentWithComponent.equals(groupAgentWithComponent));
 assertTrue(groupAgentWithComponent.equals(groupAgentWithComponent));
 assertFalse(groupAgentWithComponent.equals(componentAgent));
 assertFalse(groupAgentWithComponent.equals(groupAgentWithEmptyList));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAgentWithNullAgent() {
 groupAgentWithNullChild.addAgent(null);
 }

 @Test
 public void testAddAgent() {
 final List<Agent> children = groupAgent.getChildren();
 groupAgent.addAgent(componentAgent);
 assertEquals(1, children.size());
 assertEquals(componentAgent, children.get(0));
 }

 @Test
 public void testGetChildren() {
 final List<Agent> children = groupAgentWithComponent.getChildren();
 assertEquals(1, children.size());
 assertEquals(componentAgent, children.get(0));
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(0, groupAgentWithEmptyList.getNumberOfChildren());
 assertEquals(1, groupAgentWithComponent.getNumberOfChildren());
 }

 @Test
 public void testFindDependingComponentByName() {
 assertEquals(componentAgent, groupAgentWithComponent.findDependingComponentByName("componentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWithNonExistentComponent() {
 groupAgentWithComponent.findDependingComponentByName("nonExistentComponent");
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertEquals(groupAgentWithComponent, groupAgentWithComponent.findDependingGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWithNonExistentGroup() {
 groupAgentWithComponent.findDependingGroupByNames("NonExistentFirstName", "NonExistentLastName");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertEquals(parallelGroupAgent, groupAgentWithComponent.findDependingParallelGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWithNonExistentParallelGroup() {
 groupAgentWithComponent.findDependingParallelGroupByNames("NonExistentFirstName", "NonExistentLastName");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertEquals(this, groupAgentWithComponent.findDependingSequentialGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWithNonExistentSequentialGroup() {
 groupAgentWithComponent.findDependingSequentialGroupByNames("NonExistentFirstName", "NonExistentLastName");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgentWithComponent.removeGapsFromParallelGroups();
 assertEquals(2, groupAgentWithComponent.getNumberOfChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 final GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
 map.put(groupAgentWithTemporaryGap.getTemporaryGap(), new GapAgent(20));
 groupAgentWithComponent.replaceTemporaryGapsByRealGaps(map);
 assertEquals(2, groupAgentWithComponent.getNumberOfChildren());
 }

 @Test
 public void testAddContentToGroup() {
 final GroupLayoutEngineer groupEngineer = new GroupLayoutEngineer();
 final GroupCreatorAndElementAdder creatorAndAdder = groupEngineer.newGroupCreatorAndElementAdder();
 groupAgent.addContentToGroup(creatorAndAdder.newGroup(), creatorAndAdder);
 }

 @Test
 public void testGroupContent() {
 final GroupLayoutEngineer groupEngineer = new GroupLayoutEngineer();
 final GroupCreatorAndElementAdder creatorAndAdder = groupEngineer.newGroupCreatorAndElementAdder();
 assertEquals(groupAgentWithComponent.groupContent(creatorAndAdder), groupAgentWithComponent.getChildren());
 }

 @Test
 public void testAddAgentWithDuplicateChild() {
 groupAgentWithDuplicateChild.addAgent(componentAgent);
 groupAgentWithDuplicateChild.addAgent(componentAgent);
 assertEquals(1, groupAgentWithDuplicateChild.getNumberOfChildren());
 }
}