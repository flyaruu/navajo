package com.dexels.navajo.util.navadoc;

/**
 * <p>Title: NavaDocConfigurator</p>
 * <p>Description: convenience class for configuration of
 * of the NavaDoc documentation automation runner</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Matthew Eichler
 * @version $Revision$
 */

// our exception class
import com.dexels.navajo.util.navadoc.ConfigurationException;

// typical Java IO stuff
import java.io.File;
import java.util.Properties;

// XML Document & Parssers
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Element;

// logging
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class NavaDocConfigurator {

  private String configUri =
    System.getProperty(
      "configUri", "file:///d:/Projecten/NavaDoc/config/navadoc.xml" );
  private File servicesPath = null;
  private File stylesheetPath = null;

  private Document configDOM = null;
  private Element loggerConfig = null;
  private NodeList docProps = null;

  public NavaDocConfigurator() {
    // nothing!
  }

  public void configure()
    throws ConfigurationException {

    // get configuration as DOM
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      this.configDOM  = dBuilder.parse( this.configUri );
    } catch ( Exception e ) {
      ConfigurationException ce =
        new ConfigurationException( e.toString(), this.configUri );
      throw ( ce );
    }

    this.loggerConfig =
      (Element) this.configDOM.getElementsByTagName( "log4j:configuration" ).item( 0 );

    if ( loggerConfig == null ) {
      ConfigurationException e =
        new ConfigurationException( "logging subsystem log4j is not configured, " +
          "check the file " + this.configUri, this.configUri );
      throw ( e );
    }
    DOMConfigurator.configure( this.loggerConfig );

    Logger.getLogger( this.getClass() ).log( Priority.DEBUG, "started logging" );

    // get NavaDoc configuration from DOM
    Element navConf =
      (Element) this.configDOM.getElementsByTagName( "configuration" ).item( 0 );
    this.docProps = navConf.getElementsByTagName( "property" );

    for ( int i = 0; i < this.docProps.getLength(); i++ ) {
      Node n = this.docProps.item( i );
      NamedNodeMap nMap = n.getAttributes();
      Node nameAttr = nMap.getNamedItem( "name" );
      if ( nameAttr != null ) {
        String name = nameAttr.getNodeValue();
        if ( name.equals( "services-path" ) ) {
          Node valAttr = nMap.getNamedItem( "value" );
          if ( valAttr != null ) {
            String p = valAttr.getNodeValue();
            this.servicesPath = new File( p );
          }
        }
        if ( name.equals( "stylesheet-path" ) ) {
          Node valAttr = nMap.getNamedItem( "value" );
          if ( valAttr != null ) {
            String p = valAttr.getNodeValue();
            this.stylesheetPath = new File( p );
          }
        }
      }

    }

    if ( this.servicesPath == null ) {
      ConfigurationException e =
        new ConfigurationException( "services-path is not configured, " +
          "check the file " + this.configUri, this.configUri );

      throw ( e );
    } else {
      Logger.getLogger( this.getClass() ).log( Priority.DEBUG,
       "services-path = '" + this.servicesPath.toString() + "'" );
    }
    if ( this.stylesheetPath == null ) {
      ConfigurationException e = new ConfigurationException(
          "stylesheet-path is not configured, " +
          "check the file " + this.configUri, this.configUri  );

      throw ( e );
    } else {
      Logger.getLogger( this.getClass() ).log( Priority.DEBUG,
       "stylesheet-path = '" + this.stylesheetPath.toString() + "'" );
    }

  } // Configurator()

  // getters
  public File getServicesPath() { return( this.servicesPath ); }
  public File getStylesheetPath() { return( this.stylesheetPath ); }
  public Document getEntireConfig() { return( this.configDOM ); }
  public Element getLoggerConfig() { return( this.loggerConfig ); }
  public NodeList getAllProperties() { return( this.docProps ); }

} // public class NavaDocConfigurator

// EOF: $RCSfile$ //
