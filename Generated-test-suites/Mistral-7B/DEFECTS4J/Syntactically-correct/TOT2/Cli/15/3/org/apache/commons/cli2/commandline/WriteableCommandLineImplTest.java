import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = Option.builder("option").longOpt("long-option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = Option.builder("option").longOpt("long-option").valueSeparator('=').build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value");
        List<Object> values = writeableCommandLine.getValues(option, Collections.emptyList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_switchAlreadySet() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = Option.builder("option").longOpt("long-option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        Option result = writeableCommandLine.getOption("long-option");
        Assert.assertEquals(option, result);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = Option.builder("option").longOpt("long-option").valueSeparator('=').build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List<Object> values = writeableCommandLine.getValues(option, Collections.emptyList());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = Option.builder("option").longOpt("long-option").valueSeparator('=').build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List<Object> values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = Option.builder("option").longOpt("long-option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");
        String propertyValue = writeableCommandLine.getProperty(option, "property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option option = Option.builder("option").longOpt("long-option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        Map<String, String> properties = new HashMap<>();
        properties.put("property1", "value1");
        writeableCommandLine.addProperty(option, properties);
        Map<String, String> resultProperties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(1, resultProperties.size());
        Assert.assertEquals("value1", resultProperties.get("property1"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = Option.builder("option").longOpt("long-option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        Map<String, String> properties = new HashMap<>();
        properties.put("property1", "value1");
        writeableCommandLine.addProperty(option, properties);
        Set<String> resultProperties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(1, resultProperties.size());
        Assert.assertEquals("property1", resultProperties.iterator().next());
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Assert.assertFalse(writeableCommandLine.looksLikeOption(""));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--option"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("-o"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = Option.builder("option").longOpt("long-option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value");
        String result = writeableCommandLine.toString();
        String expected = String.join(" ", new String[]{"--long-option=value"});
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option1 = Option.builder("option1").longOpt("long-option1").build();
        Option option2 = Option.builder("option2").longOpt("long-option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List<Option> resultOptions = writeableCommandLine.getOptions();
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        Assert.assertEquals(expectedOptions, resultOptions);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option option1 = Option.builder("option1").longOpt("long-option1").build();
        Option option2 = Option.builder("option2").longOpt("long-option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set<String> resultOptionTriggers = writeableCommandLine.getOptionTriggers();
        Set<String> expectedOptionTriggers = new HashSet<>();
        expectedOptionTriggers.addAll(Collections.singletonList("option1"));
        expectedOptionTriggers.addAll(Collections.singletonList("option2"));
        Assert.assertEquals(expectedOptionTriggers, resultOptionTriggers);
    }
}