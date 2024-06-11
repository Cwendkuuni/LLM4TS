package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testDefaults() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
        assertNull(formatter.getOptionComparator());
    }

    @Test
    public void testSetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(123);
        assertEquals(123, formatter.getWidth());
        formatter.setLeftPadding(45);
        assertEquals(45, formatter.getLeftPadding());
        formatter.setDescPadding(67);
        assertEquals(67, formatter.getDescPadding());
        formatter.setSyntaxPrefix("syntax: ");
        assertEquals("syntax: ", formatter.getSyntaxPrefix());
        formatter.setNewLine("newline\n");
        assertEquals("newline\n", formatter.getNewLine());
        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getDefaultOptPrefix());
        formatter.setLongOptPrefix("-");
        assertEquals("-", formatter.getDefaultLongOptPrefix());
        formatter.setArgName("argName");
        assertEquals("argName", formatter.getDefaultArgName());
        formatter.setOptionComparator(new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o1.getKey().compareToIgnoreCase(o2.getKey());
            }
        });
        assertSame(new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o1.getKey().compareToIgnoreCase(o2.getKey());
            }
        }, formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelp() throws IOException {
        String cmdLineSyntax = "java MyClass [options]";
        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp(System.out, 100, cmdLineSyntax, null, null, null, null, null, false);

        // Verify default usage message
        List<String> lines = Arrays.asList(
                "usage: java MyClass [options]",
                "",
                "options:"
        );
        List<String> actualLines = readLinesFromOutput();
        assertEquals(lines, actualLines);

        // Test with options
        Options options = new Options();
        Option option1 = new Option("a", "arg1", false, "Description for option 1");
        Option option2 = new Option("b", "arg2", true, "Description for option 2");
        OptionGroup group1 = new OptionGroup("group1", "Options in group 1");
        group1.addOption(option1);
        group1.addOption(option2);
        options.addOption(option1);
        options.addOption(option2);
        options.addOptionGroup(group1);

        formatter.printHelp(System.out, 100, cmdLineSyntax, options, null, null, null, null, false);

        lines = Arrays.asList(
                "usage: java MyClass [options]",
                "",
                "options:",
                "  -a <arg1>                   Description for option 1",
                "  -b                          Description for option 2",
                "  --arg1 <arg1>              Description for option 1",
                "  --arg2                      Description for option 2",
                "",
                "Options in group 1:",
                "  -a <arg1>                   Description for option 1",
                "  -b                          Description for option 2"
        );
        actualLines = readLinesFromOutput();
        assertEquals(lines, actualLines);

        // Test with custom padding
        formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.setDescPadding(10);
        formatter.printHelp(System.out, 100, cmdLineSyntax, options, null, null, null, null, false);

        lines = Arrays.asList(
                "usage: java MyClass [options]",
                "",
                "options:",
                "  --   Options for advanced users:",
                "  ->  Indicates a heading",
                "    -a <arg1>                   Description for option 1",
                "    -b                          Description for option 2",
                "    --arg1 <arg1>              Description for option 1",
                "    --arg2                      Description for option 2",
                "",
                "Options in group 1:",
                "      --   Options for advanced users:",
                "      ->  Indicates a heading",
                "    -a <arg1>                   Description for option 1",
                "    -b                          Description for option 2"
        );
        actualLines = readLinesFromOutput();
        assertEquals(lines, actualLines);

        // Test with header and footer
        String header = "My Application";
        String footer = "For more information, visit www.example.com";
        formatter.printHelp(System.out, 100, cmdLineSyntax, options, header, footer, null, null, false);

        lines = Arrays.asList(
                "My Application",
                "",
                "usage: java MyClass [options]",
                "",
                "options:",
                "  --   Options for advanced users:",
                "  ->  Indicates a heading",
                "    -a <arg1>                   Description for option 1",
                "    -b                          Description for option 2",
                "    --arg1 <arg1>              Description for option 1",
                "    --arg2                      Description for option 2",
                "",
                "Options in group 1:",
                "      --   Options for advanced users:",
                "      ->  Indicates a heading",
                "    -a <arg1>                   Description for option 1",
                "    -b                          Description for option 2"
        );
        actualLines = readLinesFromOutput();
        assertEquals(lines, actualLines);

        // Test with autoUsage enabled
        formatter.printHelp(System.out, 100, cmdLineSyntax, options, null, null, null, null, true);

        lines = Arrays.asList(
                "usage: java MyClass [options]",
                "",
                "options:",
                "  -a <arg1>                   Description for option 1",
                "  -b                          Description for option 2",
                "  --arg1 <arg1>              Description for option 1",
                "  --arg2                      Description for option 2"
        );
        actualLines = readLinesFromOutput();
        assertEquals(lines, actualLines);

        // Test with custom comparator
        Comparator<Option> comparator = new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o2.getKey().compareToIgnoreCase(o1.getKey());
            }
        };
        formatter = new HelpFormatter();
        formatter.setOptionComparator(comparator);
        formatter.printHelp(System.out, 100, cmdLineSyntax, options, null, null, null, null, false);

        lines = Arrays.asList(
                "usage: java MyClass [options]",
                "",
                "options:",
                "  --arg2                      Description for option 2",
                "  --arg1 <arg1>              Description for option 1",
                "  -b                          Description for option 2",
                "  -a <arg1>                   Description for option 1"
        );
        actualLines = readLinesFromOutput();
        assertEquals(lines, actualLines);
    }

    @Test
    public void testPrintUsage() throws IOException {
        String app = "MyClass";
        Options options = new Options();
        Option option1 = new Option("a", "arg1", false, "Description for option 1");
        Option option2 = new Option("b", "arg2", true, "Description for option 2");
        OptionGroup group1 = new OptionGroup("group1", "Options in group 1");
        group1.addOption(option1);
        group1.addOption(option2);
        options.addOption(option1);
        options.addOption(option2);
        options.addOptionGroup(group1);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(System.out, 100, app, options);

        List<String> lines = Arrays.asList(
                "usage: java MyClass [options]",
                "",
                "options:",
                "  -a <arg1>                   Description for option 1",
                "  -b                          Description for option 2",
                "  --arg1 <arg1>              Description for option 1",
                "  --arg2                      Description for option 2"
        );
        List<String> actualLines = readLinesFromOutput();
        assertEquals(lines, actualLines);
    }

    private List<String> readLinesFromOutput() {
        try (StringBuilder sb = new StringBuilder();
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException("Failed to capture output", e);
        }
    }
}