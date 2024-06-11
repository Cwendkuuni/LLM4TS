package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;

public class PagingTest {
    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());

        Paging pagingWithPage = new Paging(1);
        assertEquals(1, pagingWithPage.getPage());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());

        try {
            paging.setPage(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getMaxId());

        Paging pagingWithMaxId = new Paging(null, null, 1L, 2L);
        assertEquals(2L, pagingWithMaxId.getMaxId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(1L);
        assertEquals(1L, paging.getMaxId());

        try {
            paging.setMaxId(-1L);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCount() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getCount());

        Paging pagingWithCount = new Paging(1);
        Paging pagingWithCount3 = new Paging(1, 3);
        assertEquals(1, pagingWithCount.getCount());
        assertEquals(3, pagingWithCount3.getCount());
    }

    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getSinceId());

        Paging pagingWithSinceId = new Paging(null, 1L);
        Paging pagingWithSinceId1 = new Paging(null, 1L, 1);
        assertEquals(1L, pagingWithSinceId.getSinceId());
        assertEquals(1L, pagingWithSinceId1.getSinceId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 1, 1, 1);
        Paging paging2 = new Paging(1, 1, 1, 1);
        Paging paging3 = new Paging(1, 1, 2, 1);
        Paging paging4 = new Paging(1, 2, 1, 1);

        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
        assertFalse(paging1.equals(paging4));
    }
}