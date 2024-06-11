package com.google.common.cache;

import com.google.common.annotations.*;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import java.util.concurrent.*;
import java.util.*;
import com.google.common.collect.*;
import com.google.common.base.*;
import javax.annotation.*;

@Beta
public final class CacheBuilderSpecTest {

 private static final String BASIC_SPEC = "initialCapacity=10, maximumSize=100, maximumWeight=1000, concurrencyLevel=5, weakKeys(), weakValues(), recordStats=true, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h";
 private static final String EMPTY_SPEC = "";
 private static final String INVALID_SPEC = "invalidKey=invalidValue,invalidKey=invalidValue";

 @Test
 public void testParseValidSpec() {
 CacheBuilderSpec actual = CacheBuilderSpec.parse(BASIC_SPEC);
 CacheBuilderSpec expected = new CacheBuilderSpec(BASIC_SPEC);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptySpec() {
 CacheBuilderSpec.parse(EMPTY_SPEC);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpec() {
 CacheBuilderSpec.parse(INVALID_SPEC);
 }

 @Test
 public void testToCacheBuilder_InitialCapacity() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10");
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().initialCapacity(10);
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_MaximumSize() {
 CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().maximumSize(100);
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_MaximumWeight() {
 CacheBuilderSpec spec = new CacheBuilderSpec("maximumWeight=1000");
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().maximumWeight(1000);
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_ConcurrencyLevel() {
 CacheBuilderSpec spec = new CacheBuilderSpec("concurrencyLevel=5");
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().concurrencyLevel(5);
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_WeakKeys() {
 CacheBuilderSpec spec = new CacheBuilderSpec("weakKeys()");
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().weakKeys();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_WeakValues() {
 CacheBuilderSpec spec = new CacheBuilderSpec("weakValues()");
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().weakValues();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_RecordStats() {
 CacheBuilderSpec spec = new CacheBuilderSpec("recordStats=true");
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().recordStats();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_ExpireAfterAccess() {
 CacheBuilderSpec spec = new CacheBuilderSpec("expireAfterAccess=1h");
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS);
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_ExpireAfterWrite() {
 CacheBuilderSpec spec = new CacheBuilderSpec("expireAfterWrite=2d");
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.DAYS);
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_RefreshAfterWrite() {
 CacheBuilderSpec spec = new CacheBuilderSpec("refreshAfterWrite=3h");
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().refreshAfterWrite(3, TimeUnit.HOURS);
 assertEquals(expected, actual);
 }
}