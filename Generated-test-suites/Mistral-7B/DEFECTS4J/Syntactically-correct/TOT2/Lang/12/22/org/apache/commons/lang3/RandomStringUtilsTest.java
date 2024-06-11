import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        Assert.assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        int count = 5;
        String randomString = RandomStringUtils.randomAscii(count);
        Assert.assertEquals(count, randomString.length());
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            Assert.assertTrue(c >= 32 && c <= 126);
        }
    }

    // Add other test methods for randomAlphabetic, randomAlphanumeric, randomNumeric methods

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        int count = -1;
        try {
            RandomStringUtils.random(count);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Requested random string length " + count + " is less than 0.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharsArray() {
        char[] chars = new char[0];
        try {
            RandomStringUtils.random(10, chars);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The provided chars array is empty.", e.getMessage());
        }
    }

    @Test
    public void testRandomStringWithCharsArray() {
        char[] chars = new char[]{'a', 'b', 'c'};
        int count = 5;
        String randomString = RandomStringUtils.random(count, chars);
        Assert.assertEquals(count, randomString.length());
        for (int i = 0; i < count; i++) {
            Assert.assertTrue(Arrays.binarySearch(chars, randomString.charAt(i)) >= 0);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testInvalidStartEnd() {
        char[] chars = new char[]{'a', 'b', 'c'};
        try {
            RandomStringUtils.random(1, 3, 1, false, false, chars, RANDOM);
        } catch (ArrayIndexOutOfBoundsException e) {
            Assert.assertEquals("There are not (end - start) + 1 characters in the set array.", e.getMessage());
        }
    }

    @Test
    public void testRandomStringWithCustomRandom() {
        int count = 10;
        char[] chars = new char[]{'a', 'b', 'c'};
        Random random = new Random();
        String randomString = RandomStringUtils.random(count, 0, chars.length, false, false, chars, random);
        Assert.assertEquals(count, randomString.length());
        for (int i = 0; i < count; i++) {
            Assert.assertTrue(Arrays.binarySearch(chars, randomString.charAt(i)) >= 0);
        }
    }

    
}