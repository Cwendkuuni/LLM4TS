import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 @Test
 public void testNewInstance() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 assertNull(newUnix.getUID());
 assertNull(newUnix.getGID());
 }

 @Test
 public void testSetGetUID() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 long uid = 12345;
 newUnix.setUID(uid);
 assertEquals(uid, newUnix.getUID());
 }

 @Test
 public void testSetGetGID() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 long gid = 67890;
 newUnix.setGID(gid);
 assertEquals(gid, newUnix.getGID());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeUID() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 long uid = -12345;
 newUnix.setUID(uid);
 fail("Should have thrown IllegalArgumentException for negative UID");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeGID() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 long gid = -67890;
 newUnix.setGID(gid);
 fail("Should have thrown IllegalArgumentException for negative GID");
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 int uidSize = 2;
 int gidSize = 2;
 newUnix.uid = new BigInteger("12345");
 newUnix.gid = new BigInteger("67890");
 ZipShort expectedLength = new ZipShort((short) (uidSize + gidSize + 1));
 assertEquals(expectedLength, newUnix.getLocalFileDataLength());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 ZipShort expectedLength = new ZipShort((short) 0);
 assertEquals(expectedLength, newUnix.getCentralDirectoryLength());
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 long uid = 12345;
 long gid = 67890;
 newUnix.uid = new BigInteger("12345");
 newUnix.gid = new BigInteger("67890");
 byte[] expectedData = new byte[3 + 2 + 2];
 expectedData[0] = (byte) 0x78;
 expectedData[1] = (byte) 0x75;
 expectedData[2] = (byte) 0x01;
 expectedData[3] = (byte) (uidSize >> 8);
 expectedData[4] = (byte) (uidSize & 0xFF);
 System.arraycopy(newUnix.uid.toByteArray(), 0, expectedData, 5, uidSize);
 expectedData[5 + uidSize] = (byte) (gidSize >> 8);
 expectedData[6 + uidSize] = (byte) (gidSize & 0xFF);
 System.arraycopy(newUnix.gid.toByteArray(), 0, expectedData, 7 + uidSize, gidSize);
 byte[] actualData = newUnix.getLocalFileDataData();
 assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testGetCentralDirectoryData() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 byte[] expectedData = new byte[0];
 byte[] actualData = newUnix.getCentralDirectoryData();
 assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testParseFromLocalFileData() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 byte[] data = new byte[8];
 data[0] = (byte) 0x78;
 data[1] = (byte) 0x75;
 data[2] = (byte) 0x01;
 data[3] = (byte) 0x02;
 System.arraycopy(new BigInteger("12345").toByteArray(), 0, data, 4, 4);
 System.arraycopy(new BigInteger("67890").toByteArray(), 0, data, 8, 4);
 newUnix.parseFromLocalFileData(data, 0, data.length);
 assertEquals(12345, newUnix.getUID());
 assertEquals(67890, newUnix.getGID());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidData() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 byte[] data = new byte[7];
 data[0] = (byte) 0x78;
 data[1] = (byte) 0x75;
 data[2] = (byte) 0x01;
 data[3] = (byte) 0x02;
 data[4] = (byte) 0x04;
 data[5] = (byte) 0x01;
 data[6] = (byte) 0x01;
 newUnix.parseFromLocalFileData(data, 0, data.length);
 fail("Should have thrown ZipException for invalid data");
 }
}