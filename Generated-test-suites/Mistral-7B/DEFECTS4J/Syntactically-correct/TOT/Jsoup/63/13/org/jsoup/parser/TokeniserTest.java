import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        List<Token> tokens = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            token = tokeniser.read();
            if (token != null) {
                tokens.add(token);
            } else {
                break;
            }
        }

        Assert.assertEquals(0, tokens.size());
        Assert.assertFalse(tokeniser.isEmitPending());
        Assert.assertFalse(tokeniser.getState().isData());
        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
    }

    @Test
    public void testSimpleData() {
        CharacterReader reader = new CharacterReader("Hello World");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        List<Token> tokens = new ArrayList<>();

        token = tokeniser.read();
        tokens.add(token);

        token = tokeniser.read();
        tokens.add(token);

        Assert.assertEquals(2, tokens.size());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(0).type);
        Assert.assertEquals('H', tokens.get(0).data.charAt(0));
        Assert.assertEquals(Token.TokenType.Character, tokens.get(1).type);
        Assert.assertEquals('e', tokens.get(1).data.charAt(0));
        Assert.assertFalse(tokeniser.isEmitPending());
        Assert.assertTrue(tokeniser.getState().isData());
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testDataWithTag() {
        CharacterReader reader = new CharacterReader("<p>Hello World</p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        List<Token> tokens = new ArrayList<>();

        token = tokeniser.read();
        tokens.add(token);

        token = tokeniser.read();
        tokens.add(token);

        token = tokeniser.read();
        tokens.add(token);

        token = tokeniser.read();
        tokens.add(token);

        token = tokeniser.read();
        tokens.add(token);

        Assert.assertEquals(5, tokens.size());
        Assert.assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
        Assert.assertEquals(Token.TokenType.Character, tokens.get(1).type);
        Assert.assertEquals('H', tokens.get(1).data.charAt(0));
        Assert.assertEquals(Token.TokenType.Character, tokens.get(2).type);
        Assert.assertEquals('e', tokens.get(2).data.charAt(0));
        Assert.assertEquals(Token.TokenType.Character, tokens.get(3).type);
        Assert.assertEquals('l', tokens.get(3).data.charAt(0));
        Assert.assertEquals(Token.TokenType.Character, tokens.get(4).type);
        Assert.assertEquals('l', tokens.get(4).data.charAt(0));
        Assert.assertEquals(Token.TokenType.EndTag, tokens.get(5).type);
        Assert.assertEquals("p", tokens.get(5).data);
        Assert.assertFalse(tokeniser.isEmitPending());
        Assert.assertTrue(tokeniser.getState().isData());
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testErrorScenario() {
        CharacterReader reader = new CharacterReader("<script>Hello World<script>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        List<Token> tokens = new ArrayList<>();

        token = tokeniser.read();
        tokens.add(token);

        token = tokeniser.read();
        tokens.add(token);

        token = tokeniser.read();
        tokens.add(token);

        token = tokeniser.read();
        tokens.add(token);

        token = tokeniser.read();
        tokens.add(token);

        Assert.assertEquals(5, tokens.size());
        Assert.assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
        Assert.assertEquals(Token.TokenType.Character, tokens.get(1).type);
        Assert.assertEquals('S', tokens.get(1).data.charAt(0));
        Assert.assertEquals(Token.TokenType.Character, tokens.get(2).type);
        Assert.assertEquals('c', tokens.get(2).data.charAt(0));
        Assert.assertEquals(Token.TokenType.Character, tokens.get(3).type);
        Assert.assertEquals('r', tokens.get(3).data.charAt(0));
        Assert.assertEquals(Token.TokenType.Character, tokens.get(4).type);
        Assert.assertEquals('i', tokens.get(4).data.charAt(0));
        Assert.assertEquals(Token.TokenType.Error, tokens.get(5).type);
        Assert.assertEquals("Unexpected character '<' in input state [Data]", tokens.get(5).error.getMessage());
        Assert.assertTrue(tokeniser.isEmitPending());
        Assert.assertFalse(tokeniser.getState().isData());
        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected character '<' in input state [Data]", errors.get(0).getMessage());
    }
}