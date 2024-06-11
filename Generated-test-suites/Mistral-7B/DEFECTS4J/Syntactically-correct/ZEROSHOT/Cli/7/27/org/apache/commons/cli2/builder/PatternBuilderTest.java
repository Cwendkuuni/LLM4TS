package org.apache.commons.cli2.builder;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @org.junit.Test
    public void testPatternBuilder() {
        // Test creating a new PatternBuilder
        PatternBuilder pb = new PatternBuilder();

        // Test creating an Option with no pattern
        Option option = pb.create();
        assertNull(option);

        // Test creating an Option with an empty pattern
        pb = new PatternBuilder();
        pb.withPattern("");
        option = pb.create();
        assertNull(option);

        // Test creating an Option with a simple pattern
        pb = new PatternBuilder();
        pb.withPattern("a:");
        Option option1 = pb.create();
        assertNotNull(option1);
        assertEquals("a", option1.getLongOpt());
        assertEquals(0, option1.getArgs().size());

        // Test creating an Option with a required argument
        pb = new PatternBuilder();
        pb.withPattern("a:s");
        Option option2 = pb.create();
        assertNotNull(option2);
        assertEquals("a", option2.getLongOpt());
        Argument argument = option2.getArgument();
        assertNotNull(argument);
        assertEquals("s", argument.getName());

        // Test creating an Option with a optional argument
        pb = new PatternBuilder();
        pb.withPattern("a:s");
        pb.reset();
        pb.withPattern("a:s?");
        Option option3 = pb.create();
        assertNotNull(option3);
        assertEquals("a", option3.getLongOpt());
        Argument argument3 = option3.getArgument();
        assertNotNull(argument3);
        assertEquals("s", argument3.getName());
        assertEquals(Optional.OPTIONAL, argument3.getOptionality());

        // Test creating an Option with multiple arguments
        pb = new PatternBuilder();
        pb.withPattern("p:ss");
        Option option4 = pb.create();
        assertNotNull(option4);
        assertEquals("p", option4.getLongOpt());
        Argument argument4 = option4.getArgument();
        assertNotNull(argument4);
        assertEquals("s", argument4.getName());
        Argument argument5 = option4.getArgument();
        assertNotNull(argument5);
        assertEquals("s", argument5.getName());

        // Test creating an Option with a required positional argument
        pb = new PatternBuilder();
        pb.withPattern("p:s*");
        Option option5 = pb.create();
        assertNotNull(option5);
        assertEquals("p", option5.getLongOpt());
        Argument argument6 = option5.getArgument();
        assertNotNull(argument6);
        assertEquals("s", argument6.getName());
        assertEquals(Optional.REQUIRED, argument6.getOptionality());

        // Test creating an Option with a group
        pb = new PatternBuilder();
        GroupBuilder gb = new GroupBuilder();
        gb.withHeader("Test group");
        pb.withGroup(gb);
        pb.withPattern("g:");
        Option option6 = pb.create();
        assertNotNull(option6);
        assertEquals("g", option6.getLongOpt());
        assertNull(option6.getArgument());
        Option groupOption = option6.getOption();
        assertNotNull(groupOption);
        assertEquals("Test group", groupOption.getLongOpt());

        // Test creating an Option with a validator
        pb = new PatternBuilder();
        pb.withPattern("v:@my.package.MyClass");
        Option option7 = pb.create();
        assertNotNull(option7);
        assertEquals("v", option7.getLongOpt());
        Argument argument7 = option7.getArgument();
        assertNull(argument7);
        assertTrue(option7.getValidator() instanceof ClassValidator);

        // Test creating an Option with multiple validators
        pb = new PatternBuilder();
        pb.withPattern("v:@my.package.MyClass#%d");
        Option option8 = pb.create();
        assertNotNull(option8);
        assertEquals("v", option8.getLongOpt());
        Argument argument8 = option8.getArgument();
        assertNull(argument8);
        assertTrue(option8.getValidator() instanceof ClassValidator);
        assertTrue(option8.getValidator() instanceof NumberValidator);

        // Test creating an Option with a date validator
        pb = new PatternBuilder();
        pb.withPattern("d:/<dd/MM/yyyy>");
        Option option9 = pb.create();
        assertNotNull(option9);
        assertEquals("d", option9.getLongOpt());
        Argument argument9 = option9.getArgument();
        assertNull(argument9);
        assertTrue(option9.getValidator() instanceof DateValidator);

        // Test creating an Option with a file validator
        pb = new PatternBuilder();
        pb.withPattern("f:/<.");
        Option option10 = pb.create();
        assertNotNull(option10);
        assertEquals("f", option10.getLongOpt());
        Argument argument10 = option10.getArgument();
        assertNull(argument10);
        assertTrue(option10.getValidator() instanceof FileValidator);

        // Test creating an Option with a url validator
        pb = new PatternBuilder();
        pb.withPattern("u:/<http://.+>");
        Option option11 = pb.create();
        assertNotNull(option11);
        assertEquals("u", option11.getLongOpt());
        Argument argument11 = option11.getArgument();
        assertNull(argument11);
        assertTrue(option11.getValidator() instanceof UrlValidator);

        // Test creating an Option with a number validator
        pb = new PatternBuilder();
        pb.withPattern("n:/<%d,10>");
        Option option12 = pb.create();
        assertNotNull(option12);
        assertEquals("n", option12.getLongOpt());
        Argument argument12 = option12.getArgument();
        assertNull(argument12);
        assertTrue(option12.getValidator() instanceof NumberValidator);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        // Test invalid pattern with no valid characters
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("abc");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternWithInvalidCharacters() {
        // Test invalid pattern with invalid characters
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:!b:");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternMissingArgument() {
        // Test invalid pattern missing argument
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:");
        pb.create();
        pb.withPattern("a:s");
        Option option = pb.create();
        pb.create();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternMissingType() {
        // Test invalid pattern missing type
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern(":s");
        Option option = pb.create();
    }

    @org.junit.Test
    public void testReset() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("p:ss");
        Option option1 = pb.create();
        pb.reset();
        Option option2 = pb.create();
        assertNull(option2);
    }

    @org.junit.Test
    public void testCreateMultipleOptions() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:s b:s c:s");
        Option option1 = pb.create();
        Option option2 = pb.create();
        Option option3 = pb.create();
        assertEquals("a", option1.getLongOpt());
        assertEquals("b", option2.getLongOpt());
        assertEquals("c", option3.getLongOpt());
    }

    @org.junit.Test
    public void testCreateGroup() {
        PatternBuilder pb = new PatternBuilder();
        GroupBuilder gb = new GroupBuilder();
        gb.withHeader("Test group");
        pb.withGroup(gb);
        pb.withPattern("g:");
        Option option = pb.create();
        assertEquals("g", option.getLongOpt());
        Option groupOption = option.getOption();
        assertEquals("Test group", groupOption.getLongOpt());
    }
}