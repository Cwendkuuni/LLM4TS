package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class HelpFormatterTest {

    private HelpFormatter formatter;

    @Before
    public void setup() {
        formatter = new HelpFormatter();
    }

    @Test
    public void testDefaultProperties() {
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
    public void testSetWidth() {
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        formatter.setDescPadding(7);
        assertEquals(7, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        formatter.setSyntaxPrefix("help: ");
        assertEquals("help: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        String newline = System.getProperty("line.separator");
        formatter.setNewLine(newline);
        assertEquals(newline, formatter.getNewLine());
    }

    @Test
    public void testSetOptPrefix() {
        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        formatter.setLongOptPrefix("-");
        assertEquals("-", formatter.getLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        formatter.setArgName("param");
        assertEquals("param", formatter.getArgName());
    }

    @Test
    public void testSetOptionComparator() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };
        formatter.setOptionComparator(comparator);
        assertSame(comparator, formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelpWithDefaults() {
        String cmdLineSyntax = "java MyClass";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomProperties() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        formatter.setWidth(50);
        formatter.setLeftPadding(10);
        formatter.setDescPadding(5);
        formatter.setSyntaxPrefix("help: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("param");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptions() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        Option opt = new Option("o", "opt", true, "Option description");
        options.addOption(opt);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptionGroup() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "group");
        Option opt1 = new Option("o1", "opt1", true, "Option 1 description", group);
        Option opt2 = new Option("o2", "opt2", true, "Option 2 description", group);
        group.addOption(opt1);
        group.addOption(opt2);
        options.addOptionGroup(group);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithMultipleOptionsAndGroups() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("g1", "group1");
        Option opt1 = new Option("o1", "opt1", true, "Option 1 description", group1);
        Option opt2 = new Option("o2", "opt2", true, "Option 2 description", group1);
        group1.addOption(opt1);
        group1.addOption(opt2);
        OptionGroup group2 = new OptionGroup("g2", "group2");
        Option opt3 = new Option("o3", "opt3", true, "Option 3 description", group2);
        Option opt4 = new Option("o4", "opt4", true, "Option 4 description", group2);
        group2.addOption(opt3);
        group2.addOption(opt4);
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomizedOptionsAndGroups() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "group");
        Option opt1 = new Option("o1", "opt1", true, "Option 1 description", group);
        opt1.setRequired(false);
        Option opt2 = new Option("o2", "opt2", true, "Option 2 description", group);
        opt2.setLongOpt("longOpt2");
        group.addOption(opt1);
        group.addOption(opt2);
        Option opt3 = new Option("o3", "opt3", true, "Option 3 description");
        opt3.setOpt("o3s");
        opt3.setArgName("arg3");
        opt3.setDescription("Option 3 description with arg");
        options.addOptionGroup(group);
        options.addOption(opt3);
        formatter.setWidth(50);
        formatter.setLeftPadding(10);
        formatter.setDescPadding(5);
        formatter.setSyntaxPrefix("help: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("param");
        formatter.printHelp(cmdLineSyntax, options);
    }
}