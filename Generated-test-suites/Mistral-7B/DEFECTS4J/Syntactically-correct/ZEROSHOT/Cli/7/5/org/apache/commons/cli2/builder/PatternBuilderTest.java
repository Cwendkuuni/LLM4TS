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
        PatternBuilder pb = new PatternBuilder();

        // Test empty pattern
        pb.reset();
        try {
            pb.withPattern("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test simple pattern
        pb.withPattern("a:");
        Option option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertNull(option.getArgument());

        // Test pattern with argument
        pb.reset();
        pb.withPattern("a:s");
        option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertEquals("s", option.getArgument().getName());

        // Test pattern with required argument
        pb.reset();
        pb.withPattern("a:s");
        pb.create(); // Create first option
        pb.withPattern("b:*");
        option = pb.create();
        assertNotNull(option);
        assertEquals("b", option.getLongOpt());
        assertNotNull(option.getArgument());

        // Test pattern with optional argument
        pb.reset();
        pb.withPattern("a:s");
        option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertNull(option.getArgument());

        pb.withPattern("b:s");
        option = pb.create();
        assertNotNull(option);
        assertEquals("b", option.getLongOpt());
        assertNotNull(option.getArgument());

        // Test pattern with class validator
        pb.reset();
        pb.withPattern("c:C");
        option = pb.create();
        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertTrue(option.getValidator() instanceof ClassValidator);

        // Test pattern with number validator
        pb.reset();
        pb.withPattern("d:d");
        option = pb.create();
        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertTrue(option.getValidator() instanceof NumberValidator);

        // Test pattern with date validator
        pb.reset();
        pb.withPattern("e:");
        pb.create(); // Create first option
        pb.withPattern("f:D{yyyy-MM-dd}");
        option = pb.create();
        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertTrue(option.getValidator() instanceof DateValidator);

        // Test pattern with file validator
        pb.reset();
        pb.withPattern("g:");
        pb.create(); // Create first option
        pb.withPattern("h:");
        pb.create(); // Create second option
        pb.withPattern("i:F");
        option = pb.create();
        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertTrue(option.getValidator() instanceof FileValidator);
    }
}