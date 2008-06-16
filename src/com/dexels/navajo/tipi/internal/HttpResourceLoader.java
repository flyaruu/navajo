package com.dexels.navajo.tipi.internal;

import java.io.*;
import java.net.*;
import java.util.*;

public class HttpResourceLoader extends ClassPathResourceLoader {

	private final URL baseURL;

	public HttpResourceLoader(URL baseURL) {
		this.baseURL = baseURL;
	}

	public URL getResourceURL(String location) throws MalformedURLException {
		URL u = new URL(baseURL, location);
		// System.err.println("HttpResourceLoader: Resolved to : "+u+" base:
		// "+baseURL);
		if (u == null) {
			return super.getResourceURL(location);
		}
		return u;
	}

	public InputStream getResourceStream(String location) throws IOException {
		URL u = getResourceURL(location);
		InputStream is = null;
		try {
			is = u.openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (is != null) {
			return is;
		}
		System.err.println("HttpResourceLoader failed. Looking in classpath: "+location+" base: "+baseURL);
		return super.getResourceStream(location);
	}

	public List<File> getAllResources() throws IOException {
		throw new UnsupportedOperationException("The http resource loader is unable to enumerate resources");
}
	
}
