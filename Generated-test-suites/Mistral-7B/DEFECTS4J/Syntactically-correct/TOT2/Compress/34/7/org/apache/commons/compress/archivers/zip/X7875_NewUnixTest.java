package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

/**
 * Test for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

 // Test case for constructor
 @org.junit.Test
 public void testConstructor() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 assertEquals(X7875_NewUnix.HEADER_ID, x7875.getHeaderId());
 assertNull(x7875.getUID());
 assertNull(x7875.getGID());
 }

 // Test case for getUID() and setUID() methods
 @org.junit.Test
 public void testGetUIDAndSetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(123);
 assertEquals(123L, x7875.getUID());
 }

 // Test case for getGID() and setGID() methods
 @org.junit.Test
 public void testGetGIDAndSetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setGID(456);
 assertEquals(456L, x7875.getGID());
 }

 // Test case for getLocalFileDataLength() method
 @org.junit.Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(123);
 x7875.setGID(456);
 ZipShort length = x7875.getLocalFileDataLength();
 assertEquals(7, length.getValue().shortValue());
 }

 // Test case for getCentralDirectoryLength() method
 @org.junit.Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 ZipShort length = x7875.getCentralDirectoryLength();
 assertEquals(0, length.getValue().shortValue());
 }

 // Test case for getLocalFileDataData() method
 @org.junit.Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(123);
 x7875.setGID(456);
 byte[] expectedData = new byte[]{(byte) 0x01, (byte) 0x01, 0, 0, 0, 0, 0, 0,
 (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0,
 (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, (byte) 0x01, 0, 0, 0, 0x64, 0x65, 0x32, 0x33,
 (byte) 0x01, 0, 0, 0, 0x67, 0x65, 0x34, 0x35};
 byte[] data = x7875.getLocalFileDataData();
 assertArrayEquals(expectedData, data);
 }

 // Test case for parseFromLocalFileData() method
 @org.junit.Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x01, 0, 0, 0, 0, 0, 0,
 (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, (byte) 0x01, 0, 0, 0, 0x64, 0x65, 0x32, 0x33,
 (byte) 0x01, 0, 0, 0, 0x67, 0x65, 0x34, 0x35};
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(data, 0, data.length);
 assertEquals(1L, x7875.getUID());
 assertEquals(456L, x7875.getGID());
 }

 // Test case for equals() method
 @org.junit.Test
 public void testEquals() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(123);
 x7875.setGID(456);
 X7875_NewUnix x7875Copy = new X7875_NewUnix();
 x7875Copy.setUID(123);
 x7875Copy.setGID(456);
 assertTrue(x7875.equals(x7875Copy));
 assertFalse(x7875.equals(null));
 assertFalse(x7875.equals(new Object()));
 }

 // Test case for hashCode() method
 @org.junit.Test
 public void testHashCode() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(123);
 x7875.setGID(456);
 X7875_NewUnix x7875Copy = new X7875_NewUnix();
 x7875Copy.setUID(123);
 x7875Copy.setGID(456);
 assertEquals(x7875.hashCode(), x7875Copy.hashCode());
 assertNotSame(x7875.hashCode(), new Object().hashCode());
 }

 // Test case for parseFromLocalFileData() method with wrong data length
 @org.junit.Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithWrongDataLength() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x01};
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Expected ZipException");
 }

 // Test case for parseFromLocalFileData() method with wrong version
 @org.junit.Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithWrongVersion() throws ZipException {
 byte[] data = new byte[]{(byte) 0x78, (byte) 0x75};
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Expected ZipException");
 }

 // Test case for parseFromLocalFileData() method with wrong UID size
 @org.junit.Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithWrongUIDSize() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, 0, 0, 0, 0,
 (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, (byte) 0x01, 0, 0, 0, 0x67, 0x65, 0x34, 0x35};
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Expected ZipException");
 }

 // Test case for parseFromLocalFileData() method with wrong GID size
 @org.junit.Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithWrongGIDSize() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x01, 0, 0, 0, 0, 0, 0,
 (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, (byte) 0x01, 0, 0, 0, 0x64, 0x65, 0x32, 0x33,
 (byte) 0x01, 0, 0, 0, 0x67, 0x65, 0x34, 0x35, (byte) 0x01};
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Expected ZipException");
 }
}