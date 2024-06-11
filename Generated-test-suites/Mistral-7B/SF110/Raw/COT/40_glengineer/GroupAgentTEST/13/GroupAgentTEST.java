package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.test.dummies.DummyAgent;
import glengineer.agents.test.dummies.DummyGroupAgent;
import glengineer.agents.test.dummies.DummyParallelGroupAgent;
import glengineer.agents.test.dummies.DummySequentialGroupAgent;
import glengineer.agents.test.dummies.DummyTemporaryGapAgent;
import glengineer.exceptions.IllegalArgumentException;
import glengineer.exceptions.IllegalArgumentException_ComponentNotExists;
import glengineer.exceptions.IllegalArgumentException_GroupNotExists;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new DummyGroupAgent();
        String expected = "John";
        AgentSetter.setFirstName(expected, groupAgent);
        String actual = groupAgent.getFirstName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLastName() {
        GroupAgent groupAgent = new DummyGroupAgent();
        String expected = "Doe";
        AgentSetter.setLastName(expected, groupAgent);
        String actual = groupAgent.getLastName();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFirstNameWhenNoAgentFound() {
        GroupAgent groupAgent = new DummyGroupAgent();
        String expected = "John";
        String actual = groupAgent.getFirstName();
        assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLastNameWhenNoAgentFound() {
        GroupAgent groupAgent = new DummyGroupAgent();
        String expected = "Doe";
        String actual = groupAgent.getLastName();
        assertNull(actual);
    }

    @Test
    public void testIsComponentWhenComponentExists() {
        GroupAgent groupAgent = new DummyGroupAgent();
        Agent componentAgent = new DummyComponentAgent();
        groupAgent.addAgent(componentAgent);
        assertTrue(groupAgent.isComponent("ComponentName"));
    }

    @Test
    public void testIsComponentWhenComponentDoesNotExist() {
        GroupAgent groupAgent = new DummyGroupAgent();
        assertFalse(groupAgent.isComponent("ComponentName"));
    }

    @Test
    public void testIsGroupWhenGroupExists() {
        GroupAgent groupAgent = new DummyGroupAgent("FirstName", "LastName");
        assertTrue(groupAgent.isGroup("FirstName", "LastName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsGroupWhenGroupDoesNotExist() {
        GroupAgent groupAgent = new DummyGroupAgent();
        assertFalse(groupAgent.isGroup("FirstName", "LastName"));
    }

    @Test
    public void testEqualsWhenAgentsAreEqual() {
        GroupAgent agent1 = new DummyGroupAgent("FirstName1", "LastName1");
        GroupAgent agent2 = new DummyGroupAgent("FirstName1", "LastName1");
        assertTrue(agent1.equals(agent2));
    }

    @Test
    public void testEqualsWhenAgentsAreNotEqual() {
        GroupAgent agent1 = new DummyGroupAgent("FirstName1", "LastName1");
        GroupAgent agent2 = new DummyGroupAgent("FirstName2", "LastName2");
        assertFalse(agent1.equals(agent2));
    }

    @Test
    public void testAddAgent() {
        GroupAgent groupAgent = new DummyGroupAgent();
        Agent agentToAdd = new DummyAgent();
        groupAgent.addAgent(agentToAdd);
        Agent expected = agentToAdd;
        Agent actual = groupAgent.getChildren().get(0);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAgentWithNullAgent() {
        GroupAgent groupAgent = new DummyGroupAgent();
        groupAgent.addAgent(null);
    }

    @Test
    public void testGetChildren() {
        GroupAgent groupAgent = new DummyGroupAgent();
        List<Agent> expected = new LinkedList<>();
        Agent child1 = new DummyAgent();
        Agent child2 = new DummyAgent();
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        expected.add(child1);
        expected.add(child2);
        List<Agent> actual = groupAgent.getChildren();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNumberOfChildren() {
        GroupAgent groupAgent = new DummyGroupAgent();
        Agent child1 = new DummyAgent();
        Agent child2 = new DummyAgent();
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        int expected = 2;
        int actual = groupAgent.getNumberOfChildren();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetComponent() {
        GroupAgent groupAgent = new DummyGroupAgent();
        DummyComponentAgent componentAgent = new DummyComponentAgent();
        groupAgent.addAgent(componentAgent);
        ComponentAgent expected = componentAgent;
        ComponentAgent actual = groupAgent.getComponent("ComponentName");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException_ComponentNotExists.class)
    public void testGetComponentWhenComponentDoesNotExist() {
        GroupAgent groupAgent = new DummyGroupAgent();
        assertNull(groupAgent.getComponent("ComponentName"));
    }

    @Test
    public void testGetGroup() {
        GroupAgent parentGroup = new DummyGroupAgent();
        GroupAgent groupAgent = new DummyGroupAgent("FirstName", "LastName");
        parentGroup.addAgent(groupAgent);
        GroupAgent expected = groupAgent;
        GroupAgent actual = parentGroup.getGroup("FirstName", "LastName");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException_GroupNotExists.class)
    public void testGetGroupWhenGroupDoesNotExist() {
        GroupAgent parentGroup = new DummyGroupAgent();
        assertNull(parentGroup.getGroup("FirstName", "LastName"));
    }

    @Test
    public void testFindDependingComponentByName() {
        GroupAgent groupAgent = new DummyGroupAgent();
        DummyComponentAgent componentAgent = new DummyComponentAgent();
        groupAgent.addAgent(componentAgent);
        ComponentAgent expected = componentAgent;
        ComponentAgent actual = groupAgent.findDependingComponentByName("ComponentName");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException_ComponentNotExists.class)
    public void testFindDependingComponentByNameWhenComponentDoesNotExist() {
        GroupAgent groupAgent = new DummyGroupAgent();
        assertNull(groupAgent.findDependingComponentByName("ComponentName"));
    }

    @Test
    public void testFindDependingGroupByNames() {
        GroupAgent parentGroup = new DummyGroupAgent();
        GroupAgent groupAgent = new DummyGroupAgent("FirstName", "LastName");
        parentGroup.addAgent(groupAgent);
        GroupAgent expected = groupAgent;
        GroupAgent actual = parentGroup.findDependingGroupByNames("FirstName", "LastName");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException_GroupNotExists.class)
    public void testFindDependingGroupByNamesWhenGroupDoesNotExist() {
        GroupAgent parentGroup = new DummyGroupAgent();
        assertNull(parentGroup.findDependingGroupByNames("FirstName", "LastName"));
    }

    @Test
    public void testFindDependingParallelGroupByNames() {
        GroupAgent parentGroup = new DummyParallelGroupAgent();
        GroupAgent groupAgent = new DummyGroupAgent("FirstName", "LastName");
        parentGroup.addAgent(groupAgent);
        GroupAgent expected = groupAgent;
        GroupAgent actual = parentGroup.findDependingParallelGroupByNames("FirstName", "LastName");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException_GroupNotExists.class)
    public void testFindDependingParallelGroupByNamesWhenGroupDoesNotExist() {
        GroupAgent parentGroup = new DummyParallelGroupAgent();
        assertNull(parentGroup.findDependingParallelGroupByNames("FirstName", "LastName"));
    }

    @Test
    public void testFindDependingSequentialGroupByNames() {
        GroupAgent parentGroup = new DummySequentialGroupAgent();
        GroupAgent groupAgent = new DummyGroupAgent("FirstName", "LastName");
        parentGroup.addAgent(groupAgent);
        GroupAgent expected = groupAgent;
        GroupAgent actual = parentGroup.findDependingSequentialGroupByNames("FirstName", "LastName");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException_GroupNotExists.class)
    public void testFindDependingSequentialGroupByNamesWhenGroupDoesNotExist() {
        GroupAgent parentGroup = new DummySequentialGroupAgent();
        assertNull(parentGroup.findDependingSequentialGroupByNames("FirstName", "LastName"));
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        GroupAgent groupAgent = new DummyGroupAgent();
        groupAgent.removeGapsFromParallelGroups();
        // Verify that the 'removeGapsFromParallelGroups' methods have been called in the correct order
        // and that the expected changes have been made in the 'childrenAgents' list
        // Add assertions here based on the implementation of the methods 'passTheCallToSubgroups', 'removeGapsFromParallelSubgroupsWithOtherContent', 'replaceParallelSubgroupsWithGapsByTheirSingleGaps', 'replaceContinuousSequencesOfGapsBySingleGaps', and 'replaceGroupsWithSingleElementsByThatElements'
    }
}