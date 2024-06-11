package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {

 // Basic tests for methods

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 ZipShort result = x7875.getHeaderId();
 ZipShort expected = new ZipShort(0x7875);
 assertEquals(expected, result);
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345L);
 long result = x7875.getUID();
 long expected = 12345L;
 assertEquals(expected, result);
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setGID(54321L);
 long result = x7875.getGID();
 long expected = 54321L;
 assertEquals(expected, result);
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(987654321L);
 BigInteger expectedUid = new BigInteger("987654321");
 x7875.uid = expectedUid;
 BigInteger resultUid = x7875.uid;
 assertEquals(expectedUid, resultUid);
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setGID(1357924600123L);
 BigInteger expectedGid = new BigInteger("1357924600123");
 x7875.gid = expectedGid;
 BigInteger resultGid = x7875.gid;
 assertEquals(expectedGid, resultGid);
 }

 // Test for edge cases and exception handling

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidLength() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = {0x78, 0x75, 0x01, 0x01, (byte) 0xFF, 0x00, 0x00, 0x00};
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Parse should have thrown exception");
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidVersion() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = {0x78, 0x76, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00};
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Parse should have thrown exception");
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = {0x78, 0x75, (byte) 0x01, (byte) 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
 x7875.parseFromLocalFileData(data, 0, data.length);
 BigInteger expectedUid = new BigInteger("1000");
 BigInteger expectedGid = new BigInteger("1000");
 assertEquals(expectedUid, x7875.uid);
 assertEquals(expectedGid, x7875.gid);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithMalformedData() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = {0x78, 0x75, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00};
 x7875.parseFromLocalFileData(data, 0, data.length + 1);
 fail("Parse should have thrown exception");
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithNegativeVersion() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = {0x78, 0x75, 0xFF, 0x01, 0x00, 0x00, 0x00, 0x00};
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Parse should have thrown exception");
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithNegativeUID() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = {0x78, 0x75, 0x01, 0x01, 0x80, 0x00, 0x00, 0x00};
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Parse should have thrown exception");
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithNegativeGID() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = {0x78, 0x75, 0x01, 0x01, 0x00, 0xFF, 0x00, 0x00};
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Parse should have thrown exception");
 }

 @Test
 public void testParseFromLocalFileDataWithMaximumValues() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = {0x78, 0x75, 0x01, 0x01, 0xFF, 0xFF, 0xFF, 0xFF};
 x7875.parseFromLocalFileData(data, 0, data.length);
 BigInteger expectedUid = new BigInteger("4294967295");
 BigInteger expectedGid = new BigInteger("4294967295");
 assertEquals(expectedUid, x7875.uid);
 assertEquals(expectedGid, x7875.gid);
 }

 // Test for clone method

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix original = new X7875_NewUnix();
 original.setUID(12345L);
 original.setGID(54321L);
 X7875_NewUnix clone = (X7875_NewUnix) original.clone();
 BigInteger clonedUid = clone.uid;
 BigInteger originalUid = original.uid;
 assertEquals(originalUid, clonedUid);
 }

 // Test for equals and hashcode methods

 @Test
 public void testEquals() {
 X7875_NewUnix x7875_1 = new X7875_NewUnix();
 X7875_NewUnix x7875_2 = new X7875_NewUnix();
 X7875_NewUnix x7875_3 = new X7875_NewUnix();
 x7875_1.setUID(12345L);
 x7875_1.setGID(54321L);
 x7875_2.setUID(12345L);
 x7875_2.setGID(54321L);
 x7875_3.setUID(54321L);
 x7875_3.setGID(12345L);
 assertTrue(x7875_1.equals(x7875_2));
 assertFalse(x7875_1.equals(x7875_3));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix x7875_1 = new X7875_NewUnix();
 X7875_NewUnix x7875_2 = new X7875_NewUnix();
 X7875_NewUnix x7875_3 = new X7875_NewUnix();
 x7875_1.setUID(12345L);
 x7875_1.setGID(54321L);
 x7875_2.setUID(12345L);
 x7875_2.setGID(54321L);
 x7875_3.setUID(54321L);
 x7875_3.setGID(12345L);
 int hashcode_1 = x7875_1.hashCode();
 int hashcode_2 = x7875_2.hashCode();
 int hashcode_3 = x7875_3.hashCode();
 assertNotSame(hashcode_1, hashcode_2);
 assertNotSame(hashcode_1, hashcode_3);
 }
}