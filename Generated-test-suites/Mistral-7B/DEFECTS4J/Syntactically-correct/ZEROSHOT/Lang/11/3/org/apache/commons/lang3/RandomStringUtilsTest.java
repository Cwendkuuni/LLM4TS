import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomStringLength() {
        String randomString = RandomStringUtils.random(5);
        Assert.assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        for (int i = 0; i < randomAsciiString.length(); i++) {
            Assert.assertTrue(randomAsciiString.charAt(i) >= 32 && randomAsciiString.charAt(i) <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            Assert.assertTrue(Character.isAlphabetic(randomAlphabeticString.charAt(i)));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            Assert.assertTrue(Character.isLetterOrDigit(randomAlphanumericString.charAt(i)));
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        for (int i = 0; i < randomNumericString.length(); i++) {
            Assert.assertTrue(Character.isDigit(randomNumericString.charAt(i)));
        }
    }

    @Test
    public void testRandomStringWithCustomLengthAndCharacterSet() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        Assert.assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Assert.assertTrue(Character.isLetterOrDigit(randomString.charAt(i)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringEmptyCharacterSet() {
        RandomStringUtils.random(5, new char[0]);
    }
}