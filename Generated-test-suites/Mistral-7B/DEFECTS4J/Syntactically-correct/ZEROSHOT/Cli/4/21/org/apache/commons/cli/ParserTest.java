package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Test;

/**
 * Test class for Parser.java
 *
 * @author John Doe
 */
public class ParserTest {

    /**
     * Test parse method with valid options and arguments.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testParseValidArgs() throws ParseException {
        String[] args = {"-a", "1", "--b", "2", "-c", "3", "--d"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments,
                                       boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertEquals(1, cmd.getOptionValue("a"));
        assertEquals(2, cmd.getOptionValue("b"));
        assertEquals(3, cmd.getOptionValue("c"));
    }

    /**
     * Test parse method with invalid options.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption() throws ParseException {
        String[] args = {"-x", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments,
                                       boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, args);

        fail("ParseException expected but not thrown");
    }

    /**
     * Test parse method with required options not provided.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException {
        String[] args = {"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "requiredOptionB");

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments,
                                       boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, args);

        fail("MissingOptionException expected but not thrown");
    }

    /**
     * Test parse method with mixed options and arguments.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testParseMixedArgs() throws ParseException {
        String[] args = {"--optionA", "arg1", "--optionB=value", "-c", "arg2"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", true, "Option B");
        options.addOption("c", true, "Option C");

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments,
                                       boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("optionA"));
        assertTrue(cmd.hasOption("optionB"));
        assertEquals("arg1", cmd.getOptionValue("optionA"));
        assertEquals("value", cmd.getOptionValue("optionB"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("arg2", cmd.getArg(2));
    }

    /**
     * Test parse method with properties.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testParseProperties() throws ParseException {
        String[] args = {"--property1=value1", "--property2=value2"};
        Options options = new Options();
        options.addOption("property1", false, "Property1");
        options.addOption("property2", false, "Property2");

        Properties properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments,
                                       boolean stopAtNonOption) {
                return arguments;
            }

            @Override
            public CommandLine parse(Options options, String[] arguments,
                                     Properties properties,
                                     boolean stopAtNonOption) throws ParseException {
                CommandLine cmd = super.parse(options, arguments, properties,
                        stopAtNonOption);
                processProperties(properties);
                checkRequiredOptions();
                return cmd;
            }

            @Override
            public void processProperties(Properties properties) {
                super.processProperties(properties);
                assertEquals("value1", cmd.getOptionValue("property1"));
                assertEquals("value2", cmd.getOptionValue("property2"));
            }
        };

        CommandLine cmd = parser.parse(options, args, properties, false);

        assertTrue(cmd.hasOption("property1"));
        assertTrue(cmd.hasOption("property2"));
        assertEquals("value1", cmd.getOptionValue("property1"));
        assertEquals("value2", cmd.getOptionValue("property2"));
    }

    /**
     * Test parse method with options in an OptionGroup.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testParseOptionGroup() throws ParseException {
        String[] args = {"-t", "--test", "value"};
        Options options = new Options();

        OptionGroup group = new OptionGroup("testGroup", "Test Group");
        group.setRequired(false);
        group.addOption("test", true, "Test");

        options.addOptionGroup(group);

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments,
                                       boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("test"));
        assertEquals(true, cmd.getOptionValue("test"));
        assertFalse(cmd.getOptionValues("test").length > 1);
        assertTrue(cmd.getOptionGroup("testGroup").isSelected());
    }
}