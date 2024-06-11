package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testGetToken() {
        String expectedToken = "sampleToken";
        Token token = new Token(expectedToken, "secret");
        String actualToken = token.getToken();
        assertEquals(expectedToken, actualToken);
    }
    
    @Test
    public void testGetSecret() {
        String expectedSecret = "sampleSecret";
        Token token = new Token("token", expectedSecret);
        String actualSecret = token.getSecret();
        assertEquals(expectedSecret, actualSecret);
    }
    
    @Test
    public void testGetRawResponse() {
        String expectedRawResponse = "sampleRawResponse";
        Token token = new Token("token", "secret", expectedRawResponse);
        String actualRawResponse = token.getRawResponse();
        assertEquals(expectedRawResponse, actualRawResponse);
    }
    
    @Test
    public void testTokenConstructorWithTwoArguments() {
        String expectedToken = "sampleToken";
        String expectedSecret = "sampleSecret";
        Token token = new Token(expectedToken, expectedSecret);
        String actualToken = token.getToken();
        String actualSecret = token.getSecret();
        assertEquals(expectedToken, actualToken);
        assertEquals(expectedSecret, actualSecret);
    }
    
    @Test
    public void testTokenConstructorWithThreeArguments() {
        String expectedRawResponse = "sampleRawResponse";
        String expectedToken = "sampleToken";
        String expectedSecret = "sampleSecret";
        Token token = new Token(expectedToken, expectedSecret, expectedRawResponse);
        String actualToken = token.getToken();
        String actualSecret = token.getSecret();
        String actualRawResponse = token.getRawResponse();
        assertEquals(expectedToken, actualToken);
        assertEquals(expectedSecret, actualSecret);
        assertEquals(expectedRawResponse, actualRawResponse);
    }
    
    @Test
    public void testIllegalStateException() {
        String token = "sampleToken";
        String secret = "sampleSecret";
        Token tokenWithoutRawResponse = new Token(token, secret);
        assertThrows(IllegalStateException.class, () -> tokenWithoutRawResponse.getRawResponse());
    }
    
    @Test
    public void testToString() {
        String expectedToken = "sampleToken";
        String expectedSecret = "sampleSecret";
        Token token = new Token(expectedToken, expectedSecret);
        String actualTokenString = token.toString();
        String expectedTokenString = String.format("Token[%s , %s]", expectedToken, expectedSecret);
        assertEquals(expectedTokenString, actualTokenString);
    }
    
    @Test
    public void testHashCode() {
        String token1 = "token1";
        String token2 = "token2";
        Token token1Obj = new Token(token1, "secret");
        Token token2Obj = new Token(token2, "secret");
        int expectedHashCode1 = token1.hashCode();
        int expectedHashCode2 = token2.hashCode();
        int actualHashCode1 = token1Obj.hashCode();
        int actualHashCode2 = token2Obj.hashCode();
        assertEquals(expectedHashCode1, actualHashCode1);
        assertEquals(expectedHashCode2, actualHashCode2);
    }
    
    @Test
    public void testEquals() {
        String token1 = "token1";
        String token2 = "token2";
        String secret = "secret";
        Token token1Obj = new Token(token1, secret);
        Token token2Obj = new Token(token2, secret);
        boolean expectedResult = false;
        boolean actualResult = token1Obj.equals(token2Obj);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testEqualsWithSameObject() {
        String token = "token";
        String secret = "secret";
        Token tokenObj = new Token(token, secret);
        boolean expectedResult = true;
        boolean actualResult = tokenObj.equals(tokenObj);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        String token = "token";
        String secret = "secret";
        Token tokenObj = new Token(token, secret);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(tokenObj);
        byte[] expectedBytes = byteArrayOutputStream.toByteArray();
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(expectedBytes));
        Token deserializedToken = (Token) objectInputStream.readObject();
        String actualToken = deserializedToken.getToken();
        String actualSecret = deserializedToken.getSecret();
        assertEquals(token, actualToken);
        assertEquals(secret, actualSecret);
    }
}