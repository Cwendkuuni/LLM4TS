package com.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IOContextTest {
//... (previous test cases)

@Test
public void testSetEncoding() {
IOContext ioContext = new IOContext(null, null, false);
JsonEncoding jsonEncoding = JsonEncoding.UTF8;

ioContext.setEncoding(jsonEncoding);
assertEquals(jsonEncoding, ioContext.getEncoding());
}

@Test
public void testWithEncoding() {
IOContext ioContext = new IOContext(null, null, false);
JsonEncoding jsonEncoding = JsonEncoding.UTF8;

IOContext result = ioContext.withEncoding(jsonEncoding);
assertEquals(jsonEncoding, result.getEncoding());
assertEquals(ioContext, result);
}

@Test
public void testGetSourceReference() {
IOContext ioContext = new IOContext(null, new Object(), false);
assertEquals(ioContext.getSourceReference(), ioContext._sourceRef);
}

@Test
public void testGetEncoding() {
IOContext ioContext = new IOContext(null, null, false);
assertEquals(ioContext.getEncoding(), null);

IOContext ioContextWithEncoding = new IOContext(null, null, false);
ioContextWithEncoding.setEncoding(JsonEncoding.UTF8);
assertEquals(ioContextWithEncoding.getEncoding(), JsonEncoding.UTF8);
}

@Test
public void testIsResourceManaged() {
IOContext ioContext = new IOContext(null, null, true);
assertTrue(ioContext.isResourceManaged());

IOContext ioContextFalse = new IOContext(null, null, false);
assertFalse(ioContextFalse.isResourceManaged());
}
}