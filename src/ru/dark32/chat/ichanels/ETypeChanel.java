/**
 * 
 */
package ru.dark32.chat.ichanels;

import org.bukkit.Material;

import ru.dark32.chat.Main;
import ru.dark32.chat.chanels.BaseChanel;
import ru.dark32.chat.chanels.BroadChanel;
import ru.dark32.chat.chanels.ChanceChanel;
import ru.dark32.chat.chanels.ChanelRegister;
import ru.dark32.chat.chanels.ItemChanel;
import ru.dark32.chat.chanels.PersonalMessageChanel;
import ru.dark32.chat.chanels.RangeChanel;
import ru.dark32.chat.chanels.RangeItemChanel;
import ru.dark32.chat.chanels.RangeRequisiteItemChanel;

/**
 * @author Andrew
 * 
 */
public enum ETypeChanel {
		BASE {
			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new BaseChanel();
				chanel = ETypeChanel.setBase(chanel, name);
				return chanel;

			}
		},
		RANGE {
			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new RangeChanel();
				chanel = ETypeChanel.setBase(chanel, name);
				((IRangeChanel) chanel).setRange(Main.config.getInt("Chat." + name + ".range"));
				return chanel;
			}
		},
		ITEM {
			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new ItemChanel();
				chanel = ETypeChanel.setBase(chanel, name);
				chanel = ETypeChanel.setItem(chanel, name);
				return chanel;
			}
		},
		PM {
			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new PersonalMessageChanel();
				chanel = ETypeChanel.setBase(chanel, name);
				((IPersonalMessagesChanel) chanel).setFormatTo(Main.config.getString("Chat." + name
						+ ".formatTo"));
				((IPersonalMessagesChanel) chanel).setFormatFrom(Main.config.getString("Chat."
						+ name + ".formatFrom"));
				((IPersonalMessagesChanel) chanel).setFormatSpy(Main.config.getString("Chat."
						+ name + ".formatSpy"));
				((IPersonalMessagesChanel) chanel).setPmSearchNickMode(Main.config.getInt("Chat."
						+ name + ".PMSearchNickMode", 0));
				return chanel;
			}
		},
		RANGE_ITEM {
			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new RangeItemChanel();
				chanel = ETypeChanel.setBase(chanel, name);
				chanel = ETypeChanel.setItem(chanel, name);
				((IRangeChanel) chanel).setRange(Main.config.getInt("Chat." + name + ".range"));

				return chanel;
			}
		},
		REQUISITE {
			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new RangeRequisiteItemChanel();
				chanel = ETypeChanel.setBase(chanel, name);
				chanel = ETypeChanel.setItem(chanel, name);
				((IRangeChanel) chanel).setRange(Main.config.getInt("Chat." + name + ".range"));
				return chanel;
			}
		},
		CHANCE {

			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new ChanceChanel();
				chanel = ETypeChanel.setBase(chanel, name);
				((IChanceChanel) chanel).setRange(Main.config
						.getInt("Chat." + name + ".range", 200));
				((IChanceChanel) chanel).setChance(
						Main.config.getInt("Chat." + name + ".chance", 50),
						Main.config.getInt("Chat." + name + ".min", 5));
				((IChanceChanel) chanel).setLuckUnLuck(
						Main.config.getString("Chat." + name + ".luck"),
						Main.config.getString("Chat." + name + ".unluck"));
				((IChanceChanel) chanel).setFormatRoll(Main.config.getString("Chat." + name
						+ ".formatroll"));
				return null;
			}
		},
		BROAD {

			@Override
			public IChanel setChanel(String name ) {
				IChanel chanel = new BroadChanel();
				chanel = ETypeChanel.setBase(chanel, name);
				((IBroadChanel) chanel).setPattern(Main.config.getStringList("Chat." + name
						+ ".pattern"));
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
		if (type.equalsIgnoreCase("BASE")) {
			return BASE;
		}
		if (type.equalsIgnoreCase("RANGE")) {
			return RANGE;
		}
		if (type.equalsIgnoreCase("ITEM")) {
			return ITEM;
		}
		if (type.equalsIgnoreCase("PM")) {
			return PM;
		}
		if (type.equalsIgnoreCase("RANGE_ITEM")) {
			return RANGE_ITEM;
		}
		if (type.equalsIgnoreCase("REQUISITE")) {
			return REQUISITE;
		}
		if (type.equalsIgnoreCase("CHANCE")) {
			return CHANCE;
		}
		if (type.equalsIgnoreCase("BROAD")) {
			return CHANCE;
		}
		if (type.equalsIgnoreCase("NONE")) {
			return NONE;
		}
		return NONE;
	}

	/**
	 * 
	 */
	public abstract IChanel setChanel(String name );

	private static IChanel setBase(IChanel chanel, String name ) {
		chanel.setIndex(ChanelRegister.getNextIndex());
		chanel.setName(Main.config.getString("Chat." + name + ".name"));
		chanel.setFormat(Main.config.getString("Chat." + name + ".format"));
		chanel.setEnable(Main.config.getBoolean("Chat." + name + ".enable", false));
		chanel.setWorldChat(Main.config.getBoolean("Chat." + name + ".world", false));
		chanel.setTabes(Main.config.getBoolean("Chat." + name + ".tab", true));
		chanel.setPrefix(Main.config.getString("Chat." + name + ".prefix"));
		chanel.setSign(Main.config.getString("Chat." + name + ".sign").charAt(0));
		chanel.setInnerName(name);
		return chanel;
	}

	@SuppressWarnings("deprecation" )
	private static IChanel setItem(IChanel chanel, String name ) {
		((IRangeChanel) chanel).setRange(Main.config.getInt("Chat." + name + ".range"));
		((IItemChanel) chanel).setItemId(Main.config.getInt("Chat." + name + ".id", 0));
		((IItemChanel) chanel).setSubId(Main.config.getInt("Chat." + name + ".subid", 0));
		((IItemChanel) chanel).setMaterial(Material.getMaterial(Main.config.getString("Chat."
				+ name + ".id", "DIAMOND")));
		((IItemChanel) chanel).setRequestPprefix(Main.config.getBoolean("Chat." + name
				+ ".requestPrefix", true));

		return chanel;
	}
}
