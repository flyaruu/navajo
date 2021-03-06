/**
 * <p>Title: Navajo Product Project</p>
 * <p>Description: This is the official source for the Navajo server</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Dexels BV</p>
 * @author 
 * @version $Id$.
 *
 * DISCLAIMER
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL DEXELS BV OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */
package com.dexels.navajo.adapter;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

import com.dexels.navajo.document.Navajo;
import com.dexels.navajo.document.types.Binary;
import com.dexels.navajo.mapping.Mappable;
import com.dexels.navajo.mapping.MappableException;
import com.dexels.navajo.server.Access;
import com.dexels.navajo.server.UserException;
import com.dexels.navajo.server.enterprise.queue.Queuable;
import com.dexels.navajo.server.enterprise.queue.RequestResponseQueueFactory;
import com.dexels.navajo.util.AuditLog;

public class HTTPMap implements Mappable, Queuable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5398399368623971687L;
	
	public Binary content = null;
	public String textContent = null;
	public String method = "POST";
	public String contentType = null;
	public String url = null;
	public boolean doSend = false;
	public boolean queuedSend = false;
	public boolean catchConnectionTimeOut = true;
	public boolean hasConnectionTimeOut = false;
	public long waitUntil = 0;
	public Binary result = null;
	public String textResult = null;
	public int connectTimeOut = 5000;
	public int readTimeOut = -1;
	public int retries = 0;
	public int maxRetries = 100;
	
	private static int instances = 0;
	private Navajo myNavajo;
	private Access myAccess;

	public static int maxRunningInstances = -1;
	
	public void load(Access access) throws MappableException, UserException {
		myNavajo = access.getInDoc();
		myAccess = access;
	}

	public void setTextContent(String s) {
		textContent = s;
	}
	
	public void setContent(Binary b) {
		content = b;
	}
	
	public void setUrl(String s) {
		url = s;
	}
	
	public void setContentType(String s) {
		contentType = s;
	}
	
	public void setConnectTimeOut(int i) {
		this.connectTimeOut = i;
	}
	
	public void setReadTimeOut(int i) {
		this.readTimeOut = i;
	}
	
	public void setDoSend(boolean b) throws UserException {
		if ( !queuedSend ) {
			sendOverHTTP();
		}
	}
	
	private final void sendOverHTTP() throws UserException {
		instances++;

		if ( instances > 100 ) {
			AuditLog.log("HTTPMap", "WARNING: More than 100 waiting HTTP requests", Level.WARNING, myAccess.accessID);
		}
		try {

			AuditLog.log("HTTPMap", "About to send to: " + url, Level.INFO, myAccess.accessID);
			URL u = null;
			if(!url.startsWith("http://") && (!url.startsWith("https://"))) {
				AuditLog.log("HTTPMap", "No protocol. Always prepend protocol. Assuming http.",Level.WARNING);
				u = new URL("http://" + url);
			} else {
				u = new URL(url);
			}
			
			
			HttpURLConnection con = null;
			con = (HttpURLConnection) u.openConnection();
			con.setConnectTimeout(connectTimeOut);
			if ( readTimeOut != -1 ) {
				con.setReadTimeout(readTimeOut);
			}
			con.setRequestMethod(method);
			if ( method.equals("POST")) {
				con.setDoOutput(true);
				con.setDoInput(true);
			}
			con.setUseCaches(false);
			if ( contentType != null ) {
				con.setRequestProperty("Content-type", contentType);
			}
			if ( textContent != null ) {
				//System.err.println("textContent: " + textContent);
				OutputStreamWriter osw = null;
				osw = new OutputStreamWriter(con.getOutputStream());
				try {
					osw.write(textContent);
				} finally {
					if ( osw != null ) {
						osw.close();
					}
				}
			} else if ( content != null ) {
				OutputStream os = null;
				//System.err.println("content: "  + content);
				os = con.getOutputStream();
				try {
					content.write(os);
				} finally {
					if ( os != null ) {
						os.close();
					}
				}
			} else {
				if ( method.equals("POST")) {
					AuditLog.log("HTTPMap", "Empty content.", Level.INFO, myAccess.accessID);
					throw new UserException(-1, "");
				}
			}

			InputStream is = null;
			is = con.getInputStream();
			try {
				result = new Binary(is);
			} finally {
				if ( is != null ) {
					is.close();
				}
			}

		} catch (java.net.SocketTimeoutException sto) {
			// 
			if (!catchConnectionTimeOut) {
				throw new UserException(-1, sto.getMessage(), sto);
			} else {
				hasConnectionTimeOut = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException(-1, e.getMessage(), e);
		} finally {
			instances--;
		}
	}
	
	public String getTextResult() {
		if ( result != null ) {
			return new String(result.getData());
		} else {
			return null;
		}
	}
	
	public Binary getResult() {
		return result;
	}
	
	public void store() throws MappableException, UserException {
		if (queuedSend) {
			try {
				RequestResponseQueueFactory.getInstance().send(this, 100);
			} catch (Exception e) {
				AuditLog.log("HTTPMap", e.getMessage(), Level.WARNING, myAccess.accessID);
			}
		}
	}

	public void kill() {
	}
	
	public Binary getResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean send() {
		retries++;
		try {
			sendOverHTTP();
		} catch (UserException e) {
			if ( myAccess != null ) {
				myAccess.setException(e);
			}
			return false;
		}
		return true;
	}

	public void setRequest(Binary b) {
		setContent(b);
	}

	public void setQueuedSend(boolean b) {
		queuedSend = b;
	}
	

	public Binary getRequest() {
		if ( textContent != null ) {
			return new Binary(textContent.getBytes());
		} else {
			return content;
		}
	}

	public void setWaitUntil(long w) {
		waitUntil = w;
	}
	
	public long getWaitUntil() {
		return waitUntil;
	}

	public int getRetries() {
		return retries;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int r) {
		maxRetries = r;
	}

	public void resetRetries() {
		retries = 0;
	}

	public void setMaxInstances() {
		
	}

	public Access getAccess() {
		return myAccess;
	}

	public Navajo getNavajo() {
		return myNavajo;
	}
	
	public int getMaxRunningInstances() {
		return maxRunningInstances;
	}

	public void setMaxRunningInstances(int maxRunningInstances) {
		HTTPMap.maxRunningInstances = maxRunningInstances;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setCatchConnectionTimeOut(boolean catchConnectionTimeOut) {
		this.catchConnectionTimeOut = catchConnectionTimeOut;
	}
	
}
