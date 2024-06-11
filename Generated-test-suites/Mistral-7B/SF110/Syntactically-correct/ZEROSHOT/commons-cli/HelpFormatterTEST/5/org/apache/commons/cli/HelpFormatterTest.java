package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class HelpFormatterTest {

    @org.junit.Before
    public void setUp() {
        HelpFormatter formatter = new HelpFormatter();
    }

    @org.junit.Test
    public void testDefaultConstruction() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
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
        formatter.setNewLine("\n------------------\n");
        assertEquals("\n------------------\n", formatter.getNewLine());
    }

    @org.junit.Test
    public void testGetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
    }

    @org.junit.Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("-h");
        assertEquals("-h", formatter.getOptPrefix());
    }

    @org.junit.Test
    public void testGetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("-", formatter.getOptPrefix());
    }

    @org.junit.Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--opt");
        assertEquals("--opt", formatter.getLongOptPrefix());
    }

    @org.junit.Test
    public void testGetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("--", formatter.getLongOptPrefix());
    }

    @org.junit.Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");
        assertEquals("param", formatter.getArgName());
    }

    @org.junit.Test
    public void testGetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("arg", formatter.getArgName());
    }

    @org.junit.Test
    public void testPrintHelpWithDefaults() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testPrintHelpWithWidth() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options, 50);
    }

    @org.junit.Test
    public void testPrintHelpWithHeader() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        String header = "My App Help";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, header, options);
    }

    @org.junit.Test
    public void testPrintHelpWithFooter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        String footer = "Copyright (C) 2023 MyCompany";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options, null, footer);
    }

    @org.junit.Test
    public void testPrintHelpWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomFormatter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        PrintWriter customOutput = new PrintWriter(System.out);
        formatter.printHelp(customOutput, 50, cmdLineSyntax, null, options, 5, 7, null, false);
        customOutput.flush();
    }

    @org.junit.Test
    public void testPrintUsageWithDefaults() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        formatter.printUsage(System.out, 50, cmdLineSyntax);
    }

    @org.junit.Test
    public void testPrintUsageWithCustomSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        formatter.printUsage(System.out, 50, cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testPrintOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");
        options.addOption("v", "verbose", true, "Print verbose output.");
        formatter.printHelp(System.out, 50, cmdLineSyntax, null, options, 5, 7);
    }

    @org.junit.Test
    public void testPrintWrappedText() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a very long line of text that needs to be wrapped.";
        PrintWriter customOutput = new PrintWriter(System.out);
        formatter.printWrapped(customOutput, 40, text);
        customOutput.flush();
    }

    @org.junit.Test
    public void testFindWrapPos() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a very long line of text that needs to be wrapped.";
        int width = 30;
        int startPos = 0;
        int pos = formatter.findWrapPos(text, width, startPos);
        assertEquals(-1, pos);
        text += "This is another long line.";
        pos = formatter.findWrapPos(text, width, startPos);
        assertEquals(32, pos);
    }

    @org.junit.Test
    public void testCreatePadding() {
        HelpFormatter formatter = new HelpFormatter();
        String padding = formatter.createPadding(10);
        assertEquals("         ", padding);
    }

    @org.junit.Test
    public void testRenderWrappedText() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a very long line of text that needs to be wrapped.";
        PrintWriter customOutput = new PrintWriter(System.out);
        formatter.renderWrappedText(customOutput, 40, 0, text);
        customOutput.flush();
    }

    @org.junit.Test
    public void testRenderOptions() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");
        options.addOption("v", "verbose", true, "Print verbose output.");
        StringBuffer sb = new StringBuffer();
        formatter.renderOptions(sb, 50, options, 5, 7);
        String expected = "   | Option:            Description:\n" +
                          "   | ---------------    ---------------:\n" +
                          "   | -h, --help        Print this help message.\n" +
                          "   | -v, --verbose      Print verbose output.\n";
        assertEquals(expected, sb.toString());
    }
}