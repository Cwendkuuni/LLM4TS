package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionContext;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Option optionA;
    private Option optionB;
    private Option optionC;
    private Option optionD;
    private GroupImpl group;

    @Before
    public void setup() {
        optionA = OptionBuilder.withArgName("argA")
                .withDescription("Option A")
                .create();

        optionB = OptionBuilder.withLongOpt("optionB")
                .withDescription("Option B")
                .create();

        optionC = OptionBuilder.withArgName("argC")
                .withDescription("Option C")
                .create();

        optionD = OptionBuilder.withArgName("argD")
                .withDescription("Option D")
                .create();

        group = new GroupImpl(List.of(optionA, optionB, optionC, optionD),
                "MyGroup",
                "This is my group",
                1,
                3,
                true);
    }

    @Test
    public void shouldParseWithMinimumAndMaximumOptionsWhenProvided() {
        final CommandLine cmd = new CommandLine();
        final OptionContext context = new OptionContext();
        context.setAutoHelp(false);

        group.parse(cmd, context);

        assertFalse(cmd.hasOption("optionB"));
        assertFalse(cmd.hasOption("optionC"));
        assertTrue(cmd.hasOption("optionD"));
        assertFalse(cmd.hasOption("optionA"));

        assertEquals(1, cmd.getArgs().size());
        assertEquals("argD", cmd.getArgs().get(0));
    }

    @Test(expected = ParseException.class)
    public void shouldFailIfLessThanMinimumOptionsAreProvided() {
        final CommandLine cmd = new CommandLine();
        final OptionContext context = new OptionContext();
        context.setAutoHelp(false);

        group.parse(cmd, context);

        group.validate(cmd);

        // this statement should throw a ParseException
    }

    @Test(expected = ParseException.class)
    public void shouldFailIfMoreThanMaximumOptionsAreProvided() {
        final CommandLine cmd = new CommandLine();
        final CommandLine parsedCmd = new CommandLine();
        final OptionContext context = new OptionContext();
        context.setAutoHelp(false);

        group.parse(cmd, context);

        parsedCmd.addOption(optionB);
        parsedCmd.addOption(optionC);
        parsedCmd.addOption(optionD);
        parsedCmd.addOption(optionA);

        group.validate(parsedCmd);

        // this statement should throw a ParseException
    }

    @Test
    public void shouldParseWithAnonymousArguments() {
        final CommandLine cmd = new CommandLine();
        final OptionContext context = new OptionContext();
        context.setAutoHelp(false);

        group.parse(cmd, context);

        assertFalse(cmd.hasOption("optionB"));
        assertFalse(cmd.hasOption("optionC"));
        assertTrue(cmd.hasOption("optionD"));
        assertTrue(cmd.getOptionValues("argD").length > 0);
        assertFalse(cmd.hasOption("optionA"));

        assertEquals(1, cmd.getArgs().size());
        assertTrue(cmd.getArg(0).startsWith("-"));
        assertEquals("argA", cmd.getArg(0).substring(1));
    }

    @Test
    public void shouldParseWithMixedOptionsAndArguments() {
        final CommandLine cmd = new CommandLine();
        final OptionContext context = new OptionContext();
        context.setAutoHelp(false);

        group.parse(cmd, context);

        assertFalse(cmd.hasOption("optionB"));
        assertTrue(cmd.hasOption("optionC"));
        assertTrue(cmd.hasOption("optionD"));
        assertTrue(cmd.hasOption("optionA"));

        assertEquals(2, cmd.getArgs().size());
        assertEquals("argC", cmd.getArgs().get(0));
        assertEquals("argA", cmd.getArgs().get(1));

        new HelpFormatter().printHelp("MyClass", group, true);
    }

    @Test
    public void shouldParseAndDisplayHelp() {
        final CommandLine cmd = new CommandLine();
        final OptionContext context = new OptionContext();
        context.setAutoHelp(true);

        group.parse(cmd, context);

        new HelpFormatter().printHelp("MyClass", group, true);
    }
}