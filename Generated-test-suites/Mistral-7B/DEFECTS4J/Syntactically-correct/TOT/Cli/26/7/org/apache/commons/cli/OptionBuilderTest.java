package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest {

    @Test
    public void testBasicOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .hasArg()
                .create('o');

        assertEquals("option1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertEquals("an option", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());
    }

    @Test
    public void testRequiredOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .hasArg()
                .isRequired()
                .create('o');

        assertEquals("option1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertEquals("an option", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());
    }

    @Test
    public void testOptionalArgumentOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .hasOptionalArg()
                .hasArg("arg1")
                .create('o');

        assertEquals("option1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertEquals("an option", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());

        Option parsedOption = new Option("option1", "an option", true, "arg1");
        assertEquals(parsedOption, option);

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-o", "value1"};
        Option parsed = parser.parse(args)[0];
        assertEquals(option, parsed);
        assertEquals("value1", parsed.getValue());
    }

    @Test
    public void testValueSeparatorOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withValueSeparator(':')
                .hasArg()
                .create('o');

        assertEquals("option1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertEquals("an option", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testUninitializedNumberOfArgumentsOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .create('o');

        assertEquals("option1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertEquals("an option", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgs());
        assertNull(option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLongOptOption() {
        OptionBuilder.create();
    }
}