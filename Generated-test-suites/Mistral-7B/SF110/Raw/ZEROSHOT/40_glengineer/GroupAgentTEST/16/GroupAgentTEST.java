package glengineer.agents.test;

import static org.junit.Assert.*;

import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.AgentSetterFactory;
import glengineer.agents.setters.ComponentAgentSetter;
import glengineer.agents.setters.GroupAgentSetter;
import glengineer.agents.setters.TemporaryGapAgentSetter;
import glengineer.agents.utils.AgentFactory;
import glengineer.agents.utils.GroupLayoutEngineer;
import glengineer.agents.utils.GroupLayoutEngineer.TemporaryGapsToGaps;

import java.util.*;

public class GroupAgentTest {

 private GroupAgent agent;
 private GroupAgent child1;
 private GroupAgent child2;
 private GroupAgent child3;
 private ComponentAgent component;
 private GapAgent gap;

 @Before
 public void setUp() {
 agent = AgentFactory.createGroupAgent();
 child1 = AgentFactory.createGroupAgent();
 child2 = AgentFactory.createGroupAgent();
 child3 = AgentFactory.createGroupAgent();
 component = AgentFactory.createComponentAgent();
 gap = AgentFactory.createGapAgent();
 }

 @Test
 public void testGetFirstName() {
 child1.setFirstName("John");
 child2.setFirstName("Jane");
 child3.setFirstName("Jim");
 agent.addAgent(child1);
 agent.addAgent(child2);
 agent.addAgent(child3);
 assertEquals("John", agent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 child1.setLastName("Doe");
 child2.setLastName("Smith");
 child3.setLastName("Johnson");
 agent.addAgent(child1);
 agent.addAgent(child2);
 agent.addAgent(child3);
 assertEquals("Doe", agent.getLastName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetFirstNameEmptyList() {
 agent.addAgent(child1);
 agent.addAgent(child2);
 agent.addAgent(child3);
 assertNull(agent.getFirstName());
 }

 @Test
 public void testIsGroup() {
 agent.setFirstName("John");
 agent.setLastName("Doe");
 assertTrue(agent.isGroup(agent.getFirstName(), agent.getLastName()));
 }

 @Test
 public void testIsNotGroup() {
 child1.setFirstName("John");
 child1.setLastName("Doe");
 assertFalse(agent.isGroup("John", "Smith"));
 }

 @Test
 public void testIsComponent() {
 assertFalse(agent.isComponent("ComponentName"));
 }

 @Test
 public void testIsGap() {
 assertFalse(agent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent1 = AgentFactory.createGroupAgent();
 GroupAgent groupAgent2 = AgentFactory.createGroupAgent();
 groupAgent1.setFirstName("John");
 groupAgent1.setLastName("Doe");
 groupAgent2.setFirstName("John");
 groupAgent2.setLastName("Doe");
 assertTrue(groupAgent1.equals(groupAgent2));
 }

 @Test
 public void testAddAgent() {
 agent.addAgent(child1);
 assertEquals(1, agent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 agent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 agent.addAgent(child1);
 agent.addAgent(child2);
 List<Agent> children = agent.getChildren();
 assertEquals(2, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 agent.addAgent(child1);
 agent.addAgent(child2);
 agent.addAgent(child3);
 assertEquals(3, agent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 agent.addAgent(component);
 assertEquals(component, agent.getComponent("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentNotFound() {
 agent.addAgent(component);
 assertNull(agent.getComponent("ComponentName2"));
 }

 @Test
 public void testGetGroup() {
 GroupAgent group = AgentFactory.createGroupAgent();
 group.setFirstName("John");
 group.setLastName("Doe");
 agent.addAgent(group);
 assertEquals(group, agent.getGroup("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNotFound() {
 GroupAgent group = AgentFactory.createGroupAgent();
 group.setFirstName("John");
 group.setLastName("Doe");
 agent.addAgent(group);
 assertNull(agent.getGroup("John", "Smith"));
 }

 @Test
 public void testFindDependingComponentByName() {
 ComponentAgent component = AgentFactory.createComponentAgent();
 child1.addAgent(component);
 assertEquals(component, agent.findDependingComponentByName("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameNotFound() {
 ComponentAgent component = AgentFactory.createComponentAgent();
 child1.addAgent(component);
 assertNull(agent.findDependingComponentByName("ComponentName2"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent group = AgentFactory.createGroupAgent();
 group.setFirstName("John");
 group.setLastName("Doe");
 child1.addAgent(group);
 assertEquals(group, agent.findDependingGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNotFound() {
 GroupAgent group = AgentFactory.createGroupAgent();
 group.setFirstName("John");
 group.setLastName("Doe");
 child1.addAgent(group);
 assertNull(agent.findDependingGroupByNames("John", "Smith"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 ParallelGroupAgent parallelGroup = AgentFactory.createParallelGroupAgent();
 parallelGroup.setFirstName("John");
 parallelGroup.setLastName("Doe");
 child1.addAgent(parallelGroup);
 assertEquals(parallelGroup, agent.findDependingParallelGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesNotFound() {
 ParallelGroupAgent parallelGroup = AgentFactory.createParallelGroupAgent();
 parallelGroup.setFirstName("John");
 parallelGroup.setLastName("Doe");
 child1.addAgent(parallelGroup);
 assertNull(agent.findDependingParallelGroupByNames("John", "Smith"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 SequentialGroupAgent sequentialGroup = AgentFactory.createSequentialGroupAgent();
 sequentialGroup.setFirstName("John");
 sequentialGroup.setLastName("Doe");
 child1.addAgent(sequentialGroup);
 assertEquals(sequentialGroup, agent.findDependingSequentialGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesNotFound() {
 SequentialGroupAgent sequentialGroup = AgentFactory.createSequentialGroupAgent();
 sequentialGroup.setFirstName("John");
 sequentialGroup.setLastName("Doe");
 child1.addAgent(sequentialGroup);
 assertNull(agent.findDependingSequentialGroupByNames("John", "Smith"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 // Test empty list
 agent.removeGapsFromParallelGroups();
 assertTrue(agent.getChildren().isEmpty());

 // Test single agent
 child1.addAgent(gap);
 agent.addAgent(child1);
 agent.removeGapsFromParallelGroups();
 assertEquals(0, agent.getNumberOfChildren());

 // Test multiple agents with gaps
 child1.addAgent(gap);
 child2.addAgent(gap);
 agent.addAgent(child1);
 agent.addAgent(child2);
 agent.removeGapsFromParallelGroups();
 assertEquals(2, agent.getNumberOfChildren());
 }

 @Test
 public void testPassTheCallToSubgroups() {
 GroupAgent child1 = AgentFactory.createGroupAgent();
 child1.addAgent(gap);
 GroupAgent child2 = AgentFactory.createGroupAgent();
 child2.addAgent(gap);
 agent.addAgent(child1);
 agent.addAgent(child2);
 agent.passTheCallToSubgroups();
 assertEquals(0, child1.getNumberOfChildren());
 assertEquals(0, child2.getNumberOfChildren());
 }

 @Test
 public void testRemoveAllGapsFromParallelSubgroups() {
 ParallelGroupAgent parallelGroup = AgentFactory.createParallelGroupAgent();
 parallelGroup.addAgent(gap);
 parallelGroup.addAgent(child1);
 agent.addAgent(parallelGroup);
 agent.removeGapsFromParallelGroups();
 assertEquals(1, parallelGroup.getNumberOfChildren());
 assertEquals(1, agent.getNumberOfChildren());
 }

 @Test
 public void testReplaceParallelSubgroupsWithGapsByTheirSingleGaps() {
 ParallelGroupAgent parallelGroup1 = AgentFactory.createParallelGroupAgent();
 parallelGroup1.addAgent(gap);
 parallelGroup1.addAgent(child1);
 ParallelGroupAgent parallelGroup2 = AgentFactory.createParallelGroupAgent();
 parallelGroup2.addAgent(gap);
 parallelGroup2.addAgent(child2);
 agent.addAgent(parallelGroup1);
 agent.addAgent(parallelGroup2);
 agent.replaceParallelSubgroupsWithGapsByTheirSingleGaps();
 assertEquals(2, agent.getNumberOfChildren());
 assertEquals(1, parallelGroup1.getNumberOfChildren());
 assertEquals(1, parallelGroup2.getNumberOfChildren());
 }

 @Test
 public void testReplaceContinuousSequencesOfGapsBySingleGaps() {
 GroupAgent group = AgentFactory.createGroupAgent();
 group.addAgent(gap);
 group.addAgent(gap);
 group.addAgent(gap);
 group.addAgent(child1);
 agent.addAgent(group);
 agent.replaceContinuousSequencesOfGapsBySingleGaps();
 assertEquals(4, agent.getNumberOfChildren());
 assertEquals(1, group.getNumberOfChildren());
 }

 @Test
 public void testReplaceGroupsWithSingleElementsByThatElements() {
 GroupAgent group1 = AgentFactory.createGroupAgent();
 group1.addAgent(child1);
 group1.addAgent(child2);
 agent.addAgent(group1);
 agent.replaceGroupsWithSingleElementsByThatElements();
 assertEquals(3, agent.getNumberOfChildren());
 assertEquals(1, group1.getNumberOfChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 TemporaryGapsToGaps map = new TemporaryGapsToGaps();
 TemporaryGapAgentSetter tempGapSetter = AgentSetterFactory.createTemporaryGapAgentSetter();
 tempGapSetter.setTemporaryGapsToGaps(map);
 agent.addAgent(tempGapSetter);
 agent.replaceTemporaryGapsByRealGaps(map);
 assertEquals(1, agent.getNumberOfChildren());
 }

 @Test
 public void testAddContentToGroup() {
 Settings settings = new Settings();
 GroupAgent parentGroup = AgentFactory.createGroupAgent();
 GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder() {
 @Override
 public void addGroupToGroup(GroupLayout.Group group, Settings settings, GroupLayout.Group parentGroup) {
 // Do nothing
 }

 @Override
 public void addComponentToGroup(GroupLayout.Component component, Settings settings, GroupLayout.Group parentGroup) {
 // Do nothing
 }
 };
 parentGroup.addAgent(agent);
 agent.addContentToGroup(parentGroup, creatorAndAdder);
 // Assertions will be added in the test case where 'addGroupToGroup' and 'addComponentToGroup' are implemented
 }

 @Test
 public void testGroupContent() {
 GroupAgentTest testClass = this;
 GroupLayoutEngineer engineer = new GroupLayoutEngineer();
 GroupAgent group = AgentFactory.createGroupAgent();
 group.addAgent(child1);
 group.addAgent(child2);
 GroupLayout.Group groupContent = group.groupContent(new GroupCreatorAndElementAdder() {
 @Override
 public void addGroupToGroup(GroupLayout.Group group, Settings settings, GroupLayout.Group parentGroup) {
 testClass.assertEquals(group, engineer.createGroup(groupContent, settings, parentGroup));
 }

 @Override
 public void addComponentToGroup(GroupLayout.Component component, Settings settings, GroupLayout.Group parentGroup) {
 testClass.assertEquals(component, engineer.createComponent(component.getName(), settings, parentGroup));
 }
 });
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyUnmodifiableList() {
 List<Agent> children = agent.getChildren();
 children.remove(0);
 }
}