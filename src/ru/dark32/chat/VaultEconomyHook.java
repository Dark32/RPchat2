package ru.dark32.chat;

import org.bukkit.entity.Player;

public class VaultEconomyHook {

	public static double getBalance(Player player ) {
		if (Main.economyHook) {
			return Main.economy.getBalance(player.getName());
		}
		return 0d;
	}

	public static boolean hasBalance(Player player, double amount ) {
		return (Main.economyHook && Main.economy.has(player.getName(), amount));
	}

	public static void cost(Player player, double amount ) {
		if (hasBalance(player, amount)) {
			Main.economy.withdrawPlayer(player.getName(), amount);
		}

	}
}
