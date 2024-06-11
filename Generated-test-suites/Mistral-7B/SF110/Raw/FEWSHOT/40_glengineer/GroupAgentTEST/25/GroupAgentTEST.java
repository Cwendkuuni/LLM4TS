package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import glengineer.agents.*;
import java.util.*;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent group = new MyGroupAgent();
        Agent child1 = new MyAgent("John", "Doe");
        Agent child2 = new MyAgent("Jane", "Doe");
        group.addAgent(child1);
        group.addAgent(child2);

        String result = group.getFirstName();
        assertEquals("John", result);
    }

    @Test
    public void testGetLastName() {
        GroupAgent group = new MyGroupAgent();
        Agent child1 = new MyAgent("John", "Doe");
        Agent child2 = new MyAgent("Jane", "Doe");
        group.addAgent(child1);
        group.addAgent(child2);

        String result = group.getLastName();
        assertEquals("Doe", result);
    }

    @Test
    public void testIsGroup() {
        GroupAgent group = new MyGroupAgent();
        Agent child = new MyAgent("John", "Doe");
        group.addAgent(child);

        boolean result = group.isGroup("John", "Doe");
        assertTrue(result);
    }

    @Test
    public void testIsNotGroup() {
        GroupAgent group = new MyGroupAgent();
        Agent child1 = new MyAgent("John", "Doe");
        Agent child2 = new MyAgent("Jane", "Doe");
        group.addAgent(child1);
        group.addAgent(child2);

        boolean result = group.isGroup("John", "Smith");
        assertFalse(result);
    }

    @Test
    public void testEquals() {
        GroupAgent group1 = new MyGroupAgent();
        GroupAgent group2 = new MyGroupAgent();
        Agent child1 = new MyAgent("John", "Doe");
        Agent child2 = new MyAgent("Jane", "Doe");
        group1.addAgent(child1);
        group1.addAgent(child2);
        group2.addAgent(child1);
        group2.addAgent(child2);

        boolean result = group1.equals(group2);
        assertTrue(result);
    }

    @Test
    public void testAddAgent() {
        GroupAgent group = new MyGroupAgent();
        Agent child = new MyAgent("John", "Doe");
        group.addAgent(child);
        assertEquals(1, group.getNumberOfChildren());
    }

    @Test
    public void testGetChildren() {
        GroupAgent group = new MyGroupAgent();
        Agent child1 = new MyAgent("John", "Doe");
        Agent child2 = new MyAgent("Jane", "Doe");
        group.addAgent(child1);
        group.addAgent(child2);

        List<Agent> children = group.getChildren();
        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
    }

    @Test
    public void testGetNumberOfChildren() {
        GroupAgent group = new MyGroupAgent();
        Agent child1 = new MyAgent("John", "Doe");
        Agent child2 = new MyAgent("Jane", "Doe");
        group.addAgent(child1);
        group.addAgent(child2);

        int result = group.getNumberOfChildren();
        assertEquals(2, result);
    }

    @Test
    public void testGetComponent() {
        GroupAgent group = new MyGroupAgent();
        Agent childComponent = new ComponentAgent("Component");
        Agent childGroup = new MyGroupAgent();
        group.addAgent(childComponent);
        group.addAgent(childGroup);

        ComponentAgent result = group.getComponent("Component");
        assertEquals(childComponent, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponentNotFound() {
        GroupAgent group = new MyGroupAgent();
        Agent childComponent = new ComponentAgent("Component");
        Agent childGroup = new MyGroupAgent();
        group.addAgent(childComponent);
        group.addAgent(childGroup);

        group.getComponent("NonExistentComponent");
    }

    @Test
    public void testGetGroup() {
        GroupAgent parentGroup = new MyGroupAgent();
        GroupAgent childGroup1 = new MyGroupAgent();
        GroupAgent childGroup2 = new MyGroupAgent();
        parentGroup.addAgent(childGroup1);
        parentGroup.addAgent(childGroup2);

        GroupAgent result = parentGroup.getGroup("John", "Doe");
        assertNull(result);

        childGroup1.addAgent(new MyAgent("John", "Doe"));
        result = parentGroup.getGroup("John", "Doe");
        assertEquals(childGroup1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGroupNotFound() {
        GroupAgent parentGroup = new MyGroupAgent();
        GroupAgent childGroup1 = new MyGroupAgent();
        GroupAgent childGroup2 = new MyGroupAgent();
        parentGroup.addAgent(childGroup1);
        parentGroup.addAgent(childGroup2);

        parentGroup.getGroup("John", "Smith");
    }

    @Test
    public void testFindDependingComponentByName() {
        GroupAgent group = new MyGroupAgent();
        Agent component1 = new ComponentAgent("Component1");
        Agent component2 = new ComponentAgent("Component2");
        Agent groupAgent = new MyGroupAgent();
        groupAgent.addAgent(component1);
        groupAgent.addAgent(component2);
        group.addAgent(groupAgent);

        ComponentAgent result = group.findDependingComponentByName("Component1");
        assertEquals(component1, result);

        result = group.findDependingComponentByName("Component3");
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingComponentByNameNotFound() {
        GroupAgent group = new MyGroupAgent();
        Agent component1 = new ComponentAgent("Component1");
        Agent component2 = new ComponentAgent("Component2");
        Agent groupAgent = new MyGroupAgent();
        groupAgent.addAgent(component1);
        groupAgent.addAgent(component2);
        group.addAgent(groupAgent);

        group.findDependingComponentByName("Component3");
    }

    @Test
    public void testFindDependingGroupByNames() {
        GroupAgent parentGroup = new MyGroupAgent();
        GroupAgent childGroup1 = new MyGroupAgent("Group1");
        GroupAgent childGroup2 = new MyGroupAgent("Group2");
        parentGroup.addAgent(childGroup1);
        parentGroup.addAgent(childGroup2);

        GroupAgent result = parentGroup.findDependingGroupByNames("Group1", "Group1");
        assertEquals(childGroup1, result);

        result = parentGroup.findDependingGroupByNames("Group2", "Group3");
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingGroupByNamesNotFound() {
        GroupAgent parentGroup = new MyGroupAgent();
        GroupAgent childGroup1 = new MyGroupAgent("Group1");
        GroupAgent childGroup2 = new MyGroupAgent("Group2");
        parentGroup.addAgent(childGroup1);
        parentGroup.addAgent(childGroup2);

        parentGroup.findDependingGroupByNames("Group3", "Group3");
    }

    @Test
    public void testFindDependingParallelGroupByNames() {
        ParallelGroupAgent parentGroup = new ParallelGroupAgent();
        ParallelGroupAgent childGroup1 = new ParallelGroupAgent();
        SequentialGroupAgent childGroup2 = new SequentialGroupAgent();
        parentGroup.addAgent(childGroup1);
        parentGroup.addAgent(childGroup2);

        ParallelGroupAgent result = parentGroup.findDependingParallelGroupByNames("Group1", "Group1");
        assertEquals(childGroup1, result);

        result = parentGroup.findDependingParallelGroupByNames("Group2", "Group3");
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingParallelGroupByNamesNotFoundParallel() {
        ParallelGroupAgent parentGroup = new ParallelGroupAgent();
        ParallelGroupAgent childGroup1 = new ParallelGroupAgent();
        SequentialGroupAgent childGroup2 = new SequentialGroupAgent();
        parentGroup.addAgent(childGroup1);
        parentGroup.addAgent(childGroup2);

        parentGroup.findDependingParallelGroupByNames("Group3", "Group3");
    }

    @Test
    public void testFindDependingSequentialGroupByNames() {
        SequentialGroupAgent parentGroup = new SequentialGroupAgent();
        ParallelGroupAgent childGroup1 = new ParallelGroupAgent();
        SequentialGroupAgent childGroup2 = new SequentialGroupAgent();
        parentGroup.addAgent(childGroup1);
        parentGroup.addAgent(childGroup2);

        SequentialGroupAgent result = parentGroup.findDependingSequentialGroupByNames("Group1", "Group1");
        assertEquals(childGroup2, result);

        result = parentGroup.findDependingSequentialGroupByNames("Group2", "Group3");
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingSequentialGroupByNamesNotFoundSequential() {
        SequentialGroupAgent parentGroup = new SequentialGroupAgent();
        ParallelGroupAgent childGroup1 = new ParallelGroupAgent();
        SequentialGroupAgent childGroup2 = new SequentialGroupAgent();
        parentGroup.addAgent(childGroup1);
        parentGroup.addAgent(childGroup2);

        parentGroup.findDependingSequentialGroupByNames("Group3", "Group3");
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        ParallelGroupAgent group = new ParallelGroupAgent();
        Agent gap1 = new GapAgent(10);
        Agent gap2 = new GapAgent(20);
        group.addAgent(gap1);
        group.addAgent(new MyAgent("Agent1"));
        group.addAgent(new MyAgent("Agent2"));
        group.addAgent(gap2);

        group.removeGapsFromParallelGroups();
        List<Agent> children = group.getChildren();
        assertEquals(3, children.size());
        assertFalse(children.contains(gap1));
        assertFalse(children.contains(gap2));
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        GroupAgent group = new MyGroupAgent();
        TemporaryGapAgent gap = new TemporaryGapAgent(10);
        group.addAgent(gap);

        GroupLayout.TemporaryGapsToGaps map = new GroupLayout.TemporaryGapsToGaps();
        map.put(gap, new GapAgent(10));

        group.replaceTemporaryGapsByRealGaps(map);
        List<Agent> children = group.getChildren();
        assertEquals(1, children.size());
        assertFalse(children.contains(gap));
        assertTrue(children.contains(new GapAgent(10)));
    }
}

class MyGroupAgent extends GroupAgent {
    public MyGroupAgent() {
        super();
    }

    @Override
    public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
        return new GroupLayout.Group();
    }

    @Override
    public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
        return new FunctionsOnGroupAndElementImplementation(this, null);
    }
}

class MyAgent extends Agent {
    String firstName;
    String lastName;

    public MyAgent(String firstName, String lastName) {
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

    @Override
    public boolean isComponent(String componentName) {
        return false;
    }

    @Override
    public boolean isGroup(String firstName, String lastName) {
        return false;
    }

    @Override
    public boolean isGap() {
        return false;
    }
}