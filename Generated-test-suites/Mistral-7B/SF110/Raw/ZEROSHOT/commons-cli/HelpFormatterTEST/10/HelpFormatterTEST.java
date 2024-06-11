package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class HelpFormatterTest {

    private static final int DEFAULT_WIDTH = 74;
    private static final int DEFAULT_LEFT_PAD = 1;
    private static final int DEFAULT_DESC_PAD = 3;

    @org.junit.Before
    public void setUp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(DEFAULT_WIDTH);
        formatter.setLeftPadding(DEFAULT_LEFT_PAD);
        formatter.setDescPadding(DEFAULT_DESC_PAD);
        this.formatter = formatter;
    }

    private HelpFormatter formatter;

    @org.junit.Test
    public void testDefaultInitialization() {
        HelpFormatter helpFormatter = new HelpFormatter();
        assertEquals(DEFAULT_WIDTH, helpFormatter.getWidth());
        assertEquals(DEFAULT_LEFT_PAD, helpFormatter.getLeftPadding());
        assertEquals(DEFAULT_DESC_PAD, helpFormatter.getDescPadding());
        assertEquals(DEFAULT_SYNTAX_PREFIX, helpFormatter.getSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), helpFormatter.getNewLine());
        assertEquals(DEFAULT_OPT_PREFIX, helpFormatter.getOptPrefix());
        assertEquals(DEFAULT_LONG_OPT_PREFIX, helpFormatter.getLongOptPrefix());
        assertEquals(DEFAULT_ARG_NAME, helpFormatter.getArgName());
        assertEquals(new OptionComparator(), helpFormatter.getOptionComparator());
    }

    @org.junit.Test
    public void testSettingProperties() {
        HelpFormatter formatter = new HelpFormatter();

        formatter.setWidth(10);
        assertEquals(10, formatter.getWidth());

        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());

        formatter.setDescPadding(7);
        assertEquals(7, formatter.getDescPadding());

        formatter.setSyntaxPrefix("help: ");
        assertEquals("help: ", formatter.getSyntaxPrefix());

        formatter.setNewLine("\n");
        assertEquals("\n", formatter.getNewLine());

        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getOptPrefix());

        formatter.setLongOptPrefix("--");
        assertEquals("--", formatter.getLongOptPrefix());

        formatter.setArgName("argName");
        assertEquals("argName", formatter.getArgName());

        formatter.setOptionComparator(Collections.reverseOrder());
        assertEquals(Collections.reverseOrder(), formatter.getOptionComparator());
    }

    @org.junit.Test
    public void testPrintHelpWithSingleOption() {
        Options options = new Options();
        Option option = Option.builder()
            .longOpt("log")
            .desc("Log output")
            .build();
        options.addOption(option);

        String cmdLineSyntax = "MyApp [options]";
        formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, options);

        String expected = "usage: MyApp [options]\n" +
            "options:\n" +
            "   --log\n" +
            "      Log output.\n";
        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testPrintHelpWithMultipleOptions() {
        Options options = new Options();

        Option option1 = Option.builder()
            .longOpt("log")
            .desc("Log output")
            .build();
        Option option2 = Option.builder()
            .longOpt("verbose")
            .desc("Verbose output")
            .build();
        Option option3 = Option.builder()
            .longOpt("quiet")
            .desc("Quiet output")
            .build();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        String cmdLineSyntax = "MyApp [options]";
        formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, options);

        String expected = "usage: MyApp [options]\n" +
            "options:\n" +
            "   --log\n" +
            "      Log output.\n" +
            "   --quiet\n" +
            "      Quiet output.\n" +
            "   --verbose\n" +
            "      Verbose output.\n";
        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testPrintHelpWithOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup();
        group.setName("Network");
        group.setDescription("Networking options");

        Option option1 = Option.builder()
            .longOpt("host")
            .desc("Host")
            .hasArg()
            .build();
        Option option2 = Option.builder()
            .longOpt("port")
            .desc("Port")
            .hasArg()
            .build();
        group.addOption(option1);
        group.addOption(option2);

        Option option3 = Option.builder()
            .longOpt("log")
            .desc("Log output")
            .build();
        options.addOption(option3);
        options.addOptionGroup(group);

        String cmdLineSyntax = "MyApp [options]";
        formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, options);

        String expected = "usage: MyApp [options]\n" +
            "options:\n" +
            "   --log\n" +
            "      Log output.\n" +
            "Network:\n" +
            "options:\n" +
            "   --host <arg>\n" +
            "      Host.\n" +
            "   --port <arg>\n" +
            "      Port.\n";
        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testPrintHelpWithAutoUsage() {
        Options options = new Options();

        Option option = Option.builder()
            .longOpt("log")
            .desc("Log output")
            .build();
        options.addOption(option);

        String cmdLineSyntax = "MyApp [options]\n[command] [args...]";
        formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, options, true);

        String expected = "usage: MyApp [options]\n" +
            "usage: MyApp <command> [args...]\n" +
            "options:\n" +
            "   --log\n" +
            "      Log output.\n";
        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);

        Options options = new Options();

        Option option = Option.builder()
            .longOpt("log")
            .desc("Log output")
            .build();
        options.addOption(option);

        String cmdLineSyntax = "MyApp [options]";
        formatter.printHelp(System.out, 50, cmdLineSyntax, options);

        String expected = "usage: MyApp [options]\n" +
            "options:\n" +
            "   --log\n" +
            "      Log output.\n";
        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("  usage: ");

        Options options = new Options();

        Option option = Option.builder()
            .longOpt("log")
            .desc("Log output")
            .build();
        options.addOption(option);

        String cmdLineSyntax = "MyApp [options]";
        formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, options);

        String expected = "  usage: MyApp [options]\n" +
            "options:\n" +
            "   --log\n" +
            "      Log output.\n";
        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");

        Options options = new Options();

        Option option = Option.builder()
            .longOpt("log")
            .desc("Log output")
            .build();
        options.addOption(option);

        String cmdLineSyntax = "MyApp [options]";
        formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, options);

        String expected = "usage: MyApp [options]\n" +
            "options:\n" +
            "--log\n" +
            "  Log output.\n";
        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomLeftPad() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(2);

        Options options = new Options();

        Option option = Option.builder()
            .longOpt("log")
            .desc("Log output")
            .build();
        options.addOption(option);

        String cmdLineSyntax = "MyApp [options]";
        formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, options);

        String expected = "usage: MyApp [options]\n" +
            "options:\n" +
            "   --log\n" +
            "    Log output.\n";
        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomDescPad() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(5);

        Options options = new Options();

        Option option = Option.builder()
            .longOpt("log")
            .desc("Log output")
            .build();
        options.addOption(option);

        String cmdLineSyntax = "MyApp [options]";
        formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, options);

        String expected = "usage: MyApp [options]\n" +
            "options:\n" +
            "   --log\n" +
            "    Log output.\n" +
            "                   ";
        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("---\n");

        Options options = new Options();

        Option option = Option.builder()
            .longOpt("log")
            .desc("Log output")
            .build();
        options.addOption(option);

        String cmdLineSyntax = "MyApp [options]";
        formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, options);

        String expected = "usage: MyApp [options]\n" +
            "options:\n" +
            "---\n" +
            " --log\n" +
            "  Log output.\n" +
            "---\n";
        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(new OptionNameComparator());

        Options options = new Options();

        Option option1 = Option.builder()
            .longOpt("one")
            .desc("First option")
            .build();
        Option option2 = Option.builder()
            .longOpt("two")
            .desc("Second option")
            .build();
        Option option3 = Option.builder()
            .longOpt("three")
            .desc("Third option")
            .build();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        String cmdLineSyntax = "MyApp [options]";
        formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, options);

        String expected = "usage: MyApp [options]\n" +
            "options:\n" +
            "   --one\n" +
            "      First option.\n" +
            "   --two\n" +
            "      Second option.\n" +
            "   --three\n" +
            "      Third option.\n";
        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    private static class OptionNameComparator implements Comparator<Option> {
        @Override
        public int compare(Option o1, Option o2) {
            return o1.getKey().compareToIgnoreCase(o2.getKey());
        }
    }

}