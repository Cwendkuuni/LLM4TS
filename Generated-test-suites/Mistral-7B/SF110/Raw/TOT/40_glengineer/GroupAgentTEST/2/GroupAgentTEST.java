import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import glengineer.agents.*;
import java.util.*;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent child1, child2, child3, childGap1, childGap2;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // implementation of groupContent method
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
 childGap1 = new GapAgent(10);
 childGap2 = new GapAgent(5);

 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addAgent(child3);
 groupAgent.addAgent(childGap1);
 groupAgent.addAgent(childGap2);
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
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsNotGroup() {
 assertFalse(groupAgent.isGroup("John", "Smith"));
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(child3);
 assertEquals(5, groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(4, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 assertTrue(children.contains(child3));
 assertTrue(children.contains(childGap1));
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(4, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("NonExistentComponentName"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertNull(groupAgent.findDependingGroupByNames("NonExistentFirstName", "NonExistentLastName"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertNull(groupAgent.findDependingParallelGroupByNames("NonExistentFirstName", "NonExistentLastName"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertNull(groupAgent.findDependingSequentialGroupByNames("NonExistentFirstName", "NonExistentLastName"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(3, groupAgent.getNumberOfChildren());
 }
}