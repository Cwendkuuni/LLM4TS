import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() {
 ClassInfo classInfo = new ClassInfo("java.lang.AbstractClass");
 assertTrue(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo classInfo = new ClassInfo("java.util.List");
 assertFalse(classInfo.isInterface());

 ClassInfo listInterface = new ClassInfo("java.util.List");
 assertTrue(listInterface.isInterface() || listInterface.isAbstract());
 }

 @Test
 public void testGetClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 assertEquals("java.lang.StringBuilder", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 assertNotNull(classInfo.getClassPackage());
 assertEquals("java.lang", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 assertNull(classInfo.getSuperClassName());

 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.Object", stringClassInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFound() {
 new ClassInfo("java.DoesNotExist");
 }

 @Test
 public void testPrintFields() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 String[] fields = stringClassInfo.printFields();
 assertNotNull(fields);
 assertEquals(0, fields.length);
 }

 @Test
 public void testPrintMethods() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 String[] methods = stringClassInfo.printMethods();
 assertNotNull(methods);
 assertEquals(0, methods.length);
 }

 @Test
 public void testPrintMethodsWithParam() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 String[] methods = stringClassInfo.printMethods(1);
 assertNull(methods);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 String[] constructors = stringClassInfo.printConstructors();
 assertNotNull(constructors);
 assertEquals(0, constructors.length);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo listClassInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = listClassInfo.printInterfaces();
 assertNotNull(interfaces);
 assertFalse(interfaces.length == 0);
 }
}