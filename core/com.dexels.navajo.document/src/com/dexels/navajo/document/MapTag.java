package com.dexels.navajo.document;

/**
 * <p>Title: Navajo Product Project</p>
 * <p>Description: This is the official source for the Navajo server</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Dexels BV</p>
 * @author Arjen Schoneveld
 * @version $Id$
 */

public interface MapTag {

  /**
   * Public constants for the map node.
   */
  public static final String MAP_DEFINITION = "map";
  public static final String MAP_CONDITION = "condition";
  public static final String MAP_OBJECT = "object";
  public static final String MAP_REF = "ref";
  public static final String MAP_FILTER = "filter";

  public void addField(FieldTag f);

  public void addParam(ParamTag p);

  public void addMessage(Message m);

  public String getObject();

  public void setObject(String s);

  public String getCondition();

  public void setCondition(String s);

  public String getRefAttribute();

  public void setRefAttribute(String s);

  public String getFilter();

  public void setFilter(String s);

  public Object getRef();

}