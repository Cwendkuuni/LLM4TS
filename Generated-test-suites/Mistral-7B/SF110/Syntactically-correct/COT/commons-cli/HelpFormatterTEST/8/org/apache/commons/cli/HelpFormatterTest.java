package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class HelpFormatterTest {

    

    @Test
    public void testHelpFormatterConstruction() {
        HelpFormatter formatter = new HelpFormatter();
        assertNotNull(formatter);
    }

    @Test
    public void testDefaultSettings() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getDefaultWidth());
        assertEquals(1, formatter.getDefaultLeftPad());
        assertEquals(3, formatter.getDefaultDescPad());
        assertEquals("usage: ", formatter.getDefaultSyntaxPrefix());
        assertEquals("", formatter.getDefaultNewLine());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
    }

    @Test
    public void testSetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(7);
        formatter.setSyntaxPrefix("help: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("argName");
        assertEquals(100, formatter.getDefaultWidth());
        assertEquals(5, formatter.getDefaultLeftPad());
        assertEquals(7, formatter.getDefaultDescPad());
        assertEquals("help: ", formatter.getDefaultSyntaxPrefix());
        assertEquals("\n", formatter.getDefaultNewLine());
        assertEquals("--", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("argName", formatter.getDefaultArgName());
    }

    @Test
    public void testPrintHelpWithDefaults() {
        String cmdLineSyntax = "java MyClass";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomSettings() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.setLeftPadding(3);
        formatter.setDescPadding(5);
        formatter.setSyntaxPrefix("Options: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("argument");
        formatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptions() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOptionGroup(new OptionGroup("group", "Group Options", Arrays.asList(
                new Option("g", "Group Option 1"),
                new Option("h", "Group Option 2")
        )));
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.setLeftPadding(3);
        formatter.setDescPadding(5);
        formatter.setSyntaxPrefix("Options: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("argument");
        String header = "My Class Help";
        String footer = "For more information, visit myclass.com";
        formatter.printHelp(System.out, cmdLineSyntax, options, header, footer);
    }

    @Test
    public void testPrintHelpWithOutputStream() throws IOException {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        OutputStream out = new ByteArrayOutputStream();
        formatter.printHelp(out, cmdLineSyntax, options);
        String help = out.toString();
        assertTrue(help.contains("Options:"));
        assertTrue(help.contains("Option A"));
        assertTrue(help.contains("Option B"));
        assertTrue(help.contains("Group Options"));
        assertTrue(help.contains("Group Option 1"));
        assertTrue(help.contains("Group Option 2"));
    }

    
}