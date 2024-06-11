package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import glengineer.agents.*;
import java.util.*;

public class GroupAgentTest {

 GroupAgent groupAgent = new SequentialGroupAgent();
 GroupAgent childGroup1 = new SequentialGroupAgent();
 GroupAgent childGroup2 = new SequentialGroupAgent();
 Agent childAgent1 = new ComponentAgent();
 Agent childAgent2 = new ComponentAgent();
 Agent gapAgent1 = new GapAgent(1);
 Agent gapAgent2 = new GapAgent(2);

 @Test
 public void testGetFirstName() {
 assertNull(groupAgent.getFirstName());
 groupAgent.addAgent(childGroup1);
 assertNull(childGroup1.getFirstName());
 childGroup1.addAgent(childAgent1);
 assertEquals(childAgent1.getFirstName(), childGroup1.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertNull(groupAgent.getLastName());
 groupAgent.addAgent(childGroup1);
 assertNull(childGroup1.getLastName());
 childGroup1.addAgent(childAgent1);
 childGroup1.addAgent(childAgent2);
 assertEquals(childAgent2.getLastName(), childGroup1.getLastName());
 }

 @Test
 public void testIsGroup() {
 assertFalse(groupAgent.isGroup("", ""));
 groupAgent.addAgent(childGroup1);
 assertFalse(childGroup1.isGroup("", ""));
 childGroup1.addAgent(childAgent1);
 childGroup1.addAgent(childAgent2);
 assertTrue(childGroup1.isGroup("childAgent1", "ComponentAgent"));
 assertTrue(childGroup1.isGroup("childAgent2", "ComponentAgent"));
 assertFalse(childGroup1.isGroup("childAgent1", "ComponentAgent2"));
 assertFalse(childGroup1.isGroup("nonExistentAgent", ""));
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent1 = new SequentialGroupAgent();
 GroupAgent groupAgent2 = new SequentialGroupAgent();
 GroupAgent groupAgent3 = new SequentialGroupAgent();
 groupAgent1.addAgent(childGroup1);
 groupAgent2.addAgent(childGroup1);
 groupAgent3.addAgent(childGroup2);
 assertFalse(groupAgent1.equals(groupAgent2));
 assertFalse(groupAgent1.equals(groupAgent3));
 }

 @Test
 public void testAddAgent() {
 assertNull(groupAgent.getFirstName());
 groupAgent.addAgent(childGroup1);
 assertFalse(groupAgent.isEmpty());
 groupAgent.addAgent(childAgent1);
 groupAgent.addAgent(childAgent2);
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetChildren() {
 assertNotNull(groupAgent.getChildren());
 assertFalse(groupAgent.getChildren().isEmpty());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(0, groupAgent.getNumberOfChildren());
 groupAgent.addAgent(childGroup1);
 assertEquals(1, groupAgent.getNumberOfChildren());
 groupAgent.addAgent(childAgent1);
 groupAgent.addAgent(childAgent2);
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(groupAgent.getComponent("nonExistentComponent"));
 groupAgent.addAgent(childAgent1);
 assertEquals(childAgent1, groupAgent.getComponent("ComponentAgent"));
 }

 @Test
 public void testGetGroup() {
 assertNull(groupAgent.getGroup("nonExistentFirstName", "nonExistentLastName"));
 groupAgent.addAgent(childGroup1);
 childGroup1.addAgent(childAgent1);
 childGroup1.addAgent(childAgent2);
 assertEquals(childGroup1, groupAgent.getGroup("childAgent1", "ComponentAgent"));
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("nonExistentComponent"));
 groupAgent.addAgent(childAgent1);
 assertEquals(childAgent1, groupAgent.findDependingComponentByName("ComponentAgent"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertNull(groupAgent.findDependingGroupByNames("nonExistentFirstName", "nonExistentLastName"));
 groupAgent.addAgent(childGroup1);
 childGroup1.addAgent(childAgent1);
 childGroup1.addAgent(childAgent2);
 assertEquals(childGroup1, groupAgent.findDependingGroupByNames("childAgent1", "ComponentAgent"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertNull(groupAgent.findDependingParallelGroupByNames("nonExistentFirstName", "nonExistentLastName"));
 groupAgent.addAgent(childGroup1);
 childGroup1.addAgent(new ParallelGroupAgent());
 childGroup1.addAgent(childAgent1);
 childGroup1.addAgent(childAgent2);
 assertEquals(childGroup1, groupAgent.findDependingParallelGroupByNames("childAgent1", "ComponentAgent"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertNull(groupAgent.findDependingSequentialGroupByNames("nonExistentFirstName", "nonExistentLastName"));
 groupAgent.addAgent(childGroup1);
 childGroup1.addAgent(new SequentialGroupAgent());
 childGroup1.addAgent(childAgent1);
 childGroup1.addAgent(childAgent2);
 assertEquals(childGroup1, groupAgent.findDependingSequentialGroupByNames("childAgent1", "ComponentAgent"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.addAgent(childGroup1);
 childGroup1.addAgent(new ParallelGroupAgent());
 childGroup1.addAgent(gapAgent1);
 childGroup1.addAgent(childAgent1);
 childGroup1.addAgent(childAgent2);
 groupAgent.removeGapsFromParallelGroups();
 assertFalse(groupAgent.getChildren().contains(gapAgent1));
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
 map.put(childAgent1, gapAgent1);
 groupAgent.addAgent(childGroup1);
 childGroup1.addAgent(childAgent1);
 childGroup1.addAgent(childAgent2);
 groupAgent.addAgent(new TemporaryGapAgent());
 groupAgent.addAgent(new TemporaryGapAgent());
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 assertEquals(gapAgent1, groupAgent.getChildren().get(2));
 assertEquals(gapAgent1, groupAgent.getChildren().get(4));
 }

 @Test
 public void testAddContentToGroup() {
 GroupLayoutEngineer engineer = new GroupLayoutEngineer();
 GroupLayout layout = engineer.parseLayout("new SequentialGroup() { new Label(10, 10, \"Label1\"); new Label(20, 10, \"Label2\"); new Label(30, 10, \"Label3\"); }");
 GroupAgent rootGroup = engineer.getRootGroup(layout);
 GroupAgent parentGroup = new SequentialGroupAgent();
 parentGroup.addAgent(gapAgent1);
 parentGroup.addAgent(childAgent1);
 parentGroup.addAgent(childAgent2);
 groupAgent.addContentToGroup(parentGroup, engineer.getGroupCreatorAndElementAdder(layout));
 assertEquals(parentGroup, groupAgent.getChildren().get(0));
 }

 @Test
 public void testAddGapBefore() {
 groupAgent.addAgent(childGroup1);
 groupAgent.addAgent(childAgent1);
 groupAgent.addAgent(childAgent2);
 groupAgent.addGapBefore(gapAgent1, childAgent1);
 assertEquals(gapAgent1, groupAgent.getChildren().get(1));
 assertEquals(childAgent1, groupAgent.getChildren().get(2));
 assertEquals(childAgent2, groupAgent.getChildren().get(3));
 }

 @Test
 public void testAddGapAfter() {
 groupAgent.addAgent(childGroup1);
 groupAgent.addAgent(childAgent1);
 groupAgent.addAgent(childAgent2);
 groupAgent.addGapAfter(gapAgent1, childAgent1);
 assertEquals(childAgent1, groupAgent.getChildren().get(0));
 assertEquals(gapAgent1, groupAgent.getChildren().get(1));
 assertEquals(childAgent2, groupAgent.getChildren().get(2));
 }

}