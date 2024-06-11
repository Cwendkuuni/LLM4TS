import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 private InputStream input;
 private OutputStream output;
 private ArchiveStreamFactory factory;

 @Before
 public void setUp() throws IOException {
 input = new ByteArrayInputStream("test data".getBytes());
 output = new ByteArrayOutputStream();
 factory = new ArchiveStreamFactory();
 }

 @After
 public void tearDown() throws IOException {
 IOUtils.closeQuietly(input);
 IOUtils.closeQuietly(output);
 }

 @Test
 public void testCreateArchiveInputStreamWithAR() throws IOException, ArchiveException {
 ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.AR, input);
 assertTrue(in instanceof ArArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamWithAR_Failure() throws IOException, ArchiveException {
 try {
 factory.createArchiveInputStream("unknown", input);
 fail("Should not reach here");
 } catch (ArchiveException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveInputStreamWithTar() throws IOException, ArchiveException {
 byte[] tarHeader = new byte[512];
 input.read(tarHeader);
 ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input);
 assertTrue(in instanceof TarArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamWithInvalidTar() throws IOException, ArchiveException {
 byte[] tarHeader = new byte[499];
 input.read(tarHeader);
 ArchiveInputStream in = null;
 try {
 in = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input);
 fail("Should not reach here");
 } catch (ArchiveException e) {
 // Expected
 } finally {
 IOUtils.closeQuietly(in);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWithAR() throws IOException, ArchiveException {
 ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, output);
 assertTrue(out instanceof ArArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamWithInvalidAR() throws IOException, ArchiveException {
 try {
 factory.createArchiveOutputStream("unknown", output);
 fail("Should not reach here");
 } catch (ArchiveException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWithTar() throws IOException, ArchiveException {
 ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
 assertTrue(out instanceof TarArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamWithZip() throws IOException, ArchiveException {
 ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
 assertTrue(out instanceof ZipArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamWithInvalidZip() throws IOException, ArchiveException {
 try {
 factory.createArchiveOutputStream("unknown", output);
 fail("Should not reach here");
 } catch (ArchiveException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWriteEntry() throws IOException, ArchiveException {
 ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 out.putArchiveEntry(entry);
 out.write(new byte[] { 0x61, 0x62, 0x63 }, 0, 3);
 out.closeArchiveEntry();
 out.close();
 assertEquals("test\n", new String(output.toByteArray()));
 }

 @Test
 public void testCreateArchiveInputStreamReadEntry() throws IOException, ArchiveException {
 ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(output.toByteArray()));
 ArchiveEntry entry = in.getNextEntry();
 assertEquals("test.txt", entry.getName());
 InputStream input2 = in.getInputStream();
 byte[] data = new byte[3];
 input2.read(data);
 assertEquals("test", new String(data));
 in.close();
 }

 @Test(expected = IOException.class)
 public void testCreateArchiveInputStreamReadEntryError() throws IOException, ArchiveException {
 ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(output.toByteArray()));
 ArchiveEntry entry = in.getNextEntry();
 InputStream input2 = in.getInputStream();
 byte[] data = new byte[3];
 input2.read(data);
 fail("Should throw an exception as there is no data after the entry in the test data");
 in.close();
 }
}