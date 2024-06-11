package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import glengineer.*;
import glengineer.agents.*;
import glengineer.agents.settings.*;
import glengineer.agents.setters.*;

public class GroupAgentTest {

 @BeforeClass
 static public void setUpBeforeClass() {
 // Initialize any static variables or perform any initialization that needs to be done before running any test.
 }

 @AfterClass
 static public void tearDownAfterClass() {
 // Perform any cleanup that needs to be done after all tests are executed.
 }

 @Before
 static public void setUp() {
 // Initialize any variables or perform any initialization that needs to be done before executing each test.
 }

 @After
 static public void tearDown() {
 // Perform any cleanup that needs to be done after each test is executed.
 }

 @Test
 public void testGetFirstName() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 Agent child1 = new Agent() {
 @Override
 String getFirstName() {
 return "John";
 }
 };
 Agent child2 = new Agent() {
 @Override
 String getFirstName() {
 return "Jane";
 }
 };
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 String firstName = groupAgent.getFirstName();
 assertEquals("John", firstName);
 }

 @Test
 public void testGetLastName() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 Agent child1 = new Agent() {
 @Override
 String getLastName() {
 return "Doe";
 }
 };
 Agent child2 = new Agent() {
 @Override
 String getLastName() {
 return "Smith";
 }
 };
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 String lastName = groupAgent.getLastName();
 assertEquals("Doe", lastName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetFirstNameWhenEmpty() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 String firstName = groupAgent.getFirstName();
 fail("Expected an IllegalStateException but got " + firstName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastNameWhenEmpty() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 String lastName = groupAgent.getLastName();
 fail("Expected an IllegalStateException but got " + lastName);
 }

 @Test
 public void testIsGroup() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsNotGroup() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 assertFalse(groupAgent.isGroup("John", "Smith"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsGroupWithNullFirstName() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 assertFalse(groupAgent.isGroup(null, "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsGroupWithNullLastName() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 assertFalse(groupAgent.isGroup("John", null));
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent1 = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 groupAgent1.addAgent(new Agent() {
 @Override
 String getFirstName() {
 return "John";
 }

 @Override
 String getLastName() {
 return "Doe";
 }
 });
 groupAgent2.addAgent(new Agent() {
 @Override
 String getFirstName() {
 return "John";
 }

 @Override
 String getLastName() {
 return "Doe";
 }
 });
 assertTrue(groupAgent1.equals(groupAgent2));
 }

 @Test
 public void testNotEquals() {
 GroupAgent groupAgent1 = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 groupAgent1.addAgent(new Agent() {
 @Override
 String getFirstName() {
 return "John";
 }

 @Override
 String getLastName() {
 return "Doe";
 }
 });
 groupAgent2.addAgent(new Agent() {
 @Override
 String getFirstName() {
 return "John";
 }

 @Override
 String getLastName() {
 return "Smith";
 }
 });
 assertFalse(groupAgent1.equals(groupAgent2));
 }

 @Test
 public void testAddAgent() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 Agent child = new Agent() {
 @Override
 String getFirstName() {
 return "Child";
 }

 @Override
 String getLastName() {
 return "Agent";
 }
 };
 groupAgent.addAgent(child);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(1, children.size());
 assertTrue(children.contains(child));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 try {
 groupAgent.addAgent(null);
 fail("Expected an IllegalArgumentException but got no exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetChildren() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 Agent child1 = new Agent() {
 @Override
 String getFirstName() {
 return "Child1";
 }

 @Override
 String getLastName() {
 return "Agent1";
 }
 };
 Agent child2 = new Agent() {
 @Override
 String getFirstName() {
 return "Child2";
 }

 @Override
 String getLastName() {
 return "Agent2";
 }
 };
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 }

 @Test
 public void testGetNumberOfChildren() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 Agent child1 = new Agent() {
 @Override
 String getFirstName() {
 return "Child1";
 }

 @Override
 String getLastName() {
 return "Agent1";
 }
 };
 Agent child2 = new Agent() {
 @Override
 String getFirstName() {
 return "Child2";
 }

 @Override
 String getLastName() {
 return "Agent2";
 }
 };
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 int numberOfChildren = groupAgent.getNumberOfChildren();
 assertEquals(2, numberOfChildren);
 }

 @Test
 public void testGetComponent() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 ComponentAgent componentAgent = new ComponentAgent() {
 @Override
 String getName() {
 return "ComponentAgent";
 }
 };
 groupAgent.addAgent(componentAgent);
 ComponentAgent result = groupAgent.getComponent("ComponentAgent");
 assertEquals(componentAgent, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWithNullName() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 ComponentAgent componentAgent = new ComponentAgent() {
 @Override
 String getName() {
 return "ComponentAgent";
 }
 };
 groupAgent.addAgent(componentAgent);
 try {
 groupAgent.getComponent(null);
 fail("Expected an IllegalArgumentException but got no exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWithNonExistentComponent() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 ComponentAgent componentAgent = new ComponentAgent() {
 @Override
 String getName() {
 return "ComponentAgent";
 }
 };
 groupAgent.addAgent(componentAgent);
 try {
 groupAgent.getComponent("NonExistentComponent");
 fail("Expected an IllegalArgumentException but got no exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetGroup() {
 GroupAgent parentGroupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 GroupAgent childGroupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 parentGroupAgent.addAgent(childGroupAgent);
 GroupAgent result = parentGroupAgent.getGroup("John", "Doe");
 assertEquals(childGroupAgent, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithNullFirstName() {
 GroupAgent parentGroupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 GroupAgent childGroupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 parentGroupAgent.addAgent(childGroupAgent);
 try {
 parentGroupAgent.getGroup(null, "Doe");
 fail("Expected an IllegalArgumentException but got no exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithNullLastName() {
 GroupAgent parentGroupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 GroupAgent childGroupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 parentGroupAgent.addAgent(childGroupAgent);
 try {
 parentGroupAgent.getGroup("John", null);
 fail("Expected an IllegalArgumentException but got no exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithNonExistentGroup() {
 GroupAgent parentGroupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 GroupAgent childGroupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 parentGroupAgent.addAgent(childGroupAgent);
 try {
 parentGroupAgent.getGroup("NonExistentFirstName", "Doe");
 fail("Expected an IllegalArgumentException but got no exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 groupAgent.addAgent(new GapAgent(10));
 groupAgent.addAgent(new GroupAgent());
 groupAgent.addAgent(new GapAgent(5));
 groupAgent.addAgent(new GroupAgent());
 groupAgent.addAgent(new GapAgent(3));
 groupAgent.removeGapsFromParallelGroups();
 List<Agent> children = groupAgent.getChildren();
 assertFalse(children.contains(new GapAgent(3)));
 assertFalse(children.contains(new GapAgent(5)));
 assertFalse(children.contains(new GapAgent(10)));
 }

 @Test
 public void testAddGapBefore() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 Agent element = new Agent() {
 @Override
 String getFirstName() {
 return "Element";
 }

 @Override
 String getLastName() {
 return "Agent";
 }
 };
 GapAgent gap = new GapAgent(5);
 groupAgent.addAgent(element);
 groupAgent.addGapBefore(gap, element);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 assertTrue(children.get(0) instanceof GapAgent);
 assertTrue(children.get(1) instanceof Agent);
 assertEquals(gap, children.get(0));
 assertEquals(element, children.get(1));
 }

 @Test
 public void testAddGapAfter() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 Agent element = new Agent() {
 @Override
 String getFirstName() {
 return "Element";
 }

 @Override
 String getLastName() {
 return "Agent";
 }
 };
 GapAgent gap = new GapAgent(5);
 groupAgent.addAgent(element);
 groupAgent.addGapAfter(gap, element);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 assertTrue(children.get(0) instanceof Agent);
 assertTrue(children.get(1) instanceof GapAgent);
 assertEquals(element, children.get(0));
 assertEquals(gap, children.get(1));
 }
}