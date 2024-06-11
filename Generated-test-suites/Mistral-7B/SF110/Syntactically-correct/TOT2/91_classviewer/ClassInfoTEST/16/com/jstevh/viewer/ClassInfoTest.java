import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

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
 ClassInfo stringBufferInterface = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(stringBufferInterface.isInterface());
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
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 assertNull(stringClass.getSuperClassName());
 ClassInfo stringBufferClass = new ClassInfo("java.lang.StringBuffer");
 assertEquals("java.lang.Object", stringBufferClass.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testConstructorWithInvalidClassName() throws ClassNotFoundException {
 new ClassInfo("java.not.a.Valid.Class");
 }

 @Test(expected = ClassNotFoundException.class)
 public void testConstructorWithNullName() throws ClassNotFoundException {
 new ClassInfo(null);
 }

 @Test
 public void testPrintFields() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 String[] expected = {"value=null"};
 assertArrayEquals(expected, stringClass.printFields());
 }

 @Test
 public void testPrintMethods() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 String[] expected = {"length()=length()", "charAt(int)=charAt(int)", "getClass()=getClass()", "equals(Object)=equals(Object)", "toString()=toString()"};
 assertArrayEquals(expected, stringClass.printMethods());
 }

 @Test
 public void testPrintMethodsWithParam() {
 ClassInfo stringClass = new ClassInfo("java.lang.String");
 String[] expected = {"substring(int, int)=substring(int, int)"};
 assertArrayEquals(expected, stringClass.printMethods(1));
 }
}