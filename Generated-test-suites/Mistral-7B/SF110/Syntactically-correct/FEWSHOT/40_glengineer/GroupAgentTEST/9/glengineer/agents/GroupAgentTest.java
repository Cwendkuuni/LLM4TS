package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GroupAgentTest {

    @Test
    public void testAddAndGetChildren() {
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new GroupLayout.Group();
            }
        };

        Agent child1 = new DummyAgent();
        Agent child2 = new DummyAgent();

        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);

        List<Agent> children = groupAgent.getChildren();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));

        Agent retrievedChild1 = groupAgent.getComponent("child1");
        assertSame(child1, retrievedChild1);

        Agent retrievedChild2 = groupAgent.getGroup("groupName1", "groupName2");
        assertSame(child2, retrievedChild2);
    }

    @Test
    public void testGetFirstAndLastName() {
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new GroupLayout.Group();
            }

            @Override
            public String getFirstName() {
                return "FirstName";
            }

            @Override
            public String getLastName() {
                return "LastName";
            }
        };

        assertEquals("FirstName", groupAgent.getFirstName());
        assertEquals("LastName", groupAgent.getLastName());

        Agent child1 = new DummyAgent() {
            @Override
            public String getFirstName() {
                return "Child1FirstName";
            }

            @Override
            public String getLastName() {
                return "Child1LastName";
            }
        };

        Agent child2 = new DummyAgent() {
            @Override
            public String getFirstName() {
                return "Child2FirstName";
            }

            @Override
            public String getLastName() {
                return "Child2LastName";
            }
        };

        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);

        assertEquals("FirstName", groupAgent.getFirstName());
        assertEquals("LastName", groupAgent.getLastName());

        assertEquals("Child1FirstName", child1.getFirstName());
        assertEquals("Child1LastName", child1.getLastName());
        assertEquals("Child2FirstName", child2.getFirstName());
        assertEquals("Child2LastName", child2.getLastName());
    }

    @Test
    public void testIsGroup() {
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new GroupLayout.Group();
            }
        };

        Agent child1 = new DummyAgent();
        Agent child2 = new DummyAgent();

        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);

        assertFalse(groupAgent.isGroup(null, null));
        assertFalse(groupAgent.isGroup("", ""));
        assertFalse(groupAgent.isGroup("FirstName", ""));
        assertFalse(groupAgent.isGroup("", "LastName"));
        assertFalse(groupAgent.isGroup("FirstName", "LastName1"));
        assertFalse(groupAgent.isGroup("FirstName1", "LastName"));

        assertTrue(groupAgent.isGroup("FirstName", "LastName"));
        assertTrue(groupAgent.isGroup("FirstName", "LastName"));
        assertTrue(groupAgent.isGroup("FirstName", "LastName"));

        assertTrue(child1.isGroup(groupAgent.getFirstName(), groupAgent.getLastName()));
        assertTrue(child2.isGroup(groupAgent.getFirstName(), groupAgent.getLastName()));

        assertFalse(child1.isGroup("FirstName1", "LastName1"));
        assertFalse(child2.isGroup("FirstName1", "LastName1"));
    }

    @Test
    public void testEquals() {
        GroupAgent groupAgent1 = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new GroupLayout.Group();
            }
        };

        GroupAgent groupAgent2 = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new GroupLayout.Group();
            }
        };

        Agent child1 = new DummyAgent();
        Agent child2 = new DummyAgent();

        groupAgent1.addAgent(child1);
        groupAgent1.addAgent(child2);
        groupAgent2.addAgent(child1);
        groupAgent2.addAgent(child2);

        assertFalse(groupAgent1.equals(groupAgent2));
        assertFalse(groupAgent1.equals(child1));
        assertFalse(groupAgent1.equals(child2));
        assertFalse(groupAgent1.equals(null));

        GroupAgent groupAgent3 = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new GroupLayout.Group();
            }
        };

        groupAgent3.addAgent(child1);
        groupAgent3.addAgent(child2);

        assertTrue(groupAgent1.equals(groupAgent3));
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        GroupAgent groupAgent = new GroupAgent() {
            private List<Agent> gaps = new ArrayList<>();

            @Override
            public void removeGapsFromParallelGroups() {
                passTheCallToSubgroups();
                removeGapsFromParallelSubgroupsWithOtherContent();
                replaceParallelSubgroupsWithGapsByTheirSingleGaps();
                replaceContinuousSequencesOfGapsBySingleGaps();
                replaceGroupsWithSingleElementsByThatElements();
            }

            private void passTheCallToSubgroups() {
                for (Agent child : childrenAgents) {
                    if (child instanceof GroupAgent) {
                        ((GroupAgent) child).removeGapsFromParallelGroups();
                    }
                }
            }

            private void removeGapsFromParallelSubgroupsWithOtherContent() {
                for (Agent child : childrenAgents) {
                    if (child instanceof ParallelGroupAgent) {
                        ParallelGroupAgent parallel = (ParallelGroupAgent) child;
                        if (parallel.containsGapsOnly()) {
                            continue;
                        }
                        parallel.removeAllGaps();
                        gaps.addAll(parallel.getGaps());
                    }
                }
            }

            private void replaceParallelSubgroupsWithGapsByTheirSingleGaps() {
                ListIterator<Agent> li = childrenAgents.listIterator();
                while (li.hasNext()) {
                    Agent child = li.next();
                    if (child instanceof ParallelGroupAgent) {
                        ParallelGroupAgent parallel = (ParallelGroupAgent) child;
                        Agent gap = parallel.getGapsType();
                        if (gap == null) {
                            continue;
                        }
                        li.set(gap);
                    }
                }
            }

            private void replaceContinuousSequencesOfGapsBySingleGaps() {
                ListIterator<Agent> li = childrenAgents.listIterator();
                while (li.hasNext()) {
                    Agent child = li.next();
                    if (!child.isGap()) {
                        continue;
                    }
                    Agent currentGap = child;
                    while (li.hasNext()) {
                        Agent nextGap = li.next();
                        if (!nextGap.isGap()) {
                            break;
                        }
                        if (!nextGap.equals(currentGap)) {
                            throw new IllegalArgumentException("A sequence of gaps possessing different types was found in a group.");
                        }
                        li.remove();
                    }
                    gaps.remove(currentGap);
                }
            }

            private void replaceGroupsWithSingleElementsByThatElements() {
                ListIterator<Agent> li = childrenAgents.listIterator();
                while (li.hasNext()) {
                    Agent child = li.next();
                    if (child instanceof GroupAgent) {
                        GroupAgent nextSubgroup = (GroupAgent) child;
                        if (nextSubgroup.getNumberOfChildren() != 1) {
                            continue;
                        }
                        li.set(nextSubgroup.getChildren().get(0));
                    }
                }
            }
        };

        ParallelGroupAgent parallelGroup1 = new ParallelGroupAgent() {
            @Override
            public List<Agent> getGaps() {
                return Collections.singletonList(new GapAgent(10));
            }
        };

        ParallelGroupAgent parallelGroup2 = new ParallelGroupAgent() {
            @Override
            public List<Agent> getGaps() {
                return Collections.singletonList(new GapAgent(20));
            }
        };

        SequentialGroupAgent sequentialGroup1 = new SequentialGroupAgent() {
            @Override
            public List<Agent> getGaps() {
                return Collections.emptyList();
            }
        };

        GroupAgent child1 = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new GroupLayout.Group();
            }
        };

        GroupAgent child2 = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new GroupLayout.Group();
            }
        };

        GroupAgent child3 = new GroupAgent() {
            @Override
            protected GroupLayout.Group groupContent(GroupCreatorAndElementAdder creatorAndAdder) {
                return new GroupLayout.Group();
            }
        };

        parallelGroup1.addAgent(child1);
        parallelGroup1.addAgent(parallelGroup2);
        parallelGroup1.addAgent(child3);

        groupAgent.addAgent(parallelGroup1);

        groupAgent.removeGapsFromParallelGroups();

        assertEquals(3, groupAgent.getNumberOfChildren());
        assertFalse(parallelGroup1.containsGapsOnly());
        assertEquals(0, parallelGroup1.getNumberOfChildren());
        assertNull(parallelGroup1.getChildren().get(0));
        assertNull(parallelGroup1.getChildren().get(1));
        assertNull(parallelGroup1.getChildren().get(2));
        assertEquals(1, gaps.size());
        assertSame(parallelGroup2, gaps.get(0));
    }

    private static class DummyAgent extends Agent {
        @Override
        public String getFirstName() {
            return "DummyFirstName";
        }

        @Override
        public String getLastName() {
            return "DummyLastName";
        }
    }
}