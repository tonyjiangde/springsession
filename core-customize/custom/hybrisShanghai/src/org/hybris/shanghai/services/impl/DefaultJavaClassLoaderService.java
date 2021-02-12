/**
 *
 */
package org.hybris.shanghai.services.impl;

import de.hybris.platform.core.Registry;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.catalina.Container;
import org.hybris.shanghai.services.JavaClassLoaderService;



/**
 * @author i314119
 *
 */
public class DefaultJavaClassLoaderService implements JavaClassLoaderService
{

	public static String BOOTSTRAP = "bootstrap_classloader";
	public static String SYSTEM = "system_classloader";
	public static String COMMON = "common_classloader";
	public static String PLATFORM = "platform_classloader";
	public static String WEBAPP = "webapp_classloader";



	public DefaultJavaClassLoaderService()
	{


	}





	@SuppressWarnings("resource")
	@Override
	public LinkedHashMap<String, ArrayList<String>> getPlatformURLs()
	{
		final LinkedHashMap<String, ArrayList<String>> result = new LinkedHashMap<>();
		final URLClassLoader platform = (URLClassLoader) Registry.getApplicationContext().getClass().getClassLoader();
		System.out.println("_________" + platform.getClass().getName());
		final ArrayList<String> out = new ArrayList<>();
		for (final URL url : platform.getURLs())
		{
			out.add(url.toString());
			System.out.println(url.getPath() + "_________" + url.toString());
		}
		result.put(platform.getClass().getName(), out);
		ClassLoader cl = platform.getParent();

		while (cl != null)
		{
			final ArrayList<String> tmp = new ArrayList<>();
			if (cl instanceof URLClassLoader)
			{
				for (final URL url : ((URLClassLoader) cl).getURLs())
				{
					tmp.add(url.toString());
					System.out.println(url.getPath() + "_________" + url.toString());
				}
			}

			result.put(cl.getClass().getName(), tmp);
			cl = cl.getParent();
		}

		return result;
	}

	@Override
	public LinkedHashMap<String, ArrayList<String>> getWebAppURLs()
	{
		final LinkedHashMap<String, ArrayList<String>> result = new LinkedHashMap<>();
		try
		{
			final Field privateField = org.apache.catalina.startup.Bootstrap.class.getDeclaredField("daemon");
			privateField.setAccessible(true);
			final org.apache.catalina.startup.Bootstrap daemon = (org.apache.catalina.startup.Bootstrap) privateField.get(null);

			final Field privateField2 = org.apache.catalina.startup.Bootstrap.class.getDeclaredField("catalinaDaemon");
			privateField2.setAccessible(true);
			final org.apache.catalina.startup.Catalina cata = (org.apache.catalina.startup.Catalina) privateField2.get(daemon);
			final Container[] webContexts = cata.getServer().findService("Catalina").getContainer().findChild("localhost")
					.findChildren();
			for (final Container c : webContexts)
			{
				final ArrayList<String> tmp = new ArrayList<>();
				final org.apache.catalina.core.StandardContext sc = (org.apache.catalina.core.StandardContext) c;
				final ClassLoader cl = sc.getServletContext().getClassLoader();
				if (cl instanceof URLClassLoader)
				{
					for (final URL url : ((URLClassLoader) cl).getURLs())
					{
						tmp.add(url.toString());
						System.out.println(url.getPath() + "_________" + url.toString());
					}
				}
				result.put(c.getName().concat("(" + cl.getClass().getName() + ")"), tmp);
			}

		}
		catch (final Exception e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		//org.apache.catalina.startup.Bootstrap.daemon
		//	.findChildren();


		return result;
	}

	void find_recursively(final File[] my_arr, int my_index, final int sub_level, final String pattern, final ArrayList<String> al)
	{
		final Pattern p = Pattern.compile(pattern);
		if (my_index == my_arr.length)
		{
			return;
		}
		final Matcher m = p.matcher(my_arr[my_index].getAbsolutePath());
		if (my_arr[my_index].isFile() && m.find())
		{
			al.add(my_arr[my_index].getAbsolutePath());
		}
		else if (my_arr[my_index].isDirectory())
		{
			find_recursively(my_arr[my_index].listFiles(), 0, sub_level + 1, pattern, al);
		}
		find_recursively(my_arr, ++my_index, sub_level, pattern, al);
	}

	@Override
	public LinkedHashMap<String, ArrayList<String>> findClass(final String pattern)
	{
		//final String cn = classname;
		final LinkedHashMap<String, ArrayList<String>> result = new LinkedHashMap<>();
		final Pattern p = Pattern.compile(pattern);
		try
		{
			final Field privateField = org.apache.catalina.startup.Bootstrap.class.getDeclaredField("daemon");
			privateField.setAccessible(true);
			final org.apache.catalina.startup.Bootstrap daemon = (org.apache.catalina.startup.Bootstrap) privateField.get(null);

			final Field privateField2 = org.apache.catalina.startup.Bootstrap.class.getDeclaredField("catalinaDaemon");
			privateField2.setAccessible(true);
			final org.apache.catalina.startup.Catalina cata = (org.apache.catalina.startup.Catalina) privateField2.get(daemon);
			final Container[] webContexts = cata.getServer().findService("Catalina").getContainer().findChild("localhost")
					.findChildren();
			for (final Container c : webContexts)
			{
				final ArrayList<String> tmp = new ArrayList<>();
				final org.apache.catalina.core.StandardContext sc = (org.apache.catalina.core.StandardContext) c;
				final ClassLoader cl = sc.getServletContext().getClassLoader();
				if (cl instanceof URLClassLoader)
				{
					for (final URL url : ((URLClassLoader) cl).getURLs())
					{
						System.out.println(url.getPath() + "_________");
						final File f = new File(url.getPath());
						if (f.isFile())
						{
							if (url.getPath().endsWith(".jar"))
							{
								final Matcher m = p.matcher(url.getPath());
								if (m.find())
								{
									tmp.add(url.getPath());
								}
								final JarFile jarFile = new JarFile(url.getPath());
								final Enumeration<JarEntry> e = jarFile.entries();
								while (e.hasMoreElements())
								{
									final JarEntry je = e.nextElement();
									final Matcher m2 = p.matcher(je.getName());
									if (!je.isDirectory() && m2.find())
									{
										tmp.add(url.getPath().concat("=>" + je.getName()));
									}

								}
								jarFile.close();
							}
							else
							{
								final Matcher m = p.matcher(f.getAbsolutePath());
								if (m.find())
								{
									tmp.add(f.getAbsolutePath());
								}

							}

						}
						else if (f.isDirectory())
						{
							System.out.println(url.getPath() + "*********");

							final File[] listOfFiles = f.listFiles();
							/*
							 * for (final File f : listOfFiles) { if (!f.isDirectory() && f.getAbsolutePath().endsWith(cn)) {
							 * System.out.println(f.getAbsolutePath() + "*********"); tmp.add(f.getAbsolutePath()); } }
							 */
							find_recursively(listOfFiles, 0, 0, pattern, tmp);
						}


					}
				}
				if (!tmp.isEmpty())
				{
					result.put(c.getName().concat("(" + cl.getClass().getName() + ")"), tmp);
				}

			}



			final URLClassLoader platform = (URLClassLoader) Registry.getApplicationContext().getClass().getClassLoader();
			System.out.println("_________" + platform.getClass().getName());
			final ArrayList<String> out = new ArrayList<>();
			for (final URL url : platform.getURLs())
			{

				final File f = new File(url.getPath());
				if (f.isFile())
				{
					if (url.getPath().endsWith(".jar"))
					{
						final Matcher m = p.matcher(url.getPath());
						if (m.find())
						{
							out.add(url.getPath());
						}
						final JarFile jarFile = new JarFile(url.getPath());
						final Enumeration<JarEntry> e = jarFile.entries();
						while (e.hasMoreElements())
						{
							final JarEntry je = e.nextElement();
							final Matcher m2 = p.matcher(je.getName());
							if (!je.isDirectory() && m2.find())
							{
								out.add(url.getPath().concat("=>" + je.getName()));
							}

						}
						jarFile.close();
					}
					else
					{
						final Matcher m = p.matcher(f.getAbsolutePath());
						if (m.find())
						{
							out.add(f.getAbsolutePath());
						}

					}

				}
				else if (f.isDirectory())
				{


					final File[] listOfFiles = f.listFiles();
					/*
					 * for (final File f : listOfFiles) { if (!f.isDirectory() && f.getAbsolutePath().endsWith(cn)) {
					 * System.out.println(f.getAbsolutePath() + "*********"); tmp.add(f.getAbsolutePath()); } }
					 */
					find_recursively(listOfFiles, 0, 0, pattern, out);
				}

				/*
				 * if (url.getPath().endsWith(".jar")) { final JarFile jarFile = new JarFile(url.getPath()); final
				 * Enumeration<JarEntry> e = jarFile.entries(); while (e.hasMoreElements()) { final JarEntry je =
				 * e.nextElement(); if (!je.isDirectory() && je.getName().endsWith(cn)) { out.add(url.getPath().concat("=>"
				 * + je.getName())); }
				 *
				 * } jarFile.close(); } else if (url.getPath().endsWith(File.separator)) { System.out.println(url.getPath()
				 * + "*********"); final File folder = new File(url.getPath()); final File[] listOfFiles =
				 * folder.listFiles(); for (final File f : listOfFiles) { if (!f.isDirectory() &&
				 * f.getAbsolutePath().endsWith(cn)) { System.out.println(f.getAbsolutePath() + "*********");
				 * out.add(f.getAbsolutePath()); } } }
				 */

			}
			if (!out.isEmpty())
			{
				result.put(platform.getClass().getName(), out);
			}

			ClassLoader cl = platform.getParent();

			while (cl != null)
			{
				final ArrayList<String> tmp = new ArrayList<>();
				if (cl instanceof URLClassLoader)
				{
					for (final URL url : ((URLClassLoader) cl).getURLs())
					{
						final File f = new File(url.getPath());
						if (f.isFile())
						{
							if (url.getPath().endsWith(".jar"))
							{
								final Matcher m = p.matcher(url.getPath());
								if (m.find())
								{
									tmp.add(url.getPath());
								}
								final JarFile jarFile = new JarFile(url.getPath());
								final Enumeration<JarEntry> e = jarFile.entries();
								while (e.hasMoreElements())
								{
									final JarEntry je = e.nextElement();
									final Matcher m2 = p.matcher(je.getName());
									if (!je.isDirectory() && m2.find())
									{
										tmp.add(url.getPath().concat("=>" + je.getName()));
									}

								}
								jarFile.close();
							}
							else
							{
								final Matcher m = p.matcher(f.getAbsolutePath());
								if (m.find())
								{
									tmp.add(f.getAbsolutePath());
								}

							}

						}
						else if (f.isDirectory())
						{
							System.out.println(url.getPath() + "*********");

							final File[] listOfFiles = f.listFiles();
							/*
							 * for (final File f : listOfFiles) { if (!f.isDirectory() && f.getAbsolutePath().endsWith(cn)) {
							 * System.out.println(f.getAbsolutePath() + "*********"); tmp.add(f.getAbsolutePath()); } }
							 */
							find_recursively(listOfFiles, 0, 0, pattern, tmp);
						}
						/*
						 * if (url.getPath().endsWith(".jar")) { final JarFile jarFile = new JarFile(url.getPath()); final
						 * Enumeration<JarEntry> e = jarFile.entries(); while (e.hasMoreElements()) { final JarEntry je =
						 * e.nextElement(); if (!je.isDirectory() && je.getName().endsWith(cn)) {
						 * tmp.add(url.getPath().concat("=>" + je.getName())); }
						 *
						 * } jarFile.close(); } else if (url.getPath().endsWith(File.separator)) {
						 * System.out.println(url.getPath() + "*********"); final File folder = new File(url.getPath()); final
						 * File[] listOfFiles = folder.listFiles(); for (final File f : listOfFiles) { if (!f.isDirectory() &&
						 * f.getAbsolutePath().endsWith(cn)) { System.out.println(f.getAbsolutePath() + "*********");
						 * tmp.add(f.getAbsolutePath()); } } }
						 */

					}
				}
				if (!tmp.isEmpty())
				{
					result.put(cl.getClass().getName(), tmp);
				}
				cl = cl.getParent();
			}

		}
		catch (final Exception e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		//org.apache.catalina.startup.Bootstrap.daemon
		//	.findChildren();




		return result;
	}

}
