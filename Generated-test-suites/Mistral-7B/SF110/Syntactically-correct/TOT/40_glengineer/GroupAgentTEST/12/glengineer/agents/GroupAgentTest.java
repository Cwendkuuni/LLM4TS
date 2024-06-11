import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.GroupAgentTestUtil;
import glengineer.agents.GroupAgentTestUtil.AgentFactory;
import glengineer.agents.GapAgent;
import glengineer.agents.GroupCreatorAndElementAdder;
import glengineer.agents.functions.FunctionsOnGroupAndElement;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private AgentSetter agentSetter;
 private GroupCreatorAndElementAdder groupCreatorAndElementAdder;

 @Before
 public void setUp() {
 groupAgent = new GroupAgent() {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 return GroupAgentTestUtil.createGroupContent();
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 agentSetter = new AgentSetter();
 groupCreatorAndElementAdder = new GroupCreatorAndElementAdder();
 }

 @Test
 public void testGetFirstName_EmptyList() {
 assertNull(groupAgent.getFirstName());
 }

 @Test
 public void testGetFirstName_FirstAgentHasName() {
 final Agent child1 = agentSetter.createAgentWithFirstName("John").create();
 groupAgent.addAgent(child1);
 final String expected = "John";
 final String actual = groupAgent.getFirstName();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetFirstName_NoAgentHasName() {
 final Agent child1 = agentSetter.createAgent().create();
 groupAgent.addAgent(child1);
 assertNull(groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName_EmptyList() {
 assertNull(groupAgent.getLastName());
 }

 @Test
 public void testGetLastName_LastAgentHasName() {
 final Agent child1 = agentSetter.createAgent().create();
 final Agent child2 = agentSetter.createAgentWithLastName("Doe").create();
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 final String expected = "Doe";
 final String actual = groupAgent.getLastName();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLastName_NoAgentHasName() {
 final Agent child1 = agentSetter.createAgent().create();
 final Agent child2 = agentSetter.createAgent().create();
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertNull(groupAgent.getLastName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsComponent_InvalidComponentName() {
 groupAgent.isComponent("InvalidName");
 }

 @Test
 public void testIsGroup_MatchingFirstNameAndLastName() {
 final Agent child1 = agentSetter.createAgentWithFirstName("John").create();
 final Agent child2 = agentSetter.createAgentWithFirstName("John").createWithLastName("Doe");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGroup_MismatchingFirstName() {
 final Agent child1 = agentSetter.createAgentWithFirstName("John").create();
 final Agent child2 = agentSetter.createAgentWithFirstName("Jane").create();
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertFalse(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGroup_MismatchingLastName() {
 final Agent child1 = agentSetter.createAgentWithFirstName("John").create();
 final Agent child2 = agentSetter.createAgentWithFirstName("John").createWithLastName("Doe");
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertFalse(groupAgent.isGroup("John", "Smith"));
 }

 @Test
 public void testIsGroup_EmptyList() {
 assertFalse(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGap_NotAGap() {
 assertFalse(groupAgent.isGap());
 }

 @Test
 public void testAddAgent_NullAgent() {
 assertThrows(IllegalArgumentException.class, () -> groupAgent.addAgent(null));
 }

 @Test
 public void testGetChildren_EmptyList() {
 assertEquals(0, groupAgent.getNumberOfChildren());
 assertFalse(groupAgent.getChildren().isEmpty());
 assertTrue(groupAgent.getChildren() instanceof java.util.List);
 }

 @Test
 public void testGetChildren_OneAgent() {
 final Agent child1 = agentSetter.createAgent().create();
 groupAgent.addAgent(child1);
 assertEquals(1, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.getChildren().contains(child1));
 }

 @Test
 public void testGetChildren_MultipleAgents() {
 final Agent child1 = agentSetter.createAgent().create();
 final Agent child2 = agentSetter.createAgent().create();
 groupAgent.addAgent(child1);
 groupAgent.addAgent(child2);
 assertEquals(2, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.getChildren().containsAll(Arrays.asList(child1, child2)));
 }

 @Test
 public void testGetComponent_ComponentPresent() {
 final ComponentAgent componentAgent = agentSetter.createComponentAgent().create();
 groupAgent.addAgent(componentAgent);
 assertEquals(componentAgent, groupAgent.getComponent("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponent_ComponentNotPresent() {
 groupAgent.getComponent("NonExistentComponentName");
 }

 @Test
 public void testGetGroup_GroupPresent() {
 final Agent groupAgent1 = agentSetter.createGroupAgent().create();
 groupAgent.addAgent(groupAgent1);
 assertEquals(groupAgent1, groupAgent.getGroup("FirstName", "LastName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroup_GroupNotPresent() {
 groupAgent.getGroup("NonExistentFirstName", "NonExistentLastName");
 }

 @Test
 public void testFindDependingComponentByName() {
 final ComponentAgent componentAgent1 = agentSetter.createComponentAgent().create();
 groupAgent.addAgent(componentAgent1);
 final ComponentAgent componentAgent2 = agentSetter.createComponentAgent().create();
 groupAgent1.addAgent(componentAgent2);
 assertEquals(componentAgent2, groupAgent.findDependingComponentByName("ComponentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByName_ComponentNotFound() {
 groupAgent.findDependingComponentByName("NonExistentComponentName");
 }

 @Test
 public void testFindDependingGroupByNames() {
 final GroupAgent groupAgent1 = agentSetter.createGroupAgent().create();
 groupAgent.addAgent(groupAgent1);
 final Agent groupAgent2 = agentSetter.createGroupAgent().createWithFirstName("John").createWithLastName("Doe");
 groupAgent1.addAgent(groupAgent2);
 assertEquals(groupAgent2, groupAgent.findDependingGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNames_GroupNotFound() {
 groupAgent.findDependingGroupByNames("NonExistentFirstName", "NonExistentLastName");
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 final ParallelGroupAgent parallelGroupAgent1 = agentSetter.createParallelGroupAgent().create();
 groupAgent.addAgent(parallelGroupAgent1);
 final ParallelGroupAgent parallelGroupAgent2 = agentSetter.createParallelGroupAgent().createWithFirstName("John").createWithLastName("Doe");
 parallelGroupAgent1.addAgent(parallelGroupAgent2);
 assertEquals(parallelGroupAgent2, groupAgent.findDependingParallelGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNames_ParallelGroupNotFound() {
 groupAgent.findDependingParallelGroupByNames("NonExistentFirstName", "NonExistentLastName");
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 final SequentialGroupAgent sequentialGroupAgent1 = agentSetter.createSequentialGroupAgent().create();
 groupAgent.addAgent(sequentialGroupAgent1);
 final SequentialGroupAgent sequentialGroupAgent2 = agentSetter.createSequentialGroupAgent().createWithFirstName("John").createWithLastName("Doe");
 sequentialGroupAgent1.addAgent(sequentialGroupAgent2);
 assertEquals(sequentialGroupAgent2, groupAgent.findDependingSequentialGroupByNames("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNames_SequentialGroupNotFound() {
 groupAgent.findDependingSequentialGroupByNames("NonExistentFirstName", "NonExistentLastName");
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 final GapAgent gap1 = new GapAgent(10);
 final GapAgent gap2 = new GapAgent(5);
 final ParallelGroupAgent parallelGroupAgent1 = new ParallelGroupAgent();
 parallelGroupAgent1.addAgent(gap1);
 parallelGroupAgent1.addAgent(agentSetter.createAgent().create());
 parallelGroupAgent1.addAgent(gap2);
 parallelGroupAgent1.addAgent(agentSetter.createAgent().create());
 groupAgent.addAgent(parallelGroupAgent1);
 groupAgent.removeGapsFromParallelGroups();
 assertEquals(2, groupAgent.getNumberOfChildren());
 assertTrue(groupAgent.getChildren().containsAll(Arrays.asList(gap1, parallelGroupAgent1.getChildren().get(0), gap2, parallelGroupAgent1.getChildren().get(1))));
 }
}