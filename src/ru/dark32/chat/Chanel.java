package ru.dark32.chat;

import org.bukkit.Material;

public class Chanel {
	private String		name;
	private String		format;
	private double		range;
	@Deprecated
	private int			id;
	private int			subId;
	private Material	material;

	public Chanel(String name, String format ){
		setName(name);
		setFormat(format);
	}

	public Chanel(String name, String format, String range ){
		this(name, format);
		setRange(range);
	}

	public String getName() {
		return name;
	}

	public String getFormat() {
		return format;
	}

	public double getRange() {
		return range;
	}

	@Deprecated
	public int getId() {
		return id;
	}

	public int getSubId() {
		return subId;
	}

	public Material getMaterial() {
		return material;
	}

	public void setName(String key ) {
		name = ChatListener.tCC(Main.config.getString(key, key));
	}

	public void setFormat(String key ) {
		format = ChatListener.tCC(Main.config.getString(key, key));
	}

	public void setRange(String key, double def ) {
		range = Main.config.getDouble(key, def);
	}

	@Deprecated
	public void setId(String key, int def ) {
		id = Main.config.getInt(key, def);
	}

	public void setSubId(String key, int def ) {
		subId = Main.config.getInt(key, def);
	}

	public void setMaterial(String key ) {
		material = Material.getMaterial(Main.config.getString(key));;
	}

	public void setRange(String key ) {
		setRange(key, 200d);
	}

	@Deprecated
	public void setId(String key ) {
		setId(key, 200);
	}

	public void setSubId(String key ) {
		setSubId(key, 0);
	}

}