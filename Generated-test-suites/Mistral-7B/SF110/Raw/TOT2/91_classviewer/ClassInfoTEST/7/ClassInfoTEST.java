import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*; // for convenience
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 ClassInfo abstractClass = new ClassInfo("java.lang.AbstractClass");
 assertTrue(abstractClass.isAbstract());
 ClassInfo regularClass = new ClassInfo("java.lang.String");
 assertFalse(regularClass.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo interfaceClass = new ClassInfo("java.util.List");
 assertTrue(interfaceClass.isInterface());
 ClassInfo regularClass = new ClassInfo("java.lang.String");
 assertFalse(regularClass.isInterface());
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
 ClassInfo classInfo2 = new ClassInfo("java.util.ArrayList"); // same class instance
 assertEquals("java.util.Collection", classInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("nonExistentClass");
 }

 @Test
 public void testPrintMethods() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertEquals(18, methods.length);
 }

 @Test
 public void testPrintMethodsWithParam() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
 assertNotNull(methods);
 assertEquals(11, methods.length);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertEquals(4, constructors.length);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertEquals(1, interfaces.length);
 assertEquals("java.util.Collection", interfaces[0]);
 }
}