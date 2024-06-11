import org.apache.commons.compress.archivers.zip.*;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 private X7875_NewUnix newUnix;
 private final static byte[] uidBytes = new byte[]{0x00, 0x7f, 0x00, 0x7f, 0x00, 0x01};
 private final static byte[] gidBytes = new byte[]{0x00, 0x7f, 0x00, 0x7f, 0x01, 0x00};

 @Before
 public void setUp() throws Exception {
 newUnix = new X7875_NewUnix();
 }

 @Test
 public void testHeaderId() {
 ZipShort expectedHeaderId = new ZipShort(0x7875);
 ZipShort actualHeaderId = newUnix.getHeaderId();
 assertEquals(expectedHeaderId.getValue(), actualHeaderId.getValue());
 }

 @Test
 public void testGetUID() {
 long expectedUID = 12345;
 newUnix.setUID(expectedUID);
 long actualUID = newUnix.getUID();
 assertEquals(expectedUID, actualUID);
 }

 @Test
 public void testGetGID() {
 long expectedGID = 67890;
 newUnix.setGID(expectedGID);
 long actualGID = newUnix.getGID();
 assertEquals(expectedGID, actualGID);
 }

 @Test
 public void testGetLocalFileDataLength() {
 int expectedUidSize = uidBytes.length;
 int expectedGidSize = gidBytes.length;
 int expectedLength = 3 + expectedUidSize + expectedGidSize;
 ZipShort actualLength = newUnix.getLocalFileDataLength();
 assertEquals(expectedLength, actualLength.getValue());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 int expectedLength = getLocalFileDataLength().getValue();
 ZipShort actualLength = newUnix.getCentralDirectoryLength();
 assertEquals(expectedLength, actualLength.getValue());
 }

 @Test
 public void testGetLocalFileDataData() {
 byte[] expectedData = new byte[getLocalFileDataLength().getValue()];
 System.arraycopy(uidBytes, 0, expectedData, 0, uidBytes.length);
 System.arraycopy(gidBytes, 0, expectedData, uidBytes.length, gidBytes.length);
 byte[] actualData = newUnix.getLocalFileDataData();
 assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] data = newUnix.getLocalFileDataData();
 newUnix.parseFromLocalFileData(data, 0, data.length);
 assertEquals(newUnix.getUID(), 12345);
 assertEquals(newUnix.getGID(), 67890);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
 byte[] data = new byte[]{0x78, 0x75, 0x01, 0x00, 0x7f, 0x00};
 newUnix.parseFromLocalFileData(data, 0, data.length);
 fail("ZipException should be thrown, but it wasn't.");
 }

 @Test
 public void testEquals() {
 X7875_NewUnix anotherNewUnix = new X7875_NewUnix();
 anotherNewUnix.setUID(12345);
 anotherNewUnix.setGID(67890);
 assertTrue(newUnix.equals(anotherNewUnix));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix anotherNewUnix = new X7875_NewUnix();
 anotherNewUnix.setUID(12345);
 anotherNewUnix.setGID(67890);
 int expectedHashCode = 13221213; // Computed value based on class variables
 int actualHashCode = newUnix.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

}