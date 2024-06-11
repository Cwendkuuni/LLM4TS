import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // create test data
        Option optionA = new Option("a", "a", false, "Option A");
        Option optionB = new Option("b", "b", true, "Option B");
        Option optionC = new Option("c", "c", true, "Option C");
        Option optionD = new Option("d", "d", false, "Option D", new String[]{"value1", "value2"});
        Option rootOption = new Option("root", "r", true, "Root Option");
        rootOption.addOption(optionA);
        rootOption.addOption(optionB);
        rootOption.addOption(optionC);
        rootOption.addOption(optionD);

        List arguments = new ArrayList();
        arguments.add("--a");
        arguments.add("--b");
        arguments.add("--c");
        arguments.add("--d");
        arguments.add("value3");
        arguments.add("value4");

        // create WriteableCommandLine
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // test getters
        Assert.assertEquals("Option A", commandLine.getOption("a").getLongOpt());
        Assert.assertEquals("Option B", commandLine.getOption("b").getLongOpt());
        Assert.assertEquals("Option C", commandLine.getOption("c").getLongOpt());
        Assert.assertEquals("Option D", commandLine.getOption("d").getLongOpt());

        Assert.assertEquals("Option A", commandLine.getOption("a").getOpt());
        Assert.assertEquals("Option B", commandLine.getOption("b").getOpt());
        Assert.assertEquals("Option C", commandLine.getOption("c").getOpt());
        Assert.assertEquals("Option D", commandLine.getOption("d").getOpt());

        Assert.assertEquals(false, commandLine.getSwitch(optionA));
        Assert.assertEquals(true, commandLine.getSwitch(optionB));
        Assert.assertEquals(true, commandLine.getSwitch(optionC));

        List valuesA = commandLine.getValues(optionD, null);
        Assert.assertEquals(2, valuesA.size());
        Assert.assertEquals("value1", valuesA.get(0));
        Assert.assertEquals("value2", valuesA.get(1));

        Assert.assertEquals(Collections.singletonList("value3"), commandLine.getValues(optionD, Collections.singletonList("value3")));
        Assert.assertEquals(Collections.singletonList("value4"), commandLine.getValues(optionD, Collections.singletonList("value4")));
        Assert.assertEquals(Collections.emptyList(), commandLine.getValues(optionD, null));

        // test toString
        Assert.assertEquals("--a --b --c --d value3 value4", commandLine.toString());

        // test getters returns correct options
        Assert.assertEquals(optionA, commandLine.getOption("a"));
        Assert.assertEquals(optionB, commandLine.getOption("b"));
        Assert.assertEquals(optionC, commandLine.getOption("c"));
        Assert.assertEquals(optionD, commandLine.getOption("d"));

        // test getOptionTriggers
        Assert.assertEquals(Collections.singleton("a"), commandLine.getOptionTriggers());
        Assert.assertEquals(Collections.singleton("b"), commandLine.getOptionTriggers());
        Assert.assertEquals(Collections.singleton("c"), commandLine.getOptionTriggers());
        Assert.assertEquals(Collections.singleton("d"), commandLine.getOptionTriggers());

        // test getOptions
        Assert.assertEquals(Collections.singleton(optionA), commandLine.getOptions());
        Assert.assertEquals(Collections.singleton(optionB), commandLine.getOptions());
        Assert.assertEquals(Collections.singleton(optionC), commandLine.getOptions());
        Assert.assertEquals(Collections.singleton(optionD), commandLine.getOptions());

        // test getNormalised
        Assert.assertEquals(arguments, commandLine.getNormalised());

        // test hasOption
        Assert.assertTrue(commandLine.hasOption(optionA));
        Assert.assertTrue(commandLine.hasOption(optionB));
        Assert.assertTrue(commandLine.hasOption(optionC));
        Assert.assertTrue(commandLine.hasOption(optionD));

        // test getProperty
        Assert.assertEquals("value1", commandLine.getProperty("d", null));
        Assert.assertEquals("value2", commandLine.getProperty("d", "default"));
        Assert.assertEquals("default", commandLine.getProperty("d", "default", "default"));

        // test addProperty
        commandLine.addProperty(optionD, "prop", "value5");
        Assert.assertEquals("value5", commandLine.getProperty("d", "prop"));

        // test getProperties
        Set properties = commandLine.getProperties(optionD);
        Assert.assertEquals(Collections.singleton("prop"), properties);
        Assert.assertEquals(1, commandLine.getProperties().size());

        // test addProperty without option
        commandLine.addProperty("prop", "value6");
        Set properties2 = commandLine.getProperties();
        Assert.assertEquals(Collections.singleton("prop"), properties2);
        Assert.assertEquals(1, properties2.size());
        Assert.assertEquals("value6", commandLine.getProperty("prop"));

        // test setDefaultValues
        List defaults = new ArrayList();
        defaults.add("value5");
        defaults.add("value6");
        commandLine.setDefaultValues(optionD, defaults);
        Assert.assertEquals(defaults, commandLine.getValues(optionD, null));

        // test setDefaultSwitch
        commandLine.setDefaultSwitch(optionB, false);
        Assert.assertFalse(commandLine.getSwitch(optionB));
    }
}