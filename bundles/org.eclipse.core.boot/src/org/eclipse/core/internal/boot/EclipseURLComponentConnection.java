package org.eclipse.core.internal.boot;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */

/**
 * Eclipse URL support
 * eclipse:/component/<identifeir>/	maps to component installation location
 */

import java.net.*;
import java.io.*;
import java.util.*;
 
public class EclipseURLComponentConnection extends EclipseURLConnection {

	// component/ protocol
	public static final String COMP = "component";
	public static final String COMP_URL_STRING = EclipseURLHandler.ECLIPSE+EclipseURLHandler.PROTOCOL_SEPARATOR+"/"+COMP+"/";
	
	private static final String COMP_INSTALL = "install/components/";
	private static URL installURL;
public EclipseURLComponentConnection(URL url) {
	super(url);
}
protected boolean allowCaching() {
	return true;
}
protected URL resolve() throws IOException {
	
	String spec = url.getFile().trim();
	if (spec.startsWith("/")) spec = spec.substring(1);

	if (!spec.startsWith(COMP+"/")) throw new IOException("Unsupported protocol variation "+url.toString());

	int ix = spec.indexOf("/",COMP.length()+1);
	String name = ix==-1 ? spec.substring(COMP.length()+1) : spec.substring(COMP.length()+1,ix);
	URL result = (ix==-1 || (ix+1)>=spec.length()) ? new URL(installURL,COMP_INSTALL+name+"/") : new URL(installURL,COMP_INSTALL+name+"/"+spec.substring(ix+1));
	return result;
}
public static void startup(URL url) {
	
	// register connection type for eclipse:/configuration/ handling
	if (installURL!=null) return;
	installURL = url;
	EclipseURLHandler.register(COMP, EclipseURLComponentConnection.class);
}
}
