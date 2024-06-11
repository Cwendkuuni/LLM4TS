package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testDefaultSettings() {
        HelpFormatter formatter = new HelpFormatter();
        int width = formatter.getWidth();
        int leftPad = formatter.getLeftPadding();
        int descPad = formatter.getDescPadding();
        String syntaxPrefix = formatter.getSyntaxPrefix();
        String newLine = formatter.getNewLine();
        String optPrefix = formatter.getOptPrefix();
        String longOptPrefix = formatter.getLongOptPrefix();
        String argName = formatter.getArgName();
        Comparator optionComparator = formatter.getOptionComparator();

        assertEquals(74, width);
        assertEquals(1, leftPad);
        assertEquals(3, descPad);
        assertEquals("usage: ", syntaxPrefix);
        assertNotNull(newLine);
        assertEquals("-", optPrefix);
        assertEquals("--", longOptPrefix);
        assertEquals("arg", argName);
        assertInstanceof(OptionComparator.class, optionComparator);
    }

    @Test
    public void testSettingWidth() {
        HelpFormatter formatter = new HelpFormatter();
        int width = 50;
        formatter.setWidth(width);
        assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testSettingLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        int padding = 5;
        formatter.setLeftPadding(padding);
        assertEquals(5, formatter.getLeftPadding());
    }

    @Test
    public void testSettingDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        int padding = 7;
        formatter.setDescPadding(padding);
        assertEquals(7, formatter.getDescPadding());
    }

    @Test
    public void testSettingSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        String prefix = "myUsage: ";
        formatter.setSyntaxPrefix(prefix);
        assertEquals("myUsage: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSettingNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        String newline = "\n-----------------------------\n";
        formatter.setNewLine(newline);
        assertEquals(newline, formatter.getNewLine());
    }

    @Test
    public void testSettingOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        String prefix = "-X";
        formatter.setOptPrefix(prefix);
        assertEquals("-X", formatter.getOptPrefix());
    }

    @Test
    public void testSettingLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        String prefix = "--foo";
        formatter.setLongOptPrefix(prefix);
        assertEquals("--foo", formatter.getLongOptPrefix());
    }

    @Test
    public void testSettingArgName() {
        HelpFormatter formatter = new HelpFormatter();
        String name = "param";
        formatter.setArgName(name);
        assertEquals("param", formatter.getArgName());
    }

    @Test
    public void testSettingOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        Comparator comparator = new MyComparator();
        formatter.setOptionComparator(comparator);
        assertInstanceof(MyComparator.class, formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelpWithDefaults() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomSettings() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(7);
        formatter.setSyntaxPrefix("myUsage: ");
        formatter.setNewLine("\n-----------------------------\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("param");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptions() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        options.addRequiredOption("i", "input", false, "Input file path");
        options.addOption("o", "output", true, "Output file path");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptionGroups() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption(new Option("g", "global", false, "Apply globally"));
        group.addOption(new Option("l", "local", false, "Apply locally"));
        options.addOption("v", "verbose", false, "Print verbose messages");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    private static class MyComparator implements Comparator<Option> {
        public int compare(Option o1, Option o2) {
            return o1.getKey().compareToIgnoreCase(o2.getKey());
        }
    }
}