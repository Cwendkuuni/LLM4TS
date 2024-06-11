import org.junit.Test;
import org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() {
 ClassInfo classInfo = new ClassInfo("java.util.AbstractList");
 assertTrue(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertFalse(classInfo.isInterface());
 }

 @Test
 public void testGetClassName() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.ArrayList", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertNull(classInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testInvalidClassName() {
 ClassInfo classInfo = new ClassInfo("java.doesnotexist.Class");
 fail("Exception not thrown");
 }

 @Test
 public void testPrintMethods() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 }

 @Test
 public void testPrintMethodsWithParam() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods(1);
 assertNotNull(methods);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 }

 @Test
 public void testSrchMethods() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.srchMethods("addAll");
 assertNotNull(methods);
 }

 @Test
 public void testGetFoundMethod() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 MethodData methodData = classInfo.getFoundMethod(0);
 assertNotNull(methodData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 ClassInfo classInfo = new ClassInfo(null);
 fail("Exception not thrown");
 }

 @Test(expected = IllegalAccessException.class)
 public void testAccessPrivateFields() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 try {
 final Field field = classInfo.c.getDeclaredField("size");
 field.setAccessible(true);
 final int size = (int) field.get(classInfo);
 fail("Access to private field should not be possible");
 }
 catch (IllegalAccessException e) {
 // expected
 }
 catch (NoSuchFieldException e) {
 fail("Field 'size' should exist");
 }
 }
}