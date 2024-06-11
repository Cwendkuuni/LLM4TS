import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeBinary() throws DecoderException, EncoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A};
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws DecoderException, EncoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcdefghijklmno";
        String encoded = codec.encode(input, CharEncoding.US_ASCII.name());
        String decoded = codec.decode(encoded, CharEncoding.US_ASCII.name());

        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringDefaultCharset() throws DecoderException, EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "abcdefghijklmno";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);

        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeBinaryWithWhitespace() throws DecoderException, EncoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, 0x02, 0x20, 0x03, 0x04, 0x20, 0x05, 0x06, 0x20};
        byte[] encoded = codec.encode(input);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read;
        byte buffer[] = new byte[1024];

        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(codec.encode(buffer, 0, read));
        }

        byte[] output = outputStream.toByteArray();

        Assert.assertArrayEquals(encoded, output);
    }

    @Test
    public void testDecodeBinaryWithWhitespace() throws DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, '=', '3', '2', '=', '2', '0', '=', '2', '0'};
        byte[] decoded = codec.decode(input);

        Assert.assertArrayEquals(new byte[]{0x01, 0x20, 0x03, 0x20, 0x04, 0x20, 0x05}, decoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalid() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x01, '=', '3', 'B', '=', '2', '0', '=', '2', '0'};
        codec.decode(input);
    }
}