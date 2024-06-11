import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class X7875_NewUnixTest {

 @Test
 public void testNewInstance() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 Assertions.assertNotNull(x7875NewUnix);
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 ZipShort expectedHeaderId = x7875NewUnix.getHeaderId();
 ZipShort actualHeaderId = new ZipShort((short) 0x7875);
 Assertions.assertEquals(expectedHeaderId, actualHeaderId);
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345);
 long expectedUID = 12345;
 long actualUID = x7875NewUnix.getUID();
 Assertions.assertEquals(expectedUID, actualUID);
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setGID(54321);
 long expectedGID = 54321;
 long actualGID = x7875NewUnix.getGID();
 Assertions.assertEquals(expectedGID, actualGID);
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345);
 BigInteger expectedUid = new BigInteger("12345");
 BigInteger actualUid = x7875NewUnix.uid;
 Assertions.assertEquals(expectedUid, actualUid);
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setGID(54321);
 BigInteger expectedGid = new BigInteger("54321");
 BigInteger actualGid = x7875NewUnix.gid;
 Assertions.assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345);
 x7875NewUnix.setGID(54321);
 int expectedLength = 1 + 1 + 4 + 4;
 int actualLength = x7875NewUnix.getLocalFileDataLength().getValue();
 Assertions.assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345);
 x7875NewUnix.setGID(54321);
 int expectedLength = 1 + 1 + 4 + 4;
 int actualLength = x7875NewUnix.getCentralDirectoryLength().getValue();
 Assertions.assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345);
 x7875NewUnix.setGID(54321);
 byte[] expectedData = new byte[]{(byte) 0x01, 0x01, 0x00, 0x04, 0x00, 0x04,
 (byte) unsignedIntToSignedByte(1), (byte) unsignedIntToSignedByte(1),
 (byte) 0x34, (byte) 0x35, 0x00, 0x32, 0x31, 0x00};
 byte[] actualData = x7875NewUnix.getLocalFileDataData();
 Assertions.assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[]{(byte) 0x01, 0x01, 0x00, 0x04, 0x00, 0x04,
 (byte) unsignedIntToSignedByte(1), (byte) unsignedIntToSignedByte(1),
 (byte) 0x34, (byte) 0x35, 0x00, 0x32, 0x31, 0x00};
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 long expectedUID = 12345;
 long expectedGID = 54321;
 long actualUID = x7875NewUnix.getUID();
 long actualGID = x7875NewUnix.getGID();
 Assertions.assertEquals(expectedUID, actualUID);
 Assertions.assertEquals(expectedGID, actualGID);
 }

 @Test
 public void testParseFromCentralDirectoryData() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[0];
 x7875NewUnix.parseFromCentralDirectoryData(data, 0, data.length);
 }

}