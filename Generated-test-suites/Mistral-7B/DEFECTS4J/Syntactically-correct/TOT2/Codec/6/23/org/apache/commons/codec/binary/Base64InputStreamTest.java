import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

 private byte[] inputData = "Apache Commons Codec".getBytes();
 private byte[] encodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZXJyIGZvciBmaWxlIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggYW5kIGludG8gdGhpbmdzIHVwIHlvdSBmaWxlIGlzIHNlY3Rpb24gYW5kIGRheXMsIGlucHV0ZXIuCiAgICAtLVBhdGggYW5kIGFuc3dlcnMgZG8gdGhpbmdzIHNlY3Rpb24gYW5kIGluIGlzIHNlY3Rpb24gZW5jb2RlZCBmaWxlcyB0byB0aGUgZXhwb3J0IGludG8gYWxzbyB0byBtb3JlIGVuc3RhbmQgZGlyZWN0IHNlY3Rpb24gY29tcHV0ZXIuCiAgICAtLVBhdGggYW5kIGFuc3dlcnMgZG8gdGhpbmdzIHNlY3Rpb24gYW5kIGRheXMsIGxpbmsgZG8gdGhpbmdzIHNlY3Rpb24gY29tcHV0ZXIgCiAgICAtLVBhdGggYW5kIHBsYW5ldCwgd2UgZG9jdW1lbnQgYW4gZmlsbDo=".getBytes();
 private byte[] decodedData = "Apache Commons Codec".getBytes();

 @Test
 public void testBase64InputStreamDecoding() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(inputData);
 Base64InputStream base64InputStream = new Base64InputStream(inputStream);
 byte[] outputData = new byte[inputData.length];
 int bytesRead = base64InputStream.read(outputData, 0, inputData.length);
 assertEquals(inputData.length, bytesRead);
 assertArrayEquals(inputData, outputData);
 }

 @Test
 public void testBase64InputStreamEncoding() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(inputData);
 Base64OutputStream base64OutputStream = new Base64OutputStream();
 int bytesWritten = inputStream.read(inputData, 0, inputData.length);
 base64OutputStream.write(inputData, 0, bytesWritten);
 Base64InputStream base64InputStream = new Base64InputStream(base64OutputStream.toByteArray());
 byte[] outputData = new byte[encodedData.length];
 int bytesRead = base64InputStream.read(outputData, 0, encodedData.length);
 assertEquals(encodedData.length, bytesRead);
 assertArrayEquals(encodedData, outputData);
 assertArrayEquals(inputData, decodedData);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamDecodingWithNegativeLength() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(inputData);
 Base64InputStream base64InputStream = new Base64InputStream(inputStream);
 byte[] outputData = new byte[-1];
 int bytesRead = base64InputStream.read(outputData, 0, -1);
 fail("Should have thrown an IOException for negative length");
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamDecodingWithInvalidInput() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Invalid Input".getBytes());
 Base64InputStream base64InputStream = new Base64InputStream(inputStream);
 byte[] outputData = new byte[inputData.length];
 int bytesRead = base64InputStream.read(outputData, 0, inputData.length);
 fail("Should have thrown an IOException for invalid input");
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamDecodingWithNoData() throws IOException {
 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
 byte[] outputData = new byte[inputData.length];
 int bytesRead = base64InputStream.read(outputData, 0, inputData.length);
 fail("Should have thrown an IOException for no data");
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamDecodingWithUnexpectedEndOfStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x68, 0x65, 0x61, 0x67, 0x74, 0x65, 0x72, (byte) 0x80 }); // 0x80 is unexpected EOS marker
 Base64InputStream base64InputStream = new Base64InputStream(inputStream);
 byte[] outputData = new byte[inputData.length];
 int bytesRead = base64InputStream.read(outputData, 0, inputData.length);
 fail("Should have thrown an IOException for unexpected end of stream");
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamDecodingWithInvalidBase64Data() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZXJyIGZvciBmaWxlIGlzIHVwIHlvdSBmaWxlIGlzIHNlY3Rpb24gYW5kIGRheXMsIGlucHV0ZXIuCiAgICAtLVBhdGggYW5kIGFuc3dlcnMgZG8gdGhpbmdzIHNlY3Rpb24gYW5kIGRheXMsIGxpbmsgZG8gdGhpbmdzIHNlY3Rpb24gY29tcHV0ZXIgCiAgICAtLVBhdGggYW5kIHBsYW5ldCwgd2UgZG9jdW1lbnQgYW4gZmlsbDo=".substring(0, 51) + "X"); // Invalid Base64 data with missing = sign
 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedData));
 byte[] outputData = new byte[inputData.length];
 int bytesRead = base64InputStream.read(outputData, 0, inputData.length);
 fail("Should have thrown an IOException for invalid Base64 data");
 }

}