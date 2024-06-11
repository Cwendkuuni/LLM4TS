import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior.*;
import static com.google.javascript.jscomp.ReferenceCollectingCallback.Reference.*;
import static com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection.*;
import static com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private ReferenceCollectingCallback callback;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    safeNameIdSupplier = () -> "test";
    callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
  }

  @Test
  public void testInlineObjectLiterals_Basic() {
    String code = "function foo() { var x = {a: 1, b: 2}; x.a = x.a + 1; }";

    Node root = NodeUtil.parse(code);

    callback.process(null, root);

    Scope scope = root.getFirstChild().getFirstChild().getScope();

    Var x = scope.getVar("x");
    ReferenceCollection referenceInfo = callback.getReferenceMap().get(x);
    List<Reference> references = referenceInfo.references;

    assertEquals(2, references.size());

    Reference declaration = references.get(0);
    Reference init = references.get(1);

    assertEquals(VAR, declaration.getVarType());
    assertEquals(ASSIGN, init.getNode().getOp().getTokenType());
  }

  @Test
  public void testInlineObjectLiterals_EdgeCase_SelfReference() {
    String code = "function foo() { var x = {a: function() { return x.a; }}; }";

    Node root = NodeUtil.parse(code);

    callback.process(null, root);

    Scope scope = root.getFirstChild().getFirstChild().getScope();

    Var x = scope.getVar("x");
    ReferenceCollection referenceInfo = callback.getReferenceMap().get(x);
    List<Reference> references = referenceInfo.references;

    assertEquals(2, references.size());

    Reference declaration = references.get(0);
    Reference init = references.get(1);

    assertEquals(VAR, declaration.getVarType());
    assertEquals(ASSIGN, init.getNode().getOp().getTokenType());

    // TODO: Implement a test for self-referential objects.
  }

  @Test
  public void testInlineObjectLiterals_EdgeCase_CircularReference() {
    String code = "function foo() { var x = {a: {b: x}}; }";

    Node root = NodeUtil.parse(code);

    callback.process(null, root);

    // TODO: Implement a test for circular references.
  }

  @Test
  public void testInlineObjectLiterals_EdgeCase_KeyCollision() {
    String code = "function foo() { var x = {a: 1, b: 2, a: 3}; }";

    Node root = NodeUtil.parse(code);

    callback.process(null, root);

    Scope scope = root.getFirstChild().getFirstChild().getScope();

    Var x = scope.getVar("x");
    ReferenceCollection referenceInfo = callback.getReferenceMap().get(x);
    List<Reference> references = referenceInfo.references;

    assertEquals(2, references.size());

    Reference declaration = references.get(0);
    Reference init = references.get(1);

    assertEquals(VAR, declaration.getVarType());
    assertEquals(ASSIGN, init.getNode().getOp().getTokenType());

    // TODO: Implement a test for key collision.
  }

  @Test
  public void testInlineObjectLiterals_ErrorCase_NotObjectLiteral() {
    String code = "function foo() { var x = 1; }";

    Node root = NodeUtil.parse(code);

    callback.process(null, root);

    // TODO: Implement a test for non-object literal case.
  }

  @Test
  public void testInlineObjectLiterals_ErrorCase_AssignmentToNonVariable() {
    String code = "function foo() { var x = {}; x = {}; }";

    Node root = NodeUtil.parse(code);

    callback.process(null, root);

    // TODO: Implement a test for assignment to non-variable case.
  }

  @Test
  public void testInlineObjectLiterals_ErrorCase_InvalidObjectProperty() {
    String code = "function foo() { var x = {invalid: 1}; }";

    Node root = NodeUtil.parse(code);

    callback.process(null, root);

    // TODO: Implement a test for invalid object property case.
  }

  private static class InliningBehavior implements Behavior {
    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
      // TODO: Implement behavior
    }
  }
}