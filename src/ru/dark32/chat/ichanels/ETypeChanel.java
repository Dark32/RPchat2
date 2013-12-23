package ru.dark32.chat.ichanels;

import java.util.Locale;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;

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
			public IChanel setChanel(final String name ) {
				IChanel chanel = new BaseChanel();
				ETypeChanel.setBase(chanel, name);
				return chanel;

			}
		},
		RANGE {
			@Override
			public IChanel setChanel(final String name ) {
				IChanel chanel = new RangeChanel();
				ETypeChanel.setBase(chanel, name);
				((IRangeChanel) chanel).setRange(Main.chatConfig.getInt("Chat." + name + ".range", 200));
				return chanel;
			}
		},
		ITEM {
			@Override
			public IChanel setChanel(final String name ) {
				IChanel chanel = new ItemChanel();
				ETypeChanel.setBase(chanel, name);
				ETypeChanel.setItem(chanel, name);
				return chanel;
			}
		},
		PM {
			@Override
			public IChanel setChanel(final String name ) {
				IChanel chanel = new PersonalMessageChanel();
				ETypeChanel.setBase(chanel, name);
				((IPersonalMessagesChanel) chanel).setFormatTo(Main.chatConfig.getString("Chat." + name + ".formatTo",
						"Chat." + name + ".formatTo"));
				((IPersonalMessagesChanel) chanel).setFormatFrom(Main.chatConfig.getString("Chat." + name + ".formatFrom",
						"Chat." + name + ".formatFrom"));
				((IPersonalMessagesChanel) chanel).setFormatSpy(Main.chatConfig.getString("Chat." + name + ".formatSpy",
						"Chat." + name + ".formatSpy"));
				((IPersonalMessagesChanel) chanel).setPmSearchNickMode(Main.chatConfig.getInt("Chat." + name
						+ ".PMSearchNickMode", 0));
				return chanel;
			}
		},
		RANGE_ITEM {
			@Override
			public IChanel setChanel(final String name ) {
				IChanel chanel = new RangeItemChanel();
				ETypeChanel.setBase(chanel, name);
				ETypeChanel.setItem(chanel, name);
				((IRangeChanel) chanel).setRange(Main.chatConfig.getInt("Chat." + name + ".range"));

				return chanel;
			}
		},
		REQUISITE {
			@SuppressWarnings("deprecation" )
			@Override
			public IChanel setChanel(final String name ) {
				IChanel chanel = new RangeRequisiteItemChanel();
				ETypeChanel.setBase(chanel, name);
				ETypeChanel.setItem(chanel, name);
				((IRangeChanel) chanel).setRange(Main.chatConfig.getInt("Chat." + name + ".range"));
				((IRangeRequisiteItemChanel) chanel).setRequiseteItemId(Main.chatConfig.getInt("Chat." + name
						+ ".requisete.id", ((IItemChanel) chanel).getItemId()));
				((IRangeRequisiteItemChanel) chanel).setRequiseteItemSubId(Main.chatConfig.getInt("Chat." + name
						+ ".requisete.subid", ((IItemChanel) chanel).getItemSubId()));
				((IRangeRequisiteItemChanel) chanel).setRequiseteItemAmount(Main.chatConfig.getInt("Chat." + name
						+ ".requisete.amount", 0));
				((IRangeRequisiteItemChanel) chanel).setRequiseteItemMaterial(Material.getMaterial(Main.chatConfig
						.getString("Chat." + name + ".requisete.material", ((IItemChanel) chanel).getItemMaterial()
								.name())));
				return chanel;
			}
		},
		CHANCE {

			@Override
			public IChanel setChanel(final String name ) {
				IChanel chanel = new ChanceChanel();
				ETypeChanel.setBase(chanel, name);
				((IChanceChanel) chanel).setRange(Main.chatConfig.getInt("Chat." + name + ".range", 200));
				((IChanceChanel) chanel).setChance(Main.chatConfig.getInt("Chat." + name + ".chance", 50),
						Main.chatConfig.getInt("Chat." + name + ".min", 5));
				((IChanceChanel) chanel).setLuckUnLuck(
						Main.chatConfig.getString("Chat." + name + ".luck", "Chat." + name + ".luck"),
						Main.chatConfig.getString("Chat." + name + ".unluck", "Chat." + name + ".unluck"));
				((IChanceChanel) chanel).setFormatRoll(Main.chatConfig.getString("Chat." + name + ".formatroll", "Chat."
						+ name + ".formatroll"));
				((IChanceChanel) chanel).setFormatLuck(Main.chatConfig.getString("Chat." + name + ".formatLuck", "Chat."
						+ name + ".formatLuck"));
				return chanel;
			}
		},
		BROAD {

			@Override
			public IChanel setChanel(final String name ) {
				IChanel chanel = new BroadChanel();
				ETypeChanel.setBase(chanel, name);
				((IBroadChanel) chanel).setPattern(Main.chatConfig.getStringList("Chat." + name + ".pattern"));
				return chanel;
			}
		},
		NONE {
			@Override
			public IChanel setChanel(final String name ) {
				return null;
			}
		};
	public static ETypeChanel get(final String type ) {
		ETypeChanel result = ETypeChanel.valueOf(type.toUpperCase(Locale.US));
		return result != null ? result : NONE;
	}

	/**
	 * 
	 */
	public abstract IChanel setChanel(final String name );

	private static IChanel setBase(final IChanel chanel, final String name ) {
		chanel.setIndex(ChanelRegister.getNextIndex());
		chanel.setName(Main.chatConfig.getString("Chat." + name + ".name", "Chat." + name + ".name"));
		chanel.setFormat(Main.chatConfig.getString("Chat." + name + ".format", "Chat." + name + ".format"));
		chanel.setEnable(Main.chatConfig.getBoolean("Chat." + name + ".enable", false));
		chanel.setWorldChat(Main.chatConfig.getBoolean("Chat." + name + ".world", false));
		chanel.setTabes(Main.chatConfig.getBoolean("Chat." + name + ".tab", true));
		chanel.setPrefix(Main.chatConfig.getString("Chat." + name + ".prefix", "Chat." + name + ".prefix"));
		chanel.setSign(Main.chatConfig.getString("Chat." + name + ".sign", "Chat." + name + ".sign").charAt(0));
		chanel.setListenerMessage(
				Main.chatConfig.getString("Chat." + name + ".listenerMessage", "Chat." + name + ".listenerMessage"),
				Main.chatConfig.getString("Chat." + name + ".noListenerMessage", "Chat." + name + ".noListenerMessage"),
				Main.chatConfig.getBoolean("Chat." + name + ".isListenerMessage", false));
		chanel.setNeedPerm(Main.chatConfig.getBoolean("Chat." + name + ".needPerm", false));
		String note = Main.chatConfig.getString("Chat." + name + ".pimk.note", "1F#");
		int octava = note.charAt(0);
		Note.Tone tone = Note.Tone.F;
		boolean sharped = false;
		if (note.length() >= 2 && note.length() <= 3) {
			char char0 = note.charAt(0);
			char char1 = note.charAt(1);
			octava = (char0 == '2') ? 2 : (char0 == '0' ? 0 : char0 == '1' ? 1 : 1);
			tone = ('A' <= char1 && 'F' >= char1) ? Note.Tone.valueOf(String.valueOf(char1)) : Note.Tone.F;
			sharped = (note.length() == 3 && note.charAt(1) == '#');
		}
		chanel.setPimk(Main.chatConfig.getBoolean("Chat." + name + ".pimk.enable", false), Instrument.valueOf(Main.chatConfig
				.getString("Chat." + name + ".pimk.instrument", "PIANO")), new Note(octava, tone, sharped));
		chanel.setInnerName(name);
		return chanel;
	}

	@SuppressWarnings("deprecation" )
	private static IChanel setItem(final IChanel chanel, final String name ) {
		((IItemChanel) chanel).setItemId(Main.chatConfig.getInt("Chat." + name + ".item.id", 0));
		((IItemChanel) chanel).setItemSubId(Main.chatConfig.getInt("Chat." + name + ".item.subid", 0));
		((IItemChanel) chanel).setItemAmount(Main.chatConfig.getInt("Chat." + name + ".item.amount", 1));
		((IItemChanel) chanel).setItemMaterial(Material.getMaterial(Main.chatConfig.getString("Chat." + name
				+ ".item.material", "AIR")));
		((IItemChanel) chanel).setRequestPprefix(Main.chatConfig.getBoolean("Chat." + name + ".requestPrefix", true));

		return chanel;
	}
}
