import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            assertTrue(randomAsciiString.charAt(i) >= 32 && randomAsciiString.charAt(i) <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomAlphabeticString.charAt(i)));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char ch = randomAlphanumericString.charAt(i);
            assertTrue(Character.isAlphabetic(ch) || Character.isDigit(ch));
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            assertTrue(Character.isDigit(randomNumericString.charAt(i)));
        }
    }

    @Test
    public void testRandomStringWithStartEnd() {
        String randomString = RandomStringUtils.random(5, 65, 90);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= 65 && randomString.charAt(i) <= 90);
        }
    }

    @Test
    public void testRandomStringWithStartEndAndLengthLetters() {
        String randomString = RandomStringUtils.random(5, 65, 90, true, false);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomStringWithStartEndAndLengthNumbers() {
        String randomString = RandomStringUtils.random(5, 65, 90, false, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomStringWithStartEndAndLengthLettersAndNumbers() {
        String randomString = RandomStringUtils.random(5, 65, 90, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char ch = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(ch) || Character.isDigit(ch));
        }
    }

    @Test
    public void testRandomStringWithChars() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(5, chars.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(chars.indexOf(randomString.charAt(i)) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyChars() {
        RandomStringUtils.random(5, new char[0]);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithInvalidStartEnd() {
        String randomString = RandomStringUtils.random(5, 0, -1, false, false, null, RANDOM);
    }
}