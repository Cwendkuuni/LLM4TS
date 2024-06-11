package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import glengineer.*;
import glengineer.agents.*;
import glengineer.agents.settings.*;
import glengineer.agents.setters.*;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent child1, child2, child3, child4, child5;
 private Agent gap1, gap2, gap3;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
 // Implement the group content here
 return null;
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
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
 child4 = new Agent() {
 @Override
 public String getFirstName() {
 return null;
 }

 @Override
 public String getLastName() {
 return null;
 }
 };
 child5 = new Agent() {
 @Override
 public String getFirstName() {
 return "Alice";
 }

 @Override
 public String getLastName() {
 return "Williams";
 }
 };
 gap1 = new GapAgent(10);
 gap2 = new GapAgent(5, 10, 15);
 gap3 = new GapAgent(20);

 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addAgent(child3);
 groupAgent.addAgent(child4);
 groupAgent.addAgent(child5);
 }

 @Test
 public void testGetFirstName() {
 assertEquals("John", groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("Doe", groupAgent.getLastName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFirstNameWhenEmpty() {
 groupAgent.childrenAgents.clear();
 assertNull(groupAgent.getFirstName());
 }

 @Test
 public void testGetLastNameWhenEmpty() {
 groupAgent.childrenAgents.clear();
 assertNull(groupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testIsGroup() {
 assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testEquals() {
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
 assertFalse(groupAgent.equals(groupAgent1));
 groupAgent1 = groupAgent;
 assertTrue(groupAgent.equals(groupAgent1));
 }

 @Test
 public void testAddAgent() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = groupAgent.getChildren();
 assertEquals(5, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 assertTrue(children.contains(child3));
 assertFalse(children.contains(child4));
 assertFalse(children.contains(child5));
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(5, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(groupAgent.getComponent("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentNameNotFound() {
 groupAgent.getComponent("ComponentNameNotFound");
 }

 @Test
 public void testGetGroup() {
 GroupAgent group = (GroupAgent)groupAgent.getGroup("John", "Doe");
 assertEquals("John", group.getFirstName());
 assertEquals("Doe", group.getLastName());
 assertEquals(5, group.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupNameNotFound() {
 groupAgent.getGroup("John1", "Doe1");
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameNotFound() {
 groupAgent.findDependingComponentByName("ComponentNameNotFound");
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent group = (GroupAgent)groupAgent.findDependingGroupByNames("John", "Doe");
 assertEquals("John", group.getFirstName());
 assertEquals("Doe", group.getLastName());
 assertEquals(5, group.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesNotFound() {
 groupAgent.findDependingGroupByNames("John1", "Doe1");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 GroupAgent group = (GroupAgent)groupAgent.findDependingParallelGroupByNames("John", "Doe");
 assertEquals("John", group.getFirstName());
 assertEquals("Doe", group.getLastName());
 assertEquals(5, group.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesNotFound() {
 groupAgent.findDependingParallelGroupByNames("John1", "Doe1");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 GroupAgent group = (GroupAgent)groupAgent.findDependingSequentialGroupByNames("John", "Doe");
 assertEquals("John", group.getFirstName());
 assertEquals("Doe", group.getLastName());
 assertEquals(5, group.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesNotFound() {
 groupAgent.findDependingSequentialGroupByNames("John1", "Doe1");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 groupAgent.removeGapsFromParallelGroups();
 assertTrue(groupAgent.childrenAgents.isEmpty());
 }

 @Test
 public void testPassTheCallToSubgroups() {
 List<Agent> children = new ArrayList<>();
 GroupAgent subgroup = new GroupAgent() {
 @Override
 public void removeGapsFromParallelGroups() {
 children.add(groupAgent);
 }
 };
 groupAgent.addAgent(subgroup);
 groupAgent.removeGapsFromParallelGroups();
 assertTrue(children.size() == 1);
 }

 @Test
 public void testRemoveAllGapsFromParallelSubgroupsWithOtherContent() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addAgent(new ParallelGroupAgent() {
 @Override
 public void removeAllGaps() {
 // Implement the removal logic here
 }
 });
 groupAgent.removeGapsFromParallelSubgroupsWithOtherContent();
 assertEquals(2, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.getChildren().get(1) instanceof ParallelGroupAgent);
 }

 @Test
 public void testReplaceParallelSubgroupsWithGapsByTheirSingleGaps() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(new ParallelGroupAgent() {
 @Override
 public Agent getGapsType() {
 return gap1;
 }
 });
 groupAgent.addAgent(child2);
 groupAgent.replaceParallelSubgroupsWithGapsByTheirSingleGaps();
 assertEquals(2, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.childrenAgents.get(0) instanceof GapAgent);
 assertTrue(groupAgent.childrenAgents.get(1) instanceof Agent);
 }

 @Test
 public void testReplaceContinuousSequencesOfGapsBySingleGaps() {
 groupAgent.addAgent(gap1);
 groupAgent.addAgent(gap2);
 groupAgent.addAgent(gap3);
 groupAgent.replaceContinuousSequencesOfGapsBySingleGaps();
 assertEquals(3, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.childrenAgents.get(0) instanceof GapAgent);
 assertTrue(groupAgent.childrenAgents.get(1) instanceof GapAgent);
 assertTrue(groupAgent.childrenAgents.get(2) instanceof Agent);
 }

 @Test
 public void testReplaceGroupsWithSingleElementsByThatElements() {
 groupAgent.addAgent(new GroupAgent() {
 @Override
 public int getNumberOfChildren() {
 return 3;
 }
 });
 groupAgent.replaceGroupsWithSingleElementsByThatElements();
 assertEquals(3, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.getChildren().get(0) instanceof Agent);
 assertTrue(groupAgent.getChildren().get(1) instanceof Agent);
 assertTrue(groupAgent.getChildren().get(2) instanceof Agent);
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps() {
 @Override
 public Agent gap(TemporaryGapAgent temporaryGapAgent) {
 groupAgent.addAgent(temporaryGapAgent);
 return groupAgent.getChildren().get(groupAgent.getNumberOfChildren() - 1);
 }
 };
 groupAgent.addAgent(new TemporaryGapAgent(10));
 groupAgent.replaceTemporaryGapsByRealGaps(map);
 assertEquals(2, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.childrenAgents.get(1) instanceof GapAgent);
 }

 @Test
 public void testAddContentToGroup() {
 GroupLayoutEngineer engine = new GroupLayoutEngineer();
 GroupLayout parentLayout = engine.parse("new GroupLayout() {\n" +
 "public void addGroupToGroup(GroupLayout.Group group, GroupCreatorAndElementAdder creatorAndAdder) {\n" +
 "creatorAndAdder.add(group);\n" +
 "}\n" +
 "};");
 GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder() {
 @Override
 public void addGroupToGroup(GroupLayout.Group parentGroup, GroupLayout.Group group) {
 // Implement the group content addition logic here
 }
 };
 GroupAgent groupAgent2 = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
 return new GroupLayout.Group() {
 @Override
 public void add(GroupLayout.ComponentComponent component) {
 // Implement the component addition logic here
 }
 };
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, groupAgent2);
 }
 };
 groupAgent.addAgent(groupAgent2);
 groupAgent.addContentToGroup(parentLayout, creatorAndAdder);
 }

 @Test
 public void testAddGapBefore() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(gap1);
 groupAgent.addAgent(child2);
 List<Agent> children = new ArrayList<>(groupAgent.getChildren());
 assertEquals(3, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(gap1, children.get(1));
 assertEquals(child2, children.get(2));
 }

 @Test
 public void testAddGapAfter() {
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 groupAgent.addGapAfter(gap1, child1);
 List<Agent> children = new ArrayList<>(groupAgent.getChildren());
 assertEquals(3, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(gap1, children.get(1));
 assertEquals(child2, children.get(2));
 }
}