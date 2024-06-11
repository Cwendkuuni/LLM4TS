package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.*;

/**
 * Test class for AnnotationMap.
 */
public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap test = new AnnotationMap();
        assertNull(test.get(MyAnnotation.class));
        assertFalse(test.hasSize());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap test = new AnnotationMap();
        test.add(new MyAnnotation());
        MyAnnotation annotation = test.get(MyAnnotation.class);
        assertNotNull(annotation);
        assertEquals(MyAnnotation.class, annotation.annotationType());
    }

    @Test
    public void testGetNullAnnotation() {
        AnnotationMap test = new AnnotationMap();
        assertNull(test.get(MyAnnotation2.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap test = new AnnotationMap();
        test.add(new MyAnnotation());
        test.add(new MyAnnotation2());
        List<Annotation> annotations = new ArrayList<>(test.annotations());
        assertEquals(2, annotations.size());
        assertTrue(annotations.contains(new MyAnnotation()));
        assertTrue(annotations.contains(new MyAnnotation2()));
    }

    @Test
    public void testMergeEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);
    }

    @Test
    public void testMergeNonEmptyPrimary() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new MyAnnotation2());
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
        assertTrue(merged.containsKey(MyAnnotation.class));
        assertTrue(merged.containsKey(MyAnnotation2.class));
    }

    @Test
    public void testMergeNonEmptySecondary() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new MyAnnotation2());
        secondary.add(new MyAnnotation3());
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(3, merged.size());
        assertTrue(merged.containsKey(MyAnnotation.class));
        assertTrue(merged.containsKey(MyAnnotation2.class));
        assertTrue(merged.containsKey(MyAnnotation3.class));
    }

    @Test
    public void testSize() {
        AnnotationMap test = new AnnotationMap();
        test.add(new MyAnnotation());
        test.add(new MyAnnotation2());
        assertEquals(2, test.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap test = new AnnotationMap();
        test.add(new MyAnnotation());
        assertFalse(test.addIfNotPresent(new MyAnnotation()));
        assertTrue(test.addIfNotPresent(new MyAnnotation2()));
    }

    @Test
    public void testAdd() {
        AnnotationMap test = new AnnotationMap();
        test.add(new MyAnnotation());
        test.add(new MyAnnotation());
        assertEquals(2, test.size());
    }

    @Test
    public void testToString() {
        AnnotationMap test = new AnnotationMap();
        test.add(new MyAnnotation());
        test.add(new MyAnnotation2());
        assertEquals("{com.fasterxml.jackson.databind.introspect.AnnotationMap@78f67183}", test.toString());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface MyAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface MyAnnotation2 {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface MyAnnotation3 {
    }

}