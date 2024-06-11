package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option option = patternBuilder.withPattern("a:@String,b:+Class,c:*,d:/URL,e:#Date,f:<File,g:%,h:!")
                .create();

        assertEquals("Option should have 8 arguments", 8, option.getArgs().size());

        Argument a = option.getArgs().get(0);
        assertEquals("Argument 'a' should have validator of type ClassValidator",
                ClassValidator.class, a.getValidator());

        Argument b = option.getArgs().get(1);
        assertEquals("Argument 'b' should have validator of type ClassValidator (instance)",
                ClassValidator.class, b.getValidator().getClass());

        Argument c = option.getArgs().get(2);
        assertEquals("Argument 'c' should have validator of type FileValidator (existing and file)",
                FileValidator.class, c.getValidator());

        Argument d = option.getArgs().get(3);
        assertEquals("Argument 'd' should have validator of type UrlValidator",
                UrlValidator.class, d.getValidator());

        Argument e = option.getArgs().get(4);
        assertEquals("Argument 'e' should have validator of type DateValidator",
                DateValidator.class, e.getValidator());

        Argument f = option.getArgs().get(5);
        assertEquals("Argument 'f' should have validator of type FileValidator",
                FileValidator.class, f.getValidator());

        Argument g = option.getArgs().get(6);
        assertEquals("Argument 'g' should have validator of type NumberValidator",
                NumberValidator.class, g.getValidator());

        Argument h = option.getArgs().get(7);
        assertNull("Argument 'h' should not have validator", h.getValidator());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();

        patternBuilder.withPattern("a:@String,b:+Class,c:*,d:/URL,e:#Date,f:<File,g:%,h:!");
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingArgumentType() {
        PatternBuilder patternBuilder = new PatternBuilder();

        patternBuilder.withPattern("a:@String,b:+,c:*,d:/URL,e:#Date,f:<File,g:");
    }
}