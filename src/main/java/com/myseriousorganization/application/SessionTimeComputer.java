package com.myseriousorganization.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.myseriousorganization.application.SessionStateEnum.CLOSE;
import static com.myseriousorganization.application.SessionStateEnum.OPEN;

/**
 * Created by asfernando on 10/10/16.
 */

public class SessionTimeComputer {

	private static final PluggableMissingEntryPolicy DEFAULT_MISSING_ENTRY_POLICY = new SimpleMissingEntryPolicy(40); // 40 seconds is the default difference for missing entries.

	private PluggableMissingEntryPolicy customEntryPolicy;

	// Map to store open session entries.
	private Map<String, Timestamp> m = new HashMap<>();

	public SessionTimeComputer (File file, String outputFile) {
		computeAverageSessionTime(file, outputFile);
	}

	public SessionTimeComputer (File file, String outputFile, PluggableMissingEntryPolicy customEntryPolicy) {
		this.customEntryPolicy = customEntryPolicy;
		computeAverageSessionTime(file, outputFile);
	}

	private void computeAverageSessionTime(File file, String outputFile) {
		if (file == null) {
			throw new IllegalArgumentException("No file found. Please specify a file to read.");
		}

		try (FileInputStream inputStream = new FileInputStream(file);
			 Scanner reader = new Scanner(inputStream)) {
			writeIntermediateFile(reader, outputFile);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("The file " + file.getAbsolutePath() + " was not found.");
		} catch (IOException e) {
			throw new IllegalStateException("IO Exception occurred while accessing " + file.getAbsolutePath() + " was not found ");
		}
	}

	private void sortIntermediateFile(String interFile) {

		// this is shamelessly taken from http://stackoverflow.com/questions/12807797/java-get-available-memory
		long approxFreeMemory = Runtime.getRuntime().maxMemory() - (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());

		try (	FileInputStream inStream = new FileInputStream(interFile);
				Scanner reader = new Scanner(inStream) ) {

			while (reader.hasNext()) {
				String line = reader.nextLine();
				
			}

		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Intermediate file " + interFile + " was never found. Something extremely wrong happened here. ");
			// THIS SHOULD NEVER HAPPEN.
		}
		catch (IOException e) {
			throw new IllegalStateException("Something seriously wrong happened while accessing intermediate file:= " + interFile);
			// NEITHER THIS
		}
	}

	private void writeIntermediateFile(Scanner reader, String outputFile) {

		try (	FileOutputStream outputStream = new FileOutputStream(outputFile + ".inter");
				 PrintWriter writer = new PrintWriter(outputStream, true /* autoFlush */)) {

			while (reader.hasNext()) {
				String line = reader.nextLine();

				if (line.trim().length()==0) {
					continue;
				}

				String[] tokens = line.split(",");

				String userID = tokens[0];
				Timestamp timestamp = new Timestamp(Long.parseLong(tokens[1]));
				SessionStateEnum state = SessionStateEnum.valueOf(tokens[2].toUpperCase());

				String outputEntry = null;
				if (m.containsKey(userID)) {
					if (state == CLOSE) {
						Long timeSpent = timestamp.getTime() -  m.get(userID).getTime();
						outputEntry = "{" + userID + "," + timeSpent + "},";
					}
					else {
						PluggableMissingEntryPolicy missingEntryPolicy = (customEntryPolicy!=null) ? customEntryPolicy : DEFAULT_MISSING_ENTRY_POLICY;
						Long timeSpent = missingEntryPolicy.getMissingEntryTimestamp(userID, m.get(userID), timestamp, CLOSE).getTime();
						outputEntry = "{" + userID + "," + timeSpent + "},";
					}
				}
				else {
					if (state == OPEN) {
						m.put(userID, timestamp);
						continue;
					}
					else {
						// If you get a close, then use the missing policy plugin to detemine the missing entry.
						PluggableMissingEntryPolicy missingEntryPolicy = (customEntryPolicy!=null) ? customEntryPolicy : DEFAULT_MISSING_ENTRY_POLICY;
						Long timeSpent = missingEntryPolicy.getMissingEntryTimestamp(userID, null, timestamp, CLOSE).getTime();
						outputEntry = "{" + userID + "," + timeSpent + "},";
					}
				}
				writer.write(outputEntry + System.getProperty("line.separator"));
			}
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Invalid output file " + outputFile);
		}
		catch (IOException e) {
			throw new IllegalStateException("Could not write to output file. Reason is:= " + e.getMessage());
		}
	}
}


