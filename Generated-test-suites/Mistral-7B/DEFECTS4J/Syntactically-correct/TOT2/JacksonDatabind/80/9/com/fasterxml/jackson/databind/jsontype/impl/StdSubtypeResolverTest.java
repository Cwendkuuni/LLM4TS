import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        resolver.registerSubtypes(subtype1, subtype2);
        JavaType baseTypeType = JavaType.constructTypeOrNull(baseType);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseTypeType);

        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        for (NamedType namedType : subtypes) {
            assertEquals(namedType.getType(), subtype1.equals(subtype1) ? subtype1 : subtype2);
        }
    }

    @Test
    public void testRegisterSubtypesByAnnotation() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        resolver.registerSubtypes(subtype1, subtype2);
        AnnotationIntrospector introspector = new AnnotationIntrospector();
        AnnotatedClass subtype1AnnotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(null, subtype1);
        AnnotatedMember baseTypeMember = AnnotatedMemberBuilder.builder().withRawType(baseType).build();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, baseTypeMember, null);

        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        for (NamedType namedType : subtypes) {
            assertEquals(namedType.getType(), subtype1.equals(subtype1) ? subtype1 : subtype2);
        }

        AnnotatedClass subtype2AnnotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(null, subtype2);
        Collection<NamedType> subtypes2 = resolver.collectAndResolveSubtypesByClass(null, null, null);

        assertNotNull(subtypes2);
        assertEquals(2, subtypes2.size());
        for (NamedType namedType : subtypes2) {
            assertEquals(namedType.getType(), subtype1.equals(subtype1) ? subtype1 : subtype2);
        }
    }

    @Test
    public void testNullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseTypeType = null;
        try {
            resolver.collectAndResolveSubtypesByClass(null, null, baseTypeType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseTypeType = String.class.getType();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseTypeType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testNullRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseTypeType = String.class.getType();
        resolver._registeredSubtypes = null;
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseTypeType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testSubtypeRegistrationOrder() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        resolver.registerSubtypes(subtype1, subtype2);
        // Register subtype2 again to make sure it does not override subtype1
        resolver.registerSubtypes(subtype2);
        JavaType baseTypeType = JavaType.constructTypeOrNull(baseType);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseTypeType);

        assertEquals(2, subtypes.size());
        for (NamedType namedType : subtypes) {
            if (subtype1.equals(namedType.getType())) {
                assertEquals("Integer", namedType.getName());
            } else if (subtype2.equals(namedType.getType())) {
                assertEquals("Long", namedType.getName());
            }
        }
    }

    @Test
    public void testRegisterSubtypesByAnnotationWithMultipleSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;
        Class<?> subtype3 = Float.class;
        Class<?> subtype4 = Double.class;

        resolver.registerSubtypes(subtype1, subtype2, subtype3, subtype4);
        AnnotationIntrospector introspector = new AnnotationIntrospector();
        AnnotatedClass subtype1AnnotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(null, subtype1);
        AnnotatedMember baseTypeMember = AnnotatedMemberBuilder.builder().withRawType(baseType).build();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, baseTypeMember, null);

        assertEquals(4, subtypes.size());
        for (NamedType namedType : subtypes) {
            if (subtype1.equals(namedType.getType())) {
                assertEquals("Integer", namedType.getName());
            } else if (subtype2.equals(namedType.getType())) {
                assertEquals("Long", namedType.getName());
            } else if (subtype3.equals(namedType.getType())) {
                assertEquals("Float", namedType.getName());
            } else if (subtype4.equals(namedType.getType())) {
                assertEquals("Double", namedType.getName());
            }
        }

        AnnotatedClass subtype3AnnotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(null, subtype3);
        Collection<NamedType> subtypes3 = resolver.collectAndResolveSubtypesByClass(null, null, null);

        assertEquals(4, subtypes3.size());
        for (NamedType namedType : subtypes3) {
            if (subtype1.equals(namedType.getType())) {
                assertEquals("Integer", namedType.getName());
            } else if (subtype2.equals(namedType.getType())) {
                assertEquals("Long", namedType.getName());
            } else if (subtype3.equals(namedType.getType())) {
                assertEquals("Float", namedType.getName());
            } else if (subtype4.equals(namedType.getType())) {
                assertEquals("Double", namedType.getName());
            }
        }
    }

    @Test
    public void testRegisterSubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        resolver.registerSubtypes(new NamedType(subtype1, "IntSubtype"));
        resolver.registerSubtypes(new NamedType(subtype2, "LongSubtype"));
        JavaType baseTypeType = JavaType.constructTypeOrNull(baseType);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseTypeType);

        assertEquals(2, subtypes.size());
        for (NamedType namedType : subtypes) {
            if (subtype1.equals(namedType.getType())) {
                assertEquals("IntSubtype", namedType.getName());
            } else if (subtype2.equals(namedType.getType())) {
                assertEquals("LongSubtype", namedType.getName());
            }
        }
    }
}