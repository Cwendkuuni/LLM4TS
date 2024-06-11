import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.CharacterInput;

public class TokeniserTest {

  @Test
  public void testRead() {
    CharacterInput reader = new CharacterInput("Hello World!");
    Tokeniser tokeniser = new Tokeniser(reader);

    Token token = tokeniser.read();
    assertEquals(Token.TokenType.StartTag, token.type);
    assertEquals("html", token.tagName);

    token = tokeniser.read();
    assertEquals(Token.TokenType.Character, token.type);
    assertEquals("H", token.text);

    token = tokeniser.read();
    assertEquals(Token.TokenType.Character, token.type);
    assertEquals("e", token.text);

    token = tokeniser.read();
    assertEquals(Token.TokenType.Character, token.type);
    assertEquals("l", token.text);

    token = tokeniser.read();
    assertEquals(Token.TokenType.Character, token.type);
    assertEquals("l", token.text);

    token = tokeniser.read();
    assertEquals(Token.TokenType.Character, token.type);
    assertEquals("o", token.text);

    token = tokeniser.read();
    assertEquals(Token.TokenType.Character, token.type);
    assertEquals(" ", token.text);

    token = tokeniser.read();
    assertEquals(Token.TokenType.Character, token.type);
    assertEquals("W", token.text);

    token = tokeniser.read();
    assertEquals(Token.TokenType.Character, token.type);
    assertEquals("o", token.text);

    token = tokeniser.read();
    assertEquals(Token.TokenType.Character, token.type);
    assertEquals("r", token.text);

    token = tokeniser.read();
    assertEquals(Token.TokenType.Character, token.type);
    assertEquals("l", token.text);

    token = tokeniser.read();
    assertEquals(Token.TokenType.EndTag, token.type);
    assertEquals("html", token.tagName);
  }
}