package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testDefaultSettings() {
        final HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertNull(formatter.getNewLine());
        assertEquals("-", formatter.getOptPrefix());
        assertEquals("--", formatter.getLongOptPrefix());
        assertEquals("arg", formatter.getArgName());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testSetSettings() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(2);
        formatter.setDescPadding(5);
        formatter.setSyntaxPrefix("help: ");
        formatter.setNewLine("--------------------");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("param");
        formatter.setOptionComparator(new MyComparator());

        assertEquals(50, formatter.getWidth());
        assertEquals(2, formatter.getLeftPadding());
        assertEquals(5, formatter.getDescPadding());
        assertEquals("help: ", formatter.getSyntaxPrefix());
        assertEquals("--------------------", formatter.getNewLine());
        assertEquals("--", formatter.getOptPrefix());
        assertEquals("-", formatter.getLongOptPrefix());
        assertEquals("param", formatter.getArgName());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelp_noOptions() {
        final String cmdLineSyntax = "java MyClass";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, 80, cmdLineSyntax, null);

        // Verify that no error message was printed
        assertTrue(System.out.toString().startsWith("usage: java MyClass"));
    }

    @Test
    public void testPrintHelp_singleStringOption() {
        final String cmdLineSyntax = "java MyClass [-a <arg>]";
        final HelpFormatter formatter = new HelpFormatter();
        final Option option = new Option("a", "arg", false, "Set arg value");
        final Options options = new Options();
        options.addOption(option);

        formatter.printHelp(System.out, 80, cmdLineSyntax, options, null);

        // Verify that help message was printed
        assertTrue(System.out.toString().contains("-a <arg>"));
        assertTrue(System.out.toString().contains("Set arg value"));
    }

    @Test
    public void testPrintHelp_multipleOptions() {
        final String cmdLineSyntax = "java MyClass [-a <arg>] [-b] [-c <arg>]";
        final HelpFormatter formatter = new HelpFormatter();
        final Option optionA = new Option("a", "arg", false, "Set argA value");
        final Option optionB = new Option("b", null, false, "Set argB value");
        final Option optionC = new Option("c", "arg", true, "Set argC value");
        final Options options = new Options();
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);

        formatter.printHelp(System.out, 80, cmdLineSyntax, options, null);

        // Verify that help message was printed
        assertTrue(System.out.toString().contains("-a <arg>"));
        assertTrue(System.out.toString().contains("Set argA value"));
        assertTrue(System.out.toString().contains("-b"));
        assertTrue(System.out.toString().contains("Set argB value"));
        assertTrue(System.out.toString().contains("<arg>"));
        assertTrue(System.out.toString().contains("Set argC value"));
    }

    @Test
    public void testPrintHelp_withHeaderAndFooter() {
        final String cmdLineSyntax = "java MyClass [options]";
        final String header = "My Class Options";
        final String footer = "Example: java MyClass -a argA -b";
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp(System.out, 80, cmdLineSyntax, null, header, footer);

        // Verify that help message was printed with header and footer
        assertTrue(System.out.toString().contains(header));
        assertTrue(System.out.toString().contains(footer));
    }

    @Test
    public void testPrintHelp_autoUsage() {
        final String cmdLineSyntax = "java MyClass [options]";
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp(System.out, 80, cmdLineSyntax, null, null, true);

        // Verify that usage was printed
        assertTrue(System.out.toString().startsWith("usage: java MyClass [options]"));
    }

    @Test
    public void testPrintHelp_customOptionComparator() {
        final String cmdLineSyntax = "java MyClass [-a <arg>] [-b <arg>] [-c <arg>]";
        final HelpFormatter formatter = new HelpFormatter();
        final Option optionC = new Option("c", "arg", true, "Set argC value");
        final Option optionB = new Option("b", "arg", false, "Set argB value");
        final Option optionA = new Option("a", "arg", false, "Set argA value");
        final Options options = new Options();
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        final Comparator<Option> comparator = new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o2.getKey().compareToIgnoreCase(o1.getKey());
            }
        };
        formatter.setOptionComparator(comparator);

        formatter.printHelp(System.out, 80, cmdLineSyntax, options, null);

        // Verify that help message was printed with options in reverse order
        assertTrue(System.out.toString().contains("--a <arg>"));
        assertTrue(System.out.toString().contains("--c <arg>"));
        assertTrue(System.out.toString().contains("--b <arg>"));
    }

    private static class MyComparator implements Comparator<Option> {
        @Override
        public int compare(Option o1, Option o2) {
            return o1.getKey().length() > o2.getKey().length() ? -1 : 1;
        }
    }
}