package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import java.util.*;
import static java.util.Arrays.asList;

public class GroupAgentTest {

 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 throw new UnsupportedOperationException();
 }
 };
 Settings settings = new Settings();
 List<Agent> children = new ArrayList<>();
 AgentSetter agentSetter = new AgentSetter();

 @Test
 public void testGetFirstName() {
 children.add(agentSetter.setFirstName("John").setLastName("Doe").createAgent());
 children.add(agentSetter.setFirstName("Jane").setLastName("Doe").createAgent());
 groupAgent.setChildren(children);
 assertEquals("John", groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 children.add(agentSetter.setFirstName("John").setLastName("Doe").createAgent());
 children.add(agentSetter.setFirstName("Jane").setLastName("Doe").createAgent());
 groupAgent.setChildren(children);
 assertEquals("Doe", groupAgent.getLastName());
 }

 @Test
 public void testIsGroup() {
 children.add(agentSetter.setFirstName("John").setLastName("Doe").createAgent());
 children.add(agentSetter.setFirstName("Jane").setLastName("Doe").createAgent());
 groupAgent.setChildren(children);
 assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsComponent() {
 children.add(agentSetter.setFirstName("ComponentAgent").setLastName("CA").createAgent());
 children.add(agentSetter.setFirstName("GroupAgent").setLastName("GA").createAgent());
 groupAgent.setChildren(children);
 assertFalse(groupAgent.isComponent("ComponentAgent"));
 }

 @Test
 public void testEquals() {
 children.add(agentSetter.setFirstName("John").setLastName("Doe").createAgent());
 children.add(agentSetter.setFirstName("Jane").setLastName("Doe").createAgent());
 GroupAgent groupAgent1 = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 throw new UnsupportedOperationException();
 }
 };
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 throw new UnsupportedOperationException();
 }
 };
 groupAgent1.setChildren(children);
 groupAgent2.setChildren(children);
 assertTrue(groupAgent1.equals(groupAgent2));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(agentSetter.setFirstName("Agent1").setLastName("A1").createAgent());
 assertEquals(1, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetChildren() {
 children.add(agentSetter.setFirstName("Agent1").setLastName("A1").createAgent());
 children.add(agentSetter.setFirstName("Agent2").setLastName("A2").createAgent());
 groupAgent.setChildren(children);
 List<Agent> result = groupAgent.getChildren();
 assertEquals(2, result.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 children.add(agentSetter.setFirstName("Agent1").setLastName("A1").createAgent());
 children.add(agentSetter.setFirstName("Agent2").setLastName("A2").createAgent());
 groupAgent.setChildren(children);
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 children.add(agentSetter.setFirstName("ComponentAgent").setLastName("CA").createAgent());
 groupAgent.setChildren(children);
 assertEquals(agentSetter.setFirstName("ComponentAgent").setLastName("CA").createAgent(), groupAgent.getComponent("CA"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentThrowsException() {
 children.add(agentSetter.setFirstName("Agent1").setLastName("A1").createAgent());
 groupAgent.setChildren(children);
 groupAgent.getComponent("ComponentAgent");
 }

 @Test
 public void testGetGroup() {
 children.add(agentSetter.setFirstName("GroupAgent1").setLastName("GA1").createAgent());
 children.add(agentSetter.setFirstName("GroupAgent2").setLastName("GA2").createAgent());
 children.add(agentSetter.setFirstName("Agent1").setLastName("A1").createAgent());
 groupAgent.setChildren(children);
 assertEquals(agentSetter.setFirstName("GroupAgent1").setLastName("GA1").createAgent(), groupAgent.getGroup("GA1", "GA1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupThrowsException() {
 children.add(agentSetter.setFirstName("Agent1").setLastName("A1").createAgent());
 groupAgent.setChildren(children);
 groupAgent.getGroup("GA1", "GA2");
 }

 @Test
 public void testFindDependingComponentByName() {
 children.add(agentSetter.setFirstName("ComponentAgent1").setLastName("CA1").createAgent());
 children.add(agentSetter.setFirstName("GroupAgent1").setLastName("GA1").createAgent());
 children.add(agentSetter.setFirstName("ComponentAgent2").setLastName("CA2").createAgent());
 groupAgent.setChildren(children);
 assertEquals(agentSetter.setFirstName("ComponentAgent1").setLastName("CA1").createAgent(), groupAgent.findDependingComponentByName("CA1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameThrowsException() {
 children.add(agentSetter.setFirstName("Agent1").setLastName("A1").createAgent());
 groupAgent.setChildren(children);
 groupAgent.findDependingComponentByName("CA1");
 }

 @Test
 public void testFindDependingGroupByNames() {
 children.add(agentSetter.setFirstName("GroupAgent1").setLastName("GA1").createAgent());
 children.add(agentSetter.setFirstName("GroupAgent2").setLastName("GA2").createAgent());
 children.add(agentSetter.setFirstName("GroupAgent3").setLastName("GA3").createAgent());
 groupAgent.setChildren(children);
 assertEquals(agentSetter.setFirstName("GroupAgent1").setLastName("GA1").createAgent(), groupAgent.findDependingGroupByNames("GA1", "GA1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesThrowsException() {
 children.add(agentSetter.setFirstName("Agent1").setLastName("A1").createAgent());
 groupAgent.setChildren(children);
 groupAgent.findDependingGroupByNames("GA1", "GA2");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 children.add(agentSetter.setFirstName("ParallelGroupAgent1").setLastName("PG1").createAgent());
 children.add(agentSetter.setFirstName("ParallelGroupAgent2").setLastName("PG2").createAgent());
 children.add(agentSetter.setFirstName("ParallelGroupAgent3").setLastName("PG3").createAgent());
 children.add(agentSetter.setFirstName("GroupAgent1").setLastName("GA1").createAgent());
 groupAgent.setChildren(children);
 assertEquals(agentSetter.setFirstName("ParallelGroupAgent1").setLastName("PG1").createAgent(), groupAgent.findDependingParallelGroupByNames("PG1", "PG1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesThrowsException() {
 children.add(agentSetter.setFirstName("Agent1").setLastName("A1").createAgent());
 groupAgent.setChildren(children);
 groupAgent.findDependingParallelGroupByNames("PG1", "PG2");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 children.add(agentSetter.setFirstName("SequentialGroupAgent1").setLastName("SG1").createAgent());
 children.add(agentSetter.setFirstName("SequentialGroupAgent2").setLastName("SG2").createAgent());
 children.add(agentSetter.setFirstName("SequentialGroupAgent3").setLastName("SG3").createAgent());
 children.add(agentSetter.setFirstName("GroupAgent1").setLastName("GA1").createAgent());
 groupAgent.setChildren(children);
 assertEquals(agentSetter.setFirstName("SequentialGroupAgent1").setLastName("SG1").createAgent(), groupAgent.findDependingSequentialGroupByNames("SG1", "SG1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesThrowsException() {
 children.add(agentSetter.setFirstName("Agent1").setLastName("A1").createAgent());
 groupAgent.setChildren(children);
 groupAgent.findDependingSequentialGroupByNames("SG1", "SG2");
 }
}