package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option option = patternBuilder.withPattern("a@[abc]") // optional flag 'a' with validator for string 'abc'
                .create();

        assertNotNull(option);
        assertEquals("a", option.getShortName());

        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertEquals("abc", argument.getName());

        option = patternBuilder.reset() // reset builder
                .withPattern("b+:") // optional flag 'b' with argument name ':' and validator for number with minimum 1
                .create();

        assertNotNull(option);
        assertEquals("b", option.getShortName());

        argument = option.getArgument();
        assertNotNull(argument);
        assertEquals("", argument.getName());

        option = patternBuilder.reset() // reset builder
                .withPattern("c-%") // optional flag 'c' with argument name '-' and validator for file that exists
                .create();

        assertNotNull(option);
        assertEquals("c", option.getShortName());

        argument = option.getArgument();
        assertNotNull(argument);
        assertEquals("", argument.getName());

        option = patternBuilder.reset() // reset builder
                .withPattern("d/") // optional flag 'd' with validator for url
                .create();

        assertNotNull(option);
        assertEquals("d", option.getShortName());

        argument = option.getArgument();
        assertNotNull(argument);
        assertEquals("", argument.getName());

        option = patternBuilder.reset() // reset builder
                .withPattern("e@[Ljava/lang/String;]") // optional flag 'e' with validator for class of String.class
                .create();

        assertNotNull(option);
        assertEquals("e", option.getShortName());

        argument = option.getArgument();
        assertNotNull(argument);
        assertEquals("java.lang.String", argument.getParameterType().getName());

        option = patternBuilder.reset() // reset builder
                .withPattern("f:<") // optional flag 'f' with validator for file that exists and is a directory
                .create();

        assertNotNull(option);
        assertEquals("f", option.getShortName());

        argument = option.getArgument();
        assertNotNull(argument);
        assertEquals("", argument.getName());

        option = patternBuilder.reset() // reset builder
                .withPattern("g:/") // optional flag 'g' with validator for url
                .create();

        assertNotNull(option);
        assertEquals("g", option.getShortName());

        argument = option.getArgument();
        assertNotNull(argument);
        assertEquals("", argument.getName());

        option = patternBuilder.reset() // reset builder
                .withPattern("h%") // optional flag 'h' with validator for number
                .create();

        assertNotNull(option);
        assertEquals("h", option.getShortName());

        argument = option.getArgument();
        assertNotNull(argument);
        assertEquals("", argument.getName());

        option = patternBuilder.create(); // create new option without pattern
        assertNotNull(option);
        assertEquals(0, option.getOptions().size());

        option = patternBuilder.create(); // create new option without pattern
        assertNotNull(option);
        assertEquals(0, option.getArguments().size());

        option = patternBuilder.reset() // reset builder
                .withPattern("i*") // optional flag 'i' with validator for file that can be empty
                .create();

        assertNotNull(option);
        assertEquals("i", option.getShortName());

        argument = option.getArgument();
        assertNotNull(argument);
        assertEquals("", argument.getName());

        option = patternBuilder.reset() // reset builder
                .withPattern("j") // optional flag 'j' without argument
                .create();

        assertNotNull(option);
        assertEquals("j", option.getShortName());
        assertEquals(0, option.getArguments().size());

        option = patternBuilder.create(); // create new option without pattern
        assertNotNull(option);
        assertEquals(0, option.getOptions().size());
        assertEquals(0, option.getArguments().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternBuilder() {
        PatternBuilder patternBuilder = new PatternBuilder();

        patternBuilder.withPattern("k") // mandatory flag 'k' without argument
                .create();
    }
}