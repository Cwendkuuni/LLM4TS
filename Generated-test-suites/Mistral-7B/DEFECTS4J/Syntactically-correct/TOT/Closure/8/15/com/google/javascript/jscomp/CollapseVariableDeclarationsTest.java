package com.google.javascript.jscomp.test;

import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import junit.framework.TestCase;

import java.util.List;

public class CollapseVariableDeclarationsTest extends TestCase {

 private Compiler compiler;
 private CompilerPass collapseVariableDeclarations;

 @Before
 public void setUp() {
 compiler = new Compiler();
 collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
 }

 @Test
 public void testCollapseSingleVariableDeclaration() {
 Node root = new Node("function() {}", new Token(0, 0, Token.FUNCTION));
 Node varNode = new Node("var a;", new Token(0, 1, Token.VAR));
 root.addChildToBack(varNode);

 compiler.process(null, root);
 Compiler.ProcessResult result = compiler.getProcessResult();
 String output = result.getCompiledSource();
 assertEquals("function() {" + System.lineSeparator() +
             "  var a;" + System.lineSeparator() +
             "}", output);
 }

 @Test
 public void testCollapseMultipleVariableDeclarations() {
 Node root = new Node("function() {}", new Token(0, 0, Token.FUNCTION));
 Node varNode1 = new Node("var a;", new Token(0, 1, Token.VAR));
 Node varNode2 = new Node("var b = 1;", new Token(0, 2, Token.VAR));
 Node varNode3 = new Node("var c = 2;", new Token(0, 3, Token.VAR));
 root.addChildToBack(varNode1);
 root.addChildToBack(varNode2);
 root.addChildToBack(varNode3);

 compiler.process(null, root);
 Compiler.ProcessResult result = compiler.getProcessResult();
 String output = result.getCompiledSource();
 assertEquals("function() {" + System.lineSeparator() +
             "  var a, b = 1, c = 2;" + System.lineSeparator() +
             "}", output);
 }

 @Test
 public void testCollapseMultipleAssignments() {
 Node root = new Node("function() {}", new Token(0, 0, Token.FUNCTION));
 Node varNode1 = new Node("var a;", new Token(0, 1, Token.VAR));
 Node assignNode1 = new Node("a = true;", new Token(0, 2, Token.ASSIGN));
 Node varNode2 = new Node("var b;", new Token(0, 3, Token.VAR));
 Node assignNode2 = new Node("b = true;", new Token(0, 4, Token.ASSIGN));
 Node varNode3 = new Node("var c;", new Token(0, 5, Token.VAR));
 root.addChildToBack(varNode1);
 root.addChildToBack(assignNode1);
 root.addChildToBack(varNode2);
 root.addChildToBack(assignNode2);
 root.addChildToBack(varNode3);

 compiler.process(null, root);
 Compiler.ProcessResult result = compiler.getProcessResult();
 String output = result.getCompiledSource();
 assertEquals("function() {" + System.lineSeparator() +
             "  var a, b = a = true, c;" + System.lineSeparator() +
             "}", output);
 }

 @Test
 public void testCollapseMultipleVariableDeclarationsInsideFunction() {
 Node root = new Node("function foo() {}", new Token(0, 0, Token.FUNCTION));
 Node varNode1 = new Node("var a;", new Token(1, 1, Token.VAR));
 Node varNode2 = new Node("var b = 1;", new Token(1, 2, Token.VAR));
 Node varNode3 = new Node("var c = 2;", new Token(1, 3, Token.VAR));
 root.addChildToBack(varNode1);
 root.addChildToBack(varNode2);
 root.addChildToBack(varNode3);

 Node functionBody = new Node("", new Token(1, 4, Token.BLOCK));
 root.addChildToBack(functionBody);

 compiler.process(null, root);
 Compiler.ProcessResult result = compiler.getProcessResult();
 String output = result.getCompiledSource();
 assertEquals("function foo() {" + System.lineSeparator() +
             "  var a, b = 1, c = 2;" + System.lineSeparator() +
             "}", output);
 }

 @Test
 public void testCollapseVariableDeclarationWithAssignments() {
 Node root = new Node("function() {}", new Token(0, 0, Token.FUNCTION));
 Node varNode1 = new Node("var a = 1;", new Token(0, 1, Token.VAR));
 Node varNode2 = new Node("var b = 1, c = 2;", new Token(0, 2, Token.VAR));
 root.addChildToBack(varNode1);
 root.addChildToBack(varNode2);

 compiler.process(null, root);
 Compiler.ProcessResult result = compiler.getProcessResult();
 String output = result.getCompiledSource();
 assertEquals("function() {" + System.lineSeparator() +
             "  var a = 1, b = 1, c = 2;" + System.lineSeparator() +
             "}", output);
 }

 @Test
 public void testCollapseVariableDeclarationWithIfStatement() {
 Node root = new Node("function() {}", new Token(0, 0, Token.FUNCTION));
 Node ifNode = new Node("if (true)", new Token(0, 1, Token.IF));
 Node varNode1 = new Node("var a;", new Token(1, 1, Token.VAR));
 Node varNode2 = new Node("var b = 1;", new Token(1, 2, Token.VAR));
 Node varNode3 = new Node("var c = 2;", new Token(1, 3, Token.VAR));
 Node ifBlock = new Node("{}", new Token(1, 4, Token.BLOCK));
 ifNode.addChildToBack(ifBlock);
 root.addChildToBack(ifNode);
 root.addChildToBack(varNode1);
 root.addChildToBack(varNode2);
 root.addChildToBack(varNode3);

 compiler.process(null, root);
 Compiler.ProcessResult result = compiler.getProcessResult();
 String output = result.getCompiledSource();
 assertEquals("function() {" + System.lineSeparator() +
             "  if (true)" + System.lineSeparator() +
             "  {" + System.lineSeparator() +
             "    var a, b = 1, c = 2;" + System.lineSeparator() +
             "  }" + System.lineSeparator() +
             "}", output);
 }

 @Test
 public void testCollapseVariableDeclarationWithReturnStatement() {
 Node root = new Node("function() {}", new Token(0, 0, Token.FUNCTION));
 Node returnNode = new Node("return;", new Token(0, 1, Token.RETURN));
 Node varNode1 = new Node("var a;", new Token(1, 1, Token.VAR));
 Node varNode2 = new Node("var b = 1;", new Token(1, 2, Token.VAR));
 Node varNode3 = new Node("var c = 2;", new Token(1, 3, Token.VAR));
 root.addChildToBack(varNode1);
 root.addChildToBack(varNode2);
 root.addChildToBack(varNode3);
 root.addChildToBack(returnNode);

 compiler.process(null, root);
 Compiler.ProcessResult result = compiler.getProcessResult();
 String output = result.getCompiledSource();
 assertEquals("function() {" + System.lineSeparator() +
             "  var a, b = 1, c = 2;" + System.lineSeparator() +
             "  return;" + System.lineSeparator() +
             "}", output);
 }

 @Test
 public void testCollapseVariableDeclarationWithThrowStatement() {
 Node root = new Node("function() {}", new Token(0, 0, Token.FUNCTION));
 Node throwNode = new Node("throw new Error();", new Token(0, 1, Token.THROW));
 Node varNode1 = new Node("var a;", new Token(1, 1, Token.VAR));
 Node varNode2 = new Node("var b = 1;", new Token(1, 2, Token.VAR));
 Node varNode3 = new Node("var c = 2;", new Token(1, 3, Token.VAR));
 root.addChildToBack(varNode1);
 root.addChildToBack(varNode2);
 root.addChildToBack(varNode3);
 root.addChildToBack(throwNode);

 compiler.process(null, root);
 Compiler.ProcessResult result = compiler.getProcessResult();
 String output = result.getCompiledSource();
 assertEquals("function() {" + System.lineSeparator() +
             "  var a, b = 1, c = 2;" + System.lineSeparator() +
             "  throw new Error();" + System.lineSeparator() +
             "}", output);
 }

 @After
 public void tearDown() {
 compiler = null;
 collapseVariableDeclarations = null;
 }
}