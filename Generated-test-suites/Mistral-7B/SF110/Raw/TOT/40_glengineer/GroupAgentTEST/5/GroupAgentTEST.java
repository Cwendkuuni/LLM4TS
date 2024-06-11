package glengineer.agents.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.Agent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GapAgent;
import glengineer.agents.TemporaryGapAgent;

public class GroupAgentTest {

 @Test
 public void testEmptyGroupAgent() {
 GroupAgent groupAgent = new GroupAgent();
 assertNull(groupAgent.getFirstName());
 assertNull(groupAgent.getLastName());
 assertTrue(groupAgent.isEmpty());
 }

 @Test
 public void testGroupAgentWithChildren() {
 // Create children agents
 Agent child1 = new AgentSetter("John", "Doe");
 Agent child2 = new AgentSetter("Jane", "Smith");

 // Create group agent and add children
 GroupAgent groupAgent = new GroupAgent();
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);

 // Test getFirstName() method
 assertEquals(child1.getFirstName(), groupAgent.getFirstName());

 // Test getLastName() method
 assertEquals(child2.getLastName(), groupAgent.getLastName());

 // Test isGroup() method with correct arguments
 assertTrue(groupAgent.isGroup(child1.getFirstName(), child1.getLastName()));
 assertTrue(groupAgent.isGroup(child2.getFirstName(), child2.getLastName()));

 // Test isGroup() method with incorrect arguments
 assertFalse(groupAgent.isGroup("John", "Doe2"));
 assertFalse(groupAgent.isGroup("Jane", "Smith2"));

 // Test equals() method
 assertTrue(groupAgent.equals(groupAgent));

 // Test equals() method with different group agents
 assertFalse(groupAgent.equals(new Agent()));

 // Test addAgent() method with null argument
 try {
 groupAgent.addAgent(null);
 fail("Cannot add null agents.");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test getChildren() method
 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));

 // Test getNumberOfChildren() method
 assertEquals(2, groupAgent.getNumberOfChildren());

 // Test findDependingComponentByName() method
 assertNull(groupAgent.findDependingComponentByName("Component1"));

 // Test findDependingGroupByNames() method
 assertNull(groupAgent.findDependingGroupByNames("John", "Doe"));
 assertNull(groupAgent.findDependingGroupByNames("Jane", "Smith"));

 // Test findDependingParallelGroupByNames() method
 assertNull(groupAgent.findDependingParallelGroupByNames("John", "Doe"));
 assertNull(groupAgent.findDependingParallelGroupByNames("Jane", "Smith"));

 // Test findDependingSequentialGroupByNames() method
 assertNull(groupAgent.findDependingSequentialGroupByNames("John", "Doe"));
 assertNull(groupAgent.findDependingSequentialGroupByNames("Jane", "Smith"));

 // Test removeGapsFromParallelGroups() method
 groupAgent.removeGapsFromParallelGroups();
 List<Agent> newChildren = groupAgent.getChildren();
 assertEquals(2, newChildren.size());
 assertFalse(newChildren.contains(child1));
 assertFalse(newChildren.contains(child2));
 // Check if each child is a GapAgent with correct size
 for (Agent agent : newChildren) {
 if (agent instanceof GapAgent) {
 GapAgent gapAgent = (GapAgent)agent;
 assertEquals(((GapAgent)child1).getSize(), gapAgent.getSize());
 }
 }
 }

 @Test
 public void testGroupAgentWithTemporaryGaps() {
 // Create children agents
 Agent child1 = new AgentSetter("John", "Doe");
 Agent child2 = new TemporaryGapAgent(10);
 Agent child3 = new AgentSetter("Jane", "Smith");

 // Create group agent and add children
 GroupAgent groupAgent = new GroupAgent();
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addAgent(child3);

 // Test findDependingSequentialGroupByNames() method with temporary gap
 GroupAgent result = groupAgent.findDependingSequentialGroupByNames("John", "Doe");
 assertNotNull(result);
 assertTrue(result.getClass().isAssignableFrom(GroupAgent.class));
 assertEquals(child1, result);
}

 @Test(expected = IllegalArgumentException.class)
 public void testAddAgentWithNullChild() {
 // Create group agent
 GroupAgent groupAgent = new GroupAgent();

 // Try to add null child
 groupAgent.addAgent(null);
}

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 // Create children agents
 Agent child1 = new AgentSetter("John", "Doe");
 Agent child2 = new TemporaryGapAgent(10);
 Agent child3 = new AgentSetter("Jane", "Smith");

 // Create group agent and add children
 GroupAgent groupAgent = new GroupAgent();
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addAgent(child3);

 // Replace temporary gaps by real gaps
 groupAgent.replaceTemporaryGapsByRealGaps(new GroupLayoutEngineer.TemporaryGapsToGaps() {
 @Override
 public Agent gap(TemporaryGapAgent gapAgent) {
 return new GapAgent(gapAgent.getSize());
 }
 });

 // Test findDependingSequentialGroupByNames() method with real gaps
 GroupAgent result = groupAgent.findDependingSequentialGroupByNames("John", "Doe");
 assertNotNull(result);
 assertTrue(result.getClass().isAssignableFrom(GroupAgent.class));
 assertEquals(child1, result);
}

 @Test
 public void testAddContentToGroup() {
 // Create group agent
 GroupAgent groupAgent = new GroupAgent();

 // Create parent group and element adder
 GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder() {
 @Override
 public void addGroupToGroup(GroupLayout.Group parentGroup, Settings settings) {
 // Do nothing
 }

 @Override
 public void addElementToGroup(GroupLayout.Component parentComponent, Settings settings) {
 // Do nothing
 }
 };

 // Add content to group
 groupAgent.addContentToGroup(new GroupLayout.Group("ParentGroup"), creatorAndAdder);

 // Test if addContentToGroup() method was called
 assertEquals(1, creatorAndAdder.getCallCount());
}

 @Test
 public void testAddContentToGroupWithTwoChildren() {
 // Create children agents
 Agent child1 = new AgentSetter("Child1", "Child1");
 Agent child2 = new AgentSetter("Child2", "Child2");

 // Create group agent and add children
 GroupAgent groupAgent = new GroupAgent();
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);

 // Create parent group and element adder
 GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder() {
 @Override
 public void addGroupToGroup(GroupLayout.Group parentGroup, Settings settings) {
 List<GroupLayout.Component> components = new ArrayList<>();
 components.add(new GroupLayout.Component("Component1"));
 components.add(new GroupLayout.Component("Component2"));
 parentGroup.add(components.toArray(new GroupLayout.Component[0]));
 }

 @Override
 public void addElementToGroup(GroupLayout.Component parentComponent, Settings settings) {
 // Do nothing
 }
 };

 // Add content to group
 groupAgent.addContentToGroup(new GroupLayout.Group("ParentGroup"), creatorAndAdder);

 // Test if addContentToGroup() method was called twice
 assertEquals(2, creatorAndAdder.getCallCount());
}

 @Test
 public void testPassTheCallToSubgroups() {
 // Create grandchildren agents
 Agent grandchild1 = new AgentSetter("Grandchild1", "Grandchild1");
 Agent grandchild2 = new AgentSetter("Grandchild2", "Grandchild2");
 Agent grandchild3 = new AgentSetter("Grandchild3", "Grandchild3");

 // Create children agents
 Agent child1 = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return new GroupLayout.Group("Child1");
 }
 };
 child1.addAgent(grandchild1);
 child1.addAgent(grandchild2);

 Agent child2 = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return new GroupLayout.Group("Child2");
 }
 };
 child2.addAgent(grandchild3);

 // Create group agent and add children
 GroupAgent groupAgent = new GroupAgent();
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);

 // Test passTheCallToSubgroups() method
 groupAgent.passTheCallToSubgroups();

 // Test if passTheCallToSubgroups() method was called twice on each child
 for (Agent child : groupAgent.getChildren()) {
 if (child instanceof GroupAgent) {
 GroupAgent groupChild = (GroupAgent)child;
 assertEquals(2, groupChild.getCallCount());
 }
 }
}

 // Helper class for testing the FunctionsOnGroupAndElement interface
 static class TestFunctionsOnGroupAndElement implements FunctionsOnGroupAndElement {
 @Override
 public void addPrecedingGap(int size) {
 // Do nothing
 }

 @Override
 public void addPrecedingGap(int min, int pref, int max) {
 // Do nothing
 }

 @Override
 public void addFollowingGap(int size) {
 // Do nothing
 }

 @Override
 public void addFollowingGap(int min, int pref, int max) {
 // Do nothing
 }

 @Override
 public int getCallCount() {
 return 0;
 }
 }

 static class GroupCreatorAndElementAdder implements GroupLayoutEngineer.GroupCreatorAndElementAdder {
 private int callCount;

 @Override
 public void addGroupToGroup(GroupLayout.Group parentGroup, Settings settings) {
 callCount++;
 }

 @Override
 public void addElementToGroup(GroupLayout.Component parentComponent, Settings settings) {
 callCount++;
 }

 public int getCallCount() {
 return callCount;
 }
 }
}