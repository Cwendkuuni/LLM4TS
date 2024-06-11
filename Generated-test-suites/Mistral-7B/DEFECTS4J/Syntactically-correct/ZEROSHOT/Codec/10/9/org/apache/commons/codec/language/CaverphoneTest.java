package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Caverphone.*;

public class CaverphoneTest {

    @Test
    public void testEmptyString() {
        String result = caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testNullString() {
        try {
            caverphone(null);
            fail("EncoderException expected");
        } catch (EncoderException ignore) {
            // Expected
        }
    }

    @Test
    public void testLowercaseLetters() {
        String result = caverphone("hello");
        assertEquals("h2l2o2o", result);
    }

    @Test
    public void testRemoveNonLetters() {
        String result = caverphone("hello123");
        assertEquals("h2l2o", result);
    }

    @Test
    public void testRemoveFinalE() {
        String result = caverphone("enough");
        assertEquals("enou2f", result);
    }

    @Test
    public void testHandlesStartOptions() {
        String result = caverphone("coughenough");
        assertEquals("cou2fenou2f", result);
    }

    @Test
    public void testHandlesReplacements() {
        String result = caverphone("actiontch");
        assertEquals("act2ch", result);
    }

    @Test
    public void testHandlesRemovals() {
        String result = caverphone("123two3four");
        assertEquals("2f", result);
    }

    @Test
    public void testHandlesAdditions() {
        String result = caverphone("hello");
        assertEquals("h2l2o2o11111111", result);
    }

    @Test
    public void testEncodeObject() {
        String result = encode("hello");
        assertEquals("h2l2o2o11111111", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        String str1 = "hello";
        String str2 = "world";
        assertFalse(isCaverphoneEqual(str1, str2));

        String str3 = "helloworld";
        assertTrue(isCaverphoneEqual(str1, str3));
    }
}