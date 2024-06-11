import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testReadBasic() {
        CharacterReader reader = new CharacterReader("abcdef"); // simple string input
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token;

        // Test reading a single character
        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Character, token.type);
        Validate.assertEquals('a', token.data.charAt(0));

        // Test reading a sequence of characters
        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Character, token.type);
        Validate.assertEquals('b', token.data.charAt(0));

        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Character, token.type);
        Validate.assertEquals('c', token.data.charAt(0));

        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Character, token.type);
        Validate.assertEquals('d', token.data.charAt(0));

        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Character, token.type);
        Validate.assertEquals('e', token.data.charAt(0));

        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Character, token.type);
        Validate.assertEquals('f', token.data.charAt(0));

        // Test end of input
        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Eof, token.type);
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img src=\"image.jpg\" />"); // self-closing tag input
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.StartTag, token.type);
        Validate.assertEquals("img", token.data.tagName);
        Validate.assertEquals(true, token.data.isSelfClosing);

        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Eof, token.type);
    }

    @Test
    public void testReadStartTagWithAttributes() {
        CharacterReader reader = new CharacterReader("<div id=\"container\" class=\"container\" style=\"color: red;\">"); // tag with attributes input
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.StartTag, token.type);
        Validate.assertEquals("div", token.data.tagName);
        Validate.assertNotNull(token.data.attributes);
        Validate.assertEquals("container", token.data.attributes.get("id").getValue());
        Validate.assertEquals("container", token.data.attributes.get("class").getValue());
        Validate.assertEquals("color: red;", token.data.attributes.get("style").getValue());

        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Character, token.type);
        Validate.assertNull(token.data.attributes);

        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Eof, token.type);
    }

    // Add more test cases for other methods and edge cases here
}