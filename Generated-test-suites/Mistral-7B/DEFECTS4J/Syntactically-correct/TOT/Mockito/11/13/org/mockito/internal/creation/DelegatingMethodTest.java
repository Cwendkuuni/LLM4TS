package org.mockito.internal.creation.test;

import org.junit.Test; import static org.junit.Assert.assertArrayEquals; import static org.junit.Assert.assertSame;

public class DelegatingMethodTest {

    @Test
    public void testGetJavaMethod() {
        Method mockMethod = MockitoTestUtils.mockMethod(String.class, "testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Method actualMethod = delegatingMethod.getJavaMethod();
        assertSame(mockMethod, actualMethod);
    }

    @Test
    public void testGetName() {
        Method mockMethod = MockitoTestUtils.mockMethod(String.class, "testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        String expectedName = "testMethod";
        String actualName = delegatingMethod.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Method mockMethod = MockitoTestUtils.mockMethod(String.class, "testMethod", new Class[]{int.class, String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Class<?>[] expectedParameterTypes = {int.class, String.class};
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(expectedParameterTypes, actualParameterTypes);
    }

    // test cases for other methods go here
}