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
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

/**
 * Test cases for HelpFormatter class
 */
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
        assertNull(formatter.getNewLine());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(-1);
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(-1);
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(5);
        assertEquals(5, formatter.getDescPadding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(-1);
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("help: ");
        assertEquals("help: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetSyntaxPrefixWithEmptyString() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("");
        assertEquals("", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\nSystem.out.println(\"New Line\")");
        String newLine = formatter.getNewLine();
        assertTrue(newLine.contains("New Line"));
    }

    @Test
    public void testSetOptPrefix() {
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
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("argName");
        assertEquals("argName", formatter.getDefaultArgName());
    }

    @Test
    public void testSetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Option)o1).getKey().compareToIgnoreCase(((Option)o2).getKey());
            }
        };
        formatter.setOptionComparator(comparator);
        assertEquals(comparator, formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        Option option1 = new Option("i", "inputFile", true, "input file");
        option1.setArgName("FILE");
        Option option2 = new Option("o", "outputFile", true, "output file");
        Option option3 = new Option("h", "help", false, "print this help message");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        formatter.printHelp(System.out, 80, cmdLineSyntax, options, null);

        String expectedHelp = "java MyApp [options]\n"
                + "\noptions:\n"
                + "  -i, --inputFile <FILE>  input file\n"
                + "  -o, --outputFile <FILE> output file\n"
                + "  -h, --help             print this help message\n";

        StringBuilder actualHelp = new StringBuilder();
        try (PrintWriter writer = new PrintWriter(new StringWriter())) {
            formatter.printHelp(writer, 80, cmdLineSyntax, options, null);
            actualHelp.append(writer.getBuffer().toString());
        }

        assertEquals(expectedHelp.toString(), actualHelp.toString());
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        String header = "MyApp Help";
        String footer = "For more information, visit http://example.com";
        Options options = new Options();
        Option option1 = new Option("i", "inputFile", true, "input file");
        option1.setArgName("FILE");
        Option option2 = new Option("o", "outputFile", true, "output file");
        Option option3 = new Option("h", "help", false, "print this help message");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        formatter.printHelp(System.out, 80, cmdLineSyntax, options, header, footer);

        String expectedHelp = "MyApp Help\n"
                + "java MyApp [options]\n"
                + "\noptions:\n"
                + "  -i, --inputFile <FILE>  input file\n"
                + "  -o, --outputFile <FILE> output file\n"
                + "  -h, --help             print this help message\n"
                + "\nFor more information, visit http://example.com";

        StringBuilder actualHelp = new StringBuilder();
        try (PrintWriter writer = new PrintWriter(new StringWriter())) {
            formatter.printHelp(writer, 80, cmdLineSyntax, options, header, footer);
            actualHelp.append(writer.getBuffer().toString());
        }

        assertEquals(expectedHelp.toString(), actualHelp.toString());
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        Option option1 = new Option("i", "inputFile", true, "input file");
        option1.setArgName("FILE");
        Option option2 = new Option("o", "outputFile", true, "output file");
        Option option3 = new Option("h", "help", false, "print this help message");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        formatter.printHelp(System.out, 80, cmdLineSyntax, options, null, true);

        String expectedHelp = "usage: java MyApp [options]\n"
                + "\noptions:\n"
                + "  -i, --inputFile <FILE>  input file\n"
                + "  -o, --outputFile <FILE> output file\n"
                + "  -h, --help             print this help message\n";

        StringBuilder actualHelp = new StringBuilder();
        try (PrintWriter writer = new PrintWriter(new StringWriter())) {
            formatter.printHelp(writer, 80, cmdLineSyntax, options, null, true);
            actualHelp.append(writer.getBuffer().toString());
        }

        assertEquals(expectedHelp.toString(), actualHelp.toString());
    }

    @Test
    public void testPrintHelpWithOptionGroups() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        String header = "MyApp Help";
        Options options = new Options();
        OptionGroup group1 = new OptionGroup();
        group1.setTitle("Input Options");
        Option inputOption1 = new Option("i", "inputFile1", true, "First input file");
        inputOption1.setArgName("FILE");
        inputOption1.setLongOpt("inputFile1");
        Option inputOption2 = new Option("i", "inputFile2", true, "Second input file");
        inputOption2.setArgName("FILE");
        inputOption2.setLongOpt("inputFile2");
        group1.addOption(inputOption1);
        group1.addOption(inputOption2);

        OptionGroup group2 = new OptionGroup();
        group2.setTitle("Output Options");
        Option outputOption1 = new Option("o", "outputFile1", true, "First output file");
        outputOption1.setArgName("FILE");
        outputOption1.setLongOpt("outputFile1");
        Option outputOption2 = new Option("o", "outputFile2", true, "Second output file");
        outputOption2.setArgName("FILE");
        outputOption2.setLongOpt("outputFile2");
        group2.addOption(outputOption1);
        group2.addOption(outputOption2);

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        Option option3 = new Option("h", "help", false, "print this help message");
        options.addOption(option3);

        formatter.printHelp(System.out, 80, cmdLineSyntax, options, header);

        String expectedHelp = "MyApp Help\n"
                + "java MyApp [options]\n\n"
                + "Options:\n"
                + "  -i, --inputFile1 <FILE>  First input file\n"
                + "  -i, --inputFile2 <FILE>  Second input file\n"
                + "  -o, --outputFile1 <FILE> First output file\n"
                + "  -o, --outputFile2 <FILE> Second output file\n"
                + "  -h, --help             print this help message\n"
                + "\nInput Options:\n"
                + "  -i inputFile1          First input file\n"
                + "  -i inputFile2          Second input file\n"
                + "\nOutput Options:\n"
                + "  -o outputFile1         First output file\n"
                + "  -o outputFile2         Second output file\n";

        StringBuilder actualHelp = new StringBuilder();
        try (PrintWriter writer = new PrintWriter(new StringWriter())) {
            formatter.printHelp(writer, 80, cmdLineSyntax, options, header);
            actualHelp.append(writer.getBuffer().toString());
        }

        assertEquals(expectedHelp.toString(), actualHelp.toString());
    }

    @Test
    public void testPrintHelpWithMixedOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        String header = "MyApp Help";
        Options options = new Options();
        Option inputOption = new Option("i", "inputFile", true, "Input file");
        inputOption.setArgName("FILE");
        Option outputOption = new Option("o", "outputFile", true, "Output file");
        outputOption.setArgName("FILE");
        Option helpOption = new Option("h", "help", false, "Print this help message");
        Option groupOption = new Option("g", "group", false, "Print group information");
        OptionGroup group = new OptionGroup();
        group.setTitle("Group Options");
        Option groupOption1 = new Option("a", "option1", false, "Option 1 in group");
        group.addOption(groupOption1);
        Option groupOption2 = new Option("b", "option2", false, "Option 2 in group");
        group.addOption(groupOption2);
        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);
        options.addOption(groupOption);
        options.addOptionGroup(group);

        formatter.printHelp(System.out, 80, cmdLineSyntax, options, header);

        String expectedHelp = "MyApp Help\n"
                + "java MyApp [options]\n\n"
                + "Options:\n"
                + "  -i, --inputFile <FILE>  Input file\n"
                + "  -o, --outputFile <FILE> Output file\n"
                + "  -h, --help             Print this help message\n"
                + "  -g, --group             Print group information\n"
                + "\nGroup Options:\n"
                + "  -a option1             Option 1 in group\n"
                + "  -b option2             Option 2 in group\n";

        StringBuilder actualHelp = new StringBuilder();
        try (PrintWriter writer = new PrintWriter(new StringWriter())) {
            formatter.printHelp(writer, 80, cmdLineSyntax, options, header);
            actualHelp.append(writer.getBuffer().toString());
        }

        assertEquals(expectedHelp.toString(), actualHelp.toString());
    }
}