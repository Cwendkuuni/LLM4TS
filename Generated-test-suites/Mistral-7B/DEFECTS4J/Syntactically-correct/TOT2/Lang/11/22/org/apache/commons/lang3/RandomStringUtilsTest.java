import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RandomStringUtilsTest {

 @Test
 public void testRandomString() {
 String randomString = RandomStringUtils.random(10);
 assertNotNull(randomString);
 assertEquals(10, randomString.length());
 }

 @Test
 public void testRandomAsciiString() {
 String randomAsciiString = RandomStringUtils.randomAscii(10);
 assertNotNull(randomAsciiString);
 assertEquals(10, randomAsciiString.length());
 }

 @Test
 public void testRandomAlphabeticString() {
 String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
 assertNotNull(randomAlphabeticString);
 assertEquals(10, randomAlphabeticString.length());
 }

 @Test
 public void testRandomAlphanumericString() {
 String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
 assertNotNull(randomAlphanumericString);
 assertEquals(10, randomAlphanumericString.length());
 }

 @Test
 public void testRandomNumericString() {
 String randomNumericString = RandomStringUtils.randomNumeric(10);
 assertNotNull(randomNumericString);
 assertEquals(10, randomNumericString.length());
 }

 @Test
 public void testRandomStringWithRange() {
 String randomStringWithRange = RandomStringUtils.random(10, 'a', 'z');
 assertNotNull(randomStringWithRange);
 assertEquals(10, randomStringWithRange.length());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCount() {
 RandomStringUtils.random(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyCharacterArray() {
 RandomStringUtils.random(10, new char[0]);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testOutOfBoundsRange() {
 RandomStringUtils.random(1, 'a', 'z'.codePointAt(0) - 1);
 }
}