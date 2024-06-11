import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Tokenizer;

public class TokenTest {
    @Test
    public void testDoctypeToken() {
        String html = "<!DOCTYPE html>";
        Tokenizer tokenizer = new Tokenizer(html);
        Token token = tokenizer.nextToken();
        assertEquals("Doctype", token.tokenType());
        assertEquals("html", token.asDoctype().getName());
    }
}