import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class IFXObjectTest {

 private Object createTestObject() {
 Class<IFXObject> clazz = IFXObject.class;
 try {
 return clazz.newInstance();
 } catch (InstantiationException | IllegalAccessException e) {
 e.printStackTrace();
 }
 return null;
 }

 @Test
 public void testEquals() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 assertTrue(obj1.equals(obj1));
 assertFalse(obj1.equals(obj2));
 IFXObject obj3 = createTestObject();
 assertTrue(obj1.equals(obj3)); // same object
}

 @Test
 public void testToString_NormalCase() {
 IFXObject obj = createTestObject();
 String expected = obj.getClass().getName() + " = {...}";
 String actual = obj.toString();
 assertEquals(expected, actual);
}

 @Test(expected = IllegalArgumentException.class)
 public void testToString_NullObject() {
 IFXObject obj = null;
 String actual = obj.toString();
 fail("ToString should throw an IllegalArgumentException for null objects.");
}

 @Test
 public void testToString_NestedArrays() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 IFXObject obj3 = createTestObject();
 Object array1 = new Object[] { obj1, obj2 };
 Object array2 = new Object[] { obj3 };
 IFXObject obj = new IFXObject() {
 @Override
 public Object getClass() {
 return this.getClass();
 }

 @Override
 public Object invokeMethod(Method method, Object[] args) throws Exception {
 if (method.getName().equals("getClass")) {
 return this.getClass();
 }
 if (method.getName().equals("getString")) {
 return "TestString";
 }
 if (method.getName().equals("toString")) {
 return "{" + this.getClass().getName() + " = {";
 }
 if (method.getName().equals("getArray")) {
 return new Object[] { array1, array2 };
 }
 return null;
 }
 };
 String expected = obj.getClass().getName() + " = {getArray={[{...}, {...}]}";
 String actual = obj.toString();
 assertEquals(expected, actual);
}

 @Test(expected = NullPointerException.class)
 public void testToString_NullMembers() {
 IFXObject obj = createTestObject();
 obj.toString(); // should implicitly call getClass() and toString() which will throw a NullPointerException
}

 @Test
 public void testDoCheckEquals_SameObjects() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = obj1;
 assertTrue(obj1.doCheckEquals(obj1, obj2));
}

 @Test
 public void testDoCheckEquals_DifferentClasses() {
 IFXObject obj1 = createTestObject();
 Object obj2 = new Object();
 assertFalse(obj1.doCheckEquals(obj1, obj2));
}

 @Test
 public void testDoCheckEquals_DifferentClasses_DifferentMethods() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 Method method1 = ReflectionUtils.findMethod(obj1.getClass(), "getMethod1");
 Method method2 = ReflectionUtils.findMethod(obj2.getClass(), "getMethod2");
 assertFalse(obj1.doCheckEquals(obj1, obj2, new Object[] { method1, method2 }));
}

 @Test
 public void testDoCheckEquals_SameMethods_DifferentValues() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 Method method = ReflectionUtils.findMethod(obj1.getClass(), "getField1");
 Object field1Value1 = method.invoke(obj1, (Object[])null);
 Object field1Value2 = method.invoke(obj2, (Object[])null);
 assertFalse(obj1.doCheckEquals(obj1, obj2, new Object[] { method, field1Value1, field1Value2 }));
}

 @Test
 public void testDoCheckEquals_SameMethods_SameValues() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 Method method = ReflectionUtils.findMethod(obj1.getClass(), "getField1");
 Object field1Value1 = method.invoke(obj1, (Object[])null);
 Object field1Value2 = method.invoke(obj2, (Object[])null);
 assertTrue(obj1.doCheckEquals(obj1, obj2, new Object[] { method, field1Value1, field1Value2 }));
}

 private static Method findMethod(Class clazz, String methodName) {
 try {
 return clazz.getMethod(methodName);
 } catch (NoSuchMethodException e) {
 throw new RuntimeException("Could not find method '" + methodName + "' in class " + clazz.getName(), e);
 }
}

}