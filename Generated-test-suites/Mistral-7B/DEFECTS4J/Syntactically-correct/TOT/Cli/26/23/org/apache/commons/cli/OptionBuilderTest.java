package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testOptionWithRequiredArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1")
                .isRequired()
                .hasArg();
        Option option = optionBuilder.create('o');

        // Test with valid argument
        CommandLine cmdLine1 = new DefaultParser().parse(new String[]{"-o", "arg1"});
        assertEquals("arg1", option.getValue(0));

        // Test with no argument
        try {
            CommandLine cmdLine2 = new DefaultParser().parse(new String[]{"-o"});
            fail();
        } catch (ParseException e) {
            // Expected
        }

        // Test with multiple arguments
        try {
            CommandLine cmdLine3 = new DefaultParser().parse(new String[]{"-o", "arg1", "arg2"});
            fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testOptionWithOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1")
                .isRequired(false)
                .hasArg();
        Option option = optionBuilder.create('o');

        // Test with valid argument
        CommandLine cmdLine1 = new DefaultParser().parse(new String[]{"-o", "arg1"});
        assertEquals("arg1", option.getValue(0));

        // Test with no argument
        CommandLine cmdLine2 = new DefaultParser().parse(new String[]{"-o"});
        assertNull(option.getValue(0));

        // Test with multiple arguments
        CommandLine cmdLine3 = new DefaultParser().parse(new String[]{"-o", "arg1", "arg2"});
        fail("Should not accept multiple arguments for an optional argument option");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }
}