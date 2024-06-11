package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.*;
import antlr.collections.*;

import org.junit.*;

public class ScopeUtilsTest {

 private DetailAST createTestNode(int nodeType, DetailAST parent) {
 DetailAST testNode = new BasicAST();
 testNode.setType(nodeType);
 testNode.setParent(parent);
 return testNode;
 }

 @Test
 public void testGetScopeFromMods() {
 DetailAST mods = new BasicAST();
 DetailAST publicToken = new BasicAST();
 publicToken.setText("public");
 mods.addChild(publicToken);
 DetailAST protectedToken = new BasicAST();
 protectedToken.setText("protected");
 mods.addChild(protectedToken);
 DetailAST privateToken = new BasicAST();
 privateToken.setText("private");
 mods.addChild(privateToken);

 DetailAST testNode = createTestNode(1, mods);
 Scope expected = Scope.PUBLIC;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);

 expected = Scope.PROTECTED;
 publicToken.setText("protected");
 actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);

 expected = Scope.PRIVATE;
 protectedToken.setText("private");
 actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);

 expected = Scope.PUBLIC;
 privateToken.setText("public");
 actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetScopeFromModsNullMods() {
 ScopeUtils.getScopeFromMods(null);
 }

 @Test
 public void testGetScopeFromModsEmptyMods() {
 DetailAST testNode = createTestNode(1, null);
 Scope actual = ScopeUtils.getScopeFromMods(null);
 Scope expected = Scope.PACKAGE;
 assertEquals(expected, actual);
 }

 @Test
 public void testGetScopeFromModsNoPublicProtectedOrPrivate() {
 DetailAST testNode = createTestNode(1, null);
 Scope actual = ScopeUtils.getScopeFromMods(null);
 Scope expected = Scope.PACKAGE;
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope() {
 DetailAST node = createTestNode(14, null);
 DetailAST parent = new BasicAST();
 parent.setType(14);
 parent.addChild(node);
 DetailAST expected = Scope.CLASS;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);

 node = createTestNode(15, null);
 parent = new BasicAST();
 parent.setType(15);
 parent.addChild(node);
 expected = Scope.PACKAGE;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);

 node = createTestNode(154, null);
 parent = new BasicAST();
 parent.setType(154);
 parent.addChild(node);
 expected = Scope.PACKAGE;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);

 node = createTestNode(157, null);
 parent = new BasicAST();
 parent.setType(157);
 parent.addChild(node);
 expected = Scope.ANONINNER;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);

 node = createTestNode(136, null);
 DetailAST parent2 = new BasicAST();
 parent2.setType(136);
 parent2.addChild(node);
 expected = Scope.ANONINNER;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceBlock() {
 DetailAST node = createTestNode(1, null);
 DetailAST parent = new BasicAST();
 parent.setType(15);
 parent.addChild(node);
 boolean expected = true;
 boolean actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(14);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(157);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(154);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(136);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInAnnotationBlock() {
 DetailAST node = createTestNode(1, null);
 DetailAST parent = new BasicAST();
 parent.setType(157);
 parent.addChild(node);
 boolean expected = true;
 boolean actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(15);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(14);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(136);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST node = createTestNode(1, null);
 DetailAST parent = new BasicAST();
 parent.setType(15);
 parent.addChild(node);
 boolean expected = true;
 boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(14);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(157);
 parent.addChild(node);
 expected = true;
 actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(136);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInEnumBlock() {
 DetailAST node = createTestNode(1, null);
 DetailAST parent = new BasicAST();
 parent.setType(154);
 parent.addChild(node);
 boolean expected = true;
 boolean actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(15);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(157);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(14);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(136);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInCodeBlock() {
 DetailAST node = createTestNode(1, null);
 DetailAST parent = new BasicAST();
 parent.setType(9);
 parent.addChild(node);
 boolean expected = true;
 boolean actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(8);
 parent.addChild(node);
 expected = true;
 actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(11);
 parent.addChild(node);
 expected = true;
 actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(12);
 parent.addChild(node);
 expected = true;
 actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(136);
 parent.addChild(node);
 expected = false;
 actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST node = createTestNode(14, null);
 boolean expected = false;
 boolean actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);

 node = createTestNode(15, null);
 expected = false;
 actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);

 node = createTestNode(157, null);
 expected = false;
 actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);

 node = createTestNode(154, null);
 expected = false;
 actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);

 node = createTestNode(1, null);
 expected = true;
 actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsLocalVariableDef() {
 DetailAST node = createTestNode(10, null);
 boolean expected = false;
 boolean actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);

 node = createTestNode(7, null);
 DetailAST parent = new BasicAST();
 parent.setType(1);
 parent.addChild(node);
 expected = true;
 actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);

 node = createTestNode(35, null);
 parent = new BasicAST();
 parent.setType(96);
 parent.addChild(node);
 expected = true;
 actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);

 node = createTestNode(156, null);
 parent = new BasicAST();
 parent.setType(7);
 parent.addChild(node);
 expected = true;
 actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);

 node = createTestNode(21, null);
 parent = new BasicAST();
 parent.setType(96);
 parent.addChild(node);
 expected = true;
 actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST node = createTestNode(10, null);
 boolean expected = false;
 boolean actual = ScopeUtils.isClassFieldDef(node);
 assertEquals(expected, actual);

 node = createTestNode(10, null);
 DetailAST parent = new BasicAST();
 parent.setType(1);
 parent.addChild(node);
 expected = true;
 actual = ScopeUtils.isClassFieldDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInScope() {
 DetailAST node = createTestNode(1, null);
 DetailAST parent = new BasicAST();
 parent.setType(1);
 parent.addChild(node);
 Scope expected = Scope.CLASS;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 assertEquals(Scope.CLASS, ScopeUtils.isInScope(node, Scope.CLASS));

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(14);
 parent.addChild(node);
 expected = Scope.PACKAGE;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 assertEquals(Scope.PACKAGE, ScopeUtils.isInScope(node, Scope.PACKAGE));

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(15);
 parent.addChild(node);
 expected = Scope.PACKAGE;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 assertEquals(Scope.PACKAGE, ScopeUtils.isInScope(node, Scope.PACKAGE));

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(157);
 parent.addChild(node);
 expected = Scope.ANONINNER;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 assertEquals(Scope.ANONINNER, ScopeUtils.isInScope(node, Scope.ANONINNER));

 node = createTestNode(1, null);
 parent = new BasicAST();
 parent.setType(154);
 parent.addChild(node);
 expected = Scope.PACKAGE;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 assertEquals(Scope.PACKAGE, ScopeUtils.isInScope(node, Scope.PACKAGE));
 }
}