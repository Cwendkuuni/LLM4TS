package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import glengineer.agents.GroupAgent;
import glengineer.agents.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.AgentSetterFactory;
import glengineer.agents.settings.FirstLastNameSetting;
import glengineer.agents.settings.FirstNameSetting;
import glengineer.agents.settings.LastNameSetting;
import glengineer.agents.settings.NameSetting;
import glengineer.agents.settings.Setting;
import glengineer.exceptions.IllegalArgumentException;
import glengineer.components.ComponentAgent;
import glengineer.components.GapAgent;
import glengineer.components.TemporaryGapAgent;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        assertEquals(firstName, groupAgent.getFirstName());
    }

    @Test
    public void testGetLastName() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        assertEquals(lastName, groupAgent.getLastName());
    }

    @Test
    public void testIsComponent() {
        final GroupAgent groupAgent = new GroupAgent() {
            @Override
            public ComponentAgent findDependingComponentByName(String name) {
                return null;
            }
        };
        assertFalse(groupAgent.isComponent("ComponentName"));
    }

    @Test
    public void testIsGroup() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        assertTrue(groupAgent.isGroup(firstName, lastName));
    }

    @Test
    public void testIsGap() {
        final GroupAgent groupAgent = new GroupAgent() {
            @Override
            public GapAgent getGapsType() {
                return null;
            }
        };
        assertFalse(groupAgent.isGap());
    }

    @Test
    public void testEquals() {
        final String firstName1 = "John";
        final String lastName1 = "Doe";
        final String firstName2 = "John";
        final String lastName2 = "Doe";
        final GroupAgent groupAgent1 = createGroupAgent(firstName1, lastName1);
        final GroupAgent groupAgent2 = createGroupAgent(firstName2, lastName2);
        assertTrue(groupAgent1.equals(groupAgent2));
    }

    @Test
    public void testAddAgent() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = new GroupAgent() {
            @Override
            public String getFirstName() {
                return firstName;
            }
            @Override
            public String getLastName() {
                return lastName;
            }
        };
        final AgentSetter agentSetter = AgentSetterFactory.createAgentSetter();
        final Agent childAgent = agentSetter.createAgent();
        groupAgent.addAgent(childAgent);
        assertEquals(1, groupAgent.getNumberOfChildren());
    }

    @Test
    public void testGetChildren() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final List<Agent> children = groupAgent.getChildren();
        assertEquals(0, children.size());
    }

    @Test
    public void testGetNumberOfChildren() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        assertEquals(0, groupAgent.getNumberOfChildren());
    }

    @Test
    public void testGetComponent() {
        final String componentName = "ComponentName";
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final ComponentAgent componentAgent = new ComponentAgent();
        groupAgent.addAgent(componentAgent);
        assertEquals(componentAgent, groupAgent.getComponent(componentName));
    }

    @Test
    public void testGetGroup() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final GroupAgent groupAgent2 = new GroupAgent() {
            @Override
            public String getFirstName() {
                return firstName;
            }
            @Override
            public String getLastName() {
                return lastName;
            }
        };
        groupAgent.addAgent(groupAgent2);
        assertEquals(groupAgent2, groupAgent.getGroup(firstName, lastName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAgent() {
        final GroupAgent groupAgent = new GroupAgent();
        groupAgent.addAgent(null);
    }

    private GroupAgent createGroupAgent(String firstName, String lastName) {
        final AgentSetter agentSetter = AgentSetterFactory.createAgentSetter();
        final AgentSetter firstNameSetter = AgentSetterFactory.createAgentSetter(NameSetting.class);
        final AgentSetter lastNameSetter = AgentSetterFactory.createAgentSetter(NameSetting.class);
        final Settings settings = new Settings();
        final GroupAgent groupAgent = new GroupAgent() {
            @Override
            public String getFirstName() {
                return firstName;
            }
            @Override
            public String getLastName() {
                return lastName;
            }
            @Override
            public Settings getSettings() {
                return settings;
            }
        };
        firstNameSetter.setSetting(new FirstNameSetting(firstName), groupAgent);
        lastNameSetter.setSetting(new LastNameSetting(lastName), groupAgent);
        agentSetter.setSetting(groupAgent);
        return groupAgent;
    }

    @Test
    public void testFindDependingComponentByName() {
        final String componentName = "ComponentName";
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final ComponentAgent componentAgent1 = new ComponentAgent();
        final ComponentAgent componentAgent2 = new ComponentAgent();
        groupAgent.addAgent(componentAgent1);
        groupAgent.addAgent(componentAgent2);
        assertEquals(componentAgent1, groupAgent.findDependingComponentByName(componentName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingComponentByNameNotFound() {
        final String componentName = "ComponentName";
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        groupAgent.findDependingComponentByName(componentName);
    }

    @Test
    public void testFindDependingGroupByNames() {
        final String firstName = "John";
        final String lastName = "Doe";
        final String firstName2 = "John2";
        final String lastName2 = "Doe2";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final GroupAgent groupAgent2 = new GroupAgent() {
            @Override
            public String getFirstName() {
                return firstName2;
            }
            @Override
            public String getLastName() {
                return lastName2;
            }
        };
        groupAgent.addAgent(groupAgent2);
        assertEquals(groupAgent2, groupAgent.findDependingGroupByNames(firstName2, lastName2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingGroupByNamesNotFound() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        groupAgent.findDependingGroupByNames(firstName, lastName);
    }

    @Test
    public void testFindDependingParallelGroupByNames() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final SequentialGroupAgent sequentialGroupAgent = new SequentialGroupAgent();
        final ParallelGroupAgent parallelGroupAgent = new ParallelGroupAgent();
        groupAgent.addAgent(sequentialGroupAgent);
        groupAgent.addAgent(parallelGroupAgent);
        assertEquals(parallelGroupAgent, groupAgent.findDependingParallelGroupByNames(firstName, lastName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingParallelGroupByNamesNotFound() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        groupAgent.findDependingParallelGroupByNames(firstName, lastName);
    }

    @Test
    public void testFindDependingSequentialGroupByNames() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final SequentialGroupAgent sequentialGroupAgent = new SequentialGroupAgent();
        final ParallelGroupAgent parallelGroupAgent = new ParallelGroupAgent();
        groupAgent.addAgent(sequentialGroupAgent);
        groupAgent.addAgent(parallelGroupAgent);
        assertEquals(sequentialGroupAgent, groupAgent.findDependingSequentialGroupByNames(firstName, lastName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingSequentialGroupByNamesNotFound() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        groupAgent.findDependingSequentialGroupByNames(firstName, lastName);
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final ParallelGroupAgent groupAgent1 = new ParallelGroupAgent();
        final ParallelGroupAgent groupAgent2 = new ParallelGroupAgent();
        final GapAgent gapAgent1 = new GapAgent();
        final GapAgent gapAgent2 = new GapAgent();
        final TemporaryGapAgent temporaryGapAgent = new TemporaryGapAgent();
        groupAgent.addAgent(groupAgent1);
        groupAgent.addAgent(groupAgent2);
        groupAgent1.addAgent(gapAgent1);
        groupAgent1.addAgent(temporaryGapAgent);
        groupAgent2.addAgent(groupAgent1);
        groupAgent2.addAgent(gapAgent2);
        groupAgent.removeGapsFromParallelGroups();
        assertEquals(1, groupAgent.getNumberOfChildren());
        assertEquals(1, groupAgent.getChildren().size());
        assertEquals(groupAgent1, groupAgent.getChildren().get(0));
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final ParallelGroupAgent parallelGroupAgent = new ParallelGroupAgent();
        final TemporaryGapAgent temporaryGapAgent = new TemporaryGapAgent();
        groupAgent.addAgent(parallelGroupAgent);
        parallelGroupAgent.addAgent(temporaryGapAgent);
        final GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
        map.put(temporaryGapAgent, new GapAgent(10));
        groupAgent.replaceTemporaryGapsByRealGaps(map);
        assertEquals(1, groupAgent.getNumberOfChildren());
        assertEquals(1, groupAgent.getChildren().size());
        assertEquals(parallelGroupAgent, groupAgent.getChildren().get(0));
        assertEquals(1, parallelGroupAgent.getNumberOfChildren());
        assertEquals(1, parallelGroupAgent.getChildren().size());
        assertEquals(temporaryGapAgent, parallelGroupAgent.getChildren().get(0));
        assertEquals(new GapAgent(10), parallelGroupAgent.getChildren().get(0));
    }

    @Test
    public void testAddGapBefore() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final GapAgent gapAgent = new GapAgent();
        final Agent element = new ComponentAgent();
        groupAgent.addAgent(element);
        groupAgent.addGapBefore(gapAgent, element);
        assertEquals(2, groupAgent.getNumberOfChildren());
        assertEquals(2, groupAgent.getChildren().size());
        assertEquals(element, groupAgent.getChildren().get(0));
        assertEquals(gapAgent, groupAgent.getChildren().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapBeforeInvalidElement() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final GapAgent gapAgent = new GapAgent();
        groupAgent.addGapBefore(gapAgent, null);
    }

    @Test
    public void testAddGapAfter() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final GapAgent gapAgent = new GapAgent();
        final Agent element = new ComponentAgent();
        groupAgent.addAgent(element);
        groupAgent.addGapAfter(gapAgent, element);
        assertEquals(2, groupAgent.getNumberOfChildren());
        assertEquals(2, groupAgent.getChildren().size());
        assertEquals(element, groupAgent.getChildren().get(0));
        assertEquals(gapAgent, groupAgent.getChildren().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapAfterInvalidElement() {
        final String firstName = "John";
        final String lastName = "Doe";
        final GroupAgent groupAgent = createGroupAgent(firstName, lastName);
        final GapAgent gapAgent = new GapAgent();
        groupAgent.addGapAfter(gapAgent, null);
    }
}