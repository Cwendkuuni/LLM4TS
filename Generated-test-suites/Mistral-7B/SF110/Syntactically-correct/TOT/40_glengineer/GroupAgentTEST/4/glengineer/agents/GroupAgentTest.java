package glengineer.agents.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.GroupAgent.GroupCreatorAndElementAdder;
import glengineer.agents.GroupAgent.GroupLayout.Group;
import glengineer.agents.GroupAgent.settings.Settings;
import glengineer.agents.setters.ComponentAgent;
import glengineer.agents.setters.GapAgent;
import glengineer.agents.setters.GroupAgent;
import glengineer.agents.setters.TemporaryGapAgent;
import glengineer.exceptions.IllegalArgumentException;
import glengineer.layoutengineer.GroupLayout;
import glengineer.layoutengineer.GroupLayout.GroupContent;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private GroupAgent childAgent1;
 private GroupAgent childAgent2;
 private ComponentAgent componentAgent;
 private GapAgent gapAgent1;
 private GapAgent gapAgent2;
 private TemporaryGapAgent tempGapAgent;
 private Settings settings;
 private GroupCreatorAndElementAdder creatorAndAdder;
 private GroupLayoutEngineer.TemporaryGapsToGaps map;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return new Group();
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };

 childAgent1 = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return new Group();
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };

 childAgent2 = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return new Group();
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };

 componentAgent = new ComponentAgent();
 gapAgent1 = new GapAgent(10);
 gapAgent2 = new GapAgent(5);
 tempGapAgent = new TemporaryGapAgent();
 settings = new Settings();
 creatorAndAdder = new GroupCreatorAndElementAdder() {
 @Override
 public void addGroupToGroup(GroupContent groupContent, Settings settings, Group parentGroup) {
 // empty implementation for simplicity
 }
 };
 map = new GroupLayoutEngineer.TemporaryGapsToGaps() {
 @Override
 public GroupAgent gap(TemporaryGapAgent gap) {
 return new GapAgent(gap.getSize());
 }
 };
 groupAgent.addAgent(childAgent1);
 groupAgent.addAgent(childAgent2);
 }

 @Test
 public void test_getFirstName_returnsFirstNameOfFirstChildAgent() {
 assertEquals(childAgent1.getFirstName(), groupAgent.getFirstName());
 }

 @Test
 public void test_getFirstName_returnsNullIfNoChildAgentHasFirstName() {
 assertNull(groupAgent.getFirstName());
 }

 @Test
 public void test_getLastName_returnsLastNameOfLastChildAgent() {
 assertEquals(childAgent2.getLastName(), groupAgent.getLastName());
 }

 @Test
 public void test_getLastName_returnsNullIfNoChildAgentHasLastName() {
 assertNull(groupAgent.getLastName());
 }

 @Test
 public void test_isComponent_returnsFalse() {
 assertFalse(groupAgent.isComponent("componentName"));
 }

 @Test
 public void test_isGroup_returnsTrueIfGroupAgentIsGroup() {
 assertTrue(groupAgent.isGroup(groupAgent.getFirstName(), groupAgent.getLastName()));
 }

 @Test
 public void test_isGroup_returnsFalseIfNotGroupAgent() {
 assertFalse(componentAgent.isGroup(groupAgent.getFirstName(), groupAgent.getLastName()));
 }

 @Test
 public void test_equals_returnsTrueIfTwoGroupAgentsHaveSameFirstNameAndLastName() {
 GroupAgent groupAgent1 = new GroupAgent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 assertTrue(groupAgent1.equals(groupAgent2));
 }

 @Test
 public void test_equals_returnsFalseIfTwoGroupAgentsHaveDifferentFirstNameOrLastName() {
 GroupAgent groupAgent1 = new GroupAgent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 public String getFirstName() {
 return "Jane";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 assertFalse(groupAgent1.equals(groupAgent2));
 }

 @Test
 public void test_addAgent_throwsIllegalArgumentExceptionIfNullAgentIsPassed() {
 assertThrows(IllegalArgumentException.class, () -> groupAgent.addAgent(null));
 }

 @Test
 public void test_getChildren_returnsUnmodifiableListOfChildrenAgents() {
 List<Agent> expectedChildren = Arrays.asList(childAgent1, childAgent2);
 List<Agent> children = Collections.unmodifiableList(groupAgent.getChildren());
 assertEquals(expectedChildren, children);
 }

 @Test
 public void test_getNumberOfChildren_returnsNumberOfChildrenAgents() {
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void test_getComponent_returnsComponentAgentIfAgentIsPresent() {
 ComponentAgent expectedComponentAgent = componentAgent;
 ComponentAgent actualComponentAgent = groupAgent.getComponent("componentName");
 assertEquals(expectedComponentAgent, actualComponentAgent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void test_getComponent_throwsIllegalArgumentExceptionIfComponentAgentIsNotPresent() {
 groupAgent.getComponent("nonExistentComponentName");
 }

 @Test
 public void test_getGroup_returnsGroupAgentIfAgentIsPresent() {
 GroupAgent expectedGroupAgent = childAgent1;
 GroupAgent actualGroupAgent = groupAgent.getGroup(childAgent1.getFirstName(), childAgent1.getLastName());
 assertEquals(expectedGroupAgent, actualGroupAgent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void test_getGroup_throwsIllegalArgumentExceptionIfGroupAgentIsNotPresent() {
 groupAgent.getGroup("nonExistentFirstName", "nonExistentLastName");
 }

 @Test
 public void test_findDependingComponentByName_returnsComponentAgentIfPresent() {
 ComponentAgent expectedComponentAgent = componentAgent;
 ComponentAgent actualComponentAgent = groupAgent.findDependingComponentByName("componentName");
 assertEquals(expectedComponentAgent, actualComponentAgent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void test_findDependingComponentByName_throwsIllegalArgumentExceptionIfComponentAgentIsNotPresent() {
 groupAgent.findDependingComponentByName("nonExistentComponentName");
 }

 @Test
 public void test_findDependingGroupByNames_returnsGroupAgentIfPresent() {
 GroupAgent expectedGroupAgent = childAgent1;
 GroupAgent actualGroupAgent = groupAgent.findDependingGroupByNames(childAgent1.getFirstName(), childAgent1.getLastName());
 assertEquals(expectedGroupAgent, actualGroupAgent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void test_findDependingGroupByNames_throwsIllegalArgumentExceptionIfGroupAgentIsNotPresent() {
 groupAgent.findDependingGroupByNames("nonExistentFirstName", "nonExistentLastName");
 }

 @Test
 public void test_findDependingParallelGroupByNames_returnsParallelGroupAgentIfPresent() {
 ParallelGroupAgent expectedParallelGroupAgent = new ParallelGroupAgent();
 ParallelGroupAgent actualParallelGroupAgent = groupAgent.findDependingParallelGroupByNames("parallelGroupName", "parallelLastName");
 assertEquals(expectedParallelGroupAgent, actualParallelGroupAgent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void test_findDependingParallelGroupByNames_throwsIllegalArgumentExceptionIfParallelGroupAgentIsNotPresent() {
 groupAgent.findDependingParallelGroupByNames("nonExistentParallelGroupName", "nonExistentParallelLastName");
 }

 @Test
 public void test_findDependingSequentialGroupByNames_returnsSequentialGroupAgentIfPresent() {
 SequentialGroupAgent expectedSequentialGroupAgent = new SequentialGroupAgent();
 SequentialGroupAgent actualSequentialGroupAgent = groupAgent.findDependingSequentialGroupByNames("sequentialGroupName", "sequentialLastName");
 assertEquals(expectedSequentialGroupAgent, actualSequentialGroupAgent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void test_findDependingSequentialGroupByNames_throwsIllegalArgumentExceptionIfSequentialGroupAgentIsNotPresent() {
 groupAgent.findDependingSequentialGroupByNames("nonExistentSequentialGroupName", "nonExistentSequentialLastName");
 }

 @Test
 public void test_removeGapsFromParallelGroups_removesAllGapsFromParallelSubgroups() {
 groupAgent.removeGapsFromParallelGroups();
 List<Agent> expectedChildren = Arrays.asList(childAgent1, childAgent2);
 List<Agent> children = new ArrayList<>(groupAgent.getChildren());
 for (Agent agent : children) {
 if (agent instanceof ParallelGroupAgent) {
 List<Agent> subagents = ((ParallelGroupAgent)agent).getChildren();
 assertTrue(subagents.isEmpty());
 }
 }
 }

 @Test
 public void test_replaceTemporaryGapsByRealGaps_replacesTemporaryGapsWithRealGaps() {
 List<Agent> expectedChildren = Arrays.asList(gapAgent1, tempGapAgent, gapAgent2);
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 List<Agent> children = new ArrayList<>(groupAgent.getChildren());
 assertEquals(expectedChildren, children);
 }

 @Test
 public void test_addContentToGroup_addsGroupToParentGroup() {
 GroupContent groupContent = new GroupContent();
 creatorAndAdder.addGroupToGroup(groupContent, settings, null);
 groupAgent.addContentToGroup(null, creatorAndAdder);
 }

 @Test
 public void test_addPrecedingGap_addsGapBeforeChildAgent() {
 groupAgent.addAgent(componentAgent);
 groupAgent.addGapBefore(gapAgent1, componentAgent);
 List<Agent> expectedChildren = Arrays.asList(gapAgent1, componentAgent, childAgent1, childAgent2);
 List<Agent> children = new ArrayList<>(groupAgent.getChildren());
 assertEquals(expectedChildren, children);
 }

 @Test
 public void test_addFollowingGap_addsGapAfterChildAgent() {
 groupAgent.addAgent(componentAgent);
 groupAgent.addAgent(childAgent1);
 groupAgent.addGapAfter(gapAgent1, childAgent1);
 List<Agent> expectedChildren = Arrays.asList(componentAgent, childAgent1, gapAgent1, childAgent2);
 List<Agent> children = new ArrayList<>(groupAgent.getChildren());
 assertEquals(expectedChildren, children);
 }
}