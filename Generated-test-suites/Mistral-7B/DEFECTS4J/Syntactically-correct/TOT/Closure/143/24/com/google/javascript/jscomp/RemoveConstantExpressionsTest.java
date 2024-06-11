import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.parser.Parser;

import java.util.List;

public class RemoveConstantExpressionsTest {

 private AbstractCompiler compiler;
 private Parser parser;

 @org.junit.Before
 public void setUp() {
 compiler = new AbstractCompiler();
 parser = new Parser(new SourceFile("Test.js", "testCode", null));
 }

 @org.junit.Test
 public void testSimpleExpression() {
 Node root = parser.parse(parser.getText());
 CompilerPass pass = new RemoveConstantExpressions(compiler);
 pass.process(null, root);
 List<Node> nodes = NodeTraversal.depthFirst(root);
 assertTrue(nodes.get(0).getType() == Token.EXPR_STATEMENT);
 assertTrue(nodes.get(1).getType() == Token.CALL_EXPRESSION);
 assertTrue(nodes.get(1).getFirstChild().getType() == Token.IDENTIFIER);
 assertTrue(nodes.get(1).getLastChild().getType() == Token.IDENTIFIER);
 }

 @org.junit.Test
 public void testComplexExpression() {
 Node root = parser.parse("1 + foo() * bar() + baz()");
 CompilerPass pass = new RemoveConstantExpressions(compiler);
 pass.process(null, root);
 List<Node> nodes = NodeTraversal.depthFirst(root);
 assertFalse(nodes.get(0).getType() == Token.EXPR_RESULT);
 assertTrue(nodes.get(0).getType() == Token.EXPR_STATEMENT);
 assertTrue(nodes.get(0).getFirstChild().getType() == Token.CALL_EXPRESSION);
 assertTrue(nodes.get(0).getLastChild().getType() == Token.CALL_EXPRESSION);
 assertTrue(nodes.get(1).getType() == Token.EXPR_STATEMENT);
 assertTrue(nodes.get(1).getFirstChild().getType() == Token.IDENTIFIER);
 assertTrue(nodes.get(2).getType() == Token.EXPR_STATEMENT);
 assertTrue(nodes.get(2).getFirstChild().getType() == Token.IDENTIFIER);
 }

 @org.junit.Test(expected = RuntimeException.class)
 public void testErrorScenario() {
 Node root = parser.parse("1 + (3 * 4) + foo()");
 CompilerPass pass = new RemoveConstantExpressions(compiler);
 pass.process(null, root);
 }
}