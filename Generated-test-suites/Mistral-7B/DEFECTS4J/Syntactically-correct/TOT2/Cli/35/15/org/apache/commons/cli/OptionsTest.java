import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

    @Test
    public void testAddOptionWithShortNameAndNoArgument() {
        Options options = new Options();
        options.addOption('a', "optionA", false, "Option A description");

        Option option = options.getOption("optionA");
        assertEquals("Option A description", option.getDescription());
        assertTrue(option.isRequired());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
        assertEquals('a', option.getKey());
        assertEquals("optionA", option.getLongOpt());
    }

    @Test
    public void testAddOptionWithShortNameAndRequiredArgument() {
        Options options = new Options();
        options.addOption('b', "optionB", true, "Option B description");

        Option option = options.getOption("optionB");
        assertEquals("Option B description", option.getDescription());
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertNull(option.getValue());
        assertEquals('b', option.getKey());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testAddOptionWithShortNameAndOptionalArgument() {
        Options options = new Options();
        options.addOption('c', "optionC", true, "Option C description");

        Option option = options.getOption("optionC");
        assertEquals("Option C description", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertNull(option.getValue());
        assertEquals('c', option.getKey());
        assertNull(option.getLongOpt());

        try {
            options.parse("-c test");
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        String[] args = new String[]{"-c", "test"};
        options.parse(args);
        assertEquals("test", option.getValue());
    }

    @Test
    public void testAddOptionWithShortNameAndValue() {
        Options options = new Options();
        options.addOption('d', "optionD", true, "Option D description");

        Option option = options.getOption("optionD");
        assertEquals("Option D description", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertNull(option.getValue());
        assertEquals('d', option.getKey());
        assertNull(option.getLongOpt());

        try {
            options.parse("-d");
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        String[] args = new String[]{"-d", "value"};
        options.parse(args);
        assertEquals("value", option.getValue());
    }

    @Test
    public void testAddOptionWithLongNameAndNoArgument() {
        Options options = new Options();
        options.addOption("optionE", false, "Option E description");

        Option option = options.getOption("optionE");
        assertEquals("Option E description", option.getDescription());
        assertTrue(option.isRequired());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
        assertEquals("optionE", option.getKey());
        assertEquals("optionE", option.getLongOpt());
    }

    @Test
    public void testAddOptionWithLongNameAndRequiredArgument() {
        Options options = new Options();
        options.addOption("optionF", true, "Option F description");

        Option option = options.getOption("optionF");
        assertEquals("Option F description", option.getDescription());
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertNull(option.getValue());
        assertEquals("optionF", option.getKey());
        assertEquals("optionF", option.getLongOpt());

        try {
            options.parse("--optionF");
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        String[] args = new String[]{"--optionF", "value"};
        options.parse(args);
        assertEquals("value", option.getValue());
    }

    @Test
    public void testAddOptionWithLongNameAndOptionalArgument() {
        Options options = new Options();
        options.addOption("optionG", true, "Option G description");

        Option option = options.getOption("optionG");
        assertEquals("Option G description", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertNull(option.getValue());
        assertEquals("optionG", option.getKey());
        assertEquals("optionG", option.getLongOpt());

        try {
            options.parse("--optionG");
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        String[] args = new String[]{"--optionG", "value"};
        options.parse(args);
        assertEquals("value", option.getValue());
    }

    @Test
    public void testAddOptionWithLongNameAndValue() {
        Options options = new Options();
        options.addOption("optionH", true, "Option H description");

        Option option = options.getOption("optionH");
        assertEquals("Option H description", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertNull(option.getValue());
        assertEquals("optionH", option.getKey());
        assertEquals("optionH", option.getLongOpt());

        try {
            options.parse("--optionH");
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        String[] args = new String[]{"--optionH", "value"};
        options.parse(args);
        assertEquals("value", option.getValue());
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("MyGroup", "My group description");
        options.addOptionGroup(group);

        Option option = new Option("optionI", "optionI", false, "Option I description");
        group.addOption(option);

        Option optionJ = new Option("optionJ", "optionJ", false, "Option J description");
        group.addOption(optionJ);

        Option optionK = new Option("optionK", "optionK", false, "Option K description");
        group.addOption(optionK);

        Option optionL = new Option("optionL", "optionL", false, "Option L description");
        group.addOption(optionL);

        Option optionM = new Option("optionM", "optionM", false, "Option M description");
        group.addOption(optionM);

        Option optionN = new Option("optionN", "optionN", false, "Option N description");
        group.addOption(optionN);

        Option optionO = new Option("optionO", "optionO", false, "Option O description");
        group.addOption(optionO);

        List<Option> optionsList = options.helpOptions();
        assertEquals(11, optionsList.size());

        for (Option option : optionsList) {
            if (option.getKey().equals("optionI")) {
                assertEquals("My group description", option.getDescription());
                assertEquals(group, option.getParent());
            }
        }
    }

    @Test
    public void testParseWithShortOption() {
        Options options = new Options();
        options.addOption('a', "optionA", false, "Option A description");

        String[] args = new String[]{"-a"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"-a", "value"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"-a", "value"};
        Options parsedOptions = options.parse(args);
        Option option = parsedOptions.getOption("optionA");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testParseWithLongOption() {
        Options options = new Options();
        options.addOption("optionB", false, "Option B description");

        String[] args = new String[]{"--optionB"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"--optionB", "value"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"--optionB", "value"};
        Options parsedOptions = options.parse(args);
        Option option = parsedOptions.getOption("optionB");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testParseWithShortAndLongOptions() {
        Options options = new Options();
        options.addOption('a', "optionA", false, "Option A description");
        options.addOption("optionB", false, "Option B description");

        String[] args = new String[]{"-a", "--optionB"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"-a", "--optionB", "value"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"-a", "--optionB", "value"};
        Options parsedOptions = options.parse(args);
        Option optionA = parsedOptions.getOption("optionA");
        assertEquals(false, optionA.isSet());

        Option optionB = parsedOptions.getOption("optionB");
        assertEquals("value", optionB.getValue());
    }

    @Test
    public void testParseWithShortAndLongOptionsWithValue() {
        Options options = new Options();
        options.addOption('a', "optionA", true, "Option A description");
        options.addOption("optionB", true, "Option B description");

        String[] args = new String[]{"-a", "valueA", "--optionB", "valueB"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"-a", "valueA", "--optionB", "valueB"};
        Options parsedOptions = options.parse(args);
        Option optionA = parsedOptions.getOption("optionA");
        assertEquals("valueA", optionA.getValue());

        Option optionB = parsedOptions.getOption("optionB");
        assertEquals("valueB", optionB.getValue());
    }

    @Test
    public void testParseWithInvalidShortOption() {
        Options options = new Options();
        options.addOption('a', "optionA", false, "Option A description");

        String[] args = new String[]{"-z"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"-a", "--optionZ"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithInvalidLongOption() {
        Options options = new Options();
        options.addOption("optionA", false, "Option A description");

        String[] args = new String[]{"--invalidOption"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithInvalidCombinationOfOptions() {
        Options options = new Options();
        options.addOption('a', "optionA", false, "Option A description");
        options.addOption("optionB", false, "Option B description");

        String[] args = new String[]{"--optionB", "-a", "--optionA"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("MyGroup", "My group description");
        options.addOptionGroup(group);

        Option optionI = new Option("optionI", "optionI", false, "Option I description");
        group.addOption(optionI);

        Option optionJ = new Option("optionJ", "optionJ", false, "Option J description");
        group.addOption(optionJ);

        String[] args = new String[]{"--MyGroup"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"--MyGroup", "--optionI"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"--MyGroup", "--optionJ"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"--MyGroup", "--optionI", "valueA"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"--MyGroup", "--optionJ", "valueB"};
        try {
            options.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        args = new String[]{"--MyGroup", "--optionI", "valueA", "--optionJ", "valueB"};
        Options parsedOptions = options.parse(args);
        Option optionGroup = parsedOptions.getOptionGroup("MyGroup");
        Option optionI = optionGroup.getOption("optionI");
        assertEquals("valueA", optionI.getValue());

        Option optionJ = optionGroup.getOption("optionJ");
        assertEquals("valueB", optionJ.getValue());
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption('a', "optionA", false, "Option A description");
        options.addOption("optionB", false, "Option B description");

        assertEquals("[ Options: [ short {a=}, ], [ long {} ]]", options.toString());
    }
}