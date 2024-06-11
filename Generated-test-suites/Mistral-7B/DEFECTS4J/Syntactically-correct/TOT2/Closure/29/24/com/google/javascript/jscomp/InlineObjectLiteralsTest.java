import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerOptions options;
  private Node rootNode;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
    rootNode = NodePrinter.printToString(
        "({'a': 1, 'b': 2}).c = 3;");
    compiler.compile(null, rootNode, null);
  }

  // Test basic functionality of InlineObjectLiterals
  @Test
  public void testInlineObjectLiterals() {
    CompilerPass inlineObjectLiteralsPass =
        new InlineObjectLiterals(compiler, () -> "tempName");
    inlineObjectLiteralsPass.process(null, rootNode);
    assertEquals("3;var a=1;var b=2;var c=3;", NodePrinter.printToString(compiler.getOutput()));
  }

  // Test object literal with no references
  @Test
  public void testInlineObjectLiteralNoReferences() {
    String input = "var obj = {a: 1};";
    Node rootNode = NodePrinter.parse(input);
    CompilerPass inlineObjectLiteralsPass =
        new InlineObjectLiterals(compiler, () -> "tempName");
    inlineObjectLiteralsPass.process(null, rootNode);
    assertEquals("var a=1;", NodePrinter.printToString(compiler.getOutput()));
  }

  // Test object literal with getter and setter properties
  @Test
  public void testInlineObjectLiteralWithGetSetProperties() {
    String input = "var obj = {get a() { return 1; }, set a(val) {}};";
    Node rootNode = NodePrinter.parse(input);
    CompilerPass inlineObjectLiteralsPass =
        new InlineObjectLiterals(compiler, () -> "tempName");
    inlineObjectLiteralsPass.process(null, rootNode);
    assertEquals("var a=undefined;", NodePrinter.printToString(compiler.getOutput()));
  }

  // Test object literal with circular references
  @Test(expected = RuntimeException.class)
  public void testInlineObjectLiteralWithCircularReferences() {
    String input = "var obj1 = { a: obj2 }; var obj2 = { a: obj1 };";
    Node rootNode = NodePrinter.parse(input);
    CompilerPass inlineObjectLiteralsPass =
        new InlineObjectLiterals(compiler, () -> "tempName");
    inlineObjectLiteralsPass.process(null, rootNode);
  }

  // Test object literal with undefined property
  @Test
  public void testInlineObjectLiteralWithUndefinedProperty() {
    String input = "var obj = {a: undefined};";
    Node rootNode = NodePrinter.parse(input);
    CompilerPass inlineObjectLiteralsPass =
        new InlineObjectLiterals(compiler, () -> "tempName");
    inlineObjectLiteralsPass.process(null, rootNode);
    assertEquals("var a=undefined;", NodePrinter.printToString(compiler.getOutput()));
  }

  // Test object literal with empty property
  @Test
  public void testInlineObjectLiteralWithEmptyProperty() {
    String input = "var obj = {a: {}};";
    Node rootNode = NodePrinter.parse(input);
    CompilerPass inlineObjectLiteralsPass =
        new InlineObjectLiterals(compiler, () -> "tempName");
    inlineObjectLiteralsPass.process(null, rootNode);
    assertEquals("var a={};", NodePrinter.printToString(compiler.getOutput()));
  }

  // Test object literal with null property
  @Test
  public void testInlineObjectLiteralWithNullProperty() {
    String input = "var obj = {a: null};";
    Node rootNode = NodePrinter.parse(input);
    CompilerPass inlineObjectLiteralsPass =
        new InlineObjectLiterals(compiler, () -> "tempName");
    inlineObjectLiteralsPass.process(null, rootNode);
    assertEquals("var a=null;", NodePrinter.printToString(compiler.getOutput()));
  }

  // Test object literal with non-object literal value
  @Test
  public void testInlineObjectLiteralWithNonObjectLiteralValue() {
    String input = "var obj = {a: 1, b: 'hello'};";
    Node rootNode = NodePrinter.parse(input);
    CompilerPass inlineObjectLiteralsPass =
        new InlineObjectLiterals(compiler, () -> "tempName");
    inlineObjectLiteralsPass.process(null, rootNode);
    assertEquals("var a=1;var b='hello';", NodePrinter.printToString(compiler.getOutput()));
  }

  // Test object literal with array value
  @Test
  public void testInlineObjectLiteralWithArrayValue() {
    String input = "var obj = {a: [1, 2, 3]};";
    Node rootNode = NodePrinter.parse(input);
    CompilerPass inlineObjectLiteralsPass =
        new InlineObjectLiterals(compiler, () -> "tempName");
    inlineObjectLiteralsPass.process(null, rootNode);
    assertEquals("var a=[1,2,3];", NodePrinter.printToString(compiler.getOutput()));
  }

  // Test object literal with function value
  @Test
  public void testInlineObjectLiteralWithFunctionValue() {
    String input = "var obj = {a: function() {}};";
    Node rootNode = NodePrinter.parse(input);
    CompilerPass inlineObjectLiteralsPass =
        new InlineObjectLiterals(compiler, () -> "tempName");
    inlineObjectLiteralsPass.process(null, rootNode);
    assertEquals("function(){};var a=function(){};", NodePrinter.printToString(compiler.getOutput()));
  }

  // Test object literal with string value
  @Test
  public void testInlineObjectLiteralWithStringValue() {
    String input = "var obj = {a: 'hello'};";
    Node rootNode = NodePrinter.parse(input);
    CompilerPass inlineObjectLiteralsPass =
        new InlineObjectLiterals(compiler, () -> "tempName");
    inlineObjectLiteralsPass.process(null, rootNode);
    assertEquals("var a='hello';", NodePrinter.printToString(compiler.getOutput()));
  }
}