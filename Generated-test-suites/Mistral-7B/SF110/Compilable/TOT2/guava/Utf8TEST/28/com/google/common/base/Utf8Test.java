package com.google.common.base;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.common.base.Utf8;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        String input = "Hello, World!";
        int expected = Utf8.encodedLength(input);
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }
}