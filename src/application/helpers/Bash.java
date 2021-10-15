package application.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.stream.Collectors;

public class Bash {

	/** internal helper - converts a stream to a string */
	private static String streamToString(InputStream stream) {
		return new BufferedReader(new InputStreamReader(stream))
			.lines()
			.collect(Collectors.joining("\n"));
	}

	/**
	 * Execute a bash command
	 * @param cmdTemplate the bash command
	 * @param args arguments for `MessageFormat.format`
	 * @return a string containing the STD Out
	 * @throws IOException
	 */
	public static String exec(String cmdTemplate, Object... args)
		throws IOException {
		String cmd = MessageFormat.format(cmdTemplate, args);
		Process process = new ProcessBuilder("sh", "-c", cmd)
			.directory(new File(System.getProperty("user.dir")))
			.start();

		String stdout = streamToString(process.getInputStream());
		String stderr = streamToString(process.getErrorStream());

		// throw an error if anything is printed to stderr
		if (stderr.length() > 0) {
			throw new IOException("STDErr: " + stderr);
		}

		return stdout;
	}
}
