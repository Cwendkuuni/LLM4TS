import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionConstruction() throws IllegalArgumentException {
        Option option = new Option("a", "option-a", false, "This is an option with no argument.");
        Assert.assertEquals("a", option.getOpt());
        Assert.assertEquals("option-a", option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertNull(option.getArgName());
        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasValues());
        Assert.assertFalse(option.isRequired());
        Assert.assertArrayEquals(new String[0], option.getValues());
    }

    @Test
    public void testOptionConstructionWithArgument() throws IllegalArgumentException {
        Option option = new Option("b", "option-b", true, "This is an option with an argument.");
        Assert.assertEquals("b", option.getOpt());
        Assert.assertEquals("option-b", option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasValues());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testOptionConstructionWithLongOptAndArgument() throws IllegalArgumentException {
        Option option = new Option("c", null, true, "This is an option with a long option name and an argument.");
        Assert.assertEquals(null, option.getOpt());
        Assert.assertEquals("long-option-c", option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasValues());
        Assert.assertFalse(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstruction() throws IllegalArgumentException {
        new Option(null, "invalid-option", false, "Invalid option name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithLongOpt() throws IllegalArgumentException {
        new Option("d", null, false, "Invalid option name with long opt");
    }

    @Test
    public void testOptiongetId() {
        Option optionA = new Option("a", null, false, "Option A");
        Option optionB = new Option("b", null, false, "Option B");
        Assert.assertEquals('a', optionA.getId());
        Assert.assertEquals('b', optionB.getId());
    }

    @Test
    public void testOptionGetKey() {
        Option optionA = new Option("a", null, false, "Option A");
        Option optionB = new Option("b", null, false, "Option B");
        Option optionC = new Option("c", "long-option-c", false, "Option C");
        Assert.assertEquals("a", optionA.getKey());
        Assert.assertEquals("b", optionB.getKey());
        Assert.assertEquals("long-option-c", optionC.getKey());
    }

    @Test
    public void testOptionGetType() {
        Option option = new Option("type-option", null, false, "Type option");
        option.setType("String");
        Assert.assertEquals("String", option.getType());
    }

    @Test
    public void testOptionSetType() {
        Option option = new Option("type-option", null, false, "Type option");
        option.setType(Integer.class);
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionType() {
        Option option = new Option("type-option", null, false, "Type option");
        option.setType(new Object());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option optionA = new Option("a", null, false, "Option A");
        Option optionB = new Option("b", null, true, "Option B");
        Assert.assertFalse(optionA.hasOptionalArg());
        Assert.assertTrue(optionB.hasOptionalArg());
    }

    @Test
    public void testOptionHasArg() {
        Option optionA = new Option("a", null, false, "Option A");
        Option optionB = new Option("b", null, true, "Option B");
        Option optionC = new Option("c", null, false, "Option C", 1);
        Assert.assertFalse(optionA.hasArg());
        Assert.assertFalse(optionB.hasArg());
        Assert.assertTrue(optionC.hasArg());
    }

    @Test
    public void testOptionGetDescription() {
        Option option = new Option("desc-option", null, false, "This is an option with a description.");
        Assert.assertEquals("This is an option with a description.", option.getDescription());
    }

    @Test
    public void testOptionSetDescription() {
        Option option = new Option("desc-option", null, false, "This is an option with a description.");
        option.setDescription("A new description.");
        Assert.assertEquals("A new description.", option.getDescription());
    }

    @Test
    public void testOptionIsRequired() {
        Option optionA = new Option("req-option", null, false, "Option A");
        Option optionB = new Option("opt-option", null, false, "Option B");
        Option optionC = new Option("req-option", null, true, "Option C");
        Assert.assertFalse(optionA.isRequired());
        Assert.assertFalse(optionB.isRequired());
        Assert.assertTrue(optionC.isRequired());
    }

    @Test
    public void testOptionSetRequired() {
        Option option = new Option("req-option", null, false, "Option A");
        option.setRequired(true);
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionSetArgName() {
        Option option = new Option("argname-option", null, false, "Option A");
        option.setArgName("argName");
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionArgName() {
        Option option = new Option("argname-option", null, false, "Option A");
        option.setArgName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionArgNameLength() {
        Option option = new Option("argname-option", null, false, "Option A");
        option.setArgName("argName".substring(0, 1));
    }

    @Test
    public void testOptionHasArgs() {
        Option optionA = new Option("a", null, false, "Option A");
        Option optionB = new Option("b", null, true, "Option B", 2);
        Option optionC = new Option("c", null, false, "Option C", 3);
        Assert.assertFalse(optionA.hasArgs());
        Assert.assertTrue(optionB.hasArgs());
        Assert.assertTrue(optionC.hasArgs());
    }

    @Test
    public void testOptionSetArgs() {
        Option option = new Option("args-option", null, false, "Option A");
        option.setArgs(2);
        Assert.assertEquals(2, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionSetArgs() {
        Option option = new Option("args-option", null, false, "Option A");
        option.setArgs(-1);
    }

    @Test
    public void testOptionValueSeparator() {
        Option option = new Option("sep-option", null, false, "Option A", '-');
        Assert.assertEquals('-', option.getValueSeparator());
    }

    @Test
    public void testOptionAddValueForProcessing() {
        Option option = new Option("addvalue-option", null, false, "Option A");
        option.addValueForProcessing("Value 1");
        option.addValueForProcessing("Value 2");
        Assert.assertEquals(2, option.values.size());
        Assert.assertEquals("Value 1", option.values.get(0));
        Assert.assertEquals("Value 2", option.values.get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueForProcessingInvalidNumberOfValues() {
        Option option = new Option("addvalue-option", null, false, "Option A");
        option.addValueForProcessing("Value 1");
        option.addValueForProcessing("Value 2");
        option.addValueForProcessing("Value 3");
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("value-option", null, false, "Option A");
        option.addValueForProcessing("Value 1");
        Assert.assertEquals("Value 1", option.getValue());
    }

    @Test
    public void testOptionGetValueByIndex() {
        Option option = new Option("value-option", null, false, "Option A");
        option.addValueForProcessing("Value 1");
        option.addValueForProcessing("Value 2");
        Assert.assertEquals("Value 1", option.getValue(0));
        Assert.assertEquals("Value 2", option.getValue(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionGetValueByIndexOutOfBounds() {
        Option option = new Option("value-option", null, false, "Option A");
        option.addValueForProcessing("Value 1");
        Assert.assertEquals("Value 1", option.getValue(0));
        Assert.assertEquals(0, option.values.size());
        Assert.assertNull(option.getValue(1));
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("values-option", null, false, "Option A");
        option.addValueForProcessing("Value 1");
        option.addValueForProcessing("Value 2");
        Assert.assertArrayEquals(new String[]{"Value 1", "Value 2"}, option.getValues());
    }

    @Test
    public void testOptionGetValuesList() {
        Option option = new Option("values-option", null, false, "Option A");
        option.addValueForProcessing("Value 1");
        option.addValueForProcessing("Value 2");
        Assert.assertEquals(option.values, option.getValuesList());
    }

    @Test(expected = ArrayStoreException.class)
    public void testOptionInvalidAddValue() {
        Option option = new Option("invalid-addvalue-option", null, false, "Option A");
        option.addValue("Invalid Value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValueUnsupported() {
        Option option = new Option("addvalue-option", null, false, "Option A");
        option.addValue("Value 1");
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("clearvalues-option", null, false, "Option A");
        option.addValueForProcessing("Value 1");
        option.clearValues();
        Assert.assertEquals(0, option.values.size());
    }
}