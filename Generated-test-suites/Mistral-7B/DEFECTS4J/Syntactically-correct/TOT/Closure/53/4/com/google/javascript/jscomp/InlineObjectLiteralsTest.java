import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: function() {" +
        "      console.log('Hello World');" +
        "    }" +
        "  };" +
        "  var c = obj.a;" +
        "  obj.b();" +
        "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    // Register the test pass
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tmp_");
    compiler.addPass(InlineObjectLiterals.class.getName());

    Node externs = null;
    Node root = new Node(Token.SCRIPTLET, null, null);
    compiler.compile(externs, root, jsCode, options);

    // Check that the objectliteral has been inlined correctly
    Node fooFunction = root.getFirstChild();
    Node body = fooFunction.getFirstChild();
    Node objAssignment = body.getFirstChild();
    Node objInlineA = objAssignment.getFirstChild();
    Node objInlineB = objAssignment.getNext();

    // Check that 'a' has been inlined as a separate variable
    Node inlineA = objInlineA;
    assertEquals("a", inlineA.getFirstChild().getString());
    assertNotNull(inlineA.getNext());

    // Check that 'b' has been inlined as a separate variable
    Node inlineB = objInlineB;
    assertEquals("b", inlineB.getFirstChild().getString());
    assertEquals(Token.FUNCTION, inlineB.getFirstChild().getType());
    Node functionNode = inlineB.getFirstChild().getFirstChild();
    assertEquals("console.log('Hello World')", functionNode.getString());

    // Check that 'c' is a simple variable assignment
    Node cAssignment = body.getChildAtIndex(2);
    Node cVar = cAssignment.getFirstChild();
    assertEquals("c", cVar.getString());
    assertNotNull(cVar.getNext());

    // Check that 'obj.b()' has been transformed to 'inlineB()'
    Node callNode = body.getChildAtIndex(3);
    Node callFunction = callNode.getFirstChild();
    assertEquals("inlineB", callFunction.getString());
    assertNotNull(callNode.getNext());

    // Check that the generated variables are not used elsewhere
    Node rootScope = root.getFirstChild();
    for (Node child = rootScope.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getFirstChild() != null &&
          (child.getFirstChild().getType() == Token.VAR &&
           (child.getFirstChild().getFirstChild().getString().startsWith("JSCompiler_object_inline_")))) {
        fail("Variable should not be used elsewhere: " + child.getFirstChild().getString());
      }
    }
  }

  @Test
  public void testInlineObjectLiteralWithGetters() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    get a() {" +
        "      return 1;" +
        "    }," +
        "    get b() {" +
        "      return 2;" +
        "    }" +
        "  }";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    // Register the test pass
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tmp_");
    compiler.addPass(InlineObjectLiterals.class.getName());

    Node externs = null;
    Node root = new Node(Token.SCRIPTLET, null, null);
    compiler.compile(externs, root, jsCode, options);

    // Check that the objectliteral has been inlined correctly
    Node fooFunction = root.getFirstChild();
    Node body = fooFunction.getFirstChild();
    Node objAssignment = body.getFirstChild();

    // Check that 'a' has been inlined as a separate variable
    Node inlineA = objAssignment.getFirstChild();
    assertEquals("a", inlineA.getFirstChild().getString());
    assertNotNull(inlineA.getNext());

    // Check that 'b' has been inlined as a separate variable
    Node inlineB = objAssignment.getNext();
    assertEquals("b", inlineB.getFirstChild().getString());
    assertNotNull(inlineB.getNext());

    // Check that 'obj.a' and 'obj.b' have been transformed to 'inlineA' and 'inlineB' respectively
    Node getA = body.getChildAtIndex(1);
    Node getB = body.getChildAtIndex(2);
    Node aAssignment = getA.getNext();
    Node bAssignment = getB.getNext();

    assertEquals("inlineA", aAssignment.getFirstChild().getString());
    assertEquals("inlineB", bAssignment.getFirstChild().getString());
  }

  @Test
  public void testInlineObjectLiteralWithCycle() {
    String jsCode = "function foo() {" +
        "  var objA = {" +
        "    a: 1," +
        "    b: objB" +
        "  };" +
        "  var objB = {" +
        "    b: objA" +
        "  };" +
        "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    // Register the test pass
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tmp_");
    compiler.addPass(InlineObjectLiterals.class.getName());

    Node externs = null;
    Node root = new Node(Token.SCRIPTLET, null, null);
    try {
      compiler.compile(externs, root, jsCode, options);
      fail("Expected InliningBehavior to fail due to object cycles.");
    } catch (RuntimeException e) {
      // Pass
    }
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: function() {" +
        "      return this;" +
        "    }," +
        "    b: obj.a" +
        "  };" +
        "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    // Register the test pass
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tmp_");
    compiler.addPass(InlineObjectLiterals.class.getName());

    Node externs = null;
    Node root = new Node(Token.SCRIPTLET, null, null);
    try {
      compiler.compile(externs, root, jsCode, options);
      fail("Expected InliningBehavior to fail due to self-referential objects.");
    } catch (RuntimeException e) {
      // Pass
    }
  }

  @Test
  public void testInlineObjectLiteralWithComplexObject() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {" +
        "      c: 2," +
        "      d: {" +
        "        e: 3," +
        "        f: function() {" +
        "          return 'Hello World';" +
        "        }" +
        "      }" +
        "    }" +
        "  };" +
        "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    // Register the test pass
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tmp_");
    compiler.addPass(InlineObjectLiterals.class.getName());

    Node externs = null;
    Node root = new Node(Token.SCRIPTLET, null, null);
    compiler.compile(externs, root, jsCode, options);

    // Check that the objectliteral has been inlined correctly
    Node fooFunction = root.getFirstChild();
    Node body = fooFunction.getFirstChild();
    Node objAssignment = body.getFirstChild();

    // Check that 'a' has been inlined as a separate variable
    Node inlineA = objAssignment.getFirstChild();
    assertEquals("a", inlineA.getFirstChild().getString());
    assertNotNull(inlineA.getNext());

    // Check that 'b.c' has been inlined as a separate variable
    Node inlineBC = objAssignment.getChildAtIndex(1);
    Node inlineC = inlineBC.getFirstChild();
    assertEquals("b.c", inlineC.getFirstChild().getString());
    assertNotNull(inlineC.getNext());

    // Check that 'b.d.e' has been inlined as a separate variable
    Node inlineBDE = objAssignment.getChildAtIndex(2);
    Node inlineE = inlineBDE.getFirstChild();
    assertEquals("b.d.e", inlineE.getFirstChild().getString());
    assertNotNull(inlineE.getNext());

    // Check that 'b.d.f' has been inlined as a separate variable
    Node inlineBDF = inlineBDE.getNext();
    Node inlineFFunction = inlineBDF.getFirstChild();
    assertEquals("b.d.f", inlineFFunction.getString());
    assertNotNull(inlineFFunction.getNext());

    // Check that 'obj.b.f()' has been transformed to 'inlineFFunction()'
    Node callNode = body.getChildAtIndex(4);
    Node callFunction = callNode.getFirstChild();
    assertEquals("inlineFFunction", callFunction.getString());
    assertNotNull(callNode.getNext());
  }
}