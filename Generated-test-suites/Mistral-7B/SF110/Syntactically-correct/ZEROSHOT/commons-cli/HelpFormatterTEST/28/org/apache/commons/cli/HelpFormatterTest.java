package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterInstance() {
        HelpFormatter formatter = new HelpFormatter();
        assertNotNull(formatter);
        assertEquals(74, formatter.getDefaultWidth());
        // Test other getters here
    }

    @Test
    public void testSettingWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getDefaultWidth());
    }

    @Test
    public void testPrintHelp() throws IOException {
        String cmdLineSyntax = "java MyApp -a arg1 -b arg2";
        Options options = new Options();
        options.addRequiredOption("a", "arg1", true, "Argument 1");
        options.addRequiredOption("b", "arg2", true, "Argument 2");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);
        // Add assertions here to check the output
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() throws IOException {
        String cmdLineSyntax = "java MyApp -a arg1 -b arg2";
        Options options = new Options();
        options.addRequiredOption("a", "arg1", true, "Argument 1");
        options.addRequiredOption("b", "arg2", true, "Argument 2");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.setSyntaxPrefix("Usage: ");
        formatter.setLeftPadding(20);

        String header = "MyApp - Help";
        String footer = "For more information, visit www.example.com";

        formatter.printHelp(cmdLineSyntax, header, options, footer);
        // Add assertions here to check the output
    }

    @Test
    public void testPrintHelpWithAutoUsage() throws IOException {
        String cmdLineSyntax = "java MyApp -a arg1 -b arg2";
        Options options = new Options();
        options.addRequiredOption("a", "arg1", true, "Argument 1");
        options.addRequiredOption("b", "arg2", true, "Argument 2");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.setSyntaxPrefix("Usage: ");
        formatter.setLeftPadding(20);

        formatter.printHelp(cmdLineSyntax, options, true);
        // Add assertions here to check the output
    }

    @Test
    public void testPrintUsage() throws IOException {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        options.addOption("a", true, "Argument 1");
        options.addOption("b", true, "Argument 2");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.setSyntaxPrefix("Usage: ");
        formatter.setLeftPadding(20);

        PrintWriter pw = new PrintWriter(new ByteArrayOutputStream());
        formatter.printHelp(pw, cmdLineSyntax, options);

        String expectedUsage = "Usage: java MyApp [options]\n" +
                "\n" +
                "Options:\n" +
                "   -a, --argument1\n" +
                "       Argument 1 (boolean)\n" +
                "   -b, --argument2\n" +
                "       Argument 2 (boolean)\n";

        String actualUsage = pw.getBuffer().toString();
        assertEquals(expectedUsage, actualUsage);
    }

    @Test
    public void testPrintWrapped() throws IOException {
        HelpFormatter formatter = new HelpFormatter();
        String text = "This is a long text to test printWrapped method. It should wrap the text with the specified width.";
        int width = 30;

        PrintWriter pw = new PrintWriter(new ByteArrayOutputStream());
        formatter.printWrapped(pw, width, text);

        String expectedWrappedText = "This is a long text to test printWrapped method. It should wrap the text with the specified width.\n";
        String actualWrappedText = pw.getBuffer().toString();
        assertEquals(expectedWrappedText, actualWrappedText);
    }
}