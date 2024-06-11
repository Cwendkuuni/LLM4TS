package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.Agent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.AgentSetterFactory;
import glengineer.agents.GroupAgentFactory;
import glengineer.agents.GroupLayoutEngineer;
import glengineer.agents.setters.TemporaryGapsToGaps;
import glengineer.agents.setters.TemporaryGapAgent;
import glengineer.agents.setters.GapAgent;

public class GroupAgentTest {

 private GroupAgent groupAgent;
 private Agent agent1, agent2, agent3, agent4, agent5, agent6, agent7, agent8;
 private AgentSetterFactory agentSetterFactory;
 private GroupAgentFactory groupAgentFactory;

 @Before
 public void setup() {
 this.agentSetterFactory = new AgentSetterFactory();
 this.groupAgentFactory = new GroupAgentFactory();
 this.agent1 = this.agentSetterFactory.createAgentWithFirstName("John").createAgentWithLastName("Doe");
 this.agent2 = this.agentSetterFactory.createAgentWithFirstName("Jane").createAgentWithLastName("Doe");
 this.agent3 = this.agentSetterFactory.createAgentWithFirstName("Bob").createAgentWithLastName("Smith");
 this.agent4 = this.agentSetterFactory.createAgentWithFirstName("Alice").createAgentWithLastName("Williams");
 this.agent5 = this.agentSetterFactory.createAgentWithFirstName("Mary").createAgentWithLastName("Miller");
 this.agent6 = this.agentSetterFactory.createAgentWithFirstName("Tom").createAgentWithLastName("Jones");
 this.agent7 = this.agentSetterFactory.createAgentWithFirstName("David").createAgentWithLastName("Brown");
 this.agent8 = this.agentSetterFactory.createAgentWithFirstName("Sara").createAgentWithLastName("Johnson");
 this.groupAgent = this.groupAgentFactory.createGroupAgent();
 this.groupAgent.addAgent(this.agent1);
 this.groupAgent.addAgent(this.agent2);
 }

 @Test
 public void testGetFirstName() {
 assertEquals("John", this.agent1.getFirstName());
 assertEquals(null, this.groupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertEquals("Doe", this.agent1.getLastName());
 assertEquals("Doe", this.groupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(this.groupAgent.isComponent("ComponentName"));
 }

 @Test
 public void testIsGroup() {
 assertFalse(this.groupAgent.isGroup("John", "Doe"));
 assertTrue(this.agent1.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGap() {
 assertFalse(this.groupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent anotherGroupAgent = new GroupAgent();
 anotherGroupAgent.addAgent(this.agent1);
 anotherGroupAgent.addAgent(this.agent2);
 assertEquals(this.groupAgent, anotherGroupAgent);
 }

 @Test
 public void testAddAgent() {
 this.groupAgent.addAgent(this.agent3);
 assertEquals(3, this.groupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAgent() {
 this.groupAgent.addAgent(null);
 }

 @Test
 public void testGetChildren() {
 List<Agent> children = this.groupAgent.getChildren();
 assertEquals(2, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(2, this.groupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(this.groupAgent.getComponent("ComponentName"));
 this.groupAgent.addAgent(new ComponentAgent());
 Agent component = this.groupAgent.getComponent("ComponentName");
 assertNotNull(component);
 }

 @Test
 public void testGetGroup() {
 assertNull(this.groupAgent.getGroup("John", "Doe"));
 this.groupAgent.addAgent(this.agent1);
 Agent group = this.groupAgent.getGroup("John", "Doe");
 assertNotNull(group);
 assertEquals(this.agent1, group);
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(this.groupAgent.findDependingComponentByName("ComponentName"));
 this.groupAgent.addAgent(new ComponentAgent());
 this.groupAgent.addAgent(new AgentSetterAndElementAdderAgent());
 Agent component = this.groupAgent.findDependingComponentByName("ComponentName");
 assertNotNull(component);
 assertEquals(new ComponentAgent(), component);
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertNull(this.groupAgent.findDependingGroupByNames("John", "Doe"));
 this.groupAgent.addAgent(this.agent1);
 Agent group = this.agent1.createGroupAgent();
 group.addAgent(this.agent3);
 this.groupAgent.addAgent(group);
 Agent foundGroup = this.groupAgent.findDependingGroupByNames("John", "Doe");
 assertNotNull(foundGroup);
 assertEquals(this.agent1.getFirstName(), foundGroup.getFirstName());
 assertEquals(this.agent1.getLastName(), foundGroup.getLastName());
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertNull(this.groupAgent.findDependingParallelGroupByNames("John", "Doe"));
 this.groupAgent.addAgent(this.agent1);
 this.agent1.addAgent(this.agent3);
 ParallelGroupAgent parallelGroupAgent = new ParallelGroupAgent();
 parallelGroupAgent.addAgent(this.agent4);
 this.agent1.addAgent(parallelGroupAgent);
 Agent foundParallelGroup = this.groupAgent.findDependingParallelGroupByNames("John", "Doe");
 assertNotNull(foundParallelGroup);
 assertEquals(parallelGroupAgent, foundParallelGroup);
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertNull(this.groupAgent.findDependingSequentialGroupByNames("John", "Doe"));
 this.groupAgent.addAgent(this.agent1);
 this.agent1.addAgent(new SequentialGroupAgent());
 Agent foundSequentialGroup = this.groupAgent.findDependingSequentialGroupByNames("John", "Doe");
 assertNotNull(foundSequentialGroup);
 assertEquals(new SequentialGroupAgent(), foundSequentialGroup);
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 GroupAgent parallelGroupAgent = new ParallelGroupAgent();
 parallelGroupAgent.addAgent(new TemporaryGapAgent());
 parallelGroupAgent.addAgent(this.agent3);
 parallelGroupAgent.addAgent(new TemporaryGapAgent());
 parallelGroupAgent.addAgent(this.agent4);
 this.groupAgent.addAgent(parallelGroupAgent);
 this.groupAgent.removeGapsFromParallelGroups();
 List<Agent> children = this.groupAgent.getChildren();
 List<Agent> expectedChildren = Arrays.asList(this.agent1, this.agent2, this.agent3, this.agent4);
 assertEquals(expectedChildren, children);
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 TemporaryGapsToGaps map = new TemporaryGapsToGaps();
 map.put(new TemporaryGapAgent(5), new GapAgent(5));
 this.groupAgent.replaceTemporaryGapsByRealGaps(map);
 List<Agent> children = this.groupAgent.getChildren();
 List<Agent> expectedChildren = Arrays.asList(this.agent1, new GapAgent(5), this.agent2, this.agent3, this.agent4);
 assertEquals(expectedChildren, children);
 }

 @Test
 public void testAddContentToGroup() {
 GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder();
 this.groupAgent.addContentToGroup(creatorAndAdder.getRootGroup(), creatorAndAdder);
 }

 @Test
 public void testGroupContent() {
 GroupAgent agentWithChildren = new GroupAgent();
 agentWithChildren.addAgent(this.agent1);
 agentWithChildren.addAgent(this.agent2);
 assertEquals(agentWithChildren.groupContent(null), agentWithChildren);
 }

 @Test
 public void testGetFunctionsOnGroupImplemenation() {
 FunctionsOnGroupAndElementImplementation functionsOnGroupAndElementImplementation = new FunctionsOnGroupAndElementImplementation(this.groupAgent, this.agent1);
 functionsOnGroupAndElementImplementation.addPrecedingGap(10);
 functionsOnGroupAndElementImplementation.addPrecedingGap(5, 5, 10);
 functionsOnGroupAndElementImplementation.addFollowingGap(15);
 functionsOnGroupAndElementImplementation.addFollowingGap(5, 5, 10);
 List<Agent> children = this.groupAgent.getChildren();
 Agent child = this.agent1;
 int index = this.groupAgent.getChildren().indexOf(child);
 assertEquals(child, children.get(index + 1));
 Agent precedingGap = children.get(index);
 assertEquals(new GapAgent(10), precedingGap);
 Agent gap1 = children.get(index + 1);
 assertEquals(new GapAgent(5, 5, 10), gap1);
 Agent gap2 = children.get(index + 2);
 assertEquals(new GapAgent(5, 5, 10), gap2);
 Agent followingGap = children.get(index + 3);
 assertEquals(new GapAgent(15), followingGap);
 }
}