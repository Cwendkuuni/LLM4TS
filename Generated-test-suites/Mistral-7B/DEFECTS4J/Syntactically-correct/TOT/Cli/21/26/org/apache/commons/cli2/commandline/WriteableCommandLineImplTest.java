package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testBasicUsage() {
        Option option = new Option("o", "output", true, "The output file path.");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.singletonList("input.txt"));

        cmd.addOption(option);
        cmd.addValue(option, "output.txt");

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertEquals(1, cmd.getOptions().size());
        Assert.assertEquals("o", cmd.getOption(0).getLongOpt());
        Assert.assertEquals("output", cmd.getOption(0).getOptString());
        Assert.assertEquals(true, cmd.hasOption(option));
        Assert.assertEquals("The output file path.", option.getDescription());
        Assert.assertEquals("output", option.getShortName());

        List values = cmd.getValues(option, Collections.emptyList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("output.txt", values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadySet() {
        Option option = new Option("d", "debug", false, "Enable debug mode.");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option[]{}, Collections.emptyList());
        cmd.addOption(option);
        cmd.addSwitch(option, false);
        cmd.addSwitch(option, true);
    }

    @Test
    public void testPropertyOption() {
        Option option = new Option("p", "prefix", true, "The prefix to use.");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.singletonList("--prefix"));

        cmd.addOption(option);
        cmd.addProperty(option, "prefixes", "myPrefix");

        Assert.assertEquals("myPrefix", cmd.getProperty("prefix"));
        Assert.assertEquals(1, cmd.getOptions().size());
        Assert.assertEquals("p", cmd.getOption(0).getLongOpt());
        Assert.assertEquals("prefix", cmd.getOption(0).getOptString());
        Assert.assertEquals(true, cmd.hasOption(option));
        Assert.assertEquals("The prefix to use.", option.getDescription());
        Assert.assertEquals("prefix", option.getShortName());

        Set properties = cmd.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("prefixes", properties.iterator().next());
    }

    @Test
    public void testErrorHandling() {
        Option option = new Option("e", "error", true, "The error message.");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.singletonList("--error"));

        cmd.addOption(option);
        try {
            cmd.addValue(option, null);
        } catch (IllegalArgumentException iae) {
            // expected
        }

        Assert.assertEquals(1, cmd.getOptions().size());
        Assert.assertEquals("e", cmd.getOption(0).getLongOpt());
        Assert.assertEquals("error", cmd.getOption(0).getOptString());
        Assert.assertEquals(true, cmd.hasOption(option));
        Assert.assertEquals("The error message.", option.getDescription());
        Assert.assertEquals("error", option.getShortName());

        List values = cmd.getValues(option, Collections.emptyList());
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testMultipleValues() {
        Option option = new Option("v", "verbose", true, "Specify verbose mode.");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.singletonList("--verbose", "--verbose2"));

        cmd.addOption(option);
        cmd.addValue(option, "verbose1");
        cmd.addValue(option, "verbose2");

        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertEquals("v", cmd.getOption(0).getLongOpt());
        Assert.assertEquals("verbose", cmd.getOption(0).getOptString());
        Assert.assertEquals(true, cmd.hasOption(option));
        Assert.assertEquals("Specify verbose mode.", option.getDescription());
        Assert.assertEquals("verbose", option.getShortName());

        List values = cmd.getValues(option, Collections.emptyList());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("verbose1", values.get(0));
        Assert.assertEquals("verbose2", values.get(1));
    }

    @Test
    public void testSwitch() {
        Option option = new Option("d", "debug", false, "Enable debug mode.");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);
        cmd.addSwitch(option, true);

        Assert.assertFalse(cmd.getSwitch(option, false));
        Assert.assertTrue(cmd.getSwitch(option, true));
    }

    @Test
    public void testDefaultSwitch() {
        Option option = new Option("d", "debug", false, "Enable debug mode.");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultSwitch(option, true);

        Assert.assertTrue(cmd.getSwitch(option, null));
    }

    @Test
    public void testDefaultValues() {
        Option option = new Option("o", "output", true, "The output file path.");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);
        cmd.setDefaultValues(option, Collections.singletonList("output.txt"));

        Assert.assertEquals(1, cmd.getOptions().size());
        Assert.assertEquals("o", cmd.getOption(0).getLongOpt());
        Assert.assertEquals("output", cmd.getOption(0).getOptString());
        Assert.assertEquals(true, cmd.hasOption(option));
        Assert.assertEquals("The output file path.", option.getDescription());
        Assert.assertEquals("output", option.getShortName());

        List values = cmd.getValues(option, Collections.emptyList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("output.txt", values.get(0));
    }

    @Test
    public void testNestedOptions() {
        Option rootOption = new Option(null, "root", false, "Root option.");
        Option childOption = new Option("c", "child", true, "Child option.");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmd.addOption(rootOption);
        cmd.addOption(childOption);
        cmd.addOption(rootOption);
        cmd.addOption(childOption);

        cmd.addValue(childOption, "child1");
        cmd.addValue(childOption, "child2");

        List rootOptions = cmd.getOptions();
        Assert.assertEquals(2, rootOptions.size());

        Option rootOption0 = (Option) rootOptions.get(0);
        Option rootOption1 = (Option) rootOptions.get(1);

        Assert.assertEquals(1, rootOption0.getOptions().size());
        Assert.assertEquals(1, rootOption1.getOptions().size());

        Option childOption0 = (Option) rootOption0.getOptions().get(0);
        Option childOption1 = (Option) rootOption1.getOptions().get(0);

        Assert.assertEquals("c", childOption0.getLongOpt());
        Assert.assertEquals("child", childOption0.getOptString());
        Assert.assertEquals("Child option.", childOption0.getDescription());
        Assert.assertEquals(true, childOption0.hasArg());
        Assert.assertEquals(true, childOption0.isRequired());
        Assert.assertEquals("child1", childOption0.getValue());
        Assert.assertEquals("child2", childOption1.getValue());
    }

    @Test
    public void testParseNormalised() {
        Option option = new Option("o", "output", true, "The output file path.");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.singletonList("--output", "--output2"));

        cmd.addOption(option);
        cmd.addValue(option, "output1");
        cmd.addValue(option, "output2");

        List args = cmd.getNormalised();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("--output", args.get(0).toString());
        Assert.assertEquals("--output2", args.get(1).toString());
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "The output file path.");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.singletonList("--output", "--output2"));

        cmd.addOption(option);
        cmd.addValue(option, "output1");
        cmd.addValue(option, "output2");

        Assert.assertEquals("--output output1 --output output2", cmd.toString());
    }
}