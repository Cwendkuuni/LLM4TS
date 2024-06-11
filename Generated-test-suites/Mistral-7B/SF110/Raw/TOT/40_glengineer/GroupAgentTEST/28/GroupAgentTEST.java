import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import glengineer.agents.GroupAgent;
import glengineer.agents.Agent;
import glengineer.agents.settings.*;
import glengineer.agents.setters.*;
import glengineer.agents.GapAgent;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElementImplementation;
import java.util.*;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent childAgent1, childAgent2, childAgent3, childAgent4;
 private Settings settings;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElementImplementation getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 settings = new Settings();

 childAgent1 = new DummyAgent("FirstName1", "LastName1");
 childAgent2 = new DummyAgent("FirstName2", "LastName2");
 childAgent3 = new DummyAgent("FirstName3", "LastName3");
 childAgent4 = new DummyAgent("FirstName4", "LastName4");

 groupAgent.addAgent(childAgent1);
 groupAgent.addAgent(childAgent2);
 groupAgent.addAgent(childAgent3);
 groupAgent.addAgent(childAgent4);
 }

 @Test
 public void testGetFirstName() {
 assertEquals(childAgent1.getFirstName(), groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals(childAgent4.getLastName(), groupAgent.getLastName());
 }

 @Test
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup(childAgent1.getFirstName(), childAgent1.getLastName()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsGroup_NullFirstName() {
 groupAgent.isGroup(null, childAgent1.getLastName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsGroup_NullLastName() {
 groupAgent.isGroup(childAgent1.getFirstName(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsGroup_InvalidName() {
 groupAgent.isGroup("InvalidName", "InvalidName");
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(childAgent1);
 groupAgent.addAgent(childAgent2);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(children.size(), 4);
 assertEquals(children.get(0), childAgent1);
 assertEquals(children.get(1), childAgent2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAgent_NullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(children.size(), 4);
 assertEquals(children.get(0), childAgent1);
 assertEquals(children.get(1), childAgent2);
 assertEquals(children.get(2), childAgent3);
 assertEquals(children.get(3), childAgent4);
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(groupAgent.getNumberOfChildren(), 4);
 }

 @Test
 public void testGetComponent() {
 DummyAgent component = new DummyAgent("ComponentName", null);
 groupAgent.addAgent(component);
 Agent result = groupAgent.getComponent("ComponentName");
 assertEquals(component, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponent_NullName() {
 groupAgent.getComponent(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponent_InvalidName() {
 groupAgent.getComponent("InvalidName");
 }

 @Test
 public void testGetGroup() {
 DummyAgent group = new DummyAgent("FirstName", "LastName");
 groupAgent.addAgent(group);
 GroupAgent result = (GroupAgent)groupAgent.getGroup("FirstName", "LastName");
 assertEquals(group, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroup_NullFirstName() {
 groupAgent.getGroup(null, "LastName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroup_NullLastName() {
 groupAgent.getGroup("FirstName", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroup_InvalidName() {
 groupAgent.getGroup("InvalidName", "InvalidName");
 }

 @Test
 public void testFindDependingComponentByName() {
 DummyAgent component = new DummyAgent("ComponentName", null);
 groupAgent.addAgent(component);
 Agent result = groupAgent.findDependingComponentByName("ComponentName");
 assertEquals(component, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByName_NullName() {
 groupAgent.findDependingComponentByName(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByName_InvalidName() {
 groupAgent.findDependingComponentByName("InvalidName");
 }

 @Test
 public void testFindDependingGroupByNames() {
 DummyAgent group = new DummyAgent("FirstName", "LastName");
 groupAgent.addAgent(group);
 GroupAgent result = (GroupAgent)groupAgent.findDependingGroupByNames("FirstName", "LastName");
 assertEquals(group, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNames_NullFirstName() {
 groupAgent.findDependingGroupByNames(null, "LastName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNames_NullLastName() {
 groupAgent.findDependingGroupByNames("FirstName", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNames_InvalidName() {
 groupAgent.findDependingGroupByNames("InvalidName", "InvalidName");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 ParallelGroupAgent parallelGroup = new ParallelGroupAgent();
 parallelGroup.addAgent(childAgent1);
 groupAgent.addAgent(parallelGroup);
 ParallelGroupAgent result = (ParallelGroupAgent)groupAgent.findDependingParallelGroupByNames("FirstName1", "LastName1");
 assertEquals(parallelGroup, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNames_NullFirstName() {
 groupAgent.findDependingParallelGroupByNames(null, "LastName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNames_NullLastName() {
 groupAgent.findDependingParallelGroupByNames("FirstName", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNames_InvalidName() {
 groupAgent.findDependingParallelGroupByNames("InvalidName", "InvalidName");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 SequentialGroupAgent sequentialGroup = new SequentialGroupAgent();
 sequentialGroup.addAgent(childAgent1);
 groupAgent.addAgent(sequentialGroup);
 SequentialGroupAgent result = (SequentialGroupAgent)groupAgent.findDependingSequentialGroupByNames("FirstName1", "LastName1");
 assertEquals(sequentialGroup, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNames_NullFirstName() {
 groupAgent.findDependingSequentialGroupByNames(null, "LastName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNames_NullLastName() {
 groupAgent.findDependingSequentialGroupByNames("FirstName", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNames_InvalidName() {
 groupAgent.findDependingSequentialGroupByNames("InvalidName", "InvalidName");
 }
}