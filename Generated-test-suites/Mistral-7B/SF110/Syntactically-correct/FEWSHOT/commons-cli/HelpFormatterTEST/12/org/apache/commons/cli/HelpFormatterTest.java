package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testPrintHelpWithDefaults() {
        final Options options = new Options();
        final String cmdLineSyntax = "java MyApp [options]";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        // Printed text should not be empty
        assertTrue(System.out.toString().length() > 0);
    }

    @Test
    public void testPrintHelpWithCustomizedWidth() {
        final Options options = new Options();
        final String cmdLineSyntax = "java MyApp [options]";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(120);
        formatter.printHelp(cmdLineSyntax, options);

        // Printed text should have the customized width
        final String expectedHelpText = "usage: java MyApp [options]\n" +
                "\n" +
                "options:\n" +
                "  --option1  <arg>\n" +
                "      Description of option1.\n" +
                "  --option2\n" +
                "      Description of option2.\n";
        final String actualHelpText = System.out.toString();
        assertEquals(expectedHelpText, actualHelpText);
    }

    @Test
    public void testPrintHelpWithCustomizedLeftPadding() {
        final Options options = new Options();
        final String cmdLineSyntax = "java MyApp [options]";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.printHelp(cmdLineSyntax, options);

        // Printed text should have the customized left padding
        final String expectedHelpText = "   usage: java MyApp [options]\n" +
                "   \n" +
                "   options:\n" +
                "     --option1 <arg>\n" +
                "       Description of option1.\n" +
                "     --option2\n" +
                "       Description of option2.\n";
        final String actualHelpText = System.out.toString();
        assertEquals(expectedHelpText, actualHelpText);
    }

    @Test
    public void testPrintHelpWithCustomizedDescPadding() {
        final Options options = new Options();
        final String cmdLineSyntax = "java MyApp [options]";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(10);
        formatter.printHelp(cmdLineSyntax, options);

        // Printed text should have the customized desc padding
        final String expectedHelpText = "usage: java MyApp [options]\n" +
                "\n" +
                "options:\n" +
                "  --option1 <arg>\n" +
                "      Description of option1.\n" +
                "            ------------------------------------------------------------------------------------------------------------------------------------------------------------------\n" +
                "  --option2\n" +
                "      Description of option2.\n";
        final String actualHelpText = System.out.toString();
        assertEquals(expectedHelpText, actualHelpText);
    }

    @Test
    public void testPrintHelpWithCustomizedSyntaxPrefix() {
        final Options options = new Options();
        final String cmdLineSyntax = "java MyApp [options]";
        final String customSyntaxPrefix = "Usage: ";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix(customSyntaxPrefix);
        formatter.printHelp(cmdLineSyntax, options);

        // Printed text should have the customized syntax prefix
        final String expectedHelpText = "Usage: java MyApp [options]\n" +
                "\n" +
                "options:\n" +
                "  --option1 <arg>\n" +
                "      Description of option1.\n" +
                "  --option2\n" +
                "      Description of option2.\n";
        final String actualHelpText = System.out.toString();
        assertEquals(expectedHelpText, actualHelpText);
    }

    @Test
    public void testPrintHelpWithCustomizedOptPrefix() {
        final Options options = new Options();
        final String cmdLineSyntax = "java MyApp [options]";
        final String customOptPrefix = "-x";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix(customOptPrefix);
        formatter.printHelp(cmdLineSyntax, options);

        // Printed text should have the customized opt prefix
        final String expectedHelpText = "Usage: java MyApp [options]\n" +
                "\n" +
                "options:\n" +
                "  -x:option1 <arg>\n" +
                "      Description of option1.\n" +
                "  -x:option2\n" +
                "      Description of option2.\n";
        final String actualHelpText = System.out.toString();
        assertEquals(expectedHelpText, actualHelpText);
    }

    @Test
    public void testPrintHelpWithCustomizedLongOptPrefix() {
        final Options options = new Options();
        final String cmdLineSyntax = "java MyApp [options]";
        final String customLongOptPrefix = "--x";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix(customLongOptPrefix);
        formatter.printHelp(cmdLineSyntax, options);

        // Printed text should have the customized long opt prefix
        final String expectedHelpText = "Usage: java MyApp [options]\n" +
                "\n" +
                "options:\n" +
                "  --x:option1 <arg>\n" +
                "      Description of option1.\n" +
                "  --x:option2\n" +
                "      Description of option2.\n";
        final String actualHelpText = System.out.toString();
        assertEquals(expectedHelpText, actualHelpText);
    }

    @Test
    public void testPrintHelpWithCustomizedArgName() {
        final Options options = new Options();
        final String cmdLineSyntax = "java MyApp [options]";
        final String customArgName = "param";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName(customArgName);
        formatter.printHelp(cmdLineSyntax, options);

        // Printed text should have the customized arg name
        final String expectedHelpText = "Usage: java MyApp [options]\n" +
                "\n" +
                "options:\n" +
                "  --option1 <param>\n" +
                "      Description of option1.\n" +
                "  --option2\n" +
                "      Description of option2.\n";
        final String actualHelpText = System.out.toString();
        assertEquals(expectedHelpText, actualHelpText);
    }

    @Test
    public void testPrintHelpWithOptions() {
        final Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", false, "o2");
        options.addOptionGroup(new OptionGroup("group1")
                .required(false)
                .description("This is group 1")
                .addOption("groupOption1", false, "g1")
                .addOption("groupOption2", false, "g2"));
        options.addOptionGroup(new OptionGroup("group2")
                .required(true)
                .description("This is group 2")
                .addOption("groupOption3", false, "g3")
                .addOption("groupOption4", false, "g4"));

        final String cmdLineSyntax = "java MyApp [options]";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        // Printed text should contain all options
        final String expectedHelpText = "usage: java MyApp [options]\n" +
                "\n" +
                "options:\n" +
                "  -o1\n" +
                "      Option 1 (default is true).\n" +
                "  --o2\n" +
                "      Option 2 (default is false).\n" +
                "\n" +
                "  group 1:\n" +
                "    --g1\n" +
                "      Description of groupOption1 (default is false).\n" +
                "    --g2\n" +
                "      Description of groupOption2 (default is false).\n" +
                "\n" +
                "  group 2:\n" +
                "    --g3\n" +
                "      Description of groupOption3 (default is false).\n" +
                "    --g4\n" +
                "      Description of groupOption4 (default is false).\n" +
                "\n" +
                "    (To specify several values for an option, use multiple instances of the option.\n" +
                "    e.g. --option='value1 value2').\n";
        final String actualHelpText = System.out.toString();
        assertEquals(expectedHelpText, actualHelpText);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        final Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", false, "o2");

        final String cmdLineSyntax = "java MyApp [options]";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, true);

        // Printed text should contain the usage line
        final String expectedHelpText = "usage: java MyApp [options]\n" +
                "\n" +
                "options:\n" +
                "  -o1\n" +
                "      Option 1 (default is true).\n" +
                "  --o2\n" +
                "      Option 2 (default is false).\n";
        final String actualHelpText = System.out.toString();
        assertEquals(expectedHelpText, actualHelpText);
    }

    @Test
    public void testPrintHelpWithCustomizedHeaderAndFooter() {
        final Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", false, "o2");

        final String cmdLineSyntax = "java MyApp [options]";
        final String customHeader = "My App - Help";
        final String customFooter = "Thank you for using My App.";
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("  ");
        formatter.setHeader(customHeader);
        formatter.setFooter(customFooter);
        formatter.printHelp(cmdLineSyntax, options);

        // Printed text should contain the customized header and footer
        final String expectedHelpText = " My App - Help\n" +
                "\n" +
                "  usage: java MyApp [options]\n" +
                "\n" +
                "  options:\n" +
                "    -o1\n" +
                "      Option 1 (default is true).\n" +
                "    --o2\n" +
                "      Option 2 (default is false).\n" +
                "\n" +
                "Thank you for using My App.";
        final String actualHelpText = System.out.toString();
        assertEquals(expectedHelpText, actualHelpText);
    }
}