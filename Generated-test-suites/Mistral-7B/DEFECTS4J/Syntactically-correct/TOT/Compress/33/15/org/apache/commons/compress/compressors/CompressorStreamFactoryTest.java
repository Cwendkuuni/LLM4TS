package org.apache.commons.compress.compressors.test;

import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreams;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link CompressorStreamFactory}
 */
public class CompressorStreamFactoryTest {

    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[0]);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * Test creation of compressor output stream for various compressors.
     */
    @Test
    public void testCreateCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test creation of compressor output stream for GZIP
        CompressorOutputStream gzipOutput = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        assertEquals("GZIP CompressorOutput Stream", gzipOutput.getClass().getName());
        gzipOutput.close();

        // Test creation of compressor output stream for BZIP2
        CompressorOutputStream bzip2Output = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        assertEquals("BZIP2 CompressorOutput Stream", bzip2Output.getClass().getName());
        bzip2Output.close();

        // Test creation of compressor output stream for XZ
        CompressorOutputStream xzOutput = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, outputStream);
        assertEquals("XZ CompressorOutput Stream", xzOutput.getClass().getName());
        xzOutput.close();

        // Test creation of compressor output stream for LZMA
        CompressorOutputStream lzmaOutput = factory.createCompressorOutputStream(CompressorStreamFactory.LZMA, outputStream);
        assertEquals("LZMA CompressorOutput Stream", lzmaOutput.getClass().getName());
        lzmaOutput.close();

        // Test creation of compressor output stream for Deflate
        CompressorOutputStream deflateOutput = factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, outputStream);
        assertEquals("Deflate CompressorOutput Stream", deflateOutput.getClass().getName());
        deflateOutput.close();
    }

    /**
     * Test creation of compressor input stream for various compressors.
     */
    @Test
    public void testCreateCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test creation of compressor input stream for GZIP
        InputStream gzipInput = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
        assertTrue("GZIP CompressorInput Stream", gzipInput instanceof GzipCompressorInputStream);
        gzipInput.close();

        // Test creation of compressor input stream for BZIP2
        InputStream bzip2Input = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        assertTrue("BZIP2 CompressorInput Stream", bzip2Input instanceof BZip2CompressorInputStream);
        bzip2Input.close();

        // Test creation of compressor input stream for XZ
        InputStream xzInput = factory.createCompressorInputStream(CompressorStreamFactory.XZ, inputStream);
        assertTrue("XZ CompressorInput Stream", xzInput instanceof XZCompressorInputStream);
        xzInput.close();

        // Test creation of compressor input stream for LZMA
        InputStream lzmaInput = factory.createCompressorInputStream(CompressorStreamFactory.LZMA, inputStream);
        assertTrue("LZMA CompressorInput Stream", lzmaInput instanceof LZMACompressorInputStream);
        lzmaInput.close();

        // Test creation of compressor input stream for Deflate
        InputStream deflateInput = factory.createCompressorInputStream(CompressorStreamFactory.DEFLATE, inputStream);
        assertTrue("Deflate CompressorInput Stream", deflateInput instanceof DeflateCompressorInputStream);
        deflateInput.close();

        // Test creation of compressor input stream for unknown compressor
        expectedException.expect(CompressorException.class);
        expectedException.expectMessage("Compressor: Unknown not found.");
        factory.createCompressorInputStream("Unknown", inputStream);
    }

    /**
     * Test setting the decompress concatenated flag.
     */
    @Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue("Decompress Concatenated is set to true by default", factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(false);
        assertFalse("Decompress Concatenated is set to false when explicitly set", factory.getDecompressConcatenated());
    }

    /**
     * Test creation of compressor input stream with custom decompress flags.
     */
    @Test
    public void testCreateCompressorInputStreamWithCustomDecompressFlags() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);

        // Test creation of compressor input stream for GZIP with decompressUntilEOF set to true
        InputStream gzipInput = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
        gzipInput.close();

        // Test creation of compressor input stream for GZIP with decompressUntilEOF set to false
        factory = new CompressorStreamFactory(false);
        InputStream gzipInputFalse = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
        gzipInputFalse.close();

        // Test creation of compressor input stream for BZIP2 with decompressUntilEOF set to true
        InputStream bzip2Input = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        bzip2Input.close();

        // Test creation of compressor input stream for BZIP2 with decompressUntilEOF set to false
        factory = new CompressorStreamFactory(false);
        InputStream bzip2InputFalse = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        bzip2InputFalse.close();

        // Test creation of compressor input stream for XZ with decompressUntilEOF set to true
        InputStream xzInput = factory.createCompressorInputStream(CompressorStreamFactory.XZ, inputStream);
        xzInput.close();

        // Test creation of compressor input stream for XZ with decompressUntilEOF set to false
        factory = new CompressorStreamFactory(false);
        InputStream xzInputFalse = factory.createCompressorInputStream(CompressorStreamFactory.XZ, inputStream);
        xzInputFalse.close();
    }

    /**
     * Test creation of compressor output stream with custom decompress flags.
     */
    @Test
    public void testCreateCompressorOutputStreamWithCustomDecompressFlags() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        // Test creation of compressor output stream for GZIP with decompressUntilEOF set to true
        CompressorOutputStream gzipOutputTrue = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        gzipOutputTrue.finish();
        gzipOutputTrue.close();

        // Test creation of compressor output stream for GZIP with decompressUntilEOF set to false
        factory = new CompressorStreamFactory(false);
        CompressorOutputStream gzipOutputFalse = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        gzipOutputFalse.finish();
        gzipOutputFalse.close();

        // Test creation of compressor output stream for BZIP2 with decompressUntilEOF set to true
        CompressorOutputStream bzip2OutputTrue = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        bzip2OutputTrue.finish();
        bzip2OutputTrue.close();

        // Test creation of compressor output stream for BZIP2 with decompressUntilEOF set to false
        factory = new CompressorStreamFactory(false);
        CompressorOutputStream bzip2OutputFalse = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        bzip2OutputFalse.finish();
        bzip2OutputFalse.close();

        // Test creation of compressor output stream for XZ with decompressUntilEOF set to true
        CompressorOutputStream xzOutputTrue = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, outputStream);
        xzOutputTrue.finish();
        xzOutputTrue.close();

        // Test creation of compressor output stream for XZ with decompressUntilEOF set to false
        factory = new CompressorStreamFactory(false);
        CompressorOutputStream xzOutputFalse = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, outputStream);
        xzOutputFalse.finish();
        xzOutputFalse.close();

        // Test creation of compressor output stream for DEFLATE with decompressUntilEOF set to true
        CompressorOutputStream deflateOutputTrue = factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, outputStream);
        deflateOutputTrue.finish();
        deflateOutputTrue.close();

        // Test creation of compressor output stream for DEFLATE with decompressUntilEOF set to false
        factory = new CompressorStreamFactory(false);
        CompressorOutputStream deflateOutputFalse = factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, outputStream);
        deflateOutputFalse.finish();
        deflateOutputFalse.close();

        // Test creation of compressor output stream for unknown compressor with decompressUntilEOF set to true
        expectedException.expect(CompressorException.class);
        expectedException.expectMessage("Compressor: Unknown not found.");
        factory.createCompressorOutputStream("Unknown", outputStream);

        // Test creation of compressor output stream for unknown compressor with decompressUntilEOF set to false
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot override the setting defined by the constructor");
        factory = new CompressorStreamFactory(true);
        factory.setDecompressConcatenated(false);
        factory.createCompressorOutputStream("Unknown", outputStream);
    }

    /**
     * Test creation of compressor output stream and input stream for various compressors.
     */
    @Test
    public void testCreateCompressorStreams() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test creation of compressor output and input stream for GZIP
        OutputStream gzipOutput = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, new ByteArrayOutputStream());
        InputStream gzipInput = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, new ByteArrayInputStream(new byte[0]));
        InputStream input = new ByteArrayInputStream(new byte[0]);
        CompressorStreams.streamsCopy(input, gzipOutput, 1024);
        gzipOutput.finish();
        gzipOutput.close();
        gzipInput.close();

        // Test creation of compressor output and input stream for BZIP2
        OutputStream bzip2Output = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, new ByteArrayOutputStream());
        InputStream bzip2Input = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, new ByteArrayInputStream(new byte[0]));
        InputStream input2 = new ByteArrayInputStream(new byte[0]);
        CompressorStreams.streamsCopy(input2, bzip2Output, 1024);
        bzip2Output.finish();
        bzip2Output.close();
        bzip2Input.close();

        // Test creation of compressor output and input stream for XZ
        OutputStream xzOutput = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, new ByteArrayOutputStream());
        InputStream xzInput = factory.createCompressorInputStream(CompressorStreamFactory.XZ, new ByteArrayInputStream(new byte[0]));
        InputStream input3 = new ByteArrayInputStream(new byte[0]);
        CompressorStreams.streamsCopy(input3, xzOutput, 1024);
        xzOutput.finish();
        xzOutput.close();
        xzInput.close();

        // Test creation of compressor output and input stream for LZMA
        OutputStream lzmaOutput = factory.createCompressorOutputStream(CompressorStreamFactory.LZMA, new ByteArrayOutputStream());
        InputStream lzmaInput = factory.createCompressorInputStream(CompressorStreamFactory.LZMA, new ByteArrayInputStream(new byte[0]));
        InputStream input4 = new ByteArrayInputStream(new byte[0]);
        CompressorStreams.streamsCopy(input4, lzmaOutput, 1024);
        lzmaOutput.finish();
        lzmaOutput.close();
        lzmaInput.close();

        // Test creation of compressor output and input stream for Deflate
        OutputStream deflateOutput = factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, new ByteArrayOutputStream());
        InputStream deflateInput = factory.createCompressorInputStream(CompressorStreamFactory.DEFLATE, new ByteArrayInputStream(new byte[0]));
        InputStream input5 = new ByteArrayInputStream(new byte[0]);
        CompressorStreams.streamsCopy(input5, deflateOutput, 1024);
        deflateOutput.finish();
        deflateOutput.close();
        deflateInput.close();

        // Test creation of compressor output and input stream for unknown compressor
        expectedException.expect(CompressorException.class);
        expectedException.expectMessage("Compressor: Unknown not found.");
        factory = new CompressorStreamFactory();
        OutputStream unknownOutput = factory.createCompressorOutputStream("Unknown", new ByteArrayOutputStream());
        InputStream unknownInput = factory.createCompressorInputStream("Unknown", new ByteArrayInputStream(new byte[0]));
        CompressorStreams.streamsCopy(new ByteArrayInputStream(new byte[0]), unknownOutput, 1024);
        unknownOutput.finish();
        unknownOutput.close();
        unknownInput.close();
    }
}