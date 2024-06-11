package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import com.puppycrawl.tools.checkstyle.api.AbstractLoader.ErrorHandler;
import com.puppycrawl.tools.checkstyle.api.DefaultHandler;
import com.puppycrawl.tools.checkstyle.api.EntityResolver;
import com.puppycrawl.tools.checkstyle.api.InputSource;

import org.junit.Test;
import org.mockito.Mockito;

public class AbstractLoaderTest
{
    @Test
    public void testAbstractLoaderConstructorWithPublicIdAndDtdResourceName() throws Exception
    {
        final String publicId = "publicId";
        final String dtdResourceName = "dtdResourceName";
        final AbstractLoader abstractLoader = new AbstractLoader(publicId, dtdResourceName);
        assertEquals(publicId, abstractLoader.publicIdToResourceNameMap.keySet().iterator().next());
        assertEquals(dtdResourceName, abstractLoader.publicIdToResourceNameMap.get(publicId));
    }

    @Test
    public void testAbstractLoaderConstructorWithMap() throws Exception
    {
        final Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");
        publicIdToResourceNameMap.put("publicId2", "dtdResourceName2");
        final AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);
    }

    @Test
    public void testParseInputSource() throws Exception
    {
        final InputSource inputSource = Mockito.mock(InputSource.class);
        final AbstractLoader abstractLoader = Mockito.spy(new AbstractLoader(Maps.newHashMap()));
        abstractLoader.parseInputSource(inputSource);
        Mockito.verify(abstractLoader.parser).parse(inputSource);
    }

    @Test(expected = IOException.class)
    public void testResolveEntityWithInvalidPublicId() throws Exception
    {
        final AbstractLoader abstractLoader = new AbstractLoader(Maps.newHashMap());
        abstractLoader.resolveEntity("invalidPublicId", "systemId");
    }

    @Test(expected = IOException.class)
    public void testResolveEntityWithNullSystemId() throws Exception
    {
        final AbstractLoader abstractLoader = new AbstractLoader(Maps.newHashMap());
        abstractLoader.resolveEntity("publicId", null);
    }

    @Test(expected = SAXException.class)
    public void testErrorWithSAXParseException() throws Exception
    {
        final AbstractLoader abstractLoader = Mockito.spy(new AbstractLoader(Maps.newHashMap()));
        final ErrorHandler errorHandler = Mockito.mock(ErrorHandler.class);
        abstractLoader.setErrorHandler(errorHandler);
        final SAXParseException exception = Mockito.mock(SAXParseException.class);
        abstractLoader.error(exception);
        Mockito.verify(errorHandler).fatalError(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalErrorWithSAXParseException() throws Exception
    {
        final AbstractLoader abstractLoader = Mockito.spy(new AbstractLoader(Maps.newHashMap()));
        final ErrorHandler errorHandler = Mockito.mock(ErrorHandler.class);
        abstractLoader.setErrorHandler(errorHandler);
        final SAXParseException exception = Mockito.mock(SAXParseException.class);
        abstractLoader.fatalError(exception);
        Mockito.verify(errorHandler).fatalError(exception);
    }

    @Test
    public void testResolveEntityWithCustomResource() throws Exception
    {
        final String publicId = "publicId";
        final String dtdResourceName = "dtdResourceName";
        final AbstractLoader abstractLoader = new AbstractLoader(Maps.newHashMap());
        final InputStream inputStream = Mockito.mock(InputStream.class);
        final ClassLoader classLoader = AbstractLoaderTest.class.getClassLoader();
        Mockito.when(classLoader.getResourceAsStream(dtdResourceName)).thenReturn(inputStream);
        final InputSource inputSource = new InputSource(inputStream);
        final EntityResolver entityResolver = Mockito.mock(EntityResolver.class);
        abstractLoader.setEntityResolver(entityResolver);
        final InputSource result = abstractLoader.resolveEntity(publicId, null);
        assertEquals(inputSource, result);
        Mockito.verify(entityResolver).getEntity(publicId, null);
    }
}