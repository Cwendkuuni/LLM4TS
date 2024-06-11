package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParser() throws ParseException {
        // Test with valid options
        String[] arguments = {"-a", "--optionB=valueB", "--optionC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", null, "Option C");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
        Assert.assertEquals(Arrays.asList("--optionB=valueB", "--optionC"), cmd.getArgs());

        // Test with missing required option
        arguments = new String[]{"--optionA"};
        options.addOption("a", true, "Option A").required(true);

        try {
            parser.parse(options, arguments, false);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option 'a'", e.getMessage());
        }

        // Test with extra arguments
        arguments = new String[]{"-a", "--optionB=valueB", "--optionC", "extra"};
        try {
            parser.parse(options, arguments, false);
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: extra", e.getMessage());
        }

        // Test with invalid option
        arguments = new String[]{"--invalidOption"};
        try {
            parser.parse(options, arguments, false);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: invalidOption", e.getMessage());
        }

        // Test with properties
        Properties properties = new Properties();
        properties.setProperty("optionB", "valueB");
        arguments = new String[]{"--optionA", "--optionB=valueB"};

        cmd = parser.parse(options, arguments, properties, false);
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }
}