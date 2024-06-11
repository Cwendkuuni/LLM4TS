package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList());

        cmdLine.addOption(option);

        Option actualOption = cmdLine.getOption("option");
        Option expectedOption = option;

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList());

        cmdLine.addValue(option, "value1");

        List<String> actualValues = cmdLine.getValues(option, null);
        List<String> expectedValues = new ArrayList();
        expectedValues.add("value1");

        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList());

        cmdLine.addSwitch(option, true);

        Boolean actualSwitch = cmdLine.getSwitch(option, false);
        Boolean expectedSwitch = true;

        Assert.assertEquals(expectedSwitch, actualSwitch);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList());

        cmdLine.addOption(option);

        boolean actualHasOption = cmdLine.hasOption(option);
        boolean expectedHasOption = true;

        Assert.assertEquals(expectedHasOption, actualHasOption);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_alreadySet() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList());

        cmdLine.addSwitch(option, true);
        cmdLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList());

        cmdLine.addOption(option);

        Option actualOption = cmdLine.getOption("option");
        Option expectedOption = option;

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList());

        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");

        List<String> actualValues = cmdLine.getValues(option, null);
        List<String> expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");

        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList());

        cmdLine.addValue(option, "value1");

        List<String> actualValues = cmdLine.getUndefaultedValues(option);
        List<String> expectedValues = new ArrayList();
        expectedValues.add("value1");

        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList());

        cmdLine.addSwitch(option, true);

        Boolean actualSwitch = cmdLine.getSwitch(option, false);
        Boolean expectedSwitch = true;

        Assert.assertEquals(expectedSwitch, actualSwitch);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList());

        cmdLine.addProperty(option, "property", "value1");

        String actualProperty = cmdLine.getProperty("property");
        String expectedProperty = "value1";

        Assert.assertEquals(expectedProperty, actualProperty);
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList());

        cmdLine.addValue(option, "value1");

        String actualToString = cmdLine.toString();
        String expectedToString = "value1";

        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option1 = Option.builder("o1").longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o2").longOpt("option2")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option1, new ArrayList());

        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        List<Option> actualOptions = cmdLine.getOptions();
        List<Option> expectedOptions = new ArrayList();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        Assert.assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option option1 = Option.builder("o1").longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o2").longOpt("option2")
                .required(false)
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option1, new ArrayList());

        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        Set<String> actualOptionTriggers = cmdLine.getOptionTriggers();
        Set<String> expectedOptionTriggers = new HashMap<>().keySet();
        expectedOptionTriggers.add("o1");
        expectedOptionTriggers.add("o2");

        Assert.assertEquals(expectedOptionTriggers, actualOptionTriggers);
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption_withPrefix() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList());

        String actualLooksLikeOption = cmdLine.looksLikeOption("--option");
        String expectedLooksLikeOption = "option";

        Assert.assertEquals(expectedLooksLikeOption, actualLooksLikeOption);
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption_withoutPrefix() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList());

        String actualLooksLikeOption = cmdLine.looksLikeOption("option");
        String expectedLooksLikeOption = null;

        Assert.assertNull(expectedLooksLikeOption);
        Assert.assertFalse(cmdLine.looksLikeOption(""));
    }
}