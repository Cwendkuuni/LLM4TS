package twitter4j.tests;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.align.Align;

public class OEmbedRequestTest {

@Test
public void testConstructor() {
    long statusId = 12345L;
    String url = "https://example.com/status/1";
    OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

    assertEquals(statusId, oEmbedRequest.getStatusId());
    assertEquals(url, oEmbedRequest.getUrl());
    assertFalse(oEmbedRequest.isHideMedia());
    assertFalse(oEmbedRequest.isHideThread());
    assertFalse(oEmbedRequest.isOmitScript());
    assertNull(oEmbedRequest.getAlign());
    assertArrayEquals(new String[0], oEmbedRequest.getRelated());
    assertNull(oEmbedRequest.getLang());
}

@Test
public void testSetAndGetMaxWidth() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
    oEmbedRequest.setMaxWidth(200);
    oEmbedRequest.MaxWidth(300);

    assertEquals(300, oEmbedRequest.getMaxWidth());
}

@Test
public void testBooleanMethods() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");

    assertFalse(oEmbedRequest.isHideMedia());
    assertFalse(oEmbedRequest.isHideThread());
    assertFalse(oEmbedRequest.isOmitScript());

    oEmbedRequest.setHideMedia(true);
    oEmbedRequest.HideMedia(false);

    assertTrue(oEmbedRequest.isHideMedia());
    assertFalse(oEmbedRequest.isHideMedia());

    oEmbedRequest.setHideThread(true);
    oEmbedRequest.HideThread(false);

    assertTrue(oEmbedRequest.isHideThread());
    assertFalse(oEmbedRequest.isHideThread());

    oEmbedRequest.setOmitScript(true);
    oEmbedRequest.omitScript(false);

    assertTrue(oEmbedRequest.isOmitScript());
    assertFalse(oEmbedRequest.isOmitScript());
}
}