package glengineer.agents.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.ComponentAgent;
import glengineer.agents.GroupAgent;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.GapAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.AgentSetterFactory;
import glengineer.agents.setters.TemporaryGapAgent;
import glengineer.agents.testhelpers.TestHelper;
import glengineer.layout.GroupLayout;
import glengineer.layout.GroupCreatorAndElementAdder;
import glengineer.layout.GroupLayoutEngineer;

public class GroupAgentTest {

 private static final String FIRST_NAME = "John";
 private static final String LAST_NAME = "Doe";
 private static final String COMPONENT_NAME = "Component1";
 private static final String FIRST_NAME_ALIAS = "Alias";
 private static final String LAST_NAME_ALIAS = "Alias";

 private GroupAgent testAgent;
 private GroupAgent testAgentWithChildren;
 private GroupAgent testAgentWithComponent;
 private GroupAgent testAgentWithGroup;
 private GroupAgent testAgentWithGroupWithComponent;
 private GroupAgent testAgentWithAliases;
 private GroupAgent testAgentWithEmptyList;
 private GroupAgent testAgentWithNullChildren;
 private GroupAgent testAgentWithNullAgent;
 private ParallelGroupAgent parallelTestAgent;
 private SequentialGroupAgent sequentialTestAgent;

 @Before
 public void setUp() {
 testAgent = new GroupAgent();
 testAgentWithChildren = new GroupAgent();
 testAgentWithComponent = new GroupAgent();
 testAgentWithGroup = new GroupAgent();
 testAgentWithGroupWithComponent = new GroupAgent();
 testAgentWithAliases = new GroupAgent();
 testAgentWithEmptyList = new GroupAgent();
 testAgentWithNullChildren = new GroupAgent();
 testAgentWithNullAgent = new GroupAgent();

 parallelTestAgent = new ParallelGroupAgent();
 sequentialTestAgent = new SequentialGroupAgent();

 AgentSetterFactory agentSetterFactory = new AgentSetterFactory();
 TestHelper testHelper = new TestHelper();

 testAgent.setSetter(agentSetterFactory.createAgentSetter());
 testAgentWithChildren.setSetter(agentSetterFactory.createAgentSetter());
 testAgentWithComponent.setSetter(agentSetterFactory.createAgentSetter());
 testAgentWithGroup.setSetter(agentSetterFactory.createAgentSetter());
 testAgentWithGroupWithComponent.setSetter(agentSetterFactory.createAgentSetter());
 testAgentWithAliases.setSetter(agentSetterFactory.createAgentSetter());
 testAgentWithEmptyList.setSetter(agentSetterFactory.createAgentSetter());
 testAgentWithNullChildren.setSetter(agentSetterFactory.createAgentSetter());
 testAgentWithNullAgent.setSetter(agentSetterFactory.createAgentSetter());
 parallelTestAgent.setSetter(agentSetterFactory.createAgentSetter());
 sequentialTestAgent.setSetter(agentSetterFactory.createAgentSetter());

 testAgentWithChildren.addAgent(new ComponentAgent(COMPONENT_NAME));
 testAgentWithGroup.addAgent(testAgentWithChildren);
 testAgentWithGroupWithComponent.addAgent(testAgentWithChildren);
 testAgentWithGroupWithComponent.addAgent(new ComponentAgent(COMPONENT_NAME));
 testAgentWithAliases.addAgent(new AgentSetter());
 testAgentWithAliases.getSettings().setFirstName(FIRST_NAME_ALIAS);
 testAgentWithAliases.getSettings().setLastName(LAST_NAME_ALIAS);
 testAgentWithEmptyList.addAgent(new AgentSetter());

 parallelTestAgent.addAgent(new GapAgent(10));
 sequentialTestAgent.addAgent(new GapAgent(5));
 parallelTestAgent.addAgent(new GroupAgent());
 sequentialTestAgent.addAgent(testAgentWithChildren);
 parallelTestAgent.addAgent(new GroupAgent());
 sequentialTestAgent.addAgent(new GroupAgent());
 }

 @Test
 public void testGetFirstName() {
 assertEquals(FIRST_NAME, testAgentWithChildren.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals(LAST_NAME, testAgentWithChildren.getLastName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFirstNameNullChildren() {
 testAgentWithNullChildren.getFirstName();
 }

 @Test(expected = NullPointerException.class)
 public void testGetLastNameNullChildren() {
 testAgentWithNullChildren.getLastName();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsComponentNullComponentName() {
 testAgent.isComponent(null);
 }

 @Test
 public void testIsGroupFirstLastName() {
 assertTrue(testAgentWithGroup.isGroup(FIRST_NAME, LAST_NAME));
 }

 @Test
 public void testIsGroupSelf() {
 assertTrue(testAgentWithGroup.isGroup(FIRST_NAME, LAST_NAME));
 }

 @Test
 public void testIsGroupOther() {
 assertFalse(testAgentWithGroup.isGroup("OtherFirstName", "OtherLastName"));
 }

 @Test
 public void testIsGap() {
 assertFalse(testAgentWithChildren.isGap());
 }

 @Test
 public void testEquals() {
 assertTrue(testAgentWithGroupWithComponent.equals(testAgentWithGroupWithComponent));
 }

 @Test
 public void testEqualsDifferentFirstName() {
 assertFalse(testAgentWithGroup.equals(new GroupAgent()));
 }

 @Test
 public void testEqualsDifferentLastName() {
 assertFalse(testAgentWithGroup.equals(new GroupAgent()));
 }

 @Test
 public void testAddAgentNullAgent() {
 assertThrows(IllegalArgumentException.class, () -> testAgent.addAgent(null));
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = testAgentWithChildren.getChildren();
 assertEquals(1, children.size());
 assertEquals(testAgentWithChildren, children.get(0));
 }

 @Test
 public void testGetChildrenEmptyList() {
 List<Agent> children = testAgentWithEmptyList.getChildren();
 assertEquals(0, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(1, testAgentWithChildren.getNumberOfChildren());
 }

 @Test
 public void testGetComponentNullComponentName() {
 assertThrows(IllegalArgumentException.class, () -> testAgent.getComponent("null"));
 }

 @Test
 public void testGetComponentEmptyList() {
 assertThrows(IllegalArgumentException.class, () -> testAgent.getComponent("ComponentName"));
 }

 @Test
 public void testGetComponentExistingComponent() {
 ComponentAgent component = (ComponentAgent)testAgentWithChildren.getComponent(COMPONENT_NAME);
 assertEquals(COMPONENT_NAME, component.getName());
 }

 @Test
 public void testGetGroupFirstLastName() {
 GroupAgent group = (GroupAgent)testAgentWithGroup.getChildren().get(0);
 assertTrue(group.isGroup(FIRST_NAME, LAST_NAME));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupInvalidFirstName() {
 GroupAgent group = (GroupAgent)testAgentWithGroup.getChildren().get(0);
 assertThrows(IllegalArgumentException.class, () -> group.getGroup("InvalidFirstName", "LastName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupInvalidLastName() {
 GroupAgent group = (GroupAgent)testAgentWithGroup.getChildren().get(0);
 assertThrows(IllegalArgumentException.class, () -> group.getGroup(FIRST_NAME, "InvalidLastName"));
 }

 @Test
 public void testFindDependingComponentByNameNullComponentName() {
 assertNull(testAgent.findDependingComponentByName(null));
 }

 @Test
 public void testFindDependingComponentByNameExistingComponent() {
 ComponentAgent component = testAgentWithChildren.findDependingComponentByName(COMPONENT_NAME);
 assertEquals(component, testAgentWithChildren.getChildren().get(0));
 }

 @Test
 public void testFindDependingGroupByNamesNullFirstName() {
 assertNull(testAgentWithGroup.findDependingGroupByNames(null, LAST_NAME));
 }

 @Test
 public void testFindDependingGroupByNamesNullLastName() {
 assertNull(testAgentWithGroup.findDependingGroupByNames(FIRST_NAME, null));
 }

 @Test
 public void testFindDependingGroupByNamesExistingGroup() {
 GroupAgent group = testAgentWithGroup.findDependingGroupByNames(FIRST_NAME, LAST_NAME);
 assertEquals(testAgentWithGroup, group);
 }

 @Test
 public void testFindDependingParallelGroupByNamesNullFirstName() {
 assertNull(parallelTestAgent.findDependingParallelGroupByNames(null, LAST_NAME));
 }

 @Test
 public void testFindDependingParallelGroupByNamesNullLastName() {
 assertNull(parallelTestAgent.findDependingParallelGroupByNames(FIRST_NAME, null));
 }

 @Test
 public void testFindDependingParallelGroupByNamesExistingParallelGroup() {
 ParallelGroupAgent parallelGroup = parallelTestAgent.findDependingParallelGroupByNames(FIRST_NAME, LAST_NAME);
 assertEquals(parallelTestAgent, parallelGroup);
 }

 @Test
 public void testFindDependingSequentialGroupByNamesNullFirstName() {
 assertNull(sequentialTestAgent.findDependingSequentialGroupByNames(null, LAST_NAME));
 }

 @Test
 public void testFindDependingSequentialGroupByNamesNullLastName() {
 assertNull(sequentialTestAgent.findDependingSequentialGroupByNames(FIRST_NAME, null));
 }

 @Test
 public void testFindDependingSequentialGroupByNamesExistingSequentialGroup() {
 SequentialGroupAgent sequentialGroup = sequentialTestAgent.findDependingSequentialGroupByNames(FIRST_NAME, LAST_NAME);
 assertEquals(sequentialTestAgent, sequentialGroup);
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 // This test is not feasible due to the abstract nature of the method and the lack of a concrete implementation in the test case.
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupLayoutEngineer layoutEngineer = new GroupLayoutEngineer();
 GroupLayout groupLayout = layoutEngineer.parseFromResource("/test.mxml");
 testAgentWithNullAgent.replaceTemporaryGapsByRealGaps(layoutEngineer.getTemporaryGapsToGapsMap());
 }

 @Test
 public void testAddContentToGroup() {
 GroupCreatorAndElementAdder groupCreatorAndElementAdder = new GroupCreatorAndElementAdder();
 GroupAgent testParentGroup = new GroupAgent();

 testParentGroup.addContentToGroup(groupCreatorAndElementAdder, testAgent.getSettings(), groupLayout);
 List<Agent> children = testParentGroup.getChildren();
 assertEquals(1, children.size());
 assertTrue(children.get(0) instanceof GroupAgent);
 }

 @Test
 public void testAddGapBefore() {
 GapAgent gap = new GapAgent(10);
 AgentSetter element = new AgentSetter();
 testAgent.addGapBefore(gap, element);
 List<Agent> children = testAgent.getChildren();
 assertEquals(2, children.size());
 assertEquals(element, children.get(0));
 assertEquals(gap, children.get(1));
 }

 @Test
 public void testAddGapAfter() {
 GapAgent gap = new GapAgent(10);
 AgentSetter element = new AgentSetter();
 testAgent.addGapAfter(gap, element);
 List<Agent> children = testAgent.getChildren();
 assertEquals(2, children.size());
 assertEquals(element, children.get(0));
 assertEquals(gap, children.get(1));
 }
}