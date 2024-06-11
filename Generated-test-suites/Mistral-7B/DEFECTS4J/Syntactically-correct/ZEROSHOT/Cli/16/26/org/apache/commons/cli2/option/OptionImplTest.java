package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.OptionImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class OptionImplTest {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private ResourceHelper resourceHelper = ResourceHelper.getResourceHelper();

    @org.junit.Test
    public void testCreateOptionImpl() {
        final OptionImpl option = new OptionImpl(1, true);
        assertNotNull(option);
    }

    @org.junit.Test
    public void testCanProcess() {
        final OptionImpl option = new OptionImpl(1, true);
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final List<String> arguments = Arrays.asList("arg1", "arg2");
        final ListIterator<String> iterator = arguments.listIterator();

        assertFalse(option.canProcess(commandLine, iterator));

        iterator.next();
        iterator.remove();

        assertFalse(option.canProcess(commandLine, iterator));

        iterator.add("option");
        iterator.add("value");

        assertFalse(option.canProcess(commandLine, iterator));

        iterator.add(0, "myOption");

        assertTrue(option.canProcess(commandLine, iterator));
    }

    @org.junit.Test
    public void testToString() {
        final OptionImpl option = new OptionImpl(1, true);
        final String expected = resourceHelper.getMessage(ResourceConstants.OPTION_FORMAT, new Object[]{"%d", "%s"});
        final String actual = option.toString();

        assertEquals(String.format(expected, new Object[]{1, ""}), actual);
    }

    @org.junit.Test
    public void testEquals() {
        final OptionImpl option1 = new OptionImpl(1, true);
        final OptionImpl option2 = new OptionImpl(1, true);

        assertTrue(option1.equals(option2));
        assertTrue(option2.equals(option1));

        final OptionImpl option3 = new OptionImpl(2, false);

        assertFalse(option1.equals(option3));
        assertFalse(option3.equals(option1));

        final OptionImpl option4 = new OptionImpl(1, true) {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPreferredName() {
                return "newName";
            }
        };

        assertFalse(option1.equals(option4));
        assertFalse(option4.equals(option1));
    }

    @org.junit.Test
    public void testHashCode() {
        final OptionImpl option1 = new OptionImpl(1, true);
        final OptionImpl option2 = new OptionImpl(1, true);
        final OptionImpl option3 = new OptionImpl(2, false);
        final OptionImpl option4 = new OptionImpl(1, true) {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPreferredName() {
                return "newName";
            }
        };

        assertEquals(option1.hashCode(), option2.hashCode());
        assertEquals(option1.hashCode(), option1.hashCode());

        assertNotSame(option1.hashCode(), option3.hashCode());
        assertNotSame(option1.hashCode(), option4.hashCode());
    }

    @org.junit.Test
    public void testFindOption() {
        final OptionImpl option1 = new OptionImpl(1, true);
        final OptionImpl option2 = new OptionImpl(2, true);
        final OptionImpl option3 = new OptionImpl(1, true);

        assertTrue(option1.findOption("1") == option1);
        assertTrue(option1.findOption("1") == option1);
        assertTrue(option1.findOption("1") == option1);

        assertNull(option1.findOption("2"));
        assertNull(option1.findOption("2"));

        assertTrue(option2.findOption("2") == option2);
        assertTrue(option2.findOption("2") == option2);
        assertTrue(option2.findOption("2") == option2);

        assertTrue(option3.findOption("1") == option3);
        assertTrue(option3.findOption("1") == option3);
        assertTrue(option3.findOption("1") == option3);
    }

    @org.junit.Test
    public void testIsRequired() {
        final OptionImpl option = new OptionImpl(1, true);

        assertTrue(option.isRequired());
    }

    @org.junit.Test
    public void testDefaults() {
        final OptionImpl option = new OptionImpl(1, true);

        // nothing to do by default
    }

    @org.junit.Test
    public void testCheckPrefixesEmptyPrefixes() {
        final OptionImpl option = new OptionImpl(1, true);
        final Set<String> prefixes = new HashSet<>();

        option.checkPrefixes(prefixes);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        final OptionImpl option = new OptionImpl(1, true);
        final Set<String> prefixes = new HashSet<>(Arrays.asList("prefix1", "prefix2"));

        option.checkPrefixes(prefixes);

        option.getTriggers();

        final String message = resourceHelper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, "trigger");
        throw new IllegalArgumentException(message);
    }

    @org.junit.Test
    public void testCheckPrefixesWithTriggerPrefix() {
        final OptionImpl option = new OptionImpl(1, true);
        final Set<String> prefixes = new HashSet<>(Arrays.asList("prefix1", "prefix2", "trigger"));

        option.checkPrefixes(prefixes);
    }
}