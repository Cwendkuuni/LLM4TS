package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class HelpFormatterTest {

    @org.junit.Before
    public void setUp() {
        HelpFormatter formatter = new HelpFormatter();
    }

    @org.junit.Test
    public void testDefaultSettings() {
        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options);

        // Default settings check
        assertEquals("usage: javac JavaFile.java [-d out]", getHelpText());
    }

    @org.junit.Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options);

        assertEquals("usage: javac JavaFile.java [-d out]\n", getHelpText());
    }

    @org.junit.Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(10);

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options);

        assertEquals(" usage:\n java  \t\tJavaFile.java [-d out]\n", getHelpText());
    }

    @org.junit.Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(5);

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options);

        assertEquals("usage: javac JavaFile.java [-d out]\n\n Options:\n", getHelpText());
    }

    @org.junit.Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("javac:");

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options);

        assertEquals("javac: usage: JavaFile.java [-d out]", getHelpText());
    }

    @org.junit.Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("---");

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options);

        assertEquals("javac: usage: JavaFile.java [-d out]\n---", getHelpText());
    }

    @org.junit.Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options);

        assertEquals("javac: usage: JavaFile.java [--d out]", getHelpText());
    }

    @org.junit.Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--long-");

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        Option longOption = new Option("long", "longOpt", true, "Long option description");
        options.addOption(longOption);
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options);

        assertEquals("javac: usage: JavaFile.java [--long-longOpt <Long option description>]", getHelpText());
    }

    @org.junit.Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options);

        assertEquals("javac: usage: JavaFile.java [-d out] <param>", getHelpText());
    }

    @org.junit.Test
    public void testSetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Option opt1 = (Option)o1;
                Option opt2 = (Option)o2;
                return opt2.getKey().compareToIgnoreCase(opt1.getKey());
            }
        });

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        Option opt1 = new Option("opt1", "opt1", false, "Option 1");
        Option opt2 = new Option("opt2", "opt2", false, "Option 2");
        options.addOption(opt1);
        options.addOption(opt2);
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options);

        assertEquals("usage: javac JavaFile.java [-opt2 opt2] [-opt1 opt1]\n", getHelpText());
    }

    @org.junit.Test
    public void testPrintHelpWithHeaderAndFooter() {
        HelpFormatter formatter = new HelpFormatter();
        String header = "Header";
        String footer = "Footer";

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options, header, footer);

        assertEquals("Header\n" +
                     "usage: javac JavaFile.java [-d out]\n" +
                     "Footer", getHelpText());
    }

    @org.junit.Test
    public void testPrintHelpWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        helpFormatterPrintHelp(formatter, cmdLineSyntax, options, null, null, true);

        assertEquals("usage: javac JavaFile.java [-d out]\n" +
                     "usage: javac JavaFile.java [<options>]\n", getHelpText());
    }

    @org.junit.Test
    public void testPrintHelpWithOptions() {
        HelpFormatter formatter = new HelpFormatter();

        String cmdLineSyntax = "javac JavaFile.java -d out";
        Options options = new Options();
        Option opt1 = new Option("opt1", "opt1", false, "Option 1");
        Option opt2 = new Option("opt2", "opt2", true, "Option 2");
        OptionGroup group = new OptionGroup("group");
        group.setDescription("Group options");
        group.addOption(new Option("gopt1", "gopt1", false, "Group option 1"));
        group.addOption(new Option("gopt2", "gopt2", false, "Group option 2"));
        options.addOption(opt1);
        options.addOption(opt2);
        options.addOptionGroup(group);

        helpFormatterPrintHelp(formatter, cmdLineLineSyntax, options);

        assertEquals("usage: javac JavaFile.java [-d out] [-opt1]\n" +
                     "           [--opt2]\n" +
                     "\nOptions:\n" +
                     "   -opt1, --opt1    Option 1\n" +
                     "   --opt2           Option 2\n" +
                     "\nGroup options:\n" +
                     "   --gopt1          Group option 1\n" +
                     "   --gopt2          Group option 2\n", getHelpText());
    }

    private void helpFormatterPrintHelp(HelpFormatter formatter, String cmdLineSyntax, Options options, String header, String footer, boolean autoUsage) {
        PrintWriter pw = new PrintWriter(System.out);
        formatter.printHelp(pw, formatter.getDefaultWidth(), cmdLineSyntax, header, options, autoUsage);
        formatter.printHelp(pw, formatter.getDefaultWidth(), cmdLineSyntax, header, options, formatter.getDefaultLeftPad(), formatter.getDefaultDescPad(), footer, autoUsage);
        pw.flush();
    }

    private String getHelpText() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(pw, formatter.getDefaultWidth(), null, null, null, formatter.getDefaultLeftPad(), formatter.getDefaultDescPad());
        pw.flush();
        return sw.toString();
    }

}