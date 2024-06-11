package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionContext;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test case for GroupImpl
 */
public class GroupImplTest {

    @Test
    public void testGroupWithOptions() throws ParseException {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("an option")
                .create());

        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("another option")
                .create());

        final String[] args = {"--option1", "--option2"};

        final OptionContext context = new OptionContext();
        context.setInterspersed(true);

        final CommandLine parsed = new CommandLineParser(context).parse(new DefaultParser(), args);

        final Group group = new GroupImpl(options, "group", null, 1, 2, false);
        group.validate(parsed);
        group.process(parsed, parsed.iterator());

        assertTrue(parsed.hasOption("option1"));
        assertTrue(parsed.hasOption("option2"));
        assertEquals(2, parsed.getNumOptions());

        group.appendUsage(new StringBuffer(), null, null);
        System.out.println(parsed.getUsage());
    }

    @Test
    public void testGroupWithArguments() throws ParseException {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("group")
                .withDescription("a group")
                .create());

        final Option argOption = OptionBuilder.withLongOpt("arg")
                .isRequired(true)
                .withDescription("an argument")
                .createArg();

        options.add(argOption);

        final String[] args = {"--group=value1", "--arg=value"};

        final OptionContext context = new OptionContext();

        final CommandLine parsed = new CommandLineParser(context).parse(new DefaultParser(), args);

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.validate(parsed);
        group.process(parsed, parsed.iterator());

        assertTrue(parsed.hasOption("group"));
        assertEquals("value1", parsed.getOptionValue("group"));
        assertTrue(parsed.hasOption("arg"));
        assertEquals("value", parsed.getOptionValue("arg"));

        group.appendUsage(new StringBuffer(), null, null);
        System.out.println(parsed.getUsage());
    }

    @Test
    public void testGroupWithMinMax() throws ParseException {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option")
                .withDescription("an option")
                .create());

        final String[] args = {"--option", "--option", "--option", "--option"};

        final OptionContext context = new OptionContext();

        try {
            new CommandLineParser(context).parse(new DefaultParser(), args);
            fail();
        } catch (final ParseException e) {
            // expected
        }

        final Group group = new GroupImpl(options, "group", null, 4, 10, false);
        final CommandLine parsed = new CommandLineParser(context).parse(new DefaultParser(), args);

        try {
            group.validate(parsed);
            fail();
        } catch (final OptionException e) {
            // expected
        }

        group.appendUsage(new StringBuffer(), null, null);
        System.out.println(parsed.getUsage());

        final String[] args2 = {"--option", "--option", "--option", "--option", "--option"};

        parsed = new CommandLineParser(context).parse(new DefaultParser(), args2);

        group.validate(parsed);
        group.process(parsed, parsed.iterator());

        assertTrue(parsed.hasOption("option"));
        assertEquals(5, parsed.getNumOptions());

        group.appendUsage(new StringBuffer(), null, null);
        System.out.println(parsed.getUsage());
    }

    @Test
    public void testGroupWithPrefixes() throws ParseException {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("prefix-option1")
                .withDescription("an option with prefix")
                .create());
        options.add(OptionBuilder.withLongOpt("prefix-option2")
                .withDescription("another option with prefix")
                .create());

        final String[] args = {"--prefix=prefix", "--prefix-option1", "--prefix-option2"};

        final OptionContext context = new OptionContext();

        final CommandLine parsed = new CommandLineParser(context).parse(new DefaultParser(), args);

        final Group group = new GroupImpl(options, "group", null, 1, 2, false);
        group.validate(parsed);
        group.process(parsed, parsed.iterator());

        assertTrue(parsed.hasOption("prefix-option1"));
        assertTrue(parsed.hasOption("prefix-option2"));
        assertEquals(2, parsed.getNumOptions());

        group.appendUsage(new StringBuffer(), null, null);
        System.out.println(parsed.getUsage());
    }

    @Test
    public void testGroupWithAnonymous() throws ParseException {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option")
                .isRequired(false)
                .withDescription("an option")
                .create());

        final Option argOption = OptionBuilder.withLongOpt("arg")
                .isRequired(true)
                .withDescription("an argument")
                .createArg();
        options.add(argOption);

        final String[] args = {"--option", "--arg=value"};

        final OptionContext context = new OptionContext();

        final CommandLine parsed = new CommandLineParser(context).parse(new DefaultParser(), args);

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.validate(parsed);
        group.process(parsed, parsed.iterator());

        assertTrue(parsed.hasOption("option"));
        assertNull(parsed.getOptionValue("option"));
        assertTrue(parsed.hasOption("arg"));
        assertEquals("value", parsed.getOptionValue("arg"));

        group.appendUsage(new StringBuffer(), null, null);
        System.out.println(parsed.getUsage());
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMissingOption() throws ParseException {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option")
                .isRequired(false)
                .withDescription("an option")
                .create());

        final String[] args = {"--arg=value"};

        final OptionContext context = new OptionContext();

        new CommandLineParser(context).parse(new DefaultParser(), args);
    }

    @Test(expected = OptionException.class)
    public void testGroupWithTooManyOptions() throws ParseException {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option")
                .isRequired(false)
                .withDescription("an option")
                .create());

        final String[] args = {"--option", "--option", "--option", "--option", "--option"};

        final OptionContext context = new OptionContext();

        new CommandLineParser(context).parse(new DefaultParser(), args);
    }

    @Test(expected = OptionException.class)
    public void testGroupWithInvalidOption() throws ParseException {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option")
                .isRequired(false)
                .withDescription("an option")
                .create());

        final String[] args = {"--invalid-option"};

        final OptionContext context = new OptionContext();

        new CommandLineParser(context).parse(new DefaultParser(), args);
    }

    @Test
    public void testHelp() throws ParseException {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("an option")
                .create());

        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("another option")
                .create());

        final String[] args = {};

        final OptionContext context = new OptionContext();
        context.setHelpOption(OptionBuilder.withLongOpt("help")
                .hasArg(false)
                .isRequired(false)
                .create("h"));
        context.setHelpFormatter(new HelpFormatter());

        final CommandLine parsed = new CommandLineParser(context).parse(new DefaultParser(), args);

        parsed.printHelp("MyApp", new PrintWriter(System.out));
    }
}