package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter test -a 1 -b 2 -c --optionC 3 --optionD";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter test -a 1 -b 2 -c --optionC 3 --optionD";
        String header = "Test Options";
        String footer = "Example usage:";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("");
        formatter.setHeader(header);
        formatter.setFooter(footer);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter test";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithCustomWidth() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter test -a 1 -b 2 -c --optionC 3 --optionD";
        int width = 50;
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(width);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomLeftPad() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter test -a 1 -b 2 -c --optionC 3 --optionD";
        int leftPad = 10;
        int width = 50;
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(width);
        formatter.setLeftPadding(leftPad);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomDescPad() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter test -a 1 -b 2 -c --optionC 3 --optionD";
        int descPad = 5;
        int width = 50;
        int leftPad = 10;
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(width);
        formatter.setLeftPadding(leftPad);
        formatter.setDescPadding(descPad);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomSyntaxPrefix() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter test -a 1 -b 2 -c --optionC 3 --optionD";
        String syntaxPrefix = "usage: test [options]";
        int width = 50;
        int leftPad = 10;
        int descPad = 5;
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(width);
        formatter.setLeftPadding(leftPad);
        formatter.setDescPadding(descPad);
        formatter.setSyntaxPrefix(syntaxPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomNewLine() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter test -a 1 -b 2 -c --optionC 3 --optionD";
        String newLine = "\n";
        int width = 50;
        int leftPad = 10;
        int descPad = 5;
        String syntaxPrefix = "usage: test [options]";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(width);
        formatter.setLeftPadding(leftPad);
        formatter.setDescPadding(descPad);
        formatter.setSyntaxPrefix(syntaxPrefix);
        formatter.setNewLine(newLine);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomOptPrefix() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter test -a 1 -b 2 -c --optionC 3 --optionD";
        String optPrefix = "-h";
        int width = 50;
        int leftPad = 10;
        int descPad = 5;
        String syntaxPrefix = "usage: test [options]";
        String newLine = "\n";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(width);
        formatter.setLeftPadding(leftPad);
        formatter.setDescPadding(descPad);
        formatter.setSyntaxPrefix(syntaxPrefix);
        formatter.setNewLine(newLine);
        formatter.setOptPrefix(optPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomLongOptPrefix() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter test -a 1 -b 2 -c --optionC 3 --optionD";
        String longOptPrefix = "--long";
        int width = 50;
        int leftPad = 10;
        int descPad = 5;
        String syntaxPrefix = "usage: test [options]";
        String newLine = "\n";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(width);
        formatter.setLeftPadding(leftPad);
        formatter.setDescPadding(descPad);
        formatter.setSyntaxPrefix(syntaxPrefix);
        formatter.setNewLine(newLine);
        formatter.setLongOptPrefix(longOptPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithCustomArgName() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter test -a 1 arg1 -b 2 arg2 -c --optionC 3 arg3 --optionD arg4";
        String argName = "arg";
        int width = 50;
        int leftPad = 10;
        int descPad = 5;
        String syntaxPrefix = "usage: test [options]";
        String newLine = "\n";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(width);
        formatter.setLeftPadding(leftPad);
        formatter.setDescPadding(descPad);
        formatter.setSyntaxPrefix(syntaxPrefix);
        formatter.setNewLine(newLine);
        formatter.setArgName(argName);
        formatter.printHelp(cmdLineSyntax, options);
    }
}