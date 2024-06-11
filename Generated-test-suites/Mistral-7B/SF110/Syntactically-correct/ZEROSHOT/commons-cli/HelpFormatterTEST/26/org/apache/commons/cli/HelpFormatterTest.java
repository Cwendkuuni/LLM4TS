package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class HelpFormatterTest {

    @org.junit.Before
    public void setUp() {
        HelpFormatter formatter = new HelpFormatter();
    }

    @org.junit.Test
    public void testDefaultConstruction() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("usage: <command> [options]", formatter.getSyntaxPrefix());
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("-", formatter.getOptPrefix());
        assertEquals("--", formatter.getLongOptPrefix());
        assertEquals("arg", formatter.getArgName());
    }

    @org.junit.Test
    public void testWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @org.junit.Test
    public void testLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(2);
        assertEquals(2, formatter.getLeftPadding());
    }

    @org.junit.Test
    public void testDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(5);
        assertEquals(5, formatter.getDescPadding());
    }

    @org.junit.Test
    public void testSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("help ");
        assertEquals("help ", formatter.getSyntaxPrefix());
    }

    @org.junit.Test
    public void testNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        String newline = System.getProperty("line.separator");
        formatter.setNewLine(newline);
        assertEquals(newline, formatter.getNewLine());
    }

    @org.junit.Test
    public void testOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getOptPrefix());
    }

    @org.junit.Test
    public void testLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("-");
        assertEquals("-", formatter.getLongOptPrefix());
    }

    @org.junit.Test
    public void testArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("argName");
        assertEquals("argName", formatter.getArgName());
    }

    @org.junit.Test
    public void testOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        HelpFormatter.OptionComparator comparator = formatter.getOptionComparator();
        assertNotNull(comparator);
    }

    @org.junit.Test
    public void testPrintHelpWithDefaults() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("longOpt1").withDescription("long option 1").create();
        Option option2 = OptionBuilder.withLongOpt("longOpt2").withDescription("long option 2").create();
        Option option3 = OptionBuilder.withLongOpt("longOpt3").withDescription("long option 3").create();
        Option option4 = OptionBuilder.withLongOpt("longOpt4").withDescription("long option 4").create();
        Option option5 = OptionBuilder.withLongOpt("longOpt5").withDescription("long option 5").create();
        Option option6 = OptionBuilder.withLongOpt("longOpt6").withDescription("long option 6").create();
        Option option7 = OptionBuilder.withLongOpt("longOpt7").withDescription("long option 7").create();
        Option option8 = OptionBuilder.withLongOpt("longOpt8").withDescription("long option 8").create();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);
        options.addOption(option6);
        options.addOption(option7);
        options.addOption(option8);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testPrintHelpWithWidth() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("longOpt1").withDescription("long option 1").create();
        Option option2 = OptionBuilder.withLongOpt("longOpt2").withDescription("long option 2").create();
        Option option3 = OptionBuilder.withLongOpt("longOpt3").withDescription("long option 3").create();
        Option option4 = OptionBuilder.withLongOpt("longOpt4").withDescription("long option 4").create();
        Option option5 = OptionBuilder.withLongOpt("longOpt5").withDescription("long option 5").create();
        Option option6 = OptionBuilder.withLongOpt("longOpt6").withDescription("long option 6").create();
        Option option7 = OptionBuilder.withLongOpt("longOpt7").withDescription("long option 7").create();
        Option option8 = OptionBuilder.withLongOpt("longOpt8").withDescription("long option 8").create();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);
        options.addOption(option6);
        options.addOption(option7);
        options.addOption(option8);
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @org.junit.Test
    public void testPrintHelpWithHeader() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass [options]";
        String header = "My class help";
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("longOpt1").withDescription("long option 1").create();
        Option option2 = OptionBuilder.withLongOpt("longOpt2").withDescription("long option 2").create();
        Option option3 = OptionBuilder.withLongOpt("longOpt3").withDescription("long option 3").create();
        Option option4 = OptionBuilder.withLongOpt("longOpt4").withDescription("long option 4").create();
        Option option5 = OptionBuilder.withLongOpt("longOpt5").withDescription("long option 5").create();
        Option option6 = OptionBuilder.withLongOpt("longOpt6").withDescription("long option 6").create();
        Option option7 = OptionBuilder.withLongOpt("longOpt7").withDescription("long option 7").create();
        Option option8 = OptionBuilder.withLongOpt("longOpt8").withDescription("long option 8").create();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);
        options.addOption(option6);
        options.addOption(option7);
        options.addOption(option8);
        formatter.printHelp(cmdLineSyntax, header, options);
    }

    @org.junit.Test
    public void testPrintHelpWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("longOpt1").withDescription("long option 1").create();
        Option option2 = OptionBuilder.withLongOpt("longOpt2").withDescription("long option 2").create();
        Option option3 = OptionBuilder.withLongOpt("longOpt3").withDescription("long option 3").create();
        Option option4 = OptionBuilder.withLongOpt("longOpt4").withDescription("long option 4").create();
        Option option5 = OptionBuilder.withLongOpt("longOpt5").withDescription("long option 5").create();
        Option option6 = OptionBuilder.withLongOpt("longOpt6").withDescription("long option 6").create();
        Option option7 = OptionBuilder.withLongOpt("longOpt7").withDescription("long option 7").create();
        Option option8 = OptionBuilder.withLongOpt("longOpt8").withDescription("long option 8").create();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);
        options.addOption(option6);
        options.addOption(option7);
        options.addOption(option8);
        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @org.junit.Test
    public void testPrintHelpWithFooter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass [options]";
        String header = "My class help";
        String footer = "My class footer";
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("longOpt1").withDescription("long option 1").create();
        Option option2 = OptionBuilder.withLongOpt("longOpt2").withDescription("long option 2").create();
        Option option3 = OptionBuilder.withLongOpt("longOpt3").withDescription("long option 3").create();
        Option option4 = OptionBuilder.withLongOpt("longOpt4").withDescription("long option 4").create();
        Option option5 = OptionBuilder.withLongOpt("longOpt5").withDescription("long option 5").create();
        Option option6 = OptionBuilder.withLongOpt("longOpt6").withDescription("long option 6").create();
        Option option7 = OptionBuilder.withLongOpt("longOpt7").withDescription("long option 7").create();
        Option option8 = OptionBuilder.withLongOpt("longOpt8").withDescription("long option 8").create();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);
        options.addOption(option6);
        options.addOption(option7);
        options.addOption(option8);
        formatter.printHelp(cmdLineSyntax, header, options, footer);
    }

    @org.junit.Test
    public void testPrintHelpWithCustomOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("longOpt1").withDescription("long option 1").withOrder(1).create();
        Option option2 = OptionBuilder.withLongOpt("longOpt2").withDescription("long option 2").withOrder(2).create();
        Option option3 = OptionBuilder.withLongOpt("longOpt3").withDescription("long option 3").create();
        Option option4 = OptionBuilder.withLongOpt("longOpt4").withDescription("long option 4").create();
        Option option5 = OptionBuilder.withLongOpt("longOpt5").withDescription("long option 5").create();
        Option option6 = OptionBuilder.withLongOpt("longOpt6").withDescription("long option 6").create();
        Option option7 = OptionBuilder.withLongOpt("longOpt7").withDescription("long option 7").create();
        Option option8 = OptionBuilder.withLongOpt("longOpt8").withDescription("long option 8").create();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);
        options.addOption(option6);
        options.addOption(option7);
        options.addOption(option8);
        HelpFormatter.OptionComparator comparator = new HelpFormatter.OptionComparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Option opt1 = (Option)o1;
                Option opt2 = (Option)o2;
                return opt1.getOrder() - opt2.getOrder();
            }
        };
        formatter.setOptionComparator(comparator);
        formatter.printHelp(cmdLineSyntax, options);
    }
}