package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;
import org.junit.Test;

public class ScopeUtilsTest {

 private DetailAST createMockDetailAST(int type, String text) {
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
 public void testGetScopeFromMods_Public() {
 DetailAST mods = new DefaultAST();
 DetailAST publicToken = createMockDetailAST(TokenTypes.KEYWORD, "public");
 mods.addChild(publicToken);
 Scope expected = Scope.PUBLIC;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetScopeFromMods_Protected() {
 DetailAST mods = new DefaultAST();
 DetailAST protectedToken = createMockDetailAST(TokenTypes.KEYWORD, "protected");
 mods.addChild(protectedToken);
 Scope expected = Scope.PROTECTED;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetScopeFromMods_Private() {
 DetailAST mods = new DefaultAST();
 DetailAST privateToken = createMockDetailAST(TokenTypes.KEYWORD, "private");
 mods.addChild(privateToken);
 Scope expected = Scope.PRIVATE;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetScopeFromMods_EmptyMods() {
 DetailAST mods = new DefaultAST();
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test
 public void testGetScopeFromMods_MultipleScopes() {
 DetailAST mods = new DefaultAST();
 DetailAST publicToken = createMockDetailAST(TokenTypes.KEYWORD, "public");
 DetailAST protectedToken = createMockDetailAST(TokenTypes.KEYWORD, "protected");
 mods.addChild(publicToken);
 mods.addChild(protectedToken);
 Scope expected = Scope.PROTECTED; // Protected has higher precedence than Public
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_Class() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.CLASS_DEF;
 }
 };
 Scope expected = Scope.PACKAGE; // default package
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_Method() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.METHOD_DEF;
 }
 };
 Scope expected = Scope.PACKAGE; // default package
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_AnonymousClass() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.ANON_CLASS_DEF;
 }
 };
 Scope expected = Scope.ANONINNER;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_Interface() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.INTERFACE_DEF;
 }
 };
 Scope expected = Scope.INTERFACE;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_Annotation() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.ANNOTATION;
 }
 };
 Scope expected = Scope.PACKAGE; // default package
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceBlock_Interface() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.INTERFACE_DEF;
 }
 };
 boolean expected = true;
 boolean actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceBlock_Class() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.CLASS_DEF;
 }
 };
 boolean expected = false;
 boolean actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInAnnotationBlock_Annotation() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.ANNOTATION;
 }
 };
 boolean expected = true;
 boolean actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInAnnotationBlock_Class() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.CLASS_DEF;
 }
 };
 boolean expected = false;
 boolean actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_Interface() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.INTERFACE_DEF;
 }
 };
 boolean expected = true;
 boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_Class() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.CLASS_DEF;
 }
 };
 boolean expected = false;
 boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInEnumBlock_Enum() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.ENUM_DEF;
 }
 };
 boolean expected = true;
 boolean actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInEnumBlock_Class() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.CLASS_DEF;
 }
 };
 boolean expected = false;
 boolean actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInCodeBlock_CodeBlock() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.BLOCK;
 }
 };
 boolean expected = true;
 boolean actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInCodeBlock_Comment() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.COMMENT;
 }
 };
 boolean expected = false;
 boolean actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsOuterMostType_OuterMostType() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.CLASS_DEF;
 }
 };
 boolean expected = true;
 boolean actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsOuterMostType_InnerClass() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.ANON_CLASS_DEF;
 }
 };
 boolean expected = false;
 boolean actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsLocalVariableDef_LocalVariableDef() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.VAR_DEF;
 }
 };
 boolean expected = true;
 boolean actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsLocalVariableDef_ClassField() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.FIELD_DEF;
 }
 };
 boolean expected = false;
 boolean actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsClassFieldDef_ClassField() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.FIELD_DEF;
 }
 };
 boolean expected = true;
 boolean actual = ScopeUtils.isClassFieldDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsClassFieldDef_LocalVariable() {
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.VAR_DEF;
 }
 };
 boolean expected = false;
 boolean actual = ScopeUtils.isClassFieldDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInScope_InScope() {
 DetailAST ast = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.CLASS_DEF;
 }
 };
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.FIELD_DEF;
 }
 };
 Scope scope = Scope.CLASS;
 boolean expected = true;
 boolean actual = ScopeUtils.isInScope(ast, scope);
 assertTrue(expected == actual);
 boolean actual2 = ScopeUtils.isInScope(node, scope);
 assertTrue(expected == actual2);
 }

 @Test
 public void testIsInScope_OutOfScope() {
 DetailAST ast = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.CLASS_DEF;
 }
 };
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.METHOD_DEF;
 }
 };
 Scope scope = Scope.CLASS;
 boolean expected = false;
 boolean actual = ScopeUtils.isInScope(ast, scope);
 assertTrue(expected == actual);
 boolean actual2 = ScopeUtils.isInScope(node, scope);
 assertTrue(expected == actual2);
 }

 @Test
 public void testIsInScope_DifferentScope() {
 DetailAST ast = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.INTERFACE_DEF;
 }
 };
 DetailAST node = new DetailAST() {
 @Override
 public int getType() {
 return TokenTypes.FIELD_DEF;
 }
 };
 Scope scope = Scope.CLASS;
 boolean expected = false;
 boolean actual = ScopeUtils.isInScope(ast, scope);
 assertTrue(expected == actual);
 boolean actual2 = ScopeUtils.isInScope(node, scope);
 assertTrue(expected == actual2);
 }
}