import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TokenStreamTest {
  @Test
  public void testIsKeyword() {
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("else"));
    assertTrue(TokenStream.isKeyword("while"));
    // Add more true cases here

    assertFalse(TokenStream.isKeyword("myVariable")); // False case
  }
}