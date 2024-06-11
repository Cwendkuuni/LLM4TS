import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "output directory");
        List arguments = new ArrayList();
        arguments.add("input.txt");
        arguments.add("output");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);

        commandLine.addOption(option);
        commandLine.addValue(option, "testOutput");

        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertEquals(option, commandLine.getOption("output"));
        Assert.assertEquals("testOutput", commandLine.getValues(option, null).get(0));

        Option option2 = new Option("i", "input", true, "input file");
        commandLine.addOption(option2);
        commandLine.addValue(option2, "input.txt");

        Assert.assertTrue(commandLine.hasOption(option2));
        Assert.assertEquals(option2, commandLine.getOption("input"));
        Assert.assertEquals("input.txt", commandLine.getValues(option2, null).get(0));

        Option propertyOption = new Option("p", "property", true, "property value");
        commandLine.addProperty(propertyOption, "myProperty", "testValue");

        Assert.assertEquals("testValue", commandLine.getProperty("property"));

        Option propertyOption2 = new Option("p2", "property2", true, "property value 2");
        commandLine.addProperty(propertyOption2, "myProperty2", "testValue2");

        Assert.assertEquals("testValue", commandLine.getProperty("property", "defaultValue"));
        Assert.assertEquals("testValue2", commandLine.getProperty("property2", "defaultValue2"));

        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertEquals(3, commandLine.getOptionTriggers().size());

        Assert.assertFalse(commandLine.hasOption(new Option("notAnOption", true, false, "not an option")));
        Assert.assertNull(commandLine.getOption("notAnOption"));
        Assert.assertEquals(0, commandLine.getValues(new Option("notAnOption"), null).size());

        Assert.assertEquals("input.txt output testOutput", commandLine.toString());
    }
}