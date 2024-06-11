package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

    @Test
    public void testCreateInstance() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
        assertFalse(annotationMap.annotations().hasNext());
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetAnnotationNotFound() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap.add(annotation1);

        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation2);

        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(2, annotations.size());
        assertEquals(annotation1, annotations.get(0));
        assertEquals(annotation2, annotations.get(1));
    }

    @Test
    public void testEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Iterator<Annotation> iterator = annotationMap.annotations().iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap.add(annotation1);

        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation2);

        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap emptyAnnotationMap1 = new AnnotationMap();
        AnnotationMap emptyAnnotationMap2 = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(emptyAnnotationMap1, emptyAnnotationMap2);
        assertInstanceOf(AnnotationMap.class, mergedAnnotationMap);
        assertTrue(mergedAnnotationMap.isEmpty());
    }

    @Test
    public void testMergeAnnotationMapsWithSameAnnotations() {
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();

        AnnotationMap annotationMap1 = new AnnotationMap();
        annotationMap1.add(annotation1);
        annotationMap1.add(annotation2);

        AnnotationMap annotationMap2 = new AnnotationMap();
        annotationMap2.add(annotation1);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertInstanceOf(AnnotationMap.class, mergedAnnotationMap);
        assertEquals(2, mergedAnnotationMap.size());
        Set<Annotation> mergedAnnotations = new HashSet<>(mergedAnnotationMap.annotations());
        assertEquals(2, mergedAnnotations.size());
        assertTrue(mergedAnnotations.contains(annotation1));
        assertTrue(mergedAnnotations.contains(annotation2));
    }

    @Test
    public void testMergeAnnotationMapsWithDifferentAnnotations() {
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();

        AnnotationMap annotationMap1 = new AnnotationMap();
        annotationMap1.add(annotation1);

        AnotherAnnotation anotherAnnotation = new AnotherAnnotation();
        AnnotationMap annotationMap2 = new AnnotationMap();
        annotationMap2.add(anotherAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertInstanceOf(AnnotationMap.class, mergedAnnotationMap);
        assertEquals(2, mergedAnnotationMap.size());
        Set<Annotation> mergedAnnotations = new HashSet<>(mergedAnnotationMap.annotations());
        assertEquals(2, mergedAnnotations.size());
        assertTrue(mergedAnnotations.contains(annotation1));
        assertTrue(mergedAnnotations.contains(anotherAnnotation));
    }

    @Test
    public void testToString() {
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();

        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        String annotationMapString = annotationMap.toString();
        assertTrue(annotationMapString.contains("AnnotationMap@"));
        assertTrue(annotationMapString.contains("[class " + MyAnnotation.class.getName() + "]"));
        assertTrue(annotationMapString.contains("[class " + MyAnnotation.class.getName() + "]"));
    }

    @Test
    public void testAddIfNotPresent() {
        MyAnnotation annotation = new MyAnnotation();

        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertEquals(annotation, annotationMap.get(MyAnnotation.class));

        AnnotationMap annotationMapWithAnnotation = new AnnotationMap();
        annotationMapWithAnnotation.add(annotation);
        assertFalse(annotationMapWithAnnotation.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd() {
        MyAnnotation annotation = new MyAnnotation();

        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.add(annotation));
        assertEquals(annotation, annotationMap.get(MyAnnotation.class));

        AnnotationMap annotationMapWithAnnotation = new AnnotationMap();
        annotationMapWithAnnotation.add(annotation);
        assertFalse(annotationMapWithAnnotation.add(annotation));
    }

    @Test
    public void testMultipleAdds() {
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();

        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(2, annotations.size());
        assertEquals(annotation1, annotations.get(0));
        assertEquals(annotation2, annotations.get(1));
    }

    @Test
    public void testMergeAnnotationMapAndEmptyAnnotationMap() {
        MyAnnotation annotation = new MyAnnotation();

        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(annotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, new AnnotationMap());
        assertEquals(annotation, mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testMergeAnnotationMapWithEmptyAnnotationMap() {
        MyAnnotation annotation = new MyAnnotation();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(new AnnotationMap(), annotationMap);
        assertEquals(annotation, mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testMergeAnnotationMapsWithSameAnnotationTypesButDifferentInstances() {
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();

        AnnotationMap annotationMap1 = new AnnotationMap();
        annotationMap1.add(annotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        annotationMap2.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertEquals(2, mergedAnnotationMap.size());
        Set<Annotation> mergedAnnotations = new HashSet<>(mergedAnnotationMap.annotations());
        assertEquals(2, mergedAnnotations.size());
        assertTrue(mergedAnnotations.contains(annotation1));
        assertTrue(mergedAnnotations.contains(annotation2));
    }

    @Test
    public void testMergeAnnotationMapsWithSameAnnotationTypesButSameInstances() {
        MyAnnotation annotation = new MyAnnotation();

        AnnotationMap annotationMap1 = new AnnotationMap();
        annotationMap1.add(annotation);

        AnnotationMap annotationMap2 = new AnnotationMap();
        annotationMap2.add(annotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertEquals(1, mergedAnnotationMap.size());
        Set<Annotation> mergedAnnotations = new HashSet<>(mergedAnnotationMap.annotations());
        assertEquals(1, mergedAnnotations.size());
        assertTrue(mergedAnnotations.contains(annotation));
    }

    @Test
    public void testMergeAnnotationMapsWithDifferentAnnotationTypes() {
        MyAnnotation annotation1 = new MyAnnotation();
        AnotherAnnotation anotherAnnotation = new AnotherAnnotation();

        AnnotationMap annotationMap1 = new AnnotationMap();
        annotationMap1.add(annotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        annotationMap2.add(anotherAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertEquals(2, mergedAnnotationMap.size());
        Set<Annotation> mergedAnnotations = new HashSet<>(mergedAnnotationMap.annotations());
        assertEquals(2, mergedAnnotations.size());
        assertTrue(mergedAnnotations.contains(annotation1));
        assertTrue(mergedAnnotations.contains(anotherAnnotation));
    }

    private static class MyAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return MyAnnotation.class;
        }

        @Override
        public String toString() {
            return "MyAnnotation";
        }
    }

    private static class AnotherAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return AnotherAnnotation.class;
        }

        @Override
        public String toString() {
            return "AnotherAnnotation";
        }
    }
}