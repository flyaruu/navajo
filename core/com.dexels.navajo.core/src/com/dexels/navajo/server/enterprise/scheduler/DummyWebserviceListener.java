package com.dexels.navajo.server.enterprise.scheduler;

import com.dexels.navajo.document.Navajo;
import com.dexels.navajo.server.Access;

public class DummyWebserviceListener implements WebserviceListenerRegistryInterface {

	public boolean afterWebservice(String webservice, Access a) {
		// TODO Auto-generated method stub
		return false;
	}

	public Navajo beforeWebservice(String webservice, Access a) {
		// TODO Auto-generated method stub
		return null;
	}

}