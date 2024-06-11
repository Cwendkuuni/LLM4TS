package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 @Test
 public void testNewInstance() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 assertNotNull(x7875);
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 ZipShort headerId = x7875.getHeaderId();
 assertEquals(new ZipShort((short) 0x7875), headerId);
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long uid = x7875.getUID();
 assertEquals(1000L, uid);
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long newUid = 123456789;
 x7875.setUID(newUid);
 long uid = x7875.getUID();
 assertEquals(newUid, uid);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUIDNegativeValue() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long newUid = -123456789;
 x7875.setUID(newUid);
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long gid = x7875.getGID();
 assertEquals(1000L, gid);
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long newGid = 987654321;
 x7875.setGID(newGid);
 long gid = x7875.getGID();
 assertEquals(newGid, gid);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetGIDNegativeValue() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long newGid = -987654321;
 x7875.setGID(newGid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 int length = x7875.getLocalFileDataLength().getValue();
 assertEquals(12, length);
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 int length = x7875.getCentralDirectoryLength().getValue();
 assertEquals(12, length);
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = x7875.getLocalFileDataData();
 assertNotNull(data);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09};
 x7875.parseFromLocalFileData(data, 0, data.length);
 long uid = x7875.getUID();
 long gid = x7875.getGID();
 assertEquals(1234567890123L, uid);
 assertEquals(2345678901234L, gid);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataBadData() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x02};
 x7875.parseFromLocalFileData(data, 0, data.length);
 }

 @Test
 public void testReset() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.reset();
 long uid = x7875.getUID();
 long gid = x7875.getGID();
 assertEquals(1000L, uid);
 assertEquals(1000L, gid);
 }

 @Test
 public void testToString() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 String toString = x7875.toString();
 assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", toString);
 }

}