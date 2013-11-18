package ru.dark32.chat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

public class Chanel {

	private String				name;
	private String				format;
	private double				range;
	@Deprecated
	private int					id;
	private int					subId;
	private Material			material;
	private int					index;										
	private static int			values	= 0;							
	private char				prefix;
	private char				sign;
	private Map<String, String>	custom	= new HashMap<String, String>();

	public Chanel(String name, String format ){
		setName(name);
		setFormat(format);
		index = values;
		values++;
	}

	public Chanel(String name, String format, String prefix, char sign ){
		this(name, format);
		setPrefix(prefix);
		setSign(sign);
	}

	public Chanel(String name, String format, char prefix, char sign ){
		this(name, format);
		this.prefix = prefix;
		setSign(sign);
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

	public int getIndex() {
		return index;
	}

	/***
	 * На случай неправильной расстановки индексо каналов.
	 * 
	 * @param i
	 *            - новый индекс канала
	 ***/
	@Deprecated
	public void setIndex(int i ) {
		index = i;
	}

	public static int getValues() {
		return values;
	}

	public char getPrefix() {
		return prefix;
	}
	public String getFirstLetter() {
		return String.valueOf(prefix);
	}

	public void setPrefix(String key ) {
		this.prefix = Main.config.getString(key, key).charAt(0);
	}

	public static Chanel getByIndex(int i ) {
		switch (i) {
			case -1: /* mute */
				return null;
			case 0:
				return ValueStorage.global;
			case 1:
				return ValueStorage.world;
			case 2:
				return ValueStorage.shout;
			case 3:
				return ValueStorage.local;
			case 4:
				return ValueStorage.whisper;
			case 5:
				return ValueStorage.pm;
			case 6:
				return ValueStorage.chance;
			case 7:
				return ValueStorage.broadcast;
			default:
				return null;
		}
	}

	public static int getIndexByPrefix(char ch ) {
		if (ch == ValueStorage.global.getPrefix()) {
			return ValueStorage.global.index;
		} else if (ch == ValueStorage.world.getPrefix()) {
			return ValueStorage.world.index;
		} else if (ch == ValueStorage.shout.getPrefix()) {
			return ValueStorage.shout.index;
		} else if (ch == ValueStorage.local.getPrefix()) {
			return ValueStorage.local.index;
		} else if (ch == ValueStorage.whisper.getPrefix()) {
			return ValueStorage.whisper.index;
		} else if (ch == ValueStorage.pm.getPrefix()) {
			return ValueStorage.pm.index;
		} else if (ch == ValueStorage.chance.getPrefix()) {
			return ValueStorage.chance.index;
		} else if (ch == ValueStorage.broadcast.getPrefix()) {
			return ValueStorage.broadcast.index;
		} else if (ch == '%') {
			return -1;
		} else {
			return -2;
		}
	}

	public static int getIndexBySign(char ch ) {
		if (ch == ValueStorage.global.sign) {
			return ValueStorage.global.index;
		} else if (ch == ValueStorage.world.sign) {
			return ValueStorage.world.index;
		} else if (ch == ValueStorage.shout.sign) {
			return ValueStorage.shout.index;
		} else if (ch == ValueStorage.local.sign) {
			return ValueStorage.local.index;
		} else if (ch == ValueStorage.whisper.sign) {
			return ValueStorage.whisper.index;
		} else if (ch == ValueStorage.pm.sign) {
			return ValueStorage.pm.index;
		} else if (ch == ValueStorage.chance.sign) {
			return ValueStorage.chance.index;
		} else if (ch == ValueStorage.broadcast.sign) {
			return ValueStorage.broadcast.index;
		} else if (ch == '%') {
			return -1;
		} else {
			return -2;
		}
	}

	public char getSign() {
		return sign;
	}
	public void setSign(char sign ) {
		this.sign = sign;
	}

	public void set(String key, String value ) {
		custom.put(key, value);
	}

	public String get(String key ) {
		return custom.containsKey(key) ? custom.get(key) : "key error";
	}


}