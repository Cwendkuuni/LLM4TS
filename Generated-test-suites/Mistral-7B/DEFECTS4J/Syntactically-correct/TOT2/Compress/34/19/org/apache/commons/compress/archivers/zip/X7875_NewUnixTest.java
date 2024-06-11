import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

 // Test constructor
 @Test
 public void testConstructor() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 Assert.assertEquals(1, x7875NewUnix.getVersion());
 Assert.assertEquals(BigInteger.ZERO, x7875NewUnix.getUID());
 Assert.assertEquals(BigInteger.ZERO, x7875NewUnix.getGID());
 }

 // Test getHeaderId method
 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 ZipShort expectedHeaderId = new ZipShort(0x7875);
 ZipShort actualHeaderId = x7875NewUnix.getHeaderId();
 Assert.assertEquals(expectedHeaderId, actualHeaderId);
 }

 // Test getUID method
 @Test
 public void testGetUID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(123456789);
 long expectedUID = 123456789;
 long actualUID = x7875NewUnix.getUID();
 Assert.assertEquals(expectedUID, actualUID);
 }

 // Test setUID method
 @Test
 public void testSetUID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(123456789);
 BigInteger expectedUid = BigInteger.valueOf(123456789);
 BigInteger actualUid = x7875NewUnix.getUID();
 Assert.assertEquals(expectedUid, actualUid);
 }

 // Test getGID method
 @Test
 public void testGetGID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setGID(234567890);
 long expectedGID = 234567890;
 long actualGID = x7875NewUnix.getGID();
 Assert.assertEquals(expectedGID, actualGID);
 }

 // Test setGID method
 @Test
 public void testSetGID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setGID(234567890);
 BigInteger expectedGid = BigInteger.valueOf(234567890);
 BigInteger actualGid = x7875NewUnix.getGID();
 Assert.assertEquals(expectedGid, actualGid);
 }

 // Test getLocalFileDataLength method
 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(123456789);
 x7875NewUnix.setGID(234567890);
 int expectedLength = 10; // UID size = 3, GID size = 3, version = 1
 ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
 Assert.assertEquals(expectedLength, actualLength.getValue());
 }

 // Test parseFromLocalFileData method
 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] data = new byte[] {
 0, 0, 0, // version
 (byte) 3, (byte) 1, // uid size = 3
 (byte) 0, (byte) 0, (byte) 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3 // uid = 123456789
,(byte) 3, (byte) 1, // gid size = 3
 (byte) 0, (byte) 0, (byte) 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3 // gid = 234567890
 };
 X7875_NewUnix expectedX7875NewUnix = new X7875_NewUnix();
 expectedX7875NewUnix.setUID(123456789);
 expectedX7875NewUnix.setGID(234567890);
 X7875_NewUnix actualX7875NewUnix = new X7875_NewUnix();
 actualX7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 Assert.assertEquals(expectedX7875NewUnix.getVersion(), actualX7875NewUnix.getVersion());
 Assert.assertEquals(expectedX7875NewUnix.getUID(), actualX7875NewUnix.getUID());
 Assert.assertEquals(expectedX7875NewUnix.getGID(), actualX7875NewUnix.getGID());
 }

 // Test edge cases (minimum length)
 @Test
 public void testMinLengthEdgeCases() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 // set minimum UID and GID values
 x7875NewUnix.setUID(1);
 x7875NewUnix.setGID(1);
 // Test getLocalFileDataLength method
 int expectedLength = 1;
 ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
 Assert.assertEquals(expectedLength, actualLength.getValue());
 byte[] data = x7875NewUnix.getLocalFileDataData();
 // Test parseFromLocalFileData method
 X7875_NewUnix expectedX7875NewUnix = new X7875_NewUnix();
 expectedX7875NewUnix.setUID(1);
 expectedX7875NewUnix.setGID(1);
 actualX7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 Assert.assertEquals(expectedX7875NewUnix.getVersion(), actualX7875NewUnix.getVersion());
 Assert.assertEquals(expectedX7875NewUnix.getUID(), actualX7875NewUnix.getUID());
 Assert.assertEquals(expectedX7875NewUnix.getGID(), actualX7875NewUnix.getGID());
 }

 // Test exception handling (insufficient data)
 @Test(expected = ZipException.class)
 public void testInsufficientDataException() throws ZipException {
 byte[] data = new byte[] {
 0, 0, 0 // version, uid size = 3
 };
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 }

 // Test equals and hashCode methods
 @Test
 public void testEqualsAndHashCode() {
 X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix3 = x7875NewUnix1;
 x7875NewUnix1.setUID(123456789);
 x7875NewUnix1.setGID(234567890);
 x7875NewUnix2.setUID(123456789);
 x7875NewUnix2.setGID(234567890);
 x7875NewUnix3.setUID(123456789);
 x7875NewUnix3.setGID(234567890);
 Assert.assertTrue(x7875NewUnix1.equals(x7875NewUnix2));
 Assert.assertTrue(x7875NewUnix1.equals(x7875NewUnix3));
 Assert.assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix2.hashCode());
 Assert.assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix3.hashCode());
 }

 // Test not equals method
 @Test
 public void testNotEquals() {
 X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
 x7875NewUnix1.setUID(123456789);
 x7875NewUnix1.setGID(234567890);
 x7875NewUnix2.setUID(123456788);
 x7875NewUnix2.setGID(234567890);
 Assert.assertFalse(x7875NewUnix1.equals(x7875NewUnix2));
 Assert.assertFalse(x7875NewUnix1.equals(x7875NewUnix3));
 Assert.assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix2.hashCode());
 Assert.assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix3.hashCode());
 }
}