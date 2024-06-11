import org.junit.Assert;
import org.junit.Test;
import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GapAgent;
import glengineer.agents.Agent;
import java.util.*;

public class GroupAgentTest {

 @Test
 public void testGetFirstName() {
 GroupAgent groupAgent = new MyAgent();
 Agent child1 = new ChildAgent("John", "Doe");
 Agent child2 = new ChildAgent("Jane", "Smith");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);

 Assert.assertEquals("John", groupAgent.getFirstName());
 }

 @Test
 public void testGetFirstName_EmptyList() {
 GroupAgent groupAgent = new MyAgent();

 Assert.assertNull(groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 GroupAgent groupAgent = new MyAgent();
 Agent child1 = new ChildAgent("John", "Doe");
 Agent child2 = new ChildAgent("Jane", "Smith");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);

 Assert.assertEquals("Doe", groupAgent.getLastName());
 }

 @Test
 public void testGetLastName_EmptyList() {
 GroupAgent groupAgent = new MyAgent();

 Assert.assertNull(groupAgent.getLastName());
 }

 @Test
 public void testIsGroup() {
 GroupAgent groupAgent = new MyAgent();
 Agent child1 = new ChildAgent("John", "Doe");
 groupAgent.addAgent(child1);

 Assert.assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGroup_EmptyList() {
 GroupAgent groupAgent = new MyAgent();

 Assert.assertFalse(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGroup_DifferentNames() {
 GroupAgent groupAgent = new MyAgent();
 Agent child1 = new ChildAgent("John", "Doe");
 Agent child2 = new ChildAgent("Jane", "Smith");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);

 Assert.assertFalse(groupAgent.isGroup("Jane", "Smith"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAgent_NullAgent() {
 GroupAgent groupAgent = new MyAgent();

 try {
 groupAgent.addAgent(null);
 } catch (IllegalArgumentException e) {
 // expected
 Assert.assertEquals("Cannot add null agents.", e.getMessage());
 }
 }

 @Test
 public void testGetChildren() {
 GroupAgent groupAgent = new MyAgent();
 Agent child1 = new ChildAgent("John", "Doe");
 Agent child2 = new ChildAgent("Jane", "Smith");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);

 List<Agent> children = groupAgent.getChildren();

 Assert.assertEquals(2, children.size());
 Assert.assertTrue(children.contains(child1));
 Assert.assertTrue(children.contains(child2));
 }

 @Test
 public void testGetNumberOfChildren() {
 GroupAgent groupAgent = new MyAgent();
 Agent child1 = new ChildAgent("John", "Doe");
 Agent child2 = new ChildAgent("Jane", "Smith");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);

 Assert.assertEquals(2, groupAgent.getNumberOfChildren());
 }

 @Test
 public void testFindDependingGroupByNames_EmptyList() {
 GroupAgent groupAgent = new MyAgent();

 try {
 groupAgent.findDependingGroupByNames("John", "Doe");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindDependingGroupByNames() {
 GroupAgent parent = new MyAgent();
 GroupAgent child = new ChildAgentGroup("John", "Doe");
 parent.addAgent(child);

 GroupAgent result = parent.findDependingGroupByNames("John", "Doe");

 Assert.assertEquals(child, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNames_DifferentNames() {
 GroupAgent parent = new MyAgent();
 GroupAgent child1 = new ChildAgentGroup("John", "Doe");
 GroupAgent child2 = new ChildAgentGroup("Jane", "Smith");
 parent.addAgent(child1);
 parent.addAgent(child2);

 try {
 parent.findDependingGroupByNames("Jane", "Smith");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindDependingSequentialGroupByNames_EmptyList() {
 GroupAgent groupAgent = new MyAgent();

 try {
 groupAgent.findDependingSequentialGroupByNames("John", "Doe");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 GroupAgent parent = new MyAgent();
 SequentialGroupAgent child = new SequentialGroupAgent("John", "Doe");
 parent.addAgent(child);

 SequentialGroupAgent result = parent.findDependingSequentialGroupByNames("John", "Doe");

 Assert.assertEquals(child, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNames_DifferentNames() {
 GroupAgent parent = new MyAgent();
 SequentialGroupAgent child1 = new SequentialGroupAgent("John", "Doe");
 SequentialGroupAgent child2 = new SequentialGroupAgent("Jane", "Smith");
 parent.addAgent(child1);
 parent.addAgent(child2);

 try {
 parent.findDependingSequentialGroupByNames("Jane", "Smith");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindDependingParallelGroupByNames_EmptyList() {
 GroupAgent groupAgent = new MyAgent();

 try {
 groupAgent.findDependingParallelGroupByNames("John", "Doe");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 GroupAgent parent = new MyAgent();
 ParallelGroupAgent child = new ParallelGroupAgent("John", "Doe");
 parent.addAgent(child);

 ParallelGroupAgent result = parent.findDependingParallelGroupByNames("John", "Doe");

 Assert.assertEquals(child, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNames_DifferentNames() {
 GroupAgent parent = new MyAgent();
 ParallelGroupAgent child1 = new ParallelGroupAgent("John", "Doe");
 ParallelGroupAgent child2 = new ParallelGroupAgent("Jane", "Smith");
 parent.addAgent(child1);
 parent.addAgent(child2);

 try {
 parent.findDependingParallelGroupByNames("Jane", "Smith");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent parent = new MyAgent();
 ParallelGroupAgent gap1 = new ParallelGroupAgent("Gap1", 1, null, null);
 ParallelGroupAgent group1 = new ParallelGroupAgent("Group1", 3, null, null);
 ParallelGroupAgent group2 = new ParallelGroupAgent("Group2", 2, null, null);
 ParallelGroupAgent gap2 = new ParallelGroupAgent("Gap2", 1, null, null);
 ParallelGroupAgent group3 = new ParallelGroupAgent("Group3", 1, null, null);
 parent.addAgent(gap1);
 parent.addAgent(group1);
 parent.addAgent(group2);
 parent.addAgent(gap2);
 parent.addAgent(group3);

 parent.removeGapsFromParallelGroups();

 List<Agent> result = parent.getChildren();

 Assert.assertTrue(result.contains(group1));
 Assert.assertTrue(result.contains(group2));
 Assert.assertTrue(result.contains(group3));
 Assert.assertFalse(result.contains(gap1));
 Assert.assertFalse(result.contains(gap2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReplaceTemporaryGapsByRealGaps_EmptyMap() {
 GroupAgent groupAgent = new MyAgent();
 List<Agent> children = new ArrayList<>();
 children.add(new TemporaryGapAgent("TempGap1"));
 children.add(new MyAgent());
 groupAgent.setChildren(children);

 try {
 groupAgent.replaceTemporaryGapsByRealGaps(Collections.emptyMap());
 } catch (IllegalArgumentException e) {
 // expected
 Assert.assertEquals("The map containing temporary gaps must not be empty.", e.getMessage());
 }
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 GroupAgent parent = new MyAgent();
 List<Agent> children = new ArrayList<>();
 children.add(new TemporaryGapAgent("TempGap1"));
 children.add(new MyAgent());
 parent.setChildren(children);

 Map<TemporaryGapAgent, Agent> map = new HashMap<>();
 map.put(children.get(0), new GapAgent(1));

 parent.replaceTemporaryGapsByRealGaps(map);

 List<Agent> result = parent.getChildren();

 Assert.assertTrue(result.contains(new GapAgent(1)));
 Assert.assertFalse(result.contains(children.get(0)));
 }

 @Test
 public void testAddContentToGroup() {
 GroupAgent groupAgent = new MyAgent();
 GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder();
 GroupLayout.Group expectedResult = new GroupLayout.Group(new JLabel("Group"));
 groupAgent.addContentToGroup(null, creatorAndAdder);

 Assert.assertEquals(expectedResult, groupAgent.groupContent(creatorAndAdder));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddContentToGroup_NullParent() {
 GroupAgent groupAgent = new MyAgent();
 GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder();

 try {
 groupAgent.addContentToGroup(null, creatorAndAdder);
 } catch (IllegalArgumentException e) {
 // expected
 Assert.assertEquals("The parent group cannot be null.", e.getMessage());
 }
 }

 @Test
 public void testAddPrecedingGap() {
 GroupAgent groupAgent = new MyAgent();
 Agent element = new LabelAgent("Element");
 GapAgent gap = new GapAgent(1);

 FunctionsOnGroupAndElement functionsOnGroupAndElement = new GroupAgent.FunctionsOnGroupAndElementImplementation(groupAgent, element);
 functionsOnGroupAndElement.addPrecedingGap(1);

 List<Agent> result = groupAgent.getChildren();

 Assert.assertEquals(1, result.size());
 Assert.assertEquals(element.getClass(), result.get(0).getClass());
 Assert.assertEquals(gap.getClass(), result.get(1).getClass());
 }

 @Test
 public void testAddFollowingGap() {
 GroupAgent groupAgent = new MyAgent();
 Agent element = new LabelAgent("Element");
 GapAgent gap = new GapAgent(1);

 FunctionsOnGroupAndElement functionsOnGroupAndElement = new GroupAgent.FunctionsOnGroupAndElementImplementation(groupAgent, element);
 functionsOnGroupAndElement.addFollowingGap(1);

 List<Agent> result = groupAgent.getChildren();

 Assert.assertEquals(2, result.size());
 Assert.assertEquals(element.getClass(), result.get(0).getClass());
 Assert.assertEquals(gap.getClass(), result.get(1).getClass());
 }
}

class MyAgent extends GroupAgent {
 @Override
 public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
 return new GroupLayout.Group(new JLabel("MyGroup"));
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
}

class ChildAgent extends Agent {
 public ChildAgent(final String firstName, final String lastName) {
 super();
 this.firstName = firstName;
 this.lastName = lastName;
 }

 @Override
 public String getFirstName() {
 return firstName;
 }

 @Override
 public String getLastName() {
 return lastName;
 }

 private String firstName;
 private String lastName;
}

class ChildAgentGroup extends ParallelGroupAgent {
 public ChildAgentGroup(final String firstName, final String lastName) {
 super();
 this.firstName = firstName;
 this.lastName = lastName;
 }

 @Override
 public String getFirstName() {
 return firstName;
 }

 @Override
 public String getLastName() {
 return lastName;
 }

 private String firstName;
 private String lastName;
}

class LabelAgent extends Agent {
 public LabelAgent(final String label) {
 super();
 this.label = label;
 }

 @Override
 public String toString() {
 return label;
 }

 private String label;
}

class TemporaryGapAgent extends GapAgent {
 public TemporaryGapAgent(final String label) {
 super();
 this.label = label;
 }

 @Override
 public String toString() {
 return label;
 }

 private String label;
}

class GroupCreatorAndElementAdder {
 public void addGroupToGroup(final GroupLayout.Group group, final Settings settings, final GroupLayout.Group parentGroup) {
 // implementation
 }
}