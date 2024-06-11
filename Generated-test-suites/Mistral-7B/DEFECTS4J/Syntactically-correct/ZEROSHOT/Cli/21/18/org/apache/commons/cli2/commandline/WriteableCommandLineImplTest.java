import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_constructor() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertSame(rootOption, writeableCommandLine.getRootOption());
        assertSame(arguments, writeableCommandLine.getArguments());
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        Option option1 = new Option("opt1", "option1", false, "description1");
        Option option2 = new Option("opt2", "option2", false, "description2");

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        assertTrue(writeableCommandLine.getOptions().contains(option1));
        assertTrue(writeableCommandLine.getOptions().contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", true, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertSame(expectedValues, values);
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", false, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", false, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", false, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        Option returnedOption = writeableCommandLine.getOption("opt");
        assertSame(option, returnedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", true, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertSame(expectedValues, values);
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", true, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getUndefaultedValues(option);
        assertNull(values);
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", false, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);

        writeableCommandLine.addSwitch(option, false);
        Boolean switchValue2 = writeableCommandLine.getSwitch(option, true);
        assertFalse(switchValue2);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", false, "description") {};
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addProperty(option, "property", "value");
        String propertyValue = writeableCommandLine.getProperty("property");
        assertSame("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", false, "description") {};
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        writeableCommandLine.addProperty(option, "property", properties);

        Map optionToPropertiesMap = writeableCommandLine.getOptionToProperties();
        Map expectedOptionToPropertiesMap = new HashMap();
        expectedOptionToPropertiesMap.put(option, properties);
        assertSame(expectedOptionToPropertiesMap, optionToPropertiesMap);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty_defaultValue() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", false, "description") {};
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addProperty(option, "property", "value");
        String propertyValue = writeableCommandLine.getProperty("property", "defaultValue");
        assertSame("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", false, "description") {};
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        writeableCommandLine.addProperty(option, "property", properties);

        Set propertiesSet = writeableCommandLine.getProperties(option);
        Set expectedPropertiesSet = Collections.unmodifiableSet(properties.keySet());
        assertSame(expectedPropertiesSet, propertiesSet);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        writeableCommandLine.addProperty(new PropertyOption(), "property", properties);

        Set propertiesSet = writeableCommandLine.getProperties();
        Set expectedPropertiesSet = Collections.unmodifiableSet(properties.keySet());
        assertSame(expectedPropertiesSet, propertiesSet);
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        assertTrue(writeableCommandLine.looksLikeOption("opt"));
        assertTrue(writeableCommandLine.looksLikeOption("opt --long-option"));
        assertFalse(writeableCommandLine.looksLikeOption("arg"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", false, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        String toString = writeableCommandLine.toString();
        String expectedToString = "opt value1 value2";
        assertSame(expectedToString, toString);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option1 = new Option("opt1", "option1", false, "description1");
        Option option2 = new Option("opt2", "option2", false, "description2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();
        List expectedOptions = Arrays.asList(option1, option2);
        assertSame(expectedOptions, options);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option1 = new Option("opt1", "option1", false, "description1");
        Option option2 = new Option("opt2", "option2", false, "description2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Set expectedOptionTriggers = Arrays.asList("opt1", "opt2").stream().map(String::new).collect(Collectors.toSet());
        assertSame(expectedOptionTriggers, optionTriggers);
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", true, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, null);
        List expectedValues = new ArrayList();
        expectedValues.add("defaultValue1");
        expectedValues.add("defaultValue2");
        assertSame(expectedValues, values);
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option = new Option("opt", "option", false, "description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option rootOption = new Option("root", "rootOption", false, "description");
        Option option1 = new Option("opt1", "option1", false, "description1");
        Option option2 = new Option("opt2", "option2", false, "description2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List normalised = writeableCommandLine.getNormalised();
        List expectedNormalised = Arrays.asList("root", "opt1", "opt2");
        assertSame(expectedNormalised, normalised);
    }
}