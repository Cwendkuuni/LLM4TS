package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testDefaultHelp() throws ParseException {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOption("c", "optionC", true, "Option C");
        options.addOption("d", "optionD", false, "Option D");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testCustomHelp() throws ParseException {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOption("c", "optionC", true, "Option C");
        options.addOption("d", "optionD", false, "Option D");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.setSyntaxPrefix("Usage: java MyClass [options]\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("ARG");

        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWidth() throws ParseException {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOption("c", "optionC", true, "Option C");
        options.addOption("d", "optionD", false, "Option D");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(-1);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testEmptyOptions() throws ParseException {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testOptionsWithArg() throws ParseException {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A with argument");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testOptionsWithRequiredArg() throws ParseException {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A with required argument");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithNullCmdLineSyntax() throws ParseException {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        try {
            formatter.printHelp(null, options);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPrintHelpWithNullOptions() throws ParseException {
        String cmdLineSyntax = "java MyClass [options]";
        HelpFormatter formatter = new HelpFormatter();
        try {
            formatter.printHelp(cmdLineSyntax, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPrintHelpWithAutoUsage() throws ParseException {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, true);
    }
}