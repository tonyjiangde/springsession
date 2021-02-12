/**
 *
 */
package org.hybris.shanghai.facades.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.hybris.shanghai.data.tree.Node;
import org.hybris.shanghai.facades.JavaClassLoaderFacade;
import org.hybris.shanghai.services.JavaClassLoaderService;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author i314119
 *
 */
public class DefaultJavaClassLoaderFacade implements JavaClassLoaderFacade
{


	private JavaClassLoaderService javaClassLoaderService;

	/**
	 * @param javaClassLoaderService
	 *           the javaClassLoaderService to set
	 */
	@Required
	public void setJavaClassLoaderService(final JavaClassLoaderService javaClassLoaderService)
	{
		this.javaClassLoaderService = javaClassLoaderService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.hybris.shanghai.facades.JavaClassLoaderFacade#getPlatformURLs()
	 */
	@Override
	public LinkedHashMap<String, ArrayList<String>> getPlatformURLs()
	{
		// YTODO Auto-generated method stub
		return javaClassLoaderService.getPlatformURLs();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.hybris.shanghai.facades.JavaClassLoaderFacade#getWebAppURLs()
	 */
	@Override
	public LinkedHashMap<String, ArrayList<String>> getWebAppURLs()
	{
		// YTODO Auto-generated method stub
		return javaClassLoaderService.getWebAppURLs();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.hybris.shanghai.facades.JavaClassLoaderFacade#findClass(java.lang.String)
	 */
	@Override
	public Node findClass(final String cn)
	{
		// YTODO Auto-generated method stub
		final LinkedHashMap<String, ArrayList<String>> result = javaClassLoaderService.findClass(cn);
		final Node root = new Node();
		root.setLeaf(false);
		root.setText("Classloader");
		root.setExpanded(true);
		final LinkedHashMap<String, HashSet<Node>> nodes = new LinkedHashMap<String, HashSet<Node>>();
		final Iterator<Entry<String, ArrayList<String>>> iterator = result.entrySet().iterator();
		Entry<String, ArrayList<String>> entry = null;
		final ArrayList<Node> rchildren = new ArrayList<>();
		while (iterator.hasNext())
		{
			entry = iterator.next();
			//System.out.println(entry.getKey() + "=>" + entry.getValue());
			final Node cl = new Node();
			cl.setLeaf(false);
			cl.setText(entry.getKey());
			cl.setExpanded(true);
			final ArrayList<Node> children = new ArrayList<>();
			for (final String url : entry.getValue())
			{
				if (url.contains("=>"))
				{
					final String[] sss = url.split("=>");
					final Node n1 = new Node();
					n1.setLeaf(false);
					n1.setText(sss[0]);
					n1.setExpanded(true);
					final ArrayList<Node> c = new ArrayList<>();
					final Node n2 = new Node();
					n2.setLeaf(true);
					n2.setText(sss[1]);
					n2.setFilename(sss[1].substring(sss[1].lastIndexOf(File.separator) + 1));
					if (nodes.containsKey(n2.getFilename()))
					{
						nodes.get(n2.getFilename()).add(n2);
					}
					else
					{
						final HashSet<Node> ns = new HashSet<>();
						ns.add(n2);
						nodes.put(n2.getFilename(), ns);
					}

					c.add(n2);
					n1.setChildren(c);
					children.add(n1);
				}
				else
				{
					final Node n = new Node();
					n.setLeaf(true);
					n.setText(url);
					if (!url.endsWith(File.separator))
					{
						n.setFilename(url.substring(url.lastIndexOf(File.separator) + 1));
						if (nodes.containsKey(n.getFilename()))
						{
							nodes.get(n.getFilename()).add(n);
						}
						else
						{
							final HashSet<Node> ns = new HashSet<>();
							ns.add(n);
							nodes.put(n.getFilename(), ns);
						}
					}
					n.setExpanded(false);
					children.add(n);
				}

			}
			cl.setChildren(children);
			rchildren.add(cl);
		}
		root.setChildren(rchildren);
		final Iterator<String> it = nodes.keySet().iterator();
		while (it.hasNext())
		{
			final String filename = it.next();
			final HashSet<Node> ns = nodes.get(filename);
			if (ns.size() > 1)
			{
				for (final Node n : ns)
				{
					n.setDuplicated(true);
				}
			}
		}
		return root;
	}

	@Override
	public Node getURLs()
	{
		final Node root = new Node();
		root.setLeaf(false);
		root.setText("Classloader Hierarchy");
		root.setExpanded(true);

		final LinkedHashMap<String, HashSet<Node>> nodes = new LinkedHashMap<String, HashSet<Node>>();

		final Node webapps = new Node();
		webapps.setLeaf(false);
		webapps.setText("Web Apps");
		webapps.setExpanded(false);
		final LinkedHashMap<String, ArrayList<String>> pl2 = javaClassLoaderService.getWebAppURLs();
		final Iterator<Entry<String, ArrayList<String>>> iterator2 = pl2.entrySet().iterator();
		Entry<String, ArrayList<String>> entry2 = null;
		final ArrayList<Node> rchildren2 = new ArrayList<>();
		while (iterator2.hasNext())
		{
			entry2 = iterator2.next();
			//System.out.println(entry.getKey() + "=>" + entry.getValue());
			final Node cl = new Node();
			cl.setLeaf(false);
			cl.setText(entry2.getKey());
			cl.setExpanded(false);
			final ArrayList<Node> children = new ArrayList<>();
			for (final String url : entry2.getValue())
			{
				final Node n = new Node();
				n.setLeaf(true);
				n.setText(url);
				if (!url.endsWith(File.separator))
				{
					n.setFilename(url.substring(url.lastIndexOf(File.separator) + 1));
					if (nodes.containsKey(n.getFilename()))
					{
						nodes.get(n.getFilename()).add(n);
					}
					else
					{
						final HashSet<Node> ns = new HashSet<>();
						ns.add(n);
						nodes.put(n.getFilename(), ns);
					}
				}
				n.setExpanded(false);
				children.add(n);
			}
			cl.setChildren(children);
			rchildren2.add(cl);
		}
		webapps.setChildren(rchildren2);


		final LinkedHashMap<String, ArrayList<String>> pl = javaClassLoaderService.getPlatformURLs();
		final Iterator<Entry<String, ArrayList<String>>> iterator = pl.entrySet().iterator();
		Entry<String, ArrayList<String>> entry = null;
		final ArrayList<Node> rchildren = new ArrayList<>();
		rchildren.add(webapps);
		while (iterator.hasNext())
		{
			entry = iterator.next();
			//System.out.println(entry.getKey() + "=>" + entry.getValue());
			final Node cl = new Node();
			cl.setLeaf(false);
			cl.setText(entry.getKey());
			cl.setExpanded(false);
			final ArrayList<Node> children = new ArrayList<>();
			for (final String url : entry.getValue())
			{
				final Node n = new Node();
				n.setLeaf(true);
				n.setText(url);
				if (!url.endsWith(File.separator))
				{
					n.setFilename(url.substring(url.lastIndexOf(File.separator) + 1));
					if (nodes.containsKey(n.getFilename()))
					{
						nodes.get(n.getFilename()).add(n);
					}
					else
					{
						final HashSet<Node> ns = new HashSet<>();
						ns.add(n);
						nodes.put(n.getFilename(), ns);
					}
				}
				n.setExpanded(false);
				children.add(n);
			}
			cl.setChildren(children);
			rchildren.add(cl);
		}
		root.setChildren(rchildren);

		final Iterator<String> it = nodes.keySet().iterator();
		while (it.hasNext())
		{
			final String filename = it.next();
			final HashSet<Node> ns = nodes.get(filename);
			if (ns.size() > 1)
			{
				for (final Node n : ns)
				{
					n.setDuplicated(true);
				}
			}
		}

		return root;
	}

}
