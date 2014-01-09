package ru.dark32.chat;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

import org.bukkit.entity.Player;

public class SimpleClanHook {
	private final static String	clanVariable	= "$SC.clan";
	private final static String	rankVariable	= "$SC.rank";
	private final static String	KDRVariable		= "$SC.kdr";

	public static String formatComplete(String format, Player player ) {
		ClanPlayer clanPlayer = Main.getClanManager().getClanPlayer(player);
		if (clanPlayer != null) {
			if (format.contains(clanVariable)) {
				Clan clan = clanPlayer.getClan();
				String clanTag = clan != null ? clan.getTag() : "";
				format = format.replace(clanVariable, clanTag);
			}
			if (format.contains(rankVariable)) {
				String rank = clanPlayer.getRank();
				String rankTag = rank != null ? rank : "";
				format = format.replace(rankVariable, rankTag);
			}
			if (format.contains(KDRVariable)) {
				String KDRTag = Float.toString(clanPlayer.getKDR());
				format = format.replace(KDRVariable, KDRTag);
			}
		}
		return ChanelRegister.colorUTF8(format, 3);
	}

	public static boolean equalClan(Player sender, Player target ) {
		ClanPlayer clanSenderPlayer = Main.getClanManager().getClanPlayer(sender);
		ClanPlayer clanTargetPlayer = Main.getClanManager().getClanPlayer(target);
		if (clanSenderPlayer != null && clanTargetPlayer != null) {
			Clan clanSender = clanSenderPlayer.getClan();
			Clan clanTarget = clanTargetPlayer.getClan();
			return (clanSender != null && clanTarget != null) ? clanSender == clanTarget : false;
		}
		return false;
	}

	public static boolean equalAlly(Player sender, Player target ) {
		ClanPlayer clanSenderPlayer = Main.getClanManager().getClanPlayer(sender);
		return clanSenderPlayer.isAlly(target);
	}
}
