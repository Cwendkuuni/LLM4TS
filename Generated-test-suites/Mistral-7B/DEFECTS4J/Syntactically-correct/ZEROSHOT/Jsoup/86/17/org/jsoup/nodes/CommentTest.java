package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test class for Comment class
 */
public class CommentTest {

    private Comment comment;

    @Before
    public void setup() {
        comment = new Comment("This is a comment");
    }

    @Test
    public void testCommentConstruction() {
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentNodeName() {
        Assert.assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Assert.assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclarationNull() {
        Assert.assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclarationValidDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment xmlComment = new Comment(xmlDeclaration);
        XmlDeclaration xmlDeclarationObj = xmlComment.asXmlDeclaration();

        Assert.assertThat(xmlDeclarationObj, is(instanceOf(XmlDeclaration.class)));
        Assert.assertThat(xmlDeclarationObj.version(), is("1.0"));
        Assert.assertThat(xmlDeclarationObj.encoding(), is("UTF-8"));
    }
}