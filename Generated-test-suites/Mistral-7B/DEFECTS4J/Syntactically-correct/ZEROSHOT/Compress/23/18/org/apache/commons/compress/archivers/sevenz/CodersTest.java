package org.apache.commons.compress.archivers.sevenz;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.SecretKeySpec;

import static org.apache.commons.compress.archivers.sevenz.Coders.addDecoder;
import static org.apache.commons.compress.archivers.sevenz.Coders.addEncoder;
import static org.apache.commons.compress.archivers.sevenz.Coders.CoderId.AES256SHA256;
import static org.apache.commons.compress.archivers.sevenz.Coders.CoderId.BZIP2;
import static org.apache.commons.compress.archivers.sevenz.Coders.CoderId.COPY;
import static org.apache.commons.compress.archivers.sevenz.Coders.CoderId.DEFLATE;
import static org.apache.commons.compress.archivers.sevenz.Coders.CoderId.LZMA;
import static org.apache.commons.compress.archivers.sevenz.Coders.CoderId.LZMA2;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException, NoSuchAlgorithmException {
        InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
        Coder testDecoder = mock(Coder.class);
        byte[] testPassword = new byte[0];

        InputStream result = addDecoder(testInputStream, DEFLATE, testPassword);
        assertEquals(DeflateDecoder.class, result.getClass());

        result = addDecoder(testInputStream, LZMA, testPassword);
        assertEquals(LZMADecoder.class, result.getClass());

        result = addDecoder(testInputStream, BZIP2, testPassword);
        assertEquals(BZIP2Decoder.class, result.getClass());

        result = addDecoder(testInputStream, AES256SHA256, testPassword);
        assertEquals(AES256SHA256Decoder.class, result.getClass());

        result = addDecoder(testInputStream, COPY, testPassword);
        assertEquals(CopyDecoder.class, result.getClass());

        result = addDecoder(testInputStream, new SevenZMethod("UnknownMethod"), testPassword);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testAddDecoderUnsupportedCompressionMethod() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
        Coder testDecoder = mock(Coder.class);
        byte[] testPassword = new byte[0];

        addDecoder(testInputStream, new SevenZMethod("UnknownMethod"), testPassword);
    }

    @Test
    public void testAddEncoder() throws IOException, NoSuchAlgorithmException {
        OutputStream testOutputStream = new ByteArrayOutputStream();
        SevenZMethod testMethod = DEFLATE;
        byte[] testPassword = new byte[0];

        OutputStream result = addEncoder(testOutputStream, testMethod, testPassword);
        assertEquals(DeflaterOutputStream.class, result.getClass());

        result = addEncoder(testOutputStream, LZMA, testPassword);
        assertEquals(LZMAInputStream.class, result.getClass()); // LZMA encoder is not implemented in this class

        result = addEncoder(testOutputStream, BZIP2, testPassword);
        assertEquals(BZip2CompressorOutputStream.class, result.getClass());

        result = addEncoder(testOutputStream, AES256SHA256, testPassword);
        assertEquals(CipherOutputStream.class, result.getClass()); // AES256SHA256 encoder is not implemented in this class

        result = addEncoder(testOutputStream, COPY, testPassword);
        assertEquals(FilterOutputStream.class, result.getClass());

        result = addEncoder(testOutputStream, new SevenZMethod("UnknownMethod"), testPassword);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testAddEncoderUnsupportedCompressionMethod() throws IOException {
        OutputStream testOutputStream = new ByteArrayOutputStream();
        SevenZMethod testMethod = new SevenZMethod("UnknownMethod");
        byte[] testPassword = new byte[0];

        addEncoder(testOutputStream, testMethod, testPassword);
    }
}

class SevenZMethod {
    private final String id;

    SevenZMethod(String id) {
        this.id = id;
    }

    String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SevenZMethod) {
            SevenZMethod other = (SevenZMethod) obj;
            return this.id.equals(other.id);
        }
        return false;
    }
}