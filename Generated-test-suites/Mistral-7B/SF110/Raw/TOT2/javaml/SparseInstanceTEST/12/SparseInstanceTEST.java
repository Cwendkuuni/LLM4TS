package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;

public class SparseInstanceTest {

@Test
public void testSetNoAttributes() {
SparseInstance instance = new SparseInstance();
int initialNoAttributes = instance.noAttributes();

instance.setNoAttributes(5);
int newNoAttributes = instance.noAttributes();

assertEquals(5, newNoAttributes);
}

@Test
public void testConstructorWithNoAttributes() {
SparseInstance instance = new SparseInstance(5);
int noAttributes = instance.noAttributes();

assertEquals(5, noAttributes);
}

@Test
public void testValueMethod() {
SparseInstance instance = new SparseInstance();
instance.put(Integer.valueOf(0), Double.valueOf(42.0));

assertEquals(42.0, instance.value(0));
}

@Test
public void testIsEmptyMethod() {
SparseInstance instance = new SparseInstance();

assertTrue(instance.isEmpty());

instance.put(Integer.valueOf(0), Double.valueOf(42.0));

assertFalse(instance.isEmpty());
}

@Test
public void testRemoveAttribute() {
SparseInstance instance = new SparseInstance();
instance.put(Integer.valueOf(0), Double.valueOf(42.0));
instance.put(Integer.valueOf(1), Double.valueOf(43.0));

instance.removeAttribute(0);

assertFalse(instance.containsKey(Integer.valueOf(0)));
assertEquals(1, instance.noAttributes());
}

// Add test cases for other methods, edge cases, and exception handling scenarios here

}