package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class HelpFormatterTest {

    private HelpFormatter helpFormatter;

    @Before
    public void setUp() {
        helpFormatter = new HelpFormatter();
    }

    @Test
    public void testDefaultValues() {
        Assert.assertEquals(74, helpFormatter.getDefaultWidth());
        Assert.assertEquals(1, helpFormatter.getDefaultLeftPad());
        Assert.assertEquals(3, helpFormatter.getDefaultDescPad());
        Assert.assertEquals("usage: ", helpFormatter.getSyntaxPrefix());
        Assert.assertEquals("-", helpFormatter.getDefaultOptPrefix());
        Assert.assertEquals("--", helpFormatter.getDefaultLongOptPrefix());
        Assert.assertEquals("arg", helpFormatter.getDefaultArgName());
    }

    @Test
    public void testSetWidth() {
        helpFormatter.setWidth(100);
        Assert.assertEquals(100, helpFormatter.getDefaultWidth());
    }

    @Test
    public void testPrintHelpWithDefaults() {
        String cmdLineSyntax = "java MyClass";
        Options options = new Options();
        helpFormatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomValues() {
        String cmdLineSyntax = "java MyClass";
        Options options = new Options();
        helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(120);
        helpFormatter.setLeftPadding(3);
        helpFormatter.setDescPadding(5);
        helpFormatter.setSyntaxPrefix("myPrefix ");
        helpFormatter.setOptPrefix("opt");
        helpFormatter.setLongOptPrefix("longOpt");
        helpFormatter.setArgName("argName");
        helpFormatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptions() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        Option option1 = new Option("f", "file", true, "File path");
        Option option2 = new Option("d", "debug", false, "Enable debug mode");
        Option option3 = new Option("h", "help", false, "Print this help message.");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        helpFormatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithGroupedOptions() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption(new Option("g1a", true, false, "Option A"));
        group1.addOption(new Option("g1b", true, false, "Option B"));
        group1.addOption(new Option("g1c", true, false, "Option C"));
        group1.addOption(new Option("g1d", true, false, "Option D"));
        options.addOptionGroup(group1);

        helpFormatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomizedOptionNames() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        Option option1 = new Option("f", "file", true, "File path");
        option1.setLongOpt("filePath");
        Option option2 = new Option("d", "debug", false, "Enable debug mode");
        option2.setLongOpt("debugging");
        options.addOption(option1);
        options.addOption(option2);

        helpFormatter.setOptionComparator(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Option opt1 = (Option)o1;
                Option opt2 = (Option)o2;
                return opt1.getLongOpt().compareToIgnoreCase(opt2.getLongOpt());
            }
        });

        helpFormatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        String cmdLineSyntax = null;
        Options options = new Options();
        helpFormatter.printHelp(System.out, cmdLineSyntax, options);
    }
}