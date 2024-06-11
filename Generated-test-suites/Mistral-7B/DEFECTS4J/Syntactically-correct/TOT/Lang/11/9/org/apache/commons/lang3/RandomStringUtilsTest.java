package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test cases for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test random string creation with default settings.
     */
    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertFalse(Character.isDigit(randomString.charAt(i)) && Character.isDigit(randomString.charAt(i + 1)));
        }
    }

    /**
     * Test random string creation with specified length and Ascii characters only.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isISOControl(randomString.charAt(i)) || Character.isWhitespace(randomString.charAt(i))
                    || Character.isLetterOrDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test random string creation with specified length and alphabetic characters only.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    /**
     * Test random string creation with specified length and alphanumeric characters only.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test random string creation with specified length and numeric characters only.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test random string creation with specified length, start and end position in character set, and alphanumeric characters only.
     */
    @Test
    public void testRandomAlphanumericWithRange() {
        String randomString = RandomStringUtils.random("a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,1,2,3,4,5,6,7,8,9", 10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test random string creation with specified length, character set and source of randomness.
     */
    @Test
    public void testRandomStringWithCustomSourceOfRandomness() {
        String randomString = RandomStringUtils.random("!@#$%^&*()_+{}:<>?".toCharArray(), 10, new Random(1L), false, false);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i))
                    || Character.isISOControl(randomString.charAt(i)) || Character.isWhitespace(randomString.charAt(i))
                    || randomString.charAt(i) == '!' || randomString.charAt(i) == '@' || randomString.charAt(i) == '#'
                    || randomString.charAt(i) == '$' || randomString.charAt(i) == '%' || randomString.charAt(i) == '^'
                    || randomString.charAt(i) == '&' || randomString.charAt(i) == '(' || randomString.charAt(i) == ')'
                    || randomString.charAt(i) == '+' || randomString.charAt(i) == '{' || randomString.charAt(i) == '}'
                    || randomString.charAt(i) == ':' || randomString.charAt(i) == ';' || randomString.charAt(i) == '<'
                    || randomString.charAt(i) == '>' || randomString.charAt(i) == '?' || randomString.charAt(i) == '|');
        }
    }
}