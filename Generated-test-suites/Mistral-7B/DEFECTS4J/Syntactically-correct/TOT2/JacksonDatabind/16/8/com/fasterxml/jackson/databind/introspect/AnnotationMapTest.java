import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@org.junit.Test
public class AnnotationMapTest {

    @org.junit.Before
    public void setup() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
    }

    @org.junit.Test
    public void testGetMethod() {
        Class<JsonDeserialize> jsonDeserializeClass = JsonDeserialize.class;
        JsonDeserialize jsonDeserializeAnnotation = annotationMap.get(jsonDeserializeClass);
        assertEquals(jsonDeserializeAnnotation, annotationMap.get(jsonDeserializeClass));
    }
}