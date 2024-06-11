import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.data.ZipExtraField;

public class X7875_NewUnixTest {

 private static final String TEST_FILE = "testfile.zip";

 @BeforeClass
 public static void setup() throws Exception {
 ZipFile zipFile = new ZipFile(TEST_FILE);
 ZipEntry entry = zipFile.getEntry("testentry");
 X7875_NewUnix extraField = (X7875_NewUnix) entry.getExtraField();
 extraField.setUID(12345678L);
 extraField.setGID(23456789L);
 zipFile.close();
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix extraField = new X7875_NewUnix();
 assertEquals(new ZipShort(0x7875), extraField.getHeaderId());
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix extraField = new X7875_NewUnix();
 assertEquals(0L, extraField.getUID());

 X7875_NewUnix extraFieldFromFile = new X7875_NewUnix();
 assertEquals(12345678L, extraFieldFromFile.getUID());
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix extraField = new X7875_NewUnix();
 extraField.setUID(123456789L);
 assertEquals(123456789L, extraField.getUID());
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix extraField = new X7875_NewUnix();
 assertEquals(0L, extraField.getGID());

 X7875_NewUnix extraFieldFromFile = new X7875_NewUnix();
 assertEquals(23456789L, extraFieldFromFile.getGID());
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix extraField = new X7875_NewUnix();
 extraField.setGID(123456789L);
 assertEquals(123456789L, extraField.getGID());
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix extraField = new X7875_NewUnix();
 assertEquals(new ZipShort((short) 3), extraField.getLocalFileDataLength());

 X7875_NewUnix extraFieldFromFile = new X7875_NewUnix();
 byte[] extraData = extraFieldFromFile.getLocalFileDataData();
 assertEquals(new ZipShort((short) (3 + extraData.length)), extraFieldFromFile.getLocalFileDataLength());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix extraField = new X7875_NewUnix();
 assertEquals(new ZipShort((short) 3), extraField.getCentralDirectoryLength());
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix extraField = new X7875_NewUnix();
 byte[] expectedData = new byte[3 + Long.BYTES * 2];
 extraField.parseFromLocalFileData(expectedData, 0, expectedData.length);
 reverse(expectedData);
 assertEquals(expectedData, extraField.getLocalFileDataData());
 }

 @Test
 public void testGetCentralDirectoryData() {
 X7875_NewUnix extraField = new X7875_NewUnix();
 assertEquals(new byte[0], extraField.getCentralDirectoryData());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidData() throws Exception {
 X7875_NewUnix extraField = new X7875_NewUnix();
 byte[] data = new byte[] { 0x78, 0x75, 0x01, 0x01, 0x01, 0x01 };
 extraField.parseFromLocalFileData(data, 0, data.length);
 fail("Should have thrown ZipException");
 }

 @AfterClass
 public static void teardown() throws Exception {
 // Delete the testfile.zip after testing
 java.io.File file = new java.io.File(TEST_FILE);
 file.delete();
 }
}