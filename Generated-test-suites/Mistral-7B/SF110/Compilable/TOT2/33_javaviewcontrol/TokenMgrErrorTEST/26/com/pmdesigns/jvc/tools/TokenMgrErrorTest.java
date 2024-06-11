package com.pmdesigns.jvc.tools;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "Hello, World!";
        String expected = "Hello, World!";
        String actual = TokenMgrError.addEscapes(str);
        assertEquals(expected, actual);
    }
}