package com.puppetlabs.puppetdb.javaclient.test;

import com.google.inject.AbstractModule;
import com.puppetlabs.puppetdb.javaclient.HttpConnector;

public class MockModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(HttpConnector.class).to(MockConnector.class);
	}

}
