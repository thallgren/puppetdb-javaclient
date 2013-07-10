package com.puppetlabs.puppetdb.javaclient;

import static com.puppetlabs.puppetdb.javaclient.model.GenericTypes.LIST_RESOURCE;

import java.io.IOException;
import java.util.List;

import com.google.inject.Inject;
import com.puppetlabs.puppetdb.javaclient.model.Resource;

public class PuppetDBClient {
	private final HttpConnector connector;

	@Inject
	public PuppetDBClient(HttpConnector connector) {
		this.connector = connector;
	}

	public List<Resource> getUsers() throws IOException {
		return connector.get("resources/User", null, LIST_RESOURCE);
	}
}
