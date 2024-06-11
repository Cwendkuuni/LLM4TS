package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PatternBuilderTest {

    @Test
    public void testCreateOptionWithValidPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("@com.example.MyClass:argName:/myurl.html,%d,<file.txt,#yyyy-MM-dd,+MyClass,@com.example.MyClass:*argName,@com.example.MyClass:argName:#{T(java.util.Date)}");
        Option option = builder.create();

        assertEquals("com.example.MyClass", option.getLongOpt());
        assertEquals("argName", option.getOpt());
        assertEquals("file.txt", option.getArgName());
        assertSame(DateValidator.getDateInstance(), option.getValidator());
        assertTrue(option.isRequired());

        Argument argument = option.getArg();
        assertNull(argument);
    }

    @Test
    public void testCreateOptionWithEmptyPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("");
        Option option = builder.create();

        assertNull(option);
    }

    @Test
    public void testCreateOptionWithInvalidPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("@invalid:argName");
        Option option = builder.create();

        assertNull(option);
        assertThrows(IllegalArgumentException.class, () -> builder.create());
    }

    @Test
    public void testCreateOptionWithMandatoryArg() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("@com.example.MyClass:argName:<mandatoryArg");
        Option option = builder.create();

        assertEquals("com.example.MyClass", option.getLongOpt());
        assertEquals("argName", option.getOpt());
        assertSame(FileValidator.class, option.getClass());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateOptionWithOptionalArg() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("@com.example.MyClass:argName:/mandatoryArg:<optionalArg");
        Option option = builder.create();

        assertEquals("com.example.MyClass", option.getLongOpt());
        assertEquals("argName", option.getOpt());
        assertSame(FileValidator.class, option.getClass());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCreateOptionWithNoArg() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("@com.example.MyClass:argName:");
        Option option = builder.create();

        assertEquals("com.example.MyClass", option.getLongOpt());
        assertEquals("argName", option.getOpt());
        assertNull(option.getArgName());
    }

    @Test
    public void testCreateOptionWithInvalidArgType() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("@com.example.MyClass:argName:#invalid");
        Option option = builder.create();

        assertNull(option);
        assertThrows(IllegalArgumentException.class, () -> builder.create());
    }

    @Test
    public void testCreateOptionWithInvalidValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("@com.example.MyClass:argName:~invalid");
        Option option = builder.create();

        assertNull(option);
        assertThrows(IllegalArgumentException.class, () -> builder.create());
    }
}