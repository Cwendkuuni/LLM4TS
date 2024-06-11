package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterDefaults() {
        final String cmdLineSyntax = "java MyApp [options...]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpFormatterCustomWidth() {
        final String cmdLineSyntax = "java MyApp [options...]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();

        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpFormatterCustomLeftPadding() {
        final String cmdLineSyntax = "java MyApp [options...]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();

        formatter.setLeftPadding(5);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpFormatterCustomDescPadding() {
        final String cmdLineSyntax = "java MyApp [options...]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();

        formatter.setDescPadding(10);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpFormatterCustomSyntaxPrefix() {
        final String cmdLineSyntax = "java MyApp [options...]";
        final String syntaxPrefix = "usage: myApp.jar <command> [options...]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();

        formatter.setSyntaxPrefix(syntaxPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpFormatterCustomNewLine() {
        final String cmdLineSyntax = "java MyApp [options...]";
        final String newline = "\n-------------------\n";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();

        formatter.setNewLine(newline);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpFormatterCustomOptPrefix() {
        final String cmdLineSyntax = "java MyApp [options...]";
        final String optPrefix = "-c";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();

        formatter.setOptPrefix(optPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpFormatterCustomLongOptPrefix() {
        final String cmdLineSyntax = "java MyApp [options...]";
        final String longOptPrefix = "--config-file=";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();

        formatter.setLongOptPrefix(longOptPrefix);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpFormatterCustomArgName() {
        final String cmdLineSyntax = "java MyApp [options...] <file>";
        final String argName = "filePath";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();

        formatter.setArgName(argName);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpFormatterEmptyArgs() {
        final String cmdLineSyntax = "java MyApp [options...]";
        final Options options = new Options();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            formatter.printHelp(cmdLineSyntax, options, true, null, null, null);
            Assert.fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testHelpFormatterNullArgs() {
        final String cmdLineSyntax = "java MyApp [options...]";
        final Options options = null;
        final HelpFormatter formatter = new HelpFormatter();

        try {
            formatter.printHelp(cmdLineSyntax, options);
            Assert.fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }
}