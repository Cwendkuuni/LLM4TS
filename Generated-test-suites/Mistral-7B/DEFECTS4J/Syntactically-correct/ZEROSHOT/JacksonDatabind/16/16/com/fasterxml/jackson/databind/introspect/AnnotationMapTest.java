package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Object.class));
        assertFalse(annotationMap.size() > 0);
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation1());
        assertEquals(MyAnnotation1.class, annotationMap.get(MyAnnotation1.class));
        assertNull(annotationMap.get(MyAnnotation2.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation1());
        annotationMap.add(new MyAnnotation2());

        List<Annotation> annotations = Arrays.asList(new MyAnnotation1(), new MyAnnotation2());
        assertEquals(annotations, annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap.add(new MyAnnotation1());

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(new MyAnnotation2());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        List<Annotation> expectedAnnotations = Arrays.asList(new MyAnnotation1(), new MyAnnotation2());
        assertEquals(expectedAnnotations, mergedAnnotationMap.annotations());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        annotationMap.add(new MyAnnotation1());
        annotationMap.add(new MyAnnotation2());
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();

        MyAnnotation1 annotation1 = new MyAnnotation1();
        MyAnnotation2 annotation2 = new MyAnnotation2();

        assertTrue(annotationMap.addIfNotPresent(annotation1));
        assertFalse(annotationMap.addIfNotPresent(annotation1));
        assertTrue(annotationMap.addIfNotPresent(annotation2));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();

        MyAnnotation1 annotation1 = new MyAnnotation1();
        MyAnnotation2 annotation2 = new MyAnnotation2();

        assertTrue(annotationMap.add(annotation1));
        assertFalse(annotationMap.add(annotation1));
        assertTrue(annotationMap.add(annotation2));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation1());
        annotationMap.add(new MyAnnotation2());

        String expectedToString = "[Lcom.fasterxml.jackson.databind.util.AnnotationMap$AnnotationEntry;@58e0d1d6]";
        assertEquals(expectedToString, annotationMap.toString());
    }

    @Test
    public void testEmptyList() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.annotations().isEmpty());
    }

    @Test
    public void testNullAnnotationMap() {
        assertNull(new AnnotationMap(null));
    }

    @Test
    public void testEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.annotations().isEmpty());
    }

    @Test
    public void testNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
        assertNull(annotationMap.get(Object.class));
    }

    private static class MyAnnotation1 implements Annotation {
    }

    private static class MyAnnotation2 implements Annotation {
    }
}