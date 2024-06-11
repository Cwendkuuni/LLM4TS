package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * WriteableCommandLineImplTest class file.
 */
public class WriteableCommandLineImplTest {

    /**
     * Test method for WriteableCommandLineImpl class.
     */
    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option option1 = new Option("o", "output", true, "Path to output file");
        Option option2 = new Option("i", "input", true, "Path to input file");
        Option option3 = new Option("h", "help", false, "Print this help message.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList("inputFilePath"));

        // When - setting values
        commandLine.addValue(option1, "outputFilePath");
        commandLine.addValue(option2, "inputFilePath");
        commandLine.addSwitch(option3, true);

        // Then - verified values
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option1, commandLine.getOptions().get(0));

        List values1 = commandLine.getValues(option1, null);
        Assert.assertEquals(1, values1.size());
        Assert.assertEquals("outputFilePath", values1.get(0));

        List values2 = commandLine.getValues(option2, null);
        Assert.assertEquals(1, values2.size());
        Assert.assertEquals("inputFilePath", values2.get(0));

        Boolean switch1 = commandLine.getSwitch(option3, false);
        Assert.assertTrue(switch1);

        // When - getting properties
        commandLine.addProperty("someProperty", "someValue");
        Properties properties = commandLine.getProperties();
        Assert.assertEquals("someValue", properties.getProperty("someProperty"));

        // When - looksLikeOption
        Assert.assertTrue(commandLine.looksLikeOption("o"));
        Assert.assertFalse(commandLine.looksLikeOption("x"));

        // When - toString
        Assert.assertEquals("outputFilePath inputFilePath", commandLine.toString());

        // When - getOption
        Option option = commandLine.getOption("o");
        Assert.assertEquals(option1, option);

        // When - hasOption
        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertFalse(commandLine.hasOption(new Option("x", "x", false, "Print x help message.")));

        // When - getNormalised
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("outputFilePath", normalised.get(0));
        Assert.assertEquals("inputFilePath", normalised.get(1));

        // When - getOptionTriggers
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertEquals(option1.getPreferredName(), optionTriggers.toArray()[0]);
        Assert.assertEquals(option2.getPreferredName(), optionTriggers.toArray()[1]);

        // When - setDefaultValues
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        List defaultValues = new ArrayList();
        defaultValues.add("defaultOutputFilePath");
        commandLine.setDefaultValues(option1, defaultValues);

        values1 = commandLine.getValues(option1, null);
        Assert.assertEquals(1, values1.size());
        Assert.assertEquals("defaultOutputFilePath", values1.get(0));

        // When - setDefaultSwitch
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        Boolean defaultSwitch = false;
        commandLine.setDefaultSwitch(option1, defaultSwitch);

        switch1 = commandLine.getSwitch(option1, null);
        Assert.assertNull(switch1);

        // When - toString
        commandLine.addValue(option1, "outputFilePath");
        Assert.assertEquals("outputFilePath", commandLine.toString());

        // When - getValues with default values
        values1 = commandLine.getValues(option1, null);
        Assert.assertEquals(1, values1.size());
        Assert.assertEquals("outputFilePath", values1.get(0));

        values1 = commandLine.getValues(option1, defaultValues);
        Assert.assertEquals(1, values1.size());
        Assert.assertEquals("outputFilePath", values1.get(0));
        Assert.assertEquals("defaultOutputFilePath", values1.get(1));

        // When - getSwitch with default switch
        switch1 = commandLine.getSwitch(option1, defaultSwitch);
        Assert.assertNull(switch1);

        // When - getSwitch with non-default switch
        commandLine.addSwitch(option1, true);
        switch1 = commandLine.getSwitch(option1, defaultSwitch);
        Assert.assertEquals(Boolean.TRUE, switch1);
    }
}