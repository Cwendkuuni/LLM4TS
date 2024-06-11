import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import glengineer.agents.GroupAgent;
import glengineer.agents.Agent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.AgentSetterFactory;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GapAgent;
import java.util.LinkedList;
import java.util.List;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent child1, child2, child3;
 private Settings settings;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null; // not used in this test
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null) {};
 }
 };
 settings = new Settings();
 child1 = new AgentSetterFactory().createAgent("Child1", "FirstName1", "LastName1");
 child2 = new AgentSetterFactory().createAgent("Child2", "FirstName2", "LastName2");
 child3 = new AgentSetterFactory().createAgent("Child3", "FirstName3", "LastName3");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 }

 @Test
 public void testGetFirstName() {
 assertEquals("FirstName1", groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("LastName1", groupAgent.getLastName());
 }

 @Test
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup("FirstName1", "LastName1"));
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null; // not used in this test
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null) {};
 }
 };
 groupAgent2.addAgent(child1);
 groupAgent2.addAgent(child2);
 assertTrue(groupAgent.equals(groupAgent2));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(child3);
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 fail("Should have thrown IllegalArgumentException.");
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(2, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 }

 @Test
 public void testGetComponent() {
 assertNull(groupAgent.getComponent("ComponentName"));
 }

 @Test
 public void testGetGroup() {
 assertNull(groupAgent.getGroup("FirstName2", "LastName2"));
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertNull(groupAgent.findDependingGroupByNames("FirstName2", "LastName2"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertNull(groupAgent.findDependingParallelGroupByNames("FirstName2", "LastName2"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertNull(groupAgent.findDependingSequentialGroupByNames("FirstName2", "LastName2"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testAddGapBefore() {
 groupAgent.addAgent(child3);
 groupAgent.addGapBefore(new GapAgent(10), child1);
 assertEquals(4, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.getChildren().contains(child1));
 assertTrue(groupAgent.getChildren().get(1) instanceof GapAgent);
 }

 @Test
 public void testAddGapAfter() {
 groupAgent.addAgent(child3);
 groupAgent.addGapAfter(new GapAgent(10), child1);
 assertEquals(4, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.getChildren().get(2) instanceof GapAgent);
 }
}