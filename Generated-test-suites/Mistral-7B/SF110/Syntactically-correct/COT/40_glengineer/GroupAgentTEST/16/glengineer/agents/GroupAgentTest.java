package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.settings.SettingsImpl;
import glengineer.agents.utils.AgentFactory;
import glengineer.agents.utils.AgentType;
import glengineer.agents.utils.GroupLayout.Group;
import glengineer.agents.utils.TemporaryGapAgent;
import glengineer.agents.utils.functions.FunctionsOnGroupAndElement;
import glengineer.agents.utils.functions.FunctionsOnGroupAndElement.FunctionsOnGroupAndElementImplementation;
import glengineer.exceptions.IllegalArgumentException;
import glengineer.layoutengine.GapAgent;
import glengineer.layoutengine.ParallelGroupAgent;
import glengineer.layoutengine.SequentialGroupAgent;

public class GroupAgentTest {

 private GroupAgent testGroupAgent;
 private Agent agent1, agent2, agent3, agent4, agent5, groupAgent1, groupAgent2, gapAgent1, gapAgent2;
 private Settings settings;
 private FunctionsOnGroupAndElementImplementation functionsOnGroupAndElement;

 @Before
 public void setUp() {
 testGroupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // TODO - implement and return
 return null;
 }
 };
 testGroupAgent.setSettings(new SettingsImpl());
 settings = testGroupAgent.getSettings();
 agent1 = AgentFactory.createAgent(AgentType.SEQUENTIAL, "agent1");
 agent2 = AgentFactory.createAgent(AgentType.SEQUENTIAL, "agent2");
 agent3 = AgentFactory.createAgent(AgentType.PARALLEL, "agent3");
 agent4 = AgentFactory.createAgent(AgentType.SEQUENTIAL, "agent4");
 agent5 = AgentFactory.createAgent(AgentType.SEQUENTIAL, "agent5");
 groupAgent1 = AgentFactory.createAgent(AgentType.GROUP, "groupAgent1");
 groupAgent2 = AgentFactory.createAgent(AgentType.GROUP, "groupAgent2");
 gapAgent1 = AgentFactory.createAgent(AgentType.GAP, "gapAgent1");
 gapAgent2 = AgentFactory.createAgent(AgentType.GAP, "gapAgent2");
 }

 @Test
 public void testGetFirstName() {
 List<Agent> children = new LinkedList<>();
 children.add(agent1);
 children.add(agent2);
 testGroupAgent.setChildren(children);
 assertEquals(agent1.getFirstName(), testGroupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 List<Agent> children = new LinkedList<>();
 children.add(agent3);
 children.add(agent4);
 testGroupAgent.setChildren(children);
 assertEquals(agent3.getLastName(), testGroupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(testGroupAgent.isComponent("componentName"));
 }

 @Test
 public void testIsGroup() {
 List<Agent> children = new LinkedList<>();
 children.add(agent1);
 children.add(agent2);
 testGroupAgent.setChildren(children);
 assertTrue(testGroupAgent.isGroup("agent1", "agent2"));
 }

 @Test
 public void testIsGap() {
 assertFalse(testGroupAgent.isGap());
 }

 @Test
 public void testEquals() {
 List<Agent> children = new LinkedList<>();
 children.add(agent1);
 children.add(agent2);
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // TODO - implement and return
 return null;
 }
 };
 groupAgent.setSettings(settings);
 groupAgent.setChildren(children);
 assertTrue(testGroupAgent.equals(groupAgent));
 }

 @Test
 public void testAddAgent() {
 assertThrows(IllegalArgumentException.class, () -> testGroupAgent.addAgent(null));
 List<Agent> children = new LinkedList<>();
 children.add(agent2);
 testGroupAgent.setChildren(children);
 testGroupAgent.addAgent(agent1);
 List<Agent> result = testGroupAgent.getChildren();
 assertTrue(result.contains(agent1));
 assertTrue(result.contains(agent2));
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = new LinkedList<>();
 children.add(agent3);
 children.add(agent4);
 testGroupAgent.setChildren(children);
 List<Agent> result = testGroupAgent.getChildren();
 assertEquals(children, result);
 }

 @Test
 public void testGetNumberOfChildren() {
 List<Agent> children = new LinkedList<>();
 children.add(agent3);
 children.add(agent4);
 testGroupAgent.setChildren(children);
 assertEquals(2, testGroupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 List<Agent> children = new LinkedList<>();
 children.add(agent1);
 children.add(agent3);
 children.add(agent5);
 testGroupAgent.setChildren(children);
 ComponentAgent result = testGroupAgent.getComponent("agent1");
 assertEquals(agent1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentNotFound() {
 List<Agent> children = new LinkedList<>();
 children.add(agent1);
 children.add(agent3);
 children.add(agent4);
 testGroupAgent.setChildren(children);
 testGroupAgent.getComponent("agent5");
 }

 @Test
 public void testGetGroup() {
 List<Agent> children = Arrays.asList(agent1, groupAgent1, agent3, groupAgent2, agent5);
 testGroupAgent.setChildren(children);
 GroupAgent result = testGroupAgent.getGroup("agent1", "groupAgent1");
 assertEquals(groupAgent1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNotFound() {
 List<Agent> children = Arrays.asList(agent1, agent3, agent4, agent5);
 testGroupAgent.setChildren(children);
 testGroupAgent.getGroup("agent6", "groupAgent6");
 }

 @Test
 public void testFindDependingComponentByName() {
 List<Agent> children = new LinkedList<>();
 children.add(agent1);
 children.add(agent3);
 children.add(agent5);
 testGroupAgent.setChildren(children);
 assertEquals(agent1, testGroupAgent.findDependingComponentByName("agent1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameNotFound() {
 List<Agent> children = new LinkedList<>();
 children.add(agent1);
 children.add(agent3);
 children.add(agent5);
 testGroupAgent.setChildren(children);
 testGroupAgent.findDependingComponentByName("agent6");
 }

 @Test
 public void testFindDependingGroupByNames() {
 List<Agent> children = Arrays.asList(agent1, groupAgent1, agent3, groupAgent2, agent5);
 testGroupAgent.setChildren(children);
 GroupAgent result = testGroupAgent.findDependingGroupByNames("agent1", "groupAgent1");
 assertEquals(groupAgent1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNotFound() {
 List<Agent> children = Arrays.asList(agent1, agent3, groupAgent1, agent5);
 testGroupAgent.setChildren(children);
 testGroupAgent.findDependingGroupByNames("agent6", "groupAgent6");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 List<Agent> children = Arrays.asList(agent1, groupAgent1, parallelGroupAgent1, groupAgent2, agent5);
 testGroupAgent.setChildren(children);
 ParallelGroupAgent result = testGroupAgent.findDependingParallelGroupByNames("agent1", "groupAgent1");
 assertEquals(parallelGroupAgent1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesNotFound() {
 List<Agent> children = Arrays.asList(agent1, agent3, groupAgent1, agent5);
 testGroupAgent.setChildren(children);
 testGroupAgent.findDependingParallelGroupByNames("agent6", "groupAgent6");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 List<Agent> children = Arrays.asList(agent1, groupAgent1, sequentialGroupAgent1, groupAgent2, agent5);
 testGroupAgent.setChildren(children);
 SequentialGroupAgent result = testGroupAgent.findDependingSequentialGroupByNames("agent1", "groupAgent1");
 assertEquals(sequentialGroupAgent1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesNotFound() {
 List<Agent> children = Arrays.asList(agent1, agent3, groupAgent1, agent5);
 testGroupAgent.setChildren(children);
 testGroupAgent.findDependingSequentialGroupByNames("agent6", "groupAgent6");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 List<Agent> children = Arrays.asList(parallelGroupAgent1, sequentialGroupAgent1, gapAgent1, groupAgent1, gapAgent2, groupAgent2);
 testGroupAgent.setChildren(children);
 testGroupAgent.removeGapsFromParallelGroups();
 List<Agent> expectedResult = Arrays.asList(sequentialGroupAgent1, groupAgent1, groupAgent2);
 assertEquals(expectedResult, testGroupAgent.getChildren());
 }

 @Test
 public void testPassTheCallToSubgroups() {
 List<Agent> children = Arrays.asList(groupAgent1, groupAgent2);
 testGroupAgent.setChildren(children);
 for (Agent child : children) {
 if (child instanceof GroupAgent) {
 ((GroupAgent)child).removeGapsFromParallelGroups();
 }
 }
 }

 @Test
 public void testRemoveAllGapsFromParallelSubgroups() {
 List<Agent> children = Arrays.asList(parallelGroupAgent1, gapAgent1, parallelGroupAgent2, gapAgent2);
 testGroupAgent.setChildren(children);
 for (Agent child : children) {
 if (child instanceof ParallelGroupAgent) {
 ((ParallelGroupAgent)child).removeAllGaps();
 }
 }
 List<Agent> expectedResult = Arrays.asList(parallelGroupAgent1);
 assertEquals(expectedResult, testGroupAgent.getChildren());
 }

 @Test
 public void testReplaceParallelSubgroupsWithGapsByTheirSingleGaps() {
 List<Agent> children = Arrays.asList(parallelGroupAgent1, gapAgent1, parallelGroupAgent2, gapAgent2);
 testGroupAgent.setChildren(children);
 testGroupAgent.replaceParallelSubgroupsWithGapsByTheirSingleGaps();
 List<Agent> expectedResult = Arrays.asList(gapAgent1, gapAgent2);
 assertEquals(expectedResult, testGroupAgent.getChildren());
 }

 @Test
 public void testReplaceContinuousSequencesOfGapsBySingleGaps() {
 List<Agent> children = Arrays.asList(gapAgent1, gapAgent2, gapAgent3, gapAgent4, gapAgent5);
 testGroupAgent.setChildren(children);
 testGroupAgent.replaceContinuousSequencesOfGapsBySingleGaps();
 List<Agent> expectedResult = Arrays.asList(gapAgent1, gapAgent5);
 assertEquals(expectedResult, testGroupAgent.getChildren());
 }

 @Test
 public void testReplaceGroupsWithSingleElementsByThatElements() {
 List<Agent> children = Arrays.asList(groupAgent1, groupAgent2, groupAgent3);
 testGroupAgent.setChildren(children);
 testGroupAgent.replaceGroupsWithSingleElementsByThatElements();
 List<Agent> expectedResult = Arrays.asList(groupAgent3);
 assertEquals(expectedResult, testGroupAgent.getChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgentTest.TemporaryGapsToGaps map = new GroupAgentTest.TemporaryGapsToGaps();
 map.put(gapAgent1, new GapAgent(10));
 map.put(gapAgent2, new GapAgent(20));
 testGroupAgent.replaceTemporaryGapsByRealGaps(map);
 List<Agent> expectedResult = Arrays.asList(new GapAgent(10), agent1, new GapAgent(20), agent3, agent5);
 assertEquals(expectedResult, testGroupAgent.getChildren());
 }

 @Test
 public void testAddPrecedingGap() {
 functionsOnGroupAndElement = new FunctionsOnGroupAndElementImplementation(testGroupAgent, agent3);
 functionsOnGroupAndElement.addPrecedingGap(10);
 assertEquals(gapAgent1 = new GapAgent(10), testGroupAgent.getChildren().get(0));
 }

 @Test
 public void testAddPrecedingGapWithMinPrefMax() {
 functionsOnGroupAndElement = new FunctionsOnGroupAndElementImplementation(testGroupAgent, agent3);
 functionsOnGroupAndElement.addPrecedingGap(5, 10, 15);
 assertEquals(gapAgent1 = new GapAgent(5, 10, 15), testGroupAgent.getChildren().get(0));
 }

 @Test
 public void testAddFollowingGap() {
 functionsOnGroupAndElement = new FunctionsOnGroupAndElementImplementation(testGroupAgent, agent3);
 functionsOnGroupAndElement.addFollowingGap(10);
 assertEquals(agent3, testGroupAgent.getChildren().get(1));
 assertEquals(gapAgent2 = new GapAgent(10), testGroupAgent.getChildren().get(2));
 }

 @Test
 public void testAddFollowingGapWithMinPrefMax() {
 functionsOnGroupAndElement = new FunctionsOnGroupAndElementImplementation(testGroupAgent, agent3);
 functionsOnGroupAndElement.addFollowingGap(5, 10, 15);
 assertEquals(agent3, testGroupAgent.getChildren().get(1));
 assertEquals(gapAgent2 = new GapAgent(5, 10, 15), testGroupAgent.getChildren().get(2));
 }
}

 class TemporaryGapsToGaps {
 private Map<TemporaryGapAgent, Agent> map;

 TemporaryGapsToGaps() {
 map = new HashMap<>();
 }

 void put(TemporaryGapAgent temporaryGapAgent, Agent agent) {
 map.put(temporaryGapAgent, agent);
 }

 Agent gap(TemporaryGapAgent temporaryGapAgent) {
 return map.get(temporaryGapAgent);
 }
}