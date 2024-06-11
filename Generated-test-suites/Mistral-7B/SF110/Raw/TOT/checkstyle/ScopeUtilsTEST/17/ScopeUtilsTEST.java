package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.AST;

import org.junit.Test;

public class ScopeUtilsTest {

 // Test getScopeFromMods method
 @Test
 public void testGetScopeFromMods() {
  DetailAST modsAST = createModifiersAST("public protected private");
  Scope expectedScope = Scope.PROTECTED;
  Scope actualScope = ScopeUtils.getScopeFromMods(modsAST);
  assertEquals(expectedScope, actualScope);
 }

 // Test getScopeFromMods with empty modifiers list
 @Test
 public void testGetScopeFromModsEmptyList() {
  DetailAST modsAST = createEmptyModifiersListAST();
  Scope actualScope = ScopeUtils.getScopeFromMods(modsAST);
  assertEquals(Scope.PACKAGE, actualScope);
 }

 // Test getScopeFromMods with invalid modifier
 @Test(expected = IllegalStateException.class)
 public void testGetScopeFromModsInvalidModifier() {
  DetailAST modsAST = createModifiersAST("invalid");
  ScopeUtils.getScopeFromMods(modsAST);
 }

 // Test getSurroundingScope method
 @Test
 public void testGetSurroundingScope() {
  DetailAST nodeAST = createClassAST("public class Test {}");
  Scope expectedScope = Scope.CLASS;
  Scope actualScope = ScopeUtils.getSurroundingScope(nodeAST);
  assertEquals(expectedScope, actualScope);
 }

 // Test getSurroundingScope for method call
 @Test
 public void testGetSurroundingScopeMethodCall() {
  DetailAST nodeAST = createMethodCallAST("testMethod() {}", "Test");
  Scope expectedScope = Scope.SCOPE_METHOD;
  Scope actualScope = ScopeUtils.getSurroundingScope(nodeAST);
  assertEquals(expectedScope, actualScope);
 }

 // Test getSurroundingScope for variable declaration
 @Test
 public void testGetSurroundingScopeVariableDeclaration() {
  DetailAST nodeAST = createVariableDeclarationAST("int x;");
  Scope expectedScope = Scope.SCOPE_VARIABLE;
  Scope actualScope = ScopeUtils.getSurroundingScope(nodeAST);
  assertEquals(expectedScope, actualScope);
 }

 // Test getSurroundingScope for empty statement
 @Test
 public void testGetSurroundingScopeEmptyStatement() {
  DetailAST nodeAST = createEmptyStatementAST();
  Scope actualScope = ScopeUtils.getSurroundingScope(nodeAST);
  assertNull(actualScope);
 }

 // Test isInInterfaceBlock method
 @Test
 public void testIsInInterfaceBlock() {
  DetailAST nodeAST = createInterfaceAST();
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInInterfaceBlock(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInInterfaceBlock for class node
 @Test
 public void testIsInInterfaceBlockClassNode() {
  DetailAST nodeAST = createClassAST("public class Test {}", "Test");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInInterfaceBlock(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInAnnotationBlock method
 @Test
 public void testIsInAnnotationBlock() {
  DetailAST nodeAST = createAnnotationAST("@Test");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInAnnotationBlock(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInAnnotationBlock for class node
 @Test
 public void testIsInAnnotationBlockClassNode() {
  DetailAST nodeAST = createClassAST("@Test public class Test {}", "Test");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInAnnotationBlock(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInInterfaceOrAnnotationBlock method
 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
  DetailAST nodeAST = createAnnotationAST("@Test");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInInterfaceOrAnnotationBlock for class node
 @Test
 public void testIsInInterfaceOrAnnotationBlockClassNode() {
  DetailAST nodeAST = createClassAST("public @Test class Test {}", "Test");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInEnumBlock method
 @Test
 public void testIsInEnumBlock() {
  DetailAST nodeAST = createEnumAST();
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInEnumBlock(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInEnumBlock for class node
 @Test
 public void testIsInEnumBlockClassNode() {
  DetailAST nodeAST = createClassAST("public enum Test {}", "Test");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInEnumBlock(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInCodeBlock method
 @Test
 public void testIsInCodeBlock() {
  DetailAST nodeAST = createMethodBodyAST();
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInCodeBlock(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInCodeBlock for empty statement
 @Test
 public void testIsInCodeBlockEmptyStatement() {
  DetailAST nodeAST = createEmptyStatementAST();
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInCodeBlock(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isOuterMostType method
 @Test
 public void testIsOuterMostType() {
  DetailAST nodeAST = createClassAST("public class Test {}", "Test");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isOuterMostType(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isOuterMostType for method call node
 @Test
 public void testIsOuterMostTypeMethodCall() {
  DetailAST nodeAST = createMethodCallAST("testMethod() {}", "Test");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isOuterMostType(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isLocalVariableDef method
 @Test
 public void testIsLocalVariableDef() {
  DetailAST nodeAST = createVariableDeclarationAST("int x;");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isLocalVariableDef(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isLocalVariableDef for class field node
 @Test
 public void testIsLocalVariableDefClassField() {
  DetailAST nodeAST = createClassFieldDeclarationAST("int x;");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isLocalVariableDef(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isClassFieldDef method
 @Test
 public void testIsClassFieldDef() {
  DetailAST nodeAST = createClassFieldDeclarationAST("int x;");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isClassFieldDef(nodeAST);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInScope method
 @Test
 public void testIsInScope() {
  DetailAST ast = createClassAST("public class Test {}", "Test");
  Scope scope = Scope.CLASS;
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInScope(ast, scope);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInScope for out of scope node
 @Test
 public void testIsInScopeOutOfScope() {
  DetailAST ast = createMethodCallAST("testMethod() {}", "Test");
  Scope scope = Scope.CLASS;
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInScope(ast, scope);
  assertEquals(expectedResult, actualResult);
 }

 // Test isInScope for same scope node
 @Test
 public void testIsInScopeSameScope() {
  DetailAST ast = createClassAST("public class Test {}", "Test");
  DetailAST nodeAST = ast.getFirstChild();
  Scope scope = Scope.CLASS;
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInScope(nodeAST, scope);
  assertEquals(expectedResult, actualResult);
 }

 private DetailAST createModifiersAST(String modifiers) {
  DetailAST modifiersAST = new BasicAST();
  for (String modifier : modifiers.split(" ")) {
   DetailAST modifierAST = new ModifierToken(Modifiers.MOD_PUBLIC, modifier);
   modifiersAST.addChild(modifierAST);
  }
  return modifiersAST;
 }

 private DetailAST createEmptyModifiersListAST() {
  DetailAST modifiersAST = new BasicAST();
  return modifiersAST;
 }

 private DetailAST createAnnotationAST(String annotation) {
  DetailAST annotationAST = new BasicAST();
  DetailAST annotationTypeAST = new SimpleNode("ANNOTATIONTYPE", new TextToken(annotation));
  annotationAST.setType(TokenTypes.ANNOTATION);
  annotationAST.addChild(annotationTypeAST);
  return annotationAST;
 }

 private DetailAST createClassAST(String classDefinition, String className) {
  DetailAST classAST = new ClassDeclarationAST();
  DetailAST modifiersAST = createModifiersAST("public");
  classAST.setType(TokenTypes.CLASSDEF);
  classAST.setName(new TextToken(className));
  classAST.addChild(modifiersAST);
  classAST.addChild(new ClassBodyAST());
  classAST.addChild(createClassBodyAST(classDefinition));
  return classAST;
 }

 private DetailAST createClassBodyAST(String code) {
  DetailAST classBodyAST = new ClassBodyAST();
  DetailAST statementsAST = new ListAST();
  DetailAST statementAST = new SimpleNode(TokenTypes.EMPTYSTMT, new TextToken(""));
  statementsAST.addChild(statementAST);
  classBodyAST.addChild(statementsAST);
  DetailAST codeAST = createASTFromString(code);
  statementsAST.addChild(codeAST);
  return classBodyAST;
 }

 private DetailAST createMethodCallAST(String methodCall, String className) {
  DetailAST methodCallAST = new MethodInvocationAST();
  DetailAST methodAST = new MethodDeclarationAST();
  DetailAST modifiersAST = createModifiersAST("public");
  methodAST.setType(TokenTypes.METHODDEF);
  methodAST.setName(new TextToken(methodCall.substring(0, methodCall.lastIndexOf("("))));
  methodAST.addChild(modifiersAST);
  DetailAST typeAST = new SimpleNode("TYPE", new TextToken(className));
  methodAST.setContainer(typeAST);
  DetailAST argumentsAST = new ListAST();
  DetailAST argumentAST = new SimpleNode("EXPRESSION", new TextToken(""));
  argumentsAST.addChild(argumentAST);
  methodAST.addChild(argumentsAST);
  methodCallAST.setType(TokenTypes.CLASSINSTANCEEXPRESSION);
  methodCallAST.setName(typeAST);
  methodCallAST.addChild(methodAST);
  return methodCallAST;
 }

 private DetailAST createMethodBodyAST() {
  DetailAST methodBodyAST = new ClassBodyAST();
  DetailAST statementsAST = new ListAST();
  DetailAST statementAST = new SimpleNode(TokenTypes.EMPTYSTMT, new TextToken(""));
  statementsAST.addChild(statementAST);
  methodBodyAST.addChild(statementsAST);
  return methodBodyAST;
 }

 private DetailAST createVariableDeclarationAST(String variableDeclaration) {
  DetailAST variableDeclarationAST = new VariableDeclarationAST();
  DetailAST modifiersAST = createModifiersAST("final");
  variableDeclarationAST.setType(TokenTypes.VARDECL);
  variableDeclarationAST.setName(new TextToken(variableDeclaration.substring(0, variableDeclaration.indexOf(" "))));
  variableDeclarationAST.addChild(modifiersAST);
  DetailAST initialValueAST = new InitializerAST();
  DetailAST valueAST = new SimpleNode("EXPRESSION", new TextToken(""));
  initialValueAST.setType(TokenTypes.ASSIGNMENT);
  initialValueAST.setLeftChild(variableDeclarationAST);
  initialValueAST.setRightChild(valueAST);
  variableDeclarationAST.addChild(initialValueAST);
  return variableDeclarationAST;
 }

 private DetailAST createEnumAST() {
  DetailAST enumAST = new ClassDeclarationAST();
  DetailAST modifiersAST = createModifiersAST("public enum");
  enumAST.setType(TokenTypes.CLASSDEF);
  enumAST.setName(new TextToken("TestEnum"));
  enumAST.addChild(modifiersAST);
  enumAST.addChild(new ClassBodyAST());
  DetailAST enumBodyAST = enumAST.getLastChild();
  DetailAST enumConstantsAST = new ListAST();
  DetailAST enumConstantAST = new VariableDeclarationAST();
  DetailAST enumConstantNameAST = new TextToken("TestConstant");
  DetailAST enumConstantModifiersAST = createModifiersAST("");
  enumConstantAST.setType(TokenTypes.VARDECL);
  enumConstantAST.setName(enumConstantNameAST);
  enumConstantAST.addChild(enumConstantModifiersAST);
  DetailAST enumConstantValueAST = new SimpleNode("EXPRESSION", new IntegerLiteralToken(0));
  DetailAST enumConstantInitializerAST = new InitializerAST();
  enumConstantAST.addChild(enumConstantInitializerAST);
  enumConstantAST.addChild(enumConstantValueAST);
  enumConstantsAST.addChild(enumConstantAST);
  enumBodyAST.addChild(enumConstantsAST);
  return enumAST;
 }

 private DetailAST createASTFromString(String code) {
  String lexerInput = "{ " + code + " }";
  ANTLRInputStream input = new ANTLRInputStream(new StringReader(lexerInput));
  CheckstyleLexer lexer = new CheckstyleLexer(input);
  CommonTokenStream tokens = new CommonTokenStream(lexer);
  ParseTree tree = new DetailedParser(tokens).grammarFile("Checkstyle.g4").parse();
  return tree.getRootNode();
 }
}