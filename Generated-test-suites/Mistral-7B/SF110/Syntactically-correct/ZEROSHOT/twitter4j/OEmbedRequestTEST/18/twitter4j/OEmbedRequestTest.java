package twitter4j.test;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        long statusId = 1L;
        String url = "https://example.com";
        this.oEmbedRequest = new OEmbedRequest(statusId, url);
    }

    @Test
    public void testOEmbedRequestConstruction() {
        assertNotNull(this.oEmbedRequest);
        assertEquals(1L, this.oEmbedRequest.getStatusId());
        assertEquals(url, this.oEmbedRequest.getUrl());
        assertTrue(this.oEmbedRequest.isHideMedia());
        assertTrue(this.oEmbedRequest.isHideThread());
        assertFalse(this.oEmbedRequest.isOmitScript());
        assertNull(this.oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], this.oEmbedRequest.getRelated());
        assertNull(this.oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        this.oEmbedRequest.setMaxWidth(100);
        assertEquals(100, this.oEmbedRequest.getMaxWidth());

        this.oEmbedRequest.HideMedia(false);
        assertFalse(this.oEmbedRequest.isHideMedia());

        this.oEmbedRequest.HideThread(false);
        assertFalse(this.oEmbedRequest.isHideThread());

        this.oEmbedRequest.omitScript(true);
        assertTrue(this.oEmbedRequest.isOmitScript());

        OEmbedRequest.Align align = OEmbedRequest.Align.LEFT;
        this.oEmbedRequest.align(align);
        assertEquals(align, this.oEmbedRequest.getAlign());

        String[] related = new String[]{"related1", "related2"};
        this.oEmbedRequest.related(related);
        assertArrayEquals(related, this.oEmbedRequest.getRelated());

        this.oEmbedRequest.setLang("en");
        assertEquals("en", this.oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] params = this.oEmbedRequest.asHttpParameterArray();
        List<String> names = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        for (int i = 0; i < params.length; i++) {
            assertEquals(names.get(i), params[i].getName());
            if (params[i] instanceof StringHttpParameter) {
                StringHttpParameter stringParam = (StringHttpParameter) params[i];
                if (params[i].getName().equals("related")) {
                    List<String> relatedValues = Arrays.asList(stringParam.getValue().split(","));
                    assertArrayEquals(this.oEmbedRequest.getRelated(), relatedValues.toArray(new String[0]));
                } else {
                    assertEquals(String.valueOf(this.oEmbedRequest.getClass().getField(params[i].getName()).get(this.oEmbedRequest)), stringParam.getValue());
                }
            }
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId2 = 2L;
        String url2 = "https://example.com/2";
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId2, url2);

        assertEquals(this.oEmbedRequest, this.oEmbedRequest);
        assertEquals(this.oEmbedRequest, this.oEmbedRequest);
        assertNotEquals(this.oEmbedRequest, null);
        assertNotEquals(this.oEmbedRequest, new Object());
        assertNotEquals(this.oEmbedRequest, oEmbedRequest2);

        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(this.oEmbedRequest);
        assertEquals(this.oEmbedRequest, oEmbedRequest3);

        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(statusId, url);
        assertEquals(this.oEmbedRequest, oEmbedRequest4);

        oEmbedRequest4.setMaxWidth(100);
        assertNotEquals(this.oEmbedRequest, oEmbedRequest4);

        oEmbedRequest4.setHideMedia(false);
        assertNotEquals(this.oEmbedRequest, oEmbedRequest4);

        oEmbedRequest4.setHideThread(false);
        assertNotEquals(this.oEmbedRequest, oEmbedRequest4);

        oEmbedRequest4.setOmitScript(true);
        assertNotEquals(this.oEmbedRequest, oEmbedRequest4);

        oEmbedRequest4.setAlign(OEmbedRequest.Align.LEFT);
        assertNotEquals(this.oEmbedRequest, oEmbedRequest4);

        String[] relatedArray = {"related1", "related2"};
        oEmbedRequest4.setRelated(relatedArray);
        assertNotEquals(this.oEmbedRequest, oEmbedRequest4);

        oEmbedRequest4.setLang("en");
        assertNotEquals(this.oEmbedRequest, oEmbedRequest4);

        oEmbedRequest4.setUrl(url2);
        assertNotEquals(this.oEmbedRequest, oEmbedRequest4);

        oEmbedRequest4.setStatusId(statusId2);
        assertNotEquals(this.oEmbedRequest, oEmbedRequest4);

        oEmbedRequest4.setMaxWidth(100);
        oEmbedRequest4.setHideMedia(false);
        oEmbedRequest4.setHideThread(false);
        oEmbedRequest4.setOmitScript(true);
        oEmbedRequest4.setAlign(OEmbedRequest.Align.LEFT);
        oEmbedRequest4.setRelated(relatedArray);
        oEmbedRequest4.setLang("en");
        oEmbedRequest4.setUrl(url);
        oEmbedRequest4.setStatusId(statusId);

        assertEquals(this.hashCode(), oEmbedRequest4.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("OEmbedRequest{statusId=1, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}", this.oEmbedRequest.toString());
    }
}