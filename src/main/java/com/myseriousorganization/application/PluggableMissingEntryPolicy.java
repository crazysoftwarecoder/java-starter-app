package com.myseriousorganization.application;


import java.sql.Timestamp;

/**
 * Top level interface used for plugging in missing entries. The idea is to fetch
 * a closing time if there is a missing CLOSE record between two OPEN records for the same user. Or
 * in the reverse case, there is a missing OPEN record between two CLOSE records.
 *
 * This api will get called when the second same state record is read, asking the client to specify a policy
 * to compute the closing time.
 */
public interface PluggableMissingEntryPolicy {

	/**
	 * The session state will be the state that is missing. For example, in the case of two OPEN records in succession,
	 * the state will be CLOSE.
	 *
	 * However in the case of the reverse, where there are two CLOSE records in succession, the timestamp of the previous
	 * CLOSE WILL NOT BE CAPTURED. This is to prevent a OOM where there are very unique visitors who close every time
	 * and storing the last timestamp that was closed per user will not be memory efficient.
	 * In such a case, entry1 WILL BE NULL and entry 2 will have the current timestamp value.
	 *
	 * @param userId
	 * @param t1
	 * @param t2
	 * @param missingState
	 * @return
	 */
	public Timestamp getMissingEntryTimestamp(String userId, Timestamp entry1, Timestamp entry2, SessionStateEnum missingState);

}
