package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.*; // for all options
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testBasicUsage() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        Option result = commandLine.getOption("option");
        List values = commandLine.getValues(option, null);

        Assert.assertEquals(option, result);
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testSwitchUsage() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean result = commandLine.getSwitch(option, null);

        Assert.assertTrue(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadySet() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testPropertyUsage() {
        Option option = Option.builder("p").longOpt("property")
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);
        commandLine.addProperty(option, "name", "value");

        String result = commandLine.getProperty("property.name");

        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetOption() {
        Option option1 = Option.builder("o1").longOpt("option1")
                .required(false)
                .hasArg(false)
                .build();

        Option option2 = Option.builder("o2").longOpt("option2")
                .required(false)
                .hasArg(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option resultOption1 = commandLine.getOption("option1");
        Option resultOption2 = commandLine.getOption("option2");

        Assert.assertEquals(option1, resultOption1);
        Assert.assertEquals(option2, resultOption2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionNull() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Option result = commandLine.getOption(null);
    }

    @Test
    public void testValuesEmpty() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        List values = commandLine.getValues(option, null);

        Assert.assertTrue(values.isEmpty());
    }

    @Test
    public void testValuesNonEmpty() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getUndefaultedValues(option);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testToString() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .hasArg(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        String result = commandLine.toString();

        Assert.assertEquals("o option", result);
    }
}