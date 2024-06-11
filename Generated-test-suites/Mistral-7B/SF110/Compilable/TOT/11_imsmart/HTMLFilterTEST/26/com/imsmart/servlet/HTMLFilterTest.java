package com.imsmart.servlet;
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
    
     @Test
     public void testFilterSimpleMessage() {
         String message = "This is a simple message.";
         String expected = "This is a simple message.";
         String actual = HTMLFilter.filter(message);
         assertEquals(expected, actual);
     }

     //...

     @Test
     public void testFilterSpecialCharacters() {
         String message = "<html><head><title>&quot;Test&quot; Title</title></head><body>&amp;<img src=&quot;image.jpg&quot; alt=&gt;Image&lt;/img>&amp;<br /></body></html>";
         String expected = "&lt;html&gt;&lt;head&gt;&lt;title&gt;&#34;Test&quot; Title&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&amp;<img src=&quot;image.jpg&quot; alt=&gt;Image&lt;/img>&amp;<br /&gt;&lt;/body&gt;&lt;/html&gt;";
         String actual = HTMLFilter.filter(message);
         assertEquals(expected, actual);
     }

     @Test(expected = NullPointerException.class)
     public void testFilterNullMessage() {
         String message = null;
         HTMLFilter.filter(message);
     }

     
}