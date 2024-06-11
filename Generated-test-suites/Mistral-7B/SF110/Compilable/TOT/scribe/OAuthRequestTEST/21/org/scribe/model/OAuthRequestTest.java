package org.scribe.model;
import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstructor() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com/resource";
        OAuthRequest request = new OAuthRequest(verb, url);
        Assert.assertEquals(verb, request.getVerb());
        Assert.assertEquals(url, request.getUrl());
        Assert.assertNotNull(request.getOauthParameters());
        Assert.assertEquals(0, request.getOauthParameters().size());
        
    }
}