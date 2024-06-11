package com.example.cachebuilder; // replace with the actual package name

import static org.junit.Assert.*;
import com.google.common.cache.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

 private static final String VALID_SPEC = "initialCapacity=10, maximumSize=50, concurrencyLevel=10";
 private static final String INVALID_SPEC = "initialCapacity=10, maximumSize=hello, concurrencyLevel=10";

 @Test
 public void testParseValidSpec() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse(VALID_SPEC);
  assertEquals("Initial capacity: 10, Maximum size: 50, Concurrency level: 10", spec.toParsableString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpec() {
  try {
   CacheBuilderSpec.parse(INVALID_SPEC);
  } catch (IllegalArgumentException e) {
   assertEquals("key maximumSize value set to hello, must be integer", e.getMessage());
  }
 }

 @Test
 public void testToCacheBuilder_ValidSpec() {
  CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=10");
  CacheBuilder<Integer, String> cacheBuilder = spec.toCacheBuilder();
  assertEquals(10, cacheBuilder.initialCapacity());
  assertEquals(50, cacheBuilder.maximumSize());
  assertEquals(10, cacheBuilder.concurrencyLevel());
 }

 @Test
 public void testToCacheBuilder_EmptySpec() {
  CacheBuilderSpec spec = new CacheBuilderSpec("");
  assertNull(spec.toCacheBuilder());
 }

 @Test
 public void testToCacheBuilder_NullSpec() {
  CacheBuilderSpec spec = null;
  assertNull(spec.toCacheBuilder());
 }

 @Test
 public void testToString() {
  CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=10");
  assertEquals("CacheBuilderSpec{specification='initialCapacity=10, maximumSize=50, concurrencyLevel=10'}", spec.toString());
 }

 @Test
 public void testHashCode_SameSpec() {
  CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=10");
  CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=10");
  assertEquals(spec1.hashCode(), spec2.hashCode());
 }

 @Test
 public void testHashCode_DifferentSpecs() {
  CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=10");
  CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=11");
  assertNotEquals(spec1.hashCode(), spec2.hashCode());
 }

 @Test
 public void testEquals_SameSpec() {
  CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=10");
  CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=10");
  assertTrue(spec1.equals(spec2));
 }

 @Test
 public void testEquals_DifferentSpecs() {
  CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=10");
  CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=11");
  assertFalse(spec1.equals(spec2));
 }

 @Test
 public void testEquals_NullObject() {
  CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=10");
  assertFalse(spec.equals(null));
 }

 @Test
 public void testEquals_DifferentType() {
  CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, concurrencyLevel=10");
  assertFalse(spec.equals(new Object()));
 }

}