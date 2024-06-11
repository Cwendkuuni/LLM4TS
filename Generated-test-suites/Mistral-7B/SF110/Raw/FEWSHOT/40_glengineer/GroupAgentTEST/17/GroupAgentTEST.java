package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        Agent child1 = new MyChildAgent("Child1FirstName", "Child1LastName");
        Agent child2 = new MyChildAgent("Child2FirstName", "Child2LastName");
        agent.addAgent(child1);
        agent.addAgent(child2);

        assertEquals("FirstName", agent.getFirstName());
    }

    @Test
    public void testGetLastName() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        Agent child1 = new MyChildAgent("Child1FirstName", "Child1LastName");
        Agent child2 = new MyChildAgent("Child2FirstName", "Child2LastName");
        agent.addAgent(child1);
        agent.addAgent(child2);

        assertEquals("LastName", agent.getLastName());
    }

    @Test
    public void testIsComponent() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        assertFalse(agent.isComponent("ComponentName"));
    }

    @Test
    public void testIsGroup() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        Agent child = new MyGroupAgent("ChildFirstName", "ChildLastName");
        agent.addAgent(child);

        assertTrue(agent.isGroup(agent.getFirstName(), agent.getLastName()));
    }

    @Test
    public void testIsGroup_WithNull() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        assertFalse(agent.isGroup(null, null));
    }

    @Test
    public void testEquals() {
        GroupAgent agent1 = new MyGroupAgent("FirstName1", "LastName1");
        GroupAgent agent2 = new MyGroupAgent("FirstName2", "LastName2");
        Agent child1 = new MyChildAgent("Child1FirstName", "Child1LastName");
        Agent child2 = new MyChildAgent("Child2FirstName", "Child2LastName");
        agent1.addAgent(child1);
        agent1.addAgent(child2);
        agent2.addAgent(child1);
        agent2.addAgent(child2);

        assertTrue(agent1.equals(agent2));
    }

    @Test
    public void testAddAgent() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        Agent child = new MyChildAgent("ChildFirstName", "ChildLastName");
        agent.addAgent(child);
        List<Agent> children = agent.getChildren();

        assertEquals(1, children.size());
        assertTrue(children.contains(child));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAgent_Null() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        agent.addAgent(null);
    }

    @Test
    public void testGetChildren() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        Agent child1 = new MyChildAgent("Child1FirstName", "Child1LastName");
        Agent child2 = new MyChildAgent("Child2FirstName", "Child2LastName");
        agent.addAgent(child1);
        agent.addAgent(child2);
        List<Agent> children = agent.getChildren();

        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
    }

    @Test
    public void testGetNumberOfChildren() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        Agent child1 = new MyChildAgent("Child1FirstName", "Child1LastName");
        Agent child2 = new MyChildAgent("Child2FirstName", "Child2LastName");
        agent.addAgent(child1);
        agent.addAgent(child2);

        assertEquals(2, agent.getNumberOfChildren());
    }

    @Test
    public void testGetComponent() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        ComponentAgent component = new MyComponentAgent("ComponentName");
        agent.addAgent(component);

        assertEquals(component, agent.getComponent("ComponentName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponent_InvalidName() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        assertNull(agent.getComponent("InvalidName"));
    }

    @Test
    public void testGetGroup() {
        GroupAgent agent1 = new MyGroupAgent("FirstName1", "LastName1");
        GroupAgent agent2 = new MyGroupAgent("FirstName2", "LastName2");
        agent1.addAgent(agent2);

        assertEquals(agent2, agent1.getGroup("FirstName2", "LastName2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGroup_InvalidGroup() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        assertNull(agent.getGroup("InvalidFirstName", "InvalidLastName"));
    }

    @Test
    public void testFindDependingComponentByName() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        ComponentAgent component1 = new MyComponentAgent("ComponentName1");
        ComponentAgent component2 = new MyComponentAgent("ComponentName2");
        agent.addAgent(component1);
        agent.addAgent(component2);

        assertEquals(component2, agent.findDependingComponentByName("ComponentName2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingComponentByName_InvalidName() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        assertNull(agent.findDependingComponentByName("InvalidName"));
    }

    @Test
    public void testFindDependingGroupByNames() {
        GroupAgent agent1 = new MyGroupAgent("FirstName1", "LastName1");
        GroupAgent agent2 = new MyGroupAgent("FirstName2", "LastName2");
        agent1.addAgent(agent2);

        assertEquals(agent2, agent1.findDependingGroupByNames("FirstName2", "LastName2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingGroupByNames_InvalidGroup() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        assertNull(agent.findDependingGroupByNames("InvalidFirstName", "InvalidLastName"));
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        ParallelGroupAgent parallelGroup = new ParallelGroupAgent();
        GapAgent gap = new GapAgent(10);
        parallelGroup.addAgent(gap);
        agent.addAgent(parallelGroup);

        agent.removeGapsFromParallelGroups();

        List<Agent> children = agent.getChildren();
        assertFalse(children.contains(parallelGroup));
        assertFalse(children.contains(gap));
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        GroupAgent agent = new MyGroupAgent("FirstName", "LastName");
        TemporaryGapAgent gap = new TemporaryGapAgent(10);
        agent.addAgent(gap);

        GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
        map.put(gap, new GapAgent(10));

        agent.replaceTemporaryGapsByRealGaps(map);

        List<Agent> children = agent.getChildren();
        assertTrue(children.contains(new GapAgent(10)));
        assertFalse(children.contains(gap));
    }
}

public abstract class MyGroupAgent extends GroupAgent {
    protected MyGroupAgent(String firstName, String lastName) {
        super();
        this.settings = new Settings();
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

public abstract class MyChildAgent extends Agent {
    protected MyChildAgent(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }
}