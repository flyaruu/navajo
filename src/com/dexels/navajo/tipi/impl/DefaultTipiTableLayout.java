package com.dexels.navajo.tipi.impl;

import com.dexels.navajo.tipi.*;
import com.dexels.navajo.tipi.components.*;
import nanoxml.*;
import com.dexels.navajo.document.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DefaultTipiTableLayout
    extends TipiLayout {
  private XMLElement myElement = null;
  private int label_width = 50;

  public DefaultTipiTableLayout() {
  }

  public void createLayout(TipiContext context, Tipi current, XMLElement myElement, Navajo n) throws TipiException {
    this.myElement = myElement;
    parseTable(context, current, myElement);
  }

  private void parseTable(TipiContext context, Tipi current, XMLElement table) throws TipiException {
    TipiTableLayout layout = new TipiTableLayout();
    Container con = current.getContainer();
    current.setContainerLayout(layout);
    TipiTableLayout l = layout;
    Map columnAttributes = new HashMap();
    Vector rows = table.getChildren();
    /** @todo ANOTHER UGLY CONSTRuCTION */
    for (int r = 0; r < rows.size(); r++) {
      XMLElement row = (XMLElement) rows.elementAt(r);
      l.startRow();
      Vector columns = row.getChildren();
      for (int c = 0; c < columns.size(); c++) {
        XMLElement column = (XMLElement) columns.elementAt(c);
        Enumeration attributes = column.enumerateAttributeNames();
        while (attributes.hasMoreElements()) {
          String attrName = (String) attributes.nextElement();
          columnAttributes.put(attrName, column.getStringAttribute(attrName));
        }
        l.startColumn();
        if (column.countChildren() > 1 || column.countChildren() == 0) {
          throw new TipiException(
              "More then one, or no children found inside <td>");
        }
        else {
          XMLElement component = (XMLElement) column.getChildren().elementAt(0);
          String componentName = component.getName();
          String cname = (String) component.getAttribute("name");
          if (componentName.equals("tipi-instance")) {
            current.addTipiInstance(context,columnAttributes,component);
          }
          if (componentName.equals("property")) {
            BasePropertyComponent pc = new BasePropertyComponent();

            pc.addTipiEventListener(current);

            String propertyName = (String) component.getAttribute("name");
            String lw = (String) columnAttributes.get("labelwidth");
            if (lw != null) {
              label_width = Integer.parseInt(lw);
            }
            pc.setLabelWidth(label_width);
            pc.load(component, component, context);
            current.addProperty(propertyName, pc, context, columnAttributes);
          }
          if (componentName.equals("component")) {
            BaseComponent pc = new BaseComponent();
//            String propertyName = (String) component.getAttribute("name");
            pc.load(component, null, context);
            current.addComponent(pc, context, columnAttributes);
          }
          if (componentName.equals("method")) {
            MethodComponent pc = new DefaultMethodComponent();
            pc.load(component, null, current, context);
            current.addComponent(pc, context, columnAttributes);
          }
//          if (componentName.equals("button-instance")) {
//            String buttonName = (String) component.getAttribute("name");
//            TipiButton pc = context.instantiateTipiButton(buttonName, component, current);
//            current.addComponent(pc, context, columnAttributes);
//          }
          if (componentName.equals("component-instance")) {

//            String buttonName = (String) component.getAttribute("name");
            current.addComponentInstance(context,component,columnAttributes);
//            TipiButton pc = context.instantiateTipiButton(buttonName, component, current);
//            current.addComponent(pc, context, columnAttributes);
          }
        }
        columnAttributes.clear();
        l.endColumn();
      }
      l.endRow();
    }
  }

  public boolean needReCreate() {
    return false;
  }

  public void reCreateLayout(TipiContext context, Tipi t, Navajo n) throws TipiException {
    t.clearProperties();
    createLayout(context, t, myElement, n);
  }

}