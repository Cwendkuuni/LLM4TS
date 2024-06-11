import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertFalse(randomString.isEmpty());
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertFalse(randomAsciiString.isEmpty());
        assertEquals(5, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertFalse(randomAlphabeticString.isEmpty());
        assertEquals(5, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertFalse(randomAlphanumericString.isEmpty());
        assertEquals(5, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue((Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertFalse(randomNumericString.isEmpty());
        assertEquals(5, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithRange() {
        String randomStringWithRange = RandomStringUtils.random(5, 65, 90);
        assertNotNull(randomStringWithRange);
        assertFalse(randomStringWithRange.isEmpty());
        assertEquals(5, randomStringWithRange.length());
        for (int i = 0; i < randomStringWithRange.length(); i++) {
            char c = randomStringWithRange.charAt(i);
            assertTrue(c >= 65 && c <= 90);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithInvalidCharArray() {
        char[] chars = new char[0];
        RandomStringUtils.random(1, chars);
    }
}