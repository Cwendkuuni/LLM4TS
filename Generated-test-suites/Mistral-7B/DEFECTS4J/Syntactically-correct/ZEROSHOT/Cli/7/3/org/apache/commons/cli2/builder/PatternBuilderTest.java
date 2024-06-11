package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    /**
     * Test creating a new Option instance with no options set.
     */
    @Test
    public void testCreateEmptyOption() {
        Option option = patternBuilder.create();
        assertNull(option);
    }

    /**
     * Test creating a new Option instance with a single option set.
     */
    @Test
    public void testCreateSingleOption() {
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();
        assertEquals("Option 'a' should have been created", Option.builder()
                .required(false)
                .build(), option);
    }

    /**
     * Test creating a new Option instance with a required option set.
     */
    @Test
    public void testCreateRequiredOption() {
        patternBuilder.withPattern("a:!");
        Option option = patternBuilder.create();
        assertEquals("Option 'a' should have been created as required", Option.builder()
                .required(true)
                .build(), option);
    }

    /**
     * Test creating a new Option instance with a validator set.
     */
    @Test
    public void testCreateOptionWithValidator() {
        patternBuilder.withPattern("%d:");
        Option option = patternBuilder.create();
        assertEquals("Option 'd' should have been created with NumberValidator", Option.builder()
                .required(false)
                .withValidators(new NumberValidator())
                .build(), option);
    }

    /**
     * Test creating a new Option instance with multiple options set.
     */
    @Test
    public void testCreateMultipleOptions() {
        patternBuilder.withPattern("a:b c:d e:f");
        Option option = patternBuilder.create();
        GroupBuilder groupBuilder = (GroupBuilder) option.getBuilder();
        assertEquals("Multiple options should have been created", 3, groupBuilder.getOptions().size());
        Iterator<Option> optionIterator = groupBuilder.getOptions().iterator();
        assertEquals("Option 'a' should have been created", Option.builder()
                .required(false)
                .build(), optionIterator.next());
        assertEquals("Option 'c' should have been created", Option.builder()
                .required(false)
                .build(), optionIterator.next());
        assertEquals("Option 'e' should have been created", Option.builder()
                .required(false)
                .build(), optionIterator.next());
    }

    /**
     * Test creating a new Option instance with a required and validator option set.
     */
    @Test
    public void testCreateRequiredOptionWithValidator() {
        patternBuilder.withPattern("a:!@java.lang.String:");
        Option option = patternBuilder.create();
        assertEquals("Option 'a' should have been created as required and string validator", Option.builder()
                .required(true)
                .withValidators(new ClassValidator(String.class))
                .build(), option);
    }

    /**
     * Test creating a new Option instance with multiple validators set.
     */
    @Test
    public void testCreateOptionWithMultipleValidators() {
        patternBuilder.withPattern("%d:#%m/%d/%Y");
        Option option = patternBuilder.create();
        assertEquals("Option 'd' should have been created with DateValidator and NumberValidator", Option.builder()
                .required(false)
                .withValidators(new NumberValidator(), new DateValidator("dd/MM/yyyy"))
                .build(), option);
    }

    /**
     * Test creating a new Option instance with a file validator.
     */
    @Test
    public void testCreateOptionWithFileValidator() {
        patternBuilder.withPattern("<file:>");
        Option option = patternBuilder.create();
        assertEquals("Option 'file' should have been created with FileValidator", Option.builder()
                .required(false)
                .withValidators(new FileValidator())
                .build(), option);
    }

    /**
     * Test creating a new Option instance with a url validator.
     */
    @Test
    public void testCreateOptionWithUrlValidator() {
        patternBuilder.withPattern("/url:>");
        Option option = patternBuilder.create();
        assertEquals("Option 'url' should have been created with UrlValidator", Option.builder()
                .required(false)
                .withValidators(new UrlValidator())
                .build(), option);
    }
}