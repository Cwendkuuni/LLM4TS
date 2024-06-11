package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.AST;

import org.junit.Test;

public class ScopeUtilsTest {

 @Test
 public void testGetScopeFromMods() {
  DetailAST ast = new DetailAST();
  ast.setType(5); // ModifierList
  AST modifier = new AST();
  modifier.setText("public");
  ast.addChild(modifier);
  AST modifier2 = new AST();
  modifier2.setText("protected");
  ast.addChild(modifier2);
  AST modifier3 = new AST();
  modifier3.setText("private");
  ast.addChild(modifier3);

  Scope expectedScope = Scope.PUBLIC;
  Scope actualScope = ScopeUtils.getScopeFromMods(ast);

  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_NoScopeFound() {
  DetailAST ast = new DetailAST();
  ast.setType(5); // ModifierList

  Scope expectedScope = Scope.PACKAGE;
  Scope actualScope = ScopeUtils.getScopeFromMods(ast);

  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_MultipleScopesFound() {
  DetailAST ast = new DetailAST();
  ast.setType(5); // ModifierList
  AST modifier = new AST();
  modifier.setText("public");
  ast.addChild(modifier);
  AST modifier2 = new AST();
  modifier2.setText("protected");
  ast.addChild(modifier2);
  AST modifier3 = new AST();
  modifier3.setText("private");
  ast.addChild(modifier3);
  AST modifier4 = new AST();
  modifier4.setText("default");
  ast.addChild(modifier4);

  Scope expectedScope = Scope.PUBLIC;
  Scope actualScope = ScopeUtils.getScopeFromMods(ast);

  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInClass() {
  DetailAST node = new DetailAST();
  node.setType(11); // MethodDeclaration

  Scope expectedScope = Scope.CLASS;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);

  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInInterface() {
  DetailAST node = new DetailAST();
  node.setType(15); // InterfaceDeclaration

  Scope expectedScope = Scope.INTERFACE;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);

  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInAnonymousClass() {
  DetailAST node = new DetailAST();
  node.setType(136); // AnonymousClassDeclaration

  Scope expectedScope = Scope.ANONINNER;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);

  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInEnum() {
  DetailAST node = new DetailAST();
  node.setType(154); // EnumDeclaration

  Scope expectedScope = Scope.ENUM;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);

  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInCodeBlock() {
  DetailAST node = new DetailAST();
  node.setType(9); // CompilationUnit

  Scope expectedScope = Scope.CODEBLOCK;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);

  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testIsInInterfaceBlock_MethodInInterface() {
  DetailAST node = new DetailAST();
  node.setType(15); // InterfaceDeclaration

  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInInterfaceBlock(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceBlock_MethodInClass() {
  DetailAST node = new DetailAST();
  node.setType(11); // MethodDeclaration

  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInInterfaceBlock(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_MethodInAnnotation() {
  DetailAST node = new DetailAST();
  node.setType(157); // Annotation

  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInAnnotationBlock(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_MethodOutsideAnnotation() {
  DetailAST node = new DetailAST();
  node.setType(11); // MethodDeclaration

  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInAnnotationBlock(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_MethodInInterface() {
  DetailAST node = new DetailAST();
  node.setType(15); // InterfaceDeclaration

  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_MethodInClass() {
  DetailAST node = new DetailAST();
  node.setType(11); // MethodDeclaration

  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInEnumBlock_MethodInEnum() {
  DetailAST node = new DetailAST();
  node.setType(154); // EnumDeclaration

  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInEnumBlock(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInEnumBlock_MethodOutsideEnum() {
  DetailAST node = new DetailAST();
  node.setType(11); // MethodDeclaration

  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInEnumBlock(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInCodeBlock() {
  DetailAST node = new DetailAST();
  node.setType(11); // MethodDeclaration

  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInCodeBlock(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInCodeBlock_MethodInCodeBlock() {
  DetailAST node = new DetailAST();
  node.setType(9); // CompilationUnit

  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInCodeBlock(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsOuterMostType() {
  DetailAST node = new DetailAST();
  node.setType(11); // MethodDeclaration

  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isOuterMostType(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsOuterMostType_CompilationUnit() {
  DetailAST node = new DetailAST();
  node.setType(9); // CompilationUnit

  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isOuterMostType(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsLocalVariableDef() {
  DetailAST node = new DetailAST();
  node.setType(10); // VariableDeclaration

  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isLocalVariableDef(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsLocalVariableDef_LocalVariableDeclaration() {
  DetailAST node = new DetailAST();
  node.setType(7); // VariableDeclarationList
  DetailAST variableDeclaration = new DetailAST();
  variableDeclaration.setType(10); // VariableDeclaration
  node.addChild(variableDeclaration);

  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isLocalVariableDef(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsClassFieldDef() {
  DetailAST node = new DetailAST();
  node.setType(10); // VariableDeclaration

  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isClassFieldDef(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsClassFieldDef_ClassFieldDeclaration() {
  DetailAST node = new DetailAST();
  node.setType(156); // ClassDeclaration
  DetailAST variableDeclaration = new DetailAST();
  variableDeclaration.setType(10); // VariableDeclaration
  node.addChild(variableDeclaration);

  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isClassFieldDef(node);

  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInScope() {
  DetailAST ast = new DetailAST();
  ast.setType(9); // CompilationUnit
  DetailAST node = new DetailAST();
  node.setType(11); // MethodDeclaration

  Scope expectedScope = Scope.CODEBLOCK;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);

  assertTrue(ScopeUtils.isInScope(ast, expectedScope));
  assertTrue(ScopeUtils.isInScope(node, expectedScope));
 }

 @Test
 public void testIsInScope_NotInScope() {
  DetailAST ast = new DetailAST();
  ast.setType(9); // CompilationUnit
  DetailAST node = new DetailAST();
  node.setType(11); // MethodDeclaration

  Scope expectedScope = Scope.INTERFACE;

  assertFalse(ScopeUtils.isInScope(ast, expectedScope));
  assertFalse(ScopeUtils.isInScope(node, expectedScope));
 }
}