import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import com.jstevh.viewer.*;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 ClassInfo testClass = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(testClass.isAbstract());
 ClassInfo nonAbstractClass = new ClassInfo("java.lang.StringBuilder");
 assertFalse(nonAbstractClass.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo testInterface = new ClassInfo("java.util.List");
 assertTrue(testInterface.isInterface());
 ClassInfo nonInterfaceClass = new ClassInfo("java.util.ArrayList");
 assertFalse(nonInterfaceClass.isInterface());
 }

 @Test
 public void testGetClassName() {
 ClassInfo testClass = new ClassInfo("java.lang.StringBuilder");
 assertEquals("java.lang.StringBuilder", testClass.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo testClass = new ClassInfo("java.lang.StringBuilder");
 assertEquals("java.lang", testClass.getClassPackage());
 ClassInfo testClassWithNoPackage = new ClassInfo("java.lang.String");
 assertNull(testClassWithNoPackage.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.AbstractList", testClass.getSuperClassName());
 ClassInfo testClassWithNoSuperclass = new ClassInfo("java.lang.Object");
 assertNull(testClassWithNoSuperclass.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testConstructorWithInvalidClassname() {
 new ClassInfo("InvalidClass");
 }

 @Test(expected = NullPointerException.class)
 public void testPrintMethodsWithNullClass() {
 new ClassInfo(null).printMethods();
 }

 @Test
 public void testPrintMethods() {
 ClassInfo testClass = new ClassInfo("java.lang.StringBuilder");
 String[] expectedMethods = new String[]{"append(char)", "append(char, int)", "append(String)", "capacity()", "charAt(int)", "delete(int, int)", "ensureCapacity(int)", "getLength()", "insert(int, char)", "length()"};
 String[] actualMethods = testClass.printMethods();
 assertArrayEquals(expectedMethods, actualMethods);
 }

 @Test
 public void testPrintMethodsWithIntParameter() {
 ClassInfo testClass = new ClassInfo("java.lang.StringBuilder");
 String[] expectedMethods = new String[]{"append(int)"};
 String[] actualMethods = testClass.printMethods(1);
 assertArrayEquals(expectedMethods, actualMethods);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 String[] expectedConstructors = new String[]{"ArrayList()", "ArrayList(int)"};
 String[] actualConstructors = testClass.printConstructors();
 assertArrayEquals(expectedConstructors, actualConstructors);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 String[] expectedInterfaces = new String[]{"java.util.List", "java.util.RandomAccess"};
 String[] actualInterfaces = testClass.printInterfaces();
 assertArrayEquals(expectedInterfaces, actualInterfaces);
 }
}