package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;

import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testDefaultHelpFormatter() {
        String cmdLineSyntax = "mainClass [option1] [option2]";
        Options options = new Options();
        options.addOption("option1", true, "Option 1 description");
        options.addOption("option2", 1, "Option 2 description");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        // Add your assertions for the default formatted help output here.
    }

    @Test
    public void testCustomHelpFormatter() {
        String cmdLineSyntax = "myProgram [options]";
        Options options = new Options();
        options.addOption("inputFile", true, "Input file to process");
        options.addOption("outputFile", true, "Output file for result");
        options.addOption("verbose", false, "Verbose mode");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.setLeftPadding(10);
        formatter.setDescPadding(15);
        formatter.setSyntaxPrefix("usage: myProgram ");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("arg");

        formatter.printHelp(cmdLineSyntax, options);

        // Add your assertions for the custom formatted help output here.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(-5);
        formatter.printHelp("", new Options());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, new Options());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOptions() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("", null);
    }
}