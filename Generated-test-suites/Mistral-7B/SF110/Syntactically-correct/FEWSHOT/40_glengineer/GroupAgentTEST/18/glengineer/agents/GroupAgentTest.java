package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import glengineer.agents.GroupAgent;
import glengineer.agents.settings.*;
import java.util.*;
import java.util.LinkedList;
import glengineer.*;
import javax.swing.*;
import glengineer.agents.setters.*;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent group = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "John";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return null;
            }

            @Override
            public String getLastName() {
                return "Doe";
            }
        });
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Jane";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });

        assertEquals("John", group.getFirstName());
    }

    @Test
    public void testGetLastName() {
        GroupAgent group = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "";
            }

            @Override
            public String getLastName() {
                return "Doe";
            }
        });
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "John";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Jane";
            }

            @Override
            public String getLastName() {
                return "Doe";
            }
        });

        assertEquals("Doe", group.getLastName());
    }

    @Test
    public void testIsGroup() {
        GroupAgent group = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "John";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Jane";
            }

            @Override
            public String getLastName() {
                return "Doe";
            }
        });

        assertTrue(group.isGroup("John", "Doe"));
    }

    @Test
    public void testIsComponent() {
        GroupAgent group = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "";
            }

            @Override
            public String getLastName() {
                return "";
            }

            @Override
            public boolean isComponent(String componentName) {
                return componentName.equals("Component");
            }
        });

        assertFalse(group.isComponent(null));
        assertFalse(group.isComponent(""));
        assertFalse(group.isComponent("Component1"));
        assertTrue(group.isComponent("Component"));
    }

    @Test
    public void testAddAgent() {
        GroupAgent group = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        Agent agent = new Agent() {
            @Override
            public String getFirstName() {
                return "Agent1";
            }

            @Override
            public String getLastName() {
                return "";
            }
        };
        group.addAgent(agent);
        assertEquals(1, group.getNumberOfChildren());
        assertEquals(agent, group.getChildren().get(0));
    }

    @Test
    public void testGetChildren() {
        GroupAgent group = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        Agent agent1 = new Agent() {
            @Override
            public String getFirstName() {
                return "Agent1";
            }

            @Override
            public String getLastName() {
                return "";
            }
        };
        Agent agent2 = new Agent() {
            @Override
            public String getFirstName() {
                return "Agent2";
            }

            @Override
            public String getLastName() {
                return "";
            }
        };
        group.addAgent(agent1);
        group.addAgent(agent2);

        List<Agent> children = group.getChildren();
        assertEquals(2, children.size());
        assertEquals(agent1, children.get(0));
        assertEquals(agent2, children.get(1));
    }

    @Test
    public void testEquals() {
        GroupAgent group1 = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        GroupAgent group2 = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        GroupAgent group3 = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        group1.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Agent1";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });
        group1.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Agent2";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });
        group2.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Agent1";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });
        group2.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Agent3";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });
        group3.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Agent1";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });
        group3.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Agent2";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });

        assertFalse(group1.equals(null));
        assertFalse(group1.equals(group2));
        assertTrue(group1.equals(group3));
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        GroupAgent group = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        group.addAgent(new GapAgent(10));
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Agent1";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Agent2";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });
        group.addAgent(new GapAgent(20));
        group.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "Agent3";
            }

            @Override
            public String getLastName() {
                return "";
            }
        });

        group.removeGapsFromParallelGroups();

        assertEquals(2, group.getNumberOfChildren());
        assertTrue(group.getChildren().get(0) instanceof Agent);
        assertTrue(group.getChildren().get(1) instanceof Agent);
    }

    @Test
    public void testAddGapBefore() {
        GroupAgent group = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        Agent agent = new Agent() {
            @Override
            public String getFirstName() {
                return "Agent";
            }

            @Override
            public String getLastName() {
                return "";
            }
        };
        group.addAgent(agent);
        group.addGapBefore(new GapAgent(10), agent);

        assertEquals(2, group.getNumberOfChildren());
        assertEquals(agent, group.getChildren().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapBefore_AgentNotFound() {
        GroupAgent group = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        Agent agent = new Agent() {
            @Override
            public String getFirstName() {
                return "Agent";
            }

            @Override
            public String getLastName() {
                return "";
            }
        };
        group.addAgent(agent);
        group.addGapBefore(new GapAgent(10), new Agent());
    }
}