package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    @Test 
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1");
        Option expectedOption = new Option("option1", null, false, null, null);
        Option actualOption = optionBuilder.create();
        assertEquals("OptionBuilder withLongOpt test", expectedOption, actualOption);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option(null, "arg", true, null, null);
        Option actualOption = optionBuilder.create();
        assertEquals("OptionBuilder hasArg test", expectedOption, actualOption);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option expectedOption = new Option(null, "argName", false, null, null);
        Option actualOption = optionBuilder.create();
        assertEquals("OptionBuilder withArgName test", expectedOption, actualOption);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option(null, null, true, null, null);
        Option actualOption = optionBuilder.create();
        assertEquals("OptionBuilder isRequired test", expectedOption, actualOption);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option(null, null, false, null, ':');
        Option actualOption = optionBuilder.create();
        assertEquals("OptionBuilder withValueSeparator test", expectedOption, actualOption);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option(null, null, false, null, Option.UNLIMITED_VALUES);
        Option actualOption = optionBuilder.create();
        assertEquals("OptionBuilder hasArgs test", expectedOption, actualOption);
    }

    @Test
    public void testCreate() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option expectedOption = new Option(null, null, false, null, null);
        Option actualOption = optionBuilder.create();
        assertEquals("OptionBuilder create test", expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
        Option actualOption = optionBuilder.create();
        fail("OptionBuilder create with empty longopt test should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);
        Option actualOption = optionBuilder.create();
        fail("OptionBuilder create with null longopt test should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("invalid");
        Option actualOption = optionBuilder.create();
        fail("OptionBuilder create with invalid longopt test should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(new Object());
        Option actualOption = optionBuilder.create();
        fail("OptionBuilder create with invalid type test should throw IllegalArgumentException");
    }
}