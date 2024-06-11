import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

public class RandomStringUtilsTest {

 @Test
 public void testRandomString() {
 String randomString = RandomStringUtils.random(10);
 assertEquals(10, randomString.length());
 }

 @Test
 public void testRandomAsciiString() {
 String randomAsciiString = RandomStringUtils.randomAscii(10);
 assertEquals(10, randomAsciiString.length());
 }

 @Test
 public void testRandomAlphabeticString() {
 String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
 assertEquals(10, randomAlphabeticString.length());
 }

 @Test
 public void testRandomAlphanumericString() {
 String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
 assertEquals(10, randomAlphanumericString.length());
 }

 @Test
 public void testRandomNumericString() {
 String randomNumericString = RandomStringUtils.randomNumeric(10);
 assertEquals(10, randomNumericString.length());
 }

 @Test
 public void testRandomStringWithRange() {
 String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 10);
 assertEquals(10, randomString.length());
 }

 @Test
 public void testRandomStringWithEmptyRange() {
 String randomString = RandomStringUtils.random("", 10);
 assertEquals("", randomString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRandomStringWithNegativeLength() {
 RandomStringUtils.random(10); // should throw IllegalArgumentException
 }

 @Test
 public void testRandomStringWithNullChars() {
 String randomString = RandomStringUtils.random(10, (char[]) null);
 assertEquals("", randomString); // null array returns empty string
 }

 @Test
 public void testRandomStringWithEmptyChars() {
 String randomString = RandomStringUtils.random(10, new char[0]);
 assertEquals("", randomString); // empty array returns empty string
 }

 @Test
 public void testRandomStringWithCustomRandom() {
 Random random = new Random(1);
 String randomString = RandomStringUtils.random(10, 'A', 'Z', random);
 assertEquals(10, randomString.length());
 assertNotSame("Random strings should not be the same.", RandomStringUtils.random(10, 'A', 'Z', random), randomString);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testRandomStringWithInvalidRange() {
 String randomString = RandomStringUtils.random(10, 'A', Integer.MIN_VALUE, false, false, null, RANDOM);
 }
}