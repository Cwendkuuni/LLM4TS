package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterInstanceProperties() {
        HelpFormatter formatter = new HelpFormatter();
        Assert.assertEquals(74, formatter.getWidth());
        formatter.setWidth(50);
        Assert.assertEquals(50, formatter.getWidth());

        Assert.assertEquals(1, formatter.getLeftPadding());
        formatter.setLeftPadding(3);
        Assert.assertEquals(3, formatter.getLeftPadding());

        Assert.assertEquals(3, formatter.getDescPadding());
        formatter.setDescPadding(5);
        Assert.assertEquals(5, formatter.getDescPadding());

        Assert.assertEquals("usage: ", formatter.getSyntaxPrefix());
        formatter.setSyntaxPrefix("test: ");
        Assert.assertEquals("test: ", formatter.getSyntaxPrefix());

        Assert.assertEquals("\n", formatter.getNewLine());
        formatter.setNewLine("\r\n");
        Assert.assertEquals("\r\n", formatter.getNewLine());

        Assert.assertEquals("-", formatter.getOptPrefix());
        formatter.setOptPrefix("--");
        Assert.assertEquals("--", formatter.getOptPrefix());

        Assert.assertEquals("--", formatter.getLongOptPrefix());
        formatter.setLongOptPrefix("-");
        Assert.assertEquals("-", formatter.getLongOptPrefix());

        Assert.assertEquals("arg", formatter.getArgName());
        formatter.setArgName("param");
        Assert.assertEquals("param", formatter.getArgName());

        Assert.assertNull(formatter.getOptionComparator());
        formatter.setOptionComparator(Collections.reverseOrder());
        Assert.assertEquals(Collections.reverseOrder(), formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options] argument";
        Options options = new Options();
        options.addOption("a", "alpha", true, "Prints ALPHA message");
        options.addOption("b", "beta", false, "Prints BETA message");
        options.addOptionGroup(new OptionGroup("group1", "Group 1", Arrays.asList(
                new Option("g1a", "group1-option-a", true, "Prints group1-option-a message"),
                new Option("g1b", "group1-option-b", false, "Prints group1-option-b message"))));
        options.addOptionGroup(new OptionGroup("group2", "Group 2", Arrays.asList(
                new Option("g2a", "group2-option-a", false, "Prints group2-option-a message"),
                new Option("g2b", "group2-option-b", true, "Prints group2-option-b message"))));

        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithWidth() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options] argument";
        Options options = new Options();
        options.addOption("a", "alpha", true, "Prints ALPHA message");
        options.addOption("b", "beta", false, "Prints BETA message");
        options.addOptionGroup(new OptionGroup("group1", "Group 1", Arrays.asList(
                new Option("g1a", "group1-option-a", true, "Prints group1-option-a message"),
                new Option("g1b", "group1-option-b", false, "Prints group1-option-b message"))));
        options.addOptionGroup(new OptionGroup("group2", "Group 2", Arrays.asList(
                new Option("g2a", "group2-option-a", false, "Prints group2-option-a message"),
                new Option("g2b", "group2-option-b", true, "Prints group2-option-b message"))));

        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options] argument";
        Options options = new Options();
        options.addOption("a", "alpha", true, "Prints ALPHA message");
        options.addOption("b", "beta", false, "Prints BETA message");

        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp [options] argument";
        Options options = new Options();
        options.addOption("a", "alpha", true, "Prints ALPHA message");
        options.addOption("b", "beta", false, "Prints BETA message");

        String header = "My App Help";
        String footer = "For more information, visit http://www.example.com";

        formatter.printHelp(cmdLineSyntax, header, options, footer);
    }
}