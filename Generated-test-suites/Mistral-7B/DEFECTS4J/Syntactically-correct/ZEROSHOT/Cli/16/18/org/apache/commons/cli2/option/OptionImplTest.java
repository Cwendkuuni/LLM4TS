import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.cli2.impl.TestUtils.createCommandLine;

public class OptionImplTest {

    @Test
    public void testConstructor() {
        Option option = new OptionImpl(1, true);
        Assertions.assertEquals(1, option.getId());
        Assertions.assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcess() {
        Option option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      ListIterator<String> arguments) {
                return "test".equals(arguments.next());
            }
        };

        WriteableCommandLine commandLine = createCommandLine();
        ListIterator<String> arguments = Arrays.asList("test", "arg1", "arg2").listIterator();

        Assertions.assertTrue(option.canProcess(commandLine, arguments));

        arguments = Arrays.asList("arg1", "arg2").listIterator();
        Assertions.assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        Option option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "test";
            }

            @Override
            public String getDescription() {
                return "Test option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--test", "-t"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("test", "t"));
            }
        };

        Assertions.assertEquals("Test option\n[--test, -t]\n", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "test1";
            }

            @Override
            public String getDescription() {
                return "Test option 1";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--test1", "-t1"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("test1", "t1"));
            }
        };

        Option option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "test1";
            }

            @Override
            public String getDescription() {
                return "Test option 1";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--test1", "-t1"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("test1", "t1"));
            }
        };

        Option option3 = new OptionImpl(2, false);

        Assertions.assertEquals(option1, option1);
        Assertions.assertTrue(option1.equals(option2));
        Assertions.assertFalse(option1.equals(option3));
    }

    @Test
    public void testHashCode() {
        Option option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "test1";
            }

            @Override
            public String getDescription() {
                return "Test option 1";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--test1", "-t1"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("test1", "t1"));
            }
        };

        Option option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "test2";
            }

            @Override
            public String getDescription() {
                return "Test option 2";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--test2", "-t2"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("test2", "t2"));
            }
        };

        Option option3 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "test1";
            }

            @Override
            public String getDescription() {
                return "Test option 1";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--test1", "-t1"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("test1", "t1"));
            }
        };

        Assertions.assertEquals(option1.hashCode(), option1.hashCode());
        Assertions.assertEquals(option1.hashCode(), option3.hashCode());
        Assertions.assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testFindOption() {
        Option option1 = new OptionImpl(1, true) {
            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("test1", "t1"));
            }
        };

        Option option2 = new OptionImpl(2, true) {
            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("test2", "t2"));
            }
        };

        Assertions.assertEquals(option1, option1.findOption("test1"));
        Assertions.assertEquals(option1, option1.findOption("t1"));
        Assertions.assertNull(option1.findOption("test2"));
        Assertions.assertNull(option1.findOption("t2"));

        Assertions.assertNull(option2.findOption("test1"));
        Assertions.assertNull(option2.findOption("t1"));
        Assertions.assertEquals(option2, option2.findOption("test2"));
        Assertions.assertEquals(option2, option2.findOption("t2"));
    }

    @Test
    public void testIsRequired() {
        Option option1 = new OptionImpl(1, true);
        Option option2 = new OptionImpl(2, false);

        Assertions.assertTrue(option1.isRequired());
        Assertions.assertFalse(option2.isRequired());
    }

    @Test
    public void testDefaults() {
        Option option = new OptionImpl(1, true) {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setValue(getId(), "default");
            }
        };

        WriteableCommandLine commandLine = createCommandLine();
        Assertions.assertNull(commandLine.getValue(option.getId()));

        option.defaults(commandLine);

        Assertions.assertEquals("default", commandLine.getValue(option.getId()));
    }

    @Test
    public void testCheckPrefixes() {
        Option option = new OptionImpl(1, true) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--test"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("test"));
            }
        };

        Set<String> prefixes = new HashSet<>(Arrays.asList("--other"));

        option.checkPrefixes(prefixes);

        Assertions.assertDoesNotThrow(() -> option.checkPrefixes(prefixes),
                "Prefixes should not contain any of the triggers");
    }

    @Test
    public void testCheckPrefixesWithTriggerPrefix() {
        Option option = new OptionImpl(1, true) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--test"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("test"));
            }
        };

        Set<String> prefixes = new HashSet<>(Arrays.asList("--test-arg"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> option.checkPrefixes(prefixes),
                "Prefixes should contain the trigger as a prefix");
    }
}