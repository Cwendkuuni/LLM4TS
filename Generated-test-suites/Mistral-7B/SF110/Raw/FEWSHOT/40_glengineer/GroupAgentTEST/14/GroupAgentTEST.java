package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import glengineer.agents.settings.*;
import glengineer.agents.*;
import glengineer.agents.setters.*;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent group = new MyGroupAgent();
        Agent child1 = new MyChildAgent("firstName1", "lastName1");
        Agent child2 = new MyChildAgent("firstName2", "lastName2");
        group.addAgent(child1);
        group.addAgent(child2);

        assertEquals("firstName1", group.getFirstName());
    }

    @Test
    public void testGetLastName() {
        GroupAgent group = new MyGroupAgent();
        Agent child1 = new MyChildAgent("firstName1", "lastName1");
        Agent child2 = new MyChildAgent("firstName2", "lastName2");
        group.addAgent(child1);
        group.addAgent(child2);

        assertEquals("lastName1", group.getLastName());
    }

    @Test
    public void testIsComponent() {
        GroupAgent group = new MyGroupAgent();
        assertFalse(group.isComponent("componentName"));
    }

    @Test
    public void testIsGroup() {
        GroupAgent group = new MyGroupAgent();
        assertFalse(group.isGroup("firstName", "lastName"));
    }

    @Test
    public void testEquals() {
        GroupAgent group1 = new MyGroupAgent();
        GroupAgent group2 = new MyGroupAgent();
        assertFalse(group1.equals(group2));

        GroupAgent group3 = new MyGroupAgent();
        group3.setFirstName("firstName");
        group3.setLastName("lastName");
        group1.setFirstName("firstName");
        group1.setLastName("lastName");
        assertTrue(group1.equals(group3));
    }

    @Test
    public void testAddAgent() {
        GroupAgent group = new MyGroupAgent();
        Agent child = new MyChildAgent("firstName", "lastName");
        group.addAgent(child);
        assertEquals(1, group.getNumberOfChildren());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAgent() {
        GroupAgent group = new MyGroupAgent();
        group.addAgent(null);
    }

    @Test
    public void testGetChildren() {
        GroupAgent group = new MyGroupAgent();
        Agent child1 = new MyChildAgent("firstName1", "lastName1");
        Agent child2 = new MyChildAgent("firstName2", "lastName2");
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
        Agent child1 = new MyChildAgent("firstName1", "lastName1");
        Agent child2 = new MyChildAgent("firstName2", "lastName2");
        group.addAgent(child1);
        group.addAgent(child2);

        assertEquals(2, group.getNumberOfChildren());
    }

    @Test
    public void testGetComponent() {
        GroupAgent group = new MyGroupAgent();
        ComponentAgent component = new MyComponentAgent("componentName");
        group.addAgent(component);

        ComponentAgent result = group.getComponent("componentName");
        assertEquals(component, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponentNotFound() {
        GroupAgent group = new MyGroupAgent();
        group.getComponent("componentName");
    }

    @Test
    public void testGetGroup() {
        GroupAgent group1 = new MyGroupAgent();
        GroupAgent group2 = new MyGroupAgent();
        group1.setFirstName("firstName");
        group1.setLastName("lastName");
        group2.setFirstName("firstName");
        group2.setLastName("lastName");
        group1.addAgent(group2);

        GroupAgent result = group1.getGroup("firstName", "lastName");
        assertEquals(group2, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGroupNotFound() {
        GroupAgent group = new MyGroupAgent();
        group.getGroup("firstName", "lastName");
    }

    @Test
    public void testFindDependingComponentByName() {
        GroupAgent group = new MyGroupAgent();
        ComponentAgent component1 = new MyComponentAgent("componentName1");
        ComponentAgent component2 = new MyComponentAgent("componentName2");
        group.addAgent(component1);
        group.addAgent(component2);

        ComponentAgent result = group.findDependingComponentByName("componentName1");
        assertEquals(component1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingComponentByNameNotFound() {
        GroupAgent group = new MyGroupAgent();
        group.findDependingComponentByName("componentName");
    }

    @Test
    public void testFindDependingGroupByNames() {
        GroupAgent group1 = new MyGroupAgent();
        GroupAgent group2 = new MyGroupAgent();
        group1.setFirstName("firstName");
        group1.setLastName("lastName");
        group2.setFirstName("firstName");
        group2.setLastName("lastName");
        group1.addAgent(group2);

        GroupAgent result = group1.findDependingGroupByNames("firstName", "lastName");
        assertEquals(group2, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingGroupByNamesNotFound() {
        GroupAgent group = new MyGroupAgent();
        group.findDependingGroupByNames("firstName", "lastName");
    }

    @Test
    public void testFindDependingParallelGroupByNames() {
        ParallelGroupAgent group1 = new ParallelGroupAgent();
        GroupAgent group2 = new MyGroupAgent();
        group1.setFirstName("firstName");
        group1.setLastName("lastName");
        group2.setFirstName("firstName");
        group2.setLastName("lastName");
        group1.addAgent(group2);

        ParallelGroupAgent result = group1.findDependingParallelGroupByNames("firstName", "lastName");
        assertEquals(group2, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingParallelGroupByNamesNotFound() {
        GroupAgent group = new MyGroupAgent();
        group.findDependingParallelGroupByNames("firstName", "lastName");
    }

    @Test
    public void testFindDependingSequentialGroupByNames() {
        SequentialGroupAgent group1 = new SequentialGroupAgent();
        GroupAgent group2 = new MyGroupAgent();
        group1.setFirstName("firstName");
        group1.setLastName("lastName");
        group2.setFirstName("firstName");
        group2.setLastName("lastName");
        group1.addAgent(group2);

        SequentialGroupAgent result = group1.findDependingSequentialGroupByNames("firstName", "lastName");
        assertEquals(group2, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingSequentialGroupByNamesNotFound() {
        GroupAgent group = new MyGroupAgent();
        group.findDependingSequentialGroupByNames("firstName", "lastName");
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        GroupAgent group = new MyGroupAgent();
        ParallelGroupAgent parallelGroup1 = new ParallelGroupAgent();
        ParallelGroupAgent parallelGroup2 = new ParallelGroupAgent();
        GroupAgent gapAgent1 = new MyGapAgent();
        GroupAgent gapAgent2 = new MyGapAgent();
        group.addAgent(parallelGroup1);
        parallelGroup1.addAgent(gapAgent1);
        parallelGroup1.addAgent(parallelGroup2);
        parallelGroup2.addAgent(gapAgent2);

        group.removeGapsFromParallelGroups();

        assertEquals(2, group.getNumberOfChildren());
        List<Agent> children = group.getChildren();
        assertTrue(children.contains(parallelGroup1));
        assertTrue(children.contains(parallelGroup2));
        assertFalse(parallelGroup1.containsGapsOnly());
        assertFalse(parallelGroup2.containsGapsOnly());
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        GroupAgent group = new MyGroupAgent();
        TemporaryGapAgent temporaryGap1 = new TemporaryGapAgent();
        TemporaryGapAgent temporaryGap2 = new TemporaryGapAgent();
        group.addAgent(temporaryGap1);
        group.addAgent(temporaryGap2);

        GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
        map.put(temporaryGap1, new MyGapAgent(10));
        map.put(temporaryGap2, new MyGapAgent(20));

        group.replaceTemporaryGapsByRealGaps(map);

        List<Agent> children = group.getChildren();
        assertEquals(2, children.size());
        assertTrue(children.contains(new MyGapAgent(10)));
        assertTrue(children.contains(new MyGapAgent(20)));
        assertFalse(children.contains(temporaryGap1));
        assertFalse(children.contains(temporaryGap2));
    }

    @Test
    public void testAddGapBefore() {
        GroupAgent group = new MyGroupAgent();
        GapAgent gap = new GapAgent(10);
        Agent element = new MyChildAgent("firstName", "lastName");

        group.addAgent(element);
        group.addGapBefore(gap, element);

        List<Agent> children = group.getChildren();
        assertEquals(2, children.size());
        assertTrue(children.get(0) instanceof GapAgent);
        assertTrue(children.get(1) instanceof MyChildAgent);
        assertEquals(gap, children.get(0));
        assertEquals(element, children.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapBeforeElementNotFound() {
        GroupAgent group = new MyGroupAgent();
        GapAgent gap = new GapAgent(10);
        Agent element = new MyChildAgent("firstName", "lastName");

        group.addAgent(element);
        group.addGapBefore(gap, null);
    }

    @Test
    public void testAddGapAfter() {
        GroupAgent group = new MyGroupAgent();
        GapAgent gap = new GapAgent(10);
        Agent element = new MyChildAgent("firstName", "lastName");

        group.addAgent(element);
        group.addGapAfter(gap, element);

        List<Agent> children = group.getChildren();
        assertEquals(2, children.size());
        assertTrue(children.get(0) instanceof MyChildAgent);
        assertTrue(children.get(1) instanceof GapAgent);
        assertEquals(element, children.get(0));
        assertEquals(gap, children.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapAfterElementNotFound() {
        GroupAgent group = new MyGroupAgent();
        GapAgent gap = new GapAgent(10);
        Agent element = new MyChildAgent("firstName", "lastName");

        group.addAgent(element);
        group.addGapAfter(null, element);
    }
}

class MyGroupAgent extends GroupAgent {
    @Override
    public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
        return new ArrayList<Agent>();
    }

    @Override
    public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
        return new FunctionsOnGroupAndElementImplementation(this, null);
    }
}

class MyChildAgent extends Agent {
    String firstName;
    String lastName;

    public MyChildAgent(String firstName, String lastName) {
        super("", "");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }
}

class MyComponentAgent extends Agent {
    String name;

    public MyComponentAgent(String name) {
        super("", "");
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isComponent(String componentName) {
        return this.name.equals(componentName);
    }
}

class MyGapAgent extends Agent {
    int size;

    public MyGapAgent(int size) {
        super("", "");
        this.size = size;
    }

    @Override
    public boolean isGap() {
        return true;
    }
}