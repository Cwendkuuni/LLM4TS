package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

import org.junit.Test;

public class ScopeUtilsTest {

 private DetailAST createTestNode(String text) {
 return new DetailAST() {
 @Override
 public String getText() {
 return text;
 }
 };
 }

 @Test
 public void testGetScopeFromMods() {
 DetailAST mods = createTestNode("public protected private");
 assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(mods));
 }

 @Test
 public void testGetScopeFromMods_noModifiers() {
 DetailAST mods = createTestNode("");
 assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(mods));
 }

 @Test
 public void testGetScopeFromMods_invalidModifier() {
 DetailAST mods = createTestNode("invalid-modifier");
 assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(mods));
 }

 @Test
 public void testGetScopeFromMods_noModifiersPresent() {
 DetailAST node = createTestNode("class MyClass { }");
 DetailAST mods = node.getFirstChild();
 assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(mods));
 }

 @Test
 public void testGetSurroundingScope_classScope() {
 DetailAST node = createTestNode("public class MyClass { }");
 assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));
 }

 @Test
 public void testGetSurroundingScope_methodScope() {
 DetailAST node = createTestNode("public class MyClass { public void foo() { } }");
 assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node.getFirstChild()));
 }

 @Test
 public void testGetSurroundingScope_fieldScope() {
 DetailAST node = createTestNode("public class MyClass { int x; }");
 assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node.getFirstChild()));
 }

 @Test
 public void testGetSurroundingScope_packageScope() {
 DetailAST node = createTestNode("package com.example; public class MyClass { }");
 assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(node));
 }

 @Test
 public void testGetSurroundingScope_anonymousInnerClass() {
 DetailAST node = createTestNode("class MyClass { void foo() { new Object() { } } }");
 assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(node.getFirstChild()));
 }

 @Test
 public void testIsInInterfaceBlock() {
 DetailAST node = createTestNode("interface MyInterface { }");
 assertTrue(ScopeUtils.isInInterfaceBlock(node));
 }

 @Test
 public void testIsInInterfaceBlock_notInInterfaceBlock() {
 DetailAST node = createTestNode("public class MyClass { }");
 assertFalse(ScopeUtils.isInInterfaceBlock(node));
 }

 @Test
 public void testIsInAnnotationBlock() {
 DetailAST node = createTestNode("@interface MyAnnotation { }");
 assertTrue(ScopeUtils.isInAnnotationBlock(node));
 }

 @Test
 public void testIsInAnnotationBlock_notInAnnotationBlock() {
 DetailAST node = createTestNode("public class MyClass { }");
 assertFalse(ScopeUtils.isInAnnotationBlock(node));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST node = createTestNode("public interface MyInterface { }");
 assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_notInInterfaceOrAnnotationBlock() {
 DetailAST node = createTestNode("public class MyClass { }");
 assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
 }

 @Test
 public void testIsInEnumBlock() {
 DetailAST node = createTestNode("enum MyEnum { VALUE }");
 assertTrue(ScopeUtils.isInEnumBlock(node));
 }

 @Test
 public void testIsInEnumBlock_notInEnumBlock() {
 DetailAST node = createTestNode("public class MyClass { }");
 assertFalse(ScopeUtils.isInEnumBlock(node));
 }

 @Test
 public void testIsInCodeBlock() {
 DetailAST node = createTestNode("{ int x = 5; }");
 assertTrue(ScopeUtils.isInCodeBlock(node));
 }

 @Test
 public void testIsInCodeBlock_notInCodeBlock() {
 DetailAST node = createTestNode("public class MyClass { }");
 assertFalse(ScopeUtils.isInCodeBlock(node));
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST node = createTestNode("public class MyClass { }");
 assertTrue(ScopeUtils.isOuterMostType(node));
 }

 @Test
 public void testIsOuterMostType_notOuterMostType() {
 DetailAST node = createTestNode("class MyClass extends MySuperClass { }");
 assertFalse(ScopeUtils.isOuterMostType(node));
 }

 @Test
 public void testIsLocalVariableDef() {
 DetailAST node = createTestNode("int x;");
 assertTrue(ScopeUtils.isLocalVariableDef(node));
 }

 @Test
 public void testIsLocalVariableDef_notLocalVariableDef() {
 DetailAST node = createTestNode("public class MyClass { int x; }");
 assertFalse(ScopeUtils.isLocalVariableDef(node));
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST node = createTestNode("public class MyClass { int x; }");
 assertTrue(ScopeUtils.isClassFieldDef(node.getFirstChild()));
 }

 @Test
 public void testIsClassFieldDef_notClassFieldDef() {
 DetailAST node = createTestNode("public class MyClass { void foo() { int x; } }");
 assertFalse(ScopeUtils.isClassFieldDef(node.getFirstChild()));
 }

 @Test
 public void testIsInScope() {
 DetailAST node = createTestNode("public class MyClass { int x; }");
 assertTrue(ScopeUtils.isInScope(node, Scope.PUBLIC));
 }

 @Test
 public void testIsInScope_notInScope() {
 DetailAST node = createTestNode("public class MyClass { int x; }");
 assertFalse(ScopeUtils.isInScope(node, Scope.PROTECTED));
 }
}