package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.*;

/**
 * Test class for AnnotationMap.
 *
 * @author John Doe
 */
public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetNullForMissingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        annotationMap.add(myAnnotation1);
        annotationMap.add(myAnnotation2);
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(2, annotations.size());
        assertTrue(annotations.contains(myAnnotation1));
        assertTrue(annotations.contains(myAnnotation2));
    }

    @Test
    public void testEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertTrue(annotations.isEmpty());
    }

    @Test
    public void testMerge() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap1.add(annotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap2.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(2, mergedAnnotations.size());
        assertTrue(mergedAnnotations.contains(annotation1));
        assertTrue(mergedAnnotations.contains(annotation2));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        annotationMap.add(new MyAnnotation());
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();

        assertFalse(annotationMap.addIfNotPresent(myAnnotation));
        assertEquals(0, annotationMap.size());

        annotationMap.clear();
        assertTrue(annotationMap.addIfNotPresent(myAnnotation));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();

        assertFalse(annotationMap.add(myAnnotation));
        assertEquals(0, annotationMap.size());

        annotationMap.clear();
        assertTrue(annotationMap.add(myAnnotation));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        assertEquals("[AnnotationMap(annotations=[com.fasterxml.jackson.databind.introspect.test.MyAnnotation@2d8f91f2])]", annotationMap.toString());
    }

    /**
     * Custom annotation for testing purposes.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
    @interface MyAnnotation {
    }
}