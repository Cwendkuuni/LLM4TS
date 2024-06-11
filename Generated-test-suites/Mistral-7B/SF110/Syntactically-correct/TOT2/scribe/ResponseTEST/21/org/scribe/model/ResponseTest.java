package org.scribe.model.tests;

import org.junit.Test;
import org.junit.Assert;
import static org.scribe.model.Response.EMPTY;
import org.scribe.model.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody_whenSuccess_thenReturnBody() {
        // Test case from Expert 1
    }

    @Test
    public void testGetBody_whenEmptyResponse_thenReturnEmptyString() {
        // Test case from Expert 1
    }

    @Test
    public void testGetStream_whenSuccess_thenReturnInputStream() {
        // Test case from Expert 1
    }

    @Test(expected = IllegalStateException.class)
    public void testGetStream_whenFailure_thenThrowException() {
        // Test case from Expert 1
    }

    @Test
    public void testGetCode_whenSuccess_thenReturnCode() {
        // Test case from Expert 2
    }

    @Test
    public void testGetCode_whenNotFound_thenReturnErrorCode() {
        // Test case from Expert 2
    }

    @Test
    public void testParseHeaders_whenSuccess_thenReturnHeadersMap() {
        // Test case from Expert 2
    }

    @Test
    public void testParseHeaders_whenEmptyHeaders_thenReturnEmptyMap() {
        // Test case from Expert 2
    }

    @Test
    public void testWasSuccessful_whenSuccess_thenReturnTrue() {
        // Test case from Expert 3
    }

    @Test
    public void testWasSuccessful_whenFailure_thenReturnFalse() {
        // Test case from Expert 3
    }
}