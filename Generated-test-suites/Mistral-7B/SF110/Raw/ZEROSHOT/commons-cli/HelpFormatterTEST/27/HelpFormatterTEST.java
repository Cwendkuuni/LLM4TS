package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class HelpFormatterTest {

    private HelpFormatter formatter;

    @Before
    public void setup() {
        formatter = new HelpFormatter();
    }

    @Test
    public void testDefaultWidth() {
        Options options = new Options();
        String cmdLineSyntax = "java MyApp";
        formatter.printHelp(cmdLineSyntax, options);
        assertEquals(74, formatter.getWidth());
    }

    @Test
    public void testSetWidth() {
        Options options = new Options();
        String cmdLineSyntax = "java MyApp";
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
        assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testPrintHelpWithDefaults() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is an option A");
        options.addOption("b", "optionB", true, "This is an option B");
        String cmdLineSyntax = "java MyApp";
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomSyntaxPrefix() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is an option A");
        options.addOption("b", "optionB", true, "This is an option B");
        String cmdLineSyntax = "MyApp <arg1> [options]";
        formatter.setSyntaxPrefix("MyApp: ");
        formatter.printHelp(cmdLineSyntax, options);
        assertEquals("MyApp: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testPrintHelpWithCustomLeftPadding() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is an option A");
        options.addOption("b", "optionB", true, "This is an option B");
        String cmdLineSyntax = "java MyApp";
        formatter.setLeftPadding(5);
        formatter.printHelp(cmdLineSyntax, options);
        assertEquals(5, formatter.getLeftPadding());
    }

    @Test
    public void testPrintHelpWithCustomDescPadding() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is an option A");
        options.addOption("b", "optionB", true, "This is an option B");
        String cmdLineSyntax = "java MyApp";
        formatter.setDescPadding(7);
        formatter.printHelp(cmdLineSyntax, options);
        assertEquals(7, formatter.getDescPadding());
    }

    @Test
    public void testPrintHelpWithCustomOptions() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is an option A");
        options.addOption("b", "optionB", true, "This is an option B");
        options.addOptionGroup(new OptionGroup("optionGroup", "Option group", Arrays.asList(
                new Option("g", "groupOption", false, "This is a group option"),
                new Option("h", "hiddenOption", false, "This is a hidden option")
                )));
        String cmdLineSyntax = "java MyApp";
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomHeaderAndFooter() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is an option A");
        String header = "My App Help";
        String footer = "For more information, visit www.myapp.com";
        String cmdLineSyntax = "java MyApp";
        formatter.printHelp(cmdLineSyntax, options, header, footer);
    }

    @Test
    public void testPrintHelpWithCustomAutoUsage() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is an option A");
        String cmdLineSyntax = "java MyApp [options]";
        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithCustomPrintWriter() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is an option A");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        String cmdLineSyntax = "java MyApp";
        formatter.printHelp(pw, 80, cmdLineSyntax, options);
        assertEquals(80, formatter.getWidth());
        pw.close();
        assertEquals(formatter.getSyntaxPrefix() + cmdLineSyntax + "\n" +
                "options:\n" +
                "  -a, --optionA\n" +
                "      This is an option A\n", sw.toString());
    }

    @Test
    public void testPrintHelpWithCustomLeftPaddingAndDescPadding() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is an option A");
        String cmdLineSyntax = "java MyApp";
        formatter.setLeftPadding(10);
        formatter.setDescPadding(15);
        formatter.printHelp(cmdLineSyntax, options);
        assertEquals(10, formatter.getLeftPadding());
        assertEquals(15, formatter.getDescPadding());
    }
}