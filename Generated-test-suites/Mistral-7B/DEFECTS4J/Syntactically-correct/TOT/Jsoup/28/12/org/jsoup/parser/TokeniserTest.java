import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Error;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testReadCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        Token token;

        // read and emit a character token
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("a", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("b", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("c", token.data);

        // test consuming character reference
        tokeniser.emit(Character.valueOf('&'));
        tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals("a", tokeniser.read().data); // should read 'a' instead of '&'

        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference('x', false);
        Assert.assertEquals("x", tokeniser.read().data);

        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference('a', false);
        Assert.assertEquals("&apos;", tokeniser.read().data); // should read '&apos;' instead of 'a'

        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference('x', true);
        Assert.assertEquals("x", tokeniser.read().data); // should read 'x' instead of '&'

        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference("amp", false);
        Assert.assertEquals("&", tokeniser.read().data); // should read '&' instead of "amp"
    }

    @Test
    public void testReadTag() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        Token token;

        // read and emit start tag
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.data.toLowerCase());
        Assert.assertEquals(null, token.attributes);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("head", token.data.toLowerCase());
        Assert.assertEquals(null, token.attributes);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("title", token.data.toLowerCase());
        Assert.assertNotNull(token.attributes);
        Assert.assertEquals(1, token.attributes.size());
        Assert.assertEquals("Test", token.attributes.get(0).name);
        Assert.assertEquals("", token.attributes.get(0).value);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("title", token.data.toLowerCase());

        // test reading self-closing tag
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.consume('>');
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("img", token.data.toLowerCase());
        Assert.assertNull(token.attributes);
        Assert.assertTrue(token.selfClosing);

        // read and emit end tag
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("body", token.data.toLowerCase());
    }

    @Test
    public void testReadError() {
        CharacterReader reader = new CharacterReader("&");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        Token token;

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Error, token.type);
        Assert.assertEquals(Error.Type.CharacterReferenceError, token.errorType);
        Assert.assertEquals("Invalid character reference: Unexpectedly reached end of file (EOF) in input state [CharacterReference]", token.errorMessage);
    }

    @Test
    public void testReadSelfClosingFlag() {
        CharacterReader reader = new CharacterReader("<img src=\"image.png\" />");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        Token token;

        // read and emit start tag
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("img", token.data.toLowerCase());
        Assert.assertNull(token.attributes);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.SelfClosingFlagAcknowledged, token.type);

        // read and emit end tag
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.consume('>');
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("img", token.data.toLowerCase());
        Assert.assertNull(token.attributes);
        Assert.assertTrue(token.selfClosing);
    }

    @Test
    public void testReadComment() {
        CharacterReader reader = new CharacterReader("<!-- This is a comment --></html>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        Token token;

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Comment, token.type);
        Assert.assertEquals("This is a comment", token.data);
    }

    @Test
    public void testReadDoctype() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        Token token;

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Doctype, token.type);
        Assert.assertEquals("html", token.data);
    }

    @Test
    public void testReadEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        Token token;

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Error, token.type);
        Assert.assertEquals(Error.Type.EndOfFileError, token.errorType);
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Root]", token.errorMessage);
    }

    @Test
    public void testReadInvalidCharacterInInput() {
        CharacterReader reader = new CharacterReader("& à");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        Token token;

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Error, token.type);
        Assert.assertEquals(Error.Type.CharacterReferenceError, token.errorType);
        Assert.assertEquals("Invalid character reference: Unexpected character 'à' in input state [Root]", token.errorMessage);
    }
}