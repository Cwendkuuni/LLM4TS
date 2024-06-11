import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = TestSourceFile.getNodes("for (var i in array) {}")[0];
    assertTrue(isForIn(node));
  }

  @Test
  public void testIsForIn_Invalid() {
    Node node = TestSourceFile.getNodes("for (var i = 0; i < array.length; i++) {}")[0];
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = TestSourceFile.getNodes("if (condition) {}")[0];
    assertTrue(isStatement(node));
  }

  @Test
  public void testIsStatement_Invalid() {
    Node node = TestSourceFile.getNodes("1 + 1;")[0];
    assertFalse(isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = TestSourceFile.getNodes("function foo() {}")[0];
    assertTrue(isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_Invalid() {
    Node node = TestSourceFile.getNodes("1 + function foo() {}")[0];
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = TestSourceFile.getNodes("function foo() {}")[0];
    assertTrue(isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_Invalid() {
    Node node = TestSourceFile.getNodes("function foo() {} var x = 1;")[0];
    assertFalse(isHoistedFunctionDeclaration(node));
  }
}