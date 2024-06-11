package com.example.junit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import antlr.collections.LinkedList;
import antlr.Token;

import org.junit.Test;

public class ScopeUtilsTest {

 private DetailAST getTestNode(String nodeType, String... nodeTexts) {
  DetailAST rootNode = new DefaultDetailAST();
  DetailAST modifierNode = new DefaultDetailAST();
  DetailAST identifierNode = new DefaultDetailAST();

  Token modifierToken = new Token(1, 1, 0, nodeTexts[0], 0);
  modifierNode.setType(TokenTypes.MODIFIER);
  modifierNode.setLine(1);
  modifierNode.setColumn(1);
  modifierNode.setText(nodeTexts[0]);

  Token identifierToken = new Token(1, 3, 0, nodeTexts[1], 0);
  identifierNode.setType(TokenTypes.IDENTIFIER);
  identifierNode.setLine(1);
  identifierNode.setColumn(3);
  identifierNode.setText(nodeTexts[1]);

  LinkedList children = new LinkedList();
  children.add(modifierNode);
  children.add(identifierNode);
  rootNode.setType(nodeType);
  rootNode.setChildren(children);

  return rootNode;
}

 @Test
 public void testGetScopeFromMods() {
  DetailAST mods = getTestNode(TokenTypes.MODIFIER_LIST, "public");
  assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(mods));

  mods = getTestNode(TokenTypes.MODIFIER_LIST, "protected");
  assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(mods));

  mods = getTestNode(TokenTypes.MODIFIER_LIST, "private");
  assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(mods));

  mods = getTestNode(TokenTypes.MODIFIER_LIST, "static");
  assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(mods));
 }

 @Test
 public void testGetScopeFromModsWithMultipleModifiers() {
  DetailAST mods = getTestNode(TokenTypes.MODIFIER_LIST, "public", "static");
  assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(mods));

  mods = getTestNode(TokenTypes.MODIFIER_LIST, "protected", "private");
  assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(mods));

  mods = getTestNode(TokenTypes.MODIFIER_LIST, "private", "public");
  assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(mods));
 }

 @Test
 public void testGetSurroundingScope() {
  DetailAST node = getTestNode(TokenTypes.METHOD_DECL, "public void testMethod() {}", "testMethod");
  assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));

  node = getTestNode(TokenTypes.FIELD_DECL, "private int privateField;", "privateField");
  assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(node));

  node = getTestNode(TokenTypes.CLASS_DEF, "public class TestClass {}", "TestClass");
  assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));

  node = getTestNode(TokenTypes.ANNOTATION, "@MyAnnotation", "");
  assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(node));
}

 @Test
 public void testIsInInterfaceBlock() {
  DetailAST node = getTestNode(TokenTypes.INTERFACE_TYPE, "public interface MyInterface {}", "");
  assertTrue(ScopeUtils.isInInterfaceBlock(node));

  node = getTestNode(TokenTypes.CLASS_DEF, "public interface MyInterface {}", "MyInterface");
  assertTrue(ScopeUtils.isInInterfaceBlock(node));

  node = getTestNode(TokenTypes.METHOD_DECL, "public void testMethod() {}", "testMethod");
  assertFalse(ScopeUtils.isInInterfaceBlock(node));
}

 @Test
 public void testIsInAnnotationBlock() {
  DetailAST node = getTestNode(TokenTypes.ANNOTATION, "@MyAnnotation", "");
  assertTrue(ScopeUtils.isInAnnotationBlock(node));

  node = getTestNode(TokenTypes.CLASS_DEF, "@MyAnnotation public class TestClass {}", "TestClass");
  assertFalse(ScopeUtils.isInAnnotationBlock(node));
}

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
  DetailAST node = getTestNode(TokenTypes.INTERFACE_TYPE, "public interface MyInterface {}", "");
  assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

  node = getTestNode(TokenTypes.ANNOTATION, "@MyAnnotation", "");
  assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

  node = getTestNode(TokenTypes.METHOD_DECL, "public void testMethod() {}", "testMethod");
  assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
}

 @Test
 public void testIsInEnumBlock() {
  DetailAST node = getTestNode(TokenTypes.ENUM_DEF, "public enum MyEnum { VALUE1, VALUE2 }", "");
  assertTrue(ScopeUtils.isInEnumBlock(node));

  node = getTestNode(TokenTypes.CLASS_DEF, "public enum MyEnum { VALUE1, VALUE2 }", "MyEnum");
  assertTrue(ScopeUtils.isInEnumBlock(node));

  node = getTestNode(TokenTypes.METHOD_DECL, "public void testMethod() {}", "testMethod");
  assertFalse(ScopeUtils.isInEnumBlock(node));
}

 @Test
 public void testIsInCodeBlock() {
  DetailAST node = getTestNode(TokenTypes.BLOCK, "{ int x = 1; }", "");
  assertTrue(ScopeUtils.isInCodeBlock(node));

  node = getTestNode(TokenTypes.METHOD_DECL, "public void testMethod() {}", "testMethod");
  assertTrue(ScopeUtils.isInCodeBlock(node));

  node = getTestNode(TokenTypes.CLASS_DEF, "public class TestClass {}", "TestClass");
  assertFalse(ScopeUtils.isInCodeBlock(node));
}

 @Test
 public void testIsOuterMostType() {
  DetailAST node = getTestNode(TokenTypes.CLASS_DEF, "public class TestClass {}", "TestClass");
  assertTrue(ScopeUtils.isOuterMostType(node));

  node = getTestNode(TokenTypes.METHOD_DECL, "public void testMethod() {}", "testMethod");
  assertFalse(ScopeUtils.isOuterMostType(node));
}

 @Test
 public void testIsLocalVariableDef() {
  DetailAST node = getTestNode(TokenTypes.VAR_DECL, "int x;", "");
  assertTrue(ScopeUtils.isLocalVariableDef(node));

  node = getTestNode(TokenTypes.METHOD_DECL, "public void testMethod() { int x; }", "x");
  assertTrue(ScopeUtils.isLocalVariableDef(node));

  node = getTestNode(TokenTypes.FIELD_DECL, "int x;", "x");
  assertFalse(ScopeUtils.isLocalVariableDef(node));
}

 @Test
 public void testIsClassFieldDef() {
  DetailAST node = getTestNode(TokenTypes.FIELD_DECL, "int x;", "x");
  assertFalse(ScopeUtils.isClassFieldDef(node));

  node = getTestNode(TokenTypes.CLASS_DEF, "public class TestClass { int x; }", "x");
  assertTrue(ScopeUtils.isClassFieldDef(node));
 }

 @Test
 public void testIsInScope() {
  DetailAST node = getTestNode(TokenTypes.METHOD_DECL, "public void testMethod() {}", "testMethod");
  DetailAST scopeNode = getTestNode(TokenTypes.CLASS_DEF, "public class TestClass {}", "TestClass");

  assertTrue(ScopeUtils.isInScope(node, Scope.PUBLIC));
  assertTrue(ScopeUtils.isInScope(node, Scope.PUBLIC));

  assertTrue(ScopeUtils.isInScope(node, ScopeUtils.getSurroundingScope(node)));
  assertTrue(ScopeUtils.isInScope(scopeNode, Scope.PUBLIC));

  assertFalse(ScopeUtils.isInScope(node, Scope.PROTECTED));
  assertFalse(ScopeUtils.isInScope(node, Scope.PRIVATE));
  assertFalse(ScopeUtils.isInScope(node, Scope.ANONINNER));
 }
}