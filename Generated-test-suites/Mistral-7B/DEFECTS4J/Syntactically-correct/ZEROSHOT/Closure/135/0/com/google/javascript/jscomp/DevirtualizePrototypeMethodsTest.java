import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  private static Compiler createCompiler() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    return compiler;
  }

  @org.junit.Before
  public void setup() {
    compiler = createCompiler();
  }

  private Compiler compiler;

  @org.junit.Test
  public void testDevirtualizePrototypeMethods() {
    String sourceCode =
        "function A() {\n" +
        "  this.prototype.accumulate = function(value) {\n" +
        "    this.total += value; return this.total\n" +
        "  };\n" +
        "}\n" +
        "A.prototype.accumulate(2);";

    Node root = parser.parse(sourceCode, "Test", null);
    CompilerPass devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(externs, root);

    // Check if total function was rewritten as JSCompiler_StaticMethods_accumulate
    List<Node> statements = root.statements();
    Node totalFunctionCall = statements.get(0);
    Node totalFunctionName = totalFunctionCall.getLastChild();
    assertTrue("total function call name is not rewritten",
        totalFunctionName.getString().startsWith("JSCompiler_StaticMethods_"));
    Node totalFunctionDefinition =
        findFunctionDefinitionByName("JSCompiler_StaticMethods_accumulate", root);
    assertNotNull("total function definition not found", totalFunctionDefinition);

    // Check if total function type was updated to accept 'this' as first argument
    FunctionType totalFunctionType = (FunctionType) totalFunctionDefinition.getJSType();
    List<JSType> argumentTypes = totalFunctionType.getParameters();
    JSType thisType = compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.THIS);
    assertNotNull("this type not found", thisType);
    assertFalse("this type not present as first argument",
        argumentTypes.get(0).equals(thisType));
    assertTrue("this type present in argument list", argumentTypes.contains(thisType));

    // Check if accumulate call site was updated to pass 'this' as first argument
    Node accumulateFunctionCall = statements.get(1);
    Node accumulateFunctionCallThis = accumulateFunctionCall.getFirstChild();
    Node newAccumulateFunctionName =
        accumulateFunctionCall.getLastChild().getLastChild();
    assertTrue("accumulate function call name is not rewritten",
        newAccumulateFunctionName.getString().startsWith("JSCompiler_StaticMethods_"));
    Node newAccumulateFunctionDefinition =
        findFunctionDefinitionByName("JSCompiler_StaticMethods_accumulate", root);
    Node newAccumulateFunction = newAccumulateFunctionDefinition.getBody();
    Node newAccumulateFunctionThis = newAccumulateFunction.getFirstChild();
    assertTrue("this not present in new accumulate function",
        newAccumulateFunctionThis.getString().equals("this"));

    // Check if accumulate function definition was updated to accept 'this' as first argument
    FunctionType newAccumulateFunctionType =
        (FunctionType) newAccumulateFunctionDefinition.getJSType();
    List<JSType> newAccumulateFunctionArguments = newAccumulateFunctionType.getParameters();
    assertTrue("this not present as first argument in new accumulate function type",
        newAccumulateFunctionArguments.get(0).equals(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.THIS)));

    // Check if accumulate function body was updated to use 'this' instead of 'this.total'
    Node newAccumulateFunctionBody = newAccumulateFunction.getLastChild();
    Node newAccumulateFunctionBodyThis = newAccumulateFunctionBody.getFirstChild();
    Node newAccumulatFunctionBodyTotal = newAccumulateFunctionBody.getLastChild();
    assertFalse("this.total not replaced with this",
        newAccumulatFunctionBodyTotal.getString().contains("this.total"));
    assertTrue("this present in new accumulate function body",
        newAccumulateFunctionBodyThis.getString().equals("this"));

  }

  private Node findFunctionDefinitionByName(String name, Node root) {
    for (Node node = root; node != null; node = node.getParent()) {
      if (NodeUtil.isFunction(node)) {
        String functionName = node.getLastChild().getString();
        if (functionName.equals(name)) {
          return node;
        }
      }
    }
    return null;
  }
}