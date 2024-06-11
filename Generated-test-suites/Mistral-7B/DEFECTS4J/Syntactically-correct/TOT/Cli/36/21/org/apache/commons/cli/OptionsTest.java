package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testSimpleOption() {
        Options options = new Options();
        options.addOption("a", "alert", false, "Display an alert message");

        Assert.assertFalse(options.parse("-a", new String[0]));
        Assert.assertTrue(options.isSet("alert"));

        try {
            options.parse("-a", new String[]{"foo"});
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            options.parse("-a", new String[]{"-a"});
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            options.parse("-b", new String[0]);
            Assert.assertFalse(options.isSet("alert"));
        } finally {
            Assert.assertFalse(options.hasOptions());
        }
    }

    @Test
    public void testOptionWithValue() {
        Options options = new Options();
        options.addOption("a", "alert", true, "Display an alert message (with value)");

        Assert.assertFalse(options.parse("-a", new String[0]));
        Assert.assertNull(options.getOptionValue("alert"));

        Assert.assertEquals("foo", options.parse("-a", new String[]{"-a", "foo"}));
        Assert.assertEquals("foo", options.getOptionValue("alert"));

        try {
            options.parse("-a", new String[]{"-a", "bar", "qux"});
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            options.parse("-b", new String[0]);
            Assert.assertFalse(options.isSet("alert"));
            Assert.assertNull(options.getOptionValue("alert"));
        } finally {
            Assert.assertFalse(options.hasOptions());
        }
    }

    @Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("alert", "a", false, "Display an alert message");

        Assert.assertFalse(options.parse("--alert", new String[0]));
        Assert.assertTrue(options.isSet("alert"));

        try {
            options.parse("--alert", new String[]{"--foo"});
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            options.parse("--bar", new String[0]);
            Assert.assertFalse(options.isSet("alert"));
        } finally {
            Assert.assertFalse(options.hasOptions());
        }
    }

    @Test
    public void testLongOptionWithValue() {
        Options options = new Options();
        options.addOption("alert", "a", true, "Display an alert message (with value)");

        Assert.assertFalse(options.parse("--alert", new String[0]));
        Assert.assertNull(options.getOptionValue("alert"));

        Assert.assertEquals("foo", options.parse("--alert", new String[]{"--alert", "foo"}));
        Assert.assertEquals("foo", options.getOptionValue("alert"));

        try {
            options.parse("--alert", new String[]{"--alert", "foo", "qux"});
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            options.parse("--bar", new String[0]);
            Assert.assertFalse(options.isSet("alert"));
            Assert.assertNull(options.getOptionValue("alert"));
        } finally {
            Assert.assertFalse(options.hasOptions());
        }
    }

    @Test
    public void testOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.setRequired(true);

        options.addOptionGroup(group);
        group.addOption(new Option("a", "alert", false, "Display an alert message", true));
        group.addOption(new Option("b", "info", false, "Display an info message", false));

        try {
            options.parse(new String[0][0]);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            options.parse("-a", new String[0]);
            Assert.assertTrue(options.isSet("alert"));
            Assert.assertFalse(options.isSet("info"));
        } finally {
            Assert.assertEquals(1, options.getOptions().size());
        }

        try {
            options.parse("-b", new String[0]);
            Assert.assertFalse(options.isSet("alert"));
            Assert.assertTrue(options.isSet("info"));
        } finally {
            Assert.assertEquals(1, options.getOptions().size());
        }

        try {
            options.parse("--group1", new String[0]);
            Assert.assertTrue(options.isSet("alert"));
            Assert.assertTrue(options.isSet("info"));
        } finally {
            Assert.assertEquals(2, options.getOptions().size());
        }

        try {
            options.parse("--group1", new String[]{"--alert"});
            Assert.assertTrue(options.isSet("alert"));
            Assert.assertFalse(options.isSet("info"));
        } finally {
            Assert.assertEquals(1, options.getOptions().size());
        }

        try {
            options.parse("--group1", new String[]{"--info"});
            Assert.assertFalse(options.isSet("alert"));
            Assert.assertTrue(options.isSet("info"));
        } finally {
            Assert.assertEquals(1, options.getOptions().size());
        }
    }

    @Test
    public void testMixedOptions() {
        Options options = new Options();
        options.addOption("a", "alert", false, "Display an alert message");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        OptionGroup group = new OptionGroup("group2", "Group 2");
        group.setRequired(true);
        options.addOptionGroup(group);
        group.addOption(new Option("b", "beta", false, "Disable beta functionality", true));
        group.addOption(new Option("c", "charset", true, "Character set to use", "UTF-8"));

        try {
            options.parse("--alpha --beta --charset UTF-8", new String[0]);
            Assert.assertTrue(options.isSet("alert"));
            Assert.assertTrue(options.isSet("beta"));
            Assert.assertEquals("UTF-8", options.getOptionValue("charset"));
        } finally {
            Assert.assertEquals(3, options.getOptions().size());
        }

        try {
            options.parse("--alpha --beta", new String[0]);
            Assert.assertTrue(options.isSet("alert"));
            Assert.assertTrue(options.isSet("beta"));
            Assert.assertNull(options.getOptionValue("charset"));
        } finally {
            Assert.assertEquals(2, options.getOptions().size());
        }

        try {
            options.parse("--alpha --no-beta --charset UTF-8", new String[0]);
            Assert.assertTrue(options.isSet("alert"));
            Assert.assertFalse(options.isSet("beta"));
            Assert.assertEquals("UTF-8", options.getOptionValue("charset"));
        } finally {
            Assert.assertEquals(3, options.getOptions().size());
        }
    }
}