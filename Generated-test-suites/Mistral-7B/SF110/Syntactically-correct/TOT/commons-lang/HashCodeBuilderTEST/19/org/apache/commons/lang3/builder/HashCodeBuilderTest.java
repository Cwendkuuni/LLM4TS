package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class HashCodeBuilderTest {

 private static HashCodeBuilder hashCodeBuilder;
 private static Object testObject;
 private int expectedHashCode;
 private String testClassName;

 @BeforeClass
 public static void setup() {
 HashCodeBuilder.register(HashCodeBuilderTest.class);
 testObject = new TestObject("Test Object");
 }

 @Parameters(name = "{index}: {0} with excluded fields {1}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Test cases with no excluded fields
 new Object[]{"TestObject1", null},
 // Test cases with excluded fields
 new Object[]{"TestObject2", new String[]{"field1", "field2"}}
 });
 }

 public HashCodeBuilderTest(String testClassName, Object... excludedFields) {
 this.testClassName = testClassName;
 if (excludedFields != null && excludedFields.length > 0) {
 this.expectedHashCode = HashCodeBuilder.reflectionHashCode(testObject, Arrays.asList(ReflectionToStringBuilder.class.getDeclaredField("excludedFields"), ReflectionToStringBuilder.class.getDeclaredField("hashCodeValue")), ReflectionToStringBuilder.class, true, HashCodeBuilder.class, new String[]{"excludedFields"});
 } else {
 this.expectedHashCode = HashCodeBuilder.reflectionHashCode(testObject, false, null, null);
 }
 hashCodeBuilder = new HashCodeBuilder();
 }

 @Test
 public void testHashCodeBuilder() {
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals("Hash code does not match expected value for " + testClassName, expectedHashCode, actualHashCode);
 }

 static class TestObject {
 private String name;
 private int id;

 public TestObject(String name) {
 this.name = name;
 this.id = 42;
 }

 @Override
 public String toString() {
 return ReflectionToStringBuilder.toString(this);
 }
 }

}