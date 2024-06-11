package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.apache.commons.cli.Settings;
import org.apache.commons.cli.agents.*;
import org.apache.commons.cli.agents.setters.*;
import org.apache.commons.cli.functions.*;
import org.junit.Before;
import org.junit.Test;

public class GroupAgentTest {

 GroupAgent groupAgent = mock(GroupAgent.class);
 Agent childAgent1 = mock(Agent.class);
 Agent childAgent2 = mock(Agent.class);
 Agent childAgent3 = mock(Agent.class);
 GroupAgent subGroupAgent1 = mock(GroupAgent.class);
 GroupAgent subGroupAgent2 = mock(GroupAgent.class);
 ParallelGroupAgent parallelGroupAgent1 = mock(ParallelGroupAgent.class);
 SequentialGroupAgent sequentialGroupAgent1 = mock(SequentialGroupAgent.class);
 TemporaryGapAgent temporaryGapAgent1 = mock(TemporaryGapAgent.class);
 TemporaryGapAgent temporaryGapAgent2 = mock(TemporaryGapAgent.class);

 @Before
 public void setUp() {
 when(childAgent1.getFirstName()).thenReturn("John");
 when(childAgent1.getLastName()).thenReturn("Doe");
 when(childAgent2.getFirstName()).thenReturn("Jane");
 when(childAgent2.getLastName()).thenReturn("Doe");
 when(childAgent3.getFirstName()).thenReturn("Bob");
 when(childAgent3.getLastName()).thenReturn("Smith");
 when(subGroupAgent1.getNumberOfChildren()).thenReturn(2);
 when(subGroupAgent1.getFirstName()).thenReturn("SubGroup1");
 when(subGroupAgent1.getLastName()).thenReturn("SubGroup");
 when(subGroupAgent2.getNumberOfChildren()).thenReturn(1);
 when(subGroupAgent2.getFirstName()).thenReturn("SubGroup2");
 when(subGroupAgent2.getLastName()).thenReturn("SubGroup");
 when(parallelGroupAgent1.getGapsType()).thenReturn(temporaryGapAgent1);
 when(sequentialGroupAgent1.getGapsType()).thenReturn(temporaryGapAgent2);
 when(groupAgent.getFirstName()).thenReturn("GroupAgent");
 when(groupAgent.getLastName()).thenReturn("Group");
 when(groupAgent.isGroup("GroupAgent", "Group")).thenReturn(true);
 when(groupAgent.isGroup(anyString(), anyString())).thenAnswer(i -> groupAgent.equals(GroupAgent.getAgentByName(i.getArguments()[0], i.getArguments()[1])));
 when(groupAgent.getChildren()).thenReturn(Arrays.asList(childAgent1, childAgent2, childAgent3, subGroupAgent1, subGroupAgent2, parallelGroupAgent1, sequentialGroupAgent1));
 when(groupAgent.isComponent("ComponentName")).thenReturn(false);
 }

 @Test
 public void testGetFirstName() {
 assertEquals("John", groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("Group", groupAgent.getLastName());
 }

 @Test
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup("GroupAgent", "Group"));
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent2 = mock(GroupAgent.class);
 when(groupAgent2.getFirstName()).thenReturn("GroupAgent");
 when(groupAgent2.getLastName()).thenReturn("Group");
 assertTrue(groupAgent.equals(groupAgent2));
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(childAgent1);
 verify(childrenAgents).add(childAgent1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(7, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(7, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 ComponentAgent result = groupAgent.getComponent("ComponentName");
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentNullName() {
 groupAgent.getComponent(null);
 }

 @Test
 public void testGetGroup() {
 GroupAgent result = groupAgent.getGroup("SubGroup1", "SubGroup");
 assertEquals(subGroupAgent1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNullFirstName() {
 groupAgent.getGroup(null, "SubGroup");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNullLastName() {
 groupAgent.getGroup("SubGroup1", null);
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameNullName() {
 groupAgent.findDependingComponentByName(null);
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent result = groupAgent.findDependingGroupByNames("SubGroup1", "SubGroup");
 assertEquals(subGroupAgent1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullFirstName() {
 groupAgent.findDependingGroupByNames(null, "SubGroup");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullLastName() {
 groupAgent.findDependingGroupByNames("SubGroup1", null);
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 ParallelGroupAgent result = groupAgent.findDependingParallelGroupByNames("ParallelGroup1", "ParallelGroup");
 assertEquals(parallelGroupAgent1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesNullFirstName() {
 groupAgent.findDependingParallelGroupByNames(null, "ParallelGroup");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesNullLastName() {
 groupAgent.findDependingParallelGroupByNames("ParallelGroup1", null);
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 SequentialGroupAgent result = groupAgent.findDependingSequentialGroupByNames("SequentialGroup1", "SequentialGroup");
 assertEquals(sequentialGroupAgent1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesNullFirstName() {
 groupAgent.findDependingSequentialGroupByNames(null, "SequentialGroup");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesNullLastName() {
 groupAgent.findDependingSequentialGroupByNames("SequentialGroup1", null);
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.removeGapsFromParallelGroups();
 verify(parallelGroupAgent1).removeAllGaps();
 verify(sequentialGroupAgent1).removeAllGaps();
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupLayoutEngineer.TemporaryGapsToGaps map = mock(GroupLayoutEngineer.TemporaryGapsToGaps.class);
 when(map.gap(temporaryGapAgent1)).thenReturn(new GapAgent(10));
 when(map.gap(temporaryGapAgent2)).thenReturn(new GapAgent(20));
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(6, children.size());
 assertEquals(temporaryGapAgent1, children.get(5));
 assertEquals(temporaryGapAgent2, children.get(6));
 }

 @Test
 public void testAddGapBefore() {
 GapAgent gap = new GapAgent(5);
 groupAgent.addGapBefore(gap, childAgent1);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(8, children.size());
 assertEquals(gap, children.get(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapBeforeNullGroupAgent() {
 groupAgent.addGapBefore(temporaryGapAgent1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapBeforeNullElement() {
 groupAgent.addGapBefore(temporaryGapAgent1, null);
 }

 @Test
 public void testAddGapAfter() {
 GapAgent gap = new GapAgent(5);
 groupAgent.addGapAfter(gap, childAgent1);
 List<Agent> children = groupAgent.getChildren();
 assertEquals(8, children.size());
 assertEquals(gap, children.get(3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapAfterNullGroupAgent() {
 groupAgent.addGapAfter(temporaryGapAgent1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGapAfterNullElement() {
 groupAgent.addGapAfter(temporaryGapAgent1, null);
 }
}