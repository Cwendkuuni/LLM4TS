package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.ParameterizedRunner;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(ParameterizedRunner.class)
public class HashCodeBuilderTest {

 private HashCodeBuilder builder;
 private Object object;
 private int expectedHashCode;

 @Parameters(name = "{index}: hashCode({0}) should be {1}")
 public static Collection<Object[]> testData() {
 return Collections.nCopies(10, new Object[] { new Integer(1), 1841963353,
 new Integer(2), -2012866255, new String("test"), 123456789,
 new double[0], 37, new Object[]{1, 2, 3}, 2539085785, new Object(){}, -1422052153,
 new Object[]{null}, 0, null, 0 });
 }

 @Before
 public void setUp() {
 object = getTestObject();
 builder = new HashCodeBuilder();
 }

 @Test
 public void testReflectionHashCode() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(object, false);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCodeWithTestTransients() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(object, true);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCodeWithSuperClass() {
 int superHashCode = 123456;
 int actualHashCode = HashCodeBuilder.reflectionHashCode(object, 17, 37, object.getClass(), false, null, new String[0], superHashCode);
 assertEquals(expectedHashCode + superHashCode, actualHashCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullObject() {
 HashCodeBuilder.reflectionHashCode(null, 17, 37, Object.class, false, null, new String[0]);
 }

 private Object getTestObject() {
 // Create an instance of a test class with accessible transient and static fields
 try {
 Class<?> testClass = Class.forName("test.TestClass");
 Object testObject = testClass.newInstance();
 Field transientField = testClass.getDeclaredField("transientField");
 transientField.setAccessible(true);
 transientField.set(testObject, "transientValue");
 Field staticField = testClass.getDeclaredField("staticField");
 staticField.setAccessible(true);
 staticField.set(null, "staticValue");
 return testObject;
 } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

}

class TestClass {
 private transient int transientField;
 private static int staticField;
}