import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

 @Before
 public void setUp() {
 }

 @After
 public void tearDown() {
 }

 @Test
 public void testCreateArchiveInputStreamWithArArchiverNameAndInputStream() {
 final String archiverName = ArchiveStreamFactory.AR;
 final byte[] arData = new byte[]{(byte) 0x78, (byte) 0x41, (byte) 0x78, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x0a, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x0b, (byte) 0x71, (byte) 0x75, (byte) 0x74, (byte) 0x65, (byte) 0x52, (byte) 0x6f, (byte) 0x71, (byte) 0x75, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x0a, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x0b, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x0d};
 final InputStream in = new ByteArrayInputStream(arData);
 ArchiveInputStream ais = null;
 try {
 ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, in);
 assertEquals("ar", ais.getClass().getName().substring(0, 3).toLowerCase());
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Exception while creating ArArchiveInputStream: " + e.getMessage());
 } finally {
 try {
 if (ais != null) {
 ais.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testCreateArchiveInputStreamWithArArchiverNameAndNullInputStream() {
 final String archiverName = ArchiveStreamFactory.AR;
 ArchiveInputStream ais = null;
 try {
 ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, null);
 assertNull("ArchiveInputStream should be null if input stream is null", ais);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testCreateArchiveInputStreamWithUnknownArchiverNameAndInputStream() {
 final String archiverName = "unknown";
 final InputStream in = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream ais = null;
 try {
 ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, in);
 assertNull("Unexpected ArchiveInputStream created for unknown archiver", ais);
 } catch (ArchiveException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWithArArchiverNameAndOutputStream() {
 final String archiverName = ArchiveStreamFactory.AR;
 final OutputStream out = new ByteArrayOutputStream();
 ArchiveOutputStream aos = null;
 try {
 aos = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, out);
 assertEquals("ar", aos.getClass().getName().substring(0, 3).toLowerCase());
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Exception while creating ArArchiveOutputStream: " + e.getMessage());
 } finally {
 try {
 if (aos != null) {
 aos.finish();
 aos.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWithArArchiverNameAndNullOutputStream() {
 final String archiverName = ArchiveStreamFactory.AR;
 ArchiveOutputStream aos = null;
 try {
 aos = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, null);
 assertNull("ArchiveOutputStream should be null if output stream is null", aos);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testCreateArchiveInputStreamWithUnknownArchiverNameAndInputStream() {
 final String archiverName = "unknown";
 final InputStream in = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream ais = null;
 try {
 ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, in);
 assertNull("Unexpected ArchiveInputStream created for unknown archiver", ais);
 } catch (ArchiveException e) {
 // Expected
 } finally {
 try {
 if (ais != null) {
 ais.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testCreateArchiveInputStreamWithInputStreamAndAutoDetection() {
 final String archiverName = null;
 final byte[] arData = new byte[]{(byte) 0x78, (byte) 0x41, (byte) 0x78, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x0a, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x0b, (byte) 0x71, (byte) 0x75, (byte) 0x74, (byte) 0x65, (byte) 0x52, (byte) 0x6f, (byte) 0x71, (byte) 0x75, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x0a, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x0b, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x0d};
 final InputStream in = new ByteArrayInputStream(arData);
 ArchiveInputStream ais = null;
 try {
 ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(in);
 assertEquals("ar", ais.getClass().getName().substring(0, 3).toLowerCase());
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Exception while creating ArchiveInputStream by auto-detection: " + e.getMessage());
 } finally {
 try {
 if (ais != null) {
 ais.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWithInputStreamAndAutoDetection() {
 final String archiverName = null;
 final OutputStream out = new ByteArrayOutputStream();
 ArchiveOutputStream aos = null;
 try {
 aos = ArchiveStreamFactory.getInstance().createArchiveOutputStream(out);
 fail("Unexpected ArchiveOutputStream created by auto-detection");
 } catch (ArchiveException e) {
 // Expected
 } finally {
 try {
 if (aos != null) {
 aos.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 final InputStream in = new ByteArrayInputStream(arData);
 ArchiveInputStream ais = null;
 try {
 ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(in);
 assertEquals("ar", ais.getClass().getName().substring(0, 3).toLowerCase());

 aos = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, out);
 assertEquals("ar", aos.getClass().getName().substring(0, 3).toLowerCase());
 aos.putNextEntry(new ArArchiveEntry("test.dat"));
 aos.write(arData, 0, arData.length);
 aos.finish();

 ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, new ByteArrayInputStream(out.toByteArray()));
 ArArchiveEntry ae = (ArArchiveEntry) ais.getNextEntry();
 assertEquals("test.dat", ae.getName());
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Exception while creating ArchiveOutputStream and ArchiveInputStream by auto-detection: " + e.getMessage());
 } finally {
 try {
 if (ais != null) {
 ais.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 try {
 if (aos != null) {
 aos.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }
}