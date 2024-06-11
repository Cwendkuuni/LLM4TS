
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.AnnotationMap;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNotNull(annotationMap);
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        MyAnnotation retrievedAnnotation = annotationMap.get(MyAnnotation.class);

        assertNotNull(retrievedAnnotation);
        assertEquals(myAnnotation, retrievedAnnotation);
    }

    @Test
    public void testGetAllAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());

        assertFalse(annotations.isEmpty());
        assertEquals(2, annotations.size());
    }

    @Test
    public void testMergeAnnotationMaps() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap1.add(annotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap2.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);

        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());

        assertEquals(2, mergedAnnotations.size());
    }

    @Test
    public void testSizeAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        int size = annotationMap.size();

        assertEquals(1, size);
    }

    @Test
    public void testAddIfNotPresentAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();

        // First check that the annotation isn't present
        boolean isPresent = annotationMap.addIfNotPresent(myAnnotation);
        assertTrue(isPresent);

        // Check that the annotation is present after adding it
        MyAnnotation retrievedAnnotation = annotationMap.get(MyAnnotation.class);
        assertEquals(myAnnotation, retrievedAnnotation);
    }

    @Test
    public void testAddAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();

        // Check that the annotation is added
        boolean isAdded = annotationMap.add(myAnnotation);
        assertTrue(isAdded);

        // Check that the annotation is present after adding it
        MyAnnotation retrievedAnnotation = annotationMap.get(MyAnnotation.class);
        assertEquals(myAnnotation, retrievedAnnotation);
    }

    @Test
    public void testGetAnnotationOfNonExistentType() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        // Test getting an annotation of a non-existent type
        Annotation retrievedAnnotation = annotationMap.get(NonExistentAnnotation.class);

        assertNull(retrievedAnnotation);
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap emptyAnnotationMap1 = new AnnotationMap();
        AnnotationMap emptyAnnotationMap2 = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(emptyAnnotationMap1, emptyAnnotationMap2);

        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(0, mergedAnnotations.size());
    }

    @Test
    public void testMergeEmptyAndNonEmptyAnnotationMaps() {
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        emptyAnnotationMap.add(annotation1);

        AnnotationMap nonEmptyAnnotationMap = new AnnotationMap();
        MyAnnotation annotation2 = new MyAnnotation();
        nonEmptyAnnotationMap.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(emptyAnnotationMap, nonEmptyAnnotationMap);

        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(1, mergedAnnotations.size());
        assertEquals(annotation1, mergedAnnotations.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test adding a null annotation
        annotationMap.add(null);
    }

    @Test(expected = ClassCastException.class)
    public void testAddAnnotationOfNonExistentType() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        // Test adding an annotation of a non-existent type
        NonExistentAnnotation nonExistentAnnotation = new NonExistentAnnotation();
        annotationMap.add(nonExistentAnnotation);
    }
}

class MyAnnotation {}
class NonExistentAnnotation {}
