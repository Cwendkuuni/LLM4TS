import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing true cases
 assertTrue(isKeyword("abstract"));
 assertTrue(isKeyword("arguments"));
 assertTrue(isKeyword("as"));
 assertTrue(isKeyword("boolean"));
 assertTrue(isKeyword("break"));
 assertTrue(isKeyword("byte"));
 assertTrue(isKeyword("case"));
 assertTrue(isKeyword("catch"));
 assertTrue(isKeyword("char"));
 assertTrue(isKeyword("class"));
 assertTrue(isKeyword("const"));
 assertTrue(isKeyword("continue"));
 assertTrue(isKeyword("default"));
 assertTrue(isKeyword("delete"));
 assertTrue(isKeyword("do"));
 assertTrue(isKeyword("double"));
 assertTrue(isKeyword("else"));
 assertTrue(isKeyword("enum"));
 assertTrue(isKeyword("eval"));
 assertTrue(isKeyword("export"));
 assertTrue(isKeyword("extends"));
 assertTrue(isKeyword("false"));
 assertTrue(isKeyword("final"));
 assertTrue(isKeyword("finally"));
 assertTrue(isKeyword("float"));
 assertTrue(isKeyword("for"));
 assertTrue(isKeyword("function"));
 assertTrue(isKeyword("goto"));
 assertTrue(isKeyword("if"));
 assertTrue(isKeyword("implements"));
 assertTrue(isKeyword("import"));
 assertTrue(isKeyword("in"));
 assertTrue(isKeyword("int"));
 assertTrue(isKeyword("interface"));
 assertTrue(isKeyword("let"));
 assertTrue(isKeyword("long"));
 assertTrue(isKeyword("native"));
 assertTrue(isKeyword("new"));
 assertTrue(isKeyword("null"));
 assertTrue(isKeyword("package"));
 assertTrue(isKeyword("private"));
 assertTrue(isKeyword("protected"));
 assertTrue(isKeyword("public"));
 assertTrue(isKeyword("return"));
 assertTrue(isKeyword("short"));
 assertTrue(isKeyword("static"));
 assertTrue(isKeyword("super"));
 assertTrue(isKeyword("switch"));
 assertTrue(isKeyword("synchronized"));
 assertTrue(isKeyword("this"));
 assertTrue(isKeyword("throw"));
 assertTrue(isKeyword("transient"));
 assertTrue(isKeyword("true"));
 assertTrue(isKeyword("try"));
 assertTrue(isKeyword("typeof"));
 assertTrue(isKeyword("var"));
 assertTrue(isKeyword("void"));
 assertTrue(isKeyword("volatile"));
 assertTrue(isKeyword("while"));
 assertTrue(isKeyword("with"));

 // Testing false cases
 assertFalse(isKeyword(""));
 assertFalse(isKeyword(" "));
 assertFalse(isKeyword("1"));
 assertFalse(isKeyword("123"));
 assertFalse(isKeyword("hello"));
 assertFalse(isKeyword("HelloWorld"));
 assertFalse(isKeyword("MyClass"));
 assertFalse(isKeyword("myVariable"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing true cases
 assertTrue(isJSIdentifier(""));
 assertTrue(isJSIdentifier(""));
 assertTrue(isJSIdentifier("_"));
 assertTrue(isJSIdentifier("abc"));
 assertTrue(isJSIdentifier("ABC"));
 assertTrue(isJSIdentifier("123"));
 assertTrue(isJSIdentifier("1_2_3"));
 assertTrue(isJSIdentifier("aBcDeF"));
 assertTrue(isJSIdentifier("aBc_123"));
 assertTrue(isJSIdentifier("aBc_1_2"));
 assertTrue(isJSIdentifier("aBc_1_2_3"));
 assertTrue(isJSIdentifier("aBc_123abc"));
 assertTrue(isJSIdentifier("aBc_123_abc"));
 assertTrue(isJSIdentifier("aBc_123_abc123"));
 assertTrue(isJSIdentifier("abc123"));
 assertTrue(isJSIdentifier("abc_123"));
 assertTrue(isJSIdentifier("abc_1_2"));
 assertTrue(isJSIdentifier("abc_1_2_3"));
 assertTrue(isJSIdentifier("abc_123abc"));
 assertTrue(isJSIdentifier("abc_123_abc"));
 assertTrue(isJSIdentifier("abc_123_abc123"));

 // Testing false cases
 assertFalse(isJSIdentifier(""));
 assertFalse(isJSIdentifier(" "));
 assertFalse(isJSIdentifier("1"));
 assertFalse(isJSIdentifier("123"));
 assertFalse(isJSIdentifier("hello"));
 assertFalse(isJSIdentifier("HelloWorld"));
 assertFalse(isJSIdentifier("MyClass"));
 assertFalse(isJSIdentifier("myVariable"));
 assertFalse(isJSIdentifier("my Variable"));
 assertFalse(isJSIdentifier("my variable"));
 assertFalse(isJSIdentifier("myVariable1"));
 assertFalse(isJSIdentifier("myVariable_1"));
 assertFalse(isJSIdentifier("myVariable_123"));
 assertFalse(isJSIdentifier("myVariable123"));
 assertFalse(isJSIdentifier("myVariable1_23"));
 assertFalse(isJSIdentifier("myVariable_1_23"));
 assertFalse(isJSIdentifier("myVariable_1_23_"));
 assertFalse(isJSIdentifier("myVariable_1_23_ "));
 assertFalse(isJSIdentifier("myVariable_1_23_ "));
 }
}