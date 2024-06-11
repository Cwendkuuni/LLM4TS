import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option optionA = new Option("a", "annotation", true, "An annotation example")
                .setRequired(false)
                .setLongOpt("annotation-long")
                .setValueSeparator('=');

        Option optionB = new Option("b", "boolean", false, "A boolean option")
                .setLongOpt("boolean-long");

        Option rootOption = new Option("root", "root", true, "The root option")
                .setRequired(true)
                .setArgs(1)
                .addOption(optionA)
                .addOption(optionB);

        List arguments = new ArrayList() {{
            add("test");
            add("--annotation=valueA");
            add("--boolean");
        }};

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // When
        List options = commandLine.getOptions();
        Set optionTriggers = commandLine.getOptionTriggers();
        Map properties = commandLine.getProperties();
        Boolean switchValue = commandLine.getSwitch(optionB, Boolean.FALSE);
        List values = commandLine.getValues(optionA, null);

        // Then
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(optionTriggers.contains("a"));
        Assert.assertTrue(optionTriggers.contains("annotation-long"));
        Assert.assertTrue(optionTriggers.contains("annotation"));
        Assert.assertTrue(optionTriggers.contains("boolean"));
        Assert.assertTrue(optionTriggers.contains("boolean-long"));
        Assert.assertEquals("valueA", values.get(0));
        Assert.assertFalse(switchValue);
        Assert.assertEquals(Collections.EMPTY_LIST, commandLine.getValues(optionB, null));

        // Test invalid options
        Option invalidOption = new Option("invalid", "invalid", false, "An invalid option")
                .setLongOpt("invalid-long");

        List invalidArguments = new ArrayList() {{
            add("--invalid");
        }};

        WriteableCommandLineImpl invalidCommandLine = new WriteableCommandLineImpl(rootOption, invalidArguments);

        Assert.assertFalse(invalidCommandLine.hasOption(invalidOption));
    }
}