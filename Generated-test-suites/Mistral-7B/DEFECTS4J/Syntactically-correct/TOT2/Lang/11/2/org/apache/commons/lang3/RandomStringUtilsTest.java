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
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
    }

    @Test
    public void testRandomNumericString() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
    }

    @Test
    public void testRandomStringWithRange() {
        String randomString = RandomStringUtils.random(10, 65, 90);
        assertNotNull(randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRandomStringLength() {
        RandomStringUtils.random(-5);
    }

    @Test
    public void testEmptyRandomStringArray() {
        char[] emptyCharArray = {};
        String randomString = RandomStringUtils.random(5, emptyCharArray);
        assertNotNull(randomString);
    }

    @Test
    public void testNullRandomStringArray() {
        String randomString = RandomStringUtils.random(5, null);
        assertNotNull(randomString);
    }
}