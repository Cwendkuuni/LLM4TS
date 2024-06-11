package org.apache.commons.cli; // replace with your package name if different

import static org.junit.Assert.*;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class HelpFormatterTest {

    private HelpFormatter formatter;

    @Before
    public void setUp() {
        formatter = new HelpFormatter();
    }

    @Test
    public void testDefaultValues() {
        assertEquals(74, formatter.getDefaultWidth());
        assertEquals(1, formatter.getDefaultLeftPad());
        assertEquals(3, formatter.getDefaultDescPad());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertEquals("", formatter.getNewLine());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
    }

    @Test
    public void testSetWidth() {
        formatter.setWidth(100);
        assertEquals(100, formatter.getDefaultWidth());
    }

    @Test
    public void testGetWidth() {
        formatter.setWidth(150);
        assertEquals(150, formatter.getWidth());
    }

    @Test
    public void testPrintHelpWithDefaults() {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        formatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomValues() {
        String cmdLineSyntax = "java MyApp [args]";
        int width = 50;
        String newLine = "\n";
        Options options = new Options();
        formatter.setWidth(width);
        formatter.setNewLine(newLine);
        formatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        String cmdLineSyntax = "java MyApp [args]";
        String header = "My App Help";
        String footer = "For more information, visit https://example.com";
        Options options = new Options();
        formatter.printHelp(System.out, cmdLineSyntax, header, options, footer);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        String cmdLineSyntax = "java MyApp [args]";
        String header = "My App Help";
        String footer = "For more information, visit https://example.com";
        Options options = new Options();
        formatter.printHelp(System.out, cmdLineSyntax, header, options, footer, true);
    }

    @Test
    public void testPrintHelpWithOptions() {
        String cmdLineSyntax = "java MyApp [args]";
        Options options = new Options();
        options.addOption("a", "answer", false, "Prints the answer");
        options.addOption("b", "bool", false, "Boolean option with no argument");
        options.addOption("c", "count", true, "Number of times to repeat");
        options.addOption("d", "debug", true, "Set debug mode");
        formatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithRequiredOptions() {
        String cmdLineSyntax = "java MyApp [args]";
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file name");
        options.addRequiredOption("o", "output", true, "Output file name");
        formatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithLongOptions() {
        String cmdLineSyntax = "java MyApp [args]";
        Options options = new Options();
        options.addOption("i", "input-file", true, "Input file name");
        options.addOption("o", "output-file", true, "Output file name");
        formatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptionGroup() {
        String cmdLineSyntax = "java MyApp [args]";
        Options options = new Options();
        OptionGroup group = new OptionGroup("Logging", "Logging options:");
        group.addOption("l", "log", true, "Logging level: TRACE|DEBUG|INFO|WARN|ERROR");
        group.addOption("v", "verbose", false, "Verbose output");
        options.addOptionGroup(group);
        formatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        String header = "My App Help";
        Options options = new Options();
        formatter.printHelp(System.out, null, header, options);
    }
}
