package glengineer.agents.test;

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
import glengineer.agents.ParallelGroupAgent;
import glengineer.agents.SequentialGroupAgent;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent childAgent1;
 private Agent childAgent2;
 private Agent childAgent3;
 private Agent childAgent4;
 private Agent childAgent5;
 private ParallelGroupAgent parallelGroupAgent;
 private SequentialGroupAgent sequentialGroupAgent;
 private List<Agent> children;

 @Before
 public void setup() {
 groupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 childAgent1 = new AgentSetter("Child1", "FirstName1", "LastName1");
 childAgent2 = new AgentSetter("Child2", "FirstName2", "LastName2");
 childAgent3 = new AgentSetter("Child3", "FirstName3", "LastName3");
 childAgent4 = new AgentSetter("Child4", "FirstName4", "LastName4");
 childAgent5 = new AgentSetter("Child5", "FirstName5", "LastName5");
 parallelGroupAgent = new ParallelGroupAgent();
 parallelGroupAgent.addAgent(childAgent1);
 parallelGroupAgent.addAgent(childAgent2);
 groupAgent.addAgent(parallelGroupAgent);
 groupAgent.addAgent(childAgent3);
 groupAgent.addAgent(childAgent4);
 groupAgent.addAgent(childAgent5);
 children = groupAgent.getChildren();
 }

 @Test
 public void testGetFirstName() {
 assertEquals(childAgent1.getFirstName(), groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals(childAgent1.getLastName(), groupAgent.getLastName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFirstNameWhenNoChildren() {
 assertEquals(null, groupAgent.getFirstName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetLastNameWhenNoChildren() {
 assertEquals(null, groupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testIsGroup() {
 assertFalse(groupAgent.isGroup("FirstName1", "LastName1"));
 }

 @Test
 public void testIsGroupWithSelf() {
 assertTrue(groupAgent.isGroup(groupAgent.getFirstName(), groupAgent.getLastName()));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent otherGroupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 otherGroupAgent.addAgent(childAgent1);
 otherGroupAgent.addAgent(childAgent2);
 assertTrue(groupAgent.equals(otherGroupAgent));
 }

 @Test
 public void testEqualsWithDifferentGroup() {
 GroupAgent otherGroupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 otherGroupAgent.addAgent(childAgent1);
 otherGroupAgent.addAgent(new AgentSetter("Child6", "FirstName6", "LastName6"));
 assertFalse(groupAgent.equals(otherGroupAgent));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(childAgent5);
 assertEquals(5, groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 assertEquals(children, groupAgent.getChildren());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(5, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWithNullChildren() {
 assertNull(groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test
 public void testFindDependingComponentByNameWithExistingComponent() {
 Agent componentAgent = new AgentSetter("Component", "ComponentName", "ComponentName");
 groupAgent.addAgent(componentAgent);
 assertEquals(componentAgent, groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWithNonExistingComponent() {
 assertNull(groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent groupAgentToFind = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 groupAgentToFind.addAgent(childAgent1);
 groupAgentToFind.addAgent(childAgent2);
 assertEquals(groupAgentToFind, groupAgent.findDependingGroupByNames("FirstName1", "LastName1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWithNonExistingGroup() {
 GroupAgent groupAgentToFind = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 groupAgentToFind.addAgent(childAgent1);
 groupAgentToFind.addAgent(childAgent2);
 groupAgentToFind.addAgent(new AgentSetter("Child6", "FirstName6", "LastName6"));
 assertNull(groupAgent.findDependingGroupByNames("FirstName1", "LastName1"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 ParallelGroupAgent parallelGroupAgentToFind = new ParallelGroupAgent();
 parallelGroupAgentToFind.addAgent(childAgent1);
 parallelGroupAgentToFind.addAgent(childAgent2);
 assertEquals(parallelGroupAgentToFind, groupAgent.findDependingParallelGroupByNames("FirstName1", "LastName1"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 SequentialGroupAgent sequentialGroupAgentToFind = new SequentialGroupAgent();
 sequentialGroupAgentToFind.addAgent(childAgent3);
 sequentialGroupAgentToFind.addAgent(childAgent4);
 assertEquals(sequentialGroupAgentToFind, groupAgent.findDependingSequentialGroupByNames("FirstName3", "LastName3"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(4, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
 map.put("TemporaryGap1", new GapAgent(10));
 map.put("TemporaryGap2", new GapAgent(20));
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 List<Agent> newChildren = groupAgent.getChildren();
 assertEquals(6, newChildren.size());
 assertEquals(childAgent1, newChildren.get(0));
 assertEquals(childAgent2, newChildren.get(1));
 assertEquals(map.gap(childAgent1), newChildren.get(2));
 assertEquals(childAgent3, newChildren.get(3));
 assertEquals(childAgent4, newChildren.get(4));
 assertEquals(map.gap(childAgent2), newChildren.get(5));
 }

 @Test
 public void testAddGapBefore() {
 GapAgent gapAgent = new GapAgent(5);
 groupAgent.addGapBefore(gapAgent, childAgent1);
 List<Agent> newChildren = groupAgent.getChildren();
 assertEquals(6, newChildren.size());
 assertEquals(gapAgent, newChildren.get(0));
 assertEquals(childAgent1, newChildren.get(1));
 }

 @Test
 public void testAddGapAfter() {
 GapAgent gapAgent = new GapAgent(5);
 groupAgent.addAgent(childAgent1);
 groupAgent.addAgent(childAgent2);
 groupAgent.addGapAfter(gapAgent, childAgent1);
 List<Agent> newChildren = groupAgent.getChildren();
 assertEquals(4, newChildren.size());
 assertEquals(childAgent1, newChildren.get(0));
 assertEquals(gapAgent, newChildren.get(1));
 assertEquals(childAgent2, newChildren.get(2));
 }

 @Test
 public void testFunctionsOnGroupAndElementImplementation() {
 FunctionsOnGroupAndElement implementation = new FunctionsOnGroupAndElementImplementation(groupAgent, childAgent1);
 implementation.addPrecedingGap(10);
 assertEquals(11, groupAgent.getNumberOfChildren());
 List<Agent> newChildren = groupAgent.getChildren();
 assertEquals(newAgentClass(GapAgent.class, 10), newChildren.get(0));
 assertEquals(childAgent1, newChildren.get(1));
 }

 private Class newAgentClass(Class clazz, int size) {
 try {
 return Class.forName(clazz.getName() + Integer.toString(size));
 } catch (ClassNotFoundException e) {
 throw new RuntimeException(e);
 }
 }

}