package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.Arrays;
import java.util.List;

public class PagingTest {

    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(1L);
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertEquals(1, paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertNull(paging.getCount());
        assertEquals(1, paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceId() {
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1, paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1, paging.getSinceId());
        assertEquals(2, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(10);
        paging.setSinceId(1L);
        paging.setMaxId(2L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1, paging.getSinceId());
        assertEquals(2, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithNegativeValues() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithNegativeSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        HttpParameter[] expected = new HttpParameter[]{
            new HttpParameter("page", "1"),
            new HttpParameter("count", "10"),
            new HttpParameter("since_id", "1"),
            new HttpParameter("max_id", "2")
        };
        HttpParameter[] result = paging.asPostParameterArray(Paging.SMCP, "count");
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> expected = Arrays.asList(
            new HttpParameter("page", "1"),
            new HttpParameter("count", "10"),
            new HttpParameter("since_id", "1"),
            new HttpParameter("max_id", "2")
        );
        List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(expected, result);
    }

    @Test
    public void testAsPostParameterArrayWithUnsupportedParam() {
        Paging paging = new Paging();
        assertThrows(IllegalStateException.class, () -> paging.asPostParameterArray(Paging.S, "unsupportedParam"));
    }

    @Test
    public void testAsPostParameterListWithUnsupportedParam() {
        Paging paging = new Paging();
        assertThrows(IllegalStateException.class, () -> paging.asPostParameterList(Paging.S, "unsupportedParam"));
    }
}