package glengineer.agents.tests;

import org.junit.Assert;
import org.junit.Test;

import glengineer.agents.*;
import java.util.*;

public class GroupAgentTest {
    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent();
        Agent child1 = new DummyAgent("John", "Doe");
        groupAgent.addAgent(child1);
        String expected = "John";
        String result = groupAgent.getFirstName();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetFirstName_emptyList() {
        GroupAgent groupAgent = new GroupAgent();
        String expected = null;
        String result = groupAgent.getFirstName();
        Assert.assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFirstName_nullChild() {
        GroupAgent groupAgent = new GroupAgent();
        Agent nullChild = null;
        groupAgent.addAgent(nullChild);
        groupAgent.getFirstName();
    }

    @Test
    public void testGetLastName() {
        GroupAgent groupAgent = new GroupAgent();
        Agent child1 = new DummyAgent("John", "Doe");
        groupAgent.addAgent(child1);
        String expected = "Doe";
        String result = groupAgent.getLastName();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetLastName_emptyList() {
        GroupAgent groupAgent = new GroupAgent();
        String expected = null;
        String result = groupAgent.getLastName();
        Assert.assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLastName_nullChild() {
        GroupAgent groupAgent = new GroupAgent();
        Agent nullChild = null;
        groupAgent.addAgent(nullChild);
        groupAgent.getLastName();
    }

    @Test
    public void testAddAgent() {
        GroupAgent groupAgent = new GroupAgent();
        Agent child1 = new DummyAgent("John", "Doe");
        groupAgent.addAgent(child1);
        List<Agent> expectedChildren = Arrays.asList(child1);
        List<Agent> resultChildren = groupAgent.getChildren();
        Assert.assertEquals(expectedChildren, resultChildren);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAgent_nullAgent() {
        GroupAgent groupAgent = new GroupAgent();
        Agent nullAgent = null;
        groupAgent.addAgent(nullAgent);
    }

    // Add more test cases for other methods

    @Test
    public void testIsGroup() {
        GroupAgent groupAgent = new GroupAgent();
        Agent groupChild = new DummyAgent("John", "Doe");
        groupAgent.addAgent(groupChild);
        boolean expected = true;
        boolean result = groupAgent.isGroup(groupChild.getFirstName(), groupChild.getLastName());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsGroup_differentNames() {
        GroupAgent groupAgent = new GroupAgent();
        Agent child1 = new DummyAgent("John", "Doe");
        Agent child2 = new DummyAgent("Mike", "Smith");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        boolean expected = false;
        boolean result = groupAgent.isGroup("John", "Smith");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsGroup_emptyList() {
        GroupAgent groupAgent = new GroupAgent();
        boolean expected = false;
        boolean result = groupAgent.isGroup("John", "Doe");
        Assert.assertEquals(expected, result);
    }

    // Add more test cases for other methods as needed
}