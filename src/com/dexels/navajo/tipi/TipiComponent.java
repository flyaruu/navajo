package com.dexels.navajo.tipi;

import nanoxml.*;
import java.util.*;
import java.awt.*;
import com.dexels.navajo.tipi.components.*;
import javax.swing.*;
import com.dexels.navajo.document.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public abstract class TipiComponent implements TipiBase {

  private Container myContainer = null;
  private Container myOuterContainer = null;
  protected ArrayList propertyNames = new ArrayList();
  protected ArrayList properties = new ArrayList();
  protected TipiContext myContext = null;
  protected ArrayList myEventList = new ArrayList();
  protected Navajo myNavajo = null;

  public TipiContext getContext() {
    return myContext;
  }

  public void setContext(TipiContext tc) {
    myContext = tc;
  }

  public void setValue(String s) {
    System.err.println("Setting value of some component....");
  }

  public void addProperty(String name, BasePropertyComponent bpc,TipiContext context, Map contraints) {
 //   System.err.println("Adding property in tipiComponent");
    propertyNames.add(name);
    properties.add(bpc);
    addComponent(bpc,context,contraints);
  }

//  public void addToContainer(Component c) {
//
//  }

  public void addComponent(TipiBase c, TipiContext context, Map td) {
    addToContainer(c.getOuterContainer(),td);
//    if (JInternalFrame.class.isInstance(getContainer())) {
//      ((JInternalFrame)getContainer()).getContentPane().add(c.getOuterContainer());
//    } else {
//      Container cont = getContainer();
//      if(TipiPanel.class.isInstance(cont)){
//        ((TipiPanel)cont).tipiAdd(c.getOuterContainer(), td);
//      }else{
//        cont.add(c.getOuterContainer(), td);
//      }
//    }
  }

  public void addComponentInstance(TipiContext context, XMLElement instance, Map constraints) throws TipiException {
    TipiComponent tc = context.instantiateComponent(instance);
    addComponent(tc,context,constraints);
  }

  public Navajo getNavajo() {
    if(myNavajo != null){
      System.err.println("Getting Navajo: " + myNavajo.toXml().toString());
    }else{
      System.err.println("Getting Navajo NULL");
    }
    return myNavajo;
  }

  public void addTipiEvent(TipiEvent te) {
   myEventList.add(te);
 }

 public void performTipiEvent(int type, String source){

     for(int i=0;i<myEventList.size();i++){
       TipiEvent te = (TipiEvent)myEventList.get(i);
       if(te.getType() == type && te.getSource().equals(source)){
         te.performAction(getNavajo(), source, getContext());
       }
     }
   }

   public void performEvent(TipiEvent te) {
      te.performAction(getNavajo(),te.getSource(),getContext());
    }

    public void performAllEvents(int type) {
      for (int i = 0; i < myEventList.size(); i++) {
        TipiEvent te = (TipiEvent)myEventList.get(i);
        if (te.getType()==type) {
          performEvent(te);
        }
      }
    }


  public Container getContainer() {
    return myContainer;
  }
  public void replaceContainer(Container c) {
    myContainer = c;
  }

  public Container getOuterContainer() {
    if (myOuterContainer==null) {
      return getContainer();
    }
    return myOuterContainer;
  }
  public void setContainer(Container c) {
    if (getContainer()==null) {
      replaceContainer(c);
    }
  }

  public void setOuterContainer(Container c) {
    myOuterContainer = c;
  }

}