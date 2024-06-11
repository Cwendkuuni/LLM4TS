package glengineer.agents.test;

import static org.junit.Assert.*;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import java.util.ArrayList;
import java.util.LinkedList;

public class GroupAgentTest {

    @org.junit.Before
    public void setUp() {
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return new GroupLayout.Group();
            }
        };
        groupAgent.settings = new Settings();
        this.groupAgent = groupAgent;
    }

    private GroupAgent groupAgent;

    @org.junit.Test
    public void testGetFirstName() {
        Agent child1 = new AgentSetter().setFirstName("John").setLastName("Doe").createAgent();
        Agent child2 = new AgentSetter().setFirstName("Jane").setLastName("Doe").createAgent();
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        String result = groupAgent.getFirstName();
        assertEquals("John", result);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testGetFirstNameWhenNoChildren() {
        String result = groupAgent.getFirstName();
        assertNull(result);
    }

    @org.junit.Test
    public void testGetLastName() {
        Agent child1 = new AgentSetter().setFirstName("John").setLastName("Doe").createAgent();
        Agent child2 = new AgentSetter().setFirstName("Jane").setLastName("Doe").createAgent();
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        String result = groupAgent.getLastName();
        assertEquals("Doe", result);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testGetLastNameWhenNoChildren() {
        String result = groupAgent.getLastName();
        assertNull(result);
    }

    @org.junit.Test
    public void testIsGroup() {
        Agent child = new AgentSetter().setFirstName("John").setLastName("Doe").createAgent();
        groupAgent.addAgent(child);
        boolean result = groupAgent.isGroup("John", "Doe");
        assertTrue(result);
    }

    @org.junit.Test
    public void testIsGroupWhenFirstNameDoesntMatch() {
        Agent child = new AgentSetter().setFirstName("John").setLastName("Doe").createAgent();
        groupAgent.addAgent(child);
        boolean result = groupAgent.isGroup("Jane", "Doe");
        assertFalse(result);
    }

    @org.junit.Test
    public void testIsGroupWhenLastNameDoesntMatch() {
        Agent child = new AgentSetter().setFirstName("John").setLastName("Doe").createAgent();
        groupAgent.addAgent(child);
        boolean result = groupAgent.isGroup("John", "Smith");
        assertFalse(result);
    }

    @org.junit.Test
    public void testIsGroupWhenNotAGroup() {
        Agent child = new AgentSetter().setFirstName("John").setLastName("Doe").createAgent();
        groupAgent.addAgent(child);
        boolean result = groupAgent.isGroup("", "");
        assertFalse(result);
    }

    @org.junit.Test
    public void testAddAgent() {
        Agent child = new AgentSetter().setFirstName("John").setLastName("Doe").createAgent();
        groupAgent.addAgent(child);
        assertTrue(groupAgent.getChildren().contains(child));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddNullAgent() {
        groupAgent.addAgent(null);
    }

    @org.junit.Test
    public void testGetChildren() {
        Agent child1 = new AgentSetter().setFirstName("John").setLastName("Doe").createAgent();
        Agent child2 = new AgentSetter().setFirstName("Jane").setLastName("Doe").createAgent();
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        List<Agent> result = groupAgent.getChildren();
        assertEquals(2, result.size());
        assertTrue(result.contains(child1));
        assertTrue(result.contains(child2));
    }

    @org.junit.Test
    public void testGetNumberOfChildren() {
        Agent child1 = new AgentSetter().setFirstName("John").setLastName("Doe").createAgent();
        Agent child2 = new AgentSetter().setFirstName("Jane").setLastName("Doe").createAgent();
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        int result = groupAgent.getNumberOfChildren();
        assertEquals(2, result);
    }

    @org.junit.Test
    public void testGetComponent() {
        Agent component = new AgentSetter().setFirstName("Component").setLastName("").createAgent();
        groupAgent.addAgent(component);
        ComponentAgent result = groupAgent.getComponent("Component");
        assertEquals(component, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetComponentWhenComponentNotFound() {
        groupAgent.addAgent(new AgentSetter().setFirstName("Component").setLastName("").createAgent());
        groupAgent.getComponent("NonExistentComponent");
    }

    @org.junit.Test
    public void testGetGroup() {
        Agent group = new AgentSetter().setFirstName("Group").setLastName("").createAgent();
        groupAgent.addAgent(group);
        GroupAgent result = (GroupAgent) groupAgent.getGroup("Group", "");
        assertEquals(group, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetGroupWhenGroupNotFound() {
        groupAgent.addAgent(new AgentSetter().setFirstName("Group").setLastName("").createAgent());
        groupAgent.getGroup("NonExistentGroup", "");
    }

    @org.junit.Test
    public void testFindDependingComponentByName() {
        Agent parent = new AgentSetter().setFirstName("Parent").setLastName("").createAgent();
        Agent component = new AgentSetter().setFirstName("Component").setLastName("").createAgent();
        groupAgent.addAgent(parent);
        parent.addAgent(component);
        ComponentAgent result = groupAgent.findDependingComponentByName("Component");
        assertEquals(component, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindDependingComponentByNameWhenComponentNotFound() {
        Agent parent = new AgentSetter().setFirstName("Parent").setLastName("").createAgent();
        groupAgent.addAgent(parent);
        parent.findDependingComponentByName("NonExistentComponent");
    }

    @org.junit.Test
    public void testFindDependingGroupByNames() {
        Agent parent = new AgentSetter().setFirstName("Parent").setLastName("").createAgent();
        Agent group = new AgentSetter().setFirstName("Group").setLastName("").createAgent();
        groupAgent.addAgent(parent);
        parent.addAgent(group);
        GroupAgent result = (GroupAgent) groupAgent.findDependingGroupByNames("Group", "");
        assertEquals(group, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindDependingGroupByNamesWhenGroupNotFound() {
        Agent parent = new AgentSetter().setFirstName("Parent").setLastName("").createAgent();
        groupAgent.addAgent(parent);
        parent.findDependingGroupByNames("NonExistentGroup", "");
    }

    @org.junit.Test
    public void testFindDependingParallelGroupByNames() {
        Agent parent = new AgentSetter().setFirstName("Parent").setLastName("").createAgent();
        ParallelGroupAgent parallelGroup = new ParallelGroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return new GroupLayout.Group();
            }
        };
        parallelGroup.settings = new Settings();
        groupAgent.addAgent(parent);
        parent.addAgent(parallelGroup);
        ParallelGroupAgent result = (ParallelGroupAgent) groupAgent.findDependingParallelGroupByNames("ParallelGroup", "");
        assertEquals(parallelGroup, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindDependingParallelGroupByNamesWhenParallelGroupNotFound() {
        Agent parent = new AgentSetter().setFirstName("Parent").setLastName("").createAgent();
        groupAgent.addAgent(parent);
        parent.findDependingParallelGroupByNames("NonExistentParallelGroup", "");
    }

    @org.junit.Test
    public void testFindDependingSequentialGroupByNames() {
        Agent parent = new AgentSetter().setFirstName("Parent").setLastName("").createAgent();
        SequentialGroupAgent sequentialGroup = new SequentialGroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return new GroupLayout.Group();
            }
        };
        sequentialGroup.settings = new Settings();
        groupAgent.addAgent(parent);
        parent.addAgent(sequentialGroup);
        SequentialGroupAgent result = (SequentialGroupAgent) groupAgent.findDependingSequentialGroupByNames("SequentialGroup", "");
        assertEquals(sequentialGroup, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindDependingSequentialGroupByNamesWhenSequentialGroupNotFound() {
        Agent parent = new AgentSetter().setFirstName("Parent").setLastName("").createAgent();
        groupAgent.addAgent(parent);
        parent.findDependingSequentialGroupByNames("NonExistentSequentialGroup", "");
    }

    @org.junit.Test
    public void testRemoveGapsFromParallelGroups() {
        LinkedList<Agent> children = new LinkedList<>();
        children.add(new GapAgent(5));
        children.add(new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return new GroupLayout.Group();
            }
        });
        children.add(new GapAgent(3));
        children.add(new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return new GroupLayout.Group();
            }
        });
        children.add(new GapAgent(4));
        groupAgent.childrenAgents = children;
        groupAgent.removeGapsFromParallelGroups();
        List<Agent> result = groupAgent.getChildren();
        assertEquals(3, result.size());
        assertFalse(result.contains(children.get(0)));
        assertFalse(result.contains(children.get(2)));
    }
}