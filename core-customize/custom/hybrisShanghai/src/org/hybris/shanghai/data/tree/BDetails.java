/**
 *
 */
package org.hybris.shanghai.data.tree;

import java.util.List;


/**
 * @author i314119
 *
 */
public class BDetails
{
	private String classloader;
	private String beanid;
	private String contextid;


	/**
	 * @return the beanid
	 */
	public String getBeanid()
	{
		return beanid;
	}

	/**
	 * @param beanid
	 *           the beanid to set
	 */
	public void setBeanid(final String beanid)
	{
		this.beanid = beanid;
	}

	/**
	 * @return the contextid
	 */
	public String getContextid()
	{
		return contextid;
	}

	/**
	 * @param contextid
	 *           the contextid to set
	 */
	public void setContextid(final String contextid)
	{
		this.contextid = contextid;
	}

	/**
	 * @return the classloader
	 */
	public String getClassloader()
	{
		return classloader;
	}

	/**
	 * @param classloader
	 *           the classloader to set
	 */
	public void setClassloader(final String classloader)
	{
		this.classloader = classloader;
	}

	/**
	 * @return the properties
	 */
	public List<PropertyInfo> getProperties()
	{
		return properties;
	}

	/**
	 * @param properties
	 *           the properties to set
	 */
	public void setProperties(final List<PropertyInfo> properties)
	{
		this.properties = properties;
	}

	private List<PropertyInfo> properties;
}
