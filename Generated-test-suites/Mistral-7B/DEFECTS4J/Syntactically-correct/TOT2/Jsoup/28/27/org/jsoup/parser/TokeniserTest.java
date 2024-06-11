package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest { // Test file name: TokeniserTest.java

    @Test
    public void testReadCharacter() {
        // Given: Reader with a character
        CharacterReader reader = new CharacterReader("a");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());

        // When: Read a character from the tokeniser
        Token token = tokeniser.read();

        // Then: Verify the character is read correctly
        Assert.assertEquals(new Token.Character("a"), token);
    }

    @Test
    public void testReadStartTag() {
        // Given: Reader with a start tag
        CharacterReader reader = new CharacterReader("<div>");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());

        // When: Read a token from the tokeniser
        Token token = tokeniser.read();

        // Then: Verify the start tag is read correctly
        Assert.assertEquals(new Token.StartTag("div"), token);
    }

    @Test
    public void testReadEndTag() {
        // Given: Reader with an end tag
        CharacterReader reader = new CharacterReader("</div>");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());

        // When: Read a token from the tokeniser
        Token token = tokeniser.read();

        // Then: Verify the end tag is read correctly
        Assert.assertEquals(new Token.EndTag("div"), token);
    }

    @Test
    public void testReadString() {
        // Given: Reader with a string
        CharacterReader reader = new CharacterReader("Hello World");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());

        // When: Read a token from the tokeniser
        Token token = tokeniser.read();

        // Then: Verify the string is read correctly
        Assert.assertEquals(new Token.CharacterSequence("Hello World"), token);
    }

    @Test
    public void testReadSelfClosingTag() {
        // Given: Reader with a self-closing tag
        CharacterReader reader = new CharacterReader("<img src=\"image.png\" />");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());

        // When: Read a token from the tokeniser
        Token token = tokeniser.read();

        // Then: Verify the self-closing tag is read correctly
        Assert.assertEquals(new Token.StartTag("img", "src=\"image.png\"", true), token);
    }

    @Test
    public void testErrorHandling() {
        // Given: Reader with invalid input
        CharacterReader reader = new CharacterReader("<script>alert('Hello')<");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());

        // When: Attempt to read a token from the tokeniser
        Token token = tokeniser.read();

        // Then: Verify an error is thrown
        Assert.assertNull(token);
        Assert.assertEquals(1, tokeniser.errors.size());
    }

    // ... Add test cases for other methods and edge cases here

    
    // Test all methods in the Tokeniser class
    public void testAllMethods() {
        // ... Test all methods in the Tokeniser class here
    }

    
}