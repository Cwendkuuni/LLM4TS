import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.Option;
import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("a", "optionA", false, "This is option A");
        assertEquals("a", option.getOpt());
        assertEquals("optionA", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("This is option A", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(0, option.getArgs());
        assertEquals(Option.UNINITIALIZED, option.getId());
        assertNotNull(option.getKey());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertEquals("", option.toString());
        assertTrue(option.equals(option));
        int hashCode = option.hashCode();
        Option option2 = new Option("a", "optionA", false, "This is option A");
        assertEquals(hashCode, option2.hashCode());
        assertTrue(option.equals(option2));
        Option option3 = new Option("b", "optionB", true, "This is option B");
        assertFalse(option.equals(option3));
    }

    @Test
    public void testOptionWithArg() {
        Option option = new Option("a", "optionA", true, "This is option A with argument");
        assertEquals("a", option.getOpt());
        assertEquals("optionA", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("This is option A with argument", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertEquals('A', option.getId());
        assertNotNull(option.getKey());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertEquals("optionA [ARG]", option.toString());
        Option option2 = new Option("a", "optionA", true, "This is option A with argument");
        assertTrue(option.equals(option2));
        Option option3 = new Option("b", "optionB", true, "This is option B with argument");
        assertFalse(option.equals(option3));

        try {
            option.addValueForProcessing("test");
            fail("Should not be able to add a value to an option with no values allowed");
        } catch (RuntimeException e) {
            // Expected exception
        }

        List<String> values = new ArrayList<>();
        Option option4 = new Option("a", "optionA", true, "This is option A with argument");
        option4.setValues(values);
        option4.addValueForProcessing("value1");
        option4.addValueForProcessing("value2");
        assertEquals(2, option4.getValues().size());
        assertEquals("value1", option4.getValue(0));
        assertEquals("value2", option4.getValue(1));
        assertEquals("value1 value2", option4.toString());
    }

    @Test
    public void testOptionWithArgName() {
        Option option = new Option("a", "optionA", true, "This is option A with argument [ARG_NAME]");
        assertEquals("a", option.getOpt());
        assertEquals("optionA", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("This is option A with argument [ARG_NAME]", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals("ARG_NAME", option.getArgName());
        assertTrue(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertEquals('A', option.getId());
        assertNotNull(option.getKey());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertEquals("optionA [ARG_NAME]", option.toString());
        Option option2 = new Option("a", "optionA", true, "This is option A with argument [ARG_NAME]");
        assertTrue(option.equals(option2));
        Option option3 = new Option("b", "optionB", true, "This is option B with argument [ARG_NAME]");
        assertFalse(option.equals(option3));

        try {
            option.addValueForProcessing("test");
            fail("Should not be able to add a value to an option with no values allowed");
        } catch (RuntimeException e) {
            // Expected exception
        }

        List<String> values = new ArrayList<>();
        Option option4 = new Option("a", "optionA", true, "This is option A with argument [ARG_NAME]");
        option4.setValues(values);
        option4.setArgName("name");
        option4.addValueForProcessing("value1");
        option4.addValueForProcessing("value2");
        assertEquals(2, option4.getValues().size());
        assertEquals("value1", option4.getValue(0));
        assertEquals("value2", option4.getValue(1));
        assertEquals("value1 value2", option4.toString());
    }

    @Test
    public void testOptionWithMultipleValues() {
        Option option = new Option("a", "optionA", -2, "This is option A with multiple values");
        assertEquals("a", option.getOpt());
        assertEquals("optionA", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("This is option A with multiple values", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertTrue(option.hasArgs());
        assertEquals(0, option.getArgs());
        assertEquals('A', option.getId());
        assertNotNull(option.getKey());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertEquals("optionA", option.toString());
        Option option2 = new Option("a", "optionA", -2, "This is option A with multiple values");
        assertTrue(option.equals(option2));
        Option option3 = new Option("b", "optionB", -2, "This is option B with multiple values");
        assertFalse(option.equals(option3));

        List<String> values = new ArrayList<>();
        Option option4 = new Option("a", "optionA", -2, "This is option A with multiple values");
        option4.setValues(values);
        option4.setValueSeparator(';');
        option4.addValueForProcessing("value1");
        option4.addValueForProcessing("value2");
        assertEquals(2, option4.getValues().size());
        assertEquals("value1;value2", option4.toString());
        assertEquals("value1", option4.getValue(0));
        assertEquals("value2", option4.getValue(1));
        Option option5 = new Option("a", "optionA", -2, "This is option A with multiple values");
        option5.setValues(values);
        option5.setValueSeparator(';');
        option5.addValueForProcessing("value1");
        option5.addValueForProcessing("value2");
        assertTrue(option4.equals(option5));

        Option option6 = new Option("a", "optionA", -2, "This is option A with multiple values");
        option6.setValues(values);
        option6.setValueSeparator(';');
        option6.addValueForProcessing("value1");
        option6.addValueForProcessing("value3");
        assertFalse(option4.equals(option6));
    }
}