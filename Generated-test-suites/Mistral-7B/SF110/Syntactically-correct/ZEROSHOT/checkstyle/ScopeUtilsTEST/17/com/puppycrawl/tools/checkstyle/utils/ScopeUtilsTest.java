package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;
import org.junit.Test;

public class ScopeUtilsTest {

 private DetailAST createTestNode(String nodeText) {
 DetailAST node = new BaseNodeImpl();
 node.setType(TokenTypes.IDENTIFIER);
 node.setText(nodeText);
 return node;
 }

 @Test
 public void testGetScopeFromMods() {
 Scope expectedScope;
 DetailAST mods = new BasicList();
 DetailAST mod;

 // Test public modifier
 mod = createTestNode("public");
 mods.add(mod);
 expectedScope = Scope.PUBLIC;
 Scope actualScope = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expectedScope, actualScope);

 // Test protected modifier
 mod.setText("protected");
 expectedScope = Scope.PROTECTED;
 actualScope = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expectedScope, actualScope);

 // Test private modifier
 mod.setText("private");
 expectedScope = Scope.PRIVATE;
 actualScope = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expectedScope, actualScope);

 // Test no modifier
 mod.setText("");
 actualScope = ScopeUtils.getScopeFromMods(mods);
 assertEquals(Scope.PACKAGE, actualScope);
 }

 @Test
 public void testGetSurroundingScope_publicMethod() {
 DetailAST node = createTestNode("MyClass");
 DetailAST parent = new BaseNodeImpl();
 parent.setType(TokenTypes.CLASS_DEF);
 parent.addChild(node);
 DetailAST grandParent = new BaseNodeImpl();
 grandParent.setType(TokenTypes.BLOCK);
 grandParent.addChild(parent);
 Scope expectedScope = Scope.PUBLIC;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_protectedMethod() {
 DetailAST node = createTestNode("ProtectedClass MyProtectedClass");
 DetailAST parent = new BaseNodeImpl();
 parent.setType(TokenTypes.CLASS_DEF);
 parent.addChild(node);
 DetailAST superClass = new BaseNodeImpl();
 superClass.setType(TokenTypes.IDENTIFIER);
 superClass.setText("SuperClass");
 DetailAST mods = new BasicList();
 DetailAST mod = new BaseNodeImpl();
 mod.setType(TokenTypes.KEYWORD);
 mod.setText("protected");
 mods.add(mod);
 DetailAST superClassMods = new BasicList();
 superClassMods.add(mods);
 superClass.setParent(new BaseNodeImpl());
 superClass.setParent(parent);
 superClass.setProperty("modifier", superClassMods);
 DetailAST grandParent = new BaseNodeImpl();
 grandParent.setType(TokenTypes.BLOCK);
 grandParent.addChild(parent);
 Scope expectedScope = Scope.PROTECTED;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_privateMethod() {
 DetailAST node = createTestNode("PrivateClass MyPrivateClass");
 DetailAST parent = new BaseNodeImpl();
 parent.setType(TokenTypes.CLASS_DEF);
 parent.addChild(node);
 DetailAST mods = new BasicList();
 DetailAST mod = new BaseNodeImpl();
 mod.setType(TokenTypes.KEYWORD);
 mod.setText("private");
 mods.add(mod);
 parent.setProperty("modifier", mods);
 DetailAST grandParent = new BaseNodeImpl();
 grandParent.setType(TokenTypes.BLOCK);
 grandParent.addChild(parent);
 Scope expectedScope = Scope.PRIVATE;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_innerClass() {
 DetailAST outerClass = new BaseNodeImpl();
 outerClass.setType(TokenTypes.CLASS_DEF);
 outerClass.setText("OuterClass");
 DetailAST innerClass = new BaseNodeImpl();
 innerClass.setType(TokenTypes.CLASS_DEF);
 innerClass.setText("InnerClass");
 innerClass.setParent(outerClass);
 DetailAST grandParent = new BaseNodeImpl();
 grandParent.setType(TokenTypes.BLOCK);
 grandParent.addChild(outerClass);
 Scope expectedScope = Scope.ANONINNER;
 Scope actualScope = ScopeUtils.getSurroundingScope(innerClass);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testIsInInterfaceBlock() {
 DetailAST node = createTestNode("interface MyInterface {}");
 boolean expectedValue = true;
 boolean actualValue = ScopeUtils.isInInterfaceBlock(node);
 assertTrue(actualValue == expectedValue);
 }

 @Test
 public void testIsInInterfaceBlock_notInterface() {
 DetailAST node = createTestNode("class MyClass {}");
 boolean expectedValue = false;
 boolean actualValue = ScopeUtils.isInInterfaceBlock(node);
 assert False(actualValue == expectedValue);
 }

 @Test
 public void testIsInAnnotationBlock() {
 DetailAST node = createTestNode("@MyAnnotation {}");
 boolean expectedValue = true;
 boolean actualValue = ScopeUtils.isInAnnotationBlock(node);
 assertTrue(actualValue == expectedValue);
 }

 @Test
 public void testIsInAnnotationBlock_notAnnotation() {
 DetailAST node = createTestNode("class MyClass {}");
 boolean expectedValue = false;
 boolean actualValue = ScopeUtils.isInAnnotationBlock(node);
 assert False(actualValue == expectedValue);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST node = createTestNode("interface MyInterface {}");
 boolean expectedValue = true;
 boolean actualValue = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertTrue(actualValue == expectedValue);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_notInterfaceOrAnnotation() {
 DetailAST node = createTestNode("class MyClass {}");
 boolean expectedValue = false;
 boolean actualValue = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assert False(actualValue == expectedValue);
 }

 @Test
 public void testIsInEnumBlock() {
 DetailAST node = createTestNode("enum MyEnum { VALUE }");
 boolean expectedValue = true;
 boolean actualValue = ScopeUtils.isInEnumBlock(node);
 assertTrue(actualValue == expectedValue);
 }

 @Test
 public void testIsInEnumBlock_notEnum() {
 DetailAST node = createTestNode("class MyClass {}");
 boolean expectedValue = false;
 boolean actualValue = ScopeUtils.isInEnumBlock(node);
 assert False(actualValue == expectedValue);
 }

 @Test
 public void testIsInCodeBlock() {
 DetailAST node = createTestNode("int x = 5;");
 boolean expectedValue = true;
 boolean actualValue = ScopeUtils.isInCodeBlock(node);
 assertTrue(actualValue == expectedValue);
 }

 @Test
 public void testIsInCodeBlock_notCode() {
 DetailAST node = createTestNode("interface MyInterface {}");
 boolean expectedValue = false;
 boolean actualValue = ScopeUtils.isInCodeBlock(node);
 assert False(actualValue == expectedValue);
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST node = createTestNode("class MyClass {}", 0);
 boolean expectedValue = true;
 boolean actualValue = ScopeUtils.isOuterMostType(node);
 assertTrue(actualValue == expectedValue);
 }

 @Test
 public void testIsOuterMostType_notOuterMostType() {
 DetailAST node = createTestNode("class MyClass { int x = 5; }", 1);
 boolean expectedValue = false;
 boolean actualValue = ScopeUtils.isOuterMostType(node);
 assert False(actualValue == expectedValue);
 }

 @Test
 public void testIsLocalVariableDef() {
 DetailAST node = createTestNode("int x = 5;");
 boolean expectedValue = true;
 boolean actualValue = ScopeUtils.isLocalVariableDef(node);
 assertTrue(actualValue == expectedValue);
 }

 @Test
 public void testIsLocalVariableDef_notLocalVariableDef() {
 DetailAST node = createTestNode("class MyClass { int x = 5; int y = 10; }");
 boolean expectedValue = false;
 boolean actualValue = ScopeUtils.isLocalVariableDef(node);
 assert False(actualValue == expectedValue);
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST node = createTestNode("class MyClass { int x = 5; }");
 boolean expectedValue = true;
 boolean actualValue = ScopeUtils.isClassFieldDef(node);
 assertTrue(actualValue == expectedValue);
 }

 @Test
 public void testIsClassFieldDef_notClassFieldDef() {
 DetailAST node = createTestNode("class MyClass { int x = 5; int y = 10; }");
 boolean expectedValue = false;
 boolean actualValue = ScopeUtils.isClassFieldDef(node);
 assert False(actualValue == expectedValue);
 }

 @Test
 public void testIsInScope() {
 DetailAST node = createTestNode("public class MyClass { int x = 5; }");
 Scope expectedScope = Scope.PUBLIC;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertTrue(ScopeUtils.isInScope(node, expectedScope));
 }

 @Test
 public void testIsInScope_notInScope() {
 DetailAST node = createTestNode("public class MyClass { int x = 5; }");
 Scope expectedScope = Scope.PROTECTED;
 assertFalse(ScopeUtils.isInScope(node, expectedScope));
 }

 @Test
 public void testIsInScope_nullScope() {
 DetailAST node = createTestNode("public class MyClass { int x = 5; }");
 Scope expectedScope = null;
 assertFalse(ScopeUtils.isInScope(node, expectedScope));
 }
}