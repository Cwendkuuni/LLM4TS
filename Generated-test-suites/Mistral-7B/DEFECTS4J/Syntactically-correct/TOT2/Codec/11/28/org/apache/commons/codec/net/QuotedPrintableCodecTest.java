import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

/**
 * QuotedPrintableCodecTest.java
 */
public class QuotedPrintableCodecTest {

    private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeText() throws IOException, UnsupportedEncodingException {
        String input = "Hello World!";
        byte[] encodedBytes = codec.encode(input.getBytes("UTF-8"));
        String decodedString = new String(codec.decode(encodedBytes), "UTF-8");
        Assert.assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeDecodeBinary() throws IOException, UnsupportedEncodingException {
        byte[] input = {0x68, 0x65, 0x6c, 0x6c, 0x6f};
        byte[] encodedBytes = codec.encode(input);
        byte[] decodedBytes = codec.decode(encodedBytes);
        Assert.assertArrayEquals(input, decodedBytes);
    }

    @Test
    public void testEncodeWhitespace() throws IOException, UnsupportedEncodingException {
        String input = "Hello\tWorld!\n";
        byte[] encodedBytes = codec.encode(input.getBytes("UTF-8"));
        String decodedString = new String(codec.decode(encodedBytes), "UTF-8");
        Assert.assertEquals(input, decodedString);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidData() throws IOException, UnsupportedEncodingException {
        byte[] invalidData = new byte[]{'A', 'B', 'C', '='};
        codec.decode(invalidData);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNullData() {
        codec.encode(null);
    }

    @Test
    public void testEncodeDecodeEdgeCases() throws IOException, UnsupportedEncodingException {
        String edgeCase1 = "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u0008\u000B\u000E\u000F";
        byte[] encodedBytes1 = codec.encode(edgeCase1.getBytes("UTF-8"));
        String decodedString1 = new String(codec.decode(encodedBytes1), "UTF-8");
        Assert.assertEquals(edgeCase1, decodedString1);

        String edgeCase2 = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        byte[] encodedBytes2 = codec.encode(edgeCase2.getBytes("UTF-8"));
        String decodedString2 = new String(codec.decode(encodedBytes2), "UTF-8");
        Assert.assertEquals(edgeCase2, decodedString2);

        String edgeCase3 = "\u0080\u00FF";
        byte[] encodedBytes3 = codec.encode(edgeCase3.getBytes("UTF-8"));
        String decodedString3 = new String(codec.decode(encodedBytes3), "UTF-8");
        Assert.assertEquals(edgeCase3, decodedString3);

        String edgeCase4 = "\uD800\uDC00";
        byte[] encodedBytes4 = codec.encode(edgeCase4.getBytes("UTF-16LE"));
        String decodedString4 = new String(codec.decode(encodedBytes4), "UTF-16LE");
        Assert.assertEquals(edgeCase4, decodedString4);
    }
}