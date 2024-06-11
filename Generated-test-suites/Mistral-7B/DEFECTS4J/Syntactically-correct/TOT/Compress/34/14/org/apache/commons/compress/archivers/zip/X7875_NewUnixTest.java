package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 private X7875_NewUnix x7875;

 @Before
 public void setup() {
 x7875 = new X7875_NewUnix();
 }

 /**
 * Test method for {@link X7875_NewUnix#getHeaderId()}.
 */
 @Test
 public void testGetHeaderId() {
 ZipShort expected = new ZipShort(0x7875);
 ZipShort actual = x7875.getHeaderId();
 assertEquals(expected, actual);
 }

 /**
 * Test method for {@link X7875_NewUnix#getUID()}.
 */
 @Test
 public void testGetUID() {
 long uid = 1001;
 x7875.setUID(uid);
 long actual = x7875.getUID();
 assertEquals(uid, actual);
 }

 /**
 * Test method for {@link X7875_NewUnix#getGID()}.
 */
 @Test
 public void testGetGID() {
 long gid = 1002;
 x7875.setGID(gid);
 long actual = x7875.getGID();
 assertEquals(gid, actual);
 }

 /**
 * Test method for {@link X7875_NewUnix#getLocalFileDataLength()}.
 */
 @Test
 public void testGetLocalFileDataLength() {
 int uidSize = 3; // assuming uid = 001 (3 bytes long)
 int gidSize = 3; // assuming gid = 002 (3 bytes long)
 ZipShort expected = new ZipShort(3 + uidSize + gidSize);
 ZipShort actual = x7875.getLocalFileDataLength();
 assertEquals(expected, actual);
 }

 /**
 * Test method for {@link X7875_NewUnix#parseFromLocalFileData(byte[], int, int)}.
 */
 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] data = new byte[]{
 (byte)0x78, (byte)0x75,
 (byte)0x01, (byte)0x01, // version=1
 (byte)0x03, (byte)0x00, (byte)0x01, // uid size=3, uid=001
 (byte)0x03, (byte)0x00, (byte)0x02, // gid size=3, gid=002
 };
 x7875.parseFromLocalFileData(data, 0, data.length);
 assertEquals(1, x7875.getUID());
 assertEquals(2, x7875.getGID());
 }

 /**
 * Test method for {@link X7875_NewUnix#reset()}.
 */
 @Test
 public void testReset() {
 x7875.reset();
 assertEquals(1, x7875.getVersion());
 assertEquals(BigInteger.valueOf(1000), x7875.getUID());
 assertEquals(BigInteger.valueOf(1000), x7875.getGID());
 }

 /**
 * Test method for {@link X7875_NewUnix#toString()}.
 */
 @Test
 public void testToString() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(1001);
 x7875.setGID(1002);
 String expected = "0x7875 Zip Extra Field: UID=1001 GID=1002";
 String actual = x7875.toString();
 assertEquals(expected, actual);
 }
}