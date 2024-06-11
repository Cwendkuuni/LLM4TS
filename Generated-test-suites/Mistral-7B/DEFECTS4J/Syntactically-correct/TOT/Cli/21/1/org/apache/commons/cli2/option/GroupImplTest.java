package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.MissingArgumentException;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.OptionUtils;
import org.apache.commons.cli2.SystemOutStream;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testBasicUsage() {
        final List options = new ArrayList();
        options.add(new OptionBuilder("o1")
                .longOpt("option1")
                .desc("Description for option1")
                .hasArg()
                .build());
        options.add(new OptionBuilder("o2")
                .longOpt("option2")
                .desc("Description for option2")
                .hasArg()
                .build());
        options.add(new OptionBuilder("g")
                .longOpt("group")
                .desc("Description for group")
                .minimum(1)
                .maximum(3)
                .valueSeparator(' ')
                .group(GroupBuilder.create().addOptions(options).build())
                .build());

        final CommandLine cmd = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            cmd.parse("--group option1 arg1 option2 arg2");
            Assert.assertEquals("option1,option2", cmd.getOptionValue("group"));
        } catch (final MissingArgumentException e) {
            // expected when no args are provided for the group
            formatter.printHelp("Test", "1.0", true, "", "", true);
            System.out.println(formatter.getHelpText());
            Assert.fail("MissingArgumentException expected but not thrown");
        } catch (final IllegalArgumentException e) {
            // unexpected exception
            Assert.fail("IllegalArgumentException thrown: " + e.getMessage());
        } catch (final OptionException e) {
            // unexpected exception
            Assert.fail("OptionException thrown: " + e.getMessage());
        } catch (final Exception e) {
            // unexpected exception
            Assert.fail("Exception thrown: " + e.getMessage());
        }

        try {
            cmd.parse("--group option1 arg1 option2 arg3");
            Assert.assertEquals("option1,option2", cmd.getOptionValue("group"));
        } catch (final OptionException e) {
            // expected when the group has more than the maximum allowed options
            Assert.assertEquals(" Too many options for group: maximum is 3", e.getMessage());
        }

        try {
            cmd.parse("--group option1 arg1");
            Assert.assertEquals("option1", cmd.getOptionValue("group"));
        } catch (final OptionException e) {
            // expected when the group has less than the minimum required options
            Assert.assertEquals(" Minimum 1 option(s) for group: option1", e.getMessage());
        }
    }

    @Test
    public void testInvalidUsage() {
        final List options = new ArrayList();
        options.add(new OptionBuilder("o1")
                .longOpt("option1")
                .desc("Description for option1")
                .hasArg()
                .build());
        options.add(new OptionBuilder("o2")
                .longOpt("option2")
                .desc("Description for option2")
                .hasArg()
                .build());
        options.add(new OptionBuilder("g")
                .longOpt("group")
                .desc("Description for group")
                .minimum(1)
                .maximum(3)
                .group(GroupBuilder.create().addOptions(options).build())
                .build());

        final CommandLine cmd = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            cmd.parse("--group option1 arg1");
            Assert.fail("MissingArgumentException expected but not thrown");
        } catch (final MissingArgumentException e) {
            // expected when no value is provided for an option that requires one
            formatter.printHelp("Test", "1.0", true, "", "", true);
            System.out.println(formatter.getHelpText());
        } catch (final IllegalArgumentException e) {
            // unexpected exception
            Assert.fail("IllegalArgumentException thrown: " + e.getMessage());
        } catch (final OptionException e) {
            // unexpected exception
            Assert.fail("OptionException thrown: " + e.getMessage());
        } catch (final Exception e) {
            // unexpected exception
            Assert.fail("Exception thrown: " + e.getMessage());
        }

        try {
            cmd.parse("--group option1 arg1 option2");
            Assert.fail("OptionException expected but not thrown");
        } catch (final OptionException e) {
            // expected when an option is not part of the group
            Assert.assertEquals("Option 'option1' is not part of group: group", e.getMessage());
        }

        try {
            cmd.parse("--group option1 arg1 option2 arg option3");
            Assert.fail("OptionException expected but not thrown");
        } catch (final OptionException e) {
            // expected when an option is not part of the group and has an invalid value
            Assert.assertEquals("Option 'arg' is not part of group: group", e.getMessage());
        }

        try {
            cmd.parse("--group option1 arg1 option2 arg option3 option1");
            Assert.fail("DuplicateOptionException expected but not thrown");
        } catch (final OptionException e) {
            // expected when an option is defined multiple times
            Assert.assertEquals("Duplicate option: option1", e.getMessage());
        }

        try {
            cmd.parse("--group option1 arg1 option2 arg option3 option1 foo");
            Assert.fail("OptionException expected but not thrown");
        } catch (final OptionException e) {
            // expected when an option has an invalid value
            Assert.assertEquals("Invalid value for option 'foo': 'foo' is not a valid value for option: option1", e.getMessage());
        }

        try {
            cmd.parse("--group option1 arg1 option2 arg option3 option1 arg1");
            Assert.fail("DuplicateValueException expected but not thrown");
        } catch (final OptionException e) {
            // expected when an option has the same value multiple times
            Assert.assertEquals("Duplicate value for option 'arg1' : 'arg1' is a duplicate value for option: option1", e.getMessage());
        }

        try {
            cmd.parse("--group option1 arg1 option2 arg option3 option1 --option1 arg2");
            Assert.fail("OptionException expected but not thrown");
        } catch (final OptionException e) {
            // expected when an option is already defined and has a new value
            Assert.assertEquals("Option 'option1' already defined", e.getMessage());
        }

        try {
            cmd.parse("--group option1 arg1 option2 arg option3 option1 --option1 arg2 arg3");
            Assert.fail("DuplicateValueException expected but not thrown");
        } catch (final OptionException e) {
            // expected when an option has the same value multiple times
            Assert.assertEquals("Duplicate value for option 'arg3' : 'arg3' is a duplicate value for option: option1", e.getMessage());
        }

        try {
            cmd.parse("--group option1 arg1 option2 arg option3 option1 --option1 arg2 arg3 arg4");
            Assert.fail("DuplicateValueException expected but not thrown");
        } catch (final OptionException e) {
            // expected when an option has the same value multiple times
            Assert.assertEquals("Duplicate value for option 'arg4' : 'arg4' is a duplicate value for option: option1", e.getMessage());
        }
    }
}