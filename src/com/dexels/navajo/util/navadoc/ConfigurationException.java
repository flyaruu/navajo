package com.dexels.navajo.util.navadoc;


/**
 * <p>Title: NavaDoc ConfigurationException</p>
 * <p>Description: Configuration Exception for Navajo
 * Services automated docomentation configuration and configurator</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Matthew Eichler
 * @version $Id$
 */

public class ConfigurationException extends Exception {

  private String configUri = null;

  public ConfigurationException( String msg ) {
    super( msg );
  }

  public ConfigurationException( String msg, String uri ) {
    super( msg );
    this.configUri = uri;
  }

  public void setConfigUri( String uri ) {
    this.configUri = uri;
  }

  public String getConfigUri() {
    return ( this.configUri );
  }

  public String toString() {
    if ( ( configUri != null ) &&
         ( configUri.length() > 0 ) ) {
      return ( this.getClass() + ": [" + this.configUri + "] " + this.getMessage() );
    } else {
      return ( this.getClass() + ": " + this.getMessage() );
    }
  }

} // public class ConfigurationException
