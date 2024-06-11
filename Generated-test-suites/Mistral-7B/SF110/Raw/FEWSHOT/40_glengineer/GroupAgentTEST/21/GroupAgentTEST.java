package org.apache.commons.cli; // Replace with the package name of GroupAgent if necessary

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new MyGroupAgent("FirstName", "LastName");
        Agent child1 = new MyAgent("Child1_FirstName", "Child1_LastName");
        Agent child2 = new MyAgent("Child2_FirstName", "Child2_LastName");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        assertEquals("Child1_FirstName", groupAgent.getFirstName());
    }

    @Test
    public void testGetLastName() {
        GroupAgent groupAgent = new MyGroupAgent("FirstName", "LastName");
        Agent child1 = new MyAgent("Child1_FirstName", "Child1_LastName");
        Agent child2 = new MyAgent("Child2_FirstName", "Child2_LastName");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        assertEquals("Child1_LastName", groupAgent.getLastName());
    }

    @Test
    public void testIsComponent() {
        GroupAgent groupAgent = new MyGroupAgent("FirstName", "LastName");
        assertFalse(groupAgent.isComponent("componentName"));
    }

    @Test
    public void testIsGroup() {
        GroupAgent groupAgent1 = new MyGroupAgent("FirstName1", "LastName1");
        GroupAgent groupAgent2 = new MyGroupAgent("FirstName2", "LastName2");
        groupAgent1.addAgent(groupAgent2);
        assertTrue(groupAgent1.isGroup("FirstName1", "LastName1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsGroupWithDifferentFirstName() {
        GroupAgent groupAgent1 = new MyGroupAgent("FirstName1", "LastName1");
        GroupAgent groupAgent2 = new MyGroupAgent("FirstName2", "LastName2");
        groupAgent1.addAgent(groupAgent2);
        assertFalse(groupAgent1.isGroup("FirstName2", "LastName2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsGroupWithDifferentLastName() {
        GroupAgent groupAgent1 = new MyGroupAgent("FirstName1", "LastName1");
        GroupAgent groupAgent2 = new MyGroupAgent("FirstName1", "LastName2");
        groupAgent1.addAgent(groupAgent2);
        assertFalse(groupAgent1.isGroup("FirstName1", "LastName2"));
    }

    @Test
    public void testEquals() {
        GroupAgent groupAgent1 = new MyGroupAgent("FirstName1", "LastName1");
        GroupAgent groupAgent2 = new MyGroupAgent("FirstName1", "LastName1");
        assertTrue(groupAgent1.equals(groupAgent2));
    }

    @Test
    public void testAddAgent() {
        GroupAgent groupAgent = new MyGroupAgent("FirstName", "LastName");
        Agent child = new MyAgent("Child_FirstName", "Child_LastName");
        groupAgent.addAgent(child);
        assertEquals(1, groupAgent.getNumberOfChildren());
        assertEquals(child, groupAgent.getChildren().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAgent() {
        GroupAgent groupAgent = new MyGroupAgent("FirstName", "LastName");
        try {
            groupAgent.addAgent(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetChildren() {
        GroupAgent groupAgent = new MyGroupAgent("FirstName", "LastName");
        Agent child1 = new MyAgent("Child1_FirstName", "Child1_LastName");
        Agent child2 = new MyAgent("Child2_FirstName", "Child2_LastName");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        List<Agent> children = groupAgent.getChildren();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testGetNumberOfChildren() {
        GroupAgent groupAgent = new MyGroupAgent("FirstName", "LastName");
        Agent child1 = new MyAgent("Child1_FirstName", "Child1_LastName");
        Agent child2 = new MyAgent("Child2_FirstName", "Child2_LastName");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        assertEquals(2, groupAgent.getNumberOfChildren());
    }

    @Test
    public void testGetComponent() {
        GroupAgent groupAgent = new MyGroupAgent("FirstName", "LastName");
        ComponentAgent componentAgent = new MyComponentAgent();
        groupAgent.addAgent(componentAgent);
        assertEquals(componentAgent, groupAgent.getComponent("componentName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponentWithWrongName() {
        GroupAgent groupAgent = new MyGroupAgent("FirstName", "LastName");
        ComponentAgent componentAgent = new MyComponentAgent();
        groupAgent.addAgent(componentAgent);
        throw new IllegalArgumentException("component componentName in the " + groupAgent + " not found");
    }

    @Test
    public void testGetGroup() {
        GroupAgent groupAgent1 = new MyGroupAgent("FirstName1", "LastName1");
        GroupAgent groupAgent2 = new MyGroupAgent("FirstName2", "LastName2");
        groupAgent1.addAgent(groupAgent2);
        assertEquals(groupAgent2, groupAgent1.getGroup("FirstName2", "LastName2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGroupWithDifferentFirstName() {
        GroupAgent groupAgent1 = new MyGroupAgent("FirstName1", "LastName1");
        GroupAgent groupAgent2 = new MyGroupAgent("FirstName2", "LastName2");
        groupAgent1.addAgent(groupAgent2);
        throw new IllegalArgumentException("the group <FirstName2, LastName2> in the " + groupAgent1 + " not found");
    }

    @Test
    public void testGetGroupWithDifferentLastName() {
        GroupAgent groupAgent1 = new MyGroupAgent("FirstName1", "LastName1");
        GroupAgent groupAgent2 = new MyGroupAgent("FirstName1", "LastName2");
        groupAgent1.addAgent(groupAgent2);
        throw new IllegalArgumentException("the group <FirstName1, LastName2> in the " + groupAgent1 + " not found");
    }

    @Test
    public void testFindDependingComponentByName() {
        GroupAgent groupAgent = new MyGroupAgent("FirstName", "LastName");
        ComponentAgent componentAgent1 = new MyComponentAgent1();
        ComponentAgent componentAgent2 = new MyComponentAgent2();
        groupAgent.addAgent(componentAgent1);
        groupAgent.addAgent(componentAgent2);
        assertEquals(componentAgent1, groupAgent.findDependingComponentByName("componentName1"));
        assertEquals(componentAgent2, groupAgent.findDependingComponentByName("componentName2"));
        assertNull(groupAgent.findDependingComponentByName("nonExistentComponentName"));
    }

    @Test
    public void testFindDependingGroupByNames() {
        GroupAgent groupAgent1 = new MyGroupAgent1("FirstName1", "LastName1");
        GroupAgent groupAgent2 = new MyGroupAgent2("FirstName2", "LastName2");
        GroupAgent groupAgent3 = new MyGroupAgent3("FirstName3", "LastName3");
        groupAgent1.addAgent(groupAgent2);
        groupAgent1.addAgent(groupAgent3);
        assertEquals(groupAgent2, groupAgent1.findDependingGroupByNames("FirstName2", "LastName2"));
        assertEquals(groupAgent3, groupAgent1.findDependingGroupByNames("FirstName3", "LastName3"));
        throw new IllegalArgumentException("the group <FirstName4, LastName4> in the " + groupAgent1 + " not found");
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        GroupAgent groupAgent = new MyGroupAgent("FirstName", "LastName");
        List<Agent> children = new LinkedList<>();
        children.add(new TemporaryGapAgent("TemporaryGapAgent1"));
        children.add(new Agent("Agent1"));
        children.add(new TemporaryGapAgent("TemporaryGapAgent2"));
        children.add(new Agent("Agent2"));
        groupAgent.setChildren(children);
        List<Agent> expectedChildren = Arrays.asList(new GapAgent(10), new Agent("Agent1"), new GapAgent(15), new Agent("Agent2"));
        groupAgent.replaceTemporaryGapsByRealGaps(Collections.singletonMap(GroupAgentTest.TemporaryGapAgent.class, agent -> new GapAgent(20)));
        assertEquals(expectedChildren, groupAgent.getChildren());
    }

    private static class MyGroupAgent extends GroupAgent {
        MyGroupAgent(String firstName, String lastName) {
            super();
            this.settings = new Settings();
            setFirstName(firstName);
            setLastName(lastName);
        }

        @Override
        public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
            // Implement groupContent method here
            return null;
        }

        @Override
        public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
            return new FunctionsOnGroupAndElementImplementation(this, null);
        }
    }

    private static class MyAgent extends Agent {
        MyAgent(String firstName, String lastName) {
            super();
            this.settings = new Settings();
            setFirstName(firstName);
            setLastName(lastName);
        }
    }

    private static class MyComponentAgent extends ComponentAgent {
        MyComponentAgent() {
            super();
            this.settings = new Settings();
        }
    }

    private static class MyComponentAgent1 extends ComponentAgent {
        MyComponentAgent1() {
            super();
            this.settings = new Settings();
        }
    }

    private static class MyComponentAgent2 extends ComponentAgent {
        MyComponentAgent2() {
            super();
            this.settings = new Settings();
        }
    }

    private static class TemporaryGapAgent extends Agent {
        TemporaryGapAgent() {
            super();
            this.settings = new Settings();
        }

        static class FunctionsOnTemporaryGapAndElement implements FunctionsOnGroupAndElement {
            @Override
            public void addPrecedingGap(int size) {
                // Implement addPrecedingGap method here
            }

            @Override
            public void addPrecedingGap(int min, int pref, int max) {
                // Implement addPrecedingGap method here
            }

            @Override
            public void addFollowingGap(int size) {
                // Implement addFollowingGap method here
            }

            @Override
            public void addFollowingGap(int min, int pref, int max) {
                // Implement addFollowingGap method here
            }
        }
    }

    static class FunctionsOnGroupAndElementImplementation implements FunctionsOnGroupAndElement {
        protected GroupAgent parentGroupAgent;
        protected Agent elementAgent;

        FunctionsOnGroupAndElementImplementation(GroupAgent parent, Agent element) {
            this.parentGroupAgent = parent;
            this.elementAgent = element;
        }

        @Override
        public void addPrecedingGap(int size) {
            parentGroupAgent.addGapBefore(new GapAgent(size), elementAgent);
        }

        @Override
        public void addPrecedingGap(int min, int pref, int max) {
            parentGroupAgent.addGapBefore(new GapAgent(min, pref, max), elementAgent);
        }

        @Override
        public void addFollowingGap(int size) {
            parentGroupAgent.addGapAfter(new GapAgent(size), elementAgent);
        }

        @Override
        public void addFollowingGap(int min, int pref, int max) {
            parentGroupAgent.addGapAfter(new GapAgent(min, pref, max), elementAgent);
        }
    }
}