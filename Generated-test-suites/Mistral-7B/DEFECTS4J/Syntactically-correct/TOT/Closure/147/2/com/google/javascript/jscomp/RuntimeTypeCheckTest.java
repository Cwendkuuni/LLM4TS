import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import org.junit.Test;
import static org.junit.Assert.*;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() {
    String inputCode = "function Foo() {}";
    CompilerInput input = new CompilerInput(inputCode, "Foo.js");

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = compiler.parseTree(input);
    runtimeTypeCheck.process(input, root);

    Node expectedOutput = NodeUtil.parseCode("function Foo() { instance_of__Foo: true; }");
    Node actualOutput = compiler.getOutput(input);
    assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

  @Test
  public void testRuntimeTypeCheckWithInterfaceImplementation() {
    String inputCode = "function Bar() {} function Foo() {} " +
        "Foo.prototype.implements__Bar = true;";
    CompilerInput input = new CompilerInput(inputCode, "Foo.js");

    AbstractCompiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = compiler.parseTree(input);
    runtimeTypeCheck.process(input, root);

    Node expectedOutput = NodeUtil.parseCode("function Foo() {" +
        "  instance_of__Foo: true;" +
        "  implements__Bar: true;" +
        "}");
    Node actualOutput = compiler.getOutput(input);
    assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

  @Test
  public void testRuntimeTypeCheckWithUnionType() {
    String inputCode = "function Foo() {} function Bar() {} " +
        "function Baz() {} " +
        "function Quux(x) { if (checkType(x, [valueChecker('number'), classChecker('Foo')])) {" +
        "    return 'Foo';" +
        "  } else if (checkType(x, [valueChecker('string'), classChecker('Bar')])) {" +
        "    return 'Bar';" +
        "  } else if (checkType(x, [valueChecker('number'), classChecker('Baz')])) {" +
        "    return 'Baz';" +
        "  } else {" +
        "    throw new Error('Unexpected type.');" +
        "  }}" +
        "Quux(1); // returns 'Foo'" +
        "Quux('foo'); // throws an error.";

    CompilerInput input = new CompilerInput(inputCode, "Quux.js");

    AbstractCompiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = compiler.parseTree(input);
    runtimeTypeCheck.process(input, root);

    Node expectedOutput = NodeUtil.parseCode(inputCode.substring(0, inputCode.lastIndexOf("Quux(") + 8) +
        "return 'Foo';" +
        "Quux.prototype.checkType = checkType;");
    Node actualOutput = compiler.getOutput(input);
    assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

  @Test(expected = RuntimeException.class)
  public void testRuntimeTypeCheckWithUnknownType() {
    String inputCode = "function Foo() {} function Quux(x) {" +
        "  checkType(x, [classChecker('UnknownType')]);" +
        "}";

    CompilerInput input = new CompilerInput(inputCode, "Quux.js");

    AbstractCompiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = compiler.parseTree(input);
    runtimeTypeCheck.process(input, root);

    // The test should fail with a RuntimeException.
  }

  @Test
  public void testRuntimeTypeCheckWithExternClass() {
    String inputCode = "function Foo() {}" +
        "//^ exponent: 1:0:1: Type error: Unexpected identifier 'Foo'" +
        "// External definition for Foo.";

    CompilerInput input = new CompilerInput(inputCode, "Foo.js");

    AbstractCompiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = compiler.parseTree(input);
    runtimeTypeCheck.process(input, root);

    Node expectedOutput = NodeUtil.parseCode("function Foo() {" +
        "  instance_of__Foo: true;" +
        "}");
    Node actualOutput = compiler.getOutput(input);
    assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

  @Test
  public void testRuntimeTypeCheckWithInterfaceImplementationAndExternClass() {
    String inputCode = "function Bar() {} function Foo() {} " +
        "Foo.prototype.implements__Bar = true;" +
        "// External definition for Foo." +
        "//^ exponent: 1:0:1: Type error: Unexpected identifier 'Foo'" +
        "// End of external definition for Foo.";

    CompilerInput input = new CompilerInput(inputCode, "Foo.js");

    AbstractCompiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = compiler.parseTree(input);
    runtimeTypeCheck.process(input, root);

    Node expectedOutput = NodeUtil.parseCode("function Foo() {" +
        "  instance_of__Foo: true;" +
        "  implements__Bar: true;" +
        "}");
    Node actualOutput = compiler.getOutput(input);
    assertEquals(expectedOutput.toString(), actualOutput.toString());
  }
}