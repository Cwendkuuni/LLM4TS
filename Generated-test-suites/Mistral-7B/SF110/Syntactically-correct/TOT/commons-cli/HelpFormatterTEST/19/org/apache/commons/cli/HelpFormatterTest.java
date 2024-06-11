package org.apache.commons.cli.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpFormatterTest {

    private HelpFormatter formatter;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        formatter = new HelpFormatter();
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testPrintHelpWithDefaultSettings() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        options.addOption("-a", "option A", true, "Option A description");
        options.addOption("--option-b", "Option B", false, "Option B description");

        formatter.printHelp(outputStream, 80, cmdLineSyntax, options, null, false);

        String expectedHelp = "usage: java MyClass [options]\n"
                + "\noptions:\n"
                + "  -a, --option-a          Option A description (value required)\n"
                + "  --option-b              Option B description\n";

        Assertions.assertEquals(expectedHelp, outputStream.toString());
    }

    @Test
    public void testPrintHelpWithCustomSettings() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        options.addOption("-a", "option A", true, "Option A description");
        options.addOption("--option-b", "Option B", false, "Option B description");

        formatter.setWidth(100);
        formatter.setSyntaxPrefix("Usage: ");
        formatter.setArgName("ARG");
        formatter.setLeftPadding(10);
        formatter.setDescPadding(15);

        formatter.printHelp(outputStream, 100, cmdLineSyntax, options, null, false);

        String expectedHelp = "Usage: java MyClass [options]\n"
                + "\nOptions:\n"
                + "  -a, --option-a          Option A description (value required)\n"
                + "                          ARG: Option A description\n"
                + "  --option-b              Option B description\n"
                + "                          ARG: Option B description\n";

        Assertions.assertEquals(expectedHelp, outputStream.toString());
    }

    @Test
    public void testPrintHelpWithCustomHeaderAndFooter() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        options.addOption("-a", "option A", true, "Option A description");
        options.addOption("--option-b", "Option B", false, "Option B description");

        String header = "My Class Help";
        String footer = "Thank you for using My Class";

        formatter.printHelp(outputStream, 80, cmdLineSyntax, options, header, footer, false);

        String expectedHelp = "My Class Help\n"
                + "\nusage: java MyClass [options]\n"
                + "\noptions:\n"
                + "  -a, --option-a          Option A description (value required)\n"
                + "  --option-b              Option B description\n"
                + "\nThank you for using My Class\n";

        Assertions.assertEquals(expectedHelp, outputStream.toString());
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        options.addOption("-a", "option A", true, "Option A description");
        options.addOption("--option-b", "Option B", false, "Option B description");

        formatter.printHelp(outputStream, 80, cmdLineSyntax, options, null, true);

        String expectedHelp = "usage: java MyClass [options]\n"
                + "\noptions:\n"
                + "  -a, --option-a          Option A description (value required)\n"
                + "  --option-b              Option B description\n";

        Assertions.assertEquals(expectedHelp, outputStream.toString());
    }

    @Test
    public void testPrintHelpWithEmptyCmdLineSyntax() {
        String cmdLineSyntax = "";
        Options options = new Options();

        formatter.setWidth(80);
        formatter.printHelp(outputStream, 80, cmdLineSyntax, options, null, false);

        String expectedHelp = "usage: <unknown command>\n\n";

        Assertions.assertEquals(expectedHelp, outputStream.toString());
    }

    @Test
    public void testPrintHelpWithNullOptions() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = null;

        formatter.printHelp(outputStream, 80, cmdLineSyntax, options, null, false);

        String expectedHelp = "usage: java MyClass [options]\noptions:\n";

        Assertions.assertEquals(expectedHelp, outputStream.toString());
    }

    @Test
    public void testPrintHelpWithEmptyOptions() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();

        formatter.printHelp(outputStream, 80, cmdLineSyntax, options, null, false);

        String expectedHelp = "usage: java MyClass [options]\noptions:\n";

        Assertions.assertEquals(expectedHelp, outputStream.toString());
    }

    @Test
    public void testPrintHelpWithEmptyCmdLineSyntaxAndEmptyOptions() {
        String cmdLineSyntax = "";
        Options options = new Options();

        formatter.printHelp(outputStream, 80, cmdLineSyntax, options, null, false);

        String expectedHelp = "usage: <unknown command>\noptions:\n";

        Assertions.assertEquals(expectedHelp, outputStream.toString());
    }

    @Test
    public void testPrintHelpWithCustomOptionComparator() {
        String cmdLineSyntax = "java MyClass [options]";
        Options options = new Options();
        options.addOption("--z", "Option Z", false, "Option Z description");
        options.addOption("--x", "Option X", false, "Option X description");

        formatter.setOptionComparator(Collections.reverseOrder());

        formatter.printHelp(outputStream, 80, cmdLineSyntax, options, null, false);

        String expectedHelp = "options:\n"
                + "  --z, --option-z          Option Z description\n"
                + "  --x, --option-x          Option X description\n"
                + "usage: java MyClass [options]\n";

        Assertions.assertEquals(expectedHelp, outputStream.toString());
    }

    @Test
    public void testPrintHelpWithMissingCmdLineSyntax() {
        Options options = new Options();
        options.addOption("-a", "option A", true, "Option A description");

        formatter.printHelp(outputStream, 80, null, options, null, false);

        String expectedHelp = "usage: <unknown command>\noptions:\n"
                + "  -a, --option-a          Option A description (value required)\n";

        Assertions.assertEquals(expectedHelp, outputStream.toString());
    }

    @Test
    public void testPrintHelpWithMissingOptions() {
        String cmdLineSyntax = "java MyClass [options]";

        formatter.printHelp(outputStream, 80, cmdLineSyntax, null, null, false);

        String expectedHelp = "usage: java MyClass [options]\noptions:\n";

        Assertions.assertEquals(expectedHelp, outputStream.toString());
    }
}