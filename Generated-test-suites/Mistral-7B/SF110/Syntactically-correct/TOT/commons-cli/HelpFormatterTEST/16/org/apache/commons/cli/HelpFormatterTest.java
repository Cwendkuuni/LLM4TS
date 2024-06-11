package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Test for HelpFormatter class
 */
public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithWidth() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        String cmdLineSyntax = null;
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test(expected = ParseException.class)
    public void testPrintHelpWithInvalidOptions() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");
        options.addOption("z", "zoo", true, "Zoo visit.");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomSyntaxPrefix() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("myapp: ");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomOptPrefix() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomLongOptPrefix() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--my-");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomArgName() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("parameter");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomLeftPadding() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(10);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomDescPadding() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(10);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomHeader() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");

        HelpFormatter formatter = new HelpFormatter();
        String header = "Usage: MyApp [options] arg1 arg2";
        formatter.printHelp(cmdLineSyntax, header, options);
    }

    @Test
    public void testPrintHelpWithCustomFooter() {
        String cmdLineSyntax = "MyApp [options] arg1 arg2";
        Options options = new Options();
        options.addRequiredOption("h", "help", false, "Print this help message.");
        options.addRequiredOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Verbose output.");

        HelpFormatter formatter = new HelpFormatter();
        String footer = "For more information, visit www.myapp.com";
        formatter.printHelp(cmdLineSyntax, options, footer);
    }
}