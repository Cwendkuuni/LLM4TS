// RandomStringUtilsTest.java

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
    }

    @Test
    public void testRandomStringWithRange() {
        String randomString = RandomStringUtils.random(10, 33, 126);
        assertNotNull(randomString);
    }

    @Test
    public void testRandomStringWithChars() {
        String randomString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c'});
        assertNotNull(randomString);
    }

    @Test
    public void testRandomStringWithCustomRandom() {
        String randomString = RandomStringUtils.random(10, 33, 126, new Random(1L));
        assertNotNull(randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringEmptyChars() {
        RandomStringUtils.random(1, (char[]) null);
    }

    @Test
    public void testRandomStringHighSurrogates() {
        String randomString = RandomStringUtils.random(1, 55296, 57343, true, true);
        assertNotNull(randomString);
    }

    @Test
    public void testRandomStringEdgeCases() {
        String randomString = RandomStringUtils.random(1, '¡', '~', true, true);
        assertNotNull(randomString);
    }
}