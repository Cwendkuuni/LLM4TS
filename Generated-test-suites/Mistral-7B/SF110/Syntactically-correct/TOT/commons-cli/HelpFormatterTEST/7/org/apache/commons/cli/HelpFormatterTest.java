package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp <arg1> [--option1 arg1 | --option2 arg2]";

        Options options = new Options();
        options.addRequiredOption("o", "option1", true, "Option 1");
        options.addOption("d", "option2", true, "Option 2");

        formatter.printHelp(System.out, cmdLineSyntax, "MyApp", options);

        String expectedHelp = "usage: java MyApp <arg1> [--option1 arg1 | --option2 arg2]\n" +
                "\n" +
                "options:\n" +
                "  -o, --option1 arg1\n" +
                "      Option 1 (required)\n" +
                "  -d, --option2 arg2\n" +
                "      Option 2\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithNoSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        formatter.printHelp(System.out, null, "MyApp", options);

        String expectedHelp = "MyApp\n" +
                "\n" +
                "options:\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp <arg1> [--option1 arg1 | --option2 arg2]";

        Options options = new Options();
        options.addRequiredOption("o", "option1", true, "Option 1");
        options.addOption("d", "option2", true, "Option 2");

        formatter.setWidth(90);
        formatter.setLeftPadding(10);
        formatter.setDescPadding(20);

        String header = "MyApp Help";
        String footer = "For more information, visit http://www.example.com";

        formatter.printHelp(System.out, cmdLineSyntax, header, options, footer);

        String expectedHelp = "===============================================================================================================\n" +
                " MyApp Help\n" +
                "===============================================================================================================\n" +
                "\n" +
                "usage: java MyApp <arg1> [--option1 arg1 | --option2 arg2]\n" +
                "\n" +
                "\n" +
                "options:\n" +
                "  -o, --option1 arg1\n" +
                "      Option 1 (required)\n" +
                "  -d, --option2 arg2\n" +
                "      Option 2\n" +
                "\n" +
                "===============================================================================================================\n" +
                "For more information, visit http://www.example.com\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp <arg1> [--option1 arg1 | --option2 arg2]";

        Options options = new Options();
        options.addRequiredOption("o", "option1", true, "Option 1");
        options.addOption("d", "option2", true, "Option 2");

        formatter.printHelp(System.out, cmdLineSyntax, null, options, true);

        String expectedHelp = "usage: java MyApp <arg1> [--option1 arg1 | --option2 arg2]\n" +
                "\n" +
                "options:\n" +
                "  -o, --option1 arg1\n" +
                "      Option 1 (required)\n" +
                "  -d, --option2 arg2\n" +
                "      Option 2\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithEmptyOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";

        Options options = new Options();

        formatter.printHelp(System.out, cmdLineSyntax, null, options);

        String expectedHelp = "MyApp\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithMultipleCommandLineOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp option1 arg1 option2 arg2";

        Options options = new Options();
        options.addOption("o", "option1", true, "Option 1");
        options.addOption("d", "option2", true, "Option 2");

        formatter.printHelp(System.out, cmdLineSyntax, null, options);

        String expectedHelp = "usage: java MyApp option1 arg1 option2 arg2\n" +
                "\n" +
                "options:\n" +
                "  -o, --option1 arg1\n" +
                "      Option 1\n" +
                "  -d, --option2 arg2\n" +
                "      Option 2\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithComplexOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [--option=value | --another-option=value]";

        Options options = new Options();
        options.addOption("o", "option", true, "Option");
        options.addOption("a", "another-option", true, "Another Option");

        formatter.printHelp(System.out, cmdLineSyntax, null, options);

        String expectedHelp = "usage: java MyApp [--option=value | --another-option=value]\n" +
                "\n" +
                "options:\n" +
                "  -o, --option=value\n" +
                "      Option\n" +
                "  -a, --another-option=value\n" +
                "      Another Option\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithOptionGroup() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [--group1 | --group2] [--option1 arg1 | --option2 arg2]";

        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.setRequired(false);
        group1.addOption("g1", "option1", true, "Option 1 in Group 1");
        group1.addOption("g2", "option2", true, "Option 2 in Group 1");
        options.addOptionGroup(group1);

        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        group2.setRequired(false);
        group2.addOption("g3", "option3", true, "Option 1 in Group 2");
        group2.addOption("g4", "option4", true, "Option 2 in Group 2");
        options.addOptionGroup(group2);

        formatter.printHelp(System.out, cmdLineSyntax, null, options);

        String expectedHelp = "usage: java MyApp [--group1 | --group2] [--option1 arg1 | --option2 arg2]\n" +
                "\n" +
                "options:\n" +
                "  --group1\n" +
                "      Group 1\n" +
                "      (--option1 arg1 | --option2 arg2)\n" +
                "  --group2\n" +
                "      Group 2\n" +
                "      (--option3 arg1 | --option4 arg1)\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithInvalidSyntax() {
        HelpFormatter formatter = new HelpFormatter();

        try {
            formatter.printHelp(System.out, null, null, null, "Invalid syntax");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}