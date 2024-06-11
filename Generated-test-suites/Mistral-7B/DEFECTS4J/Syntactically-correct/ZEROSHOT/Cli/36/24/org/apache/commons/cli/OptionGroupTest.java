import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for OptionGroup.java
 */
public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getOptions().size());
        Assert.assertTrue(optionGroup.getOptions().contains(option1));
        Assert.assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getNames().size());
        Assert.assertTrue(optionGroup.getNames().contains("o1"));
        Assert.assertTrue(optionGroup.getNames().contains("o2"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals("o1", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // expected
        }

        try {
            optionGroup.setSelected(option2);
            Assert.assertEquals("o2", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // expected
        }

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals("o1", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // not expected
            Assert.fail();
        }
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals("[--option1, --option2]", optionGroup.toString());
    }
}