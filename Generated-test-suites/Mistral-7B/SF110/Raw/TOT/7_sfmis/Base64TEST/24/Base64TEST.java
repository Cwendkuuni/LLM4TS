import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

private static final String VALID_BASE64_STRING = "RG9jdW1lbnRzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggZm9yIG5vdCBjb250ZW50IGdyYW5kIHJlc3BvbnNlIHBsYXlzIG9mIGlzIGdyZWVuIG9mIHJlc3BvbnNlIG9mIHNlcnZlZCArIEl0J3MgZXhhbXBsZSArIGRhdGFbMF0gLSBQYXVsIFIuCiAgICAtdGltZSArIGRhdGFbMQ==";
private static final String INVALID_BASE64_STRING_TOO_SHORT = "RG9jdW1lbnRzIGlz";
private static final String INVALID_BASE64_STRING_TOO_LONG = "RG9jdW1lbnRzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggZm9yIG5vdCBjb250ZW50IGdyYW5kIHJlc3BvbnNlIHBsYXlzIG9mIGlzIGdyZWVuIG9mIHJlc3BvbnNlIG9mIHNlcnZlZCArIEl0J3MgZXhhbXBsZSArIGRhdGFbMF0gLSBQYXVsIFIuCiAgICAtdGltZSArIGRhdGFbMQ==ABCDEFG";
private static final String INVALID_BASE64_STRING_INVALID_CHAR = "RG9jdW1lbnRzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggZm9yIG5vdCBjb250ZW50IGdyYW5kIHJlc3BvbnNlIHBsYXlzIG9mIGlzIGdyZWVuIG9mIHJlc3BvbnNlIG9mIHNlcnZlZCArIEl0J3MgZXhhbXBsZSArIGRhdGFbMF0gLSBQYXVsIFIuCiAgICAtdGltZSArIGRhdGFbMQ==!";

@Test
public void testAltBase64ToByteArray() {
String input = VALID_BASE64_STRING;
byte[] expectedOutput = "_$23180".substring(1).getBytes();
byte[] output = Base64.altBase64ToByteArray(input);
Assert.assertArrayEquals(expectedOutput, output);
}

@Test(expected = IllegalArgumentException.class)
public void testAltBase64ToByteArrayStringTooShort() {
String input = INVALID_BASE64_STRING_TOO_SHORT;
Base64.altBase64ToByteArray(input);
}

@Test(expected = IllegalArgumentException.class)
public void testAltBase64ToByteArrayStringTooLong() {
String input = INVALID_BASE64_STRING_TOO_LONG;
Base64.altBase64ToByteArray(input);
}

@Test(expected = IllegalArgumentException.class)
public void testAltBase64ToByteArrayStringInvalidChar() {
String input = INVALID_BASE64_STRING_INVALID_CHAR;
Base64.altBase64ToByteArray(input);
}

@Test
public void testBase64ToByteArray() {
String input = VALID_BASE64_STRING;
byte[] expectedOutput = "_$23180".substring(1).getBytes();
byte[] output = Base64.byteArrayToBase64(expectedOutput);
byte[] byteOutput = Base64.base64ToByteArray(output);
Assert.assertArrayEquals(expectedOutput, byteOutput);
}

@Test
public void testByteArrayToAltBase64() {
byte[] input = "_$23180".substring(1).getBytes();
String expectedOutput = VALID_BASE64_STRING;
String output = Base64.byteArrayToAltBase64(input);
Assert.assertEquals(expectedOutput, output);
}

@Test
public void testByteArrayToBase64() {
byte[] input = "_$23180".substring(1).getBytes();
String expectedOutput = VALID_BASE64_STRING;
String output = Base64.byteArrayToBase64(input);
Assert.assertEquals(expectedOutput, output);
}
}