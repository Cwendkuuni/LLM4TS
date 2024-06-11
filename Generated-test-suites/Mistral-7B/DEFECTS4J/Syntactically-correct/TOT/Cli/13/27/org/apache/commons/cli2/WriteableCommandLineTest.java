package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Test;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setup() {
        commandLine = new CliCommandLine();
        option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertNotNull(commandLine.getOption("o"));
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "test");
        List<String> values = commandLine.getValuesForOption("o");
        assertEquals(1, values.size());
        assertEquals("test", values.get(0));
    }

    @Test
    public void testLooksLikeOption() {
        commandLine = new CliCommandLine();

        // Positive test case: valid option trigger
        String optionTrigger = "--option";
        assertTrue(commandLine.looksLikeOption(optionTrigger));

        // Negative test case: invalid argument
        String invalidArgument = "invalidArgument";
        assertFalse(commandLine.looksLikeOption(invalidArgument));

        // Edge case: empty argument
        String emptyArgument = "";
        assertFalse(commandLine.looksLikeOption(emptyArgument));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "test");
        assertEquals("test", commandLine.getProperty("property"));
    }

    @Test
    public void testDefaultValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .defaultValue("test")
                .build();
        commandLine.addOption(option);
        List<String> values = commandLine.getValuesForOption("o");
        assertEquals(1, values.size());
        assertEquals("test", values.get(0));
    }

    @Test
    public void testDefaultSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .defaultValue(true)
                .build();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("s"));
    }
}