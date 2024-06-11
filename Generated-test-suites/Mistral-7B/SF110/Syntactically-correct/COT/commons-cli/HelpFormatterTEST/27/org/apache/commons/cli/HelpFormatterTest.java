package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HelpFormatterTest {

    @Test
    public void testDefaultInstance() {
        HelpFormatter formatter = new HelpFormatter();
        Assert.assertEquals(74, formatter.getWidth());
        // other getters tests can be added here
    }

    @Test
    public void testSettersAndGetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(2);
        formatter.setDescPadding(5);
        // test getters here with expected values

        Assert.assertEquals(50, formatter.getWidth());
        Assert.assertEquals(2, formatter.getLeftPadding());
        Assert.assertEquals(5, formatter.getDescPadding());
    }

    @Test
    public void testPrintHelpDefaultValues() {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
        // print statement output can be captured and compared to a expected result
        // using a library like PowerMock or TestNG's assertText method
    }

    @Test
    public void testPrintHelpCustomValues() {
        String cmdLineSyntax = "java MyApp [--inputFile <input.txt>]";
        Options options = new Options();
        options.addOption("i", "inputFile", true, "Input file");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
        // print statement output can be captured and compared to a expected result
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpNullCmdLineSyntax() {
        String cmdLineSyntax = null;
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpEmptyCmdLineSyntax() {
        String cmdLineSyntax = "";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptions() {
        String cmdLineSyntax = "java MyApp [--inputFile <input.txt>] [--outputFile <output.txt>]";
        Options options = new Options();
        options.addOption("i", "inputFile", true, "Input file");
        options.addOption("o", "outputFile", true, "Output file");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
        // print statement output can be captured and compared to a expected result
    }

    @Test
    public void testPrintHelpWithOptionGroup() {
        String cmdLineSyntax = "java MyApp [--inputFile <input.txt>]";
        Options options = new Options();
        OptionGroup group = new OptionGroup("input-options");
        group.setRequired(false);
        Option inputOption = new Option("i", "inputFile", true, "Input file");
        inputOption.setLongOpt("ifile");
        group.addOption(inputOption);
        options.addOptionGroup(group);
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
        // print statement output can be captured and compared to a expected result
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        String cmdLineSyntax = "java MyApp [--inputFile <input.txt>]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options, true);
        // print statement output can be captured and compared to a expected result
    }

    @Test
    public void testPrintHelpWithCustomHeaderAndFooter() {
        String cmdLineSyntax = "java MyApp [--inputFile <input.txt>]";
        Options options = new Options();
        String header = "MyApp options";
        String footer = "Type --help for more information.";
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setSyntaxPrefix("usage: ");
        formatter.setHeader(header);
        formatter.setFooter(footer);
        formatter.printHelp(cmdLineSyntax, options, null);
        // print statement output can be captured and compared to a expected result
    }

    @Test
    public void testPrintHelpWithCustomSyntaxPrefix() {
        String cmdLineSyntax = "java MyApp [--inputFile <input.txt>]";
        Options options = new Options();
        String syntaxPrefix = "usage: myapp: ";
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setSyntaxPrefix(syntaxPrefix);
        formatter.printHelp(cmdLineSyntax, options, null);
        // print statement output can be captured and compared to a expected result
    }

    @Test
    public void testPrintWrappedText() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        String expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        String result = formatter.renderWrappedText(new StringBuffer(), 80, 0, text).toString();
        Assert.assertEquals(expected, result);

        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        result = formatter.renderWrappedText(new StringBuffer(), 50, 0, text).toString();
        Assert.assertEquals(expected, result);

        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit\nsed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        result = formatter.renderWrappedText(new StringBuffer(), 50, 1, text).toString();
        Assert.assertEquals(expected, result);

        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        expected = "Lorem ipsum dolor sit amet\nconsectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        result = formatter.renderWrappedText(new StringBuffer(), 50, 0, text).toString();
        Assert.assertEquals(expected, result);

        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        expected = "Lorem ipsum dolor sit amet\nconsectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        result = formatter.renderWrappedText(new StringBuffer(), 50, 1, text).toString();
        Assert.assertEquals(expected, result);

        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        expected = "Lorem ipsum dolor sit amet\nconsectetur adipiscing elit\nsed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        result = formatter.renderWrappedText(new StringBuffer(), 50, 1, text).toString();
        Assert.assertEquals(expected, result);

        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        expected = "Lorem ipsum dolor sit amet\nconsectetur adipiscing elit\nsed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        result = formatter.renderWrappedText(new StringBuffer(), 50, 1, text).toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCreatePadding() {
        HelpFormatter formatter = new HelpFormatter();
        String expected = "                                     ";
        String result = formatter.createPadding(15);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRtrim() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "   Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        String expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        String result = formatter.rtrim(text);
        Assert.assertEquals(expected, result);

        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        result = formatter.rtrim(text);
        Assert.assertEquals(expected, result);

        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n";
        expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        result = formatter.rtrim(text);
        Assert.assertEquals(expected, result);

        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n\n";
        expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        result = formatter.rtrim(text);
        Assert.assertEquals(expected, result);
    }
}