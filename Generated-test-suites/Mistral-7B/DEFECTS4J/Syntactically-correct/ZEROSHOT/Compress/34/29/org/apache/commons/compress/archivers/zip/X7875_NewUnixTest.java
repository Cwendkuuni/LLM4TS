import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;

public class X7875_NewUnixTest {

 private X7875_NewUnix newUnix = new X7875_NewUnix();

 @Test
 public void testGetHeaderId() {
 Assert.assertEquals(new ZipShort(0x7875), newUnix.getHeaderId());
 }

 @Test
 public void testGetUID() {
 newUnix.setUID(1234567890L);
 Assert.assertEquals(1234567890L, newUnix.getUID());
 }

 @Test
 public void testSetUID() {
 newUnix.setUID(1234567890L);
 Assert.assertEquals(new BigInteger("1234567890"), newUnix.uid);
 }

 @Test
 public void testGetGID() {
 newUnix.setGID(2345678901L);
 Assert.assertEquals(2345678901L, newUnix.getGID());
 }

 @Test
 public void testSetGID() {
 newUnix.setGID(2345678901L);
 Assert.assertEquals(new BigInteger("2345678901"), newUnix.gid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 newUnix.setUID(123L);
 newUnix.setGID(456L);

 int uidSize = newUnix.uid.toByteArray().length;
 int gidSize = newUnix.gid.toByteArray().length;

 int expectedLength = 3 + uidSize + gidSize;
 int actualLength = newUnix.getLocalFileDataLength().getValue();
 Assert.assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 newUnix.setUID(123L);
 newUnix.setGID(456L);

 int uidSize = newUnix.uid.toByteArray().length;
 int gidSize = newUnix.gid.toByteArray().length;

 int expectedLength = 3 + uidSize + gidSize;
 int actualLength = newUnix.getCentralDirectoryLength().getValue();
 Assert.assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 newUnix.setUID(123L);
 newUnix.setGID(456L);

 byte[] expectedData = new byte[6]; // 3 + uidSize + gidSize
 int uidSize = newUnix.uid.toByteArray().length;
 int gidSize = newUnix.gid.toByteArray().length;

 System.arraycopy(newUnix.uid.toByteArray(), 0, expectedData, 3, uidSize);
 System.arraycopy(newUnix.gid.toByteArray(), 0, expectedData, 3 + uidSize, gidSize);

 byte[] actualData = newUnix.getLocalFileDataData();
 Assert.assertArrayEquals(expectedData, actualData);
 }

 @Test (expected = ZipException.class)
 public void testParseFromCentralDirectoryData() {
 byte[] data = new byte[0];
 newUnix.parseFromCentralDirectoryData(data, 0, data.length);
 fail("Expected ZipException");
 }

 @Test
 public void testParseFromLocalFileData() {
 byte[] data = {0x01, (byte) 0x03, 0x75, 0x78, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04};
 newUnix.parseFromLocalFileData(data, 0, data.length);

 assertEquals(1, newUnix.version);
 assertEquals(new BigInteger("1"), newUnix.uid);
 assertEquals(new BigInteger("2"), newUnix.gid);
 }

 @Test
 public void testReset() {
 newUnix.setUID(123L);
 newUnix.setGID(456L);
 newUnix.reset();

 assertEquals(1, newUnix.version.getValue());
 assertEquals(new BigInteger("1000"), newUnix.uid);
 assertEquals(new BigInteger("1000"), newUnix.gid);
 }
}