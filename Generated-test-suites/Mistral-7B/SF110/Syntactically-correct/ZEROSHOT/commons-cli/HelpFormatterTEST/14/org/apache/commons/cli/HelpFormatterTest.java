package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HelpFormatterTest {

    @Test
    public void testDefaultHelpFormatter() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testCustomWidth() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.printHelp("test", options);
    }

    @Test
    public void testCustomLeftPadding() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.printHelp("test", options);
    }

    @Test
    public void testCustomDescPadding() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(10);
        formatter.printHelp("test", options);
    }

    @Test
    public void testCustomSyntaxPrefix() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("help: ");
        formatter.printHelp("test", options);
    }

    @Test
    public void testCustomNewLine() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\n-------------------------------------\n");
        formatter.printHelp("test", options);
    }

    @Test
    public void testCustomOptPrefix() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--");
        formatter.printHelp("test", options);
    }

    @Test
    public void testCustomLongOptPrefix() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--");
        formatter.printHelp("test", options);
    }

    @Test
    public void testCustomArgName() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");
        formatter.printHelp("test", options);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        String header = "Help for MyApp";
        String footer = "Type 'MyApp -h' for more information.";
        formatter.printHelp("test", options, header, footer);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options, true);
    }

    @Test
    public void testPrintHelpWithEmptyCmdLineSyntax() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("", options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("c", "optionC", false, "Option C")
                .addOption("d", "optionD", true, "Option D"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, options);
    }

    @Test
    public void testPrintHelpWithEmptyOptions() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", new Options());
    }

    @Test
    public void testPrintHelpWithOptionsInDifferentGroups() {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption("a", "optionA", false, "Option A");
        group1.addOption("b", "optionB", true, "Option B");
        options.addOptionGroup(group1);

        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        group2.addOption("c", "optionC", false, "Option C");
        group2.addOption("d", "optionD", true, "Option D");
        options.addOptionGroup(group2);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testPrintHelpWithOptionalOptions() {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testPrintHelpWithMixedOptions() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOption("c", "optionC", false, "Option C");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testPrintHelpWithComplexOptions() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.addOption("c", "optionC", false, "Option C");
        group.addOption("d", "optionD", true, "Option D");
        options.addOptionGroup(group);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testPrintHelpWithLongOptions() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.addOption("c", "optionC", false, "Option C with long name");
        group.addOption("d", "optionD", true, "Option D with long name");
        options.addOptionGroup(group);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testPrintHelpWithOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        Option option = new Option("c", "optionC", true, "Option C with argument");
        option.setArgName("inputFile");
        options.addOption(option);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testPrintHelpWithEmptyOptionsAndArguments() {
        Options options = new Options();
        Option option = new Option("c", "optionC", true, "Option C with empty argument");
        option.setArgName("");
        options.addOption(option);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testPrintHelpWithOptionsAndArgumentsWithValues() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        Option option = new Option("c", "optionC", true, "Option C with argument");
        option.setArgName("inputFile");
        option.setValueRequired(false);
        option.setDefaultValue("defaultValue");
        options.addOption(option);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testPrintHelpWithCustomOptionComparator() {
        Options options = new Options();
        options.addOption("z", "optionZ", false, "Option Z");
        options.addOption("y", "optionY", true, "Option Y");
        options.addOption("x", "optionX", false, "Option X");
        options.addOption("w", "optionW", false, "Option W");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Option option1 = (Option)o1;
                Option option2 = (Option)o2;
                return option2.getKey().compareToIgnoreCase(option1.getKey());
            }
        });
        formatter.printHelp("test", options);
    }
}