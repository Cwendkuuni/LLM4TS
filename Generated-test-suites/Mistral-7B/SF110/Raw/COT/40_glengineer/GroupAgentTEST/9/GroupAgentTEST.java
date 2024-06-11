package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import glengineer.*;
import glengineer.agents.*;
import glengineer.agents.settings.*;
import glengineer.agents.setters.*;

@RunWith(JUnit4.class)
public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent child1, child2, child3, child4, gapAgent;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 child1 = new Agent() {
 @Override
 public String getFirstName() {
 return "Child1";
 }

 @Override
 public String getLastName() {
 return "Child1";
 }
 };
 child2 = new Agent() {
 @Override
 public String getFirstName() {
 return "Child2";
 }

 @Override
 public String getLastName() {
 return "Child2";
 }
 };
 child3 = new Agent() {
 @Override
 public String getFirstName() {
 return "Child3";
 }

 @Override
 public String getLastName() {
 return "Child3";
 }
 };
 child4 = new Agent() {
 @Override
 public String getFirstName() {
 return "Child4";
 }

 @Override
 public String getLastName() {
 return "Child4";
 }
 };
 gapAgent = new GapAgent(10);
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addAgent(child3);
 groupAgent.addAgent(gapAgent);
 groupAgent.addAgent(child4);
 }

 @After
 public void tearDown() {
 groupAgent = null;
 child1 = null;
 child2 = null;
 child3 = null;
 child4 = null;
 gapAgent = null;
 }

 @Test
 public void testGetFirstName() {
 assertEquals("Child1", groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("Child4", groupAgent.getLastName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetFirstNameWhenEmpty() {
 groupAgent = new GroupAgent();
 assertNull(groupAgent.getFirstName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastNameWhenEmpty() {
 groupAgent = new GroupAgent();
 assertNull(groupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup("Child1", "Child1"));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent otherGroupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 otherGroupAgent.addAgent(child1);
 otherGroupAgent.addAgent(child2);
 otherGroupAgent.addAgent(child3);
 otherGroupAgent.addAgent(child4);
 assertTrue(groupAgent.equals(otherGroupAgent));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addAgent(child3);
 groupAgent.addAgent(child4);
 assertEquals(4, groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(4, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(4, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(groupAgent.getComponent("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWhenComponentNotFound() {
 groupAgent.getComponent("ComponentName");
 }

 @Test
 public void testGetGroup() {
 GroupAgent group = (GroupAgent)groupAgent.getGroup("Child1", "Child1");
 assertTrue(group.equals(groupAgent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWhenGroupNotFound() {
 groupAgent.getGroup("Child1", "Child2");
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWhenComponentNotFound() {
 groupAgent.findDependingComponentByName("ComponentName");
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent group = (GroupAgent)groupAgent.findDependingGroupByNames("Child1", "Child1");
 assertTrue(group.equals(groupAgent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWhenGroupNotFound() {
 groupAgent.findDependingGroupByNames("Child1", "Child2");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 GroupAgent parallelGroup = (GroupAgent)groupAgent.findDependingParallelGroupByNames("Child1", "Child1");
 assertTrue(parallelGroup.equals(groupAgent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWhenParallelGroupNotFound() {
 groupAgent.findDependingParallelGroupByNames("Child1", "Child2");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 GroupAgent sequentialGroup = (GroupAgent)groupAgent.findDependingSequentialGroupByNames("Child1", "Child1");
 assertTrue(sequentialGroup.equals(groupAgent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWhenSequentialGroupNotFound() {
 groupAgent.findDependingSequentialGroupByNames("Child1", "Child2");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testPassTheCallToSubgroups() {
 GroupAgent subgroup = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 groupAgent.addAgent(subgroup);
 ((GroupAgent)subgroup).removeGapsFromParallelGroups();
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testRemoveAllGapsFromParallelSubgroups() {
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceParallelSubgroupsWithGapsByTheirSingleGaps() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(new GapAgent(5));
 groupAgent.addAgent(child2);
 groupAgent.addAgent(new GapAgent(2));
 groupAgent.addAgent(child3);
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceContinuousSequencesOfGapsBySingleGaps() {
 groupAgent.addAgent(new GapAgent(1));
 groupAgent.addAgent(new GapAgent(1));
 groupAgent.addAgent(new GapAgent(1));
 groupAgent.addAgent(new GapAgent(1));
 groupAgent.addAgent(child1);
 groupAgent.replaceContinuousSequencesOfGapsBySingleGaps();
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceGroupsWithSingleElementsByThatElements() {
 GroupAgent groupAgentWithSingleGroup = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 };
 groupAgentWithSingleGroup.addAgent(new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return null;
 }
 }.getChildren().get(0));
 groupAgentWithSingleGroup.addAgent(new GapAgent(10));
 groupAgentWithSingleGroup.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "SingleElement";
 }

 @Override
 public String getLastName() {
 return "SingleElement";
 }
 });
 groupAgent.replaceGroupsWithSingleElementsByThatElements();
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 groupAgent.replaceTemporaryGapsByRealGaps(new GroupLayoutEngineer.TemporaryGapsToGapsMap());
 assertEquals(5, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testAddGapBefore() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addGapBefore(gapAgent, child1);
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testAddGapAfter() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addGapAfter(gapAgent, child1);
 assertEquals(3, groupAgent.getNumberOfChildren());
 }

}