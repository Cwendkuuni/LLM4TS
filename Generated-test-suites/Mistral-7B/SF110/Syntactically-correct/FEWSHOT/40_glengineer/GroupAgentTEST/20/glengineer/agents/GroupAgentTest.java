package org.apache.commons.cli; // Replace with the correct package name for GroupAgent tests

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import glengineer.*;
import glengineer.agents.*;
import glengineer.agents.setters.*;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        group.addAgent(new AgentTest("Child1"));
        group.addAgent(new AgentTest("Child2"));

        assertEquals("Child1", group.getFirstName());
    }

    @Test
    public void testGetLastName() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        group.addAgent(new AgentTest("Child1"));
        group.addAgent(new AgentTest("Child2"));

        assertEquals("LastName", group.getLastName());
    }

    @Test
    public void testIsComponent() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        Agent component = new ComponentAgentTest("ComponentName");
        group.addAgent(component);

        assertFalse(group.isComponent("NonExistentComponentName"));
        assertTrue(group.isComponent("ComponentName"));
    }

    @Test
    public void testIsGroup() {
        GroupAgent group1 = new MyGroupAgent("Group1FirstName", "Group1LastName");
        GroupAgent group2 = new MyGroupAgent("Group2FirstName", "Group2LastName");
        group1.addAgent(group2);

        assertTrue(group1.isGroup("Group1FirstName", "Group1LastName"));
        assertTrue(group1.isGroup("Group2FirstName", "Group2LastName"));
    }

    @Test
    public void testIsGap() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        GapAgent gap = new GapAgent(10);
        group.addAgent(gap);

        assertFalse(group.isGap());
        assertFalse(gap.isGroup(group.getFirstName(), group.getLastName()));
        assertTrue(gap.isGap());
    }

    @Test
    public void testEquals() {
        GroupAgent group1 = new MyGroupAgent("Group1FirstName", "Group1LastName");
        GroupAgent group2 = new MyGroupAgent("Group2FirstName", "Group2LastName");
        GroupAgent group3 = new MyGroupAgent("Group1FirstName", "Group1LastName");

        assertFalse(group1.equals(group2));
        assertTrue(group1.equals(group3));
    }

    @Test
    public void testAddAgent() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        Agent agent = new AgentTest("AgentName");
        group.addAgent(agent);

        assertTrue(group.getChildren().contains(agent));
    }

    @Test
    public void testGetChildren() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        Agent child1 = new AgentTest("Child1");
        Agent child2 = new AgentTest("Child2");
        group.addAgent(child1);
        group.addAgent(child2);

        List<Agent> children = group.getChildren();
        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
    }

    @Test
    public void testGetNumberOfChildren() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        Agent child1 = new AgentTest("Child1");
        Agent child2 = new AgentTest("Child2");
        group.addAgent(child1);
        group.addAgent(child2);

        assertEquals(2, group.getNumberOfChildren());
    }

    @Test
    public void testGetComponent() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        ComponentAgent component = new ComponentAgentTest("ComponentName");
        group.addAgent(component);

        assertEquals(component, group.getComponent("ComponentName"));
    }

    @Test
    public void testGetGroup() {
        GroupAgent group1 = new MyGroupAgent("Group1FirstName", "Group1LastName");
        GroupAgent group2 = new MyGroupAgent("Group2FirstName", "Group2LastName");
        group1.addAgent(group2);

        assertEquals(group2, group1.getGroup("Group2FirstName", "Group2LastName"));
    }

    @Test
    public void testFindDependingComponentByName() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        ComponentAgent component1 = new ComponentAgentTest("Component1Name");
        ComponentAgent component2 = new ComponentAgentTest("Component2Name");
        group.addAgent(component1);
        group.addAgent(component2);

        assertEquals(component1, group.findDependingComponentByName("Component1Name"));
    }

    @Test
    public void testFindDependingGroupByNames() {
        GroupAgent group1 = new MyGroupAgent("Group1FirstName", "Group1LastName");
        GroupAgent group2 = new MyGroupAgent("Group2FirstName", "Group2LastName");
        GroupAgent group3 = new MyGroupAgent("Group3FirstName", "Group3LastName");
        group1.addAgent(group2);
        group1.addAgent(group3);

        assertEquals(group2, group1.findDependingGroupByNames("Group2FirstName", "Group2LastName"));
    }

    @Test
    public void testFindDependingParallelGroupByNames() {
        ParallelGroupAgent parallelGroup1 = new ParallelGroupAgent("ParallelGroup1FirstName", "ParallelGroup1LastName");
        ParallelGroupAgent parallelGroup2 = new ParallelGroupAgent("ParallelGroup2FirstName", "ParallelGroup2LastName");
        ParallelGroupAgent parallelGroup3 = new ParallelGroupAgent("ParallelGroup3FirstName", "ParallelGroup3LastName");
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        group.addAgent(parallelGroup1);
        group.addAgent(parallelGroup2);
        group.addAgent(parallelGroup3);

        assertEquals(parallelGroup1, group.findDependingParallelGroupByNames("ParallelGroup1FirstName", "ParallelGroup1LastName"));
    }

    @Test
    public void testFindDependingSequentialGroupByNames() {
        SequentialGroupAgent sequentialGroup1 = new SequentialGroupAgent("SequentialGroup1FirstName", "SequentialGroup1LastName");
        SequentialGroupAgent sequentialGroup2 = new SequentialGroupAgent("SequentialGroup2FirstName", "SequentialGroup2LastName");
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        group.addAgent(sequentialGroup1);
        group.addAgent(sequentialGroup2);

        assertEquals(sequentialGroup1, group.findDependingSequentialGroupByNames("SequentialGroup1FirstName", "SequentialGroup1LastName"));
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        GapAgent gap1 = new GapAgent(10);
        ParallelGroupAgent parallelGroup1 = new ParallelGroupAgent("ParallelGroupFirstName", "ParallelGroupLastName");
        parallelGroup1.addAgent(gap1);
        group.addAgent(parallelGroup1);

        group.removeGapsFromParallelGroups();

        assertTrue(group.getChildren().isEmpty());
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        GapAgent gap1 = new TemporaryGapAgent(10);
        group.addAgent(gap1);

        Map<TemporaryGapAgent, Agent> map = new HashMap<>();
        map.put(gap1, new GapAgent(10));

        group.replaceTemporaryGapsByRealGaps(map);

        assertTrue(group.getChildren().size() == 1);
        assertTrue(group.getChildren().get(0) instanceof GapAgent);
    }

    @Test
    public void testAddContentToGroup() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");
        GroupLayoutEngineer engine = Mockito.mock(GroupLayoutEngineer.class);
        GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder(engine);

        group.addContentToGroup(null, creatorAndAdder);

        Mockito.verify(engine, Mockito.never()).addGroupToGroup(any(GroupLayout.Group.class), any(GroupCreatorAndElementAdder.class), any(GroupLayout.Group.class));
    }

    @Test
    public void testGroupContent() {
        GroupAgent group = new MyGroupAgent("FirstName", "LastName");

        try {
            group.groupContent(null);
            fail("Should throw an exception");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}

class AgentTest extends Agent {
    public AgentTest(String name) {
        super(name);
    }
}

class ComponentAgentTest extends AgentTest implements ComponentAgent {
}