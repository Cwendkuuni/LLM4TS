import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option rootOption = new Option("opt", "option", true, "The option");
        List arguments = new ArrayList();
        arguments.add("value1");
        arguments.add("value2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // When

        // Add option
        Option option1 = new Option("o1", "option1", false, "Option 1");
        writeableCommandLine.addOption(option1);

        // Add value for option
        writeableCommandLine.addValue(option1, "value3");

        // Add switch for option
        writeableCommandLine.addSwitch(option1, true);

        // Add property for option
        writeableCommandLine.addProperty(option1, "property", "propertyValue");

        // Then
        Assert.assertTrue(writeableCommandLine.hasOption(option1));
        Assert.assertEquals("value3", writeableCommandLine.getValues(option1, null).get(0));
        Assert.assertTrue(writeableCommandLine.getSwitch(option1, false));
        Assert.assertEquals("propertyValue", writeableCommandLine.getProperty(option1, "property"));

        // Verify options
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Option option = (Option) options.get(0);
        Assert.assertEquals("o1", option.getShortOpt());
        Assert.assertEquals("option1", option.getLongOpt());
        Assert.assertEquals(true, option.isRequired());
        Assert.assertEquals(true, option.isHidden());
        Assert.assertEquals(null, option.getValue());
        Assert.assertEquals(Collections.singletonList("value3"), writeableCommandLine.getValues(option, null));
        Assert.assertEquals(true, writeableCommandLine.getSwitch(option, false));
        Assert.assertEquals("propertyValue", writeableCommandLine.getProperty(option, "property"));
        Assert.assertEquals(Collections.singletonMap("property", "propertyValue"), writeableCommandLine.getProperties(option));

        // Verify normalised arguments
        Assert.assertEquals(2, writeableCommandLine.getNormalised().size());
        Assert.assertEquals("value1", writeableCommandLine.getNormalised().get(0));
        Assert.assertEquals("value2", writeableCommandLine.getNormalised().get(1));
    }
}