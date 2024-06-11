package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import org.junit.Assert.*;

public class IFXObjectTest {

@Test
public void testEquals() {
IFXObject obj1 = new IFXObject();
IFXObject obj2 = new IFXObject();
assertTrue(obj1.equals(obj1)); // checks reflexivity
assertTrue(obj1.equals(obj2) ^ obj2.equals(obj1)); // checks symmetry
assertFalse(obj1.equals(null)); // checks null case
assertFalse(obj1.equals(new Object())); // checks non-IFXObject case
}

@Test
public void testToString() {
IFXObject obj1 = new IFXObject();
IFXObject obj2 = new IFXObject();
String toStringObj1 = obj1.toString();
String toStringObj2 = obj2.toString();
assertTrue(toStringObj1.contains("IFXObject"));
assertTrue(toStringObj1.contains("={"));
assertTrue(toStringObj1.contains("}"));
assertFalse(toStringObj1.equals(toStringObj2)); // checks two objects have different hash codes
}

@Test(expected = InvocationTargetException.class)
public void testStringify_NullObject() {
IFXObject obj = null;
try {
String result = obj.stringify(obj, 0);
fail("Expected InvocationTargetException");
} catch (InvocationTargetException e) {
throw e.getCause();
}
}

@Test(expected = UndeclaredThrowableException.class)
public void testStringify_ExceptionThrown() {
IFXObject obj = new IFXObject() {
@Override
public String toString() {
throw new RuntimeException("Exception thrown");
}
};
try {
String result = obj.stringify(obj, 0);
fail("Expected UndeclaredThrowableException");
} catch (UndeclaredThrowableException e) {
throw e.getException();
}
}

@Test
public void testDoCheckEquals_SameObject() {
IFXObject obj1 = new IFXObject();
IFXObject obj2 = obj1;
assertTrue(obj1.doCheckEquals(obj1, obj2));
}

@Test
public void testDoCheckEquals_DifferentClasses() {
IFXObject obj1 = new IFXObject();
Object obj2 = new Object();
assertFalse(obj1.doCheckEquals(obj1, obj2));
}

@Test
public void testDoCheckEquals_ArrayObjects() {
IFXObject obj1 = new IFXObject();
IFXObject obj2 = new IFXObject();
IFXObject[] arrayObj1 = {obj1};
IFXObject[] arrayObj2 = {obj2};
IFXObject[] arrayObj3 = {obj1, obj2};
IFXObject[] arrayObj4 = {obj2, obj1};
IFXObject[] arrayObj5 = {obj1, new Object()};
IFXObject[] arrayObj6 = {new Object(), obj1};

assertTrue(obj1.doCheckEquals(obj1, obj1));
assertTrue(obj1.doCheckEquals(obj1, obj2) ^ obj2.doCheckEquals(obj1, obj1));
assertFalse(obj1.doCheckEquals(obj1, null));
assertFalse(obj1.doCheckEquals(obj1, new Object()));
assertFalse(obj1.doCheckEquals(obj1, arrayObj1));
assertFalse(obj1.doCheckEquals(obj1, arrayObj2));
assertTrue(obj1.doCheckEquals(obj1, arrayObj3));
assertTrue(obj1.doCheckEquals(obj1, arrayObj4));
assertFalse(obj1.doCheckEquals(obj1, arrayObj5));
assertFalse(obj1.doCheckEquals(obj1, arrayObj6));
}
}