import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static com.google.javascript.rhino.NodeTraversing.getFirstChild;
import static com.google.javascript.rhino.NodeTraversing.getLastChild;
import static com.google.javascript.rhino.NodeTraversing.getParent;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.DefinitionSite;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.*;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() throws Exception {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    // Add DevirtualizePrototypeMethods pass
    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    passManager.addPass(devirtualizer);

    // Create test case
    String input = "function A() { this.total = 0; } " +
        "A.prototype.accumulate = function(value) { this.total += value; return this.total; } " +
        "var a = new A(); a.accumulate(2); ";

    Node externs = Node.externalize(input);
    Node root = Node.parse(input);

    // Run compilation passes
    passManager.process(externs, root);

    // Check for correct rewriting
    Node totalNode = getFirstChild(getLastChild(root));
    assertNotNull(totalNode);
    assertEquals(Node.OP_ASSIGN, totalNode.getOperator());
    assertEquals(Token.NUMBER, totalNode.getFirstChild().getType());
    assertEquals(Token.INT_0, totalNode.getFirstChild().integerValueExact());

    Node aNode = getFirstChild(getFirstChild(root));
    Node aConstructorNode = getFirstChild(aNode);
    assertNotNull(aConstructorNode);
    assertEquals(Node.CALL, aConstructorNode.getOperator());
    assertEquals(Token.NAME, aConstructorNode.getFirstChild().getType());
    assertEquals("A", aConstructorNode.getFirstChild().getString());

    Node accumulateCallNode = getFirstChild(getLastChild(getLastChild(aNode)));
    assertNotNull(accumulateCallNode);
    assertEquals(Node.CALL, accumulateCallNode.getOperator());
    assertEquals(Token.NAME, accumulateCallNode.getFirstChild().getType());
    assertEquals("accumulate", accumulateCallNode.getFirstChild().getString());
    assertEquals(Token.NAME, accumulateCallNode.getLastChild().getType());
    assertEquals("a", accumulateCallNode.getLastChild().getString());

    // Check for correct static method rewriting
    DefinitionSite definitionSite = getDefinitionSite(compiler, "accumulate");
    assertNotNull(definitionSite);
    Definition definition = definitionSite.definition;
    Node definitionNode = definition.getNode();
    String newMethodName = definitionSite.definition.getRValue().getString();
    assertNotNull(definitionNode);

    Node functionNode = getLastChild(definitionNode);
    assertNotNull(functionNode);
    assertEquals(Token.VAR, functionNode.getOperator());
    Node newNameNode = getFirstChild(functionNode);
    assertEquals(Node.NAME, newNameNode.getType());
    assertEquals(newMethodName, newNameNode.getString());

    Node argList = getFirstChild(getFirstChild(functionNode));
    assertEquals(Token.LIST, argList.getType());
    assertEquals(3, argList.getChildCount());

    Node thisArgNode = getFirstChild(argList);
    assertEquals(Node.NAME, thisArgNode.getType());
    assertEquals("self", thisArgNode.getString());

    Node callNode = getLastChild(getLastChild(aNode));
    assertEquals(Node.CALL, callNode.getOperator());
    Node newMethodCallNode = getFirstChild(callNode);
    assertEquals(Node.NAME, newMethodCallNode.getType());
    assertEquals(newMethodName, newMethodCallNode.getString());

    Node thisArgNodeInCall = getFirstChild(getFirstChild(newMethodCallNode));
    assertEquals(Node.NAME, thisArgNodeInCall.getType());
    assertEquals("a", thisArgNodeInCall.getString());

    // Check for correct type rewriting
    FunctionType functionType = (FunctionType) functionNode.getJSType();
    assertNotNull(functionType);
    List<JSType> argTypes = functionType.getParameters();
    assertEquals(3, argTypes.size());
    assertEquals(JSType.thisType, argTypes.get(0));
    assertEquals(JSType.NUMBER, argTypes.get(1));
    assertEquals(JSType.NUMBER, argTypes.get(2));

    ObjectType thisType = functionType.getTypeOfThis();
    assertEquals(JSTypeNative.UNKNOWN_TYPE, thisType);

    assertEquals(JSType.VOID, functionType.getReturnType());

  }

  private DefinitionSite getDefinitionSite(AbstractCompiler compiler,
                                          String methodName) {
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, null);
    List<DefinitionSite> definitionSites =
        defFinder.getDefinitionSitesByName(methodName);
    if (definitionSites.isEmpty()) {
      return null;
    }
    return definitionSites.get(0);
  }
}