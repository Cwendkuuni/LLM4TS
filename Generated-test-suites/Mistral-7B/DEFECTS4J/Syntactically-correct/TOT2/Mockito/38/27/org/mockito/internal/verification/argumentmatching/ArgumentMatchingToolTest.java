package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher m1 = argThat(containsString("Mockito"));
        Matcher m2 = argThat(containsString("ArgumentMatchingTool"));
        Object[] arguments = new Object[]{"Mockito", "ArgumentMatchingTool", 1, "Mockito"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expected = {2};
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersNull() {
        Object[] arguments = new Object[]{"Mockito", "ArgumentMatchingTool"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_argumentsNull() {
        List<Matcher> matchers = new ArrayList<>();

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersEmpty() {
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[]{"Mockito", "ArgumentMatchingTool"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Integer[] expected = new Integer[0];

        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_customMatcher() {
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[]{"Mockito", new CustomMatcher(), "ArgumentMatchingTool"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nullInArguments() {
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[]{"Mockito", "ArgumentMatchingTool", null};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentLengths() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher m1 = argThat(containsString("Mockito"));
        Matcher m2 = argThat(containsString("ArgumentMatchingTool"));
        Object[] arguments = new Object[]{"Mockito", "ArgumentMatchingTool", 1};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Integer[] expected = new Integer[0];

        Assert.assertArrayEquals(expected, actual);
    }
}