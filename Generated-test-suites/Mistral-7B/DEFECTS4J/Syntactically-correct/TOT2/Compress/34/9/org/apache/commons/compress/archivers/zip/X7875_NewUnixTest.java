import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

 // Test constructor
 @Test
 public void testConstructor() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 Assert.assertEquals(X7875_NewUnix.HEADER_ID, x7875.getHeaderId());
 Assert.assertEquals(BigInteger.ZERO, x7875.getUID());
 Assert.assertEquals(BigInteger.ZERO, x7875.getGID());
 }

 // Test getUID() method
 @Test
 public void testGetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 Assert.assertEquals(12345, x7875.getUID());
 }

 // Test setUID() method
 @Test
 public void testSetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 Assert.assertEquals(12345, x7875.getUID());
 }

 // Test getGID() method
 @Test
 public void testGetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setGID(54321);
 Assert.assertEquals(54321, x7875.getGID());
 }

 // Test setGID() method
 @Test
 public void testSetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setGID(54321);
 Assert.assertEquals(54321, x7875.getGID());
 }

 // Test getLocalFileDataLength() method
 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 x7875.setGID(54321);
 ZipShort expectedLength = new ZipShort((short) (3 + 1 + 1 + 4 + 4));
 Assert.assertEquals(expectedLength, x7875.getLocalFileDataLength());
 }

 // Test getCentralDirectoryLength() method
 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 x7875.setGID(54321);
 ZipShort expectedLength = new ZipShort((short) (3 + 1 + 1 + 4 + 4));
 Assert.assertEquals(expectedLength, x7875.getCentralDirectoryLength());
 }

 // Test getLocalFileDataData() method
 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 x7875.setGID(54321);
 byte[] expectedData = new byte[] {
 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00,
 0x01, 0x00, 0x32, 0x35, 0x34, 0x32, 0x35
 };
 byte[] actualData = x7875.getLocalFileDataData();
 Assert.assertArrayEquals(expectedData, actualData);
 }

 // Test parseFromLocalFileData() method
 @Test
 public void testParseFromLocalFileData() {
 byte[] testData = new byte[] {
 0x01, (byte) 0x78, 0x75, 0x01, (byte) 0x04, 0x00, 0x00, 0x00, 0x00, 0x32, 0x35, 0x34, 0x32, 0x35, 0x01, 0x00,
 0x01, 0x00, 0x32, 0x35, 0x34, 0x32, 0x35
 };
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(testData, 0, testData.length);
 Assert.assertEquals(1, x7875.getVersion());
 Assert.assertEquals(BigInteger.valueOf(12345), x7875.getUID());
 Assert.assertEquals(BigInteger.valueOf(12345), x7875.getGID());
 }

 // Test equals() method
 @Test
 public void testEquals() {
 X7875_NewUnix x7875_1 = new X7875_NewUnix();
 X7875_NewUnix x7875_2 = new X7875_NewUnix();
 X7875_NewUnix x7875_3 = new X7875_NewUnix();
 X7875_NewUnix x7875_4 = x7875_1;

 x7875_1.setUID(12345);
 x7875_1.setGID(54321);
 x7875_2.setUID(12345);
 x7875_2.setGID(54321);

 Assert.assertTrue(x7875_1.equals(x7875_2));
 Assert.assertTrue(x7875_1.equals(x7875_4));

 x7875_3.setUID(12346);
 x7875_3.setGID(54321);
 Assert.assertFalse(x7875_1.equals(x7875_3));
 }

 // Test hashCode() method
 @Test
 public void testHashCode() {
 X7875_NewUnix x7875_1 = new X7875_NewUnix();
 X7875_NewUnix x7875_2 = new X7875_NewUnix();
 X7875_NewUnix x7875_3 = new X7875_NewUnix();
 X7875_NewUnix x7875_4 = x7875_1;

 x7875_1.setUID(12345);
 x7875_1.setGID(54321);
 x7875_2.setUID(12345);
 x7875_2.setGID(54321);

 Assert.assertEquals(x7875_1.hashCode(), x7875_2.hashCode());
 Assert.assertEquals(x7875_1.hashCode(), x7875_4.hashCode());

 x7875_3.setUID(12346);
 x7875_3.setGID(54321);
 Assert.assertNotEquals(x7875_1.hashCode(), x7875_3.hashCode());
 }

 // Test trimLeadingZeroesForceMinLength() method
 @Test
 public void testTrimLeadingZeroesForceMinLength() {
 byte[] zeroUID = new byte[] { (byte) 0x78, (byte) 0x75, 0x01, (byte) 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00 };
 byte[] trimmedUID = X7875_NewUnix.trimLeadingZeroesForceMinLength(zeroUID);
 byte[] expectedTrimmedUID = new byte[] { 0x78, 0x75, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00 };
 Assert.assertArrayEquals(expectedTrimmedUID, trimmedUID);
 }

 // Test parseFromCentralDirectoryData() method (not implemented in this class)
 @Test(expected = UnsupportedOperationException.class)
 public void testParseFromCentralDirectoryData() {
 byte[] testData = new byte[] { /* test data */ };
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromCentralDirectoryData(testData, 0, testData.length);
 }
}