package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterInstance() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertSame(System.getProperty("line.separator"), formatter.getNewLine());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
        assertNull(formatter.getOptionComparator());
    }

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(6);
        assertEquals(6, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("help: ");
        assertEquals("help: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("-----\n");
        assertEquals("-----\n", formatter.getNewLine());
    }

    @Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("/");
        assertEquals("/", formatter.getDefaultOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--");
        assertEquals("--", formatter.getDefaultLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");
        assertEquals("param", formatter.getDefaultArgName());
    }

    @Test
    public void testSetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 1;
            }
        };
        formatter.setOptionComparator(comparator);
        assertSame(comparator, formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelp_default() {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
        // Verify the output is not empty
        // Add more assertions to validate the output
    }

    @Test
    public void testPrintHelp_withArg() {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
        // Verify the output contains "arg1" and "arg2"
        // Add more assertions to validate the output
    }

    @Test
    public void testPrintHelp_withOptions() {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        options.addOption("a", "optionA", true, "Example option");
        options.addOption("b", "optionB", false, "Another example option");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
        // Verify the output contains "optionA" and "optionB"
        // Add more assertions to validate the output
    }

    @Test
    public void testPrintHelp_withOptionGroup() {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        OptionGroup group = new OptionGroup("Group", "Group options");
        group.setRequired(false);
        Option option1 = new Option("g1", "groupOption1", false, "Option in group 1");
        Option option2 = new Option("g2", "groupOption2", false, "Option in group 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
        // Verify the output contains "groupOption1" and "groupOption2"
        // Add more assertions to validate the output
    }

    @Test
    public void testPrintHelp_withHeaderAndFooter() {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        String header = "MyApp help";
        String footer = "More information";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, header, footer);
        // Verify the output contains "MyApp help" and "More information"
        // Add more assertions to validate the output
    }

    @Test
    public void testPrintHelp_withAutoUsage() {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, true);
        // Verify the output contains the default usage description
        // Add more assertions to validate the output
    }

    @Test
    public void testPrintHelp_withCustomWidth() {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
        // Verify the output is wrapped with a custom width
        // Add more assertions to validate the output
    }

    @Test
    public void testPrintHelp_withCustomSyntaxPrefix() {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        String syntaxPrefix = "MyApp: ";
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix(syntaxPrefix);
        formatter.printHelp(cmdLineSyntax, options);
        // Verify the output contains the custom syntax prefix
        // Add more assertions to validate the output
    }

    @Test
    public void testPrintHelp_withCustomLeftPadding() {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        int leftPadding = 5;
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(leftPadding);
        formatter.printHelp(cmdLineSyntax, options);
        // Verify the output is left-padded with the custom padding
        // Add more assertions to validate the output
    }

    @Test
    public void testPrintHelp_withCustomDescPadding() {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        int descPadding = 6;
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(descPadding);
        formatter.printHelp(cmdLineSyntax, options);
        // Verify the output is desc-padded with the custom padding
        // Add more assertions to validate the output
    }
}