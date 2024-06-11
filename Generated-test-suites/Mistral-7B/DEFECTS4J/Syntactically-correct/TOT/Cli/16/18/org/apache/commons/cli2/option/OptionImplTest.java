package org.apache.commons.cli2.option;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionImpl;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.resource.SimpleResourceHelper;
import org.apache.commons.cli2.resource.TestResourceConstants;

public class OptionImplTest {

    private static final ResourceHelper resourceHelper = new SimpleResourceHelper();

    private Option option;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .create(new OptionImpl(1, false));
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcess() {
        assertFalse(option.canProcess(commandLine, null));

        commandLine.parseArgument(new String[]{"--testOption"});
        assertTrue(option.canProcess(commandLine, null));

        commandLine.parseArgument(new String[]{"--otherOption"});
        assertFalse(option.canProcess(commandLine, null));

        commandLine.parseArgument(new String[]{"--testOption", "arg"});
        assertTrue(option.canProcess(commandLine, null));

        commandLine.parseArgument(new String[]{"--testOption", "arg", "--otherOption"});
        assertTrue(option.canProcess(commandLine, null));
    }

    @Test
    public void testFindOption() {
        Option option2 = OptionBuilder.withLongOpt("testOption2")
                .withDescription("Another test option")
                .create(new OptionImpl(2, false));

        assertNull(commandLine.findOption("nonexistentOption"));
        assertNull(commandLine.findOption("testOption"));
        assertNull(commandLine.findOption("testOption2"));

        commandLine.parseArgument("--testOption");
        assertNull(commandLine.findOption("testOption"));
        assertNull(commandLine.findOption("testOption2"));

        commandLine.parseArguments(new String[]{"--testOption", "--testOption2"});
        assertNull(commandLine.findOption("testOption"));
        assertNull(commandLine.findOption("nonexistentOption"));
        assertSame(option2, commandLine.findOption("testOption2"));
        assertSame(option, commandLine.findOption("testOption"));
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);
        assertNotNull(commandLine.getOptionValues("testOption"));
        assertNotNull(commandLine.getOptionValues("nonexistentOption"));
        assertNull(commandLine.getOptionValue("testOption"));
        assertNull(commandLine.getOptionValue("nonexistentOption"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        final String[] prefixes = new String[]{"test"};
        Option option2 = OptionBuilder.withLongOpt("testOption2")
                .withPrefixes(prefixes)
                .withDescription("Another test option")
                .create(new OptionImpl(2, false));

        resourceHelper.setResource(TestResourceConstants.OPTION_NAMES, "TestOptionNames.properties");
        Option option3 = OptionBuilder.withLongOpt("testOption3")
                .withPrefixes(prefixes)
                .withDescription("Another test option")
                .create(new OptionImpl(3, false));

        OptionImpl testOption = new OptionImpl(0, false) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }
        };

        testOption.checkPrefixes(Set.of("test"));

        testOption.checkPrefixes(Set.of("testOption"));
        testOption.checkPrefixes(Set.of("testOption2"));
        testOption.checkPrefixes(Set.of("testOption3"));
        testOption.checkPrefixes(Set.of("testOption4"));

        testOption.checkPrefixes(Set.of("testOption")); // already has this prefix, should be fine

        testOption.checkPrefixes(Set.of("testOptionWithLongPrefix"));

        testOption.checkPrefixes(Set.of("testOptionWithShortPrefix"));

        testOption.checkPrefixes(Set.of("testOption", "testOption2")); // multiple prefixes are fine

        OptionImpl testOptionWithLongPrefix = new OptionImpl(4, false) {
            @Override
            public String getPreferredName() {
                return "testOptionWithLongPrefix";
            }
        };
        testOptionWithLongPrefix.checkPrefixes(Set.of("test")); // should throw an exception
    }
}