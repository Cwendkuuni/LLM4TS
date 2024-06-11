import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

@SuppressWarnings("unchecked")
public class BeanConfigurationTest {

	private BeanConfiguration beanConfiguration;
	private SensitivePropertyHandler sensitivePropertyHandler;
	private CloudMetadataClient cloudMetadataClient;

	@Before
	public void setUp() {
		beanConfiguration = new BeanConfiguration();
		sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
		cloudMetadataClient = mock(CloudMetadataClient.class);
		beanConfiguration.cloudMetadataClient = cloudMetadataClient;
	}

	@Test
	public void testPasswordEncoder() {
		PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
		String encodedPassword = passwordEncoder.encode("password");
		assertTrue(passwordEncoder.matches("password", encodedPassword));
	}

	@Test
	public void testObjectMapper() throws Exception {
		CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
				.addSerializer(Internationalizable.class, new I18nOutputSerializer());
		ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
				.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
				.registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

		ObjectMapper actualObjectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
		assertEquals(objectMapper.getSerializerProvider(), actualObjectMapper.getSerializerProvider());
	}

	@Test
	public void testDefaultHttpFirewall() {
		HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
		assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
		assertTrue(defaultHttpFirewall.getAllowUrlEncodedSlash());
	}

	@Test
	public void testCloudMetadataClient() {
		when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
		CloudMetadataClient actualCloudMetadataClient = beanConfiguration.cloudMetadataClient();
		assertEquals(cloudMetadataClient, actualCloudMetadataClient);
	}

	@Test
	public void testCookieSerializer() {
		CookieSerializer serializer = beanConfiguration.cookieSerializer();
		assertEquals("JSESSIONID", serializer.getCookieName());
	}

	@Test
	public void testNullCloudMetadataClient() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
		assertFalse(nullCloudMetadataClient.supportsTenantInstance());
		assertFalse(nullCloudMetadataClient.needsOBTenantName());
		assertFalse(nullCloudMetadataClient.needsSysTenantUser());
		assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
		assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
		assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
		assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
		assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
		OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
		assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
		assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
	}

}