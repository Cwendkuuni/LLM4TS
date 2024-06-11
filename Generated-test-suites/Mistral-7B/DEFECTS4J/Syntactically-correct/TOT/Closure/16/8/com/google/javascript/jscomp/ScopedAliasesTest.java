package com.google.javascript.jscomp.test;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.Node;

public class ScopedAliasesTest extends TestCase {

 private Compiler compiler;
 private HotSwapCompilerPass scopedAliasesPass;

 @Before
 public void setUp() {
 compiler = new Compiler();
 scopedAliasesPass = new ScopedAliases(compiler, null, null);
 }

 @Test
 public void testSimpleAlias() {
 String input = "goog.scope(function() {" +
 "  var dom = goog.dom;" +
 "  var DIV = dom.TagName.DIV;" +
 "  goog.dom.createElement(DIV);" +
 "});";
 String expectedOutput = "goog.dom.createElement(goog.dom.TagName.DIV);";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);

 assertEquals("Expected alias replacement to occur.", 1, outputNode.getChildCount());
 assertEquals("Expected alias replacement to be the correct node.",
 "goog.dom.createElement", outputNode.getChild(0).getQualifiedName());
 }

 @Test
 public void testAliasUsageInFunctionParameter() {
 String input = "function foo(goog) {" +
 "  goog.dom.createElement('DIV');" +
 "}" +
 "goog.scope(function() {" +
 "  var goog = {};" +
 "});";
 String expectedOutput = "function foo(undefined) {" +
 "  goog.dom.createElement('DIV');" +
 "}";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);

 assertEquals("Expected alias replacement to occur.", 1, outputNode.getChildCount());
 assertEquals("Expected alias replacement to be the correct node.",
 "function foo", outputNode.getChild(0).getQualifiedName());
 }

 @Test
 public void testAliasUsageInGlobalVariable() {
 String input = "goog.scope(function() {" +
 "  var goog = {};" +
 "});" +
 "goog.dom.createElement('DIV');";
 String expectedOutput = "goog.dom.createElement('DIV');";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);

 assertEquals("Expected alias replacement not to occur.", 1, outputNode.getChildCount());
 assertEquals("Expected alias replacement not to be the first child.",
 "goog.dom.createElement", outputNode.getChild(0).getQualifiedName());
 }

 @Test
 public void testAliasRedefinition() {
 String input = "goog.scope(function() {" +
 "  var goog = {};" +
 "  goog.dom = {};" +
 "  var DIV = goog.dom.TagName.DIV;" +
 "  goog.dom.createElement(DIV);" +
 "  goog.dom.createElement(DIV);" +
 "});";
 String expectedError = "JSC_GOOG_SCOPE_ALIAS_REDEFINED: DIV";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = null;
 try {
 outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);
 } catch (Compiler.CompilationError e) {
 // This is the expected behavior
 }

 assertTrue("Expected error to be thrown.", e instanceof Compiler.CompilationError);
 assertEquals("Expected error message to match.", expectedError, e.getMessage());
 }

 @Test
 public void testAliasUsageInFunctionBody() {
 String input = "goog.scope(function() {" +
 "  function foo() {" +
 "    return goog.dom.createElement('DIV');" +
 "  }" +
 "});";
 String expectedOutput = "function foo() {" +
 "  return goog.dom.createElement('DIV');" +
 "}";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);

 assertEquals("Expected alias replacement to occur.", 1, outputNode.getChildCount());
 assertEquals("Expected alias replacement to be the correct node.",
 "function foo", outputNode.getChild(0).getQualifiedName());
 }

 @Test
 public void testAliasUsageInForLoop() {
 String input = "goog.scope(function() {" +
 "  for (var i = 0; i < 10; i++) {" +
 "    goog.dom.createElement('DIV');" +
 "  }" +
 "});";
 String expectedOutput = "for (var i = 0; i < 10; i++) {" +
 "  goog.dom.createElement('DIV');" +
 "}";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);

 assertEquals("Expected alias replacement to occur.", 1, outputNode.getChildCount());
 assertEquals("Expected alias replacement to be the correct node.",
 "for", outputNode.getChild(0).getQualifiedName());
 }

 @Test
 public void testAliasUsageInConditionalExpression() {
 String input = "goog.scope(function() {" +
 "  var x = 10;" +
 "  goog.dom.createElement(x > 5 ? goog.dom.TagName.DIV : goog.dom.TagName.SPAN);" +
 "});";
 String expectedOutput = "goog.dom.createElement(x > 5 ? goog.dom.TagName.DIV : goog.dom.TagName.SPAN);";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);

 assertEquals("Expected alias replacement not to occur.", 1, outputNode.getChildCount());
 assertEquals("Expected alias replacement not to be the first child.",
 "goog.dom.createElement", outputNode.getChild(0).getQualifiedName());
 }

 @Test
 public void testAliasUsageInReturnStatement() {
 String input = "goog.scope(function() {" +
 "  function foo() {" +
 "    return goog.dom.createElement('DIV');" +
 "  }" +
 "  return foo();" +
 "});";
 String expectedError = "JSC_GOOG_SCOPE_USES_RETURN";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = null;
 try {
 outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);
 } catch (Compiler.CompilationError e) {
 // This is the expected behavior
 }

 assertTrue("Expected error to be thrown.", e instanceof Compiler.CompilationError);
 assertEquals("Expected error message to match.", expectedError, e.getMessage());
 }

 @Test
 public void testAliasUsageInThrowStatement() {
 String input = "goog.scope(function() {" +
 "  function foo() {" +
 "    throw new Error('test');" +
 "  }" +
 "  goog.dom.createElement('DIV');" +
 "  try {" +
 "    foo();" +
 "  } catch (e) {" +
 "    // Do nothing" +
 "  }" +
 "});";
 String expectedOutput = "goog.dom.createElement('DIV');" +
 "try {" +
 "  foo();" +
 "} catch (e) {" +
 "  // Do nothing" +
 "}";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);

 assertEquals("Expected alias replacement not to occur.", 1, outputNode.getChildCount());
 assertEquals("Expected alias replacement not to be the first child.",
 "goog.dom.createElement", outputNode.getChild(0).getQualifiedName());
 }

 @Test
 public void testAliasUsageInThisKeyword() {
 String input = "goog.scope(function() {" +
 "  this.foo = goog.dom.createElement('DIV');" +
 "});";
 String expectedError = "JSC_GOOG_SCOPE_REFERENCES_THIS";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = null;
 try {
 outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);
 } catch (Compiler.CompilationError e) {
 // This is the expected behavior
 }

 assertTrue("Expected error to be thrown.", e instanceof Compiler.CompilationError);
 assertEquals("Expected error message to match.", expectedError, e.getMessage());
 }

 @Test
 public void testAliasUsageInInvalidCallToScopeMethod() {
 String input = "goog.scope(function() {" +
 "  goog.scope(function() {});" +
 "});";
 String expectedError = "JSC_GOOG_SCOPE_USED_IMPROPERLY";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = null;
 try {
 outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);
 } catch (Compiler.CompilationError e) {
 // This is the expected behavior
 }

 assertTrue("Expected error to be thrown.", e instanceof Compiler.CompilationError);
 assertEquals("Expected error message to match.", expectedError, e.getMessage());
 }

 @Test
 public void testAliasUsageInInvalidCallToScopeMethodWithParameter() {
 String input = "goog.scope(function() {" +
 "  goog.scope(function(a) {});" +
 "});";
 String expectedError = "JSC_GOOG_SCOPE_USED_IMPROPERLY";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = null;
 try {
 outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);
 } catch (Compiler.CompilationError e) {
 // This is the expected behavior
 }

 assertTrue("Expected error to be thrown.", e instanceof Compiler.CompilationError);
 assertEquals("Expected error message to match.", expectedError, e.getMessage());
 }

 @Test
 public void testAliasUsageInValidCallToScopeMethodWithInvalidFunction() {
 String input = "goog.scope(function() {" +
 "  foo();" +
 "});";
 String expectedError = "JSC_GOOG_SCOPE_USED_IMPROPERLY";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = null;
 try {
 outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);
 } catch (Compiler.CompilationError e) {
 // This is the expected behavior
 }

 assertTrue("Expected error to be thrown.", e instanceof Compiler.CompilationError);
 assertEquals("Expected error message to match.", expectedError, e.getMessage());
 }

 @Test
 public void testAliasUsageInCallToScopeMethodWithInvalidParameter() {
 String input = "goog.scope(function() {" +
 "  goog.scope(123);" +
 "});";
 String expectedError = "JSC_GOOG_SCOPE_USED_IMPROPERLY";

 Node inputNode = NodePrinter.print(input);
 Node outputNode = null;
 try {
 outputNode = compiler.process(NodePrinter.print(expectedOutput), inputNode, null);
 } catch (Compiler.CompilationError e) {
 // This is the expected behavior
 }

 assertTrue("Expected error to be thrown.", e instanceof Compiler.CompilationError);
 assertEquals("Expected error message to match.", expectedError, e.getMessage());
 }
}