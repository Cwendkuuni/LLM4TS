package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testConstructors() {
        // Default constructor
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        // Constructor with page number
        Paging paging1 = new Paging(1);
        assertEquals(1, paging1.getPage());
        assertEquals(-1, paging1.getCount());
        assertEquals(-1L, paging1.getSinceId());
        assertEquals(-1L, paging1.getMaxId());

        // Constructor with sinceId
        Paging paging2 = new Paging(1L);
        assertEquals(-1, paging2.getPage());
        assertEquals(-1, paging2.getCount());
        assertEquals(1L, paging2.getSinceId());
        assertEquals(-1L, paging2.getMaxId());

        // Constructor with page number and count
        Paging paging3 = new Paging(1, 10);
        assertEquals(1, paging3.getPage());
        assertEquals(10, paging3.getCount());
        assertEquals(-1L, paging3.getSinceId());
        assertEquals(-1L, paging3.getMaxId());

        // Constructor with page number, count, sinceId
        Paging paging4 = new Paging(1, 10, 1L);
        assertEquals(1, paging4.getPage());
        assertEquals(10, paging4.getCount());
        assertEquals(1L, paging4.getSinceId());
        assertEquals(-1L, paging4.getMaxId());

        // Constructor with page number, count, sinceId, maxId
        Paging paging5 = new Paging(1, 10, 1L, 2L);
        assertEquals(1, paging5.getPage());
        assertEquals(10, paging5.getCount());
        assertEquals(1L, paging5.getSinceId());
        assertEquals(2L, paging5.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging(1, 10, 1L, 2L);

        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());

        paging.setPage(2);
        assertEquals(2, paging.getPage());

        paging.setCount(5);
        assertEquals(5, paging.getCount());

        paging.setSinceId(3L);
        assertEquals(3L, paging.getSinceId());

        paging.setMaxId(4L);
        assertEquals(4L, paging.getMaxId());
    }

    @Test
    public void testEqualsAndHashcode() {
        Paging paging1 = new Paging(1, 10, 1L, 2L);
        Paging paging2 = new Paging(1, 10, 1L, 2L);
        Paging paging3 = new Paging(1, 11, 1L, 2L);
        Paging paging4 = new Paging(1, 10, 1L, 3L);

        assertEquals(paging1, paging1);
        assertEquals(paging1, paging2);
        assertNotSame(paging1, paging3);
        assertNotSame(paging1, paging4);

        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
        assertNotEquals(paging1.hashCode(), paging4.hashCode());
    }

    @Test
    public void testAsPostParameterListAndArray() {
        Paging paging = new Paging(1, 10, 1L, 2L);

        List<HttpParameter> list = paging.asPostParameterList(Paging.SMCP, "count");
        HttpParameter[] array = paging.asPostParameterArray(Paging.SMCP, "count");

        assertEquals(4, list.size());
        assertEquals("page", list.get(0).getName());
        assertEquals("1", list.get(0).getValue());
        assertEquals("count", list.get(1).getName());
        assertEquals("10", list.get(1).getValue());
        assertEquals("since_id", list.get(2).getName());
        assertEquals("1", list.get(2).getValue());
        assertEquals("max_id", list.get(3).getName());
        assertEquals("2", list.get(3).getValue());

        assertEquals(4, array.length);
        assertEquals("page", array[0].getName());
        assertEquals("1", array[0].getValue());
        assertEquals("count", array[1].getName());
        assertEquals("10", array[1].getValue());
        assertEquals("since_id", array[2].getName());
        assertEquals("1", array[2].getValue());
        assertEquals("max_id", array[3].getName());
        assertEquals("2", array[3].getValue());
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> list = paging.asPostParameterList(Paging.SMCP, "count");

        paging.addPostParameter(Paging.SMCP, 'p', list, "page", 1);
        assertEquals(1, list.size());
        assertEquals("page", list.get(0).getName());
        assertEquals("1", list.get(0).getValue());

        paging.addPostParameter(Paging.SMCP, 's', list, "since_id", 1L);
        assertEquals(2, list.size());
        assertEquals("page", list.get(0).getName());
        assertEquals("1", list.get(0).getValue());
        assertEquals("since_id", list.get(1).getName());
        assertEquals("1", list.get(1).getValue());

        paging.addPostParameter(Paging.SMCP, 'c', list, "count", 10);
        assertEquals(3, list.size());
        assertEquals("page", list.get(0).getName());
        assertEquals("1", list.get(0).getValue());
        assertEquals("since_id", list.get(1).getName());
        assertEquals("1", list.get(1).getValue());
        assertEquals("count", list.get(2).getName());
        assertEquals("10", list.get(2).getValue());

        paging.addPostParameter(Paging.SMCP, 'm', list, "max_id", 2L);
        assertEquals(4, list.size());
        assertEquals("page", list.get(0).getName());
        assertEquals("1", list.get(0).getValue());
        assertEquals("since_id", list.get(1).getName());
        assertEquals("1", list.get(1).getValue());
        assertEquals("count", list.get(2).getName());
        assertEquals("10", list.get(2).getValue());
        assertEquals("max_id", list.get(3).getName());
        assertEquals("2", list.get(3).getValue());
    }

    @Test
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();

        List<HttpParameter> list = paging.asPostParameterList(Paging.SMCP, "count");
        assertThrows(IllegalStateException.class, () -> paging.addPostParameter(Paging.SMCP, 'x', list, "x", 1));
    }

    @Test
    public void testAsPostParameterListAndArrayWithNullSupportedParams() {
        Paging paging = new Paging();

        List<HttpParameter> list = paging.asPostParameterList(null, "count");
        assertNull(list);

        HttpParameter[] array = paging.asPostParameterArray(null, "count");
        assertNull(array);
    }

    @Test
    public void testAsPostParameterListAndArrayWithEmptySupportedParams() {
        Paging paging = new Paging();

        List<HttpParameter> list = paging.asPostParameterList(new char[0], "count");
        assertEquals(0, list.size());
        HttpParameter[] array = paging.asPostParameterArray(new char[0], "count");
        assertEquals(0, array.length);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        Paging paging = new Paging(1, 10, 1L, 2L);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(paging);
        oos.close();

        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Paging deserializedPaging = (Paging) ois.readObject();
        ois.close();

        assertEquals(paging, deserializedPaging);
    }
}