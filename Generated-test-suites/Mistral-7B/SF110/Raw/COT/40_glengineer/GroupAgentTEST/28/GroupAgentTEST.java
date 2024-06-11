package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.*;

import glengineer.*;
import glengineer.agents.*;
import glengineer.agents.settings.*;
import glengineer.agents.setters.*;
import org.junit.Test;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent child1, child2, child3;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
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
 return "Bob";
 }

 @Override
 public String getLastName() {
 return "Smith";
 }
 };
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 }

 @Test
 public void testGetFirstName() {
 assertEquals("John", groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("Doe", groupAgent.getLastName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFirstNameWhenNoChildren() {
 groupAgent = new GroupAgent();
 assertNull(groupAgent.getFirstName());
 }

 @Test
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup(child1.getFirstName(), child1.getLastName()));
 }

 @Test
 public void testIsGroupWithWrongFirstName() {
 assertFalse(groupAgent.isGroup("John", "Anne"));
 }

 @Test
 public void testIsGroupWithWrongLastName() {
 assertFalse(groupAgent.isGroup("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWithNonExistentComponent() {
 groupAgent.getComponent("NonExistentComponent");
 }

 @Test
 public void testGetGroupWithExistentGroup() {
 GroupAgent result = (GroupAgent)groupAgent.getGroup(child1.getFirstName(), child1.getLastName());
 assertEquals(child1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithNonExistentGroup() {
 groupAgent.getGroup("John", "Anne");
 }

 @Test
 public void testFindDependingComponentByNameWithExistentComponent() {
 ComponentAgent result = groupAgent.findDependingComponentByName("ComponentA");
 assertNotNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWithNonExistentComponent() {
 groupAgent.findDependingComponentByName("NonExistentComponent");
 }

 @Test
 public void testFindDependingGroupByNamesWithExistentGroup() {
 GroupAgent result = (GroupAgent)groupAgent.findDependingGroupByNames("John", "Doe");
 assertEquals(child1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWithNonExistentGroup() {
 groupAgent.findDependingGroupByNames("John", "Anne");
 }

 @Test
 public void testFindDependingParallelGroupByNamesWithExistentParallelGroup() {
 ParallelGroupAgent result = (ParallelGroupAgent)groupAgent.findDependingParallelGroupByNames("John", "Doe");
 assertEquals(child1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWithNonExistentParallelGroup() {
 groupAgent.findDependingParallelGroupByNames("John", "Anne");
 }

 @Test
 public void testFindDependingSequentialGroupByNamesWithExistentSequentialGroup() {
 SequentialGroupAgent result = (SequentialGroupAgent)groupAgent.findDependingSequentialGroupByNames("John", "Doe");
 assertEquals(child1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWithNonExistentSequentialGroup() {
 groupAgent.findDependingSequentialGroupByNames("John", "Anne");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(2, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.getChildren().get(0) instanceof Agent);
 assertTrue(groupAgent.getChildren().get(1) instanceof ParallelGroupAgent);
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
 map.put(child1, new GapAgent(10));
 map.put(child2, new GapAgent(5, 3, 7));
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 assertEquals(3, groupAgent.getNumberOfChildren());
 assertFalse(groupAgent.getChildren().contains(child1));
 assertFalse(groupAgent.getChildren().contains(child2));
 assertTrue(groupAgent.getChildren().contains(new GapAgent(10)));
 assertTrue(groupAgent.getChildren().contains(new GapAgent(5, 3, 7)));
 }

 @Test
 public void testAddContentToGroup() {
 // Implement this method in your test case if necessary
 }

 @Test
 public void testAddGapBefore() {
 groupAgent.addAgent(child3);
 groupAgent.addGapBefore(new GapAgent(1), child1);
 assertEquals(3, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.getChildren().get(0) instanceof GapAgent);
 assertTrue(groupAgent.getChildren().get(1) instanceof Agent);
 assertTrue(groupAgent.getChildren().get(2) instanceof Agent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapBeforeWithInvalidElement() {
 groupAgent.addAgent(child3);
 try {
 groupAgent.addGapBefore(new GapAgent(1), null);
 } catch (IllegalArgumentException e) {
 assertEquals("Cannot add gap before null elements.", e.getMessage());
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddGapBeforeWithInvalidPosition() {
 groupAgent.addAgent(child3);
 try {
 groupAgent.addGapBefore(new GapAgent(1), child1);
 groupAgent.addAgent(child3);
 groupAgent.addAgent(child3);
 groupAgent.addAgent(child3);
 groupAgent.addAgent(child3);
 groupAgent.addGapBefore(new GapAgent(1), 5);
 } catch (IndexOutOfBoundsException e) {
 assertEquals("Index: 5, Size: 5", e.getMessage());
 }
 }

 @Test
 public void testAddGapAfter() {
 groupAgent.addAgent(child3);
 groupAgent.addGapAfter(new GapAgent(1), child1);
 assertEquals(4, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.getChildren().get(0) instanceof Agent);
 assertTrue(groupAgent.getChildren().get(1) instanceof GapAgent);
 assertTrue(groupAgent.getChildren().get(2) instanceof Agent);
 assertTrue(groupAgent.getChildren().get(3) instanceof Agent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapAfterWithInvalidElement() {
 groupAgent.addAgent(child3);
 try {
 groupAgent.addGapAfter(new GapAgent(1), null);
 } catch (IllegalArgumentException e) {
 assertEquals("Cannot add gap after null elements.", e.getMessage());
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddGapAfterWithInvalidPosition() {
 groupAgent.addAgent(child3);
 try {
 groupAgent.addGapAfter(new GapAgent(1), 5);
 } catch (IndexOutOfBoundsException e) {
 assertEquals("Index: 5, Size: 3", e.getMessage());
 }
 }
}