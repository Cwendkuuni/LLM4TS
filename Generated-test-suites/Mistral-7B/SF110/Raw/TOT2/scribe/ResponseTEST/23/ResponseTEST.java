package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoJUnitRunner;
import java.io.*;

@RunWith(MockitoJUnitRunner.class)
public class ResponseTest {

    private static final String EMPTY = "";

    @Test
    public void testGetBody() {
        String expectedBody = "Expected body";
        Response response = new Response(new URL("http://test.com").openConnection());
        response.parseBodyContents();
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void testGetStream() {
        ByteArrayInputStream expectedStream = new ByteArrayInputStream("Expected stream".getBytes());
        Response response = new Response(new URL("http://test.com").openConnection());
        response.stream = expectedStream;

        InputStream actualStream = response.getStream();
        assertSame(expectedStream, actualStream);
    }

    @Test
    public void testGetCode() {
        Response response = new Response(new URL("http://test.com").openConnection());
        response.code = 200;

        int actualCode = response.getCode();
        assertEquals(200, actualCode);
    }

    @Test
    public void testGetHeaders() {
        // Implement the test case for getHeaders() method here
    }

    @Test
    public void testGetHeader() {
        // Implement the test case for getHeader() method here
    }

    @Test
    public void testGetBody_EmptyBody() {
        Response response = new Response(mock(HttpURLConnection.class).mock());
        when(mockStream.available()).thenReturn(0);
        when(mockStream.readAllBytes()).thenReturn(new byte[0]);

        String actualBody = response.getBody();
        assertEquals(Response.EMPTY, actualBody);
    }

    @Test
    public void testGetBody_NullBody() {
        Response response = new Response(mock(HttpURLConnection.class).mock());
        response.body = null;

        String actualBody = response.getBody();
        assertNull(actualBody);
    }

    @Test(expected = IOException.class)
    public void testGetBody_ExceptionWhenParsingBody() throws IOException {
        Response response = new Response(mock(HttpURLConnection.class).mock());
        when(mockStream.available()).thenReturn(1024);
        when(mockStream.readAllBytes()).thenThrow(new IOException("Test error"));

        String actualBody = response.getBody();
    }

    @Test
    public void testGetStream_NullStream() {
        Response response = new Response(mock(HttpURLConnection.class).mock());
        response.stream = null;

        InputStream actualStream = response.getStream();
        assertNull(actualStream);
    }

    @Test(expected = IOException.class)
    public void testGetStream_ExceptionWhenGettingStream() throws IOException {
        Response response = new Response(mock(HttpURLConnection.class).mock());
        when(mockConnection.getInputStream()).thenThrow(new IOException("Test error"));

        InputStream actualStream = response.getStream();
    }

    @Test
    public void testGetCode_NegativeCode() {
        Response response = new Response(mock(HttpURLConnection.class).mock());
        response.code = -1;

        int actualCode = response.getCode();
        assertEquals(-1, actualCode);
    }

    @Test
    public void testGetCode_NonIntegerCode() {
        Response response = new Response(mockConnection);
        response.code = "Not an integer";

        int actualCode = response.getCode();
        assertEquals(-1, actualCode);
    }

    @Test(expected = IOException.class)
    public void testGetCode_ExceptionWhenGettingCode() throws IOException {
        Response response = new Response(mockConnection);
        when(mockConnection.getResponseCode()).thenThrow(new IOException("Test error"));

        int actualCode = response.getCode();
    }
}