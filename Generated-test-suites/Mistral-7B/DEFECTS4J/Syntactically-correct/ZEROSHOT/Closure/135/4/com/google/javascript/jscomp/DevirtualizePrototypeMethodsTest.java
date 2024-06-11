import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.DefinitionSite;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.NodeUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    Compiler compiler = new Compiler();
    CompilerPass devirtualizer = new DevirtualizePrototypeMethods(compiler);

    String sourceCode = "function A() {}" +
        "A.prototype.accumulate = function(value) {" +
        "  this.total += value; return this.total" +
        "}" +
        "var a = new A();" +
        "var total = a.accumulate(2);";

    Node root = AbstractCompilerTest.processSource(compiler, sourceCode);

    devirtualizer.process(null, root);

    Node accumulateNode = NodeUtil.findFunctionByName(root, "accumulate");
    assertNotNull(accumulateNode);
    Node accumulateFunctionNode = accumulateNode.getBody();
    assertEquals(2, accumulateFunctionNode.getChildCount());

    Node firstChild = accumulateFunctionNode.getFirstChild();
    assertEquals("JSCompiler_StaticMethods_accumulate", firstChild.getString());
    assertEquals(Node.NEW_VAR, firstChild.getType());
    assertEquals(Token.NAME, firstChild.getFirstChild().getType());
    assertEquals("self", firstChild.getFirstChild().getString());
    assertEquals(Node.ASSIGN, firstChild.getLastChild().getType());
    Node selfAssignNode = firstChild.getLastChild();
    assertEquals("total", selfAssignNode.getFirstChild().getString());
    assertEquals(Node.ADD, selfAssignNode.getLastChild().getType());

    Node argumentNode = accumulateFunctionNode.getLastChild();
    assertEquals("arg", argumentNode.getString());
    assertEquals(Node.ASSIGN, argumentNode.getParent().getType());

    Node callNode = NodeUtil.findCallNode(accumulateNode, "total");
    assertEquals(3, callNode.getChildCount());
    Node callFunctionNode = callNode.getFirstChild();
    assertEquals("JSCompiler_StaticMethods_accumulate", callFunctionNode.getString());
    assertEquals(Node.NEW_VAR, callFunctionNode.getType());
    Node callSelfAssignNode = callFunctionNode.getLastChild();
    assertEquals("arg", callSelfAssignNode.getFirstChild().getString());
    assertEquals("total", callSelfAssignNode.getLastChild().getString());

    // test that devirtualizer didn't change other things
    Node originalAccumulateNode = NodeUtil.findFunctionByName(root, "accumulate");
    assertNotNull(originalAccumulateNode);
    Node originalAccumulateFunctionNode = originalAccumulateNode.getBody();
    assertEquals(3, originalAccumulateFunctionNode.getChildCount());
  }
}