package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserTest {

    @Test
    public void testParseSimple() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-a"};
        CommandLine cmd = parser.parse(options, args, false);
        assertEquals(true, cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithValue() throws ParseException {
        Options options = new Options();
        options.addOption("b", true, "Option B");
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-b", "value"};
        CommandLine cmd = parser.parse(options, args, false);
        assertEquals(true, cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("c", true, "Option C");
        options.addRequiredOption("d", false, "Option D");
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-c"};
        CommandLine cmd = parser.parse(options, args, false);
        assertEquals(true, cmd.hasOption("c"));
        assertEquals(true, cmd.hasOption("d"));
        assertNull(cmd.getOptionValue("d"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("e", true, "Option E");
        options.addRequiredOption("f", false, "Option F");
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-e"};
        CommandLine cmd = parser.parse(options, args, false);
        assertEquals(true, cmd.hasOption("e"));
        assertFalse(cmd.hasOption("f"));
    }

    @Test
    public void testParseMultipleValues() throws ParseException {
        Options options = new Options();
        options.addOption("g", true, "Option G");
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-g", "value1", "-g", "value2"};
        CommandLine cmd = parser.parse(options, args, false);
        List<String> values = cmd.getOptionValues("g");
        assertEquals(values.size(), 2);
        assertEquals(values.get(0), "value1");
        assertEquals(values.get(1), "value2");
    }

    @Test
    public void testParseOptionalGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group");
        group.setRequired(false);
        group.addOption("h", true, "Option H");
        group.addOption("i", true, "Option I");
        options.addOptionGroup(group);
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-h"};
        CommandLine cmd = parser.parse(options, args, false);
        assertTrue(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("i"));
        assertFalse(group.isSelected());
    }

    @Test
    public void testParseSelectedGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group");
        group.setRequired(false);
        group.addOption("h", true, "Option H");
        group.addOption("i", true, "Option I");
        options.addOptionGroup(group);
        String[] args = new String[]{"--g"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
        assertTrue(cmd.hasOption("h"));
        assertTrue(cmd.hasOption("i"));
        assertTrue(group.isSelected());
    }

    @Test(expected = ParseException.class)
    public void testParseMissingGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group");
        group.setRequired(false);
        group.addOption("h", true, "Option H");
        group.addOption("i", true, "Option I");
        options.addOptionGroup(group);
        String[] args = new String[]{"--j"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
        assertFalse(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("i"));
        assertFalse(group.isSelected());
    }

    @Test
    public void testParseHelp() {
        Options options = new Options();
        options.addOption("h", true, "Help");
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, null, true, null);
        String help = formatter.printHelp("parser", "", options, true);
        assertTrue(help.contains("Option H"));
        assertTrue(help.contains("--help"));
    }

    @Test
    public void testParseInvalidOption() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"--invalid"};
        try {
            parser.parse(options, args, false);
            fail();
        } catch (ParseException e) {
            assertEquals("Unrecognized option: invalid", e.getMessage());
        }
    }

    @Test
    public void testParseMissingOptionValue() throws ParseException {
        Options options = new Options();
        options.addOption("m", true, "Option M");
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-m"};
        try {
            parser.parse(options, args, false);
            fail();
        } catch (ParseException e) {
            assertEquals("Missing value for option: m", e.getMessage());
        }
    }
}