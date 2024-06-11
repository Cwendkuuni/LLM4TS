package org.apache.commons.cli; // import necessary packages here

import org.junit.Assert;
import org.junit.Test;

public class HelpFormatterTest {
    

    @Test
    public void testDefaultConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        Assert.assertEquals(74, formatter.getWidth());
        Assert.assertEquals(1, formatter.getLeftPadding());
        Assert.assertEquals(3, formatter.getDescPadding());
        Assert.assertEquals("usage: ", formatter.getSyntaxPrefix());
        Assert.assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
        Assert.assertEquals("-", formatter.getDefaultOptPrefix());
        Assert.assertEquals("--", formatter.getDefaultLongOptPrefix());
        Assert.assertEquals("arg", formatter.getDefaultArgName());
    }

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        Assert.assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testPrintHelpWithValidCommandLineSyntaxAndOptions() {
        String cmdLineSyntax = "java MyClass -a 1 -b 2 -c arg1 --d arg2";
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        Option optA = new Option("a", true, "a", "An integer value");
        Option optB = new Option("b", true, "b", "A second integer value");
        Option optC = new Option("c", true, "c", "A string value");
        Option optD = new Option("d", true, "d", "A long string value");
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);
        options.addOption(optD);
        formatter.printHelp(System.out, cmdLineSyntax, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCommandLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, null, new Options());
    }

    @Test
    public void testPrintHelpWithEmptyCommandLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, "", new Options());
    }

    @Test
    public void testPrintHelpWithValidCommandLineSyntaxAndHeader() {
        String cmdLineSyntax = "java MyClass -a 1 -b 2 -c arg1 --d arg2";
        HelpFormatter formatter = new HelpFormatter();
        String header = "Options";
        Options options = new Options();
        Option optA = new Option("a", true, "a", "An integer value");
        Option optB = new Option("b", true, "b", "A second integer value");
        Option optC = new Option("c", true, "c", "A string value");
        Option optD = new Option("d", true, "d", "A long string value");
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);
        options.addOption(optD);
        formatter.printHelp(System.out, cmdLineSyntax, header, options);
    }

    @Test
    public void testPrintHelpWithValidCommandLineSyntaxAndFooter() {
        String cmdLineSyntax = "java MyClass -a 1 -b 2 -c arg1 --d arg2";
        HelpFormatter formatter = new HelpFormatter();
        String footer = "For more information, see www.example.com";
        Options options = new Options();
        Option optA = new Option("a", true, "a", "An integer value");
        Option optB = new Option("b", true, "b", "A second integer value");
        Option optC = new Option("c", true, "c", "A string value");
        Option optD = new Option("d", true, "d", "A long string value");
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);
        options.addOption(optD);
        formatter.printHelp(System.out, cmdLineSyntax, new Options(), footer);
    }

    @Test
    public void testPrintHelpWithValidCommandLineSyntaxAndAutoUsage() {
        String cmdLineSyntax = "java MyClass -a 1 -b 2 -c arg1 --d arg2";
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        Option optA = new Option("a", true, "a", "An integer value");
        Option optB = new Option("b", true, "b", "A second integer value");
        Option optC = new Option("c", true, "c", "A string value");
        Option optD = new Option("d", true, "d", "A long string value");
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);
        options.addOption(optD);
        formatter.printHelp(System.out, cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithValidCommandLineSyntaxAndAutoUsageAndHeader() {
        String cmdLineSyntax = "java MyClass -a 1 -b 2 -c arg1 --d arg2";
        HelpFormatter formatter = new HelpFormatter();
        String header = "Options";
        Options options = new Options();
        Option optA = new Option("a", true, "a", "An integer value");
        Option optB = new Option("b", true, "b", "A second integer value");
        Option optC = new Option("c", true, "c", "A string value");
        Option optD = new Option("d", true, "d", "A long string value");
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);
        options.addOption(optD);
        formatter.printHelp(System.out, cmdLineSyntax, header, options, true);
    }

    @Test
    public void testPrintHelpWithValidCommandLineSyntaxAndAutoUsageAndFooter() {
        String cmdLineSyntax = "java MyClass -a 1 -b 2 -c arg1 --d arg2";
        HelpFormatter formatter = new HelpFormatter();
        String footer = "For more information, see www.example.com";
        Options options = new Options();
        Option optA = new Option("a", true, "a", "An integer value");
        Option optB = new Option("b", true, "b", "A second integer value");
        Option optC = new Option("c", true, "c", "A string value");
        Option optD = new Option("d", true, "d", "A long string value");
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);
        options.addOption(optD);
        formatter.printHelp(System.out, cmdLineSyntax, new Options(), footer, true);
    }

    @Test
    public void testPrintHelpWithValidCommandLineSyntaxAndAutoUsageAndHeaderAndFooter() {
        String cmdLineSyntax = "java MyClass -a 1 -b 2 -c arg1 --d arg2";
        HelpFormatter formatter = new HelpFormatter();
        String header = "Options";
        String footer = "For more information, see www.example.com";
        Options options = new Options();
        Option optA = new Option("a", true, "a", "An integer value");
        Option optB = new Option("b", true, "b", "A second integer value");
        Option optC = new Option("c", true, "c", "A string value");
        Option optD = new Option("d", true, "d", "A long string value");
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);
        options.addOption(optD);
        formatter.printHelp(System.out, cmdLineSyntax, header, options, footer, true);
    }

    
}