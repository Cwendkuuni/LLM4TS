package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        final String cmdLineSyntax = "java MainClass [options]";
        final Options options = new Options();
        options.addOption(new Option("a", true, "a help message for option a"));
        options.addOption(new Option("b", false, "a help message for option b"));
        options.addOptionGroup(new OptionGroup("group1")
                .addOption(new Option("c", false, "a help message for option c in group1"))
                .addOption(new Option("d", true, "a help message for option d in group1"))
                .addOption(new Option("e", false, "a help message for option e in group1")));
        options.addOptionGroup(new OptionGroup("group2")
                .addOption(new Option("f", true, "a help message for option f in group2"))
                .addOption(new Option("g", false, "a help message for option g in group2")));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        final String cmdLineSyntax = "java MainClass [options]";
        final String header = "Header";
        final String footer = "Footer";
        final Options options = new Options();
        options.addOption(new Option("a", true, "a help message for option a"));
        options.addOption(new Option("b", false, "a help message for option b"));
        options.addOptionGroup(new OptionGroup("group1")
                .addOption(new Option("c", false, "a help message for option c in group1"))
                .addOption(new Option("d", true, "a help message for option d in group1"))
                .addOption(new Option("e", false, "a help message for option e in group1")));
        options.addOptionGroup(new OptionGroup("group2")
                .addOption(new Option("f", true, "a help message for option f in group2"))
                .addOption(new Option("g", false, "a help message for option g in group2")));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.setDescPadding(10);
        formatter.setWidth(120);
        formatter.setSyntaxPrefix("Usage: ");
        formatter.setOptPrefix("  ");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("ARG");
        formatter.setOptionComparator(new OptionComparator());
        formatter.printHelp(cmdLineSyntax, header, options, footer);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        final String cmdLineSyntax = "java MainClass [options]";
        final String header = "Header";
        final String footer = "Footer";
        final Options options = new Options();
        options.addOption(new Option("a", true, "a help message for option a"));
        options.addOption(new Option("b", false, "a help message for option b"));
        options.addOptionGroup(new OptionGroup("group1")
                .addOption(new Option("c", false, "a help message for option c in group1"))
                .addOption(new Option("d", true, "a help message for option d in group1"))
                .addOption(new Option("e", false, "a help message for option e in group1")));
        options.addOptionGroup(new OptionGroup("group2")
                .addOption(new Option("f", true, "a help message for option f in group2"))
                .addOption(new Option("g", false, "a help message for option g in group2")));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.setDescPadding(10);
        formatter.setWidth(120);
        formatter.setSyntaxPrefix("Usage: ");
        formatter.setOptPrefix("  ");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("ARG");
        formatter.setOptionComparator(new OptionComparator());
        formatter.printHelp(cmdLineSyntax, header, options, footer, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        final String header = "Header";
        final String footer = "Footer";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, header, options, footer);
    }

    @Test
    public void testPrintUsage() {
        final String app = "MainClass";
        final Options options = new Options();
        options.addOption(new Option("a", true, "a help message for option a"));
        options.addOption(new Option("b", false, "a help message for option b"));
        options.addOptionGroup(new OptionGroup("group1")
                .addOption(new Option("c", false, "a help message for option c in group1"))
                .addOption(new Option("d", true, "a help message for option d in group1"))
                .addOption(new Option("e", false, "a help message for option e in group1")));
        options.addOptionGroup(new OptionGroup("group2")
                .addOption(new Option("f", true, "a help message for option f in group2"))
                .addOption(new Option("g", false, "a help message for option g in group2")));

        final HelpFormatter formatter = new HelpFormatter();
        final PrintWriter pw = new PrintWriter(System.out);
        formatter.printUsage(pw, 80, app, options);
        pw.flush();
    }

    @Test
    public void testPrintUsageWithNullApp() {
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        final PrintWriter pw = new PrintWriter(System.out);
        formatter.printUsage(pw, 80, null, options);
        pw.flush();
    }

    @Test
    public void testPrintOptions() {
        final Options options = new Options();
        options.addOption(new Option("a", true, "a help message for option a"));
        options.addOption(new Option("b", false, "a help message for option b"));
        options.addOptionGroup(new OptionGroup("group1")
                .addOption(new Option("c", false, "a help message for option c in group1"))
                .addOption(new Option("d", true, "a help message for option d in group1"))
                .addOption(new Option("e", false, "a help message for option e in group1")));
        options.addOptionGroup(new OptionGroup("group2")
                .addOption(new Option("f", true, "a help message for option f in group2"))
                .addOption(new Option("g", false, "a help message for option g in group2")));

        final HelpFormatter formatter = new HelpFormatter();
        final PrintWriter pw = new PrintWriter(System.out);
        formatter.printOptions(pw, 80, options, 5, 15);
        pw.flush();
    }

    @Test
    public void testPrintWrappedText() {
        final HelpFormatter formatter = new HelpFormatter();
        final StringBuffer sb = new StringBuffer();

        formatter.printWrapped(sb, 80, null, "This is a test for the printWrapped method.");
        assertEquals("This is a test for the printWrapped method.\n", sb.toString());

        formatter.printWrapped(sb, 50, 10, "This is a test for the printWrapped method.");
        assertEquals("This is a test for the printWrapped method.\n", sb.toString());

        formatter.printWrapped(sb, 50, 15, "This is a test for the printWrapped method.");
        assertEquals("This is a test for the printWrapped method.\nThis is a test for the printWrapped method.\n", sb.toString());
    }
}