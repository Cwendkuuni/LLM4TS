package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterDefaults() {
        HelpFormatter formatter = new HelpFormatter();
        assertNotNull(formatter);
        assertEquals(74, formatter.getDefaultWidth());
        assertEquals(1, formatter.getDefaultLeftPad());
        assertEquals(3, formatter.getDefaultDescPad());
        assertEquals("usage: ", formatter.getDefaultSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), formatter.getDefaultNewLine());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
    }

    @Test
    public void testHelpFormatterCustomization() {
        HelpFormatter formatter = new HelpFormatter();

        formatter.setWidth(50);
        formatter.setLeftPadding(2);
        formatter.setDescPadding(5);
        formatter.setSyntaxPrefix("help: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("option");

        assertEquals(50, formatter.getDefaultWidth());
        assertEquals(2, formatter.getDefaultLeftPad());
        assertEquals(5, formatter.getDefaultDescPad());
        assertEquals("help: ", formatter.getDefaultSyntaxPrefix());
        assertEquals("\n", formatter.getDefaultNewLine());
        assertEquals("--", formatter.getDefaultOptPrefix());
        assertEquals("-", formatter.getDefaultLongOptPrefix());
        assertEquals("option", formatter.getDefaultArgName());
    }

    @Test
    public void testPrintHelpDefaults() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, null, "Option A");
        options.addOption("b", true, "10", "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1", Arrays.asList(new Option("g1", false, null, "Group 1 Option"))));
        options.addOptionGroup(new OptionGroup("group2", "Group 2", Arrays.asList(new Option("g2", false, null, "Group 2 Option"))));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("SampleApp", options);
    }

    @Test
    public void testPrintHelpCustomization() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, null, "Option A");
        options.addOption("b", true, "10", "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1", Arrays.asList(new Option("g1", false, null, "Group 1 Option"))));
        options.addOptionGroup(new OptionGroup("group2", "Group 2", Arrays.asList(new Option("g2", false, null, "Group 2 Option"))));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(2);
        formatter.setDescPadding(5);
        formatter.setSyntaxPrefix("help: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("option");

        formatter.printHelp("SampleApp", options);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, null, "Option A");
        options.addOption("b", true, "10", "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1", Arrays.asList(new Option("g1", false, null, "Group 1 Option"))));
        options.addOptionGroup(new OptionGroup("group2", "Group 2", Arrays.asList(new Option("g2", false, null, "Group 2 Option"))));

        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("SampleApp", options, "MyApplication header.", "MyApplication footer.");
    }

    @Test
    public void testPrintHelpWithAutoUsage() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, null, "Option A");
        options.addOption("b", true, "10", "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1", Arrays.asList(new Option("g1", false, null, "Group 1 Option"))));
        options.addOptionGroup(new OptionGroup("group2", "Group 2", Arrays.asList(new Option("g2", false, null, "Group 2 Option"))));

        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("SampleApp", options, null, null, true);
    }
}