package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterInitialization() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getDefaultWidth());
        assertEquals(1, formatter.getDefaultLeftPad());
        assertEquals(3, formatter.getDefaultDescPad());
        assertEquals("usage: ", formatter.getDefaultSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), formatter.getDefaultNewLine());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testHelpFormatterCustomization() {
        HelpFormatter formatter = new HelpFormatter();

        formatter.setWidth(50);
        formatter.setLeftPadding(2);
        formatter.setDescPadding(4);
        formatter.setSyntaxPrefix("syntax: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("parameter");

        assertEquals(50, formatter.getDefaultWidth());
        assertEquals(2, formatter.getDefaultLeftPad());
        assertEquals(4, formatter.getDefaultDescPad());
        assertEquals("syntax: ", formatter.getDefaultSyntaxPrefix());
        assertEquals("\n", formatter.getDefaultNewLine());
        assertEquals("--", formatter.getDefaultOptPrefix());
        assertEquals("-", formatter.getDefaultLongOptPrefix());
        assertEquals("parameter", formatter.getDefaultArgName());
    }

    @Test
    public void testPrintHelpWithNoOptions() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();

        formatter.printHelp(System.out, 80, cmdLineLineSyntax, options, null, false);
    }

    @Test
    public void testPrintHelpWithOptions() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [<option1> <option2>]";
        Options options = new Options();
        options.addOption("opt1", true, "Option 1", "Description 1");
        options.addOption("opt2", false, "Option 2", "Description 2");

        formatter.printHelp(System.out, 80, cmdLineSyntax, options, null, false);
    }

    @Test
    public void testPrintHelpWithCustomizedOutput() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [<option1> <option2>]";
        Options options = new Options();
        options.addOption("opt1", true, "Option 1", "Description 1");
        options.addOption("opt2", false, "Option 2", "Description 2");

        formatter.setWidth(50);
        formatter.setLeftPadding(2);
        formatter.setDescPadding(4);
        formatter.setSyntaxPrefix("syntax: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("parameter");

        formatter.printHelp(System.out, 50, cmdLineSyntax, options, null, false);
    }

    @Test
    public void testPrintHelpWithAutoUsage() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [<option1> <option2>]";
        Options options = new Options();
        options.addOption("opt1", true, "Option 1", "Description 1");
        options.addOption("opt2", false, "Option 2", "Description 2");

        formatter.printHelp(System.out, 80, cmdLineSyntax, options, null, true);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [<option1> <option2>]";
        Options options = new Options();
        options.addOption("opt1", true, "Option 1", "Description 1");
        options.addOption("opt2", false, "Option 2", "Description 2");

        String header = "MyApp Command Line Interface\n";
        String footer = "For more information, please visit http://myapp.com";

        formatter.printHelp(System.out, 80, cmdLineSyntax, options, header, footer, false);
    }

    @Test
    public void testPrintHelpWithPrintWriter() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [<option1> <option2>]";
        Options options = new Options();
        options.addOption("opt1", true, "Option 1", "Description 1");
        options.addOption("opt2", false, "Option 2", "Description 2");

        PrintWriter writer = new PrintWriter(System.out);
        formatter.printHelp(writer, 80, cmdLineSyntax, options, null, false);
        writer.flush();
    }

    @Test
    public void testPrintHelpWithCustomWriter() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [<option1> <option2>]";
        Options options = new Options();
        options.addOption("opt1", true, "Option 1", "Description 1");
        options.addOption("opt2", false, "Option 2", "Description 2");

        PrintWriter writer = new PrintWriter(new StringWriter());
        formatter.printHelp(writer, 80, cmdLineSyntax, options, null, false);
        String output = writer.getBuffer().toString();
        writer.flush();

        assertEquals("usage: java MyApp [<option1> <option2>]\n"
                + "\n"
                + "options:\n"
                + "  -opt1, --option1   Option 1 (default: false)\n"
                + "      Description 1\n"
                + "  -opt2, --option2   Option 2 (default: false)\n"
                + "      Description 2\n", output);
    }

    @Test
    public void testPrintUsage() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [<option1> <option2>]";
        Options options = new Options();
        options.addOption("opt1", true, "Option 1", "Description 1");
        options.addOption("opt2", false, "Option 2", "Description 2");

        PrintWriter writer = new PrintWriter(System.out);
        formatter.printUsage(writer, 80, cmdLineSyntax, options);
        writer.flush();
    }
}