package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.AST;

import org.junit.Test;

public class ScopeUtilsTest {

 @Test
 public void testGetScopeFromMods() {
  DetailAST mods = createModsAST("public protected private");
  Scope expectedScope = Scope.PROTECTED;
  Scope actualScope = ScopeUtils.getScopeFromMods(mods);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_EmptyMods() {
  DetailAST mods = createModsAST("");
  Scope expectedScope = Scope.PACKAGE;
  Scope actualScope = ScopeUtils.getScopeFromMods(mods);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_InvalidModifier() {
  DetailAST mods = createModsAST("invalidModifier");
  Scope expectedScope = Scope.PACKAGE;
  Scope actualScope = ScopeUtils.getScopeFromMods(mods);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_PublicClass() {
  DetailAST node = createNode("public class Test { }");
  Scope expectedScope = Scope.PUBLIC;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_ProtectedClass() {
  DetailAST node = createNode("protected class Test { }");
  Scope expectedScope = Scope.PROTECTED;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_PrivateClass() {
  DetailAST node = createNode("private class Test { }");
  Scope expectedScope = Scope.PRIVATE;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_AnonymousClass() {
  DetailAST node = createNode("class Test extends SomeClass { }");
  Scope expectedScope = Scope.ANONINNER;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_Interface() {
  DetailAST node = createNode("interface Test { }");
  Scope expectedScope = Scope.INTERFACE;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_Annotation() {
  DetailAST node = createNode("@Test annotation { }");
  Scope expectedScope = Scope.ANNOTATION;
  Scope actualScope = ScopeUtils.getSurroundingScope(node);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testIsInInterfaceBlock_Interface() {
  DetailAST node = createNode("interface Test { }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInInterfaceBlock(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceBlock_Class() {
  DetailAST node = createNode("public class Test { }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInInterfaceBlock(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_Annotation() {
  DetailAST node = createNode("@Test annotation { }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInAnnotationBlock(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_Class() {
  DetailAST node = createNode("public class Test { }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInAnnotationBlock(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_Interface() {
  DetailAST node = createNode("interface Test { }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_Class() {
  DetailAST node = createNode("public class Test { }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInEnumBlock_Enum() {
  DetailAST node = createNode("enum Test { VALUE }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInEnumBlock(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInEnumBlock_Class() {
  DetailAST node = createNode("public class Test { }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInEnumBlock(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInCodeBlock_Method() {
  DetailAST node = createNode("public void test() { }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInCodeBlock(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInCodeBlock_Variable() {
  DetailAST node = createNode("int test;");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInCodeBlock(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsOuterMostType_OuterMostType() {
  DetailAST node = createNode("public class Test { }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isOuterMostType(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsOuterMostType_InnerClass() {
  DetailAST node = createNode("public class Outer { public class Inner { } }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isOuterMostType(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsLocalVariableDef_LocalVariable() {
  DetailAST node = createNode("int test;");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isLocalVariableDef(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsLocalVariableDef_Field() {
  DetailAST node = createNode("int test;");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isLocalVariableDef(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsClassFieldDef_Field() {
  DetailAST node = createNode("int test;");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isClassFieldDef(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsClassFieldDef_Variable() {
  DetailAST node = createNode("int test;");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isClassFieldDef(node);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInScope_InScope() {
  DetailAST ast = createNode("public class Outer { public class Inner { int test; } }");
  Scope scope = Scope.INNER;
  DetailAST node = createNode("Inner test;");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInScope(ast, scope);
  assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInScope_OutOfScope() {
  DetailAST ast = createNode("public class Outer { public class Inner { int test; } }");
  Scope scope = Scope.OUTER;
  DetailAST node = createNode("int test;");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInScope(ast, scope);
  assertEquals(expectedResult, actualResult);
 }

 private DetailAST createModsAST(String modifiers) {
  DetailAST mods = new SimpleNode("Modifiers");
  for (String modifier : modifiers.split(" ")) {
    DetailAST modifierAST = new SimpleNode(Modifier.class, Modifier.MODIFIERS.get(modifier));
    modifierAST.setLine(1);
    modifierAST.setColumn(1);
    mods.addChild(modifierAST);
  }
  return mods;
 }

 private DetailAST createNode(String nodeText) {
  DetailAST node = new SimpleNode("Statement");
  node.setText(nodeText);
  node.setLine(1);
  node.setColumn(1);
  return node;
 }
}