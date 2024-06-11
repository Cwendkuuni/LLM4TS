package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
        assertEquals("usage: ", formatter.getDefaultSyntaxPrefix());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
        assertNull(formatter.getOptionComparator());
    }

    @Test
    public void testSettingWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testSettingLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(3);
        assertEquals(3, formatter.getLeftPadding());
    }

    @Test
    public void testSettingDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(5);
        assertEquals(5, formatter.getDescPadding());
    }

    @Test
    public void testSettingSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("help: ");
        assertEquals("help: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSettingNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\n---\n");
        assertEquals("\n---\n", formatter.getNewLine());
    }

    @Test
    public void testSettingOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("-x");
        assertEquals("-x", formatter.getOptPrefix());
    }

    @Test
    public void testSettingLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--my");
        assertEquals("--my", formatter.getLongOptPrefix());
    }

    @Test
    public void testSettingArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");
        assertEquals("param", formatter.getArgName());
    }

    @Test
    public void testSettingOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Option option1 = (Option) o1;
                Option option2 = (Option) o2;
                return option1.getKey().compareToIgnoreCase(option2.getKey());
            }
        };
        formatter.setOptionComparator(comparator);
        assertSame(comparator, formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelp_simple() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command", Arrays.asList(new Option("a", "arg1", true, "description1"), new Option("b", "arg2")));
        String expected = "usage: command [-a ARG1] [-b ARG2]\n"
                + "\narguments:\n"
                + "  ARG1:description1 (required)\n"
                + "  ARG2:\n\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_longOptions() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command", Arrays.asList(new Option("a", "arg1", true, "description1"),
                new Option("long-arg1", "long-description1", true),
                new Option("b", "arg2", false, "description2"),
                new Option("long-arg2", "long-description2", false)));
        String expected = "usage: command [-a ARG1] [--long-arg1 LONG-DESCRIPTION1]\n"
                + "           [--long-arg2 LONG-DESCRIPTION2]\n"
                + "           [-b ARG2]\n"
                + "\narguments:\n"
                + "  ARG1:description1 (required)\n"
                + "  ARG2:\n"
                + "\noptional arguments:\n"
                + "  --long-arg1 LONG-DESCRIPTION1\n"
                + "  --long-arg2 LONG-DESCRIPTION2\n\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_groupedOptions() {
        HelpFormatter formatter = new HelpFormatter();
        OptionGroup group = new OptionGroup("group1", "group1");
        group.setRequired(false);
        group.addOption(new Option("a", "arg1", true, "description1", group));
        group.addOption(new Option("b", "arg2", true, "description2", group));
        formatter.printHelp("command", Arrays.asList(group));
        String expected = "usage: command [--group1 ARG1 ARG2]\n"
                + "\ngroup1:\n"
                + "  ARG1:description1 (required)\n"
                + "  ARG2:description2 (required)\n\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_multiLineOptions() {
        HelpFormatter formatter = new HelpFormatter();
        Option option = new Option("a", "arg1", true, "description1\ndescription2");
        formatter.printHelp("command", Arrays.asList(option));
        String expected = "usage: command [-a ARG1]\n"
                + "\narguments:\n"
                + "  ARG1:description1\n"
                + "          description2\n\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_noDescription() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command", Arrays.asList(new Option("a", "arg1", true)));
        String expected = "usage: command [-a ARG1]\n\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_noArguments() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command", Arrays.asList(new Option("a", "arg1")));
        String expected = "usage: command [-a ARG1]\n\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_noOptions() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command");
        String expected = "usage: command\n\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_noDescriptionAndArguments() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command", Arrays.asList(new Option("a", "arg1")));
        String expected = "usage: command [-a ARG1]\n\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_noArgumentsAndDescription() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command", Arrays.asList(new Option("a", "arg1", true, "")));
        String expected = "usage: command [-a ARG1]\n\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_noOptionsAndDescription() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command", Collections.emptyList());
        String expected = "usage: command\n\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_withHeader() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command", Arrays.asList(new Option("a", "arg1", true, "description1")), "My command header");
        String expected = "My command header\n"
                + "\nusage: command [-a ARG1]\n"
                + "\narguments:\n"
                + "  ARG1:description1\n\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_withFooter() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command", Arrays.asList(new Option("a", "arg1", true, "description1")), null, "My command footer");
        String expected = "usage: command [-a ARG1]\n\n"
                + "My command footer\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_withHeaderAndFooter() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command", Arrays.asList(new Option("a", "arg1", true, "description1")), "My command header", "My command footer");
        String expected = "My command header\n"
                + "\nusage: command [-a ARG1]\n"
                + "\narguments:\n"
                + "  ARG1:description1\n\n"
                + "My command footer\n";
        assertEquals(expected, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_withAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("command", Arrays.asList(new Option("a", "arg1", true, "description1")));
        String expectedUsage = "usage: command [-a ARG1]\n";
        String expectedHelp = "arguments:\n"
                + "  ARG1:description1\n\n";
        assertEquals(expectedUsage, System.out.getBuffer().toString());
        formatter.printHelp("command", null, true);
        assertEquals(expectedUsage + expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelp_withWidthAndAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp("command", Arrays.asList(new Option("a", "arg1", true, "description1")));
        String expectedUsage = "usage: command [-a ARG1]\n";
        String expectedHelp = "arguments:\n"
                + "  ARG1:description1\n\n";
        assertEquals(expectedUsage, System.out.getBuffer().toString());
        formatter.printHelp("command", null, true);
        String help = System.out.getBuffer().toString();
        assertTrue(help.length() <= 50);
        assertEquals(expectedUsage.substring(0, 50) + "\n...", help);
    }
}