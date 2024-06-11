import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {

 private Compiler compiler;
 private NodeFactory nodeFactory;

 @Before
 public void setUp() {
 compiler = new Compiler();
 compiler.setOption(CompilerOptions.LanguageMode.ES5);
 nodeFactory = compiler.getFactory();
 }

 @Test
 public void testRemoveConstantExpressions() {
 // Test case 1: Simple expression statement with constant expression
 Node constantExpression = nodeFactory.createBinaryOp(
 Node.OP_PLUS,
 nodeFactory.createNumberLiteral(1),
 nodeFactory.createCall(nodeFactory.createName("foo"), Lists.newArrayList()),
 nodeFactory.createCall(nodeFactory.createName("bar"), Lists.newArrayList())
 );
 Node root = nodeFactory.createStatement(nodeFactory.createExpressionStatement(constantExpression));

 RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
 NodeTraversal.traverse(pass, root, null);
 Node simplifiedRoot = pass.getResult().getOutput();

 assertEquals(
 "foo();bar()",
 nodeFactory.getSourceCode(simplifiedRoot).getCode()
 );

 // Test case 2: Complex expression statement with constant expressions
 Node complexExpression = nodeFactory.createBinaryOp(
 Node.OP_PLUS,
 nodeFactory.createBinaryOp(Node.OP_PLUS, nodeFactory.createNumberLiteral(1), nodeFactory.createCall(nodeFactory.createName("foo"), Lists.newArrayList())),
 nodeFactory.createBinaryOp(Node.OP_PLUS, nodeFactory.createCall(nodeFactory.createName("bar"), Lists.newArrayList()), nodeFactory.createNumberLiteral(2))
 );
 Node complexRoot = nodeFactory.createStatement(nodeFactory.createExpressionStatement(complexExpression));

 RemoveConstantExpressions pass2 = new RemoveConstantExpressions(compiler);
 NodeTraversal.traverse(pass2, complexRoot, null);
 Node simplifiedComplexRoot = pass2.getResult().getOutput();

 assertEquals(
 "foo();bar()",
 nodeFactory.getSourceCode(simplifiedComplexRoot).getCode()
 );

 // Test case 3: Error scenario - expression statement with undefined function
 Node undefinedFunctionCall = nodeFactory.createCall(nodeFactory.createName("undefinedFunction"), Lists.newArrayList());
 Node errorRoot = nodeFactory.createStatement(nodeFactory.createExpressionStatement(undefinedFunctionCall));

 RemoveConstantExpressions pass3 = new RemoveConstantExpressions(compiler);
 try {
 NodeTraversal.traverse(pass3, errorRoot, null);
 fail("RemoveConstantExpressions should not process undefined functions");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}