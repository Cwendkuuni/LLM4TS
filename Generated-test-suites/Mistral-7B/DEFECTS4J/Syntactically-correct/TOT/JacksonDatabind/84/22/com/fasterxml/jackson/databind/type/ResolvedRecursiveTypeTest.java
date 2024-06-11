import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 TypeBindings bindings = TypeBindings.empty();
 Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
 JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
 type1.setReference(refType);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
 type2.setReference(type1);
 assertEquals(type1, type2.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 TypeBindings bindings = TypeBindings.empty();
 Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
 type.setReference(refType);
 type.setReference(refType); // This will throw an IllegalStateException
 }
}