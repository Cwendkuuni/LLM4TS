package org.scribe.model;
import org.junit.Test;
import org.scribe.model.Request;
import static org.junit.Assert.assertNotNull;

public class RequestTest {

    @Test
    public void testRequestConstruction() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertNotNull(request);
    }
}