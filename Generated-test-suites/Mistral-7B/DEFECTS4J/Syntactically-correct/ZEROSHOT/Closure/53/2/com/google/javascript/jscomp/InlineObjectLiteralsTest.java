import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = new Scope();
    Var v = scope.declareVar("obj", Node.newObjectLiteral(
        Node.newProperty("a", Node.newNumberLiteral(1)),
        Node.newProperty("b", Node.newNumberLiteral(2))));

    Node root = Node.newProgram(Node.newBlock(scope.getRootNode()));

    inlineObjectLiterals.process(Node.newScope(Node.SCOPE_GLOBAL), root);

    assertEquals(3, scope.getVars().size());

    Var a = scope.getVar("JSCompiler_object_inline_a");
    Var b = scope.getVar("JSCompiler_object_inline_b");

    assertEquals(Node.newNumberLiteral(1), a.getInitializer());
    assertEquals(Node.newNumberLiteral(2), b.getInitializer());

    assertEquals(Node.newVar(Node.newString(Token.NAME, "obj"),
        Node.newObjectLiteral(
            Node.newProperty(Node.newString(Token.NAME, "a"), a),
            Node.newProperty(Node.newString(Token.NAME, "b"), b))),
        v.getInitializer());
  }

  @Test
  public void testInlineObjectLiteralsWithFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = new Scope();
    Var obj = scope.declareVar("obj", Node.newObjectLiteral(
        Node.newProperty("a", Node.newNumberLiteral(1)),
        Node.newProperty("fn", Node.newFunction(Node.newName("fn"), null,
            Node.newBlock(
                Node.newVariableStatement(
                    Node.newVar(Node.newName("x"), Node.newName("obj"))),
                    Node.newReturnStatement(Node.newName("x.fn")))))));

    Node root = Node.newProgram(Node.newBlock(scope.getRootNode()));

    inlineObjectLiterals.process(Node.newScope(Node.SCOPE_GLOBAL), root);

    assertEquals(4, scope.getVars().size());

    Var a = scope.getVar("JSCompiler_object_inline_a");
    Var fn = scope.getVar("JSCompiler_object_inline_fn");

    assertEquals(Node.newNumberLiteral(1), a.getInitializer());

    Node fnNode = fn.getInitializer();
    assertEquals(Node.newName("fn"), fnNode.getFirstChild());
    assertEquals(Node.newObjectLiteral(
        Node.newProperty("x", Node.newVar(Node.newName("obj")))),
        fnNode.getLastChild());

    assertEquals(Node.newVar(Node.newName("obj"),
        Node.newObjectLiteral(
            Node.newProperty(Node.newString(Token.NAME, "a"), a),
            Node.newProperty(Node.newString(Token.NAME, "fn"), fn))),
        obj.getInitializer());
  }
}