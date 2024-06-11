package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ValidationError;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testCreateOption() {
        PatternBuilder builder = new PatternBuilder();

        // Test creating an option with no arguments
        builder.withPattern("t:");
        Option option = builder.create();
        Assert.assertEquals("t", option.getShortName());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(0, option.getArgs().length);

        // Test creating an option with a single required argument
        builder.reset();
        builder.withPattern("t:s");
        option = builder.create();
        Assert.assertEquals("t", option.getShortName());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(1, option.getArgs().length);

        // Test creating an option with a single optional argument
        builder.reset();
        builder.withPattern("t:s?");
        option = builder.create();
        Assert.assertEquals("t", option.getShortName());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(1, option.getArgs().length);

        // Test creating an option with multiple optional arguments
        builder.reset();
        builder.withPattern("t:s{3}");
        option = builder.create();
        Assert.assertEquals("t", option.getShortName());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(3, option.getArgs().length);

        // Test creating an option with an invalid pattern
        builder.reset();
        builder.withPattern("t:!");
        try {
            option = builder.create();
        } catch (ValidationError e) {
            Assert.assertEquals("Invalid option pattern: !", e.getMessage());
        }
    }

    @Test
    public void testCreateGroup() {
        PatternBuilder builder = new PatternBuilder();

        // Test creating a group with no options
        builder.reset();
        builder.withPattern("");
        Option group = builder.create();
        Assert.assertNotNull(group);
        Assert.assertFalse(group.isOptional());
        Assert.assertEquals(0, group.getOptions().size());

        // Test creating a group with a single option
        builder.reset();
        builder.withPattern("g:t:s");
        group = builder.create();
        Assert.assertNotNull(group);
        Assert.assertFalse(group.isOptional());
        Option option = group.getOptions().iterator().next();
        Assert.assertEquals("t", option.getShortName());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(1, option.getArgs().length);

        // Test creating a group with multiple options
        builder.reset();
        builder.withPattern("g:t:s,g2:i,g3:f");
        group = builder.create();
        Assert.assertNotNull(group);
        Assert.assertFalse(group.isOptional());
        Assert.assertEquals(3, group.getOptions().size());
        for (Option opt : group.getOptions()) {
            if (opt.getShortName().equals("t")) {
                Assert.assertEquals("t", opt.getShortName());
                Assert.assertFalse(opt.isRequired());
                Assert.assertEquals(1, opt.getArgs().length);
            } else if (opt.getShortName().equals("i")) {
                Assert.assertEquals("i", opt.getShortName());
                Assert.assertFalse(opt.isRequired());
                Assert.assertEquals(0, opt.getArgs().length);
            } else if (opt.getShortName().equals("f")) {
                Assert.assertEquals("f", opt.getShortName());
                Assert.assertFalse(opt.isRequired());
                Assert.assertEquals(1, opt.getArgs().length);
            }
        }

        // Test creating a group with a required option
        builder.reset();
        builder.withPattern("g:t:s,g2:i:");
        group = builder.create();
        Assert.assertNotNull(group);
        Assert.assertFalse(group.isOptional());
        Assert.assertEquals(2, group.getOptions().size());
        for (Option opt : group.getOptions()) {
            if (opt.getShortName().equals("t")) {
                Assert.assertEquals("t", opt.getShortName());
                Assert.assertFalse(opt.isRequired());
                Assert.assertEquals(1, opt.getArgs().length);
            } else if (opt.getShortName().equals("i")) {
                Assert.assertEquals("i", opt.getShortName());
                Assert.assertTrue(opt.isRequired());
                Assert.assertEquals(0, opt.getArgs().length);
            }
        }
    }
}