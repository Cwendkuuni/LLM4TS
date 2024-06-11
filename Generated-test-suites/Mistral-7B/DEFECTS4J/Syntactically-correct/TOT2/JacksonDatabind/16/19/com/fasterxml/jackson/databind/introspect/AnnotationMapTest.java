import static org.junit.Assert.*;
import static org.junit.platform.commons.util.ReflectionUtils.getAllDeclaredConstructors;
import static org.junit.platform.commons.util.ReflectionUtils.getAllDeclaredMethods;
import static org.junit.platform.commons.util.ReflectionUtils.getFieldValue;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.*;
import org.junit.Test;
import org.junit.platform.commons.util.ReflectionUtils;

public class AnnotationMapTest {

    @Before
    public void setUp() {
        AnnotationMap annotationMap = new AnnotationMap();
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize annotation = new JsonDeserialize();
        annotationMap.add(annotation);
        JsonDeserialize expected = annotationMap.get(JsonDeserialize.class);

        assertEquals(annotation, expected);
    }

    @Test
    public void testGetAnnotationWhenMapIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(null, annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testGetAnnotationWhenAnnotationIsNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize annotation = new JsonDeserialize();
        annotationMap.add(new Object());
        assertEquals(null, annotationMap.get(JsonDeserialize.class));
    }

    @Test(expected = ClassCastException.class)
    public void testGetAnnotationWhenClassIsNotAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new Object());
        annotationMap.get(Object.class);
    }

    @Test
    public void testAddAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        Annotation expectedAnnotation = annotationMap.get(JsonDeserialize.class);

        assertEquals(jsonDeserializeAnnotation, expectedAnnotation);
    }

    @Test
    public void testAddDuplicateAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation1);
        annotationMap.add(jsonDeserializeAnnotation2);
        Annotation expectedAnnotation = jsonDeserializeAnnotation1;

        assertEquals(jsonDeserializeAnnotation1, annotationMap.get(JsonDeserialize.class));
        assertEquals(jsonDeserializeAnnotation1, annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation1);
        annotationMap.add(jsonDeserializeAnnotation2);

        List<Annotation> annotations = new ArrayList<>();
        annotations.add(jsonDeserializeAnnotation1);
        annotations.add(jsonDeserializeAnnotation2);

        List<Annotation> expectedAnnotations = annotations;

        List<Annotation> actualAnnotations = new ArrayList<>(annotationMap.annotations());

        assertEquals(expectedAnnotations, actualAnnotations);
    }

    @Test
    public void testMerge() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        annotationMap1.add(jsonDeserializeAnnotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        annotationMap2.add(jsonDeserializeAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        JsonDeserialize expectedAnnotation = jsonDeserializeAnnotation1;

        assertEquals(expectedAnnotation, mergedAnnotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testMergeWhenFirstMapIsEmpty() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        AnnotationMap annotationMap2 = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap2.add(jsonDeserializeAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);

        assertEquals(jsonDeserializeAnnotation, mergedAnnotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testMergeWhenSecondMapIsEmpty() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap1.add(jsonDeserializeAnnotation);

        AnnotationMap annotationMap2 = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);

        assertEquals(jsonDeserializeAnnotation, mergedAnnotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation1);
        annotationMap.add(jsonDeserializeAnnotation2);

        int expectedSize = 2;
        int actualSize = annotationMap.size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation1);
        annotationMap.add(jsonDeserializeAnnotation2);

        String expectedToString = "{"
                + "JsonDeserialize(annotationType=class java.lang.annotation.JsonDeserialize, value=JsonDeserializeImpl@1b9d5e1)"
                + ", JsonDeserialize(annotationType=class java.lang.annotation.JsonDeserialize, value=JsonDeserializeImpl@1b1a2c5)"
                + "}";

        String actualToString = annotationMap.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        Constructor<AnnotationMap> constructor = getAllDeclaredConstructors(AnnotationMap.class).get(0);
        AnnotationMap annotationMap = constructor.newInstance();

        assertNotNull(annotationMap);
    }

    @Test
    public void testConstructorWithMap() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        Constructor<AnnotationMap> constructor = getAllDeclaredConstructors(AnnotationMap.class).get(1);
        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        AnnotationMap annotationMap = constructor.newInstance(annotations);

        assertNotNull(annotationMap);
    }

    @Test
    public void testGetClassAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        @JsonDeserialize JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        Class<JsonDeserialize> jsonDeserializeClass = JsonDeserialize.class;
        annotationMap.add(jsonDeserializeAnnotation);

        JsonDeserialize expectedAnnotation = jsonDeserializeAnnotation;
        Class<?> expectedClass = jsonDeserializeClass;

        Class<?> actualClass = annotationMap.get(jsonDeserializeClass).annotationType();

        assertEquals(expectedClass, actualClass);
        assertEquals(expectedAnnotation, annotationMap.get(jsonDeserializeClass));
    }
}