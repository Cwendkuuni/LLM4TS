import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionCreation() {
        Option option = new Option("a", "arg", true, "This is an option with an argument");
        Assert.assertEquals("a", option.getOpt());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals("This is an option with an argument", option.getDescription());
    }

    @Test
    public void testOptionCreationWithoutArg() {
        Option option = new Option("b", "noArg", false, "This is an option without an argument");
        Assert.assertEquals("b", option.getOpt());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals("This is an option without an argument", option.getDescription());
    }

    @Test
    public void testOptionCreationWithLongOpt() {
        Option option = new Option("c", "longOpt", false, "This is an option with a long option name");
        Assert.assertEquals("c", option.getOpt());
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals("This is an option with a long option name", option.getDescription());
    }

    @Test
    public void testOptionCreationWithArgAndLongOpt() {
        Option option = new Option("d", "longOpt", true, "This is an option with an argument and a long option name");
        Assert.assertEquals("d", option.getOpt());
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals("This is an option with an argument and a long option name", option.getDescription());
    }

    @Test
    public void testOptionCreationWithOptionalArg() {
        Option option = new Option("e", null, true, "This is an optional option with an argument");
        Assert.assertEquals("e", option.getOpt());
        Assert.assertNull(option.getArgName());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals("This is an optional option with an argument", option.getDescription());
    }

    @Test
    public void testOptionCreationWithValue() {
        Option option = new Option("f", null, false, "This is an option with a value", Integer.TYPE);
        Assert.assertEquals("f", option.getOpt());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals("This is an option with a value", option.getDescription());
        Assert.assertEquals(Integer.TYPE, option.getType());
    }

    @Test
    public void testOptionCreationWithValues() {
        Option option = new Option("g", null, false, "This is an option with multiple values", String[].class);
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        option.setValues(expectedValues.toArray(new String[expectedValues.size()]));
        Assert.assertEquals("g", option.getOpt());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals("This is an option with multiple values", option.getDescription());
        Assert.assertEquals(String[].class, option.getType());
        Assert.assertNotNull(option.getValuesList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidOptionName() {
        new Option(" ", null, false, "Invalid option name");
    }
}