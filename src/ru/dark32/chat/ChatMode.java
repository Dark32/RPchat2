package ru.dark32.chat;


public enum ChatMode {

		GLOBAL(0, "$" ),
		WORLD(1, ">" ),
		SHOUT(2, "!" ),
		WHISPER(3, "#" ),
		LOCAL(4, "-" ),
		PM(5, "@" );
	private int		id;
	private String	ch;

	private ChatMode(int i, String s ){
		id = i;
		ch = s;
	}

	public int getModeId() {
		return id;
	}

	public String getStartChar() {
		return ch;
	}
}
