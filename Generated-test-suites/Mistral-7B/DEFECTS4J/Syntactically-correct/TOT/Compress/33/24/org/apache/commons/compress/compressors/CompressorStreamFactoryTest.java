package org.apache.commons.compress.compressors.test;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test the CompressorStreamFactory class
 */
public class CompressorStreamFactoryTest {

    private CompressorStreamFactory factory;
    private InputStream input;
    private OutputStream output;

    @Before
    public void setUp() throws Exception {
        factory = new CompressorStreamFactory();
        input = new ByteArrayInputStream(new byte[0]);
        output = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testCreateCompressorInputStream_nullInputStream() {
        try {
            factory.createCompressorInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_autodetect() throws IOException, CompressorException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write("MZ\u0000\u0004".getBytes());
        InputStream is = new ByteArrayInputStream(bos.toByteArray());

        CompressorInputStream in = factory.createCompressorInputStream(is);

        assertSame(XZCompressorInputStream.class, in.getClass());
        assertTrue(in.isClosed());

        is.close();
    }

    @Test
    public void testCreateCompressorInputStream_autodetect_unknown() throws IOException, CompressorException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write("dummy".getBytes());
        InputStream is = new ByteArrayInputStream(bos.toByteArray());

        try {
            factory.createCompressorInputStream(is);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // expected
        }

        is.close();
    }

    @Test
    public void testCreateCompressorInputStream_byName_known() throws IOException, CompressorException {
        InputStream is = new ByteArrayInputStream("GZip signature".getBytes());

        CompressorInputStream in = factory.createCompressorInputStream("GZIP", is);

        assertSame(GzipCompressorInputStream.class, in.getClass());
        assertFalse(in.isClosed());
    }

    @Test
    public void testCreateCompressorInputStream_byName_unknown() throws IOException, CompressorException {
        InputStream is = new ByteArrayInputStream("dummy signature".getBytes());

        try {
            factory.createCompressorInputStream("unknown", is);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // expected
        }

        is.close();
    }

    @Test
    public void testCreateCompressorOutputStream_nullOutputStream() {
        try {
            factory.createCompressorOutputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_byName_known() throws IOException, CompressorException {
        OutputStream os = new ByteArrayOutputStream();

        CompressorOutputStream out = factory.createCompressorOutputStream("GZIP", os);

        assertSame(GzipCompressorOutputStream.class, out.getClass());
        assertTrue(out.isClosed());

        os.close();
    }

    @Test
    public void testCreateCompressorOutputStream_byName_unknown() throws IOException, CompressorException {
        OutputStream os = new ByteArrayOutputStream();

        try {
            factory.createCompressorOutputStream("unknown", os);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // expected
        }

        os.close();
    }

    @Test
    public void testCreateCompressorOutputStream_autodetect() throws IOException, CompressorException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write("PK\u0007\u0001\u0014\u0000\u0004".getBytes());
        OutputStream os = new ByteArrayOutputStream(10);
        InputStream is = new ByteArrayInputStream(bos.toByteArray());
        CompressorInputStream in = factory.createCompressorInputStream(is);

        try {
            CompressorOutputStream out = factory.createCompressorOutputStream(is);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // expected
        } finally {
            in.close();
            is.close();
        }

        assertSame(XZCompressorOutputStream.class, factory.createCompressorOutputStream(os).getClass());
        assertTrue(factory.createCompressorOutputStream(os).isClosed());

        os.close();
    }

    @Test
    public void testFactorySettings() {
        CompressorStreamFactory factory1 = new CompressorStreamFactory();
        CompressorStreamFactory factory2 = new CompressorStreamFactory(true);

        assertFalse(factory1.getDecompressConcatenated());
        assertTrue(factory2.getDecompressConcatenated());
    }
}