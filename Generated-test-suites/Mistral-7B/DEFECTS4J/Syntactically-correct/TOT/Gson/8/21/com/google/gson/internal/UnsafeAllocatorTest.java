package com.example.unsafeallocator;

import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

public class UnsafeAllocatorTest {

 private UnsafeAllocator allocator;

 @Before
 public void setUp() {
 allocator = UnsafeAllocator.create();
 }

 @Test
 public void testNewInstanceNull() {
 assertThrows(NullPointerException.class, () -> allocator.newInstance(null));
 }

 @Test
 public void testNewInstancePrimitive() {
 assertInstanceOf(Integer.class, allocator.newInstance(Integer.TYPE));
 }

 @Test
 public void testNewInstanceWrapper() {
 assertInstanceOf(Integer.class, allocator.newInstance(Integer.class));
 }

 @Test
 public void testNewInstanceString() {
 String str = allocator.newInstance(String.class);
 assertEquals("", str);
 }

 @Test
 public void testNewInstanceArray() {
 int[] arr = (int[]) allocator.newInstance(int[].class);
 assertNull(arr);
 }

 @Test
 public void testNewInstanceCollection() {
 Set<Integer> set = (Set<Integer>) allocator.newInstance(HashSet.class);
 assertInstanceOf(HashSet.class, set);
 assertInstanceOf(Set.class, set);
 assertInstanceOf(Collection.class, set);
 assertInstanceOf(Iterable.class, set);
 }

 @Test
 public void testNewInstanceList() {
 @SuppressWarnings("unchecked")
 ArrayList<Integer> list = (ArrayList<Integer>) allocator.newInstance(ArrayList.class);
 assertInstanceOf(ArrayList.class, list);
 assertInstanceOf(List.class, list);
 assertInstanceOf(Collection.class, list);
 assertInstanceOf(Iterable.class, list);
 }

 @Test
 public void testNewInstanceMap() {
 @SuppressWarnings("unchecked")
 HashMap<String, Integer> map = (HashMap<String, Integer>) allocator.newInstance(HashMap.class);
 assertInstanceOf(HashMap.class, map);
 assertInstanceOf(Map.class, map);
 assertInstanceOf(Collection.class, map.values());
 }

 @Test
 public void testNewInstanceInterface() {
 assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(Runnable.class));
 }

 @Test
 public void testNewInstanceAbstract() {
 assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(AbstractList.class));
 }

 @Test
 public void testNewInstanceException() {
 try {
 allocator.newInstance(Exception.class);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testNewInstanceCustomClass() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
 Class<CustomClass> customClass = CustomClass.class;
 Object instance = customClass.newInstance();
 Object unsafeInstance = allocator.newInstance(customClass);

 assertEquals(instance.getClass(), unsafeInstance.getClass());
 }
}

class CustomClass {
 // empty class
}