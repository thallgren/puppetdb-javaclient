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
package com.puppetlabs.puppetdb.javaclient.test;

import static com.puppetlabs.puppetdb.javaclient.query.Query.and;
import static com.puppetlabs.puppetdb.javaclient.query.Query.eq;
import static com.puppetlabs.puppetdb.javaclient.query.Query.inFacts;
import static com.puppetlabs.puppetdb.javaclient.query.Query.inResources;
import static com.puppetlabs.puppetdb.javaclient.query.Query.match;
import static com.puppetlabs.puppetdb.javaclient.query.Query.or;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.puppetlabs.puppetdb.javaclient.PuppetDBClient;
import com.puppetlabs.puppetdb.javaclient.PuppetDBClientFactory;
import com.puppetlabs.puppetdb.javaclient.model.Event;
import com.puppetlabs.puppetdb.javaclient.model.Fact;
import com.puppetlabs.puppetdb.javaclient.model.Node;
import com.puppetlabs.puppetdb.javaclient.model.Report;
import com.puppetlabs.puppetdb.javaclient.model.Resource;

@SuppressWarnings("javadoc")
public class APILiveTest {
	private PuppetDBClient client;

	// Change these to make the test run in your setup.
	// TODO: Make it configurable from an external file
	private static final String NODE_THAT_IS_KNOWN_TO_EXIST = "home.tada.se";

	private static final String SERVICE_URL = "http://localhost:9080/";

	@Before
	public void before() {
		client = PuppetDBClientFactory.newClient(SERVICE_URL);
	}

	@Test
	public void getActiveNodes() throws Exception {
		List<Node> nodes = client.getActiveNodes(null);
		assertNotNull("should not return a null list", nodes);
		assertEquals("should return all nodes", 1, nodes.size());
	}

	@Test
	public void getClassesWithQuery() throws Exception {
		List<Resource> classes = client.getResources(or(eq(Resource.TITLE, "main"), eq(Resource.TITLE, "Settings")), "Class");
		assertNotNull("should not return a null list", classes);
		assertEquals("should return two class resources", 2, classes.size());
	}

	@Test
	public void getClassMain() throws Exception {
		List<Resource> classes = client.getResources(null, "Class", "main");
		assertNotNull("should not return a null list", classes);
		assertEquals("should return one class resource", 1, classes.size());
		assertEquals("should have title main", "main", classes.get(0).getTitle());
	}

	@Test
	public void getEvents() throws Exception {
		List<Event> events = client.getEvents(eq(Event.CERTNAME, NODE_THAT_IS_KNOWN_TO_EXIST));
		assertNotNull("should not return a null list", events);
		assertTrue("should return events", events.size() > 0);
	}

	@Test
	public void getFactNames() throws Exception {
		List<String> names = client.getFactNames();
		assertNotNull("should not return a null list", names);
		assertTrue("should return at least 10 names", names.size() >= 10);
	}

	@Test
	public void getFacts() throws Exception {
		List<Fact> facts = client.getFacts(eq(Fact.NAME, "operatingsystem"));
		assertNotNull("should not return a null list", facts);
		assertEquals("should return all facts", 1, facts.size());
	}

	@Test
	public void getFactsWithClass() throws Exception {
		List<Fact> facts = client.getFacts(and(
			eq(Fact.NAME, "ipaddress"),
			inResources(Fact.CERTNAME, Resource.CERTNAME, and(eq(Resource.TYPE, "Class"), eq(Resource.TITLE, "main")))));
		assertNotNull("should not return a null list", facts);
		assertEquals("should return 1 fact", 1, facts.size());
	}

	@Test
	public void getNumberOfNodesMetric() throws Exception {
		Map<String, Object> numberOfNodesMetric = client.getMetric("com.puppetlabs.puppetdb.query.population:type=default,name=num-nodes");
		assertNotNull("should not return a null map", numberOfNodesMetric);
		assertEquals("should return 1 metric", 1, numberOfNodesMetric.size());
		Object value = numberOfNodesMetric.get("Value");
		assertNotNull("should return an attribute named 'Value'", value);
		assertTrue("should return an numeric value", value instanceof Number);
	}

	@Test
	public void getMetrics() throws Exception {
		Map<String, String> metrics = client.getMetrics();
		assertNotNull("should not return a null map", metrics);
		assertTrue("should return the at least 30 metrics", metrics.size() >= 30);
	}

	@Test
	public void getNamedFacts() throws Exception {
		List<Fact> facts = client.getFacts(null, "kernel");
		assertNotNull("should not return a null list", facts);
		assertEquals("should return the 'kernel' fact", 1, facts.size());
	}

	@Test
	public void getNodeStatus() throws Exception {
		Node node = client.getNodeStatus(NODE_THAT_IS_KNOWN_TO_EXIST);
		assertNotNull("should not return null", node);
	}

	@Test
	public void getNodesWithClass() throws Exception {
		List<Node> node = client.getActiveNodes(inResources(
			Node.NAME, Resource.CERTNAME, and(eq(Resource.TYPE, "Class"), eq(Resource.TITLE, "Settings"))));
		assertNotNull("should not return a null list", node);
		assertEquals("should return one node", 1, node.size());
	}

	@Test
	public void getNodesWithFacts() throws Exception {
		List<Node> node = client.getActiveNodes(inFacts(
			Node.NAME, Fact.CERTNAME, and(eq(Fact.NAME, "puppetversion"), match(Fact.VALUE, "^3\\.2.*"))));
		assertNotNull("should not return a null list", node);
		assertEquals("should return one node", 1, node.size());
	}

	@Test
	public void getReports() throws Exception {
		List<Report> reports = client.getReports(eq(Report.CERTNAME, NODE_THAT_IS_KNOWN_TO_EXIST));
		assertNotNull("should not return a null list", reports);
		assertTrue("should return reports", reports.size() > 0);
	}

}
