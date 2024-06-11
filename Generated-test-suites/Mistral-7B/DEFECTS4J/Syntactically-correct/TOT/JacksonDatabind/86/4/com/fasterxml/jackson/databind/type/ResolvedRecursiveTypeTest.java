import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 TypeBindings bindings = new TypeBindings();
 JavaType javaType1 = new ResolvedRecursiveType(Integer.TYPE, bindings);
 JavaType javaType2 = new ResolvedRecursiveType(String.TYPE, bindings);
 javaType1.setReference(javaType2);
 assertNotNull(javaType1.getSelfReferencedType());
 assertNotSame(javaType1, javaType2);
 assertSame(javaType2, javaType1.getSelfReferencedType());
 }
}