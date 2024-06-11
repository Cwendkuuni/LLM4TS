package glengineer.agents.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.SettingsSetter;
import glengineer.agents.GroupAgentTestUtils;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.GapAgent;
import glengineer.agents.TemporaryGapAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Settings settings;
 private List<Agent> listOfChildren;
 private AgentSetter agentSetter;
 private SettingsSetter settingsSetter;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
 return GroupAgentTestUtils.getTestGroupContent();
 }
 };
 settings = new Settings();
 listOfChildren = new ArrayList<>();
 agentSetter = new AgentSetter();
 settingsSetter = new SettingsSetter();
 }

 @After
 public void tearDown() {
 groupAgent = null;
 settings = null;
 listOfChildren.clear();
 agentSetter = null;
 settingsSetter = null;
 }

 @Test
 public void testGetFirstName() {
 Agent child1 = agentSetter.createAgentWithFirstName("John");
 Agent child2 = agentSetter.createAgentWithFirstName("Mike");
 agentSetter.setFirstNameForAgent(child1, "John");
 agentSetter.setFirstNameForAgent(child2, "Mike");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertEquals("John", groupAgent.getFirstName());
 }

 @Test
 public void testGetFirstName_whenNoChildren_thenNull() {
 assertEquals(null, groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 Agent child1 = agentSetter.createAgentWithLastName("Doe");
 Agent child2 = agentSetter.createAgentWithLastName("Smith");
 agentSetter.setLastNameForAgent(child1, "Doe");
 agentSetter.setLastNameForAgent(child2, "Smith");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertEquals("Doe", groupAgent.getLastName());
 }

 @Test
 public void testGetLastName_whenNoChildren_thenNull() {
 assertEquals(null, groupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("componentName"));
 }

 @Test
 public void testIsGroup() {
 Agent child1 = agentSetter.createAgentWithFirstName("John").setLastName("Doe");
 Agent child2 = agentSetter.createAgentWithFirstName("Mike").setLastName("Smith");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGroup_whenNoChildren_thenFalse() {
 assertFalse(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 Agent child1 = agentSetter.createAgentWithFirstName("John").setLastName("Doe");
 Agent child2 = agentSetter.createAgentWithFirstName("John").setLastName("Doe");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
assertTrue(groupAgent.equals(groupAgent));
 assertTrue(groupAgent.equals(groupAgent));
 assertTrue(groupAgent.equals(groupAgent));
 }

 @Test
 public void testEquals_whenDifferentGroupAgents_thenFalse() {
 GroupAgent anotherGroupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
 return GroupAgentTestUtils.getTestGroupContent();
 }
 };
 assertFalse(groupAgent.equals(anotherGroupAgent));
 }

 @Test
 public void testEquals_whenDifferentObjects_thenFalse() {
 String testString = "testString";
 assertFalse(groupAgent.equals(testString));
 }

 @Test
 public void testAddAgent() {
 Agent child = agentSetter.createAgent();
 groupAgent.addAgent(child);
 assertTrue(groupAgent.getChildren().contains(child));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAgent_whenNullAgent_thenExceptionThrown() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
 return GroupAgentTestUtils.getTestGroupContent();
 }
 };
 Agent nullAgent = null;
 assertFalse(groupAgent.addAgent(nullAgent));
 }

 @Test
 public void testGetChildren() {
 List<Agent> expectedChildren = Arrays.asList(createAgentWithFirstName("John"), createAgentWithFirstName("Mike"));
 groupAgent.addAgent(createAgentWithFirstName("John"));
 groupAgent.addAgent(createAgentWithFirstName("Mike"));
 assertEquals(expectedChildren, groupAgent.getChildren());
 }

 @Test
 public void testGetNumberOfChildren() {
 groupAgent.addAgent(createAgentWithFirstName("John"));
 groupAgent.addAgent(createAgentWithFirstName("Mike"));
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 Agent componentAgent = createComponentAgentWithName("TestComponent");
 groupAgent.addAgent(componentAgent);
 assertEquals(componentAgent, groupAgent.getComponent("TestComponent"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponent_whenComponentNameNotFound_thenExceptionThrown() {
 Agent componentAgent = createComponentAgentWithName("TestComponent");
 groupAgent.addAgent(componentAgent);
 assertThrows(IllegalArgumentException.class, () -> groupAgent.getComponent("NonExistentComponentName"));
 }

 @Test
 public void testGetGroup() {
 Agent groupAgentChild1 = createAgentWithFirstName("Child1").setLastName("ChildLastName1");
 Agent groupAgentChild2 = createAgentWithFirstName("Child1").setLastName("ChildLastName2");
 groupAgent.addAgent(groupAgentChild1);
 groupAgent.addAgent(groupAgentChild2);
 assertEquals(groupAgentChild1, groupAgent.getGroup("Child1", "ChildLastName1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroup_whenGroupWithSameFirstNameButDifferentLastNameNotFound_thenExceptionThrown() {
 Agent groupAgentChild1 = createAgentWithFirstName("Child1").setLastName("ChildLastName1");
 Agent groupAgentChild2 = createAgentWithFirstName("Child1").setLastName("ChildLastName2");
 groupAgent.addAgent(groupAgentChild1);
 groupAgent.addAgent(groupAgentChild2);
 assertThrows(IllegalArgumentException.class, () -> groupAgent.getGroup("Child1", "ChildLastName3"));
 }

 @Test
 public void testFindDependingComponentByName() {
 Agent componentAgent = createComponentAgentWithName("TestComponent");
 groupAgent.addAgent(componentAgent);
 Agent childAgent = createAgentWithFirstName("Child");
 groupAgent.addAgent(childAgent);
 assertEquals(componentAgent, groupAgent.findDependingComponentByName("TestComponent"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByName_whenComponentNameNotFound_thenExceptionThrown() {
 Agent componentAgent = createComponentAgentWithName("TestComponent");
 groupAgent.addAgent(componentAgent);
 Agent childAgent = createAgentWithFirstName("Child");
 groupAgent.addAgent(childAgent);
 assertThrows(IllegalArgumentException.class, () -> groupAgent.findDependingComponentByName("NonExistentComponentName"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 Agent groupAgentChild1 = createAgentWithFirstName("Child1").setLastName("ChildLastName1");
 Agent groupAgentChild2 = createAgentWithFirstName("Child1").setLastName("ChildLastName2");
 groupAgent.addAgent(groupAgentChild1);
 groupAgent.addAgent(groupAgentChild2);
 assertEquals(groupAgentChild1, groupAgent.findDependingGroupByNames("Child1", "ChildLastName1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNames_whenGroupWithSameFirstNameButDifferentLastNameNotFound_thenExceptionThrown() {
 Agent groupAgentChild1 = createAgentWithFirstName("Child1").setLastName("ChildLastName1");
 Agent groupAgentChild2 = createAgentWithFirstName("Child1").setLastName("ChildLastName2");
 groupAgent.addAgent(groupAgentChild1);
 groupAgent.addAgent(groupAgentChild2);
 assertThrows(IllegalArgumentException.class, () -> groupAgent.findDependingGroupByNames("Child1", "ChildLastName3"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 ParallelGroupAgent parallelGroupAgentChild1 = new ParallelGroupAgent();
 ParallelGroupAgent parallelGroupAgentChild2 = new ParallelGroupAgent();
 groupAgent.addAgent(parallelGroupAgentChild1);
 groupAgent.addAgent(parallelGroupAgentChild2);
 assertEquals(parallelGroupAgentChild1, groupAgent.findDependingParallelGroupByNames("ParallelGroupAgentName", "ParallelGroupAgentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNames_whenParallelGroupWithSameNameButDifferentInstance_thenExceptionThrown() {
 ParallelGroupAgent parallelGroupAgentChild1 = new ParallelGroupAgent();
 ParallelGroupAgent parallelGroupAgentChild2 = new ParallelGroupAgent();
 groupAgent.addAgent(parallelGroupAgentChild1);
 groupAgent.addAgent(parallelGroupAgentChild2);
 assertThrows(IllegalArgumentException.class, () -> groupAgent.findDependingParallelGroupByNames("ParallelGroupAgentName", "ParallelGroupAgentName"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 SequentialGroupAgent sequentialGroupAgentChild1 = new SequentialGroupAgent();
 SequentialGroupAgent sequentialGroupAgentChild2 = new SequentialGroupAgent();
 groupAgent.addAgent(sequentialGroupAgentChild1);
 groupAgent.addAgent(sequentialGroupAgentChild2);
 assertEquals(sequentialGroupAgentChild1, groupAgent.findDependingSequentialGroupByNames("SequentialGroupAgentName", "SequentialGroupAgentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNames_whenSequentialGroupWithSameNameButDifferentInstance_thenExceptionThrown() {
 SequentialGroupAgent sequentialGroupAgentChild1 = new SequentialGroupAgent();
 SequentialGroupAgent sequentialGroupAgentChild2 = new SequentialGroupAgent();
 groupAgent.addAgent(sequentialGroupAgentChild1);
 groupAgent.addAgent(sequentialGroupAgentChild2);
 assertThrows(IllegalArgumentException.class, () -> groupAgent.findDependingSequentialGroupByNames("SequentialGroupAgentName", "SequentialGroupAgentName"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
 return GroupAgentTestUtils.getTestGroupContentWithGaps();
 }
 };
 groupAgent.removeGapsFromParallelGroups();
 List<Agent> expectedChildrenAfterRemoveGaps = new ArrayList<>(Arrays.asList(createAgentWithFirstName("Child1"), createAgentWithFirstName("Child2"), createAgentWithFirstName("Child3")));
 assertEquals(expectedChildrenAfterRemoveGaps, groupAgent.getChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
 return GroupAgentTestUtils.getTestGroupContentWithTemporaryGaps();
 }
 };
 GroupAgentTestUtils.TemporaryGapsToGaps map = new GroupAgentTestUtils.TemporaryGapsToGaps();
 map.put("TemporaryGap1", new GapAgent(20));
 map.put("TemporaryGap2", new GapAgent(10, 5, 15));
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 List<Agent> expectedChildrenAfterReplaceGaps = new ArrayList<>(Arrays.asList(createAgentWithFirstName("Child1"), new GapAgent(20), new GapAgent(10, 5, 15), createAgentWithFirstName("Child3")));
 assertEquals(expectedChildrenAfterReplaceGaps, groupAgent.getChildren());
 }

 @Test
 public void testAddPrecedingGap() {
 GroupAgent groupAgent = new GroupAgent();
 Agent childAgent = createAgentWithFirstName("Child");
 groupAgent.addAgent(childAgent);
 GroupAgentTestUtils.FunctionsOnGroupAndElement functionsOnGroupAndElement = new GroupAgentTestUtils.FunctionsOnGroupAndElementImplementation(groupAgent, childAgent);
 functionsOnGroupAndElement.addPrecedingGap(20);
 List<Agent> expectedChildrenWithGap = new ArrayList<>(Arrays.asList(new GapAgent(20), childAgent));
 assertEquals(expectedChildrenWithGap, groupAgent.getChildren());
 }

 @Test
 public void testAddPrecedingGap_whenNoChildren_thenAddGapToEmptyList() {
 GroupAgent groupAgent = new GroupAgent();
 GroupAgentTestUtils.FunctionsOnGroupAndElement functionsOnGroupAndElement = new GroupAgentTestUtils.FunctionsOnGroupAndElementImplementation(groupAgent, null);
 functionsOnGroupAndElement.addPrecedingGap(20);
 List<Agent> expectedChildrenWithGap = new ArrayList<>(Collections.singletonList(new GapAgent(20)));
 assertEquals(expectedChildrenWithGap, groupAgent.getChildren());
 }

 @Test
 public void testAddFollowingGap() {
 GroupAgent groupAgent = new GroupAgent();
 Agent childAgent = createAgentWithFirstName("Child");
 groupAgent.addAgent(childAgent);
 GroupAgentTestUtils.FunctionsOnGroupAndElement functionsOnGroupAndElement = new GroupAgentTestUtils.FunctionsOnGroupAndElementImplementation(groupAgent, childAgent);
 functionsOnGroupAndElement.addFollowingGap(20);
 List<Agent> expectedChildrenWithGap = new ArrayList<>(Arrays.asList(childAgent, new GapAgent(20)));
 assertEquals(expectedChildrenWithGap, groupAgent.getChildren());
 }

 @Test
 public void testAddFollowingGap_whenNoChildren_thenAddGapToEmptyList() {
 GroupAgent groupAgent = new GroupAgent();
 GroupAgentTestUtils.FunctionsOnGroupAndElement functionsOnGroupAndElement = new GroupAgentTestUtils.FunctionsOnGroupAndElementImplementation(groupAgent, null);
 functionsOnGroupAndElement.addFollowingGap(20);
 List<Agent> expectedChildrenWithGap = new ArrayList<>(Collections.singletonList(new GapAgent(20)));
 assertEquals(expectedChildrenWithGap, groupAgent.getChildren());
 }
}