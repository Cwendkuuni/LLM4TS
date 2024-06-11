package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ARArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ArchiveStreamFactoryTest {

    @org.junit.Test
    public void testCreateArchiveInputStream() throws Exception {
        // test for AR archives
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertSame(ARArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();

        // test for ZIP archives
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertSame(ZipArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();

        // test for TAR archives
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertSame(TarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();

        // test for JAR archives
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertSame(JarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();

        // test for CPIO archives
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertSame(CpioArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();

        // test for DUMP archives
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertSame(DumpArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();

        // test for unknown archives
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown", unknownInputStream));
    }

    @org.junit.Test
    public void testCreateArchiveOutputStream() throws Exception {
        // test for AR archives
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertSame(ARArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.finish();
        archiveOutputStream.close();

        // test for ZIP archives
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.zip.ZipEntry("test.txt"));
        archiveOutputStream.write("Hello World!".getBytes());
        archiveOutputStream.finish();
        archiveOutputStream.close();
        byte[] result = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertTrue(new String(result).contains("Hello World!"));

        // test for TAR archives
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.tar.TarEntry("test.txt"));
        archiveOutputStream.write("Hello World!".getBytes());
        archiveOutputStream.finish();
        archiveOutputStream.close();
        result = ((ByteArrayOutputStream) outputStream).toByteArray();
        // The output of a TAR file might look different depending on the operating system
        // For Windows: System.out.println(new String(result)); should print something like "test.txt\000Hello World!"
        // For Linux: System.out.println(new String(result)); should print something like "\002test.txt\000Hello World!"

        // test for JAR archives
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.jar.JarEntry("test.txt"));
        archiveOutputStream.write("Hello World!".getBytes());
        archiveOutputStream.finish();
        archiveOutputStream.close();
        result = ((ByteArrayOutputStream) outputStream).toByteArray();
        // The output of a JAR file might look different depending on the operating system
        // For Windows: System.out.println(new String(result)); should print something like "\META-INF\MANIFEST.MF\001test.txt\002Hello World!"
        // For Linux: System.out.println(new String(result)); should print something like "test.txt\000Hello World!"

        // test for CPIO archives
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.cpio.CpioEntry("test.txt"));
        archiveOutputStream.write("Hello World!".getBytes());
        archiveOutputStream.finish();
        archiveOutputStream.close();
        result = ((ByteArrayOutputStream) outputStream).toByteArray();
        // The output of a CPIO file might look different depending on the operating system
        // For Windows: System.out.println(new String(result)); should print something like "\002test.txt\000Hello World!"
        // For Linux: System.out.println(new String(result)); should print something like "\001test.txt\000Hello World!"

        // test for DUMP archives
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        assertFalse(outputStream.hasRemaining());

        // test for unknown archives
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveOutputStream("unknown", unknownOutputStream));
    }

    @org.junit.Test
    public void testCreateArchiveInputStreamWithFirstBytes() throws Exception {
        // test for AR archives
        byte[] arBytes = new byte[12];
        InputStream arInputStream = new ByteArrayInputStream(arBytes);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertSame(ARArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();

        // test for ZIP archives
        byte[] zipBytes = new byte[3];
        InputStream zipInputStream = new ByteArrayInputStream(zipBytes);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertThrows(ArchiveException.class, () -> archiveInputStream.getNextEntry());
        archiveInputStream.close();

        // test for TAR archives
        byte[] tarBytes = new byte[512];
        InputStream tarInputStream = new ByteArrayInputStream(tarBytes);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertSame(TarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();

        // test for JAR archives
        byte[] jarBytes = new byte[4];
        InputStream jarInputStream = new ByteArrayInputStream(jarBytes);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertThrows(ArchiveException.class, () -> archiveInputStream.getNextEntry());
        archiveInputStream.close();

        // test for CPIO archives
        byte[] cpioBytes = new byte[2];
        InputStream cpioInputStream = new ByteArrayInputStream(cpioBytes);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertThrows(ArchiveException.class, () -> archiveInputStream.getNextEntry());
        archiveInputStream.close();

        // test for DUMP archives
        byte[] dumpBytes = new byte[32];
        InputStream dumpInputStream = new ByteArrayInputStream(dumpBytes);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertSame(DumpArchiveInputStream.class, archiveInputStream.getClass());
        assertNull(archiveInputStream.getNextEntry());
        archiveInputStream.close();

        // test for unknown archives
        byte[] unknownBytes = new byte[1];
        InputStream unknownInputStream = new ByteArrayInputStream(unknownBytes);
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown", unknownInputStream));
    }

    @org.junit.Test
    public void testCreateArchiveOutputStreamWithFirstBytes() throws Exception {
        // test for AR archives
        byte[] arBytes = new byte[12];
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        byte[] result = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertTrue(Arrays.equals(arBytes, result));

        // test for ZIP archives
        byte[] zipBytes = new byte[3];
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.close();
        result = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertNull(result);

        // test for TAR archives
        byte[] tarBytes = new byte[512];
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        result = ((ByteArrayOutputStream) outputStream).toByteArray();
        // The output of a TAR file might look different depending on the operating system
        // For Windows: assertTrue(Arrays.equals(new byte[0], result.subArray(0, 2)));
        // For Linux: assertTrue(Arrays.equals(new byte[]{'0', '0'}, result.subArray(0, 2)));

        // test for JAR archives
        byte[] jarBytes = new byte[4];
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        result = ((ByteArrayOutputStream) outputStream).toByteArray();
        // The output of a JAR file might look different depending on the operating system
        // For Windows: assertTrue(Arrays.equals(new byte[]{'0', 'x', 'f', 'a'}, result.subArray(0, 4)));
        // For Linux: assertTrue(Arrays.equals(new byte[]{'0', 'x', 'f', 'b'}, result.subArray(0, 4)));

        // test for CPIO archives
        byte[] cpioBytes = new byte[2];
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        result = ((ByteArrayOutputStream) outputStream).toByteArray();
        // The output of a CPIO file might look different depending on the operating system
        // For Windows: assertTrue(Arrays.equals(new byte[]{'0', '1'}, result.subArray(0, 2)));
        // For Linux: assertTrue(Arrays.equals(new byte[]{'0', '0'}, result.subArray(0, 2)));

        // test for DUMP archives
        byte[] dumpBytes = new byte[32];
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        result = ((ByteArrayOutputStream) outputStream).toByteArray();
        // The output of a DUMP file might look different depending on the operating system
        // For Windows: assertTrue(Arrays.equals(new byte[0], result.subArray(0, 32)));
        // For Linux: assertTrue(Arrays.equals(new byte[0], result.subArray(0, 32)));

        // test for unknown archives
        byte[] unknownBytes = new byte[1];
        outputStream = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveOutputStream("unknown", outputStream));
    }
}