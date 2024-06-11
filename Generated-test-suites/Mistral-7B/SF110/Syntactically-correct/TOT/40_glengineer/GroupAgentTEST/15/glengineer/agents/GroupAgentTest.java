import org.junit.Test;
import static org.junit.Assert.*;
import glengineer.agents.GroupAgent;
import glengineer.agents.Agent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.GapAgent;
import java.util.LinkedList;
import java.util.List;

public class GroupAgentTest {

 @Test
 public void testGetFirstName() {
 GroupAgent groupAgent = new MyGroupAgent();
 AgentSetter agentSetter = new AgentSetter();
 Agent firstAgent = agentSetter.createAgent("John", "Doe");
 groupAgent.addAgent(firstAgent);

 String expected = "John";
 String actual = groupAgent.getFirstName();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetFirstName_EmptyList() {
 GroupAgent groupAgent = new MyGroupAgent();
 String actual = groupAgent.getFirstName();
 assertNull(actual);
 }

 @Test
 public void testGetLastName() {
 GroupAgent groupAgent = new MyGroupAgent();
 AgentSetter agentSetter = new AgentSetter();
 Agent lastAgent = agentSetter.createAgent("John", "Doe");
 groupAgent.addAgent(lastAgent);

 String expected = "Doe";
 String actual = groupAgent.getLastName();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLastName_EmptyList() {
 GroupAgent groupAgent = new MyGroupAgent();
 String actual = groupAgent.getLastName();
 assertNull(actual);
 }

 @Test
 public void testIsGroup() {
 GroupAgent groupAgent = new MyGroupAgent();
 AgentSetter agentSetter = new AgentSetter();
 Agent groupAgentAgent = agentSetter.createAgent("John", "Doe");
 groupAgent.addAgent(groupAgentAgent);

 boolean expected = true;
 boolean actual = groupAgent.isGroup("John", "Doe");
 assertEquals(expected, actual);
 }

 @Test
 public void testIsGroup_DifferentNames() {
 GroupAgent groupAgent = new MyGroupAgent();
 AgentSetter agentSetter = new AgentSetter();
 Agent firstAgent = agentSetter.createAgent("John", "Doe");
 Agent secondAgent = agentSetter.createAgent("Mike", "Smith");
 groupAgent.addAgent(firstAgent);
 groupAgent.addAgent(secondAgent);

 boolean expected = false;
 boolean actual = groupAgent.isGroup("John", "Smith");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void addNullAgent() {
 GroupAgent groupAgent = new MyGroupAgent();
 Agent nullAgent = null;

 try {
 groupAgent.addAgent(nullAgent);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Cannot add null agents.";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test
 public void testGetChildren() {
 GroupAgent groupAgent = new MyGroupAgent();
 AgentSetter agentSetter = new AgentSetter();
 Agent firstAgent = agentSetter.createAgent("John", "Doe");
 Agent secondAgent = agentSetter.createAgent("Mike", "Smith");
 groupAgent.addAgent(firstAgent);
 groupAgent.addAgent(secondAgent);

 List<Agent> expected = Arrays.asList(firstAgent, secondAgent);
 List<Agent> actual = groupAgent.getChildren();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetNumberOfChildren() {
 GroupAgent groupAgent = new MyGroupAgent();
 AgentSetter agentSetter = new AgentSetter();
 Agent firstAgent = agentSetter.createAgent("John", "Doe");
 Agent secondAgent = agentSetter.createAgent("Mike", "Smith");
 groupAgent.addAgent(firstAgent);
 groupAgent.addAgent(secondAgent);

 int expected = 2;
 int actual = groupAgent.getNumberOfChildren();
 assertEquals(expected, actual);
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent parentGroupAgent = new MyGroupAgent();
 GroupAgent childGroupAgent1 = new MyGroupAgent();
 GroupAgent childGroupAgent2 = new MyGroupAgent();
 parentGroupAgent.addAgent(childGroupAgent1);
 parentGroupAgent.addAgent(childGroupAgent2);
 childGroupAgent1.addAgent(new MyAgent("Child1"));
 childGroupAgent2.addAgent(new MyAgent("Child2"));

 MyGroupAgent expected = (MyGroupAgent)childGroupAgent1;
 MyGroupAgent actual = (MyGroupAgent)parentGroupAgent.findDependingGroupByNames("Child1Agent", "Child1LastName");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void findDependingGroupByNames_NotFound() {
 GroupAgent parentGroupAgent = new MyGroupAgent();
 GroupAgent childGroupAgent1 = new MyGroupAgent();
 GroupAgent childGroupAgent2 = new MyGroupAgent();
 parentGroupAgent.addAgent(childGroupAgent1);
 parentGroupAgent.addAgent(childGroupAgent2);
 childGroupAgent1.addAgent(new MyAgent("Child1"));
 childGroupAgent2.addAgent(new MyAgent("Child2"));

 try {
 parentGroupAgent.findDependingGroupByNames("Child2Agent", "Child2LastName");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "the group <Child2Agent, Child2LastName> in the " + parentGroupAgent + " not found";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 GroupAgent parentGroupAgent = new MyParallelGroupAgent();
 ParallelGroupAgent childParallelGroupAgent1 = new MyParallelGroupAgent();
 ParallelGroupAgent childParallelGroupAgent2 = new MyParallelGroupAgent();
 parentGroupAgent.addAgent(childParallelGroupAgent1);
 parentGroupAgent.addAgent(childParallelGroupAgent2);
 childParallelGroupAgent1.addAgent(new MyAgent("Child1"));
 childParallelGroupAgent2.addAgent(new MyAgent("Child2"));

 MyParallelGroupAgent expected = (MyParallelGroupAgent)childParallelGroupAgent1;
 MyParallelGroupAgent actual = (MyParallelGroupAgent)parentGroupAgent.findDependingParallelGroupByNames("Child1Agent", "Child1LastName");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void findDependingParallelGroupByNames_NotFound() {
 GroupAgent parentGroupAgent = new MyParallelGroupAgent();
 ParallelGroupAgent childParallelGroupAgent1 = new MyParallelGroupAgent();
 ParallelGroupAgent childParallelGroupAgent2 = new MyParallelGroupAgent();
 parentGroupAgent.addAgent(childParallelGroupAgent1);
 parentGroupAgent.addAgent(childParallelGroupAgent2);
 childParallelGroupAgent1.addAgent(new MyAgent("Child1"));
 childParallelGroupAgent2.addAgent(new MyAgent("Child2"));

 try {
 parentGroupAgent.findDependingParallelGroupByNames("Child2Agent", "Child2LastName");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "the group <Child2Agent, Child2LastName> in the " + parentGroupAgent + " not found";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 GroupAgent parentGroupAgent = new MySequentialGroupAgent();
 SequentialGroupAgent childSequentialGroupAgent1 = new MySequentialGroupAgent();
 SequentialGroupAgent childSequentialGroupAgent2 = new MySequentialGroupAgent();
 parentGroupAgent.addAgent(childSequentialGroupAgent1);
 parentGroupAgent.addAgent(childSequentialGroupAgent2);
 childSequentialGroupAgent1.addAgent(new MyAgent("Child1"));
 childSequentialGroupAgent2.addAgent(new MyAgent("Child2"));

 MySequentialGroupAgent expected = (MySequentialGroupAgent)childSequentialGroupAgent1;
 MySequentialGroupAgent actual = (MySequentialGroupAgent)parentGroupAgent.findDependingSequentialGroupByNames("Child1Agent", "Child1LastName");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void findDependingSequentialGroupByNames_NotFound() {
 GroupAgent parentGroupAgent = new MySequentialGroupAgent();
 SequentialGroupAgent childSequentialGroupAgent1 = new MySequentialGroupAgent();
 SequentialGroupAgent childSequentialGroupAgent2 = new MySequentialGroupAgent();
 parentGroupAgent.addAgent(childSequentialGroupAgent1);
 parentGroupAgent.addAgent(childSequentialGroupAgent2);
 childSequentialGroupAgent1.addAgent(new MyAgent("Child1"));
 childSequentialGroupAgent2.addAgent(new MyAgent("Child2"));

 try {
 parentGroupAgent.findDependingSequentialGroupByNames("Child2Agent", "Child2LastName");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "the group <Child2Agent, Child2LastName> in the " + parentGroupAgent + " not found";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new MyParallelGroupAgent());
 groupAgent.addAgent(new MyParallelGroupAgent());
 groupAgent.addAgent(new MyAgent("Child"));
 groupAgent.addAgent(new MyParallelGroupAgent());
 groupAgent.addAgent(new MyParallelGroupAgent());
 groupAgent.addAgent(new MyAgent("Child"));
 groupAgent.addAgent(new MyParallelGroupAgent());
 groupAgent.addAgent(new MyAgent("Child"));
 groupAgent.removeGapsFromParallelGroups();

 List<Agent> expected = new ArrayList<>();
 expected.add(new MyAgent("Child"));
 expected.add(new MyParallelGroupAgent());
 expected.add(new MyAgent("Child"));
 expected.add(new MyParallelGroupAgent());
 expected.add(new MyAgent("Child"));

 List<Agent> actual = groupAgent.getChildren();
 assertEquals(expected, actual);
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgent groupAgent = new MyGroupAgent();
 AgentSetter agentSetter = new AgentSetter();
 TemporaryGapAgent temporaryGapAgent1 = agentSetter.createTemporaryGapAgent(1);
 TemporaryGapAgent temporaryGapAgent2 = agentSetter.createTemporaryGapAgent(2);
 TemporaryGapAgent temporaryGapAgent3 = agentSetter.createTemporaryGapAgent(3);
 groupAgent.addAgent(temporaryGapAgent1);
 groupAgent.addAgent(temporaryGapAgent2);
 groupAgent.addAgent(temporaryGapAgent3);

 GroupAgent.TemporaryGapsToGaps map = new GroupAgent.TemporaryGapsToGaps();
 map.put(temporaryGapAgent1, new GapAgent(1));
 map.put(temporaryGapAgent2, new GapAgent(2));
 map.put(temporaryGapAgent3, new GapAgent(3));

 groupAgent.replaceTemporaryGapsByRealGaps(map);

 List<Agent> expected = new ArrayList<>();
 expected.add(new GapAgent(1));
 expected.add(new GapAgent(2));
 expected.add(new GapAgent(3));

 List<Agent> actual = groupAgent.getChildren();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddGapBefore() {
 GroupAgent groupAgent = new MyGroupAgent();
 AgentSetter agentSetter = new AgentSetter();
 Agent agent = agentSetter.createAgent("Agent");
 GapAgent gap = new GapAgent(1);

 groupAgent.addAgent(agent);
 groupAgent.addGapBefore(gap, agent);

 List<Agent> expected = new ArrayList<>();
 expected.add(gap);
 expected.add(agent);

 List<Agent> actual = groupAgent.getChildren();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddGapAfter() {
 GroupAgent groupAgent = new MyGroupAgent();
 AgentSetter agentSetter = new AgentSetter();
 Agent agent = agentSetter.createAgent("Agent");
 GapAgent gap = new GapAgent(1);

 groupAgent.addAgent(agent);
 groupAgent.addGapAfter(gap, agent);

 List<Agent> expected = new ArrayList<>();
 expected.add(agent);
 expected.add(gap);

 List<Agent> actual = groupAgent.getChildren();
 assertEquals(expected, actual);
 }
}

class MyGroupAgent extends GroupAgent {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return new GroupLayout.Group();
 }
}

class MyAgent extends Agent {
 @Override
 public String getFirstName() {
 return "Agent";
 }

 @Override
 public String getLastName() {
 return "Agent";
 }
}

class MyParallelGroupAgent extends ParallelGroupAgent {
 @Override
 protected GroupAgent.FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, new MyAgent(""));
 }
}

class MySequentialGroupAgent extends SequentialGroupAgent {
 @Override
 protected GroupAgent.FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, new MyAgent(""));
 }
}