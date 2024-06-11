package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/**
 * Tests for the Caverphone class
 */
public class CaverphoneTest {

    /**
     * Instance of Caverphone under test
     */
    private Caverphone caverphone = new Caverphone();

    /**
     * Test encoding an empty string
     */
    @Test
    public void testEmptyString() {
        String expected = "1111111111";
        String actual = caverphone.encode("");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with only lowercase letters
     */
    @Test
    public void testLowerCaseString() {
        String expected = "2222222222";
        String actual = caverphone.encode("apple");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with special characters
     */
    @Test
    public void testSpecialCharactersString() {
        String input = "C0ff33 c@n p1t!";
        String expected = "C0f2f k3n p1t1";
        String actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encoding a string that matches start options
     */
    @Test
    public void testStartOptionsString() {
        String input = "Coughing";
        String expected = "cou2f2ng";
        String actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Rough";
        expected = "rou2f";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Enough";
        expected = "enou2f";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Tough";
        expected = "tou2f";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Trough";
        expected = "trou2f";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Gn";
        expected = "2n";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with various replacements
     */
    @Test
    public void testReplacementsString() {
        String input = "Cq";
        String expected = "2q";
        String actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Ci";
        expected = "si";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Ce";
        expected = "se";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Cy";
        expected = "sy";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Tch";
        expected = "2ch";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "C";
        expected = "k";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Q";
        expected = "k";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "X";
        expected = "k";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "V";
        expected = "f";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Dg";
        expected = "2g";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Tio";
        expected = "sio";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Tia";
        expected = "sia";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "D";
        expected = "t";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Ph";
        expected = "fh";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "B";
        expected = "p";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Sh";
        expected = "s2";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Z";
        expected = "s";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Why";
        expected = "Why"; // Test for identity check in isCaverphoneEqual method
        String actual1 = caverphone.encode(input);
        Assert.assertEquals(expected, actual1);

        String actual2 = new Caverphone().encode(input);
        Assert.assertEquals(expected, actual2);

        input = "Why"; // Test for identity check in isCaverphoneEqual method
        String expected1 = caverphone.encode(input);
        String actual3 = caverphone.encode(input);
        Assert.assertEquals(expected1, actual3);
    }

    /**
     * Test encoding a string with various removals
     */
    @Test
    public void testRemovalsString() {
        String input = "2";
        String expected = "";
        String actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "3$";
        expected = "A";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "3";
        expected = "";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with various special cases
     */
    @Test
    public void testSpecialCasesString() {
        String input = "A";
        String expected = "A";
        String actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "H";
        expected = "A";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "R3";
        expected = "R3";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "R$";
        expected = "3";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "L3";
        expected = "L3";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "L$";
        expected = "3";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Y3";
        expected = "Y3"; // 1.0 only
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "W3";
        expected = "W3";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Wy"; // 1.0 only
        expected = "Wy";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "Wh3";
        expected = "Wh3";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "W";
        expected = "2";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "h";
        expected = "A";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "h";
        expected = "2";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "r3";
        expected = "R3";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "r$";
        expected = "3";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "r";
        expected = "2";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "l3";
        expected = "L3";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "l$";
        expected = "3";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "ly"; // 1.0 only
        expected = "Ly";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "l";
        expected = "2";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "j";
        expected = "y"; // 1.0 only
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "y3"; // 1.0 only
        expected = "Y3";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        input = "y"; // 1.0 only
        expected = "2";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with an Object as argument
     */
    @Test(expected = EncoderException.class)
    public void testStringAsObject() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(new Integer(1));
    }

    /**
     * Test encoding an empty string with an Object as argument
     */
    @Test
    public void testEmptyStringAsObject() {
        String emptyString = "";
        Caverphone caverphone = new Caverphone();
        String actual = (String) caverphone.encode(emptyString);
        String expected = "1111111111";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with special characters as an Object as argument
     */
    @Test
    public void testSpecialCharactersAsObject() {
        String input = "C@ff33 c@n p1t!";
        Caverphone caverphone = new Caverphone();
        String actual = (String) caverphone.encode(input);
        String expected = "C0f2f k3n p1t1";
        Assert.assertEquals(expected, actual);
    }
}