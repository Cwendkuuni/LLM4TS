package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Objects;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object a = new Object();
 Object b = a;
 Truth truth = new Truth();

 truth.that(Objects.equal(a, b)).isTrue();
 truth.that(Objects.equal(null, null)).isTrue();
 truth.that(Objects.equal(null, new Object())).isFalse();
 truth.that(Objects.equal(new Object(), new Object())).isFalse();
 truth.that(Objects.equal(new Object(), null)).isFalse();
 truth.that(Objects.equal(1, 1)).isTrue();
 truth.that(Objects.equal(1, "1")).isFalse();
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[]{null, new Object(), new Object(), new Object()};
 int hashCode = Objects.hashCode(objects);
 Truth truth = new Truth();
 truth.that(hashCode).isNotZero();
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNullArray() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.add("name1", "value1");
 helper.add("name2", null);
 helper.add("name3", new Object());
 helper.omitNullValues();

 truth.that(helper.toString()).isEqualTo("Object{name1=value1, name3=<com.google.common.base.Objects@1b2d1a>}");
 }

 @Test
 public void testToStringHelperAddValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.addValue(1);

 truth.that(helper.toString()).isEqualTo("Object{1}");
 }

 @Test
 public void testToStringHelperAddValueBoolean() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.addValue(true);

 truth.that(helper.toString()).isEqualTo("Object{true}");
 }

 @Test
 public void testToStringHelperAddValueChar() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.addValue('c');

 truth.that(helper.toString()).isEqualTo("Object{'c'}");
 }

 @Test
 public void testToStringHelperAddValueDouble() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.addValue(1.0);

 truth.that(helper.toString()).isEqualTo("Object{1.0}");
 }

 @Test
 public void testToStringHelperAddValueFloat() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.addValue(1.0f);

 truth.that(helper.toString()).isEqualTo("Object{1.0}");
 }

 @Test
 public void testToStringHelperAddValueInt() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.addValue(1);

 truth.that(helper.toString()).isEqualTo("Object{1}");
 }

 @Test
 public void testToStringHelperAddValueLong() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.addValue(1L);

 truth.that(helper.toString()).isEqualTo("Object{1}");
 }

 @Test
 public void testToStringHelperAdd() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.add("name1", "value1");

 truth.that(helper.toString()).isEqualTo("Object{name1=value1}");
 }

 @Test
 public void testToStringHelperAddBoolean() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.add("name1", true);

 truth.that(helper.toString()).isEqualTo("Object{name1=true}");
 }

 @Test
 public void testToStringHelperAddChar() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.add("name1", 'c');

 truth.that(helper.toString()).isEqualTo("Object{name1=c}");
 }

 @Test
 public void testToStringHelperAddDouble() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.add("name1", 1.0);

 truth.that(helper.toString()).isEqualTo("Object{name1=1.0}");
 }

 @Test
 public void testToStringHelperAddFloat() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.add("name1", 1.0f);

 truth.that(helper.toString()).isEqualTo("Object{name1=1.0}");
 }

 @Test
 public void testToStringHelperAddInt() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.add("name1", 1);

 truth.that(helper.toString()).isEqualTo("Object{name1=1}");
 }

 @Test
 public void testToStringHelperAddLong() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.add("name1", 1L);

 truth.that(helper.toString()).isEqualTo("Object{name1=1}");
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 Truth truth = new Truth();

 helper.add("name1", "value1");
 helper.add("name2", null);
 helper.omitNullValues();

 truth.that(helper.toString()).isEqualTo("Object{name1=value1}");
 }
}