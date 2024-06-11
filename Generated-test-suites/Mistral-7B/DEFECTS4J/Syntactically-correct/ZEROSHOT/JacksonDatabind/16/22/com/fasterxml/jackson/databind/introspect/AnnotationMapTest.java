package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.*;

/**
 * Unit tests for AnnotationMap class
 */
public class AnnotationMapTest {

    @Test
    public void testEmptyMap() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());
        assertNull(map.get(MyAnnotation1.class));
        assertNull(map.get(MyAnnotation2.class));
        assertNull(map.get(Object.class));
        assertNull(map.get(null));
        assertTrue(map.annotations().isEmpty());
    }

    @Test
    public void testAddAnnotations() {
        AnnotationMap map = new AnnotationMap();

        MyAnnotation1 annotation1 = new MyAnnotation1();
        MyAnnotation2 annotation2 = new MyAnnotation2();

        map.add(annotation1);
        map.add(annotation2);

        assertEquals(2, map.size());
        assertEquals(annotation1, map.get(MyAnnotation1.class));
        assertEquals(annotation2, map.get(MyAnnotation2.class));
        assertNull(map.get(Object.class));
        assertNull(map.get(null));

        Iterable<Annotation> annotations = map.annotations();
        List<Annotation> annotationList = new ArrayList<>();
        annotations.forEach(annotationList::add);

        assertEquals(2, annotationList.size());
        assertEquals(annotation1, annotationList.get(0));
        assertEquals(annotation2, annotationList.get(1));
    }

    @Test
    public void testMergeMaps() {
        AnnotationMap map1 = new AnnotationMap();
        AnnotationMap map2 = new AnnotationMap();

        MyAnnotation1 annotation1 = new MyAnnotation1();
        MyAnnotation2 annotation2 = new MyAnnotation2();
        MyAnnotation3 annotation3 = new MyAnnotation3();

        map1.add(annotation1);
        map2.add(annotation2);
        map2.add(annotation3);

        AnnotationMap mergedMap = AnnotationMap.merge(map1, map2);

        assertEquals(3, mergedMap.size());
        assertEquals(annotation1, mergedMap.get(MyAnnotation1.class));
        assertEquals(annotation2, mergedMap.get(MyAnnotation2.class));
        assertEquals(annotation3, mergedMap.get(MyAnnotation3.class));

        Iterable<Annotation> mergedAnnotations = mergedMap.annotations();
        List<Annotation> mergedAnnotationList = new ArrayList<>();
        mergedAnnotations.forEach(mergedAnnotationList::add);

        assertEquals(3, mergedAnnotationList.size());
        assertEquals(annotation1, mergedAnnotationList.get(0));
        assertEquals(annotation2, mergedAnnotationList.get(1));
        assertEquals(annotation3, mergedAnnotationList.get(2));
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();

        MyAnnotation1 annotation1 = new MyAnnotation1();

        assertFalse(map.addIfNotPresent(annotation1));
        assertEquals(0, map.size());

        map.add(annotation1);
        assertTrue(map.addIfNotPresent(annotation1));
        assertEquals(1, map.size());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void testNullClassArgument() {
        AnnotationMap map = new AnnotationMap();
        map.addIfNotPresent(null);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void testNullAnnotationArgument() {
        AnnotationMap map = new AnnotationMap();
        map.addIfNotPresent(null);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface MyAnnotation1 {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface MyAnnotation2 {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface MyAnnotation3 {
    }
}