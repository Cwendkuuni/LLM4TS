package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2. validation.ClassValidator;
import org.apache.commons.cli2. validation.DateValidator;
import org.apache.commons.cli2. validation.FileValidator;
import org.apache.commons.cli2. validation.NumberValidator;
import org.apache.commons.cli2. validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

 @Test
 public void testPatternBuilder() {
 // Test creating a new PatternBuilder
 PatternBuilder patternBuilder = new PatternBuilder();

 // Test creating an Option with no pattern
 Option option1 = patternBuilder.create();
 assertNotNull(option1);
 assertEquals("", option1.getLongOpt());
 assertNull(option1.getArgument());
 assertEquals(0, option1.getOptions().size());

 // Test creating an Option with a simple pattern
 patternBuilder.withPattern("a:");
 Option option2 = patternBuilder.create();
 assertNotNull(option2);
 assertEquals("a", option2.getShortName());
 assertNull(option2.getLongOpt());
 assertNull(option2.getArgument());
 assertEquals(0, option2.getOptions().size());

 // Test creating an Option with a required argument
 patternBuilder.reset();
 patternBuilder.withPattern("a:s");
 Option option3 = patternBuilder.create();
 assertNotNull(option3);
 assertEquals("a", option3.getShortName());
 assertEquals("s", option3.getLongOpt());
 assertInstanceOf(Argument.class, option3.getArgument());

 // Test creating an Option with a required argument and multiple instances
 patternBuilder.reset();
 patternBuilder.withPattern("a:s");
 patternBuilder.withPattern("b:s");
 Option option4 = patternBuilder.create();
 assertNotNull(option4);
 assertEquals("a or b", option4.getLongOpt());
 assertInstanceOf(Argument.class, option4.getArgument());

 // Test creating an Option with a required argument and multiple instances, with an argument name
 patternBuilder.reset();
 patternBuilder.withPattern("g:ss");
 Option option5 = patternBuilder.create();
 assertNotNull(option5);
 assertEquals("g", option5.getShortName());
 assertEquals("ss", option5.getLongOpt());
 Argument argument = (Argument) option5.getArgument();
 assertNotNull(argument);
 assertEquals("ss", argument.getName());

 // Test creating an Option with a optional argument
 patternBuilder.reset();
 patternBuilder.withPattern("a:s");
 patternBuilder.withPattern("b:.");
 Option option6 = patternBuilder.create();
 assertNotNull(option6);
 assertEquals("a or b", option6.getLongOpt());
 Argument argument1 = (Argument) option6.getArgument();
 assertNotNull(argument1);
 assertNull(argument1.getName());

 // Test creating an Option with multiple optional arguments
 patternBuilder.reset();
 patternBuilder.withPattern("a:s");
 patternBuilder.withPattern("b:s");
 patternBuilder.withPattern("c:s");
 Option option7 = patternBuilder.create();
 assertNotNull(option7);
 assertEquals("a or b or c", option7.getLongOpt());
 Argument argument2 = (Argument) option7.getArgument();
 assertNotNull(argument2);
 assertNull(argument2.getName());

 // Test creating an Option with an optional argument with name
 patternBuilder.reset();
 patternBuilder.withPattern("g:ss");
 Option option8 = patternBuilder.create();
 assertNotNull(option8);
 assertEquals("g", option8.getShortName());
 assertEquals("ss", option8.getLongOpt());
 Argument argument3 = (Argument) option8.getArgument();
 assertNotNull(argument3);
 assertEquals("ss", argument3.getName());

 // Test creating an Option with an optional argument with name and multiple occurrences
 patternBuilder.reset();
 patternBuilder.withPattern("g:ss");
 patternBuilder.withPattern("g:ss");
 Option option9 = patternBuilder.create();
 assertNotNull(option9);
 assertEquals("g", option9.getShortName());
 assertEquals("ss or ss", option9.getLongOpt());
 Argument argument4 = (Argument) option9.getArgument();
 assertNotNull(argument4);
 assertNull(argument4.getName());

 // Test creating an Option with a validator
 patternBuilder.reset();
 patternBuilder.withPattern("n:");
 Validator numberValidator = NumberValidator.getNumberInstance();
 patternBuilder.withValidator(numberValidator);
 Option option10 = patternBuilder.create();
 assertNotNull(option10);
 assertEquals("n", option10.getShortName());
 assertEquals("n", option10.getLongOpt());
 assertNull(option10.getArgument());
 assertSame(numberValidator, option10.getValidator());

 // Test creating an Option with multiple validators
 patternBuilder.reset();
 patternBuilder.withPattern("n:");
 Validator numberValidator1 = NumberValidator.getNumberInstance();
 Validator numberValidator2 = NumberValidator.getNumberInstance();
 patternBuilder.withValidator(numberValidator1, numberValidator2);
 Option option11 = patternBuilder.create();
 assertNotNull(option11);
 assertEquals("n", option11.getShortName());
 assertEquals("n", option11.getLongOpt());
 assertNull(option11.getArgument());
 assertTrue(option11.getValidators().containsAll(Arrays.asList(numberValidator1, numberValidator2)));
 }
}