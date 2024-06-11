import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = newDefaultCommandLine();

        Option option = Option.builder("o")
                .longOpt("option")
                .required(false)
                .build();

        commandLine.addOption(option);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = newDefaultCommandLine();

        Option option = Option.builder("o")
                .longOpt("option")
                .valueSeparator('=')
                .build();

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");

        values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = newDefaultCommandLine();

        Option option = Option.builder("o")
                .longOpt("option")
                .required(false)
                .build();

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));

        values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");

        values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        values = commandLine.getUndefaultedValues(new Option("newOption", true, "n"));
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = newDefaultCommandLine();

        Option option = Option.builder("o")
                .longOpt("option")
                .required(false)
                .build();

        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));

        commandLine.addValue(option, "value1");

        values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
        Assert.assertEquals("value1", values.get(2));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = newDefaultCommandLine();

        Option option = Option.builder("o")
                .longOpt("option")
                .required(false)
                .build();

        commandLine.addOption(option);

        try {
            commandLine.addSwitch(option, true);
            Assert.fail("Should not be able to add switch value to an Option that doesn't have a switch");
        } catch (IllegalStateException e) {
            // expected
        }

        option.setType(OptionType.BUILD_SWITCH);

        commandLine.addSwitch(option, true);

        boolean switchValue = commandLine.getOptionValue(option, false);
        Assert.assertTrue(switchValue);

        try {
            commandLine.addSwitch(option, true);
            Assert.fail("Should not be able to add switch value to an Option that already has a switch");
        } catch (IllegalStateException e) {
            // expected
        }

        commandLine.setDefaultSwitch(option, true);

        switchValue = commandLine.getOptionValue(option, false);
        Assert.assertTrue(switchValue);

        commandLine.setDefaultSwitch(option, false);

        switchValue = commandLine.getOptionValue(option, false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = newDefaultCommandLine();

        Option option = Option.builder("o")
                .longOpt("option")
                .required(false)
                .build();

        commandLine.addOption(option);

        try {
            commandLine.setDefaultSwitch(option, true);
            Assert.fail("Should not be able to set default switch for an Option that doesn't have a switch");
        } catch (IllegalStateException e) {
            // expected
        }

        option.setType(OptionType.BUILD_SWITCH);

        commandLine.setDefaultSwitch(option, true);

        boolean switchValue = commandLine.getOptionValue(option, false);
        Assert.assertTrue(switchValue);

        commandLine.setDefaultSwitch(option, false);

        switchValue = commandLine.getOptionValue(option, false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = newDefaultCommandLine();

        Option option = Option.builder("o")
                .longOpt("option")
                .required(false)
                .build();

        commandLine.addOption(option);

        commandLine.addProperty(option, "property1", "value1");

        String propertyValue = commandLine.getOptionProperty(option, "property1");
        Assert.assertEquals("value1", propertyValue);

        commandLine.addProperty(option, "property2", "value2");

        propertyValue = commandLine.getOptionProperty(option, "property2");
        Assert.assertEquals("value2", propertyValue);

        commandLine.setDefaultProperties(Arrays.asList("defaultProperty1=defaultValue1", "defaultProperty2=defaultValue2"));

        propertyValue = commandLine.getOptionProperty(option, "property1");
        Assert.assertEquals("value1", propertyValue);

        propertyValue = commandLine.getOptionProperty(option, "property2");
        Assert.assertEquals("value2", propertyValue);

        propertyValue = commandLine.getOptionProperty(option, "nonexistentProperty");
        Assert.assertNull(propertyValue);

        commandLine.addProperty(option, "property1", "newValue1");

        propertyValue = commandLine.getOptionProperty(option, "property1");
        Assert.assertEquals("newValue1", propertyValue);
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = newDefaultCommandLine();

        commandLine.addProperty("property1", "defaultValue1");

        String propertyValue = commandLine.getOptionProperty("property1");
        Assert.assertEquals("defaultValue1", propertyValue);

        commandLine.addProperty("property1", "newValue1");

        propertyValue = commandLine.getOptionProperty("property1");
        Assert.assertEquals("newValue1", propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = newDefaultCommandLine();

        Assert.assertFalse(commandLine.looksLikeOption(""));
        Assert.assertFalse(commandLine.looksLikeOption("arg"));
        Assert.assertFalse(commandLine.looksLikeOption("--arg"));
        Assert.assertFalse(commandLine.looksLikeOption("--"));
        Assert.assertFalse(commandLine.looksLikeOption("--arg="));
        Assert.assertFalse(commandLine.looksLikeOption("--arg=" + System.lineSeparator()));
        Assert.assertFalse(commandLine.looksLikeOption("--arg=" + System.lineSeparator() + "--arg"));

        Option option = Option.builder("o")
                .longOpt("option")
                .required(false)
                .build();

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertTrue(commandLine.looksLikeOption("--option="));
        Assert.assertFalse(commandLine.looksLikeOption("--arg"));

        commandLine.addOption(Option.builder("a")
                .longOpt("another-option")
                .required(false)
                .build());

        Assert.assertTrue(commandLine.looksLikeOption("--another-option"));
        Assert.assertTrue(commandLine.looksLikeOption("--another-option="));
        Assert.assertFalse(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = newDefaultCommandLine();

        Option option1 = Option.builder("o1")
                .longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o2")
                .longOpt("option2")
                .required(false)
                .build();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        commandLine.setCurrentOption(option1);

        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(option1, currentOption);

        commandLine.setCurrentOption(option2);

        currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(option2, currentOption);

        commandLine.setCurrentOption(null);

        currentOption = commandLine.getCurrentOption();
        Assert.assertNull(currentOption);
    }

    private WriteableCommandLine newDefaultCommandLine() {
        WriteableCommandLine commandLine = newDefaultParser().parse(new OptionParser() {
            @Override
            public void parse(ParseContext context) {
                // empty implementation
            }
        }, new CommandLineBase());

        return commandLine;
    }
}