package com.dexels.navajo.tipi.components;

import com.dexels.navajo.tipi.components.swing.*;
import javax.swing.*;
import com.dexels.navajo.tipi.tipixml.*;
import com.dexels.navajo.tipi.*;
import java.awt.event.*;
import java.util.*;
import com.dexels.navajo.document.*;
import java.awt.Container;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TipiMenuItem extends SwingTipiComponent {
  private TipiContext myContext = null;
  private ArrayList myEvents = new ArrayList();
  private TipiSwingMenuItem myItem;


  public TipiMenuItem() {
    try {
      initContainer();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

//  public void load(XMLElement definition,XMLElement instance, TipiContext context) throws TipiException{
//    System.err.println("ITEM: "+instance);
//    super.load(definition,instance,context);
//    myContext = context;
//    String name = (String)x.getAttribute("name");
//    ((JMenuItem)getContainer()).setText(name);
//    Vector w = x.getChildren();
//    for (int j = 0; j < w.size(); j++) {
//      XMLElement current = (XMLElement)w.get(j);
//      TipiEvent te = new TipiEvent();
//      te.load(null,current,context);
//      addEvent(te);
//    }
//  }

  public Object getComponentValue(String name){
    System.err.println("-=-=-=-=-=-=-=-=-=-=-=-===>>> Getting componentValue for TipiMenuItem");
    if("text".equals(name)){
      return myItem.getText();
    }
    return super.getComponentValue(name);
  }

  public void addEvent(TipiEvent te) {
    myEvents.add(te);
  }
  private void jbInit() throws Exception {
    ((TipiSwingMenuItem)getContainer()).addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        this_actionPerformed(e);
      }
    });
  }

  void this_actionPerformed(ActionEvent e) {
    try {
      performTipiEvent("onActionPerformed",e);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  public Container createContainer() {
    myItem = new TipiSwingMenuItem(this);
    return myItem;
  }

  public Container getContainer(){
    return myItem;
  }
  public void setComponentValue(String name, Object object) {
      super.setComponentValue(name, object);
      if ("text".equals(name)) {
        myItem.setText((String)object);
      }
      if ("mnemonic".equals(name)) {
        String ch = (String) object;
        char mn = ch.charAt(0);
        myItem.setMnemonic(mn);
      }
      if("accelerator".equals(name)){
        myItem.setAccelerator(KeyStroke.getKeyStroke((String)object));
    }


  }
//  public void load(XMLElement def, XMLElement instance, TipiContext context) throws com.dexels.navajo.tipi.TipiException {
//    super.load(def, instance, context);
//  }
}