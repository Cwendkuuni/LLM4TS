package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

 @Test
 public void testSimpleHashCode() {
 HashCodeBuilder hcb = new HashCodeBuilder();
 hcb.append(1);
 hcb.append(true);
 hcb.append("test");
 int hashCode = hcb.toHashCode();
 assertEquals(2135101515, hashCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOddInitialValue() {
 HashCodeBuilder hcb = new HashCodeBuilder(4);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOddMultiplierValue() {
 HashCodeBuilder hcb = new HashCodeBuilder(17, 2);
 }

 @Test
 public void testArrayHashCode() {
 byte[] array = {1, 2, 3};
 HashCodeBuilder hcb = new HashCodeBuilder();
 hcb.append(array);
 int hashCode = hcb.toHashCode();
 assertEquals(1382565564, hashCode);
 }

 @Test
 public void testNestedObjectsHashCode() {
 class Nested {
 int a;
 int b;
 }
 Nested nested = new Nested();
 nested.a = 1;
 nested.b = 2;
 HashCodeBuilder hcb = new HashCodeBuilder();
 hcb.append(nested);
 int hashCode = hcb.toHashCode();
 assertEquals(1392035661, hashCode);
 }

 @Test
 public void testHashCodeWithTransients() {
 class Nested {
 int a;
 transient int b;
 }
 Nested nested = new Nested();
 nested.a = 1;
 HashCodeBuilder hcb = new HashCodeBuilder();
 reflectionHashCode(hcb, nested, HashCodeBuilder.class, true);
 int hashCode = hcb.toHashCode();
 assertEquals(1977514783, hashCode);
 }

 @Test
 public void testHashCodeWithExcludedFields() {
 class Nested {
 int a;
 int b;
 int c;
 }
 Nested nested = new Nested();
 nested.a = 1;
 nested.b = 2;
 HashCodeBuilder hcb = new HashCodeBuilder();
 reflectionHashCode(hcb, nested, HashCodeBuilder.class, false, Nested.class, new String[]{"b"});
 int hashCode = hcb.toHashCode();
 assertEquals(1392035661, hashCode); // without 'b' field
 }

 @Test
 public void testHashCodeWithStaticAndTransientFields() {
 class Nested {
 int a;
 transient int b;
 static int c;
 }
 Nested nested = new Nested();
 nested.a = 1;
 HashCodeBuilder hcb = new HashCodeBuilder();
 reflectionHashCode(hcb, nested, HashCodeBuilder.class, false, Nested.class, new String[0]);
 int hashCode = hcb.toHashCode();
 assertEquals(418535561, hashCode); // without 'b' and 'c' fields
 }

 @Test
 public void testHashCodeWithSuperClass() {
 class Child extends Parent {
 int a;
 }
 Child child = new Child();
 child.a = 1;
 HashCodeBuilder hcb = new HashCodeBuilder();
 hcb.appendSuper(child.getClass().getSuperclass().getHashCode());
 hcb.append(child.a);
 int hashCode = hcb.toHashCode();
 assertEquals(117572152, hashCode);
 }

 @Test
 public void testRegistry() {
 HashCodeBuilder hcb = new HashCodeBuilder();
 Object object1 = new Object();
 Object object2 = new Object();
 reflectionHashCode(hcb, object1, HashCodeBuilder.class, false, Object.class, new String[0]);
 reflectionHashCode(hcb, object2, HashCodeBuilder.class, false, Object.class, new String[0]);
 Set<Object> registry = HashCodeBuilder.getRegistry();
 assertEquals(2, registry.size());
 assertEquals(object1, registry.toArray()[0]);
 assertEquals(object2, registry.toArray()[1]);
 }

 @Test
 public void testNullParameter() {
 HashCodeBuilder hcb = new HashCodeBuilder();
 hcb.append((Object)null);
 int hashCode = hcb.toHashCode();
 assertEquals(-2108960475, hashCode);
 }
}