import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import com.jstevh.viewer.*;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 ClassInfo abstractClass = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(abstractClass.isAbstract());
 ClassInfo nonAbstractClass = new ClassInfo("java.lang.StringBuilder");
 assertFalse(nonAbstractClass.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 assertFalse(stringClass.isInterface());
 ClassInfo stringBufferClass = new ClassInfo("java.lang.StringBuffer");
 assertFalse(stringBufferClass.isInterface());
 ClassInfo listInterface = new ClassInfo("java.util.List");
 assertTrue(listInterface.isInterface());
 }

 @Test
 public void testGetClassName() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", stringClass.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 assertEquals("java.lang", stringClass.getClassPackage());
 ClassInfo listClass = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util", listClass.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 assertNull(stringClass.getSuperClassName());
 ClassInfo stringBufferClass = new ClassInfo("java.lang.StringBuffer");
 assertEquals("java.lang.Object", stringBufferClass.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("java.foo.BarFoo");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 new ClassInfo(null);
 }

 @Test
 public void testPrintFields() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 String[] fields = stringClass.printFields();
 assertNotNull(fields);
 assertEquals(0, fields.length);
 }

 @Test
 public void testPrintMethods() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 String[] methods = stringClass.printMethods();
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testPrintMethodsWithParam() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 String[] methods = stringClass.printMethods(1);
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 String[] constructors = stringClass.printConstructors();
 assertNotNull(constructors);
 assertFalse(constructors.length == 0);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo javaUtilArrayListClass = new ClassInfo("java.util.ArrayList");
 String[] interfaces = javaUtilArrayListClass.printInterfaces();
 assertNotNull(interfaces);
 assertFalse(interfaces.length == 0);
 }

}