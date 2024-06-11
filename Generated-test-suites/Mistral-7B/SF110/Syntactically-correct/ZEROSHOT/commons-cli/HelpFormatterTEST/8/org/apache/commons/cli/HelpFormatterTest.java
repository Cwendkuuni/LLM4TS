package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class HelpFormatterTest {

    @org.junit.Test
    public void testHelpFormatterInstance() {
        HelpFormatter formatter = new HelpFormatter();
        assertNotNull(formatter);
    }

    @org.junit.Test
    public void testDefaultSettings() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertNull(formatter.getNewLine());
        assertEquals("-", formatter.getOptPrefix());
        assertEquals("--", formatter.getLongOptPrefix());
        assertEquals("arg", formatter.getArgName());
        assertNotNull(formatter.getOptionComparator());
    }

    @org.junit.Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @org.junit.Test
    public void testGetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
    }

    @org.junit.Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(4);
        assertEquals(4, formatter.getLeftPadding());
    }

    @org.junit.Test
    public void testGetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(1, formatter.getLeftPadding());
    }

    @org.junit.Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(2);
        assertEquals(2, formatter.getDescPadding());
    }

    @org.junit.Test
    public void testGetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(3, formatter.getDescPadding());
    }

    @org.junit.Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("myPrefix ");
        assertEquals("myPrefix ", formatter.getSyntaxPrefix());
    }

    @org.junit.Test
    public void testGetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("usage: ", formatter.getSyntaxPrefix());
    }

    @org.junit.Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        String newLine = System.lineSeparator();
        formatter.setNewLine(newLine);
        assertEquals(newLine, formatter.getNewLine());
    }

    @org.junit.Test
    public void testGetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
    }

    @org.junit.Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getOptPrefix());
    }

    @org.junit.Test
    public void testGetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("-", formatter.getOptPrefix());
    }

    @org.junit.Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--long-");
        assertEquals("--long-", formatter.getLongOptPrefix());
    }

    @org.junit.Test
    public void testGetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("--", formatter.getLongOptPrefix());
    }

    @org.junit.Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("argument");
        assertEquals("argument", formatter.getArgName());
    }

    @org.junit.Test
    public void testGetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("arg", formatter.getArgName());
    }

    @org.junit.Test
    public void testGetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(formatter.getOptionComparator(), formatter.getOptionComparator());
    }

    @org.junit.Test
    public void testSetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(Collections.reverseOrder());
        assertSame(Collections.reverseOrder(), formatter.getOptionComparator());
    }

    @org.junit.Test
    public void testPrintHelpWithDefaults() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "MyProgram [options]";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomSettings() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(2);
        formatter.setSyntaxPrefix("help: ");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--long-");
        formatter.setArgName("arg");
        String cmdLineSyntax = "MyProgram [options]";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testPrintHelpWithOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "MyProgram [options]";
        Options options = new Options();
        Option opt = new Option("opt", "option", false, "Short description");
        Option longOpt = new Option("long-opt", "long option", true, "Long description");
        Option argOpt = new Option("arg", "an argument", true, "Argument description");
        options.addOption(opt);
        options.addOption(longOpt);
        options.addOption(argOpt);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomSettingsAndOptions() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(2);
        formatter.setSyntaxPrefix("help: ");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--long-");
        formatter.setArgName("arg");
        String cmdLineSyntax = "MyProgram [options]";
        Options options = new Options();
        Option opt = new Option("opt", "option", false, "Short description");
        Option longOpt = new Option("long-opt", "long option", true, "Long description");
        Option argOpt = new Option("arg", "an argument", true, "Argument description");
        options.addOption(opt);
        options.addOption(longOpt);
        options.addOption(argOpt);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomSettingsAndEmptyCommandLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(2);
        formatter.setSyntaxPrefix("help: ");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--long-");
        formatter.setArgName("arg");
        String cmdLineSyntax = "";
        Options options = new Options();
        Option opt = new Option("opt", "option", false, "Short description");
        Option longOpt = new Option("long-opt", "long option", true, "Long description");
        Option argOpt = new Option("arg", "an argument", true, "Argument description");
        options.addOption(opt);
        options.addOption(longOpt);
        options.addOption(argOpt);
        try {
            formatter.printHelp(cmdLineSyntax, options);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testPrintHelpWithEmptyOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "MyProgram [options]";
        Options options = new Options();
        try {
            formatter.printHelp(cmdLineSyntax, options);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testPrintHelpWithCustomSettingsAndCustomPrintHelpMethod() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(2);
        formatter.setSyntaxPrefix("help: ");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--long-");
        formatter.setArgName("arg");
        String cmdLineSyntax = "MyProgram [options]";
        Options options = new Options();
        Option opt = new Option("opt", "option", false, "Short description");
        Option longOpt = new Option("long-opt", "long option", true, "Long description");
        Option argOpt = new Option("arg", "an argument", true, "Argument description");
        options.addOption(opt);
        options.addOption(longOpt);
        options.addOption(argOpt);
        PrintStream out = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        formatter.printHelp(printStream, 50, cmdLineSyntax, null, options, 5, 2, null, false);
        printStream.close();
        String output = baos.toString();
        String expected = "help: MyProgram [options]\n" +
                "OPTIONS:\n" +
                "--opt         Short description\n" +
                "--long-opt    Long description\n" +
                "--arg         Argument description\n";
        assertEquals(expected, output);
        System.setOut(out);
    }
}