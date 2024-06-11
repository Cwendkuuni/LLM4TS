import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class X7875_NewUnixTest {

 @Test
 public void testDefaultValues() {
 X7875_NewUnix test = new X7875_NewUnix();
 assertEquals(1, test.getVersion());
 assertNull(test.getUid());
 assertNull(test.getGid());
 assertEquals(new ZipShort(0x7875), test.getHeaderId());
 }

 @Test
 public void testSetGetUid() {
 X7875_NewUnix test = new X7875_NewUnix();
 test.setUID(123456789012345l);
 assertEquals(123456789012345l, test.getUID());
 }

 @Test
 public void testSetGetGid() {
 X7875_NewUnix test = new X7875_NewUnix();
 test.setGID(987654321098765l);
 assertEquals(987654321098765l, test.getGID());
 }

 @Test
 public void testParseFromLocalFileData() {
 X7875_NewUnix test = new X7875_NewUnix();
 byte[] data = new byte[]{(byte) 1, (byte) 4, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0};
 test.parseFromLocalFileData(data, 0, data.length);
 assertEquals(1, test.getVersion());
 assertEquals(new BigInteger("1234567890"), test.getUid());
 assertEquals(new BigInteger("1"), test.getGid());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidData() {
 X7875_NewUnix test = new X7875_NewUnix();
 byte[] data = new byte[]{(byte) 1, (byte) 4, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1};
 test.parseFromLocalFileData(data, 0, data.length);
 fail();
 }

 @Test
 public void testEquals() {
 X7875_NewUnix test1 = new X7875_NewUnix();
 X7875_NewUnix test2 = new X7875_NewUnix();
 assertFalse(test1.equals(test2));
 X7875_NewUnix test3 = new X7875_NewUnix();
 test3.setUID(123456789012345l);
 test3.setGID(987654321098765l);
 test1 = test3;
 assertTrue(test1.equals(test3));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix test1 = new X7875_NewUnix();
 X7875_NewUnix test2 = new X7875_NewUnix();
 test2.setUID(123456789012345l);
 test2.setGID(987654321098765l);
 assertEquals(test1.hashCode(), test2.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix test = new X7875_NewUnix();
 test.setUID(123456789012345l);
 test.setGID(987654321098765l);
 X7875_NewUnix clone = (X7875_NewUnix) test.clone();
 assertEquals(test.getUID(), clone.getUID());
 assertEquals(test.getGID(), clone.getGID());
 }
}