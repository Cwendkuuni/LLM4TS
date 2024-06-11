package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final String header = "My Command Line Application";
        final String footer = "For more information, visit http://www.example.com";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("  Usage: ");
        formatter.setWidth(80);
        formatter.setLeftPadding(2);
        formatter.setDescPadding(4);
        formatter.printHelp(cmdLineSyntax, header, options, footer);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithOptionalOption() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final Options options = new Options();
        options.addRequiredOption("r", "required", false, "This is a required option.");
        options.addOption("o", "optional", false, "This is an optional option.");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptionGroup() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final Options options = new Options();
        final OptionGroup group = new OptionGroup("formatter", "Formatting options");
        group.setRequired(false);
        group.addOption("f", "format", true, "Format the output using a specific format.");
        group.addOption("c", "color", false, "Enable color output.");
        options.addOptionGroup(group);
        options.addRequiredOption("r", "required", false, "This is a required option.");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithComplexOption() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final Options options = new Options();
        options.addOption("l", "list", true, "List all available themes.");
        options.addOption("t", "theme", true, "Set the theme for the output.");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithLongOptions() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final Options options = new Options();
        options.addOption("my-long-option", "this-is-a-long-option", false, "This is a long option.");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithArgumentOption() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options] [arg]";
        final Options options = new Options();
        options.addRequiredOption("a", "arg", true, "The argument option.");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithArgumentOptionAndArgName() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options] [argName] [arg]";
        final Options options = new Options();
        options.addOption("a", "argName", true, "The name of the argument.");
        options.addOption("a", "arg", true, "The argument value.");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("ARG");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithMultipleArguments() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options] [arg1] [arg2]";
        final Options options = new Options();
        options.addOption("a", "arg1", true, "The first argument.");
        options.addOption("b", "arg2", true, "The second argument.");
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("ARG");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomSyntaxPrefix() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final String syntaxPrefix = "usage: ";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix(syntaxPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomNewLine() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final String newLine = "\n-------------------------------------------\n";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine(newLine);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomWidth() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final int width = 50;
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(width);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomLeftPadding() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final int leftPadding = 5;
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(leftPadding);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomDescPadding() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final int descPadding = 5;
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(descPadding);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomOptPrefix() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final String optPrefix = "--";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix(optPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomLongOptPrefix() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options]";
        final String longOptPrefix = "long-";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix(longOptPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomArgName() {
        final String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter [options] [argName] [arg]";
        final String argName = "ARGUMENT";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName(argName);
        formatter.printHelp(cmdLineSyntax, options);
    }
}