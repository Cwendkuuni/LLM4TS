package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;

public class HelpFormatterTest {

    @org.junit.Before
    public void setUp() {
        HelpFormatter formatter = new HelpFormatter();
    }

    @org.junit.Test
    public void testDefaultConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertNull(formatter.getNewLine());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
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
        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());
    }

    @org.junit.Test
    public void testGetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(1, formatter.getLeftPadding());
    }

    @org.junit.Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(7);
        assertEquals(7, formatter.getDescPadding());
    }

    @org.junit.Test
    public void testGetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(3, formatter.getDescPadding());
    }

    @org.junit.Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("help: ");
        assertEquals("help: ", formatter.getSyntaxPrefix());
    }

    @org.junit.Test
    public void testGetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("usage: ", formatter.getSyntaxPrefix());
    }

    @org.junit.Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("-----\n");
        assertEquals("-----\n", formatter.getNewLine());
    }

    @org.junit.Test
    public void testGetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        assertNull(formatter.getNewLine());
    }

    @org.junit.Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getDefaultOptPrefix());
    }

    @org.junit.Test
    public void testGetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("-", formatter.getDefaultOptPrefix());
    }

    @org.junit.Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--long-");
        assertEquals("--long-", formatter.getDefaultLongOptPrefix());
    }

    @org.junit.Test
    public void testGetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("--", formatter.getDefaultLongOptPrefix());
    }

    @org.junit.Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("value");
        assertEquals("value", formatter.getDefaultArgName());
    }

    @org.junit.Test
    public void testGetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("arg", formatter.getDefaultArgName());
    }

    @org.junit.Test
    public void testPrintHelp() throws ParseException {
        Options options = new Options();
        options.addOption(true, "help", "print help message");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyApp", options);
    }

    @org.junit.Test
    public void testPrintHelpWithOptions() throws ParseException {
        Options options = new Options();
        options.addOption(true, "help", "print help message");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyApp", options, true);
    }

    @org.junit.Test
    public void testPrintHelpWithHeaderFooter() throws ParseException {
        Options options = new Options();
        options.addOption(true, "help", "print help message");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyApp", options, "Header\nFooter:", true);
    }

    @org.junit.Test
    public void testPrintHelpWithAutoUsage() throws ParseException {
        Options options = new Options();
        options.addOption(true, "help", "print help message");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyApp", options, true, false);
    }

    @org.junit.Test
    public void testPrintHelpWithPrintWriter() throws ParseException {
        Options options = new Options();
        options.addOption(true, "help", "print help message");
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        formatter.printHelp(pw, 80, "MyApp", options, null);
        pw.flush();
        String output = writer.getBuffer().toString();
        assertTrue(output.contains("MyApp"));
        assertTrue(output.contains("--help"));
        assertTrue(output.contains("print help message"));
    }

    @org.junit.Test
    public void testPrintUsage() throws ParseException {
        Options options = new Options();
        options.addOption(true, "help", "print help message");
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        formatter.printUsage(pw, 80, "MyApp", options);
        pw.flush();
        String output = writer.getBuffer().toString();
        assertTrue(output.contains("usage: MyApp"));
        assertTrue(output.contains("--help"));
        assertTrue(output.contains("print help message"));
    }

    @org.junit.Test
    public void testPrintUsageWithAutoUsage() throws ParseException {
        Options options = new Options();
        options.addOption(true, "help", "print help message");
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        formatter.printUsage(pw, 80, "MyApp", options, true);
        pw.flush();
        String output = writer.getBuffer().toString();
        assertTrue(output.contains("MyApp"));
        assertTrue(output.contains("--help"));
        assertTrue(output.contains("print help message"));
    }

    @org.junit.Test
    public void testPrintUsageWithHeaderFooter() throws ParseException {
        Options options = new Options();
        options.addOption(true, "help", "print help message");
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        formatter.printUsage(pw, 80, "MyApp", options, "Header\nFooter:", true);
        pw.flush();
        String output = writer.getBuffer().toString();
        assertTrue(output.contains("MyApp"));
        assertTrue(output.contains("--help"));
        assertTrue(output.contains("print help message"));
        assertTrue(output.contains("Header"));
        assertTrue(output.contains("Footer"));
    }

    @org.junit.Test
    public void testPrintWrappedText() {
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        String text = "This text contains some long words which need to be wrapped";
        formatter.printWrapped(pw, 50, text);
        pw.flush();
        String output = writer.getBuffer().toString();
        String expected = "This text contains some long words which need to be\nwrapped";
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testRenderOptions() {
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        Options options = new Options();
        options.addOption(new Option("a", "apple", true, "Sets apple option"));
        options.addOption(new Option("b", "banana", false, "Sets banana option"));
        options.addOption(new Option("c", null, false, "Sets car option"));
        options.addOptionGroup(new OptionGroup("group", "Group options", 1, "g", "Sets group options"));
        options.addOption(new Option("d", "dog", false, "Sets dog option", true, "Dog group"));
        options.addOption(new Option("e", null, false, "Sets egg option", 2, "Egg group"));
        formatter.printOptions(pw, 80, options, 5, 5);
        pw.flush();
        String output = writer.getBuffer().toString();
        String expected =
                "   -a      Sets apple option\n" +
                "   -b                   Sets banana option\n" +
                "   -c                   Sets car option\n" +
                "   --group                Group options:\n" +
                "       --d,--dog                 Sets dog option\n" +
                "       --e,--egg          Egg group:\n";
        assertEquals(expected, output);
    }
}