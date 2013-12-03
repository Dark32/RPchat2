package ru.dark32.chat.chanels;

import java.util.List;

import ru.dark32.chat.ichanels.IBroadChanel;

public class BroadChanel extends BaseChanel implements IBroadChanel {

	private List<String>	paterns;

	@Override
	public List<String> getPatterns() {
		return paterns;
	}

	@Override
	public void setPattern(List<String> list ) {
		paterns = list;

	}

}
