package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HelpFormatterTest {

    private HelpFormatter formatter;

    @Before
    public void setUp() {
        formatter = new HelpFormatter();
    }

    @Test
    public void testDefaultInitialization() {
        Assert.assertEquals(74, formatter.getWidth());
        Assert.assertEquals(1, formatter.getLeftPadding());
        Assert.assertEquals(3, formatter.getDescPadding());
        Assert.assertEquals("usage: ", formatter.getSyntaxPrefix());
        Assert.assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
        Assert.assertEquals("-", formatter.getOptPrefix());
        Assert.assertEquals("--", formatter.getLongOptPrefix());
        Assert.assertEquals("arg", formatter.getArgName());
    }

    @Test
    public void testSettingWidth() {
        formatter.setWidth(50);
        Assert.assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testPrintHelpWithDefaults() {
        String cmdLineSyntax = "java MyClass --option arg1 arg2";
        Options options = new Options();
        options.addOption("option", "o", false, "An option");
        options.addOption("arg1", true, "Required arg1");
        options.addOption("arg2", false, "Optional arg2");

        formatter.printHelp(cmdLineSyntax, options);

        // Add your assertions here to check the output
    }

    @Test
    public void testPrintHelpWithCustomSettings() {
        String cmdLineSyntax = "java MyClass [--option arg1 <arg>] [--arg2]";
        Options options = new Options();
        options.addOption("option", "o", false, "An option");
        options.addOption("arg1", true, "Required arg1");
        options.addOption("arg2", false, "Optional arg2");

        formatter.setWidth(100);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(5);
        formatter.setSyntaxPrefix("  usage: ");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("arg");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        formatter.printHelp(out, 100, cmdLineSyntax, options);
        out.flush();

        String expectedHelp = "  usage:  java MyClass [--option arg1 <arg>] [--arg2]\n" +
                "                            ------------------------------------------\n" +
                "  options:\n" +
                "    -option, --option    An option\n" +
                "    -arg1, --arg1=ARG   Required arg1\n" +
                "    -arg2, --arg2        Optional arg2\n";

        Assert.assertEquals(expectedHelp, outContent.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenCmdLineSyntaxIsNull() {
        String cmdLineSyntax = null;
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptions() {
        String cmdLineSyntax = "java MyClass [options...]";
        Options options = new Options();
        options.addOption("option1", false, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", false, "Option 3")
                .longOpt("long-option3")
                .valueSeparator('=')
                .required(true)
                .desc("A long option");
        options.addOption("option4", false, "Option 4")
                .longOpt("long-option4")
                .desc("Another long option")
                .aliases(new String[]{"a", "aliased"});

        formatter.printHelp(cmdLineSyntax, options);

        // Add your assertions here to check the output
    }

    @Test
    public void testPrintHelpWithCustomOptionComparator() {
        String cmdLineSyntax = "java MyClass [--option1 arg1] [--option2 arg2]";
        Options options = new Options();
        options.addOption("option2", false, "Option 2");
        options.addOption("option1", false, "Option 1")
                .desc("An option")
                .sortOrder(Option.ORDER_ALPHABETICALLY);

        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Option opt1 = (Option)o1;
                Option opt2 = (Option)o2;
                return opt1.getKey().compareToIgnoreCase("option1");
            }
        };

        formatter.setOptionComparator(comparator);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        formatter.printHelp(out, 80, cmdLineSyntax, options);
        out.flush();

        String expectedHelp = "usage:  java MyClass [--option1 arg1] [--option2 arg2]\n" +
                "                            ---------------------------------------\n" +
                "  options:\n" +
                "    -option1, --option1   An option\n" +
                "    -option2, --option2   Option 2\n";

        Assert.assertEquals(expectedHelp, outContent.toString());
    }
}