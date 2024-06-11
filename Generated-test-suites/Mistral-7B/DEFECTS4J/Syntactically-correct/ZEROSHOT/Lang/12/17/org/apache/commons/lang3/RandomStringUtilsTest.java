package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method.
     */
    @Test
    public void testRandom() {
        // Test empty string
        assertEquals("", RandomStringUtils.random(0));

        // Test with length 1
        assertEquals("a", RandomStringUtils.random(1));

        // Test with length greater than 0
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());

        // Test with length less than 0
        try {
            RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with length 0
        try {
            RandomStringUtils.random(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for randomAscii method.
     */
    @Test
    public void testRandomAscii() {
        // Test empty string
        assertEquals("", RandomStringUtils.randomAscii(0));

        // Test with length 1
        assertEquals("a", RandomStringUtils.randomAscii(1));

        // Test with length greater than 0
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());

        // Test with length less than 0
        try {
            RandomStringUtils.randomAscii(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with length 0
        try {
            RandomStringUtils.randomAscii(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for randomAlphabetic method.
     */
    @Test
    public void testRandomAlphabetic() {
        // Test empty string
        assertEquals("", RandomStringUtils.randomAlphabetic(0));

        // Test with length 1
        assertEquals("a", RandomStringUtils.randomAlphabetic(1));

        // Test with length greater than 0
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());

        // Test with length less than 0
        try {
            RandomStringUtils.randomAlphabetic(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with length 0
        try {
            RandomStringUtils.randomAlphabetic(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for randomAlphanumeric method.
     */
    @Test
    public void testRandomAlphanumeric() {
        // Test empty string
        assertEquals("", RandomStringUtils.randomAlphanumeric(0));

        // Test with length 1
        assertEquals("a", RandomStringUtils.randomAlphanumeric(1));

        // Test with length greater than 0
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());

        // Test with length less than 0
        try {
            RandomStringUtils.randomAlphanumeric(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with length 0
        try {
            RandomStringUtils.randomAlphanumeric(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for randomNumeric method.
     */
    @Test
    public void testRandomNumeric() {
        // Test empty string
        assertEquals("", RandomStringUtils.randomNumeric(0));

        // Test with length 1
        assertEquals("0", RandomStringUtils.randomNumeric(1));

        // Test with length greater than 0
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());

        // Test with length less than 0
        try {
            RandomStringUtils.randomNumeric(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with length 0
        try {
            RandomStringUtils.randomNumeric(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for random method with custom range.
     */
    @Test
    public void testRandomWithCustomRange() {
        // Test empty string
        assertEquals("", RandomStringUtils.random(0, 'a', 'z'));

        // Test with length 1
        assertEquals("a", RandomStringUtils.random(1, 'a', 'z'));

        // Test with length greater than 0
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertEquals(10, randomString.length());

        // Test with length less than 0
        try {
            RandomStringUtils.random(-1, 'a', 'z');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with length 0
        try {
            RandomStringUtils.random(0, 'a', 'z');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for randomAscii method with custom range.
     */
    @Test
    public void testRandomAsciiWithCustomRange() {
        // Test empty string
        assertEquals("", RandomStringUtils.randomAscii(0));

        // Test with length 1
        assertEquals("!", RandomStringUtils.randomAscii(1));

        // Test with length greater than 0
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());

        // Test with length less than 0
        try {
            RandomStringUtils.randomAscii(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with length 0
        try {
            RandomStringUtils.randomAscii(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for randomAlphabetic method with custom range.
     */
    @Test
    public void testRandomAlphabeticWithCustomRange() {
        // Test empty string
        assertEquals("", RandomStringUtils.randomAlphabetic(0, false));

        // Test with length 1
        assertEquals("a", RandomStringUtils.randomAlphabetic(1, false));

        // Test with length greater than 0
        String randomString = RandomStringUtils.randomAlphabetic(10, false);
        assertEquals(10, randomString.length());

        // Test with length less than 0
        try {
            RandomStringUtils.randomAlphabetic(-1, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with length 0
        try {
            RandomStringUtils.randomAlphabetic(0, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for randomAlphanumeric method with custom range.
     */
    @Test
    public void testRandomAlphanumericWithCustomRange() {
        // Test empty string
        assertEquals("", RandomStringUtils.randomAlphanumeric(0, false, false));

        // Test with length 1
        assertEquals("0", RandomStringUtils.randomAlphanumeric(1, false, false));

        // Test with length greater than 0
        String randomString = RandomStringUtils.randomAlphanumeric(10, true, true);
        assertEquals(10, randomString.length());

        // Test with length less than 0
        try {
            RandomStringUtils.randomAlphanumeric(-1, true, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with length 0
        try {
            RandomStringUtils.randomAlphanumeric(0, true, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for random method with custom character set.
     */
    @Test
    public void testRandomWithCustomCharacterSet() {
        String customSet = "!@#$%&*()_+-=[]{}|;:,.<>";

        // Test empty string
        assertEquals("", RandomStringUtils.random(0, customSet.toCharArray()));

        // Test with length 1
        assertEquals("!", RandomStringUtils.random(1, customSet.toCharArray()));

        // Test with length greater than 0
        String randomString = RandomStringUtils.random(10, customSet.toCharArray());
        assertEquals(10, randomString.length());

        // Test with length less than 0
        try {
            RandomStringUtils.random(-1, customSet.toCharArray());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with length 0
        try {
            RandomStringUtils.random(0, customSet.toCharArray());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for random method with custom character set and random.
     */
    @Test
    public void testRandomWithCustomCharacterSetAndRandom() {
        String customSet = "!@#$%&*()_+-=[]{}|;:,.<>";
        Random random = new Random();

        // Test empty string
        assertEquals("", RandomStringUtils.random(0, customSet.toCharArray(), random));

        // Test with length 1
        assertEquals("!", RandomStringUtils.random(1, customSet.toCharArray(), random));

        // Test with length greater than 0
        String randomString = RandomStringUtils.random(10, customSet.toCharArray(), random);
        assertEquals(10, randomString.length());

        // Test with length less than 0
        try {
            RandomStringUtils.random(-1, customSet.toCharArray(), random);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with length 0
        try {
            RandomStringUtils.random(0, customSet.toCharArray(), random);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}