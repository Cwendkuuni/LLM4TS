 package org.apache.commons.cli2.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.DefaultWriteableCommandLine;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testWriteableCommandLine() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        Option option1 = OptionBuilder.newOption()
                .withLongOpt("option1")
                .withDescription("Option 1")
                .build();
        cmd.addOption(option1);

        Option option2 = OptionBuilder.newOption()
                .withLongOpt("option2")
                .withDescription("Option 2 with value")
                .withValueSeparator('=')
                .isRequired(false)
                .build();
        cmd.addOption(option2);
        cmd.addValue(option2, "value1");

        Option option3 = OptionBuilder.newOption()
                .withLongOpt("option3")
                .withDescription("Option 3")
                .isRequired(true)
                .build();
        cmd.addOption(option3);

        cmd.setDefaultValues(option1, Arrays.asList("default1", "default2"));

        try {
            cmd.addSwitch(option1, false);
        } catch (IllegalStateException e) {
            assertEquals("Option [option1] is a valued option, switch values are not supported.", e.getMessage());
        }

        cmd.addSwitch(option2, true);
        cmd.setDefaultSwitch(option3, false);

        cmd.addProperty("property1", "value1");
        cmd.addProperty("property2", null);

        assertEquals(Arrays.asList(option1, option2, option3), cmd.getOptions());
        assertEquals("value1", cmd.getOptionValues("option2").get(0));
        assertTrue(option2.getValue().isSetValue());
        assertTrue(option3.getValue().isSetValue());
        assertEquals(Arrays.asList("default1", "default2"), cmd.getOptionValues("option1"));
        assertNull(cmd.getOptionValues("option3"));
        assertTrue(option2.getValue().getValue().isSetValue());
        assertFalse(option3.getValue().isSetValue());
        assertEquals("value1", cmd.getProperty("property1"));
        assertNull(cmd.getProperty("property2"));
    }
}
