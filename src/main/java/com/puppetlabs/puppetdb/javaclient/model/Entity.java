/**
 * Copyright (c) 2012 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Cloudsmith
 * 
 */
package com.puppetlabs.puppetdb.javaclient.model;

import com.puppetlabs.puppetdb.javaclient.GsonProvider;

/**
 * Super class of all model entitites. Provides basic JSON capability.
 */
public class Entity {
	protected static boolean safeEquals(Object a, Object b) {
		return a == b || a != null && b != null && a.equals(b);
	}

	protected static int safeHash(Object a) {
		return a == null
				? 773
				: a.hashCode();
	}

	/**
	 * Produces a JSON representation of the Entity
	 * 
	 * @return The JSON string
	 */
	@Override
	public final String toString() {
		return GsonProvider.toJSON(this);
	}
}
