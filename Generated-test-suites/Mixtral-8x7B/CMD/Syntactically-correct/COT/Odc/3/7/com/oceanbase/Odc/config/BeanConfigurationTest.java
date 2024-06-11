import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
import com.oceanbase.odc.service.connection.ModelTests.OBDatabaseUserTest;
import com.oceanbase.odc.service.connection.ModelTests.OBInstanceTest;
import com.oceanbase.odc.service.connection.ModelTests.OBTenantTest;
import com.oceanbase.odc.service.connection.ModelTests.OBTenantEndpointTest;
import com.oceanbase.odc.service.connection.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

	@Mock
	private SensitivePropertyHandler sensitivePropertyHandler;

	@InjectMocks
	private BeanConfiguration beanConfiguration;

	@Before
	public void setup() {
		when(sensitivePropertyHandler.decrypt("test")).thenReturn("test");
	}

	@Test
	public void testPasswordEncoder() {
		PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
		assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
	}

	@Test
	public void testObjectMapper() throws Exception {
		ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
		assertNotNull(objectMapper);

		CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
				.addSerializer(Internationalizable.class, new I18nOutputSerializer());

		assertEquals(customOutputSerializer, objectMapper.getSerializationConfig().getAnnotationIntrospector()
				.findSerializerByName(objectMapper.createType(Internationalizable.class).getTypeHandle()).getSerializer());

		assertEquals(JacksonFactory.unsafeJsonMapper().getSerializationConfig().getAnnotationIntrospector()
				.findSerializerByName(JacksonFactory.unsafeJsonMapper().createType(Internationalizable.class).getTypeHandle()).getSerializer(),
				objectMapper.getSerializationConfig().getAnnotationIntrospector()
						.findSerializerByName(objectMapper.createType(Internationalizable.class).getTypeHandle()).getSerializer());
	}

	@Test
	public void testDefaultHttpFirewall() {
		HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
		assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
		assertTrue(defaultHttpFirewall.getAllowUrlEncodedSlash());
	}

	@Test
	public void testCloudMetadataClient() {
		CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
		assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
	}

	@Test
	public void testCookieSerializer() {
		CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
		assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
		assertEquals("JSESSIONID", cookieSerializer.getCookieName());
	}

	@Test
	public void testNullCloudMetadataClient() {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		assertNotNull(nullCloudMetadataClient);
	}

	@Test(expected = UnsupportedException.class)
	public void testListInstances() throws Exception {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		List<OBInstance> instances = nullCloudMetadataClient.listInstances();
		assertNotNull(instances);
		instances.size();
	}

	@Test(expected = UnsupportedException.class)
	public void testListTenants() throws Exception {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		List<OBTenant> tenants = nullCloudMetadataClient.listTenants("instanceId");
		assertNotNull(tenants);
		tenants.size();
	}

	@Test(expected = UnsupportedException.class)
	public void testGetTenant() throws Exception {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		OBTenant tenant = nullCloudMetadataClient.getTenant("instanceId", "tenantId");
		assertNotNull(tenant);
	}

	@Test(expected = UnsupportedException.class)
	public void testListDatabaseUsers() throws Exception {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		List<OBDatabaseUser> users = nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
		assertNotNull(users);
		users.size();
	}

	@Test
	public void testGetTenantEndpoint() throws Exception {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
		assertNotNull(endpoint);
		assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
	}

	@Test(expected = UnsupportedException.class)
	public void testGetSysTenantUser() throws Exception {
		NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
		OBDatabaseUser user = nullCloudMetadataClient.getSysTenantUser("instanceId");
		assertNotNull(user);
	}

	private List<OBInstance> getMockInstances() {
		List<OBInstance> instances = new ArrayList<OBInstance>();
		instances.add(new OBInstanceTest().getMockOBInstance());
		return instances;
	}

	private List<OBTenant> getMockTenants() {
		List<OBTenant> tenants = new ArrayList<OBTenant>();
		tenants.add(new OBTenantTest().getMockOBTenant());
		return tenants;
	}

	private OBTenant getMockTenant() {
		return new OBTenantTest().getMockOBTenant();
	}

	private List<OBDatabaseUser> getMockDatabaseUsers() {
		List<OBDatabaseUser> users = new ArrayList<OBDatabaseUser>();
		users.add(new OBDatabaseUserTest().getMockOBDatabaseUser());
		return users;
	}

	private OBTenantEndpoint getMockTenantEndpoint() {
		return new OBTenantEndpointTest().getMockOBTenantEndpoint();
	}

	private OBDatabaseUser getMockSysTenantUser() {
		return new OBDatabaseUserTest().getMockOBDatabaseUser();
	}
}