import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // test encoding and decoding of a single byte
        byte input = 0x61;
        byte[] encoded = codec.encode(new byte[]{input});
        byte decoded = codec.decode(encoded)[0];
        Assert.assertEquals(input, decoded);

        // test encoding and decoding of multiple bytes
        byte[] inputBytes = new byte[]{0x61, 0x62, 0x63, 0x64};
        byte[] encodedBytes = codec.encode(inputBytes);
        byte[] decodedBytes = codec.decode(encodedBytes);
        Assert.assertArrayEquals(inputBytes, decodedBytes);

        // test encoding and decoding of a string using default charset
        String inputString = "abcdef";
        String encodedString = codec.encode(inputString);
        String decodedString = codec.decode(encodedString);
        Assert.assertEquals(inputString, decodedString);

        // test encoding and decoding of a string using specific charset
        String charset = "UTF-8";
        String inputStringUtf8 = "abcdef";
        byte[] inputBytesUtf8 = inputStringUtf8.getBytes(charset);
        String encodedStringUtf8 = codec.encode(inputBytesUtf8, charset);
        byte[] decodedBytesUtf8 = codec.decode(encodedStringUtf8, charset);
        String decodedStringUtf8 = new String(decodedBytesUtf8, charset);
        Assert.assertEquals(inputStringUtf8, decodedStringUtf8);
    }

    @Test(expected = DecoderException.class)
    public void testInvalidEncoding() throws IOException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] inputBytes = new byte[]{0x61, 0x62, 0x63, 0x64, 0x65}; // one extra byte
        byte[] encodedBytes = codec.encode(inputBytes);
        codec.decode(encodedBytes); // should throw an exception
    }
}