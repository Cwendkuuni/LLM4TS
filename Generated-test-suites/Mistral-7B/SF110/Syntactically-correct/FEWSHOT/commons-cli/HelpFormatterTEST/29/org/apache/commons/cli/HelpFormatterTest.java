package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testPrintHelpWithDefaults() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Prints help message.").create('h'));
        options.addOption(OptionBuilder.withLongOpt("verbose").withDescription("Prints verbose output.").create('v'));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: java MyApp [options]\n\noptions:\n" +
                "  -h, --help             Prints help message.\n" +
                "  -v, --verbose          Prints verbose output.\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomWidth() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Prints help message.").create('h'));
        options.addOption(OptionBuilder.withLongOpt("verbose").withDescription("Prints verbose output.").create('v'));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(120);
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: java MyApp [options]\n\noptions:\n" +
                "  --help                  Prints help message.\n" +
                "  --verbose              Prints verbose output.\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomPrefix() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Prints help message.").create('h'));
        options.addOption(OptionBuilder.withLongOpt("verbose").withDescription("Prints verbose output.").create('v'));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("usage [MyApp]:");
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage [MyApp]: java MyApp [options]\n\noptions:\n" +
                "  -h                     Prints help message.\n" +
                "  -v                     Prints verbose output.\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomOptPrefix() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Prints help message.").create('h'));
        options.addOption(OptionBuilder.withLongOpt("verbose").withDescription("Prints verbose output.").create('v'));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: java MyApp [options]\n\noptions:\n" +
                "--help                  Prints help message.\n" +
                "--verbose              Prints verbose output.\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomLongOptPrefix() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Prints help message.").create('h'));
        options.addOption(OptionBuilder.withLongOpt("verbose").withDescription("Prints verbose output.").create('v'));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--");
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: java MyApp [options]\n\noptions:\n" +
                "--help                  Prints help message.\n" +
                "--verbose              Prints verbose output.\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomArgName() {
        String cmdLineSyntax = "java MyApp [options] [arg]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Prints help message.").create('h'));
        options.addOption(OptionBuilder.withLongOpt("verbose").withDescription("Prints verbose output.").create('v'));
        options.addOption(OptionBuilder.withLongOpt("inputFile").withDescription("Input file path.").withRequiredArg().create('i'));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("file");
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: java MyApp [options] [file]\n\noptions:\n" +
                "  -h, --help             Prints help message.\n" +
                "  -v, --verbose          Prints verbose output.\n" +
                "  -i, --inputFile <file>  Input file path.\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomOptionGroup() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        OptionGroup group = new OptionGroup("MyGroup");
        group.setDescription("My Group");
        group.addOption(OptionBuilder.withLongOpt("help").withDescription("Prints help message.").create('h'));
        group.addOption(OptionBuilder.withLongOpt("verbose").withDescription("Prints verbose output.").create('v'));
        options.addOptionGroup(group);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: java MyApp [options]\n\noptions:\n" +
                "group MyGroup:\n" +
                "  --help                  Prints help message.\n" +
                "  --verbose              Prints verbose output.\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomOptionOrder() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("help1").withDescription("Option 1").create('1');
        Option option2 = OptionBuilder.withLongOpt("help2").withDescription("Option 2").create('2');
        options.addOption(option2);
        options.addOption(option1);

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(new OptionComparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Option option1 = (Option)o1;
                Option option2 = (Option)o2;
                return option1.getKey().compareToIgnoreCase("help1") - option2.getKey().compareToIgnoreCase("help2");
            }
        });
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: java MyApp [options]\n\noptions:\n" +
                "  --help1                 Option 1.\n" +
                "  --help2                 Option 2.\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomPadding() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Prints help message.").create('h'));
        options.addOption(OptionBuilder.withLongOpt("verbose").withDescription("Prints verbose output.").create('v'));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.setDescPadding(10);
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: java MyApp [options]\n\noptions:\n" +
                "   help                     Prints help message.\n" +
                "    ----------------------------------------------------------\n" +
                "    verbose                 Prints verbose output.\n" +
                "    ----------------------------------------------------------\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithLongOptions() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("longOption1").withDescription("Long option 1.").create('o'));
        options.addOption(OptionBuilder.withLongOpt("longOption2").withDescription("Long option 2.").create('l'));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: java MyApp [options]\n\noptions:\n" +
                "  -o, --longOption1         Long option 1.\n" +
                "  -l, --longOption2         Long option 2.\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithArguments() {
        String cmdLineSyntax = "java MyApp [options] [arg]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Prints help message.").create('h'));
        options.addOption(OptionBuilder.withLongOpt("inputFile").withDescription("Input file path.").withRequiredArg().create('i'));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: java MyApp [options] [arg]\n\noptions:\n" +
                "  -h, --help             Prints help message.\n" +
                "  -i, --inputFile <arg>   Input file path.\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomHeaderAndFooter() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Prints help message.").create('h'));

        HelpFormatter formatter = new HelpFormatter();
        String header = "My App Help";
        String footer = "For more information, visit https://example.com";
        formatter.printHelp(cmdLineSyntax, options, header, footer);

        String expectedHelpMessage = "My App Help\n\n" +
                "usage: java MyApp [options]\n\noptions:\n" +
                "  -h, --help             Prints help message.\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithEmptyOptions() {
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: java MyApp [options]\n\noptions:\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, options);
    }

    @Test
    public void testPrintHelpWithEmptyCmdLineSyntax() {
        String cmdLineSyntax = "";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelpMessage = "usage: \n\noptions:\n";

        TestUtil.assertEquals(expectedHelpMessage, System.out.getBuffer().toString());
    }

    private static class OptionComparator implements Comparator<Option> {
        @Override
        public int compare(Option o1, Option o2) {
            return o1.getKey().compareToIgnoreCase(o2.getKey());
        }
    }

    private static class TestUtil {
        public static void assertEquals(String expected, String actual) {
            assertEquals(expected, actual, false);
        }

        public static void assertEquals(String expected, String actual, boolean ignoreLineEndings) {
            StringBuilder buffer = new StringBuilder();
            String lineSeparator = System.getProperty("line.separator");

            if (ignoreLineEndings) {
                lineSeparator = "";
            }

            if (expected != null) {
                buffer.append(expected);
            }

            String actualHelpMessage = System.out.getBuffer().toString();
            String actualTrimmedHelpMessage = actualHelpMessage.trim();

            String[] expectedLines = expected.split(lineSeparator);
            String[] actualLines = actualTrimmedHelpMessage.split(lineSeparator);

            assertEquals(expectedLines.length, actualLines.length);

            for (int i = 0; i < expectedLines.length; i++) {
                assertEquals(expectedLines[i], actualLines[i]);
            }

            System.out.getBuffer().setLength(0);
        }
    }
}