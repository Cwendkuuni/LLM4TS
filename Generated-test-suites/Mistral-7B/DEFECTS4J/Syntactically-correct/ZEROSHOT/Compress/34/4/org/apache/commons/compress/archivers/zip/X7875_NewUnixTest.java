import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class X7875_NewUnixTest {

 @Test
 public void testNewInstance() {
 X7875_NewUnix xfu = new X7875_NewUnix();
 assertNotNull(xfu);
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix xfu = new X7875_NewUnix();
 ZipShort headerId = xfu.getHeaderId();
 assertEquals(new ZipShort(0x7875), headerId);
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix xfu = new X7875_NewUnix();
 long uid = 12345;
 xfu.setUID(uid);
 long expectedUid = uid;
 long actualUid = xfu.getUID();
 assertEquals(expectedUid, actualUid);
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix xfu = new X7875_NewUnix();
 long uid = 12345;
 xfu.setUID(uid);
 BigInteger expectedUid = new BigInteger(Long.toString(uid));
 BigInteger actualUid = xfu.uid;
 assertEquals(expectedUid, actualUid);
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix xfu = new X7875_NewUnix();
 long gid = 67890;
 xfu.setGID(gid);
 long expectedGid = gid;
 long actualGid = xfu.getGID();
 assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix xfu = new X7875_NewUnix();
 long gid = 67890;
 xfu.setGID(gid);
 BigInteger expectedGid = new BigInteger(Long.toString(gid));
 BigInteger actualGid = xfu.gid;
 assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix xfu = new X7875_NewUnix();
 int uidSize = 2;
 int gidSize = 3;
 xfu.uid = new BigInteger(Integer.toString(uidSize));
 xfu.gid = new BigInteger(Integer.toString(gidSize));
 ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
 ZipShort actualLength = xfu.getLocalFileDataLength();
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix xfu = new X7875_NewUnix();
 int uidSize = 2;
 int gidSize = 3;
 xfu.uid = new BigInteger(Integer.toString(uidSize));
 xfu.gid = new BigInteger(Integer.toString(gidSize));
 ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
 ZipShort actualLength = xfu.getCentralDirectoryLength();
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix xfu = new X7875_NewUnix();
 int uidSize = 2;
 int gidSize = 3;
 xfu.uid = new BigInteger(Integer.toString(uidSize));
 xfu.gid = new BigInteger(Integer.toString(gidSize));
 byte[] expectedData = new byte[3 + uidSize + gidSize];
 int pos = 0;
 expectedData[pos++] = (byte) 0x01;
 expectedData[pos++] = (byte) uidSize;
 System.arraycopy(uidSize > 0 ? new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) (uidSize > 1 ? (byte) 0xFF : 0x00), (byte) (uid & 0xFF), (byte) ((uid >> 8) & 0xFF), (byte) ((uid >> 16) & 0xFF), (byte) ((uid >> 24) & 0xFF)} : new byte[0], pos, expectedData, pos, uidSize);
 pos += uidSize;
 expectedData[pos++] = (byte) gidSize;
 System.arraycopy(gidSize > 0 ? new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) (gidSize > 1 ? (byte) 0xFF : 0x00), (byte) (gid & 0xFF), (byte) ((gid >> 8) & 0xFF), (byte) ((gid >> 16) & 0xFF), (byte) ((gid >> 24) & 0xFF)} : new byte[0], pos, expectedData, pos, gidSize);
 byte[] actualData = xfu.getLocalFileDataData();
 assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix xfu = new X7875_NewUnix();
 byte[] testData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F, (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19, (byte) 0x1A, (byte) 0x1B, (byte) 0x1C, (byte) 0x1D, (byte) 0x1E, (byte) 0x1F};
 int offset = 0;
 int length = testData.length;
 xfu.parseFromLocalFileData(testData, offset, length);
 int expectedVersion = 0x01;
 int uidSize = 3;
 byte[] uidBytes = new byte[]{0x0A, 0x0B, 0x0C};
 int gidSize = 3;
 byte[] gidBytes = new byte[]{0x0D, 0x0E, 0x0F};
 int pos = 0;
 byte versionByte = testData[pos++];
 assertEquals(expectedVersion, unsignedIntToSignedByte(versionByte));
 byte uidSizeByte = testData[pos++];
 assertEquals(uidSize, unsignedIntToSignedByte(uidSizeByte));
 byte[] uidBytes = new byte[uidSize];
 System.arraycopy(testData, pos, uidBytes, 0, uidSize);
 reverse(uidBytes);
 BigInteger expectedUid = new BigInteger(1, uidBytes);
 BigInteger actualUid = xfu.uid;
 assertEquals(expectedUid, actualUid);
 byte gidSizeByte = testData[pos++];
 assertEquals(gidSize, unsignedIntToSignedByte(gidSizeByte));
 byte[] gidBytes = new byte[gidSize];
 System.arraycopy(testData, pos, gidBytes, 0, gidSize);
 reverse(gidBytes);
 BigInteger expectedGid = new BigInteger(1, gidBytes);
 BigInteger actualGid = xfu.gid;
 assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testParseFromCentralDirectoryData() throws ZipException {
 X7875_NewUnix xfu = new X7875_NewUnix();
 byte[] testData = new byte[0];
 int offset = 0;
 int length = testData.length;
 xfu.parseFromCentralDirectoryData(testData, offset, length);
 fail("Expected ZipException to be thrown");
 }

 @Test
 public void testReset() {
 X7875_NewUnix xfu = new X7875_NewUnix();
 xfu.reset();
 assertEquals(1, xfu.version);
 assertEquals(BigInteger.ONE, xfu.uid);
 assertEquals(BigInteger.ONE, xfu.gid);
 }

 @Test
 public void testToString() {
 X7875_NewUnix xfu = new X7875_NewUnix();
 xfu.version = 2;
 xfu.uid = new BigInteger("12345");
 xfu.gid = new BigInteger("67890");
 String expectedString = "0x7875 Zip Extra Field: UID=12345 GID=67890";
 String actualString = xfu.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix xfu = new X7875_NewUnix();
 X7875_NewUnix clonedXfu = (X7875_NewUnix) xfu.clone();
 assertNotNull(clonedXfu);
 }

 @Test
 public void testEquals() {
 X7875_NewUnix xfu1 = new X7875_NewUnix();
 X7875_NewUnix xfu2 = new X7875_NewUnix();
 X7875_NewUnix xfu3 = xfu1;
 assertTrue(xfu1.equals(xfu1));
 assertFalse(xfu1.equals(xfu2));
 assertFalse(xfu1.equals(xfu3));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix xfu1 = new X7875_NewUnix();
 X7875_NewUnix xfu2 = new X7875_NewUnix();
 X7875_NewUnix xfu3 = xfu1;
 int hashCode1 = xfu1.hashCode();
 int hashCode2 = xfu2.hashCode();
 int hashCode3 = xfu3.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 assertEquals(hashCode1, hashCode3);
 }
}