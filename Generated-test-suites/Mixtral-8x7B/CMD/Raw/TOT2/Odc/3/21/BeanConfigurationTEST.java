import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

	@Mock
	private SensitivePropertyHandler sensitivePropertyHandler;

	@InjectMocks
	private BeanConfiguration beanConfiguration;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testPasswordEncoder() {
		PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
		assertNotNull(passwordEncoder);
		assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
	}

	@Test
	public void testObjectMapper() {
		CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
				.addSerializer(Internationalizable.class, new I18nOutputSerializer());
		ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
		assertNotNull(objectMapper);
		// add more assertions for customOutputSerializer if necessary
	}

	@Test
	public void testDefaultHttpFirewall() {
		HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
		assertNotNull(defaultHttpFirewall);
		assertEquals(DefaultHttpFirewall.class, defaultHttpFirewall.getClass());
	}

	@Test
	public void testCloudMetadataClient() {
		CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
		assertNotNull(cloudMetadataClient);
		assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
	}

	@Test
	public void testCookieSerializer() {
		CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
		assertNotNull(cookieSerializer);
		assertEquals(DefaultCookieSerializer.class, cookieSerializer.getClass());
		assertEquals("JSESSIONID", cookieSerializer.getCookieName());
	}

	@Test
	public void testNullCloudMetadataClient() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
	}

	@Test
	public void testNullCloudMetadataClient_supportsCloudMetadata() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
	}

	@Test
	public void testNullCloudMetadataClient_needsSysTenantUser() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		assertFalse(nullCloudMetadataClient.needsSysTenantUser());
	}

	@Test
	public void testNullCloudMetadataClient_includeClusterNameForJdbcUsername() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
	}

	@Test
	public void testNullCloudMetadataClient_oceanbaseAccessMode() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
	}

	@Test
	public void testNullCloudMetadataClient_getTenantEndpoint() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
		assertNotNull(endpoint);
		assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
	}

	@Test
	public void testNullCloudMetadataClient_listDatabaseUsers() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		List<OBDatabaseUser> databaseUsers = nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
		assertNotNull(databaseUsers);
		assertEquals(0, databaseUsers.size());
	}

	@Test
	public void testNullCloudMetadataClient_listTenants() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		List<OBTenant> tenants = nullCloudMetadataClient.listTenants("instanceId");
		assertNotNull(tenants);
		assertEquals(0, tenants.size());
	}

	@Test
	public void testNullCloudMetadataClient_getTenant() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		OBTenant tenant = nullCloudMetadataClient.getTenant("instanceId", "tenantId");
		assertNull(tenant);
	}

	@Test
	public void testNullCloudMetadataClient_getSysTenantUser() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
	}

	@Test
	public void testNullCloudMetadataClient_supportsTenantInstance() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		assertFalse(nullCloudMetadataClient.supportsTenantInstance());
	}

	@Test
	public void testNullCloudMetadataClient_needsOBTenantName() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
		assertFalse(nullCloudMetadataClient.needsOBTenantName());
	}
}