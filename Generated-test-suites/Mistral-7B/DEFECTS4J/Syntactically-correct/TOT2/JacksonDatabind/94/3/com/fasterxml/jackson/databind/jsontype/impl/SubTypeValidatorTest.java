package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

    @Test
    public void testInstance() {
        SubTypeValidator validator = SubTypeValidator.instance();
        assertNotNull(validator);
    }

    @Test
    public void testGetDefaultNoDeserClassNames() {
        Set<String> classNames = SubTypeValidator.getDefaultNoDeserClassNames();
        assertEquals(30, classNames.size());
    }

    @Test
    public void testValidateSubType_ValidClass() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, type);

        verify(ctxt, times(1)).asmackingException(any(JsonMappingException.class));
        verifyNoMoreInteractions(ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalClass() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        when(type.getRawClass().getName()).thenReturn("com.mchange.v2.c3p0.ComboPooledDataSource");

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, type);

        verify(ctxt, times(1)).asmackingException(any(JsonMappingException.class));
    }

    @Test
    public void testValidateSubType_Interface() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        when(type.getRawClass().isInterface()).thenReturn(true);

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, type);

        verifyNoInteractions(ctxt);
    }
}