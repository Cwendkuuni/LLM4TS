import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;

public class IFXObjectTest {

 private IFXObject createTestObject() {
 return new IFXObject() {
 // override getClass() method to test equals() method properly
 @Override
 public Class<?> getClass() {
 return this.getClass();
 }
 };
 }

 @Test
 public void testEquals() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 IFXObject obj3 = createTestObject();

 assertTrue(obj1.equals(obj1));
 assertFalse(obj1.equals(obj2));
 assertFalse(obj1.equals(obj3));
 assertFalse(obj2.equals(obj3));

 obj2 = obj1;
 assertTrue(obj1.equals(obj2));
 assertTrue(obj2.equals(obj1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsNull() {
 IFXObject obj1 = createTestObject();
 assertFalse(obj1.equals(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsDifferentType() {
 IFXObject obj1 = createTestObject();
 String str = new String("test");
 assertFalse(obj1.equals(str));
 }

 @Test
 public void testToString() {
 IFXObject obj = createTestObject();
 String expected = "IFXObject = {" +
 "getClass = class org.sourceforge.ifx.basetypes.IFXObject = {" +
 "getName = IFXObject" +
 "}," +
 "getMethods = [Ljava.lang.reflect.Method;@22e05b" +
 "}";
 String actual = obj.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = SecurityException.class)
 @SuppressWarnings("unchecked")
 public void testToStringReflection() throws SecurityException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
 IFXObject obj = createTestObject();
 Method toStringMethod = IFXObject.class.getMethod("toString", (Class[])null);
 String actual = (String)toStringMethod.invoke(obj, (Object[])null);
 fail("toString method should throw SecurityException when invoked reflectively");
 }

 @Test
 public void testStringifyMethodMatchesGetMethods() {
 IFXObject obj = createTestObject();
 Method[] methods = obj.getClass().getMethods();
 String expected = "IFXObject = {";
 for (Method method : methods) {
 if (method.getName().startsWith("get") && !method.getName().equals("getClass")) {
 String methodName = method.getName();
 String memberName = methodName.substring(3);
 expected += "\n" + this.tab(1) + memberName + " = ";
 Class<?> returnType = method.getReturnType();
 if (returnType.isArray()) {
 int numObjs = Array.getLength(returnType.newInstance(new Object[0]));
 expected += "[";
 for (int i = 0; i < numObjs; ++i) {
 if (i > 0) {
 expected += ",\n";
 }
 expected += this.tab(2) + "[" + i + "]:";
 expected += this.tab(3) + "IFXObject";
 }
 expected += "]";
 } else if (returnType.isInstance(IBaseType.class)) {
 expected += "'" + memberName + "'";
 } else {
 expected += "IFXObject";
 }
 }
 }
 expected += "\n" + this.tab(1) + "}";
 String actual = obj.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringifyMethodDifferentObjectTypes() {
 IFXObject obj1 = createTestObject();
 String str = new String("test");
 String expected = "IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject = {getName = IFXObject}, getMethods = [Ljava.lang.reflect.Method;@22e05b}, getClass = class java.lang.String = {getName = String}}";
 String actual = obj1.stringify(str, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringifyNull() {
 IFXObject obj = createTestObject();
 try {
 obj.stringify(null, 0);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testStringifyArrayObject() {
 IFXObject[] arrayObj = new IFXObject[3];
 for (int i = 0; i < arrayObj.length; ++i) {
 arrayObj[i] = new IFXObject();
 }
 IFXObject obj = new IFXObject();
 String expected = "IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject = {getName = IFXObject}, getMethods = [Ljava.lang.reflect.Method;@3eb567}, getClass = [Lorg.sourceforge.ifx.basetypes.IFXObject;@2b7e9ab]}";
 String actual = obj.stringify(arrayObj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringifyArrayObjectDifferentLength() {
 IFXObject[] arrayObj1 = new IFXObject[3];
 IFXObject[] arrayObj2 = new IFXObject[4];
 IFXObject obj = new IFXObject();
 String expected = "IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject = {getName = IFXObject}, getMethods = [Ljava.lang.reflect.Method;@3eb567}, getClass = [Lorg.sourceforge.ifx.basetypes.IFXObject;@75b9f1b]}";
 String actual = obj.stringify(arrayObj2, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringifyArrayObjectDifferentTypes() {
 IFXObject[] arrayObj1 = new IFXObject[3];
 for (int i = 0; i < arrayObj1.length; ++i) {
 arrayObj1[i] = new IFXObject();
 }
 String[] arrayStr = new String[3];
 for (int i = 0; i < arrayStr.length; ++i) {
 arrayStr[i] = new String("test");
 }
 IFXObject obj = new IFXObject();
 String expected = "IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject = {getName = IFXObject}, getMethods = [Ljava.lang.reflect.Method;@3eb567}, getClass = [Ljava.lang.Object;@26b6e93]}";
 String actual = obj.stringify(arrayStr, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testStringifyArrayObjectInvalidIndex() {
 IFXObject[] arrayObj = new IFXObject[3];
 for (int i = 0; i < arrayObj.length; ++i) {
 arrayObj[i] = new IFXObject();
 }
 IFXObject obj = new IFXObject();
 try {
 obj.stringify(arrayObj, 3);
 fail();
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testDoCheckEqualsSameObjects() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 assertTrue(obj2.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsDifferentClasses() {
 IFXObject obj1 = createTestObject();
 String str = new String("test");
 assertFalse(obj1.doCheckEquals(obj1, str));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoCheckEqualsNull() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = null;
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsSameClassDifferentValues() {
 IFXObject obj1 = new IFXObject() {{
 setValue1(1);
 setValue2("test");
 }};
 IFXObject obj2 = new IFXObject() {{
 setValue1(-1);
 setValue2("test2");
 }};
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsSameClassSameValues() {
 IFXObject obj1 = new IFXObject() {{
 setValue1(1);
 setValue2("test");
 }};
 IFXObject obj2 = new IFXObject() {{
 setValue1(1);
 setValue2("test");
 }};
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 assertTrue(obj2.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsArrayObjects() {
 IFXObject[] arrayObj1 = new IFXObject[3];
 for (int i = 0; i < arrayObj1.length; ++i) {
 arrayObj1[i] = new IFXObject();
 }
 IFXObject[] arrayObj2 = new IFXObject[3];
 for (int i = 0; i < arrayObj2.length; ++i) {
 arrayObj2[i] = new IFXObject();
 }
 Object[] objArray1 = arrayObj1.clone();
 Object[] objArray2 = arrayObj2.clone();
 for (int i = 0; i < objArray1.length; ++i) {
 IFXObject obj1 = (IFXObject)objArray1[i];
 IFXObject obj2 = (IFXObject)objArray2[i];
 obj1.setValue1(i + 1);
 obj2.setValue1(i + 2);
 }
 IFXObject obj = new IFXObject();
 assertTrue(obj.doCheckEquals(arrayObj1, arrayObj2));
 assertTrue(obj.doCheckEquals(arrayObj2, arrayObj1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoCheckEqualsNullArray() {
 IFXObject obj = new IFXObject();
 IFXObject[] arrayObj = null;
 assertFalse(obj.doCheckEquals(arrayObj, arrayObj));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoCheckEqualsDifferentArrayLengths() {
 IFXObject obj = new IFXObject();
 IFXObject[] arrayObj1 = new IFXObject[3];
 IFXObject[] arrayObj2 = new IFXObject[2];
 assertFalse(obj.doCheckEquals(arrayObj1, arrayObj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoCheckEqualsDifferentArrayTypes() {
 IFXObject obj = new IFXObject();
 IFXObject[] arrayObj1 = new IFXObject[3];
 String[] arrayStr = new String[3];
 assertFalse(obj.doCheckEquals(arrayObj1, arrayStr));
 }

 private String tab(final int level) {
 StringBuffer buf = new StringBuffer();
 for (int i = 0; i < level; ++i) {
 buf.append("  ");
 }
 return buf.toString();
 }
}