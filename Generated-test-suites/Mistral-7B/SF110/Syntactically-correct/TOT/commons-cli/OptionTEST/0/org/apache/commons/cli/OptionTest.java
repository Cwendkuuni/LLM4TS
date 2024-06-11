import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionCreation() {
        // Test creating an option with no arguments
        Option option1 = new Option("optA", "Description for option A");
        Assert.assertEquals("optA", option1.getOpt());
        Assert.assertNull(option1.getLongOpt());
        Assert.assertFalse(option1.hasArg());
        Assert.assertNull(option1.getValue());
        Assert.assertEquals("Description for option A", option1.getDescription());
        Assert.assertFalse(option1.isRequired());
        Assert.assertFalse(option1.hasOptionalArg());
        Assert.assertFalse(option1.hasLongOpt());
        Assert.assertFalse(option1.hasArgName());
        Assert.assertFalse(option1.hasArgs());
        Assert.assertEquals(0, option1.getArgs());
        Assert.assertNotNull(option1.toString());
        Assert.assertFalse(option1.equals(null));
        Assert.assertTrue(option1.equals(option1));

        // Test creating an option with an argument
        Option option2 = new Option("optB", "Description for option B", true, "arg");
        Assert.assertEquals("optB", option2.getOpt());
        Assert.assertNull(option2.getLongOpt());
        Assert.assertTrue(option2.hasArg());
        Assert.assertEquals(1, option2.getArgs());
        Assert.assertNull(option2.getValue());
        Assert.assertEquals("arg", option2.getArgName());
        Assert.assertEquals("Description for option B", option2.getDescription());
        Assert.assertFalse(option2.isRequired());
        Assert.assertTrue(option2.hasOptionalArg());
        Assert.assertFalse(option2.hasLongOpt());
        Assert.assertFalse(option2.hasValueSeparator());
        Assert.assertFalse(option2.hasArgs());
        Assert.assertEquals(0, option2.hashCode());
        Assert.assertEquals(option2, option2);

        // Test creating an option with a long option
        Option option3 = new Option("optC", "longOptC", false, "arg", "Long option C");
        Assert.assertEquals("optC", option3.getOpt());
        Assert.assertEquals("longOptC", option3.getLongOpt());
        Assert.assertFalse(option3.hasArg());
        Assert.assertEquals(0, option3.getArgs());
        Assert.assertNull(option3.getValue());
        Assert.assertEquals("Long option C", option3.getDescription());
        Assert.assertFalse(option3.isRequired());
        Assert.assertFalse(option3.hasOptionalArg());
        Assert.assertTrue(option3.hasLongOpt());
        Assert.assertFalse(option3.hasArgName());
        Assert.assertFalse(option3.hasArgs());
        Assert.assertEquals(option3.hashCode(), option3.hashCode());
        Assert.assertEquals(option3, option3);

        // Test creating an option with multiple values
        Option option4 = new Option("optD", "Description for option D", false, null, "Option D");
        option4.setArgs(2);
        option4.setValueSeparator(' ');
        option4.addValueForProcessing("value1");
        option4.addValueForProcessing("value2");
        Assert.assertEquals(3, option4.getArgs());
        Assert.assertEquals(2, option4.values.size());
        Assert.assertEquals("value1 value2", option4.getValue());
        Assert.assertEquals("Description for option D", option4.getDescription());
        Assert.assertFalse(option4.isRequired());
        Assert.assertFalse(option4.hasOptionalArg());
        Assert.assertFalse(option4.hasLongOpt());
        Assert.assertFalse(option4.hasArgName());
        Assert.assertTrue(option4.hasArgs());
        Assert.assertTrue(option4.hasValueSeparator());
        Assert.assertEquals(' ', option4.getValueSeparator());
        Assert.assertEquals(option4.hashCode(), option4.hashCode());
        Assert.assertEquals(option4, option4);
    }

    @Test
    public void testOptionEquality() {
        Option option1 = new Option("optA", "Description for option A");
        Option option2 = new Option("optA", "Description for option A");

        Assert.assertEquals(option1, option2);
        Assert.assertEquals(option1.hashCode(), option2.hashCode());

        option2.setArgName("arg2");
        Assert.assertNotEquals(option1, option2);
        Assert.assertNotEquals(option1.hashCode(), option2.hashCode());

        option1.setArgName("arg2");
        Assert.assertEquals(option1, option2);
        Assert.assertEquals(option1.hashCode(), option2.hashCode());

        option1.setDescription("Description for option A 2");
        Assert.assertNotEquals(option1, option2);
        Assert.assertNotEquals(option1.hashCode(), option2.hashCode());

        option2.setDescription("Description for option A 2");
        Assert.assertEquals(option1, option2);
        Assert.assertEquals(option1.hashCode(), option2.hashCode());

        option1.setLongOpt("longOptA");
        Assert.assertNotEquals(option1, option2);
        Assert.assertNotEquals(option1.hashCode(), option2.hashCode());

        option2.setLongOpt("longOptA");
        Assert.assertEquals(option1, option2);
        Assert.assertEquals(option1.hashCode(), option2.hashCode());

        option1.setType(String.class);
        Assert.assertNotEquals(option1, option2);
        Assert.assertNotEquals(option1.hashCode(), option2.hashCode());

        option2.setType(String.class);
        Assert.assertEquals(option1, option2);
        Assert.assertEquals(option1.hashCode(), option2.hashCode());

        option1.clearValues();
        Assert.assertNotEquals(option1, option2);
        Assert.assertNotEquals(option1.hashCode(), option2.hashCode());

        option2.clearValues();
        Assert.assertEquals(option1, option2);
        Assert.assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueThrowsException() {
        Option option = new Option("optE", "Description for option E");
        option.addValue("value");
    }
}