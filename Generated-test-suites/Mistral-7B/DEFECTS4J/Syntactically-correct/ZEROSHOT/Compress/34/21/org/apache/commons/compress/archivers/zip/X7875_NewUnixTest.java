import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 private X7875_NewUnix newUnix;
 private byte[] data;

 @Before
 public void setUp() {
 newUnix = new X7875_NewUnix();
 data = newUnix.getLocalFileDataData();
 }

 @Test
 public void testGetHeaderId() {
 ZipShort headerId = newUnix.getHeaderId();
 assertEquals(0x7875, headerId.getValue().intValue());
 }

 @Test
 public void testGetUID() {
 long uid = newUnix.getUID();
 assertEquals(1000, uid);
 }

 @Test
 public void testSetUID() {
 newUnix.setUID(2000);
 long newUid = newUnix.getUID();
 assertEquals(2000, newUid);
 }

 @Test
 public void testGetGID() {
 long gid = newUnix.getGID();
 assertEquals(1000, gid);
 }

 @Test
 public void testSetGID() {
 newUnix.setGID(3000);
 long newGid = newUnix.getGID();
 assertEquals(3000, newGid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 ZipShort length = newUnix.getLocalFileDataLength();
 assertEquals(7, length.getValue().intValue());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 ZipShort length = newUnix.getCentralDirectoryLength();
 assertEquals(7, length.getValue().intValue());
 }

 @Test
 public void testGetLocalFileDataData() {
 byte[] data = newUnix.getLocalFileDataData();
 assertEquals(7, data.length);
 }

 @Test(expected = ZipException.class)
 public void testParseFromCentralDirectoryData() {
 newUnix.parseFromCentralDirectoryData(new byte[0], 0, 0);
 }

 @Test
 public void testReset() {
 newUnix.reset();
 assertEquals(1000, newUnix.getUID());
 assertEquals(1000, newUnix.getGID());
 }

 @Test
 public void testToString() {
 assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", newUnix.toString());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix newUnixClone = (X7875_NewUnix) newUnix.clone();
 assertNotSame(newUnix, newUnixClone);
 assertEquals(newUnix.getUID(), newUnixClone.getUID());
 assertEquals(newUnix.getGID(), newUnixClone.getGID());
 }

 @Test
 public void testEquals() {
 X7875_NewUnix newUnix2 = new X7875_NewUnix();
 X7875_NewUnix newUnix3 = new X7875_NewUnix();
 assertFalse(newUnix.equals(null));
 assertTrue(newUnix.equals(newUnix));
 assertFalse(newUnix.equals(newUnix2));
 newUnix2.setUID(1000);
 newUnix2.setGID(1000);
 assertTrue(newUnix.equals(newUnix2));
 assertNotEquals(newUnix, newUnix3);
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix newUnix2 = new X7875_NewUnix();
 X7875_NewUnix newUnix3 = new X7875_NewUnix();
 assertEquals(-134533435, newUnix.hashCode());
 newUnix2.setUID(1000);
 newUnix2.setGID(1000);
 assertEquals(-134533435, newUnix.hashCode());
 newUnix3.setUID(2000);
 newUnix3.setGID(2000);
 assertNotEquals(newUnix.hashCode(), newUnix3.hashCode());
 }
}