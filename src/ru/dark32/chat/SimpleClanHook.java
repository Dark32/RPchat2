package ru.dark32.chat;

import org.bukkit.entity.Player;

import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.rank.Rank;

public class SimpleClanHook {
	private final static String	clanVariable	= "$SC.clan";
	private final static String	rankVariable	= "$SC.rank";

	public static String formatComplete(String format, Player player ) {
		ClanPlayer clanPlayer = Main.getClanPlayerManager().getClanPlayer(player);
		if (clanPlayer != null) {
			Clan clan = clanPlayer.getClan();
			Rank rank = clanPlayer.getRank();
			String clanTag = rank != null ? rank.getTag() : "";
			String rankTag = clan != null ? clanTag = clan.getTag() : "";
			if (format.contains(clanVariable)) {
				format = format.replace(clanVariable, clanTag);
			}
			if (format.contains(rankVariable)) {
				format = format.replace(rankVariable, rankTag);
			}
		}
		return ChanelRegister.colorUTF8(format, 3);
	}

	public static boolean equalClan(Player sender, Player target ) {
		ClanPlayer clanSenderPlayer = Main.getClanPlayerManager().getClanPlayer(sender);
		ClanPlayer clanTargetPlayer = Main.getClanPlayerManager().getClanPlayer(target);
		if (clanSenderPlayer != null && clanTargetPlayer != null) {
			Clan clanSender = clanSenderPlayer.getClan();
			Clan clanTarget = clanTargetPlayer.getClan();
			return (clanSender != null && clanTarget != null) ? clanSender.getID() == clanTarget.getID() : false;
		}
		return false;
	}
}
