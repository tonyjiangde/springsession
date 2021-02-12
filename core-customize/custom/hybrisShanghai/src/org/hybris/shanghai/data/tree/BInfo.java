/**
 *
 */
package org.hybris.shanghai.data.tree;

/**
 * @author i314119
 *
 */
public class BInfo
{
	/** Default serialVersionUID value. */

	private static final long serialVersionUID = 1L;

	/**
	 * <i>Generated property</i> for <code>MyBeanInfo.name</code> property defined at extension
	 * <code>hybrisShanghai</code>.
	 */

	private String name;

	/**
	 * <i>Generated property</i> for <code>MyBeanInfo.classname</code> property defined at extension
	 * <code>hybrisShanghai</code>.
	 */

	private String classname;

	/**
	 * <i>Generated property</i> for <code>MyBeanInfo.alias</code> property defined at extension
	 * <code>hybrisShanghai</code>.
	 */

	private String[] alias;

	/**
	 * <i>Generated property</i> for <code>MyBeanInfo.scope</code> property defined at extension
	 * <code>hybrisShanghai</code>.
	 */

	private String scope;

	/**
	 * <i>Generated property</i> for <code>MyBeanInfo.isSingleton</code> property defined at extension
	 * <code>hybrisShanghai</code>.
	 */

	private boolean isSingleton;

	/**
	 * <i>Generated property</i> for <code>MyBeanInfo.isAbstract</code> property defined at extension
	 * <code>hybrisShanghai</code>.
	 */

	private boolean isAbstract;

	public BInfo()
	{
		// default constructor
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setClassname(final String classname)
	{
		this.classname = classname;
	}

	public String getClassname()
	{
		return classname;
	}

	public void setAlias(final String[] alias)
	{
		this.alias = alias;
	}

	public String[] getAlias()
	{
		return alias;
	}

	public void setScope(final String scope)
	{
		this.scope = scope;
	}

	public String getScope()
	{
		return scope;
	}

	public void setIsSingleton(final boolean isSingleton)
	{
		this.isSingleton = isSingleton;
	}

	public boolean isIsSingleton()
	{
		return isSingleton;
	}

	public void setIsAbstract(final boolean isAbstract)
	{
		this.isAbstract = isAbstract;
	}

	public boolean isIsAbstract()
	{
		return isAbstract;
	}
}
