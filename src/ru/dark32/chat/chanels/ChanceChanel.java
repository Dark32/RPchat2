package ru.dark32.chat.chanels;

import java.util.Random;

import org.bukkit.entity.Player;

import ru.dark32.chat.Util;
import ru.dark32.chat.ichanels.IChanceChanel;

public class ChanceChanel extends RangeChanel implements IChanceChanel {

	private String	formatRoll;
	private String	luck;
	private String	unluck;
	private int		chance;
	private int		min;

	@Override
	public void setFormatRoll(final String format ) {
		formatRoll = format;
	}

	@Override
	public void setLuckUnLuck(final String luck, final String unluck ) {
		this.luck = luck;
		this.unluck = unluck;
	}

	@Override
	public void setChance(final int chance, final int min ) {
		this.chance = chance;
		this.min = min;
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
	public String preformat(Player sender, String message ) {
		int iChance;
		if (Util.isInteger(message)) {
			iChance = message.length() < 5 ? Integer.parseInt(message) : 9999;
			iChance = iChance > getMinRoll() ? iChance : getMinRoll();
			final int iRoll = rand.nextInt(iChance) + 1;
			message = getFormatRoll().replace("$1", String.valueOf(iRoll)).replace("$2",
					String.valueOf(iChance));

		} else {
			message = ChanelRegister.colorize("&5" + message);
			final int chance = rand.nextInt(100);
			message = ChanelRegister.colorize(
					getFormatLuck().replace("$1",
							(chance > getChance()) ? getLuck() : getUnLuck())).replace("$msg",
					message);
		}
		return message;
	}

	@Override
	public String getFormatLuck() {
		return formatLuck;
	}

	@Override
	public void setFormatLuck(String format ) {
		formatLuck = format;
	}

	@Override
	public String format(final Player player, final String msg ) {
		return super.format(player, msg);
	}
}
