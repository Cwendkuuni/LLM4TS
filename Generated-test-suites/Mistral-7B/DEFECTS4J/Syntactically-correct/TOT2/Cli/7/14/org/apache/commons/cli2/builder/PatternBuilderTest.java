package org.junit.jupiter.api.impl.test.util.PatternBuilderTest;

import org.apache.commons.cli2.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateWithEmptyPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("");
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testCreateWithValidPatternNoArguments() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("OPTION:%");
        Option option = patternBuilder.create();
        assertEquals("OPTION", option.getLongOpt());
        assertNull(option.getArgument());
    }

    @Test
    public void testCreateWithValidPatternOneRequiredArgument() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("OPTION:ARG%");
        Option option = patternBuilder.create();
        assertEquals("OPTION", option.getLongOpt());
        assertEquals("ARG", option.getOptArgName());
    }

    @Test
    public void testCreateWithValidPatternOneOptionalArgument() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("OPTION:ARG%:ARG2%");
        Option option = patternBuilder.create();
        assertEquals("OPTION", option.getLongOpt());
        assertEquals("ARG", option.getOptArgName(0));
        assertEquals("ARG2", option.getOptArgName(1));
        assertNull(option.getOptArgValue());
    }

    @Test
    public void testCreateWithMixedPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("OPTION:ARG1%:ARG2%:ARG3%");
        Option option = patternBuilder.create();
        assertEquals("OPTION", option.getLongOpt());
        assertEquals("ARG1", option.getOptArgName(0));
        assertEquals("ARG2", option.getOptArgName(1));
        assertEquals("ARG3", option.getOptArgName(2));
        assertNull(option.getOptArgValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("OPTION:ARG%ARG");
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("OPTION:ARG%");
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testCreateOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Argument argument = patternBuilder.createOption('A', false, null);
        assertNotNull(argument);
        assertEquals("A", argument.getName());
        assertFalse(argument.isRequired());
        assertNull(argument.getValue());
    }

    @Test
    public void testCreateOptionWithArgument() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Argument argument = patternBuilder.createOption('B', true, "ARG");
        assertNotNull(argument);
        assertEquals("B", argument.getName());
        assertTrue(argument.isRequired());
        assertEquals("ARG", argument.getOptArgName());
        assertNull(argument.getValue());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("OPTION:ARG%");
        assertEquals(1, patternBuilder.options.size());
        Option option = (Option)patternBuilder.options.iterator().next();
        assertEquals("OPTION", option.getLongOpt());
        assertNull(option.getArgument());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithPatternInvalidPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("OPTION:ARG%ARG");
    }
}