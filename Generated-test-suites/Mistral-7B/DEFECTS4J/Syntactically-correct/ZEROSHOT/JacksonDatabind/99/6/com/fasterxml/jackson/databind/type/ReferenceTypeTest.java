package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeModifier;
import com.fasterxml.jackson.databind.type.TypeReference;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(int.class);

        // Test construct method
        ReferenceType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), javaIntType, null, javaStringType);
        assertEquals("expected reference type instance", referenceType.getClass(), ReferenceType.class);
        assertEquals("class and referencedType should match", stringClass, referenceType.getClassInternal());
        assertEquals("referencedType should match", javaStringType, referenceType.getContentType());

        // Test upgradeFrom method
        JavaType javaStringReferenceType = ReferenceType.upgradeFrom(javaStringType, javaIntType);
        ReferenceType referenceTypeFromUpgrade = (ReferenceType) javaStringReferenceType;
        assertEquals("constructor and upgraded type should match", referenceType.getClassInternal(), referenceTypeFromUpgrade.getClassInternal());
        assertEquals("referencedType should match", javaStringType, referenceType.getContentType());
        assertEquals("baseType should match", javaStringType, referenceTypeFromUpgrade.getBaseType());
    }

    @Test
    public void testReferenceTypeMethods() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(int.class);

        // Test getContentType, isReferenceType, hasContentType methods
        ReferenceType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), javaIntType, null, javaStringType);
        assertTrue("should be a reference type", referenceType.isReferenceType());
        assertTrue("should have content type", referenceType.hasContentType());
        assertEquals("content type should match", javaStringType, referenceType.getContentType());

        // Test getAnchorType method
        assertEquals("anchor type should match the reference type itself", referenceType, referenceType.getAnchorType());

        // Test getErasedSignature and getGenericSignature methods
        StringBuilder erasedSignature = new StringBuilder();
        StringBuilder genericSignature = new StringBuilder();
        referenceType.getErasedSignature(erasedSignature);
        referenceType.getGenericSignature(genericSignature);

        String expectedErasedSignature = "[reference type, class java.lang.String]";
        String expectedGenericSignature = "[reference type, class java.lang.String<T>]";

        assertEquals("erasedSignature should match", expectedErasedSignature, erasedSignature.toString());
        assertEquals("genericSignature should match", expectedGenericSignature, genericSignature.toString());

        // Test equals method
        ReferenceType sameReferenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), javaIntType, null, javaStringType);
        assertTrue("same reference types should be equal", referenceType.equals(sameReferenceType));

        // Test not equal to different types
        JavaType javaListStringType = TypeFactory.defaultInstance().constructType(List.class, TypeReference.UNRESOLVED_TYPE);
        assertFalse("different types should not be equal", referenceType.equals(javaListStringType));

        // Test narrow method (this method is deprecated)
        JavaType stringSubclass = StringSubclass.class;
        ReferenceType stringSubclassReferenceType = ReferenceType.upgradeFrom(javaStringType, stringClass);
        JavaType narrowedType = referenceType._narrow(stringSubclass);
        assertSame("narrowed types should be the same", stringSubclassReferenceType, narrowedType);

        // Test withContentType method
        JavaType javaByteType = TypeFactory.defaultInstance().constructType(byte.class);
        ReferenceType byteReferenceType = ReferenceType.construct(byte.class, TypeBindings.emptyBindings(), javaIntType, null, javaByteType);
        JavaType withContentType = byteReferenceType.withContentType(javaStringType);
        assertNotSame("types should not be the same", byteReferenceType, withContentType);
        assertEquals("content type should match", javaStringType, withContentType.getContentType());
    }

    @Test
    public void testTypeHandlers() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(int.class);

        // Test withTypeHandler method
        TypeDeserializer stringDeserializer = new StringDeserializer();
        ReferenceType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), javaIntType, null, javaStringType);
        JavaType withTypeHandler = referenceType.withTypeHandler(stringDeserializer);
        assertNotSame("types should not be the same", referenceType, withTypeHandler);
        assertEquals("typeHandler should match", stringDeserializer, withTypeHandler.getTypeHandler());

        // Test withContentTypeHandler method
        TypeDeserializer stringValueHandler = new StringValueDeserializer();
        JavaType javaStringWithValueHandlerType = ReferenceType.construct(StringValueHandler.class, TypeBindings.emptyBindings(), javaIntType, null, javaStringType);
        JavaType withContentTypeHandler = referenceType.withContentTypeHandler(stringValueHandler);
        assertNotSame("types should not be the same", referenceType, withContentTypeHandler);
        assertEquals("contentTypeHandler should match", stringValueHandler, withContentTypeHandler.getContentTypeHandler());

        // Test withValueHandler method
        TypeDeserializer intValueHandler = new IntValueDeserializer();
        JavaType javaStringWithValueHandler = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), javaIntType, null, javaStringType);
        JavaType withValueHandler = javaStringWithValueHandler.withValueHandler(intValueHandler);
        assertNotSame("types should not be the same", javaStringWithValueHandler, withValueHandler);
        assertEquals("valueHandler should match", intValueHandler, withValueHandler.getValueHandler());
    }

    @JsonTypeName("StringSubclass")
    static class StringSubclass extends String {
    }

    static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    static class StringValueDeserializer extends StdDeserializer<String> {
        public StringValueDeserializer() {
            this(null);
        }

        public StringValueDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            char[] chars = NON_EMPTY_STRING_VALUE;
            String value = Character.isISOControl(chars[0]) ? new String(chars, 0, chars.length - 1) : new String(chars, 0, chars.length);
            return value;
        }
    }

    static class IntValueDeserializer extends StdDeserializer<Integer> {
        public IntValueDeserializer() {
            this(null);
        }

        public IntValueDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }
    }
}

class TypeFactory {
    static TypeFactory defaultInstance = new TypeFactory();

    public JavaType constructType(Class<?> rawType) {
        return new TypeBase(ClassUtil.handleRawType(rawType), TypeBindings.emptyBindings());
    }

    public JavaType constructType(Class<?> rawType, JavaType contentType) {
        return new TypeBase(rawType, TypeBindings.emptyBindings()).muteConstructor().withContentType(contentType);
    }

    public JavaType constructType(Class<?> rawType, JavaType contentType, JavaType[] superTypes) {
        TypeBase typeBase = new TypeBase(rawType, TypeBindings.emptyBindings());
        for (JavaType superType : superTypes) {
            typeBase = typeBase.withSuperType(superType);
        }
        typeBase.muteConstructor().withContentType(contentType);
        return typeBase;
    }
}