package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;
import org.junit.Test;

public class ScopeUtilsTest {

 private DetailAST createTestNode(int type, DetailAST... children) {
 return new DetailAST() {
 @Override
 public int getType() {
 return type;
 }

 @Override
 public DetailAST[] getChildren() {
 return children;
 }
 };
 }

 @Test
 public void testGetScopeFromMods_Public() {
 DetailAST mods = createTestNode(5);
 DetailAST publicToken = createTestNode(1, createTestNode("public"));
 mods.addChild(publicToken);
 Scope result = ScopeUtils.getScopeFromMods(mods);
 assertEquals(Scope.PUBLIC, result);
 }

 @Test
 public void testGetScopeFromMods_Protected() {
 DetailAST mods = createTestNode(5);
 DetailAST protectedToken = createTestNode(1, createTestNode("protected"));
 mods.addChild(protectedToken);
 Scope result = ScopeUtils.getScopeFromMods(mods);
 assertEquals(Scope.PROTECTED, result);
 }

 @Test
 public void testGetScopeFromMods_Private() {
 DetailAST mods = createTestNode(5);
 DetailAST privateToken = createTestNode(1, createTestNode("private"));
 mods.addChild(privateToken);
 Scope result = ScopeUtils.getScopeFromMods(mods);
 assertEquals(Scope.PRIVATE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetScopeFromMods_EmptyMods() {
 DetailAST mods = createTestNode(5);
 Scope result = ScopeUtils.getScopeFromMods(mods);
 fail("Expected IllegalArgumentException, but got: " + result);
 }

 @Test
 public void testGetScopeFromMods_NoScopeFound() {
 DetailAST mods = createTestNode(5);
 Scope result = ScopeUtils.getScopeFromMods(mods);
 assertEquals(Scope.PACKAGE, result);
 }

 @Test
 public void testGetSurroundingScope_Class() {
 DetailAST node = createTestNode(14); // class declaration
 Scope result = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.CLASS, result);
 }

 @Test
 public void testGetSurroundingScope_Interface() {
 DetailAST node = createTestNode(15); // interface declaration
 Scope result = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.INTERFACE, result);
 }

 @Test
 public void testGetSurroundingScope_Annotation() {
 DetailAST node = createTestNode(157); // annotation
 Scope result = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.ANNOTATION, result);
 }

 @Test
 public void testGetSurroundingScope_AnonymousClass() {
 DetailAST node = createTestNode(136); // anonymous class
 Scope result = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.ANONINNER, result);
 }

 @Test
 public void testGetSurroundingScope_Method() {
 DetailAST node = createTestNode(1); // method declaration
 Scope result = ScopeUtils.getSurroundingScope(node);
 fail("Expected ScopeUtils.ANONINNER, but got: " + result);
 }

 @Test
 public void testIsInInterfaceBlock_WithinInterface() {
 DetailAST node = createTestNode(15);
 Scope result = ScopeUtils.getSurroundingScope(node);
 assertTrue(ScopeUtils.isInInterfaceBlock(node));
 }

 @Test
 public void testIsInInterfaceBlock_NotInInterface() {
 DetailAST node = createTestNode(14);
 assertFalse(ScopeUtils.isInInterfaceBlock(node));
 }

 @Test
 public void testIsInAnnotationBlock_WithinAnnotation() {
 DetailAST node = createTestNode(157);
 Scope result = ScopeUtils.getSurroundingScope(node);
 assertTrue(ScopeUtils.isInAnnotationBlock(node));
 }

 @Test
 public void testIsInAnnotationBlock_NotInAnnotation() {
 DetailAST node = createTestNode(14);
 assertFalse(ScopeUtils.isInAnnotationBlock(node));
 }

 @Test
 public void testIsInScope_InScope() {
 DetailAST node = createTestNode(1);
 Scope scope = createTestNode(5, createTestNode(1, createTestNode("public")));
 ScopeUtils.getSurroundingScope(node);
 assertTrue(ScopeUtils.isInScope(node, scope));
 }

 @Test
 public void testIsInScope_NotInScope() {
 DetailAST node = createTestNode(1);
 Scope scope = createTestNode(5, createTestNode(1, createTestNode("protected")));
 assertFalse(ScopeUtils.isInScope(node, scope));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsNull_NullNode() {
 DetailAST node = null;
 ScopeUtils.getSurroundingScope(node);
 fail("Expected IllegalArgumentException, but got null");
 }
}