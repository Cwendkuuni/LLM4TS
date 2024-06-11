package glengineer.agents.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.test.mocks.MockAgent;
import glengineer.agents.test.mocks.MockComponentAgent;
import glengineer.agents.test.mocks.MockGroupAgent;
import glengineer.agents.test.mocks.MockParallelGroupAgent;
import glengineer.agents.test.mocks.MockSequentialGroupAgent;
import glengineer.agents.test.mocks.MockTemporaryGapAgent;
import glengineer.exceptions.IllegalArgumentException;
import glengineer.layoutEngineer.GroupContent;
import glengineer.layoutEngineer.GroupCreatorAndElementAdder;
import glengineer.layoutEngineer.GroupLayout.Group;
import glengineer.layoutEngineer.functionsOnGroup.FunctionsOnGroupAndElement;
import glengineer.layoutEngineer.functionsOnGroup.FunctionsOnGroupAndElementImplementation;

public class GroupAgentTest {

 private GroupAgent agent;
 private List<Agent> children;
 private GroupContent groupContent;
 private GroupCreatorAndElementAdder creatorAndAdder;
 private FunctionsOnGroupAndElement functionsOnGroup;
 private Settings settings;

 @Before
 public void setUp() {
 agent = new MockGroupAgent();
 children = new LinkedList<>();
 groupContent = mock(GroupContent.class);
 creatorAndAdder = mock(GroupCreatorAndElementAdder.class);
 functionsOnGroup = new FunctionsOnGroupAndElementImplementation(agent, null);
 settings = new Settings();
 }

 @Test
 public void testGetFirstName() throws Exception {
 final String expected = "John";
 children.add(new MockComponentAgent("John Doe"));
 assertEquals(expected, agent.getFirstName());
 }

 @Test
 public void testGetLastName() throws Exception {
 final String expected = "Doe";
 children.add(new MockComponentAgent("John Doe"));
 assertEquals(expected, agent.getLastName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetFirstNameWhenNoChildren() throws Exception {
 assertEquals(null, agent.getFirstName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastNameWhenNoChildren() throws Exception {
 assertEquals(null, agent.getLastName());
 }

 @Test
 public void testIsComponent() throws Exception {
 assertFalse(agent.isComponent("Component"));
 }

 @Test
 public void testIsGroup() throws Exception {
 assertTrue(agent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGroupWhenFirstNameIsNull() throws Exception {
 assertFalse(agent.isGroup(null, "Doe"));
 }

 @Test
 public void testIsGroupWhenLastNameIsNull() throws Exception {
 assertFalse(agent.isGroup("John", null));
 }

 @Test
 public void testIsGroupWhenNotGroup() throws Exception {
 assertFalse(agent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGap() throws Exception {
 assertFalse(agent.isGap());
 }

 @Test
 public void testEquals() throws Exception {
 final MockGroupAgent other = new MockGroupAgent();
 final Agent otherChild = new MockComponentAgent("John Doe");
 other.children.add(otherChild);
 assertTrue(agent.equals(other));
 other.children.remove(otherChild);
 assertFalse(agent.equals(other));
 other.children.add(otherChild);
 other.settings = settings;
 assertTrue(agent.equals(other));
 }

 @Test
 public void testAddAgent() throws Exception {
 final Agent child = new MockComponentAgent("John Doe");
 agent.addAgent(child);
 assertTrue(agent.children.contains(child));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() throws Exception {
 assertNull(agent.getFirstName());
 try {
 agent.addAgent(null);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetChildren() throws Exception {
 assertEquals(Collections.emptyList(), agent.getChildren());
 children.add(new MockComponentAgent("John Doe"));
 children.add(new MockComponentAgent("Jane Doe"));
 assertEquals(children, agent.getChildren());
 }

 @Test
 public void testGetNumberOfChildren() throws Exception {
 assertEquals(0, agent.getNumberOfChildren());
 children.add(new MockComponentAgent("John Doe"));
 children.add(new MockComponentAgent("Jane Doe"));
 assertEquals(2, agent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() throws Exception {
 final String componentName = "Component";
 children.add(new MockComponentAgent("John Doe"));
 children.add(new MockComponentAgent("Jane Doe"));
 children.add(new MockComponentAgent("John Doe") {
 @Override
 public boolean isComponent(String name) {
 return name.equals(componentName);
 }
 });
 assertEquals(children.get(children.size() - 1), agent.getComponent(componentName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWhenComponentNotFound() throws Exception {
 assertNull(agent.getComponent("Component"));
 }

 @Test
 public void testGetGroup() throws Exception {
 final String firstName = "John";
 final String lastName = "Doe";
 children.add(new MockComponentAgent("John Doe"));
 children.add(new MockComponentAgent("Jane Doe"));
 children.add(new MockGroupAgent(firstName, lastName));
 assertEquals(children.get(children.size() - 1), agent.getGroup(firstName, lastName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWhenGroupNotFound() throws Exception {
 assertNull(agent.getGroup("John", "Doe"));
 }

 @Test
 public void testFindDependingComponentByName() throws Exception {
 final String componentName = "Component";
 final Agent component = new MockComponentAgent("Component");
 children.add(new MockComponentAgent("John Doe"));
 children.add(component);
 children.add(new MockComponentAgent("Jane Doe"));
 assertEquals(component, agent.findDependingComponentByName(componentName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWhenComponentNotFound() throws Exception {
 assertNull(agent.findDependingComponentByName("Component"));
 }

 @Test
 public void testFindDependingGroupByNames() throws Exception {
 final String firstName = "John";
 final String lastName = "Doe";
 children.add(new MockComponentAgent("John Doe"));
 children.add(new MockComponentAgent("Jane Doe"));
 children.add(new MockGroupAgent(firstName, lastName));
 assertEquals(children.get(children.size() - 1), agent.findDependingGroupByNames(firstName, lastName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWhenGroupNotFound() throws Exception {
 assertNull(agent.findDependingGroupByNames("John", "Doe"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() throws Exception {
 final String firstName = "John";
 final String lastName = "Doe";
 children.add(new MockParallelGroupAgent(firstName, lastName));
 children.add(new MockComponentAgent("John Doe"));
 children.add(new MockComponentAgent("Jane Doe"));
 assertEquals(children.get(0), agent.findDependingParallelGroupByNames(firstName, lastName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWhenGroupNotFound() throws Exception {
 assertNull(agent.findDependingParallelGroupByNames("John", "Doe"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() throws Exception {
 final String firstName = "John";
 final String lastName = "Doe";
 children.add(new MockSequentialGroupAgent(firstName, lastName));
 children.add(new MockComponentAgent("John Doe"));
 children.add(new MockComponentAgent("Jane Doe"));
 assertEquals(children.get(0), agent.findDependingSequentialGroupByNames(firstName, lastName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWhenGroupNotFound() throws Exception {
 assertNull(agent.findDependingSequentialGroupByNames("John", "Doe"));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() throws Exception {
 final List<Agent> initialChildren = new ArrayList<>(children);
 agent.removeGapsFromParallelGroups();
 final List<Agent> finalChildren = new ArrayList<>(children);
 assertEquals(initialChildren, finalChildren);
 }

 @Test
 public void testRemoveGapsFromParallelGroupsWhenHasNoParallelGroup() throws Exception {
 final List<Agent> initialChildren = new ArrayList<>(children);
 agent = new MockGroupAgent();
 agent.removeGapsFromParallelGroups();
 final List<Agent> finalChildren = new ArrayList<>(agent.children);
 assertEquals(initialChildren, finalChildren);
 }

 @Test
 public void testRemoveAllGaps() throws Exception {
 final ParallelGroupAgent parallelGroup = new MockParallelGroupAgent();
 parallelGroup.children.add(new MockComponentAgent("Component 1"));
 parallelGroup.children.add(new MockGapAgent());
 parallelGroup.children.add(new MockComponentAgent("Component 2"));
 parallelGroup.children.add(new MockGapAgent());
 parallelGroup.children.add(new MockComponentAgent("Component 3"));
 parallelGroup.removeAllGaps();
 assertEquals(3, parallelGroup.children.size());
 }

 @Test
 public void testReplaceParallelSubgroupsWithGapsByTheirSingleGaps() throws Exception {
 final ParallelGroupAgent parallelGroup = new MockParallelGroupAgent();
 parallelGroup.children.add(new MockComponentAgent("Component 1"));
 parallelGroup.children.add(new MockParallelGroupAgent());
 parallelGroup.children.add(new MockComponentAgent("Component 2"));
 parallelGroup.children.add(new MockGapAgent());
 parallelGroup.children.add(new MockComponentAgent("Component 3"));
 parallelGroup.children.get(1).children.add(new MockGapAgent());
 parallelGroup.children.get(1).children.add(new MockComponentAgent("Component 4"));
 parallelGroup.replaceParallelSubgroupsWithGapsByTheirSingleGaps();
 assertEquals(3, parallelGroup.children.size());
 }

 @Test
 public void testReplaceContinuousSequencesOfGapsBySingleGaps() throws Exception {
 final ParallelGroupAgent parallelGroup = new MockParallelGroupAgent();
 parallelGroup.children.add(new MockComponentAgent("Component 1"));
 parallelGroup.children.add(new MockGapAgent());
 parallelGroup.children.add(new MockComponentAgent("Component 2"));
 parallelGroup.children.add(new MockGapAgent());
 parallelGroup.children.add(new MockComponentAgent("Component 3"));
 parallelGroup.children.add(new MockGapAgent());
 parallelGroup.children.add(new MockComponentAgent("Component 4"));
 parallelGroup.children.add(new MockGapAgent());
 parallelGroup.children.add(new MockComponentAgent("Component 5"));
 parallelGroup.replaceContinuousSequencesOfGapsBySingleGaps();
 assertEquals(8, parallelGroup.children.size());
 }

 @Test
 public void testReplaceGroupsWithSingleElementsByThatElements() throws Exception {
 final SequentialGroupAgent sequentialGroup = new MockSequentialGroupAgent();
 sequentialGroup.children.add(new MockGroupAgent("Group 1", "Group"));
 sequentialGroup.children.add(new MockComponentAgent("Component 1"));
 sequentialGroup.children.add(new MockComponentAgent("Component 2"));
 sequentialGroup.children.add(new MockComponentAgent("Component 3"));
 sequentialGroup.replaceGroupsWithSingleElementsByThatElements();
 assertEquals(3, sequentialGroup.children.size());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() throws Exception {
 final GroupAgent agent = new MockGroupAgent();
 final GroupCreatorAndElementAdder creatorAndAdder = mock(GroupCreatorAndElementAdder.class);
 final FunctionsOnGroupAndElement functionsOnGroup = mock(FunctionsOnGroupAndElement.class);
 final GroupLayoutEngineer.TemporaryGapsToGaps map = mock(GroupLayoutEngineer.TemporaryGapsToGaps.class);
 when(map.gap(any(TemporaryGapAgent.class))).thenReturn(new MockGapAgent());
 when(functionsOnGroup.addPrecedingGap(anyInt())).thenCallRealMethod();
 when(functionsOnGroup.addFollowingGap(anyInt())).thenCallRealMethod();
 agent.addContentToGroup(any(Group.class), creatorAndAdder);
 agent.addContentToGroup(any(Group.class), creatorAndAdder);
 agent.addContentToGroup(any(Group.class), creatorAndAdder);
 agent.replaceTemporaryGapsByRealGaps(map);
 verify(functionsOnGroup, times(3)).addPrecedingGap(anyInt());
 verify(functionsOnGroup, times(3)).addFollowingGap(anyInt());
 }
}