import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Test safe usage of 'this'
    String safeCode = "function Foo() {}\n" +
                      "Foo.prototype.bar = function() {}\n" +
                      "function FooConstructor() {}\n" +
                      "FooConstructor.prototype.baz = function() {}\n" +
                      "var FooVar = function() {}\n" +
                      "FooVar.prototype.qux = function() {}\n" +
                      "function FooFunction() {}\n" +
                      "FooFunction.prototype.quux = function() {}\n" +
                      "var FooVarPrototype = {quuz: function() {}}\n";
    Node root = Parser.parseJavaScript(safeCode, null, null).body;
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse();
    assertNull(checkGlobalThis.assignLhsChild);

    // Test unsafe usage of 'this'
    String unsafeCode = "function evil() { this.useful = undefined; }";
    Node unsafeRoot = Parser.parseJavaScript(unsafeCode, null, null).body;
    NodeTraversal unsafeTraversal = new NodeTraversal(unsafeRoot, checkGlobalThis);
    unsafeTraversal.traverse();
    assertTrue(checkGlobalThis.shouldReportThis(unsafeRoot.getFirstChild()));

    // Test 'this' as an assignment target
    String assignLeftCode = "var a = function() { this.useful = undefined; }";
    Node assignLeftRoot = Parser.parseJavaScript(assignLeftCode, null, null).body;
    Node assignLeftLhs = assignLeftRoot.getFirstChild();
    Node assignLeftThis = assignLeftLhs.getFirstChild();
    assertTrue(checkGlobalThis.shouldReportThis(assignLeftThis));

    // Test 'this' as a property access target
    String getCode = "var a = { getThis: function() { return this; } };\n" +
                      "a.getThis.useful = undefined;";
    Node getRoot = Parser.parseJavaScript(gCode, null, null).body;
    Node getThis = getRoot.getFirstChild().getLastChild();
    Node getThisProp = getThis.getLastChild();
    assertTrue(checkGlobalThis.shouldReportThis(getThisProp));
  }

  @Test
  public void testNoTraversal() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Test non-function nodes
    String nonFunctionCode = "var x = 5;";
    Node nonFunctionRoot = Parser.parseJavaScript(nonFunctionCode, null, null).body;
    NodeTraversal nonFunctionTraversal = new NodeTraversal(nonFunctionRoot, checkGlobalThis);
    assertFalse(nonFunctionTraversal.shouldTraverse(null, nonFunctionRoot, null));

    // Test non-global contexts
    String nonGlobalCode = "function Foo() {}\n" +
                           "function FooInner() {}\n";
    Node nonGlobalRoot = Parser.parseJavaScript(nonGlobalCode, null, null).body;
    NodeTraversal nonGlobalTraversal = new NodeTraversal(nonGlobalRoot, checkGlobalThis);
    assertFalse(nonGlobalTraversal.shouldTraverse(null, nonGlobalRoot, null));

    // Test prototype lending
    String prototypeLendingCode = "function Foo() {}\n" +
                                  "var p = {\n" +
                                  "  prototype: {\n" +
                                  "    bar: function() {}\n" +
                                  "  }\n" +
                                  "};\n" +
                                  "Foo.prototype = p.prototype;\n";
    Node prototypeLendingRoot = Parser.parseJavaScript(prototypeLendingCode, null, null).body;
    NodeTraversal prototypeLendingTraversal = new NodeTraversal(prototypeLendingRoot, checkGlobalThis);
    assertFalse(prototypeLendingTraversal.shouldTraverse(null, prototypeLendingRoot, null));

    // Test nested assignments
    String nestedAssignmentsCode = "var a = {\n" +
                                    "  b: function() {\n" +
                                    "    this.useful = undefined;\n" +
                                    "  }\n" +
                                    "};\n" +
                                    "var b = a.b;\n";
    Node nestedAssignmentsRoot = Parser.parseJavaScript(nestedAssignmentsCode, null, null).body;
    Node nestedAssignmentsLhs = nestedAssignmentsRoot.getFirstChild();
    Node nestedAssignmentsThis = nestedAssignmentsLhs.getLastChild();
    assertTrue(checkGlobalThis.shouldReportThis(nestedAssignmentsThis));

    // Test getters with 'prototype' property
    String getterWithPrototypeCode = "function Foo() {}\n" +
                                      "Foo.prototype = {\n" +
                                      "  get useful() { return this.prototype; }\n" +
                                      "}";
    Node getterWithPrototypeRoot = Parser.parseJavaScript(getterWithPrototypeCode, null, null).body;
    Node getterWithPrototypeLhs = getterWithPrototypeRoot.getFirstChild();
    Node getterWithPrototypeGet = getterWithPrototypeLhs.getLastChild();
    assertTrue(checkGlobalThis.shouldReportThis(getterWithPrototypeGet));
  }
}