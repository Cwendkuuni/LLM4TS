package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * HelpFormatterTest class to test all methods in HelpFormatter class.
 *
 * @author [Your Name]
 */
public class HelpFormatterTest {

    @Test
    public void testDefaultConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(HelpFormatter.DEFAULT_WIDTH, formatter.getWidth());
        assertEquals(HelpFormatter.DEFAULT_LEFT_PAD, formatter.getLeftPadding());
        assertEquals(HelpFormatter.DEFAULT_DESC_PAD, formatter.getDescPadding());
        assertEquals(HelpFormatter.DEFAULT_SYNTAX_PREFIX, formatter.getSyntaxPrefix());
        assertEquals(HelpFormatter.DEFAULT_NEW_LINE, formatter.getNewLine());
        assertEquals(HelpFormatter.DEFAULT_OPT_PREFIX, formatter.getOptPrefix());
        assertEquals(HelpFormatter.DEFAULT_LONG_OPT_PREFIX, formatter.getLongOptPrefix());
        assertEquals(HelpFormatter.DEFAULT_ARG_NAME, formatter.getArgName());
    }

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        assertEquals(100, formatter.getWidth());
    }

    @Test
    public void testGetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(HelpFormatter.DEFAULT_WIDTH, formatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());
    }

    @Test
    public void testGetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(HelpFormatter.DEFAULT_LEFT_PAD, formatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(7);
        assertEquals(7, formatter.getDescPadding());
    }

    @Test
    public void testGetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(HelpFormatter.DEFAULT_DESC_PAD, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("custom: ");
        assertEquals("custom: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testGetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(HelpFormatter.DEFAULT_SYNTAX_PREFIX, formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\nCustom NewLine");
        assertEquals("\nCustom NewLine", formatter.getNewLine());
    }

    @Test
    public void testGetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(HelpFormatter.DEFAULT_NEW_LINE, formatter.getNewLine());
    }

    @Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("custom-");
        assertEquals("custom-", formatter.getOptPrefix());
    }

    @Test
    public void testGetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(HelpFormatter.DEFAULT_OPT_PREFIX, formatter.getOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--custom-");
        assertEquals("--custom-", formatter.getLongOptPrefix());
    }

    @Test
    public void testGetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(HelpFormatter.DEFAULT_LONG_OPT_PREFIX, formatter.getLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("customArg");
        assertEquals("customArg", formatter.getArgName());
    }

    @Test
    public void testGetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(HelpFormatter.DEFAULT_ARG_NAME, formatter.getArgName());
    }

    @Test
    public void testPrintHelpDefault() throws Exception {
        String cmdLineSyntax = "java org.apache.commons.cli.example.Test -a arg1 -b arg2";
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addRequiredOption(null, "a", false, "Option A");
        options.addRequiredOption(null, "b", false, "Option B");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpCustomSyntax() throws Exception {
        String cmdLineSyntax = "myapp [options]";
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addRequiredOption(null, "a", false, "Option A");
        options.addRequiredOption(null, "b", false, "Option B");
        formatter.setSyntaxPrefix("myapp: ");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpCustomHeaderFooter() throws Exception {
        String cmdLineSyntax = "myapp [options]";
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addRequiredOption(null, "a", false, "Option A");
        options.addRequiredOption(null, "b", false, "Option B");
        formatter.setSyntaxPrefix("myapp: ");
        formatter.setHeader("Custom Header");
        formatter.setFooter("Custom Footer");
        formatter.printHelp(cmdLineSyntax, options, "Custom Header\n", "Custom Footer");
    }

    @Test
    public void testPrintHelpCustomLeftRightPadding() throws Exception {
        String cmdLineSyntax = "myapp [options]";
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addRequiredOption(null, "a", false, "Option A");
        options.addRequiredOption(null, "b", false, "Option B");
        formatter.setSyntaxPrefix("myapp: ");
        formatter.setLeftPadding(5);
        formatter.setDescPadding(8);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintUsage() throws Exception {
        String cmdLineSyntax = "myapp [options]";
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addRequiredOption(null, "a", false, "Option A");
        options.addRequiredOption(null, "b", false, "Option B");
        formatter.printUsage(System.out, 80, cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithOptionGroups() throws Exception {
        String cmdLineSyntax = "myapp [options]";
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();

        OptionGroup group = new OptionGroup("mygroup");
        group.setDescription("My Group");

        optionGroup.addRequiredOption(null, "a", false, "Option A1", "A1");
        optionGroup.addRequiredOption(null, "b", false, "Option B1", "B1");
        options.addOptionGroup(group);

        OptionGroup group2 = new OptionGroup("mygroup2");
        group2.setDescription("My Group 2");

        optionGroup2.addRequiredOption(null, "c", false, "Option C1", "C1");
        optionGroup2.addRequiredOption(null, "d", false, "Option D1", "D1");
        options.addOptionGroup(group2);

        formatter.printHelp(cmdLineSyntax, options);
    }
}