package ru.dark32.chat.chanels;

import java.util.Random;

import org.bukkit.entity.Player;

import ru.dark32.chat.ChanelRegister;
import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ichanels.IChanceChanel;

@Deprecated
public class ChanceChanel extends RangeChanel implements IChanceChanel {
	private String	formatRoll;
	private String	luck;
	private String	unluck;
	private int		chance;
	private int		min;

	public ChanceChanel(String name ){
		super(name);
		this.chance = Main.chatConfig.getInt("Chat." + name + ".chance", 50);
		this.min = Main.chatConfig.getInt("Chat." + name + ".min", 5);
		this.luck = Main.chatConfig.getString("Chat." + name + ".luck", "Chat." + name + ".luck");
		this.unluck = Main.chatConfig.getString("Chat." + name + ".unluck", "Chat." + name + ".unluck");
		this.formatRoll = Main.chatConfig.getString("Chat." + name + ".formatroll", "Chat." + name + ".formatroll");
		this.formatLuck = Main.chatConfig.getString("Chat." + name + ".formatLuck", "Chat." + name + ".formatLuck");
	}

	@Override
	public String getFormatRoll() {
		return formatRoll;
	}

	@Override
	public String getLuck() {
		return luck;
	}

	@Override
	public String getUnLuck() {
		return unluck;
	}

	@Override
	public int getMinRoll() {
		return min;
	}

	@Override
	public int getChance() {
		return chance;
	}

	final private Random	rand	= new Random();
	private String			formatLuck;

	@Override
	public String preformatMessage(final Player sender, String message ) {
		int iChance;
		if (Util.isInteger(message)) {
			iChance = message.length() < 5 ? Integer.parseInt(message) : 9999;
			iChance = iChance > getMinRoll() ? iChance : getMinRoll();
			final int iRoll = rand.nextInt(iChance) + 1;
			message = getFormatRoll().replace("$1", String.valueOf(iRoll)).replace("$2", String.valueOf(iChance));

		} else {
			message = ChanelRegister.colorize("&5" + message);
			final int chance = rand.nextInt(100);
			message = ChanelRegister.colorize(
					getFormatLuck().replace("$1", (chance > getChance()) ? getLuck() : getUnLuck())).replace("$msg",
					message);
		}
		return message;
	}

	@Override
	public String getFormatLuck() {
		return formatLuck;
	}
}
