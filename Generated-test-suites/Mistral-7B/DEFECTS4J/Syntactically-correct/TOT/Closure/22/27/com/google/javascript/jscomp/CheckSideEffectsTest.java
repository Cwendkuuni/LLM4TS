package com.google.javascript.jscomp.tests;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.JSDocInfoBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CheckSideEffectsTest {

 private static AbstractCompiler getCompiler(boolean protectSideEffects) {
 return new CompilerWithCheckSideEffects(new CompilerOptions(), protectSideEffects);
 }

 @Test
 public void testSideEffectFreeCode() {
 CompilerInput input = makeInput("sideEffectFreeCode.js");
 AbstractCompiler compiler = getCompiler(false);
 Node root = compiler.process(input, null);
 Checker checker = new Checker(compiler.getOptions(), root);
 List<Node> problems = Lists.newArrayList();
 checker.check(root, null);
 assertEquals(0, problems.size());
 }

 @Test
 public void testSideEffectCode() {
 CompilerInput input = makeInput("sideEffectCode.js");
 AbstractCompiler compiler = getCompiler(false);
 Node root = compiler.process(input, null);
 Checker checker = new Checker(compiler.getOptions(), root);
 List<Node> problems = Lists.newArrayList();
 checker.check(root, null);
 assertEquals(3, problems.size());
 }

 @Test
 public void testProtectSideEffectFreeCode() {
 CompilerInput input = makeInput("sideEffectFreeCode.js");
 AbstractCompiler compiler = getCompiler(true);
 Node root = compiler.process(input, null);
 Checker checker = new Checker(compiler.getOptions(), root);
 List<Node> problems = Lists.newArrayList();
 checker.check(root, null);
 assertEquals(0, problems.size());
 }

 @Test(expected = AssertionError.class)
 public void testProtectSideEffectCode() {
 CompilerInput input = makeInput("sideEffectCode.js");
 AbstractCompiler compiler = getCompiler(true);
 Node root = compiler.process(input, null);
 Checker checker = new Checker(compiler.getOptions(), root);
 List<Node> problems = Lists.newArrayList();
 checker.check(root, null);
 assertEquals(3, problems.size());
 }

 @Test
 public void testProtectAndRemoveSideEffectCode() {
 CompilerInput input = makeInput("sideEffectCode.js");
 AbstractCompiler compiler = getCompiler(true);
 Node root = compiler.process(input, null);
 CheckSideEffects pass = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
 NodeTraversal.traverse(compiler, root, pass);
 Checker checker = new Checker(compiler.getOptions(), root);
 List<Node> problems = Lists.newArrayList();
 checker.check(root, null);
 assertEquals(0, problems.size());
 }

 private CompilerInput makeInput(String fileName) {
 return new CompilerInput(fileName, new StringReader(getInput(fileName)));
 }

 private String getInput(String fileName) {
 switch (fileName) {
 case "sideEffectFreeCode.js":
 return "function foo() {}";
 case "sideEffectCode.js":
 return "function foo() { var x = 0; }";
 case "sideEffectCodeWithJSDoc.js":
 return "/**\n * @return {number}\n */\n function foo() { var x = 0; }";
 case "sideEffectCodeWithAssignment.js":
 return "function foo() { var x = 1; x = 2; }";
 case "sideEffectCodeWithCall.js":
 return "function foo() { return bar(); }";
 case "sideEffectCodeWithExpression.js":
 return "function foo() { return 1 + 2; }";
 case "sideEffectCodeWithVariableDeclaration.js":
 return "function foo() { var x; }";
 case "sideEffectCodeWithVariableAssignment.js":
 return "function foo() { var x = 1; x = 2; }";
 case "sideEffectCodeWithStatements.js":
 return "function foo() { var x = 1; x = 2; y = 3; }";
 case "sideEffectCodeWithLabeledStatement.js":
 return "function foo() { label1: for (var i = 0; i < 10; i++) { continue label1; } }";
 case "sideEffectCodeWithReturn.js":
 return "function foo() { return; }";
 case "sideEffectCodeWithThrow.js":
 return "function foo() { throw new Error(); }";
 case "sideEffectCodeWithEmptyStatement.js":
 return "function foo() {}";
 case "sideEffectCodeWithSemicolon.js":
 return "function foo() {; }";
 case "sideEffectCodeWithComment.js":
 return "function foo() { // comment }";
 default:
 throw new RuntimeException("Unknown file: " + fileName);
 }
 }
}

final class CompilerWithCheckSideEffects extends AbstractCompiler {
 private boolean protectSideEffects;

 CompilerWithCheckSideEffects(CompilerOptions options, boolean protectSideEffects) {
 super(options);
 this.protectSideEffects = protectSideEffects;
 }

 @Override
 public HotSwapCompilerPass getHotSwapPass() {
 return new CheckSideEffects(this, CheckLevel.WARNING, protectSideEffects);
 }
}