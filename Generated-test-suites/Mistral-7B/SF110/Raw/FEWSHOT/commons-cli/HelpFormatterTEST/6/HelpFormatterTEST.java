package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterInitialization() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
        assertEquals(new OptionComparator(), formatter.getOptionComparator());
    }

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testPrintHelpWithDefaults() {
        final String cmdLineSyntax = "java MyClass -a valueA -b valueB";
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        final PrintWriter pw = new PrintWriter(System.out);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(pw, cmdLineSyntax, options);
        pw.flush();

        final String expectedHelp = "usage: java MyClass [-a <valueA>] [-b <valueB>]\n"
                + "\noptions:\n"
                + "  -a, --option-a\n"
                + "      Option A\n"
                + "  -b, --option-b\n"
                + "      Option B\n";
        assertEquals(expectedHelp, pw.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomSyntaxPrefix() {
        final String cmdLineSyntax = "myTool <command> [options]";
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        final PrintWriter pw = new PrintWriter(System.out);
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("Usage: ");
        formatter.printHelp(pw, cmdLineSyntax, options);
        pw.flush();

        final String expectedHelp = "Usage: myTool <command> [options]\n"
                + "\noptions:\n"
                + "  -a, --option-a\n"
                + "      Option A\n"
                + "  -b, --option-b\n"
                + "      Option B\n";
        assertEquals(expectedHelp, pw.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomLeftPadding() {
        final String cmdLineSyntax = "java MyClass -a valueA -b valueB";
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        final PrintWriter pw = new PrintWriter(System.out);
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.printHelp(pw, cmdLineSyntax, options);
        pw.flush();

        final String expectedHelp = "    usage: java MyClass [-a <valueA>] [-b <valueB>]\n"
                + "\noptions:\n"
                + "      -a, --option-a\n"
                + "          Option A\n"
                + "      -b, --option-b\n"
                + "          Option B\n";
        assertEquals(expectedHelp, pw.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomDescPadding() {
        final String cmdLineSyntax = "java MyClass -a valueA -b valueB";
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        final PrintWriter pw = new PrintWriter(System.out);
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(10);
        formatter.printHelp(pw, cmdLineSyntax, options);
        pw.flush();

        final String expectedHelp = "usage: java MyClass [-a <valueA>] [-b <valueB>]\n"
                + "\noptions:\n"
                + "  -a, --option-a\n"
                + "      Option A\n"
                + "           A description with more than 10 padding\n"
                + "  -b, --option-b\n"
                + "      Option B\n"
                + "           A description with more than 10 padding\n";
        assertEquals(expectedHelp, pw.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomNewLine() {
        final String cmdLineSyntax = "java MyClass -a valueA -b valueB";
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        final PrintWriter pw = new PrintWriter(System.out);
        String newline = System.getProperty("line.separator");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\n---\n");
        formatter.printHelp(pw, cmdLineSyntax, options);
        pw.flush();

        final String expectedHelp = "usage: java MyClass [-a <valueA>] [-b <valueB>]\n"
                + "---\n"
                + "options:\n"
                + "  -a, --option-a\n"
                + "      Option A\n"
                + "---\n"
                + "  -b, --option-b\n"
                + "      Option B\n"
                + "---\n";
        assertEquals(expectedHelp, pw.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomOptPrefix() {
        final String cmdLineSyntax = "java MyClass --option-a valueA --option-b valueB";
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        final PrintWriter pw = new PrintWriter(System.out);
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        formatter.printHelp(pw, cmdLineSyntax, options);
        pw.flush();

        final String expectedHelp = "usage: java MyClass --option-a <valueA> --option-b <valueB>\n"
                + "\noptions:\n"
                + "  --option-a\n"
                + "      Option A\n"
                + "  --option-b\n"
                + "      Option B\n";
        assertEquals(expectedHelp, pw.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomLongOptPrefix() {
        final String cmdLineSyntax = "java MyClass --long-option-a valueA --long-option-b valueB";
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        final PrintWriter pw = new PrintWriter(System.out);
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--long-");
        formatter.printHelp(pw, cmdLineSyntax, options);
        pw.flush();

        final String expectedHelp = "usage: java MyClass --long-option-a <valueA> --long-option-b <valueB>\n"
                + "\noptions:\n"
                + "  --long-option-a\n"
                + "      Option A\n"
                + "  --long-option-b\n"
                + "      Option B\n";
        assertEquals(expectedHelp, pw.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomArgName() {
        final String cmdLineSyntax = "java MyClass --option-a valueA paramA --option-b valueB paramB";
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        final PrintWriter pw = new PrintWriter(System.out);
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");
        formatter.printHelp(pw, cmdLineSyntax, options);
        pw.flush();

        final String expectedHelp = "usage: java MyClass --option-a <valueA> param --option-b <valueB> param\n"
                + "\noptions:\n"
                + "  --option-a\n"
                + "      Option A\n"
                + "  --option-b\n"
                + "      Option B\n";
        assertEquals(expectedHelp, pw.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomOptionComparator() {
        final String cmdLineSyntax = "java MyClass -c valueC -d valueD";
        final Options options = new Options();
        options.addOption("d", true, "Option D");
        options.addOption("c", true, "Option C");

        final PrintWriter pw = new PrintWriter(System.out);
        Comparator<Option> comparator = new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o1.getLongOpt().compareToIgnoreCase(o2.getLongOpt());
            }
        };
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(comparator);
        formatter.printHelp(pw, cmdLineSyntax, options);
        pw.flush();

        final String expectedHelp = "usage: java MyClass [-c <valueC>] [-d <valueD>]\n"
                + "\noptions:\n"
                + "  -c, --option-c\n"
                + "      Option C\n"
                + "  -d, --option-d\n"
                + "      Option D\n";
        assertEquals(expectedHelp, pw.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithOptionGroup() {
        final String cmdLineSyntax = "java MyClass --group [--option-a valueA | --option-b valueB | --option-c valueC]";
        final Options options = new Options();
        final OptionGroup group = new OptionGroup("group", "Group description");
        group.setRequired(false);
        group.addOption(new Option("a", true, "Option A", "a"));
        group.addOption(new Option("b", true, "Option B", "b"));
        group.addOption(new Option("c", true, "Option C", "c"));
        options.addOptionGroup(group);

        final PrintWriter pw = new PrintWriter(System.out);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(pw, cmdLineSyntax, options);
        pw.flush();

        final String expectedHelp = "usage: java MyClass --group [--option-a <valueA> | --option-b <valueB> | --option-c <valueC>]\n"
                + "\noptions:\n"
                + "group:\n"
                + "  --option-a\n"
                + "      Option A\n"
                + "  --option-b\n"
                + "      Option B\n"
                + "  --option-c\n"
                + "      Option C\n";
        assertEquals(expectedHelp, pw.getBuffer().toString());
    }
}