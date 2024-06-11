package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class HelpFormatterTest {

    @Test
    public void testDefaultConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertNull(formatter.getOptionComparator());
    }

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testPrintHelpWithOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass -a 1 -b arg1 -c 23 --longOpt arg2";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");
        options.addOptionGroup(new OptionGroup("exampleGroup", "Example group"));
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");
        options.addOption("g", false, "Option G");

        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOptionGroup(new OptionGroup("exampleGroup", "Example group"));
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");
        options.addOption("g", false, "Option G");

        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithHeaderFooter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOptionGroup(new OptionGroup("exampleGroup", "Example group"));
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");
        options.addOption("g", false, "Option G");

        formatter.printHelp(cmdLineSyntax, options, "My program", "Usage information for My program");
    }

    @Test
    public void testPrintHelpWithCustomWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        String cmdLineSyntax = "java MyClass -a 1 -b arg1 -c 23 --longOpt arg2";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");
        options.addOptionGroup(new OptionGroup("exampleGroup", "Example group"));
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");
        options.addOption("g", false, "Option G");

        PrintWriter pw = new PrintWriter(System.out);
        formatter.printHelp(pw, 50, cmdLineSyntax, options);
        pw.flush();
    }

    @Test
    public void testPrintHelpWithCustomPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("myprogram: ");
        String cmdLineSyntax = "java MyClass -a 1 -b arg1 -c 23 --longOpt arg2";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");
        options.addOptionGroup(new OptionGroup("exampleGroup", "Example group"));
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");
        options.addOption("g", false, "Option G");

        PrintWriter pw = new PrintWriter(System.out);
        formatter.printHelp(pw, 50, cmdLineSyntax, options);
        pw.flush();

        String output = System.out.getBuffer().toString();
        assertTrue(output.contains("myprogram: usage: java MyClass"));
    }

    @Test
    public void testPrintHelpWithCustomOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        String cmdLineSyntax = "java MyClass -a 1 -b arg1 -c 23 --longOpt arg2";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");
        options.addOptionGroup(new OptionGroup("exampleGroup", "Example group"));
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");
        options.addOption("g", false, "Option G");

        PrintWriter pw = new PrintWriter(System.out);
        formatter.printHelp(pw, 50, cmdLineSyntax, options);
        pw.flush();

        String output = System.out.getBuffer().toString();
        assertTrue(output.contains("--usage: java MyClass"));
    }

    @Test
    public void testPrintHelpWithCustomArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");
        String cmdLineSyntax = "java MyClass -a 1 -b arg1 -c 23 --longOpt arg2";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");
        options.addOptionGroup(new OptionGroup("exampleGroup", "Example group"));
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");
        options.addOption("g", false, "Option G");

        PrintWriter pw = new PrintWriter(System.out);
        formatter.printHelp(pw, 50, cmdLineSyntax, options);
        pw.flush();

        String output = System.out.getBuffer().toString();
        assertTrue(output.contains("param: usage: java MyClass"));
    }

    @Test
    public void testPrintHelpWithCustomLeftPadDescPad() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.setDescPadding(8);
        String cmdLineSyntax = "java MyClass -a 1 -b arg1 -c 23 --longOpt arg2";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");
        options.addOptionGroup(new OptionGroup("exampleGroup", "Example group"));
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");
        options.addOption("g", false, "Option G");

        PrintWriter pw = new PrintWriter(System.out);
        formatter.printHelp(pw, 50, cmdLineSyntax, options);
        pw.flush();

        String output = System.out.getBuffer().toString();
        assertTrue(output.contains("    Option A: true   Option A"));
        assertTrue(output.contains("          Option B: arg1   Option B"));
    }

    @Test
    public void testPrintHelpWithOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        Comparator<Option> comparator = new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o1.getLongOpt().compareToIgnoreCase(o2.getLongOpt());
            }
        };
        formatter.setOptionComparator(comparator);
        String cmdLineSyntax = "java MyClass -a 1 -b arg1 -c 23 --longOpt1 arg2 --longOpt2 arg3";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");
        options.addOption("g", false, "Option G");
        options.addOption("longOpt1", false, "Option Long 1");
        options.addOption("longOpt2", false, "Option Long 2");

        PrintWriter pw = new PrintWriter(System.out);
        formatter.printHelp(pw, 50, cmdLineSyntax, options);
        pw.flush();

        String output = System.out.getBuffer().toString();
        List<String> lines = Arrays.asList(output.split("\n"));
        String firstLongOpt = lines.get(lines.size() - 3);
        String secondLongOpt = lines.get(lines.size() - 2);
        assertTrue(firstLongOpt.contains("--longOpt1"));
        assertTrue(secondLongOpt.contains("--longOpt2"));
    }
}