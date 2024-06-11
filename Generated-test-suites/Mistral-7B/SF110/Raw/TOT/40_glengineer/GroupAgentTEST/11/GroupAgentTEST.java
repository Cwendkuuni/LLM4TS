package glengineer.agents.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.GapAgent;
import glengineer.agents.setters.TemporaryGapAgent;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private List<Agent> children;
 private Agent child1, child2, child3;

 @Before
 public void setup() {
 groupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }
 };
 children = new ArrayList<>();
 child1 = new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 child2 = new Agent() {
 @Override
 public String getFirstName() {
 return "Jane";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 child3 = new Agent() {
 @Override
 public String getFirstName() {
 return null;
 }

 @Override
 public String getLastName() {
 return null;
 }
 };
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addAgent(child3);
 }

 @Test
 public void testGetFirstName() {
 assertEquals("John", groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("Doe", groupAgent.getLastName());
 }

 @Test
 public void testIsGroupWithCorrectFirstNameLastName() {
 assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGroupWithIncorrectFirstNameLastName() {
 assertFalse(groupAgent.isGroup("John", "Smith"));
 }

 @Test
 public void testIsComponentWithCorrectName() {
 assertFalse(groupAgent.isComponent("nonExistentComponent"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsComponentWithNullName() {
 groupAgent.isComponent(null);
 }

 @Test
 public void testEqualsWithCorrectGroupAgent() {
 GroupAgent otherGroupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }
 };
 otherGroupAgent.addAgent(child1);
 otherGroupAgent.addAgent(child2);
 otherGroupAgent.addAgent(child3);
 assertTrue(groupAgent.equals(otherGroupAgent));
 }

 @Test
 public void testEqualsWithDifferentGroupAgent() {
 GroupAgent otherGroupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }
 };
 otherGroupAgent.addAgent(child1);
 otherGroupAgent.addAgent(child4); // Add a new child
 assertFalse(groupAgent.equals(otherGroupAgent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 assertEquals(children, groupAgent.getChildren());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponentWithCorrectName() {
 assertEquals(child1, groupAgent.getComponent("John Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWithIncorrectName() {
 groupAgent.getComponent("nonExistentComponent");
 }

 @Test
 public void testGetGroupWithCorrectFirstNameLastName() {
 assertEquals(groupAgent, groupAgent.getGroup("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithIncorrectFirstNameLastName() {
 groupAgent.getGroup("John", "Smith");
 }

 @Test
 public void testFindDependingComponentByNameWithCorrectName() {
 assertEquals(child1, groupAgent.findDependingComponentByName("John Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWithIncorrectName() {
 groupAgent.findDependingComponentByName("nonExistentComponent");
 }

 @Test
 public void testFindDependingGroupByNamesWithCorrectFirstNameLastName() {
 assertEquals(groupAgent, groupAgent.findDependingGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWithIncorrectFirstNameLastName() {
 groupAgent.findDependingGroupByNames("John", "Smith");
 }

 @Test
 public void testFindDependingParallelGroupByNamesWithCorrectFirstNameLastName() {
 // Assuming GroupAgent implements ParallelGroupAgent
 assertEquals(groupAgent, groupAgent.findDependingParallelGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWithIncorrectFirstNameLastName() {
 groupAgent.findDependingParallelGroupByNames("John", "Smith");
 }

 @Test
 public void testFindDependingSequentialGroupByNamesWithCorrectFirstNameLastName() {
 // Assuming GroupAgent implements SequentialGroupAgent
 assertEquals(groupAgent, groupAgent.findDependingSequentialGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWithIncorrectFirstNameLastName() {
 groupAgent.findDependingSequentialGroupByNames("John", "Smith");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 // Assuming GroupAgent implements removal methods
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(3, groupAgent.getNumberOfChildren());
 List<Agent> childrenAfterRemoval = new ArrayList<>(groupAgent.getChildren());
 assertFalse(childrenAfterRemoval.contains(child3));
 }
}