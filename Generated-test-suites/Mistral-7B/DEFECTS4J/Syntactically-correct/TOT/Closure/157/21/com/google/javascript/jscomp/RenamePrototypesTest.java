import static org.junit.Assert.*;
import static com.google.javascript.jscomp.compiler.TestCompiler.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

import java.util.Arrays;

public class RenamePrototypesTest {

  @org.junit.Test
  public void testRenamePrototypes() {
    String sourceCode = ""
        + "function Foo() {}\n"
        + "Foo.prototype.bar = function() {}\n"
        + "Foo.prototype.baz = 'abc'\n"
        + "var x = new Foo();\n"
        + "x.bar();\n"
        + "alert(x.baz);\n"
        + "\n"
        + "function Foo2() {}\n"
        + "Foo2.prototype.qux = function() {}\n"
        + "var y = new Foo2();\n"
        + "y.qux();\n"
        + "\n"
        + "function Foo3() {}\n"
        + "Foo3.prototype._qux = function() {}\n"
        + "var z = new Foo3();\n"
        + "z._qux();\n"
        + "\n"
        + "function Foo4() {}\n"
        + "Foo4.prototype.qux = function() {}\n"
        + "Foo4.prototype.baz = 'def';\n"
        + "var a = new Foo4();\n"
        + "a.qux();\n"
        + "alert(a.baz);\n"
        + "\n"
        + "function Foo5() {}\n"
        + "Foo5.prototype.bar = function() {}\n"
        + "Foo5.prototype.prototype.qux = function() {}\n"
        + "var b = new Foo5();\n"
        + "b.bar();\n"
        + "b.qux();\n";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler();

    // Compile the code once to generate prototype names for reuse
    compiler.compile(sourceCode, options, null);

    // Test that the code compiles without errors
    compiler.compile(sourceCode, options, null);

    // Perform the RenamePrototypes pass
    CompilerPass rp = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(rp);

    // Compile the code again, this time with the RenamePrototypes pass
    compiler.compile(sourceCode, options, null);

    // Check that the code compiles without errors
    assertCompilesTo("", compiler.toSource(compiler.getIntermediateRepresentation()));

    // Check that the prototype property names have been renamed
    VariableMap usedRenameMap = rp.getPropertyMap();
    String[] expectedRenames = {"baz", "qux", "_qux"};
    assertArrayEquals("Unexpected number of renamed properties", expectedRenames.length, usedRenameMap.size().intValue());
    String[] actualRenames = new String[usedRenameMap.size().intValue()];
    int i = 0;
    for (Map.Entry<String, String> entry : usedRenameMap.entrySet()) {
      actualRenames[i++] = entry.getKey();
    }
    assertArrayEquals("Incorrectly renamed properties", Arrays.asList(expectedRenames).toArray(new String[expectedRenames.length]), Arrays.asList(actualRenames).toArray(new String[actualRenames.length]));

    // Check that the object literal property names have not been renamed
    Node ast = compiler.getAbstractSyntaxTree();
    for (Iterator<Node> it = ast.getDescendants().iterator(); it.hasNext(); ) {
      Node n = it.next();
      if (n.getType() == Token.OBJECTLIT && n.getFirstChild().getFirstChild() != null) {
        for (Node prop = n.getFirstChild().getFirstChild();
             prop != null; prop = prop.getNext()) {
          String propName = prop.getFirstChild().getString();
          String actualRename = usedRenameMap.lookupNewName(propName);
          assertNull("Unexpectedly renamed object literal property", actualRename);
        }
      }
    }
  }
}