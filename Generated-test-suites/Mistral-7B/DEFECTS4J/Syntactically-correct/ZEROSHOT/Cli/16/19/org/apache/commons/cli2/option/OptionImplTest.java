package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.OptionImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link OptionImpl} class.
 */
public class OptionImplTest {

    private ResourceHelper resourceHelper;
    private OptionImpl option;

    @Before
    public void setUp() {
        resourceHelper = ResourceHelper.getResourceHelper();
        option = new OptionImpl(1, true, null, null, null, null, new HashSet<>(), new HashSet<>());
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--test", "arg1", "--option", "arg2");
        ListIterator<String> listIterator = arguments.listIterator();

        assertFalse(option.canProcess(commandLine, listIterator));

        listIterator.next();

        assertTrue(option.canProcess(commandLine, listIterator));
        assertEquals(option.getId(), commandLine.getOption(0).getId());
        assertEquals(option.toString(), commandLine.getOption(0).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("test");

        option.checkPrefixes(prefixes);

        option.setTriggers(Arrays.asList("option"));
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true, null, null, null, null, new HashSet<>(), new HashSet<>());
        OptionImpl option2 = new OptionImpl(1, true, null, null, null, null, new HashSet<>(), new HashSet<>());

        assertEquals(option1, option2);

        OptionImpl option3 = new OptionImpl(2, true, null, null, null, null, new HashSet<>(), new HashSet<>());

        assertNotEquals(option1, option3);

        option1.setPreferredName("test");
        option2.setPreferredName("test2");

        assertNotEquals(option1, option2);

        option1.setDescription("Description 1");
        option2.setDescription("Description 2");

        assertNotEquals(option1, option2);

        option1.getPrefixes().add("prefix1");
        option2.getPrefixes().add("prefix2");

        assertNotEquals(option1, option2);

        option1.getTriggers().add("trigger1");
        option2.getTriggers().add("trigger2");

        assertNotEquals(option1, option2);

        option1.setTriggers(null);
        option2.setTriggers(Arrays.asList("trigger1"));

        assertNotEquals(option1, option2);
    }

    @Test
    public void testFindOption() {
        Set<String> triggers = new HashSet<>();
        triggers.add("option1");
        triggers.add("option2");

        option.setTriggers(triggers);

        OptionImpl option1 = new OptionImpl(1, true, null, null, null, null, new HashSet<>(), triggers);
        OptionImpl option2 = new OptionImpl(2, true, null, null, null, null, new HashSet<>(), triggers);

        assertEquals(option1, option1.findOption("option1"));
        assertEquals(option2, option2.findOption("option2"));
        assertNull(option1.findOption("option3"));
        assertNull(option2.findOption("option1"));
    }

    @Test
    public void testHashCode() {
        OptionImpl option1 = new OptionImpl(1, true, null, null, null, null, new HashSet<>(), new HashSet<>());
        OptionImpl option2 = new OptionImpl(1, true, null, null, null, null, new HashSet<>(), new HashSet<>());

        assertEquals(option1.hashCode(), option2.hashCode());

        option1.setPreferredName("test");
        option2.setPreferredName("test2");

        assertNotEquals(option1.hashCode(), option2.hashCode());

        option1.setDescription("Description 1");
        option2.setDescription("Description 2");

        assertNotEquals(option1.hashCode(), option2.hashCode());

        option1.getPrefixes().add("prefix1");
        option2.getPrefixes().add("prefix2");

        assertNotEquals(option1.hashCode(), option2.hashCode());

        option1.getTriggers().add("trigger1");
        option2.getTriggers().add("trigger2");

        assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("", option.toString());

        option.setPreferredName("test");
        assertEquals("--test", option.toString());

        option.setDescription("Description");
        assertEquals("--test <Description>", option.toString());

        option.getPrefixes().add("prefix");
        assertEquals("--test [prefix<OPTION>]", option.toString());

        option.setRequired(false);
        assertEquals("--test [prefix<OPTION>]", option.toString());
    }
}