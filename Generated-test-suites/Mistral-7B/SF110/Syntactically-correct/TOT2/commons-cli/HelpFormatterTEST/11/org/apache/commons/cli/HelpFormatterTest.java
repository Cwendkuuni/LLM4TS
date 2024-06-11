package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.PrintWriter;
import java.io.StringWriter;

@Category(UnitTest.class)
public class HelpFormatterTest {

    @Test
    public void testPrintHelpWithValidInputs() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        String header = "Options:";
        String footer = "Use -h or --help for more information.";
        formatter.printHelp(System.out, 80, cmdLineSyntax, header, options, footer);
        String expectedOutput = "usage: java MyApp\n" +
                "Options:\n";
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        formatter.printHelp(printWriter, 80, cmdLineSyntax, header, options, footer);
        String actualOutput = stringWriter.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = null;
        Options options = new Options();
        formatter.printHelp(System.out, 80, cmdLineSyntax, null, options, null, null, null);
    }

    @Test
    public void testPrintHelpWithEmptyCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "";
        Options options = new Options();
        formatter.printHelp(System.out, 80, cmdLineSyntax, null, options, null, null, null);
        String expectedOutput = "";
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        formatter.printHelp(printWriter, 80, cmdLineSyntax, null, options, null, null, null);
        String actualOutput = stringWriter.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";
        Options options = null;
        formatter.printHelp(System.out, 80, cmdLineSyntax, null, options, null, null, null);
    }

    @Test
    public void testPrintHelpWithEmptyOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        formatter.printHelp(System.out, 80, cmdLineSyntax, null, options, null, null, null);
        String expectedOutput = "usage: java MyApp\n";
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        formatter.printHelp(printWriter, 80, cmdLineSyntax, null, options, null, null, null);
        String actualOutput = stringWriter.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}