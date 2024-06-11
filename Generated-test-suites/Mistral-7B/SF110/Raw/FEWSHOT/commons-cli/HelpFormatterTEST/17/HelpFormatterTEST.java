package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B with arg");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C in group 1")
                .addOption("d", "optionD", false, "Option D in group 1")
                .addOption("e", "optionE", false, "Option E in group 1", 1));
        options.addOptionGroup(new OptionGroup("group2", "Group 2")
                .addOption("f", "optionF", false, "Option F in group 2")
                .addOption("g", "optionG", false, "Option G in group 2", 2));

        HelpFormatter formatter = new HelpFormatter();
        String expectedHelp = new StringBuffer()
                .append("usage: [class|java] [options]...")
                .append(System.getProperty("line.separator"))
                .append("Options:")
                .append(System.getProperty("line.separator"))
                .append("  -a, --optionA        Option A")
                .append(System.getProperty("line.separator"))
                .append("  -b, --optionB         Option B with arg")
                .append(System.getProperty("line.separator"))
                .append("  --group1             group 1")
                .append(System.getProperty("line.separator"))
                .append("    --c, --optionC       Option C in group 1")
                .append(System.getProperty("line.separator"))
                .append("    --d, --optionD       Option D in group 1")
                .append(System.getProperty("line.separator"))
                .append("    --e, --optionE<int>  Option E in group 1 (value is int)")
                .append(System.getProperty("line.separator"))
                .append("  --group2             group 2")
                .append(System.getProperty("line.separator"))
                .append("    --f, --optionF       Option F in group 2")
                .append(System.getProperty("line.separator"))
                .append("    --g, --optionG<int>  Option G in group 2 (value is int)")
                .toString();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(outContent);
        formatter.printHelp(pw, 100, "Test", options, null);
        pw.flush();

        String actualHelp = new String(outContent.toByteArray());
        assertEquals(expectedHelp, actualHelp);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B with arg");

        HelpFormatter formatter = new HelpFormatter();
        String header = "Help for MyApp";
        String footer = "For more information, visit www.myapp.com";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(outContent);
        formatter.printHelp(pw, 100, "Test", options, header, footer);
        pw.flush();

        String actualHelp = new String(outContent.toByteArray());
        String expectedHelp = new StringBuffer()
                .append(header)
                .append(System.getProperty("line.separator"))
                .append("usage: [class|java] [options]...")
                .append(System.getProperty("line.separator"))
                .append("Options:")
                .append(System.getProperty("line.separator"))
                .append("  -a, --optionA        Option A")
                .append(System.getProperty("line.separator"))
                .append("  -b, --optionB         Option B with arg")
                .append(System.getProperty("line.separator"))
                .append(footer)
                .toString();

        assertEquals(expectedHelp, actualHelp);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B with arg");

        HelpFormatter formatter = new HelpFormatter();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(outContent);
        formatter.printHelp(pw, 100, "Test", options);
        pw.flush();

        String actualHelp = new String(outContent.toByteArray());
        String expectedHelp = new StringBuffer()
                .append("usage: [class|java] [options]...")
                .append(System.getProperty("line.separator"))
                .append("Options:")
                .append(System.getProperty("line.separator"))
                .append("  -a, --optionA        Option A")
                .append(System.getProperty("line.separator"))
                .append("  -b, --optionB         Option B with arg")
                .toString();

        assertEquals(expectedHelp, actualHelp);
    }
}