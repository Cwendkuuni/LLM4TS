import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Node;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObject() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");

    String source = "var obj = {a: 1, b: 2};";
    Node root = Parser.parse(source);
    pass.process(null, root);

    Node expectedResult = IR.assignment(
        IR.name("obj"),
        IR.objectLit(
            IR.property(
                IR.name("a"),
                IR.intLiteral(1)
            ),
            IR.property(
                IR.name("b"),
                IR.intLiteral(2)
            )
        )
    );

    Node actualResult = NodeUtil.getFirstChild(root);
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @Test
  public void testInlineObjectWithFunctionProperty() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");

    String source = "var obj = {a: 1, fn: function() {}};";
    Node root = Parser.parse(source);
    pass.process(null, root);

    Node expectedResult = IR.assignment(
        IR.name("obj"),
        IR.objectLit(
            IR.property(
                IR.name("a"),
                IR.intLiteral(1)
            ),
            IR.property(
                IR.name("fn"),
                IR.function(
                    null, // Not functional interfaces
                    null, // No arguments
                    null, // No return type
                    null // Empty body
                )
            )
        )
    );

    Node actualResult = NodeUtil.getFirstChild(root);
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @Test
  public void testInlineObjectWithCycle() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");

    String source = "var obj1 = {a: {b: obj1}};";
    Node root = Parser.parse(source);
    try {
      pass.process(null, root);
      fail("Expected InliningBehavior.InliningError to be thrown");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectWithSelfReference() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");

    String source = "var obj = {a: function() {return this;}, b: 1};";
    Node root = Parser.parse(source);
    pass.process(null, root);

    Node expectedResult = IR.assignment(
        IR.name("obj"),
        IR.objectLit(
            IR.property(
                IR.name("a"),
                IR.function(
                    null, // Not functional interfaces
                    null, // No arguments
                    null, // No return type
                    IR.block(
                        IR.returnNode(IR.thisNode())
                    )
                )
            ),
            IR.property(
                IR.name("b"),
                IR.intLiteral(1)
            )
        )
    );

    Node actualResult = NodeUtil.getFirstChild(root);
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @Test
  public void testInlineObjectWithExportedVariable() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");

    String source = "var obj = {exported: 1};\nexport let x = obj.exported;";
    Node root = Parser.parse(source);
    pass.process(null, root);

    Node expectedResult = IR.assignment(
        IR.name("obj"),
        IR.objectLit(
            IR.property(
                IR.name("exported"),
                IR.intLiteral(1)
            )
        )
    );

    Node actualResult = NodeUtil.getFirstChild(root);
    assertEquals(expectedResult.toString(), actualResult.toString());

    Node xAssignment = NodeUtil.getFirstChild(NodeUtil.getFirstChild(root));
    assertEquals("x", xAssignment.getLastChild().getString());
    assertEquals(1, xAssignment.getLastChild().getFirstChild().getInt());
  }

  @Test
  public void testInlineObjectWithGlobalVariable() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");

    String source = "var obj = {glob: window.g};";
    Node root = Parser.parse(source);
    pass.process(null, root);

    Node expectedResult = IR.assignment(
        IR.name("obj"),
        IR.objectLit(
            IR.property(
                IR.name("glob"),
                IR.globalGet("g")
            )
        )
    );

    Node actualResult = NodeUtil.getFirstChild(root);
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectWithInvalidPropertyName() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");

    String source = "var obj = {invalid property name: 1};";
    Node root = Parser.parse(source);
    pass.process(null, root);

    fail("Expected InliningBehavior.InvalidPropertyNameError to be thrown");
  }
}