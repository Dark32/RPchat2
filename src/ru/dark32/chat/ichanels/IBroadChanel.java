package ru.dark32.chat.ichanels;

import java.util.List;

public interface IBroadChanel extends IChanel {
	public List<String> getPatterns();
	public void setPattern(List<String> list);
}
