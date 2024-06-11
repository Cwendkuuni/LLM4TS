package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @org.junit.Test
    public void testParse() throws Exception {
        String[] args = new String[]{"-a", "--optionB=valueB", "--optionC",
                "-d", "argD", "--optionA=valueA"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B")
                .required(true)
                .longOpt("optionB");
        options.addOption("c", false, "Option C");
        options.addOption("d", true, "Option D");

        parser.setOptions(options);

        CommandLine cmd = parser.parse(args);

        assertEquals(5, cmd.getArgs().length);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertEquals("argD", cmd.getArgs()[0]);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseMissingOption() throws Exception {
        String[] args = new String[]{"--invalidOption"};

        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);

        parser.parse(args);

        fail("Expected ParseException not thrown");
    }

    @org.junit.Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws Exception {
        String[] args = new String[]{"--invalidOption"};

        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);

        parser.parse(args);
    }

    @org.junit.Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws Exception {
        String[] args = new String[]{"-a", "--optionB"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A")
                .required(true);
        options.addOption("b", true, "Option B")
                .required(true)
                .longOpt("optionB");

        parser.setOptions(options);

        parser.parse(args);
    }

    @org.junit.Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws Exception {
        String[] args = new String[]{"-a", "-b"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B")
                .requiresArg();

        parser.setOptions(options);

        parser.parse(args);
    }

    @org.junit.Test
    public void testParseWithProperties() throws Exception {
        String[] args = new String[]{"--optionA=valueA1", "--optionA=valueA2"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A")
                .longOpt("optionA")
                .required(true);

        parser.setOptions(options);

        CommandLine cmd = parser.parse(args, new Properties() {{
            put("OptionA", "valueA1,valueA2");
        }});

        assertEquals(0, cmd.getArgs().length);
        assertEquals(2, cmd.getOptionValues("a").length);
        assertArrayEquals(new String[]{"valueA1", "valueA2"},
                cmd.getOptionValues("a"));
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseWithPropertiesMissingOption() throws Exception {
        String[] args = new String[]{"--optionX=valueX"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A")
                .longOpt("optionA")
                .required(true);

        parser.setOptions(options);

        parser.parse(args, new Properties() {{
            put("OptionA", "valueA1,valueA2");
            put("OptionX", "valueX");
        }});

        fail("Expected ParseException not thrown");
    }

    @org.junit.Test
    public void testParseWithOptionGroup() throws Exception {
        String[] args = new String[]{"--groupOptionA", "-g", "--groupOptionB"};

        Parser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Option group")
                .required(false);
        group.addOption("groupOptionA", true, "Group option A");
        group.addOption("groupOptionB", true, "Group option B");

        options.addOptionGroup(group);
        options.addOption("g", true, "Option G");

        parser.setOptions(options);

        CommandLine cmd = parser.parse(args);

        assertTrue(cmd.hasOption("groupOptionA"));
        assertTrue(cmd.hasOption("groupOptionB"));
        assertTrue(cmd.hasOption("g"));

        assertEquals(0, cmd.getArgs().length);
    }
}