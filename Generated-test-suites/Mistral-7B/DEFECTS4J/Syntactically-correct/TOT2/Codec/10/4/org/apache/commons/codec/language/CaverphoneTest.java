
package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphoneEmpty() {
        String input = "";
        String expected = "1111111111";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneNull() {
        String input = null;
        String expected = "1111111111";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneSingleCharacter() {
        String input = "a";
        String expected = "A";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeString() {
        String input = "example";
        String expected = "2222222222222222A";
        String result = caverphone.encode(input);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObjectNull() {
        Object pObject = null;
        caverphone.encode(pObject);
    }

    @Test
    public void testEncodeObjectNonString() {
        Object pObject = new Integer(42);
        String result = (String) caverphone.encode(pObject);
        assertNull(result);
    }

    @Test
    public void testIsCaverphoneEqualEmptyStrings() {
        String str1 = "";
        String str2 = "";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqualNullStrings() {
        String str1 = null;
        String str2 = null;
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqualDifferentStrings() {
        String str1 = "example1";
        String str2 = "exampleX";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertFalse(result);
    }
}