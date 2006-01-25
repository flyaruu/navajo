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
package com.dexels.navajo.scheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;

import com.dexels.navajo.document.Message;
import com.dexels.navajo.document.Navajo;
import com.dexels.navajo.document.NavajoFactory;
import com.dexels.navajo.document.Property;
import com.dexels.navajo.server.Access;
import com.dexels.navajo.server.Dispatcher;
import com.dexels.navajo.server.NavajoConfig;

public class TaskRunner implements Runnable {

	private int maxSize = 25;
	private static TaskRunner instance = null;
	private Map tasks = null;
	private NavajoConfig myConfig;
	private Dispatcher myDispatcher;
	private long configTimestamp = -1;
	
	public static TaskRunner getInstance(NavajoConfig config) {
		return getInstance(config, null);
	}
	
	private long getConfigTimeStamp() {
		java.io.File f = new java.io.File(myConfig.getConfigPath() + "/tasks.xml");
		if ( f != null ) {
		 return f.lastModified();
		}
		return -1;
	}
	
	private void setConfigTimeStamp() {
		configTimestamp = getConfigTimeStamp();
	}
	
	private boolean isConfigModified() {
		if ( configTimestamp != getConfigTimeStamp() ) {
			return true;
		} else {
			return false;
		}
	}
	
	private void clearTaskMap() {
		if ( tasks != null ) {
			for (Iterator iter = tasks.values().iterator(); iter.hasNext();) {
				Task element = (Task) iter.next();
				System.err.println("About to remove task: " + element.getId());
				element.setRemove(true);
			}
			tasks.clear();
		}
		tasks = Collections.synchronizedMap(new HashMap());
	}
	
	private synchronized void readConfig() {
		// Read task configuration file to read predefined tasks config/tasks.xml
    	try {
    		Navajo taskDoc = myConfig.readConfig("tasks.xml");
    		
    		if ( taskDoc != null ) {
    			// Remove all previous tasks.
    			clearTaskMap();
	    		ArrayList allTasks = taskDoc.getMessages("tasks");
	    		for (int i = 0; i < allTasks.size(); i++) {
	    			Message m = (Message) allTasks.get(i);
	    			String id = m.getProperty("id").getValue();
	    			String webservice = m.getProperty("webservice").getValue();
	    			String username = m.getProperty("username").getValue();
	    			String password = m.getProperty("password").getValue();
	    			String trigger = m.getProperty("trigger").getValue();
	    			System.err.println("From tasks.xml, id = " + id);
	    			Trigger trig = Trigger.parseTrigger(trigger);
	    			Access newAcces = new Access(-1, -1, -1, username, webservice, "Taskrunner", "127.0.0.1", "localhost", false, null);
	    			Task t = new Task(webservice, username, password, newAcces, trig);
	    			t.setDispatcher(myDispatcher);
	    			instance.addTask(id, t);
	    		}
	    		setConfigTimeStamp();
    		}
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static TaskRunner getInstance(NavajoConfig config, Dispatcher myDispatcher) {
		
		if ( instance != null ) {
			return instance;
		}
		
		instance = new TaskRunner();
		instance.myConfig = config;
		instance.myDispatcher = myDispatcher;
		Thread thread = new Thread(instance);
	    thread.setDaemon(true);
	    thread.start();
	    
	    if ( config != null && myDispatcher != null ) {
	    	instance.readConfig();
	    }
	    System.err.println("Started task scheduler process $Id$");
	    return instance;
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				// Check whether tasks.xml has gotten updated.
				if ( isConfigModified() ) {
					readConfig();
				}
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
			
		}
	}
	
	public int getTaskListSize() {
		return tasks.size();
	}
	
	public synchronized void removeTask(String id) {
		
		if ( !tasks.containsKey(id) ) {
			return;
		}
		
		Task t = (Task) tasks.get(id);
		tasks.remove(id);
		t.setRemove(true);
		
		// Remove task from configuration file config/tasks.xml
		Navajo taskDoc;
		try {
			taskDoc = myConfig.readConfig("tasks.xml");
			if (taskDoc != null) {
				Message allTasks = taskDoc.getMessage("tasks");
				if (allTasks != null) {
					Message tbr = containsTask(allTasks, id);
					if ( tbr != null ) {
						allTasks.removeMessage(tbr);
						myConfig.writeConfig("tasks.xml", taskDoc);
						setConfigTimeStamp();
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Message containsTask(Message allTasks, String id) throws Exception {
		ArrayList list = allTasks.getAllMessages();
		for (int i = 0; i < list.size(); i++) {
			if ( ((Message) list.get(i)).getProperty("id").getValue().equals(id) ) {
				return (Message) list.get(i);
			}
		}
		return null;
	}
	
	public synchronized boolean addTask(String id, Task t) {
		
		if ( tasks.containsKey(id) ) {
			System.err.println("Task already exists");
			return false;
		}
		
		if ( tasks.size() == maxSize ) {
			System.err.println("Task pool is full");
			return false;
		}
		System.err.println("Addind task: " + id);
		tasks.put(id, t);
		t.setId(id);
		Thread thread = new Thread(t);
		t.setThread(thread);
		thread.start();
		System.err.println("Leaving addTask");
		// Add to task configuration file config/tasks.xml
		
		Navajo taskDoc;
		try {
			taskDoc = myConfig.readConfig("tasks.xml");
			if (taskDoc == null) {
				taskDoc = NavajoFactory.getInstance().createNavajo();
				Message m = NavajoFactory.getInstance().createMessage(taskDoc, "tasks", Message.MSG_TYPE_ARRAY);
				taskDoc.addMessage(m);
			}
			Message allTasks = taskDoc.getMessage("tasks");
			if ( allTasks != null && containsTask(allTasks, id) == null ) {
				Message newTask = NavajoFactory.getInstance().createMessage(taskDoc, "tasks");
				allTasks.addMessage(newTask);
				Property propId = NavajoFactory.getInstance().createProperty(taskDoc, "id", Property.STRING_PROPERTY, t.getId(), 0, "", Property.DIR_OUT);
				Property propUser = NavajoFactory.getInstance().createProperty(taskDoc, "username", Property.STRING_PROPERTY, t.getUsername(), 0, "", Property.DIR_OUT);
				Property propPassword = NavajoFactory.getInstance().createProperty(taskDoc, "password", Property.STRING_PROPERTY, t.getPassword(), 0, "", Property.DIR_OUT);
				Property propService = NavajoFactory.getInstance().createProperty(taskDoc, "webservice", Property.STRING_PROPERTY, t.getWebservice(), 0, "", Property.DIR_OUT);
				Property propTrigger = NavajoFactory.getInstance().createProperty(taskDoc, "trigger", Property.STRING_PROPERTY, t.getTrigger().getDescription(), 0, "", Property.DIR_OUT);
				newTask.addProperty(propId);
				newTask.addProperty(propUser);
				newTask.addProperty(propPassword);
				newTask.addProperty(propService);
				newTask.addProperty(propTrigger);
				
				myConfig.writeConfig("tasks.xml", taskDoc);
				instance.setConfigTimeStamp();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}
	
	public static void main(String [] args) throws Exception {
		TaskRunner tr = TaskRunner.getInstance(null);
		System.err.println("STARTED TASKRUNNER");
		Task t1 = new Task("InitBM", "ROOT", "", null, new TimeTrigger(1, 25, 11, 26, null));
		Task t2 = new Task("InitBM", "ROOT", "", null, new TimeTrigger(1, 25, 11, 27, null));
		System.err.println("ABOUT TO ADD TASK T1");
		tr.addTask("myTask", t1);
		System.err.println("ABOUT TO ADD TASK T2");
		tr.addTask("myOtherTask", t2);
		System.err.println("LEAVING MAIN");
	}
}
