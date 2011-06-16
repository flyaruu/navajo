package com.dexels.navajo.tipi.classdef;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import navajo.ExtensionDefinition;

import com.dexels.navajo.functions.util.FunctionDefinition;
import com.dexels.navajo.tipi.TipiContext;
import com.dexels.navajo.tipi.TipiException;
import com.dexels.navajo.tipi.tipixml.XMLElement;

public final class ClassManager {
	protected final Map<String, XMLElement> tipiClassDefMap = new HashMap<String, XMLElement>();
	private final TipiContext myContext;
	protected final Map<String, List<String>> unresolvedExtensions = new HashMap<String, List<String>>();
	private final Map<String, XMLElement> interfaceMap = new HashMap<String, XMLElement>();
	private final Map<String, FunctionDefinition> functionDefinitionMap = new HashMap<String, FunctionDefinition>();

	private final Map<String, ExtensionDefinition> extensionMapper = new HashMap<String, ExtensionDefinition>();

	
	public ClassManager(TipiContext context) {
		assert(context!=null);
		myContext = context;
	}

	public XMLElement getClassDef(String name) {
		XMLElement xmlElement = tipiClassDefMap.get(name);
		if(xmlElement==null) {
			System.err.println("Missing classdef: "+name);
			System.err.println("tipiClass: "+tipiClassDefMap.keySet());
		}
		return xmlElement;
	}
	
	public XMLElement getAssembledClassDef(String name) throws TipiException {
		XMLElement classDef = getClassDef(name);
		if (classDef == null) {
			throw new TipiException("Error loading class def: " + name);
		}
		
		XMLElement result = null;
		List<XMLElement> interfaces = getInterfacesForClassDef(classDef);
		if(interfaces==null) {
			result = classDef;
		} else {
			interfaces.add(classDef);
			result = assembleClassDefs(interfaces,name);
		}
		return result;
	}


	public Map<String, XMLElement> getClassMap() {
		return tipiClassDefMap;
	}

	public Map<String, FunctionDefinition> getFunctionDefMap() {
		return functionDefinitionMap;
	}
	public void clearClassMap() {
		tipiClassDefMap.clear();
	}

	public final void addTipiClassDefinition(XMLElement xe, ExtensionDefinition ed) {
		String name = (String) xe.getAttribute("name");
		String clas = (String) xe.getAttribute("class");
		System.err.println("Adding class: "+name+" extension: "+ed.getId());
		if(clas==null) {
				interfaceMap.put(name,xe);
		}
		extensionMapper.put(name, ed);
		String extending = (String) xe.getAttribute("implements");
		StringTokenizer st = null;
		List<String> isExtending = null;
		if(extending!=null) {
			st = new StringTokenizer(extending,",");
			isExtending = new LinkedList<String>();
			while(st.hasMoreTokens()) {
				isExtending.add(st.nextToken());
			}
		}
		if(isExtending!=null) {
			unresolvedExtensions.put(name, isExtending);
		}
		// TODO
		tipiClassDefMap.put(name, xe);
	}


	public Class<?> getTipiClass(XMLElement xe) {
		Class<?> cc = null;
		String pack = (String) xe.getAttribute("package");
		String clas = (String) xe.getAttribute("class");
		String name = (String) xe.getAttribute("name");
		String fullDef = pack + "." + clas;
		ExtensionDefinition ed = extensionMapper.get(name);
		try {
			if(ed!=null) {
				ClassLoader cl = ed.getClass().getClassLoader();
				cc = Class.forName(fullDef, true, cl);
				return cc;
			}
			System.err.println("FALLBACK: Loading class without Extension definition");
			cc = Class.forName(fullDef, true, myContext.getClassLoader());
		} catch (ClassNotFoundException ex) {
			System.err.println("Error loading class: " + fullDef);
			ex.printStackTrace();
		} catch (SecurityException ex) {
			System.err.println("Security Error loading class: " + fullDef);
			ex.printStackTrace();

		}
		return cc;
	}

	private List<XMLElement> getInterfacesForClassDef(XMLElement classDef) throws TipiException {
		String extending = classDef.getStringAttribute("implements");
		if(extending!=null) {
			StringTokenizer st = new StringTokenizer(extending,",");
			LinkedList<XMLElement> isExtending = new LinkedList<XMLElement>();
			while(st.hasMoreTokens()) {
				String currentName = st.nextToken();
				XMLElement element = tipiClassDefMap.get(currentName);
				if(element==null) {
					throw new TipiException("Error: ClassDef: "+classDef.getStringAttribute("name")+" has an unknown super interface: "+currentName);
				}
				isExtending.add(element);
			}
			return isExtending;
		}
		return null;
	}
	
	private XMLElement assembleClassDefs(List<XMLElement> interfaces,String name) {
		assert (interfaces!=null);
		assert (interfaces.size()>0);
		if(interfaces.size()==1) {
			// maybe copy?
			return interfaces.get(0);
		}
		ClassModel cl = new ClassModel(name);
		for (XMLElement element : interfaces) {
			cl.addDefinition(element);
		}
		return cl.buildResult();
	}
	


	public Set<String> getClassNameSet() {
		return tipiClassDefMap.keySet();
	}

	public void addFunctionDefinition(String name, FunctionDefinition fd) {
		functionDefinitionMap.put(name, fd);
	}

}
