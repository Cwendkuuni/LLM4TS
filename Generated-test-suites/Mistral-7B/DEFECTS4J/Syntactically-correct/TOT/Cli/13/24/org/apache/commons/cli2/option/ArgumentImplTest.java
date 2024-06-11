package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.ArgumentImpl;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.List;

public class ArgumentImplTest {

    private Argument option;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        option = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, "--", null, 0);
        parser = new DefaultParser();
    }

    @Test
    public void testProcessValues_OneValue_Success() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> arguments = List.of("--arg", "value");

        option.process(commandLine, arguments.listIterator(), option);

        List<String> values = commandLine.getValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testProcessValues_OneValue_InvalidQuotes() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> arguments = List.of("--arg", "\"value\"");

        option.process(commandLine, arguments.listIterator(), option);

        List<String> values = commandLine.getValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testProcessValues_MultipleValues_Success() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> arguments = List.of("--arg", "value1", "value2");

        option.process(commandLine, arguments.listIterator(), option);

        List<String> values = commandLine.getValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testProcessValues_MultipleValues_InvalidQuotes() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> arguments = List.of("--arg", "\"value1\"", "value2");

        option.process(commandLine, arguments.listIterator(), option);

        List<String> values = commandLine.getValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testProcessValues_InvalidValue_ThrowsOptionException() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> arguments = List.of("--arg", "value1", "invalid-value");

        try {
            option.process(commandLine, arguments.listIterator(), option);
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals("value1", commandLine.getValues(option).get(0));
            Assert.assertEquals("invalid-value", e.getMessage());
        }
    }

    @Test
    public void testProcessValues_RemainingValues_Consumes() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> arguments = List.of("--arg", "--consume", "value1", "value2", "value3");

        option.process(commandLine, arguments.listIterator(), option);

        List<String> values = commandLine.getValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        List<String> remainingValues = commandLine.getRemainingOptions();
        Assert.assertEquals(1, remainingValues.size());
        Assert.assertEquals("--consume", remainingValues.get(0));
    }

    @Test
    public void testProcessValues_RemainingValues_DoesNotConsume() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> arguments = List.of("--arg", "value1", "--consume", "value3");

        try {
            option.process(commandLine, arguments.listIterator(), option);
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals(1, commandLine.getValues(option).size());
            Assert.assertEquals("value1", commandLine.getValues(option).get(0));
            Assert.assertEquals("--consume", e.getMessage());
        }
    }

    @Test
    public void testValidate_ValidValues() {
        try {
            WriteableCommandLine commandLine = new DefaultCommandLine();
            List<String> arguments = List.of("--arg", "value1", "value2");
            option.validate(commandLine, option);
        } catch (OptionException e) {
            Assert.fail();
        }
    }

    @Test
    public void testValidate_InvalidValues() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> arguments = List.of("--arg", "value1", "invalid-value");

        try {
            option.validate(commandLine, option);
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals("invalid-value", e.getMessage());
        }
    }
}