import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import com.jstevh.viewer.ClassInfo;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstractMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 assertTrue(testClass.isAbstract());
 testClass = new ClassInfo("java.util.ArrayList");
 assertFalse(testClass.isAbstract());
 }

 @Test
 public void testIsInterfaceMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 assertFalse(testClass.isInterface());
 testClass = new ClassInfo("java.util.Collections");
 assertTrue(testClass.isInterface());
 }

 @Test
 public void testGetClassNameMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.ArrayList", testClass.getClassName());
 }

 @Test
 public void testGetClassPackageMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 assertNull(testClass.getClassPackage());
 }

 @Test
 public void testGetSuperClassNameMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 assertNull(testClass.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("nonExistentClass");
 }

 @Test
 public void testPrintFieldsMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 String[] expected = new String[] {
 "final static int capacity = 10",
 "final static int defaultCapacity = 16",
 "private static final int MAX_ARRAY_SIZE = 2147483647",
 "transient int size;",
 "transient Object[] elementData;",
 "private static final long serialVersionUID = 8683452581122892189L;"
 };
 String[] fields = testClass.printFields();
 assertArrayEquals(expected, fields);
 }

 @Test
 public void testPrintMethodsMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 String[] expected = new String[] {
 "public int size()",
 "public Object get(int index)",
 "public Object set(int index, Object element)",
 "public void add(int index, Object element)",
 "public void add(Object element)",
 "public void clear()",
 "public void trimToSize()",
 "public boolean contains(Object o)",
 "public boolean remove(Object o)",
 "public boolean removeRange(int fromIndex, int toIndex)",
 "public boolean addAll(Collection<? extends Object> c)",
 "public boolean addAll(int index, Collection<? extends Object> c)",
 "public void removeRange(int fromIndex, int toIndex)",
 "public int indexOf(Object o)",
 "public int lastIndexOf(Object o)",
 "public ListIterator<Object> listIterator()",
 "public ListIterator<Object> listIterator(int index)",
 "public Iterator<Object> iterator()",
 "public Object[] toArray()",
 "public <T> T[] toArray(T[] a)",
 "public boolean equals(Object o)",
 "public int hashCode()",
 "public String toString()",
 "public Class<?> getClass()",
 "public native int size()",
 "public native Object get(int index)",
 "public native Object set(int index, Object element)",
 "public native void add(int index, Object element)",
 "public native void add(Object element)",
 "public native void clear()",
 "public native void trimToSize()",
 "public native boolean contains(Object o)",
 "public native boolean remove(Object o)",
 "public native boolean removeRange(int fromIndex, int toIndex)",
 "public native boolean addAll(Collection<? extends Object> c)",
 "public native boolean addAll(int index, Collection<? extends Object> c)",
 "public native void removeRange(int fromIndex, int toIndex)",
 "public native int indexOf(Object o)",
 "public native int lastIndexOf(Object o)",
 "public native ListIterator<Object> listIterator()",
 "public native ListIterator<Object> listIterator(int index)",
 "public native Iterator<Object> iterator()",
 "public native Object[] toArray()",
 "public native <T> T[] toArray(T[] a)",
 "public native boolean equals(Object o)",
 "public native int hashCode()",
 "public native String toString()",
 "public native Class<?> getClass()",
 "public native boolean isEmpty()",
 "public native Object get(int index) throws IndexOutOfBoundsException",
 "public native Object set(int index, Object element) throws IndexOutOfBoundsException",
 "public native void add(int index, Object element) throws IndexOutOfBoundsException",
 "public native void remove(int index) throws IndexOutOfBoundsException",
 "public native void removeRange(int fromIndex, int toIndex) throws IndexOutOfBoundsException",
 "public native void addAll(int index, Collection<? extends Object> c) throws IndexOutOfBoundsException",
 "public native void addAll(Collection<? extends Object> c) throws CapacityExceededException"
 };
 String[] methods = testClass.printMethods();
 assertArrayEquals(expected, methods);
 }

 @Test
 public void testPrintMethodsParamMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 String[] expected = new String[] {
 "public int size()",
 "public Object get(int index)",
 "public Object set(int index, Object element)",
 "public void add(int index, Object element)",
 "public void add(Object element)",
 "public void clear()",
 "public void trimToSize()",
 "public boolean contains(Object o)",
 "public boolean remove(Object o)",
 "public boolean removeRange(int fromIndex, int toIndex)",
 "public boolean addAll(Collection<? extends Object> c)",
 "public boolean addAll(int index, Collection<? extends Object> c)",
 "public void removeRange(int fromIndex, int toIndex)",
 "public int indexOf(Object o)",
 "public int lastIndexOf(Object o)"
 };
 String[] methods = testClass.printMethods(1);
 assertArrayEquals(expected, methods);
 }

 @Test
 public void testPrintConstructorsMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 String[] expected = new String[] {
 "public ArrayList()",
 "public ArrayList(int initialCapacity)",
 "public ArrayList(Collection<? extends Object> c)"
 };
 String[] constructors = testClass.printConstructors();
 assertArrayEquals(expected, constructors);
 }

 @Test
 public void testPrintInterfacesMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 String[] expected = new String[] {
 "java.util.Collection",
 "java.util.List",
 "java.io.Serializable"
 };
 String[] interfaces = testClass.printInterfaces();
 assertArrayEquals(expected, interfaces);
 }

 @Test
 public void testSrchMethodsMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 String[] expected = new String[] {
 "public void add(int index, Object element)",
 "public void add(Object element)",
 "public boolean remove(Object o)",
 "public boolean removeRange(int fromIndex, int toIndex)",
 "public boolean addAll(Collection<? extends Object> c)"
 };
 String[] methods = testClass.srchMethods("add");
 assertArrayEquals(expected, methods);
 }

 @Test
 public void testSrchMethodsMethodWithIndexes() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 String[] expected = new String[] {
 "public void add(int index, Object element)",
 "public void add(int index, Object element)",
 "public void add(int index, Object element)",
 "public void add(int index, Object element)",
 "public void add(int index, Object element)"
 };
 String[] methods = testClass.srchMethods("add", new String[] { "int" });
 assertArrayEquals(expected, methods);
 }

 @Test
 public void testGetFoundMethodMethod() {
 ClassInfo testClass = new ClassInfo("java.util.ArrayList");
 String[] expectedMethods = new String[] {
 "public int size()",
 "public Object get(int index)",
 "public Object set(int index, Object element)",
 "public void add(int index, Object element)",
 "public void add(Object element)",
 "public void clear()",
 "public void trimToSize()",
 "public boolean contains(Object o)",
 "public boolean remove(Object o)",
 "public boolean removeRange(int fromIndex, int toIndex)",
 "public boolean addAll(Collection<? extends Object> c)",
 "public boolean addAll(int index, Collection<? extends Object> c)",
 "public void removeRange(int fromIndex, int toIndex)",
 "public int indexOf(Object o)",
 "public int lastIndexOf(Object o)"
 };
 Method expectedMethod = null;
 for (Method method : testClass.c.getMethods()) {
 if (method.getName().equals("size")) {
 expectedMethod = method;
 break;
 }
 }
 int position = Arrays.asList(expectedMethods).indexOf(expectedMethod.toGenericString());
 MethodData methodData = testClass.getFoundMethod(position);
 String[] expectedFields = new String[] {
 "className=java.util.ArrayList",
 "methClass=class java.util.ArrayList",
 "methName=size",
 "getMethPackage()=java.util",
 "getMethClass()=java.util.ArrayList",
 "getMethName()=size"
 };
 assertArrayEquals(expectedFields, methodData.getInfo());
 }

}