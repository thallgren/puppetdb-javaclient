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
package com.puppetlabs.puppetdb.javaclient;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.puppetlabs.puppetdb.javaclient.impl.DefaultModule;

/**
 * A factory used for creating fully configured PuppetDBClient instances
 */
public class PuppetDBClientFactory {

	/**
	 * Create a new PuppetDBClient using the bindings of one or several
	 * Guice modules.
	 * 
	 * @param modules
	 *            The Guice modules where the bindings have been defined
	 * @return The created client instance
	 */
	public static PuppetDBClient newClient(Module... modules) {
		return Guice.createInjector(modules).getInstance(PuppetDBClient.class);
	}

	/**
	 * Create a new PuppetDBClient that will connect to the given <code>serviceURL</code>. It must be an absolute URL that ends
	 * with a slash. Do not include the version segment.
	 * 
	 * @param serviceURL
	 *            The URL of the PuppetDB service
	 * @return The created client instance
	 */
	public static PuppetDBClient newClient(String serviceURL) {
		return newClient(new DefaultModule(serviceURL));
	}
}
