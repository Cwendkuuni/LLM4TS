package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.GroupAgent.TemporaryGapAgent;
import glengineer.agents.GroupAgent.TemporaryGapsToGaps;
import glengineer.agents.Agent;
import glengineer.agents.ComponentAgent;
import glengineer.agents.GapAgent;
import glengineer.agents.GroupAgent.ParallelGroupAgent;
import glengineer.agents.GroupAgent.SequentialGroupAgent;
import glengineer.layout.GroupLayout;
import glengineer.layout.GroupCreatorAndElementAdder;

public class GroupAgentTest {

 @Test
 public void testGetFirstName() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(new ComponentAgent("Jane", "Doe"));

 String result = groupAgent.getFirstName();
 assertEquals("John", result);
 }

 @Test
 public void testGetLastName() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(new ComponentAgent("Jane", "Doe"));

 String result = groupAgent.getLastName();
 assertEquals("Doe", result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFirstNameWhenNoChildren() {
 GroupAgent groupAgent = new MyGroupAgent();
 String result = groupAgent.getFirstName();
 assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetLastNameWhenNoChildren() {
 GroupAgent groupAgent = new MyGroupAgent();
 String result = groupAgent.getLastName();
 assertNull(result);
 }

 @Test
 public void testIsGroupWithFirstNameAndLastName() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));

 assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGroupWithFirstNameAndLastNameWhenNotGroup() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(new ComponentAgent("Jane", "Doe"));

 assertFalse(groupAgent.isGroup("Jane", "Doe"));
 }

 @Test
 public void testIsComponentWithName() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(new ComponentAgent("Jane", "Doe"));
 groupAgent.addAgent(new ComponentAgent("Component", "Name"));

 assertTrue(groupAgent.isComponent("Component"));
 }

 @Test
 public void testIsComponentWithNameWhenNotComponent() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(new ComponentAgent("Jane", "Doe"));

 assertFalse(groupAgent.isComponent("John"));
 }

 @Test
 public void testEqualsWithSameGroup() {
 GroupAgent groupAgent1 = new MyGroupAgent();
 groupAgent1.addAgent(new ComponentAgent("John", "Doe"));

 GroupAgent groupAgent2 = new MyGroupAgent();
 groupAgent2.addAgent(new ComponentAgent("John", "Doe"));

 assertTrue(groupAgent1.equals(groupAgent2));
 }

 @Test
 public void testEqualsWithDifferentGroup() {
 GroupAgent groupAgent1 = new MyGroupAgent();
 groupAgent1.addAgent(new ComponentAgent("John", "Doe"));

 GroupAgent groupAgent2 = new MyGroupAgent();
 groupAgent2.addAgent(new ComponentAgent("Jane", "Doe"));

 assertFalse(groupAgent1.equals(groupAgent2));
 }

 @Test
 public void testAddAgent() {
 GroupAgent groupAgent = new MyGroupAgent();

 Agent agent = new ComponentAgent("John", "Doe");
 groupAgent.addAgent(agent);
 List<Agent> children = groupAgent.getChildren();

 assertEquals(1, children.size());
 assertTrue(children.contains(agent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 GroupAgent groupAgent = new MyGroupAgent();

 try {
 groupAgent.addAgent(null);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetChildren() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(new ComponentAgent("Jane", "Doe"));

 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(new ComponentAgent("Jane", "Doe"));

 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponentWhenComponentExists() {
 GroupAgent groupAgent = new MyGroupAgent();
 ComponentAgent component = new ComponentAgent("Component", "Name");
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(component);
 groupAgent.addAgent(new ComponentAgent("Jane", "Doe"));

 assertEquals(component, groupAgent.getComponent("Component"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWhenComponentDoesNotExist() {
 GroupAgent groupAgent = new MyGroupAgent();
 ComponentAgent component = new ComponentAgent("Component", "Name");
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(new ComponentAgent("Jane", "Doe"));

 try {
 groupAgent.getComponent("NonExistentComponent");
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetGroupWhenGroupExists() {
 GroupAgent groupAgent = new MyGroupAgent();
 GroupAgent group = new MyGroupAgent();
 group.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(group);
 groupAgent.addAgent(new ComponentAgent("Jane", "Doe"));

 assertEquals(group, groupAgent.getGroup("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWhenGroupDoesNotExist() {
 GroupAgent groupAgent = new MyGroupAgent();
 GroupAgent group = new MyGroupAgent();
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(new ComponentAgent("Jane", "Doe"));

 try {
 groupAgent.getGroup("Jane", "Doe");
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindDependingComponentByName() {
 GroupAgent groupAgent = new MyGroupAgent();
 ComponentAgent component1 = new ComponentAgent("Component1", "Name1");
 ComponentAgent component2 = new ComponentAgent("Component2", "Name2");
 groupAgent.addAgent(component1);
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(component2);

 assertEquals(component1, groupAgent.findDependingComponentByName("Component1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWhenComponentDoesNotExist() {
 GroupAgent groupAgent = new MyGroupAgent();
 ComponentAgent component1 = new ComponentAgent("Component1", "Name1");
 ComponentAgent component2 = new ComponentAgent("Component2", "Name2");
 groupAgent.addAgent(component1);
 groupAgent.addAgent(new ComponentAgent("John", "Doe"));
 groupAgent.addAgent(component2);

 try {
 groupAgent.findDependingComponentByName("NonExistentComponent");
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent groupAgent = new MyGroupAgent();
 GroupAgent group1 = new MyGroupAgent();
 group1.addAgent(new ComponentAgent("John1", "Doe1"));
 groupAgent.addAgent(group1);
 groupAgent.addAgent(new ComponentAgent("John2", "Doe2"));
 GroupAgent group2 = new MyGroupAgent();
 group2.addAgent(new ComponentAgent("John3", "Doe3"));
 groupAgent.addAgent(group2);

 assertEquals(group1, groupAgent.findDependingGroupByNames("John1", "Doe1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWhenGroupDoesNotExist() {
 GroupAgent groupAgent = new MyGroupAgent();
 GroupAgent group1 = new MyGroupAgent();
 group1.addAgent(new ComponentAgent("John1", "Doe1"));
 GroupAgent group2 = new MyGroupAgent();
 groupAgent.addAgent(group1);
 groupAgent.addAgent(group2);
 groupAgent.addAgent(new ComponentAgent("John2", "Doe2"));

 try {
 groupAgent.findDependingGroupByNames("John3", "Doe3");
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 GroupAgent groupAgent = new MyGroupAgent();
 ParallelGroupAgent group1 = new ParallelGroupAgent();
 group1.addAgent(new ComponentAgent("John1", "Doe1"));
 groupAgent.addAgent(group1);
 groupAgent.addAgent(new ComponentAgent("John2", "Doe2"));
 SequentialGroupAgent group2 = new SequentialGroupAgent();
 group2.addAgent(new ComponentAgent("John3", "Doe3"));
 groupAgent.addAgent(group2);

 assertEquals(group1, groupAgent.findDependingParallelGroupByNames("John1", "Doe1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWhenParallelGroupDoesNotExist() {
 GroupAgent groupAgent = new MyGroupAgent();
 ParallelGroupAgent group1 = new ParallelGroupAgent();
 group1.addAgent(new ComponentAgent("John1", "Doe1"));
 GroupAgent group2 = new MyGroupAgent();
 groupAgent.addAgent(group1);
 groupAgent.addAgent(group2);
 groupAgent.addAgent(new ComponentAgent("John2", "Doe2"));
 SequentialGroupAgent group3 = new SequentialGroupAgent();
 groupAgent.addAgent(group3);

 try {
 groupAgent.findDependingParallelGroupByNames("John3", "Doe3");
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 GroupAgent groupAgent = new MyGroupAgent();
 ParallelGroupAgent group1 = new ParallelGroupAgent();
 group1.addAgent(new ComponentAgent("John1", "Doe1"));
 groupAgent.addAgent(group1);
 groupAgent.addAgent(new ComponentAgent("John2", "Doe2"));
 ParallelGroupAgent group2 = new ParallelGroupAgent();
 group2.addAgent(new ComponentAgent("John3", "Doe3"));
 groupAgent.addAgent(group2);
 SequentialGroupAgent group3 = new SequentialGroupAgent();
 group3.addAgent(new ComponentAgent("John4", "Doe4"));
 groupAgent.addAgent(group3);

 assertEquals(group3, groupAgent.findDependingSequentialGroupByNames("John4", "Doe4"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWhenSequentialGroupDoesNotExist() {
 GroupAgent groupAgent = new MyGroupAgent();
 ParallelGroupAgent group1 = new ParallelGroupAgent();
 group1.addAgent(new ComponentAgent("John1", "Doe1"));
 groupAgent.addAgent(group1);
 groupAgent.addAgent(new ComponentAgent("John2", "Doe2"));
 ParallelGroupAgent group2 = new ParallelGroupAgent();
 group2.addAgent(new ComponentAgent("John3", "Doe3"));
 groupAgent.addAgent(group2);
 SequentialGroupAgent group3 = new SequentialGroupAgent();
 groupAgent.addAgent(group3);
 groupAgent.addAgent(new ComponentAgent("John4", "Doe4"));

 try {
 groupAgent.findDependingSequentialGroupByNames("John5", "Doe5");
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent groupAgent = new MyGroupAgent();
 ParallelGroupAgent parallelGroup1 = new ParallelGroupAgent();
 parallelGroup1.addAgent(new ComponentAgent("Component1", "Name1"));
 parallelGroup1.addAgent(new TemporaryGapAgent());
 parallelGroup1.addAgent(new ComponentAgent("Component2", "Name2"));
 parallelGroup1.addAgent(new ComponentAgent("Component3", "Name3"));
 parallelGroup1.addAgent(new TemporaryGapAgent());
 parallelGroup1.addAgent(new ComponentAgent("Component4", "Name4"));
 groupAgent.addAgent(parallelGroup1);
 parallelGroup1.setGapsType(new TemporaryGapsToGaps());
 parallelGroup1.removeGapsFromParallelGroups();

 List<Agent> children = groupAgent.getChildren();
 List<Agent> expectedChildren = new LinkedList<Agent>();
 expectedChildren.add(parallelGroup1.getChildren().get(0));
 expectedChildren.add(parallelGroup1.getChildren().get(2));
 expectedChildren.add(parallelGroup1.getChildren().get(4));
 assertEquals(expectedChildren, children);
 }

 @Test
 public void testAddContentToGroup() {
 GroupAgent groupAgent = new MyGroupAgent();
 GroupLayout layout = new GroupLayout();
 GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder(layout, new AgentSetter());

 groupAgent.addContentToGroup(layout, creatorAndAdder);

 // verify that the group content was added to the layout
 List<Agent> children = layout.getChildren();
 assertTrue(children.contains(groupAgent));
 }

 @Test
 public void testGroupContent() {
 GroupAgent groupAgent = new MyGroupAgent();
 GroupAgent testGroup = new MyGroupAgent();
 testGroup.addAgent(new ComponentAgent("Component1", "Name1"));
 testGroup.addAgent(new ComponentAgent("Component2", "Name2"));

 groupAgent.addAgent(testGroup);

 GroupLayout.Group groupContent = groupAgent.groupContent(new GroupCreatorAndElementAdder());

 assertEquals(testGroup, groupContent);
 }

 @Test
 public void testGetFunctionsOnGroupImplemenation() {
 GroupAgent groupAgent = new MyGroupAgent();
 FunctionsOnGroupAndElement functions = groupAgent.getFunctionsOnGroupImplemenation();
 assertNotNull(functions);
 }

 @Test
 public void testAddGapBefore() {
 GroupAgent groupAgent = new MyGroupAgent();
 GapAgent gap = new GapAgent(10);
 ComponentAgent component = new ComponentAgent("Component", "Name");
 groupAgent.addAgent(component);
 groupAgent.addGapBefore(gap, component);

 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 Agent gapAgent = children.get(1);
 assertEquals(gap, gapAgent);
 }

 @Test
 public void testAddGapAfter() {
 GroupAgent groupAgent = new MyGroupAgent();
 GapAgent gap = new GapAgent(10);
 ComponentAgent component = new ComponentAgent("Component", "Name");
 groupAgent.addAgent(component);
 groupAgent.addGapAfter(gap, component);

 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 Agent gapAgent = children.get(0);
 assertEquals(component, gapAgent);
 assertEquals(gap, children.get(1));
 }
}

public class MyGroupAgent extends GroupAgent {
 // empty
}