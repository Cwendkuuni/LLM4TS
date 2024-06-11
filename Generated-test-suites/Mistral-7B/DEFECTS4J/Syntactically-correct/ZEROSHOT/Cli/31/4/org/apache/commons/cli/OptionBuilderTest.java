package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder
 */
public class OptionBuilderTest {

    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)} .
     */
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withLongOpt("longopt1");
        Option option = optionBuilder.create();
        assertEquals("longopt1", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()} .
     */
    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertEquals(true, option.hasArg());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()} .
     */
    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertEquals(true, option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)} .
     */
    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        assertEquals("argName", option.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()} .
     */
    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs();
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)} .
     */
    @Test
    public void testHasArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        assertEquals(2, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()} .
     */
    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertEquals(true, option.hasOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)} .
     */
    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withDescription("description");
        Option option = optionBuilder.create();
        assertEquals("description", option.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#create()} .
     */
    @Test
    public void testCreate() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertNull(option.getValue());
        assertNull(option.getValueSeparator());
        assertNull(option.getArgs());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Object)} .
     */
    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withType("TestType.class");
        Option option = optionBuilder.create();
        assertEquals("TestType.class", option.getType().toString());
    }
}