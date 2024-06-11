import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertFalse("Random string should not contain duplicate characters", randomString.charAt(i) == randomString.charAt(i + 1));
        }
    }

    @Test
    public void testRandomString_Ascii() {
        int count = 10;
        String randomString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assert(32 <= randomString.charAt(i) && randomString.charAt(i) <= 126);
        }
    }

    @Test
    public void testRandomString_Alphabetic() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should contain only alphabetic characters", Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomString_Alphanumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should contain only alphanumeric characters", Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomString_Numeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should contain only numeric characters", Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomString_NegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test
    public void testRandomString_EmptyCharArray() {
        RandomStringUtils.random(0, new char[0]);
    }

    @Test
    public void testRandomString_NoAlphabeticAndNumeric() {
        String randomString = RandomStringUtils.random(10, false, false);
        assertFalse("Random string should not contain only whitespaces", randomString.matches("\\s+"));
        for (int i = 0; i < 10; i++) {
            assert(32 > randomString.charAt(i) || randomString.charAt(i) < 127);
        }
    }

    @Test
    public void testRandomString_CustomCharSet() {
        int count = 10;
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;':\"<>,.?/~`";
        String randomString = RandomStringUtils.random(count, charSet.toCharArray());
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should contain only the provided characters", charSet.indexOf(randomString.charAt(i)) != -1);
        }
    }

    @Test
    public void testRandomString_EmptyStringAsCharSet() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, "");
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should contain any character", true);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomString_EmptyCharSet() {
        RandomStringUtils.random(10, new char[0]);
    }

    @Test
    public void testRandomString_CustomCharSetWithMinAndMax() {
        int start = 97;
        int end = 123;
        String charSet = new String(new char[end - start + 1]).substring(start, end + 1);
        int count = 10;
        String randomString = RandomStringUtils.random(count, start, end, false, false, null, RANDOM);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should contain only the provided characters", charSet.indexOf(randomString.charAt(i)) != -1);
            assertTrue("Random string should have the given range", start <= randomString.charAt(i) && randomString.charAt(i) <= end);
        }
    }

    @Test
    public void testRandomString_LettersAndNumbers() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, true, true);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should contain only alphabetic and numeric characters", Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomString_LettersAndNumbersWithCustomCharSet() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;':\"<>,.?/~`";
        int count = 10;
        String randomString = RandomStringUtils.random(count, true, true, charSet.toCharArray(), RANDOM);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should contain only alphabetic and numeric characters", Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)) || charSet.indexOf(randomString.charAt(i)) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomString_InvalidBooleanValues() {
        RandomStringUtils.random(10, true, false, null, null, null, null);
    }
}