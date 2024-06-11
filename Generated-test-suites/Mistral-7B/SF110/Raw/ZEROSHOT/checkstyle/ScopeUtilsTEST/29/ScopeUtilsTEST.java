package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.*;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

 private DetailAST createNode(final int type, final String text) {
 return new DetailAST() {
 @Override
 public int getType() {
 return type;
 }

 @Override
 public String getText() {
 return text;
 }
 };
 }

 @Test
 public void testGetScopeFromMods() {
 Scope scope;
 DetailAST mods = new BasicAST();

 // Public scope
 mods.addChild(createNode(TokenTypes.KEYWORD, "public"));
 scope = ScopeUtils.getScopeFromMods(mods);
 assertEquals(Scope.PUBLIC, scope);

 // Protected scope
 mods.addChild(createNode(TokenTypes.KEYWORD, "protected"));
 scope = ScopeUtils.getScopeFromMods(mods);
 assertEquals(Scope.PROTECTED, scope);

 // Private scope
 mods.addChild(createNode(TokenTypes.KEYWORD, "private"));
 scope = ScopeUtils.getScopeFromMods(mods);
 assertEquals(Scope.PRIVATE, scope);

 // No scope
 mods.addChild(createNode(TokenTypes.IDENTIFIER, "test"));
 scope = ScopeUtils.getScopeFromMods(mods);
 assertEquals(Scope.PACKAGE, scope);
 }

 @Test
 public void testGetSurroundingScope() {
 DetailAST node;
 Scope scope;

 // Package scope
 node = new BasicAST();
 scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.PACKAGE, scope);

 // Class scope
 node.setType(TokenTypes.CLASS_DEF);
 scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.CLASS, scope);

 // Interface scope
 node.setType(TokenTypes.INTERFACE_DEF);
 scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.INTERFACE, scope);

 // Anonymous class scope
 node.setType(TokenTypes.ANON_CLASS_DEF);
 scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.ANONINNER, scope);

 // Method scope
 node.setType(TokenTypes.METHOD_DEF);
 scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.METHOD, scope);

 // Variable scope
 node.setType(TokenTypes.VAR_DEF);
 scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.LOCAL, scope);

 // Interface block scope
 node.setParent(new BasicAST().setType(TokenTypes.INTERFACE_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.INTERFACE, scope);

 // Annotation block scope
 node.setParent(new BasicAST().setType(TokenTypes.ANNOTATION));
 scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.ANNOTATION, scope);

 // Enum block scope
 node.setParent(new BasicAST().setType(TokenTypes.ENUM_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.ENUM, scope);

 // Code block scope
 node.setParent(new BasicAST().setType(TokenTypes.BLOCK));
 scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.CODE_BLOCK, scope);

 // Inner class with outermost type
 node.setParent(new BasicAST().setType(TokenTypes.CLASS_DEF).setParent(new BasicAST().setType(TokenTypes.CLASS_DEF)));
 scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(Scope.OUTERMOST_TYPE, scope);
 }

 @Test
 public void testIsInInterfaceBlock() {
 DetailAST node;
 boolean inInterfaceBlock;

 // Not in interface block
 node = new BasicAST();
 inInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);
 assertFalse(inInterfaceBlock);

 // In interface block
 node.setParent(new BasicAST().setType(TokenTypes.INTERFACE_DEF));
 inInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);
 assertTrue(inInterfaceBlock);
 }

 @Test
 public void testIsInAnnotationBlock() {
 DetailAST node;
 boolean inAnnotationBlock;

 // Not in annotation block
 node = new BasicAST();
 inAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);
 assertFalse(inAnnotationBlock);

 // In annotation block
 node.setParent(new BasicAST().setType(TokenTypes.ANNOTATION));
 inAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);
 assertTrue(inAnnotationBlock);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST node;
 boolean inInterfaceOrAnnotationBlock;

 // Not in interface or annotation block
 node = new BasicAST();
 inInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertFalse(inInterfaceOrAnnotationBlock);

 // In interface block
 node.setParent(new BasicAST().setType(TokenTypes.INTERFACE_DEF));
 inInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertTrue(inInterfaceOrAnnotationBlock);

 // In annotation block
 node.setParent(new BasicAST().setType(TokenTypes.ANNOTATION));
 inInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertTrue(inInterfaceOrAnnotationBlock);
 }

 @Test
 public void testIsInEnumBlock() {
 DetailAST node;
 boolean inEnumBlock;

 // Not in enum block
 node = new BasicAST();
 inEnumBlock = ScopeUtils.isInEnumBlock(node);
 assertFalse(inEnumBlock);

 // In enum block
 node.setParent(new BasicAST().setType(TokenTypes.ENUM_DEF));
 inEnumBlock = ScopeUtils.isInEnumBlock(node);
 assertTrue(inEnumBlock);
 }

 @Test
 public void testIsInCodeBlock() {
 DetailAST node;
 boolean inCodeBlock;

 // Not in code block
 node = new BasicAST();
 inCodeBlock = ScopeUtils.isInCodeBlock(node);
 assertFalse(inCodeBlock);

 // In code block
 node.setParent(new BasicAST().setType(TokenTypes.BLOCK));
 inCodeBlock = ScopeUtils.isInCodeBlock(node);
 assertTrue(inCodeBlock);
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST node;
 boolean isOuterMostType;

 // Is outermost type
 node = new BasicAST().setType(TokenTypes.CLASS_DEF).setParent(null);
 isOuterMostType = ScopeUtils.isOuterMostType(node);
 assertTrue(isOuterMostType);

 // Not outermost type
 node = new BasicAST().setType(TokenTypes.CLASS_DEF);
 isOuterMostType = ScopeUtils.isOuterMostType(node);
 assertFalse(isOuterMostType);
 }

 @Test
 public void testIsLocalVariableDef () {
 DetailAST node;
 boolean isLocalVariableDef;

 // Not a local variable definition
 node = new BasicAST().setType(TokenTypes.CLASS_DEF);
 isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
 assertFalse(isLocalVariableDef);

 // Local variable definition in class
 node = new BasicAST().setType(TokenTypes.VAR_DEF).setParent(new BasicAST().setType(TokenTypes.CLASS_DEF));
 isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
 assertTrue(isLocalVariableDef);

 // Local variable definition in method
 node.setParent(new BasicAST().setType(TokenTypes.METHOD_DEF));
 isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
 assertTrue(isLocalVariableDef);
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST node;
 boolean isClassFieldDef;

 // Not a class field definition
 node = new BasicAST().setType(TokenTypes.VAR_DEF).setParent(new BasicAST().setType(TokenTypes.CLASS_DEF));
 isClassFieldDef = ScopeUtils.isClassFieldDef(node);
 assertFalse(isClassFieldDef);

 // Class field definition
 node = new BasicAST().setType(TokenTypes.FIELD_DEF).setParent(new BasicAST().setType(TokenTypes.CLASS_DEF));
 isClassFieldDef = ScopeUtils.isClassFieldDef(node);
 assertTrue(isClassFieldDef);
 }

 @Test
 public void testIsInScope() {
 DetailAST node;
 Scope scope;
 Scope expectedScope;

 // Inner class in class scope
 node = new BasicAST().setType(TokenTypes.ANON_CLASS_DEF).setParent(new BasicAST().setType(TokenTypes.CLASS_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.CLASS;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Inner class in method scope
 node.setParent(new BasicAST().setType(TokenTypes.METHOD_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.METHOD;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Local variable in method scope
 node = new BasicAST().setType(TokenTypes.VAR_DEF).setParent(new BasicAST().setType(TokenTypes.METHOD_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.LOCAL;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Class field in class scope
 node = new BasicAST().setType(TokenTypes.FIELD_DEF).setParent(new BasicAST().setType(TokenTypes.CLASS_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.CLASS;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Enum constant in enum scope
 node = new BasicAST().setType(TokenTypes.IDENTIFIER).setParent(new BasicAST().setType(TokenTypes.ENUM_CONSTANT_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.ENUM;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Variable in interface scope
 node = new BasicAST().setType(TokenTypes.VAR_DEF).setParent(new BasicAST().setType(TokenTypes.INTERFACE_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.INTERFACE;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Variable in interface block scope
 node = new BasicAST().setType(TokenTypes.VAR_DEF).setParent(new BasicAST().setType(TokenTypes.INTERFACE_BLOCK));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.INTERFACE;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Method in interface scope
 node = new BasicAST().setType(TokenTypes.METHOD_DEF).setParent(new BasicAST().setType(TokenTypes.INTERFACE_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.INTERFACE;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Method in interface block scope
 node = new BasicAST().setType(TokenTypes.METHOD_DEF).setParent(new BasicAST().setType(TokenTypes.INTERFACE_BLOCK));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.INTERFACE;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Class in package scope
 node = new BasicAST().setType(TokenTypes.CLASS_DEF).setParent(new BasicAST().setType(TokenTypes.PACKAGE_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.PACKAGE;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Class in package block scope
 node = new BasicAST().setType(TokenTypes.CLASS_DEF).setParent(new BasicAST().setType(TokenTypes.PACKAGE_BLOCK));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.PACKAGE;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Anonymous class in method scope
 node = new BasicAST().setType(TokenTypes.ANON_CLASS_DEF).setParent(new BasicAST().setType(TokenTypes.METHOD_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.METHOD;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Anonymous class in method block scope
 node = new BasicAST().setType(TokenTypes.ANON_CLASS_DEF).setParent(new BasicAST().setType(TokenTypes.BLOCK));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.CODE_BLOCK;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Anonymous class in class scope
 node = new BasicAST().setType(TokenTypes.ANON_CLASS_DEF).setParent(new BasicAST().setType(TokenTypes.CLASS_DEF));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.CLASS;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Variable in code block scope
 node = new BasicAST().setType(TokenTypes.VAR_DEF).setParent(new BasicAST().setType(TokenTypes.BLOCK));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.CODE_BLOCK;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Method in code block scope
 node = new BasicAST().setType(TokenTypes.METHOD_DEF).setParent(new BasicAST().setType(TokenTypes.BLOCK));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.CODE_BLOCK;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Class in code block scope
 node = new BasicAST().setType(TokenTypes.CLASS_DEF).setParent(new BasicAST().setType(TokenTypes.BLOCK));
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = Scope.CODE_BLOCK;
 assertTrue(ScopeUtils.isInScope(node, expectedScope));

 // Variable outside of any scope
 node = new BasicAST();
 scope = ScopeUtils.getSurroundingScope(node);
 expectedScope = null;
 assertNull(ScopeUtils.isInScope(node, expectedScope));
 }
}