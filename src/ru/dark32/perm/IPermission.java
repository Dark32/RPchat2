package ru.dark32.perm;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface IPermission {
	/***
	 * Получить группу
	 * 
	 * @param base
	 * @return
	 */
	String getGroup(Player base );

	/***
	 * Получить группы
	 * 
	 * @param base
	 * @return
	 */
	List<String> getGroups(Player base );

	/***
	 * В группе ли
	 * 
	 * @param base
	 * @param group
	 * @return
	 */
	boolean inGroup(Player base, String group );

	/***
	 * Есть ли право
	 * 
	 * @param base
	 * @param node
	 * @return
	 */
	boolean hasPermission(CommandSender base, String node );
	boolean hasPermission(String base, String node );

	/***
	 * Префикс
	 * 
	 * @param base
	 * @return
	 */
	String getPrefix(Player base );

	/***
	 * Суффикс
	 * 
	 * @param base
	 * @return
	 */
	String getSuffix(Player base );
}