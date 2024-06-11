package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testDefaultValues() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertNull(formatter.getNewLine());
        assertEquals("-", formatter.getOptPrefix());
        assertEquals("--", formatter.getLongOptPrefix());
        assertEquals("arg", formatter.getArgName());
        assertSame(new OptionComparator(), formatter.getOptionComparator());
    }

    @Test
    public void testSetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(10);
        formatter.setSyntaxPrefix("help: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("-x");
        formatter.setLongOptPrefix("--x");
        formatter.setArgName("param");
        formatter.setOptionComparator(Collections.reverseOrder());

        assertEquals(50, formatter.getWidth());
        assertEquals(5, formatter.getLeftPadding());
        assertEquals(10, formatter.getDescPadding());
        assertEquals("help: ", formatter.getSyntaxPrefix());
        assertEquals("\n", formatter.getNewLine());
        assertEquals("-x", formatter.getOptPrefix());
        assertEquals("--x", formatter.getLongOptPrefix());
        assertEquals("param", formatter.getArgName());
        assertSame(Collections.reverseOrder(), formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelpWithDefaults() {
        String cmdLineSyntax = "java MyClass";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithMinimalOptions() {
        String cmdLineSyntax = "java MyClass [<arg>]";
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "an argument");
        options.addOption(opt);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptions() {
        String cmdLineSyntax = "java MyClass [<arg>]";
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "an argument");
        opt.setRequired(true);
        Option opt2 = new Option("b", "bool", false, "a boolean");
        OptionGroup group = new OptionGroup("group", "A group", 1);
        group.addOption(new Option("c", "char", false, "a character"));
        group.addOption(new Option("d", "digit", false, "a digit"));
        group.addOption(new Option("e", "error", false, "an error"));
        options.addOption(opt);
        options.addOption(opt2);
        options.addOptionGroup(group);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithDifferentWidths() {
        String cmdLineSyntax = "java MyClass [<arg>]";
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "an argument");
        Option opt2 = new Option("b", "bool", false, "a boolean");
        OptionGroup group = new OptionGroup("group", "A group", 1);
        group.addOption(new Option("c", "char", false, "a character"));
        group.addOption(new Option("d", "digit", false, "a digit"));
        group.addOption(new Option("e", "error", false, "an error"));
        options.addOption(opt);
        options.addOption(opt2);
        options.addOptionGroup(group);

        HelpFormatter formatterShort = new HelpFormatter(30);
        formatterShort.printHelp(cmdLineSyntax, options);

        HelpFormatter formatterMedium = new HelpFormatter(60);
        formatterMedium.printHelp(cmdLineSyntax, options);

        HelpFormatter formatterLong = new HelpFormatter(120);
        formatterLong.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        String cmdLineSyntax = "java MyClass [<arg>]";
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "an argument");
        opt.setRequired(true);
        options.addOption(opt);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        String cmdLineSyntax = "java MyClass [<arg>]";
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "an argument");
        opt.setRequired(true);
        options.addOption(opt);
        String header = "Usage: MyClass [<arg>]";
        String footer = "Copyright (C) 2023 MyCompany";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, header, footer);
    }

    @Test
    public void testPrintHelpWithCustomNewline() {
        String cmdLineSyntax = "java MyClass [<arg>]";
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "an argument");
        opt.setRequired(true);
        options.addOption(opt);
        String newline = "\n\n";
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine(newline);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomOptPrefixAndLongOptPrefix() {
        String cmdLineSyntax = "java MyClass [<arg>]";
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "an argument");
        opt.setRequired(true);
        options.addOption(opt);
        String optPrefix = "opt:";
        String longOptPrefix = "--long-opt:";
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix(optPrefix);
        formatter.setLongOptPrefix(longOptPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomArgName() {
        String cmdLineSyntax = "java MyClass [<arg>]";
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "an argument");
        opt.setRequired(true);
        options.addOption(opt);
        String argName = "value";
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName(argName);
        formatter.printHelp(cmdLineSyntax, options);
    }
}