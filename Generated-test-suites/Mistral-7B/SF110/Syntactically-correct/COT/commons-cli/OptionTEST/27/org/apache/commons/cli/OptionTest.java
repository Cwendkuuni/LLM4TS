import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionWithEmptyOptAndDescription() {
        Option option = new Option("", "Description");
        Assert.assertNotNull(option);
    }

    @Test
    public void testOptionWithSingleCharOptAndDescription() {
        Option option = new Option("a", "Description");
        Assert.assertNotNull(option);
    }

    @Test
    public void testOptionWithLongOptAndDescription() {
        Option option = new Option("--longOption", "Description");
        Assert.assertNotNull(option);
    }

    @Test
    public void testOptionWithHasArgTrue() {
        Option option = new Option("b", "Description", true, "");
        Assert.assertTrue(option.hasArg());
    }

    @Test
    public void testOptionWithHasArgFalse() {
        Option option = new Option("c", "Description", false, "");
        Assert.assertFalse(option.hasArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidOpt() {
        Option option = new Option("", "Description"); // Empty opt should not be allowed
        Assert.fail();
    }

    @Test
    public void testOptionWithHasOptionalArgTrue() {
        Option option = new Option("d", "Description", true, "");
        option.setOptionalArg(true);
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionWithHasOptionalArgFalse() {
        Option option = new Option("e", "Description", false, "");
        option.setOptionalArg(false);
        Assert.assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testOptionWithHasLongOptTrue() {
        Option option = new Option("f", "", false, "Long Option");
        Assert.assertTrue(option.hasLongOpt());
    }

    @Test
    public void testOptionWithHasLongOptFalse() {
        Option option = new Option("g", "Description");
        Assert.assertFalse(option.hasLongOpt());
    }

    @Test
    public void testOptionWithHasDescriptionTrue() {
        Option option = new Option("h", "Description");
        Assert.assertTrue(option.hasDescription());
    }

    @Test
    public void testOptionWithNoDescription() {
        Option option = new Option("i", "");
        Assert.assertFalse(option.hasDescription());
    }

    @Test
    public void testOptionWithHasTypeTrue() {
        Option option = new Option("j", "", false, "", new Integer(5));
        Assert.assertNotNull(option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidType() {
        Option option = new Option("k", "", false, "", new String("InvalidType"));
        Assert.fail();
    }

    @Test
    public void testOptionWithValues() {
        Option option = new Option("l", "Option with values", false, "", new String[]{"value1", "value2"});
        Assert.assertNotNull(option.getValues());
        Assert.assertEquals(2, option.getValues().size());
        Assert.assertEquals("value1", option.getValues().get(0));
        Assert.assertEquals("value2", option.getValues().get(1));
    }

    @Test
    public void testOptionWithValueSeparator() {
        Option option = new Option("m", "Option with value separator", false, "", "", ';');
        option.setValueSeparator(';');
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        option.setValues(values);
        Assert.assertEquals(2, option.getValues().size());
        Assert.assertEquals("value1;value2", option.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionWithInvalidIndex() {
        Option option = new Option("n", "Description", false, "");
        option.setValues(new ArrayList<>());
        option.addValue("Value1");
        Assert.assertArrayEquals(new String[]{"Value1"}, option.getValues());
        try {
            option.getValue(1);
            Assert.fail();
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testOptionWithEquals() {
        Option option1 = new Option("o", "Description1");
        Option option2 = new Option("o", "Description2");
        Assert.assertFalse(option1.equals(option2));

        Option option3 = new Option("o", "Description1");
        Assert.assertTrue(option1.equals(option3));
    }

    @Test
    public void testOptionWithHashCode() {
        Option option1 = new Option("p", "Description1");
        Option option2 = new Option("p", "Description2");
        Assert.assertFalse(option1.hashCode() == option2.hashCode());

        Option option3 = new Option("p", "Description1");
        Assert.assertTrue(option1.hashCode() == option3.hashCode());
    }
}