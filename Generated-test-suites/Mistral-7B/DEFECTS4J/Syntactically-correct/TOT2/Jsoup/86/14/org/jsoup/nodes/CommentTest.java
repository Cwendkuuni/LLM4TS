package org.junit.tests.junit4.junit4_test_cases.com.example.junit4testcases.node;

import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;

public class CommentTest {
    @BeforeClass
    public static void setUpBeforeClass() {
        // Setup before tests run
    }

    @AfterClass
    public static void tearDownAfterClass() {
        // Teardown after tests run
    }

    @Before
    public void setUp() {
        // Setup before each test runs
    }

    @After
    public void tearDown() {
        // Teardown after each test runs
    }

    @Test
    public void testCommentConstruction() {
        String data = "This is a test comment.";
        Comment comment = new Comment(data);
        String commentValue = comment.getData();

        // Basic test: Check that the comment value matches the constructor argument
        assert (commentValue.equals(data));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommentConstructionWithNullData() {
        String data = null;
        Comment comment = new Comment(data); // Should throw IllegalArgumentException
    }

    @Test
    public void testCommentConstructionWithEmptyString() {
        String data = "";
        Comment comment = new Comment(data);
        String commentValue = comment.getData();

        // Edge case: Check that an empty string returns an empty comment
        assertEquals("", commentValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedCommentConstruction() {
        String data = "Test comment";
        Comment comment = new Comment(data, null); // Should throw an IllegalArgumentException
    }

    @Test
    public void testNodeNameForEmptyString() {
        Comment comment = new Comment("");
        String nodeName = comment.nodeName();

        // Edge case: Check that an empty comment returns the expected node name
        assertEquals("#comment", nodeName);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataForNullComment() {
        Comment comment = null;
        String commentValue = comment.getData(); // Should throw NullPointerException
    }

    @Test
    public void testIsXmlDeclarationForEmptyComment() {
        Comment comment = new Comment("");
        boolean isXmlDeclaration = comment.isXmlDeclaration();

        // Edge case: Check that an empty comment does not return as an XML declaration
        assertFalse(isXmlDeclaration);
    }

    @Test
    public void testIsXmlDeclarationForXmlDeclarationComment() {
        String xmlDeclarationData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlDeclarationData);
        boolean isXmlDeclaration = comment.isXmlDeclaration();

        // Test case: Check that an XML declaration comment returns as an XML declaration
        assertTrue(isXmlDeclaration);
    }

    @Test
    public void testAsXmlDeclarationForXmlDeclarationComment() {
        String xmlDeclarationData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlDeclarationData);
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();

        // Test case: Check that an XML declaration comment can be parsed correctly
        assertNotNull(xmlDeclaration);
    }

    @Test
    public void testAsXmlDeclarationForNonXmlDeclarationComment() {
        String commentData = "This is a test comment.";
        Comment comment = new Comment(commentData);
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();

        // Test case: Check that a non-XML declaration comment returns null
        assertNull(xmlDeclaration);
    }

    @After
    public void tearDown() {
        // Teardown after each test runs
    }
}