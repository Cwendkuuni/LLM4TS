package glengineer.agents.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.*;
import glengineer.agents.settings.*;
import glengineer.exceptions.*;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent child1;
 private Agent child2;
 private Agent child3;
 private Agent child4;
 private Agent childGap;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // Overridden method body
 throw new UnsupportedOperationException("Not yet implemented");
 }

 @Override
 abstract FunctionsOnGroup getFunctionsOnGroupImplemenation();
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

 @Override
 public boolean equals(Agent agent) {
 return false;
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

 @Override
 public boolean equals(Agent agent) {
 return false;
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

 @Override
 public boolean equals(Agent agent) {
 return false;
 }
 };
 child4 = new Agent() {
 @Override
 public String getFirstName() {
 return "Alice";
 }

 @Override
 public String getLastName() {
 return "Johnson";
 }

 @Override
 public boolean equals(Agent agent) {
 return false;
 }
 };
 childGap = new GapAgent(10);
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addAgent(child3);
 }

 @Test
 public void testGetFirstName() {
 Agent firstAgent = groupAgent.getGroup(child1.getFirstName(), child1.getLastName());
 assertEquals(child1.getFirstName(), firstAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 Agent lastAgent = groupAgent.getGroup(child1.getFirstName(), child1.getLastName());
 assertEquals(child1.getLastName(), lastAgent.getLastName());
 }

 @Test
 public void testGetChildrenSize() {
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetGroupWithEmptyChildren() {
 assertNull(groupAgent.getGroup(null, null));
 }

 @Test
 public void testAddAgentWithNullChild() {
 try {
 groupAgent.addAgent(null);
 fail("An IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(3, children.size());
 }

 @Test
 public void testFindDependingComponentByName() {
 Agent component = groupAgent.findDependingComponentByName("componentName");
 assertNull(component);
 }

 @Test
 public void testFindDependingGroupByNames() {
 try {
 groupAgent.findDependingGroupByNames("firstName1", "lastName1");
 fail("An IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFindDependingGroupByNamesWithExistingGroup() {
 GroupAgent existingGroup = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // Overridden method body
 throw new UnsupportedOperationException("Not yet implemented");
 }

 @Override
 abstract FunctionsOnGroup getFunctionsOnGroupImplemenation();
 };existingGroup.addAgent(child1);
 existingGroup.addAgent(child2);
 existingGroup.addAgent(child3);
Agent foundGroup = groupAgent.findDependingGroupByNames("John", "Doe");
 assertEquals(existingGroup, foundGroup);
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent parallelGroup = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 // Overridden method body
 throw new UnsupportedOperationException("Not yet implemented");
 }

 @Override
 abstract FunctionsOnGroup getFunctionsOnGroupImplemenation();
 };
 parallelGroup.addAgent(child1);
 parallelGroup.addAgent(childGap);
 parallelGroup.addAgent(child2);
 parallelGroup.addAgent(child3);
 parallelGroup.removeGapsFromParallelGroups();
 List<Agent> children = parallelGroup.getChildren();
 assertEquals(3, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 assertTrue(children.contains(child3));
 assertFalse(children.contains(childGap));
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
 map.put(childGap, new GapAgent(10));
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(4, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 assertTrue(children.contains(child3));
 assertTrue(children.contains(childGap));
 }

 @Test
 public void testAddGapsBeforeAndAfter() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addGapBefore(childGap, child1);
 groupAgent.addGapAfter(childGap, child2);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(4, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 assertTrue(children.contains(childGap));
 assertTrue(children.get(0) instanceof GapAgent);
 assertTrue(children.get(2) instanceof GapAgent);
 }
}