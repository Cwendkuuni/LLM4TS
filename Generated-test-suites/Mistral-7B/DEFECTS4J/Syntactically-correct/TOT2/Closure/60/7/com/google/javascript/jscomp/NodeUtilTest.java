import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // ...
  }

  @Test
  public void testIsStatement() {
    // ...
  }

  @Test
  public void testIsFunctionDeclaration() {
    // ...
  }

  // Add test cases for other methods in NodeUtil here

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclaration = new Node("function foo() {}", Token.FUNCTION);
    Node hoistedFunctionDeclaration = new Node("function foo() {}", Token.FUNCTION, null, null);

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }
}