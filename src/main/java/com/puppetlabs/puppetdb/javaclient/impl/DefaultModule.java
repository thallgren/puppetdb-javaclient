/**
 * Copyright (c) 2013 Puppet Labs, Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Contributors:
 *   Puppet Labs
 */
package com.puppetlabs.puppetdb.javaclient.impl;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.puppetlabs.puppetdb.javaclient.APIPreferences;
import com.puppetlabs.puppetdb.javaclient.HttpConnector;
import com.puppetlabs.puppetdb.javaclient.PuppetDBClient;

/**
 * Default Guice Injection module
 */
public class DefaultModule extends AbstractModule {
	/**
	 * Default connection timeout when establishing a new connection to the PuppetDB service
	 */
	public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

	/**
	 * Default response timeout for requests sent to the PuppetDB service
	 */
	public static final int DEFAULT_READ_TIMEOUT = 5000;

	private final String serviceURL;

	/**
	 * Create a module with a given URL. It be an absolute URL that ends
	 * with a slash. Do not include the version segment.
	 * 
	 * @param serviceURL
	 *            The URL of the PuppetDB service
	 */
	public DefaultModule(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	@Override
	protected void configure() {
		bind(Gson.class).toProvider(GsonProvider.class);
		bind(APIPreferences.class).toInstance(new APIPreferences() {
			@Override
			public int getConnectTimeout() {
				return DEFAULT_CONNECTION_TIMEOUT;
			}

			@Override
			public int getReadTimeout() {
				return DEFAULT_READ_TIMEOUT;
			}

			@Override
			public String getServiceURL() {
				return serviceURL;
			}
		});
		bind(HttpConnector.class).to(HttpComponentsConnector.class);
		bind(PuppetDBClient.class).to(PuppetDBClientImpl.class);
	}
}
