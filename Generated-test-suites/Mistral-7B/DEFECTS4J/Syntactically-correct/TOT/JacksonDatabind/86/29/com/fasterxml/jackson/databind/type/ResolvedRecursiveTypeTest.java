import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType actual = new ResolvedRecursiveType(type, null);

        JavaType reference = new TypeReference<ResolvedRecursiveType>() {}.getType();
        actual.setReference(reference);
        ResolvedRecursiveType expected = new ResolvedRecursiveType(type, null);
        expected.setReference(reference);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType actual = new ResolvedRecursiveType(type, null);
        JavaType expected = null;

        assertNull(actual.getSelfReferencedType());
        assertNull(expected);

        JavaType reference = new TypeReference<ResolvedRecursiveType>() {}.getType();
        actual.setReference(reference);
        expected = reference;

        assertEquals(expected, actual.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType actual = new ResolvedRecursiveType(type, null);
        StringBuilder expectedSb = new StringBuilder();
        StringBuilder actualSb = new StringBuilder();

        actual.getGenericSignature(expectedSb);
        actual.getGenericSignature(actualSb);

        ObjectMapper mapper = new ObjectMapper();
        String expectedString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectedSb.toString());
        String actualString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualSb.toString());

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetErasedSignature() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType actual = new ResolvedRecursiveType(type, null);
        StringBuilder expectedSb = new StringBuilder();
        StringBuilder actualSb = new StringBuilder();

        actual.getErasedSignature(expectedSb);
        actual.getErasedSignature(actualSb);

        ObjectMapper mapper = new ObjectMapper();
        String expectedString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectedSb.toString());
        String actualString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualSb.toString());

        assertEquals(expectedString, actualString);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
        ResolvedRecursiveType actual = new ResolvedRecursiveType(type, null);

        JavaType reference = new TypeReference<ResolvedRecursiveType>() {}.getType();
        actual.setReference(reference);
        actual.setReference(reference);
    }
}