import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

 // Test for createArchiveInputStream(String, InputStream)
 @Test
 public void testCreateArchiveInputStreamStringInputStream() throws Exception {
 String archiverName = "zip";
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
 Assert.assertEquals(ArchiveStreamFactory.ZIP, archiverName);
 Assert.assertNotNull(archiveInputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamWrongArchiverName() throws Exception {
 String archiverName = "foobar";
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamNullInputStream() throws Exception {
 String archiverName = "zip";
 InputStream inputStream = null;
 new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamInvalidSignature() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream);
 }

 // Test for createArchiveOutputStream(String, OutputStream)
 @Test
 public void testCreateArchiveOutputStreamStringOutputStream() throws Exception {
 String archiverName = "zip";
 OutputStream outputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
 Assert.assertEquals(ArchiveStreamFactory.ZIP, archiverName);
 Assert.assertNotNull(archiveOutputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamWrongArchiverName() throws Exception {
 String archiverName = "foobar";
 OutputStream outputStream = new ByteArrayOutputStream();
 new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamNullOutputStream() throws Exception {
 String archiverName = "zip";
 OutputStream outputStream = null;
 new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
 }

 // Test for createArchiveInputStream(InputStream)
 @Test
 public void testCreateArchiveInputStreamInputStream() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamInvalidSignatureInputStream() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
 }

 // Test for createArchiveOutputStream(OutputStream)
 @Test
 public void testCreateArchiveOutputStreamOutputStream() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("zip", outputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamWrongArchiverNameOutputStream() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 new ArchiveStreamFactory().createArchiveOutputStream("foobar", outputStream);
 }
}