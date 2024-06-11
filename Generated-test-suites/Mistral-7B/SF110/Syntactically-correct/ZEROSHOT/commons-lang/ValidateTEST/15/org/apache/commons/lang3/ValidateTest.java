package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

 @Test
 public void testIsTrue() {
 Validate.isTrue(true, "Expected true but was false");
 Validate.isTrue(false, "Expected false but was true", 1L);
 Validate.isTrue(false, "Expected false but was true", 2.0);
 Validate.isTrue(new Object[]{"expectedValue"}, "Expected true but was false");
 Validate.isTrue(false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithException() {
 Validate.isTrue(false, "Expected true but was false", 1L, "Unexpected message with value: 1");
 Validate.isTrue(false, "Expected true but was false", 2.0, "Unexpected message with value: 2.0");
 Validate.isTrue(new Object[]{"expectedValue"}, "Expected true but was false", new Object[]{"unexpectedValue"});
 Validate.isTrue(false, "Unexpected message");
 }

 @Test
 public void testNotNull() {
 Object obj = new Object();
 Validate.notNull(obj, "Expected object to not be null");

 Object nullObj = null;
 try {
 Validate.notNull(nullObj, "Expected object to not be null");
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyArray() {
 T[] emptyArray = new T[0];
 try {
 Validate.notEmpty(emptyArray, "Expected array to not be empty");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 T[] nonEmptyArray = new T[1];
 Validate.notEmpty(nonEmptyArray, "Expected array to not be empty");
 }

 @Test(expected = NullPointerException.class)
 public void testNoNullElementsArray() {
 T[] arrayWithNull = new T[]{null};
 try {
 Validate.noNullElements(arrayWithNull, "Expected array to not contain null elements");
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 T[] arrayWithoutNull = new T[]{new T()};
 Validate.noNullElements(arrayWithoutNull);
 }

 @Test
 public void testValidIndexArray() {
 T[] array = new T[3];
 Validate.validIndex(array, 0);
 Validate.validIndex(array, 1);
 Validate.validIndex(array, 2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInvalidIndexArray() {
 T[] array = new T[3];
 Validate.validIndex(array, 3);
 }

 @Test
 public void testNotEmptyCollection() {
 Collection<T> collection = new ArrayList<>();
 collection.add(new T());
 Validate.notEmpty(collection);
 }

 @Test(expected = NullPointerException.class)
 public void testCollectionWithNull() {
 Collection<T> collectionWithNull = new ArrayList<T>(){
 @Override
 public boolean add(T e) {
 if (e == null) {
 return false;
 }
 super.add(e);
 return true;
 }
 }.add(null);
 try {
 Validate.noNullElements(collectionWithNull);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidIndexCollection() {
 Collection<T> collection = new ArrayList<>();
 collection.add(new T());
 Validate.validIndex(collection, 1);
 try {
 Validate.validIndex(collection, 2);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testNotEmptyMap() {
 Map<String, T> map = new HashMap<>();
 map.put("key", new T());
 Validate.notEmpty(map);
 }

 @Test(expected = NullPointerException.class)
 public void testMapWithNullKey() {
 Map<String, T> mapWithNullKey = new HashMap<String, T>(){
 @Override
 public T put(String key, T value) {
 if (key == null) {
 return null;
 }
 super.put(key, value);
 return value;
 }
 }.put(null, new T());
 try {
 Validate.noNullElements(mapWithNullKey.entrySet().toArray(new Map.Entry[0]));
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidIndexMap() {
 Map<String, T> map = new HashMap<>();
 map.put("key1", new T());
 map.put("key2", new T());
 Validate.validIndex(map.entrySet().toArray(new Map.Entry[0]), 1);
 try {
 Validate.validIndex(map.entrySet().toArray(new Map.Entry[0]), 2);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testValidState() {
 Validate.validState(true);
 Validate.validState(false, "Expected false but was true");
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidState() {
 Validate.validState(false);
 }

 @Test
 public void testMatchesPattern() {
 String input = "abcd";
 String pattern = ".*d";
 Validate.matchesPattern(input, pattern);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMatchesPattern() {
 String input = "abcd";
 String pattern = ".*x";
 Validate.matchesPattern(input, pattern);
 }

 @Test
 public void testInclusiveBetween() {
 T start = (T) 1;
 T end = (T) 3;
 Validate.inclusiveBetween(start, end, (Comparable<T>) 2);
 Validate.inclusiveBetween(start, end, (Comparable<T>) 1, "Value %s is not in the specified inclusive range of %s to %s", 2, start, end);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutsideInclusiveBetween() {
 T start = (T) 1;
 T end = (T) 3;
 Validate.inclusiveBetween(start, end, (Comparable<T>) 0);
 Validate.inclusiveBetween(start, end, (Comparable<T>) 4, "Value %s is not in the specified inclusive range of %s to %s", 0, start, end);
 }

 @Test
 public void testExclusiveBetween() {
 T start = (T) 1;
 T end = (T) 3;
 Validate.exclusiveBetween(start, end, (Comparable<T>) 2);
 Validate.exclusiveBetween(start, end, (Comparable<T>) 2, "Value %s is not in the specified exclusive range of %s to %s", 2, start, end);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutsideExclusiveBetween() {
 T start = (T) 1;
 T end = (T) 3;
 Validate.exclusiveBetween(start, end, (Comparable<T>) 0);
 Validate.exclusiveBetween(start, end, (Comparable<T>) 3, "Value %s is not in the specified exclusive range of %s to %s", 0, start, end);
 }

 @Test
 public void testIsInstanceOf() {
 Class<T> type = T.class;
 Object obj = new T();
 Validate.isInstanceOf(type, obj);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotInstanceOf() {
 Class<T> type = T.class;
 Object obj = new Object();
 Validate.isInstanceOf(type, obj, "Expected object of type %s but was %s", T.class, obj.getClass());
 }

 @Test
 public void testIsAssignableFrom() {
 Class<T> superType = Object.class;
 Class<T> type = T.class;
 Validate.isAssignableFrom(superType, type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotAssignableFrom() {
 Class<T> superType = Object.class;
 Class<T> type = String.class;
 Validate.isAssignableFrom(superType, type, "Expected type %s to be assignable from %s but was not", type, superType);
 }
}