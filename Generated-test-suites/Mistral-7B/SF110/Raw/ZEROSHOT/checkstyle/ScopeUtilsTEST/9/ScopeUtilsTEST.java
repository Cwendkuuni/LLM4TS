package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;
import org.junit.*;

public class ScopeUtilsTest {

 @BeforeClass
 public static void setUp() {
 // Set up any static resources or initiate any static data here
 }

 @AfterClass
 public static void tearDown() {
 // Clean up any static resources here
 }

 @Before
 public void setUpBeforeEachTest() {
 // Set up any test-specific resources or initialize any test data here
 }

 @After
 public void tearDownAfterEachTest() {
 // Clean up any test-specific resources here
 }

 @Test
 public void testGetScopeFromMods() {
 DetailAST ast = new DetailAST();
 ast.setText("public class TestClass { }");

 Scope expected = Scope.PUBLIC;
 Scope actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());

 assertEquals(expected, actual);

 ast.setText("protected class TestClass { }");
 expected = Scope.PROTECTED;
 actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());

 assertEquals(expected, actual);

 ast.setText("private class TestClass { }");
 expected = Scope.PRIVATE;
 actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());

 assertEquals(expected, actual);

 ast.setText("package TestPackage; public class TestClass { }");
 expected = Scope.PACKAGE;
 actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());

 assertEquals(expected, actual);
 }

 @Test
 public void testGetScopeFromMods_noModifiers() {
 DetailAST ast = new DetailAST();
 ast.setText("class TestClass { }");

 Scope actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());

 assertEquals(Scope.PACKAGE, actual);
 }

 @Test
 public void testGetScopeFromMods_noModifiers_annotation() {
 DetailAST ast = new DetailAST();
 ast.setText("@interface TestInterface { }");

 Scope actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());

 assertEquals(Scope.ANNOTATION, actual);
 }

 @Test
 public void testGetScopeFromMods_multipleModifiers() {
 DetailAST ast = new DetailAST();
 ast.setText("public protected static final int TEST = 42;");

 Scope expected = Scope.PUBLIC_STATIC_FINAL;
 Scope actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());

 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope() {
 DetailAST ast = new DetailAST();
 ast.setText("package TestPackage; public class TestClass { }");

 DetailAST node = ast.findFirstToken(1); // TestClass

 Scope expected = Scope.PACKAGE;
 Scope actual = ScopeUtils.getSurroundingScope(node);

 assertEquals(expected, actual);

 DetailAST annotationNode = new DetailAST();
 annotationNode.setText("@Test public void test() { }");

 node = annotationNode.findFirstToken(1); // Test

 expected = Scope.ANNOTATION;
 actual = ScopeUtils.getSurroundingScope(node);

 assertEquals(expected, actual);

 ast.setText("public class TestClass { int test = 42; }");
 node = ast.findFirstChild(); // int

 expected = Scope.LOCAL_VARIABLE;
 actual = ScopeUtils.getSurroundingScope(node);

 assertEquals(expected, actual);

 ast.setText("public interface TestInterface { int test(); }");
 node = ast.findFirstToken(1); // TestInterface

 expected = Scope.INTERFACE;
 actual = ScopeUtils.getSurroundingScope(node);

 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceBlock() {
 DetailAST ast = new DetailAST();
 ast.setText("@interface TestInterface { }");

 assertTrue(ScopeUtils.isInInterfaceBlock(ast.findFirstToken(1)));

 ast.setText("public class TestClass { }");
 assertFalse(ScopeUtils.isInInterfaceBlock(ast.findFirstToken(1)));

 ast.setText("public interface TestInterface { int test(); } public class TestClass { }");
 assertFalse(ScopeUtils.isInInterfaceBlock(ast.findFirstToken(1)));
 }

 @Test
 public void testIsInAnnotationBlock() {
 DetailAST ast = new DetailAST();
 ast.setText("@Test public void test() { }");

 assertTrue(ScopeUtils.isInAnnotationBlock(ast.findFirstToken(1)));

 ast.setText("package TestPackage; public class TestClass { }");
 assertFalse(ScopeUtils.isInAnnotationBlock(ast.findFirstToken(1)));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST ast = new DetailAST();
 ast.setText("@Test public interface TestInterface { int test(); }");

 assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast.findFirstToken(1)));

 ast.setText("public class TestClass { }");
 assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast.findFirstToken(1)));
 }

 @Test
 public void testIsInEnumBlock() {
 DetailAST ast = new DetailAST();
 ast.setText("public enum TestEnum { VALUE }");

 assertTrue(ScopeUtils.isInEnumBlock(ast.findFirstChild()));

 ast.setText("public class TestClass { }");
 assertFalse(ScopeUtils.isInEnumBlock(ast.findFirstChild()));
 }

 @Test
 public void testIsInCodeBlock() {
 DetailAST ast = new DetailAST();
 ast.setText("public class TestClass { int test = 42; }");

 assertTrue(ScopeUtils.isInCodeBlock(ast.findFirstChild()));

 ast.setText("public interface TestInterface { int test(); }");
 assertFalse(ScopeUtils.isInCodeBlock(ast.findFirstToken(1)));
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST ast = new DetailAST();
 ast.setText("public class TestClass { }");

 assertTrue(ScopeUtils.isOuterMostType(ast.findFirstChild()));

 ast.setText("public class TestClass { public static class InnerClass { } }");
 assertFalse(ScopeUtils.isOuterMostType(ast.findFirstChild()));
 }

 @Test
 public void testIsLocalVariableDef() {
 DetailAST ast = new DetailAST();
 ast.setText("public class TestClass { int test = 42; }");

 DetailAST node = ast.findFirstToken(2); // int

 assertTrue(ScopeUtils.isLocalVariableDef(node));

 ast.setText("public class TestClass { public int test; }");
 assertTrue(ScopeUtils.isLocalVariableDef(node));

 ast.setText("public class TestClass { void test() { int test = 42; } }");
 assertTrue(ScopeUtils.isLocalVariableDef(ast.findFirstToken(9))); // int

 ast.setText("public class TestClass { int test; void test() { } }");
 assertTrue(ScopeUtils.isLocalVariableDef(ast.findFirstToken(2))); // int
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST ast = new DetailAST();
 ast.setText("public class TestClass { int test = 42; }");

 DetailAST node = ast.findFirstToken(1); // TestClass

 assertTrue(ScopeUtils.isClassFieldDef(node.findFirstChild()));

 ast.setText("public class TestClass { public int test; }");
 assertTrue(ScopeUtils.isClassFieldDef(node.findFirstChild()));

 ast.setText("public class TestClass { int test; public int test2; }");
 assertTrue(ScopeUtils.isClassFieldDef(ast.findFirstChild()));
 }

 @Test
 public void testIsInScope() {
 DetailAST ast = new DetailAST();
 ast.setText("public class TestClass { int test = 42; }");

 DetailAST node = ast.findFirstChild(); // int
 Scope expected = Scope.LOCAL_VARIABLE;
 Scope actual = ScopeUtils.getSurroundingScope(node);

 assertTrue(ScopeUtils.isInScope(ast.findFirstChild(), Scope.LOCAL));
 assertTrue(ScopeUtils.isInScope(node, Scope.LOCAL));
 assertTrue(ScopeUtils.isInScope(node, expected));
 assertTrue(ScopeUtils.isInScope(node, actual));

 expected = Scope.CLASS;
 assertTrue(ScopeUtils.isInScope(ast.findFirstChild(), expected));
 assertTrue(ScopeUtils.isInScope(node, Scope.CLASS));
 assertFalse(ScopeUtils.isInScope(node, Scope.INTERFACE));
 assertFalse(ScopeUtils.isInScope(node, Scope.ANNOTATION));
 assertFalse(ScopeUtils.isInScope(node, Scope.ANONINNER));
 assertFalse(ScopeUtils.isInScope(node, Scope.PACKAGE));
 }

 @Test
 public void testIsInScope_invalidScope() {
 DetailAST ast = new DetailAST();
 ast.setText("public class TestClass { int test = 42; }");

 DetailAST node = ast.findFirstChild(); // int

 assertFalse(ScopeUtils.isInScope(node, Scope.INVALID));
 }
}