package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class HelpFormatterTest {

    private HelpFormatter formatter;

    @Before
    public void setUp() {
        formatter = new HelpFormatter();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(formatter);
        assertEquals(HelpFormatter.DEFAULT_WIDTH, formatter.getWidth());
        assertEquals(HelpFormatter.DEFAULT_LEFT_PAD, formatter.getLeftPadding());
        assertEquals(HelpFormatter.DEFAULT_DESC_PAD, formatter.getDescPadding());
        assertEquals(HelpFormatter.DEFAULT_SYNTAX_PREFIX, formatter.getSyntaxPrefix());
        assertEquals(HelpFormatter.DEFAULT_NEW_LINE, formatter.getNewLine());
        assertEquals(HelpFormatter.DEFAULT_OPT_PREFIX, formatter.getOptPrefix());
        assertEquals(HelpFormatter.DEFAULT_LONG_OPT_PREFIX, formatter.getLongOptPrefix());
        assertEquals(HelpFormatter.DEFAULT_ARG_NAME, formatter.getArgName());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testSetWidth() {
        formatter.setWidth(120);
        assertEquals(120, formatter.getWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWidthNegative() {
        formatter.setWidth(-10);
    }

    @Test
    public void testPrintHelpWithDefaults() {
        String cmdLineSyntax = "java MyClass";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomOptions() {
        String cmdLineSyntax = "java MyClass [arg1] [arg2]";
        Options options = new Options();
        Option opt1 = new Option("o", "opt1", true, "Option 1");
        Option opt2 = new Option("p", "opt2", false, "Option 2");
        Option optGroup = new OptionGroup("group", "Group options", 1, "Options in group");
        optGroup.addOption(opt1);
        optGroup.addOption(opt2);
        options.addOptionGroup(optGroup);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomSettings() {
        String cmdLineSyntax = "java MyClass [arg1] [arg2]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(6);
        formatter.setSyntaxPrefix("usage: myapp ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("arg");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithNullCmdLineSyntax() {
        Options options = new Options();
        try {
            formatter.printHelp(null, options);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPrintHelpWithNullOptions() {
        String cmdLineSyntax = "java MyClass [arg1] [arg2]";
        try {
            formatter.printHelp(cmdLineSyntax, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}