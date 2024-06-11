package glengineer.agents.tests;

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
import glengineer.agents.setters.ComponentAgentSetter;
import glengineer.agents.setters.GroupAgentSetter;
import glengineer.agents.setters.TemporaryGapAgentSetter;
import glengineer.agents.GapAgent;
import glengineer.exceptions.IllegalArgumentException;
import glengineer.layout.GroupLayout.Group;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent agent1;
 private Agent agent2;
 private Agent agent3;
 private Agent agent4;
 private Agent agent5;
 private List<Agent> expectedList;

 @Before
 public void setup() {
 groupAgent = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 agent1 = new ComponentAgentSetter().setName("agent1").createAgent();
 agent2 = new GroupAgentSetter().setName("agent2").createAgent();
 agent3 = new GroupAgentSetter().setName("agent3").createAgent();
 agent4 = new TemporaryGapAgentSetter().setSize(1).createAgent();
 agent5 = new ComponentAgentSetter().setName("agent5").createAgent();
 expectedList = new ArrayList<Agent>() {{
 add(agent1);
 add(agent4);
 add(agent2);
 add(agent5);
 }};
 groupAgent.addAgent(agent1);
 groupAgent.addAgent(agent2);
 groupAgent.addAgent(agent3);
 }

 @Test
 public void testGetFirstName() {
 assertEquals(agent1.getFirstName(), groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals(agent2.getLastName(), groupAgent.getLastName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFirstNameWhenNoChildren() {
 groupAgent.removeAllAgents();
 assertNull(groupAgent.getFirstName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetLastNameWhenNoChildren() {
 groupAgent.removeAllAgents();
 assertNull(groupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("nonExistentComponent"));
 }

 @Test
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup(groupAgent.getFirstName(), groupAgent.getLastName()));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent agentToCompare = new GroupAgent() {{
 settings = new Settings();
 childrenAgents = new LinkedList<Agent>() {{
 add(agent1);
 add(agent2);
 add(agent3);
 }};
 }};
 assertTrue(groupAgent.equals(agentToCompare));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(agent5);
 assertEquals(expectedList.size(), groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.getChildren().containsAll(expectedList));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 assertEquals(expectedList, groupAgent.getChildren());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(expectedList.size(), groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWhenComponentDoesNotExist() {
 groupAgent.getComponent("nonExistentComponent");
 }

 @Test
 public void testGetGroupWhenGroupDoesNotExist() {
 groupAgent.addAgent(agent4);
 try {
 groupAgent.getGroup("nonExistentFirstName", "nonExistentLastName");
 fail("should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindDependingComponentByName() {
 assertEquals(agent1, groupAgent.findDependingComponentByName("agent1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWhenComponentDoesNotExist() {
 groupAgent.findDependingComponentByName("nonExistentComponent");
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent groupToReturn = (GroupAgent)agent2;
 assertEquals(groupToReturn, groupAgent.findDependingGroupByNames("agent2", "agent2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWhenGroupDoesNotExist() {
 groupAgent.findDependingGroupByNames("nonExistentFirstName", "nonExistentLastName");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 GroupAgent parallelGroupToReturn = new ParallelGroupAgent() {{
 settings = new Settings();
 childrenAgents = new LinkedList<Agent>() {{
 add(agent3);
 add(agent4);
 add(agent5);
 }};
 }};
 assertEquals(parallelGroupToReturn, groupAgent.findDependingParallelGroupByNames("agent3", "agent3"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWhenParallelGroupDoesNotExist() {
 groupAgent.findDependingParallelGroupByNames("nonExistentFirstName", "nonExistentLastName");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 GroupAgent sequentialGroupToReturn = new SequentialGroupAgent() {{
 settings = new Settings();
 childrenAgents = new LinkedList<Agent>() {{
 add(agent2);
 add(agent4);
 add(agent5);
 }};
 }};
 assertEquals(sequentialGroupToReturn, groupAgent.findDependingSequentialGroupByNames("agent2", "agent2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWhenSequentialGroupDoesNotExist() {
 groupAgent.findDependingSequentialGroupByNames("nonExistentFirstName", "nonExistentLastName");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.addAgent(agent4);
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(expectedList.size() - 1, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 TestTemporaryGapsToGaps map = new TestTemporaryGapsToGaps();
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 assertEquals(expectedList, groupAgent.getChildren());
 }

 @Test
 public void testAddGapBefore() {
 groupAgent.addAgent(agent1);
 groupAgent.addGapBefore(agent4, agent1);
 assertEquals(expectedList.size(), groupAgent.getNumberOfChildren());
 assertEquals(agent4, groupAgent.getChildren().get(1));
 }

 @Test
 public void testAddGapAfter() {
 groupAgent.addAgent(agent1);
 groupAgent.addAgent(agent2);
 groupAgent.addGapAfter(agent4, agent2);
 assertEquals(expectedList.size(), groupAgent.getNumberOfChildren());
 assertEquals(agent4, groupAgent.getChildren().get(2));
 }
}

 class TestTemporaryGapsToGaps implements GroupLayoutEngineer.TemporaryGapsToGaps {
 private final Map<TemporaryGapAgent, Agent> map = new HashMap<>();

 @Override
 public Agent gap(TemporaryGapAgent gap) {
 Agent agent = map.get(gap);
 if (agent == null) {
 agent = new GapAgent(gap.getSize());
 map.put(gap, agent);
 }
 return agent;
 }
}