package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParseOptions;
import org.apache.commons.cli.TestHelper;

/**
 * Test cases for {@link Parser} class
 *
 * @author John Keyes
 */
public class ParserTest {

    @org.junit.Test
    public void testParse() throws Exception {

        String[] args = new String[] { "-a", "1", "--option", "value", "-b",
                "-c", "three" };

        Options options = new Options();
        options.addRequiredOption("a", "a", true, "an option");
        options.addOption("b", "b", false, "another option");
        options.addOptionGroup(new OptionGroup("testGroup", "test group") {
            {
                addOption(new Option("c", "c", true, "a test option"));
                addOption(new Option("d", null, false, "another test option"));
            }
        });

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a").equals("1"));
        assertSame(cmd.getOptionValue("option"), "value");
        assertFalse(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("testGroup.c"));
        assertTrue(cmd.getOptionValue("testGroup.c").equals("three"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("z"));
        assertNull(cmd.getOptionValue("z"));
        assertNull(cmd.getArgList().toArray()[0]);
        assertNull(cmd.getArgList().toArray()[1]);
        assertTrue(cmd.getArgList().toArray().length == 2);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "", options, true);
        assertTrue(formatter.getHelpMessage().contains("an option"));
        assertTrue(formatter.getHelpMessage().contains("--option value"));
        assertTrue(formatter.getHelpMessage().contains("another option"));
        assertTrue(formatter.getHelpMessage().contains("test group"));
        assertTrue(formatter.getHelpMessage().contains("a test option"));
        assertFalse(formatter.getHelpMessage().contains("z"));
        assertFalse(formatter.getHelpMessage().contains("another test option"));

        // test with empty args
        args = new String[0];
        cmd = parser.parse(options, args);
        assertTrue(cmd.getArgList().isEmpty());

        // test with invalid args
        args = new String[] { "--", "-a=", "-a=1---", "--option=value---" };
        try {
            cmd = parser.parse(options, args);
            fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[] { "-a=1--" };
        try {
            cmd = parser.parse(options, args);
            fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[] { "-a", "--option=value", "-b" };
        try {
            cmd = parser.parse(options, args);
            fail();
        } catch (ParseException e) {
            // expected
        }

        // test with missing required option
        args = new String[] { "-b" };
        try {
            cmd = parser.parse(options, args);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }

        // test with missing required option group
        Options options2 = new Options();
        options2.addOptionGroup(new OptionGroup("testGroup2", "test group 2") {
            {
                addOption(new Option("c", "c", true, "a test option"));
            }
        });
        options2.addOption("d", "d", false, "another test option");

        CommandLineParser parser2 = new DefaultParser();
        try {
            cmd = parser2.parse(options2, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }

        // test with help
        args = new String[] { "--help" };
        cmd = parser.parse(options, args);
        assertTrue(formatter.getHelpMessage().contains("Help:"));
        assertTrue(formatter.getHelpMessage().contains("Options:"));
        assertFalse(formatter.getHelpMessage().contains("--help"));

        // test with invalid long option
        args = new String[] { "--long-invalid" };
        try {
            cmd = parser.parse(options, args);
            fail();
        } catch (UnrecognizedOptionException e) {
            // expected
        }

        // test with multiple values for option
        options = new Options();
        options.addOption("m", "m", true, "multi-valued option");
        cmd = parser.parse(options, new String[] { "-m", "value1", "-m",
                "value2" });
        List<String> values = cmd.getOptionValues("m");
        assertTrue(values.size() == 2);
        assertTrue(values.get(0).equals("value1"));
        assertTrue(values.get(1).equals("value2"));

        // test with missing value for option
        options = new Options();
        options.addOption("o", "o", true, "option with required value");
        try {
            cmd = parser.parse(options, new String[] { "-o" });
            fail();
        } catch (MissingArgumentException e) {
            // expected
        }

        // test with too many arguments
        args = new String[] { "arg1", "arg2", "arg3" };
        try {
            cmd = parser.parse(options, args);
            fail();
        } catch (ParseException e) {
            // expected
        }

        // test with empty property
        Properties properties = new Properties();
        cmd = parser.parse(options, args, properties);
        assertNull(cmd.getOptionValues("m"));

        // test with properties and valid values
        properties = new Properties();
        properties.put("m", "value1");
        properties.put("m", "value2");
        cmd = parser.parse(options, args, properties);
        values = cmd.getOptionValues("m");
        assertTrue(values.size() == 2);
        assertTrue(values.get(0).equals("value1"));
        assertTrue(values.get(1).equals("value2"));

        // test with invalid value in property
        properties = new Properties();
        properties.put("m", "invalid value");
        properties.put("m", "value2");
        try {
            cmd = parser.parse(options, args, properties);
            fail();
        } catch (RuntimeException e) {
            // expected
        }

        // test with invalid option in property
        properties = new Properties();
        properties.put("z", "invalid value");
        try {
            cmd = parser.parse(options, args, properties);
            fail();
        } catch (UnrecognizedOptionException e) {
            // expected
        }
    }
}