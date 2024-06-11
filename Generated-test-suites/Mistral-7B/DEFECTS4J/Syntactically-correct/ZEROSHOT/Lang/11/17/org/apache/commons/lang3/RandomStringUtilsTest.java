package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test RandomStringUtils.random().
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test RandomStringUtils.randomAscii().
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    /**
     * Test RandomStringUtils.randomAlphabetic().
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test RandomStringUtils.randomAlphanumeric().
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test RandomStringUtils.randomNumeric().
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test RandomStringUtils.random(int, boolean, boolean).
     */
    @Test
    public void testRandomLettersAndNumbers() {
        String randomString = RandomStringUtils.random(5, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test RandomStringUtils.random(int, int, int, boolean, boolean, char[], Random).
     */
    @Test
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(5, 65, 90, true, true, null, RandomStringUtils.RANDOM);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test RandomStringUtils.random(int, int, int, boolean, boolean, char[], Random)
     * with custom character set.
     */
    @Test
    public void testRandomCustomCharacterSet() {
        String randomString = RandomStringUtils.random(5, 97, 122, true, true, new char[]{'a', 'b'}, RandomStringUtils.RANDOM);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isLetter(c) && (c >= 'a' && c <= 'z'));
        }
    }

    /**
     * Test RandomStringUtils.random(int, int, int, boolean, boolean, char[], Random)
     * with character set smaller than start.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomSmallCharacterSet() {
        RandomStringUtils.random(5, 100, 97, true, true, new char[]{'a', 'b'}, RandomStringUtils.RANDOM);
    }

    /**
     * Test RandomStringUtils.random(int, int, int, boolean, boolean, char[], Random)
     * with character set larger than end.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomLargeCharacterSet() {
        RandomStringUtils.random(5, 65, 123, true, true, new char[]{'a', 'b'}, RandomStringUtils.RANDOM);
    }

    /**
     * Test RandomStringUtils.random(int, char[]).
     */
    @Test
    public void testRandomCustomCharacterSetNoLength() {
        String randomString = RandomStringUtils.random(5, new char[]{'a', 'b'});
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(new String(new char[]{c}).matches("[ab]"));
        }
    }

    /**
     * Test RandomStringUtils.random(int, String).
     */
    @Test
    public void testRandomCustomCharacterSetString() {
        String randomString = RandomStringUtils.random(5, "ab");
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(new String(new char[]{c}).matches("[ab]"));
        }
    }

    /**
     * Test RandomStringUtils.random(int, String) with empty string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSet() {
        RandomStringUtils.random(5, "");
    }

    /**
     * Test RandomStringUtils.random(int, int) with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(5);
    }

    /**
     * Test RandomStringUtils.random(int, int, int, boolean, boolean, char[], Random) with null character array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullCharacterSet() {
        RandomStringUtils.random(5, 65, 90, true, true, null, RandomStringUtils.RANDOM);
    }

    /**
     * Test RandomStringUtils.random(int, int, int, boolean, boolean, char[], Random) with null random.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullRandom() {
        RandomStringUtils.random(5, 65, 90, true, true, new char[]{'a', 'b'}, null);
    }
}