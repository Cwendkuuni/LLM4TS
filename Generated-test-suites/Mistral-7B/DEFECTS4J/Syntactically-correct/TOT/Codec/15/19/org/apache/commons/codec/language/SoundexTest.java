package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    /**
     * Test method for {@link Soundex#Soundex()}.
     */
    @Test
    public void testSoundexDefaultConstructor() {
        Soundex soundex = new Soundex();
        String expected = soundex.encode("test");
        String actual = soundex.soundex("test");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Soundex#Soundex(char[])}.
     */
    @Test
    public void testSoundexCustomMappingConstructor() {
        char[] customMapping = {'1', '1', '1', '1', '2', '2', '2', '2', '3', '3', '3', '3', '4', '4', '4', '4', '5', '5', '5', '5'};
        Soundex soundex = new Soundex(customMapping);
        String expected = soundex.encode("test");
        String actual = soundex.soundex("test");
        Assert.assertNotEquals(expected, actual); // Custom mapping should result in different soundex codes
    }

    /**
     * Test method for {@link Soundex#Soundex(String)}.
     */
    @Test
    public void testSoundexMappingStringConstructor() {
        Soundex soundex = new Soundex("1111011022220102");
        String expected = soundex.encode("test");
        String actual = soundex.soundex("test");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Soundex#difference(String, String)}.
     */
    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("test1", "test2");
        Assert.assertEquals(difference, 0); // Different strings
        difference = soundex.difference("test1", "test1");
        Assert.assertEquals(difference, 4); // Same string
    }

    /**
     * Test method for {@link Soundex#encode(Object)}.
     */
    @Test
    public void testEncodeStringObject() {
        Soundex soundex = new Soundex();
        Object obj = "test";
        String expected = soundex.encode(obj);
        String actual = soundex.encode(obj.toString());
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Soundex#encode(String)}.
     */
    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String input = "test";
        String expected = soundex.encode(input);
        String actual = soundex.encode(input.toUpperCase());
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Soundex#getMappingCode(String, int)}.
     */
    @Test(expected = EncoderException.class)
    public void testGetMappingCodeException() {
        Soundex soundex = new Soundex();
        soundex.getMappingCode("test", -1);
    }

    /**
     * Test method for {@link Soundex#getMappingCode(String, int)}.
     */
    @Test
    public void testGetMappingCode() {
        Soundex soundex = new Soundex();
        char mappingCode = soundex.getMappingCode("test", 0);
        Assert.assertEquals(mappingCode, soundex.map('T'));
    }

    /**
     * Test method for {@link Soundex#map(char)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMapException() {
        Soundex soundex = new Soundex();
        soundex.map(' ');
    }

    /**
     * Test method for {@link Soundex#map(char)}.
     */
    @Test
    public void testMap() {
        Soundex soundex = new Soundex();
        char expected = soundex.map('T');
        char actual = soundex.getSoundexMapping()[6];
        Assert.assertEquals(expected, actual);
    }

}