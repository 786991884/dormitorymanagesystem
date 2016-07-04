package cn.edu.sxau.dormitorymanage.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 菜单实体
 * 
 */
public class Tmenu implements Serializable {
	private static final long serialVersionUID = 5821401479054953646L;
	private String id;// 菜单id
	private Tmenu tmenu;// 父级菜单
	private String text;// 菜单说明
	private String iconCls;// 菜单样式
	private String url;// 菜单链接
	private Set<Tmenu> tmenus = new HashSet<Tmenu>();// 子级菜单

	public Tmenu() {
	}

	public Tmenu(String id) {
		this.id = id;
	}

	public Tmenu(String id, Tmenu tmenu, String text, String iconCls, String url, Set<Tmenu> tmenus) {
		this.id = id;
		this.tmenu = tmenu;
		this.text = text;
		this.iconCls = iconCls;
		this.url = url;
		this.tmenus = tmenus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Tmenu getTmenu() {
		return tmenu;
	}

	public void setTmenu(Tmenu tmenu) {
		this.tmenu = tmenu;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<Tmenu> getTmenus() {
		return tmenus;
	}

	public void setTmenus(Set<Tmenu> tmenus) {
		this.tmenus = tmenus;
	}
}
