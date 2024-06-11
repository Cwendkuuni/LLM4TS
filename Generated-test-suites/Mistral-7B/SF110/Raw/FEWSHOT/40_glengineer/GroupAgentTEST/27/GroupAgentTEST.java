package org.apache.commons.cli; // this package is different from the given one, please replace it with the correct one for GroupAgent tests

import static org.junit.Assert.*;
import org.junit.Test;

import glengineer.agents.*;
import java.util.*;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent childAgent1 = new MyAgent("John", "Doe");
        Agent childAgent2 = new MyAgent("Jane", "Doe");
        groupAgent.addAgent(childAgent1);
        groupAgent.addAgent(childAgent2);

        assertEquals("John", groupAgent.getFirstName());
    }

    @Test
    public void testGetLastName() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent childAgent1 = new MyAgent("John", "Doe");
        Agent childAgent2 = new MyAgent("Jane", "Doe");
        groupAgent.addAgent(childAgent1);
        groupAgent.addAgent(childAgent2);

        assertEquals("Doe", groupAgent.getLastName());
    }

    @Test
    public void testIsComponent() {
        GroupAgent groupAgent = new MyGroupAgent();
        ComponentAgent componentAgent = new MyComponentAgent();
        groupAgent.addAgent(componentAgent);

        assertFalse(groupAgent.isComponent("ComponentName"));
    }

    @Test
    public void testIsGroup() {
        GroupAgent groupAgent1 = new MyGroupAgent();
        GroupAgent groupAgent2 = new MyGroupAgent();
        groupAgent1.addAgent(groupAgent2);

        assertTrue(groupAgent1.isGroup(groupAgent2.getFirstName(), groupAgent2.getLastName()));
    }

    @Test
    public void testIsGap() {
        GroupAgent groupAgent = new MyGroupAgent();
        GapAgent gapAgent = new GapAgent(10);
        groupAgent.addAgent(gapAgent);

        assertFalse(groupAgent.isGap());
    }

    @Test
    public void testEquals() {
        GroupAgent groupAgent1 = new MyGroupAgent();
        GroupAgent groupAgent2 = new MyGroupAgent();
        groupAgent1.addAgent(new MyAgent("John", "Doe"));
        groupAgent2.addAgent(new MyAgent("John", "Doe"));

        assertTrue(groupAgent1.equals(groupAgent2));
    }

    @Test
    public void testAddAgent() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent agent = new MyAgent("John", "Doe");

        groupAgent.addAgent(agent);
        assertTrue(groupAgent.getChildren().contains(agent));
    }

    @Test
    public void testGetChildren() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent childAgent1 = new MyAgent("John", "Doe");
        Agent childAgent2 = new MyAgent("Jane", "Doe");
        groupAgent.addAgent(childAgent1);
        groupAgent.addAgent(childAgent2);

        List<Agent> children = groupAgent.getChildren();
        assertTrue(children.contains(childAgent1));
        assertTrue(children.contains(childAgent2));
    }

    @Test
    public void testGetNumberOfChildren() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent childAgent1 = new MyAgent("John", "Doe");
        Agent childAgent2 = new MyAgent("Jane", "Doe");
        groupAgent.addAgent(childAgent1);
        groupAgent.addAgent(childAgent2);

        assertEquals(2, groupAgent.getNumberOfChildren());
    }

    @Test
    public void testGetComponent() {
        GroupAgent groupAgent = new MyGroupAgent();
        ComponentAgent componentAgent = new MyComponentAgent();
        groupAgent.addAgent(componentAgent);

        assertEquals(componentAgent, groupAgent.getComponent("ComponentName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponentWithInvalidName() {
        GroupAgent groupAgent = new MyGroupAgent();

        groupAgent.getComponent("InvalidComponentName");
    }

    @Test
    public void testGetGroup() {
        GroupAgent groupAgent1 = new MyGroupAgent();
        GroupAgent groupAgent2 = new MyGroupAgent();
        groupAgent1.addAgent(groupAgent2);

        assertEquals(groupAgent2, groupAgent1.getGroup("FirstName", "LastName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGroupWithInvalidName() {
        GroupAgent groupAgent = new MyGroupAgent();
        Agent childAgent = new MyAgent("John", "Doe");
        groupAgent.addAgent(childAgent);

        groupAgent.getGroup("InvalidFirstName", "InvalidLastName");
    }

    @Test
    public void testFindDependingComponentByName() {
        GroupAgent groupAgent = new MyGroupAgent();
        ComponentAgent componentAgent1 = new MyComponentAgent();
        ComponentAgent componentAgent2 = new MyComponentAgent();
        groupAgent.addAgent(componentAgent1);
        groupAgent.addAgent(componentAgent2);

        assertEquals(componentAgent1, groupAgent.findDependingComponentByName("ComponentName1"));
        assertEquals(componentAgent2, groupAgent.findDependingComponentByName("ComponentName2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingComponentByNameWithInvalidName() {
        GroupAgent groupAgent = new MyGroupAgent();
        ComponentAgent componentAgent = new MyComponentAgent();
        groupAgent.addAgent(componentAgent);

        groupAgent.findDependingComponentByName("InvalidComponentName");
    }

    @Test
    public void testFindDependingGroupByNames() {
        GroupAgent groupAgent1 = new MyGroupAgent();
        GroupAgent groupAgent2 = new MyGroupAgent();
        GroupAgent groupAgent3 = new MyGroupAgent();
        groupAgent1.addAgent(groupAgent2);
        groupAgent1.addAgent(groupAgent3);

        assertEquals(groupAgent2, groupAgent1.findDependingGroupByNames("FirstName1", "LastName1"));
        assertEquals(groupAgent3, groupAgent1.findDependingGroupByNames("FirstName3", "LastName3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingGroupByNamesWithInvalidNames() {
        GroupAgent groupAgent = new MyGroupAgent();
        GroupAgent childAgent = new MyGroupAgent();
        groupAgent.addAgent(childAgent);

        groupAgent.findDependingGroupByNames("InvalidFirstName1", "InvalidLastName1");
    }

    @Test
    public void testFindDependingParallelGroupByNames() {
        ParallelGroupAgent parallelGroupAgent1 = new MyParallelGroupAgent();
        ParallelGroupAgent parallelGroupAgent2 = new MyParallelGroupAgent();
        ParallelGroupAgent parallelGroupAgent3 = new MyParallelGroupAgent();
        GroupAgent groupAgent = new MyGroupAgent();
        groupAgent.addAgent(parallelGroupAgent1);
        groupAgent.addAgent(parallelGroupAgent2);
        groupAgent.addAgent(parallelGroupAgent3);

        assertEquals(parallelGroupAgent1, groupAgent.findDependingParallelGroupByNames("FirstName1", "LastName1"));
        assertEquals(parallelGroupAgent3, groupAgent.findDependingParallelGroupByNames("FirstName3", "LastName3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingParallelGroupByNamesWithInvalidNames() {
        GroupAgent groupAgent = new MyGroupAgent();
        ParallelGroupAgent parallelGroupAgent = new MyParallelGroupAgent();
        groupAgent.addAgent(parallelGroupAgent);

        groupAgent.findDependingParallelGroupByNames("InvalidFirstName1", "InvalidLastName1");
    }

    @Test
    public void testFindDependingSequentialGroupByNames() {
        SequentialGroupAgent sequentialGroupAgent1 = new MySequentialGroupAgent();
        SequentialGroupAgent sequentialGroupAgent2 = new MySequentialGroupAgent();
        GroupAgent groupAgent = new MyGroupAgent();
        groupAgent.addAgent(sequentialGroupAgent1);
        groupAgent.addAgent(sequentialGroupAgent2);

        assertEquals(sequentialGroupAgent1, groupAgent.findDependingSequentialGroupByNames("FirstName1", "LastName1"));
        assertEquals(sequentialGroupAgent2, groupAgent.findDependingSequentialGroupByNames("FirstName2", "LastName2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingSequentialGroupByNamesWithInvalidNames() {
        GroupAgent groupAgent = new MyGroupAgent();
        SequentialGroupAgent sequentialGroupAgent = new MySequentialGroupAgent();
        groupAgent.addAgent(sequentialGroupAgent);

        groupAgent.findDependingSequentialGroupByNames("InvalidFirstName1", "InvalidLastName1");
    }
}

class MyGroupAgent extends GroupAgent {
    @Override
    public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
        throw new UnsupportedOperationException();
    }
}

class MyAgent implements Agent {
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
    public boolean isComponent(final String componentName) {
        return false;
    }

    @Override
    public boolean isGroup(final String firstName, final String lastName) {
        return false;
    }

    @Override
    public boolean isGap() {
        return false;
    }

    @Override
    public boolean equals(final Agent agent) {
        return false;
    }
}

class MyComponentAgent extends ComponentAgent {
    public MyComponentAgent() {
        super("ComponentName");
    }
}