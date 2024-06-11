import static com.google.javascript.rhino.TokenStream.*;
import org.junit.Test;

public class TokenStreamTest {
  @Test
  public void testIsKeyword() {
    // Test valid keywords
    assertTrue(isKeyword("if"));
    assertTrue(isKeyword("else"));
    assertTrue(isKeyword("while"));
    assertTrue(isKeyword("for"));
    assertTrue(isKeyword("switch"));
    assertTrue(isKeyword("case"));
    assertTrue(isKeyword("default"));
    assertTrue(isKeyword("break"));
    assertTrue(isKeyword("continue"));
    assertTrue(isKeyword("return"));
    assertTrue(isKeyword("boolean"));
    assertTrue(isKeyword("int"));
    assertTrue(isKeyword("float"));
    assertTrue(isKeyword("long"));
    assertTrue(isKeyword("short"));
    assertTrue(isKeyword("byte"));
    assertTrue(isKeyword("char"));
   assertTrue(isKeyword("void"));
    assertTrue(isKeyword("void"));
    assertTrue(isKeyword("null"));
    assertTrue(isKeyword("true"));
    assertTrue(isKeyword("false"));

    // Test invalid keywords
    assertFalse(isKeyword(""));
    assertFalse(isKeyword("test"));
    assertFalse(isKeyword("Test"));
    assertFalse(isKeyword("123"));
    assertFalse(isKeyword("123d"));
  }

  @Test
  public void testIsJSIdentifier() {
    // Test valid JavaScript identifiers
    assertTrue(isJSIdentifier("myVariable"));
    assertTrue(isJSIdentifier("_myVariable"));
    assertTrue(isJSIdentifier("my_variable"));
    assertTrue(isJSIdentifier("my_long_variable"));
    assertTrue(isJSIdentifier("myLongVariable"));
    assertTrue(isJSIdentifier("my_long_Variable"));
    assertTrue(isJSIdentifier("my_long_Variable_1"));
    assertTrue(isJSIdentifier("myLongVariable_1"));

    // Test invalid JavaScript identifiers
    assertFalse(isJSIdentifier("my-variable"));
    assertFalse(isJSIdentifier("my variable"));
    assertFalse(isJSIdentifier("my variable "));
    assertFalse(isJSIdentifier("123"));
    assertFalse(isJSIdentifier("my 123"));
    assertFalse(isJSIdentifier("my123"));
  }
}