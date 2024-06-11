import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing keywords with correct length and characters
 String[] expectedKeywords = {"abstract", "arguments", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "delete", "do", "double", "else", "export", "extends", "false", "final", "finally", "float", "for", "function", "goto", "if", "implements", "import", "in", "instanceof", "int", "interface", "let", "long", "native", "new", "null", " package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "typeof", "var", "void", "volatile", "while", "with", "yield"};

 for (String keyword : expectedKeywords) {
 assertTrue(TokenStream.isKeyword(keyword));
 }

 // Testing keywords with incorrect length or characters
 String[] incorrectKeywords = {"ifo", "abtraact", "falsey", "clas", "publick"};

 for (String incorrectKeyword : incorrectKeywords) {
 assertFalse(TokenStream.isKeyword(incorrectKeyword));
 }
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing valid JavaScript identifiers
 String[] validIdentifiers = {"myVariable", "my_variable", "_myVariable", "myVariable123", "my_variable_123", "my_long_identifier"};

 for (String validIdentifier : validIdentifiers) {
 assertTrue(TokenStream.isJSIdentifier(validIdentifier));
 }

 // Testing invalid JavaScript identifiers
 String[] invalidIdentifiers = {"my-variable", "my variable", "my variable 123", "my variable_123", "my_variable-123", "my_variable 123", "my variable_123-"};

 for (String invalidIdentifier : invalidIdentifiers) {
 assertFalse(TokenStream.isJSIdentifier(invalidIdentifier));
 }
 }
}