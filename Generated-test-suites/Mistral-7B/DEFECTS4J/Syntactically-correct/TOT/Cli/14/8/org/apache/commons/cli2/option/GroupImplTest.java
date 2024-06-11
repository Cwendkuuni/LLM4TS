package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class GroupImplTest {

    @Test
    public void testGroupWithOptionsAndArguments() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("g").withDescription("a group")
                .createOption());
        options.add(OptionBuilder.withName("h").withDescription("an option")
                .createOption());
        options.add(OptionBuilder.withName("i").withDescription("another option")
                .createOption());
        options.add(OptionBuilder.withName("j").withDescription("an argument")
                .withArgName("ARG")
                .createOption());

        final Group group = new GroupImpl(options, "MyGroup", "A group", 0, Integer.MAX_VALUE);

        final CommandLine cmdLine = new CommandLine("-g h i");
        group.parse(cmdLine);

        assertEquals("h", cmdLine.getOptionValue("h"));
        assertFalse(cmdLine.hasOption("i"));
        assertNull(cmdLine.getOptionValue("j"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyGroup", "", group.getOptionSpecs(), true);
    }

    @Test
    public void testGroupWithMissingOption() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("g").withDescription("a group")
                .createOption());
        options.add(OptionBuilder.withName("h").withDescription("an option")
                .createOption());
        options.add(OptionBuilder.withName("i").withDescription("another option")
                .createOption());

        final Group group = new GroupImpl(options, "MyGroup", "A group", 1, Integer.MAX_VALUE);

        final CommandLine cmdLine = new CommandLine("-g");
        try {
            group.parse(cmdLine);
            assert False : "ParseException expected";
        } catch (final OptionException e) {
            assertEquals("Missing option -h. Required option(s) are: h", e.getMessage());
        }
    }

    @Test
    public void testGroupWithTooManyOptions() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("g").withDescription("a group")
                .createOption());
        options.add(OptionBuilder.withName("h").withDescription("an option")
                .createOption());
        options.add(OptionBuilder.withName("i").withDescription("another option")
                .createOption());

        final Group group = new GroupImpl(options, "MyGroup", "A group", 1, 2);

        final CommandLine cmdLine = new CommandLine("-g h i j");
        try {
            group.parse(cmdLine);
            assert False : "ParseException expected";
        } catch (final OptionException e) {
            assertEquals("Too many options", e.getMessage());
        }
    }

    @Test
    public void testGroupWithMissingArgument() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("g").withDescription("a group")
                .createOption());
        options.add(OptionBuilder.withName("j").withDescription("an argument")
                .withArgName("ARG")
                .createOption());

        final Group group = new GroupImpl(options, "MyGroup", "A group", 0, Integer.MAX_VALUE);

        final CommandLine cmdLine = new CommandLine("-g j 42");
        try {
            group.parse(cmdLine);
            assert False : "ParseException expected";
        } catch (final OptionException e) {
            assertEquals("Option j requires an argument.", e.getMessage());
        }
    }

    @Test
    public void testGroupWithExtraArguments() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("g").withDescription("a group")
                .createOption());
        options.add(OptionBuilder.withName("j").withDescription("an argument")
                .withArgName("ARG")
                .createOption());

        final Group group = new GroupImpl(options, "MyGroup", "A group", 0, Integer.MAX_VALUE);

        final CommandLine cmdLine = new CommandLine("-g j 42 foo");
        try {
            group.parse(cmdLine);
            assert False : "ParseException expected";
        } catch (final OptionException e) {
            assertEquals("Unrecognized option: foo", e.getMessage());
        }
    }

    @Test
    public void testGroupWithMixedOptionsAndArguments() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("g").withDescription("a group")
                .createOption());
        options.add(OptionBuilder.withName("h").withDescription("an option")
                .createOption());
        options.add(OptionBuilder.withName("i").withDescription("another option")
                .createOption());
        options.add(OptionBuilder.withName("j").withDescription("an argument")
                .withArgName("ARG")
                .createOption());

        final Group group = new GroupImpl(options, "MyGroup", "A group", 1, Integer.MAX_VALUE);

        final CommandLine cmdLine = new CommandLine("-g h -j ARG2");
        group.parse(cmdLine);

        assertEquals("h", cmdLine.getOptionValue("h"));
        assertNull(cmdLine.getOptionValue("i"));
        assertEquals("ARG2", cmdLine.getOptionValue("j"));
    }
}