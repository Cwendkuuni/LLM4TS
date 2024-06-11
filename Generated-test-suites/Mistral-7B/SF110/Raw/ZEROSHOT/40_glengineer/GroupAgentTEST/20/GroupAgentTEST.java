package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.GroupAgent;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.AgentSetterFactory;
import glengineer.agents.setters.GroupAgentSetter;
import glengineer.agents.setters.GroupAgentSetterFactory;
import glengineer.agents.GroupAgent.GapAgent;
import glengineer.agents.GroupAgent.TemporaryGapAgent;
import glengineer.agents.GroupCreatorAndElementAdder;
import glengineer.agents.ParallelGroupAgent;
import glengineer.agents.SequentialGroupAgent;
import glengineer.agents.GroupLayoutEngineer;
import glengineer.agents.GroupLayout.Group;
import glengineer.layout.Gap;

public class GroupAgentTest {

 private GroupAgent testAgent;
 private Agent testChild1;
 private Agent testChild2;
 private Agent testChild3;
 private ParallelGroupAgent parallelGroup;
 private SequentialGroupAgent sequentialGroup;
 private GroupLayoutEngineer engine;

 @Before
 public void setUp() {
 testAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testChild1 = new AgentSetterFactory().createAgent("Child1");
 testChild2 = new AgentSetterFactory().createAgent("Child2");
 testChild3 = new AgentSetterFactory().createAgent("Child3");
 parallelGroup = new ParallelGroupAgent();
 sequentialGroup = new SequentialGroupAgent();
 engine = new GroupLayoutEngineer();
 }

 @Test
 public void testGetFirstName() {
 assertEquals(testChild1.getFirstName(), testAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals(testChild1.getLastName(), testAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(testAgent.isComponent("Component"));
 }

 @Test
 public void testIsGroup() {
 assertTrue(testAgent.isGroup(testChild1.getFirstName(), testChild1.getLastName()));
 }

 @Test
 public void testIsGap() {
 assertFalse(testAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent otherAgent = new GroupAgent();
 otherAgent.addAgent(testChild1);
 otherAgent.addAgent(testChild2);
 otherAgent.addAgent(testChild3);
 assertEquals(testAgent, otherAgent);
 }

 @Test
 public void testAddAgent() {
 testAgent.addAgent(testChild1);
 assertEquals(1, testAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 testAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = testAgent.getChildren();
 assertEquals(0, children.size());
 testAgent.addAgent(testChild1);
 testAgent.addAgent(testChild2);
 children = testAgent.getChildren();
 assertEquals(2, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(0, testAgent.getNumberOfChildren());
 testAgent.addAgent(testChild1);
 testAgent.addAgent(testChild2);
 assertEquals(2, testAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(testAgent.getComponent("Component"));
 testAgent.addAgent(testChild1);
 testChild1.setComponentName("Component");testAgent.addAgent(testChild2);
 assertEquals(testChild1, testAgent.getComponent("Component"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentNullComponentName() {
 testAgent.getComponent(null);
 }

 @Test
 public void testGetGroup() {
 assertNull(testAgent.getGroup(testChild1.getFirstName(), testChild1.getLastName()));
 testAgent.addAgent(testChild1);testAgent.addAgent(testChild2);
 assertEquals(testAgent, testAgent.getGroup(testChild1.getFirstName(), testChild1.getLastName()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNullFirstName() {
 testAgent.getGroup(null, testChild1.getLastName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNullLastName() {
 testAgent.getGroup(testChild1.getFirstName(), null);
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(testAgent.findDependingComponentByName("Component"));
 testAgent.addAgent(testChild1);testChild1.setComponentName("Component");
 testAgent.addAgent(testChild2);
 assertEquals(testChild1, testAgent.findDependingComponentByName("Component"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameNullComponentName() {
 testAgent.findDependingComponentByName(null);
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertNull(testAgent.findDependingGroupByNames(testChild1.getFirstName(), testChild1.getLastName()));
 testAgent.addAgent(testChild1);testAgent.addAgent(testChild2);
 assertEquals(testAgent, testAgent.findDependingGroupByNames(testChild1.getFirstName(), testChild1.getLastName()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullFirstName() {
 testAgent.findDependingGroupByNames(null, testChild1.getLastName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullLastName() {
 testAgent.findDependingGroupByNames(testChild1.getFirstName(), null);
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertNull(testAgent.findDependingParallelGroupByNames(testChild1.getFirstName(), testChild1.getLastName()));
 testAgent.addAgent(parallelGroup);testAgent.addAgent(testChild1);testAgent.addAgent(testChild2);
 assertEquals(parallelGroup, testAgent.findDependingParallelGroupByNames(testChild1.getFirstName(), testChild1.getLastName()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesNullFirstName() {
 testAgent.findDependingParallelGroupByNames(null, testChild1.getLastName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesNullLastName() {
 testAgent.findDependingParallelGroupByNames(testChild1.getFirstName(), null);
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertNull(testAgent.findDependingSequentialGroupByNames(testChild1.getFirstName(), testChild1.getLastName()));
 testAgent.addAgent(sequentialGroup);testAgent.addAgent(testChild1);testAgent.addAgent(testChild2);
 assertEquals(sequentialGroup, testAgent.findDependingSequentialGroupByNames(testChild1.getFirstName(), testChild1.getLastName()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesNullFirstName() {
 testAgent.findDependingSequentialGroupByNames(null, testChild1.getLastName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesNullLastName() {
 testAgent.findDependingSequentialGroupByNames(testChild1.getFirstName(), null);
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 testAgent.addAgent(parallelGroup);testAgent.addAgent(testChild1);testAgent.addAgent(new GapAgent(10, 5, 15));
 parallelGroup.addAgent(testChild2);parallelGroup.addAgent(new TemporaryGapAgent(10));
 parallelGroup.addAgent(testChild3);
 testAgent.removeGapsFromParallelGroups();
 assertEquals(2, testAgent.getNumberOfChildren());
 assertFalse(parallelGroup.containsGapsOnly());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
 map.put(testChild3, new Gap(10, 5, 15));
 testAgent.addAgent(parallelGroup);testAgent.addAgent(testChild1);testAgent.addAgent(new TemporaryGapAgent(10));
 parallelGroup.addAgent(testChild2);parallelGroup.addAgent(testChild3);
 testAgent.replaceTemporaryGapsByRealGaps(map);
 assertEquals(2, testAgent.getNumberOfChildren());
 assertTrue(parallelGroup.containsGapsOnly());
 }

 @Test
 public void testAddContentToGroup() {
 GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder();
 testAgent.addContentToGroup(engine.getRoot(), creatorAndAdder);
 assertTrue(creatorAndAdder.isGroupAdded());
 }

 @Test
 public void testGroupContent() {
 Group testGroup = null;
 try {
 testGroup = testAgent.groupContent(new GroupCreatorAndElementAdder());
 } catch (Exception e) {
 fail(e.getMessage());
 }
 assertNotNull(testGroup);
 }
}