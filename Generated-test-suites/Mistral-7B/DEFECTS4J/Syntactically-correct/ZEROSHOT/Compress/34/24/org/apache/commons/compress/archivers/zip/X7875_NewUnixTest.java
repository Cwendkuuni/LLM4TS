import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 @Test
 public void testNewInstance() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 assertNull(x7875NewUnix.getUID());
 assertNull(x7875NewUnix.getGID());
 assertEquals(1, x7875NewUnix.getVersion());
 }

 @Test
 public void testSetGetUid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345L);
 assertEquals(12345L, x7875NewUnix.getUID());
 }

 @Test
 public void testSetGetGid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setGID(23456L);
 assertEquals(23456L, x7875NewUnix.getGID());
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345L);
 x7875NewUnix.setGID(23456L);

 byte[] uidBytes = x7875NewUnix.uid.toByteArray();
 byte[] gidBytes = x7875NewUnix.gid.toByteArray();

 int uidSize = trimLeadingZeroesForceMinLength(uidBytes).length;
 int gidSize = trimLeadingZeroesForceMinLength(gidBytes).length;

 ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
 ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
 assertEquals(expectedLength.getValue(), actualLength.getValue());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345L);
 x7875NewUnix.setGID(23456L);

 ZipShort expectedLength = x7875NewUnix.getLocalFileDataLength();
 ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();
 assertEquals(expectedLength.getValue(), actualLength.getValue());
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345L);
 x7875NewUnix.setGID(23456L);

 byte[] expectedData = x7875NewUnix.getLocalFileDataData();
 byte[] uidBytes = x7875NewUnix.uid.toByteArray();
 byte[] gidBytes = x7875NewUnix.gid.toByteArray();

 // reverse() switches byte array from big-endian to little-endian.
 byte[] reversedUidBytes = new byte[uidBytes.length];
 byte[] reversedGidBytes = new byte[gidBytes.length];
 ZipUtil.reverse(uidBytes, reversedUidBytes);
 ZipUtil.reverse(gidBytes, reversedGidBytes);

 byte[] mergedData = new byte[3 + uidBytes.length + gidBytes.length];
 mergedData[0] = (byte) unsignedIntToSignedByte(x7875NewUnix.getVersion());
 mergedData[1] = (byte) unsignedIntToSignedByte(uidBytes.length);
 System.arraycopy(reversedUidBytes, 0, mergedData, 2, uidBytes.length);
 mergedData[2 + uidBytes.length] = (byte) unsignedIntToSignedByte(gidBytes.length);
 System.arraycopy(reversedGidBytes, 0, mergedData, 2 + uidBytes.length, gidBytes.length);

 assertArrayEquals(mergedData, expectedData);
 }

 @Test(expected = ZipException.class)
 public void testParseFromCentralDirectoryData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.parseFromCentralDirectoryData(new byte[0], 0, 0);
 }

 @Test
 public void testToString() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345L);
 x7875NewUnix.setGID(23456L);
 String expectedString = "0x7875 Zip Extra Field: UID=12345 GID=23456";
 String actualString = x7875NewUnix.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix original = new X7875_NewUnix();
 original.setUID(12345L);
 original.setGID(23456L);
 X7875_NewUnix clone = (X7875_NewUnix) original.clone();
 assertEquals(original.getUID(), clone.getUID());
 assertEquals(original.getGID(), clone.getGID());
 }

 @Test
 public void testEquals() {
 X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();

 x7875NewUnix1.setUID(12345L);
 x7875NewUnix1.setGID(23456L);
 x7875NewUnix2.setUID(12345L);
 x7875NewUnix2.setGID(23456L);
 x7875NewUnix3.setUID(54321L);
 x7875NewUnix3.setGID(65432L);

 assertTrue(x7875NewUnix1.equals(x7875NewUnix2));
 assertFalse(x7875NewUnix1.equals(x7875NewUnix3));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();

 x7875NewUnix1.setUID(12345L);
 x7875NewUnix1.setGID(23456L);
 x7875NewUnix2.setUID(12345L);
 x7875NewUnix2.setGID(23456L);
 x7875NewUnix3.setUID(54321L);
 x7875NewUnix3.setGID(65432L);

 int hashCode1 = x7875NewUnix1.hashCode();
 int hashCode2 = x7875NewUnix2.hashCode();
 int hashCode3 = x7875NewUnix3.hashCode();

 // We assume uid and gid can never be null.
 assertNotEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}