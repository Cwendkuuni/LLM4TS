import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithSingleArgument() throws IllegalArgumentException {
        Option option = new Option("a", "Option A", false);
        assertEquals("a", option.getOpt());
        assertEquals("Option A", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getLongOpt());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasNoValues());
        assertFalse(option.equals(null));
        assertEquals(option, option);
        assertNotEquals(option, "Test");
    }

    @Test
    public void testOptionConstructorWithLongOptionAndSingleArgument() throws IllegalArgumentException {
        Option option = new Option("a", "long-option-a", false, "arg");
        assertEquals("a", option.getOpt());
        assertEquals("long-option-a", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals("Option A", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasNoValues());
        assertFalse(option.equals(null));
        assertEquals(option, option);
        assertNotEquals(option, "Test");
    }

    @Test
    public void testOptionConstructorWithLongOptionAndMultipleArguments() throws IllegalArgumentException {
        Option option = new Option("a", "long-option-a", true, "arg1 arg2");
        assertEquals("a", option.getOpt());
        assertEquals("long-option-a", option.getLongOpt());
        assertEquals("arg1 arg2", option.getArgName());
        assertEquals("Option A", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.hasArgs());
        assertNull(option.getType());
        assertEquals(2, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasNoValues());
        assertFalse(option.equals(null));
        assertEquals(option, option);
        assertNotEquals(option, "Test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructor() throws IllegalArgumentException {
        new Option(null, "Description");
    }

    @Test
    public void testGetId() {
        Option option = new Option("a", "Description");
        assertEquals('a', option.getId());
    }

    @Test
    public void testGetKey() {
        Option option = new Option("a", "Description");
        assertEquals("a", option.getKey());
        Option optionLong = new Option("a", "long-option-a", false, "arg");
        assertEquals("a", option.getKey());
        assertEquals("long-option-a", optionLong.getKey());
    }

    @Test
    public void testGetOpt() {
        Option option = new Option("a", "Description");
        assertEquals("a", option.getOpt());
        Option optionLong = new Option("a", "long-option-a", false, "arg");
        assertEquals("a", option.getOpt());
        assertEquals(null, optionLong.getOpt());
    }

    @Test
    public void testSetType() {
        Option option = new Option("a", "Description");
        assertNull(option.getType());
        option.setType("String");
        assertEquals("String", option.getType());
    }

    @Test
    public void testGetType() {
        Option option = new Option("a", "Description");
        assertNull(option.getType());
        Option optionType = new Option("a", "Description");
        optionType.setType("String");
        assertEquals("String", optionType.getType());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("a", "Description");
        assertNull(option.getLongOpt());
        option.setLongOpt("long-option-a");
        assertEquals("long-option-a", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("a", "Description");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = new Option("a", "Description");
        assertFalse(option.hasOptionalArg());
        Option optionOptional = new Option("a", "Description");
        optionOptional.setOptionalArg(true);
        assertTrue(optionOptional.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("a", "Description");
        assertFalse(option.hasLongOpt());
        Option optionLong = new Option("a", "long-option-a", false, "arg");
        assertTrue(optionLong.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("a", "Description");
        assertFalse(option.hasArg());
        Option optionArg = new Option("a", "Description");
        optionArg.setArgName("arg");
        assertTrue(optionArg.hasArg());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option("a", "Description");
        assertEquals("Description", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("a", "Description");
        assertEquals("Description", option.getDescription());
        option.setDescription("New Description");
        assertEquals("New Description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("a", "Description");
        assertFalse(option.isRequired());
        Option optionRequired = new Option("a", "Description");
        optionRequired.setRequired(true);
        assertTrue(optionRequired.isRequired());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("a", "Description");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("a", "Description");
        assertNull(option.getArgName());
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testGetArgName() {
        Option option = new Option("a", "Description");
        assertNull(option.getArgName());
        Option optionArgName = new Option("a", "Description");
        optionArgName.setArgName("argName");
        assertEquals("argName", optionArgName.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("a", "Description");
        assertFalse(option.hasArgName());
        Option optionArgName = new Option("a", "Description");
        optionArgName.setArgName("argName");
        assertTrue(optionArgName.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("a", "Description");
        assertFalse(option.hasArgs());
        Option optionArg = new Option("a", "Description");
        optionArg.setArgName("arg1 arg2");
        assertTrue(optionArg.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option option = new Option("a", "Description");
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithInvalidNumberOfArguments() {
        Option option = new Option("a", "Description");
        option.addValue("Value");
    }

    @Test
    public void testAddValueForProcessingWithSingleValue() {
        Option option = new Option("a", "Description");
        option.addValueForProcessing("Value");
        assertEquals(1, option.getValues().size());
        assertEquals("Value", option.getValues().get(0));
    }

    @Test
    public void testAddValueForProcessingWithMultipleValues() {
        Option option = new Option("a", "Description");
        option.addValueForProcessing("Value1");
        option.addValueForProcessing("Value2");
        assertEquals(2, option.getValues().size());
        assertEquals("Value1", option.getValues().get(0));
        assertEquals("Value2", option.getValues().get(1));
    }

    @Test
    public void testGetValue() {
        Option option = new Option("a", "Description");
        option.addValue("Value1");
        assertEquals("Value1", option.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        Option option = new Option("a", "Description");
        option.addValue("Value1");
        option.addValue("Value2");
        option.getValue(2);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("a", "Description");
        option.addValue("Value1");
        option.addValue("Value2");
        String[] values = option.getValues();
        assertEquals(2, values.length);
        assertEquals("Value1", values[0]);
        assertEquals("Value2", values[1]);
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("a", "Description");
        option.addValue("Value1");
        option.addValue("Value2");
        List valuesList = option.getValuesList();
        assertEquals(2, valuesList.size());
        assertEquals("Value1", valuesList.get(0));
        assertEquals("Value2", valuesList.get(1));
    }

    @Test
    public void testToString() {
        Option option = new Option("a", "Description");
        assertEquals("[ option: a :: Description ]", option.toString());
    }

    @Test
    public void testClearValues() {
        Option option = new Option("a", "Description");
        option.addValue("Value1");
        option.addValue("Value2");
        option.clearValues();
        assertTrue(option.getValues().isEmpty());
    }
}