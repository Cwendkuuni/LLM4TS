import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithOptAndDescription() throws IllegalArgumentException {
        Option option = new Option("a", "option A", false, "Description for option A");
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getLongOpt());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(-1, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.equals(null));
        assertFalse(option.equals("not an Option object"));
        assertTrue(option.equals(option));
        assertEquals(option.hashCode(), option.hashCode());
        Option cloneOption = option.clone();
        assertNotSame(option, cloneOption);
        assertEquals(option, cloneOption);
        assertEquals(option.hashCode(), cloneOption.hashCode());
    }

    @Test
    public void testOptionConstructorWithOptLongOptHasArgDescription() throws IllegalArgumentException {
        Option option = new Option("a", "longOptionA", true, "Description for option A", true);
        assertEquals("a", option.getOpt());
        assertEquals("longOptionA", option.getLongOpt());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.equals(null));
        assertFalse(option.equals("not an Option object"));
        assertTrue(option.equals(option));
        assertEquals(option.hashCode(), option.hashCode());
        Option cloneOption = option.clone();
        assertNotSame(option, cloneOption);
        assertEquals(option, cloneOption);
        assertEquals(option.hashCode(), cloneOption.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidOpt() throws IllegalArgumentException {
        new Option("", "Description for option A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidHasArg() {
        try {
            new Option("a", "", true, "Description for option A");
        } catch (IllegalArgumentException iae) {
            assertEquals("The option argument 'hasArg' should be either true or false", iae.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidDescription() {
        try {
            new Option("a", "", false, "");
        } catch (IllegalArgumentException iae) {
            assertEquals("Option description cannot be an empty string", iae.getMessage());
        }
    }

    @Test
    public void testOptionGetId() {
        Option option = new Option("a", "Description for option A");
        assertEquals('a', option.getId());
    }

    @Test
    public void testOptionGetKey() {
        Option option = new Option("a", "Description for option A");
        assertEquals("a", option.getKey());
        Option optionWithLongOpt = new Option("a", "longOptionA", false, "Description for option A");
        assertEquals("a", option.getKey());
        assertEquals("longOptionA", optionWithLongOpt.getKey());
    }

    @Test
    public void testOptionGetOpt() {
        Option option = new Option("a", "Description for option A");
        assertEquals("a", option.getOpt());
    }

    @Test
    public void testOptionGetLongOpt() {
        Option option = new Option("a", "Description for option A");
        assertNull(option.getLongOpt());
        Option optionWithLongOpt = new Option("a", "longOptionA", false, "Description for option A");
        assertEquals("longOptionA", optionWithLongOpt.getLongOpt());
    }

    @Test
    public void testOptionSetLongOpt() {
        Option option = new Option("a", "Description for option A");
        assertNull(option.getLongOpt());
        option.setLongOpt("longOption");
        assertEquals("longOption", option.getLongOpt());
    }

    @Test
    public void testOptionSetOptionalArgFalse() {
        Option option = new Option("a", "Description for option A");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(false);
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testOptionSetOptionalArgTrue() {
        Option option = new Option("a", "Description for option A");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValue() {
        Option option = new Option("a", "Description for option A");
        option.addValue("value");
    }

    @Test
    public void testOptionAddValueForProcessing() {
        Option option = new Option("a", "Description for option A");
        option.setArgs(1);
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
        option.clearValues();
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        String[] values = option.getValues();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueForProcessingWithNoArgs() {
        Option option = new Option("a", "Description for option A");
        option.addValueForProcessing("value1");
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueForProcessingWithTooManyValues() {
        Option option = new Option("a", "Description for option A");
        option.setArgs(1);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("a", "Description for option A");
        option.setValue("value");
        assertEquals("value", option.getValue());
        option.clearValues();
        assertEquals(null, option.getValue());
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("a", "Description for option A");
        option.setValue("value1");
        option.setValue("value2");
        String[] values = option.getValues();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        option.clearValues();
        String[] emptyValues = option.getValues();
        assertEquals(0, emptyValues.length);
    }

    @Test
    public void testOptionGetValuesList() {
        Option option = new Option("a", "Description for option A");
        option.setValue("value1");
        option.setValue("value2");
        List valuesList = option.getValuesList();
        assertEquals(2, valuesList.size());
        assertEquals("value1", valuesList.get(0));
        assertEquals("value2", valuesList.get(1));
        option.clearValues();
        List emptyValuesList = option.getValuesList();
        assertEquals(0, emptyValuesList.size());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("a", "Description for option A");
        assertEquals("[ option: a :: Description for option A ]", option.toString());
        Option optionWithLongOpt = new Option("a", "longOptionA", false, "Description for option A");
        assertEquals("[ option: a  longOptionA :: Description for option A ]", optionWithLongOpt.toString());
        Option optionWithArg = new Option("a", "Description for option A", true, "Description for option A");
        assertEquals("[ option: a [ARG] :: Description for option A ]", optionWithArg.toString());
        Option optionWithArgs = new Option("a", "Description for option A", true, "Description for option A", 2);
        assertEquals("[ option: a [ARG1] [ARG2] :: Description for option A ]", optionWithArgs.toString());
        Option optionWithValue = new Option("a", "Description for option A", false, "Description for option A", "value");
        assertEquals("[ option: a :: Description for option A value ]", optionWithValue.toString());
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("a", "Description for option A");
        option.setValue("value1");
        option.setValue("value2");
        option.clearValues();
        assertEquals(null, option.getValue());
        String[] values = option.getValues();
        assertEquals(0, values.length);
        List valuesList = option.getValuesList();
        assertEquals(0, valuesList.size());
    }
}