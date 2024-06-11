import org.junit.Test;
import static org.junit.Assert.*;
import glengineer.agents.GroupAgent;
import glengineer.agents.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.Agent;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GapAgent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GroupAgentTest {

 @Test
 public void testGetFirstName() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new AgentSetter("FirstName1", "LastName1").createAgent());
 groupAgent.addAgent(new AgentSetter("FirstName2", "LastName2").createAgent());
 assertEquals("FirstName1", groupAgent.getFirstName());
 }

 @Test
 public void testGetFirstNameWhenEmpty() {
 GroupAgent groupAgent = new MyGroupAgent();
 assertNull(groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new AgentSetter("FirstName1", "LastName1").createAgent());
 groupAgent.addAgent(new AgentSetter("FirstName2", "LastName2").createAgent());
 assertEquals("LastName1", groupAgent.getLastName());
 }

 @Test
 public void testGetLastNameWhenEmpty() {
 GroupAgent groupAgent = new MyGroupAgent();
 assertNull(groupAgent.getLastName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 GroupAgent groupAgent = new MyGroupAgent();
 Agent child1 = new AgentSetter("Child1FirstName", "Child1LastName").createAgent();
 Agent child2 = new AgentSetter("Child2FirstName", "Child2LastName").createAgent();
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 GroupAgent groupAgent = new MyGroupAgent();
 Agent child1 = new AgentSetter("Child1FirstName", "Child1LastName").createAgent();
 Agent child2 = new AgentSetter("Child2FirstName", "Child2LastName").createAgent();
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testIsGroup() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new AgentSetter("FirstName1", "LastName1").createAgent());
 assertEquals(true, groupAgent.isGroup("FirstName1", "LastName1"));
 }

 @Test
 public void testIsGroupFalse() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new AgentSetter("FirstName1", "LastName1").createAgent());
 assertEquals(false, groupAgent.isGroup("FirstName2", "LastName2"));
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent1 = new MyGroupAgent();
 groupAgent1.addAgent(new AgentSetter("FirstName1", "LastName1").createAgent());
 GroupAgent groupAgent2 = new MyGroupAgent();
 groupAgent2.addAgent(new AgentSetter("FirstName1", "LastName1").createAgent());
 assertEquals(true, groupAgent1.equals(groupAgent2));
 }

 @Test
 public void testEqualsDifferentFirstName() {
 GroupAgent groupAgent1 = new MyGroupAgent();
 groupAgent1.addAgent(new AgentSetter("FirstName1", "LastName1").createAgent());
 GroupAgent groupAgent2 = new MyGroupAgent();
 groupAgent2.addAgent(new AgentSetter("FirstName2", "LastName1").createAgent());
 assertEquals(false, groupAgent1.equals(groupAgent2));
 }

 @Test
 public void testEqualsDifferentLastName() {
 GroupAgent groupAgent1 = new MyGroupAgent();
 groupAgent1.addAgent(new AgentSetter("FirstName1", "LastName1").createAgent());
 GroupAgent groupAgent2 = new MyGroupAgent();
 groupAgent2.addAgent(new AgentSetter("FirstName1", "LastName2").createAgent());
 assertEquals(false, groupAgent1.equals(groupAgent2));
 }

 @Test
 public void testAddAgentToEmptyGroup() {
 GroupAgent groupAgent = new MyGroupAgent();
 Agent agentToAdd = new AgentSetter("FirstName1", "LastName1").createAgent();
 groupAgent.addAgent(agentToAdd);
 assertEquals(1, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 GroupAgent groupAgent = new MyGroupAgent();
 ComponentAgent componentAgent = new AgentSetter("ComponentName", "ComponentLastName").createAgent();
 groupAgent.addAgent(componentAgent);
 assertEquals(componentAgent, groupAgent.getComponent("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWhenComponentDoesntExist() {
 GroupAgent groupAgent = new MyGroupAgent();
 assertEquals(null, groupAgent.getComponent("NonExistentComponentName"));
 }

 @Test
 public void testGetGroup() {
 GroupAgent groupAgent = new MyGroupAgent();
 GroupAgent groupToFind = new MyGroupAgent();
 groupAgent.addAgent(groupToFind);
 assertEquals(groupToFind, groupAgent.getGroup("FirstName1", "LastName1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWhenGroupDoesntExist() {
 GroupAgent groupAgent = new MyGroupAgent();
 assertEquals(null, groupAgent.getGroup("FirstName2", "LastName2"));
 }

 @Test
 public void testFindDependingComponentByName() {
 GroupAgent groupAgent = new MyGroupAgent();
 ComponentAgent componentAgent1 = new AgentSetter("ComponentName1", "ComponentLastName1").createAgent();
 ComponentAgent componentAgent2 = new AgentSetter("ComponentName2", "ComponentLastName2").createAgent();
 groupAgent.addAgent(componentAgent1);
 groupAgent.addAgent(componentAgent2);
 assertEquals(componentAgent1, groupAgent.findDependingComponentByName("ComponentName1"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent groupAgent = new MyGroupAgent();
 GroupAgent groupToFind1 = new MyGroupAgent();
 GroupAgent groupToFind2 = new MyGroupAgent();
 groupAgent.addAgent(groupToFind1);
 groupAgent.addAgent(groupToFind2);
 assertEquals(groupToFind1, groupAgent.findDependingGroupByNames("FirstName1", "LastName1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWhenGroupDoesntExist() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new MyGroupAgent());
 groupAgent.addAgent(new MyGroupAgent());
 assertEquals(null, groupAgent.findDependingGroupByNames("FirstName2", "LastName2"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 GroupAgent groupAgent = new MyGroupAgent();
 ParallelGroupAgent parallelGroupAgent1 = new MyParallelGroupAgent();
 ParallelGroupAgent parallelGroupAgent2 = new MyParallelGroupAgent();
 groupAgent.addAgent(parallelGroupAgent1);
 groupAgent.addAgent(parallelGroupAgent2);
 assertEquals(parallelGroupAgent1, groupAgent.findDependingParallelGroupByNames("FirstName1", "LastName1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWhenGroupDoesntExist() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new MyParallelGroupAgent());
 groupAgent.addAgent(new MyParallelGroupAgent());
 assertEquals(null, groupAgent.findDependingParallelGroupByNames("FirstName2", "LastName2"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 GroupAgent groupAgent = new MyGroupAgent();
 SequentialGroupAgent sequentialGroupAgent1 = new MySequentialGroupAgent();
 SequentialGroupAgent sequentialGroupAgent2 = new MySequentialGroupAgent();
 groupAgent.addAgent(sequentialGroupAgent1);
 groupAgent.addAgent(sequentialGroupAgent2);
 assertEquals(sequentialGroupAgent1, groupAgent.findDependingSequentialGroupByNames("FirstName1", "LastName1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWhenGroupDoesntExist() {
 GroupAgent groupAgent = new MyGroupAgent();
 groupAgent.addAgent(new MySequentialGroupAgent());
 groupAgent.addAgent(new MySequentialGroupAgent());
 assertEquals(null, groupAgent.findDependingSequentialGroupByNames("FirstName2", "LastName2"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent groupAgent = new MyGroupAgent();
 ParallelGroupAgent parallelGroupAgent1 = new MyParallelGroupAgent();
 ParallelGroupAgent parallelGroupAgent2 = new MyParallelGroupAgent();
 GroupAgent gapAgent1 = new MyGapAgent();
 GroupAgent gapAgent2 = new MyGapAgent();
 groupAgent.addAgent(parallelGroupAgent1);
 groupAgent.addAgent(gapAgent1);
 groupAgent.addAgent(gapAgent2);
 groupAgent.addAgent(parallelGroupAgent2);
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgent groupAgent = new MyGroupAgent();
 TemporaryGapAgent temporaryGapAgent1 = new MyTemporaryGapAgent();
 TemporaryGapAgent temporaryGapAgent2 = new MyTemporaryGapAgent();
 groupAgent.addAgent(temporaryGapAgent1);
 groupAgent.addAgent(temporaryGapAgent2);
 GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
 map.put(temporaryGapAgent1, new MyGapAgent());
 map.put(temporaryGapAgent2, new MyGapAgent());
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testAddContentToGroup() {
 GroupAgent groupAgent = new MyGroupAgent();
 Agent elementToAdd = new AgentSetter("FirstName1", "LastName1").createAgent();
 groupAgent.addContentToGroup(new MyGroupLayoutEngineer(), new MyGroupCreatorAndElementAdder());
 assertEquals(elementToAdd, groupAgent.findDependingComponentByName("FirstName1"));
 }

 @Test
 public void testGroupContent() {
 GroupAgent groupAgent = new MyGroupAgent();
 return groupAgent.groupContent(new MyGroupCreatorAndElementAdder());
 }

 @Test
 public void testGetFunctionsOnGroupImplemenation() {
 GroupAgent groupAgent = new MyGroupAgent();
 FunctionsOnGroupAndElement functionsOnGroup = groupAgent.getFunctionsOnGroupImplemenation();
 }

 @Test
 public void testAddGapBefore() {
 GroupAgent groupAgent = new MyGroupAgent();
 GapAgent gapAgent = new MyGapAgent();
 Agent element = new AgentSetter("ElementFirstName", "ElementLastName").createAgent();
 groupAgent.addAgent(element);
 groupAgent.addGapBefore(gapAgent, element);
 assertEquals(gapAgent, groupAgent.getChildren().get(0));
 }

 @Test
 public void testAddGapAfter() {
 GroupAgent groupAgent = new MyGroupAgent();
 GapAgent gapAgent = new MyGapAgent();
 Agent element = new AgentSetter("ElementFirstName", "ElementLastName").createAgent();
 groupAgent.addAgent(element);
 groupAgent.addGapAfter(gapAgent, element);
 assertEquals(element, groupAgent.getChildren().get(0));
 assertEquals(gapAgent, groupAgent.getChildren().get(1));
 }
}

class MyGroupAgent extends GroupAgent {
 @Override
 protected List<Agent> groupContent(final GroupCreatorAndElementAdder p0) {
 List<Agent> result = new ArrayList<>();
 result.add(new AgentSetter("Child1FirstName", "Child1LastName").createAgent());
 result.add(new AgentSetter("Child2FirstName", "Child2LastName").createAgent());
 return result;
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, new AgentSetter("ElementFirstName", "ElementLastName").createAgent());
 }
}

class MyParallelGroupAgent extends ParallelGroupAgent {
 @Override
 public Agent getGapsType() {
 return new MyGapAgent();
 }
}

class MySequentialGroupAgent extends SequentialGroupAgent {
 @Override
 public Agent getGapsType() {
 return new MyGapAgent();
 }
}

class MyTemporaryGapAgent extends TemporaryGapAgent {
}

class MyGapAgent extends GapAgent {
}

class MyGroupLayoutEngineer implements GroupLayoutEngineer {
 // Implement the methods required by the GroupLayoutEngineer interface
}

class MyGroupCreatorAndElementAdder implements GroupCreatorAndElementAdder {
 // Implement the methods required by the GroupCreatorAndElementAdder interface
}