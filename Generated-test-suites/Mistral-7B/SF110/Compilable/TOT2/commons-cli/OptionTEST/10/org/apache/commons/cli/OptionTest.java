package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("opt", "desc");
        assertEquals("opt", option.getOpt());
        assertEquals("desc", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
    }
}