import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

 private static class TestCompiler extends AbstractCompiler {
  @Override
  public CheckLevel getOptions() {
    return CheckLevel.WARNT;
  }
 }

 @Test
 public void testCheckGlobalThis_FunctionWithThisAnnotation() {
  String code = "function MyFunction() { @this }";
  TestCompiler compiler = new TestCompiler();
  NodeTraversal traversal = new NodeTraversal(compiler);
  Node root = compiler.parse(code, null, null);
  traversal.traverse(root);
  assertTrue(compiler.getErrors().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithPrototypeMethod() {
  String code = "function MyFunction() { this.prototype.myMethod = function() { } }";
  TestCompiler compiler = new TestCompiler();
  NodeTraversal traversal = new NodeTraversal(compiler);
  Node root = compiler.parse(code, null, null);
  traversal.traverse(root);
  assertTrue(compiler.getErrors().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithThisKeywordOnLeftSideOfAssignment() {
  String code = "var a = function() { this = {} };";
  TestCompiler compiler = new TestCompiler();
  NodeTraversal traversal = new NodeTraversal(compiler);
  Node root = compiler.parse(code, null, null);
  traversal.traverse(root);
  assertFalse(compiler.getErrors().isEmpty());
  assertEquals(1, compiler.getErrors().size());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithThisKeywordOnRightSideOfAssignment() {
  String code = "var a = {}; function b() { a = this; }";
  TestCompiler compiler = new TestCompiler();
  NodeTraversal traversal = new NodeTraversal(compiler);
  Node root = compiler.parse(code, null, null);
  traversal.traverse(root);
  assertTrue(compiler.getErrors().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithThisKeywordAsFunctionName() {
  String code = "function thisFunction() { }";
  TestCompiler compiler = new TestCompiler();
  NodeTraversal traversal = new NodeTraversal(compiler);
  Node root = compiler.parse(code, null, null);
  traversal.traverse(root);
  assertTrue(compiler.getErrors().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithNoReasonableThisAnnotation() {
  String code = "function MyFunction() { }";
  TestCompiler compiler = new TestCompiler();
  NodeTraversal traversal = new NodeTraversal(compiler);
  Node root = compiler.parse(code, null, null);
  traversal.traverse(root);
  assertTrue(compiler.getErrors().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithThisKeywordOnLeftSideOfPropertyAccess() {
  String code = "var a = {}; function b() { a.thisProperty = 5; }";
  TestCompiler compiler = new TestCompiler();
  NodeTraversal traversal = new NodeTraversal(compiler);
  Node root = compiler.parse(code, null, null);
  traversal.traverse(root);
  assertTrue(compiler.getErrors().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithThisKeywordOnRightSideOfPropertyAccess() {
  String code = "var a = {}; function b() { a = this.anotherProperty; }";
  TestCompiler compiler = new TestCompiler();
  NodeTraversal traversal = new NodeTraversal(compiler);
  Node root = compiler.parse(code, null, null);
  traversal.traverse(root);
  assertTrue(compiler.getErrors().isEmpty());
 }
}