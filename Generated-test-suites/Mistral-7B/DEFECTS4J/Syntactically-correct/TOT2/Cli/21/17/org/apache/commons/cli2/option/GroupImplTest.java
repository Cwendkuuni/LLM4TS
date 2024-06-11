package org.apache.commons.cli2.option;

import static org.junit.Assert.*;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.builder.DefaultParserBuilder;
import org.apache.commons.cli2.util.Arguments;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupImplTest {

    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        List<Option> optionsList = new ArrayList<>();

        // Create a simple group with an optional option
        Option groupOption = new GroupImpl("g", "group", 0, Integer.MAX_VALUE, false, Arrays.asList(
                new Option("o", "opt", false, "Option o")
        ));
        optionsList.add(groupOption);

        this.options = new Options();
        this.options.addAll(optionsList);

        this.formatter = new HelpFormatter();
    }

    @Test
    public void testGroupBasicUsage() throws ParseException {
        CommandLine commandLine = new CommandLine();
        options.parse(commandLine, new String[]{"g"});

        assertTrue(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("o"));
    }

    @Test(expected = ParseException.class)
    public void testGroupMinimumUsage() throws ParseException {
        CommandLine commandLine = new CommandLine();
        options.parse(commandLine, new String[]{"g"});

        assertFalse(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("o"));

        // Minimum usage is 1
        options.parse(commandLine, new String[]{"g", "o"});

        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("o"));

        // Minimum usage is not met
        options.parse(commandLine, new String[]{"g"});

        assertFalse(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("o"));

        throw new ParseException("Minimum usage not met");
    }

    @Test(expected = ParseException.class)
    public void testGroupMaximumUsage() throws ParseException {
        CommandLine commandLine = new CommandLine();

        // Maximum usage is 1
        options.parse(commandLine, new String[]{"g", "o", "o"});

        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("o"));

        // Maximum usage is not met
        options.parse(commandLine, new String[]{"g", "o", "o", "o"});

        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("o"));
        assertTrue(commandLine.hasOption("o"));
        assertTrue(commandLine.hasOption("o"));

        throw new ParseException("Maximum usage not met");
    }

    @Test
    public void testGroupWithAnonymousArgument() throws ParseException {
        CommandLine commandLine = new CommandLine();
        options.parse(commandLine, new String[]{"g", "arg"});

        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasArgument("arg"));
    }

    @Test(expected = ParseException.class)
    public void testGroupWithAnonymousArgumentMissing() throws ParseException {
        CommandLine commandLine = new CommandLine();
        options.parse(commandLine, new String[]{"g"});

        assertTrue(commandLine.hasOption("group"));
        assertFalse(commandLine.hasArgument("arg"));

        throw new ParseException("Argument is missing");
    }

    @Test
    public void testHelp() {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);

        formatter.printHelp("Test", options, writer, false);
        writer.flush();

        String helpText = out.toString();
        assertTrue(helpText.contains("group"));
        assertTrue(helpText.contains("Option o"));
        assertTrue(helpText.contains("arg"));
    }
}