package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getDefaultWidth());
    }

    @Test
    public void testGetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getDefaultLeftPad());
    }

    @Test
    public void testGetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(7);
        assertEquals(7, formatter.getDefaultDescPad());
    }

    @Test
    public void testGetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(7);
        assertEquals(7, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("usage: myApp ");
        assertEquals("usage: myApp ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testPrintHelp() {
        HelpFormatter formatter = new HelpFormatter();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(new PrintWriter(outContent));
        String cmdLineSyntax = "myApp <arg1> [-o <opt1>] [--long-opt1 <arg2>]";
        Options options = new Options();
        options.addOption("o", "opt1", true, "option description");
        options.addOptionGroup(new OptionGroup("group1", "group-description", Collections.emptyList())
                .addOption("l", "long-opt1", true, "long option description")
                .addOption("g", "group-option", false, "group option description"));

        formatter.printHelp(out, 100, cmdLineSyntax, options);

        String expectedOutput = "usage: myApp <arg1>\n"
                + "\noptions:\n"
                + "  -o, --opt1\n"
                + "      option description\n"
                + "      (value optional)\n"
                + "\n  --long-opt1 <arg2>\n"
                + "      long option description\n"
                + "      (value required)\n"
                + "\ngroup-description:\n"
                + "  -g, --group-option\n"
                + "      group option description\n";

        String output = outContent.toString();
        assertEquals(expectedOutput, output);

        out.close();
        outContent = null;
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(new PrintWriter(outContent));
        String cmdLineSyntax = "myApp [<arg1>]\n";
        Options options = new Options();
        options.addOption("o", "opt1", true, "option description");

        formatter.printHelp(out, 100, cmdLineSyntax, options, true);

        String expectedOutput = "usage: myApp [<arg1>]\n"
                + "\noptions:\n"
                + "  -o, --opt1\n"
                + "      option description\n"
                + "      (value optional)\n";

        String output = outContent.toString();
        assertEquals(expectedOutput, output);

        out.close();
        outContent = null;
    }

    @Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getDefaultOptPrefix());
    }

    @Test
    public void testGetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getDefaultOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--long-");
        assertEquals("--long-", formatter.getDefaultLongOptPrefix());
    }

    @Test
    public void testGetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--long-");
        assertEquals("--long-", formatter.getDefaultLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");
        assertEquals("param", formatter.getDefaultArgName());
    }

    @Test
    public void testGetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");
        assertEquals("param", formatter.getDefaultArgName());
    }

    @Test
    public void testSetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };
        formatter.setOptionComparator(comparator);
        assertEquals(comparator, formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelpWithCustomComparator() {
        HelpFormatter formatter = new HelpFormatter();
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Option opt1 = (Option) o1;
                Option opt2 = (Option) o2;
                return opt1.getKey().compareToIgnoreCase(opt2.getKey());
            }
        };
        formatter.setOptionComparator(comparator);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(new PrintWriter(outContent));
        String cmdLineSyntax = "myApp [-o <opt1>] [--long-opt1 <arg2>]";
        Options options = new Options();
        options.addOption("o", "opt1", true, "option description");
        options.addOption("long-opt1", "long-opt1", true, "long option description");

        formatter.printHelp(out, 100, cmdLineSyntax, options);

        String expectedOutput = "usage: myApp [-o <opt1>] [--long-opt1 <arg2>]\n"
                + "\noptions:\n"
                + "  -o, --opt1\n"
                + "      option description\n"
                + "      (value optional)\n"
                + "  --long-opt1\n"
                + "      long option description\n"
                + "      (value required)\n";

        String output = outContent.toString();
        assertEquals(expectedOutput, output);

        out.close();
        outContent = null;
    }
}