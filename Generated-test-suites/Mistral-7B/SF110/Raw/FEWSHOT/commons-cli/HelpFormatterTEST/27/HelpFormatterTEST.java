package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp_Defaults() {
        final String cmdLineSyntax = "java MyApp";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_CustomSyntaxPrefix() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("syntax: ");
        formatter.printHelp(cmdLineSyntax, options);
        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelp_CustomWidth() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_CustomLeftPadding() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_CustomDescPadding() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(10);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithOptions() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final Options options = new Options();
        options.addRequiredOption("i", "input", true, "the input file");
        options.addOption("o", "output", true, "the output file");
        options.addOption("v", false, "verbose");
        options.addOption("h", false, "help");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithHeaderAndFooter() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final String header = "My Tool";
        final String footer = "Copyright 2022 My Company";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("syntax: ");
        formatter.setHeader(header);
        formatter.setFooter(footer);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithCustomOptPrefix() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final String optPrefix = "--";
        final Options options = new Options();
        options.addRequiredOption("i", "input", true, "the input file");
        options.addOption("o", "output", true, "the output file");
        options.addOption("v", false, "verbose");
        options.addOption("h", false, "help");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("syntax: ");
        formatter.setOptPrefix(optPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithCustomLongOptPrefix() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final String longOptPrefix = "--";
        final Options options = new Options();
        options.addRequiredOption("i", "input", true, "the input file");
        options.addOption("o", "output", true, "the output file");
        options.addOption("v", false, "verbose");
        options.addOption("help", false, "help");
        options.addOptionGroup(new OptionGroup("input-options", "Input options"));
        options.addOption("i-file", false, "input-options", "use a file for input");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("syntax: ");
        formatter.setLongOptPrefix(longOptPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithCustomArgName() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final String argName = "filename";
        final Options options = new Options();
        options.addRequiredOption("i", "input", true, "the " + argName);
        options.addOption("o", "output", true, "the " + argName);
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName(argName);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithCustomOptionComparator() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final Options options = new Options();
        options.addRequiredOption("i", "input", true, "the input file");
        options.addOption("o", "output", true, "the output file");
        options.addOption("v", false, "verbose");
        options.addOption("h", false, "help");
        final HelpFormatter formatter = new HelpFormatter();
        final OptionComparator customComparator = new OptionComparator() {
            @Override
            public int compare(final Option o1, final Option o2) {
                return o1.getOpt().compareToIgnoreCase(o2.getOpt());
            }
        };
        formatter.setOptionComparator(customComparator);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithCustomLineSeparator() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final String newLine = "\n--------------------";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine(newLine);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithCustomOptionOrder() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final Options options = new Options();
        options.addRequiredOption("i", "input", true, "the input file");
        options.addOption("h", false, "help");
        options.addOption("o", "output", true, "the output file");
        options.addOption("v", false, "verbose");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
        formatter.printHelp(cmdLineSyntax, options, true);
        final List<Option> sortedOptions = Arrays.asList(options.getOptions());
        Collections.sort(sortedOptions, formatter.getOptionComparator());
        final String sortedHelp = formatter.printHelp(cmdLineSyntax, options, true);
        assertEquals(sortedHelp, formatter.printHelp(cmdLineSyntax, options, false));
    }

    @Test
    public void testPrintHelp_WithCustomOptionGroup() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final Options options = new Options();
        final OptionGroup inputOptions = new OptionGroup("input-options", "Input options");
        inputOptions.addOption("i-file", false, "input-options", "use a file for input");
        inputOptions.addOption("i-dir", false, "input-options", "use a directory for input");
        options.addOptionGroup(inputOptions);
        options.addRequiredOption("i", "input", true, "the input");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithCustomOptionValueSeparator() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final String valueSeparator = "=";
        final Options options = new Options();
        options.addOption("i", "input", true, "the input file", valueSeparator);
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setValueSeparator(valueSeparator);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithCustomOptionValueSeparatorAndEmptyValue() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final String valueSeparator = "=";
        final Options options = new Options();
        options.addOption("i", "input", true, "the input file", valueSeparator);
        options.addOption("o", "output", true, "");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setValueSeparator(valueSeparator);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithCustomOptionValueSeparatorAndMultipleValues() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final String valueSeparator = "=";
        final Options options = new Options();
        options.addOption("i", "input", true, "the input files", valueSeparator);
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setValueSeparator(valueSeparator);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelp_WithCustomOptionValueSeparatorAndNoValue() {
        final String cmdLineSyntax = "mytool <arg1> [options]";
        final String valueSeparator = "=";
        final Options options = new Options();
        options.addOption("i", "input", false, "use input");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setValueSeparator(valueSeparator);
        formatter.printHelp(cmdLineSyntax, options);
    }
}