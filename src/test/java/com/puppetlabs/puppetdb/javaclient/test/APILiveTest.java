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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.puppetlabs.puppetdb.javaclient.PuppetDBClient;
import com.puppetlabs.puppetdb.javaclient.PuppetDBClientFactory;
import com.puppetlabs.puppetdb.javaclient.model.Event;
import com.puppetlabs.puppetdb.javaclient.model.Event.Status;
import com.puppetlabs.puppetdb.javaclient.model.Fact;
import com.puppetlabs.puppetdb.javaclient.model.Facts;
import com.puppetlabs.puppetdb.javaclient.model.Node;
import com.puppetlabs.puppetdb.javaclient.model.Report;
import com.puppetlabs.puppetdb.javaclient.model.Resource;

@SuppressWarnings("javadoc")
public class APILiveTest {
	private PuppetDBClient client;

	// Change these to make the test run in your setup.
	// TODO: Make it configurable from an external file
	private static final String NODE_THAT_IS_KNOWN_TO_EXIST = "home.tada.se";

	private static final String SERVICE_URL = "https://localhost:9081/";

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
		assertTrue("should return at least 1 name", names.size() >= 1);
	}

	@Test
	public void getHostnameFact() throws Exception {
		List<Fact> facts = client.getFacts(eq(Fact.NAME, "hostname"));
		assertNotNull("should not return a null list", facts);
		assertEquals("should return all facts", 1, facts.size());
	}

	@Test
	public void getFactsWithClass() throws Exception {
		List<Fact> facts = client.getFacts(and(
			eq(Fact.NAME, "hostname"),
			inResources(Fact.CERTNAME, Resource.CERTNAME, and(eq(Resource.TYPE, "Class"), eq(Resource.TITLE, "main")))));
		assertNotNull("should not return a null list", facts);
		assertEquals("should return 1 fact", 1, facts.size());
	}

	@Test
	public void getMetrics() throws Exception {
		Map<String, String> metrics = client.getMetrics();
		assertNotNull("should not return a null map", metrics);
		assertTrue("should return the at least 30 metrics", metrics.size() >= 30);
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
	public void getNumberOfNodesMetric() throws Exception {
		Map<String, Object> numberOfNodesMetric = client.getMetric("com.puppetlabs.puppetdb.query.population:type=default,name=num-nodes");
		assertNotNull("should not return a null map", numberOfNodesMetric);
		assertEquals("should return 1 metric", 1, numberOfNodesMetric.size());
		Object value = numberOfNodesMetric.get("Value");
		assertNotNull("should return an attribute named 'Value'", value);
		assertTrue("should return an numeric value", value instanceof Number);
	}

	@Test
	public void getReports() throws Exception {
		List<Report> reports = client.getReports(eq(Report.CERTNAME, NODE_THAT_IS_KNOWN_TO_EXIST));
		assertNotNull("should not return a null list", reports);
		assertTrue("should return reports", reports.size() > 0);
	}

	@Test
	public void replaceFacts() throws Exception {
		// Retrieve the current facts
		List<String> names = client.getFactNames();
		assertNotNull("should not return a null list", names);
		Map<String, String> values = new HashMap<String, String>(names.size());
		for(String factName : names) {
			List<Fact> facts = client.getFacts(eq(Fact.CERTNAME, NODE_THAT_IS_KNOWN_TO_EXIST), factName);
			if(facts.size() > 0)
				values.put(factName, facts.get(0).getValue());
		}

		String origHostName = values.put("hostname", "www.example.com");

		Facts newFacts = new Facts();
		newFacts.setCertname(NODE_THAT_IS_KNOWN_TO_EXIST);
		newFacts.setValues(values);
		UUID uuid = client.replaceFacts(newFacts);
		assertNotNull("should not return a null command uuid", uuid);
		System.out.println(uuid);

		// Command is asynchronous. Give it some time
		Thread.sleep(2000);
		List<Fact> facts = client.getFacts(eq(Fact.CERTNAME, NODE_THAT_IS_KNOWN_TO_EXIST), "hostname");
		assertEquals("should have replaced the 'hostname' fact", "www.example.com", facts.get(0).getValue());

		// Restore the old value
		values.put("hostname", origHostName);
		assertNotNull("should not return a null command uuid", client.replaceFacts(newFacts));
	}

	@Test
	public void storeReport() throws Exception {
		List<Report> reports = client.getReports(eq(Report.CERTNAME, NODE_THAT_IS_KNOWN_TO_EXIST));
		assertNotNull("should not return a null list", reports);
		assertTrue("should return reports", reports.size() > 0);
		Report template = reports.get(0);
		Report report = new Report();
		report.setCertname(template.getCertname());
		report.setPuppetVersion(template.getPuppetVersion());
		report.setConfigurationVersion(template.getConfigurationVersion());
		report.setReportFormat(template.getReportFormat());
		Date now = new Date();
		report.setStartTime(new Date(now.getTime() - 1000));
		report.setEndTime(now);

		List<Event> events = new ArrayList<Event>();
		Event event = new Event();
		event.setMessage("Test event 1");
		event.setStatus(Status.success);
		event.setTimestamp(new Date(now.getTime() - 500));
		event.setResourceType("Test");
		event.setResourceTitle("dummy");
		events.add(event);

		event = new Event();
		event.setMessage("Test event 2");
		event.setStatus(Status.success);
		event.setTimestamp(new Date(now.getTime() - 400));
		event.setResourceType("Test");
		event.setResourceTitle("dummy");
		events.add(event);

		report.setResourceEvents(events);
		UUID cmdId = client.storeReport(report);
		assertNotNull("should not return a null command uuid", cmdId);
	}

}
