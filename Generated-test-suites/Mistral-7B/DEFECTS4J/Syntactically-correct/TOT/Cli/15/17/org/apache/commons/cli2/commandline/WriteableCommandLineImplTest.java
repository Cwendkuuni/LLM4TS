import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option1 = new Option("o", "output", true, "Output file");
        Option option2 = new Option("i", "input", true, "Input file");
        Option option3 = new Option("h", "help", false, "Print help message");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test.java"));
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        commandLine.addValue(option1, "output.txt");
        commandLine.addValue(option2, "input.txt");
        commandLine.addSwitch(option3, true);

        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
        Assert.assertEquals(option3, options.get(2));

        List<Object> values1 = commandLine.getValues(option1, null);
        Assert.assertEquals("output.txt", values1.get(0));

        List<Object> values2 = commandLine.getValues(option2, null);
        Assert.assertEquals("input.txt", values2.get(0));

        Boolean switchValue = commandLine.getSwitch(option3, false);
        Assert.assertTrue(switchValue);

        CommandLine parsedCommandLine = new DefaultParser().parse(new DefaultCommandLineParserConfiguration(), commandLine.toString().split(" "));
        List<Option> parsedOptions = parsedCommandLine.getOptions();
        Assert.assertEquals(3, parsedOptions.size());

        Option parsedOption1 = null;
        for (Option option : parsedOptions) {
            if (option.getLongOpt().equals("output")) {
                parsedOption1 = option;
                break;
            }
        }
        Assert.assertEquals("output.txt", parsedOption1.getValue());

        Option parsedOption2 = null;
        for (Option option : parsedOptions) {
            if (option.getLongOpt().equals("input")) {
                parsedOption2 = option;
                break;
            }
        }
        Assert.assertEquals("input.txt", parsedOption2.getValue());

        Option parsedOption3 = null;
        for (Option option : parsedOptions) {
            if (option.getLongOpt().equals("help")) {
                parsedOption3 = option;
                break;
            }
        }
        Assert.assertNull(parsedOption3.getValue());

        commandLine.clear();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        Assert.assertFalse(commandLine.hasOption(option1));
        commandLine.addValue(option1, "output.txt");
        Assert.assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testWriteableCommandLineImplWithDefaultValues() {
        Option option1 = new Option("o", "output", true, "Output file");
        Option option2 = new Option("i", "input", true, "Input file");
        Option option3 = new Option("h", "help", false, "Print help message");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        commandLine.addSwitch(option3, false);

        List<Object> values1 = commandLine.getValues(option1, Arrays.asList("output.def"));
        Assert.assertNull(values1);

        List<Object> values2 = commandLine.getValues(option2, Arrays.asList("input.def"));
        Assert.assertNull(values2);

        Boolean switchValue = commandLine.getSwitch(option3, false);
        Assert.assertFalse(switchValue);

        commandLine.setDefaultValues(option1, Arrays.asList("output.def"));
        List<Object> values1WithDefault = commandLine.getValues(option1, null);
        Assert.assertEquals("output.def", values1WithDefault.get(0));

        commandLine.setDefaultSwitch(option3, true);
        Boolean switchValueWithDefault = commandLine.getSwitch(option3, false);
        Assert.assertTrue(switchValueWithDefault);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImplWithDuplicateSwitch() {
        Option option1 = new Option("s", "switch", true, "Switch value");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option1, true);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImplWithDuplicateValue() {
        Option option1 = new Option("o", "output", true, "Output file");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addValue(option1, "output.txt");
        commandLine.addValue(option1, "output.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteableCommandLineImplWithInvalidValueType() {
        Option option1 = new Option("o", "output", true, "Output file");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addValue(option1, new Object());
    }
}