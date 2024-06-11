import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 ClassInfo abstractClass = new ClassInfo("java.lang.AbstractClassName");
 assertTrue(abstractClass.isAbstract());
 ClassInfo nonAbstractClass = new ClassInfo("java.lang.NonAbstractClassName");
 assertFalse(nonAbstractClass.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo interfaceClass = new ClassInfo("java.util.List");
 assertTrue(interfaceClass.isInterface());
 ClassInfo nonInterfaceClass = new ClassInfo("java.lang.String");
 assertFalse(nonInterfaceClass.isInterface());
 }

 @Test
 public void testGetClassName() {
 ClassInfo classNameTest = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", classNameTest.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo packageTest = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util", packageTest.getClassPackage());
 ClassInfo noPackageClass = new ClassInfo("java.lang.String");
 assertNull(noPackageClass.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo superClassNameTest = new ClassInfo("java.lang.Object");
 assertNull(superClassNameTest.getSuperClassName());
 ClassInfo subClassTest = new ClassInfo("java.util.ArrayList");
 assertEquals("java.lang.Object", subClassTest.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("com.nonexistent.ClassName");
 }

 @Test
 public void testPrintFields() {
 ClassInfo fieldsTest = new ClassInfo("java.util.Hashtable");
 String[] fields = fieldsTest.printFields();
 assertNotNull(fields);
 assertEquals(fields.length, 11);
 }

 @Test
 public void testPrintMethods() {
 ClassInfo methodsTest = new ClassInfo("java.util.Hashtable");
 String[] methods = methodsTest.printMethods();
 assertNotNull(methods);
 assertEquals(methods.length, 22);
 }

 @Test
 public void testPrintMethodsWithParam() {
 ClassInfo methodsTest = new ClassInfo("java.util.Hashtable");
 String[] methods = methodsTest.printMethods(1);
 assertNotNull(methods);
 assertEquals(methods.length, 1);
 assertEquals(methods[0], "public boolean isEmpty()");
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo constructorsTest = new ClassInfo("java.util.ArrayList");
 String[] constructors = constructorsTest.printConstructors();
 assertNotNull(constructors);
 assertEquals(constructors.length, 1);
 assertEquals(constructors[0], "public ArrayList()");
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo interfacesTest = new ClassInfo("java.util.ArrayList");
 String[] interfaces = interfacesTest.printInterfaces();
 assertNotNull(interfaces);
 assertEquals(interfaces.length, 1);
 assertEquals(interfaces[0], "java.io.Serializable");
 }
}