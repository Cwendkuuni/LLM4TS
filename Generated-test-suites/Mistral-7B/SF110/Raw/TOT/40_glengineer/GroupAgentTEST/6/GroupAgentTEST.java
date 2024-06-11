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
import glengineer.agents.setters.GroupCreatorAndElementAdder;
import glengineer.agents.GroupAgent.GroupLayout.Group;
import glengineer.agents.GapAgent;
import glengineer.agents.TemporaryGapAgent;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private GroupAgent child1;
 private GroupAgent child2;
 private GroupAgent child3;
 private AgentSetter agentSetter;
 private Settings settings;
 private GroupCreatorAndElementAdder creatorAndAdder;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // implementation of the abstract method
 return null;
 }
 };
 settings = new Settings();
 agentSetter = new AgentSetter();
 child1 = agentSetter.createAgent("Child1", "FirstName1", "LastName1");
 child2 = agentSetter.createAgent("Child2", "FirstName2", "LastName2");
 child3 = agentSetter.createAgent("Child3", "FirstName3", "LastName3");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addAgent(child3);
 creatorAndAdder = new GroupCreatorAndElementAdder();
 }

 @Test
 public void testGetFirstName() {
 assertEquals("FirstName1", groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("LastName1", groupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup("FirstName1", "LastName1"));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // implementation of the abstract method
 return null;
 }
 };
 groupAgent2.setFirstName("FirstName1");
 groupAgent2.setLastName("LastName1");
 assertTrue(groupAgent.equals(groupAgent2));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(3, children.size());
 assertTrue(children.get(0) instanceof GroupAgent);
 assertTrue(children.get(1) instanceof GroupAgent);
 assertTrue(children.get(2) instanceof GroupAgent);
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(groupAgent.getComponent("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNullFirstName() {
 groupAgent.getGroup(null, "LastName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNullLastName() {
 groupAgent.getGroup("FirstName", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupInvalidNames() {
 groupAgent.getGroup("InvalidName", "InvalidName");
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullFirstName() {
 groupAgent.findDependingGroupByNames(null, "LastName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullLastName() {
 groupAgent.findDependingGroupByNames("FirstName", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesInvalidNames() {
 groupAgent.findDependingGroupByNames("InvalidName", "InvalidName");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 GroupAgent parallelGroup = new ParallelGroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // implementation of the abstract method
 return null;
 }
 };
 parallelGroup.setFirstName("ParallelGroupName");
 parallelGroup.setLastName("ParallelGroupName");
 groupAgent.addAgent(parallelGroup);
 assertEquals(parallelGroup, groupAgent.findDependingParallelGroupByNames("ParallelGroupName", "ParallelGroupName"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 GroupAgent sequentialGroup = new SequentialGroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // implementation of the abstract method
 return null;
 }
 };
 sequentialGroup.setFirstName("SequentialGroupName");
 sequentialGroup.setLastName("SequentialGroupName");
 groupAgent.addAgent(sequentialGroup);
 assertEquals(sequentialGroup, groupAgent.findDependingSequentialGroupByNames("SequentialGroupName", "SequentialGroupName"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.addAgent(new TemporaryGapAgent(1));
 groupAgent.addAgent(child1);
 groupAgent.addAgent(new TemporaryGapAgent(1));
 groupAgent.addAgent(child2);
 groupAgent.addAgent(new TemporaryGapAgent(1));
 groupAgent.removeGapsFromParallelGroups();
 List<Agent> children = groupAgent.getChildren();
 assertEquals(3, children.size());
 assertEquals(child1.getClass(), children.get(1).getClass());
 assertEquals(child2.getClass(), children.get(2).getClass());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgent.TemporaryGapsToGaps map = new GroupAgent.TemporaryGapsToGaps() {
 @Override
 public Agent gap(TemporaryGapAgent temporaryGapAgent) {
 return new GapAgent(temporaryGapAgent.getSize());
 }
 };
 groupAgent.addAgent(new TemporaryGapAgent(1));
 groupAgent.addAgent(child1);
 groupAgent.addAgent(new TemporaryGapAgent(1));
 groupAgent.addAgent(child2);
 groupAgent.addAgent(new TemporaryGapAgent(1));
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(3, children.size());
 assertEquals(child1.getClass(), children.get(1).getClass());
 assertEquals(child2.getClass(), children.get(2).getClass());
 }

 @Test
 public void testAddContentToGroup() {
 Group agentToAdd = new Agent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // implementation of the abstract method
 return null;
 }
 };
 Group agentGroup = new Group();
 creatorAndAdder.addGroupToGroup(agentToAdd.groupContent(creatorAndAdder), settings, agentGroup);
 groupAgent.addContentToGroup(agentGroup, creatorAndAdder);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(4, children.size());
 assertTrue(children.get(3) instanceof Group);
 }

 @Test
 public void testAddGapBefore() {
 GroupAgent gapAgent = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // implementation of the abstract method
 return null;
 }
 };
 gapAgent.addAgent(new GapAgent(1));
 groupAgent.addAgent(gapAgent);
 groupAgent.addAgent(child1);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(4, children.size());
 List<Agent> expected = new ArrayList<>();
 expected.add(child1);
 expected.add(gapAgent);
 expected.add(child2);
 expected.add(child3);
 assertTrue(children.subList(0, 3).containsAll(expected));
 }

 @Test
 public void testAddGapAfter() {
 GroupAgent gapAgent = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // implementation of the abstract method
 return null;
 }
 };
 groupAgent.addAgent(child1);
 groupAgent.addAgent(gapAgent);
 groupAgent.addAgent(child2);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(4, children.size());
 List<Agent> expected = new ArrayList<>();
 expected.add(child1);
 expected.add(child2);
 expected.add(gapAgent);
 expected.add(child3);
 assertTrue(children.subList(0, 3).containsAll(expected));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapBeforeNullAgent() {
 groupAgent.addAgent(null);
 groupAgent.addGapBefore(new GapAgent(1), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapAfterNullAgent() {
 groupAgent.addAgent(null);
 groupAgent.addGapAfter(new GapAgent(1), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapBeforeNullGap() {
 groupAgent.addAgent(child1);
 groupAgent.addGapBefore(null, child1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapAfterNullGap() {
 groupAgent.addAgent(child1);
 groupAgent.addGapAfter(null, child1);
 }

 @Test
 public void testAddGapBeforeElementNotExist() {
 groupAgent.addAgent(child1);
 assertFalse(groupAgent.contains(child2));
 groupAgent.addGapBefore(new GapAgent(1), child2);
 assertFalse(groupAgent.contains(child2));
 }

 @Test
 public void testAddGapAfterElementNotExist() {
 groupAgent.addAgent(child1);
 assertFalse(groupAgent.contains(child2));
 groupAgent.addGapAfter(new GapAgent(1), child2);
 assertFalse(groupAgent.contains(child2));
 }
}