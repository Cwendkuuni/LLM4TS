package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.Parser;
import org.apache.commons.cli2.TestUtil;
import org.apache.commons.cli2.util.HelpFormatterBuilder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    @Test
    public void testBasicUsage() {
        final List<String> args = new ArrayList<>();
        args.add("TestGroup");
        args.add("--option1");
        args.add("--value1");
        args.add("--option2");
        args.add("--value2");

        final Options options = new Options();
        final Group group = new Group("TestGroup", "Test group description") {
            @Override
            protected Option buildOption(String longName, String shortName) {
                return new Option("o", longName, false, "Test option description").setRequired(false);
            }
        }.addOption(new Option("option1", "value1").setRequired(false));
        group.addOption(new Option("option2", "value2").setRequired(false));

        final Parser parser = new Parser();
        final CommandLine commandLine;

        try {
            commandLine = parser.parse(options, args.toArray(new String[args.size()]));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("TestGroup"));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));

        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertEquals("value2", commandLine.getOptionValue("option2"));

        assertFalse(commandLine.hasOption("nonexistent"));
        assertNull(commandLine.getOptionValue("nonexistent"));

        final HelpFormatter formatter = new HelpFormatterBuilder()
                .setHelpCommand("%h [options]")
                .setOptionHandler("%h", new HelpFormatter.DefaultHandler())
                .addOption("TestGroup", "Test group description")
                    .addOption(Option.builder("option1")
                        .longOpt("option1")
                        .desc("Test option description")
                        .build())
                    .addOption(Option.builder("option2")
                        .longOpt("option2")
                        .desc("Test option description")
                        .build())
                .toHelpFormatter();

        final String help = formatter.printHelp("TestGroup", "", true);
        assertTrue(help.contains("Test group description"));
        assertTrue(help.contains("Test option description"));
        assertTrue(help.contains("--option1 value1"));
        assertTrue(help.contains("--option2 value2"));
    }

    @Test
    public void testMinimum() {
        final List<String> args = new ArrayList<>();
        args.add("TestGroup");

        final Options options = new Options();
        final Group group = new Group("TestGroup", "Test group description") {
            @Override
            protected Option buildOption(String longName, String shortName) {
                return new Option("o", longName, false, "Test option description").setRequired(true);
            }
        }.addOption(new Option("option1", "value1").setRequired(false));

        try {
            final Parser parser = new Parser();
            parser.parse(options, args.toArray(new String[args.size()]));
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("TestGroup requires more than 0 values", e.getMessage());
        }
    }

    @Test
    public void testMaximum() {
        final List<String> args = new ArrayList<>();
        args.add("TestGroup");
        args.add("--option1");
        args.add("--option1");

        final Options options = new Options();
        final Group group = new Group("TestGroup", "Test group description") {
            @Override
            protected Option buildOption(String longName, String shortName) {
                return new Option("o", longName, false, "Test option description").setRequired(false)
                    .setMaxOccurs(1);
            }
        }.addOption(new Option("option1", "value1").setRequired(false));

        try {
            final Parser parser = new Parser();
            parser.parse(options, args.toArray(new String[args.size()]));
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("TestGroup may define zero to one values", e.getMessage());
        }

        args.add("--option1");

        try {
            final Parser parser = new Parser();
            parser.parse(options, args.toArray(new String[args.size()]));
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("TestGroup may define zero to one values", e.getMessage());
        }

        args.remove(args.size() - 1);

        final CommandLine commandLine;

        try {
            commandLine = parser.parse(options, args.toArray(new String[args.size()]));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        assertTrue(commandLine.hasOption("TestGroup"));
        assertTrue(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option1"));

        assertEquals("value1", commandLine.getOptionValue("option1"));
    }

    @Test
    public void testMinMax() {
        final List<String> args = new ArrayList<>();
        args.add("TestGroup");
        args.add("--option1");
        args.add("--option2");

        final Options options = new Options();
        final Group group = new Group("TestGroup", "Test group description") {
            @Override
            protected Option buildOption(String longName, String shortName) {
                return new Option("o", longName, false, "Test option description").setRequired(true)
                    .setMinOccurs(1).setMaxOccurs(3);
            }
        }.addOption(new Option("option1", "value1").setRequired(false));
        group.addOption(new Option("option2", "value2").setRequired(false));

        try {
            final Parser parser = new Parser();
            parser.parse(options, args.toArray(new String[args.size()]));
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("TestGroup requires exactly 2 values", e.getMessage());
        }

        args.add("--option3");

        try {
            final Parser parser = new Parser();
            parser.parse(options, args.toArray(new String[args.size()]));
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("TestGroup requires exactly 2 values", e.getMessage());
        }

        args.remove(args.size() - 1);

        final CommandLine commandLine;

        try {
            commandLine = parser.parse(options, args.toArray(new String[args.size()]));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        assertTrue(commandLine.hasOption("TestGroup"));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));

        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertEquals("value2", commandLine.getOptionValue("option2"));
    }

    @Test
    public void testAnonymous() {
        final List<String> args = new ArrayList<>();
        args.add("TestGroup");
        args.add("--value1");
        args.add("value2");

        final Options options = new Options();
        final Group group = new Group("TestGroup", "Test group description") {
            @Override
            protected Option buildOption(String longName, String shortName) {
                return new Argument(longName, shortName, "value", "Test anonymous argument description")
                    .setRequired(false);
            }
        };

        group.addOption(new Option("value1", "value1").setRequired(false));
        group.addOption(new Option("value2", "value2").setRequired(false));

        try {
            final Parser parser = new Parser();
            parser.parse(options, args.toArray(new String[args.size()]));
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("TestGroup requires exactly 1 argument", e.getMessage());
        }

        args.add("arg");

        try {
            final Parser parser = new Parser();
            parser.parse(options, args.toArray(new String[args.size()]));
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("TestGroup requires exactly 1 argument", e.getMessage());
        }

        args.remove(args.size() - 1);

        final CommandLine commandLine;

        try {
            commandLine = parser.parse(options, args.toArray(new String[args.size()]));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        assertTrue(commandLine.hasOption("TestGroup"));
        assertTrue(commandLine.hasOption("value1"));
        assertTrue(commandLine.hasOption("value2"));

        assertEquals("value1", commandLine.getOptionValue("value1"));
        assertEquals("value2", commandLine.getOptionValue("value2"));
        assertEquals("value2", commandLine.getArgValue(0));
    }

    @Test
    public void testMissingValues() {
        final List<String> args = new ArrayList<>();
        args.add("TestGroup");

        final Options options = new Options();
        final Group group = new Group("TestGroup", "Test group description") {
            @Override
            protected Option buildOption(String longName, String shortName) {
                return new Option("o", longName, false, "Test option description").setRequired(true);
            }
        }.addOption(new Option("option1", "value1").setRequired(true));

        try {
            final Parser parser = new Parser();
            parser.parse(options, args.toArray(new String[args.size()]));
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("TestGroup requires more than 0 values", e.getMessage());
        }
    }

    @Test
    public void testMissingRequired() {
        final List<String> args = new ArrayList<>();
        args.add("TestGroup");

        final Options options = new Options();
        final Group group = new Group("TestGroup", "Test group description") {
            @Override
            protected Option buildOption(String longName, String shortName) {
                return new Option("o", longName, false, "Test option description").setRequired(true);
            }
        }.addOption(new Option("option1", "value1").setRequired(false));

        try {
            final Parser parser = new Parser();
            parser.parse(options, args.toArray(new String[args.size()]));
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("TestGroup requires value 'option1' is missing", e.getMessage());
        }
    }
}