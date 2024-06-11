package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.ComponentAgent;
import glengineer.agents.FunctionsOnGroupAndElement;
import glengineer.agents.GroupAgent;
import glengineer.agents.GapAgent;
import glengineer.agents.Settings;
import glengineer.agents.setters.SettingsSetter;
import glengineer.agents.utils.GroupLayoutEngineer;
import glengineer.agents.utils.GroupCreatorAndElementAdder;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private SettingsSetter settingsSetter;
 private GroupCreatorAndElementAdder groupCreatorAndElementAdder;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 settingsSetter = new SettingsSetter();
 groupCreatorAndElementAdder = new GroupCreatorAndElementAdder();
 }

 @Test
 public void testGetFirstName() {
 Agent child1 = new ComponentAgent("Child1");
 Agent child2 = new ComponentAgent("Child2");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 String firstName = groupAgent.getFirstName();
 assertEquals("Child1", firstName);
 }

 @Test
 public void testGetLastName() {
 Agent child1 = new ComponentAgent("Child1");
 Agent child2 = new ComponentAgent("Child2");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 String lastName = groupAgent.getLastName();
 assertEquals("Child2", lastName);
 }

 @Test
 public void testIsGroup() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent.addAgent(new ComponentAgent("Child1"));
 groupAgent.addAgent(new ComponentAgent("Child2"));
 assertTrue(groupAgent.isGroup("Child1", "Child2"));
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent1 = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent1.addAgent(new ComponentAgent("Child1"));
 groupAgent1.addAgent(new ComponentAgent("Child2"));
 groupAgent2.addAgent(new ComponentAgent("Child1"));
 groupAgent2.addAgent(new ComponentAgent("Child2"));
 assertTrue(groupAgent1.equals(groupAgent2));
 }

 @Test
 public void testAddAgent() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 Agent child = new ComponentAgent("Child");
 groupAgent.addAgent(child);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(1, children.size());
 assertEquals(child, children.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 Agent nullAgent = null;
 try {
 groupAgent.addAgent(nullAgent);
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testGetChildren() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 Agent child1 = new ComponentAgent("Child1");
 Agent child2 = new ComponentAgent("Child2");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 Agent child1 = new ComponentAgent("Child1");
 Agent child2 = new ComponentAgent("Child2");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 Agent component = new ComponentAgent("Component");
 groupAgent.addAgent(component);
 assertEquals(component, groupAgent.getComponent("Component"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentNullName() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 try {
 groupAgent.getComponent(null);
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testGetGroup() {
 GroupAgent groupAgent1 = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 Agent groupAgent2 = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent1.addAgent(new ComponentAgent("Child1"));
 groupAgent1.addAgent(new ComponentAgent("Child2"));
 groupAgent2.addAgent(new ComponentAgent("Child1"));
 groupAgent2.addAgent(new ComponentAgent("Child3"));
 try {
 groupAgent1.getGroup("Child1", "Child3");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 try {
 groupAgent2.getGroup("Child1", "Child1");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 try {
 groupAgent1.getGroup("Child1", "Child2");
 assertEquals(groupAgent1, groupAgent1.getGroup("Child1", "Child2"));
 } catch (IllegalArgumentException e) {
 fail();
 }
 }

 @Test
 public void testFindDependingComponentByName() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 ComponentAgent component1 = new ComponentAgent("Component1");
 ComponentAgent component2 = new ComponentAgent("Component2");
 groupAgent.addAgent(component1);
 groupAgent.addAgent(component2);
 ComponentAgent result = groupAgent.findDependingComponentByName("Component1");
 assertEquals(component1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameNullName() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 try {
 groupAgent.findDependingComponentByName(null);
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent groupAgent1 = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 Agent groupAgent2 = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent1.addAgent(new ComponentAgent("Child1"));
 groupAgent1.addAgent(new ComponentAgent("Child2"));
 groupAgent2.addAgent(new ComponentAgent("Child1"));
 groupAgent2.addAgent(new ComponentAgent("Child3"));
 try {
 groupAgent1.findDependingGroupByNames("Child1", "Child3");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 try {
 groupAgent2.findDependingGroupByNames("Child1", "Child1");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 try {
 groupAgent1.findDependingGroupByNames("Child1", "Child2");
 assertEquals(groupAgent1, groupAgent1.findDependingGroupByNames("Child1", "Child2"));
 } catch (IllegalArgumentException e) {
 fail();
 }
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 GroupAgent groupAgent1 = new ParallelGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 Agent groupAgent2 = new SequentialGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent1.addAgent(new ComponentAgent("Child1"));
 groupAgent1.addAgent(new ComponentAgent("Child2"));
 groupAgent2.addAgent(new ComponentAgent("Child1"));
 groupAgent2.addAgent(new ComponentAgent("Child3"));
 try {
 groupAgent1.findDependingParallelGroupByNames("Child1", "Child3");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 try {
 groupAgent2.findDependingParallelGroupByNames("Child1", "Child1");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 try {
 groupAgent1.findDependingParallelGroupByNames("Child1", "Child2");
 assertEquals(groupAgent1, groupAgent1.findDependingParallelGroupByNames("Child1", "Child2"));
 } catch (IllegalArgumentException e) {
 fail();
 }
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 GroupAgent groupAgent1 = new SequentialGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 Agent groupAgent2 = new ParallelGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent1.addAgent(new ComponentAgent("Child1"));
 groupAgent1.addAgent(new ComponentAgent("Child2"));
 groupAgent2.addAgent(new ComponentAgent("Child1"));
 groupAgent2.addAgent(new ComponentAgent("Child3"));
 try {
 groupAgent1.findDependingSequentialGroupByNames("Child1", "Child3");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 try {
 groupAgent2.findDependingSequentialGroupByNames("Child1", "Child1");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 try {
 groupAgent1.findDependingSequentialGroupByNames("Child1", "Child2");
 assertEquals(groupAgent1, groupAgent1.findDependingSequentialGroupByNames("Child1", "Child2"));
 } catch (IllegalArgumentException e) {
 fail();
 }
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent groupAgent = new ParallelGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent.addAgent(new GapAgent(10));
 groupAgent.addAgent(new ComponentAgent("Component"));
 groupAgent.addAgent(new GapAgent(5));
 groupAgent.addAgent(new ComponentAgent("Component"));
 groupAgent.addAgent(new GapAgent(3));
 groupAgent.addAgent(new ComponentAgent("Component"));
 groupAgent.removeGapsFromParallelGroups();
 List<Agent> agents = groupAgent.getChildren();
 assertEquals(3, agents.size());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 List<Agent> children = new LinkedList<>();
 children.add(new ComponentAgent("Component1"));
 children.add(new TemporaryGapAgent(10));
 children.add(new ComponentAgent("Component2"));
 children.add(new TemporaryGapAgent(15));
 children.add(new ComponentAgent("Component3"));
 groupAgent.addAgent(children);
 GroupLayoutEngineer temporaryGapsToGaps = new GroupLayoutEngineer();
 GroupAgent result = groupAgent.replaceTemporaryGapsByRealGaps(temporaryGapsToGaps);
 List<Agent> resultChildren = result.getChildren();
 List<Agent> expectedChildren = new LinkedList<>();
 expectedChildren.add(new ComponentAgent("Component1"));
 expectedChildren.add(new GapAgent(10));
 expectedChildren.add(new ComponentAgent("Component2"));
 expectedChildren.add(new GapAgent(5));
 expectedChildren.add(new ComponentAgent("Component3"));
 assertEquals(expectedChildren, resultChildren);
 }

 @Test
 public void testAddGapBefore() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 ComponentAgent component = new ComponentAgent("Component");
 GapAgent gap = new GapAgent(10);
 groupAgent.addAgent(component);
 groupAgent.addGapBefore(gap, component);
 List<Agent> agents = groupAgent.getChildren();
 assertEquals(2, agents.size());
 assertEquals(gap, agents.get(0));
 assertEquals(component, agents.get(1));
 }

 @Test
 public void testAddGapAfter() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 ComponentAgent component = new ComponentAgent("Component");
 GapAgent gap = new GapAgent(10);
 groupAgent.addAgent(component);
 groupAgent.addGapAfter(gap, component);
 List<Agent> agents = groupAgent.getChildren();
 assertEquals(2, agents.size());
 assertEquals(component, agents.get(0));
 assertEquals(gap, agents.get(1));
 }
}