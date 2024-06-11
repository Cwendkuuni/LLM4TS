package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AnnotationMapTest {

    @Test
    public void testInitialization() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(JsonDeserialize.class));
        assertFalse(annotationMap.size() > 0);
    }

    @Test
    public void testAddAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        assertTrue(annotationMap.add(jsonDeserializeAnnotation));
        assertEquals(jsonDeserializeAnnotation, annotationMap.get(JsonDeserialize.class));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddDuplicateAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        assertTrue(annotationMap.add(jsonDeserializeAnnotation));
        assertFalse(annotationMap.add(jsonDeserializeAnnotation));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertSame(primaryAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeSingleAnnotationInPrimaryAnnotationMap() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        primaryAnnotationMap.add(jsonDeserializeAnnotation1);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        secondaryAnnotationMap.add(jsonDeserializeAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(jsonDeserializeAnnotation1, mergedAnnotationMap.get(JsonDeserialize.class));
        assertEquals(1, mergedAnnotationMap.size());
    }

    @Test
    public void testMergeSingleAnnotationInSecondaryAnnotationMap() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        secondaryAnnotationMap.add(jsonDeserializeAnnotation1);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(jsonDeserializeAnnotation1, mergedAnnotationMap.get(JsonDeserialize.class));
        assertEquals(1, mergedAnnotationMap.size());
    }

    @Test
    public void testMergeMultipleAnnotationsInBothAnnotationMaps() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        primaryAnnotationMap.add(jsonDeserializeAnnotation1);

        JsonSerialize jsonSerializeAnnotation1 = new JsonSerialize();
        primaryAnnotationMap.add(jsonSerializeAnnotation1);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        secondaryAnnotationMap.add(jsonDeserializeAnnotation2);

        JsonSerialize jsonSerializeAnnotation2 = new JsonSerialize();
        secondaryAnnotationMap.add(jsonSerializeAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(jsonDeserializeAnnotation1, mergedAnnotationMap.get(JsonDeserialize.class));
        assertEquals(jsonSerializeAnnotation1, mergedAnnotationMap.get(JsonSerialize.class));
        assertEquals(jsonDeserializeAnnotation2, mergedAnnotationMap.get(JsonDeserialize.class));
        assertEquals(jsonSerializeAnnotation2, mergedAnnotationMap.get(JsonSerialize.class));
        assertEquals(4, mergedAnnotationMap.size());
    }

    @Test
    public void testIterator() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation1);

        JsonSerialize jsonSerializeAnnotation1 = new JsonSerialize();
        annotationMap.add(jsonSerializeAnnotation1);

        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(2, annotations.size());
        assertEquals(jsonDeserializeAnnotation1, annotations.get(0));
        assertEquals(jsonSerializeAnnotation1, annotations.get(1));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);

        String toString = annotationMap.toString();
        assertTrue(toString.startsWith("[{@"));
        assertTrue(toString.endsWith("}]"));
        assertTrue(toString.contains(",{@"));
    }
}