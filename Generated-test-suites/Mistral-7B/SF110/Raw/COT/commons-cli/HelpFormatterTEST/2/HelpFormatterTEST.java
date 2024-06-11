package org.junit.tests;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class HelpFormatterTest {

    

    @Test
    public void testDefaultConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        Assert.assertEquals(74, formatter.getWidth());
        Assert.assertEquals(1, formatter.getLeftPadding());
        Assert.assertEquals(3, formatter.getDescPadding());
        // other default values can be checked similarly
    }

    @Test
    public void testSettersAndGetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(7);
        Assert.assertEquals(100, formatter.getWidth());
        Assert.assertEquals(5, formatter.getLeftPadding());
        Assert.assertEquals(7, formatter.getDescPadding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(null);
    }

    @Test
    public void testPrintHelpWithNoOptions() {
        String cmdLineSyntax = "myCommand";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax);
    }

    @Test
    public void testPrintHelpWithOptions() {
        String cmdLineSyntax = "myCommand [option1] [option2]";
        Options options = new Options();
        Option opt1 = new Option("o", "option1", false, "Option 1");
        Option opt2 = new Option("e", "option2", true, "Option 2 with argument");
        options.addOption(opt1);
        options.addOption(opt2);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullOptions() {
        String cmdLineSyntax = "myCommand [option]";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullAutoUsage() {
        String cmdLineSyntax = "myCommand [option]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, (boolean)null);
    }

    @Test
    public void testPrintHelpWithCustomWidth() {
        String cmdLineSyntax = "myCommand [option1] [option2]";
        Options options = new Options();
        Option opt1 = new Option("o", "option1", false, "Option 1");
        Option opt2 = new Option("e", "option2", true, "Option 2 with argument");
        options.addOption(opt1);
        options.addOption(opt2);
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintUsageWithNoApp() {
        HelpFormatter formatter = new HelpFormatter();
        String output = "";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(outContent);
        formatter.printUsage(pw, 100, null, new Options());
        output += outContent.toString();
        Assert.assertTrue(output.contains("usage:"));
    }

    @Test
    public void testPrintUsageWithApp() {
        HelpFormatter formatter = new HelpFormatter();
        String app = "myCommand";
        String output = "";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(outContent);
        formatter.printUsage(pw, 100, app, new Options());
        output += outContent.toString();
        Assert.assertTrue(output.contains("usage: myCommand"));
    }

    @Test
    public void testPrintWrappedText() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a very long string that needs to be wrapped.";
        int width = 30;
        String output = "";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(outContent);
        formatter.printWrapped(pw, width, text);
        output += outContent.toString();
        Assert.assertEquals(3, output.split(System.lineSeparator().replaceAll("\\s+", "")).length);
    }

    @Test
    public void testFindWrapPos() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a very long string that needs to be wrapped.";
        int width = 30;
        int pos = formatter.findWrapPos(text, width, 0);
        Assert.assertNotEquals(-1, pos);
        Assert.assertEquals(17, pos);
    }

    @Test
    public void testCreatePadding() {
        HelpFormatter formatter = new HelpFormatter();
        int len = 20;
        String padding = formatter.createPadding(len);
        Assert.assertEquals(len, padding.length());
    }

    @Test
    public void testRenderWrappedText() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a very long string that needs to be wrapped and wrapped again.";
        int width = 30;
        String output = "";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(outContent);
        formatter.renderWrappedText(new StringBuffer(), width, 0, text);
        output += outContent.toString();
        Assert.assertEquals(3, output.split(System.lineSeparator().replaceAll("\\s+", "")).length);
    }

    @Test
    public void testRenderOptions() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        Option opt1 = new Option("o", "opt1", false, "Option 1");
        Option opt2 = new Option("e", "opt2", true, "Option 2 with argument");
        Option opt3 = new Option("a", "opt3", false, "Option 3 without argument");
        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);
        String output = "";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(outContent);
        formatter.renderOptions(pw, 30, options, 0, 0);
        output += outContent.toString();
        Assert.assertTrue(output.contains("-o, --opt1"));
        Assert.assertTrue(output.contains("-e, --opt2 <arg>"));
        Assert.assertTrue(output.contains("-a, --opt3"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("x", "invalid", true, "Invalid option");
        options.addOption(option);
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "myCommand [option]";
        formatter.parse(new StringTokenizer("myCommand x invalid arg"), cmdLineSyntax, options, true);
    }

    
}