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

import java.io.File;

/**
 * Preferences used when connecting to the PuppetDB instance
 */
public interface APIPreferences {
	/**
	 * Connection timeout in milliseconds
	 * 
	 * @return A millisecond timeout
	 */
	int getConnectTimeout();

	/**
	 * Read timeout in milliseconds.
	 * 
	 * @return A millisecond timeout
	 */
	int getReadTimeout();

	/**
	 * The URL for all requests to the PuppetDB service. This is an absolute URL that ends
	 * with a slash. It does not include the version segment.
	 * 
	 * @return The absolute URL of the PuppetDB service
	 */
	String getServiceURL();

	/**
	 * Returns the path of the Certificate Authority Certificate PEM file.
	 * 
	 * @return An absolute path
	 */
	File getCaCertPEM();

	/**
	 * Returns the path of the Certificate PEM file.
	 * 
	 * @return An absolute path
	 */
	File getCertPEM();

	/**
	 * Returns the path of the Private Key PEM file.
	 * 
	 * @return An absolute path
	 */
	File getPrivateKeyPEM();
}
