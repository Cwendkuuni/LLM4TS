package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.GroupAgent;
import glengineer.agents.Settings;
import glengineer.agents.setters.ComponentAgent;
import glengineer.agents.setters.GroupAgentTestHelper;
import glengineer.agents.setters.GapAgent;
import glengineer.agents.setters.TemporaryGapAgent;
import glengineer.exceptions.IllegalArgumentException;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent child1, child2, child3, child4, child5;
 private List<Agent> children;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };

 child1 = new ComponentAgent("Child1");
 child2 = new ComponentAgent("Child2");
 child3 = new GroupAgentTestHelper.GroupAgentDummy("Child3");
 child4 = new TemporaryGapAgent("Gap1");
 child5 = new ComponentAgent("Child5");
 children = new LinkedList<Agent>();
 groupAgent.setChildren(children);
 }

 @After
 public void tearDown() {
 children.clear();
 groupAgent = null;
 }

 @Test
 public void testGetFirstName() {
 assertNull(groupAgent.getFirstName());
 children.add(child1);
 assertEquals(child1.getFirstName(), groupAgent.getFirstName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFirstName_whenNoChildWithFirstName() {
 children.clear();
 children.add(child1);
 children.add(child2);
 assertNull(groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertNull(groupAgent.getLastName());
 children.add(child3);
 assertEquals(child3.getLastName(), groupAgent.getLastName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLastName_whenNoChildWithLastName() {
 children.clear();
 children.add(child1);
 children.add(child2);
 children.add(child3);
 assertNull(groupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testIsGroup() {
 assertFalse(groupAgent.isGroup("FirstName", "LastName"));
 children.clear();
 children.add(child1);
 children.add(child2);
 assertFalse(groupAgent.isGroup("FirstName1", "LastName1"));
 children.add(child3);
 assertTrue(groupAgent.isGroup("FirstName", "LastName"));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 assertFalse(groupAgent.equals(groupAgent2));
 children.clear();
 children.add(child1);
 children.add(child2);
 GroupAgent groupAgent3 = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent3.setChildren(children);
 assertTrue(groupAgent.equals(groupAgent3));
 }

 @Test
 public void testAddAgent() {
 assertFalse(groupAgent.getChildren().contains(child4));
 groupAgent.addAgent(child4);
 assertTrue(groupAgent.getChildren().contains(child4));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAgent_whenNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 assertEquals(0, groupAgent.getNumberOfChildren());
 children.add(child1);
 children.add(child2);
 List<Agent> childrenList = groupAgent.getChildren();
 assertEquals(2, childrenList.size());
 assertTrue(childrenList.contains(child1));
 assertTrue(childrenList.contains(child2));
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(0, groupAgent.getNumberOfChildren());
 children.add(child1);
 children.add(child2);
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(groupAgent.getComponent("ComponentName"));
 children.add(child1);
 ComponentAgent component = new ComponentAgent("ComponentName");
 children.add(component);
 assertEquals(component, groupAgent.getComponent("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponent_whenComponentNotFound() {
 children.clear();
 children.add(child1);
 children.add(child2);
 assertNull(groupAgent.getComponent("ComponentName"));
 }

 @Test
 public void testGetGroup() {
 assertNull(groupAgent.getGroup("FirstName", "LastName"));
 children.clear();
 children.add(child1);
 children.add(child2);
 children.add(child3);
 assertNull(groupAgent.getGroup("FirstName1", "LastName1"));
 GroupAgent group = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 group.setChildren(Arrays.asList(child3));
 children.add(group);
 assertEquals(group, groupAgent.getGroup("FirstName", "LastName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroup_whenGroupNotFound() {
 children.clear();
 children.add(child1);
 children.add(child2);
 children.add(child3);
 assertNull(groupAgent.getGroup("FirstName", "LastName"));
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("ComponentName"));
 children.add(child1);
 children.add(child2);
 ComponentAgent component = new ComponentAgent("ComponentName");
 children.add(component);
 assertEquals(component, groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByName_whenComponentNotFound() {
 children.clear();
 children.add(child1);
 children.add(child2);
 assertNull(groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertNull(groupAgent.findDependingGroupByNames("FirstName", "LastName"));
 children.clear();
 children.add(child1);
 children.add(child2);
 children.add(child3);
 assertNull(groupAgent.findDependingGroupByNames("FirstName1", "LastName1"));
 GroupAgent group = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 group.setChildren(Arrays.asList(child3));
 children.add(group);
 assertEquals(group, groupAgent.findDependingGroupByNames("FirstName", "LastName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNames_whenGroupNotFound() {
 children.clear();
 children.add(child1);
 children.add(child2);
 children.add(child3);
 assertNull(groupAgent.findDependingGroupByNames("FirstName", "LastName"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertNull(groupAgent.findDependingParallelGroupByNames("FirstName", "LastName"));
 children.clear();
 children.add(child1);
 children.add(child2);
 children.add(child3);
 children.add(new ParallelGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 });
 GroupAgent group = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 group.setChildren(Arrays.asList(child3));
 children.add(group);
 assertEquals(group, groupAgent.findDependingParallelGroupByNames("FirstName", "LastName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNames_whenGroupNotFound() {
 children.clear();
 children.add(child1);
 children.add(child2);
 children.add(child3);
 children.add(new ParallelGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 });
 GroupAgent group = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 group.setChildren(Arrays.asList(child3));
 assertNull(groupAgent.findDependingParallelGroupByNames("FirstName", "LastName"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertNull(groupAgent.findDependingSequentialGroupByNames("FirstName", "LastName"));
 children.clear();
 children.add(child1);
 children.add(child2);
 children.add(child3);
 children.add(new SequentialGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 });
 GroupAgent group = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 group.setChildren(Arrays.asList(child3));
 assertEquals(group, groupAgent.findDependingSequentialGroupByNames("FirstName", "LastName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNames_whenGroupNotFound() {
 children.clear();
 children.add(child1);
 children.add(child2);
 children.add(child3);
 children.add(new SequentialGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 });
 GroupAgent group = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupAndElementImplementation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 group.setChildren(Arrays.asList(child3));
 assertNull(groupAgent.findDependingSequentialGroupByNames("FirstName", "LastName"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent groupAgentBefore = groupAgent;
 groupAgent.removeGapsFromParallelGroups();
 assertNotSame(groupAgentBefore, groupAgent);
 }

 @Test
 public void testRemoveGapsFromParallelGroups_whenNoParallelGroups() {
 assertFalse(groupAgent.containsGapsOnly());
 groupAgent.removeGapsFromParallelGroups();
 assertTrue(groupAgent.containsGapsOnly());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 assertNull(groupAgent.getFunctionsOnGroupAndElementImplementation());
 groupAgent.replaceTemporaryGapsByRealGaps(null);
 assertNotNull(groupAgent.getFunctionsOnGroupAndElementImplementation());
 }
}