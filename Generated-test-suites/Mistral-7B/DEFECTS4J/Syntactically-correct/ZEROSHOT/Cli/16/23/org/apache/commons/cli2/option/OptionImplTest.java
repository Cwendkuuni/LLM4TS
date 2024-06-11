package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class OptionImplTest {

    private OptionImpl option;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        commandLine = Mockito.mock(WriteableCommandLine.class);
        option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "test description";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--test");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test", "t");
            }
        };
    }

    @Test
    public void testId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testToString() {
        assertEquals("--test\n test description\n", option.toString());
    }

    @Test
    public void testEquals() {
        final OptionImpl sameOption = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "test description";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--test");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test", "t");
            }
        };
        final OptionImpl differentOption = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "test description";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--test");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test", "t");
            }
        };
        final OptionImpl differentOptionNoPrefixes = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "test description";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList();
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test", "t");
            }
        };
        final OptionImpl differentOptionNoDescription = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--test");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test", "t");
            }
        };

        assertTrue(option.equals(sameOption));
        assertFalse(option.equals(differentOption));
        assertFalse(option.equals(differentOptionNoPrefixes));
        assertFalse(option.equals(differentOptionNoDescription));
        assertFalse(option.equals(null));
        assertFalse(option.equals("test"));
    }

    @Test
    public void testHashCode() {
        final OptionImpl sameOption = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "test description";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--test");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test", "t");
            }
        };
        final OptionImpl differentOption = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "test description";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--test");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test", "t");
            }
        };
        final OptionImpl differentOptionNoPrefixes = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "test description";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList();
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test", "t");
            }
        };
        final OptionImpl differentOptionNoDescription = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--test");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test", "t");
            }
        };

        assertEquals(option.hashCode(), sameOption.hashCode());
        assertNotEquals(option.hashCode(), differentOption.hashCode());
        assertNotEquals(option.hashCode(), differentOptionNoPrefixes.hashCode());
        assertNotEquals(option.hashCode(), differentOptionNoDescription.hashCode());
    }

    @Test
    public void testFindOption() {
        final OptionImpl otherOption = new OptionImpl(2, true) {
            @Override
            public String getPreferredName() {
                return "otherOption";
            }

            @Override
            public String getDescription() {
                return "other description";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--other");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("other", "o");
            }
        };

        assertNull(option.findOption(null));
        assertNull(option.findOption("other"));
        assertNull(option.findOption(""));
        assertNull(option.findOption(" "));
        assertNull(option.findOption("--"));
        assertNull(option.findOption("--testother"));

        assertNull(otherOption.findOption("test"));
        assertNull(otherOption.findOption("--test"));

        assertSame(option, option.findOption("test"));
        assertSame(option, option.findOption("--test"));
    }

    @Test
    public void testCanProcess() {
        final Parser parser = Mockito.mock(Parser.class);
        Mockito.when(parser.parse(commandLine, new String[]{"--test", "arg1"}))
                .thenReturn(commandLine);
        Mockito.when(parser.parse(commandLine, new String[]{"test", "arg1"}))
                .thenReturn(commandLine);

        assertTrue(option.canProcess(commandLine, Arrays.asList(commandLine.getParsedArgs()).listIterator()));
        assertTrue(option.canProcess(commandLine, Arrays.asList("--test", "arg1").listIterator()));
        assertTrue(option.canProcess(commandLine, Arrays.asList("test", "arg1").listIterator()));

        assertFalse(option.canProcess(commandLine, Arrays.asList("--other", "arg1").listIterator()));
        assertFalse(option.canProcess(commandLine, Arrays.asList("other", "arg1").listIterator()));
        assertFalse(option.canProcess(commandLine, Arrays.asList().listIterator()));
        assertFalse(option.canProcess(commandLine, Arrays.asList("arg1", "arg2").listIterator()));

        Parser parserWithPrefixes = Mockito.mock(Parser.class);
        Mockito.when(parserWithPrefixes.parse(commandLine, new String[]{"--testarg", "arg1"}))
                .thenReturn(commandLine);

        assertTrue(option.canProcess(commandLine, Arrays.asList("--testarg", "arg1").listIterator()));
        assertFalse(option.canProcess(commandLine, Arrays.asList("--otherarg", "arg1").listIterator()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        final Set<String> prefixes = new HashSet<>();
        option.checkPrefixes(prefixes);

        prefixes.add("test");
        option.checkPrefixes(prefixes);

        prefixes.clear();
        option.checkPrefixes(prefixes);

        prefixes.add("--test");
        prefixes.add("test");
        option.checkPrefixes(prefixes);

        prefixes.add("test");
        option.checkPrefixes(prefixes);

        prefixes.add("--testother");
        option.checkPrefixes(prefixes);
    }
}