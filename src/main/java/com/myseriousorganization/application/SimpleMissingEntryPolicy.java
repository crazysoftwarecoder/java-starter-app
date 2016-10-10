package com.myseriousorganization.application;

import java.sql.Timestamp;

/**
 * Created by asfernando on 10/10/16.
 */
public class SimpleMissingEntryPolicy implements PluggableMissingEntryPolicy {

	private int defaultDifference;

	public SimpleMissingEntryPolicy(int defaultDifference) {
		this.defaultDifference = defaultDifference;
	}

	@Override
	public Timestamp getMissingEntryTimestamp(String userId, Timestamp entry1, Timestamp entry2, SessionStateEnum missingState) {
		if (missingState == SessionStateEnum.CLOSE) {
			return new Timestamp((entry2.getTime() - entry1.getTime()) / 2);
		}
		else {
			return new Timestamp(entry2.getTime() - (1000 * defaultDifference));
		}
	}
}
