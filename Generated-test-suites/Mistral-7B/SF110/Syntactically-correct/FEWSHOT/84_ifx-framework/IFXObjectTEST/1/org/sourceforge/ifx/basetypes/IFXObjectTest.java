package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import org.junit.Test;

public class IFXObjectTest {

 IFXObject createIFXObjectWithValue(Class<? extends IFXObject> clazz, Object value) throws Exception {
 Method constructor = clazz.getDeclaredConstructor();
 constructor.setAccessible(true);
 return (IFXObject)constructor.newInstance(value);
 }

 @Test
 public void testEquals_NullObject() {
 IFXObject obj1 = new IFXObject(42);
 IFXObject obj2 = null;
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_DifferentTypes() {
 IFXObject obj1 = new IFXObject(42);
 String obj2 = "Hello";
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_DifferentClasses() {
 IFXObject obj1 = new IFXObject(42);
 IFXObject obj2 = new IFXObject(13);
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_SameInstance() {
 IFXObject obj1 = new IFXObject(42);
 assertEquals(obj1, obj1);
 }

 @Test
 public void testEquals_SameValue() throws Exception {
 IFXObject obj1 = new IFXObject(42);
 IFXObject obj2 = createIFXObjectWithValue(IFXObject.class, 42);
 assertEquals(obj1, obj2);
 }

 @Test
 public void testToString_EmptyObject() {
 IFXObject obj = new IFXObject(null);
 assertEquals("IFXObject = {}", obj.toString());
 }

 @Test
 public void testToString_ObjectWithNoGetMethods() {
 IFXObject obj = new IFXObject(42);
 assertEquals("IFXObject = {}", obj.toString());
 }

 @Test
 public void testToString_ObjectWithGetMethods() throws Exception {
 Class<IFXObject> clazz = IFXObject.class;
 IFXObject obj = createIFXObjectWithValue(clazz, new int[]{1, 2, 3, 4});
 String expected = "IFXObject = {\ngetNumElements = 4\n[" +
                  "0]:\nIFXObject = {int = 1}\n[" +
                  "1]:\nIFXObject = {int = 2}\n[" +
                  "2]:\nIFXObject = {int = 3}\n[" +
                  "3]:\nIFXObject = {int = 4}\n}";
 assertEquals(expected, obj.toString());
 }

 @Test
 public void testToString_NestedObjectWithGetMethods() throws Exception {
 Class<IFXObject> outerClazz = IFXObject.class;
 Class<Integer> innerClass = Integer.TYPE;
 IFXObject outerObj = createIFXObjectWithValue(outerClazz, new IFXObject[]{
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(outerClazz, new int[]{1, 2, 3, 4})
         }),
         createIFXObjectWithValue(outerClazz, new int[]{5, 6, 7, 8})
 });
 String expected = "IFXObject = {\ngetNumElements = 2\n[" +
                  "0]:\nIFXObject = {\ngetNumElements = 1\n[" +
                  "0]:\nIFXObject = {\ngetNumElements = 4\n[" +
                  "0]:\nIFXObject = {int = 1}\n[" +
                  "1]:\nIFXObject = {int = 2}\n[" +
                  "2]:\nIFXObject = {int = 3}\n[" +
                  "3]:\nIFXObject = {int = 4}\n}\n}";
 assertEquals(expected, outerObj.toString());
 }

 @Test
 public void testToString_NestedObjectWithArrays() throws Exception {
 Class<IFXObject> outerClazz = IFXObject.class;
 Class<Integer> innerClass = Integer.TYPE;
 IFXObject outerObj = createIFXObjectWithValue(outerClazz, new IFXObject[]{
         createIFXObjectWithValue(outerClazz, new int[]{1, 2, 3, 4}),
         createIFXObjectWithValue(outerClazz, new int[][]{
                 new int[]{5, 6},
                 new int[]{7, 8}
         })
 });
 String expected = "IFXObject = {\ngetNumElements = 2\n[" +
                  "0]:\nIFXObject = {\ngetNumElements = 4\n[" +
                  "0]:\nIFXObject = {int = 1}\n[" +
                  "1]:\nIFXObject = {int = 2}\n[" +
                  "2]:\nIFXObject = {int = 3}\n[" +
                  "3]:\nIFXObject = {int = 4}\n}\n[" +
                  "1]:\nIFXObject = {\ngetNumElements = 2\n[" +
                  "0]:\nIFXObject = {int = 5}\n[" +
                  "1]:\nIFXObject = {int = 6}\n}";
 assertEquals(expected, outerObj.toString());
 }

 @Test
 public void testToString_NestedObjectWithObjects() throws Exception {
 Class<IFXObject> outerClazz = IFXObject.class;
 Class<IFXObject> innerClazz = IFXObject.class;
 IFXObject outerObj = createIFXObjectWithValue(outerClazz, new IFXObject[]{
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, "Hello"),
                 createIFXObjectWithValue(innerClazz, "World")
         }),
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, "Apple"),
                 createIFXObjectWithValue(innerClazz, "Banana")
         })
 });
 String expected = "IFXObject = {\ngetNumElements = 2\n[" +
                  "0]:\nIFXObject = {\ngetNumElements = 2\n[" +
                  "0]:\nIFXObject = {String = Hello}\n[" +
                  "1]:\nIFXObject = {String = World}\n}\n}";
 assertEquals(expected, outerObj.toString());
 }

 @Test
 public void testToString_RecursiveObject() throws Exception {
 Class<IFXObject> clazz = IFXObject.class;
 IFXObject obj = createIFXObjectWithValue(clazz, obj);
 fail("toString should not be able to recursively print itself");
 }

 @Test
 public void testEquals_ObjectsWithSameValuesButDifferentClasses() throws Exception {
 Class<IFXObject> clazz1 = IFXObject.class;
 Class<Integer> clazz2 = Integer.TYPE;
 IFXObject obj1 = createIFXObjectWithValue(clazz1, new IFXObject[]{createIFXObjectWithValue(clazz2, 42)});
 IFXObject obj2 = createIFXObjectWithValue(clazz2, createIFXObjectWithValue(clazz1, new IFXObject[]{new IFXObject(42)}));
 assertTrue(obj1.equals(obj2));
 }

 @Test
 public void testEquals_ObjectsWithSameValuesButDifferentOrder() throws Exception {
 Class<IFXObject> clazz = IFXObject.class;
 IFXObject obj1 = createIFXObjectWithValue(clazz, new IFXObject[]{new IFXObject(42), new IFXObject(13)});
 IFXObject obj2 = createIFXObjectWithValue(clazz, new IFXObject[]{new IFXObject(13), new IFXObject(42)});
 assertTrue(obj1.equals(obj2));
 }

 @Test
 public void testEquals_ObjectsWithSameValuesButDifferentNestedValues() throws Exception {
 Class<IFXObject> outerClazz = IFXObject.class;
 Class<IFXObject> innerClazz = IFXObject.class;
 IFXObject obj1 = createIFXObjectWithValue(outerClazz, new IFXObject[]{
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, "Hello"),
                 createIFXObjectWithValue(innerClazz, "World")
         }),
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, "Apple"),
                 createIFXObjectWithValue(innerClazz, "Banana")
         })
 });
 IFXObject obj2 = createIFXObjectWithValue(outerClazz, new IFXObject[]{
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, "World"),
                 createIFXObjectWithValue(innerClazz, "Hello")
         }),
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, "Banana"),
                 createIFXObjectWithValue(innerClazz, "Apple")
         })
 });
 assertTrue(obj1.equals(obj2));
 }

 @Test
 public void testEquals_ObjectsWithDifferentValuesButSameType() throws Exception {
 Class<IFXObject> clazz = IFXObject.class;
 IFXObject obj1 = createIFXObjectWithValue(clazz, new int[]{1, 2, 3, 4});
 IFXObject obj2 = createIFXObjectWithValue(clazz, new int[]{5, 6, 7, 8});
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_ObjectsWithDifferentValuesButDifferentTypes() throws Exception {
 Class<IFXObject> clazz1 = IFXObject.class;
 Class<Integer> clazz2 = Integer.TYPE;
 IFXObject obj1 = createIFXObjectWithValue(clazz1, new IFXObject[]{createIFXObjectWithValue(clazz2, 42)});
 IFXObject obj2 = createIFXObjectWithValue(clazz1, new String[]{"Hello", "World"});
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_ObjectsWithNullValues() throws Exception {
 Class<IFXObject> clazz = IFXObject.class;
 IFXObject obj1 = createIFXObjectWithValue(clazz, new IFXObject[]{null, null});
 IFXObject obj2 = createIFXObjectWithValue(clazz, new IFXObject[]{null, new IFXObject(42)});
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_ObjectsWithNullValuesInNestedObjects() throws Exception {
 Class<IFXObject> outerClazz = IFXObject.class;
 Class<IFXObject> innerClazz = IFXObject.class;
 IFXObject obj1 = createIFXObjectWithValue(outerClazz, new IFXObject[]{
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, null),
                 createIFXObjectWithValue(innerClazz, "World")
         }),
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, "Hello"),
                 createIFXObjectWithValue(innerClazz, null)
         })
 });
 IFXObject obj2 = createIFXObjectWithValue(outerClazz, new IFXObject[]{
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, "World"),
                 createIFXObjectWithValue(innerClazz, null)
         }),
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, "Hello"),
                 createIFXObjectWithValue(innerClazz, "Apple")
         })
 });
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_ArraysWithSameValuesButDifferentOrders() throws Exception {
 Class<IFXObject> clazz = IFXObject.class;
 IFXObject obj1 = createIFXObjectWithValue(clazz, new IFXObject[]{new IFXObject(42), new IFXObject(13)});
 IFXObject obj2 = createIFXObjectWithValue(clazz, new IFXObject[]{new IFXObject(13), new IFXObject(42)});
 assertTrue(obj1.equals(obj2));
 }

 @Test
 public void testEquals_ArraysWithSameValuesButDifferentTypes() throws Exception {
 Class<IFXObject> clazz = IFXObject.class;
 IFXObject obj1 = createIFXObjectWithValue(clazz, new IFXObject[]{new Integer(42), new String("Hello")});
 IFXObject obj2 = createIFXObjectWithValue(clazz, new IFXObject[]{new String("Hello"), new Integer(42)});
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_ArraysWithNullValues() throws Exception {
 Class<IFXObject> clazz = IFXObject.class;
 IFXObject obj1 = createIFXObjectWithValue(clazz, new IFXObject[]{new IFXObject(42), null});
 IFXObject obj2 = createIFXObjectWithValue(clazz, new IFXObject[]{null, new IFXObject(42)});
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_ArraysWithNestedObjects() throws Exception {
 Class<IFXObject> outerClazz = IFXObject.class;
 Class<IFXObject> innerClazz = IFXObject.class;
 IFXObject obj1 = createIFXObjectWithValue(outerClazz, new IFXObject[]{
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, new IFXObject[]{new Integer(1), new Integer(2)}),
                 createIFXObjectWithValue(innerClazz, new Integer(3))
         }),
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, new Integer(4)),
                 createIFXObjectWithValue(innerClazz, new IFXObject[]{new Integer(5), new Integer(6)})
         })
 });
 IFXObject obj2 = createIFXObjectWithValue(outerClazz, new IFXObject[]{
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, new Integer(3)),
                 createIFXObjectWithValue(innerClazz, new IFXObject[]{new Integer(2), new Integer(1)})
         }),
         createIFXObjectWithValue(outerClazz, new IFXObject[]{
                 createIFXObjectWithValue(innerClazz, new Integer(6)),
                 createIFXObjectWithValue(innerClazz, new IFXObject[]{new Integer(5), new Integer(4)})
         })
 });
 assertTrue(obj1.equals(obj2));
 }

}