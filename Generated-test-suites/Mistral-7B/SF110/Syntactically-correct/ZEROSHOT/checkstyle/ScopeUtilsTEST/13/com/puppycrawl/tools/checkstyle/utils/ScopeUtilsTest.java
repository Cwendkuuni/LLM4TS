package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.*;
import antlr.collections.*;

import org.junit.*;

public class ScopeUtilsTest {

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 // Set up class here if needed
 }

 @AfterClass
 public static void tearDownAfterClass() throws Exception {
 // Tear down class here if needed
 }

 @Before
 public void setUp() {
 // Set up for each test here
 }

 @After
 public void tearDown() {
 // Tear down for each test here
 }

 @Test
 public void testGetScopeFromMods() {
 DetailAST mods = new DetailAST(); // initialize with validmods
 Scope expected = Scope.PUBLIC;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);

 // test with other scopes
 expected = Scope.PROTECTED;
 mods.setText("protected class TestClass { }");
 actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);

 expected = Scope.PRIVATE;
 mods.setText("private class TestClass { }");
 actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);

 expected = Scope.PACKAGE;
 mods.setText("class TestClass { }");
 actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);

 // test with empty mods
 actual = ScopeUtils.getScopeFromMods(new DetailAST());
 assertEquals(Scope.PACKAGE, actual);
 }

 @Test
 public void testGetSurroundingScope() {
 DetailAST node = new DetailAST(); // initialize with valid node
 Scope expected = Scope.PACKAGE;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);

 // test with inner class scope
 node.setText("public class OuterClass { public class InnerClass {} }");
 expected = Scope.ANONINNER;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);

 // test with interface block scope
 node.setText("public interface TestInterface {}", new DetailAST());
 expected = Scope.INTERFACE;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);

 // test with annotation block scope
 node.setText("@Test public class TestClass { @Annotation annotation; }", new DetailAST());
 expected = Scope.ANNOTATION;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);

 // test with enum block scope
 node.setText("enum TestEnum { VALUE, VALUE2 }", new DetailAST());
 expected = Scope.ENUM;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);

 // test with code block scope
 node.setText("public void test() { }", new DetailAST());
 expected = Scope.CODE;
 actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);

 // test with outermost type scope
 node.setText("public class OuterClass { public class InnerClass { } }", new DetailAST());
 expected = Scope.CLASS;
 actual = ScopeUtils.getSurroundingScope(node.getFirstChild());
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceBlock() {
 DetailAST node = new DetailAST(); // initialize with valid node
 boolean expected = false;
 boolean actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);

 // test with interface block
 node.setText("public interface TestInterface {}", new DetailAST());
 expected = true;
 actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInAnnotationBlock() {
 DetailAST node = new DetailAST(); // initialize with valid node
 boolean expected = false;
 boolean actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);

 // test with annotation block
 node.setText("@Test public class TestClass { @Annotation annotation; }", new DetailAST());
 expected = true;
 actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST node = new DetailAST(); // initialize with valid node
 boolean expected = false;
 boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);

 // test with interface block
 node.setText("public interface TestInterface {}", new DetailAST());
 expected = true;
 actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);

 // test with annotation block
 node.setText("@Test public class TestClass { @Annotation annotation; }", new DetailAST());
 expected = true;
 actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);

 // test with neither interface nor annotation block
 node.setText("public class TestClass { }", new DetailAST());
 expected = false;
 actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInEnumBlock() {
 DetailAST node = new DetailAST(); // initialize with valid node
 boolean expected = false;
 boolean actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);

 // test with enum block
 node.setText("enum TestEnum { VALUE, VALUE2 }", new DetailAST());
 expected = true;
 actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInCodeBlock() {
 DetailAST node = new DetailAST(); // initialize with valid node
 boolean expected = false;
 boolean actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);

 // test with code block
 node.setText("public void test() { }", new DetailAST());
 expected = true;
 actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST node = new DetailAST(); // initialize with valid node
 boolean expected = true;
 boolean actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);

 // test with inner class
 node.setText("public class OuterClass { public class InnerClass {} }", new DetailAST());
 expected = false;
 actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsLocalVariableDef() {
 DetailAST node = new DetailAST(); // initialize with valid node
 boolean expected = false;
 boolean actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);

 // test with local variable
 node.setText("int x = 1;", new DetailAST());
 expected = true;
 actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);

 // test with class field
 node.setText("int x;", new DetailAST());
 expected = false;
 actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST node = new DetailAST(); // initialize with valid node
 boolean expected = false;
 boolean actual = ScopeUtils.isClassFieldDef(node);
 assertEquals(expected, actual);

 // test with class field
 node.setText("int x;", new DetailAST());
 expected = true;
 actual = ScopeUtils.isClassFieldDef(node);
 assertEquals(expected, actual);

 // test with local variable
 node.setText("int x = 1;", new DetailAST());
 expected = false;
 actual = ScopeUtils.isClassFieldDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInScope() {
 DetailAST ast = new DetailAST(); // initialize with valid ast
 Scope scope = Scope.INTERFACE;
 boolean expected = false;
 boolean actual = ScopeUtils.isInScope(ast, scope);
 assertEquals(expected, actual);

 // test with ast in scope
 ast.setText("public interface TestInterface {}", new DetailAST());
 DetailAST node = ast.findFirstToken(5);
 expected = true;
 actual = ScopeUtils.isInScope(node, scope);
 assertEquals(expected, actual);
 }

}