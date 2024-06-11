package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionWithRequiredArg() {
        OptionBuilder builder = OptionBuilder.withLongOpt("inputFile")
                .withDescription("Specify input file")
                .isRequired()
                .hasArg();
        Option option = builder.create('i');

        CommandLine cmd = null;
        try {
            cmd = new DefaultParser().parse(new String[]{"-i", "test.txt"});
            assertEquals("test.txt", option.getValue());
        } catch (ParseException e) {
            fail("Failed to parse command line: " + e.getMessage());
        }
    }

    @Test
    public void testOptionWithOptionalArg() {
        OptionBuilder builder = OptionBuilder.withLongOpt("outputFile")
                .withDescription("Specify output file")
                .hasOptionalArg();
        Option option = builder.create('o');

        CommandLine cmd1 = null;
        try {
            cmd1 = new DefaultParser().parse(new String[]{"-o", "output.txt"});
            assertEquals("output.txt", option.getValue());
        } catch (ParseException e) {
            fail("Failed to parse command line: " + e.getMessage());
        }

        CommandLine cmd2 = null;
        try {
            cmd2 = new DefaultParser().parse(new String[]{"-o"});
            assertNull(option.getValue());
        } catch (ParseException e) {
            fail("Failed to parse command line: " + e.getMessage());
        }
    }

    @Test
    public void testOptionWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.withLongOpt("ports")
                .withDescription("Specify comma-separated list of ports")
                .hasArgs()
                .withValueSeparator(',');
        Option option = builder.create('p');

        CommandLine cmd = null;
        try {
            cmd = new DefaultParser().parse(new String[]{"-p", "8080,8000,8001"});
            String[] ports = option.getValues();
            assertEquals(3, ports.length);
            assertEquals("8080", ports[0]);
            assertEquals("8000", ports[1]);
            assertEquals("8001", ports[2]);
        } catch (ParseException e) {
            fail("Failed to parse command line: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testOptionWithInvalidArgNumber() {
        OptionBuilder builder = OptionBuilder.withLongOpt("inputFile")
                .withDescription("Specify input file");
        Option option = builder.create('i');

        CommandLine cmd = null;
        try {
            cmd = new DefaultParser().parse(new String[]{"-i", "test.txt", "invalid"});
            fail("Expected ParseException but command line was parsed successfully.");
        } catch (ParseException e) {
            // Expected
        }

        assertEquals(1, option.getArgCount());
        assertEquals("test.txt", option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithoutLongOptSet() {
        OptionBuilder builder = OptionBuilder.hasArg();
        try {
            builder.create('i');
            fail("Expected IllegalArgumentException but command was executed successfully.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}