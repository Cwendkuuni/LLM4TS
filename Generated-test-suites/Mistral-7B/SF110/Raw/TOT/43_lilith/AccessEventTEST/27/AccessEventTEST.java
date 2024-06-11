
package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testSetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expectedTimeStamp = 1L;
        event.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = event.getTimeStamp();
        assertEquals("time stamp is not set correctly", expectedTimeStamp, actualTimeStamp);
    }

    @Test
    public void testGetLoggerContext() {
        LoggerContext loggerContextMock = mock(LoggerContext.class);
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(loggerContextMock);
        LoggerContext actualLoggerContext = event.getLoggerContext();
        assertEquals("logger context is not retrieved correctly", loggerContextMock, actualLoggerContext);
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(mock(LoggerContext.class));
        event1.setRequestURI("testURI");
        event2.setTimeStamp(1L);
        event2.setLoggerContext(mock(LoggerContext.class));
        event2.setRequestURI("testURI");

        assertTrue("Two equal AccessEvent objects are not considered equal", event1.equals(event2));

        event2.setTimeStamp(2L);
        assertFalse("Two not equal AccessEvent objects are considered equal", event1.equals(event2));
    }

    @Test
    public void testGetAndSetRemoteHost() {
        AccessEvent event = new AccessEvent();
        String expectedRemoteHost = "testHost";
        event.setRemoteHost(expectedRemoteHost);
        String actualRemoteHost = event.getRemoteHost();
        assertEquals("remote host is not set correctly", expectedRemoteHost, actualRemoteHost);

        event.setRemoteHost("differentHost");
        String actualRemoteHost2 = event.getRemoteHost();
        assertNotEquals("remote host is not updated correctly", expectedRemoteHost, actualRemoteHost2);
    }

    @Test
    public void testGetAndSetStatusCode() {
        AccessEvent event = new AccessEvent();
        int expectedStatusCode = 200;
        event.setStatusCode(expectedStatusCode);
        int actualStatusCode = event.getStatusCode();
        assertEquals("status code is not set correctly", expectedStatusCode, actualStatusCode);

        event.setStatusCode(404);
        int actualStatusCode2 = event.getStatusCode();
        assertNotEquals("status code is not updated correctly", expectedStatusCode, actualStatusCode2);
    }

    @Test
    public void testGetAndSetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> expectedRequestParameters = new HashMap<>();
        event.setRequestParameters(expectedRequestParameters);
        Map<String, String[]> actualRequestParameters = event.getRequestParameters();
        assertEquals("request parameters are not set correctly", expectedRequestParameters, actualRequestParameters);

        Map<String, String[]> newRequestParameters = new HashMap<>();
        newRequestParameters.put("testKey", new String[]{"testValue1", "testValue2"});
        event.setRequestParameters(newRequestParameters);
        Map<String, String[]> actualRequestParameters2 = event.getRequestParameters();
        assertNotEquals("request parameters are not updated correctly", expectedRequestParameters, actualRequestParameters2);
    }

    // Add more test cases for other methods and edge cases if necessary
}
