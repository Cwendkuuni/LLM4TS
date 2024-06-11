package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", true, "Beta option");
        options.addOption("c", "char", 'c', "Character option");
        options.addOption("d", "delta", 1, "Delta option with value");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);

        String expectedHelp = "usage: Test [options]...\n" +
                "\noptions:\n" +
                "  -a, --alpha            Alpha option\n" +
                "  -b, --beta             Beta option (default: false)\n" +
                "  -c CHAR, --char CHAR    Character option\n" +
                "  -d, --delta INTEGER     Delta option with value\n" +
                "\nGroups:\n" +
                "  group1:\n" +
                "    This is group 1\n" +
                "  group2:\n" +
                "    This is group 2\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomWidth() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", true, "Beta option");
        options.addOption("c", "char", 'c', "Character option");
        options.addOption("d", "delta", 1, "Delta option with value");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.printHelp("Test", options);

        String expectedHelp = "usage: Test [options]...\n" +
                "\noptions:\n" +
                "  --alpha                 Alpha option\n" +
                "  -b, --beta               Beta option (default: false)\n" +
                "  --char CHAR              Character option\n" +
                "  -d, --delta INTEGER       Delta option with value\n" +
                "\nGroups:\n" +
                "  group1:\n" +
                "    This is group 1\n" +
                "  group2:\n" +
                "    This is group 2\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomPrefix() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", true, "Beta option");
        options.addOption("c", "char", 'c', "Character option");
        options.addOption("d", "delta", 1, "Delta option with value");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("test: ");
        formatter.printHelp("Test", options);

        String expectedHelp = "test: usage: Test [options]...\n" +
                "\noptions:\n" +
                "  --alpha                 Alpha option\n" +
                "  -b, --beta               Beta option (default: false)\n" +
                "  --char CHAR              Character option\n" +
                "  -d, --delta INTEGER       Delta option with value\n" +
                "\nGroups:\n" +
                "  group1:\n" +
                "    This is group 1\n" +
                "  group2:\n" +
                "    This is group 2\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCmdLineSyntax() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, options);
    }

    @Test
    public void testPrintHelpWithAutoUsage() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", true, "Beta option");
        options.addOption("c", "char", 'c', "Character option");
        options.addOption("d", "delta", 1, "Delta option with value");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options, true);

        String expectedHelp = "usage: Test [options]...\n" +
                "\noptions:\n" +
                "  -a                       Alpha option\n" +
                "  -b                       Beta option (default: false)\n" +
                "  -c CHAR                  Character option\n" +
                "  -d INTEGER               Delta option with value\n" +
                "\nGroups:\n" +
                "  group1:\n" +
                "    This is group 1\n" +
                "  group2:\n" +
                "    This is group 2\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", true, "Beta option");
        options.addOption("c", "char", 'c', "Character option");
        options.addOption("d", "delta", 1, "Delta option with value");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options, "Header", "Footer");

        String expectedHelp = "Header\n" +
                "\nusage: Test [options]...\n" +
                "\noptions:\n" +
                "  -a                       Alpha option\n" +
                "  -b                       Beta option (default: false)\n" +
                "  -c CHAR                  Character option\n" +
                "  -d INTEGER               Delta option with value\n" +
                "\nGroups:\n" +
                "  group1:\n" +
                "    This is group 1\n" +
                "  group2:\n" +
                "    This is group 2\n" +
                "\nFooter";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomLeftPadAndDescPad() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", true, "Beta option");
        options.addOption("c", "char", 'c', "Character option");
        options.addOption("d", "delta", 1, "Delta option with value");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.setDescPadding(5);
        formatter.printHelp("Test", options);

        String expectedHelp = "usage: Test [options]...\n" +
                "\noptions:\n" +
                "  -a                       Alpha option\n" +
                "  -b                       Beta option (default: false)\n" +
                "     βeta option\n" +
                "  -c CHAR                  Character option\n" +
                "      c option\n" +
                "  -d INTEGER               Delta option with value\n" +
                "      Delta option with value\n" +
                "\nGroups:\n" +
                "  group1:\n" +
                "    This is group 1\n" +
                "      Group 1\n" +
                "  group2:\n" +
                "    This is group 2\n" +
                "      Group 2\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomAutoUsage() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", true, "Beta option");
        options.addOption("c", "char", 'c', "Character option");
        options.addOption("d", "delta", 1, "Delta option with value");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options, true, "Custom help");

        String expectedHelp = "usage: Test [options]...\n" +
                "\noptions:\n" +
                "  -a                       Alpha option\n" +
                "  -b                       Beta option (default: false)\n" +
                "  -c CHAR                  Character option\n" +
                "  -d INTEGER               Delta option with value\n" +
                "\nCustom help";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomPREFIX() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", true, "Beta option");
        options.addOption("c", "char", 'c', "Character option");
        options.addOption("d", "delta", 1, "Delta option with value");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("%-15s:");
        formatter.printHelp("Test", options);

        String expectedHelp = "usage: Test [options]...\n" +
                "\noptions:\n" +
                "Alpha option  :\n" +
                "Beta option   : (default: false)\n" +
                "Character option :\n" +
                "Delta option with value :\n" +
                "\nGroups:\n" +
                "Group 1:\n" +
                "  This is group 1\n" +
                "Group 2:\n" +
                "  This is group 2\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomPREFIXAndLeftPad() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", true, "Beta option");
        options.addOption("c", "char", 'c', "Character option");
        options.addOption("d", "delta", 1, "Delta option with value");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("%15s:");
        formatter.setLeftPadding(10);
        formatter.printHelp("Test", options);

        String expectedHelp = "usage: Test [options]...\n" +
                "\noptions:\n" +
                "Alpha option :\n" +
                "Beta option   : (default: false)\n" +
                "Character option :\n" +
                "Delta option with value :\n" +
                "\nGroups:\n" +
                "Group 1:\n" +
                "  This is group 1\n" +
                "Group 2:\n" +
                "  This is group 2\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }
}