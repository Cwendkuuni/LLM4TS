package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testDefaultSettings() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertNull(formatter.getDefaultNewLine());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
        assertNull(formatter.getOptionComparator());
    }

    @Test
    public void testSettersAndGetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(7);
        formatter.setSyntaxPrefix("help: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("param");
        formatter.setOptionComparator(new OptionComparator());

        assertEquals(50, formatter.getWidth());
        assertEquals(5, formatter.getLeftPadding());
        assertEquals(7, formatter.getDescPadding());
        assertEquals("help: ", formatter.getSyntaxPrefix());
        assertEquals("\n", formatter.getDefaultNewLine());
        assertEquals("--", formatter.getDefaultOptPrefix());
        assertEquals("-", formatter.getDefaultLongOptPrefix());
        assertEquals("param", formatter.getDefaultArgName());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelpDefaults() {
        final String cmdLineSyntax = "java MyClass [options]";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }

    @Test
    public void testPrintHelpCustom() {
        final String cmdLineSyntax = "java MyClass [options]";
        final String header = "My Class Help";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, header, options);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        final String cmdLineSyntax = "java MyClass [options]";
        final String header = "My Class Help";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, header, options, true);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }

    @Test
    public void testPrintHelpWidth() {
        final String cmdLineSyntax = "java MyClass [options]";
        final String header = "My Class Help";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, header, options);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }

    @Test
    public void testPrintHelpLeftPadding() {
        final String cmdLineSyntax = "java MyClass [options]";
        final String header = "My Class Help";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.printHelp(cmdLineSyntax, header, options);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }

    @Test
    public void testPrintHelpDescPadding() {
        final String cmdLineSyntax = "java MyClass [options]";
        final String header = "My Class Help";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(7);
        formatter.printHelp(cmdLineSyntax, header, options);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }

    @Test
    public void testPrintHelpSyntaxPrefix() {
        final String cmdLineSyntax = "java MyClass [options]";
        final String header = "My Class Help";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("help: ");
        formatter.printHelp(cmdLineSyntax, header, options);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }

    @Test
    public void testPrintHelpNewLine() {
        final String cmdLineSyntax = "java MyClass [options]";
        final String header = "My Class Help";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\r\n");
        formatter.printHelp(cmdLineSyntax, header, options);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }

    @Test
    public void testPrintHelpOptPrefix() {
        final String cmdLineSyntax = "java MyClass [options]";
        final String header = "My Class Help";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        formatter.printHelp(cmdLineSyntax, header, options);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }

    @Test
    public void testPrintHelpLongOptPrefix() {
        final String cmdLineSyntax = "java MyClass [options]";
        final String header = "My Class Help";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("-");
        formatter.printHelp(cmdLineSyntax, header, options);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }

    @Test
    public void testPrintHelpArgName() {
        final String cmdLineSyntax = "java MyClass [options] <file> [times]";
        final String header = "My Class Help";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path"))
                .requiredOption(new Option("f", "file", true, "file name"))
                .requiredOption(new Option("t", "times", true, "number of times"))
                .option(new Option("h", "help", false, "display this help message"))
                .option(new Option("q", "quiet", false, "do not output any message")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("files");
        formatter.printHelp(cmdLineSyntax, header, options);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }

    @Test
    public void testPrintHelpOptionComparator() {
        final String cmdLineSyntax = "java MyClass [options]";
        final String header = "My Class Help";
        final Options options = new Options();
        options.addOption(new Option("a", "append", true, "append to a file"));
        options.addOption(new Option("b", "boolean", false, "a boolean option"));
        options.addOptionGroup(new OptionGroup("g")
                .requiredOption(new Option("c", "count", true, "number of times to repeat an action"))
                .requiredOption(new Option("d", "dir", true, "directory path")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(new OptionComparator() {
            @Override
            public int compare(Option o1, Option o2) {
                return o2.getKey().compareToIgnoreCase(o1.getKey());
            }
        });
        formatter.printHelp(cmdLineSyntax, header, options);

        // Print statements are not testable since they are printed to the console.
        // You can check the console output or print it to a file and check its content
        // instead.
    }
}