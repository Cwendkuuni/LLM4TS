package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.test.utils.TestHelper;
import glengineer.agents.test.utils.TestUtils;
import glengineer.agents.utils.AgentType;
import glengineer.agents.utils.GroupLayout;
import glengineer.agents.utils.GroupLayout.Group;
import glengineer.agents.utils.functions.FunctionsOnGroupAndElement;
import glengineer.agents.utils.functions.FunctionsOnGroupAndElement.FunctionsOnGroupAndElementImplementation;
import glengineer.components.ComponentAgent;
import glengineer.components.GapAgent;
import glengineer.exceptions.AgentCreationException;
import glengineer.exceptions.IllegalArgumentException;

public class GroupAgentTest
{
    private GroupAgent testGroupAgent;
    private Settings testSettings;
    private GroupAgent childGroupAgent1;
    private GroupAgent childGroupAgent2;
    private ComponentAgent testComponentAgent;
    private GapAgent testGapAgent;

    @Before
    public void initialize()
    {
        testSettings = new Settings();
        testGroupAgent = new GroupAgent()
        {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0)
            {
                return TestHelper.getTestGroupContent();
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation()
            {
                return new FunctionsOnGroupAndElementImplementation(this, null);
            }
        };
        childGroupAgent1 = new GroupAgent()
        {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0)
            {
                return TestHelper.getTestGroupContent();
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation()
            {
                return new FunctionsOnGroupAndElementImplementation(this, null);
            }
        };
        childGroupAgent2 = new GroupAgent()
        {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0)
            {
                return TestHelper.getTestGroupContent();
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation()
            {
                return new FunctionsOnGroupAndElementImplementation(this, null);
            }
        };
        testComponentAgent = new AgentSetter().createComponentAgent("testComponent");
        testGapAgent = new GapAgent(1);

        testGroupAgent.addAgent(childGroupAgent1);
        testGroupAgent.addAgent(childGroupAgent2);
    }

    @Test
    public void testGetFirstName()
    {
        assertNull(testGroupAgent.getFirstName());
        String testFirstName = "testFirstName";
        childGroupAgent1.setFirstName(testFirstName);
        assertEquals(testFirstName, testGroupAgent.getFirstName());
    }

    @Test
    public void testGetLastName()
    {
        assertNull(testGroupAgent.getLastName());
        String testLastName = "testLastName";
        childGroupAgent1.setLastName(testLastName);
        assertEquals(testLastName, testGroupAgent.getLastName());
    }

    @Test
    public void testIsComponent()
    {
        assertFalse(testGroupAgent.isComponent("testComponent"));
        testGroupAgent.addAgent(testComponentAgent);
        assertTrue(testGroupAgent.isComponent("testComponent"));
    }

    @Test
    public void testIsGroup()
    {
        assertFalse(testGroupAgent.isGroup("testFirstName", "testLastName"));
        childGroupAgent1.setFirstName("testFirstName");
        childGroupAgent1.setLastName("testLastName");
        assertTrue(testGroupAgent.isGroup("testFirstName", "testLastName"));
    }

    @Test
    public void testIsGap()
    {
        assertFalse(testGroupAgent.isGap());
    }

    @Test
    public void testEquals()
    {
        GroupAgent testGroupAgent2 = new GroupAgent()
        {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0)
            {
                return TestHelper.getTestGroupContent();
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation()
            {
                return new FunctionsOnGroupAndElementImplementation(this, null);
            }
        };
        assertFalse(testGroupAgent.equals(testGroupAgent2));
        testGroupAgent2.setFirstName("testFirstName");
        testGroupAgent2.setLastName("testLastName");
        assertTrue(testGroupAgent.equals(testGroupAgent2));
    }

    @Test
    public void testAddAgent()
    {
        assertFalse(testGroupAgent.getChildren().contains(childGroupAgent1));
        testGroupAgent.addAgent(childGroupAgent1);
        assertTrue(testGroupAgent.getChildren().contains(childGroupAgent1));
    }

    @Test
    public void testGetChildren()
    {
        assertEquals(0, testGroupAgent.getNumberOfChildren());
        testGroupAgent.addAgent(childGroupAgent1);
        testGroupAgent.addAgent(childGroupAgent2);
        assertEquals(2, testGroupAgent.getNumberOfChildren());
        List<Agent> children = testGroupAgent.getChildren();
        assertTrue(children.contains(childGroupAgent1));
        assertTrue(children.contains(childGroupAgent2));
    }

    @Test
    public void testGetNumberOfChildren()
    {
        assertEquals(0, testGroupAgent.getNumberOfChildren());
        testGroupAgent.addAgent(childGroupAgent1);
        testGroupAgent.addAgent(childGroupAgent2);
        assertEquals(2, testGroupAgent.getNumberOfChildren());
    }

    @Test
    public void testGetComponent()
    {
        assertNull(testGroupAgent.getComponent("testComponent"));
        testGroupAgent.addAgent(testComponentAgent);
        assertEquals(testComponentAgent, testGroupAgent.getComponent("testComponent"));
    }

    @Test
    public void testGetGroup()
    {
        assertNull(testGroupAgent.getGroup("testFirstName", "testLastName"));
        childGroupAgent1.setFirstName("testFirstName");
        childGroupAgent1.setLastName("testLastName");
        assertEquals(childGroupAgent1, testGroupAgent.getGroup("testFirstName", "testLastName"));
    }

    @Test
    public void testFindDependingComponentByName()
    {
        assertNull(testGroupAgent.findDependingComponentByName("testComponent"));
        testGroupAgent.addAgent(testComponentAgent);
        assertEquals(testComponentAgent, testGroupAgent.findDependingComponentByName("testComponent"));
    }

    @Test
    public void testFindDependingGroupByNames()
    {
        assertNull(testGroupAgent.findDependingGroupByNames("testFirstName", "testLastName"));
        childGroupAgent1.setFirstName("testFirstName");
        childGroupAgent1.setLastName("testLastName");
        assertEquals(childGroupAgent1, testGroupAgent.findDependingGroupByNames("testFirstName", "testLastName"));
    }

    @Test
    public void testFindDependingParallelGroupByNames()
    {
        assertNull(testGroupAgent.findDependingParallelGroupByNames("testFirstName", "testLastName"));
        childGroupAgent1.setFirstName("testFirstName");
        childGroupAgent1.setLastName("testLastName");
        childGroupAgent1.setAgentType(AgentType.PARALLEL);
        assertEquals(childGroupAgent1, testGroupAgent.findDependingParallelGroupByNames("testFirstName", "testLastName"));
    }

    @Test
    public void testFindDependingSequentialGroupByNames()
    {
        assertNull(testGroupAgent.findDependingSequentialGroupByNames("testFirstName", "testLastName"));
        childGroupAgent1.setFirstName("testFirstName");
        childGroupAgent1.setLastName("testLastName");
        childGroupAgent1.setAgentType(AgentType.SEQUENTIAL);
        assertEquals(childGroupAgent1, testGroupAgent.findDependingSequentialGroupByNames("testFirstName", "testLastName"));
    }

    @Test
    public void testRemoveGapsFromParallelGroups()
    {
        testGroupAgent.removeGapsFromParallelGroups();
        assertTrue(testGroupAgent.getChildren().isEmpty());
        testGroupAgent.addAgent(childGroupAgent1);
        testGroupAgent.addAgent(childGroupAgent2);
        testGroupAgent.removeGapsFromParallelGroups();
        assertTrue(testGroupAgent.getChildren().isEmpty());
    }

    @Test
    public void testAddGapBefore()
    {
        testGroupAgent.addAgent(childGroupAgent1);
        testGroupAgent.addAgent(childGroupAgent2);
        List<Agent> children = new ArrayList<>(testGroupAgent.getChildren());
        GroupAgent expectedParent = testGroupAgent;
        int expectedIndex = testGroupAgent.getChildren().indexOf(childGroupAgent1);
        testGroupAgent.addGapBefore(testGapAgent, childGroupAgent1);
        List<Agent> actualChildren = testGroupAgent.getChildren();
        assertEquals(children.size(), actualChildren.size());
        for (int i = 0; i < children.size(); i++)
        {
            Agent actualChild = actualChildren.get(i);
            Agent expectedChild = children.get(i);
            if (i == expectedIndex)
            {
                assertEquals(testGapAgent, actualChild);
                assertEquals(childGroupAgent1, expectedParent.getChildren().get(i + 1));
            }
            else
            {
                assertEquals(expectedChild, actualChild);
            }
        }
    }

    @Test
    public void testAddGapAfter()
    {
        testGroupAgent.addAgent(childGroupAgent1);
        testGroupAgent.addAgent(childGroupAgent2);
        List<Agent> children = new ArrayList<>(testGroupAgent.getChildren());
        GroupAgent expectedParent = testGroupAgent;
        int expectedIndex = testGroupAgent.getChildren().indexOf(childGroupAgent2);
        testGroupAgent.addGapAfter(testGapAgent, childGroupAgent1);
        List<Agent> actualChildren = testGroupAgent.getChildren();
        assertEquals(children.size(), actualChildren.size());
        for (int i = 0; i < children.size(); i++)
        {
            Agent actualChild = actualChildren.get(i);
            Agent expectedChild = children.get(i);
            if (i == expectedIndex)
            {
                assertEquals(testGapAgent, actualChild);
                assertEquals(childGroupAgent1, expectedParent.getChildren().get(i));
            }
            else
            {
                assertEquals(expectedChild, actualChild);
            }
        }
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps()
    {
        testGroupAgent.addAgent(testComponentAgent);
        testGroupAgent.addAgent(testGapAgent);
        testGroupAgent.addAgent(childGroupAgent1);
        List<Agent> children = new ArrayList<>(testGroupAgent.getChildren());
        GroupAgent expectedParent = testGroupAgent;
        int expectedIndex = testGroupAgent.getChildren().indexOf(testGapAgent);
        Agent expectedGap = testGapAgent;
        testGroupAgent.replaceTemporaryGapsByRealGaps(TestUtils.getTestTemporaryGapsToGapsMap());
        List<Agent> actualChildren = testGroupAgent.getChildren();
        assertEquals(children.size(), actualChildren.size());
        for (int i = 0; i < children.size(); i++)
        {
            Agent actualChild = actualChildren.get(i);
            Agent expectedChild = children.get(i);
            if (i == expectedIndex)
            {
                if (expectedChild instanceof GapAgent)
                {
                    assertEquals(expectedGap, actualChild);
                }
                else
                {
                    fail("Expected a gap agent, but got " + actualChild.getClass().getName());
                }
            }
            else
            {
                assertEquals(expectedChild, actualChild);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPrecedingGapWithNullAgent()
    {
        testGroupAgent.addAgent(childGroupAgent1);
        FunctionsOnGroupAndElementImplementation functionsOnGroupAndElement = testGroupAgent.getFunctionsOnGroupImplemenation();
        functionsOnGroupAndElement.addPrecedingGap(null);
    }
}