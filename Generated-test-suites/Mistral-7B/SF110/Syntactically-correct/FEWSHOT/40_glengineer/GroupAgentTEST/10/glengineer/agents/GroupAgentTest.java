package org.apache.commons.cli; // Replace with the package name of GroupAgent

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GroupAgentTest {

 static class TestGroupAgent extends GroupAgent {
 @Override
 public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
 throw new UnsupportedOperationException();
 }

 @Override
 public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
 throw new UnsupportedOperationException();
 }
 }

 @Test
 public void testGetFirstName() {
 TestGroupAgent agent = new TestGroupAgent();
 agent.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 String firstName = agent.getFirstName();
 assertEquals("John", firstName);
 }

 @Test
 public void testGetLastName() {
 TestGroupAgent agent = new TestGroupAgent();
 agent.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 String lastName = agent.getLastName();
 assertEquals("Doe", lastName);
 }

 @Test
 public void testIsComponent() {
 TestGroupAgent agent = new TestGroupAgent();
 assertFalse(agent.isComponent("componentName"));
 }

 @Test
 public void testIsGroup() {
 TestGroupAgent agent = new TestGroupAgent();
 agent.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 assertFalse(agent.isGroup("John", "Doe2"));

 TestGroupAgent groupAgent = new TestGroupAgent();
 groupAgent.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 groupAgent.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 assertTrue(groupAgent.isGroup("John", "Doe"));
 }

 @Test
 public void testIsGap() {
 TestGroupAgent agent = new TestGroupAgent();
 assertFalse(agent.isGap());
 }

 @Test
 public void testEquals() {
 TestGroupAgent groupAgent1 = new TestGroupAgent();
 TestGroupAgent groupAgent2 = new TestGroupAgent();
 groupAgent1.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 groupAgent2.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 TestGroupAgent groupAgent3 = new TestGroupAgent();
 groupAgent3.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John2";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 assertTrue(groupAgent1.equals(groupAgent2));
 assertFalse(groupAgent1.equals(groupAgent3));
 }

 @Test
 public void testAddAgent() {
 TestGroupAgent agent = new TestGroupAgent();
 assertFalse(agent.getChildren().isEmpty());
 agent.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 List<Agent> children = agent.getChildren();
 assertEquals(2, children.size());
 }

 @Test
 public void testGetChildren() {
 TestGroupAgent agent = new TestGroupAgent();
 agent.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 List<Agent> children = agent.getChildren();
 assertEquals(1, children.size());
 }

 @Test
 public void testGetNumberOfChildren() {
 TestGroupAgent agent = new TestGroupAgent();
 agent.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 agent.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John2";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 assertEquals(2, agent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 TestGroupAgent agent = new TestGroupAgent();
 ComponentAgent component = new ComponentAgent() {
 @Override
 public String getName() {
 return "componentName";
 }
 };
 agent.addAgent(component);
 assertEquals(component, agent.getComponent("componentName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetComponentWithInvalidName() {
 TestGroupAgent agent = new TestGroupAgent();
 assertNull(agent.getComponent("invalidName"));
 }

 @Test
 public void testGetGroup() {
 TestGroupAgent agent = new TestGroupAgent();
 TestGroupAgent group = new TestGroupAgent();
 TestGroupAgent group2 = new TestGroupAgent();
 group.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 group2.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 agent.addAgent(group);
 agent.addAgent(group2);
 assertEquals(group, agent.getGroup("John", "Doe"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGroupWithInvalidNames() {
 TestGroupAgent agent = new TestGroupAgent();
 TestGroupAgent group = new TestGroupAgent();
 TestGroupAgent group2 = new TestGroupAgent();
 group.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 group2.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe2";
 }
 });
 agent.addAgent(group);
 agent.addAgent(group2);
 assertNull(agent.getGroup("John", "Doe2"));
 }

 @Test
 public void testFindDependingComponentByName() {
 TestGroupAgent agent = new TestGroupAgent();
 ComponentAgent component = new ComponentAgent() {
 @Override
 public String getName() {
 return "componentName";
 }
 };
 agent.addAgent(component);
 ComponentAgent result = agent.findDependingComponentByName("componentName");
 assertEquals(component, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingComponentByNameWithInvalidName() {
 TestGroupAgent agent = new TestGroupAgent();
 ComponentAgent component = new ComponentAgent() {
 @Override
 public String getName() {
 return "componentName";
 }
 };
 agent.addAgent(component);
 assertNull(agent.findDependingComponentByName("invalidName"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 TestGroupAgent agent = new TestGroupAgent();
 TestGroupAgent group = new TestGroupAgent();
 TestGroupAgent group2 = new TestGroupAgent();
 group.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 group2.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 agent.addAgent(group);
 agent.addAgent(group2);
 GroupAgent result = agent.findDependingGroupByNames("John", "Doe");
 assertEquals(group, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingGroupByNamesWithInvalidNames() {
 TestGroupAgent agent = new TestGroupAgent();
 TestGroupAgent group = new TestGroupAgent();
 TestGroupAgent group2 = new TestGroupAgent();
 group.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe";
 }
 });
 group2.addAgent(new Agent() {
 @Override
 public String getFirstName() {
 return "John";
 }

 @Override
 public String getLastName() {
 return "Doe2";
 }
 });
 agent.addAgent(group);
 agent.addAgent(group2);
 assertNull(agent.findDependingGroupByNames("John", "Doe2"));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 TestGroupAgent agent = new TestGroupAgent();
 ParallelGroupAgent parallelGroup = new ParallelGroupAgent();
 TestGroupAgent group = new TestGroupAgent();
 TestGroupAgent group2 = new TestGroupAgent();
 parallelGroup.addAgent(group);
 parallelGroup.addAgent(group2);
 agent.addAgent(parallelGroup);
 ParallelGroupAgent result = agent.findDependingParallelGroupByNames("John", "Doe");
 assertEquals(group, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingParallelGroupByNamesWithInvalidNames() {
 TestGroupAgent agent = new TestGroupAgent();
 ParallelGroupAgent parallelGroup = new ParallelGroupAgent();
 TestGroupAgent group = new TestGroupAgent();
 TestGroupAgent group2 = new TestGroupAgent();
 parallelGroup.addAgent(group);
 parallelGroup.addAgent(group2);
 agent.addAgent(parallelGroup);
 assertNull(agent.findDependingParallelGroupByNames("John2", "Doe"));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 TestGroupAgent agent = new TestGroupAgent();
 SequentialGroupAgent sequentialGroup = new SequentialGroupAgent();
 TestGroupAgent group = new TestGroupAgent();
 TestGroupAgent group2 = new TestGroupAgent();
 sequentialGroup.addAgent(group);
 sequentialGroup.addAgent(group2);
 agent.addAgent(sequentialGroup);
 SequentialGroupAgent result = agent.findDependingSequentialGroupByNames("John", "Doe");
 assertEquals(group, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDependingSequentialGroupByNamesWithInvalidNames() {
 TestGroupAgent agent = new TestGroupAgent();
 SequentialGroupAgent sequentialGroup = new SequentialGroupAgent();
 TestGroupAgent group = new TestGroupAgent();
 TestGroupAgent group2 = new TestGroupAgent();
 sequentialGroup.addAgent(group);
 sequentialGroup.addAgent(group2);
 agent.addAgent(sequentialGroup);
 assertNull(agent.findDependingSequentialGroupByNames("John2", "Doe"));
 }
}