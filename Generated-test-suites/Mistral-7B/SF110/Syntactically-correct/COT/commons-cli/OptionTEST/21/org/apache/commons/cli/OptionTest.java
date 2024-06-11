import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionConstructorWithOptAndDescription() throws IllegalArgumentException {
        Option option = new Option("test", "Test option", false);
        Assert.assertEquals("test", option.getOpt());
        Assert.assertEquals("Test option", option.getDescription());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.hasArg());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertTrue(option.hasNoValues());
    }

    @Test
    public void testOptionConstructorWithOptLongAndDescription() throws IllegalArgumentException {
        Option option = new Option("test", "Test long option", false, "long-test");
        Assert.assertEquals("test", option.getOpt());
        Assert.assertEquals("Test long option", option.getDescription());
        Assert.assertEquals("long-test", option.getLongOpt());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.hasArg());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertTrue(option.hasLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertTrue(option.hasNoValues());
    }

    @Test
    public void testOptionConstructorWithOptHasArgAndDescription() throws IllegalArgumentException {
        Option option = new Option("test", "Test option", true, "arg-test");
        Assert.assertEquals("test", option.getOpt());
        Assert.assertEquals("Test option", option.getDescription());
        Assert.assertNull(option.getLongOpt());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertTrue(option.hasArg());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertTrue(option.hasNoValues());
    }

    @Test
    public void testOptionConstructorWithOptLongHasArgAndDescription() throws IllegalArgumentException {
        Option option = new Option("test", "Test long option", true, "long-arg-test", "arg");
        Assert.assertEquals("test", option.getOpt());
        Assert.assertEquals("Test long option", option.getDescription());
        Assert.assertEquals("long-arg-test", option.getLongOpt());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertTrue(option.hasArg());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertTrue(option.hasLongOpt());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArgs());
        Assert.assertTrue(option.hasNoValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidOpt() throws IllegalArgumentException {
        new Option(null, "Test option", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidDescription() throws IllegalArgumentException {
        new Option("test", null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidHasArg() throws IllegalArgumentException {
        new Option("test", "Test option", -1, "arg-test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidArgName() throws IllegalArgumentException {
        new Option("test", "Test option", true, null, "arg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithEmptyDescription() throws IllegalArgumentException {
        new Option("test", "", false);
    }

    @Test
    public void testOptionGetId() {
        Option option = new Option("test", "Test option", false);
        Assert.assertEquals('t', option.getId());
    }

    @Test
    public void testOptionSetType() {
        Option option = new Option("test", "Test option", false);
        option.setType(String.class);
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testOptionGetType() {
        Option option = new Option("test", "Test option", false);
        option.setType(String.class);
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testOptionSetLongOpt() {
        Option option = new Option("test", "Test option", false);
        option.setLongOpt("long-test");
        Assert.assertEquals("long-test", option.getLongOpt());
    }

    @Test
    public void testOptionSetOptionalArg() {
        Option option = new Option("test", "Test option", false);
        option.setOptionalArg(true);
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionSetArgName() {
        Option option = new Option("test", "Test option", false);
        option.setArgName("arg");
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionSetArgs() {
        Option option = new Option("test", "Test option", false);
        option.setArgs(2);
        Assert.assertEquals(2, option.getArgs());
    }

    @Test
    public void testOptionSetValueSeparator() {
        Option option = new Option("test", "Test option", false);
        option.setValueSeparator(';');
        Assert.assertEquals(';', option.getValueSeparator());
    }

    @Test
    public void testOptionHasValueSeparator() {
        Option option = new Option("test", "Test option", false);
        option.setValueSeparator(';');
        Assert.assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testOptionGetArgs() {
        Option option = new Option("test", "Test option", false);
        Assert.assertEquals(-1, option.getArgs());
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("test", "Test option", false);
        List values = new ArrayList();
        option.values = values;
        option.addValue("value1");
        option.addValue("value2");
        option.clearValues();
        Assert.assertTrue(values.isEmpty());
    }

    @Test
    public void testOptionAddValueForProcessing() {
        Option option = new Option("test", "Test option", false);
        option.addValueForProcessing("value1");
        List values = option.values;
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueForProcessingWithNoArgs() {
        Option option = new Option("test", "Test option", false);
        option.addValueForProcessing("value1");
    }

    @Test
    public void testOptionProcessValue() {
        Option option = new Option("test", "Test option", false, null, null);
        option.setValueSeparator(';');
        option.processValue("value1;value2;value3");
        List values = option.values;
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));
    }

    @Test
    public void testOptionAdd() {
        Option option = new Option("test", "Test option", false, null, null);
        option.addValue("value1");
        option.addValue("value2");
        Assert.assertEquals(2, option.values.size());
        Assert.assertEquals("value1", option.values.get(0));
        Assert.assertEquals("value2", option.values.get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddWithNoArgs() {
        Option option = new Option("test", "Test option", false);
        option.addValue("value1");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionGetValueWithNoneValues() {
        Option option = new Option("test", "Test option", false);
        Assert.assertNull(option.getValue());
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("test", "Test option", false, null, null);
        option.addValue("value1");
        Assert.assertEquals("value1", option.getValue());
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("test", "Test option", false, null, null);
        option.addValue("value1");
        option.addValue("value2");
        String[] values = option.getValues();
        Assert.assertNotNull(values);
        Assert.assertEquals(2, values.length);
        Assert.assertEquals("value1", values[0]);
        Assert.assertEquals("value2", values[1]);
    }

    @Test
    public void testOptionGetValuesList() {
        Option option = new Option("test", "Test option", false, null, null);
        option.addValue("value1");
        option.addValue("value2");
        Assert.assertEquals(option.values, option.getValuesList());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("test", "Test option", false, null, null);
        Assert.assertEquals("[ option: test :: Test option ]", option.toString());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("test1", "Test option1", false);
        Option option2 = new Option("test2", "Test option2", false);
        Option option3 = new Option("test1", "Test option1", false);
        Option option4 = new Option("test1", "Test option1", false);
        Assert.assertFalse(option1.equals(option2));
        Assert.assertTrue(option1.equals(option3));
        Assert.assertTrue(option1.equals(option4));
        Assert.assertFalse(option1.equals(null));
        Assert.assertFalse(option1.equals(""));
        Assert.assertFalse(option1.equals(1));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("test1", "Test option1", false);
        Option option2 = new Option("test2", "Test option2", false);
        Option option3 = new Option("test1", "Test option1", false);
        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();
        int hashCode3 = option3.hashCode();
        Assert.assertEquals(hashCode1, hashCode3);
        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("test", "Test option", false);
        Option clonedOption = option.clone();
        Assert.assertEquals(option, clonedOption);
    }
}