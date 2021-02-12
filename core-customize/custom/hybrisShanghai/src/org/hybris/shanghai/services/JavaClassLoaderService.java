/**
 *
 */
package org.hybris.shanghai.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * @author i314119
 *
 */
public interface JavaClassLoaderService
{
	/*
	 * public static String BOOTSTRAP = "bootstrap_classloader"; public static String SYSTEM = "system_classloader";
	 * public static String COMMON = "common_classloader"; public static String PLATFORM = "platform_classloader"; public
	 * static String WEBAPP = "webapp_classloader";
	 *
	 * private final URLClassLoader platform;
	 *
	 * public JavaClassLoaderService() { platform = (URLClassLoader)
	 * Registry.getApplicationContext().getClass().getClassLoader();
	 *
	 * }
	 *
	 *
	 *
	 * public ArrayList<String> getPlatformURLs() { System.out.println("_________" + platform.getClass().getName());
	 * final ArrayList<String> out = new ArrayList<>(); for (final URL url : platform.getURLs()) {
	 * out.add(url.toString()); System.out.println(url.getPath() + "_________" + url.toString()); }
	 *
	 * return out; }
	 */

	public LinkedHashMap<String, ArrayList<String>> getPlatformURLs();

	public LinkedHashMap<String, ArrayList<String>> getWebAppURLs();

	public LinkedHashMap<String, ArrayList<String>> findClass(final String cn);
}
