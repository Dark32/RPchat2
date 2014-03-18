package ru.dark32.chat.logger;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.*;

import org.bukkit.entity.Player;

import ru.dark32.chat.ichanels.IChanel;

public class LogAgent {
	final private Logger			logger;
	private static final Formatter	FORMATTER	= new LogFormatter();
	final static String default_path = "./plugin/rpChat/log/";
	
	final private static class LogFormatter extends Formatter {
		private final SimpleDateFormat	SDF	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

		@Override
		public String format(final LogRecord record ) {
			StringBuilder sb = new StringBuilder();
			sb.append(SDF.format(record.getMillis())).append("[").append(record.getLevel().getName()).append("] ")
					.append(record.getMessage()).append("\n");

			Throwable thrown = record.getThrown();
			if (thrown != null) {
				StringWriter sw = new StringWriter();
				thrown.printStackTrace(new PrintWriter(sw));
				sb.append(sw.toString());
			}

			return sb.toString();
		}
	}

	File	logFile;
	public LogAgent(final String logName ){
		this(logName, default_path);
	}
	public LogAgent(final String logName, final String path ){
		logFile = new File(path + logName+".log");
		logger = Logger.getLogger(logName);
		logger.setUseParentHandlers(false);
		try {
			final FileHandler fHandler = new FileHandler(logFile.getAbsolutePath(), true);
			fHandler.setFormatter(FORMATTER);
			logger.addHandler(fHandler);
		}
		catch (Exception e) {
			logger.log(Level.WARNING, "Failed write log to " + logFile.getAbsolutePath(), e);
		}
	}

	/**
	 * Записываем обычное сообщение
	 * 
	 * @param message
	 *            сообщение
	 */
	public void post(final String message ) {
		logger.log(Level.INFO, message);
	}

	/**
	 * Записываем обычное сообщение
	 * 
	 * @param message
	 *            сообщение
	 */
	public void postChat(final IChanel chanel, final Player player, final String message ) {
		String _message = chanel.format(player, message).replace("%1$s", player.getName()).replace("%2$s", message);
		logger.log(Level.INFO, _message);
	}

	/**
	 * Сообщение об опасности
	 * 
	 * @param message
	 *            сообщение
	 */
	public void warning(final String message ) {
		logger.log(Level.WARNING, message);
	}

}