import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.DefinitionSite;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.ObjectType;

public class DevirtualizePrototypeMethodsTest {

  private static final String TEST_CASE_CODE =
      "function Foo() {}\n" +
      "Foo.prototype.bar = function(a) { this.b = a; };\n" +
      "var f = new Foo();\n" +
      "f.bar(5);";

  @Test
  public void testDevirtualizePrototypeMethods() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = NodeFactory.parse(TEST_CASE_CODE);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);

    compiler.compile(root, options, null);

    // Ensure no errors were thrown during compilation
    assertEquals("Expected 0 but got " + compiler.getErrorCount() + " errors.",
        0, compiler.getErrorCount());

    // Check the output of the compiled code
    Node output = compiler.getCompiledCode();
    Node expectedOutput = NodeFactory.parse(
        "function JSCompiler_StaticMethods_bar(self, a) {" +
        "  self.b = a;" +
        "}" +
        "var f = new Foo();" +
        "JSCompiler_StaticMethods_bar(f, 5)"
    );

    // Check the output type of the compiled code
    ObjectType outputType = (ObjectType) output.getJSType();
    ObjectType expectedOutputType = NodeFactory.createObjectType(
        "JSCompiler_StaticMethods", new FunctionType(null, null, null)
    );
    assertEquals("Output types do not match.", expectedOutputType, outputType);

    // Check that the 'bar' method was rewritten in the output
    Node barMethod = findNodeByType(output, new FunctionType(
        ObjectType.prototype, null, new JSType[] { ObjectType.ANY, Token.INT_LITERAL }));
    assertNotNull("Expected 'bar' method to be rewritten.", barMethod);

    // Check that the original 'bar' method was removed from the output
    Node originalBarMethod = findNodeByType(output, new FunctionType(
        NodeUtil.getPrototypeType(Foo.TYPE), null, new JSType[] { NodeUtil.OBJECT_TYPE, Token.INT_LITERAL }));
    assertNull("Expected 'bar' method to be removed.", originalBarMethod);

    // Check that the 'bar' method call site was rewritten in the output
    Node callSite = findNodeByType(output, new NodeType(NodeUtil.GET_PROP,
        NodeUtil.getGlobalType(), new String("Foo"), new String("prototype"), new String("bar")));
    assertNotNull("Expected 'bar' method call site to be rewritten.", callSite);
    assertEquals("Expected 'this' to be replaced with 'f'.", "f",
        callSite.getFirstChild().getString());
  }
}