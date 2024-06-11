package org.apache.commons.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    private HelpFormatter formatter;

    @Before
    public void setUp() {
        formatter = new HelpFormatter();
    }

    @After
    public void tearDown() {
        formatter = null;
    }

    @Test
    public void testDefaultInitialization() {
        HelpFormatter helper = new HelpFormatter();
        assertEquals(74, helper.getWidth());
        assertEquals(1, helper.getLeftPadding());
        assertEquals(3, helper.getDescPadding());
        assertEquals("usage: ", helper.getSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), helper.getNewLine());
        assertEquals("-", helper.getOptPrefix());
        assertEquals("--", helper.getLongOptPrefix());
        assertEquals("arg", helper.getArgName());
    }

    @Test
    public void testSetWidth() {
        formatter.setWidth(100);
        assertEquals(100, formatter.getWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWidthWithNegativeValue() {
        formatter.setWidth(-5);
    }

    @Test
    public void testPrintHelpWithDefaults() {
        Options options = new Options();
        String cmdLineSyntax = "java MyClass [args]";
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomSyntaxPrefix() {
        Options options = new Options();
        String cmdLineSyntax = "command myCommand [args]";
        HelpFormatter helper = new HelpFormatter();
        helper.setSyntaxPrefix("Command usage: ");
        helper.printHelp(cmdLineSyntax, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        Options options = new Options();
        HelpFormatter helper = new HelpFormatter();
        helper.printHelp(null, options);
    }

    @Test
    public void testPrintHelpWithCustomHeader() {
        Options options = new Options();
        String cmdLineSyntax = "java MyClass [args]";
        String header = "Options for MyClass";
        HelpFormatter helper = new HelpFormatter();
        helper.setSyntaxPrefix("usage: ");
        helper.setHeader(header);
        helper.printHelp(cmdLineSyntax, options, header);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullHeader() {
        Options options = new Options();
        String cmdLineSyntax = "java MyClass [args]";
        HelpFormatter helper = new HelpFormatter();
        helper.printHelp(cmdLineSyntax, options, null);
    }

    @Test
    public void testPrintHelpWithCustomFooter() {
        Options options = new Options();
        String cmdLineSyntax = "java MyClass [args]";
        String footer = "Help for MyClass";
        HelpFormatter helper = new HelpFormatter();
        helper.setSyntaxPrefix("usage: ");
        helper.setFooter(footer);
        helper.printHelp(cmdLineSyntax, options, null, footer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullFooter() {
        Options options = new Options();
        String cmdLineSyntax = "java MyClass [args]";
        HelpFormatter helper = new HelpFormatter();
        helper.printHelp(cmdLineSyntax, options, null, null);
    }
}