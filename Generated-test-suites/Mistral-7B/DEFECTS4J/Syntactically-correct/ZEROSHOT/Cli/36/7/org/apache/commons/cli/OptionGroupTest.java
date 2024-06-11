import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest {

    /**
     * Test method for the constructor, and the getNames() and getOptions() methods.
     */
    @Test
    public void testOptionGroup() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        Option option3 = new Option("o3", "option3", false, "Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        Set<String> names = new HashSet<>(group.getNames());
        Assert.assertEquals(3, names.size());
        Assert.assertTrue(names.containsAll(Arrays.asList("o1", "o2", "o3")));

        Set<Option> options = new HashSet<>(group.getOptions());
        Assert.assertEquals(3, options.size());
        Assert.assertTrue(options.containsAll(Arrays.asList(option1, option2, option3)));
    }

    /**
     * Test method for the setSelected() method with no option selected.
     */
    @Test
    public void testSetSelectedNoOption() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(null);

        Assert.assertNull(group.getSelected());
    }

    /**
     * Test method for the setSelected() method with an option already selected.
     */
    @Test(expected = org.apache.commons.cli.AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);
    }

    /**
     * Test method for the setSelected() method with a null option.
     */
    @Test
    public void testSetSelectedNullOption() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "Option 1");

        group.addOption(option1);

        group.setSelected(null);

        Assert.assertNull(group.getSelected());
    }

    /**
     * Test method for the setSelected() method with a valid option.
     */
    @Test
    public void testSetSelectedValidOption() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        Assert.assertEquals("o1", group.getSelected());
    }

    /**
     * Test method for the isRequired() method with a required group.
     */
    @Test
    public void testIsRequiredTrue() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);

        Assert.assertTrue(group.isRequired());
    }

    /**
     * Test method for the isRequired() method with a non-required group.
     */
    @Test
    public void testIsRequiredFalse() {
        OptionGroup group = new OptionGroup();
        group.setRequired(false);

        Assert.assertFalse(group.isRequired());
    }

    /**
     * Test method for the toString() method.
     */
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        Assert.assertEquals("[--option1, --option2]", group.toString());
    }
}