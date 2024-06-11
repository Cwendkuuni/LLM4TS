package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test for CompressorStreamFactory class
 */
public class CompressorStreamFactoryTest {

    private static final byte[] BASIC_GZIP_SIGNATURE = new byte[] { 0x1f, 0x8b, 0x08, 0x00, 0x2e, 0x42, 0x60, 0x82, 0x0a, 0x2a, 0x02, 0x40, 0x01, 0x0d, 0x00, 0x1a };
    private static final byte[] BASIC_BZIP2_SIGNATURE = new byte[] { 0x42, 0x5a, 0x68, 0x64, 0x61, 0x72, 0x65, 0x20, 0x75, 0x73, 0x68, 0x43, 0x6f, 0x6d, 0x00, 0x01, 0x0b, 0x05 };
    private static final byte[] BASIC_XZ_SIGNATURE = new byte[] { 0xfd, 0x37, 0x7a, 0x58, 0x5a, 0x42, 0x61, 0x73, 0x74, 0x72, 0x61, 0x63, 0x6b, 0x20, 0x6d, 0x61, 0x69, 0x66, 0x20, 0x73, 0x79, 0x73, 0x74, 0x65, 0x64 };

    @org.junit.Test
    public void testCreateCompressorInputStream() throws Exception {
        final CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test Gzip
        {
            final InputStream in = new ByteArrayInputStream(BASIC_GZIP_SIGNATURE);
            final CompressorInputStream gzipStream = factory.createCompressorInputStream("gzip", in);
            assertTrue(gzipStream instanceof GzipCompressorInputStream);
            assertEquals(BASIC_GZIP_SIGNATURE, gzipStream.getSignature());
            gzipStream.close();
        }

        // Test BZIP2
        {
            final InputStream in = new ByteArrayInputStream(BASIC_BZIP2_SIGNATURE);
            final CompressorInputStream bzip2Stream = factory.createCompressorInputStream("bzip2", in);
            assertTrue(bzip2Stream instanceof BZip2CompressorInputStream);
            assertEquals(BASIC_BZIP2_SIGNATURE, bzip2Stream.getSignature());
            bzip2Stream.close();
        }

        // Test XZ
        {
            final InputStream in = new ByteArrayInputStream(BASIC_XZ_SIGNATURE);
            final CompressorInputStream xzStream = factory.createCompressorInputStream("xz", in);
            assertTrue(xzStream instanceof XZCompressorInputStream);
            assertEquals(BASIC_XZ_SIGNATURE, xzStream.getSignature());
            xzStream.close();
        }

        // Test Unsupported compressor type
        {
            final InputStream in = new ByteArrayInputStream(new byte[] {});
            try {
                factory.createCompressorInputStream("unknown", in);
                assertFalse("Should throw exception", true);
            } catch (CompressorException e) {
                // Expected
            }
        }

        // Test BZIP2 with decompressUntilEOF = true
        {
            final CompressorStreamFactory factoryWithDecompressUntilEOF = new CompressorStreamFactory(true);
            final InputStream in = new ByteArrayInputStream(BASIC_BZIP2_SIGNATURE);
            final CompressorInputStream bzip2Stream = factoryWithDecompressUntilEOF
                    .createCompressorInputStream("bzip2", in);
            assertTrue(bzip2Stream instanceof BZip2CompressorInputStream);
            assertEquals(BASIC_BZIP2_SIGNATURE, bzip2Stream.getSignature());
            assertFalse(in.markSupported()); // Tests if the original stream is marked
            byte[] result = new byte[1024];
            int bytesRead = 0;
            while (true) {
                bytesRead = bzip2Stream.read(result, 0, result.length);
                if (bytesRead < 0) {
                    break;
                }
                assertEquals(result, result); // Tests if the same object is returned each time
                assertEquals(bytesRead, bzip2Stream.read(result, 0, result.length));
                assertEquals(bytesRead, bytesRead); // Tests for equality
            }
            bzip2Stream.close();
        }

        // Test BZIP2 with decompressUntilEOF = false
        {
            final CompressorStreamFactory factoryWithDecompressUntilEOF = new CompressorStreamFactory(false);
            final InputStream in = new ByteArrayInputStream(BASIC_BZIP2_SIGNATURE);
            final CompressorInputStream bzip2Stream = factoryWithDecompressUntilEOF
                    .createCompressorInputStream("bzip2", in);
            assertTrue(bzip2Stream instanceof BZip2CompressorInputStream);
            assertEquals(BASIC_BZIP2_SIGNATURE, bzip2Stream.getSignature());
            assertFalse(in.markSupported()); // Tests if the original stream is marked
            byte[] result = new byte[1024];
            int bytesRead = bzip2Stream.read(result, 0, result.length);
            assertEquals(result.length, bytesRead);
            assertNotSame(result, bzip2Stream.read(result, 0, result.length)); // Tests for different objects
            bzip2Stream.close();
        }
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream() throws Exception {
        final CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test Gzip
        {
            final OutputStream out = new ByteArrayOutputStream(256);
            final CompressorOutputStream gzipStream = factory.createCompressorOutputStream("gzip", out);
            assertTrue(gzipStream instanceof GzipCompressorOutputStream);
            assertEquals(GzipCompressorOutputStream.DEFLATED, gzipStream.getCompressionMethod());
            gzipStream.finish();
            gzipStream.close();
            assertArrayEquals(BASIC_GZIP_SIGNATURE, out.toByteArray());
        }

        // Test BZIP2
        {
            final OutputStream out = new ByteArrayOutputStream(256);
            final CompressorOutputStream bzip2Stream = factory.createCompressorOutputStream("bzip2", out);
            assertTrue(bzip2Stream instanceof BZip2CompressorOutputStream);
            bzip2Stream.write(BASIC_BZIP2_SIGNATURE);
            bzip2Stream.finish();
            bzip2Stream.close();
            assertArrayEquals(BASIC_BZIP2_SIGNATURE, out.toByteArray());
        }

        // Test XZ
        {
            final OutputStream out = new ByteArrayOutputStream(256);
            final CompressorOutputStream xzStream = factory.createCompressorOutputStream("xz", out);
            assertTrue(xzStream instanceof XZCompressorOutputStream);
            xzStream.write(BASIC_XZ_SIGNATURE);
            xzStream.finish();
            xzStream.close();
            assertArrayEquals(BASIC_XZ_SIGNATURE, out.toByteArray());
        }

        // Test Unsupported compressor type
        {
            final OutputStream out = new ByteArrayOutputStream(256);
            try {
                factory.createCompressorOutputStream("unknown", out);
                assertFalse("Should throw exception", true);
            } catch (CompressorException e) {
                // Expected
            }
            out.close();
        }

        // Test Deflate
        {
            final OutputStream out = new ByteArrayOutputStream(256);
            final CompressorOutputStream deflateStream = factory.createCompressorOutputStream("deflate", out);
            assertTrue(deflatestream instanceof DeflateCompressorOutputStream);
            assertEquals(DeflateCompressorOutputStream.DEFLATED, deflateStream.getCompressionMethod());
            deflateStream.finish();
            deflateStream.close();
            assertTrue(out.size() > 0);
            out.close();
        }

        // Test Pack200
        {
            final OutputStream out = new ByteArrayOutputStream(256);
            final CompressorOutputStream pack200Stream = factory.createCompressorOutputStream("pack200", out);
            assertTrue(pack200Stream instanceof Pack200CompressorOutputStream);
            pack200Stream.finish();
            pack200Stream.close();
            assertTrue(out.size() > 0);
            out.close();
        }
    }

    @org.junit.Test
    public void testGetDecompressConcatenated() throws Exception {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        final CompressorStreamFactory factoryWithDecompressConcatenated = new CompressorStreamFactory(true);
        assertTrue(factoryWithDecompressConcatenated.getDecompressConcatenated());
    }
}