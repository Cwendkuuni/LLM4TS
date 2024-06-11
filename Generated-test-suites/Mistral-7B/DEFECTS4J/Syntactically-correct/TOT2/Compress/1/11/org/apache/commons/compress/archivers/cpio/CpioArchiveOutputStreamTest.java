import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Test for CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

 private OutputStream out;
 private CpioArchiveOutputStream cpioOut;
 private ByteArrayOutputStream byteArrayOut;

 @Before
 public void beforeTest() throws IOException {
 out = new GZIPOutputStream(new ByteArrayOutputStream());
 cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
 byteArrayOut = new ByteArrayOutputStream();
 }

 /**
 * Test writing a new entry with default values
 */
 @Test
 public void testWriteNewEntryWithDefaultValues() throws IOException {
 CpioArchiveEntry entry = new CpioArchiveEntry();
 entry.setName("testfile");
 cpioOut.putNextEntry(entry);
 cpioOut.write("12345".getBytes());
 cpioOut.closeArchiveEntry();

 byte[] expectedOutput = generateExpectedOutput("testfile", "12345");
 Assert.assertArrayEquals(expectedOutput, byteArrayOut.toByteArray());
 }

 /**
 * Test writing a new entry with custom values
 */
 @Test
 public void testWriteNewEntryWithCustomValues() throws IOException {
 CpioArchiveEntry entry = new CpioArchiveEntry();
 entry.setName("testfile");
 entry.setFormat(CpioConstants.FORMAT_NEW_CRC);
 entry.setMode(0755);
 entry.setSize(1234);
 entry.setUID(501);
 entry.setGID(502);
 cpioOut.putNextEntry(entry);
 cpioOut.write("12345".getBytes());
 cpioOut.closeArchiveEntry();

 byte[] expectedOutput = generateExpectedOutput("testfile", "12345", 0755, 1234, 501, 502);
 Assert.assertArrayEquals(expectedOutput, byteArrayOut.toByteArray());
 }

 /**
 * Test writing multiple entries
 */
 @Test
 public void testWriteMultipleEntries() throws IOException {
 CpioArchiveEntry entry1 = new CpioArchiveEntry();
 entry1.setName("testfile1");
 cpioOut.putNextEntry(entry1);
 cpioOut.write("12345".getBytes());
 cpioOut.closeArchiveEntry();

 CpioArchiveEntry entry2 = new CpioArchiveEntry();
 entry2.setName("testfile2");
 cpioOut.putNextEntry(entry2);
 cpioOut.write("67890".getBytes());
 cpioOut.closeArchiveEntry();

 byte[] expectedOutput = generateExpectedOutput("testfile1", "12345")
 .concat(generateExpectedOutput("testfile2", "67890"));
 Assert.assertArrayEquals(expectedOutput, byteArrayOut.toByteArray());
 }

 /**
 * Test writing an entry with incorrect format
 */
 @Test(expected = IllegalArgumentException.class)
 public void testWriteEntryWithIncorrectFormat() throws IOException {
 CpioArchiveEntry entry = new CpioArchiveEntry();
 entry.setFormat(-1);
 cpioOut.putNextEntry(entry);
 }

 /**
 * Test writing an entry with duplicate name
 */
 @Test(expected = IOException.class)
 public void testWriteEntryWithDuplicateName() throws IOException {
 CpioArchiveEntry entry1 = new CpioArchiveEntry();
 entry1.setName("testfile");
 cpioOut.putNextEntry(entry1);
 cpioOut.write("12345".getBytes());
 cpioOut.closeArchiveEntry();

 CpioArchiveEntry entry2 = new CpioArchiveEntry();
 entry2.setName("testfile");
 cpioOut.putNextEntry(entry2);
 }

 /**
 * Test writing an entry larger than the expected size
 */
 @Test(expected = IOException.class)
 public void testWriteEntryLargerThanExpectedSize() throws IOException {
 CpioArchiveEntry entry = new CpioArchiveEntry();
 entry.setName("testfile");
 entry.setSize(12345);
 cpioOut.putNextEntry(entry);
 cpioOut.write("123456789012345".getBytes());
 }

 /**
 * Test writing an entry smaller than the expected size
 */
 @Test
 public void testWriteEntrySmallerThanExpectedSize() throws IOException {
 CpioArchiveEntry entry = new CpioArchiveEntry();
 entry.setName("testfile");
 entry.setSize(1234);
 cpioOut.putNextEntry(entry);
 cpioOut.write("12345".getBytes(0, 4));
 cpioOut.closeArchiveEntry();

 byte[] expectedOutput = generateExpectedOutput("testfile", "1234");
 Assert.assertArrayEquals(expectedOutput, byteArrayOut.toByteArray());
 }

 /**
 * Test writing an entry with CRC
 */
 @Test
 public void testWriteEntryWithCrc() throws IOException {
 CpioArchiveEntry entry = new CpioArchiveEntry();
 entry.setName("testfile");
 entry.setFormat(CpioConstants.FORMAT_NEW_CRC);
 entry.setSize(1234);
 cpioOut.putNextEntry(entry);
 cpioOut.write("12345".getBytes());
 cpioOut.finish();

 byte[] expectedOutput = generateExpectedOutputWithCrc("testfile", "12345");
 Assert.assertArrayEquals(expectedOutput, byteArrayOut.toByteArray());
 }

 /**
 * Test closing the archive without writing any entry
 */
 @Test
 public void testCloseArchiveWithoutWritingEntry() throws IOException {
 cpioOut.close();
 }

 /**
 * Test closing the archive after writing all entries
 */
 @Test
 public void testCloseArchiveAfterWritingAllEntries() throws IOException {
 cpioOut.finish();
 cpioOut.close();
 }

 /**
 * Generates expected output for a given entry with given data and optional
 * custom values
 *
 * @param name name of the entry
 * @param data data to be written to the entry
 * @param mode optional mode value
 * @param size optional size value
 * @param uid optional UID value
 * @param gid optional GID value
 * @return expected output as byte array
 */
 private byte[] generateExpectedOutput(String name, String data, int... optionalValues) {
 byte[] dataBytes = data.getBytes();
 long size = dataBytes.length;
 if (optionalValues != null && optionalValues.length > 0) {
 size = optionalValues[0];
 }
 long inode = CpioUtil.generateInode();
 long modeLong = optionalValues.length > 1 ? optionalValues[1] : 0;
 long mode = (short) modeLong;
 long uid = optionalValues.length > 2 ? optionalValues[2] : 0;
 long gid = optionalValues.length > 3 ? optionalValues[3] : 0;
 long numberOfLinks = optionalValues.length > 4 ? optionalValues[4] : 1;

 byte[] expectedOutput = new byte[size + getHeaderSize(name.length() + 1)];
 writeNewEntryHeader(expectedOutput, name.getBytes(), inode, mode, uid, gid, numberOfLinks, size);
 System.arraycopy(dataBytes, 0, expectedOutput, 0, dataBytes.length);
 return expectedOutput;
 }

 /**
 * Generates expected output for a given entry with given data, custom values
 * and CRC checksum
 *
 * @param name name of the entry
 * @param data data to be written to the entry
 * @param mode optional mode value
 * @param size optional size value
 * @param uid optional UID value
 * @param gid optional GID value
 * @param numberOfLinks optional number of links value
 * @return expected output as byte array
 */
 private byte[] generateExpectedOutputWithCrc(String name, String data, int... optionalValues) {
 byte[] dataBytes = data.getBytes();
 long size = dataBytes.length;
 if (optionalValues != null && optionalValues.length > 0) {
 size = optionalValues[0];
 }
 long inode = CpioUtil.generateInode();
 long modeLong = optionalValues.length > 1 ? optionalValues[1] : 0;
 long mode = (short) modeLong;
 long uid = optionalValues.length > 2 ? optionalValues[2] : 0;
 long gid = optionalValues.length > 3 ? optionalValues[3] : 0;
 long numberOfLinks = optionalValues.length > 4 ? optionalValues[4] : 1;
 long chksum = CpioUtil.calculateChecksum(dataBytes);

 byte[] expectedOutput = new byte[size + getHeaderSize(name.length() + 1) + CpioConstants.FORMAT_NEW_CRC_SIZE];
 writeNewEntryHeaderWithCrc(expectedOutput, name.getBytes(), inode, mode, uid, gid, numberOfLinks, size, chksum);
 System.arraycopy(dataBytes, 0, expectedOutput, 0, dataBytes.length);
 return expectedOutput;
 }

 /**
 * Gets header size based on the name length
 *
 * @param nameLength name length
 * @return header size
 */
 private int getHeaderSize(int nameLength) {
 int headerSize = 16; // magic number and format code
 headerSize += 8 * (4 + 2 + 2 + 1 + 1 + 1 + 1); // name length + 1
 headerSize += 8 * (1 + 1 + 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8); // for each entry field
 headerSize += 8 * nameLength; // name
 return headerSize;
 }

 /**
 * Writes new entry header without CRC
 *
 * @param output byte array to write to
 * @param name name of the entry
 * @param inode inode number
 * @param mode mode value
 * @param uid UID value
 * @param gid GID value
 * @param numberOfLinks number of links value
 * @param size size value
 */
 private void writeNewEntryHeader(byte[] output, byte[] name, long inode, short mode, long uid, long gid, long numberOfLinks, long size) {
 writeMagicNumber(output);
 writeFormatCode(output, CpioConstants.FORMAT_NEW);
 writeInodeNumber(output, inode);
 writeMode(output, mode);
 writeUid(output, uid);
 writeGid(output, gid);
 writeNumberOfLinks(output, numberOfLinks);
 writeSize(output, size);
 writeDeviceMajorAndMinor(output, 0, 0);
 writeName(output, name);
 }

 /**
 * Writes new entry header with CRC
 *
 * @param output byte array to write to
 * @param name name of the entry
 * @param inode inode number
 * @param mode mode value
 * @param uid UID value
 * @param gid GID value
 * @param numberOfLinks number of links value
 * @param size size value
 * @param chksum checksum value
 */
 private void writeNewEntryHeaderWithCrc(byte[] output, byte[] name, long inode, short mode, long uid, long gid, long numberOfLinks, long size, long chksum) {
 writeMagicNumber(output);
 writeFormatCode(output, CpioConstants.FORMAT_NEW_CRC);
 writeInodeNumber(output, inode);
 writeMode(output, mode);
 writeUid(output, uid);
 writeGid(output, gid);
 writeNumberOfLinks(output, numberOfLinks);
 writeSize(output, size);
 writeDeviceMajorAndMinor(output, 0, 0);
 writeName(output, name);
 writeChecksum(output, chksum);
 }

 /**
 * Writes magic number to the output
 *
 * @param output byte array to write to
 */
 private void writeMagicNumber(byte[] output) {
 writeBytes(output, MAGIC_NEW);
 }

 /**
 * Writes format code to the output
 *
 * @param output byte array to write to
 * @param formatCode format code
 */
 private void writeFormatCode(byte[] output, short formatCode) {
 writeShort(output, formatCode);
 }

 /**
 * Writes inode number to the output
 *
 * @param output byte array to write to
 * @param inode inode number
 */
 private void writeInodeNumber(byte[] output, long inode) {
 writeLong(output, inode);
 }

 /**
 * Writes mode to the output
 *
 * @param output byte array to write to
 * @param mode mode value
 */
 private void writeMode(byte[] output, short mode) {
 writeShort(output, mode);
 }

 /**
 * Writes UID to the output
 *
 * @param output byte array to write to
 * @param uid UID value
 */
 private void writeUid(byte[] output, long uid) {
 writeLong(output, uid);
 }

 /**
 * Writes GID to the output
 *
 * @param output byte array to write to
 * @param gid GID value
 */
 private void writeGid(byte[] output, long gid) {
 writeLong(output, gid);
 }

 /**
 * Writes number of links to the output
 *
 * @param output byte array to write to
 * @param numberOfLinks number of links value
 */
 private void writeNumberOfLinks(byte[] output, long numberOfLinks) {
 writeLong(output, numberOfLinks);
 }

 /**
 * Writes size to the output
 *
 * @param output byte array to write to
 * @param size size value
 */
 private void writeSize(byte[] output, long size) {
 writeLong(output, size);
 }

 /**
 * Writes device major and minor to the output
 *
 * @param output byte array to write to
 * @param major major value
 * @param minor minor value
 */
 private void writeDeviceMajorAndMinor(byte[] output, int major, int minor) {
 writeShort(output, (short) (major << 8 | minor));
 }

 /**
 * Writes name to the output
 *
 * @param output byte array to write to
 * @param name name to write
 */
 private void writeName(byte[] output, byte[] name) {
 int length = name.length + 1;
 writeShort(output, (short) length);
 System.arraycopy(name, 0, output, output.length - length, length);
 }

 /**
 * Writes checksum to the output
 *
 * @param output byte array to write to
 * @param chksum checksum value
 */
 private void writeChecksum(byte[] output, long chksum) {
 for (int i = 0; i < CpioConstants.FORMAT_NEW_CRC_SIZE; i++) {
 writeByte(output, (byte) ((chksum >> 8 * i) & 0xFF));
 }
 }

 /**
 * Writes bytes to the output
 *
 * @param output byte array to write to
 * @param bytes bytes to write
 */
 private void writeBytes(byte[] output, byte[] bytes) {
 System.arraycopy(bytes, 0, output, 0, bytes.length);
 }

 /**
 * Writes short to the output
 *
 * @param output byte array to write to
 * @param value short value to write
 */
 private void writeShort(byte[] output, short value) {
 writeBytes(output, CpioUtil.short2byteArray(value));
 }

 /**
 * Writes long to the output
 *
 * @param output byte array to write to
 * @param value long value to write
 */
 private void writeLong(byte[] output, long value) {
 writeBytes(output, CpioUtil.long2byteArray(value));
 }

 /**
 * Writes byte to the output
 *
 * @param output byte array to write to
 * @param value byte value to write
 */
 private void writeByte(byte[] output, byte value) {
 output[output.length - 1] = value;
 }
}