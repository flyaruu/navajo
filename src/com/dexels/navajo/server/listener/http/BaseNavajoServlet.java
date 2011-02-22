package com.dexels.navajo.server.listener.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.dexels.navajo.document.Header;
import com.dexels.navajo.document.Navajo;
import com.dexels.navajo.document.NavajoException;
import com.dexels.navajo.document.NavajoFactory;
import com.dexels.navajo.server.Dispatcher;
import com.dexels.navajo.server.DispatcherFactory;
import com.dexels.navajo.server.DispatcherInterface;

public abstract class BaseNavajoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3874256795878319625L;

	protected String configurationPath = "";
	protected String rootPath = null;
	protected boolean extremeEdition = false;
	protected static boolean streamingMode = true; 

	public static final String DEFAULT_SERVER_XML = "config/server.xml";
	
	public static final String COMPRESS_GZIP = "gzip";
	public static final String COMPRESS_JZLIB = "jzlib";


	protected DispatcherInterface initDispatcher() {
		return DispatcherFactory.getInstance();
	}


	protected final void dumHttp(HttpServletRequest request, long index, File dir) {
		// Dump stuff.
		if (request != null) {
			StringBuffer sb = new StringBuffer();

			sb.append("HTTP DUMP (" + request.getRemoteAddr() + "/"
					+ request.getRequestURI());
			Enumeration e = request.getHeaderNames();
			while (e.hasMoreElements()) {
				String headerName = (String) e.nextElement();
				sb.append(headerName + "=" + request.getHeader(headerName) + "\n");
			}
			try {

				if (dir != null) {
					FileWriter fw = new FileWriter(new File(dir, "httpdump-"
							+ index));
					fw.write(sb.toString());
					fw.close();
				} else {
					System.err.println(sb.toString());
				}
			} catch (IOException ioe) {
				ioe.printStackTrace(System.err);
			}
		} else {
			System.err.println("EMPTY REQUEST OBJECT!!");
		}
	}


	protected final void copyResource(OutputStream out, InputStream in){
		BufferedInputStream bin = new BufferedInputStream(in);
		BufferedOutputStream bout = new BufferedOutputStream(out);
		byte[] buffer = new byte[1024];
		int read = -1;
		boolean ready = false;
		while (!ready) {
			try {
				read = bin.read(buffer);
				if ( read > -1 ) {
					bout.write(buffer,0,read);
				}
			} catch (IOException e) {
			}
			if ( read <= -1) {
				ready = true;
			}
		}
		try {
			bin.close();
			bout.flush();
			bout.close();
		} catch (IOException e) {

		}
	}
}
