import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testReadEmptyInput() {
        CharacterReader reader = new StringReader("");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadSingleCharacter() {
        CharacterReader reader = new StringReader("a");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals("a", token.asCharacter().toString());
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadCharacterReference() {
        CharacterReader reader = new StringReader("&#123;");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('\u007B', token.asCharacter().charValue());
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadCharacterReferenceWithAllowedAdditionalCharacter() {
        CharacterReader reader = new StringReader("&#x123;");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals('\u007B', token.asCharacter().charValue());
        Assert.assertEquals(0, errors.size());
    }

    @Test(expected = AssertionError.class)
    public void testReadCharacterReferenceWithInvalidCharacter() {
        CharacterReader reader = new StringReader("&#x12F000;");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        Assert.assertEquals(1, errors.size());
        Assert.assertNotNull(errors.get(0).getMessage().contains("character outside of valid range"));
    }

    @Test
    public void testReadCharacterReferenceWithMissingSemicolon() {
        CharacterReader reader = new StringReader("&#123");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        Assert.assertEquals(1, errors.size());
        Assert.assertNotNull(errors.get(0).getMessage().contains("missing semicolon"));
    }

    @Test
    public void testReadCharacterReferenceWithInvalidNamedReference() {
        CharacterReader reader = new StringReader("&#InvalidName;");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        Assert.assertEquals(1, errors.size());
        Assert.assertNotNull(errors.get(0).getMessage().contains("invalid named reference"));
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new StringReader("<img src=\"image.jpg\" />");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = tokeniser.read();
        Token endTag = tokeniser.read();

        Assert.assertEquals(TokenType.StartTag, startTag.type);
        Assert.assertEquals("img", startTag.asStartTag().tagName);
        Assert.assertTrue(startTag.asStartTag().selfClosing);
        Assert.assertEquals(TokenType.EndTag, endTag.type);
        Assert.assertEquals("img", endTag.asEndTag().tagName);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadMixedContent() {
        CharacterReader reader = new StringReader("<p>Hello <b>world</b></p>");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = null;
        while ((token = tokeniser.read()) != null) {
            switch (token.type) {
                case StartTag:
                    Assert.assertEquals("p", token.asStartTag().tagName);
                    break;
                case Character:
                    Assert.assertEquals(" ", token.asCharacter().toString().charAt(0));
                    break;
                case EndTag:
                    Assert.assertEquals("p", token.asEndTag().tagName);
                    break;
                case StartTag:
                    Assert.assertEquals("b", token.asStartTag().tagName);
                    break;
                case Character:
                    Assert.assertEquals("w", token.asCharacter().toString().charAt(0));
                    break;
                case Character:
                    Assert.assertEquals("o", token.asCharacter().toString().charAt(0));
                    break;
                case Character:
                    Assert.assertEquals("l", token.asCharacter().toString().charAt(0));
                    break;
                case Character:
                    Assert.assertEquals("d", token.asCharacter().toString().charAt(0));
                    break;
                case EndTag:
                    Assert.assertEquals("b", token.asEndTag().tagName);
                    break;
            }
        }

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadInvalidInput() {
        CharacterReader reader = new StringReader("<script>Hello World</script>"); // intentionally missing semicolon at the end of script tag
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = null;
        while ((token = tokeniser.read()) != null) {
            switch (token.type) {
                case StartTag:
                    Assert.assertEquals("script", token.asStartTag().tagName);
                    break;
                case Character:
                    // read characters until we reach first error
                    break;
                case ParseError:
                    Assert.assertEquals(1, errors.size());
                    Assert.assertNotNull(errors.get(0).getMessage().contains("Unexpected character 's' in input state [Data]"));
                    return;
                default:
                    Assert.fail("Unexpected token: " + token.type);
            }
        }

        Assert.assertEquals(1, errors.size());
        Assert.assertNotNull(errors.get(0).getMessage().contains("Unexpected character 's' in input state [Data]"));
    }

    @Test
    public void testReadEOF() {
        CharacterReader reader = new StringReader("<html>");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = null;
        while ((token = tokeniser.read()) != null) {
            // read all tokens from input
        }

        tokeniser.eofError(TokeniserState.Data);

        Assert.assertEquals(1, errors.size());
        Assert.assertNotNull(errors.get(0).getMessage().contains("Unexpectedly reached end of file (EOF) in input state [Data]"));
    }

    @Test
    public void testReadUnexpectedCharacter() {
        CharacterReader reader = new StringReader("<html>x");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.error(TokeniserState.Data);

        Assert.assertEquals(1, errors.size());
        Assert.assertNotNull(errors.get(0).getMessage().contains("Unexpected character 'x' in input state [Data]"));
    }
}