package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebServiceDescriptorTest {

	@Test
	public void testWebServiceDescriptorWithValidArguments() {
		Class serviceClazz = Mockito.mock(Class.class);
		String targetNamespace = "targetNamespace";
		String typesNamespace = "typesNamespace";

		WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

		assertNotNull(webServiceDescriptor);
		assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
		assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
		assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
	}

	@Test
	public void testWebServiceDescriptorWithNullServiceClazz() {
		String targetNamespace = "targetNamespace";
		String typesNamespace = "typesNamespace";

		WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, typesNamespace);

		assertNull(webServiceDescriptor.getServiceClazz());
	}

	@Test
	public void testWebServiceDescriptorWithNullTargetNamespace() {
		Class serviceClazz = Mockito.mock(Class.class);
		String typesNamespace = "typesNamespace";

		WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, typesNamespace);

		assertNull(webServiceDescriptor.getTargetNamespace());
	}

	@Test
	public void testWebServiceDescriptorWithNullTypesNamespace() {
		Class serviceClazz = Mockito.mock(Class.class);
		String targetNamespace = "targetNamespace";

		WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, null);

		assertNull(webServiceDescriptor.getTypesNamespace());
	}

	@Test
	public void testWebServiceDescriptorHashCode() {
		Class serviceClazz1 = Mockito.mock(Class.class);
		Class serviceClazz2 = Mockito.mock(Class.class);
		String targetNamespace = "targetNamespace";
		String typesNamespace = "typesNamespace";

		WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
		WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

		assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());

		webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
		webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, null, typesNamespace);

		assertNotSame(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
	}

	@Test
	public void testWebServiceDescriptorEqual() {
		Class serviceClazz1 = Mockito.mock(Class.class);
		Class serviceClazz2 = Mockito.mock(Class.class);
		String targetNamespace = "targetNamespace";
		String typesNamespace = "typesNamespace";

		WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
		WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

		assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));

		webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
		WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz2, "targetNamespace", typesNamespace);

		assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));

		webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
		WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(null, targetNamespace, typesNamespace);

		assertFalse(webServiceDescriptor1.equals(webServiceDescriptor4));
	}

	@Test
	public void testWebServiceDescriptorEqualWithNull() {
		Class serviceClazz = Mockito.mock(Class.class);
		String targetNamespace = "targetNamespace";
		String typesNamespace = "typesNamespace";

		WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

		assertFalse(webServiceDescriptor.equals(null));
	}

	@Test
	public void testWebServiceDescriptorWithDifferentClasses() {
		Class serviceClazz1 = Mockito.mock(Class.class);
		Class serviceClazz2 = Mockito.mock(Class.class);
		String targetNamespace = "targetNamespace";
		String typesNamespace = "typesNamespace";

		WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
		WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

		assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
	}
}