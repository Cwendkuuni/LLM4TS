import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option optionA = new Option("a", "alpha", true, "This is option A");
        Option optionB = new Option("b", "beta", false, "This is option B");
        Option optionC = new Option("c", "char", 'c', "This is option C");
        Option optionD = new Option("d", "delta", null, "This is option D");

        List<String> arguments = new ArrayList<>();
        arguments.add("--alpha");
        arguments.add("true");
        arguments.add("--beta");
        arguments.add("false");
        arguments.add("--char=c");

        Options options = new Options();
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        options.addOption(optionD);

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(optionA, arguments);
        commandLine.parseOptions(options);

        // Test getOption method
        Option actualOptionA = commandLine.getOption("a");
        Assert.assertEquals(optionA, actualOptionA);

        // Test getValue method
        List<Object> actualValuesA = commandLine.getValues(optionA, null);
        Assert.assertEquals("true", actualValuesA.get(0).toString());

        // Test hasOption method
        boolean actualHasOptionB = commandLine.hasOption(optionB);
        Assert.assertTrue(actualHasOptionB);

        // Test getSwitch method
        boolean actualSwitchC = commandLine.getSwitch(optionC, null);
        Assert.assertEquals(Character.toString(optionC.getValue().charAt(0)), actualSwitchC.toString());

        // Test getProperty method
        Option propertyOption = new Option("p", "property", true, "This is a property option");
        commandLine.addProperty(propertyOption, "key", "value");
        String actualPropertyValue = commandLine.getProperty("p:key");
        Assert.assertEquals("value", actualPropertyValue);

        // Test getProperties method
        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("key", "value");
        Map<String, String> actualProperties = commandLine.getProperties(propertyOption);
        Assert.assertEquals(expectedProperties, actualProperties);

        // Test looksLikeOption method
        boolean actualLooksLikeOptionD = commandLine.looksLikeOption("--delta");
        Assert.assertTrue(actualLooksLikeOptionD);

        // Test getValues method with no values
        Option optionE = new Option("e", "echo", false, "This is option E");
        commandLine = new WriteableCommandLineImpl(optionE, Collections.emptyList());
        List<Object> actualValuesE = commandLine.getValues(optionE, null);
        Assert.assertEquals(Collections.EMPTY_LIST, actualValuesE);

        // Test getSwitch method with no value
        Option optionF = new Option("f", "flag", false, "This is option F");
        commandLine = new WriteableCommandLineImpl(optionF, Collections.emptyList());
        Boolean actualSwitchF = commandLine.getSwitch(optionF, null);
        Assert.assertNull(actualSwitchF);

        // Test getProperty method with no value
        Option optionG = new Option("g", "get", true, "This is option G");
        commandLine = new WriteableCommandLineImpl(optionG, Collections.emptyList());
        String actualPropertyValueG = commandLine.getProperty("g:key", "defaultValue");
        Assert.assertEquals("defaultValue", actualPropertyValueG);

        // Test parseOptions method with invalid arguments
        Options invalidOptions = new Options();
        List<String> invalidArguments = new ArrayList<>();
        invalidArguments.add("--invalid");
        commandLine = new WriteableCommandLineImpl(optionA, invalidArguments);
        try {
            commandLine.parseOptions(invalidOptions);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}