package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test random method with default settings
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Test random method with specific character set
        String randomStringAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomStringAlphanumeric);
        assertEquals(10, randomStringAlphanumeric.length());
        for (char c : randomStringAlphanumeric.toCharArray()) {
            if (Character.isLetter(c) || Character.isDigit(c)) {
                continue;
            } else {
                throw new AssertionError("Character is not alphanumeric: " + c);
            }
        }

        // Test random method with specific length and character range
        String randomStringAscii = RandomStringUtils.randomAscii(10);
        assertNotNull(randomStringAscii);
        assertEquals(10, randomStringAscii.length());
        for (char c : randomStringAscii.toCharArray()) {
            if ((c < 32) || (c > 126)) {
                throw new AssertionError("Character is not ASCII: " + c);
            }
        }

        // Test random method with specific length and character set
        String randomStringCustom = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", 10);
        assertNotNull(randomStringCustom);
        assertEquals(10, randomStringCustom.length());
        for (char c : randomStringCustom.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                throw new AssertionError("Character is not in the character set: " + c);
            }
        }
    }

    @Test
    public void testRandomWithSeed() {
        Random originalRandom = new Random();
        String randomString1 = RandomStringUtils.random(10, originalRandom);

        Random seededRandom = new Random(originalRandom.nextLong());
        String randomString2 = RandomStringUtils.random(10, seededRandom);

        assertEquals(randomString1, randomString2);
    }
}