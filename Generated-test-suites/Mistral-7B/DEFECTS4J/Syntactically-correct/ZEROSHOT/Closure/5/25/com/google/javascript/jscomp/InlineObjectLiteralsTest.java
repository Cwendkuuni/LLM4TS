import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest extends CompilerPassTestCase {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    compiler = newSimpleCompiler();
    safeNameIdSupplier = () -> "testId_" + System.currentTimeMillis();
  }

  public void testInlineObjectLiterals() throws Exception {
    String source =
        "function f() {" +
        "  var obj = {a: 1, b: 2};" +
        "  var c = obj.a;" +
        "  var d = obj.b;" +
        "  return c + d;" +
        "}" +
        "function g() {" +
        "  var obj = {a: 1, b: 2, c: function() {}};" +
        "  return obj.a + obj.b + obj.c();" +
        "}" +
        "function h() {" +
        "  var obj = {a: null};" +
        "  if (obj) {" +
        "    return obj.a;" +
        "  } else {" +
        "    return 0;" +
        "  }" +
        "}";

    Node root = parse(source);

    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    pass.process(null, root);

    assertEquals(0, countNodes(root, "JSCompiler_object_inline_a_testId_"));
    assertEquals(0, countNodes(root, "JSCompiler_object_inline_b_testId_"));

    // Check that the object has been split into separate variables.
    Var objVar = root.getVariable("obj");
    assertEquals(3, objVar.getScope().getVars().size());

    Var aVar = objVar.getScope().getVars().get(0);
    assertEquals("JSCompiler_object_inline_a_testId_", aVar.name);
    assertEquals(Var.VAR_TYPE, aVar.type);
    assertEquals(Node.NUMBER_LITERAL, aVar.getInitializer().getType());
    assertEquals(1, aVar.getInitializer().getFirstChild().getInt());

    Var bVar = objVar.getScope().getVars().get(1);
    assertEquals("JSCompiler_object_inline_b_testId_", bVar.name);
    assertEquals(Var.VAR_TYPE, bVar.type);
    assertEquals(Node.NUMBER_LITERAL, bVar.getInitializer().getType());
    assertEquals(2, bVar.getInitializer().getFirstChild().getInt());

    Var cVar = objVar.getScope().getVars().get(2);
    assertEquals("JSCompiler_object_inline_c_testId_", cVar.name);
    assertEquals(Var.FUNCTION_TYPE, cVar.type);

    Node fNode = root.getFirstChild();
    // Check that the object literal assignments have been replaced with
    // multiple assignments.
    List<Node> statements = Lists.newArrayList(
        fNode.getStatementList());
    assertEquals(5, statements.size());

    Node aAssign = statements.get(1);
    Node bAssign = statements.get(2);
    Node cCall = statements.get(4);

    assertEquals(Node.ASSIGN, aAssign.getType());
    assertEquals("JSCompiler_object_inline_a_testId_",
        aAssign.getFirstChild().getString());
    assertEquals(Node.NAME, aAssign.getLastChild().getType());
    assertEquals("a", aAssign.getLastChild().getString());

    assertEquals(Node.ASSIGN, bAssign.getType());
    assertEquals("JSCompiler_object_inline_b_testId_",
        bAssign.getFirstChild().getString());
    assertEquals(Node.NAME, bAssign.getLastChild().getType());
    assertEquals("b", bAssign.getLastChild().getString());

    assertEquals(Node.CALL, cCall.getType());
    assertEquals("JSCompiler_object_inline_c_testId_",
        cCall.getFirstChild().getString());

    Node functionCall = cCall.getLastChild();
    assertEquals(Node.NAME, functionCall.getType());
    assertEquals("c", functionCall.getString());

    // Check that getprops have been replaced with variables.
    Node cGetProp = root.getFirstChild().getStatementList().get(3);
    assertEquals(Node.GETPROP, cGetProp.getType());
    assertEquals("c", cGetProp.getFirstChild().getString());
    assertEquals("obj", cGetProp.getLastChild().getString());

    Node dGetProp = root.getFirstChild().getStatementList().get(5);
    assertEquals(Node.GETPROP, dGetProp.getType());
    assertEquals("b", dGetProp.getFirstChild().getString());
    assertEquals("obj", dGetProp.getLastChild().getString());

    // Check that the return statement has been updated.
    Node returnStmt = root.getFirstChild().getLastChild();
    assertEquals(Node.BINOP, returnStmt.getType());
    assertEquals(Token.PLUS, returnStmt.getFirstChild().getTokenType());
    assertEquals(Node.NAME, returnStmt.getLastChild().getType());
    assertEquals("c", returnStmt.getLastChild().getString());

    assertEquals(Node.FUNCTION_DECL, root.getType());
    assertEquals("f", root.getString());

    // Check that g() function is not transformed.
    Node gFunction = root.getChildAtIndex(1);
    assertEquals(Node.FUNCTION_DECL, gFunction.getType());
    assertEquals("g", gFunction.getString());

    // Check that h() function is not transformed.
    Node hFunction = root.getChildAtIndex(2);
    assertEquals(Node.FUNCTION_DECL, hFunction.getType());
    assertEquals("h", hFunction.getString());

    // Check that the extracted function does not affect the output.
    assertEquals("function f() {" +
        "  var a = JSCompiler_object_inline_a_testId_;" +
        "  var b = JSCompiler_object_inline_b_testId_;" +
        "  var c = function() {};" +
        "  return a + b + c();" +
        "}",
        sourceToStr(root));
  }

  private int countNodes(Node node, String name) {
    int count = 0;
    NodeUtil.visitPostOrder(node, new NodeUtil.Visitor() {
      @Override
      public void visit(Node n) {
        if (n.isName() && n.getString().equals(name)) {
          count++;
        }
      }
    });
    return count;
  }
}