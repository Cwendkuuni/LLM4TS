package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.compressors.BaseInputStream;
import org.apache.commons.compress.compressors.compressors.BaseOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAUtils;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory.CompressorType;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    private CompressorStreamFactory factory;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        factory = new CompressorStreamFactory();
        inputStream = new ByteArrayInputStream(new byte[0]);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testCreateCompressorInputStreamWithUnsupportedName() {
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorInputStream("UnsupportedName", inputStream);
        });
    }

    @Test
    public void testCreateCompressorInputStreamGZIP() throws Exception {
        CompressorInputStream gzipInputStream = factory.createCompressorInputStream(CompressorType.GZIP, inputStream);
        assertSame(GzipCompressorInputStream.class, gzipInputStream.getClass());

        InputStream testInputStream = new MockGzipInputStream(inputStream);
        CompressorInputStream gzipInputStreamFromName = factory.createCompressorInputStream("gzip", testInputStream);
        assertSame(gzipInputStreamFromName.getClass(), gzipInputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStreamBZIP2() throws Exception {
        CompressorInputStream bzip2InputStream = factory.createCompressorInputStream(CompressorType.BZIP2, inputStream);
        assertSame(BZip2CompressorInputStream.class, bzip2InputStream.getClass());

        InputStream testInputStream = new MockBzip2InputStream(inputStream);
        CompressorInputStream bzip2InputStreamFromName = factory.createCompressorInputStream("bzip2", testInputStream);
        assertSame(bzip2InputStreamFromName.getClass(), bzip2InputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStreamXZ() throws Exception {
        CompressorInputStream xzInputStream = factory.createCompressorInputStream(CompressorType.XZ, inputStream);
        assertSame(XZCompressorInputStream.class, xzInputStream.getClass());

        InputStream testInputStream = new MockXZInputStream(inputStream);
        CompressorInputStream xzInputStreamFromName = factory.createCompressorInputStream("xz", testInputStream);
        assertSame(xzInputStreamFromName.getClass(), xzInputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStreamLZMA() throws Exception {
        CompressorInputStream lzmaInputStream = factory.createCompressorInputStream(CompressorType.LZMA, inputStream);
        assertSame(LZMACompressorInputStream.class, lzmaInputStream.getClass());

        InputStream testInputStream = new MockLZMAInputStream(inputStream);
        CompressorInputStream lzmaInputStreamFromName = factory.createCompressorInputStream("lzma", testInputStream);
        assertSame(lzmaInputStreamFromName.getClass(), lzmaInputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStreamDeflate() throws Exception {
        CompressorInputStream deflateInputStream = factory.createCompressorInputStream(CompressorType.DEFLATE, inputStream);
        assertSame(DeflateCompressorInputStream.class, deflateInputStream.getClass());

        InputStream testInputStream = new MockDeflateInputStream(inputStream);
        CompressorInputStream deflateInputStreamFromName = factory.createCompressorInputStream("deflate", testInputStream);
        assertSame(deflateInputStreamFromName.getClass(), deflateInputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStreamSNAPPY_FRAMED() throws Exception {
        CompressorInputStream snappyFramedInputStream = factory.createCompressorInputStream(CompressorType.SNAPPY_FRAMED, inputStream);
        assertSame(FramedSnappyCompressorInputStream.class, snappyFramedInputStream.getClass());

        InputStream testInputStream = new MockSnappyFramedInputStream(inputStream);
        CompressorInputStream snappyFramedInputStreamFromName = factory.createCompressorInputStream("snappy-framed", testInputStream);
        assertSame(snappyFramedInputStreamFromName.getClass(), snappyFramedInputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStreamSNAPPY_RAW() throws Exception {
        CompressorInputStream snappyRawInputStream = factory.createCompressorInputStream(CompressorType.SNAPPY_RAW, inputStream);
        assertSame(SnappyCompressorInputStream.class, snappyRawInputStream.getClass());

        InputStream testInputStream = new MockSnappyRawInputStream(inputStream);
        CompressorInputStream snappyRawInputStreamFromName = factory.createCompressorInputStream("snappy-raw", testInputStream);
        assertSame(snappyRawInputStreamFromName.getClass(), snappyRawInputStream.getClass());
    }

    @Test
    public void testCreateCompressorOutputStreamWithUnsupportedName() {
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorOutputStream("UnsupportedName", outputStream);
        });
    }

    @Test
    public void testCreateCompressorOutputStreamGZIP() throws Exception {
        CompressorOutputStream gzipOutputStream = factory.createCompressorOutputStream(CompressorType.GZIP, outputStream);
        assertSame(GzipCompressorOutputStream.class, gzipOutputStream.getClass());

        OutputStream testOutputStream = new MockGzipOutputStream(outputStream);
        CompressorOutputStream gzipOutputStreamFromName = factory.createCompressorOutputStream("gzip", testOutputStream);
        assertSame(gzipOutputStreamFromName.getClass(), gzipOutputStream.getClass());
    }

    @Test
    public void testCreateCompressorOutputStreamBZIP2() throws Exception {
        CompressorOutputStream bzip2OutputStream = factory.createCompressorOutputStream(CompressorType.BZIP2, outputStream);
        assertSame(BZip2CompressorOutputStream.class, bzip2OutputStream.getClass());

        OutputStream testOutputStream = new MockBzip2OutputStream(outputStream);
        CompressorOutputStream bzip2OutputStreamFromName = factory.createCompressorOutputStream("bzip2", testOutputStream);
        assertSame(bzip2OutputStreamFromName.getClass(), bzip2OutputStream.getClass());
    }

    @Test
    public void testCreateCompressorOutputStreamXZ() throws Exception {
        CompressorOutputStream xzOutputStream = factory.createCompressorOutputStream(CompressorType.XZ, outputStream);
        assertSame(XZCompressorOutputStream.class, xzOutputStream.getClass());

        OutputStream testOutputStream = new MockXZOutputStream(outputStream);
        CompressorOutputStream xzOutputStreamFromName = factory.createCompressorOutputStream("xz", testOutputStream);
        assertSame(xzOutputStreamFromName.getClass(), xzOutputStream.getClass());
    }

    @Test
    public void testCreateCompressorOutputStreamLZMA() throws Exception {
        CompressorOutputStream lzmaOutputStream = factory.createCompressorOutputStream(CompressorType.LZMA, outputStream);
        assertSame(LZMACompressorOutputStream.class, lzmaOutputStream.getClass());

        OutputStream testOutputStream = new MockLZMAOutputStream(outputStream);
        CompressorOutputStream lzmaOutputStreamFromName = factory.createCompressorOutputStream("lzma", testOutputStream);
        assertSame(lzmaOutputStreamFromName.getClass(), lzmaOutputStream.getClass());
    }

    @Test
    public void testCreateCompressorOutputStreamDEFLATE() throws Exception {
        CompressorOutputStream deflateOutputStream = factory.createCompressorOutputStream(CompressorType.DEFLATE, outputStream);
        assertSame(DeflateCompressorOutputStream.class, deflateOutputStream.getClass());

        OutputStream testOutputStream = new MockDeflateOutputStream(outputStream);
        CompressorOutputStream deflateOutputStreamFromName = factory.createCompressorOutputStream("deflate", testOutputStream);
        assertSame(deflateOutputStreamFromName.getClass(), deflateOutputStream.getClass());
    }

    @Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertFalse(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(false);
        assertFalse(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    public static class MockGzipInputStream extends BaseInputStream implements CompressorInputStream {
        public MockGzipInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }

    public static class MockBzip2InputStream extends BaseInputStream implements CompressorInputStream {
        public MockBzip2InputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }

    public static class MockXZInputStream extends BaseInputStream implements CompressorInputStream {
        public MockXZInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }

    public static class MockLZMAInputStream extends BaseInputStream implements CompressorInputStream {
        public MockLZMAInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }

    public static class MockDeflateInputStream extends BaseInputStream implements CompressorInputStream {
        public MockDeflateInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }

    public static class MockSnappyFramedInputStream extends BaseInputStream implements CompressorInputStream {
        public MockSnappyFramedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }

    public static class MockSnappyRawInputStream extends BaseInputStream implements CompressorInputStream {
        public MockSnappyRawInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }

    public static class MockGzipOutputStream extends BaseOutputStream implements CompressorOutputStream {
        public MockGzipOutputStream(OutputStream outputStream) {
            super(outputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }

    public static class MockBzip2OutputStream extends BaseOutputStream implements CompressorOutputStream {
        public MockBzip2OutputStream(OutputStream outputStream) {
            super(outputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }

    public static class MockXZOutputStream extends BaseOutputStream implements CompressorOutputStream {
        public MockXZOutputStream(OutputStream outputStream) {
            super(outputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }

    public static class MockLZMAOutputStream extends BaseOutputStream implements CompressorOutputStream {
        public MockLZMAOutputStream(OutputStream outputStream) {
            super(outputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }

    public static class MockDeflateOutputStream extends BaseOutputStream implements CompressorOutputStream {
        public MockDeflateOutputStream(OutputStream outputStream) {
            super(outputStream);
        }

        @Override
        public void close() throws IOException {
        }
    }
}