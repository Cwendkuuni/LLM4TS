package org.apache.commons.cli; // Change this to the correct package name for GroupAgent tests

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;
import glengineer.agents.setters.ComponentAgentSetter;
import glengineer.agents.setters.GroupAgentSetter;
import glengineer.agents.setters.TemporaryGapAgentSetter;
import glengineer.agents.utils.GroupLayout;
import glengineer.agents.utils.ParallelGroupLayout;
import glengineer.agents.utils.SequentialGroupLayout;
import glengineer.agents.utils.functions.FunctionsOnGroupAndElement;
import glengineer.agents.utils.functions.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.utils.functions.GroupCreatorAndElementAdder;
import glengineer.agents.utils.functions.FunctionsOnGroupLayout;
import glengineer.agents.utils.functions.FunctionsOnGroupLayoutImplementation;
import glengineer.agents.utils.functions.GroupLayoutEngineer;
import glengineer.agents.utils.functions.TemporaryGapsToGaps;
import glengineer.agents.utils.functions.TemporaryGapsToGapsImplementation;
import glengineer.exceptions.GroupAgentException;
import glengineer.utils.GapAgent;
import glengineer.utils.TemporaryGapAgent;

public class GroupAgentTest {

    @Test
    public void testGroupAgent() {
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new SequentialGroupLayout();
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return new FunctionsOnGroupAndElementImplementation(this, null);
            }
        };

        assertNull(groupAgent.getFirstName());
        assertNull(groupAgent.getLastName());
        assertEquals(0, groupAgent.getNumberOfChildren());
        assertFalse(groupAgent.isComponent("component"));
        assertFalse(groupAgent.isGroup("firstName", "lastName"));
        assertFalse(groupAgent.isGap());
        assertFalse(groupAgent.equals(null));

        // Test adding and removing agents
        Agent agent1 = new AgentSetter().createAgent("ComponentAgent1");
        groupAgent.addAgent(agent1);
        assertEquals(1, groupAgent.getNumberOfChildren());
        assertEquals(agent1, groupAgent.getChildren().get(0));
        groupAgent.removeAgent(agent1);
        assertEquals(0, groupAgent.getNumberOfChildren());

        // Test adding and getting group agents
        GroupAgent groupAgent1 = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new ParallelGroupLayout();
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return new FunctionsOnGroupAndElementImplementation(this, null);
            }
        };
        groupAgent.addAgent(groupAgent1);
        assertEquals(1, groupAgent.getNumberOfChildren());
        assertEquals(groupAgent1, groupAgent.getChildren().get(0));
        GroupAgent groupAgent2 = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new SequentialGroupLayout();
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return new FunctionsOnGroupAndElementImplementation(this, null);
            }
        };
        groupAgent1.addAgent(groupAgent2);
        assertEquals(1, groupAgent1.getNumberOfChildren());
        assertEquals(groupAgent2, groupAgent1.getChildren().get(0));
        assertEquals(1, groupAgent.getNumberOfChildren());
        assertEquals(groupAgent1, groupAgent.getChildren().get(0));
        assertEquals(1, groupAgent1.getNumberOfChildren());
        assertEquals(groupAgent2, groupAgent1.getChildren().get(0));

        // Test finding agents
        assertEquals(agent1, groupAgent.findDependingComponentByName("ComponentAgent1"));
        assertNull(groupAgent.findDependingGroupByNames("firstName", "lastName"));

        // Test removing gaps from parallel groups
        GroupAgent parallelGroupAgent = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new ParallelGroupLayout();
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return new FunctionsOnGroupAndElementImplementation(this, null);
            }
        };
        parallelGroupAgent.addAgent(new TemporaryGapAgentSetter().createAgent("TemporaryGap1"));
        parallelGroupAgent.addAgent(new TemporaryGapAgentSetter().createAgent("ComponentAgent1"));
        parallelGroupAgent.addAgent(new TemporaryGapAgentSetter().createAgent("TemporaryGap2"));
        parallelGroupAgent.removeGapsFromParallelGroups();
        assertEquals(1, parallelGroupAgent.getNumberOfChildren());
        assertEquals(new TemporaryGapAgentSetter().createAgent("ComponentAgent1"), parallelGroupAgent.getChildren().get(0));

        // Test replacing temporary gaps by real gaps
        TemporaryGapsToGaps gapsToGaps = new TemporaryGapsToGapsImplementation();
        groupAgent.replaceTemporaryGapsByRealGaps(gapsToGaps);
        assertEquals(2, groupAgent.getNumberOfChildren());
        assertEquals(groupAgent1, groupAgent.getChildren().get(0));
        assertEquals(groupAgent2, groupAgent.getChildren().get(1));

        // Test adding gaps
        gapBefore(groupAgent, groupAgent1, new GapAgent(50));
        gapAfter(groupAgent, groupAgent1, new GapAgent(50));
        gapBefore(groupAgent1, groupAgent2, new GapAgent(50));
        gapAfter(groupAgent1, groupAgent2, new GapAgent(50));

        // Test functions on group and element
        FunctionsOnGroupLayout functionsOnGroupLayout = new FunctionsOnGroupLayoutImplementation();
        FunctionsOnGroup groupFunctions = groupAgent.getFunctionsOnGroupImplemenation();
        groupFunctions.addPrecedingGap(50);
        groupFunctions.addFollowingGap(50);
        FunctionsOnGroupLayout groupLayoutFunction = functionsOnGroupLayout.getFunctionsOnGroup(groupAgent);
        assertEquals(50, groupLayoutFunction.getPrecedingGapSize());
        assertEquals(50, groupLayoutFunction.getFollowingGapSize());
    }

    private void gapBefore(GroupAgent parent, Agent child, GapAgent gap) {
        parent.addGapBefore(gap, child);
    }

    private void gapAfter(GroupAgent parent, Agent child, GapAgent gap) {
        parent.addGapAfter(gap, child);
    }
}