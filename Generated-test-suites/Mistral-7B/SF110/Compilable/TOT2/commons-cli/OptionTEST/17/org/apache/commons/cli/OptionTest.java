package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    @Test
    public void testOptionConstructionWithOptAndDescription() {
        Option option = new Option("a", "Option A description");
        assertEquals("a", option.getOpt());
        assertEquals("Option A description", option.getDescription());
    }
}