package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for HelpFormatter.
 */
public class HelpFormatterTest {

    private HelpFormatter formatter;
    private Options options;

    @Before
    public void setup() {
        formatter = new HelpFormatter();
        options = new Options();
    }

    @Test
    public void testDefaultHelp() throws Exception {
        formatter.printHelp("test", options);
    }

    @Test
    public void testCustomHelpWidth() throws Exception {
        formatter.setWidth(10);
        formatter.printHelp("test", options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCmdLineSyntax() throws Exception {
        formatter.printHelp(null, options);
    }

    @Test
    public void testPrintUsageWithOptions() throws Exception {
        options.addOption("a", false, false, "Option A");
        options.addOption("b", true, "B value", "Option B");
        formatter.printUsage(System.out, 80, "test", options);
    }

    @Test
    public void testPrintUsageWithHeaderAndFooter() throws Exception {
        options.addOption("a", false, false, "Option A");
        formatter.setSyntaxPrefix("Usage: ");
        formatter.setHeader("Test Program Options");
        formatter.setFooter("Copyright (C) 2023 Apache Software Foundation");
        formatter.printHelp(System.out, 80, "test", options, "My Program");
    }

    @Test
    public void testPrintUsageWithAutoUsage() throws Exception {
        options.addOption("a", false, false, "Option A");
        formatter.setSyntaxPrefix("Usage: ");
        formatter.printHelp(System.out, 80, "test", options, null, true);
    }

    @Test
    public void testPrintHelpWithOptionsGroup() throws Exception {
        OptionGroup group = new OptionGroup("MyGroup", "Group Options");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption("g", false, false, "Group Option 1");
        group.addOption("h", false, false, "Group Option 2");
        formatter.printHelp(System.out, 80, "test", options);
    }

    @Test
    public void testPrintHelpWithPrintWriter() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintWriter pw = new PrintWriter(baos);
        options.addOption("a", false, false, "Option A");
        formatter.printHelp(pw, 80, "test", options);
        pw.flush();
        assertEquals("(Expected help output here)", baos.toString(), formatter.getHelpString("test", options));
    }

    @Test
    public void testPrintHelpWithCustomOptionComparator() throws Exception {
        Options options = new Options();
        options.addOption("b", false, false, "Option B");
        options.addOption("a", false, false, "Option A");
        formatter.setOptionComparator(new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o2.getKey().compareToIgnoreCase(o1.getKey());
            }
        });
        formatter.printHelp(System.out, 80, "test", options);
    }

    @Test
    public void testPrintHelpWithCustomWidth() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(10);
        String test = "test";
        Options options = new Options();
        formatter.printHelp(System.out, 10, test, options);
    }

    @Test
    public void testPrintHelpWithCustomOptPrefix() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        String test = "test";
        Options options = new Options();
        formatter.printHelp(System.out, 80, test, options);
    }

    @Test
    public void testPrintHelpWithCustomArgName() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("ARG");
        String test = "test";
        Options options = new Options();
        formatter.printHelp(System.out, 80, test, options);
    }

    @Test
    public void testPrintHelpWithCustomDescPadding() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(5);
        String test = "test";
        Options options = new Options();
        formatter.printHelp(System.out, 80, test, options);
    }

    @Test
    public void testPrintHelpWithCustomLeftPadding() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        String test = "test";
        Options options = new Options();
        formatter.printHelp(System.out, 80, test, options);
    }

    @Test
    public void testPrintHelpWithCustomSyntaxPrefix() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("Help: ");
        String test = "test";
        Options options = new Options();
        formatter.printHelp(System.out, 80, test, options);
    }

    @Test
    public void testPrintHelpWithCustomNewLine() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\n\n");
        String test = "test";
        Options options = new Options();
        formatter.printHelp(System.out, 80, test, options);
    }

    @Test
    public void testPrintHelpWithCustomOptionComparator() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        Comparator<Option> customComparator = new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o1.getKey().compareToIgnoreCase(o2.getKey());
            }
        };
        formatter.setOptionComparator(customComparator);
        String test = "test";
        Options options = new Options();
        options.addOption("z", false, false, "Option Z");
        options.addOption("y", false, false, "Option Y");
        options.addOption("x", false, false, "Option X");
        formatter.printHelp(System.out, 80, test, options);
    }

    @Test
    public void testPrintHelpWithCustomOptionComparatorAndNull() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        Comparator<Option> customComparator = null;
        formatter.setOptionComparator(customComparator);
        String test = "test";
        Options options = new Options();
        formatter.printHelp(System.out, 80, test, options);
    }
}