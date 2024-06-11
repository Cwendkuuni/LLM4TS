package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.builder.PatternBuilder;

/**
 * Test class for PatternBuilder.
 */
public class PatternBuilderTest {

    @org.junit.Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNull(option);

        patternBuilder.reset();
        patternBuilder.createOption('a', true, 'a');
        Option option1 = patternBuilder.create();
        assertEquals("--a", option1.getLongOpt());
        assertEquals('a', option1.getOpt());
        assertFalse(option1.hasArg());
        assertTrue(option1.isRequired());

        patternBuilder.reset();
        patternBuilder.createOption('b', false, 'b');
        Option option2 = patternBuilder.create();
        assertEquals("--b", option2.getLongOpt());
        assertEquals('b', option2.getOpt());
        assertFalse(option2.hasArg());
        assertFalse(option2.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("a:");
        Option option3 = patternBuilder.create();
        assertEquals("--a", option3.getLongOpt());
        assertEquals(null, option3.getOpt());
        assertTrue(option3.hasArg());
        assertFalse(option3.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("@com.example.MyClass:");
        Option option4 = patternBuilder.create();
        assertEquals("--my-class", option4.getLongOpt());
        assertEquals(null, option4.getOpt());
        assertTrue(option4.hasArg());
        assertTrue(option4.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("%d");
        Option option5 = patternBuilder.create();
        assertEquals("--", option5.getLongOpt());
        assertEquals('%', option5.getOpt());
        assertTrue(option5.hasArg());
        assertFalse(option5.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("/foo");
        Option option6 = patternBuilder.create();
        assertEquals("--foo", option6.getLongOpt());
        assertEquals('/', option6.getOpt());
        assertTrue(option6.hasArg());
        assertFalse(option6.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("#%d");
        Option option7 = patternBuilder.create();
        assertEquals("--date", option7.getLongOpt());
        assertEquals(null, option7.getOpt());
        assertTrue(option7.hasArg());
        assertFalse(option7.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("<foo>");
        Option option8 = patternBuilder.create();
        assertEquals("--foo", option8.getLongOpt());
        assertEquals(null, option8.getOpt());
        assertTrue(option8.hasArg());
        assertTrue(option8.isRequired());
    }

    @org.junit.Test
    public void testCreateWithMultipleOptions() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        patternBuilder.withPattern("a:,b:,c:@com.example.MyClass:");

        List<Option> options = new ArrayList<>();
        Option option1 = patternBuilder.create();
        options.add(option1);
        Option option2 = patternBuilder.create();
        options.add(option2);
        Option option3 = patternBuilder.create();
        options.add(option3);

        Set<String> longOptNames = new HashSet<>();
        for (Option option : options) {
            longOptNames.add(option.getLongOpt());
        }
        List<String> expectedLongOptNames = Arrays.asList("--a", "--b", "--my-class");
        assertEquals(expectedLongOptNames, longOptNames);

        for (Option option : options) {
            assertEquals("--" + option.getLongOpt(), option.getLongOpt());
        }

        for (Option option : options) {
            if ("--a".equals(option.getLongOpt())) {
                Argument argument = option.getArgument();
                assertEquals("a", argument.getOpt());
                assertEquals("", argument.getName());
                assertEquals(1, argument.getMinimum());
                assertEquals(1, argument.getMaximum());
            } else if ("--b".equals(option.getLongOpt())) {
                Argument argument = option.getArgument();
                assertEquals("b", argument.getOpt());
                assertEquals("", argument.getName());
                assertEquals(1, argument.getMinimum());
                assertEquals(1, argument.getMaximum());
            } else if ("--my-class".equals(option.getLongOpt())) {
                Argument argument = option.getArgument();
                assertEquals(null, argument.getOpt());
                assertEquals("myClass", argument.getName());
                assertEquals(1, argument.getMinimum());
                assertEquals(1, argument.getMaximum());
            }
        }
    }

    @org.junit.Test
    public void testCreateWithValidators() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        patternBuilder.withPattern("%d/%m/%Y");

        Option option = patternBuilder.create();
        assertSame(DateValidator.class, option.getValidator().getClass());
        LocalDate localDate = LocalDate.of(2022, 12, 25);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("%d/%m/%Y");
        assertEquals(localDate, option.getValidator().validate(dateFormatter.format(localDate)));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        patternBuilder.withPattern("@com.example.MyClass:@");
    }

}