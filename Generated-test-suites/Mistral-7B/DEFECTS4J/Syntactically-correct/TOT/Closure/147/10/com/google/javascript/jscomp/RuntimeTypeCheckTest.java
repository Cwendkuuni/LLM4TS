import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() {
    String sourceCode = "function MyClass() {}";
    CompilerInput input = new CompilerInput(null, null, null, sourceCode, null);
    AbstractCompiler compiler = newCompiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    compiler.addPass(runtimeTypeCheck);
    Node root = compiler.parseTree(input, null, null);

    // Test checking instance of a class
    Node constructor = NodeUtil.getFunctionDeclaration(root, "MyClass");
    Node instance = NodeUtil.newObjectLiteral(-1, -1);
    Node checkInstance = NodeUtil.newCall(jsCode("checkInstance"),
        constructor, instance);
    Node result = NodeUtil.newCall(checkInstance, Node.newString("MyClass"));
    assertTrue(result.toSource(null).equals("true"));

    // Test checking implementation of an interface
    String interfaceSourceCode = "interface MyInterface {}";
    CompilerInput interfaceInput = new CompilerInput(null, null, null,
        interfaceSourceCode, null);
    Node interfaceRoot = newCompiler().parseTree(interfaceInput, null, null);
    ObjectType interfaceType = (ObjectType) NodeUtil.getFunctionDeclaration(
        interfaceRoot, "MyInterface").getJSType();

    String classSourceCode = "class MyClass implements MyInterface {}";
    CompilerInput classInput = new CompilerInput(null, null, null,
        classSourceCode, null);
    Node classRoot = newCompiler().parseTree(classInput, null, null);
    FunctionType classType = (FunctionType) NodeUtil.getFunctionDeclaration(
        classRoot, "MyClass").getJSType();

    Node checkImplementation = NodeUtil.newCall(jsCode("checkImplementation"),
        classType, interfaceType);
    Node resultImplementation = NodeUtil.newCall(checkImplementation,
        NodeUtil.getFunctionDeclaration(root, "MyClass"));
    assertTrue(resultImplementation.toSource(null).equals("true"));

    // Test checking null type
    Node checkNull = NodeUtil.newCall(jsCode("checkNull"), Node.newNull());
    Node resultNull = NodeUtil.newCall(checkNull, Node.newNull());
    assertTrue(resultNull.toSource(null).equals("true"));

    // Test checking boolean value type
    Node checkBoolean = NodeUtil.newCall(jsCode("checkBoolean"),
        Node.newBoolean(true));
    Node resultBoolean = NodeUtil.newCall(checkBoolean, Node.newBoolean(true));
    assertTrue(resultBoolean.toSource(null).equals("true"));

    // Test checking number value type
    Node checkNumber = NodeUtil.newCall(jsCode("checkNumber"),
        Node.newNumber(1));
    Node resultNumber = NodeUtil.newCall(checkNumber, Node.newNumber(1));
    assertTrue(resultNumber.toSource(null).equals("true"));

    // Test checking string value type
    Node checkString = NodeUtil.newCall(jsCode("checkString"),
        Node.newString("test"));
    Node resultString = NodeUtil.newCall(checkString, Node.newString("test"));
    assertTrue(resultString.toSource(null).equals("true"));

    // Test checking void type
    Node checkVoid = NodeUtil.newCall(jsCode("checkVoid"),
        Node.newVoidExpression());
    Node resultVoid = NodeUtil.newCall(checkVoid, Node.newVoidExpression());
    assertTrue(resultVoid.toSource(null).equals("true"));

    // Test checking union type
    String unionSourceCode = "function MyFunction(a: number|string) {}";
    CompilerInput unionInput = new CompilerInput(null, null, null,
        unionSourceCode, null);
    Node unionRoot = newCompiler().parseTree(unionInput, null, null);
    FunctionType unionType = (FunctionType) NodeUtil.getFunctionDeclaration(
        unionRoot, "MyFunction").getJSType();

    Node checkUnion = NodeUtil.newCall(jsCode("checkUnion"),
        unionType, Node.newNumber(1));
    Node resultUnionNum = NodeUtil.newCall(checkUnion, Node.newNumber(1));
    assertTrue(resultUnionNum.toSource(null).equals("true"));

    Node checkUnionString = NodeUtil.newCall(jsCode("checkUnion"),
        unionType, Node.newString("test"));
    Node resultUnionString = NodeUtil.newCall(checkUnionString,
        Node.newString("test"));
    assertTrue(resultUnionString.toSource(null).equals("true"));
  }

  private AbstractCompiler newCompiler() {
    return newAbstractCompilerWithSourceMap(null);
  }

  private static Node jsCode(String prop) {
    return NodeUtil.newQualifiedNameNode("jscomp.typecheck." + prop, -1, -1);
  }
}