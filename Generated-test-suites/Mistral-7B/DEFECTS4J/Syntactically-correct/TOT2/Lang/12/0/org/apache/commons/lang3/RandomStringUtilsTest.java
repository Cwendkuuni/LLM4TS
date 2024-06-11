import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
        // Basic functionality test
    }

    @Test
    public void testRandomAsciiString() {
        int count = 5;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        for (int i = 0; i < count; i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue("Character should be ASCII", (c >= 32 && c <= 126));
        }
        // Basic functionality test
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 7;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        for (int i = 0; i < count; i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue("Character should be alphabetic", Character.isAlphabetic(c));
        }
        // Basic functionality test
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 12;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        for (int i = 0; i < count; i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue("Character should be alphanumeric", Character.isAlphabetic(c) || Character.isDigit(c));
        }
        // Basic functionality test
    }

    @Test
    public void testRandomNumericString() {
        int count = 15;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        for (int i = 0; i < count; i++) {
            char c = randomNumericString.charAt(i);
            assertTrue("Character should be numeric", Character.isDigit(c));
        }
        // Basic functionality test
    }

    @Test
    public void testRandomStringWithRange() {
        int count = 10;
        int start = 97, end = 122;
        String randomString = RandomStringUtils.random(count, start, end, false, false);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be between start and end", start <= c && c <= end);
        }
        // Basic functionality test
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharArray() {
        RandomStringUtils.random(1, new char[0]);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutOfBoundsCharArray() {
        int count = 10;
        int start = 97, end = 122;
        char[] chars = new char[count + 1];
        RandomStringUtils.random(count, start, end, false, false, chars, RANDOM);
    }

    @Test
    public void testRandomStringWithLettersAndNumbers() {
        int count = 15;
        String randomString = RandomStringUtils.random(count, true, true);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be alphanumeric", Character.isAlphabetic(c) || Character.isDigit(c));
        }
        // Basic functionality test
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStart() {
        int count = 10;
        int start = -1, end = 122;
        RandomStringUtils.random(count, start, end, false, false, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEnd() {
        int count = 10;
        int start = 97, end = -1;
        RandomStringUtils.random(count, start, end, false, false, null, RANDOM);
    }

    @Test
    public void testRandomStringWithCustomRandom() {
        int count = 10;
        Random customRandom = new Random(1);
        String randomString = RandomStringUtils.random(count, 97, 122, false, false, null, customRandom);
        // Test for custom random
    }

    @Test
    public void testRandomStringWithEmptyString() {
        int count = 0;
        String randomString = RandomStringUtils.random(count, "abcdefghijklmnopqrstuvwxyz");
        assertEquals("Empty string", "", randomString);
        // Basic functionality test
    }

    @Test
    public void testRandomStringWithNullString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, null);
        // Test for null string
    }

}