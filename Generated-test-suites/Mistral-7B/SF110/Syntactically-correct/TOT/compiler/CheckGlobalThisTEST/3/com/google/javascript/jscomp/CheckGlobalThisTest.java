import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.js.JsParser;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

 private static CheckerFactory checkerFactory = new CheckerFactory();
 private static Compiler compiler = checkerFactory.newCompiler();

 @Test
 public void testSafeUsesOfThis() {
 Node node = new JsParser(null).parseExpression("function safeThis() { this.a = 1; }");
 AbstractCompiler abstractCompiler = new AbstractCompiler(compiler) {
 @Override
 public NodeTraversal createTraversal(Node node, Node parent) {
 return new NodeTraversal(node, parent) {
 @Override
 public Callback getCallback(Node n) {
 return new CheckGlobalThis(this);
 }
 };
 }
 };
 abstractCompiler.traverse(node);
 assertFalse("No warnings expected for safe uses of this", abstractCompiler.getErrors().length > 0);
 }

 @Test
 public void testUnsafeUseOfThisInAssignment() {
 Node node = new JsParser(null).parseExpression("var a = this; a.b = 1;");
 AbstractCompiler abstractCompiler = new AbstractCompiler(compiler) {
 @Override
 public NodeTraversal createTraversal(Node node, Node parent) {
 return new NodeTraversal(node, parent) {
 @Override
 public Callback getCallback(Node n) {
 return new CheckGlobalThis(this);
 }
 };
 }
 };
 abstractCompiler.traverse(node);
 assertTrue("One warning expected for unsafe use of this in assignment", abstractCompiler.getErrors().length == 1);
 }

 @Test
 public void testUnsafeUseOfThisInPropertyAccess() {
 Node node = new JsParser(null).parseExpression("this.a.b = 1;");
 AbstractCompiler abstractCompiler = new AbstractCompiler(compiler) {
 @Override
 public NodeTraversal createTraversal(Node node, Node parent) {
 return new NodeTraversal(node, parent) {
 @Override
 public Callback getCallback(Node n) {
 return new CheckGlobalThis(this);
 }
 };
 }
 };
 abstractCompiler.traverse(node);
 assertTrue("One warning expected for unsafe use of this in property access", abstractCompiler.getErrors().length == 1);
}

 @Test
 public void testFunctionAnnotatedWithConstructor() {
 Node node = new JsParser(null).parseExpression("@constructor function SafeThisConstructor() { this.a = 1; }");
 AbstractCompiler abstractCompiler = new AbstractCompiler(compiler) {
 @Override
 public NodeTraversal createTraversal(Node node, Node parent) {
 return new NodeTraversal(node, parent) {
 @Override
 public Callback getCallback(Node n) {
 return new CheckGlobalThis(this);
 }
 };
 }
 };
 abstractCompiler.traverse(node);
 assertFalse("No warnings expected for constructor functions", abstractCompiler.getErrors().length > 0);
 }

 @Test
 public void testFunctionAnnotatedWithThis() {
 Node node = new JsParser(null).parseExpression("function SafeThisFunction() { @this this.a = 1; }");
 AbstractCompiler abstractCompiler = new AbstractCompiler(compiler) {
 @Override
 public NodeTraversal createTraversal(Node node, Node parent) {
 return new NodeTraversal(node, parent) {
 @Override
 public Callback getCallback(Node n) {
 return new CheckGlobalThis(this);
 }
 };
 }
 };
 abstractCompiler.traverse(node);
 assertFalse("No warnings expected for functions annotated with @this", abstractCompiler.getErrors().length > 0);
 }

 @Test
 public void testFunctionWithNoLogicalPlaceForThisAnnotation() {
 Node node = new JsParser(null).parseExpression("function SafeThisFunction() { this.a = 1; }");
 Node nodeWithThisAnnotation = new JsParser(null).parseExpression("function SafeThisFunction() { @this this.a = 1; }");
 AbstractCompiler abstractCompiler = new AbstractCompiler(compiler) {
 @Override
 public NodeTraversal createTraversal(Node node, Node parent) {
 NodeTraversal nodeTraversal = new NodeTraversal(node, parent) {
 @Override
 public Callback getCallback(Node n) {
 return new CheckGlobalThis(this);
 }
 };
 if (node.isFunction()) {
 return nodeTraversal;
 } else {
 return super.createTraversal(node, parent);
 }
 }
 };
 NodeTraversal nodeTraversal = abstractCompiler.createTraversal(nodeWithThisAnnotation, null);
 nodeTraversal.traverse();
 assertFalse("No warnings expected for functions with no logical place for this annotation", nodeTraversal.getErrors().length > 0);
 nodeTraversal = abstractCompiler.createTraversal(node, null);
 nodeTraversal.traverse();
 assertTrue("One warning expected for functions with no logical place for this annotation", nodeTraversal.getErrors().length == 1);
 }
}