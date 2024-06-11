package glengineer.agents.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.ComponentAgentSetter;
import glengineer.agents.setters.GroupAgentSetter;
import glengineer.agents.setters.TemporaryGapAgentSetter;
import glengineer.agents.utils.GapAgent;
import glengineer.agents.utils.GroupAgentFactory;
import glengineer.agents.utils.GroupCreatorAndElementAdder;
import glengineer.agents.utils.GroupLayout;
import glengineer.agents.utils.TemporaryGapAgent;

public class GroupAgentTest {

 private GroupAgent agent;
 private List<Agent> children;
 private Settings settings;
 private GroupAgentSetter groupAgentSetter;
 private AgentSetter agentSetter;
 private ComponentAgentSetter componentAgentSetter;
 private TemporaryGapAgentSetter temporaryGapAgentSetter;
 private GroupCreatorAndElementAdder creatorAndAdder;
 private GroupAgentFactory agentFactory;

 @Before
 public void setUp() {
 agent = new GroupAgent();
 children = new LinkedList<>();
 agent.setChildren(children);
 settings = new Settings();
 groupAgentSetter = new GroupAgentSetter();
 agentSetter = new AgentSetter();
 componentAgentSetter = new ComponentAgentSetter();
 temporaryGapAgentSetter = new TemporaryGapAgentSetter();
 creatorAndAdder = new GroupCreatorAndElementAdder();
 agentFactory = new GroupAgentFactory();
 }

 @Test
 public void testGetFirstName() {
 final String firstName = "John";
 final ComponentAgent componentAgent = agentSetter.createComponentAgent("TestComponent", firstName);
 children.add(componentAgent);
 final String result = agent.getFirstName();
 assertEquals(firstName, result);
 }

 @Test
 public void testGetLastName() {
 final String lastName = "Doe";
 final ComponentAgent componentAgent = agentSetter.createComponentAgent("TestComponent", null, lastName);
 children.add(componentAgent);
 final String result = agent.getLastName();
 assertEquals(lastName, result);
 }

 @Test
 public void testGetFirstNameWhenNoChildren() {
 final String result = agent.getFirstName();
 assertNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetFirstNameWhenChildrenIsEmptyButNotNull() {
 final String result = agent.getFirstName();
 fail();
 }

 @Test
 public void testIsGroup() {
 final String firstName = "John";
 final String lastName = "Doe";
 final GroupAgent testGroup = agentFactory.createTestGroupAgent(firstName, lastName);
 children.add(testGroup);
 assertTrue(agent.isGroup(firstName, lastName));
 }

 @Test
 public void testIsGroupWhenFirstNameDoesNotMatch() {
 final String firstName = "John";
 final String lastName = "Doe";
 final String incorrectFirstName = "Jane";
 final GroupAgent testGroup = agentFactory.createTestGroupAgent(firstName, lastName);
 children.add(testGroup);
 assertFalse(agent.isGroup(incorrectFirstName, lastName));
 }

 @Test
 public void testIsGroupWhenLastNameDoesNotMatch() {
 final String firstName = "John";
 final String lastName = "Doe";
 final String incorrectLastName = "Smith";
 final GroupAgent testGroup = agentFactory.createTestGroupAgent(firstName, lastName);
 children.add(testGroup);
 assertFalse(agent.isGroup(firstName, incorrectLastName));
 }

 @Test
 public void testIsGroupWhenGroupDoesNotExist() {
 final String firstName = "John";
 final String lastName = "Doe";
 final String incorrectFirstName = "Jane";
 final String incorrectLastName = "Smith";
 assertFalse(agent.isGroup(incorrectFirstName, incorrectLastName));
 }

 @Test
 public void testAddAgent() {
 final Agent testAgent = agentSetter.createComponentAgent("TestAgent");
 agent.addAgent(testAgent);
 assertTrue(agent.getChildren().contains(testAgent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 agent.addAgent(null);
 fail();
 }

 @Test
 public void testGetChildren() {
 final GroupAgent testGroup = agentFactory.createTestGroupAgent("TestGroup", "TestLastName");
 children.add(testGroup);
 final List<Agent> result = agent.getChildren();
 assertEquals(children, result);
 }

 @Test
 public void testGetNumberOfChildren() {
 final GroupAgent testGroup = agentFactory.createTestGroupAgent("TestGroup", "TestLastName");
 children.add(testGroup);
 final int result = agent.getNumberOfChildren();
 assertEquals(1, result);
 }

 @Test
 public void testGetNumberOfChildrenWhenEmpty() {
 final int result = agent.getNumberOfChildren();
 assertEquals(0, result);
 }

 @Test
 public void testGetComponent() {
 final String name = "TestComponent";
 final ComponentAgent testComponent = agentSetter.createComponentAgent("TestComponent");
 children.add(testComponent);
 final ComponentAgent result = agent.getComponent(name);
 assertEquals(testComponent, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWhenComponentDoesNotExist() {
 final String name = "TestComponent";
 final String errorMessage = "component " + name + " in the " + agent + " not found";
 throw new IllegalArgumentException(errorMessage);
 }

 @Test
 public void testGetGroup() {
 final String firstName = "John";
 final String lastName = "Doe";
 final GroupAgent testGroup = agentFactory.createTestGroupAgent(firstName, lastName);
 children.add(testGroup);
 final GroupAgent result = agent.getGroup(firstName, lastName);
 assertEquals(testGroup, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWhenGroupDoesNotExist() {
 final String firstName = "John";
 final String lastName = "Doe";
 final String errorMessage = "the group <" + firstName + "," + lastName + "> in the " + agent + " not found";
 throw new IllegalArgumentException(errorMessage);
 }

 @Test
 public void testFindDependingComponentByName() {
 final String name = "TestComponent";
 final ComponentAgent testComponent = agentSetter.createComponentAgent("TestComponent");
 children.add(testComponent);
 final ComponentAgent result = agent.findDependingComponentByName(name);
 assertEquals(testComponent, result);
 }

 @Test
 public void testFindDependingGroupByNames() {
 final String firstName = "John";
 final String lastName = "Doe";
 final GroupAgent testGroup = agentFactory.createTestGroupAgent(firstName, lastName);
 children.add(testGroup);
 final GroupAgent result = agent.findDependingGroupByNames(firstName, lastName);
 assertEquals(testGroup, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWhenGroupDoesNotExist() {
 final String firstName = "John";
 final String lastName = "Doe";
 final String errorMessage = "the group <" + firstName + "," + lastName + "> in the " + agent + " not found";
 throw new IllegalArgumentException(errorMessage);
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 final String firstName = "John";
 final String lastName = "Doe";
 final ParallelGroupAgent parallelGroup = agentFactory.createParallelGroupAgent(firstName, lastName);
 children.add(parallelGroup);
 final ParallelGroupAgent result = agent.findDependingParallelGroupByNames(firstName, lastName);
 assertEquals(parallelGroup, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWhenParallelGroupDoesNotExist() {
 final String firstName = "John";
 final String lastName = "Doe";
 final String errorMessage = "the parallel group <" + firstName + "," + lastName + "> in the " + agent + " not found";
 throw new IllegalArgumentException(errorMessage);
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 final String firstName = "John";
 final String lastName = "Doe";
 final SequentialGroupAgent sequentialGroup = agentFactory.createSequentialGroupAgent(firstName, lastName);
 children.add(sequentialGroup);
 final SequentialGroupAgent result = agent.findDependingSequentialGroupByNames(firstName, lastName);
 assertEquals(sequentialGroup, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWhenSequentialGroupDoesNotExist() {
 final String firstName = "John";
 final String lastName = "Doe";
 final String errorMessage = "the sequential group <" + firstName + "," + lastName + "> in the " + agent + " not found";
 throw new IllegalArgumentException(errorMessage);
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 final ParallelGroupAgent parallelGroup = agentFactory.createTestParallelGroupAgent();
 children.add(parallelGroup);
 parallelGroup.addChild(agentSetter.createComponentAgent("Component1"));
 parallelGroup.addChild(agentSetter.createComponentAgent("Component2"));
 parallelGroup.addChild(agentSetter.createComponentAgent("Component3"));
 parallelGroup.addChild(agentSetter.createGapAgent());
 parallelGroup.addChild(agentSetter.createComponentAgent("Component4"));
 parallelGroup.addChild(agentSetter.createComponentAgent("Component5"));
 parallelGroup.addChild(agentSetter.createGapAgent());
 parallelGroup.addChild(agentSetter.createGapAgent());
 parallelGroup.addChild(agentSetter.createComponentAgent("Component6"));
 agent.removeGapsFromParallelGroups();
 final List<Agent> result = parallelGroup.getChildren();
 assertEquals(5, result.size());
 assertTrue(result.containsAll(Arrays.asList(parallelGroup.getChildren().get(0), parallelGroup.getChildren().get(2), parallelGroup.getChildren().get(4), parallelGroup.getChildren().get(5), parallelGroup.getChildren().get(6))));
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 final TemporaryGapAgent gap = temporaryGapAgentSetter.createTemporaryGapAgent();
 children.add(gap);
 final Agent replacedGap = agentFactory.createGapAgent();
 agent.replaceTemporaryGapsByRealGaps(creatorAndAdder.createMap(gap));
 final List<Agent> result = agent.getChildren();
 assertEquals(1, result.size());
 assertEquals(replacedGap, result.get(0));
 }

 @Test
 public void testAddContentToGroup() {
 final GroupAgent testGroup = agentFactory.createTestGroupAgent("TestGroup", "TestLastName");
 final GroupCreatorAndElementAdder.Group parentGroup = creatorAndAdder.createGroup("ParentGroup");
 agent.addContentToGroup(parentGroup, creatorAndAdder);
 final List<Agent> result = parentGroup.getGroupElements();
 assertEquals(testGroup, result.get(0));
 }

 @Test
 public void testGroupContent() {
 final GroupAgent testGroup = agentFactory.createTestGroupAgent("TestGroup", "TestLastName");
 final GroupCreatorAndElementAdder.Group parentGroup = creatorAndAdder.createGroup("ParentGroup");
 final GroupLayout.Group result = testGroup.groupContent(creatorAndAdder);
 assertNotNull(result);
 }

 @Test
 public void testGetFunctionsOnGroupImplemenation() {
 final GroupAgent testGroup = agentFactory.createTestGroupAgent("TestGroup", "TestLastName");
 final FunctionsOnGroupAndElementImplementation functionsOnGroupAndElement = new FunctionsOnGroupAndElementImplementation(testGroup, null);
 final FunctionsOnGroup functionsOnGroup = testGroup.getFunctionsOnGroupImplemenation();
 assertEquals(functionsOnGroupAndElement, functionsOnGroup);
 }

 @Test
 public void testAddGapBefore() {
 final GapAgent gap = agentSetter.createGapAgent();
 final ComponentAgent component = agentSetter.createComponentAgent("TestComponent");
 children.add(component);
 agent.addGapBefore(gap, component);
 final List<Agent> result = agent.getChildren();
 assertEquals(2, result.size());
 assertEquals(gap, result.get(0));
 assertEquals(component, result.get(1));
 }

 @Test
 public void testAddGapAfter() {
 final GapAgent gap = agentSetter.createGapAgent();
 final ComponentAgent component = agentSetter.createComponentAgent("TestComponent");
 children.add(component);
 agent.addGapAfter(gap, component);
 final List<Agent> result = agent.getChildren();
 assertEquals(2, result.size());
 assertEquals(component, result.get(0));
 assertEquals(gap, result.get(1));
 }
}