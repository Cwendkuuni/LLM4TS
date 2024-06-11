import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parsers.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getOptionValues("o");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getOptionValues("o");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("s").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValues("o");
        Assert.assertEquals(2, values.size());
        Assert.assertNull(commandLine.getOptionValues("o").get(0));
        Assert.assertEquals("default1", commandLine.getOptionValues("o").get(1));
        Assert.assertEquals("default2", commandLine.getOptionValues("o").get(2));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("s").build();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("s"));
        Assert.assertTrue(commandLine.getOptionState("s").isSet());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("s").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("s"));
        Assert.assertTrue(commandLine.getOptionState("s").isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getProperties().get("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertTrue(commandLine.looksLikeOption("o"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }
}