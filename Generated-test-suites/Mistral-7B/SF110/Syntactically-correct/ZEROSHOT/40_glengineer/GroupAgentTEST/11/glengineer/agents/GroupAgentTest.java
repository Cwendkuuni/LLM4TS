package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.GroupAgentSetter;
import glengineer.exceptions.IllegalArgumentException;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.GroupAgent.GroupLayout.Group;
import glengineer.agents.components.ComponentAgent;
import glengineer.agents.gaps.GapAgent;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private ComponentAgent componentAgent;
 private GroupAgent childGroupAgent;
 private List<Agent> children;
 private Settings settings;

 @Before
 public void setup() {
 groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 childGroupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 componentAgent = new AgentSetter().setFirstName("John").setLastName("Doe").createComponentAgent();
 settings = new Settings();
 groupAgent.setSettings(settings);
 groupAgent.addAgent(componentAgent);
 children = groupAgent.getChildren();
 }

 @Test
 public void testGetFirstName() {
 assertEquals("John", groupAgent.getFirstName());
 assertEquals(null, childGroupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("Doe", groupAgent.getLastName());
 assertEquals(null, childGroupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("componentName"));
 assertTrue(componentAgent.isComponent("componentName"));
 }

 @Test
 public void testIsGroup() {
 assertFalse(groupAgent.isGroup("firstName", "lastName"));
 assertTrue(groupAgent.isGroup(groupAgent.getFirstName(), groupAgent.getLastName()));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 assertFalse(groupAgent.equals(childGroupAgent));
 assertTrue(groupAgent.equals(groupAgent));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(childGroupAgent);
 children = groupAgent.getChildren();
 assertEquals(2, children.size());
 }

 @Test
 public void testGetChildren() {
 assertEquals(0, groupAgent.getNumberOfChildren());
 assertEquals(1, componentAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(groupAgent.getComponent("componentName"));
 assertEquals(componentAgent, groupAgent.getComponent("componentName"));
 }

 @Test
 public void testGetGroup() {
 assertNull(groupAgent.getGroup("firstName", "lastName"));
 assertEquals(childGroupAgent, groupAgent.getGroup("childFirstName", "childLastName"));
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("componentName"));
 assertEquals(componentAgent, groupAgent.findDependingComponentByName("componentName"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertNull(groupAgent.findDependingGroupByNames("firstName", "lastName"));
 assertEquals(childGroupAgent, groupAgent.findDependingGroupByNames("childFirstName", "childLastName"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.addAgent(childGroupAgent);
 childGroupAgent.addAgent(new GapAgent(10));
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(1, children.size());
 }

 @Test
 public void testAddGapBefore() {
 groupAgent.addAgent(childGroupAgent);
 groupAgent.addGapBefore(new GapAgent(5), componentAgent);
 assertEquals(3, children.size());
 }

 @Test
 public void testAddGapAfter() {
 groupAgent.addAgent(childGroupAgent);
 groupAgent.addGapAfter(new GapAgent(5), componentAgent);
 assertEquals(3, children.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddComponentWithNull() {
 groupAgent.addAgent(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddComponentWithEmptyList() {
 groupAgent.addAgent(new LinkedList<Agent>());
 }
}