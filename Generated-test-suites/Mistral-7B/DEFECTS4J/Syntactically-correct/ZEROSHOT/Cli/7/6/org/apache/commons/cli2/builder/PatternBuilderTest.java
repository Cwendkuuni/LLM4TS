package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option option = patternBuilder.withPattern("a:@com.test.MyClass") // valid class
                .withPattern("b:+MyClass") // valid class with instance
                .withPattern("c:#") // valid date
                .withPattern("d:%d") // valid number
                .withPattern("e:<test.txt>") // valid existing file
                .withPattern("f:>") // valid directory
                .withPattern("g:*") // valid multiple files
                .withPattern("h:/") // valid url
                .create();

        assertNotNull(option);

        patternBuilder.reset();

        patternBuilder.withPattern("i:") // invalid pattern
                .create();

        assertNull(patternBuilder.create());
    }
}