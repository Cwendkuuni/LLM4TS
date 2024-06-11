import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.JSDocInfo.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.TestScopeCreator;
import com.google.javascript.rhino.JSDocInfo;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() throws Exception {
    String source = "function f() { var x = 1; }";

    AbstractCompiler compiler = new TestCompiler();
    ScopeCreator ssc = new TestScopeCreator(compiler);

    Scope scope = ssc.createScope(new Node(source), null);
    assertEquals(Token.FUNCTION, scope.getNode().getType());
    assertEquals("f", scope.getNode().getFirstChild().getString());

    Scope localScope = scope.getChildScope();
    assertEquals(Token.VAR, localScope.getNode().getType());
    assertEquals(1, localScope.size());
    assertEquals("x", localScope.getVariableName(0).getName());
    assertEquals(JSType.NUMBER, localScope.getVariableType(0));
  }

  @Test
  public void testCreateScopeWithDuplicateDeclaration() throws Exception {
    String source = "function f() { var x = 1; var x = 2; }";

    AbstractCompiler compiler = new TestCompiler();
    ScopeCreator ssc = new TestScopeCreator(compiler);

    try {
      Scope scope = ssc.createScope(new Node(source), null);
      fail();
    } catch (IllegalStateException e) {
      String expectedErrorMessage = "Variable x first declared in f";
      assertEquals(expectedErrorMessage, e.getMessage());
    }
  }

  // Add more test cases for edge cases and exceptions here
}