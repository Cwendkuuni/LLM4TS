import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cmd(); // Assuming Cmd is a concrete implementation of WriteableCommandLine
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option")
                .valueSeparator('=')
                .required(true)
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("test"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option")
                .valueSeparator(',')
                .required(false)
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test1,test2");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("test1", "test2"), values);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        List defaults = Arrays.asList("default1", "default2");
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertNotNull(values);
        Assert.assertEquals(defaults, values);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.getValues().contains(Boolean.TRUE));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, Boolean.TRUE);
        Assert.assertTrue(option.getValues().contains(Boolean.TRUE));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getPropertyValue(option, "property"));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = new Cmd();
        commandLine.addProperty("property", "defaultValue");
        Assert.assertEquals("defaultValue", commandLine.getPropertyValue("property"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }
}