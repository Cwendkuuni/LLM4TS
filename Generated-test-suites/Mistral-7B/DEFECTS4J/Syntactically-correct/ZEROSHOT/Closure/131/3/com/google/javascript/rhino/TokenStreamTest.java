import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing positive cases
 assertTrue(TokenStream.isKeyword("break"));
 assertTrue(TokenStream.isKeyword("case"));
 assertTrue(TokenStream.isKeyword("catch"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("continue"));
 assertTrue(TokenStream.isKeyword("default"));
 assertTrue(TokenStream.isKeyword("delete"));
 assertTrue(TokenStream.isKeyword("do"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("export"));
 assertTrue(TokenStream.isKeyword("extends"));
 assertTrue(TokenStream.isKeyword("false"));
 assertTrue(TokenStream.isKeyword("finally"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("function"));
 assertTrue(TokenStream.isKeyword("goto"));
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("import"));
 assertTrue(TokenStream.isKeyword("in"));
 assertTrue(TokenStream.isKeyword("instanceof"));
 assertTrue(TokenStream.isKeyword("int"));
 assertTrue(TokenStream.isKeyword("interface"));
 assertTrue(TokenStream.isKeyword("new"));
 assertTrue(TokenStream.isKeyword("null"));
 assertTrue(TokenStream.isKeyword("package"));
 assertTrue(TokenStream.isKeyword("private"));
 assertTrue(TokenStream.isKeyword("protected"));
assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("short"));
 assertTrue(TokenStream.isKeyword("static"));
 assertTrue(TokenStream.isKeyword("super"));
 assertTrue(TokenStream.isKeyword("switch"));
 assertTrue(TokenStream.isKeyword("synchronized"));
 assertTrue(TokenStream.isKeyword("this"));
 assertTrue(TokenStream.isKeyword("throw"));
 assertTrue(TokenStream.isKeyword("transient"));
 assertTrue(TokenStream.isKeyword("true"));
 assertTrue(TokenStream.isKeyword("try"));
 assertTrue(TokenStream.isKeyword("typeof"));
 assertTrue(TokenStream.isKeyword("var"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("with"));

 // Testing negative cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword("abcd"));
 assertFalse(TokenStream.isKeyword("1234"));
 assertFalse(TokenStream.isKeyword("_abc"));
 assertFalse(TokenStream.isKeyword("AbC"));
 assertFalse(TokenStream.isKeyword("Ab_C"));
 assertFalse(TokenStream.isKeyword("Ab_c"));
 assertFalse(TokenStream.isKeyword("abC"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing positive cases
 assertTrue(TokenStream.isJSIdentifier("identifier"));
 assertTrue(TokenStream.isJSIdentifier("_identifier"));
 assertTrue(TokenStream.isJSIdentifier("identifier_"));
 assertTrue(TokenStream.isJSIdentifier("identifier1"));
 assertTrue(TokenStream.isJSIdentifier("identifier123"));
 assertTrue(TokenStream.isJSIdentifier("identifier_123"));
 assertTrue(TokenStream.isJSIdentifier("identifier_123_"));

 // Testing negative cases
 assertFalse(TokenStream.isJSIdentifier(""));
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("_123"));
 assertFalse(TokenStream.isJSIdentifier("_"));
 assertFalse(TokenStream.isJSIdentifier("1"));
 }
}