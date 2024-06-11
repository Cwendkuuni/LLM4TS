package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testDefaultInitialization() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
        assertEquals("-", formatter.getOptPrefix());
        assertEquals("--", formatter.getLongOptPrefix());
        assertEquals("arg", formatter.getArgName());
        assertNull(formatter.getOptionComparator());
    }

    @Test
    public void testSettingWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testSettingLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(3);
        assertEquals(3, formatter.getLeftPadding());
    }

    @Test
    public void testSettingDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(5);
        assertEquals(5, formatter.getDescPadding());
    }

    @Test
    public void testSettingSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("help: ");
        assertEquals("help: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSettingNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\n");
        assertEquals("\n", formatter.getNewLine());
    }

    @Test
    public void testSettingOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("/");
        assertEquals("/", formatter.getOptPrefix());
    }

    @Test
    public void testSettingLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--");
        assertEquals("--", formatter.getLongOptPrefix());
    }

    @Test
    public void testSettingArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");
        assertEquals("param", formatter.getArgName());
    }

    @Test
    public void testSettingOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        Comparator<Option> comparator = new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o1.getKey().compareToIgnoreCase(o2.getKey());
            }
        };
        formatter.setOptionComparator(comparator);
        assertEquals(comparator, formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelpDefault() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWidth() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpCustomSyntaxPrefix() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("options: ");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpCustomLeftPadding() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpCustomDescPadding() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(7);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptions() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        String header = "Options:";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        options.addOption(OptionBuilder.withLongOpt("quiet").withDescription("Quiet mode").create('q'));
        options.addOptionGroup(new OptionGroup("Group", "Group Options"))
            .addOption(OptionBuilder.withLongOpt("g1").withDescription("Option 1 in group").withRequiredArg(true).create('g'))
            .addOption(OptionBuilder.withLongOpt("g2").withDescription("Option 2 in group").withRequiredArg(false).create('t'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, header, options);
    }

    @Test
    public void testPrintHelpWithFooter() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        String header = "Options:";
        String footer = "Thank you for using our application.";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        options.addOption(OptionBuilder.withLongOpt("quiet").withDescription("Quiet mode").create('q'));
        options.addOptionGroup(new OptionGroup("Group", "Group Options"))
            .addOption(OptionBuilder.withLongOpt("g1").withDescription("Option 1 in group").withRequiredArg(true).create('g'))
            .addOption(OptionBuilder.withLongOpt("g2").withDescription("Option 2 in group").withRequiredArg(false).create('t'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, header, options, footer);
    }

    @Test
    public void testPrintHelpAutoUsage() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        String header = "Options:";
        String footer = "Thank you for using our application.";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        options.addOption(OptionBuilder.withLongOpt("quiet").withDescription("Quiet mode").create('q'));
        options.addOptionGroup(new OptionGroup("Group", "Group Options"))
            .addOption(OptionBuilder.withLongOpt("g1").withDescription("Option 1 in group").withRequiredArg(true).create('g'))
            .addOption(OptionBuilder.withLongOpt("g2").withDescription("Option 2 in group").withRequiredArg(false).create('t'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, header, options, footer);
    }

    @Test
    public void testPrintHelpPrintWrapped() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        String header = "Options:";
        String footer = "Thank you for using our application.";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        options.addOption(OptionBuilder.withLongOpt("quiet").withDescription("Quiet mode").create('q'));
        options.addOptionGroup(new OptionGroup("Group", "Group Options"))
            .addOption(OptionBuilder.withLongOpt("g1").withDescription("Long description for option 1 in group, longer than the width.").withRequiredArg(true).create('g'))
            .addOption(OptionBuilder.withLongOpt("g2").withDescription("Option 2 in group, width-compatible description.").withRequiredArg(false).create('t'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, header, options, footer);
    }

    @Test
    public void testPrintUsage() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        String header = "Options:";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(System.out, cmdLineSyntax, header, options);
    }

    @Test
    public void testPrintUsageWidth() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        String header = "Options:";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printUsage(System.out, cmdLineSyntax, header, options);
    }

    @Test
    public void testPrintUsageCustomSyntaxPrefix() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        String header = "Options:";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("Usage: ");
        formatter.printUsage(System.out, cmdLineSyntax, header, options);
    }

    @Test
    public void testPrintUsageWithOptions() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        String header = "Options:";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        options.addOption(OptionBuilder.withLongOpt("quiet").withDescription("Quiet mode").create('q'));
        options.addOptionGroup(new OptionGroup("Group", "Group Options"))
            .addOption(OptionBuilder.withLongOpt("g1").withDescription("Option 1 in group").withRequiredArg(true).create('g'))
            .addOption(OptionBuilder.withLongOpt("g2").withDescription("Option 2 in group").withRequiredArg(false).create('t'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(System.out, cmdLineSyntax, header, options);
    }

    @Test
    public void testPrintUsageCustomLeftPadding() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        String header = "Options:";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.printUsage(System.out, cmdLineSyntax, header, options);
    }

    @Test
    public void testPrintUsageCustomDescPadding() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatterTest [options]";
        String header = "Options:";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").withDescription("Print help message").create('h'));
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(7);
        formatter.printUsage(System.out, cmdLineSyntax, header, options);
    }
}