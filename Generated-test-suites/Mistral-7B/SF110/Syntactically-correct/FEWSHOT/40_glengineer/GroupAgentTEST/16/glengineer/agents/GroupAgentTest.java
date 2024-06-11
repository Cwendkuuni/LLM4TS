package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class GroupAgentTest {

    @Test
    public void testEmptyGroup() {
        final GroupAgent group = new GroupAgent();
        assertNull(group.getFirstName());
        assertNull(group.getLastName());
        assertTrue(group.isGroup(null, null));
        assertTrue(group.isEmpty());
        assertEquals(0, group.getNumberOfChildren());
        assertNull(group.getChildren());
    }

    @Test
    public void testSingleChildGroup() {
        final Agent child = new DummyAgent("child");
        final GroupAgent group = new GroupAgent();
        group.addAgent(child);

        assertEquals(child.getFirstName(), group.getFirstName());
        assertEquals(child.getLastName(), group.getLastName());
        assertTrue(group.isGroup(child.getFirstName(), child.getLastName()));
        assertFalse(group.isEmpty());
        assertEquals(1, group.getNumberOfChildren());
        assertEquals(Collections.singletonList(child), group.getChildren());
    }

    @Test
    public void testMultipleChildrenGroup() {
        final Agent child1 = new DummyAgent("child1");
        final Agent child2 = new DummyAgent("child2");
        final GroupAgent group = new GroupAgent();
        group.addAgent(child1);
        group.addAgent(child2);

        assertEquals(child1.getFirstName(), group.getFirstName());
        assertEquals(child2.getLastName(), group.getLastName());
        assertTrue(group.isGroup(child1.getFirstName(), child2.getLastName()));
        assertFalse(group.isEmpty());
        assertEquals(2, group.getNumberOfChildren());
        assertEquals(Arrays.asList(child1, child2), group.getChildren());
    }

    @Test
    public void testGetComponent() {
        final ComponentAgent component = new DummyComponentAgent("component");
        final Agent group = new GroupAgent();
        group.addAgent(component);

        assertEquals(component, group.getComponent("component"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponentNotFound() {
        final Agent group = new GroupAgent();
        group.getComponent("component");
    }

    @Test
    public void testGetGroup() {
        final Agent child1 = new DummyAgent("child1");
        final Agent child2 = new DummyAgent("child2");
        final GroupAgent parent = new GroupAgent();
        parent.addAgent(child1);
        parent.addAgent(child2);

        final GroupAgent group = new GroupAgent();
        group.addAgent(parent);

        assertEquals(parent, group.getGroup("child1", "child2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGroupNotFound() {
        final Agent parent = new GroupAgent();
        final Agent group = new GroupAgent();
        group.addAgent(parent);
        group.getGroup("child1", "child2");
    }

    @Test
    public void testFindDependingComponentByName() {
        final ComponentAgent component1 = new DummyComponentAgent("component1");
        final ComponentAgent component2 = new DummyComponentAgent("component2");
        final Agent group = new GroupAgent();
        group.addAgent(component1);
        group.addAgent(component2);

        assertEquals(component1, group.findDependingComponentByName("component1"));
        assertEquals(component2, group.findDependingComponentByName("component2"));
        assertNull(group.findDependingComponentByName("nonExistingComponent"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDependingGroupByNamesNotFound() {
        final Agent child1 = new DummyAgent("child1");
        final Agent child2 = new DummyAgent("child2");
        final Agent parent = new GroupAgent();
        parent.addAgent(child1);
        parent.addAgent(child2);
        final Agent group = new GroupAgent();
        group.addAgent(parent);

        group.findDependingGroupByNames("child1", "child3");
    }

    @Test
    public void testFindDependingParallelGroupByNames() {
        final ParallelGroupAgent parallelGroup1 = new ParallelGroupAgent();
        final ParallelGroupAgent parallelGroup2 = new ParallelGroupAgent();
        final Agent group = new GroupAgent();
        group.addAgent(parallelGroup1);
        group.addAgent(parallelGroup2);

        assertEquals(parallelGroup1, group.findDependingParallelGroupByNames("parallelGroup1"));
        assertEquals(parallelGroup2, group.findDependingParallelGroupByNames("parallelGroup2"));
        assertNull(group.findDependingParallelGroupByNames("nonExistingParallelGroup"));
    }

    @Test
    public void testFindDependingSequentialGroupByNames() {
        final SequentialGroupAgent sequentialGroup1 = new SequentialGroupAgent();
        final SequentialGroupAgent sequentialGroup2 = new SequentialGroupAgent();
        final Agent group = new GroupAgent();
        group.addAgent(sequentialGroup1);
        group.addAgent(sequentialGroup2);

        assertEquals(sequentialGroup1, group.findDependingSequentialGroupByNames("sequentialGroup1"));
        assertEquals(sequentialGroup2, group.findDependingSequentialGroupByNames("sequentialGroup2"));
        assertNull(group.findDependingSequentialGroupByNames("nonExistingSequentialGroup"));
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        final SequentialGroupAgent seq1 = new SequentialGroupAgent();
        final SequentialGroupAgent seq2 = new SequentialGroupAgent();
        final ParallelGroupAgent par1 = new ParallelGroupAgent();
        final ParallelGroupAgent par2 = new ParallelGroupAgent();
        final Agent group = new GroupAgent();
        group.addAgent(seq1);
        group.addAgent(seq2);
        group.addAgent(par1);
        group.addAgent(par2);

        par1.addAgent(new GapAgent());
        par1.addAgent(new ComponentAgent("comp1"));
        par1.addAgent(new GapAgent());
        par1.addAgent(new ComponentAgent("comp2"));
        par1.addAgent(new GapAgent());

        par2.addAgent(new ComponentAgent("comp3"));
        par2.addAgent(new GapAgent());
        par2.addAgent(new ComponentAgent("comp4"));
        par2.addAgent(new GapAgent());

        group.removeGapsFromParallelGroups();

        assertEquals(2, par1.getNumberOfChildren());
        assertEquals(2, par2.getNumberOfChildren());

        assertTrue(par1.contains(new ComponentAgent("comp1")));
        assertTrue(par1.contains(new ComponentAgent("comp2")));
        assertFalse(par1.contains(new GapAgent()));

        assertTrue(par2.contains(new ComponentAgent("comp3")));
        assertTrue(par2.contains(new ComponentAgent("comp4")));
        assertFalse(par2.contains(new GapAgent()));
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        final TemporaryGapAgent tempGap1 = new TemporaryGapAgent();
        final TemporaryGapAgent tempGap2 = new TemporaryGapAgent();
        final Agent group = new GroupAgent();
        group.addAgent(tempGap1);
        group.addAgent(tempGap2);

        final GroupAgent realGap1 = new GapAgent();
        final GroupAgent realGap2 = new GapAgent();

        group.replaceTemporaryGapsByRealGaps(Collections.singletonMap(tempGap1, realGap1));
        group.replaceTemporaryGapsByRealGaps(Collections.singletonMap(tempGap2, realGap2));

        assertEquals(realGap1, group.getChildren().get(0));
        assertEquals(realGap2, group.getChildren().get(1));
    }

    @Test
    public void testAddGapBefore() {
        final Agent group = new GroupAgent();
        final GapAgent gap = new GapAgent();
        final Agent element = new ComponentAgent("element");

        group.addAgent(element);
        group.addGapBefore(gap, element);

        assertEquals(1, group.getNumberOfChildren());
        assertEquals(element, group.getChildren().get(0));
        assertEquals(gap, group.getChildren().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapBeforeNotFound() {
        final Agent group = new GroupAgent();
        final GapAgent gap = new GapAgent();
        final Agent element = new ComponentAgent("element");

        group.addAgent(element);
        group.addGapBefore(gap, null);
    }

    @Test
    public void testAddGapAfter() {
        final Agent group = new GroupAgent();
        final GapAgent gap = new GapAgent();
        final Agent element = new ComponentAgent("element");

        group.addAgent(element);
        group.addGapAfter(gap, element);

        assertEquals(1, group.getNumberOfChildren());
        assertEquals(element, group.getChildren().get(0));
        assertEquals(gap, group.getChildren().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapAfterNotFound() {
        final Agent group = new GroupAgent();
        final GapAgent gap = new GapAgent();
        final Agent element = new ComponentAgent("element");

        group.addAgent(element);
        group.addGapAfter(gap, null);
    }

    private static class DummyAgent {
        private final String name;

        public DummyAgent(final String name) {
            this.name = name;
        }

        public String getFirstName() {
            return name.substring(0, name.indexOf(' '));
        }

        public String getLastName() {
            return name.substring(name.indexOf(' ') + 1);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final DummyAgent dummyAgent = (DummyAgent) o;
            return name.equals(dummyAgent.name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class DummyComponentAgent extends DummyAgent implements ComponentAgent {
        public DummyComponentAgent(final String name) {
            super(name);
        }

        @Override
        public boolean isComponent(final String name) {
            return true;
        }
    }
}