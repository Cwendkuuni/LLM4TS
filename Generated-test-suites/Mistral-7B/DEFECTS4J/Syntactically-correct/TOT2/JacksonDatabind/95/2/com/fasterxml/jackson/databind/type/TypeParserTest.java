import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.MyTokenizer;

public class TypeParserTest {

    TypeFactory typeFactory = new DefaultTypeFactory();
    TypeParser typeParser = new TypeParser(typeFactory);

    @Test
    public void testParse() {
        String canonical = "java.lang.String";
        JavaType expectedType = typeFactory.constructType(canonical);
        JavaType actualType = typeParser.parse(canonical);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testParse_EmptyString() {
        try {
            typeParser.parse("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParse_InvalidType() {
        try {
            typeParser.parse("abcd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParse_InvalidToken() {
        String canonical = "[java.lang.String";
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParse_NonExistentClass() {
        String canonical = "com.nonexistent.MyClass";
        typeParser.parse(canonical);
    }

    @Test
    public void testParseType() {
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        JavaType expectedType = typeFactory.constructType("java.lang.String");
        JavaType actualType = typeParser.parseType(tokens);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testParseType_EmptyTokenizer() {
        MyTokenizer tokens = new MyTokenizer("");
        try {
            typeParser.parseType(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseType_UnexpectedToken() {
        MyTokenizer tokens = new MyTokenizer("[java.lang.String");
        try {
            typeParser.parseType(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseType_NotEnoughTokens() {
        MyTokenizer tokens = new MyTokenizer("java.lang.");
        try {
            typeParser.parseType(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}