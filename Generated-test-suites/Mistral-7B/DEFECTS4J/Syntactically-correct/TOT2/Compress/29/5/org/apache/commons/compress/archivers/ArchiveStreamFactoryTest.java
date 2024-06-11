import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import java.io.*;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 private static InputStream testInputStream;
 private static OutputStream testOutputStream;

 @BeforeClass
 public static void setup() throws IOException {
 testInputStream = new ByteArrayInputStream(new byte[1024]);
 testOutputStream = new ByteArrayOutputStream();
 }

 @AfterClass
 public static void teardown() throws IOException {
 IOUtils.closeQuietly(testInputStream);
 IOUtils.closeQuietly(testOutputStream);
 }

 /**
 * Test creating an ArchiveInputStream with a known format
 */
 @Test
 public void testCreateArchiveInputStream() throws IOException, ArchiveException {
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
 assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
 }

 /**
 * Test creating an ArchiveInputStream with an unknown format
 */
 @Test(expected = ArchiveException.class)
 public void testCreateUnknownFormatArchiveInputStream() throws IOException {
 new ArchiveStreamFactory()
 .createArchiveInputStream("unknownformat", testInputStream);
 }

 /**
 * Test creating an ArchiveOutputStream with a known format
 */
 @Test
 public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, testOutputStream);
 assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
 }

 /**
 * Test creating an ArchiveOutputStream with an unknown format
 */
 @Test(expected = ArchiveException.class)
 public void testCreateUnknownFormatArchiveOutputStream() throws IOException {
 new ArchiveStreamFactory()
 .createArchiveOutputStream("unknownformat", testOutputStream);
 }

 /**
 * Test creating an ArchiveInputStream from an InputStream with a known format
 */
 @Test
 public void testCreateArchiveInputStreamFromInputStream() throws IOException, ArchiveException {
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
 assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
 }

 /**
 * Test creating an ArchiveOutputStream from an InputStream with an unknown format
 */
 @Test(expected = ArchiveException.class)
 public void testCreateUnknownFormatArchiveInputStreamFromInputStream() throws IOException {
 new ArchiveStreamFactory()
 .createArchiveInputStream(ArchiveStreamFactory.UNKNOWN_FORMAT, testInputStream);
 }

 /**
 * Test creating an ArchiveInputStream from a byte array with a known format
 */
 @Test
 public void testCreateArchiveInputStreamFromByteArray() throws IOException, ArchiveException {
 byte[] testData = new byte[1024];
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(testData));
 assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
 }

 /**
 * Test creating an ArchiveOutputStream from a byte array with an unknown format
 */
 @Test(expected = ArchiveException.class)
 public void testCreateUnknownFormatArchiveInputStreamFromByteArray() throws IOException {
 new ArchiveStreamFactory()
 .createArchiveInputStream(ArchiveStreamFactory.UNKNOWN_FORMAT, new ByteArrayInputStream(new byte[1024]));
 }

 /**
 * Test creating an ArchiveInputStream with a null InputStream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithNullInputStream() {
 new ArchiveStreamFactory()
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
 }

 /**
 * Test creating an ArchiveOutputStream with a null OutputStream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNullOutputStream() {
 new ArchiveStreamFactory()
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
 }

 /**
 * Test creating an ArchiveInputStream with a null archiverName
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithNullArchiverName() {
 new ArchiveStreamFactory()
 .createArchiveInputStream(null, testInputStream);
 }

 /**
 * Test creating an ArchiveOutputStream with a null archiverName
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNullArchiverName() {
 new ArchiveStreamFactory()
 .createArchiveOutputStream(null, testOutputStream);
 }

 /**
 * Test creating an ArchiveInputStream with an empty archiverName
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithEmptyArchiverName() {
 new ArchiveStreamFactory()
 .createArchiveInputStream("", testInputStream);
 }

 /**
 * Test creating an ArchiveOutputStream with an empty archiverName
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithEmptyArchiverName() {
 new ArchiveStreamFactory()
 .createArchiveOutputStream("", testOutputStream);
 }

 /**
 * Test creating an ArchiveInputStream with a long archiverName
 */
 @Test
 public void testCreateArchiveInputStreamWithLongArchiverName() throws IOException, ArchiveException {
 String longArchiverName = "A".repeat(1024);
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
 .createArchiveInputStream(longArchiverName, testInputStream);
 assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
 }

 /**
 * Test creating an ArchiveOutputStream with a long archiverName
 */
 @Test
 public void testCreateArchiveOutputStreamWithLongArchiverName() throws IOException, ArchiveException {
 String longArchiverName = "A".repeat(1024);
 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
 .createArchiveOutputStream(longArchiverName, testOutputStream);
 assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
 }

 /**
 * Test creating an ArchiveInputStream with an invalid encoding
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
 }

 /**
 * Test creating an ArchiveOutputStream with an invalid encoding
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, testOutputStream);
 }

 /**
 * Test creating an ArArchiveInputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateArArchiveInputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveInputStream(ArchiveStreamFactory.AR, testInputStream);
 }

 /**
 * Test creating an ArArchiveOutputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateArArchiveOutputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveOutputStream(ArchiveStreamFactory.AR, testOutputStream);
 }

 /**
 * Test creating an ArjArchiveInputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateArjArchiveInputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveInputStream(ArchiveStreamFactory.ARJ, testInputStream);
 }

 /**
 * Test creating an ArjArchiveOutputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateArjArchiveOutputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveOutputStream(ArchiveStreamFactory.ARJ, testOutputStream);
 }

 /**
 * Test creating an CpioArchiveInputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateCpioArchiveInputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveInputStream(ArchiveStreamFactory.CPIO, testInputStream);
 }

 /**
 * Test creating an CpioArchiveOutputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateCpioArchiveOutputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveOutputStream(ArchiveStreamFactory.CPIO, testOutputStream);
 }

 /**
 * Test creating an DumpArchiveInputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateDumpArchiveInputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveInputStream(ArchiveStreamFactory.DUMP, testInputStream);
 }

 /**
 * Test creating an DumpArchiveOutputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateDumpArchiveOutputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveOutputStream(ArchiveStreamFactory.DUMP, testOutputStream);
 }

 /**
 * Test creating an JarArchiveInputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateJarArchiveInputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveInputStream(ArchiveStreamFactory.JAR, testInputStream);
 }

 /**
 * Test creating an JarArchiveOutputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateJarArchiveOutputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveOutputStream(ArchiveStreamFactory.JAR, testOutputStream);
 }

 /**
 * Test creating a TarArchiveInputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateTarArchiveInputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveInputStream(ArchiveStreamFactory.TAR, testInputStream);
 }

 /**
 * Test creating a TarArchiveOutputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateTarArchiveOutputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveOutputStream(ArchiveStreamFactory.TAR, testOutputStream);
 }

 /**
 * Test creating a ZipArchiveInputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateZipArchiveInputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
 }

 /**
 * Test creating a ZipArchiveOutputStream with an invalid encoding
 */
 @Test(expected = ArchiveException.class)
 public void testCreateZipArchiveOutputStreamWithInvalidEncoding() throws IOException, ArchiveException {
 new ArchiveStreamFactory("invalidencoding")
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, testOutputStream);
 }

 /**
 * Test creating an ArchiveInputStream with an empty InputStream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithEmptyInputStream() {
 new ArchiveStreamFactory()
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
 }

 /**
 * Test creating an ArchiveOutputStream with an empty OutputStream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithEmptyOutputStream() {
 new ArchiveStreamFactory()
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
 }

 /**
 * Test creating an ArchiveInputStream with a null archiverName and null InputStream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithNullArchiverNameAndNullInputStream() {
 new ArchiveStreamFactory()
 .createArchiveInputStream(null, null);
 }

 /**
 * Test creating an ArchiveOutputStream with a null archiverName and non-null OutputStream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNullArchiverNameAndNonNullOutputStream() {
 new ArchiveStreamFactory()
 .createArchiveOutputStream(null, testOutputStream);
 }

 /**
 * Test creating an ArchiveOutputStream with a non-null archiverName and null OutputStream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNonNullArchiverNameAndNullOutputStream() {
 new ArchiveStreamFactory("test")
 .createArchiveOutputStream(null, null);
 }

 /**
 * Test creating an ArchiveInputStream with a null archiverName and non-null InputStream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithNullArchiverNameAndNonNullInputStream() {
 new ArchiveStreamFactory()
 .createArchiveInputStream(null, testInputStream);
 }

 /**
 * Test creating an ArchiveOutputStream with a non-null archiverName and null InputStream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNonnullArchiverNameAndNullInputStream() {
 new ArchiveStreamFactory("test")
 .createArchiveOutputStream(null, null);
 }

 /**
 * Test creating an ArchiveInputStream with a null archiverName and null InputStream with invalid signature
 */
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamWithNullArchiverNameAndNullInputStreamWithInvalidSignature() throws IOException, ArchiveException {
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
 testOutputStream.write("invalid signature".getBytes());
 testOutputStream.close();
 InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());
 new ArchiveStreamFactory()
 .createArchiveInputStream(null, testInputStream);
 }

 /**
 * Test creating an ArchiveOutputStream with a non-null archiverName and null InputStream with invalid signature
 */
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamWithNonnullArchiverNameAndNullInputStreamWithInvalidSignature() throws IOException, ArchiveException {
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
 testOutputStream.write("invalid signature".getBytes());
 testOutputStream.close();
 InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());
 new ArchiveStreamFactory("test")
 .createArchiveOutputStream(null, testInputStream);
 }
}