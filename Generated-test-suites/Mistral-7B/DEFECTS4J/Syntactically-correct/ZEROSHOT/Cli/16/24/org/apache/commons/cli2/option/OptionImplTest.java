package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionImplTest {

    @Test
    public void testOptionImplConstruction() {
        Option option = new OptionImpl(1, true);
        Assert.assertEquals(1, option.getId());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcess() {
        Option option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("testArgument").listIterator();

        Assert.assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCanProcessWhenArgumentsAreEmpty() {
        Option option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList().listIterator();

        option.canProcess(commandLine, arguments);
    }

    @Test
    public void testToString() {
        Option option = new OptionImpl(1, true);
        String expected = "OptionImpl[id=1, required=true]";
        Assert.assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new OptionImpl(1, true);
        Option option2 = new OptionImpl(1, true);
        Option option3 = new OptionImpl(2, true);

        Assert.assertTrue(option1.equals(option1));
        Assert.assertTrue(option1.equals(option2));
        Assert.assertFalse(option1.equals(option3));
        Assert.assertFalse(option1.equals("testString"));
    }

    @Test
    public void testHashCode() {
        Option option1 = new OptionImpl(1, true);
        Option option2 = new OptionImpl(1, true);
        Option option3 = new OptionImpl(2, true);

        Assert.assertEquals(option1.hashCode(), option1.hashCode());
        Assert.assertEquals(option1.hashCode(), option2.hashCode());
        Assert.assertNotEquals(option1.hashCode(), option3.hashCode());
        Assert.assertNotEquals(option1.hashCode(), "testString".hashCode());
    }

    @Test
    public void testFindOption() {
        Option option1 = new OptionImpl(1, true);
        Option option2 = new OptionImpl(2, true);
        Option option3 = new OptionImpl(1, true);

        Set<String> triggers1 = new HashSet<>(Arrays.asList("t1", "t2"));
        Set<String> triggers2 = new HashSet<>(Arrays.asList("t2", "t3"));
        Set<String> triggers3 = new HashSet<>(Arrays.asList("t1", "t2"));

        option1.setTriggers(triggers1);
        option2.setTriggers(triggers2);
        option3.setTriggers(triggers3);

        Assert.assertEquals(option1, option1.findOption("t1"));
        Assert.assertEquals(option1, option1.findOption("t2"));
        Assert.assertNull(option1.findOption("t3"));
        Assert.assertNull(option1.findOption("t4"));
    }

    @Test
    public void testIsRequired() {
        Option option = new OptionImpl(1, true);
        Assert.assertTrue(option.isRequired());

        option = new OptionImpl(2, false);
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testDefaults() {
        Option option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        option.defaults(commandLine);

        // No default implementation
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithEmptyPrefixesSet() {
        Option option = new OptionImpl(1, true);
        Set<String> prefixes = new HashSet<>();

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithPrefix() {
        Option option = new OptionImpl(1, true);
        option.setPrefixes(Arrays.asList("p1"));
        Set<String> prefixes = new HashSet<>(Arrays.asList("p1"));

        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithNoMatchingPrefix() {
        Option option = new OptionImpl(1, true);
        option.setTriggers(Arrays.asList("t1"));
        Set<String> prefixes = new HashSet<>(Arrays.asList("p1"));

        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithMultipleMatchingPrefixes() {
        Option option = new OptionImpl(1, true);
        option.setTriggers(Arrays.asList("p1_t1", "p2_t1"));
        Set<String> prefixes = new HashSet<>(Arrays.asList("p1", "p2"));

        option.checkPrefixes(prefixes);
    }
}