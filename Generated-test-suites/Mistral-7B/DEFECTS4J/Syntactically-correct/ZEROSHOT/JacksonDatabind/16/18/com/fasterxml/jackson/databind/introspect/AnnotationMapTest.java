package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;

public class AnnotationMapTest {

    @Test
    public void testCreateAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(JsonValue.class));
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonValue annotation = new JsonValue();
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(JsonValue.class));
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationNullMap() {
        AnnotationMap annotationMap = null;
        annotationMap.get(JsonValue.class);
    }

    @Test
    public void testGetAnnotationNullAnnotationType() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(null));
    }

    @Test
    public void testAnnotationsEmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        List<Annotation> annotations = annotationMap.annotations();
        assertEquals(0, annotations.size());
    }

    @Test
    public void testAnnotationsNonEmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonValue annotation1 = new JsonValue();
        JsonValue annotation2 = new JsonValue();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        List<Annotation> annotations = annotationMap.annotations();
        assertEquals(2, annotations.size());
        assertTrue(annotations.contains(annotation1));
        assertTrue(annotations.contains(annotation2));
    }

    @Test
    public void testMergeNullPrimary() {
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonValue annotation1 = new JsonValue();
        JsonValue annotation2 = new JsonValue();
        secondaryAnnotationMap.add(annotation1);
        secondaryAnnotationMap.add(annotation2);

        AnnotationMap primaryAnnotationMap = null;

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(secondaryAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeNullSecondary() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        JsonValue annotation1 = new JsonValue();
        primaryAnnotationMap.add(annotation1);

        AnnotationMap secondaryAnnotationMap = null;

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(primaryAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeEqual() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        JsonValue annotation1 = new JsonValue();
        primaryAnnotationMap.add(annotation1);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(annotation1);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(primaryAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeDifferent() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        JsonValue annotation1 = new JsonValue();
        primaryAnnotationMap.add(annotation1);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonValue annotation2 = new JsonValue();
        secondaryAnnotationMap.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(annotationMapSize(2), mergedAnnotationMap.size());
        assertTrue(annotationsContainAll(mergedAnnotationMap, annotation1, annotation2));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        JsonValue annotation1 = new JsonValue();
        annotationMap.add(annotation1);
        assertEquals(1, annotationMap.size());

        JsonValue annotation2 = new JsonValue();
        annotationMap.add(annotation2);
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonValue annotation1 = new JsonValue();

        assertTrue(annotationMap.addIfNotPresent(annotation1));
        assertEquals(1, annotationMap.size());

        JsonValue annotation2 = new JsonValue();

        assertTrue(annotationMap.addIfNotPresent(annotation2));
        assertEquals(2, annotationMap.size());

        JsonValue annotation3 = new JsonValue();

        assertFalse(annotationMap.addIfNotPresent(annotation3));
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonValue annotation1 = new JsonValue();

        assertTrue(annotationMap.add(annotation1));
        assertEquals(1, annotationMap.size());

        JsonValue annotation2 = new JsonValue();

        assertTrue(annotationMap.add(annotation2));
        assertEquals(2, annotationMap.size());

        JsonValue annotation3 = new JsonValue();

        assertTrue(annotationMap.add(annotation3));
        assertEquals(3, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonValue annotation1 = new JsonValue();
        annotationMap.add(annotation1);
        JsonValue annotation2 = new JsonValue();
        annotationMap.add(annotation2);

        String stringRepresentation = annotationMap.toString();
        String expectedStringRepresentation = "{\n" +
                "  AnnotationMap(\n" +
                "    _annotations=[ClassKey(AnnotationType[com.fasterxml.jackson.annotation.JsonValue]), AnnotationImpl(\n" +
                "      JsonValue(value=true, hidden=false, visible=true, repeatable=false, serializable=true, defaultValue=null)\n" +
                "    ), ClassKey(AnnotationType[com.fasterxml.jackson.annotation.JsonValue]), AnnotationImpl(\n" +
                "      JsonValue(value=true, hidden=false, visible=true, repeatable=false, serializable=true, defaultValue=null)\n" +
                "    )]\n" +
                "  )\n" +
                "}";

        assertEquals(expectedStringRepresentation, stringRepresentation);
    }

    private int annotationMapSize(AnnotationMap annotationMap) {
        return annotationMap.size();
    }

    private boolean annotationsContainAll(AnnotationMap annotationMap, Annotation... annotations) {
        List<Annotation> annotationsList = annotationMap.annotations();

        for (Annotation annotation : annotations) {
            assertTrue("Annotation not found in map: " + annotation, annotationsList.contains(annotation));
        }

        return true;
    }
}