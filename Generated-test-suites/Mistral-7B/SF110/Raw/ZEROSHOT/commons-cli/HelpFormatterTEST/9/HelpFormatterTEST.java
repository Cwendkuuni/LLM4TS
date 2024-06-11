package org.apache.commons.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class HelpFormatterTest {

    private ByteArrayOutputStream outContent;
    private PrintWriter out;

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        out = new PrintWriter(outContent);
    }

    @After
    public void tearDown() {
        out.close();
    }

    @Test
    public void testDefaultConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
        assertEquals("-", formatter.getOptPrefix());
        assertEquals("--", formatter.getLongOptPrefix());
        assertEquals("arg", formatter.getArgName());
    }

    @Test
    public void testPrintHelpWithDefaultOptions() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(out, 74, "test", options);

        String expected = "usage: test\n" +
                "\n" +
                "options:\n" +
                "\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithOptions() {
        Options options = new Options();
        Option opt = new Option("opt", "option", false, "Short option description");
        Option longOpt = new Option("long-opt", "long option description", true, "Long option with argument");
        opt.setRequired(false);
        options.addOption(opt);
        options.addOption(longOpt);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(out, 74, "test", options);

        String expected = "usage: test\n" +
                "\n" +
                "options:\n" +
                "  -opt, --option        Short option description\n" +
                "  -long-opt, --long-option <arg>  Long option with argument\n" +
                "\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithRequiredOptions() {
        Options options = new Options();
        Option opt = new Option("opt", "option", true, "Short option description");
        Option longOpt = new Option("long-opt", "long option description", true, "Long option with argument");
        opt.setRequired(true);
        options.addOption(opt);
        options.addOption(longOpt);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(out, 74, "test", options);

        String expected = "usage: test [<option>]<args>\n" +
                "\n" +
                "options:\n" +
                "  -opt, --option        Short option description\n" +
                "  -long-opt, --long-option <arg>  Long option with argument\n" +
                "\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithOptionGroups() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group description");
        Option opt1 = new Option("opt1", "Option 1", false, "Option 1 description");
        Option opt2 = new Option("opt2", "Option 2", false, "Option 2 description");
        group.addOption(opt1);
        group.addOption(opt2);
        options.addOptionGroup(group);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(out, 74, "test", options);

        String expected = "usage: test\n" +
                "\n" +
                "options:\n" +
                "  group:\n" +
                "    -opt1, --option-1        Option 1 description\n" +
                "    -opt2, --option-2        Option 2 description\n" +
                "\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithCustomWidth() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(out, 50, "test", options);

        String expected = "usage: test\n" +
                "\n" +
                "options:\n" +
                "\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithCustomSyntaxPrefix() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("test: ");
        formatter.printHelp(out, 74, "test", options);

        String expected = "test: test\n" +
                "\n" +
                "options:\n" +
                "\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithCustomOptPrefix() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        formatter.printHelp(out, 74, "test", options);

        String expected = "usage: test\n" +
                "\n" +
                "options:\n" +
                "\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithCustomLongOptPrefix() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--long-");
        formatter.printHelp(out, 74, "test", options);

        String expected = "usage: test\n" +
                "\n" +
                "options:\n" +
                "\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithCustomArgName() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("argName");
        formatter.printHelp(out, 74, "test", options);

        String expected = "usage: test <argName>\n" +
                "\n" +
                "options:\n" +
                "\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithCustomOptionComparator() {
        Options options = new Options();
        Option opt1 = new Option("opt1", "Option 1", false, "Option 1 description");
        Option opt2 = new Option("opt2", "Option 2", false, "Option 2 description");
        options.addOption(opt2);
        options.addOption(opt1);

        HelpFormatter formatter = new HelpFormatter();
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 1;
            }
        };
        formatter.setOptionComparator(comparator);
        formatter.printHelp(out, 74, "test", options);

        String expected = "usage: test\n" +
                "\n" +
                "options:\n" +
                "  -opt2, --option-2        Option 2 description\n" +
                "  -opt1, --option-1        Option 1 description\n" +
                "\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithCustomLeftPadding() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.printHelp(out, 74, "test", options);

        String expected = "  usage: test\n" +
                "  \n" +
                "options:\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithCustomDescPadding() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(10);
        formatter.printHelp(out, 74, "test", options);

        String expected = "usage: test\n" +
                "\n" +
                "options:\n" +
                "                        \n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithCustomHeaderAndFooter() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(out, 74, "test", options, "My header", "My footer");

        String expected = "My header\n" +
                "usage: test\n" +
                "\n" +
                "options:\n" +
                "\n" +
                "My footer\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithCustomAutoUsage() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(out, 74, "test", options, null, true);

        String expected = "usage: test\n" +
                "test: test [<option>]* [<arg>...]\n" +
                "\n" +
                "options:\n" +
                "\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintHelpWithCustomWidthAndCustomAutoUsage() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(out, 50, "test", options, null, true);

        String expected = "test: test [<option>]* [<arg>...]\n" +
                "\noptions:\n";

        String actual = outContent.toString();
        assertEquals(expected, actual);
    }
}