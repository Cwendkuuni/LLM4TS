package org.apache.commons.cli2.option;

import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.commandline.ArgParser;
import org.apache.commons.cli2.commandline.OptionDescriptor;
import org.apache.commons.cli2.commandline.OptionSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GroupImplTest {

    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setup() {
        final List<OptionDescriptor> optionsList = new ArrayList<>();

        optionsList.add(new OptionBuilder("g", "group")
                .withLongOpt("group")
                .withDescription("A test group")
                .withArgument("arg")
                .isRequired()
                .create());

        optionsList.add(new OptionBuilder("a", "anonymous")
                .withLongOpt("anonymous")
                .withDescription("An anonymous option")
                .create());

        optionsList.add(new OptionBuilder("i", "int")
                .withLongOpt("int")
                .withDescription("An integer option")
                .create());

        options = new Options();

        final Group group = new Group("group", "A test group", Arrays.asList(optionsList.toArray(new OptionDescriptor[0])));
        options.addOptionGroup(group);

        formatter = new HelpFormatter();
    }

    @Test
    public void testParseGroupWithArguments() {
        final String[] args = new String[]{"-g", "arg1", "arg2"};

        final ParseContext parseContext = new ParseContext();
        final ArgParser parser = new ArgParser(options);

        parser.parseArgument(args, parseContext);

        Assert.assertEquals("arg1", parseContext.getParseResult().getOptions().getOptionValue("group.arg"));
        Assert.assertEquals(2, parseContext.getParseResult().getOptions().getGroupValues("group").size());
    }

    @Test
    public void testParseGroupWithoutArguments() {
        final String[] args = new String[]{"-g"};

        final ParseContext parseContext = new ParseContext();
        final ArgParser parser = new ArgParser(options);

        try {
            parser.parseArgument(args, parseContext);
            Assert.fail();
        } catch (final OptionException e) {
            // expected
        }

        Assert.assertEquals(Collections.emptyList(), parseContext.getParseResult().getOptions().getGroupValues("group"));
    }

    @Test
    public void testParseGroupWithAnonymousOption() {
        final String[] args = new String[]{"-g", "-a"};

        final ParseContext parseContext = new ParseContext();
        final ArgParser parser = new ArgParser(options);

        try {
            parser.parseArgument(args, parseContext);
            Assert.fail();
        } catch (final OptionException e) {
            // expected
        }

        Assert.assertEquals(Collections.singletonList(parseContext.getParseResult().getOptions().findOption("anonymous")),
                parseContext.getParseResult().getOptions().getAllOptions());
    }

    @Test
    public void testParseGroupWithMixedOptions() {
        final String[] args = new String[]{"-g", "arg1", "-i", "5"};

        final ParseContext parseContext = new ParseContext();
        final ArgParser parser = new ArgParser(options);

        parser.parseArgument(args, parseContext);

        Assert.assertEquals("arg1", parseContext.getParseResult().getOptions().getOptionValue("group.arg"));
        Assert.assertEquals(2, parseContext.getParseResult().getOptions().getGroupValues("group").size());
        Assert.assertEquals("5", parseContext.getParseResult().getOptions().getOptionValue("int"));
    }

    @Test
    public void testPrintHelp() {
        final String help = formatter.printHelp("Test", options, true);

        Assert.assertTrue(help.contains("A test group"));
        Assert.assertTrue(help.contains("arg (required, repeating)"));
        Assert.assertTrue(help.contains("anonymous"));
        Assert.assertTrue(help.contains("An integer option"));
    }

    @Test
    public void testPrintHelpWithGroupExpanded() {
        final String help = formatter.printHelp("Test", options, true, "Test", true);

        Assert.assertTrue(help.contains("A test group"));
        Assert.assertTrue(help.contains("An integer option"));
        Assert.assertTrue(help.contains("arg (required, repeating)"));
        Assert.assertTrue(help.contains("anonymous"));

        final List<String> lines = Arrays.asList(help.split("\n"));

        final int groupIndex = lines.indexOf("A test group");

        final int groupLinesEnd = lines.indexOf("", groupIndex) + 1;
        final int helpLinesStart = lines.indexOf("", groupLinesEnd);

        final List<String> groupLines = lines.subList(groupIndex, groupLinesEnd);

        Assert.assertEquals(3, groupLines.size());
        Assert.assertEquals("group", groupLines.get(0).split(":")[0]);
        Assert.assertEquals("A test group", groupLines.get(0).split(":")[1]);
        Assert.assertTrue(groupLines.get(1).startsWith("  "));
        Assert.assertEquals("arg", groupLines.get(1).split(":")[1].split(" ")[0]);
        Assert.assertEquals("An argument (required, repeating)", groupLines.get(1).split(":")[1].split(" ")[1]);
        Assert.assertTrue(groupLines.get(2).startsWith("  "));
        Assert.assertEquals("anonymous", groupLines.get(2).split(":")[1].split(" ")[0]);
        Assert.assertEquals("An anonymous option", groupLines.get(2).split(":")[1].split(" ")[1]);

        final List<String> helpLines = lines.subList(helpLinesStart, lines.size());

        Assert.assertEquals(2, helpLines.size());
        Assert.assertEquals("An integer option", helpLines.get(0).split(":")[1]);
        Assert.assertEquals("anonymous", helpLines.get(1).split(":")[1]);
    }
}