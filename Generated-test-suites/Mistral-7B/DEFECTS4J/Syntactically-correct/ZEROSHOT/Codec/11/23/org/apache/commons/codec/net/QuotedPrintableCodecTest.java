import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class QuotedPrintableCodecTest {

    private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeByteArray() throws EncoderException, DecoderException {
        byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x31, 0x30, 0x31, 0x20, 0x42, 0x6F, 0x6F, 0x6B, 0x65, 0x6E, 0x2E};
        byte[] output = codec.encode(input);
        byte[] decodedOutput = codec.decode(output);

        Assert.assertArrayEquals(input, decodedOutput);
    }

    @Test
    public void testEncodeDecodeString() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String input = "Hello World!10 test.binary";
        String encodedOutput = codec.encode(input, StandardCharsets.UTF_8.name());
        String decodedOutput = codec.decode(encodedOutput, StandardCharsets.UTF_8.name());

        Assert.assertEquals(input, decodedOutput);
    }

    @Test(expected = EncoderException.class)
    public void testFailEncodeByteArray() throws EncoderException {
        byte[] input = {(byte) 0x80};
        codec.encode(input);
    }

    @Test(expected = DecoderException.class)
    public void testFailDecodeByteArray() throws DecoderException {
        byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x31, 0x30, 0x31, 0x20, 0x42, 0x6F, 0x6F, 0x6B, 0x65, 0x6E, 0x2E, 0x31};
        codec.decode(input);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testFailEncodeStringWithUnsupportedCharset() throws UnsupportedEncodingException {
        codec.encode("test", "InvalidCharset");
    }
}