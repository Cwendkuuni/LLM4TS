package com.google.common.cache;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import com.google.common.annotations.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;
import static com.google.common.cache.CacheBuilderSpec.KEYS_SPLITTER;
import static com.google.common.cache.CacheBuilderSpec.KEY_VALUE_SPLITTER;
import static com.google.common.cache.CacheBuilderSpec.VALUE_PARSERS;

@Beta
@GuavaOptions(value = TestingMode.MANUAL)
public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setUp() {
 spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, expireAfterWrite=1h, expireAfterAccess=30s, refreshAfterWrite=1m");
 }

 @Test
 public void testParse() {
 assertEquals("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, expireAfterWrite=1h, expireAfterAccess=30s, refreshAfterWrite=1m", spec.toParsableString());
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder() //
 .initialCapacity(5) //
 .maximumSize(10) //
 .maximumWeight(20) //
 .concurrencyLevel(5) //
 .weakKeys() //
 .softValues() //
 .recordStats() //
 .expireAfterWrite(1, TimeUnit.HOURS) //
 .expireAfterAccess(30, TimeUnit.SECONDS) //
 .refreshAfterWrite(1, TimeUnit.MINUTES), cacheBuilder);
 }

 @Test
 public void testParseWithInvalidSpec() {
 try {
 CacheBuilderSpec.parse("invalidSpecification");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseWithMissingValue() {
 try {
 CacheBuilderSpec.parse("initialCapacity=");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseWithMissingKey() {
 try {
 CacheBuilderSpec.parse("=10");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseWithInvalidKey() {
 try {
 CacheBuilderSpec.parse("invalidKey=10");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseWithInvalidValueType() {
 try {
 CacheBuilderSpec.parse("invalidKey=invalidValue");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseWithInvalidDurationFormat() {
 try {
 CacheBuilderSpec.parse("expireAfterWrite=1d invalid");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseWithDuplicateKey() {
 try {
 CacheBuilderSpec.parse("initialCapacity=5, initialCapacity=10");
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

}