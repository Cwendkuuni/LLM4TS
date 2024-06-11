package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.Paging;

public class PagingTest {

    @Test
    public void testGetPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPageNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test
    public void testGetCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCountNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSinceIdNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging(1, 10, 1L, 10L);
        assertEquals(10L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }
}