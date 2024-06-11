package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * Test class for Parser
 */
public class ParserTest {

    @Test
    public void testParse() throws Exception {
        String[] args = new String[]{"-a", "--b=2", "-c=3", "--d", "e", "f"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B").required();
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], cmd.getArgs().get(i));
        }
        assertEquals(true, cmd.hasOption("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals(true, cmd.hasOption("c"));
        assertEquals("3", cmd.getOptionValue("c"));
        assertEquals(true, cmd.hasOption("d"));
        assertNull(cmd.getOptionValue("e"));
        assertNull(cmd.getOptionValue("f"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws Exception {
        String[] args = new String[]{"--g"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B").required();
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        String[] args = new String[]{"--h"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testMissingRequiredOption() throws Exception {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A").required();
        CommandLineParser parser = new DefaultParser();

        try {
            parser.parse(options, args, false);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a", e.getMessage());
        }
    }

    @Test
    public void testParseWithProperties() throws Exception {
        String[] args = new String[]{"--foo=bar"};
        Properties props = new Properties();
        props.setProperty("foo", "baz");
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, props, false);
        assertEquals("baz", cmd.getOptionValue("foo"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        String[] args = new String[]{"--foo", "bar", "baz"};
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true, false);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("baz", cmd.getArgs().get(1));
    }

    @Test
    public void testParseWithStopAtNonOptionAndOptionalArg() throws Exception {
        String[] args = new String[]{"--foo=bar", "baz"};
        Options options = new Options();
        options.addOption("foo", true, "Option Foo").hasArg();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true, false);
        assertEquals("bar", cmd.getOptionValue("foo"));
        assertEquals("baz", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithOptionGroup() throws Exception {
        String[] args = new String[]{"--group", "--x"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption("x", true, "Option X");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
        assertTrue(group.isSelected());
        assertEquals(true, cmd.hasOption("x"));
    }
}