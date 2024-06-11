import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeOperations;

public class CheckSideEffectsTest {

 private AbstractCompiler compiler;
 private CompilerOptions options;
 private Parser parser;

 @Before
 public void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions();
 parser = new Parser(options);
 }

 @Test
 public void testCheckSideEffects() {
 String code = "function foo() { return 42; } "
     + "var x = 1; "
     + "x = foo(); "
     + "var y = 2; ";

 Node ast = parser.parse(code, null, null);
 compiler.compile(ast, options, null);
 List<Node> problemNodes = Lists.newArrayList();

 CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
 NodeTraversal.traverse(checker, ast, null);
 problemNodes.addAll(checker.problemNodes);

 assertEquals(0, problemNodes.size());
 }

 @Test
 public void testCheckSideEffectsWithUsefulCode() {
 String code = "function foo() { "
     + "document.body.style.backgroundColor = 'red'; "
     + "return 42; "
     + "} "
     + "var x = 1; "
     + "x = foo(); "
     + "var y = 2; ";

 Node ast = parser.parse(code, null, null);
 compiler.compile(ast, options, null);
 List<Node> problemNodes = Lists.newArrayList();

 CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
 NodeTraversal.traverse(checker, ast, null);
 problemNodes.addAll(checker.problemNodes);

 assertFalse(problemNodes.isEmpty());
 }

 @Test
 public void testCheckSideEffectsWithProtectedCode() {
 String code = "function foo() { "
     + "JSCOMPILER_PRESERVE(x = 1); // this is protected code "
     + "document.body.style.backgroundColor = 'red'; "
     + "return 42; "
     + "} "
     + "var x = 1; "
     + "x = foo(); "
     + "var y = 2; ";

 Node ast = parser.parse(code, null, null);
 CompilerInput input = compiler.getSynthesizedExternsInput();
 input.getAstRoot(compiler).addChildrenToBack(ast);
 compiler.compile(ast, options, null);
 List<Node> problemNodes = Lists.newArrayList();

 CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, true);
 NodeTraversal.traverse(checker, ast, null);
 problemNodes.addAll(checker.problemNodes);

 assertEquals(0, problemNodes.size());
 }

 @Test
 public void testStripProtection() {
 String code = "function CheckSideEffects() { "
     + "function foo() { "
     + "JSCOMPILER_PRESERVE(x = 1); // this is protected code "
     + "document.body.style.backgroundColor = 'red'; "
     + "return 42; "
     + "} "
     + "this.check = function() { return foo(); }; "
     + "} "
     + "var obj = new CheckSideEffects(); "
     + "obj.check();";

 Node ast = parser.parse(code, null, null);
 compiler.compile(ast, options, null);
 List<Node> problemNodes = Lists.newArrayList();
 CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
 NodeTraversal.traverse(checker, ast, null);
 problemNodes.addAll(checker.problemNodes);

 StripProtection stripper = new StripProtection(compiler);
 NodeTraversal.traverse(stripper, ast, null);

 assertTrue(NodeOperations.equals(ast, parser.parse(
 "function CheckSideEffects() { "
 + "function foo() { document.body.style.backgroundColor = 'red'; return 42; } "
 + "this.check = function() { return foo(); }; "
 + "}", null).getRootNode()));
 }

}