package org.mockito.internal.util.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockitoMockFactory;
import org.mockito.internal.util.reflection.LenientReflection;
import org.mockito.internal.util.reflection.LenientStubber;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockSettingsImpl settings;
 private Object spiedInstance;
 private Class<? extends Object> classToMock;

 @Before
 public void setup() {
 mockUtil = new MockUtil(new CreationValidator());
 settings = new MockSettingsImpl();
 spiedInstance = new Object();
 classToMock = Object.class;
 }

 @Test
 public void testCreateMockWithValidArguments() {
 // GIVEN
 when(mockUtil.createMock(classToMock, settings)).thenReturn(mockitoMockFactory().createMock(classToMock));

 // WHEN
 Object mock = mockUtil.createMock(classToMock, settings);

 // THEN
 assertNotNull(mock);
 assertTrue(isMockitoMock(mock));
 verify(mockitoMockFactory()).createMock(classToMock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 // GIVEN
 settings.setSpiedInstance(spiedInstance);

 // WHEN
 mockUtil.createMock(null, settings);

 // THEN
 fail("Expected NotAMockException, but was not thrown.");
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 // GIVEN
 settings.setSpiedInstance(spiedInstance);
 settings.setExtraInterfaces(new Class[0]);

 // WHEN
 mockUtil.createMock(String.class, settings);

 // THEN
 fail("Expected NotAMockException, but was not thrown.");
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSpiedInstance() {
 // GIVEN
 settings.setExtraInterfaces(new Class[0]);

 // WHEN
 mockUtil.createMock(classToMock, null);

 // THEN
 fail("Expected NotAMockException, but was not thrown.");
 }

 @Test
 public void testCreateMockWithSpiedInstanceAndExtraInterfaces() {
 // GIVEN
 when(mockUtil.createMock(classToMock, settings)).thenReturn(mockitoMockFactory().createMock(classToMock));
 settings.setExtraInterfaces(new Class[]{Interface1.class, Interface2.class});

 // WHEN
 Object mock = mockUtil.createMock(classToMock, settings);

 // THEN
 assertNotNull(mock);
 assertTrue(isMockitoMock(mock));
 verify(mockitoMockFactory()).createMock(classToMock, Interface1.class, Interface2.class);
 }

 @Test
 public void testResetMock() {
 // GIVEN
 Object mock = mockitoMockFactory().createMock(classToMock);
 mockUtil.resetMock(mock);

 // WHEN
 doCallRealMethod().when(mock).methodToTest();

 // THEN
 assertSame(mock, mockUtil.getMockHandler(mock).getMockSettings().getMockName());
 verify(mock).methodToTest();
 }

 @Test
 public void testGetMockHandler() {
 // GIVEN
 Object mock = mockitoMockFactory().createMock(classToMock);

 // WHEN
 MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);

 // THEN
 assertNotNull(mockHandler);
 assertEquals(mockUtil.getMockName(mock), mockHandler.getMockName());
 }

 @Test
 public void testIsMock() {
 // GIVEN
 Object mock = mockitoMockFactory().createMock(classToMock);
 Object notAMock = new Object();

 // WHEN
 // THEN
 assertTrue(mockUtil.isMock(mock));
 assertFalse(mockUtil.isMock(notAMock));
 }

 private Object mockitoMockFactory() {
 return LenientReflection.mock(Object.class);
 }

 private LenientStubber lenientStubber() {
 return LenientStubber.create();
 }

 private Factory mockitoMockFactory() {
 return lenientStubber().createMockFactory();
 }

}