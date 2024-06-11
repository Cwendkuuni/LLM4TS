package org.apache.commons.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        assertNotNull(formatter.getWidth());
        assertEquals(74, formatter.getWidth());
        // other assertions can be added here
    }

    @Test
    public void testSettersAndGetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        assertEquals(100, formatter.getWidth());

        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());

        formatter.setDescPadding(7);
        assertEquals(7, formatter.getDescPadding());

        formatter.setSyntaxPrefix("help: ");
        assertEquals("help: ", formatter.getSyntaxPrefix());

        formatter.setNewLine("\n");
        assertEquals("\n", formatter.getNewLine());

        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getOptPrefix());

        formatter.setLongOptPrefix("--");
        assertEquals("--", formatter.getLongOptPrefix());

        formatter.setArgName("argName");
        assertEquals("argName", formatter.getArgName());

        // other assertions can be added here
    }

    @Test
    public void testPrintHelp() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "alpha option");
        options.addOption("b", "beta", true, "beta option");
        options.addOptionGroup(new OptionGroup("g", "group", "group options", Arrays.asList(
                new Option("c", "choices", true, "choices option with multiple values"),
                new Option("d", "dummy", false, "dummy option"))));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(out, 100, "cmd", options);

        String expectedOutput = "" +
                "----------------------------------------------------------------------------\n" +
                "cmd\n" +
                "----------------------------------------------------------------------------\n" +
                "usage: cmd [options]\n\n" +
                "Options:\n" +
                "   -a, --alpha       alpha option\n" +
                "   -b                 beta option (required)\n" +
                "   --group           group options\n" +
                "       --choices      choices option with multiple values\n" +
                "       --dummy        dummy option\n" +
                "\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintHelpWithHeaderFooter() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "alpha option");
        options.addOption("b", "beta", true, "beta option");
        options.addOptionGroup(new OptionGroup("g", "group", "group options", Arrays.asList(
                new Option("c", "choices", true, "choices option with multiple values"),
                new Option("d", "dummy", false, "dummy option"))));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.setSyntaxPrefix("usage: ");
        formatter.setNewLine("\n");
        formatter.setLeftPadding(5);
        formatter.setDescPadding(5);

        String header = "Header";
        String footer = "Footer";

        formatter.printHelp(out, 80, "cmd", options, header, footer);

        String expectedOutput = "" +
                "----------------------------------------------------------------------------\n" +
                "Header\n" +
                "----------------------------------------------------------------------------\n" +
                "usage: cmd [options]\n\n" +
                "Options:\n" +
                "   -a, --alpha       alpha option\n" +
                "   -b                 beta option (required)\n" +
                "   --group           group options\n" +
                "       --choices      choices option with multiple values\n" +
                "       --dummy        dummy option\n" +
                "----------------------------------------------------------------------------\n" +
                "Footer\n" +
                "\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "alpha option");
        options.addOption("b", "beta", true, "beta option");
        options.addOptionGroup(new OptionGroup("g", "group", "group options", Arrays.asList(
                new Option("c", "choices", true, "choices option with multiple values"),
                new Option("d", "dummy", false, "dummy option"))));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(out, 100, "cmd", options, null, null, true);

        String expectedOutput = "" +
                "usage: cmd [options]\n\n" +
                "Options:\n" +
                "   -a, --alpha       alpha option\n" +
                "   -b                 beta option (required)\n" +
                "   --group            group options\n" +
                "       --choices       choices option with multiple values\n" +
                "       --dummy         dummy option\n" +
                "\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintHelpWithEmptyCmdLineSyntax() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "alpha option");
        options.addOption("b", "beta", true, "beta option");
        options.addOptionGroup(new OptionGroup("g", "group", "group options", Arrays.asList(
                new Option("c", "choices", true, "choices option with multiple values"),
                new Option("d", "dummy", false, "dummy option"))));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(out, 100, "", options);

        String expectedOutput = "" +
                "----------------------------------------------------------------------------\n" +
                "" +
                "----------------------------------------------------------------------------\n" +
                "usage: <command> [options]\n\n" +
                "Options:\n" +
                "   -a, --alpha       alpha option\n" +
                "   -b                 beta option (required)\n" +
                "   --group           group options\n" +
                "       --choices      choices option with multiple values\n" +
                "       --dummy        dummy option\n" +
                "\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(out, 100, "cmd");

        String expectedOutput = "usage: cmd [options]\n\n";
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintUsageWithCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(out, 100, "cmd <arg>");

        String expectedOutput = "usage: cmd [options] <arg>\n\n";
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintOptions() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "alpha option");
        options.addOption("b", "beta", true, "beta option");
        options.addOptionGroup(new OptionGroup("g", "group", "group options", Arrays.asList(
                new Option("c", "choices", true, "choices option with multiple values"),
                new Option("d", "dummy", false, "dummy option"))));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printOptions(out, 100, options);

        String expectedOutput = "" +
                "Options:\n" +
                "   -a, --alpha       alpha option\n" +
                "   -b                 beta option (required)\n" +
                "   --group           group options\n" +
                "       --choices      choices option with multiple values\n" +
                "       --dummy        dummy option\n" +
                "\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintWrapped() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printWrapped(out, 100, "This is a test for HelpFormatter.printWrapped() method.", 5);

        String expectedOutput = "This is a test for HelpFormatter.printWrapped() method.\n" +
                "\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintWrappedWithNextLineTabStop() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printWrapped(out, 100, "This is a test for HelpFormatter.printWrapped() method with next line tab stop.", 15);

        String expectedOutput = "This is a test for HelpFormatter.printWrapped() method with next line tab stop.\n" +
                "\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintWrappedWithLongText() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a very very very very long test text for HelpFormatter.printWrapped() method.";

        formatter.printWrapped(out, 50, text);

        String expectedOutput = "This is a very very very very long test text for HelpFormatter.printWrapped() method.\n" +
                "\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFindWrapPos() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a test for HelpFormatter.findWrapPos() method.\n" +
                "Line 1\n" +
                "Line 2\n" +
                "Line 3\n" +
                "Line 4\n" +
                "Line 5\n" +
                "Line 6\n" +
                "Line 7\n" +
                "Line 8\n" +
                "Line 9\n" +
                "Line 10\n" +
                "Line 11\n" +
                "Line 12\n" +
                "Line 13\n" +
                "Line 14\n" +
                "Line 15\n" +
                "Line 16\n";

        int width = 80;
        int pos = formatter.findWrapPos(text, width, 0);

        String expectedOutput = "Line 1\n" +
                "\n" +
                "This is a test for HelpFormatter.findWrapPos() method.\n" +
                "\n" +
                "----------------------------------------------------------------------------\n" +
                "...\n" +
                "----------------------------------------------------------------------------\n";

        if (pos == -1) {
            expectedOutput += "\n" +
                    "Line 16\n";
        } else {
            expectedOutput += "\n" +
                    "Line " + (pos + 1) + "\n";
        }

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCreatePadding() {
        HelpFormatter formatter = new HelpFormatter();
        int len = 10;

        String padding = formatter.createPadding(len);

        String expectedOutput = new String(new char[len]);

        assertEquals(expectedOutput, padding);
    }

    @Test
    public void testRtrim() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "   This is a test for HelpFormatter.rtrim() method.\n";

        String expectedOutput = "This is a test for HelpFormatter.rtrim() method.";

        String actualOutput = formatter.rtrim(text);

        assertEquals(expectedOutput, actualOutput);
    }
}