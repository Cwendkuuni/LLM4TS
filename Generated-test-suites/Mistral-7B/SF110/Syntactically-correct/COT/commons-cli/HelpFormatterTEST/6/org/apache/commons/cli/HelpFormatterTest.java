package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterInstance() {
        HelpFormatter formatter = new HelpFormatter();
        assertNotNull(formatter);
    }

    @Test
    public void testDefaultValues() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
    }

    @Test
    public void testSetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(2);
        formatter.setDescPadding(4);
        formatter.setSyntaxPrefix("help: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("argName");
        assertEquals(50, formatter.getWidth());
        assertEquals(2, formatter.getLeftPadding());
        assertEquals(4, formatter.getDescPadding());
        assertEquals("help: ", formatter.getSyntaxPrefix());
        assertEquals("\n", formatter.getNewLine());
        assertEquals("--", formatter.getDefaultOptPrefix());
        assertEquals("-", formatter.getDefaultLongOptPrefix());
        assertEquals("argName", formatter.getDefaultArgName());
    }

    @Test
    public void testPrintHelpWithDefaultOptions() {
        Options options = new Options();
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        Option option3 = new Option("help", false, "help message");
        Option optionGroup = new OptionGroup("PG", "Printing options");
        Option option4 = new Option("a", "all", false, "print all records", optionGroup);
        Option option5 = new Option("b", "brief", false, "print only header", optionGroup);

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOptionGroup(optionGroup);
        options.addOption(option4);
        options.addOption(option5);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("HelpFormatterTest", options);

        String expected = "usage: HelpFormatterTest\n"
                + "\n"
                + "options:\n"
                + "  -i, --input          input file\n"
                + "  -o, --output         output file\n"
                + "  -help, --help        help message\n"
                + "  --all                print all records\n"
                + "  --brief              print only header\n";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintHelpWithCustomOptions() {
        Options options = new Options();
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        Option option3 = new Option("h", "help", false, "help message");
        Option option4 = new Option.Builder("v").longOpt("verbose").desc("verbose mode").build();
        Option option5 = new Option("l", "logfile", true, "log file").required(true);

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("HelpFormatterTest", options);

        String expected = "usage: HelpFormatterTest\n"
                + "\n"
                + "options:\n"
                + "  -i, --input          input file\n"
                + "  -o, --output         output file\n"
                + "  -h, --help           help message\n"
                + "  -v, --verbose        verbose mode\n"
                + "  -l, --logfile        log file (required)\n";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintHelpWithCustomSyntaxPrefix() {
        Options options = new Options();
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        Option option3 = new Option("h", "help", false, "help message");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("HelpFormatterTest: ");
        formatter.printHelp("HelpFormatterTest", options);

        String expected = "HelpFormatterTest: usage: HelpFormatterTest\n"
                + "\n"
                + "options:\n"
                + "  -i, --input          input file\n"
                + "  -o, --output         output file\n"
                + "  -h, --help           help message\n";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintHelpWithCustomOptionComparator() {
        Options options = new Options();
        Option option1 = new Option("z", "zebra", true, "zebra color");
        Option option2 = new Option("a", "apple", true, "apple color");

        Comparator<Option> comparator = new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o2.getKey().compareToIgnoreCase(o1.getKey());
            }
        };

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(comparator);
        formatter.printHelp("HelpFormatterTest", options);

        String expected = "usage: HelpFormatterTest\n"
                + "\n"
                + "options:\n"
                + "  -z, --zebra          zebra color\n"
                + "  -a, --apple          apple color\n";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintHelpWithCustomLeftPaddingAndDescPadding() {
        Options options = new Options();
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        Option option3 = new Option("help", false, "help message");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.setDescPadding(7);
        formatter.printHelp("HelpFormatterTest", options);

        String expected = "HelpFormatterTest:\n"
                + "usage: HelpFormatterTest\n"
                + "\n"
                + "options:\n"
                + "  --input                          input file\n"
                + "                                       arg\n"
                + "  --output                         output file\n"
                + "                                       arg\n"
                + "  --help                          help message\n";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        Options options = new Options();
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        Option option3 = new Option("help", false, "help message");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullOptions() {
        String cmdLineSyntax = "HelpFormatterTest";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, null);
    }

    @Test
    public void testPrintHelpWithNoOptions() {
        String cmdLineSyntax = "HelpFormatterTest";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax);

        String expected = "HelpFormatterTest:\n"
                + "usage: HelpFormatterTest\n";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintHelpWithEmptyOptions() {
        String cmdLineSyntax = "HelpFormatterTest";

        Options options = new Options();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        String expected = "HelpFormatterTest:\n"
                + "usage: HelpFormatterTest\n";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintHelpWithCustomHeaderAndFooter() {
        Options options = new Options();
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        Option option3 = new Option("help", false, "help message");

        HelpFormatter formatter = new HelpFormatter();

        String header = "HelpFormatterTest - usages";
        String footer = "Type 'java HelpFormatterTest <command>' for more information.";

        formatter.printHelp("HelpFormatterTest", options, header, footer);

        String expected = "HelpFormatterTest - usages:\n"
                + "usage: HelpFormatterTest\n"
                + "\n"
                + "options:\n"
                + "  -i, --input          input file\n"
                + "                       arg\n"
                + "  -o, --output         output file\n"
                + "                       arg\n"
                + "  -help, --help        help message\n"
                + "\n"
                + "Type 'java HelpFormatterTest <command>' for more information.";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintHelpWithCustomAutoUsage() {
        Options options = new Options();
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        Option option3 = new Option("help", false, "help message");

        HelpFormatter formatter = new HelpFormatter();

        String cmdLineSyntax = "HelpFormatterTest <command>";

        formatter.printHelp("HelpFormatterTest", options, null, null, true);

        String expected = "HelpFormatterTest:\n"
                + "usage: HelpFormatterTest <command>\n";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintHelpWithCustomWidth() {
        Options options = new Options();
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        Option option3 = new Option("help", false, "help message");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);

        String cmdLineSyntax = "HelpFormatterTest <command>";

        formatter.printHelp("HelpFormatterTest", options, null, null);

        String expected = "HelpFormatterTest:\n"
                + "usage: HelpFormatterTest <command>\n"
                + "\n"
                + "options:\n"
                + "  -i, --input          input file\n"
                + "                       arg\n"
                + "  -o, --output         output file\n"
                + "                       arg\n"
                + "  -help, --help        help message\n";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintHelpWithCustomOptionGroup() {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("P", "Printing");
        Option option1 = OptionBuilder.withLongOpt("print")
                .withShortOpt("p")
                .withDescription("print the data")
                .withOptionGroup(group1)
                .create("print");
        Option option2 = OptionBuilder.withLongOpt("no-print")
                .withShortOpt("np")
                .withDescription("do not print the data")
                .withOptionGroup(group1)
                .create("no-print");
        Option option3 = new Option("help", false, "help message");

        HelpFormatter formatter = new HelpFormatter();

        String cmdLineSyntax = "HelpFormatterTest";

        formatter.printHelp("HelpFormatterTest", options);

        String expected = "HelpFormatterTest:\n"
                + "usage: HelpFormatterTest\n"
                + "\n"
                + "options:\n"
                + "  -help                  help message\n"
                + "\n"
                + "Printing options:\n"
                + "  -p, --print            print the data\n"
                + "  -np, --no-print        do not print the data\n";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintHelpWithCustomOptionOrder() {
        Options options = new Options();
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        Option option3 = new Option("h", "help", false, "help message");

        Comparator<Option> comparator = new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o1.getKey().compareToIgnoreCase(o2.getKey());
            }
        };

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(comparator);

        String cmdLineSyntax = "HelpFormatterTest";

        formatter.printHelp("HelpFormatterTest", options);

        String expected = "HelpFormatterTest:\n"
                + "usage: HelpFormatterTest\n"
                + "\n"
                + "options:\n"
                + "  -h, --help           help message\n"
                + "  -i, --input          input file\n"
                + "                       arg\n"
                + "  -o, --output         output file\n"
                + "                       arg\n";

        String result = System.out.getBuffer().toString();
        assertEquals(expected, result);
    }
}