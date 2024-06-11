package com.pmdesigns.jvc.tools;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "\0ba\tb\tc\n\fd\r\"'\\";
        String expectedOutput = "\\0\\b\\b\\t\\n\\f\\r\\\"\\'\\\\";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }
}