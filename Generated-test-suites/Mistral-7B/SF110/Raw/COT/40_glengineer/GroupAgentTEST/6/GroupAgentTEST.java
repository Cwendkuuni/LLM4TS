package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.ComponentAgentSetter;
import glengineer.agents.setters.GapAgentSetter;
import glengineer.agents.setters.GroupAgentSetter;
import glengineer.agents.setters.TemporaryGapAgentSetter;
import glengineer.agents.utils.FunctionOnGroupAndElement;
import glengineer.agents.utils.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.utils.GroupAgentTestUtils;
import glengineer.agents.utils.GroupLayoutEngineer;
import glengineer.agents.utils.GroupLayout.Group;
import glengineer.components.ComponentAgent;
import glengineer.components.GapAgent;
import glengineer.components.TemporaryGapAgent;
import glengineer.exceptions.IllegalArgumentException;

@RunWith(Parameterized.class)
public class GroupAgentTest {

    private GroupAgent agent;
    private Settings settings;

    @Parameterized.Parameters(name = "{index}: test case {0}")
    public static List<Object[]> data() {
        return GroupAgentTestUtils.generateData();
    }

    @Parameterized.Constructor
    public GroupAgentTest(GroupAgent agent, Settings settings) {
        this.agent = agent;
        this.settings = settings;
    }

    @Test
    public void testGetFirstName() {
        String expected = AgentSetter.getDefaultFirstName();
        String actual = this.agent.getFirstName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLastName() {
        String expected = AgentSetter.getDefaultLastName();
        String actual = this.agent.getLastName();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFirstNameWhenEmpty() {
        this.agent.getFirstName();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLastNameWhenEmpty() {
        this.agent.getLastName();
    }

    @Test
    public void testIsComponent() {
        assertFalse(this.agent.isComponent(""));
    }

    @Test
    public void testIsGroup() {
        assertTrue(this.agent.isGroup(AgentSetter.getDefaultFirstName(), AgentSetter.getDefaultLastName()));
    }

    @Test
    public void testIsGap() {
        assertFalse(this.agent.isGap());
    }

    @Test
    public void testEquals() {
        GroupAgent another = new GroupAgentSetter(AgentSetter.getDefaultFirstName(), AgentSetter.getDefaultLastName()).createAgent();
        assertTrue(this.agent.equals(another));
    }

    @Test
    public void testAddAgent() {
        ComponentAgent child = new ComponentAgentSetter("Child").createAgent();
        this.agent.addAgent(child);
        List<Agent> children = this.agent.getChildren();
        assertEquals(1, children.size());
        assertEquals(child, children.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAgent() {
        this.agent.addAgent(null);
    }

    @Test
    public void testGetChildren() {
        List<Agent> children = this.agent.getChildren();
        assertTrue(children.isEmpty());
    }

    @Test
    public void testGetNumberOfChildren() {
        this.agent.addAgent(new ComponentAgentSetter("Child").createAgent());
        int actual = this.agent.getNumberOfChildren();
        assertEquals(1, actual);
    }

    @Test
    public void testGetComponent() {
        ComponentAgent child = new ComponentAgentSetter("Child").createAgent();
        this.agent.addAgent(child);
        ComponentAgent result = this.agent.getComponent("Child");
        assertEquals(child, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponentWhenNotFound() {
        this.agent.getComponent("NonExistentChild");
    }

    @Test
    public void testGetGroup() {
        GroupAgent child = new GroupAgentSetter(AgentSetter.getDefaultFirstName(), AgentSetter.getDefaultLastName()).createAgent();
        this.agent.addAgent(child);
        GroupAgent result = (GroupAgent)this.agent.getGroup(AgentSetter.getDefaultFirstName(), AgentSetter.getDefaultLastName());
        assertEquals(child, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGroupWhenNotFound() {
        this.agent.getGroup("NonExistentFirstName", "NonExistentLastName");
    }

    @Test
    public void testFindDependingComponentByName() {
        ComponentAgent child = new ComponentAgentSetter("Child").createAgent();
        this.agent.addAgent(child);
        ComponentAgent result = this.agent.findDependingComponentByName("Child");
        assertEquals(child, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingComponentByNameWhenNotFound() {
        this.agent.findDependingComponentByName("NonExistentName");
    }

    @Test
    public void testFindDependingGroupByNames() {
        GroupAgent child = new GroupAgentSetter(AgentSetter.getDefaultFirstName(), AgentSetter.getDefaultLastName()).createAgent();
        this.agent.addAgent(child);
        GroupAgent result = (GroupAgent)this.agent.findDependingGroupByNames(AgentSetter.getDefaultFirstName(), AgentSetter.getDefaultLastName());
        assertEquals(child, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingGroupByNamesWhenNotFound() {
        this.agent.findDependingGroupByNames("NonExistentFirstName", "NonExistentLastName");
    }

    @Test
    public void testFindDependingParallelGroupByNames() {
        ParallelGroupAgent child = new ParallelGroupAgentSetter(AgentSetter.getDefaultFirstName(), AgentSetter.getDefaultLastName()).createAgent();
        this.agent.addAgent(child);
        ParallelGroupAgent result = (ParallelGroupAgent)this.agent.findDependingParallelGroupByNames(AgentSetter.getDefaultFirstName(), AgentSetter.getDefaultLastName());
        assertEquals(child, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingParallelGroupByNamesWhenNotFound() {
        this.agent.findDependingParallelGroupByNames("NonExistentFirstName", "NonExistentLastName");
    }

    @Test
    public void testFindDependingSequentialGroupByNames() {
        SequentialGroupAgent child = new SequentialGroupAgentSetter(AgentSetter.getDefaultFirstName(), AgentSetter.getDefaultLastName()).createAgent();
        this.agent.addAgent(child);
        SequentialGroupAgent result = (SequentialGroupAgent)this.agent.findDependingSequentialGroupByNames(AgentSetter.getDefaultFirstName(), AgentSetter.getDefaultLastName());
        assertEquals(child, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingSequentialGroupByNamesWhenNotFound() {
        this.agent.findDependingSequentialGroupByNames("NonExistentFirstName", "NonExistentLastName");
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        GroupAgent child1 = new GroupAgentSetter("Child1", "LastName1").createAgent();
        ParallelGroupAgent child2 = new ParallelGroupAgentSetter("Child2", "LastName2").createAgent();
        this.agent.addAgent(child1);
        this.agent.addAgent(child2);
        int initialSize = this.agent.getNumberOfChildren();
        this.agent.removeGapsFromParallelGroups();
        int finalSize = this.agent.getNumberOfChildren();
        assertEquals(initialSize - child2.getNumberOfGaps(), finalSize);
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        GroupAgent agentWithTemporaryGaps = new GroupAgentSetter("GroupWithTemporaryGaps", "LastName").createAgent();
        TemporaryGapAgent gap1 = new TemporaryGapAgentSetter(1).createAgent();
        TemporaryGapAgent gap2 = new TemporaryGapAgentSetter(2).createAgent();
        agentWithTemporaryGaps.addAgent(gap1);
        agentWithTemporaryGaps.addAgent(gap2);

        GroupAgent agentWithRealGaps = new GroupAgentSetter("GroupWithRealGaps", "LastName").createAgent();
        GapAgent gap3 = new GapAgentSetter(1).createAgent();
        agentWithRealGaps.addAgent(gap3);

        GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
        map.put(gap1, gap3);
        map.put(gap2, gap3);

        agentWithTemporaryGaps.replaceTemporaryGapsByRealGaps(map);
        List<Agent> children = agentWithTemporaryGaps.getChildren();
        assertEquals(1, children.size());
        assertEquals(gap3, children.get(0));
    }

    @Test
    public void testAddContentToGroup() {
        GroupLayoutEngineer engine = new GroupLayoutEngineer();
        Group parentGroup = engine.createGroup();
        FunctionOnGroupAndElement functionOnGroupAndElement = new FunctionsOnGroupAndElementImplementation(this.agent, new ComponentAgentSetter("Component").createAgent());
        this.agent.addContentToGroup(parentGroup, functionOnGroupAndElement);

        List<Agent> children = this.agent.getChildren();
        Agent component = children.get(0);
        Group childGroup = parentGroup.getChildren().get(0);
        List<Agent> childGroupChildren = childGroup.getChildren();

        assertEquals(1, children.size());
        assertEquals(component, children.get(0));
        assertEquals(1, childGroupChildren.size());
        assertEquals(component, childGroupChildren.get(0));
    }

    @Test
    public void testAddGapBefore() {
        GroupAgent agent = new GroupAgentSetter("Agent").createAgent();
        ComponentAgent component = new ComponentAgentSetter("Component").createAgent();
        GapAgent gap = new GapAgentSetter(1).createAgent();

        agent.addAgent(component);
        agent.addGapBefore(gap, component);

        List<Agent> children = agent.getChildren();
        assertEquals(2, children.size());
        Agent firstChild = children.get(0);
        Agent secondChild = children.get(1);

        assertEquals(gap, firstChild);
        assertEquals(component, secondChild);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapBeforeWithNullAgent() {
        GroupAgent agent = new GroupAgentSetter("Agent").createAgent();
        GapAgent gap = new GapAgentSetter(1).createAgent();
        agent.addGapBefore(gap, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapBeforeWithNullGap() {
        GroupAgent agent = new GroupAgentSetter("Agent").createAgent();
        ComponentAgent component = new ComponentAgentSetter("Component").createAgent();
        agent.addGapBefore(null, component);
    }

    @Test
    public void testAddGapAfter() {
        GroupAgent agent = new GroupAgentSetter("Agent").createAgent();
        ComponentAgent component = new ComponentAgentSetter("Component").createAgent();
        GapAgent gap = new GapAgentSetter(1).createAgent();

        agent.addAgent(component);
        agent.addGapAfter(gap, component);

        List<Agent> children = agent.getChildren();
        assertEquals(2, children.size());
        Agent firstChild = children.get(0);
        Agent secondChild = children.get(1);

        assertEquals(component, firstChild);
        assertEquals(gap, secondChild);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapAfterWithNullAgent() {
        GroupAgent agent = new GroupAgentSetter("Agent").createAgent();
        GapAgent gap = new GapAgentSetter(1).createAgent();
        agent.addGapAfter(gap, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapAfterWithNullGap() {
        GroupAgent agent = new GroupAgentSetter("Agent").createAgent();
        ComponentAgent component = new ComponentAgentSetter("Component").createAgent();
        agent.addGapAfter(null, component);
    }
}