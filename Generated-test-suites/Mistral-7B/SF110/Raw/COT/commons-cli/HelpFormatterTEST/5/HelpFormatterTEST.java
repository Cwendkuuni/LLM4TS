package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * Test cases for HelpFormatter class.
 */
public class HelpFormatterTest {

    @Test
    public void testDefaultConstruction() {
        HelpFormatter formatter = new HelpFormatter();
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
    public void testCustomConstruction() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(2);
        formatter.setDescPadding(4);
        formatter.setSyntaxPrefix("help: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("option");
        assertEquals(50, formatter.getDefaultWidth());
        assertEquals(2, formatter.getDefaultLeftPad());
        assertEquals(4, formatter.getDefaultDescPad());
        assertEquals("help: ", formatter.getDefaultSyntaxPrefix());
        assertEquals("\n", formatter.getDefaultNewLine());
        assertEquals("--", formatter.getDefaultOptPrefix());
        assertEquals("-", formatter.getDefaultLongOptPrefix());
        assertEquals("option", formatter.getDefaultArgName());
    }

    @Test
    public void testSettersAndGetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(10);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(7);
        formatter.setSyntaxPrefix("test: ");
        formatter.setNewLine("---");
        formatter.setOptPrefix("+");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("argument");
        assertEquals(10, formatter.getDefaultWidth());
        assertEquals(5, formatter.getDefaultLeftPad());
        assertEquals(7, formatter.getDefaultDescPad());
        assertEquals("test: ", formatter.getDefaultSyntaxPrefix());
        assertEquals("---", formatter.getDefaultNewLine());
        assertEquals("+", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("argument", formatter.getDefaultArgName());
    }

    @Test
    public void testPrintHelpBasic() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpNullCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, new Options());
    }

    @Test
    public void testPrintHelpWithHeaderFooter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp {args}";
        String header = "Welcome to MyApp!";
        String footer = "For more information, visit https://myapp.com";
        formatter.printHelp(cmdLineSyntax, header, new Options(), footer);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp {args}";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithLeftPadDescPad() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp {args}";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options, 5, 10);
    }

    @Test
    public void testPrintHelpWithWidth() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp {args}";
        Options options = new Options();
        formatter.setWidth(30);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintUsageBasic() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";
        formatter.printUsage(System.out, 80, cmdLineSyntax);
    }

    @Test
    public void testPrintUsageWithHeader() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp {args}";
        formatter.printUsage(System.out, 80, cmdLineSyntax, new Options());
    }

    @Test
    public void testPrintUsageWithNullCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(System.out, 80, null);
        assertThrows(IllegalArgumentException.class, () -> formatter.printUsage(System.out, 80, null, new Options()));
    }

    @Test
    public void testPrintUsageWithOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp {args}";
        Options options = new Options();
        options.addOption("a", "append", false, "Append lines to file instead of overwriting.");
        options.addOption("b", "backup", true, "Make a backup of the input file before overwriting.");
        options.addOption("c", "configfile", true, "Loads config file from the specified file.");
        formatter.printUsage(System.out, 80, cmdLineSyntax, options);
    }

    @Test
    public void testPrintUsageWithEmptyCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "";
        formatter.printUsage(System.out, 80, cmdLineSyntax, new Options());
    }

    @Test
    public void testPrintUsageWithEmptyOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp {args}";
        formatter.printUsage(System.out, 80, cmdLineSyntax, new Options());
    }

    @Test
    public void testPrintUsageWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp {args}";
        formatter.printUsage(System.out, 80, cmdLineSyntax, new Options(), true);
    }

    @Test
    public void testPrintUsageWithWidth() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp {args}";
        formatter.setWidth(30);
        formatter.printUsage(System.out, 80, cmdLineSyntax, new Options());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintUsageWithInvalidWidth() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp {args}";
        formatter.setWidth(-5);
        formatter.printUsage(System.out, 80, cmdLineSyntax, new Options());
    }

    @Test
    public void testPrintWrapped() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a test. This is a very long test. This is a test that tests the wrapping functionality of HelpFormatter.";
        formatter.printWrapped(System.out, 50, text);
    }

    @Test
    public void testPrintWrappedWithNegativeLength() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a test. This is a very long test. This is a test that tests the wrapping functionality of HelpFormatter.";
        assertThrows(IllegalArgumentException.class, () -> formatter.printWrapped(System.out, -10, text));
    }

    @Test
    public void testPrintWrappedWithTextLongerThanWidth() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a test. This is a very long test. This is a test that tests the wrapping functionality of HelpFormatter.";
        formatter.printWrapped(System.out, 25, text);
    }

    @Test
    public void testPrintWrappedWithTextShorterThanWidth() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a test.";
        formatter.printWrapped(System.out, 50, text);
    }

    @Test
    public void testRenderOptions() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addOption("a", "append", false, "Append lines to file instead of overwriting.");
        options.addOption("b", "backup", true, "Make a backup of the input file before overwriting.");
        options.addOption("c", "configfile", true, "Loads config file from the specified file.");
        List<Option> optionsList = Arrays.asList(options.getOptions().toArray(new Option[0]));
        String result = formatter.renderOptions(new StringBuilder(), 80, options, 0, 0).toString();
        String expected = "   --append                             Append lines to file instead of overwriting.\n" +
                "   --backup                                      Make a backup of the input file before overwriting.\n" +
                "   --configfile <file>                           Loads config file from the specified file.\n";
        assertEquals(expected, result);
    }

    @Test
    public void testRenderOptionsEmpty() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        List<Option> optionsList = Collections.emptyList();
        String result = formatter.renderOptions(new StringBuilder(), 80, options, 0, 0).toString();
        assertEquals("", result);
    }
}