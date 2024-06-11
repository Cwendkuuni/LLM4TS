import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        String[] keywords = {"if", "else", "while", "for", "switch", "case", "default", "break", "continue", "return", "int", "float", "double", "boolean", "true", "false", "null", "this", "super", "class", "interface", "extends", "implements", "package", "private", "protected", "public", "static", "final", "abstract", "void", "throw", "catch", "try", "finally", "new", "import", "export", "export default", "export class", "let", "const"};
        for (String keyword : keywords) {
            boolean result = TokenStream.isKeyword(keyword);
            assertEquals(true, result);
        }
    }
}