package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Test case 1: Simple options
        String[] args1 = {"--option1", "--option2=value1", "-o", "--option3"};
        Options options1 = new Options();
        options1.addOption("option1", true, "o");
        options1.addOption("option2", true, "v");
        options1.addOption("option3", true);

        PosixParser parser1 = new PosixParser();
        String[] result1 = parser1.flatten(options1, args1, false);
        List<String> expected1 = Arrays.asList(new String[]{"--option1", "-o", "--option2=value1", "--option3"});
        Assert.assertArrayEquals(expected1.toArray(), result1);

        // Test case 2: Long options
        String[] args2 = {"--long-option", "--long-option-with-value=value"};
        Options options2 = new Options();
        options2.addOption("longOption", true, "l");
        options2.addOption("longOptionWithValue", true, "lv");

        PosixParser parser2 = new PosixParser();
        String[] result2 = parser2.flatten(options2, args2, false);
        List<String> expected2 = Arrays.asList(new String[]{"--long-option", "--long-option-with-value=value"});
        Assert.assertArrayEquals(expected2.toArray(), result2);

        // Test case 3: Stop at non-option
        String[] args3 = {"--option1", "--option2=value1", "-o", "--", "nonOption"};
        Options options3 = new Options();
        options3.addOption("option1", true, "o");
        options3.addOption("option2", true, "v");

        PosixParser parser3 = new PosixParser();
        String[] result3 = parser3.flatten(options3, args3, true);
        List<String> expected3 = Arrays.asList(new String[]{"--option1", "-o", "--", "nonOption"});
        Assert.assertArrayEquals(expected3.toArray(), result3);

        // Test case 4: Stop at non-option with long options
        String[] args4 = {"--option1", "--option2=value1", "--long-option", "--long-option-with-value=value", "--"};
        Options options4 = new Options();
        options4.addOption("option1", true, "o");
        options4.addOption("option2", true, "v");
        options4.addOption("longOption", true, "l");
        options4.addOption("longOptionWithValue", true, "lv");

        PosixParser parser4 = new PosixParser();
        String[] result4 = parser4.flatten(options4, args4, true);
        List<String> expected4 = Arrays.asList(new String[]{"--option1", "--option2=value1", "--long-option", "--long-option-with-value=value"});
        Assert.assertArrayEquals(expected4.toArray(), result4);

        // Test case 5: Bursting
        String[] args5 = {"-o", "-a", "--longOption", "value", "-b", "--longOption2"};
        Options options5 = new Options();
        options5.addOption("option", false, "o");
        options5.addOption("optionA", false, "a");
        options5.addOption("longOption", true, "l");
        options5.addOption("longOption2", true, "lv2");

        PosixParser parser5 = new PosixParser();
        String[] result5 = parser5.flatten(options5, args5, false);
        List<String> expected5 = Arrays.asList(new String[]{"-o", "-a", "--longOption", "value", "-b", "--longOption2"});
        Assert.assertArrayEquals(expected5.toArray(), result5);

        List<String> expected5Bursted = Arrays.asList(new String[]{"-o", "-a", "--long-option", "l", "value", "-b", "--long-option2"});
        PosixParser parser5Bursted = new PosixParser();
        String[] result5Bursted = parser5Bursted.flatten(options5, args5, false);
        Assert.assertArrayEquals(expected5Bursted.toArray(), result5Bursted);
    }
}