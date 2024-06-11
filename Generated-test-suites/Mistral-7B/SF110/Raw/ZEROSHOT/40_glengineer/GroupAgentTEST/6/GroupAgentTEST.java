package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.AgentSetterFactory;
import glengineer.agents.setters.GroupAgentSetter;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.setters.TemporaryGapAgent;
import glengineer.agents.test.mocks.MockComponentAgent;
import glengineer.agents.test.mocks.MockGroupAgent;
import glengineer.agents.test.mocks.MockTemporaryGapAgent;
import glengineer.exceptions.IllegalArgumentException;
import glengineer.exceptions.IllegalArgumentExceptionWithMessage;
import glengineer.exceptions.IllegalStateException;
import glengineer.layout.GroupLayout;
import glengineer.layout.GroupLayout.Group;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private GroupAgent subGroupAgent;
 private ComponentAgent componentAgent;
 private TemporaryGapAgent gapAgent;

 @Before
 public void setUp() {
 groupAgent = new MockGroupAgent();
 subGroupAgent = new MockGroupAgent();
 componentAgent = new MockComponentAgent();
 gapAgent = new MockTemporaryGapAgent();
 }

 @Test
 public void testGetFirstName() {
 // Test when there is no child agent with first name
 assertNull(groupAgent.getFirstName());

 // Test when there is a child agent with first name
 AgentSetter agentSetter = AgentSetterFactory.getInstance();
 GroupAgentSetter groupAgentSetter = (GroupAgentSetter)agentSetter;
 groupAgentSetter.setFirstName(subGroupAgent, "John");
 groupAgent.addAgent(subGroupAgent);
 assertEquals("John", groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 // Test when there is no child agent with last name
 assertNull(groupAgent.getLastName());

 // Test when there is a child agent with last name
 groupAgentSetter.setLastName(subGroupAgent, "Doe");
 groupAgent.addAgent(subGroupAgent);
 assertEquals("Doe", groupAgent.getLastName());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsComponentWithInvalidComponentName() {
 groupAgent.isComponent("InvalidComponentName");
 }

 @Test
 public void testIsGroup() {
 // Test when group agent is not a group
 assertFalse(groupAgent.isGroup("John", "Doe"));

 // Test when group agent is a group
 groupAgentSetter.setFirstName(groupAgent, "John");
 groupAgentSetter.setLastName(groupAgent, "Doe");
 assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent1 = new MockGroupAgent();
 GroupAgent groupAgent2 = new MockGroupAgent();
 groupAgent1.setFirstName("John");
 groupAgent1.setLastName("Doe");
 groupAgent2.setFirstName("John");
 groupAgent2.setLastName("Doe");
 assertTrue(groupAgent1.equals(groupAgent2));

 GroupAgent groupAgent3 = new MockGroupAgent();
 groupAgent3.setFirstName("John");
 groupAgent3.setLastName("Smith");
 assertFalse(groupAgent1.equals(groupAgent3));
 }

 @Test
 public void testAddAgent() {
 // Test when adding a valid agent
 groupAgent.addAgent(componentAgent);
 assertEquals(1, groupAgent.getNumberOfChildren());

 // Test when adding a null agent
 try {
 groupAgent.addAgent(null);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(0, children.size());

 groupAgent.addAgent(subGroupAgent);
 children = groupAgent.getChildren();
 assertEquals(1, children.size());
 assertEquals(subGroupAgent, children.get(0));
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(0, groupAgent.getNumberOfChildren());
 groupAgent.addAgent(subGroupAgent);
 assertEquals(1, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 // Test when component is not found
 try {
 groupAgent.getComponent("ComponentName");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test when component is found
 groupAgent.addAgent(componentAgent);
 assertEquals(componentAgent, groupAgent.getComponent("ComponentName"));
 }

 @Test
 public void testGetGroup() {
 // Test when group is not found
 try {
 groupAgent.getGroup("FirstName", "LastName");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test when group is found
 groupAgentSetter.setFirstName(subGroupAgent, "FirstName");
 groupAgentSetter.setLastName(subGroupAgent, "LastName");
 groupAgent.addAgent(subGroupAgent);
 assertEquals(subGroupAgent, groupAgent.getGroup("FirstName", "LastName"));
 }

 @Test
 public void testFindDependingComponentByName() {
 // Test when component is not found
 try {
 groupAgent.findDependingComponentByName("ComponentName");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test when component is found in a child agent
 groupAgent.addAgent(subGroupAgent);
 componentAgent.setName("ComponentName");
 subGroupAgent.addAgent(componentAgent);
 assertEquals(componentAgent, groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test
 public void testFindDependingGroupByName() {
 // Test when group is not found
 try {
 groupAgent.findDependingGroupByName("FirstName", "LastName");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test when group is found in a child agent
 groupAgent.addAgent(subGroupAgent);
 subGroupAgent.setFirstName("FirstName");
 subGroupAgent.setLastName("LastName");
 groupAgent.addAgent(groupAgent);
 assertEquals(groupAgent, groupAgent.findDependingGroupByName("FirstName", "LastName"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.addAgent(subGroupAgent);
 subGroupAgent.addAgent(gapAgent);
 gapAgent.setGapType(GroupAgent.GapType.PARALLEL);
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(0, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgent.TemporaryGapsToGaps map = new GroupAgent.TemporaryGapsToGaps();
 map.put(gapAgent, new GapAgent(10));
 groupAgent.addAgent(subGroupAgent);
 subGroupAgent.addAgent(gapAgent);
 gapAgent.setGapType(GroupAgent.GapType.TEMPORARY);
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 assertEquals(1, groupAgent.getNumberOfChildren());
 assertEquals(new GapAgent(10), groupAgent.getChildren().get(0));
 }

 @Test
 public void testAddContentToGroup() {
 GroupLayoutEngineer.GroupLayoutEngineer engine = new GroupLayoutEngineer();
 GroupLayoutEngineer.GroupEngine groupEngine = engine.createGroupEngine();
 Group engineGroup = new Group();
 GroupAgentSetter groupAgentSetter = (GroupAgentSetter)agentSetter;
 groupAgentSetter.setFirstName(groupAgent, "John");
 groupAgentSetter.setLastName(groupAgent, "Doe");
 groupAgent.addAgent(subGroupAgent);
 groupAgentSetter.setFirstName(subGroupAgent, "SubGroupFirstName");
 groupAgentSetter.setLastName(subGroupAgent, "SubGroupLastName");
 groupAgentSetter.setFirstName(componentAgent, "ComponentName");
 groupAgentSetter.setLastName(componentAgent, "ComponentLastName");
 groupAgent.addAgent(componentAgent);
 groupAgent.addAgent(gapAgent);
 groupAgent.addContentToGroup(engineGroup, new GroupCreatorAndElementAdder());
 }

 @Test
 public void testAddGapBefore() {
 groupAgent.addAgent(componentAgent);
 groupAgent.addGapBefore(gapAgent, componentAgent);
 assertEquals(2, groupAgent.getNumberOfChildren());
 assertEquals(gapAgent, groupAgent.getChildren().get(1));
 }

 @Test
 public void testAddGapAfter() {
 groupAgent.addAgent(componentAgent);
 groupAgent.addAgent(gapAgent);
 groupAgent.addGapAfter(gapAgent, componentAgent);
 assertEquals(3, groupAgent.getNumberOfChildren());
 assertEquals(componentAgent, groupAgent.getChildren().get(1));
 }

 @Test
 public void testFunctionsOnGroupAndElementImplementation() {
 FunctionsOnGroupAndElement functionsOnGroupAndElement = new GroupAgent.FunctionsOnGroupAndElementImplementation(groupAgent, componentAgent);
 functionsOnGroupAndElement.addPrecedingGap(10);
 assertEquals(1, groupAgent.getNumberOfChildren());
 assertEquals(11, ((GapAgent)groupAgent.getChildren().get(0)).getWidth());
 functionsOnGroupAndElement.addFollowingGap(10);
 assertEquals(2, groupAgent.getNumberOfChildren());
 assertEquals(21, ((GapAgent)groupAgent.getChildren().get(1)).getWidth());
 }
}