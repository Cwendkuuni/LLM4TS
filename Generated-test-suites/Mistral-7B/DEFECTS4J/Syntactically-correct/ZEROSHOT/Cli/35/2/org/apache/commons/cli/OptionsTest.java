package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.junit.Test;

/**
 * Test class for Options.
 */
public class OptionsTest {

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} .
     */
    @Test
    public void testAddOption() {
        final Options options = new Options();

        // test short option
        Option optionA = new Option("a", "optionA", false, "Description for option A");
        options.addOption(optionA.getKey(), optionA.getLongOpt(), optionA.isRequired(), optionA.getDescription());
        assertEquals("Description for option A", options.getOption(optionA.getKey()).getDescription());

        // test long option
        Option optionB = new Option("b", "optionB", false, "Description for option B");
        options.addOption(optionB);
        assertEquals("Description for option B", options.getOption(optionB.getKey()).getDescription());

        // test short and long option
        Option optionC = new Option("c", "optionC", false, "Description for option C");
        options.addOption(optionC.getKey(), optionC.getLongOpt(), optionC.isRequired(), optionC.getDescription());
        assertEquals("Description for option C", options.getOption(optionC.getKey()).getDescription());
        assertEquals(optionC.getKey(), options.getOption(optionC.getLongOpt()).getKey());

        // test short and long option with same name
        Option optionD = new Option("d", null, false, "Description for option D");
        options.addOption(optionD.getKey(), optionD.getLongOpt(), optionD.isRequired(), optionD.getDescription());
        Option optionE = new Option("d", null, false, "Description for option E");
        options.addOption(optionE.getKey(), optionE.getLongOpt(), optionE.isRequired(), optionE.getDescription());
        assertEquals(2, options.getOptions().size());
        assertEquals("Description for option D", options.getOption(optionD.getKey()).getDescription());
        assertEquals("Description for option E", options.getOption(optionE.getKey()).getDescription());

        // test short option with argument
        Option optionF = new Option("f", "optionF", true, "Description for option F");
        options.addOption(optionF.getKey(), optionF.getLongOpt(), optionF.isRequired(), optionF.getDescription());
        assertEquals("Description for option F", options.getOption(optionF.getKey()).getDescription());
        assertTrue(optionF.getValue().isEmpty());

        // test long option with argument
        Option optionG = new Option("g", "optionG", true, "Description for option G");
        options.addOption(optionG);
        assertEquals("Description for option G", options.getOption(optionG.getKey()).getDescription());
        assertTrue(optionG.getValue().isEmpty());

        // test short option with argument
        Option optionH = new Option("h", "optionH", true, "Description for option H");
        options.addOption(optionH.getKey(), optionH.getLongOpt(), optionH.isRequired(), optionH.getDescription());
        assertEquals("Description for option H", options.getOption(optionH.getKey()).getDescription());
        assertTrue(optionH.getValue().isEmpty());

        // test long option with argument
        Option optionI = new Option("i", null, true, "Description for option I");
        options.addOption(optionI);
        assertEquals("Description for option I", options.getOption(optionI.getKey()).getDescription());
        assertTrue(optionI.getValue().isEmpty());

        // test short option with argument
        Option optionJ = new Option("j", "optionJ", true, "Description for option J");
        options.addOption(optionJ.getKey(), optionJ.getLongOpt(), optionJ.isRequired(), optionJ.getDescription());
        assertEquals("Description for option J", options.getOption(optionJ.getKey()).getDescription());
        assertTrue(optionJ.getValue().isEmpty());
        optionJ.setValue("value");
        assertEquals("value", optionJ.getValue());

        // test long option with argument
        Option optionK = new Option("k", "optionK", true, "Description for option K");
        options.addOption(optionK);
        assertEquals("Description for option K", options.getOption(optionK.getKey()).getDescription());
        assertTrue(optionK.getValue().isEmpty());
        optionK.setValue("value");
        assertEquals("value", optionK.getValue());

        // test multiple options with argument
        final String arg = "arg";
        Option optionL = new Option("l", "optionL", true, "Description for option L");
        Option optionM = new Option("m", "optionM", true, "Description for option M");
        options.addOption(optionL);
        options.addOption(optionM);
        Option optionN = new Option("n", "optionN", true, "Description for option N");
        optionN.setValue(arg);
        options.addOption(optionN);
        List<Option> optionsList = options.getOptions();
        assertEquals(3, optionsList.size());
        Option optionP = optionsList.get(0);
        assertEquals("Description for option L", optionP.getDescription());
        assertEquals("", optionP.getValue());
        Option optionQ = optionsList.get(1);
        assertEquals("Description for option M", optionQ.getDescription());
        assertEquals("", optionQ.getValue());
        Option optionR = optionsList.get(2);
        assertEquals("Description for option N", optionR.getDescription());
        assertEquals(arg, optionR.getValue());
    }

    /**
     * Test method for {@link Options#addOption(String, boolean, String)} .
     */
    @Test
    public void testAddOptionWithoutDescription() {
        final Options options = new Options();

        // test short option
        Option optionA = new Option("a", false);
        options.addOption(optionA.getKey(), optionA.isRequired());
        assertNull(options.getOption(optionA.getKey()).getDescription());

        // test long option
        Option optionB = new Option("b", false);
        options.addOption(optionB);
        assertNull(options.getOption(optionB.getKey()).getDescription());

        // test short and long option
        Option optionC = new Option("c", false);
        options.addOption(optionC.getKey(), optionC.isRequired());
        assertEquals(optionC.getKey(), options.getOption(optionC.getLongOpt()).getKey());
        assertNull(options.getOption(optionC.getKey()).getDescription());
        assertNull(options.getOption(optionC.getLongOpt()).getDescription());
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} with
     * a simple option group.
     */
    @Test
    public void testAddOptionWithOptionGroup() {
        final Options options = new Options();

        // create option groups
        OptionGroup groupA = new OptionGroup("groupA", "groupA description");
        OptionGroup groupB = new OptionGroup("groupB", "groupB description");

        // create options
        Option optionA = new Option("a1", "a1", false, "description for a1");
        Option optionB = new Option("b1", "b1", false, "description for b1");
        Option optionC = new Option("c1", "c1", false, "description for c1");

        // add options to the groups
        groupA.addOption(optionA);
        groupB.addOption(optionB);
        groupB.addOption(optionC);

        // add groups to the options
        options.addOptionGroup(groupA);
        options.addOptionGroup(groupB);

        // test getOption()
        assertEquals(optionA, options.getOption(optionA.getKey()));
        assertEquals(optionB, options.getOption(optionB.getKey()));
        assertEquals(optionC, options.getOption(optionC.getKey()));

        // test getMatchingOptions()
        List<String> matchingOptions = options.getMatchingOptions("g");
        assertEquals(Collections.singletonList(groupB.getKey()), matchingOptions);

        // test hasOption()
        assertTrue(options.hasOption(optionA.getKey()));
        assertTrue(options.hasOption(optionB.getKey()));
        assertTrue(options.hasOption(optionC.getKey()));
        assertFalse(options.hasOption("nonExistingOption"));

        // test hasLongOption()
        assertTrue(options.hasLongOption(optionA.getLongOpt()));
        assertTrue(options.hasLongOption(optionB.getLongOpt()));
        assertTrue(options.hasLongOption(optionC.getLongOpt()));
        assertFalse(options.hasLongOption("nonExistingOption"));

        // test hasShortOption()
        assertTrue(options.hasShortOption(optionA.getKey().charAt(0)));
        assertTrue(options.hasShortOption(optionB.getKey().charAt(0)));
        assertTrue(options.hasShortOption(optionC.getKey().charAt(0)));
        assertFalse(options.hasShortOption('z'));

        // test getOptionGroup()
        assertEquals(groupA, options.getOptionGroup(optionA));
        assertEquals(groupB, options.getOptionGroup(optionB));
        assertNull(options.getOptionGroup(new Option("nonExistingOption")));
    }

    /**
     * Test method for {@link Options#addOptionGroup(OptionGroup)} .
     */
    @Test
    public void testAddOptionGroup() {
        final Options options = new Options();

        // create option groups
        OptionGroup groupA = new OptionGroup("groupA", "groupA description");
        OptionGroup groupB = new OptionGroup("groupB", "groupB description");

        // add groups to the options
        options.addOptionGroup(groupA);
        options.addOptionGroup(groupB);

        // test getOptionGroups()
        assertEquals(Arrays.asList(groupA, groupB), options.getOptionGroups());

        // test getOption()
        Option optionA = new Option("a1", "a1", false, "description for a1");
        Option optionB = new Option("b1", "b1", false, "description for b1");
        Option optionC = new Option("c1", "c1", false, "description for c1");

        // add options to the groups
        groupA.addOption(optionA);
        groupB.addOption(optionB);
        groupB.addOption(optionC);

        // test getOptions()
        assertEquals(Arrays.asList(optionA, optionB, optionC), options.getOptions());

        // test getRequiredOptions()
        assertEquals(Collections.emptyList(), options.getRequiredOptions());

        // add required options
        Option requiredOption = new Option("r1", "r1", false, "description for required option");
        options.addOption(requiredOption);

        // test getRequiredOptions()
        assertEquals(Collections.singletonList(requiredOption), options.getRequiredOptions());
    }
}