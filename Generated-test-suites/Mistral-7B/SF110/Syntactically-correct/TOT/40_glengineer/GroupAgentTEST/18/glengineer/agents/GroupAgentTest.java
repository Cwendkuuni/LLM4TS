package glengineer.agents.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.Agent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.TemporaryGapAgent;
import glengineer.agents.setters.GapAgent;
import glengineer.agents.GroupCreatorAndElementAdder;
import glengineer.agents.ParallelGroupAgent;
import glengineer.agents.SequentialGroupAgent;
import glengineer.layout.GroupLayout;
import glengineer.layout.GroupLayoutEngineer;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private GroupAgent childGroup1;
 private GroupAgent childGroup2;
 private Agent agent1;
 private Agent agent2;
 private Agent agent3;
 private TemporaryGapAgent gap1;
 private TemporaryGapAgent gap2;
 private Settings settings;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 childGroup1 = new SequentialGroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 childGroup2 = new ParallelGroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 agent1 = new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }
 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 agent2 = new Agent() {
 @Override
 public String getFirstName() {
 return "Jane";
 }
 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 agent3 = new Agent() {
 @Override
 public String getFirstName() {
 return null;
 }
 @Override
 public String getLastName() {
 return null;
 }
 };
 gap1 = new TemporaryGapAgent(10);
 gap2 = new TemporaryGapAgent(20);
 settings = new Settings();
 groupAgent.addAgent(childGroup1);
 groupAgent.addAgent(childGroup2);
 }

 @Test
 public void testGetFirstName() {
 assertEquals("Jane", childGroup1.getFirstName());
 assertEquals(null, groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("Doe", childGroup1.getLastName());
 assertEquals(null, groupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("component1"));
 }

 @Test
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup(agent1.getFirstName(), agent1.getLastName()));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 groupAgent2.addAgent(childGroup1);
 groupAgent2.addAgent(childGroup2);
 assertTrue(groupAgent.equals(groupAgent2));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(agent3);
 assertFalse(groupAgent.childrenAgents.isEmpty());
 assertEquals(agent3, groupAgent.childrenAgents.getLast());
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = Arrays.asList(childGroup1, childGroup2);
 assertEquals(children, groupAgent.getChildren());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("component1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullFirstName() {
 groupAgent.findDependingGroupByNames(null, "Doe");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullLastName() {
 groupAgent.findDependingGroupByNames("John", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullFirstNameAndLastName() {
 groupAgent.findDependingGroupByNames(null, null);
 }

 @Test
 public void testFindDependingGroupByNames() {
 List<Agent> children = Arrays.asList(childGroup1, childGroup2);
 assertEquals(childGroup1, groupAgent.findDependingGroupByNames("John", "Doe"));
 assertEquals(childGroup2, groupAgent.findDependingGroupByNames("Jane", "Doe"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertNull(groupAgent.findDependingParallelGroupByNames("John", "Doe"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertNull(groupAgent.findDependingSequentialGroupByNames("John", "Doe"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.removeGapsFromParallelGroups();
 assertTrue(groupAgent.childrenAgents.isEmpty());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupLayoutEngineer engineer = new GroupLayoutEngineer();
 GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
 map.put(gap1, new GapAgent(10));
 map.put(gap2, new GapAgent(20));
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 assertEquals(Arrays.asList(childGroup1, childGroup2), groupAgent.getChildren());
 }

 @Test
 public void testAddContentToGroup() {
 GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder();
 GroupLayout layout = new GroupLayout();
 groupAgent.addContentToGroup(layout, creatorAndAdder);
 assertEquals(Collections.emptyList(), layout.getComponents());
 }

 @Test
 public void testAddGapBefore() {
 groupAgent.addAgent(agent1);
 groupAgent.addGapBefore(gap1, agent1);
 assertEquals(agent1, groupAgent.childrenAgents.getLast());
 assertEquals(gap1, groupAgent.childrenAgents.getLastPrevious());
 }

 @Test
 public void testAddGapAfter() {
 groupAgent.addAgent(agent1);
 groupAgent.addAgent(agent2);
 groupAgent.addGapAfter(gap1, agent1);
 assertEquals(agent1, groupAgent.childrenAgents.get(0));
 assertEquals(agent2, groupAgent.childrenAgents.get(1));
 assertEquals(gap1, groupAgent.childrenAgents.get(2));
 }

}