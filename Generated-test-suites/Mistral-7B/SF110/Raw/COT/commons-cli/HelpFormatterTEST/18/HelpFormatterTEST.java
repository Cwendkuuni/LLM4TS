package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

public class HelpFormatterTest {

    private HelpFormatter formatter;

    @Before
    public void setup() {
        formatter = new HelpFormatter();
    }

    @Test
    public void testDefaultHelpFormatterInitialization() {
        HelpFormatter formatter = new HelpFormatter();
        assertNotNull(formatter.getDefaultWidth());
        assertNotNull(formatter.getDefaultLeftPad());
        assertNotNull(formatter.getDefaultDescPad());
        assertNotNull(formatter.getDefaultSyntaxPrefix());
        assertNotNull(formatter.getDefaultNewLine());
        assertNotNull(formatter.getDefaultOptPrefix());
        assertNotNull(formatter.getDefaultLongOptPrefix());
        assertNotNull(formatter.getDefaultArgName());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testSettersAndGetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
        formatter.setLeftPadding(2);
        assertEquals(2, formatter.getLeftPadding());
        formatter.setDescPadding(5);
        assertEquals(5, formatter.getDescPadding());
        formatter.setSyntaxPrefix("test_prefix ");
        assertEquals("test_prefix ", formatter.getSyntaxPrefix());
        formatter.setNewLine("\nTest NewLine");
        assertEquals("\nTest NewLine", formatter.getNewLine());
        formatter.setOptPrefix("_");
        assertEquals("_", formatter.getOptPrefix());
        formatter.setLongOptPrefix("--long");
        assertEquals("--long", formatter.getLongOptPrefix());
        formatter.setArgName("test_arg");
        assertEquals("test_arg", formatter.getArgName());
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 1;
            }
        };
        formatter.setOptionComparator(comparator);
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelp_Defaults() {
        String cmdLineSyntax = "Command [options]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, cmdLineSyntax, options);
        // You can add assertions here to check the output of printHelp
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelp_NullCmdLineSyntax() {
        String cmdLineSyntax = null;
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_Customized() {
        String cmdLineSyntax = "java MyApp [options]";
        String header = "Usage Info for MyApp";
        String footer = "Help available at http://example.com";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(10);
        formatter.setSyntaxPrefix("usage: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--long");
        formatter.setArgName("arg");
        formatter.printHelp(System.out, cmdLineSyntax, header, options, footer);
        // You can add assertions here to check the output of printHelp
    }

    @Test
    public void testPrintUsage_Defaults() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        StringWriter sw = new StringWriter();
        formatter.printUsage(sw, 75, cmdLineSyntax, options);
        String usage = sw.toString();
        // You can add assertions here to check the output of printUsage
    }

    @Test
    public void testPrintUsage_Customized() {
        String cmdLineSyntax = "java MyApp [options]";
        String header = "Usage Info for MyApp";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(10);
        formatter.setSyntaxPrefix("usage: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--long");
        formatter.setArgName("arg");
        StringWriter sw = new StringWriter();
        formatter.printUsage(sw, 75, cmdLineSyntax, header, options);
        String usage = sw.toString();
        // You can add assertions here to check the output of printUsage
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelp_NullOptions() {
        String cmdLineSyntax = "java MyApp [options]";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, cmdLineSyntax, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelp_NullCmdLineSyntax() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, null, options);
    }

    @Test
    public void testPrintHelp_EmptyOptions() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, cmdLineSyntax, options);
        // You can add assertions here to check the output of printHelp
        // when there are no options defined
    }

    @Test
    public void testRenderOptions_Defaults() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        StringBuffer output = new StringBuffer();
        formatter.renderOptions(output, 80, options, 5, 8);
        String result = output.toString();
        // You can add assertions here to check the output of renderOptions
        // with default settings
    }

    @Test
    public void testRenderOptions_Customized() {
        Options options = new Options();
        Options option1 = new Option("o", "opt1", true, "Option 1");
        options.addOption(option1);
        Option option2 = new Option("o", "opt2", false, "Option 2");
        option2.setLongOpt("long-opt2");
        option2.setArgName("arg2");
        options.addOption(option2);

        HelpFormatter formatter = new HelpFormatter();
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 1;
            }
        };
        formatter.setOptionComparator(comparator);
        StringBuffer output = new StringBuffer();
        formatter.renderOptions(output, 80, options, 5, 8);
        String result = output.toString();
        // You can add assertions here to check the output of renderOptions
        // with customized settings
    }
}