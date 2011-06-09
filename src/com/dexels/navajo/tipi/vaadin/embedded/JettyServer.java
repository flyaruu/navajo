package com.dexels.navajo.tipi.vaadin.embedded;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import com.dexels.navajo.tipi.vaadin.application.servlet.TipiVaadinServlet;
import com.vaadin.terminal.gwt.server.ApplicationServlet;

//<context-param>
//<description>
//Vaadin production mode</description>
//<param-name>productionMode</param-name>
//<param-value>false</param-value>
//</context-param>
//<servlet>
//<servlet-name>Vaadintest Application</servlet-name>
//<servlet-class>com.vaadin.terminal.gwt.server.ApplicationServlet</servlet-class>
//<init-param>
//	<description>
//	Vaadin application class to start</description>
//	<param-name>application</param-name>
//	<param-value>com.dexels.vaadintest.VaadinApplication</param-value>
//</init-param>
//<init-param>
//	<description>
//	Application widgetset</description>
//	<param-name>widgetset</param-name>
//	<param-value>com.dexels.vaadintest.widgetset.VaadintestWidgetset</param-value>
//</init-param>
//</servlet>



public class JettyServer {
	public void init(int port) {
		Server jettyServer = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		jettyServer.addConnector(connector);
		ServletContextHandler webAppContext = new ServletContextHandler(jettyServer,"/",true,false);
		webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		
		resource_handler.setResourceBase(".");

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, webAppContext });
		jettyServer.setHandler(handlers);
//		Resource r = webAppContext.getBaseResource();
//		System.err.println("R: "+r);
//		
		TipiVaadinServlet vaadin = new TipiVaadinServlet();
		ServletHolder sh = new ServletHolder(vaadin);
		sh.setInitParameter("application", "com.dexels.navajo.tipi.vaadin.application.TipiVaadinApplication");
		webAppContext.addServlet(sh,"/vaadin/*");

//		jettyServer.setHandler(webAppContext);
//		try {
//			System.err.println("Respaths: "+webAppContext.getResourcePaths("/"));
//			Resource rr = webAppContext.getResource("/brooklynbridge.jpg");
//			System.err.println("Reee: "+rr);
//		} catch (MalformedURLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		try {
			jettyServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
}
