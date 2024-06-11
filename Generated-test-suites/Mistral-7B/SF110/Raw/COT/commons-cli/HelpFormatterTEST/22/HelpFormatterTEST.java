package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HelpFormatterTest {

    private static final int DEFAULT_WIDTH = HelpFormatter.DEFAULT_WIDTH;
    private static final int DEFAULT_LEFT_PAD = HelpFormatter.DEFAULT_LEFT_PAD;
    private static final int DEFAULT_DESC_PAD = HelpFormatter.DEFAULT_DESC_PAD;
    private static final String DEFAULT_SYNTAX_PREFIX = HelpFormatter.DEFAULT_SYNTAX_PREFIX;
    private static final String DEFAULT_OPT_PREFIX = HelpFormatter.DEFAULT_OPT_PREFIX;
    private static final String DEFAULT_LONG_OPT_PREFIX = HelpFormatter.DEFAULT_LONG_OPT_PREFIX;
    private static final String DEFAULT_ARG_NAME = HelpFormatter.DEFAULT_ARG_NAME;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        formatter = new HelpFormatter();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(DEFAULT_WIDTH, formatter.getWidth());
        assertEquals(DEFAULT_LEFT_PAD, formatter.getLeftPadding());
        assertEquals(DEFAULT_DESC_PAD, formatter.getDescPadding());
        assertEquals(DEFAULT_SYNTAX_PREFIX, formatter.getSyntaxPrefix());
        assertEquals(DEFAULT_OPT_PREFIX, formatter.getOptPrefix());
        assertEquals(DEFAULT_LONG_OPT_PREFIX, formatter.getLongOptPrefix());
        assertEquals(DEFAULT_ARG_NAME, formatter.getArgName());
    }

    @Test
    public void testSetters() {
        formatter.setWidth(100);
        assertEquals(100, formatter.getWidth());

        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());

        formatter.setDescPadding(10);
        assertEquals(10, formatter.getDescPadding());

        formatter.setSyntaxPrefix("help: ");
        assertEquals("help: ", formatter.getSyntaxPrefix());

        formatter.setNewLine("\n");
        assertEquals("\n", formatter.getNewLine());

        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getOptPrefix());

        formatter.setLongOptPrefix("--long-");
        assertEquals("--long-", formatter.getLongOptPrefix());

        formatter.setArgName("argName");
        assertEquals("argName", formatter.getArgName());
    }

    @Test
    public void testPrintHelp_SimpleUsage() throws ParseException {
        final String cmdLineSyntax = "java MyClass [args]";
        final Options options = new Options();
        options.addOption("a", "arg1", true, "Sample argument");
        options.addOption("h", "help", false, "Print this help.");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outContent);
        formatter.printHelp(printStream, DEFAULT_WIDTH, cmdLineSyntax, options, null, false);
        String help = outContent.toString();

        String expectedHelp = "usage: java MyClass [args]\n" +
                " options:\n" +
                "   -a, --arg1\n" +
                "       Sample argument.\n" +
                "   -h, --help\n" +
                "       Print this help.";

        assertEquals(expectedHelp, help);
    }

    @Test
    public void testPrintHelp_ComplexUsage() throws ParseException {
        final String cmdLineSyntax = "java MyClass [option1] [option2] [arg1] [arg2]";
        final Options options = new Options();
        options.addOption("o1", "option1", false, "Option 1.");
        options.addOption("o2", null, true, "Option 2.");
        options.addOption("a1", null, true, "Argument 1.");
        options.addOption("a2", "arg2", false, "Argument 2.");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outContent);
        formatter.printHelp(printStream, DEFAULT_WIDTH, cmdLineSyntax, options, null, false);
        String help = outContent.toString();

        String expectedHelp = "usage: java MyClass [option1] [option2] [arg1] [arg2]\n" +
                " options:\n" +
                "   -o1, --option1\n" +
                "       Option 1.\n" +
                "   -o2\n" +
                "       Option 2.\n" +
                "   -a1\n" +
                "       Argument 1.\n" +
                "   -a2, --arg2\n" +
                "       Argument 2.";

        assertEquals(expectedHelp, help);
    }

    @Test
    public void testPrintHelp_AutoUsage() throws ParseException {
        final String cmdLineSyntax = "java MyClass [args]";
        final Options options = new Options();
        options.addOption("a", "arg1", true, "Sample argument");
        options.addOption("h", "help", false, "Print this help.");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outContent);
        formatter.printHelp(printStream, DEFAULT_WIDTH, cmdLineSyntax, options, null, true);
        String help = outContent.toString();

        String expectedHelp = "usage: java MyClass [args]\n" +
                " options:\n" +
                "   -a, --arg1\n" +
                "       Sample argument.\n" +
                "   -h, --help\n" +
                "       Print this help.";

        assertEquals(expectedHelp, help);
    }

    @Test
    public void testPrintHelp_WithHeaderAndFooter() throws ParseException {
        final String cmdLineSyntax = "java MyClass [args]";
        final Options options = new Options();
        options.addOption("a", "arg1", true, "Sample argument");
        options.addOption("h", "help", false, "Print this help.");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outContent);

        String header = "My Command Line Tool";
        String footer = "For more information, visit www.example.com";

        formatter.printHelp(printStream, DEFAULT_WIDTH, cmdLineSyntax, null, header, footer, false);
        String help = outContent.toString();

        String expectedHelp = header + "\n" +
                usage + "\n" +
                footer;

        assertEquals(expectedHelp, help);
    }

    @Test
    public void testPrintHelp_Empty() {
        final String cmdLineSyntax = "";
        final Options options = new Options();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outContent);
        formatter.printHelp(printStream, DEFAULT_WIDTH, cmdLineSyntax, options, null, false);
        String help = outContent.toString();

        assertEquals("", help);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelp_NullCmdLineSyntax() {
        final Options options = new Options();
        formatter.printHelp(null, DEFAULT_WIDTH, null, options, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelp_NullOptions() {
        final String cmdLineSyntax = "java MyClass [args]";
        formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, null, null, false);
    }
}