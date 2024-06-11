package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

public class HelpFormatterTest {

    private HelpFormatter formatter;

    @Before
    public void setup() {
        formatter = new HelpFormatter();
    }

    @Test
    public void testDefaultHelp() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpWithCustomSyntaxPrefix() throws ParseException {
        String cmdLineSyntax = "user myApp <args...";
        Options options = new Options();
        formatter.setSyntaxPrefix("user ");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpWithCustomWidth() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpWithCustomLeftPadding() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();
        formatter.setLeftPadding(10);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpWithCustomDescPadding() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();
        formatter.setDescPadding(15);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpWithCustomNewLine() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();
        String newLine = System.getProperty("line.separator") + "--------------";
        formatter.setNewLine(newLine);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpWithCustomOptPrefix() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();
        formatter.setOptPrefix("--");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpWithCustomLongOptPrefix() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();
        formatter.setLongOptPrefix("--long-");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpWithCustomArgName() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();
        formatter.setArgName("param");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpWithOptions() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();

        options.addOption("i", "integer", true, "an integer for testing");
        options.addOption("s", "string", false, "a string for testing");
        options.addOption("f", null, false, "a flag");

        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpWithOptionGroups() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption("g1", "group1", false, "option in group 1");
        group1.addOption("g2", "group2", false, "option in group 1");

        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        group2.addOption("h1", "help1", false, "option in group 2");
        group2.addOption("h2", "help2", false, "option in group 2");

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testHelpWithCustomOptionComparator() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();

        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Option opt1 = (Option) o1;
                Option opt2 = (Option) o2;

                return opt1.getKey().compareToIgnoreCase(opt2.getKey()) * -1;
            }
        };

        formatter.setOptionComparator(comparator);
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithPrintWriter() throws ParseException {
        String cmdLineSyntax = "java MyApp arg1 arg2";
        Options options = new Options();

        formatter.printHelp(new PrintWriter(System.out), 50, cmdLineSyntax, options);
    }

}