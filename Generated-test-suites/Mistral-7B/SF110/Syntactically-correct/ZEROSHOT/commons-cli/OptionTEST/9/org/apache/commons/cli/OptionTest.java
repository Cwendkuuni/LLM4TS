package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructionWithValidArguments() {
        Option option = new Option("option1", "Option 1", true, "Description 1");
        assertEquals("option1", option.getOpt());
        assertEquals("Option 1", option.getDescription());
        assertTrue(option.isRequired());

        option = new Option("option2", "Option 2", false, "Description 2");
        assertEquals("option2", option.getOpt());
        assertEquals("Option 2", option.getDescription());
        assertFalse(option.isRequired());

        option = new Option("option3", "Option 3", false, "Description 3", 'a');
        assertEquals("option3", option.getOpt());
        assertEquals("Option 3", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals('a', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidArgumentForShortOpt() {
        new Option("", "Description", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidArgumentForLongOpt() {
        new Option("option1", "", false, "Description");
    }

    @Test
    public void testOptionConstructionWithValidArgumentsAndOptionalArg() {
        Option option = OptionBuilder.withLongOpt("option4")
            .withDescription("Option 4")
            .isOptional()
            .create();
        assertEquals("option4", option.getLongOpt());
        assertFalse(option.isRequired());
    }

    @Test
    public void testOptionConstructionWithValidArgumentsAndArgument() {
        Option option = OptionBuilder.withLongOpt("option5")
            .withDescription("Option 5")
            .hasArg()
            .create();
        assertEquals("option5", option.getLongOpt());
        assertTrue(option.hasArg());
    }

    @Test
    public void testOptionConstructionWithValidArgumentsAndMultipleArguments() {
        Option option = OptionBuilder.withLongOpt("option6")
            .withDescription("Option 6")
            .hasArgs(2)
            .create();
        assertEquals("option6", option.getLongOpt());
        assertTrue(option.hasArgs());
        assertEquals(2, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidNumberOfArguments() {
        Option option = new Option("option7", "Option 7", false, "Description 7") {
            @Override
            public void setArgs(int num) {
                super.setArgs(num + 1);
            }
        };
        option.setArgs(1);
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("option8", "Option 8", false, "Description 8") {
            @Override
            public String getArgName() {
                return "Argument 8";
            }
        };
        assertEquals("[ option: option8 :: Option 8 :: [Argument 8] :: Description 8 ]", option.toString());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("option9", "Option 9", false, "Description 9");
        Option option2 = new Option("option9", "Option 9", false, "Description 9");
        Option option3 = new Option("option10", "Option 10", false, "Description 10");

        assertEquals(option1, option1);
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("option11", "Option 11", false, "Description 11");
        Option option2 = new Option("option11", "Option 11", false, "Description 11");
        Option option3 = new Option("option12", "Option 12", false, "Description 12");

        int hash1 = option1.hashCode();
        int hash2 = option2.hashCode();
        int hash3 = option3.hashCode();

        assertEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("option13", "Option 13", false, "Description 13");
        Option clonedOption = option.clone();

        assertEquals(option, clonedOption);
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("option14", "Option 14", false, "Description 14");
        List<String> values = new ArrayList<>();
        values.add("Value 1");
        values.add("Value 2");
        option.setValues(values);
        option.clearValues();

        assertEmpty(option.getValuesList());
    }

    @Test
    public void testOptionAddValue() {
        Option option = new Option("option15", "Option 15", false, "Description 15");
        List<String> values = new ArrayList<>();
        values.add("Value 1");
        values.add("Value 2");
        option.setValues(values);

        option.addValue("Value 3");
        List<String> result = option.getValuesList();

        assertEquals(3, result.size());
        assertEquals("Value 1", result.get(0));
        assertEquals("Value 2", result.get(1));
        assertEquals("Value 3", result.get(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValueUnsupported() {
        Option option = new Option("option16", "Option 16", false, "Description 16");
        option.addValue("Value 1");
        option.addValue("Value 2");
        option.addValue("Value 3");
        option.addValue("Value 4");
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("option17", "Option 17", false, "Description 17");
        List<String> values = new ArrayList<>();
        values.add("Value 1");
        values.add("Value 2");
        option.setValues(values);

        assertEquals("Value 1", option.getValue());
    }

    @Test
    public void testOptionGetValueEmpty() {
        Option option = new Option("option18", "Option 18", false, "Description 18");

        assertEquals(null, option.getValue());
    }

    @Test
    public void testOptionGetValueWithDefault() {
        Option option = new Option("option19", "Option 19", false, "Description 19");

        assertEquals(null, option.getValue("Default Value"));
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("option20", "Option 20", false, "Description 20");
        List<String> values = new ArrayList<>();
        values.add("Value 1");
        values.add("Value 2");
        option.setValues(values);

        String[] result = option.getValues();

        assertEquals(2, result.length);
        assertEquals("Value 1", result[0]);
        assertEquals("Value 2", result[1]);
    }

    @Test
    public void testOptionGetValuesEmpty() {
        Option option = new Option("option21", "Option 21", false, "Description 21");

        String[] result = option.getValues();

        assertArrayEquals(new String[0], result);
    }

}