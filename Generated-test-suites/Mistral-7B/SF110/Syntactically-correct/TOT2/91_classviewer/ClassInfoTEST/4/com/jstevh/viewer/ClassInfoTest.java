import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 ClassInfo testClassInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(testClassInfo.isAbstract());

 ClassInfo notAbstractClassInfo = new ClassInfo("java.lang.StringBuilder");
 assertFalse(notAbstractClassInfo.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo interfaceClassInfo = new ClassInfo("java.util.List");
 assertTrue(interfaceClassInfo.isInterface());

 ClassInfo notInterfaceClassInfo = new ClassInfo("java.util.ArrayList");
 assertFalse(notInterfaceClassInfo.isInterface());
 }

 @Test
 public void testGetClassName() {
 ClassInfo testClassInfo = new ClassInfo("java.lang.String");
 String className = testClassInfo.getClassName();
 assertEquals("java.lang.String", className);
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo testClassInfo = new ClassInfo("java.util.ArrayList");
 String packageName = testClassInfo.getClassPackage();
 assertEquals("java.util", packageName);
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo testClassInfo = new ClassInfo("java.util.ArrayList");
 String superClassName = testClassInfo.getSuperClassName();
 assertEquals("java.util.AbstractList", superClassName);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 ClassInfo classInfo = new ClassInfo("com.jstevh.nonExistentClass");
 fail("ClassNotFoundException not thrown");
 }

 @Test
 public void testPrintMethods() {
 ClassInfo testClassInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = testClassInfo.printMethods();
 assertNotNull(methods);
 assertEquals(15, methods.length);
 }

 @Test
 public void testPrintFields() {
 ClassInfo testClassInfo = new ClassInfo("java.util.ArrayList");
 String[] fields = testClassInfo.printFields();
 assertNotNull(fields);
 assertEquals(8, fields.length);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo testClassInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = testClassInfo.printConstructors();
 assertNotNull(constructors);
 assertEquals(5, constructors.length);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo testClassInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = testClassInfo.printInterfaces();
 assertNotNull(interfaces);
 assertEquals(1, interfaces.length);
 assertEquals("java.util.List", interfaces[0]);
 }

 @Test
 public void testSrchMethods() {
 ClassInfo testClassInfo = new ClassInfo("java.util.ArrayList");
 String searchString = "add";
 String[] methods = testClassInfo.srchMethods(searchString);
 assertNotNull(methods);
 assertEquals(4, methods.length);
 }

 @Test
 public void testSrchMethodsWithIndex() {
 ClassInfo testClassInfo = new ClassInfo("java.util.ArrayList");
 String searchString = "remove";
 String[] index = {"int", "int"};
 String[] methods = testClassInfo.srchMethods(searchString, index);
 assertNotNull(methods);
 assertEquals(1, methods.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 ClassInfo classInfo = new ClassInfo("", null);
 fail("IllegalArgumentException not thrown");
 }

 @Test(expected = IllegalAccessException.class)
 public void testIllegalAccessException() {
 ClassInfo classInfo = new ClassInfo("java.lang.Object");
 try {
 classInfo.printFields();
 fail("IllegalAccessException not thrown");
 }
 catch (IllegalAccessException e) {
 // expected
 }
 }
}