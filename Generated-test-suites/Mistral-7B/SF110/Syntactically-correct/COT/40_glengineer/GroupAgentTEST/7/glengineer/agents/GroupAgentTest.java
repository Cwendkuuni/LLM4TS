package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.GroupAgent;
import glengineer.agents.Settings;
import glengineer.agents.setters.GapAgent;
import glengineer.agents.setters.GroupAgentSetter;
import glengineer.agents.setters.TemporaryGapAgent;
import glengineer.layout.GroupCreatorAndElementAdder;
import glengineer.layout.GroupLayout;
import glengineer.layout.GroupLayout.Group;

public class GroupAgentTest {

 @SuppressWarnings("unchecked")
 private GroupAgent createGroupAgent() {
 GroupAgent agent = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 Group group = new Group();
 return group;
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, new Object());
 }
 };
 agent.setSettings(new Settings());
 return agent;
 }

 @Test
 public void testGetFirstName() {
 GroupAgent agent = createGroupAgent();
 Agent child1 = new GroupAgentSetter() {
 @Override
 public String getFirstName() {
 return "John";
 }
 }.setLastName("Doe");
 agent.addAgent(child1);

 String result = agent.getFirstName();
 assertEquals("John", result);
 }

 @Test
 public void testGetFirstName_whenNoChildren() {
 GroupAgent agent = createGroupAgent();
 String result = agent.getFirstName();
 assertNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetFirstName_whenChildrenAreEmpty() {
 GroupAgent agent = createGroupAgent();
 agent.addAgent(new Object());
 agent.removeAllAgents();
 String result = agent.getFirstName();
 }

 @Test
 public void testGetLastName() {
 GroupAgent agent = createGroupAgent();
 Agent child1 = new GroupAgentSetter() {
 @Override
 public String getLastName() {
 return "Doe";
 }
 }.setFirstName("John");
 agent.addAgent(child1);

 String result = agent.getLastName();
 assertEquals("Doe", result);
 }

 @Test
 public void testGetLastName_whenNoChildren() {
 GroupAgent agent = createGroupAgent();
 String result = agent.getLastName();
 assertNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastName_whenChildrenAreEmpty() {
 GroupAgent agent = createGroupAgent();
 agent.addAgent(new Object());
 agent.removeAllAgents();
 String result = agent.getLastName();
 }

 @Test
 public void testIsGroup_whenIsGroup() {
 GroupAgent agent = createGroupAgent();
 Agent child = new GroupAgentSetter() {
 @Override
 public String getFirstName() {
 return "John";
 }
 }.setLastName("Doe");
 agent.addAgent(child);

 boolean result = agent.isGroup("John", "Doe");
 assertTrue(result);
 }

 @Test
 public void testIsGroup_whenNotAGroup() {
 GroupAgent agent = createGroupAgent();
 Agent child = new TemporaryGapAgent();
 agent.addAgent(child);

 boolean result = agent.isGroup("John", "Doe");
 assertFalse(result);
 }

 @Test
 public void testIsGroup_whenWrongFirstName() {
 GroupAgent agent = createGroupAgent();
 Agent child = new GroupAgentSetter() {
 @Override
 public String getFirstName() {
 return "John";
 }
 }.setLastName("Doe");
 agent.addAgent(child);

 boolean result = agent.isGroup("Jane", "Doe");
 assertFalse(result);
 }

 @Test
 public void testIsGroup_whenWrongLastName() {
 GroupAgent agent = createGroupAgent();
 Agent child = new GroupAgentSetter() {
 @Override
 public String getFirstName() {
 return "John";
 }
 }.setLastName("Doe");
 agent.addAgent(child);

 boolean result = agent.isGroup("John", "Jane");
 assertFalse(result);
 }

 @Test
 public void testIsComponent_whenIsNotComponent() {
 GroupAgent agent = createGroupAgent();
 boolean result = agent.isComponent("componentName");
 assertFalse(result);
 }

 @Test
 public void testIsGap_whenIsNotGap() {
 GroupAgent agent = createGroupAgent();
 boolean result = agent.isGap();
 assertFalse(result);
 }

 @Test
 public void testEquals_whenEqualGroup() {
 GroupAgent agent1 = createGroupAgent();
 GroupAgent agent2 = createGroupAgent();
 Agent child = new GroupAgentSetter() {
 @Override
 public String getFirstName() {
 return "John";
 }
 }.setLastName("Doe");
 agent1.addAgent(child);
 agent2.addAgent(child);

 Agent result = agent1.equals(agent2);
 assertTrue(result instanceof GroupAgent);
 }

 @Test
 public void testEquals_whenNotEqualGroup() {
 GroupAgent agent1 = createGroupAgent();
 GroupAgent agent2 = createGroupAgent();
 Agent child1 = new GroupAgentSetter() {
 @Override
 public String getFirstName() {
 return "John";
 }
 }.setLastName("Doe");
 agent1.addAgent(child1);
 Agent child2 = new GroupAgentSetter() {
 @Override
 public String getFirstName() {
 return "John";
 }
 }.setLastName("Doe");
 agent2.addAgent(child2);

 Agent result = agent1.equals(agent2);
 assertFalse(result instanceof GroupAgent);
 }

 @Test
 public void testAddAgent() {
 GroupAgent agent = createGroupAgent();
 Agent child = new Object();
 agent.addAgent(child);

 List<Agent> result = agent.getChildren();
 assertEquals(1, result.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAgent_whenNullAgent() {
 GroupAgent agent = createGroupAgent();
 agent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 GroupAgent agent = createGroupAgent();
 Agent child1 = new Object();
 Agent child2 = new Object();
 agent.addAgent(child1);
 agent.addAgent(child2);

 List<Agent> result = agent.getChildren();
 assertEquals(2, result.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 GroupAgent agent = createGroupAgent();
 Agent child1 = new Object();
 Agent child2 = new Object();
 agent.addAgent(child1);
 agent.addAgent(child2);

 int result = agent.getNumberOfChildren();
 assertEquals(2, result);
 }

 @Test
 public void testGetComponent() {
 GroupAgent agent = createGroupAgent();
 ComponentAgent component = new Object();
 agent.addAgent(component);

 ComponentAgent result = agent.getComponent("componentName");
 assertEquals(component, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponent_whenComponentNotFound() {
 GroupAgent agent = createGroupAgent();
 Agent component = new Object();
 agent.addAgent(component);

 agent.getComponent("componentNameDifferent");
 }

 @Test
 public void testGetGroup() {
 GroupAgent agent = createGroupAgent();
 GroupAgent group = new Object();
 agent.addAgent(group);

 GroupAgent result = agent.getGroup("John", "Doe");
 assertEquals(group, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroup_whenGroupNotFound() {
 GroupAgent agent = createGroupAgent();
 Agent group = new Object();
 agent.addAgent(group);

 agent.getGroup("John", "Jane");
 }

 @Test
 public void testFindDependingComponentByName() {
 GroupAgent agent = createGroupAgent();
 ComponentAgent component1 = new Object();
 ComponentAgent component2 = new Object();
 agent.addAgent(component1);
 agent.addAgent(component2);

 ComponentAgent result = agent.findDependingComponentByName("componentName");
 assertTrue(result instanceof ComponentAgent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByName_whenComponentNotFound() {
 GroupAgent agent = createGroupAgent();
 ComponentAgent component = new Object();
 agent.addAgent(component);

 agent.findDependingComponentByName("componentNameDifferent");
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent agent = createGroupAgent();
 GroupAgent group1 = new Object();
 GroupAgent group2 = new Object();
 agent.addAgent(group1);
 agent.addAgent(group2);

 GroupAgent result = agent.findDependingGroupByNames("John", "Doe");
 assertEquals(group1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNames_whenGroupNotFound() {
 GroupAgent agent = createGroupAgent();
 GroupAgent group = new Object();
 agent.addAgent(group);

 agent.findDependingGroupByNames("John", "Jane");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 GroupAgent agent = createGroupAgent();
 ParallelGroupAgent parallelGroup1 = new Object();
 ParallelGroupAgent parallelGroup2 = new Object();
 agent.addAgent(parallelGroup1);
 agent.addAgent(parallelGroup2);

 ParallelGroupAgent result = agent.findDependingParallelGroupByNames("John", "Doe");
 assertEquals(parallelGroup1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNames_whenParallelGroupNotFound() {
 GroupAgent agent = createGroupAgent();
 ParallelGroupAgent parallelGroup = new Object();
 agent.addAgent(parallelGroup);

 agent.findDependingParallelGroupByNames("John", "Jane");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 GroupAgent agent = createGroupAgent();
 SequentialGroupAgent sequentialGroup1 = new Object();
 SequentialGroupAgent sequentialGroup2 = new Object();
 agent.addAgent(sequentialGroup1);
 agent.addAgent(sequentialGroup2);

 SequentialGroupAgent result = agent.findDependingSequentialGroupByNames("John", "Doe");
 assertEquals(sequentialGroup1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNames_whenSequentialGroupNotFound() {
 GroupAgent agent = createGroupAgent();
 SequentialGroupAgent sequentialGroup = new Object();
 agent.addAgent(sequentialGroup);

 agent.findDependingSequentialGroupByNames("John", "Jane");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent agent = createGroupAgent();
 ParallelGroupAgent parallelGroup = new Object();
 GapAgent gap = new GapAgent();
 agent.addAgent(parallelGroup);
 parallelGroup.addAgent(gap);

 agent.removeGapsFromParallelGroups();

 List<Agent> result = agent.getChildren();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testRemoveGapsFromParallelGroups_whenNoGaps() {
 GroupAgent agent = createGroupAgent();
 ParallelGroupAgent parallelGroup = new Object();
 agent.addAgent(parallelGroup);

 agent.removeGapsFromParallelGroups();

 List<Agent> result = agent.getChildren();
 assertEquals(1, result.size());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgent agent = createGroupAgent();
 TemporaryGapAgent temporaryGap = new TemporaryGapAgent();
 agent.addAgent(temporaryGap);
 GroupAgentSetter gapSetter = new GroupAgentSetter() {
 @Override
 public String getFirstName() {
 return "John";
 }
 }.setLastName("Doe");
 agent.addAgent(gapSetter);

 agent.replaceTemporaryGapsByRealGaps(new GroupAgentSetter() {
 @Override
 public GroupLayout.Group gap(TemporaryGapAgent gap) {
 return new Group();
 }
 });

 List<Agent> result = agent.getChildren();
 assertEquals(2, result.size());
 }

 @Test
 public void testAddContentToGroup() {
 GroupAgent agent = createGroupAgent();
 GroupCreatorAndElementAdder creatorAndAdder = new Object();

 agent.addContentToGroup(new Object(), creatorAndAdder);
 }
}