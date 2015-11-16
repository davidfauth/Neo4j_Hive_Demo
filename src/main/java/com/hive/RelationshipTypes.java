package com.hive;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {
	RRAemployee,
	Project_Lead,
	Project_CoLead,
	Project_Researcher,
	JM_IN_INTERVIEW_SCJM,
	CC_IN_INTERVIEW_SCCC,
	REFERRED,
	ATTENDED,
	CC_IN_INTERVIEW_CCCA,
	CC_in_SCCC,
	CC_in_CCSM,
	JM_IN_INTERVIEW_JMCA,
	MI_ABOUT,
	MI_ON
}
