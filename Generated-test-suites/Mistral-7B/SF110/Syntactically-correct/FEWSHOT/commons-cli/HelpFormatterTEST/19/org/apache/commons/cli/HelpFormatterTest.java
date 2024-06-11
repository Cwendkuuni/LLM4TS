package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterInstance() {
        HelpFormatter formatter = new HelpFormatter();
        assertNotNull(formatter);
        assertEquals(74, formatter.getDefaultWidth());
        assertEquals(1, formatter.getDefaultLeftPad());
        assertEquals(3, formatter.getDefaultDescPad());
        assertSame(DefaultSyntaxPrefix, formatter.getDefaultSyntaxPrefix());
        assertSame(System.getProperty("line.separator"), formatter.getDefaultNewLine());
        assertSame("-", formatter.getDefaultOptPrefix());
        assertSame("--", formatter.getDefaultLongOptPrefix());
        assertSame("arg", formatter.getDefaultArgName());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(4);
        assertEquals(4, formatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(5);
        assertEquals(5, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("myPrefix ");
        assertEquals("myPrefix ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\nMy Newline");
        assertEquals("\nMy Newline", formatter.getNewLine());
    }

    @Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        assertSame("--", formatter.getDefaultOptPrefix());
        assertSame("--", formatter.getOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("-L");
        assertSame("-L", formatter.getDefaultLongOptPrefix());
        assertSame("-L", formatter.getLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");
        assertSame("param", formatter.getDefaultArgName());
        assertSame("param", formatter.getArgName());
    }

    @Test
    public void testPrintHelpWithNullOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";
        formatter.printHelp(cmdLineSyntax);
    }

    @Test
    public void testPrintHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp <arg1> [option1] [option2]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("option1").withDescription("Option 1").create('o'));
        options.addOption(OptionBuilder.withLongOpt("option2").withDescription("Option 2").create('p'));
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp <arg1> [option1] [option2]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("option1").withDescription("Option 1").create('o'));
        options.addOption(OptionBuilder.withLongOpt("option2").withDescription("Option 2").create('p'));
        formatter.printHelp(cmdLineSyntax, options, "My App header", "My App footer");
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp <arg1> [option1] [option2]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("option1").withDescription("Option 1").create('o'));
        options.addOption(OptionBuilder.withLongOpt("option2").withDescription("Option 2").create('p'));
        formatter.printHelp(cmdLineSyntax, options, null, null, true);
    }

    @Test
    public void testPrintHelpWithCustomLeftPaddingAndDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp <arg1> [option1] [option2]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("option1").withDescription("Option 1").create('o'));
        options.addOption(OptionBuilder.withLongOpt("option2").withDescription("Option 2").create('p'));
        formatter.setLeftPadding(10);
        formatter.setDescPadding(15);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintUsage() {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        String cmdLineSyntax = "java MyApp <arg1> [option1] [option2]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("option1").withDescription("Option 1").create('o'));
        options.addOption(OptionBuilder.withLongOpt("option2").withDescription("Option 2").create('p'));
        formatter.printUsage(pw, 80, cmdLineSyntax, options);
    }

    @Test
    public void testPrintUsageWithCustomSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        String cmdLineSyntax = "java MyApp <arg1> [option1] [option2]";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("option1").withDescription("Option 1").create('o'));
        options.addOption(OptionBuilder.withLongOpt("option2").withDescription("Option 2").create('p'));
        formatter.setSyntaxPrefix("usage: myPrefix ");
        formatter.printUsage(pw, 80, cmdLineSyntax, options);
    }

    @Test
    public void testPrintWrappedText() {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed auctor, magna vel bibendum auctor, ante urna bibendum arcu, nec scelerisque lorem nisi et nibh. Proin eget nisl ac ipsum pellentesque facilisis. Aliquam erat volutpat.";
        formatter.printWrapped(pw, 50, text);
    }

    @Test
    public void testPrintWrappedTextWithCustomNextLineTabStop() {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed auctor, magna vel bibendum auctor, ante urna bibendum arcu, nec scelerisque lorem nisi et nibh. Proin eget nisl ac ipsum pellentesque facilisis. Aliquam erat volutpat.";
        formatter.printWrapped(pw, 50, 10, text);
    }

    @Test
    public void testFindWrapPos() {
        HelpFormatter formatter = new HelpFormatter();
        int pos = formatter.findWrapPos("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", 40, 0);
        assertEquals(-1, pos);
        pos = formatter.findWrapPos("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", 40, 14);
        assertEquals(14, pos);
        pos = formatter.findWrapPos("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", 40, 40);
        assertEquals(-1, pos);
    }

    @Test
    public void testCreatePadding() {
        HelpFormatter formatter = new HelpFormatter();
        String padding = formatter.createPadding(5);
        assertEquals("     ", padding);
    }

}