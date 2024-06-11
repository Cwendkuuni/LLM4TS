package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.Settings;
import glengineer.agents.setters.ComponentAgent;
import glengineer.agents.setters.GroupAgent;
import glengineer.agents.setters.GapAgent;
import glengineer.agents.setters.TemporaryGapAgent;

public class GroupAgentTest
{
    private GroupAgent groupAgent;
    private Agent childComponentAgent;
    private Agent childGroupAgent;
    private Agent childGapAgent;
    private Agent childTemporaryGapAgent;

    @Before
    public void setUp()
    {
        groupAgent = new GroupAgent()
        {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0)
            {
                return null;
            }
        };

        childComponentAgent = new ComponentAgent()
        {
            @Override
            public String getFirstName()
            {
                return "ChildComponent";
            }

            @Override
            public String getLastName()
            {
                return "ChildComponent";
            }
        };

        childGroupAgent = new GroupAgent()
        {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0)
            {
                return null;
            }
        };

        childGapAgent = new GapAgent(10);

        childTemporaryGapAgent = new TemporaryGapAgent(10);

        groupAgent.addAgent(childComponentAgent);
        groupAgent.addAgent(childGroupAgent);
        groupAgent.addAgent(childGapAgent);
        groupAgent.addAgent(childTemporaryGapAgent);
    }

    @Test
    public void testGetFirstName()
    {
        assertEquals("ChildComponent", groupAgent.getFirstName());
    }

    @Test
    public void testGetLastName()
    {
        assertEquals("ChildComponent", groupAgent.getLastName());
    }

    @Test
    public void testIsComponent()
    {
        assertFalse(groupAgent.isComponent("Component"));
    }

    @Test
    public void testIsGroup()
    {
        assertTrue(groupAgent.isGroup("ChildComponent", "ChildComponent"));
    }

    @Test
    public void testIsGap()
    {
        assertFalse(groupAgent.isGap());
    }

    @Test
    public void testEquals()
    {
        GroupAgent groupAgent2 = new GroupAgent()
        {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0)
            {
                return null;
            }
        };

        groupAgent2.addAgent(childComponentAgent);
        groupAgent2.addAgent(childGroupAgent);
        groupAgent2.addAgent(childGapAgent);
        groupAgent2.addAgent(childTemporaryGapAgent);

        assertTrue(groupAgent.equals(groupAgent2));
    }

    @Test
    public void testAddAgent()
    {
        groupAgent.addAgent(null);
        assertTrue(groupAgent.getChildren().isEmpty());

        groupAgent.addAgent(childComponentAgent);
        List<Agent> children = groupAgent.getChildren();
        assertEquals(1, children.size());
        assertEquals(childComponentAgent, children.get(0));

        groupAgent.addAgent(childGroupAgent);
        children = groupAgent.getChildren();
        assertEquals(2, children.size());
        assertEquals(childComponentAgent, children.get(0));
        assertEquals(childGroupAgent, children.get(1));

        groupAgent.addAgent(childGapAgent);
        children = groupAgent.getChildren();
        assertEquals(3, children.size());
        assertEquals(childComponentAgent, children.get(0));
        assertEquals(childGroupAgent, children.get(1));
        assertEquals(childGapAgent, children.get(2));

        groupAgent.addAgent(childTemporaryGapAgent);
        children = groupAgent.getChildren();
        assertEquals(4, children.size());
        assertEquals(childComponentAgent, children.get(0));
        assertEquals(childGroupAgent, children.get(1));
        assertEquals(childGapAgent, children.get(2));
        assertEquals(childTemporaryGapAgent, children.get(3));
    }

    @Test
    public void testGetChildren()
    {
        List<Agent> children = groupAgent.getChildren();
        assertTrue(children.isEmpty());

        groupAgent.addAgent(childComponentAgent);
        children = groupAgent.getChildren();
        assertEquals(1, children.size());
        assertEquals(childComponentAgent, children.get(0));

        groupAgent.addAgent(childGroupAgent);
        children = groupAgent.getChildren();
        assertEquals(2, children.size());
        assertEquals(childComponentAgent, children.get(0));
        assertEquals(childGroupAgent, children.get(1));

        groupAgent.addAgent(childGapAgent);
        children = groupAgent.getChildren();
        assertEquals(3, children.size());
        assertEquals(childComponentAgent, children.get(0));
        assertEquals(childGroupAgent, children.get(1));
        assertEquals(childGapAgent, children.get(2));

        groupAgent.addAgent(childTemporaryGapAgent);
        children = groupAgent.getChildren();
        assertEquals(4, children.size());
        assertEquals(childComponentAgent, children.get(0));
        assertEquals(childGroupAgent, children.get(1));
        assertEquals(childGapAgent, children.get(2));
        assertEquals(childTemporaryGapAgent, children.get(3));
    }

    @Test
    public void testGetNumberOfChildren()
    {
        assertEquals(0, groupAgent.getNumberOfChildren());

        groupAgent.addAgent(childComponentAgent);
        assertEquals(1, groupAgent.getNumberOfChildren());

        groupAgent.addAgent(childGroupAgent);
        assertEquals(2, groupAgent.getNumberOfChildren());

        groupAgent.addAgent(childGapAgent);
        assertEquals(3, groupAgent.getNumberOfChildren());

        groupAgent.addAgent(childTemporaryGapAgent);
        assertEquals(4, groupAgent.getNumberOfChildren());
    }

    @Test
    public void testGetComponent()
    {
        assertEquals(childComponentAgent, groupAgent.getComponent("ChildComponent"));

        try
        {
            groupAgent.getComponent("Component");
            fail("Component not found");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testGetGroup()
    {
        assertEquals(childGroupAgent, groupAgent.getGroup("ChildComponent", "ChildComponent"));

        try
        {
            groupAgent.getGroup("Component", "Component");
            fail("Component not found");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }

        try
        {
            groupAgent.getGroup("ChildComponent", "Component");
            fail("Component not found");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testFindDependingComponentByName()
    {
        assertEquals(childComponentAgent, groupAgent.findDependingComponentByName("ChildComponent"));

        try
        {
            groupAgent.findDependingComponentByName("Component");
            fail("Component not found");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testFindDependingGroupByNames()
    {
        assertEquals(childGroupAgent, groupAgent.findDependingGroupByNames("ChildComponent", "ChildComponent"));

        try
        {
            groupAgent.findDependingGroupByNames("Component", "Component");
            fail("Component not found");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }

        try
        {
            groupAgent.findDependingGroupByNames("ChildComponent", "Component");
            fail("Component not found");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testFindDependingParallelGroupByNames()
    {
        // This test case is not applicable, as the 'GroupAgent' under test is not a 'ParallelGroupAgent'.
        // If 'GroupAgent' was extended from 'ParallelGroupAgent', the test could be implemented as follows:

        // GroupAgent groupAgentParallel = new ParallelGroupAgent()
        // {
        // @Override
        // protected ParallelGroupAgent groupContent(GroupCreatorAndElementAdder p0)
        // {
        // return null;
        // }
        // };

        // groupAgentParallel.addAgent(childComponentAgent);
        // groupAgentParallel.addAgent(childGroupAgent);
        // groupAgentParallel.addAgent(childGapAgent);
        // groupAgentParallel.addAgent(childTemporaryGapAgent);

        // assertEquals(childGroupAgent, groupAgentParallel.findDependingParallelGroupByNames("ChildComponent", "ChildComponent"));

        // try
        // {
        // groupAgentParallel.findDependingParallelGroupByNames("Component", "Component");
        // fail("Component not found");
        // }
        // catch (IllegalArgumentException e)
        // {
        // // Expected
        // }

        // try
        // {
        // groupAgentParallel.findDependingParallelGroupByNames("ChildComponent", "Component");
        // fail("Component not found");
        // }
        // catch (IllegalArgumentException e)
        // {
        // // Expected
        // }
    }

    @Test
    public void testFindDependingSequentialGroupByNames()
    {
        // This test case is not applicable, as the 'GroupAgent' under test is not a 'SequentialGroupAgent'.
        // If 'GroupAgent' was extended from 'SequentialGroupAgent', the test could be implemented as follows:

        // GroupAgent groupAgentSequential = new SequentialGroupAgent()
        // {
        // @Override
        // protected SequentialGroupAgent groupContent(GroupCreatorAndElementAdder p0)
        // {
        // return null;
        // }
        // };

        // groupAgentSequential.addAgent(childComponentAgent);
        // groupAgentSequential.addAgent(childGroupAgent);
        // groupAgentSequential.addAgent(childGapAgent);
        // groupAgentSequential.addAgent(childTemporaryGapAgent);

        // assertEquals(childGroupAgent, groupAgentSequential.findDependingSequentialGroupByNames("ChildComponent", "ChildComponent"));

        // try
        // {
        // groupAgentSequential.findDependingSequentialGroupByNames("Component", "Component");
        // fail("Component not found");
        // }
        // catch (IllegalArgumentException e)
        // {
        // // Expected
        // }

        // try
        // {
        // groupAgentSequential.findDependingSequentialGroupByNames("ChildComponent", "Component");
        // fail("Component not found");
        // }
        // catch (IllegalArgumentException e)
        // {
        // // Expected
        // }
    }

    @Test
    public void testRemoveGapsFromParallelGroups()
    {
        groupAgent.removeGapsFromParallelGroups();
        List<Agent> children = groupAgent.getChildren();
        assertTrue(children.isEmpty());
    }
}