import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getOptionValues("o");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValues("o");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(option.isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "propertyValue");
        String value = commandLine.getOptionProperty(option, "property");
        Assert.assertEquals("propertyValue", value);
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = new CliCommandLine();
        commandLine.addProperty("property", "defaultValue");
        String value = commandLine.getOptionProperty("property", "property");
        Assert.assertEquals("defaultValue", value);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }

    // Add tests for other methods or edge cases as necessary

    // Helper class to create a WriteableCommandLine instance for testing
    private static class CliCommandLine extends CommandLineBase implements WriteableCommandLine {
        @Override
        public void addOption(Option option) {
            super.addOption(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            super.addValue(option, value);
        }

        @Override
        public List getUndefaultedValues(Option option) {
            return super.getValues(option);
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            super.setDefaultValues(option, defaultValues);
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            super.setValue(option, value);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            super.setDefaultValue(option, defaultSwitch);
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            super.addProperty(option, property, value);
        }

        @Override
        public void addProperty(String property, String value) {
            super.addProperty(property, value);
        }
    }

}