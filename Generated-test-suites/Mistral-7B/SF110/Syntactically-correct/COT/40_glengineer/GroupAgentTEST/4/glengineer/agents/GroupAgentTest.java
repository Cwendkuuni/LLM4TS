package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import glengineer.agents.*;
import glengineer.agents.settings.*;
import glengineer.agents.setters.*;
import glengineer.exceptions.IllegalArgumentException;

public class GroupAgentTest {

 private Agent agent1;
 private Agent agent2;
 private Agent agent3;
 private Agent agent4;
private GroupAgent groupAgent;

 @Before
 public void initialize() {
 groupAgent = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 agent1 = new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 agent2 = new Agent() {
 @Override
 public String getFirstName() {
 return "Jane";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 agent3 = new Agent() {
 @Override
 public String getFirstName() {
 return "Bob";
 }

 @Override
 public String getLastName() {
 return "Smith";
 }
 };
 agent4 = new Agent() {
 @Override
 public String getFirstName() {
 return "Alice";
 }

 @Override
 public String getLastName() {
 return "Williams";
 }
 };
 groupAgent.addAgent(agent1);
 groupAgent.addAgent(agent2);
 }

 @Test
 public void testGetFirstName() {
 assertEquals("John", groupAgent.getFirstName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFirstNameWhenNoChildren() {
 groupAgent = new GroupAgent();
 assertNull(groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("Doe", groupAgent.getLastName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetLastNameWhenNoChildren() {
 groupAgent = new GroupAgent();
 assertNull(groupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("componentName"));
 }

 @Test
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsGroupWithInvalidFirstName() {
 groupAgent.isGroup("InvalidFirstName", "Doe");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsGroupWithInvalidLastName() {
 groupAgent.isGroup("John", "InvalidLastName");
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent1 = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 groupAgent1.addAgent(agent1);
 groupAgent1.addAgent(agent2);
 assertTrue(groupAgent1.equals(groupAgent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAgentWithNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(groupAgent.getComponent("componentName"));
 groupAgent.addAgent(agent3);
 assertEquals(agent3, groupAgent.getComponent("componentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWithInvalidName() {
 groupAgent.getComponent("InvalidComponentName");
 }

 @Test
 public void testGetGroup() {
 assertNull(groupAgent.getGroup("InvalidFirstName", "InvalidLastName"));
 groupAgent.addAgent(agent3);
 groupAgent.addAgent(agent4);
 assertEquals(agent4, groupAgent.getGroup("Alice", "Williams"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithInvalidFirstName() {
 groupAgent.getGroup("InvalidFirstName", "Williams");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithInvalidLastName() {
 groupAgent.getGroup("John", "InvalidLastName");
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("componentName"));
 groupAgent.addAgent(agent3);
 assertEquals(agent3, groupAgent.findDependingComponentByName("componentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWithInvalidName() {
 groupAgent.findDependingComponentByName("InvalidComponentName");
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertNull(groupAgent.findDependingGroupByNames("InvalidFirstName", "InvalidLastName"));
 groupAgent.addAgent(agent3);
 groupAgent.addAgent(agent4);
 assertEquals(agent4, groupAgent.findDependingGroupByNames("Alice", "Williams"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWithInvalidFirstName() {
 groupAgent.findDependingGroupByNames("InvalidFirstName", "Williams");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWithInvalidLastName() {
 groupAgent.findDependingGroupByNames("John", "InvalidLastName");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertNull(groupAgent.findDependingParallelGroupByNames("InvalidFirstName", "InvalidLastName"));
 groupAgent.addAgent(agent3);
 groupAgent.addAgent(agent4);
 GroupAgent parallelAgent = new ParallelGroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 parallelAgent.addAgent(agent3);
 groupAgent.addAgent(parallelAgent);
 assertEquals(parallelAgent, groupAgent.findDependingParallelGroupByNames("Bob", "Smith"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWithInvalidFirstName() {
 groupAgent.findDependingParallelGroupByNames("InvalidFirstName", "Smith");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWithInvalidLastName() {
 groupAgent.findDependingParallelGroupByNames("Bob", "InvalidLastName");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertNull(groupAgent.findDependingSequentialGroupByNames("InvalidFirstName", "InvalidLastName"));
 groupAgent.addAgent(agent3);
 groupAgent.addAgent(agent4);
 GroupAgent sequentialAgent = new SequentialGroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 sequentialAgent.addAgent(agent3);
 groupAgent.addAgent(sequentialAgent);
 assertEquals(sequentialAgent, groupAgent.findDependingSequentialGroupByNames("Bob", "Smith"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWithInvalidFirstName() {
 groupAgent.findDependingSequentialGroupByNames("InvalidFirstName", "Smith");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWithInvalidLastName() {
 groupAgent.findDependingSequentialGroupByNames("Bob", "InvalidLastName");
 }
}