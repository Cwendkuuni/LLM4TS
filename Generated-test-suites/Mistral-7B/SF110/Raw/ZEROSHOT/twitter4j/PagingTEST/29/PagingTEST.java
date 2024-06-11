package com.example.twitter4j.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.Paging;

public class PagingTest {

    @Test
    public void testNullObject() {
        assertNull(new Paging());
    }

    @Test
    public void testEmptyConstructor() {
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
        assertEquals(1L, paging.getSinceId());
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
        assertEquals(1L, paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithAllParams() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals(1, paging.getPage());
        paging.setPage(2);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        paging.setCount(20);
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        paging.setSinceId(2L);
        assertEquals(2L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
        paging.setMaxId(200L);
        assertEquals(200L, paging.getMaxId());
    }

    @Test
    public void testEqualsAndHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        Paging paging3 = new Paging(1, 10, 1L, 101L);
        Paging paging4 = new Paging(2, 10, 1L, 100L);
        assertEquals(paging1, paging2);
        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1, paging3);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
        assertNotEquals(paging1, paging4);
        assertNotEquals(paging1.hashCode(), paging4.hashCode());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] params = paging.asPostParameterArray();
        assertNotNull(params);
        assertEquals(4, params.length);
        assertEquals("page", params[0].getName());
        assertEquals("1", params[0].getValue());
        assertEquals("count", params[1].getName());
        assertEquals("10", params[1].getValue());
        assertEquals("since_id", params[2].getName());
        assertEquals("1", params[2].getValue());
        assertEquals("max_id", params[3].getName());
        assertEquals("100", params[3].getValue());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> params = paging.asPostParameterList();
        assertNotNull(params);
        assertEquals(4, params.size());
        assertEquals("page", params.get(0).getName());
        assertEquals("1", params.get(0).getValue());
        assertEquals("count", params.get(1).getName());
        assertEquals("10", params.get(1).getValue());
        assertEquals("since_id", params.get(2).getName());
        assertEquals("1", params.get(2).getValue());
        assertEquals("max_id", params.get(3).getName());
        assertEquals("100", params.get(3).getValue());
    }

    @Test
    public void testAsPostParameterListWithNullParameters() {
        Paging paging = new Paging();
        List<HttpParameter> params = paging.asPostParameterList();
        assertNotNull(params);
        assertEquals(0, params.size());
    }

    @Test
    public void testAsPostParameterArrayWithNullParameters() {
        Paging paging = new Paging();
        HttpParameter[] params = paging.asPostParameterArray();
        assertNotNull(params);
        assertEquals(0, params.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> params = new ArrayList<>();
        paging.addPostParameter(new char[] {'s', 'u'}, 'u', params, "unsupported", -1L);
    }
}