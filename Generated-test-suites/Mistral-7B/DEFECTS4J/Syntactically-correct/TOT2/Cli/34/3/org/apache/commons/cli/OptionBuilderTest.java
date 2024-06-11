package org.junit.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OptionBuilderTest {

    @BeforeClass
    public static void setUpClass() {
        OptionBuilder.reset();
    }

    @Test
    public void testCreateOption() throws Exception {
        Option option = OptionBuilder.create(' opt')
                .withDescription("Option test")
                .create();

        assertNotNull(option);
        assertEquals("Option test", option.getDescription());
        assertEquals(' opt', option.getOpt());
    }

    // ... other test cases ...

    @AfterClass
    public static void tearDownClass() {
        OptionBuilder.reset();
    }
}