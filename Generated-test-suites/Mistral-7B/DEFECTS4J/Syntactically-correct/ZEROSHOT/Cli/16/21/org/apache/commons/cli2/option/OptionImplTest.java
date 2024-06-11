package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.command.Arg;
import org.apache.commons.cli2.command.Option;
import org.apache.commons.cli2.command.Parser;
import org.apache.commons.cli2.option.OptionImpl;
import org.apache.commons.cli2.option.OptionBuilder;
import org.apache.commons.cli2.strategy.DefaultValueStrategy;

import java.util.Arrays;
import java.util.List;

public class OptionImplTest {

    @org.junit.Before
    public void setup() {
    }

    @org.junit.Test
    public void testOptionImplConstructor() {
        final int id = 1;
        final boolean required = true;
        final OptionImpl option = new OptionImpl(id, required);
        assertEquals(id, option.getId());
        assertTrue(option.isRequired());
    }

    @org.junit.Test
    public void testCanProcess() {
        final String[] args = {"--arg1"};
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final List<Arg> arguments = commandLine.getArgs();

        final Option option = OptionBuilder.withLongOpt("arg1")
                .withDescription("Test argument 1")
                .create();

        assertTrue(option.canProcess(commandLine, arguments.listIterator()));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCanProcessNotFound() {
        final String[] args = {"--test"};
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        final Option option = new OptionImpl(1, true);

        option.checkPrefixes(Arrays.asList("arg"));
        option.canProcess(commandLine, Arrays.asList("test").listIterator());
    }

    @org.junit.Test
    public void testToString() {
        final Option option = OptionBuilder.withLongOpt("arg1")
                .withDescription("Test argument 1")
                .create();

        assertEquals("--arg1, -a, Test argument 1\n", option.toString());
    }

    @org.junit.Test
    public void testEquals() {
        final Option option1 = OptionBuilder.withLongOpt("arg1")
                .withDescription("Test argument 1")
                .create();

        final Option option2 = OptionBuilder.withLongOpt("arg1")
                .withDescription("Test argument 1")
                .create();

        assertTrue(option1.equals(option2));
    }

    @org.junit.Test
    public void testFindOption() {
        final Option option = OptionBuilder.withLongOpt("arg1")
                .withDescription("Test argument 1")
                .create();

        assertTrue(option.equals(option.findOption("arg1")));
        assertNull(option.findOption("arg2"));
    }

    @org.junit.Test
    public void testIsRequired() {
        final Option option = OptionBuilder.withLongOpt("arg1")
                .isRequired(true)
                .withDescription("Test argument 1")
                .create();

        assertTrue(option.isRequired());

        final Option option2 = OptionBuilder.withLongOpt("arg2")
                .withDescription("Test argument 2")
                .create();

        assertFalse(option2.isRequired());
    }

    @org.junit.Test
    public void testDefaults() {
        final Option option = OptionBuilder.withLongOpt("arg1")
                .withDescription("Test argument 1")
                .create();

        option.defaults(new WriteableCommandLine());
    }

    @org.junit.Test
    public void testCheckPrefixes() {
        final Option option = OptionBuilder.withLongOpt("arg1")
                .withDescription("Test argument 1")
                .create();

        option.checkPrefixes(Arrays.asList("arg"));
    }
}