package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.test.dto.TestAgent;
import glengineer.agents.test.dto.TestGroupAgent;
import glengineer.agents.test.dto.TestParallelGroupAgent;
import glengineer.agents.test.dto.TestSequentialGroupAgent;
import glengineer.agents.test.dto.TestTemporaryGapAgent;
import glengineer.agents.test.dto.TestGapAgent;
import glengineer.agents.test.util.AgentCreator;
import glengineer.agents.test.util.GroupCreator;
import glengineer.agents.test.util.GroupLayoutEngineer;
import glengineer.agents.test.util.GroupCreatorAndElementAdder;
import glengineer.agents.util.FunctionsOnGroupAndElement;

public class GroupAgentTest
{
    private GroupAgent groupAgent;
    private LinkedList<Agent> childrenAgents;
    private TestAgent agent1;
    private TestAgent agent2;
    private TestAgent agent3;
    private TestAgent groupAgent1;
    private TestAgent groupAgent2;
    private TestAgent parallelGroupAgent1;
    private TestAgent parallelGroupAgent2;
    private TestAgent sequentialGroupAgent1;
    private TestAgent sequentialGroupAgent2;
    private TestTemporaryGapAgent gapAgent;

    @Before
    public void setUp()
    {
        groupAgent = new TestGroupAgent();
        childrenAgents = new LinkedList<Agent>();
        groupAgent.setChildren(childrenAgents);
        agent1 = AgentCreator.createTestAgent("agent1");
        agent2 = AgentCreator.createTestAgent("agent2");
        agent3 = AgentCreator.createTestAgent("agent3");
        groupAgent1 = AgentCreator.createTestGroupAgent("groupAgent1");
        groupAgent2 = AgentCreator.createTestGroupAgent("groupAgent2");
        parallelGroupAgent1 = AgentCreator.createTestParallelGroupAgent("parallelGroupAgent1");
        parallelGroupAgent2 = AgentCreator.createTestParallelGroupAgent("parallelGroupAgent2");
        sequentialGroupAgent1 = AgentCreator.createTestSequentialGroupAgent("sequentialGroupAgent1");
        sequentialGroupAgent2 = AgentCreator.createTestSequentialGroupAgent("sequentialGroupAgent2");
        gapAgent = AgentCreator.createTestTemporaryGapAgent("gapAgent");
    }

    @Test
    public void testGetFirstName()
    {
        childrenAgents.add(agent1);
        childrenAgents.add(agent2);
        String result = groupAgent.getFirstName();
        assertEquals(agent1.getFirstName(), result);
    }

    @Test
    public void testGetLastName()
    {
        childrenAgents.add(agent1);
        childrenAgents.add(agent2);
        String result = groupAgent.getLastName();
        assertEquals(agent2.getLastName(), result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetFirstNameWhenNoAgent()
    {
        String result = groupAgent.getFirstName();
        fail("An IllegalStateException should be thrown when no agent is present in the group.");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetLastNameWhenNoAgent()
    {
        String result = groupAgent.getLastName();
        fail("An IllegalStateException should be thrown when no agent is present in the group.");
    }

    @Test
    public void testIsComponent()
    {
        assertFalse(groupAgent.isComponent("componentName"));
    }

    @Test
    public void testIsGroup()
    {
        assertTrue(groupAgent.isGroup(groupAgent1.getFirstName(), groupAgent1.getLastName()));
    }

    @Test
    public void testIsGap()
    {
        assertFalse(groupAgent.isGap());
    }

    @Test
    public void testEquals()
    {
        TestGroupAgent otherGroupAgent = new TestGroupAgent();
        otherGroupAgent.setChildren(childrenAgents);
        assertTrue(groupAgent.equals(otherGroupAgent));
    }

    @Test
    public void testAddAgent()
    {
        groupAgent.addAgent(agent1);
        assertTrue(childrenAgents.contains(agent1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAgent()
    {
        groupAgent.addAgent(null);
        fail("An IllegalArgumentException should be thrown when trying to add a null agent.");
    }

    @Test
    public void testGetChildren()
    {
        assertEquals(Collections.unmodifiableList(childrenAgents), groupAgent.getChildren());
    }

    @Test
    public void testGetNumberOfChildren()
    {
        childrenAgents.add(agent1);
        childrenAgents.add(agent2);
        childrenAgents.add(agent3);
        assertEquals(3, groupAgent.getNumberOfChildren());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponentWhenComponentNotFound()
    {
        assertNull(groupAgent.getComponent("componentName"));
    }

    @Test
    public void testGetGroupWhenGroupNotFound()
    {
        childrenAgents.add(groupAgent1);
        GroupAgent result = groupAgent.getGroup(groupAgent1.getFirstName(), groupAgent1.getLastName());
        assertEquals(groupAgent1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGroupWhenGroupNotFoundWithEmptyChildren()
    {
        GroupAgent result = groupAgent.getGroup(groupAgent1.getFirstName(), groupAgent1.getLastName());
        fail("An IllegalArgumentException should be thrown when trying to get a group that is not present in the children.");
    }

    @Test
    public void testFindDependingComponentByName()
    {
        childrenAgents.add(agent1);
        childrenAgents.add(agent2);
        childrenAgents.add(agent3);
        childrenAgents.add(agent1);
        ComponentAgent result = groupAgent.findDependingComponentByName("agent1");
        assertEquals(agent1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingComponentByNameWhenComponentNotFound()
    {
        ComponentAgent result = groupAgent.findDependingComponentByName("agent4");
        fail("An IllegalArgumentException should be thrown when trying to find a non-existent component.");
    }

    @Test
    public void testFindDependingGroupByNames()
    {
        childrenAgents.add(groupAgent1);
        childrenAgents.add(groupAgent2);
        childrenAgents.add(groupAgent1);
        GroupAgent result = groupAgent.findDependingGroupByNames(groupAgent1.getFirstName(), groupAgent1.getLastName());
        assertEquals(groupAgent1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingGroupByNamesWhenGroupNotFound()
    {
        GroupAgent result = groupAgent.findDependingGroupByNames("groupAgent3", "groupAgent3");
        fail("An IllegalArgumentException should be thrown when trying to find a non-existent group.");
    }

    @Test
    public void testFindDependingParallelGroupByNames()
    {
        childrenAgents.add(parallelGroupAgent1);
        childrenAgents.add(parallelGroupAgent2);
        childrenAgents.add(parallelGroupAgent1);
        ParallelGroupAgent result = groupAgent.findDependingParallelGroupByNames(parallelGroupAgent1.getFirstName(), parallelGroupAgent1.getLastName());
        assertEquals(parallelGroupAgent1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingParallelGroupByNamesWhenParallelGroupNotFound()
    {
        ParallelGroupAgent result = groupAgent.findDependingParallelGroupByNames("parallelGroupAgent3", "parallelGroupAgent3");
        fail("An IllegalArgumentException should be thrown when trying to find a non-existent parallel group.");
    }

    @Test
    public void testFindDependingSequentialGroupByNames()
    {
        childrenAgents.add(sequentialGroupAgent1);
        childrenAgents.add(sequentialGroupAgent2);
        childrenAgents.add(sequentialGroupAgent1);
        SequentialGroupAgent result = groupAgent.findDependingSequentialGroupByNames(sequentialGroupAgent1.getFirstName(), sequentialGroupAgent1.getLastName());
        assertEquals(sequentialGroupAgent1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingSequentialGroupByNamesWhenSequentialGroupNotFound()
    {
        SequentialGroupAgent result = groupAgent.findDependingSequentialGroupByNames("sequentialGroupAgent3", "sequentialGroupAgent3");
        fail("An IllegalArgumentException should be thrown when trying to find a non-existent sequential group.");
    }

    @Test
    public void testRemoveGapsFromParallelGroups()
    {
        childrenAgents.add(parallelGroupAgent1);
        childrenAgents.add(gapAgent);
        childrenAgents.add(parallelGroupAgent2);
        groupAgent.removeGapsFromParallelGroups();
        assertEquals(1, childrenAgents.size());
        assertTrue(childrenAgents.get(0) instanceof ParallelGroupAgent);
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps()
    {
        childrenAgents.add(gapAgent);
        childrenAgents.add(agent1);
        GroupLayoutEngineer engine = new GroupLayoutEngineer();
        engine.addTemporaryGapToMap(gapAgent, new TestGapAgent(5));
        groupAgent.replaceTemporaryGapsByRealGaps(engine.getMap());
        assertEquals(1, childrenAgents.size());
        assertTrue(childrenAgents.get(0) instanceof TestGapAgent);
        assertEquals(5, ((TestGapAgent)childrenAgents.get(0)).getSize());
    }

    @Test
    public void testAddContentToGroup()
    {
        GroupCreator groupCreator = new GroupCreator();
        GroupLayout.Group result = new GroupLayout.Group();
        GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder(groupCreator, new Settings());
        groupAgent.addContentToGroup(result, creatorAndAdder);
        assertTrue(result.getElements().size() > 0);
    }

    @Test
    public void testGroupContent()
    {
        GroupLayout.Group result = groupAgent.groupContent(new GroupCreatorAndElementAdder());
        assertNotNull(result);
    }

    @Test
    public void testGetFunctionsOnGroupImplemenation()
    {
        FunctionsOnGroupAndElement functionsOnGroup = groupAgent.getFunctionsOnGroupImplemenation();
        assertNotNull(functionsOnGroup);
    }

    @Test
    public void testAddGapBefore()
    {
        childrenAgents.add(agent1);
        childrenAgents.add(gapAgent);
        childrenAgents.add(agent2);
        groupAgent.addAgent(agent1);
        groupAgent.addAgent(agent2);
        groupAgent.addGapBefore(gapAgent, agent1);
        assertEquals(2, childrenAgents.size());
        Agent previousAgent = childrenAgents.get(0);
        assertTrue(previousAgent instanceof GapAgent);
        assertEquals(agent1, childrenAgents.get(1));
    }

    @Test
    public void testAddGapAfter()
    {
        childrenAgents.add(agent1);
        childrenAgents.add(gapAgent);
        childrenAgents.add(agent2);
        groupAgent.addAgent(agent1);
        groupAgent.addAgent(agent2);
        groupAgent.addGapAfter(gapAgent, agent1);
        assertEquals(3, childrenAgents.size());
        Agent nextAgent = childrenAgents.get(2);
        assertTrue(nextAgent instanceof GapAgent);
        assertEquals(agent2, childrenAgents.get(1));
    }
}