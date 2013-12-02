/**
 * 
 */
package ru.dark32.chat.ichanels;

import org.bukkit.Material;

import ru.dark32.chat.Main;
import ru.dark32.chat.chanels.BaseChanel;
import ru.dark32.chat.chanels.ChanelRegister;
import ru.dark32.chat.chanels.ItemChanel;
import ru.dark32.chat.chanels.PersonalMessageChanel;
import ru.dark32.chat.chanels.RangeChanel;
import ru.dark32.chat.chanels.RangeItemChanel;

/**
 * @author Andrew
 * 
 */
public enum ETypeChanel {
		BASE {
			public IChanel setChanel(String name ) {
				IChanel chanel = new BaseChanel();
				chanel = setBase(chanel, name);
				return chanel;

			}
		},
		RANGE {
			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new RangeChanel();
				chanel = setBase(chanel, name);
				((IRangeChanel) chanel).setRange(Main.config.getInt("Chat." + name + ".range"));
				return chanel;
			}
		},
		ITEM {
			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new ItemChanel();
				chanel = setBase(chanel, name);
				((IItemChanel) chanel).setItemId(Main.config.getInt("Chat." + name + ".Id", 0));
				((IItemChanel) chanel).setSubId(Main.config.getInt("Chat." + name + ".SubId", 0));
				((IItemChanel) chanel).setMaterial(Material.getMaterial(Main.config.getString(
						"Chat." + name + ".Ma", "DIAMOND")));
				return chanel;
			}
		},
		PM {
			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new PersonalMessageChanel();
				chanel = setBase(chanel, name);
				((IPersonalMessagesChanel)chanel).setFormatTo(Main.config.getString("Chat." + name + ".formatTo"));
				((IPersonalMessagesChanel)chanel).setFormatFrom(Main.config.getString("Chat." + name + ".formatFrom"));
				((IPersonalMessagesChanel)chanel).setFormatSpy(Main.config.getString("Chat." + name + ".formatSpy"));
				return chanel;
			}
		},
		RANGE_ITEM {
			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new RangeItemChanel();
				chanel = setBase(chanel, name);
				((IRangeChanel) chanel).setRange(Main.config.getInt("Chat." + name + ".range"));
				((IItemChanel) chanel).setItemId(Main.config.getInt("Chat." + name + ".id", 0));
				((IItemChanel) chanel).setSubId(Main.config.getInt("Chat." + name + ".subid", 0));
				((IItemChanel) chanel).setMaterial(Material.getMaterial(Main.config.getString(
						"Chat." + name + ".id", "DIAMOND")));
				return chanel;
			}
		},
		NONE {
			@Override
			public IChanel setChanel(String name ) {
				return null;
			}
		};
	public static ETypeChanel get(String type ) {
		if (type.equalsIgnoreCase("BASE")) return BASE;
		if (type.equalsIgnoreCase("RANGE")) return RANGE;
		if (type.equalsIgnoreCase("ITEM")) return ITEM;
		if (type.equalsIgnoreCase("PM")) return PM;
		if (type.equalsIgnoreCase("RANGE_ITEM")) return RANGE_ITEM;
		if (type.equalsIgnoreCase("NONE")) return NONE;
		return NONE;
	}

	/**
	 * 
	 */
	public abstract IChanel setChanel(String name );

	public IChanel setBase(IChanel chanel, String name ) {
		chanel.setIndex(ChanelRegister.getNextIndex());
		chanel.setName(Main.config.getString("Chat." + name + ".name"));
		chanel.setFormat("Chat." + name + ".format");
		chanel.setEnable(Main.config.getBoolean("Chat." + name + ".enable", false));
		chanel.setWorldChat(Main.config.getBoolean("Chat." + name + ".world", false));
		chanel.setPrefix("Chat." + name + ".prefix");
		chanel.setSign(Main.config.getString("Chat." + name + ".sign").charAt(0));
		chanel.setInnerName(name);
		return chanel;
	}
}
