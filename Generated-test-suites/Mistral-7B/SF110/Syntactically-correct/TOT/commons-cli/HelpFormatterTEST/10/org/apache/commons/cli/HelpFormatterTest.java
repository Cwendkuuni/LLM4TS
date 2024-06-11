package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatter() {
        Options options = new Options();
        options.addRequiredOption("a", "arg", true, "an argument");
        options.addOption("b", "boolValue", false, "a boolean value");
        options.addOptionGroup(new OptionGroup("group", "A group"));
        options.addOption(new Option("g", "groupArg", true, "an arg for group", true, "group"));
        options.addOption("h", "help", false, "print help");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);

        String expectedHelp = "usage: Test [-a <arg>] [-b] [-g <groupArg>] [-h]\n\n" +
                "options:\n" +
                "  -a, --arg\t\t\t<arg>\t\tan argument\n" +
                "  -b, --boolValue\t\tb\t\ta boolean value\n" +
                "  -g, --groupArg <groupArg>\t<groupArg>\tan arg for group\n" +
                "  -h, --help\t\th\t\tprint help\n" +
                "group:\n" +
                "  --group\t\t\tgroup\t\tgroup\n";

        Assert.assertEquals(expectedHelp, formatter.getHelpMessage(true));
    }

    @Test
    public void testHelpFormatterWithInvalidOptions() {
        Options options = new Options();
        options.addRequiredOption("a", "arg", true, "an argument");
        options.addOption("b", "boolValue", false, "a boolean value");
        options.addOptionGroup(new OptionGroup("group", "A group"));
        options.addOption(new Option("g", "groupArg", true, "an arg for group", true, "group"));
        options.addOption("z", "notExistingOption", false, "a non-existing option");

        HelpFormatter formatter = new HelpFormatter();
        try {
            formatter.printHelp("Test", options);
            Assert.fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        String expectedHelp = "usage: Test [-a <arg>] [-b] [-g <groupArg>]\n\n" +
                "options:\n" +
                "  -a, --arg <arg>\t\tan argument\n" +
                "  -b\t\tb\t\ta boolean value\n" +
                "  -g, --groupArg <groupArg>\t<groupArg>\tan arg for group\n" +
                "  -z, --notExistingOption\t<notExistingOption>\tan invalid option";

        formatter = new HelpFormatter();
        formatter.setOptionComparator(null); // disable sorting options
        formatter.printHelp("Test", options);

        Assert.assertEquals(expectedHelp, formatter.getHelpMessage(true));
    }

    @Test
    public void testHelpFormatterWithWrongSyntax() {
        Options options = new Options();
        options.addRequiredOption("a", "arg", true, "an argument");

        HelpFormatter formatter = new HelpFormatter();
        try {
            formatter.printHelp("Test", options, "wrongSyntax");
            Assert.fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("syntax: ");
        try {
            formatter.printHelp("Test", options, "wrongSyntax");
            Assert.fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        String expectedHelp = "usage: Test [-a <arg>]\n\n" +
                "options:\n" +
                "  -a, --arg <arg>\t\tan argument";

        formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("syntax: ");
        formatter.printHelp("Test", options);

        Assert.assertEquals(expectedHelp, formatter.getHelpMessage(true));
    }

    @Test
    public void testHelpFormatterWithCustomLeftPadding() {
        Options options = new Options();
        options.addRequiredOption("a", "arg", true, "an argument");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(10);
        formatter.printHelp("Test", options);

        String expectedHelp = "usage: Test [-a <arg>]\n\n" +
                "        options:\n" +
                "       ----\n" +
                "        -a, --arg <arg>\t\tan argument";

        Assert.assertEquals(expectedHelp, formatter.getHelpMessage(true));
    }
}