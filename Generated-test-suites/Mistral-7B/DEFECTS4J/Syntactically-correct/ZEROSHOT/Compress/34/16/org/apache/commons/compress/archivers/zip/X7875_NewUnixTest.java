import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 private X7875_NewUnix createNewX7875_NewUnix() {
 return new X7875_NewUnix();
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 ZipShort actualHeaderId = x7875NewUnix.getHeaderId();
 ZipShort expectedHeaderId = new ZipShort(0x7875);
 assertEquals("Unexpected headerId", expectedHeaderId, actualHeaderId);
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 x7875NewUnix.setUID(1234567890L);
 long actualUID = x7875NewUnix.getUID();
 long expectedUID = 1234567890L;
 assertEquals("Unexpected UID", expectedUID, actualUID);
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 long testUID = 1234567890L;
 x7875NewUnix.setUID(testUID);
 BigInteger actualUid = x7875NewUnix.uid;
 BigInteger expectedUid = new BigInteger(testUID);
 assertEquals("Unexpected UID", expectedUid, actualUid);
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 x7875NewUnix.setGID(1234567890L);
 long actualGID = x7875NewUnix.getGID();
 long expectedGID = 1234567890L;
 assertEquals("Unexpected GID", expectedGID, actualGID);
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 long testGID = 1234567890L;
 x7875NewUnix.setGID(testGID);
 BigInteger actualGid = x7875NewUnix.gid;
 BigInteger expectedGid = new BigInteger(testGID);
 assertEquals("Unexpected GID", expectedGid, actualGid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 x7875NewUnix.setUID(123L);
 x7875NewUnix.setGID(456L);
 ZipShort actualLocalFileDataLength = x7875NewUnix.getLocalFileDataLength();
 ZipShort expectedLocalFileDataLength = new ZipShort((short) 5);
 assertEquals("Unexpected localFileDataLength", expectedLocalFileDataLength, actualLocalFileDataLength);
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 x7875NewUnix.setUID(123L);
 x7875NewUnix.setGID(456L);
 ZipShort actualCentralDirectoryLength = x7875NewUnix.getCentralDirectoryLength();
 ZipShort expectedCentralDirectoryLength = new ZipShort((short) 5);
 assertEquals("Unexpected centralDirectoryLength", expectedCentralDirectoryLength, actualCentralDirectoryLength);
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 x7875NewUnix.setUID(123L);
 x7875NewUnix.setGID(456L);
 byte[] actualLocalFileDataData = x7875NewUnix.getLocalFileDataData();
 byte[] expectedLocalFileDataData = new byte[]{(byte) 0x55, (byte) 0x78, (byte) 0x31, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x45, (byte) 0x78, (byte) 0x31, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x31, (byte) 0x23};
 assertArrayEquals("Unexpected localFileDataData", expectedLocalFileDataData, actualLocalFileDataData);
 }

 @Test(expected = ZipException.class)
 public void testParseFromCentralDirectoryDataThrowsException() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 byte[] emptyData = new byte[0];
 x7875NewUnix.parseFromCentralDirectoryData(emptyData, 0, emptyData.length);
 }

 @Test
 public void testParseFromLocalFileData() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 byte[] testData = new byte[]{(byte) 0x31, (byte) 0x23, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x45, (byte) 0x78, (byte) 0x31, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x31, (byte) 0x23, (byte) 0x01, (byte) 0x00, (byte) 0x55, (byte) 0x78, (byte) 0x31, (byte) 0x01, (byte) 0x00, (byte) 0x45, (byte) 0x78, (byte) 0x31, (byte) 0x01, (byte) 0x00};
 x7875NewUnix.parseFromLocalFileData(testData, 0, testData.length);
 assertEquals("Unexpected version", 1, x7875NewUnix.version);
 BigInteger actualUid = x7875NewUnix.uid;
 BigInteger expectedUid = new BigInteger("123");
 assertEquals("Unexpected UID", expectedUid, actualUid);
 BigInteger actualGid = x7875NewUnix.gid;
 BigInteger expectedGid = new BigInteger("456");
 assertEquals("Unexpected GID", expectedGid, actualGid);
 }

 @Test
 public void testReset() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 x7875NewUnix.setUID(123L);
 x7875NewUnix.setGID(456L);
 x7875NewUnix.reset();
 assertFalse("UID should be reset to default", x7875NewUnix.uid.equals(BigInteger.ONE_THOUSAND));
 assertFalse("GID should be reset to default", x7875NewUnix.gid.equals(BigInteger.ONE_THOUSAND));
 }

 @Test
 public void testToString() {
 X7875_NewUnix x7875NewUnix = createNewX7875_NewUnix();
 x7875NewUnix.setUID(123L);
 x7875NewUnix.setGID(456L);
 String actualToString = x7875NewUnix.toString();
 String expectedToString = "0x7875 Zip Extra Field: UID=123 GID=456";
 assertEquals("Unexpected toString", expectedToString, actualToString);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix originalX7875NewUnix = createNewX7875_NewUnix();
 originalX7875NewUnix.setUID(123L);
 originalX7875NewUnix.setGID(456L);
 X7875_NewUnix clonedX7875NewUnix = (X7875_NewUnix) originalX7875NewUnix.clone();
 assertEquals("Unexpected version", 1, clonedX7875NewUnix.version);
 BigInteger originalUid = originalX7875NewUnix.uid;
 BigInteger clonedUid = clonedX7875NewUnix.uid;
 assertEquals("Unexpected UID", originalUid, clonedUid);
 BigInteger originalGid = originalX7875NewUnix.gid;
 BigInteger clonedGid = clonedX7875NewUnix.gid;
 assertEquals("Unexpected GID", originalGid, clonedGid);
 }

 @Test
 public void testEquals() {
 X7875_NewUnix x7875NewUnix1 = createNewX7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = createNewX7875_NewUnix();
 X7875_NewUnix x7875NewUnix3 = createNewX7875_NewUnix();
 x7875NewUnix1.setUID(123L);
 x7875NewUnix1.setGID(456L);
 x7875NewUnix2.setUID(123L);
 x7875NewUnix2.setGID(456L);
 x7875NewUnix3.setUID(456L);
 x7875NewUnix3.setGID(123L);
 assertTrue("x7875NewUnix1 and x7875NewUnix2 should be equal", x7875NewUnix1.equals(x7875NewUnix2));
 assertFalse("x7875NewUnix1 and x7875NewUnix3 should not be equal", x7875NewUnix1.equals(x7875NewUnix3));
 assertFalse("x7875NewUnix2 and x7875NewUnix3 should not be equal", x7875NewUnix2.equals(x7875NewUnix3));
 assertFalse("x7875NewUnix1 and null should not be equal", x7875NewUnix1.equals(null));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix x7875NewUnix1 = createNewX7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = createNewX7875_NewUnix();
 X7875_NewUnix x7875NewUnix3 = createNewX7875_NewUnix();
 x7875NewUnix1.setUID(123L);
 x7875NewUnix1.setGID(456L);
 x7875NewUnix2.setUID(123L);
 x7875NewUnix2.setGID(456L);
 x7875NewUnix3.setUID(456L);
 x7875NewUnix3.setGID(123L);
 int actualHashCode1 = x78785NewUnix1.hashCode();
 int actualHashCode2 = x78785NewUnix2.hashCode();
 int actualHashCode3 = x78785NewUnix3.hashCode();
 int expectedHashCode1 = 1472563395;
 int expectedHashCode2 = 1472563395;
 int expectedHashCode3 = 1068178288;
 assertEquals("Unexpected hashCode1", expectedHashCode1, actualHashCode1);
 assertEquals("Unexpected hashCode2", expectedHashCode2, actualHashCode2);
 assertEquals("Unexpected hashCode3", expectedHashCode3, actualHashCode3);
 }
}