import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("optionA")
                .longOpt("longOptionA")
                .desc("This is an option A")
                .build(), new ArrayList<>());

        cli.addOption(new Option.Builder("optionB")
                .longOpt("longOptionB")
                .desc("This is an option B")
                .build());

        List options = cli.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(new Option("optionA", "longOptionA", false, "This is an option A")));
        Assert.assertTrue(options.contains(new Option("optionB", "longOptionB", false, "This is an option B")));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .desc("An option with a value")
                .build(), new ArrayList<>());

        cli.addOption(new Option.Builder("option")
                .longOpt("option")
                .valueSeparator(' ')
                .desc("An option with a value")
                .build());

        cli.addValue(cli.getOption("option"), "value1");
        cli.addValue(cli.getOption("option"), "value2");

        List values = cli.getValues(cli.getOption("option"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .desc("An option with a switch")
                .build(), new ArrayList<>());

        cli.addSwitch(cli.getOption("option"), false);
        boolean switchValue = cli.getSwitch(cli.getOption("option"), null);
        Assert.assertFalse(switchValue);

        cli.addSwitch(cli.getOption("option"), true);
        switchValue = cli.getSwitch(cli.getOption("option"), null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .desc("An option")
                .build(), new ArrayList<>());

        cli.addOption(new Option.Builder("option")
                .longOpt("option")
                .desc("An option")
                .build());

        boolean hasOption = cli.hasOption(cli.getOption("option"));
        Assert.assertTrue(hasOption);

        Option notExistingOption = new Option.Builder("notExistingOption")
                .longOpt("notExistingOption")
                .desc("Not existing option")
                .build();
        hasOption = cli.hasOption(notExistingOption);
        Assert.assertFalse(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .desc("An option")
                .build(), new ArrayList<>());

        cli.addOption(new Option.Builder("option")
                .longOpt("option")
                .desc("An option")
                .build());

        Option option = cli.getOption("option");
        Assert.assertEquals("option", option.getLongOpt());
        Assert.assertEquals("An option", option.getDescription());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .valueSeparator(' ')
                .desc("An option with a value")
                .build(), new ArrayList<>());

        cli.addOption(new Option.Builder("option")
                .longOpt("option")
                .valueSeparator(' ')
                .desc("An option with a value")
                .build());

        cli.addValue(cli.getOption("option"), "value1");
        cli.addValue(cli.getOption("option"), "value2");

        List values = cli.getValues(cli.getOption("option"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .valueSeparator(' ')
                .desc("An option with a value")
                .build(), new ArrayList<>());

        cli.addOption(new Option.Builder("option")
                .longOpt("option")
                .valueSeparator(' ')
                .desc("An option with a value")
                .build());

        cli.addValue(cli.getOption("option"), "value1");
        cli.addValue(cli.getOption("option"), "value2");

        List values = cli.getUndefaultedValues(cli.getOption("option"));
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .desc("An option with a switch")
                .build(), new ArrayList<>());

        cli.addSwitch(cli.getOption("option"), false);

        Boolean switchValue = cli.getSwitch(cli.getOption("option"), null);
        Assert.assertFalse(switchValue);

        cli.addSwitch(cli.getOption("option"), true);
        switchValue = cli.getSwitch(cli.getOption("option"), null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .desc("An option with a property")
                .build(), new ArrayList<>());

        cli.addProperty(cli.getOption("option"), "property", "value");

        String propertyValue = cli.getProperty(cli.getOption("option"), "property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .desc("An option with a property")
                .build(), new ArrayList<>());

        cli.addProperty(cli.getOption("option"), "property", "value");

        Properties properties = cli.getProperties(cli.getOption("option"));
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("property", properties.keySet().iterator().next());
        Assert.assertEquals("value", properties.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .desc("An option")
                .build(), new ArrayList<>());

        String trigger = "option";
        boolean isOption = cli.looksLikeOption(trigger);
        Assert.assertTrue(isOption);

        trigger = "arg";
        isOption = cli.looksLikeOption(trigger);
        Assert.assertFalse(isOption);

        Option option = new Option.Builder("option")
                .longOpt("option")
                .desc("An option")
                .build();
        trigger = option.getLongOpt();
        isOption = cli.looksLikeOption(trigger);
        Assert.assertTrue(isOption);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .desc("An option")
                .build(), new ArrayList<>());

        cli.addOption(new Option.Builder("option")
                .longOpt("option")
                .desc("An option")
                .build());

        cli.addValue(cli.getOption("option"), "value1");
        cli.addValue(cli.getOption("option"), "value2");

        String cliString = cli.toString();
        String expectedCliString = "option value1 value2";
        Assert.assertEquals(expectedCliString, cliString);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadySet() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option.Builder("option")
                .longOpt("option")
                .desc("An option with a switch")
                .build(), new ArrayList<>());

        cli.addSwitch(cli.getOption("option"), true);
        cli.addSwitch(cli.getOption("option"), true);
    }
}