package com.dexels.navajo.tribe;

import com.dexels.navajo.document.Navajo;
import com.dexels.navajo.mapping.Mappable;
import com.dexels.navajo.mapping.MappableException;
import com.dexels.navajo.server.Access;
import com.dexels.navajo.server.Dispatcher;
import com.dexels.navajo.server.NavajoConfig;
import com.dexels.navajo.server.Parameters;
import com.dexels.navajo.server.UserException;

public class PingAnswer extends Answer implements Mappable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7500090213107679662L;
	
	public double cpuLoad;
	public int threadCount;
	public long requestCount;
	public long uptime;
	public boolean busy = false;
	
	public PingAnswer(Request q) {
		super(q);
		refreshPingStatistics();
	}

	private void refreshPingStatistics() {
		cpuLoad = Dispatcher.getInstance().getNavajoConfig().getCurrentCPUload();
		threadCount = Dispatcher.getInstance().getAccessSetSize();
		requestCount = Dispatcher.getInstance().requestCount;
		uptime = Dispatcher.getInstance().getUptime();
		busy = Dispatcher.getInstance().isBusy();
	}
	
	public boolean acknowledged() {
		return true;
	}

	public double getCpuLoad() {
		return cpuLoad;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public long getRequestCount() {
		return requestCount;
	}

	public long getUptime() {
		return uptime;
	}

	public void kill() {
	}

	public void load(Parameters parms, Navajo inMessage, Access access,
			NavajoConfig config) throws MappableException, UserException {
	}

	public void store() throws MappableException, UserException {
	}

	public boolean isBusy() {
		return busy;
	}

}
