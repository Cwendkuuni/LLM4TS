package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionFactory;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

/**
 * Test cases for {@link OptionImpl}.
 */
public class OptionImplTest {

    /**
     * Test for {@link OptionImpl#OptionImpl(int, boolean)}.
     */
    @org.junit.Test
    public void testOptionImpl() {
        final Option option = new OptionBuilder("o")
                .withLongOpt("option")
                .isRequired()
                .withDescription("An option")
                .create();

        final OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "o";
            }

            @Override
            public String getDescription() {
                return "An option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<String>(Arrays.asList("--"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<String>(Arrays.asList("o", "option"));
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      ListIterator<String> arguments) {
                return true;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
            }

            @Override
            public void parseArgument(String argument, WriteableCommandLine commandLine)
                    throws ParseException {
                // not implemented
            }
        };

        assertEquals(option, optionImpl);
        assertEquals(1, optionImpl.getId());
        assertTrue(optionImpl.isRequired());
    }

    /**
     * Test for {@link OptionImpl#canProcess(WriteableCommandLine, ListIterator)}.
     */
    @org.junit.Test
    public void testCanProcess() {
        final OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "o";
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      ListIterator<String> arguments) {
                return arguments.hasNext() && "--o".equals(arguments.next());
            }

            @Override
            public void parseArgument(String argument, WriteableCommandLine commandLine)
                    throws ParseException {
                // not implemented
            }
        };

        final CommandLine commandLine = new CommandLine();
        final DefaultParser parser = new DefaultParser();

        final List<String> arguments = new ArrayList<>();
        arguments.add("--o");

        final HelpFormatter formatter = new HelpFormatter();

        // test successful processing
        parser.parse(optionImpl, arguments.subList(0, arguments.size()).toArray(new String[0]), commandLine, true, formatter, null);
        assertTrue(optionImpl.canProcess(commandLine, arguments.listIterator()));

        // test failure to process
        arguments.remove(0);
        assertFalse(optionImpl.canProcess(commandLine, arguments.listIterator()));
    }

    /**
     * Test for {@link OptionImpl#toString()}.
     */
    @org.junit.Test
    public void testToString() {
        final OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "o";
            }

            @Override
            public String getDescription() {
                return "An option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<String>(Arrays.asList("--"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<String>(Arrays.asList("o", "option"));
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      ListIterator<String> arguments) {
                return true;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
            }

            @Override
            public void parseArgument(String argument, WriteableCommandLine commandLine)
                    throws ParseException {
                // not implemented
            }
        };

        final String expected = "OPTION: --o<o> OPTION: --option <o>\n" +
                "OPTION: o OPTION: option\n" +
                "An option\n";

        final String actual = optionImpl.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link OptionImpl#equals(Object)}.
     */
    @org.junit.Test
    public void testEquals() {
        final OptionImpl optionImpl1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "o";
            }

            @Override
            public String getDescription() {
                return "An option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<String>(Arrays.asList("--"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<String>(Arrays.asList("o", "option"));
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      ListIterator<String> arguments) {
                return true;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
            }

            @Override
            public void parseArgument(String argument, WriteableCommandLine commandLine)
                    throws ParseException {
                // not implemented
            }
        };

        final OptionImpl optionImpl2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "o";
            }

            @Override
            public String getDescription() {
                return "An option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<String>(Arrays.asList("--"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<String>(Arrays.asList("o", "option"));
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      ListIterator<String> arguments) {
                return true;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
            }

            @Override
            public void parseArgument(String argument, WriteableCommandLine commandLine)
                    throws ParseException {
                // not implemented
            }
        };

        final OptionImpl optionImpl3 = new OptionImpl(2, false);

        assertEquals(optionImpl1, optionImpl1);
        assertEquals(optionImpl1, optionImpl2);
        assertNotSame(optionImpl1, optionImpl3);
    }

    /**
     * Test for {@link OptionImpl#hashCode()}.
     */
    @org.junit.Test
    public void testHashCode() {
        final OptionImpl optionImpl1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "o";
            }

            @Override
            public String getDescription() {
                return "An option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<String>(Arrays.asList("--"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<String>(Arrays.asList("o", "option"));
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      ListIterator<String> arguments) {
                return true;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
            }

            @Override
            public void parseArgument(String argument, WriteableCommandLine commandLine)
                    throws ParseException {
                // not implemented
            }
        };

        final OptionImpl optionImpl2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "o";
            }

            @Override
            public String getDescription() {
                return "An option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<String>(Arrays.asList("--"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<String>(Arrays.asList("o", "option"));
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      ListIterator<String> arguments) {
                return true;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
            }

            @Override
            public void parseArgument(String argument, WriteableCommandLine commandLine)
                    throws ParseException {
                // not implemented
            }
        };

        final OptionImpl optionImpl3 = new OptionImpl(2, false);

        assertEquals(optionImpl1.hashCode(), optionImpl1.hashCode());
        assertEquals(optionImpl1.hashCode(), optionImpl2.hashCode());
        assertNotEquals(optionImpl1.hashCode(), optionImpl3.hashCode());
    }

    /**
     * Test for {@link OptionImpl#findOption(String)}.
     */
    @org.junit.Test
    public void testFindOption() {
        final Option option = new OptionBuilder("o")
                .withLongOpt("option")
                .isRequired()
                .withDescription("An option")
                .create();

        final OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "o";
            }

            @Override
            public String getDescription() {
                return "An option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<String>(Arrays.asList("--"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<String>(Arrays.asList("o", "option"));
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      ListIterator<String> arguments) {
                return true;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
            }

            @Override
            public void parseArgument(String argument, WriteableCommandLine commandLine)
                    throws ParseException {
                // not implemented
            }
        };

        assertNull(optionImpl.findOption(null));
        assertNull(optionImpl.findOption("--ignore"));
        assertSame(optionImpl, optionImpl.findOption("o"));
        assertSame(optionImpl, optionImpl.findOption("option"));
    }

    /**
     * Test for {@link OptionImpl#isRequired()}.
     */
    @org.junit.Test
    public void testIsRequired() {
        final OptionImpl optionImpl1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "o";
            }
        };

        final OptionImpl optionImpl2 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "o";
            }
        };

        assertTrue(optionImpl1.isRequired());
        assertFalse(optionImpl2.isRequired());
    }
}