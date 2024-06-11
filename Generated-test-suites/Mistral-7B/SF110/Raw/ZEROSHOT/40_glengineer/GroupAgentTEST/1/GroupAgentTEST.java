package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.AgentSetterFactory;
import glengineer.agents.setters.GapAgentSetter;
import glengineer.agents.test.testutils.TestUtils;
import glengineer.agents.test.testutils.TestUtilsAgentFactory;
import glengineer.agents.test.testutils.TestUtilsGroupAgentFactory;
import glengineer.agents.test.testutils.TestUtilsSettingsFactory;
import glengineer.agents.test.testutils.TestUtilsTemporaryGapsToGapsMap;
import glengineer.layout.GroupLayout;
import glengineer.layout.GroupLayoutEngineer;
import glengineer.layout.GroupCreatorAndElementAdder;
import glengineer.layout.GroupCreatorAndElementAdderFactory;
import glengineer.layout.ParallelGroupLayout;
import glengineer.layout.SequentialGroupLayout;
import glengineer.layout.components.ComponentAgent;
import glengineer.layout.components.GapAgent;
import glengineer.layout.components.TestUtilsComponentAgentFactory;
import glengineer.layout.groups.GroupAgent;
import glengineer.layout.groups.ParallelGroupAgent;
import glengineer.layout.groups.SequentialGroupAgent;
import glengineer.layout.groups.TestUtilsGroupAgentFactory;
import glengineer.layout.groups.TestUtilsParallelGroupAgentFactory;
import glengineer.layout.groups.TestUtilsSequentialGroupAgentFactory;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private AgentSetter agentSetter;
 private GroupCreatorAndElementAdder groupCreatorAndElementAdder;
 private GroupCreatorAndElementAdderFactory groupCreatorAndElementAdderFactory;
 private GroupLayoutEngineer groupLayoutEngineer;
 private List<Agent> childrenAgents;

 @Before
 public void setUp() {
 childrenAgents = new LinkedList<>();
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 agentSetter = AgentSetterFactory.getInstance();
 groupCreatorAndElementAdderFactory = GroupCreatorAndElementAdderFactory.getInstance();
 groupLayoutEngineer = new GroupLayoutEngineer();
 }

 @Test
 public void testGetFirstName() {
 Agent firstAgent = TestUtilsAgentFactory.createAgentWithFirstName("John");
 childrenAgents.add(firstAgent);
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 String firstName = groupAgent.getFirstName();
 assertEquals("John", firstName);
 }

 @Test
 public void testGetLastName() {
 Agent lastAgent = TestUtilsAgentFactory.createAgentWithLastName("Doe");
 childrenAgents.add(lastAgent);
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 String lastName = groupAgent.getLastName();
 assertEquals("Doe", lastName);
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("test"));
 }

 @Test
 public void testIsGroup() {
 GroupAgent groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 assertTrue(groupAgent.isGroup(groupAgent.getFirstName(), groupAgent.getLastName()));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent anotherGroupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 assertTrue(groupAgent.equals(anotherGroupAgent));
 }

 @Test
 public void testAddAgent() {
 Agent agentToAdd = TestUtilsAgentFactory.createAgent();
 groupAgent.addAgent(agentToAdd);
 assertEquals(1, groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 assertEquals(childrenAgents, groupAgent.getChildren());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(childrenAgents.size(), groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 ComponentAgent componentAgent = TestUtilsComponentAgentFactory.createComponentAgent();
 childrenAgents.add(componentAgent);
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 ComponentAgent result = groupAgent.getComponent("testName");
 assertNull(result);
 componentAgent = TestUtilsComponentAgentFactory.createComponentAgentWithName("testName");
 childrenAgents.set(0, componentAgent);
 result = groupAgent.getComponent("testName");
 assertEquals(componentAgent, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentInvalidName() {
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 String invalidName = "";
 groupAgent.getComponent(invalidName);
 }

 @Test
 public void testGetGroup() {
 GroupAgent groupToFind = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 childrenAgents.add(groupToFind);
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 GroupAgent result = groupAgent.getGroup("testFirstName", "testLastName");
 assertNull(result);
 groupToFind.setFirstName("testFirstName");
 groupToFind.setLastName("testLastName");
 result = groupAgent.getGroup("testFirstName", "testLastName");
 assertEquals(groupToFind, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupInvalidGroup() {
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 String invalidFirstName = "";
 String invalidLastName = "";
 groupAgent.getGroup(invalidFirstName, invalidLastName);
 }

 @Test
 public void testFindDependingComponentByName() {
 ComponentAgent componentAgentToFind = TestUtilsComponentAgentFactory.createComponentAgentWithName("testName");
 childrenAgents.add(componentAgentToFind);
 childrenAgents.add(groupAgent);
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 ComponentAgent result = groupAgent.findDependingComponentByName("testName");
 assertEquals(componentAgentToFind, result);
 }

 @Test
 public void testFindDependingComponentByNameNotFound() {
 ComponentAgent componentAgentToFind = TestUtilsComponentAgentFactory.createComponentAgentWithName("testName");
 childrenAgents.add(componentAgentToFind);
 childrenAgents.add(groupAgent);
 childrenAgents.set(1, new GapAgent());
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 ComponentAgent result = groupAgent.findDependingComponentByName("testName");
 assertNull(result);
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent groupToFind = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 childrenAgents.add(groupToFind);
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 GroupAgent result = groupAgent.findDependingGroupByNames("testFirstName", "testLastName");
 assertNull(result);
 groupToFind.setFirstName("testFirstName");
 groupToFind.setLastName("testLastName");
 result = groupAgent.findDependingGroupByNames("testFirstName", "testLastName");
 assertEquals(groupToFind, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesInvalidGroup() {
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 String invalidFirstName = "";
 String invalidLastName = "";
 groupAgent.findDependingGroupByNames(invalidFirstName, invalidLastName);
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 ParallelGroupAgent parallelGroupAgentToFind = TestUtilsParallelGroupAgentFactory.createParallelGroupAgent();
 childrenAgents.add(parallelGroupAgentToFind);
 childrenAgents.add(groupAgent);
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 ParallelGroupAgent result = groupAgent.findDependingParallelGroupByNames("testFirstName", "testLastName");
 assertNull(result);
 parallelGroupAgentToFind.setFirstName("testFirstName");
 parallelGroupAgentToFind.setLastName("testLastName");
 result = groupAgent.findDependingParallelGroupByNames("testFirstName", "testLastName");
 assertEquals(parallelGroupAgentToFind, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesInvalidGroup() {
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 String invalidFirstName = "";
 String invalidLastName = "";
 groupAgent.findDependingParallelGroupByNames(invalidFirstName, invalidLastName);
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 SequentialGroupAgent sequentialGroupAgentToFind = TestUtilsSequentialGroupAgentFactory.createSequentialGroupAgent();
 childrenAgents.add(sequentialGroupAgentToFind);
 childrenAgents.add(groupAgent);
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 SequentialGroupAgent result = groupAgent.findDependingSequentialGroupByNames("testFirstName", "testLastName");
 assertNull(result);
 sequentialGroupAgentToFind.setFirstName("testFirstName");
 sequentialGroupAgentToFind.setLastName("testLastName");
 result = groupAgent.findDependingSequentialGroupByNames("testFirstName", "testLastName");
 assertEquals(sequentialGroupAgentToFind, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesInvalidGroup() {
 groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 String invalidFirstName = "";
 String invalidLastName = "";
 groupAgent.findDependingSequentialGroupByNames(invalidFirstName, invalidLastName);
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 ParallelGroupAgent parallelGroupAgent = TestUtilsParallelGroupAgentFactory.createParallelGroupAgentWithChildren(childrenAgents);
 GroupAgent groupAgentToTest = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 groupAgentToTest.addAgent(parallelGroupAgent);
 groupAgentToTest.removeGapsFromParallelGroups();
 assertEquals(0, parallelGroupAgent.getNumberOfChildren());
 }

 @Test
 public void testRemoveGapsFromParallelSubgroupsWithOtherContent() {
 ParallelGroupAgent parallelGroupAgentToRemoveFrom = TestUtilsParallelGroupAgentFactory.createParallelGroupAgent();
 parallelGroupAgentToRemoveFrom.addAgent(TestUtilsComponentAgentFactory.createComponentAgent());
 parallelGroupAgentToRemoveFrom.addAgent(TestUtilsGapAgentFactory.createGapAgent());
 parallelGroupAgentToRemoveFrom.addAgent(TestUtilsComponentAgentFactory.createComponentAgent());
 GroupAgent groupAgentToTest = TestUtilsGroupAgentFactory.createGroupAgent();
 groupAgentToTest.addAgent(parallelGroupAgentToRemoveFrom);
 groupAgentToTest.removeGapsFromParallelSubgroupsWithOtherContent();
 assertEquals(1, parallelGroupAgentToRemoveFrom.getNumberOfChildren());
 }

 @Test
 public void testReplaceParallelSubgroupsWithGapsByTheirSingleGaps() {
 ParallelGroupAgent parallelGroupAgentToReplace = TestUtilsParallelGroupAgentFactory.createParallelGroupAgentWithChildren(childrenAgents);
 Agent gapAgentToReplaceWith = TestUtilsGapAgentFactory.createGapAgent();
 groupAgentToTest.addAgent(parallelGroupAgentToReplace);
 groupAgentToTest.replaceParallelSubgroupsWithGapsByTheirSingleGaps();
 assertEquals(1, groupAgentToTest.getNumberOfChildren());
 assertEquals(gapAgentToReplaceWith, groupAgentToTest.getChildren().get(0));
 }

 @Test
 public void testReplaceContinuousSequencesOfGapsBySingleGaps() {
 List<Agent> childrenToModify = new LinkedList<>();
 childrenToModify.add(new GapAgent());
 childrenToModify.add(new GapAgent());
 childrenToModify.add(new GapAgent());
 groupAgentToTest = TestUtilsGroupAgentFactory.createGroupAgent();
 groupAgentToTest.setChildren(childrenToModify);
 groupAgentToTest.replaceContinuousSequencesOfGapsBySingleGaps();
 assertEquals(3, groupAgentToTest.getNumberOfChildren());
 }

 @Test
 public void testReplaceGroupsWithSingleElementsByThatElements() {
 GroupAgent groupAgentToReplace = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 Agent elementAgentToReplaceWith = TestUtilsComponentAgentFactory.createComponentAgent();
 groupAgentToTest.addAgent(groupAgentToReplace);
 groupAgentToTest.replaceGroupsWithSingleElementsByThatElements();
 assertEquals(1, groupAgentToTest.getNumberOfChildren());
 assertEquals(elementAgentToReplaceWith, groupAgentToTest.getChildren().get(0));
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgent groupAgentToTest = TestUtilsGroupAgentFactory.createGroupAgent();
 List<Agent> childrenToModify = new LinkedList<>();
 childrenToModify.add(TestUtilsAgentFactory.createAgent());
 childrenToModify.add(TestUtilsTemporaryGapsToGapsMap.getTemporaryGapAgent());
 childrenToModify.add(TestUtilsAgentFactory.createAgent());
 groupAgentToTest.setChildren(childrenToModify);
 groupAgentToTest.replaceTemporaryGapsByRealGaps(TestUtilsTemporaryGapsToGapsMap.getTemporaryGapsToGapsMap());
 assertEquals(2, groupAgentToTest.getNumberOfChildren());
 assertEquals(TestUtilsAgentFactory.createAgent(), groupAgentToTest.getChildren().get(0));
 assertEquals(TestUtilsAgentFactory.createAgent(), groupAgentToTest.getChildren().get(1));
 }

 @Test
 public void testAddContentToGroup() {
 GroupAgent groupAgentToAddContentTo = TestUtilsGroupAgentFactory.createGroupAgent();
 childrenAgents.add(groupAgentToAddContentTo);
 GroupAgent testGroupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 GroupCreatorAndElementAdder groupCreatorAndElementAdder = groupCreatorAndElementAdderFactory.createGroupCreatorAndElementAdder(groupLayoutEngineer);
 testGroupAgent.addContentToGroup(new ParallelGroupLayout(), groupCreatorAndElementAdder);
 assertEquals(new ParallelGroupLayout(), groupAgentToAddContentTo.getLayout());
 }

 @Test
 public void testGroupContent() {
 GroupAgent groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 GroupLayout.Group result = groupAgent.groupContent(groupCreatorAndElementAdderFactory.createGroupCreatorAndElementAdder(groupLayoutEngineer));
 assertNotNull(result);
 }

 @Test
 public void testGetFunctionsOnGroupImplemenation() {
 GroupAgent groupAgent = TestUtilsGroupAgentFactory.createGroupAgentWithChildren(childrenAgents);
 FunctionsOnGroup functionsOnGroup = groupAgent.getFunctionsOnGroupImplemenation();
 assertNotNull(functionsOnGroup);
 }

 @Test
 public void testAddGapBefore() {
 GroupAgent groupAgent = TestUtilsGroupAgentFactory.createGroupAgent();
 GapAgent gapAgentToAddBefore = TestUtilsGapAgentFactory.createGapAgent();
 Agent agentToAddGapBefore = TestUtilsAgentFactory.createAgent();
 groupAgent.addAgent(agentToAddGapBefore);
 groupAgent.addGapBefore(gapAgentToAddBefore, agentToAddGapBefore);
 assertEquals(2, groupAgent.getNumberOfChildren());
 assertEquals(gapAgentToAddBefore, groupAgent.getChildren().get(0));
 assertEquals(agentToAddGapBefore, groupAgent.getChildren().get(1));
 }

 @Test
 public void testAddGapAfter() {
 GroupAgent groupAgent = TestUtilsGroupAgentFactory.createGroupAgent();
 GapAgent gapAgentToAddAfter = TestUtilsGapAgentFactory.createGapAgent();
 Agent agentToAddGapAfter = TestUtilsAgentFactory.createAgent();
 groupAgent.addAgent(agentToAddGapAfter);
 groupAgent.addGapAfter(gapAgentToAddAfter, agentToAddGapAfter);
 assertEquals(2, groupAgent.getNumberOfChildren());
 assertEquals(agentToAddGapAfter, groupAgent.getChildren().get(0));
 assertEquals(gapAgentToAddAfter, groupAgent.getChildren().get(1));
 }
}