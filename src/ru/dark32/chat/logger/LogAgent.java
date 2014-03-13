package ru.dark32.chat.logger;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.*;

public class LogAgent {
	private Logger					logger;
	private static final Formatter	FORMATTER	= new LogFormatter();

	private static class LogFormatter extends Formatter {
		private final SimpleDateFormat	SDF	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

		@Override
		public String format(LogRecord record ) {
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

	public LogAgent(String logName, File logFile ){
		logger = Logger.getLogger(logName);
		logger.setUseParentHandlers(false);
		try {
			FileHandler fHandler = new FileHandler(logFile.getAbsolutePath(), true);
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
	public void post(String message ) {
		logger.log(Level.FINE, message);
	}

	/**
	 * Информационное сообщение
	 * 
	 * @param message
	 *            сообщение
	 */
	public void info(String message ) {
		logger.log(Level.INFO, message);
	}

	/**
	 * Сообщение об опасности
	 * 
	 * @param message
	 *            сообщение
	 */
	public void warning(String message ) {
		logger.log(Level.WARNING, message);
	}

}