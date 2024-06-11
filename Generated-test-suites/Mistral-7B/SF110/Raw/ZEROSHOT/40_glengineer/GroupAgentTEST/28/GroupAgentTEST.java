package test.glengineer.agents;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.SettingsSetter;
import glengineer.agents.settings.SettingsType;
import glengineer.agents.setters.SettingsTypeSetter;
import glengineer.agents.utils.AgentFactory;
import glengineer.agents.utils.AgentType;
import glengineer.agents.utils.AgentWrapper;
import glengineer.agents.utils.GapAgent;
import glengineer.agents.utils.GroupLayout;
import glengineer.agents.utils.GroupLayout.Group;
import glengineer.agents.utils.GroupCreatorAndElementAdder;
import glengineer.agents.utils.TemporaryGapAgent;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private AgentWrapper agentWrapper;
 private Settings settings;
 private SettingsType settingsType;
 private SettingsTypeSetter settingsTypeSetter;
 private SettingsSetter settingsSetter;
 private GroupCreatorAndElementAdder creatorAndAdder;
 private GroupLayout layout;
 private List<Agent> children;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 agentWrapper = new AgentWrapper();
 settings = new Settings();
 settingsType = new SettingsType();
 settingsTypeSetter = new SettingsTypeSetter();
 settingsSetter = new SettingsSetter();
 creatorAndAdder = new GroupCreatorAndElementAdder();
 children = new LinkedList<>();
 }

 @Test
 public void testGetFirstName() {
 Agent child1 = AgentFactory.createAgent(AgentType.COMPONENT_AGENT, "Child1");
 child1.setFirstName("John");
 child1.setLastName("Doe1");
 groupAgent.addAgent(child1);
 assertEquals("John", groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 Agent child1 = AgentFactory.createAgent(AgentType.COMPONENT_AGENT, "Child1");
 child1.setFirstName("John");
 child1.setLastName("Doe1");
 groupAgent.addAgent(child1);
 assertEquals("Doe1", groupAgent.getLastName());
 }

 @Test
 public void testIsGroup() {
 Agent child1 = AgentFactory.createAgent(AgentType.GROUP_AGENT, "Child1");
 child1.setFirstName("John");
 child1.setLastName("Doe1");
 groupAgent.addAgent(child1);
 assertTrue(groupAgent.isGroup("John", "Doe1"));
 }

 @Test
 public void testIsComponent() {
 Agent child1 = AgentFactory.createAgent(AgentType.COMPONENT_AGENT, "Child1");
 child1.setFirstName("John");
 child1.setLastName("Doe1");
 groupAgent.addAgent(child1);
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testIsGap() {
 Agent gap = new GapAgent();
 assertFalse(groupAgent.isGap());
 groupAgent.addAgent(gap);
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 Agent child1 = AgentFactory.createAgent(AgentType.GROUP_AGENT, "Child1");
 child1.setFirstName("John");
 child1.setLastName("Doe1");
 Agent child2 = AgentFactory.createAgent(AgentType.GROUP_AGENT, "Child2");
 child2.setFirstName("John");
 child2.setLastName("Doe1");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertTrue(groupAgent.equals(child1));
 assertTrue(groupAgent.equals(child2));
 }

 @Test
 public void testAddAgent() {
 Agent child1 = AgentFactory.createAgent(AgentType.COMPONENT_AGENT, "Child1");
 groupAgent.addAgent(child1);
 assertEquals(1, groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 groupAgent.addAgent(child1);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(1, children.size());
 assertEquals(child1, children.get(0));
 }

 @Test
 public void testGetNumberOfChildren() {
 groupAgent.addAgent(child1);
 assertEquals(1, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 Agent component = AgentFactory.createAgent(AgentType.COMPONENT_AGENT, "Component1");
 groupAgent.addAgent(component);
 assertEquals(component, groupAgent.getComponent("Component1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWithInvalidName() {
 groupAgent.getComponent("InvalidName");
 }

 @Test
 public void testGetGroup() {
 Agent group = AgentFactory.createAgent(AgentType.GROUP_AGENT, "Group1");
 group.setFirstName("John");
 group.setLastName("Doe1");
 groupAgent.addAgent(group);
 assertEquals(group, groupAgent.getGroup("John", "Doe1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithInvalidName() {
 groupAgent.getGroup("InvalidFirstName", "InvalidLastName");
 }

 @Test
 public void testFindDependingComponentByName() {
 Agent component = AgentFactory.createAgent(AgentType.COMPONENT_AGENT, "Component1");
 groupAgent.addAgent(component);
 Agent child1 = AgentFactory.createAgent(AgentType.GROUP_AGENT, "Child1");
 child1.addAgent(component);
 groupAgent.addAgent(child1);
 assertEquals(component, groupAgent.findDependingComponentByName("Component1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWithInvalidName() {
 groupAgent.findDependingComponentByName("InvalidName");
 }

 @Test
 public void testFindDependingGroupByNames() {
 Agent group = AgentFactory.createAgent(AgentType.GROUP_AGENT, "Group1");
 group.setFirstName("John");
 group.setLastName("Doe1");
 groupAgent.addAgent(group);
 Agent child1 = AgentFactory.createAgent(AgentType.GROUP_AGENT, "Child1");
 child1.setFirstName("John");
 child1.setLastName("Doe2");
 group.addAgent(child1);
 assertEquals(group, groupAgent.findDependingGroupByNames("John", "Doe1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWithInvalidName() {
 groupAgent.findDependingGroupByNames("InvalidFirstName", "InvalidLastName");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 Agent parallelGroup = AgentFactory.createAgent(AgentType.PARALLEL_GROUP_AGENT, "ParallelGroup1");
 parallelGroup.setFirstName("John");
 parallelGroup.setLastName("Doe1");
 groupAgent.addAgent(parallelGroup);
 Agent child1 = AgentFactory.createAgent(AgentType.GROUP_AGENT, "Child1");
 child1.setFirstName("John");
 child1.setLastName("Doe2");
 parallelGroup.addAgent(child1);
 assertEquals(parallelGroup, groupAgent.findDependingParallelGroupByNames("John", "Doe1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWithInvalidName() {
 groupAgent.findDependingParallelGroupByNames("InvalidFirstName", "InvalidLastName");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 Agent sequentialGroup = AgentFactory.createAgent(AgentType.SEQUENTIAL_GROUP_AGENT, "SequentialGroup1");
 sequentialGroup.setFirstName("John");
 sequentialGroup.setLastName("Doe1");
 groupAgent.addAgent(sequentialGroup);
 Agent child1 = AgentFactory.createAgent(AgentType.GROUP_AGENT, "Child1");
 child1.setFirstName("John");
 child1.setLastName("Doe2");
 sequentialGroup.addAgent(child1);
 assertEquals(sequentialGroup, groupAgent.findDependingSequentialGroupByNames("John", "Doe1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWithInvalidName() {
 groupAgent.findDependingSequentialGroupByNames("InvalidFirstName", "InvalidLastName");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(new TemporaryGapAgent(5));
 groupAgent.addAgent(child1);
 groupAgent.addAgent(new TemporaryGapAgent(2));
 groupAgent.addAgent(child1);
 groupAgent.addAgent(new TemporaryGapAgent(3));
 groupAgent.addAgent(child1);
 groupAgent.addAgent(new TemporaryGapAgent(1));
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(4, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 settingsTypeSetter.setSettingsType(settings, SettingsType.FILL_WITH_GAPS);
 settingsSetter.setSettingsValue(settingsType, settings, 2);
 groupAgent.replaceTemporaryGapsByRealGaps(new GroupLayoutEngineer.TemporaryGapsToGaps() {
 @Override
 public Agent gap(TemporaryGapAgent gapAgent) {
 return new GapAgent(gapAgent.getGapSize());
 }
 });
 assertEquals(6, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testAddGroupToGroup() {
 Group group = new Group();
 layout = new GroupLayout();
 layout.setAutoCreateGaps(true);
 layout.setConstraints(group, new int[] { 0, 0, 0, 0 });
 creatorAndAdder.addGroupToGroup(group, settings, layout);
 assertTrue(groupAgent.addContentToGroup(layout, creatorAndAdder));
 }

 @Test
 public void testGroupContent() {
 throw new TestIncompleteException("Abstract method 'groupContent(GroupCreatorAndElementAdder)' not implemented in class 'GroupAgentTest'");
 }
}