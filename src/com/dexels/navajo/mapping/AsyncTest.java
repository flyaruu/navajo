package com.dexels.navajo.mapping;

/**
 * <p>Title: Navajo Product Project</p>
 * <p>Description: This is the official source for the Navajo server</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Dexels BV</p>
 * @author Arjen Schoneveld
 * @version 1.0
 */

import com.dexels.navajo.document.*;
import com.dexels.navajo.server.*;

public class AsyncTest extends AsyncMappable {

  public double result = 0.0;
  public double d = 1.0;
  public int iter = 1000000;

  private float ready = (float) 0.0;

  public void load(Parameters parms, Navajo inMessage, Access access, NavajoConfig config) throws com.dexels.navajo.server.UserException, com.dexels.navajo.mapping.MappableException {
    System.out.println("in AsyncTest load()");
  }

  public void kill() {
    System.out.println("in AsyncTest kill()");
  }

  public void store() throws com.dexels.navajo.server.UserException, com.dexels.navajo.mapping.MappableException {
    System.out.println("in AsyncTest store()");
  }

  public void setIter(int i) {
    this.iter = i;
  }

  public void setD(double d) {
    System.out.println("in AsyncTest setD(), d = " + d);
    this.d = d;
  }

  public double getResult() {
    System.out.println("in AsyncTest getResult()");
    return result;
  }

  public void run() throws com.dexels.navajo.server.UserException {
      System.out.println("in AsyncTest run()");
      double a = 1000000000.0;
      for (int i = 0; i < iter; i++) {
        a = a/d;
        ready = (float) i / (float) (iter+1) * 100;
        if (this.isStopped()) {
          System.out.println("KILLING THREAD...");
          i = iter + 1;
        } else if (this.isInterrupted()) {
          goToSleep();
        }
        if (i % 10000 == 0)
          System.out.print(".");
        result = a;
      }
      System.out.println("leaving AsyncTest run()");
  }

  public int getPercReady() {
    return (int) ready;
  }

  public static void main(String [] args) throws Exception {
    AsyncStore store = AsyncStore.getInstance((float) 10000.0);
    AsyncTest t = new AsyncTest();
    String ref = store.addInstance(t);
    Navajo n = NavajoFactory.getInstance().createNavajo();
    t.afterReload("map",ref);
    t.setD(1.5);
    t.runThread();
    System.out.println(n.toString());
    System.out.println("STARTED THREAD, WAITING...., ref = " + ref);
    AsyncMappable a =  store.getInstance(ref);
    while (!a.isFinished(n, null));
    System.out.println("RESULT: " + ((AsyncTest) a).getResult());
    System.out.println(n.toString());

  }
}