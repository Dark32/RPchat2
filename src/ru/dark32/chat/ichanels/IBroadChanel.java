package ru.dark32.chat.ichanels;

import java.util.List;

public interface IBroadChanel extends IChanel {
	/**
	 * @return получаем лист шаблонов
	 */
	List<String> getPatterns();
}
