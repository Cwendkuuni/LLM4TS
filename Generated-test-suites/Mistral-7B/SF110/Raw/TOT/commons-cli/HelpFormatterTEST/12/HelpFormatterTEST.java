package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;

public class HelpFormatterTest {

    @org.junit.Test
    public void testDefaultHelpFormatter() {
        String cmdLineSyntax = "java org.apache.commons.cli.CommandLineParser test [args...]";
        Options options = new Options();
        options.addOption("a", "arg1", false, "Option with arg1");
        options.addOption("b", "arg2", true, "Option with arg2");
        options.addOption("c", "arg3", false, "Option with arg3");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelp = "usage: java org.apache.commons.cli.CommandLineParser test [args...]\n\n" +
                "options:\n" +
                "  -a, --arg1        Option with arg1 (boolean)\n" +
                "  -b, --arg2        Option with arg2 (an argument)\n" +
                "  -c, --arg3        Option with arg3 (boolean)\n";

        String help = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().toString() : System.out.toString();
        assertEquals(expectedHelp, help);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullCmdLineSyntax() {
        String cmdLineSyntax = null;
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testCustomHelpFormatter() {
        String cmdLineSyntax = "java org.apache.commons.cli.CommandLineParser test [args...]";
        Options options = new Options();
        options.addOption("a", "arg1", false, "Option with arg1");
        options.addOption("b", "arg2", true, "Option with arg2");
        options.addOption("c", "arg3", false, "Option with arg3");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(120);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(10);
        formatter.setSyntaxPrefix("Usage: ");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("ARG");

        String expectedHelp = "Usage: java org.apache.commons.cli.CommandLineParser test [args...]\n\n" +
                "Options:\n" +
                "  --arg1        OPTION_ARG1  Option with arg1 (boolean)\n" +
                "  --arg2 <ARG>  OPTION_ARG2  Option with arg2 (an argument)\n" +
                "  --arg3        OPTION_ARG3  Option with arg3 (boolean)\n";

        formatter.printHelp(cmdLineSyntax, options);

        String help = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().toString() : System.out.toString();
        assertEquals(expectedHelp, help);
    }

    @org.junit.Test
    public void testEmptyOptions() {
        String cmdLineSyntax = "java org.apache.commons.cli.CommandLineParser test [args...]";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, new Options());

        String expectedHelp = "usage: java org.apache.commons.cli.CommandLineParser test [args...]\n\n";
        String help = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().toString() : System.out.toString();
        assertEquals(expectedHelp, help);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullOptions() {
        String cmdLineSyntax = "java org.apache.commons.cli.CommandLineParser test [args...]";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, (Options)null);
    }

    @org.junit.Test
    public void testPrintHelpWithHeaderAndFooter() {
        String cmdLineSyntax = "java org.apache.commons.cli.CommandLineParser test [args...]";
        String header = "Help for test";
        String footer = "For more information, visit http://commons.apache.org/cli";
        Options options = new Options();
        options.addOption("a", "arg1", false, "Option with arg1");
        options.addOption("b", "arg2", true, "Option with arg2");
        options.addOption("c", "arg3", false, "Option with arg3");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, header, footer);

        String expectedHelp = "Help for test\n\n" +
                "usage: java org.apache.commons.cli.CommandLineParser test [args...]\n\n" +
                "options:\n" +
                "  -a, --arg1        Option with arg1 (boolean)\n" +
                "  -b, --arg2 <ARG>  Option with arg2 (an argument)\n" +
                "  -c, --arg3        Option with arg3 (boolean)\n" +
                "\nFor more information, visit http://commons.apache.org/cli";

        String help = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().toString() : System.out.toString();
        assertEquals(expectedHelp, help);
    }

    @org.junit.Test
    public void testPrintHelpWithAutoUsage() {
        String cmdLineSyntax = "java org.apache.commons.cli.CommandLineParser test [args...]";
        String header = "Help for test";
        String footer = "For more information, visit http://commons.apache.org/cli";
        Options options = new Options();
        options.addOption("a", "arg1", false, "Option with arg1");
        options.addOption("b", "arg2", true, "Option with arg2");
        options.addOption("c", "arg3", false, "Option with arg3");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, header, footer, true);

        String expectedHelp = "usage: java org.apache.commons.cli.CommandLineParser test [args...]\n\n" +
                "options:\n" +
                "  -a, --arg1        Option with arg1 (boolean)\n" +
                "  -b <arg2>        Option with arg2 (an argument)\n" +
                "  -c                 Option with arg3 (boolean)\n";

        String help = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().toString() : System.out.toString();
        assertEquals(expectedHelp, help);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomWidth() {
        String cmdLineSyntax = "java org.apache.commons.cli.CommandLineParser test [args...]";
        String header = "Help for test";
        String footer = "For more information, visit http://commons.apache.org/cli";
        Options options = new Options();
        options.addOption("a", "arg1", false, "Option with arg1");
        options.addOption("b", "arg2", true, "Option with arg2");
        options.addOption("c", "arg3", false, "Option with arg3");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.printHelp(cmdLineSyntax, options, header, footer);

        String expectedHelp = "Help for test\n\n" +
                "usage: java org.apache.commons.cli.CommandLineParser test [args...]\n\n" +
                "options:\n" +
                "  -a, --arg1        Option with arg1 (boolean)\n" +
                "  -b <ARG>          Option with arg2 (an argument)\n" +
                "  -c                 Option with arg3 (boolean)\n";

        String help = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().toString() : System.out.toString();
        assertEquals(expectedHelp, help);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomOptPrefix() {
        String cmdLineSyntax = "java org.apache.commons.cli.CommandLineParser test [args...]";
        String header = "Help for test";
        String footer = "For more information, visit http://commons.apache.org/cli";
        Options options = new Options();
        options.addOption("a", "arg1", false, "Option with arg1");
        options.addOption("b", "arg2", true, "Option with arg2");
        options.addOption("c", "arg3", false, "Option with arg3");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        formatter.printHelp(cmdLineSyntax, options, header, footer);

        String expectedHelp = "Help for test\n\n" +
                "usage: java org.apache.commons.cli.CommandLineParser test [args...]\n\n" +
                "options:\n" +
                "  --arg1        OPTION_ARG1  Option with arg1 (boolean)\n" +
                "  --arg2 <ARG>  OPTION_ARG2  Option with arg2 (an argument)\n" +
                "  --arg3        OPTION_ARG3  Option with arg3 (boolean)\n";

        String help = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().toString() : System.out.toString();
        assertEquals(expectedHelp, help);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomLongOptPrefix() {
        String cmdLineSyntax = "java org.apache.commons.cli.CommandLineParser test [args...]";
        String header = "Help for test";
        String footer = "For more information, visit http://commons.apache.org/cli";
        Options options = new Options();
        options.addOption("a", "arg1", false, "Option with arg1");
        options.addOption("b", "arg2", true, "Option with arg2");
        options.addOption("c", "arg3", false, "Option with arg3");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--long-");
        formatter.printHelp(cmdLineSyntax, options, header, footer);

        String expectedHelp = "Help for test\n\n" +
                "usage: java org.apache.commons.cli.CommandLineParser test [args...]\n\n" +
                "options:\n" +
                "  --arg1        OPTION_ARG1  Option with arg1 (boolean)\n" +
                "  --long--arg2 <ARG>  OPTION_ARG2  Option with arg2 (an argument)\n" +
                "  --arg3        OPTION_ARG3  Option with arg3 (boolean)\n";

        String help = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().toString() : System.out.toString();
        assertEquals(expectedHelp, help);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomArgName() {
        String cmdLineSyntax = "java org.apache.commons.cli.CommandLineParser test [args...]";
        String header = "Help for test";
        String footer = "For more information, visit http://commons.apache.org/cli";
        Options options = new Options();
        options.addOption("a", "arg1", false, "Option with arg1");
        options.addOption("b", "arg2", true, "Option with arg2");
        options.addOption("c", "arg3", false, "Option with arg3");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("ARGUMENT");
        formatter.printHelp(cmdLineSyntax, options, header, footer);

        String expectedHelp = "Help for test\n\n" +
                "usage: java org.apache.commons.cli.CommandLineParser test [args...]\n\n" +
                "options:\n" +
                "  -a, --arg1        Option with arg1 (boolean)\n" +
                "  -b <ARGUMENT>     Option with arg2 (an argument)\n" +
                "  -c                 Option with arg3 (boolean)\n";

        String help = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().toString() : System.out.toString();
        assertEquals(expectedHelp, help);
    }
}