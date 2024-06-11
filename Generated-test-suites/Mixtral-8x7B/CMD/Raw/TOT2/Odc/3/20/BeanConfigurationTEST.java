import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
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
	public void testObjectMapper() throws Exception {
		ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
		assertNotNull(objectMapper);

		CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
				.addSerializer(Internationalizable.class, new I18nOutputSerializer());
		assertEquals(customOutputSerializer.getClass(), objectMapper.getSerializerProvider().getSerializers().findSerializer(Internationalizable.class).getClass());
	}

	@Test
	public void testDefaultHttpFirewall() {
		HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
		assertNotNull(defaultHttpFirewall);
		assertEquals(DefaultHttpFirewall.class, defaultHttpFirewall.getClass());
		assertEquals(true, defaultHttpFirewall.getAllowUrlEncodedSlash());
	}

	@Test
	public void testCloudMetadataClient() {
		CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
		assertNotNull(cloudMetadataClient);
		assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());

		assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
		assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants("instanceId"));
		assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("instanceId", "tenantId"));
		assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
		assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
		assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser("instanceId"));
	}

	@Test
	public void testCookieSerializer() {
		CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
		assertNotNull(cookieSerializer);
		assertEquals("JSESSIONID", cookieSerializer.getCookieName());
	}

	private class NullCloudMetadataClient implements CloudMetadataClient {

		@Override
		public boolean supportsCloudMetadata() {
			return false;
		}

		@Override
		public boolean supportsTenantInstance() {
			return false;
		}

		@Override
		public boolean needsOBTenantName() {
			return false;
		}

		@Override
		public boolean needsSysTenantUser() {
			return false;
		}

		@Override
		public boolean includeClusterNameForJdbcUsername() {
			return true;
		}

		@Override
		public OceanBaseAccessMode oceanbaseAccessMode() {
			return OceanBaseAccessMode.DIRECT;
		}

		@Override
		public List<OBInstance> listInstances() {
			throw new UnsupportedException("CloudMetadata not supported");
		}

		@Override
		public List<OBTenant> listTenants(String instanceId) {
			throw new UnsupportedException("CloudMetadata not supported");
		}

		@Override
		public OBTenant getTenant(String instanceId, String tenantId) {
			throw new UnsupportedException("getTenant not supported in non-could");
		}

		@Override
		public List<OBDatabaseUser> listDatabaseUsers(String instanceId, String tenantId) {
			throw new UnsupportedException("CloudMetadata not supported");
		}

		@Override
		public OBTenantEndpoint getTenantEndpoint(String instanceId, String tenantId) {
			OBTenantEndpoint endpoint = new OBTenantEndpoint();
			endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
			return endpoint;
		}

		@Override
		public OBDatabaseUser getSysTenantUser(String instanceId) {
			throw new UnsupportedException("CloudMetadata not supported");
		}
	}
}