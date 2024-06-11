import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

/**
 * WriteableCommandLineImplTest class.
 */
public class WriteableCommandLineImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Option rootOption = new Option("test", "Test option", true, "Test value")
                .required(false)
                .longOpt("long-test")
                .args(new String[]{"arg1", "arg2"});

        List arguments = new ArrayList();
        arguments.add("--test");
        arguments.add("arg1");
        arguments.add("arg2");
        arguments.add("--long-test");
        arguments.add("value1");
        arguments.add("--long-test");
        arguments.add("value2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Assert.assertNotNull(commandLine);
    }

    /**
     * Test 'hasOption' method.
     */
    @Test
    public void testHasOption() {
        Option testOption = new Option("test", "Test option", true, "Test value")
                .required(false)
                .longOpt("long-test");

        WriteableCommandLineImpl commandLine = testOptionHasOption(commandLine(), testOption);

        Assert.assertTrue(commandLine.hasOption(testOption));
    }

    /**
     * Test 'getOption' method.
     */
    @Test
    public void testGetOption() {
        Option testOption = new Option("test", "Test option", true, "Test value")
                .required(false)
                .longOpt("long-test");

        WriteableCommandLineImpl commandLine = testOptionHasOption(commandLine(), testOption);

        Option resultOption = commandLine.getOption(testOption.getPreferredName());
        Option resultOptionLong = commandLine.getOption("long-test");

        Assert.assertEquals(testOption, resultOption);
        Assert.assertEquals(testOption, resultOptionLong);
    }

    /**
     * Test 'getValues' method.
     */
    @Test
    public void testGetValues() {
        Option testOption = new Option("test", "Test option", true, "Test value")
                .required(false)
                .longOpt("long-test");

        WriteableCommandLineImpl commandLine = testOptionHasValue(commandLine(), testOption, "arg1", "arg2");

        List resultValues = commandLine.getValues(testOption, null);

        Assert.assertEquals(2, resultValues.size());
        Assert.assertEquals("arg1", resultValues.get(0));
        Assert.assertEquals("arg2", resultValues.get(1));
    }

    /**
     * Test 'getSwitch' method.
     */
    @Test
    public void testGetSwitch() {
        Option testOption = new Option("test", "Test option", false, "Test switch");

        WriteableCommandLineImpl commandLine = testOptionHasSwitch(commandLine(), testOption, true);

        Boolean resultSwitch = commandLine.getSwitch(testOption, false);

        Assert.assertTrue(resultSwitch);
    }

    /**
     * Test 'getProperty' method.
     */
    @Test
    public void testGetProperty() {
        Option propertyOption = new Option("test", "Test option", true, "Test value")
                .required(false)
                .longOpt("long-test")
                .args(new String[]{"argument"})
                .valueSeparator('=')
                .numberOfValues(1)
                .valueParser(new StringValueParser());

        WriteableCommandLineImpl commandLine = propertyOptionHasProperty(commandLine(), propertyOption, "property1", "value1");

        String resultProperty = commandLine.getProperty("property1");

        Assert.assertEquals("value1", resultProperty);
    }

    /**
     * Test 'addProperty' method.
     */
    @Test
    public void testAddProperty() {
        Option propertyOption = new Option("test", "Test option", true, "Test value")
                .required(false)
                .longOpt("long-test")
                .args(new String[]{"argument"})
                .valueSeparator('=')
                .numberOfValues(1)
                .valueParser(new StringValueParser());

        WriteableCommandLineImpl commandLine = commandLineNoProperties();

        commandLine.addProperty(propertyOption, "property1", "value1");

        String resultProperty = commandLine.getProperty("property1");

        Assert.assertEquals("value1", resultProperty);
    }

    /**
     * Test 'getProperties' method.
     */
    @Test
    public void testGetProperties() {
        Option propertyOption = new Option("test", "Test option", true, "Test value")
                .required(false)
                .longOpt("long-test")
                .args(new String[]{"argument"})
                .valueSeparator('=')
                .numberOfValues(1)
                .valueParser(new StringValueParser());

        WriteableCommandLineImpl commandLine = propertyOptionHasProperties(commandLine(), propertyOption, "property1", "value1", "property2", "value2");

        Set resultProperties = commandLine.getProperties();

        Assert.assertEquals(2, resultProperties.size());
        Assert.assertTrue(resultProperties.contains("property1"));
        Assert.assertTrue(resultProperties.contains("property2"));
    }

    /**
     * Test 'toString' method.
     */
    @Test
    public void testToString() {
        Option testOption = new Option("test", "Test option", true, "Test value")
                .required(false)
                .longOpt("long-test");

        WriteableCommandLineImpl commandLine = testOptionHasOption(commandLine(), testOption);

        String resultString = commandLine.toString();

        String expectedString = "--test arg1 arg2 --long-test value1 value2";

        Assert.assertEquals(expectedString, resultString);
    }

    /**
     * Test 'getOptions' method.
     */
    @Test
    public void testGetOptions() {
        Option testOption1 = new Option("test1", "Test option 1", true, "Test value 1")
                .required(false)
                .longOpt("long-test1");

        Option testOption2 = new Option("test2", "Test option 2", false, "Test switch 2");

        WriteableCommandLineImpl commandLine = testOptions(commandLine(), testOption1, testOption2);

        List resultOptions = commandLine.getOptions();

        Assert.assertEquals(2, resultOptions.size());
        Assert.assertTrue(resultOptions.contains(testOption1));
        Assert.assertTrue(resultOptions.contains(testOption2));
    }

    /**
     * Test 'getOptionTriggers' method.
     */
    @Test
    public void testGetOptionTriggers() {
        Option testOption1 = new Option("test1", "Test option 1", true, "Test value 1")
                .required(false)
                .longOpt("long-test1");

        Option testOption2 = new Option("test2", "Test option 2", false, "Test switch 2");

        WriteableCommandLineImpl commandLine = testOptions(commandLine(), testOption1, testOption2);

        Set resultTriggers = commandLine.getOptionTriggers();

        Assert.assertEquals(2, resultTriggers.size());
        Assert.assertTrue(resultTriggers.contains("test1"));
        Assert.assertTrue(resultTriggers.contains("test2"));
    }

    /**
     * Helper method to create a new WriteableCommandLineImpl instance.
     *
     * @return WriteableCommandLineImpl instance
     */
    private WriteableCommandLineImpl commandLine() {
        Option rootOption = new Option("test", "Test option", true, "Test value")
                .required(false)
                .longOpt("long-test");

        List arguments = new ArrayList();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        return commandLine;
    }

    /**
     * Helper method to create a WriteableCommandLineImpl instance with an option.
     *
     * @param commandLine WriteableCommandLineImpl instance
     * @param option      Option to be added
     * @return WriteableCommandLineImpl instance
     */
    private WriteableCommandLineImpl testOptionHasOption(WriteableCommandLineImpl commandLine, Option option) {
        commandLine.addOption(option);

        return commandLine;
    }

    /**
     * Helper method to create a WriteableCommandLineImpl instance with an option and value.
     *
     * @param commandLine WriteableCommandLineImpl instance
     * @param option      Option to be added
     * @param value       Value to be added
     * @return WriteableCommandLineImpl instance
     */
    private WriteableCommandLineImpl testOptionHasValue(WriteableCommandLineImpl commandLine, Option option, String... value) {
        commandLine.addOption(option);
        commandLine.addValue(option, value[0]);

        return commandLine;
    }

    /**
     * Helper method to create a WriteableCommandLineImpl instance with an option switch.
     *
     * @param commandLine WriteableCommandLineImpl instance
     * @param option      Option to be added
     * @param switchValue Switch value to be added
     * @return WriteableCommandLineImpl instance
     */
    private WriteableCommandLineImpl testOptionHasSwitch(WriteableCommandLineImpl commandLine, Option option, boolean switchValue) {
        commandLine.addOption(option);
        commandLine.addSwitch(option, switchValue);

        return commandLine;
    }

    /**
     * Helper method to create a WriteableCommandLineImpl instance with an option and property.
     *
     * @param commandLine WriteableCommandLineImpl instance
     * @param option      Option to be added
     * @param property    Property to be added
     * @param value       Property value to be added
     * @return WriteableCommandLineImpl instance
     */
    private WriteableCommandLineImpl propertyOptionHasProperty(WriteableCommandLineImpl commandLine, Option option, String property, String value) {
        commandLine.addOption(option);
        commandLine.addProperty(option, property, value);

        return commandLine;
    }

    /**
     * Helper method to create a WriteableCommandLineImpl instance with options.
     *
     * @param commandLine WriteableCommandLineImpl instance
     * @param options     Options to be added
     * @return WriteableCommandLineImpl instance
     */
    private WriteableCommandLineImpl testOptions(WriteableCommandLineImpl commandLine, Option... options) {
        for (Option option : options) {
            commandLine.addOption(option);
        }

        return commandLine;
    }

    /**
     * Helper method to create a WriteableCommandLineImpl instance with no properties.
     *
     * @param commandLine WriteableCommandLineImpl instance
     * @return WriteableCommandLineImpl instance
     */
    private WriteableCommandLineImpl commandLineNoProperties() {
        Option rootOption = new Option("test", "Test option", true, "Test value")
                .required(false)
                .longOpt("long-test");

        List arguments = new ArrayList();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        return commandLine;
    }

    /**
     * Helper method to create a WriteableCommandLineImpl instance with options and properties.
     *
     * @param commandLine WriteableCommandLineImpl instance
     * @param option      Option to be added
     * @param property    Property to be added
     * @param value       Property value to be added
     * @return WriteableCommandLineImpl instance
     */
    private WriteableCommandLineImpl propertyOptionHasProperties(WriteableCommandLineImpl commandLine, Option option, String property, String value) {
        commandLine.addOption(option);
        commandLine.addProperty(option, property, value);

        Properties properties = new Properties();
        properties.setProperty(property, value);

        commandLine.addProperty(option, properties);

        return commandLine;
    }
}