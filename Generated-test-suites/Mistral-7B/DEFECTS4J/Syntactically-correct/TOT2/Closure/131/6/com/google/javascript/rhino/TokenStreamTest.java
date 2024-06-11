import static org.junit.Assert.*; // For Assert.assertTrue and Assert.assertFalse
import static com.google.javascript.rhino.TokenStream.*; // For static imports

public class TokenStreamTest {

  // Test for isKeyword method
  @Test
  public void testIsKeyword() {
    // Test with valid keywords
    assertTrue(isKeyword("if"));
    assertTrue(isKeyword("else"));
    assertTrue(isKeyword("switch"));
    assertTrue(isKeyword("while"));
    assertTrue(isKeyword("for"));
    assertTrue(isKeyword("int"));
    assertTrue(isKeyword("float"));
    assertTrue(isKeyword("double"));
    assertTrue(isKeyword("long"));
    assertTrue(isKeyword("short"));
    assertTrue(isKeyword("boolean"));
    assertTrue(isKeyword("char"));
    assertTrue(isKeyword("void"));
    assertTrue(isKeyword("class"));
    assertTrue(isKeyword("static"));
    assertTrue(isKeyword("public"));
    assertTrue(isKeyword("private"));
    assertTrue(isKeyword("protected"));
    assertTrue(isKeyword("interface"));
    assertTrue(isKeyword("extends"));
    assertTrue(isKeyword("implements"));
    assertTrue(isKeyword("package"));
    assertTrue(isKeyword("import"));
    assertTrue(isKeyword("new"));
    assertTrue(isKeyword("this"));
    assertTrue(isKeyword("super"));
    assertTrue(isKeyword("throw"));
    assertTrue(isKeyword("try"));
    assertTrue(isKeyword("catch"));
    assertTrue(isKeyword("finally"));

    // Test with invalid strings
    assertFalse(isKeyword(""));
    assertFalse(isKeyword("invalidKeyword"));
    assertFalse(isKeyword("1234"));
    assertFalse(isKeyword("myVar"));
    assertFalse(isKeyword("myFunction"));
  }
}