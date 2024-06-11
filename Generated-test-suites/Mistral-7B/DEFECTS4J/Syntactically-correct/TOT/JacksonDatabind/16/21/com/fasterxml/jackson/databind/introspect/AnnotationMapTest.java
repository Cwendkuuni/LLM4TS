import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOClass;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link AnnotationMap}.
 */
public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test getting a null annotation
        assertNull(annotationMap.get(null));

        // Test getting an existing annotation
        @JsonDeserialize Annotation annotation = new JsonDeserialize();
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(JsonDeserialize.class));

        // Test getting a non-existing annotation
        assertNull(annotationMap.get(JsonTypeInfo.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test empty map
        Assert.assertEquals(Collections.emptyIterator(), annotationMap.annotations());

        // Test map with one annotation
        @JsonPOJOClass JsonPOJOClassAnnotation annotation1 = new JsonPOJOClass();
        annotationMap.add(annotation1);
        List<Annotation> annotations1 = new ArrayList<>(annotationMap.annotations());
        Assert.assertEquals(1, annotations1.size());
        Assert.assertEquals(annotation1, annotations1.get(0));

        // Test map with multiple annotations
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD) TestAnnotation annotation2 = new TestAnnotation();
        annotationMap.add(annotation2);
        List<Annotation> annotations2 = new ArrayList<>(annotationMap.annotations());
        Assert.assertEquals(2, annotations2.size());
        Assert.assertNotSame(annotation1, annotations2.get(1));
        Assert.assertEquals(annotation2, annotations2.get(1));
    }

    @Test
    public void testMerge() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        @JsonDeserialize Annotation annotation1 = new JsonDeserialize();
        annotationMap1.add(annotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        @JsonPOJOClass JsonPOJOClassAnnotation annotation2 = new JsonPOJOClass();
        annotationMap2.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        Assert.assertEquals(2, mergedAnnotations.size());
        Assert.assertEquals(annotation1, mergedAnnotations.get(0));
        Assert.assertEquals(annotation2, mergedAnnotations.get(1));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        @JsonDeserialize Annotation annotation1 = new JsonDeserialize();
        annotationMap.add(annotation1);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        @JsonDeserialize Annotation annotation1 = new JsonDeserialize();

        // Test adding existing annotation
        Assert.assertFalse(annotationMap.addIfNotPresent(annotation1));

        // Test adding non-existing annotation
        Annotation annotation2 = new TestAnnotation();
        Assert.assertTrue(annotationMap.addIfNotPresent(annotation2));
        assertEquals(1, annotationMap.size());
        assertTrue(annotationMap.containsKey(annotation2.annotationType()));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        @JsonDeserialize Annotation annotation1 = new JsonDeserialize();

        // Test adding existing annotation
        Assert.assertFalse(annotationMap.add(annotation1));

        // Test adding non-existing annotation
        Annotation annotation2 = new TestAnnotation();
        Assert.assertTrue(annotationMap.add(annotation2));
        assertEquals(1, annotationMap.size());
        assertTrue(annotationMap.containsKey(annotation2.annotationType()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        @JsonDeserialize Annotation annotation1 = new JsonDeserialize();
        annotationMap.add(annotation1);

        String expected = "{" +
                "  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(Lcom.fasterxml.jackson.databind.annotation.JsonDeserialize;)=" +
                "  @com.fasterxml.jackson.annotation.JsonPOJOClass(Lcom.fasterxml.jackson.annotation.JsonPOJOClass;)=" +
                "}";
        @JsonPOJOClass JsonPOJOClassAnnotation annotation2 = new JsonPOJOClass();
        annotationMap.add(annotation2);
        String actual = annotationMap.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashSet() {
        AnnotationMap annotationMap = new AnnotationMap();
        @JsonDeserialize Annotation annotation1 = new JsonDeserialize();
        annotationMap.add(annotation1);

        HashSet<Annotation> hashSet = new HashSet<>(annotationMap.annotations());
        assertEquals(1, hashSet.size());
        assertTrue(hashSet.contains(annotation1));
    }

    @Test
    public void testCollectionsMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        @JsonDeserialize Annotation annotation1 = new JsonDeserialize();
        annotationMap.add(annotation1);

        Map<Class<? extends Annotation>, Annotation> map = new HashMap<>(annotationMap.annotations());
        assertEquals(1, map.size());
        assertEquals(annotation1, map.get(JsonDeserialize.class));
    }

    @Test
    public void testAnnotationsWithNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        @JsonDeserialize Annotation annotation1 = new JsonDeserialize();
        annotationMap.add(annotation1);

        // Test null map
        Assert.assertEquals(Collections.emptyIterator(), annotationMap.annotations());

        // Test null annotations
        Annotation nullAnnotation = null;
        annotationMap.add(nullAnnotation);
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        Assert.assertEquals(1, annotations.size());
        Assert.assertNull(annotations.get(0));
    }

    @Test
    public void testAnnotationsWithEmptyList() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test empty list
        List<Annotation> emptyList = Collections.emptyList();
        annotationMap.annotations(emptyList);
        Assert.assertEquals(Collections.emptyIterator(), annotationMap.annotations());
    }

    @Test
    public void testAnnotationsWithIterableAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test iterable annotation
        Iterable<Annotation> iterableAnnotation = Arrays.asList(new JsonDeserialize(), new JsonPOJOClass());
        annotationMap.annotations(iterableAnnotation);
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        Assert.assertEquals(2, annotations.size());
        Assert.assertEquals(annotations.get(0), annotations.get(1));
    }

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
    static class TestAnnotation {}
}