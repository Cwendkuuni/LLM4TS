import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class X7875_NewUnixTest {

 @Test
 public void testNewInstance() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 Assertions.assertEquals(1, x7875.getVersion());
 Assertions.assertNull(x7875.getUID());
 Assertions.assertNull(x7875.getGID());
 }

 @Test
 public void testSetAndGetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 Assertions.assertEquals(12345, x7875.getUID());
 }

 @Test
 public void testSetAndGetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setGID(67890);
 Assertions.assertEquals(67890, x7875.getGID());
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 x7875.setGID(67890);
 ZipShort expectedLength = new ZipShort((short) (3 + 1 + 1 + 1 + 5 + 1 + 1 + 5));
 Assertions.assertEquals(expectedLength, x7875.getLocalFileDataLength());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 x7875.setGID(67890);
 ZipShort expectedLength = new ZipShort((short) 3); // Central directory length is 0 in this case
 Assertions.assertEquals(expectedLength, x7875.getCentralDirectoryLength());
 }

 @Test
 public void testGetLocalFileDataData() throws Exception {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 x7875.setGID(67890);
 byte[] expectedData = new byte[] {
 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05, 0x00, 0x01,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05, 0x00, 0x02, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05
 };
 byte[] actualData = x7875.getLocalFileDataData();
 Assertions.assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testParseFromLocalFileData() throws Exception {
 X7875_NewUnix x7875 = new byte[15][];
 x7875[0] = new byte[] { 0x78, 0x75, 0x01, 0x01, 0x03, 0x00, 0x00, 0x00, 0x05, 0x00, 0x01, 0x00, 0x00, 0x00, 0x05, 0x00, 0x02, 0x00, 0x00, 0x00, 0x05 };
 X7875_NewUnix parsedX7875 = new X7875_NewUnix();
 parsedX7875.parseFromLocalFileData(x7875[0], 0, x7875[0].length);
 Assertions.assertEquals(1, parsedX7875.getVersion());
 Assertions.assertEquals(1, parsedX7875.getUID().intValue());
 Assertions.assertEquals(2, parsedX7875.getGID().intValue());
 }

 @Test
 public void testParseFromCentralDirectoryData() {
 // Not applicable since this class does not store data in central directory.
 }

 @Test
 public void testReset() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 x7875.setGID(67890);
 x7875.reset();
 Assertions.assertEquals(1, x7875.getVersion());
 Assertions.assertNull(x7875.getUID());
 Assertions.assertNull(x7875.getGID());
 }

 @Test
 public void testToString() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 x7875.setGID(67890);
 String expectedString = "0x7875 Zip Extra Field: UID=12345 GID=67890";
 Assertions.assertEquals(expectedString, x7875.toString());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix original = new X7875_NewUnix();
 original.setUID(12345);
 original.setGID(67890);
 X7875_NewUnix clone = (X7875_NewUnix) original.clone();
 Assertions.assertEquals(original.getUID(), clone.getUID());
 Assertions.assertEquals(original.getGID(), clone.getGID());
 }

 @Test
 public void testEquals() {
 X7875_NewUnix x7875_1 = new X7875_NewUnix();
 X7875_NewUnix x7875_2 = new X7875_NewUnix();
 X7875_NewUnix x7875_3 = new X7875_NewUnix();
 x7875_1.setUID(12345);
 x7875_1.setGID(67890);
 x7875_2.setUID(12345);
 x7875_2.setGID(67890);
 x7875_3.setUID(12346);
 x7875_3.setGID(67890);
 Assertions.assertTrue(x7875_1.equals(x7875_2));
 Assertions.assertFalse(x7875_1.equals(x7875_3));
 Assertions.assertFalse(x7875_1.equals(null));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix x7875_1 = new X7875_NewUnix();
 X7875_NewUnix x7875_2 = new X7875_NewUnix();
 X7875_NewUnix x7875_3 = new X7875_NewUnix();
 x7875_1.setUID(12345);
 x7875_1.setGID(67890);
 x7875_2.setUID(12345);
 x7875_2.setGID(67890);
 x7875_3.setUID(12346);
 x7875_3.setGID(67890);
 int expectedHashCode_1 = 1124329977; // This is the result of running the code in the hashCode() method.
 int expectedHashCode_2 = 885882037; // Same here, just a different instance of X7875_NewUnix.
 Assertions.assertEquals(expectedHashCode_1, x7875_1.hashCode());
 Assertions.assertEquals(expectedHashCode_2, x7875_2.hashCode());
 Assertions.assertNotEquals(expectedHashCode_1, x7875_3.hashCode());
 }

}