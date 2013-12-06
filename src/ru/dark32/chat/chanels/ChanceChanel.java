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
	public void setFormatRoll(String format ) {
		formatRoll = format;
	}

	@Override
	public void setLuckUnLuck(String luck, String unluck ) {
		this.luck = luck;
		this.unluck = unluck;
	}

	@Override
	public void setChance(int chance, int min ) {
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

	private Random	rand	= new Random();

	@Override
	public String preformat(Player sender, String message ) {
		int iChance = 0;
		if (Util.isInteger(message)) {
			iChance = message.length() < 5 ? Integer.parseInt(message) : 9999;
			iChance = iChance > getMinRoll() ? iChance : getMinRoll();
			int iRoll = rand.nextInt(iChance) + 1;
			message = getFormatRoll().replace("$1", String.valueOf(iRoll)).replace("$2",
					String.valueOf(iChance));

		} else {
			message = ChanelRegister.colorize("&5" + message);
			int chance = rand.nextInt(100);
			message = ChanelRegister.colorize(getFormat().replace("$1",
					(chance > getChance()) ? getLuck() : getUnLuck()));
		}
		return message;
	}
}
