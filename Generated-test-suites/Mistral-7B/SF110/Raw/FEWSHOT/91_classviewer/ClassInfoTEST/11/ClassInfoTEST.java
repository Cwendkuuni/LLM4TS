package com.jstevh.viewer.test;import static org.junit.Assert.*;import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Before
 public void setUp() {
 try {
 classInfo = new ClassInfo("java.lang.String");
 } catch (ClassNotFoundException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testIsAbstract() {
 assertFalse(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() {
 assertFalse(classInfo.isInterface());
 }

 @Test
 public void testGetClassName() {
 assertEquals("java.lang.String", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 assertEquals("java.lang", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 assertNull(classInfo.getSuperClassName());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullName() {
 new ClassInfo(null);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testConstructorUnknownClass() {
 new ClassInfo("UnknownClass");
 }

 @Test
 public void testPrintFields() {
 assertEquals(0, classInfo.printFields().length);
 }

 @Test
 public void testPrintMethods() {
 String[] methods = classInfo.printMethods();
 assertEquals(56, methods.length);
 }

 @Test
 public void testPrintMethodsParam() {
 String[] methods = classInfo.printMethods(1);
 assertEquals(1, methods.length);
 }

 @Test
 public void testPrintConstructors() {
 String[] constructors = classInfo.printConstructors();
 assertEquals(1, constructors.length);
 }

 @Test
 public void testPrintInterfaces() {
 String[] interfaces = classInfo.printInterfaces();
 assertEquals(0, interfaces.length);
 }

 @Test
 public void testSrchMethods() {
 String[] methods = classInfo.srchMethods("toString");
 assertEquals(1, methods.length);
 MethodData md = classInfo.getFoundMethod(0);
 assertEquals("java.lang.String", md.getMethClass());
 assertEquals("toString", md.getMethName());
 }

 @Test
 public void testSrchMethodsNull() {
 assertNull(classInfo.srchMethods(null));
 }

 @Test
 public void testSrchMethodsNullIndex() {
 assertNull(classInfo.getFoundMethod(100));
 }
}