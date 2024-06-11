package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OptionsTest {

    @Test
    public void testOptionsCreation() {
        // Given
        Options options = new Options();

        // When

        // Then
        Assert.assertEquals("Options: [ short = [], long = {} ]", options.toString());
    }

    @Test
    public void testAddOptionWithShortNameOnly() {
        // Given
        Options options = new Options();

        // When
        options.addOption("a", "optionA", false, "Option A");

        // Then
        Assert.assertEquals("Options: [ short={a}, long={} ]", options.toString());
        Assert.assertEquals("optionA", options.getOption("a").getKey());
    }

    @Test
    public void testAddOptionWithLongNameOnly() {
        // Given
        Options options = new Options();

        // When
        options.addOption("optionB", false, "Option B");

        // Then
        Assert.assertEquals("Options: [ short={}, long={optionB} ]", options.toString());
        Assert.assertEquals("optionB", options.getOption("optionB").getKey());
    }

    @Test
    public void testAddOptionWithShortAndLongNames() {
        // Given
        Options options = new Options();

        // When
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("optionB", false, "Option B");

        // Then
        Assert.assertEquals("Options: [ short={a}, long={optionA, optionB} ]", options.toString());
        Assert.assertEquals("optionA", options.getOption("a").getKey());
        Assert.assertEquals("optionB", options.getOption("optionB").getKey());
    }

    @Test
    public void testAddOptionWithValue() {
        // Given
        Options options = new Options();

        // When
        options.addOption("a", "optionA", true, "Option A");

        // Then
        Assert.assertEquals("Options: [ short={a}, long={} ]", options.toString());
        Assert.assertEquals("optionA", options.getOption("a").getKey());
        Assert.assertTrue(options.getOption("a").hasArg());
    }

    @Test
    public void testAddOptionsWithValues() {
        // Given
        Options options = new Options();

        // When
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", true, "Option B");

        // Then
        Assert.assertEquals("Options: [ short={a, b}, long={optionA, optionB} ]", options.toString());
        Assert.assertEquals("optionA", options.getOption("a").getKey());
        Assert.assertTrue(options.getOption("a").hasArg());
        Assert.assertEquals("optionB", options.getOption("b").getKey());
        Assert.assertTrue(options.getOption("b").hasArg());
    }

    @Test
    public void testAddOptionGroup() {
        // Given
        Options options = new Options();
        OptionGroup group = new OptionGroup("testGroup", "Test Group");
        group.setRequired(false);
        group.addOption(new Option("g", "groupOption", false, "Group Option"));

        // When
        options.addOptionGroup(group);

        // Then
        Assert.assertEquals("Options: [ short={}, long={}, optionGroups={testGroup} ]", options.toString());
        Assert.assertEquals("testGroup", options.getOptionGroup("testGroup").getName());
        Assert.assertNull(options.getOption("testGroup"));
        Assert.assertEquals("groupOption", options.getOptionGroup("testGroup").getOptions().get(0).getKey());
    }

    @Test
    public void testHasShortOption() {
        // Given
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        // When
        boolean result = options.hasOption("a");

        // Then
        Assert.assertTrue(result);
    }

    @Test
    public void testHasLongOption() {
        // Given
        Options options = new Options();
        options.addOption("optionB", false, "Option B");

        // When
        boolean result = options.hasLongOption("optionB");

        // Then
        Assert.assertTrue(result);
    }

    @Test
    public void testHasShortOptionWithWrongName() {
        // Given
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        // When
        boolean result = options.hasOption("b");

        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void testHasLongOptionWithWrongName() {
        // Given
        Options options = new Options();
        options.addOption("optionB", false, "Option B");

        // When
        boolean result = options.hasLongOption("optionC");

        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void testGetMatchingOptions() {
        // Given
        Options options = new Options();
        options.addOption("optA", false, "Option A");
        options.addOption("optB", false, "Option B");
        options.addOption("optC", false, "Option C");

        // When
        List<String> result = options.getMatchingOptions("opt");

        // Then
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(Arrays.asList("optA", "optB").containsAll(result));
    }

    @Test
    public void testGetMatchingOptionsWithNoMatchingOptions() {
        // Given
        Options options = new Options();
        options.addOption("optA", false, "Option A");
        options.addOption("optB", false, "Option B");

        // When
        List<String> result = options.getMatchingOptions("optX");

        // Then
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testGetMatchingOptionsWithLongNames() {
        // Given
        Options options = new Options();
        String longName = "longOptionName";
        options.addOption(new Option(null, longName, false, "Long Option Name"));

        // When
        List<String> result = options.getMatchingOptions("longOptionName");

        // Then
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(longName, result.get(0));
    }

    @Test
    public void testGetMatchingOptionsWithPartialShortName() {
        // Given
        Options options = new Options();
        options.addOption("optA", false, "Option A");
        options.addOption("optB", false, "Option B");

        // When
        List<String> result = options.getMatchingOptions("op");

        // Then
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(Arrays.asList("optA", "optB").containsAll(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullDescription() {
        // Given
        Options options = new Options();

        // When
        options.addOption(null);

        // Then
        // An IllegalArgumentException should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionGroupWithNullName() {
        // Given
        Options options = new Options();

        // When
        options.addOptionGroup(null);

        // Then
        // An IllegalArgumentException should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionGroupWithEmptyName() {
        // Given
        Options options = new Options();

        // When
        options.addOptionGroup(new OptionGroup("", ""));

        // Then
        // An IllegalArgumentException should be thrown
    }

    @Test
    public void testAddOptionWithSameNameButDifferentDescription() {
        // Given
        Options options = new Options();
        String description1 = "Description 1";
        String description2 = "Description 2";

        // When
        Option option1 = new Option("a", "optionA", false, description1);
        Option option2 = new Option("a", "optionA", false, description2);
        options.addOption(option1);
        options.addOption(option2);

        // Then
        Assert.assertEquals("Options: [ short={a}, long={optionA} ]", options.toString());
        Assert.assertEquals(description1, options.getOption("a").getDescription());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithSameNameButDifferentValues() {
        // Given
        Options options = new Options();
        String value1 = "value1";
        String value2 = "value2";

        // When
        Option option1 = new Option("a", "optionA", true, value1);
        Option option2 = new Option("a", "optionA", true, value2);
        options.addOption(option1);
        options.addOption(option2);

        // Then
        // An IllegalStateException should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithEmptyShortName() {
        // Given
        Options options = new Options();

        // When
        options.addOption(new Option("", "optionA", true, "Option A"));

        // Then
        // An IllegalArgumentException should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithEmptyLongName() {
        // Given
        Options options = new Options();

        // When
        options.addOption(new Option("optionA", "", true, "Option A"));

        // Then
        // An IllegalArgumentException should be thrown
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithSameLongNameButDifferentShortNames() {
        // Given
        Options options = new Options();

        // When
        Option option1 = new Option("optionA", "optionA1", true, "Option A");
        Option option2 = new Option("optionB", "optionA2", true, "Option A");
        options.addOption(option1);
        options.addOption(option2);

        // Then
        // An IllegalStateException should be thrown
    }

    @Test
    public void testToString() {
        // Given
        Options options = new Options();

        // When
        String result = options.toString();

        // Then
        Assert.assertEquals("", result);

        // When - Add options
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");

        // Then
        result = options.toString();
        Assert.assertEquals("Options: [ short={a, b}, long={optionA, optionB} ]", result);

        // When - Add option group
        OptionGroup group = new OptionGroup("testGroup", "Test Group");
        group.addOption(new Option("g", "groupOption", false, "Group Option"));
        options.addOptionGroup(group);

        // Then
        result = options.toString();
        Assert.assertEquals("Options: [ short={a, b}, long={optionA, optionB}, optionGroups={testGroup} ]", result);
    }
}