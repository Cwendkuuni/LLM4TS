package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Modifier;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilderDefaultBehavior() throws JsonMappingException {
        ObjectNode node = mapper().createObjectNode();

        PropertyBuilder builder = new PropertyBuilder(SerializationConfigFactory.getEmpty(), BeanDescription.createIntrospectAndBind(MyBean.class));

        BeanPropertyWriter writer = builder.buildWriter(mapper().getSerializerProvider(), null, null, null, null, null, null, false);

        writer.serializeAsField(node, "name");

        Assert.assertEquals(node.get("name"), null);
    }

    @Test
    public void testPropertyBuilderIncludeNonDefault() throws JsonMappingException {
        ObjectNode node = mapper().createObjectNode();

        PropertyBuilder builder = new PropertyBuilder(SerializationConfigFactory.getEmpty(), BeanDescription.createIntrospectAndBind(MyBean.class));
        builder._defaultInclusion = JsonInclude.Value.STANDARD_EXCLUDE;

        MyBean bean = new MyBean();
        Object defaultBean = builder.getDefaultBean();
        if (defaultBean != null) {
            bean = (MyBean) ReflectUtil.setAccessible(BeanPropertyWriter.class, "defaultBean", builder, defaultBean);
        }

        BeanPropertyWriter writer = builder.buildWriter(mapper().getSerializerProvider(), null, null, null, null, null, null, false);

        writer.serializeAsField(node, "name");

        Assert.assertEquals(node.get("name"), null);

        builder._defaultBean = bean;

        writer = builder.buildWriter(mapper().getSerializerProvider(), null, null, null, null, null, null, false);

        writer.serializeAsField(node, "name");

        Assert.assertEquals(node.get("name"), "defaultValue");
    }

    @Test
    public void testPropertyBuilderIncludeNonEmpty() throws JsonMappingException {
        ObjectNode node = mapper().createObjectNode();

        PropertyBuilder builder = new PropertyBuilder(SerializationConfigFactory.getEmpty(), BeanDescription.createIntrospectAndBind(MyBean.class));
        builder._defaultInclusion = JsonInclude.Value.STANDARD_EXCLUDE;

        MyBean bean = new MyBean();

        BeanPropertyWriter writer = builder.buildWriter(mapper().getSerializerProvider(), null, null, null, null, null, null, false);

        writer.serializeAsField(node, "list");

        Assert.assertEquals(node.size(), 0);

        bean.list = new ArrayList<>();

        writer = builder.buildWriter(mapper().getSerializerProvider(), null, null, null, null, null, null, false);

        writer.serializeAsField(node, "list");

        Assert.assertEquals(node.size(), 0);

        bean.list.add(new MyInnerBean());

        writer = builder.buildWriter(mapper().getSerializerProvider(), null, null, null, null, null, null, false);

        writer.serializeAsField(node, "list");

        Assert.assertEquals(node.size(), 1);
    }

    @Test
    public void testPropertyBuilderIncludeCustom() throws JsonMappingException {
        ObjectNode node = mapper().createObjectNode();

        PropertyBuilder builder = new PropertyBuilder(SerializationConfigFactory.getEmpty(), BeanDescription.createIntrospectAndBind(MyBean.class));
        builder._defaultInclusion = JsonInclude.Value.STANDARD_EXCLUDE;

        MyBean bean = new MyBean();

        MyCustomInclude include = new MyCustomInclude();

        BeanPropertyWriter writer = builder.buildWriter(mapper().getSerializerProvider(), null, null, null, null, null, null, false);

        writer.setFinderFactory(new CustomIncludeFinderFactory(include));

        writer.serializeAsField(node, "name");

        Assert.assertEquals(node.get("name"), "defaultValue");

        bean.name = "newValue";

        writer = builder.buildWriter(mapper().getSerializerProvider(), null, null, null, null, null, null, false);

        writer.serializeAsField(node, "name");

        Assert.assertEquals(node.get("name"), "newValue");
    }

    @Test
    public void testPropertyBuilderNullSerialization() throws JsonMappingException {
        ObjectNode node = mapper().createObjectNode();

        PropertyBuilder builder = new PropertyBuilder(SerializationConfigFactory.getEmpty(), BeanDescription.createIntrospectAndBind(MyBean.class));

        BeanPropertyWriter writer = builder.buildWriter(mapper().getSerializerProvider(), null, null, null, null, null, null, false);

        writer.setNullSerializer(NullSerializer.instance);

        writer.serializeAsField(node, "name");

        Assert.assertEquals(node.get("name"), null);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class MyBean {
        String name;
        List<MyInnerBean> list;

        public MyBean() {}
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class MyInnerBean {
    }

    static class MyCustomInclude implements JsonInclude.Value {
        @Override
        public JsonInclude.Include getValueInclusion() {
            return JsonInclude.Include.NON_EMPTY;
        }
    }

    static class CustomIncludeFinderFactory implements BeanPropertyWriter.FinderFactory {

        private final JsonInclude.Value _value;

        public CustomIncludeFinderFactory(JsonInclude.Value value) {
            _value = value;
        }

        @Override
        public JsonInclude.Value findValue() {
            return _value;
        }
    }

    static ObjectMapper mapper() {
        return new ObjectMapper();
    }
}