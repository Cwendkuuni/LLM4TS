package org.apache.commons.cli; // This is not the correct package for GroupAgent tests, please replace it with the actual package name.

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import glengineer.agents.settings.*;
import glengineer.*;
import javax.swing.*;
import glengineer.agents.setters.*;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent child1 = new MyAgent("John", "Doe");
        Agent child2 = new MyAgent("Jane", "Doe");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);

        assertEquals("John", groupAgent.getFirstName());
    }

    @Test
    public void testGetLastName() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent child1 = new MyAgent("John", "Doe");
        Agent child2 = new MyAgent("Jane", "Doe");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);

        assertEquals("Doe", groupAgent.getLastName());
    }

    @Test
    public void testIsGroup() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent child = new MyAgent("John", "Doe");
        groupAgent.addAgent(child);

        assertTrue(groupAgent.isGroup("John", "Doe"));
    }

    @Test
    public void testIsNotGroup() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent child1 = new MyAgent("John", "Doe");
        Agent child2 = new MyAgent("Jane", "Doe");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);

        assertFalse(groupAgent.isGroup("John", "Smith"));
    }

    @Test
    public void testIsComponent() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent child = new MyComponentAgent("Component");
        groupAgent.addAgent(child);

        assertFalse(groupAgent.isComponent("Component"));
    }

    @Test
    public void testAddAgent() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent child = new MyAgent("John", "Doe");

        groupAgent.addAgent(child);
        assertEquals(1, groupAgent.getNumberOfChildren());
    }

    @Test
    public void testGetChildren() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent child1 = new MyAgent("John", "Doe");
        Agent child2 = new MyAgent("Jane", "Doe");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);

        List<Agent> children = groupAgent.getChildren();
        assertEquals(2, children.size());
    }

    @Test
    public void testGetNumberOfChildren() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent child1 = new MyAgent("John", "Doe");
        Agent child2 = new MyAgent("Jane", "Doe");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);

        assertEquals(2, groupAgent.getNumberOfChildren());
    }

    @Test
    public void testFindDependingComponentByName() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent child1 = new MyComponentAgent("Component1");
        Agent child2 = new MyComponentAgent("Component2");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);

        ComponentAgent result = groupAgent.findDependingComponentByName("Component1");
        assertEquals(child1, result);

        result = groupAgent.findDependingComponentByName("Component3");
        assertNull(result);
    }

    @Test
    public void testFindDependingGroupByNames() {
        GroupAgent parentGroupAgent = new MyGroupAgent();
        GroupAgent childGroupAgent1 = new MyGroupAgent();
        GroupAgent childGroupAgent2 = new MyGroupAgent();
        parentGroupAgent.addAgent(childGroupAgent1);
        parentGroupAgent.addAgent(childGroupAgent2);

        childGroupAgent1.addAgent(new MyAgent("Child1", "Child1"));
        childGroupAgent1.addAgent(new MyAgent("Child2", "Child2"));
        childGroupAgent2.addAgent(new MyAgent("Child3", "Child3"));
        childGroupAgent2.addAgent(new MyAgent("Child4", "Child4"));

        GroupAgent result = parentGroupAgent.findDependingGroupByNames("Child1", "Child1");
        assertEquals(childGroupAgent1, result);

        result = parentGroupAgent.findDependingGroupByNames("Child3", "Child4");
        assertNull(result);
    }

    @Test
    public void testFindDependingParallelGroupByNames() {
        ParallelGroupAgent parentGroupAgent = new MyParallelGroupAgent();
        ParallelGroupAgent childGroupAgent1 = new MyParallelGroupAgent();
        ParallelGroupAgent childGroupAgent2 = new MyParallelGroupAgent();
        parentGroupAgent.addAgent(childGroupAgent1);
        parentGroupAgent.addAgent(childGroupAgent2);

        childGroupAgent1.addAgent(new MyAgent("Child1", "Child1"));
        childGroupAgent1.addAgent(new MyAgent("Child2", "Child2"));
        childGroupAgent2.addAgent(new MyAgent("Child3", "Child3"));
        childGroupAgent2.addAgent(new MyAgent("Child4", "Child4"));

        ParallelGroupAgent result = parentGroupAgent.findDependingParallelGroupByNames("Child1", "Child1");
        assertEquals(childGroupAgent1, result);

        result = parentGroupAgent.findDependingParallelGroupByNames("Child3", "Child4");
        assertNull(result);
    }

    @Test
    public void testFindDependingSequentialGroupByNames() {
        SequentialGroupAgent parentGroupAgent = new MySequentialGroupAgent();
        SequentialGroupAgent childGroupAgent1 = new MySequentialGroupAgent();
        SequentialGroupAgent childGroupAgent2 = new MySequentialGroupAgent();
        parentGroupAgent.addAgent(childGroupAgent1);
        parentGroupAgent.addAgent(childGroupAgent2);

        childGroupAgent1.addAgent(new MyAgent("Child1", "Child1"));
        childGroupAgent1.addAgent(new MyAgent("Child2", "Child2"));
        childGroupAgent2.addAgent(new MyAgent("Child3", "Child3"));
        childGroupAgent2.addAgent(new MyAgent("Child4", "Child4"));

        SequentialGroupAgent result = parentGroupAgent.findDependingSequentialGroupByNames("Child1", "Child1");
        assertEquals(childGroupAgent1, result);

        result = parentGroupAgent.findDependingSequentialGroupByNames("Child3", "Child4");
        assertNull(result);
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        GroupAgent groupAgent = new MyGroupAgent();
        GapAgent gap1 = new GapAgent(10);
        GapAgent gap2 = new GapAgent(5);
        Agent agent1 = new MyAgent("Agent1");
        Agent agent2 = new MyAgent("Agent2");

        groupAgent.addAgent(agent1);
        groupAgent.addAgent(gap1);
        groupAgent.addAgent(agent2);
        groupAgent.addAgent(gap2);

        groupAgent.removeGapsFromParallelGroups();

        List<Agent> children = groupAgent.getChildren();
        assertEquals(3, children.size());
        assertEquals(agent1, children.get(0));
        assertEquals(agent2, children.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveGapsFromParallelGroupsWithInvalidArguments() {
        GroupAgent groupAgent = new MyGroupAgent();
        GapAgent gap1 = new GapAgent(10);
        Agent agent1 = new MyAgent("Agent1");

        groupAgent.addAgent(agent1);
        groupAgent.addAgent(gap1);

        groupAgent.removeGapsFromParallelGroups();
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        GroupAgent groupAgent = new MyGroupAgent();
        TemporaryGapAgent gap1 = new TemporaryGapAgent(10);
        Agent agent1 = new MyAgent("Agent1");

        groupAgent.addAgent(agent1);
        groupAgent.addAgent(gap1);

        GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
        map.put(gap1, new GapAgent(5));

        groupAgent.replaceTemporaryGapsByRealGaps(map);

        List<Agent> children = groupAgent.getChildren();
        assertEquals(2, children.size());
        assertEquals(agent1, children.get(0));
        assertEquals(new GapAgent(5), children.get(1));
    }

    @Test
    public void testAddContentToGroup() {
        GroupAgent groupAgent = new MyGroupAgent();
        GroupLayoutEngineer engine = new GroupLayoutEngineer();
        GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder();

        groupAgent.addContentToGroup(engine.createRootGroup(), creatorAndAdder);
    }

    @Test
    public void testAddGapBefore() {
        GroupAgent groupAgent = new MyGroupAgent();
        GapAgent gap = new GapAgent(10);
        Agent element = new MyAgent("Element");

        groupAgent.addAgent(element);
        groupAgent.addGapBefore(gap, element);

        List<Agent> children = groupAgent.getChildren();
        assertEquals(2, children.size());
        assertEquals(element, children.get(0));
        assertEquals(gap, children.get(1));
    }

    @Test
    public void testAddGapAfter() {
        GroupAgent groupAgent = new MyGroupAgent();
        GapAgent gap = new GapAgent(10);
        Agent element = new MyAgent("Element");

        groupAgent.addAgent(element);
        groupAgent.addGapAfter(gap, element);

        List<Agent> children = groupAgent.getChildren();
        assertEquals(2, children.size());
        assertEquals(element, children.get(0));
        assertEquals(gap, children.get(1));
    }

    private static class MyAgent extends Agent {
        private String firstName;
        private String lastName;

        public MyAgent(String firstName, String lastName) {
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

        @Override
        public boolean equals(Agent agent) {
            if (agent instanceof MyAgent) {
                MyAgent other = (MyAgent)agent;
                return other.getFirstName().equals(this.firstName) && other.getLastName().equals(this.lastName);
            }
            return false;
        }
    }

    private static class MyComponentAgent extends Agent implements ComponentAgent {
        private String name;

        public MyComponentAgent(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static class MyGroupAgent extends GroupAgent {
        public MyGroupAgent() {
            super();
        }

        @Override
        public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
            throw new UnsupportedOperationException();
        }

        @Override
        public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
            throw new UnsupportedOperationException();
        }
    }

    private static class MyParallelGroupAgent extends ParallelGroupAgent {
        public MyParallelGroupAgent() {
            super();
        }

        @Override
        public Agent getGapsType() {
            throw new UnsupportedOperationException();
        }
    }

    private static class MySequentialGroupAgent extends SequentialGroupAgent {
        public MySequentialGroupAgent() {
            super();
        }

        @Override
        public Agent getGapsType() {
            throw new UnsupportedOperationException();
        }
    }

    private static class TemporaryGapAgent extends Agent implements GapAgent {
        private int size;

        public TemporaryGapAgent(int size) {
            this.size = size;
        }

        @Override
        public int getSize() {
            return size;
        }
    }
}