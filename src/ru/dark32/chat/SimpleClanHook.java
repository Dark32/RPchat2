package ru.dark32.chat;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

import org.bukkit.entity.Player;

public class SimpleClanHook {
	private final static String	ClanVariable	= "$SC.clan";
	private final static String	rankVariable	= "$SC.rank";
	private final static String	KDRVariable		= "$SC.kdr";

	public static String formatComplete(String format, final Player player ) {
		final ClanPlayer clanPlayer = Main.getClanManager().getClanPlayer(player);
		if (clanPlayer != null) {
			if (format.contains(ClanVariable)) {
				final Clan clan = clanPlayer.getClan();
				final String clanTag = clan != null ? clan.getTag() : "";
				format = format.replace(ClanVariable, clanTag);
			}
			if (format.contains(rankVariable)) {
				final String rank = clanPlayer.getRank();
				final String rankTag = rank != null ? rank : "";
				format = format.replace(rankVariable, rankTag);
			}
			if (format.contains(KDRVariable)) {
				final String kdrTag = Float.toString(clanPlayer.getKDR());
				format = format.replace(KDRVariable, kdrTag);
			}
		}
		return ChanelRegister.colorUTF8(format, 3);
	}

	public static boolean equalClan(final Player sender, final Player target ) {
		final ClanPlayer clanSenderPlayer = Main.getClanManager().getClanPlayer(sender);
		final ClanPlayer clanTargetPlayer = Main.getClanManager().getClanPlayer(target);
		if (clanSenderPlayer != null && clanTargetPlayer != null) {
			final Clan clanSender = clanSenderPlayer.getClan();
			final Clan clanTarget = clanTargetPlayer.getClan();
			return (clanSender != null && clanTarget != null) ? clanSender == clanTarget : false;
		}
		return false;
	}

	public static boolean equalAlly(final Player sender, final Player target ) {
		final ClanPlayer clanSenderPlayer = Main.getClanManager().getClanPlayer(sender);
		return clanSenderPlayer.isAlly(target);
	}
}
