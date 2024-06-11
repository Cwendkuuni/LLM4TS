package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.validator.ClassValidator;
import org.apache.commons.cli2.validator.DateValidator;
import org.apache.commons.cli2.validator.FileValidator;
import org.apache.commons.cli2.validator.NumberValidator;
import org.apache.commons.cli2.validator.UrlValidator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the PatternBuilder class
 */
public class PatternBuilderTest {

    private PatternBuilder builder;

    @Before
    public void setUp() {
        builder = new PatternBuilder();
    }

    /**
     * Tests creating a new Option instance with no pattern set
     */
    @Test
    public void testCreateNoPattern() {
        final Option option = builder.create();
        assertNull(option);
    }

    /**
     * Tests creating a new Option instance with a simple pattern
     */
    @Test
    public void testCreateSimplePattern() {
        builder.withPattern("A:");
        final Option option = builder.create();
        assertSame(Option.Type.ARGUMENT, option.getType());
        assertTrue(option.isRequired());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getArgument());
        assertSame('A', option.getOpt());
    }

    /**
     * Tests creating a new Option instance with a required option pattern
     */
    @Test
    public void testCreateRequiredOptionPattern() {
        builder.withPattern("B!");
        final Option option = builder.create();
        assertSame(Option.Type.VALUE, option.getType());
        assertTrue(option.isRequired());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getArgument());
        assertSame('B', option.getOpt());
    }

    /**
     * Tests creating a new Option instance with a optional option pattern
     */
    @Test
    public void testCreateOptionalOptionPattern() {
        builder.withPattern("C:");
        final Option option = builder.create();
        assertSame(Option.Type.ARGUMENT, option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getArgument());
        assertSame('C', option.getOpt());
    }

    /**
     * Tests creating a new Option instance with a long option pattern
     */
    @Test
    public void testCreateLongOptionPattern() {
        builder.withPattern("D::");
        final Option option = builder.create();
        assertSame(Option.Type.VALUE, option.getType());
        assertFalse(option.isRequired());
        assertTrue(option.hasLongOpt());
        assertSame("d", option.getLongOpt());
        assertNull(option.getOpt());
        assertNull(option.getArgument());
    }

    /**
     * Tests creating a new Option instance with a long option pattern and a value
     */
    @Test
    public void testCreateLongOptionPatternWithValue() {
        builder.withPattern("E::F");
        final Option option = builder.create();
        assertSame(Option.Type.VALUE, option.getType());
        assertFalse(option.isRequired());
        assertTrue(option.hasLongOpt());
        assertSame("e", option.getLongOpt());
        assertSame('F', option.getOpt());
        assertNull(option.getArgument());
    }

    /**
     * Tests creating a new Option instance with a long option pattern and an argument
     */
    @Test
    public void testCreateLongOptionPatternWithArgument() {
        builder.withPattern("G::H*");
        final Option option = builder.create();
        assertSame(Option.Type.ARGUMENT, option.getType());
        assertFalse(option.isRequired());
        assertTrue(option.hasLongOpt());
        assertSame("g", option.getLongOpt());
        assertSame('G', option.getOpt());
        assertSame("h", option.getArgument().getName());
        assertTrue(option.getArgument().isMultiple());
    }

    /**
     * Tests creating a new Option instance with a long option pattern, a value and an argument
     */
    @Test
    public void testCreateLongOptionPatternWithValueAndArgument() {
        builder.withPattern("I::J::K");
        final Option option = builder.create();
        assertSame(Option.Type.VALUE, option.getType());
        assertFalse(option.isRequired());
        assertTrue(option.hasLongOpt());
        assertSame("i", option.getLongOpt());
        assertSame('I', option.getOpt());
        assertNull(option.getArgument());
    }

    /**
     * Tests creating a new Option instance with a pattern containing multiple types
     */
    @Test
    public void testCreatePatternWithMultipleTypes() {
        builder.withPattern("L:@<T:");
        final Option option = builder.create();
        assertSame(Option.Type.ARGUMENT, option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getArgument());
        assertSame('L', option.getOpt());
        final Set validators = option.getValidators();
        assertTrue(validators.contains(new ClassValidator()));
        assertTrue(validators.contains(new FileValidator()));
        assertFalse(validators.contains(NumberValidator.GetNumberInstance()));
        assertFalse(validators.contains(DateValidator.GetDateInstance()));
    }

    /**
     * Tests creating a new Option instance with a pattern that includes multiple options
     */
    @Test
    public void testCreatePatternWithMultipleOptions() {
        builder.withPattern("M:+:N:");
        final Option option = builder.create();
        final Option option1 = ((OptionBuilder)option.getBuilder()).getChildOption("N");
        assertSame(Option.Type.VALUE, option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getArgument());
        assertSame('M', option.getOpt());
        assertSame(Option.Type.VALUE, option1.getType());
        assertFalse(option1.isRequired());
        assertFalse(option1.hasLongOpt());
        assertNull(option1.getLongOpt());
        assertNull(option1.getArgument());
        assertSame('N', option1.getOpt());
    }

    /**
     * Tests creating a new Option instance with a pattern that includes multiple arguments
     */
    @Test
    public void testCreatePatternWithMultipleArguments() {
        builder.withPattern("O:*:P:*");
        final Option option = builder.create();
        final Argument arg1 = ((ArgumentBuilder)option.getArgument()).getArgument();
        final Argument arg2 = ((ArgumentBuilder)option.getArgument()).getArgument2();
        assertSame(Option.Type.ARGUMENT, option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getArgument());
        assertSame('O', option.getOpt());
        assertSame("o", arg1.getName());
        assertSame("", arg1.getDefaultValue());
        assertSame("", arg1.getDescription());
        assertSame("p", arg2.getName());
        assertSame("", arg2.getDefaultValue());
        assertSame("", arg2.getDescription());
        assertSame(1, arg1.getMinimum());
        assertSame(1, arg1.getMaximum());
        assertSame(1, arg2.getMinimum());
        assertSame(1, arg2.getMaximum());
    }

    /**
     * Tests creating a new Option instance with a pattern that includes multiple validators
     */
    @Test
    public void testCreatePatternWithMultipleValidators() {
        builder.withPattern("Q::R:S:T:");
        final Option option = builder.create();
        final Set validators = option.getValidators();
        assertTrue(validators.contains(NumberValidator.GetNumberInstance()));
        assertTrue(validators.contains(DateValidator.GetDateInstance()));
        assertTrue(validators.contains(FileValidator.getInstance()));
        assertTrue(validators.contains(UrlValidator.class));
    }
}