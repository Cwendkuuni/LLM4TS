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

    @org.junit.Test
    public void testHelpFormatterDefaults() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testHelpFormatterCustomWidth() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        formatter.setWidth(80);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testHelpFormatterCustomLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        formatter.setLeftPadding(5);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testHelpFormatterCustomDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        formatter.setDescPadding(10);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testHelpFormatterCustomSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        formatter.setSyntaxPrefix("usage: myapp: ");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testHelpFormatterCustomNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        String newline = "\n------------------------";
        formatter.setNewLine(newline);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testHelpFormatterCustomOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        formatter.setOptPrefix("--");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testHelpFormatterCustomLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        formatter.setLongOptPrefix("--");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testHelpFormatterCustomArgName() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        formatter.setArgName("param");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testHelpFormatterCustomOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        formatter.setOptionComparator(new CustomOptionComparator());
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testHelpFormatterWithOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        Option opt = new Option("i", "inputFile", true, "Input file");
        OptionGroup group = new OptionGroup("inputGroup", "Input options");
        group.addOption(opt);
        options.addOptionGroup(group);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testHelpFormatterWithHeaders() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        String header = "Options for MyApp";
        String footer = "For more information visit http://www.example.com";
        formatter.printHelp(cmdLineSyntax, options, header, footer);
    }

    @org.junit.Test
    public void testHelpFormatterWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        Option opt = new Option("i", "inputFile", true, "Input file");
        OptionGroup group = new OptionGroup("inputGroup", "Input options");
        group.addOption(opt);
        options.addOptionGroup(group);
        formatter.printHelp(cmdLineSyntax, options, null, null, true);
    }

    @org.junit.Test
    public void testHelpFormatterPrintToPrintWriter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options]";
        Options options = new Options();
        PrintWriter writer = new PrintWriter(System.out);
        formatter.printHelp(cmdLineSyntax, options, writer, 80, 1, 1, null, false);
    }

    private static class CustomOptionComparator implements Comparator<Option> {
        @Override
        public int compare(Option o1, Option o2) {
            int result = o1.getKey().compareToIgnoreCase(o2.getKey());
            if (result == 0) {
                result = o2.isRequired() ? 1 : -1;
            }
            return result;
        }
    }
}