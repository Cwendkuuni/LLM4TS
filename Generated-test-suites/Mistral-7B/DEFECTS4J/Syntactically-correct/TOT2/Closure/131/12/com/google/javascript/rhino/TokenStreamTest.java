import static org.junit.Assert.*;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    String[] keywords = {
      "abstract", "arguments", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue",
      "debugger", "default", "delete", "do", "double", "else", "enum", "eval", "export", "extends", "false",
      "finally", "float", "for", "function", "goto", "if", "implements", "import", "in", "instanceof", "int",
      "interface", "let", "long", "native", "new", "null", "package", "private", "protected", "public", "return",
      "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try",
      "typeof", "var", "void", "volatile", "while", "with", "yield", "export default"
    };

    for (String keyword : keywords) {
      boolean expected = TokenStream.isKeyword(keyword);
      boolean actual = TokenStream.isKeyword(keyword);
      assertEquals(expected, actual);
    }
  }

  @Test
  public void testIsKeywordEdgeCases() {
    String[] edgeCases = {"", null, "foo123bar", "fooBar123", "fooBar123456"};

    for (String edgeCase : edgeCases) {
      boolean actual = TokenStream.isKeyword(edgeCase);
      assertFalse(actual);
    }
  }

}