import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 JavaType refType = new TypeFactory().constructType(String.class);
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String[].class, new TypeBindings());
 type1.setReference(refType);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String[].class, new TypeBindings());
 type2.setReference(type1);

 assertEquals(type1, type2.getSelfReferencedType());
 assertEquals(type2, type1.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType refType = new TypeFactory().constructType(String.class);
 ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, new TypeBindings());
 type.setReference(refType);
 type.setReference(refType);
 }
}