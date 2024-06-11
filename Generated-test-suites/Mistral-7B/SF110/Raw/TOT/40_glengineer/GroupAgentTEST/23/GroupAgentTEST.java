package glengineer.agents.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.ComponentAgent;
import glengineer.agents.GapAgent;
import glengineer.agents.GroupAgent;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElement;
import glengineer.agents.GroupAgent.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.AgentSetter.AgentType;
import glengineer.agents.setters.AgentSetterFactory;

public class GroupAgentTest
{
    private GroupAgent group;
    private GroupAgent childGroup;
    private ComponentAgent component;
    private Agent temporaryGap;
    private Agent gap;

    @Before
    public void setUp()
    {
        group = new GroupAgent()
        {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0)
            {
                return new GroupLayout.Group();
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation()
            {
                return new FunctionsOnGroupAndElementImplementation(this, null)
                {
                    @Override
                    public void addPrecedingGap(final int size)
                    {
                        super.addPrecedingGap(size);
                        this.parentGroupAgent.addContentToGroup(this.parentGroup, this);
                    }

                    @Override
                    public void addPrecedingGap(final int min, final int pref, final int max)
                    {
                        super.addPrecedingGap(min, pref, max);
                        this.parentGroupAgent.addContentToGroup(this.parentGroup, this);
                    }

                    @Override
                    public void addFollowingGap(final int size)
                    {
                        super.addFollowingGap(size);
                        this.parentGroupAgent.addContentToGroup(this.parentGroup, this);
                    }

                    @Override
                    public void addFollowingGap(final int min, final int pref, final int max)
                    {
                        super.addFollowingGap(min, pref, max);
                        this.parentGroupAgent.addContentToGroup(this.parentGroup, this);
                    }
                };
            }
        };

        component = new ComponentAgent()
        {
            @Override
            public String getName()
            {
                return "TestComponent";
            }
        };

        childGroup = new GroupAgent()
        {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0)
            {
                return new GroupLayout.Group();
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation()
            {
                return new FunctionsOnGroupAndElementImplementation(this, null)
                {
                    @Override
                    public void addPrecedingGap(final int size)
                    {
                        super.addPrecedingGap(size);
                        this.parentGroupAgent.addContentToGroup(this.parentGroup, this);
                    }

                    @Override
                    public void addPrecedingGap(final int min, final int pref, final int max)
                    {
                        super.addPrecedingGap(min, pref, max);
                        this.parentGroupAgent.addContentToGroup(this.parentGroup, this);
                    }

                    @Override
                    public void addFollowingGap(final int size)
                    {
                        super.addFollowingGap(size);
                        this.parentGroupAgent.addContentToGroup(this.parentGroup, this);
                    }

                    @Override
                    public void addFollowingGap(final int min, final int pref, final int max)
                    {
                        super.addFollowingGap(min, pref, max);
                        this.parentGroupAgent.addContentToGroup(this.parentGroup, this);
                    }
                };
            }
        };

        group.addAgent(component);
        group.addAgent(childGroup);

        gap = new GapAgent(5);
        temporaryGap = new TemporaryGapAgent(10);
    }

    @Test
    public void testGetFirstName()
    {
        assertEquals("TestComponent", group.getFirstName());
    }

    @Test
    public void testGetLastName()
    {
        assertNull(group.getLastName());
    }

    @Test
    public void testIsComponent()
    {
        assertFalse(group.isComponent("TestComponent"));
    }

    @Test
    public void testIsGroup()
    {
        assertFalse(group.isGroup("TestFirstName", "TestLastName"));
    }

    @Test
    public void testIsGap()
    {
        assertFalse(group.isGap());
    }

    @Test
    public void testEquals()
    {
        GroupAgent anotherGroup = new GroupAgent();
        assertFalse(group.equals(anotherGroup));

        GroupAgent anotherGroupWithSameValues = new GroupAgent();
        anotherGroupWithSameValues.addAgent(component);
        anotherGroupWithSameValues.addAgent(childGroup);
        assertTrue(group.equals(anotherGroupWithSameValues));
    }

    @Test
    public void testAddAgent()
    {
        List<Agent> children = new ArrayList<Agent>(group.getChildren());
        assertFalse(children.contains(component));

        group.addAgent(component);

        children = new ArrayList<Agent>(group.getChildren());
        assertTrue(children.contains(component));
    }

    @Test
    public void testGetChildren()
    {
        List<Agent> children = new ArrayList<Agent>(group.getChildren());
        assertEquals(2, children.size());
        assertTrue(children.contains(component));
        assertTrue(children.contains(childGroup));
    }

    @Test
    public void testGetNumberOfChildren()
    {
        assertEquals(2, group.getNumberOfChildren());
    }

    @Test
    public void testGetComponent()
    {
        assertEquals(component, group.getComponent("TestComponent"));
    }

    @Test
    public void testGetGroup()
    {
        assertThrows(IllegalArgumentException.class, () -> group.getGroup("TestFirstName", "TestLastName"));
    }

    @Test
    public void testFindDependingComponentByName()
    {
        assertEquals(component, group.findDependingComponentByName("TestComponent"));
    }

    @Test
    public void testFindDependingGroupByNames()
    {
        assertThrows(IllegalArgumentException.class, () -> group.findDependingGroupByNames("TestFirstName", "TestLastName"));
    }

    @Test
    public void testFindDependingParallelGroupByNames()
    {
        assertNull(group.findDependingParallelGroupByNames("TestFirstName", "TestLastName"));
    }

    @Test
    public void testFindDependingSequentialGroupByNames()
    {
        assertNull(group.findDependingSequentialGroupByNames("TestFirstName", "TestLastName"));
    }

    @Test
    public void testRemoveGapsFromParallelGroups()
    {
        group.removeGapsFromParallelGroups();
        List<Agent> children = new ArrayList<Agent>(group.getChildren());
        assertTrue(children.isEmpty());
    }

    @Test
    public void testAddContentToGroup()
    {
        List<Agent> childrenBefore = new ArrayList<Agent>(group.getChildren());

        group.addContentToGroup(new GroupLayout.Group(), new AgentSetterFactory().newAgentSetter(AgentType.GROUP, "NewGroup"));

        List<Agent> childrenAfter = new ArrayList<Agent>(group.getChildren());
        assertEquals(childrenBefore.size() + 1, childrenAfter.size());
        assertTrue(childrenAfter.contains(new GroupAgent()));
    }

    @Test
    public void testAddGapBefore()
    {
        group.addAgent(component);
        List<Agent> childrenBefore = new ArrayList<Agent>(group.getChildren());

        group.addGapBefore(gap, component);

        List<Agent> childrenAfter = new ArrayList<Agent>(group.getChildren());
        assertEquals(childrenBefore.size() + 1, childrenAfter.size());
        assertTrue(childrenAfter.get(childrenBefore.indexOf(component) - 1).equals(gap));
        assertTrue(childrenAfter.contains(component));
    }

    @Test
    public void testAddGapAfter()
    {
        group.addAgent(component);
        List<Agent> childrenBefore = new ArrayList<Agent>(group.getChildren());

        group.addGapAfter(gap, component);

        List<Agent> childrenAfter = new ArrayList<Agent>(group.getChildren());
        assertEquals(childrenBefore.size() + 1, childrenAfter.size());
        assertTrue(childrenAfter.get(childrenBefore.indexOf(component) + 1).equals(gap));
        assertTrue(childrenAfter.contains(component));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapBeforeWithEmptyList()
    {
        group.addGapBefore(gap, component);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapAfterWithEmptyList()
    {
        group.addGapAfter(gap, component);
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps()
    {
        group.addAgent(temporaryGap);
        List<Agent> childrenBefore = new ArrayList<Agent>(group.getChildren());

        GroupAgentTest.TemporaryGapsToGaps map = new GroupAgentTest.TemporaryGapsToGaps();
        map.put(temporaryGap, gap);

        group.replaceTemporaryGapsByRealGaps(map);

        List<Agent> childrenAfter = new ArrayList<Agent>(group.getChildren());
        assertEquals(childrenBefore.size(), childrenAfter.size());
        assertFalse(childrenAfter.contains(temporaryGap));
        assertTrue(childrenAfter.contains(gap));
    }
}

class TemporaryGapsToGaps implements java.util.Map<Agent, Agent>
{
    private final LinkedHashMap<Agent, Agent> map = new LinkedHashMap<Agent, Agent>();

    public void put(final Agent key, final Agent value)
    {
        map.put(key, value);
    }

    public Agent get(final Agent key)
    {
        return map.get(key);
    }
}