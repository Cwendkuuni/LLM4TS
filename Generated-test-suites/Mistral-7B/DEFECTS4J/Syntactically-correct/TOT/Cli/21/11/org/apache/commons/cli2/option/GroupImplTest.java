package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroup() throws Exception {
        // create sample options
        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("first option")
                .withRequiredArg()
                .create(String.class);

        final Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("second option")
                .isOptional()
                .create(String.class);

        final Option option3 = OptionBuilder.withLongOpt("option3")
                .withDescription("third option")
                .create(boolean.class);

        final Argument arg = ArgumentBuilder.withLongOpt("argument")
                .withDescription("an argument")
                .create(String.class);

        final List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(arg);

        final GroupImpl group = new GroupImpl(options, "group", "a group", 0, Integer.MAX_VALUE, false);

        // create sample command line
        final CommandLine commandLine = new CommandLine();

        // parse command line
        final DefaultParser parser = new DefaultParser();
        parser.parse(group, commandLine, new String[]{"--option1", "value1", "--option2", "value2"});

        // test properties
        Assert.assertEquals("value1", commandLine.getOptionValue("option1"));
        Assert.assertNull(commandLine.getOptionValue("option2"));
        Assert.assertTrue((Boolean) commandLine.hasOption("option3"));
        Assert.assertNull(commandLine.getOptionValue("option3"));
        Assert.assertNull(commandLine.getArgs());

        // test usage
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", group.getPreferredName(), true);

        // test invalid command line
        parser.parse(group, commandLine, new String[]{"--option1", "value1", "--option1", "value2"});
        Assert.fail("Did not get expected OptionException");
    }
}