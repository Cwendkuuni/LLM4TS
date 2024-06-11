package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    @org.junit.Test
    public void testCreateCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test uncompressed stream
        InputStream in = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream cis = factory.createCompressorInputStream(null, in);
        assertNull(cis);

        // Test GZip
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(bos);
        gcos.write(new byte[0]);
        gcos.close();
        InputStream gis = new ByteArrayInputStream(bos.toByteArray());
        CompressorInputStream cgis = factory.createCompressorInputStream(GZIP, gis);
        assertEquals(GzipCompressorInputStream.class, cgis.getClass());

        // Test BZip2
        byte[] bz2Input = XZUtils.createBZip2TestFile();
        InputStream bis = new ByteArrayInputStream(bz2Input);
        CompressorInputStream cbis = factory.createCompressorInputStream(BZIP2, bis);
        assertEquals(BZip2CompressorInputStream.class, cbis.getClass());

        // Test XZ
        byte[] xzInput = XZUtils.createXZTestFile();
        InputStream xis = new ByteArrayInputStream(xzInput);
        CompressorInputStream cxis = factory.createCompressorInputStream(XZ, xis);
        assertEquals(XZCompressorInputStream.class, cxis.getClass());

        // Test unknown algorithm
        InputStream unknown = new ByteArrayInputStream(new byte[0]);
        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream("unknown", unknown));
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test uncompressed stream
        OutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream cos = factory.createCompressorOutputStream(null, out);
        assertNull(cos);

        // Test GZip
        OutputStream gos = new ByteArrayOutputStream();
        CompressorOutputStream cgos = factory.createCompressorOutputStream(GZIP, gos);
        assertEquals(GzipCompressorOutputStream.class, cgos.getClass());

        // Test BZip2
        OutputStream bos = new ByteArrayOutputStream();
        CompressorOutputStream cbos = factory.createCompressorOutputStream(BZIP2, bos);
        assertEquals(BZip2CompressorOutputStream.class, cbos.getClass());

        // Test XZ
        OutputStream xos = new ByteArrayOutputStream();
        CompressorOutputStream cxos = factory.createCompressorOutputStream(XZ, xos);
        assertEquals(XZCompressorOutputStream.class, cxos.getClass());

        // Test unknown algorithm
        OutputStream ou = new ByteArrayOutputStream();
        assertThrows(CompressorException.class, () -> factory.createCompressorOutputStream("unknown", ou));

        // Test invalid compressor name
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorOutputStream("", null));
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorOutputStream("null", null));
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_WithDecompressConcatenated() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);

        // Test GZip
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(bos);
        gcos.write(new byte[0]);
        gcos.finish();
        gcos.close();
        InputStream gis = new ByteArrayInputStream(bos.toByteArray());
        CompressorInputStream cgis = factory.createCompressorInputStream(GZIP, gis);
        assertEquals(GzipCompressorInputStream.class, cgis.getClass());
        assertTrue(cgis.isDecompressConcatenated());

        // Test BZip2
        byte[] bz2Input = XZUtils.createBZip2TestFile();
        InputStream bis = new ByteArrayInputStream(bz2Input);
        CompressorInputStream cbis = factory.createCompressorInputStream(BZIP2, bis);
        assertEquals(BZip2CompressorInputStream.class, cbis.getClass());
        assertTrue(cbis.isDecompressConcatenated());

        // Test XZ
        byte[] xzInput = XZUtils.createXZTestFile();
        InputStream xis = new ByteArrayInputStream(xzInput);
        CompressorInputStream cxis = factory.createCompressorInputStream(XZ, xis);
        assertEquals(XZCompressorInputStream.class, cxis.getClass());
        assertTrue(cxis.isDecompressConcatenated());

        // Test unknown algorithm
        InputStream unknown = new ByteArrayInputStream(new byte[0]);
        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream("unknown", unknown));

        // Test invalid compressor name
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorInputStream("", null));
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorInputStream("null", null));
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_DecompressUntilEOF() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);

        // Test GZip
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(bos);
        gcos.write(new byte[0]);
        gcos.finish();
        gcos.close();
        InputStream gis = new ByteArrayInputStream(bos.toByteArray());
        CompressorInputStream cgis = factory.createCompressorInputStream(GZIP, gis);
        assertEquals(GzipCompressorInputStream.class, cgis.getClass());
        assertTrue(cgis.isDecompressConcatenated());
        assertFalse(cgis.isDecompressUntilEOF());

        // Test BZip2
        byte[] bz2Input = XZUtils.createBZip2TestFile();
        InputStream bis = new ByteArrayInputStream(bz2Input);
        CompressorInputStream cbis = factory.createCompressorInputStream(BZIP2, bis);
        assertEquals(BZip2CompressorInputStream.class, cbis.getClass());
        assertTrue(cbis.isDecompressConcatenated());
        assertFalse(cbis.isDecompressUntilEOF());

        // Test XZ
        byte[] xzInput = XZUtils.createXZTestFile();
        InputStream xis = new ByteArrayInputStream(xzInput);
        CompressorInputStream cxis = factory.createCompressorInputStream(XZ, xis);
        assertEquals(XZCompressorInputStream.class, cxis.getClass());
        assertTrue(cxis.isDecompressConcatenated());
        assertFalse(cxis.isDecompressUntilEOF());

        // Test unknown algorithm
        InputStream unknown = new ByteArrayInputStream(new byte[0]);
        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream("unknown", unknown));

        // Test invalid compressor name
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorInputStream("", null));
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorInputStream("null", null));
    }

    @org.junit.Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factoryWithConcatenated = new CompressorStreamFactory(true);
        assertTrue(factoryWithConcatenated.getDecompressConcatenated());
    }
}