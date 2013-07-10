package com.puppetlabs.puppetdb.javaclient.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.puppetlabs.puppetdb.javaclient.PuppetDBClient;
import com.puppetlabs.puppetdb.javaclient.model.Resource;

public class APITests {
	private static Injector injector;

	private PuppetDBClient client;

	@BeforeClass
	public void beforeClass() {
		injector = Guice.createInjector(new MockModule());
	}

	@Before
	public void before() {
		PuppetDBClient client = injector.getInstance(PuppetDBClient.class);
	}

	@Test
	public void getUsers() throws Exception {
		List<Resource> users = client.getUsers();
		Assert.assertNotNull("should not return a null list", users);
		assertEquals("should return two user resources", users.size());
	}
}
