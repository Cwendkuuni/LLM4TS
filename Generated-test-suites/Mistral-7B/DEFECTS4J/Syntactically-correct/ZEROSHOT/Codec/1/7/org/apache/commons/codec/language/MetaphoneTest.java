package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;

/**
 * Test file for the Metaphone class
 */
public class MetaphoneTest {

    /**
     * Instance of Metaphone class under test
     */
    private Metaphone metaphone;

    /**
     * Setup method for the test class
     * Initializes an instance of the Metaphone class
     */
    @Before
    public void setup() {
        metaphone = new Metaphone();
    }

    /**
     * Test method for the metaphone(String txt) method.
     * @throws EncoderException
     */
    @Test
    public void testMetaphone() throws EncoderException {
        String input = "apple";
        String expected = "A0P0L1E0";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "banana";
        expected = "B2N1A0N1A0";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "cat";
        expected = "K1T0";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "dog";
        expected = "D2G0";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "elephant";
        expected = "E1L1P1H1N1T0";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "example";
        expected = "E1X1A0M1P1L1E0";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "hello";
        expected = "H2L0";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "i";
        expected = "I0";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "indian";
        expected = "I0N1D1A0N1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "kangaroo";
        expected = "K1NG1A0R1U0";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "kitten";
        expected = "K1T1I0N1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "letter";
        expected = "L1T1E0R1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "metaphone";
        expected = "M1T1A0T1A0P1H1O1N1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "november";
        expected = "N1V1V1R1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "orange";
        expected = "O1R1A0N1G1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "pineapple";
        expected = "P1N1E1A0P1L1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "queen";
        expected = "Q1U1I1N1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "racecar";
        expected = "R1A1S1E1C1A1R1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "tomato";
        expected = "T1O1M1A0T1O1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "tuesday";
        expected = "T2U1S1D1A1Y1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "universe";
        expected = "U1N11V1S1R1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "watermelon";
        expected = "W1T1A0M1E1L1O1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "xylophone";
        expected = "X2Y1L1O1F1O1N1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "zebra";
        expected = "Z1B1R1A0";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    /**
     * Test method for the isMetaphoneEqual(String str1, String str2) method.
     */
    @Test
    public void testIsMetaphoneEqual() {
        String str1 = "apple";
        String str2 = "apple";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "apple";
        str2 = "banana";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "cat";
        str2 = "cat";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "cat";
        str2 = "dog";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "elephant";
        str2 = "elephant";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "example";
        str2 = "example";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "hello";
        str2 = "hello";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "i";
        str2 = "i";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "indian";
        str2 = "indian";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "kangaroo";
        str2 = "kangaroo";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "kitten";
        str2 = "kitten";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "letter";
        str2 = "letter";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "metaphone";
        str2 = "metaphone";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "november";
        str2 = "november";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "orange";
        str2 = "orange";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "pineapple";
        str2 = "pineapple";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "queen";
        str2 = "queen";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "racecar";
        str2 = "racecar";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "tomato";
        str2 = "tomato";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "tuesday";
        str2 = "tuesday";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "universe";
        str2 = "universe";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "watermelon";
        str2 = "watermelon";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "xylophone";
        str2 = "xylophone";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "zebra";
        str2 = "zebra";
        assertTrue(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "apple";
        str2 = "applewood";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "banana";
        str2 = "orange";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "cat";
        str2 = "dog";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "elephant";
        str2 = "zebra";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "example";
        str2 = "kangaroo";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "hello";
        str2 = "i";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "indian";
        str2 = "letter";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "kitten";
        str2 = "metaphone";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "letter";
        str2 = "november";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "metaphone";
        str2 = "pineapple";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "november";
        str2 = "queen";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "orange";
        str2 = "racecar";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "pineapple";
        str2 = "tomato";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "queen";
        str2 = "tuesday";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "racecar";
        str2 = "universe";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "tomato";
        str2 = "watermelon";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "tuesday";
        str2 = "xylophone";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "universe";
        str2 = "zebra";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "watermelon";
        str2 = "apple";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));

        str1 = "xylophone";
        str2 = "banana";
        assertFalse(metaphone.isMetaphoneEqual(str1, str2));
    }

    /**
     * Test method for the getMaxCodeLen() method.
     */
    @Test
    public void testGetMaxCodeLen() {
        int maxCodeLen = metaphone.getMaxCodeLen();
        assertEquals(4, maxCodeLen);
    }

    /**
     * Test method for the setMaxCodeLen(int maxCodeLen) method.
     */
    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(5);
        int maxCodeLen = metaphone.getMaxCodeLen();
        assertEquals(5, maxCodeLen);
    }
}