package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.ComponentAgent;
import glengineer.agents.GroupAgent;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.GroupAgent.GapAgent;
import glengineer.agents.GroupAgent.Settings;
import glengineer.agents.setters.TemporaryGapAgent;
import glengineer.agents.settings.GroupLayout;
import glengineer.agents.settings.GroupLayout.Group;
import glengineer.agents.settings.GroupCreatorAndElementAdder;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Settings settings;
 private List<Agent> expectedChildren;
 private Agent componentAgent, groupAgent1, groupAgent2, temporaryGapAgent1, temporaryGapAgent2;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 Group group = new Group();
 return group;
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 settings = new Settings();
 expectedChildren = new ArrayList<>();
 componentAgent = new ComponentAgent();
 groupAgent1 = new GroupAgent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }

 @Override
 public List<Agent> getChildren() {
 return Collections.unmodifiableList(new LinkedList<>());
 }
 };
 groupAgent2 = new GroupAgent() {
 @Override
 public String getFirstName() {
 return "Jane";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }

 @Override
 public List<Agent> getChildren() {
 return Collections.unmodifiableList(new LinkedList<>());
 }
 };
 expectedChildren.add(componentAgent);
 expectedChildren.add(groupAgent1);
 expectedChildren.add(groupAgent2);
 }

 @Test
 public void testGetFirstName() {
 assertEquals(null, groupAgent.getFirstName());
 GroupAgent groupAgentWithChildren = new GroupAgent() {
 @Override
 public String getFirstName() {
 return groupAgent1.getFirstName();
 }
 };
 groupAgentWithChildren.addAgent(groupAgent1);
 groupAgentWithChildren.addAgent(groupAgent2);
 assertEquals("John", groupAgentWithChildren.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals(null, groupAgent.getLastName());
 GroupAgent groupAgentWithChildren = new GroupAgent() {
 @Override
 public String getLastName() {
 return groupAgent1.getLastName();
 }
 };
 groupAgentWithChildren.addAgent(groupAgent1);
 groupAgentWithChildren.addAgent(groupAgent2);
 assertEquals("Doe", groupAgentWithChildren.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testIsGroup() {
 assertFalse(groupAgent.isGroup("John", "Doe"));
 GroupAgent groupAgentWithName = new GroupAgent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 groupAgentWithName.addAgent(groupAgent1);
 groupAgentWithName.addAgent(groupAgent2);
 assertTrue(groupAgentWithName.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgentWithName = new GroupAgent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 groupAgentWithName.addAgent(groupAgent1);
 groupAgentWithName.addAgent(groupAgent2);
 assertFalse(groupAgent.equals(groupAgentWithName));
 GroupAgent groupAgentWithSameChildren = new GroupAgent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 };
 groupAgentWithSameChildren.addAgent(componentAgent);
 groupAgentWithSameChildren.addAgent(groupAgent1);
 groupAgentWithSameChildren.addAgent(groupAgent2);
 assertTrue(groupAgent.equals(groupAgentWithSameChildren));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(componentAgent);
 assertEquals(1, groupAgent.getNumberOfChildren());
 assertEquals(componentAgent, groupAgent.getChildren().get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 assertEquals(0, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(0, groupAgent.getNumberOfChildren());
 GroupAgent groupAgentWithChildren = new GroupAgent() {
 @Override
 public List<Agent> getChildren() {
 return expectedChildren;
 }
 };
 groupAgentWithChildren.addAgent(groupAgent1);
 groupAgentWithChildren.addAgent(groupAgent2);
 assertEquals(3, groupAgentWithChildren.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(groupAgent.getComponent("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNullFirstName() {
 groupAgent.getGroup(null, "Doe");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNullLastName() {
 groupAgent.getGroup("John", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNullFirstNameAndLastName() {
 groupAgent.getGroup(null, null);
 }

 @Test
 public void testGetGroup() {
 assertNull(groupAgent.getGroup("John", "Doe"));
 GroupAgent groupAgentWithChildren = new GroupAgent() {
 @Override
 public List<Agent> getChildren() {
 return Collections.unmodifiableList(new LinkedList<>());
 }
 };
 groupAgentWithChildren.addAgent(groupAgent1);
 groupAgentWithChildren.addAgent(groupAgent2);
 assertTrue(groupAgentWithChildren.getGroup("John", "Doe") instanceof GroupAgent);
 assertEquals(groupAgentWithName, groupAgentWithChildren.getGroup("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameNullComponentName() {
 groupAgent.findDependingComponentByName(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullFirstName() {
 groupAgent.findDependingGroupByNames(null, "Doe");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullLastName() {
 groupAgent.findDependingGroupByNames("John", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNullFirstNameAndLastName() {
 groupAgent.findDependingGroupByNames(null, null);
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertNull(groupAgent.findDependingGroupByNames("John", "Doe"));
 GroupAgent groupAgentWithChildren = new GroupAgent() {
 @Override
 public List<Agent> getChildren() {
 return Collections.unmodifiableList(new LinkedList<>());
 }
 };
 groupAgentWithChildren.addAgent(groupAgent1);
 groupAgentWithChildren.addAgent(groupAgent2);
 assertTrue(groupAgentWithChildren.findDependingGroupByNames("John", "Doe") instanceof GroupAgent);
 assertEquals(groupAgentWithName, groupAgentWithChildren.findDependingGroupByNames("John", "Doe"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertNull(groupAgent.findDependingParallelGroupByNames("John", "Doe"));
 GroupAgent groupAgentWithChildren = new GroupAgent() {
 @Override
 public List<Agent> getChildren() {
 return Collections.unmodifiableList(new LinkedList<>());
 }
 };
 groupAgentWithChildren.addAgent(groupAgent1);
 groupAgentWithChildren.addAgent(groupAgent2);
 assertTrue(groupAgentWithChildren instanceof GroupAgent);
 assertFalse(groupAgentWithChildren.findDependingParallelGroupByNames("John", "Doe") instanceof ParallelGroupAgent);
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertNull(groupAgent.findDependingSequentialGroupByNames("John", "Doe"));
 GroupAgent groupAgentWithChildren = new GroupAgent() {
 @Override
 public List<Agent> getChildren() {
 return Collections.unmodifiableList(new LinkedList<>());
 }
 };
 groupAgentWithChildren.addAgent(groupAgent1);
 groupAgentWithChildren.addAgent(groupAgent2);
 assertTrue(groupAgentWithChildren instanceof GroupAgent);
 assertFalse(groupAgentWithChildren.findDependingSequentialGroupByNames("John", "Doe") instanceof SequentialGroupAgent);
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(0, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testPassTheCallToSubgroups() {
 final List<Agent> subgroups = new ArrayList<>();
 subgroups.add(groupAgent1);
 subgroups.add(groupAgent2);
 final ParallelGroupAgent parallelGroupAgent = new ParallelGroupAgent() {
 @Override
 public List<Agent> getChildren() {
 return Collections.unmodifiableList(new LinkedList<>());
 }
 };
 parallelGroupAgent.addAgent(groupAgent1);
 parallelGroupAgent.addAgent(groupAgent2);
 parallelGroupAgent.removeGapsFromParallelSubgroupsWithOtherContent();
 parallelGroupAgent.passTheCallToSubgroups();
 for (final Agent subgroup : subgroups) {
 assertTrue(subgroup instanceof GroupAgent);
 assertTrue(subgroup.removeGapsFromParallelGroups() instanceof Void);
 }
 }

 @Test
 public void testRemoveAllGaps() {
 groupAgent1.addAgent(temporaryGapAgent1 = new TemporaryGapAgent(1));
 groupAgent1.addAgent(componentAgent);
 groupAgent1.addAgent(temporaryGapAgent2 = new TemporaryGapAgent(2));
 groupAgentWithChildren.removeAllGaps();
 assertEquals(2, groupAgentWithChildren.getNumberOfChildren());
 assertFalse(groupAgentWithChildren.getChildren().get(0) instanceof TemporaryGapAgent);
 assertTrue(groupAgentWithChildren.getChildren().get(1) instanceof ComponentAgent);
 assertFalse(groupAgentWithChildren.getChildren().get(1) instanceof GapAgent);
 }

 @Test
 public void testGetGapsType() {
 assertNull(groupAgent1.getGapsType());
 assertNull(groupAgent2.getGapsType());
 }

 @Test
 public void testReplaceParallelSubgroupsWithGapsByTheirSingleGaps() {
 final List<Agent> expectedSubgroups = new ArrayList<>();
 expectedSubgroups.add(new GapAgent(1));
 expectedSubgroups.add(componentAgent);
 expectedSubgroups.add(new GapAgent(2));
 final ParallelGroupAgent parallelGroupAgent = new ParallelGroupAgent() {
 @Override
 public List<Agent> getChildren() {
 return Collections.unmodifiableList(new LinkedList<>());
 }
 };
 parallelGroupAgent.addAgent(groupAgent1);
 parallelGroupAgent.addAgent(groupAgent2);
 parallelGroupAgent.getFunctionsOnGroupImplemenation().addPrecedingGap(1);
 parallelGroupAgent.getFunctionsOnGroupImplemenation().addFollowingGap(2);
 parallelGroupAgent.replaceParallelSubgroupsWithGapsByTheirSingleGaps();
 assertEquals(expectedSubgroups, parallelGroupAgent.getChildren());
 }

 @Test
 public void testReplaceContinuousSequencesOfGapsBySingleGaps() {
 final List<Agent> expectedSubgroups = new ArrayList<>();
 expectedSubgroups.add(new GapAgent(2));
 expectedSubgroups.add(componentAgent);
 expectedSubgroups.add(new GapAgent(1));
 final ParallelGroupAgent parallelGroupAgent = new ParallelGroupAgent() {
 @Override
 public List<Agent> getChildren() {
 return new LinkedList<>(Arrays.asList(new GapAgent(1), componentAgent, new GapAgent(2), new GapAgent(1)));
 }
 };
 parallelGroupAgent.replaceContinuousSequencesOfGapsBySingleGaps();
 assertEquals(expectedSubgroups, parallelGroupAgent.getChildren());
 }

 @Test
 public void testReplaceGroupsWithSingleElementsByThatElements() {
 final List<Agent> expectedSubgroups = new ArrayList<>();
 expectedSubgroups.add(componentAgent);
 expectedSubgroups.add(groupAgent1.getChildren().get(0));
 final ParallelGroupAgent parallelGroupAgent = new ParallelGroupAgent() {
 @Override
 public List<Agent> getChildren() {
 return Collections.unmodifiableList(new LinkedList<>(Arrays.asList(groupAgent1, groupAgent2)));
 }
 };
 parallelGroupAgent.replaceGroupsWithSingleElementsByThatElements();
 assertEquals(expectedSubgroups, parallelGroupAgent.getChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 final GroupAgent.TemporaryGapsToGaps map = new GroupAgent.TemporaryGapsToGaps() {
 @Override
 public Agent gap(TemporaryGapAgent temporaryGapAgent) {
 return new GapAgent(temporaryGapAgent.getSize());
 }
 };
 groupAgentWithChildren.replaceTemporaryGapsByRealGaps(map);
 assertEquals(3, groupAgentWithChildren.getNumberOfChildren());
 assertFalse(groupAgentWithChildren.getChildren().contains(temporaryGapAgent1));
 assertFalse(groupAgentWithChildren.getChildren().contains(temporaryGapAgent2));
 }

 @Test
 public void testAddPrecedingGap() {
 final GroupAgent.FunctionsOnGroupAndElement functionsOnGroupAndElement = groupAgent.getFunctionsOnGroupImplemenation();
 functionsOnGroupAndElement.addPrecedingGap(5);
 assertEquals(1, groupAgent.getNumberOfChildren());
 final Agent precedingGapAgent = groupAgent.getChildren().get(0);
 assertTrue(precedingGapAgent instanceof GapAgent);
 assertEquals(5, precedingGapAgent.getSize());
 }

 @Test
 public void testAddPrecedingGapWithMinMax() {
 final GroupAgent.FunctionsOnGroupAndElement functionsOnGroupAndElement = groupAgent.getFunctionsOnGroupImplemenation();
 functionsOnGroupAndElement.addPrecedingGap(2, 4, 6);
 assertEquals(1, groupAgent.getNumberOfChildren());
 final Agent precedingGapAgent = groupAgent.getChildren().get(0);
 assertTrue(precedingGapAgent instanceof GapAgent);
 assertEquals(2, precedingGapAgent.getMinSize());
 assertEquals(4, precedingGapAgent.getPreferredSize());
 assertEquals(6, precedingGapAgent.getMaxSize());
 }

 @Test
 public void testAddFollowingGap() {
 final GroupAgent.FunctionsOnGroupAndElement functionsOnGroupAndElement = groupAgent.getFunctionsOnGroupImplemenation();
 functionsOnGroupAndElement.addFollowingGap(5);
 assertEquals(2, groupAgent.getNumberOfChildren());
 final Agent followingGapAgent = groupAgent.getChildren().get(1);
 assertTrue(followingGapAgent instanceof GapAgent);
 assertEquals(5, followingGapAgent.getSize());
 }

 @Test
 public void testAddFollowingGapWithMinMax() {
 final GroupAgent.FunctionsOnGroupAndElement functionsOnGroupAndElement = groupAgent.getFunctionsOnGroupImplemenation();
 functionsOnGroupAndElement.addFollowingGap(2, 4, 6);
 assertEquals(2, groupAgent.getNumberOfChildren());
 final Agent followingGapAgent = groupAgent.getChildren().get(1);
 assertTrue(followingGapAgent instanceof GapAgent);
 assertEquals(2, followingGapAgent.getMinSize());
 assertEquals(4, followingGapAgent.getPreferredSize());
 assertEquals(6, followingGapAgent.getMaxSize());
 }
}