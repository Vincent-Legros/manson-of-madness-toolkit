/*
 * @copyright 2011 Ridha Chelghaf
 * @license GNU General Public License
 * 
 * This file is part of MansionOfMadnessToolKit.
 *
 * MansionOfMadnessToolKit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MansionOfMadnessToolKit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MansionOfMadnessToolKit.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.amphiprion.mansionofmadness.dto;

public class CombatCard extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum DbField {
		ID, MONSTERCLASS, ATKTYPEINV, TESTINV, SUCCESSINV, FAILUREINV, ATKTYPEMON, TESTMON, SUCCESSMON, FAILUREMON, IS_EMBEDDED
	}

	private String monsterClass;
	private String atkTypeInv;
	private String testInv;
	private String successInv;
	private String failureInv;
	private String atkTypeMon;
	private String testMon;
	private String successMon;
	private String failureMon;
	private boolean embedded;

	/**
	 * 
	 */
	public CombatCard() {
		super();
	}

	public CombatCard(String id) {
		super(id);
	}

	/**
	 * @return the monsterClass
	 */
	public String getMonsterClass() {
		return monsterClass;
	}

	/**
	 * @param monsterClass the monsterClass to set
	 */
	public void setMonsterClass(String monsterClass) {
		this.monsterClass = monsterClass;
	}

	/**
	 * @return the atkTypeInv
	 */
	public String getAtkTypeInv() {
		return atkTypeInv;
	}

	/**
	 * @param atkTypeInv the atkTypeInv to set
	 */
	public void setAtkTypeInv(String atkTypeInv) {
		this.atkTypeInv = atkTypeInv;
	}

	/**
	 * @return the testInv
	 */
	public String getTestInv() {
		return testInv;
	}

	/**
	 * @param testInv the testInv to set
	 */
	public void setTestInv(String testInv) {
		this.testInv = testInv;
	}

	/**
	 * @return the successInv
	 */
	public String getSuccessInv() {
		return successInv;
	}

	/**
	 * @param successInv the successInv to set
	 */
	public void setSuccessInv(String successInv) {
		this.successInv = successInv;
	}

	/**
	 * @return the failureInv
	 */
	public String getFailureInv() {
		return failureInv;
	}

	/**
	 * @param failureInv the failureInv to set
	 */
	public void setFailureInv(String failureInv) {
		this.failureInv = failureInv;
	}

	/**
	 * @return the atkTypeMon
	 */
	public String getAtkTypeMon() {
		return atkTypeMon;
	}

	/**
	 * @param atkTypeMon the atkTypeMon to set
	 */
	public void setAtkTypeMon(String atkTypeMon) {
		this.atkTypeMon = atkTypeMon;
	}

	/**
	 * @return the testMon
	 */
	public String getTestMon() {
		return testMon;
	}

	/**
	 * @param testMon the testMon to set
	 */
	public void setTestMon(String testMon) {
		this.testMon = testMon;
	}

	/**
	 * @return the successMon
	 */
	public String getSuccessMon() {
		return successMon;
	}

	/**
	 * @param successMon the successMon to set
	 */
	public void setSuccessMon(String successMon) {
		this.successMon = successMon;
	}

	/**
	 * @return the failureMon
	 */
	public String getFailureMon() {
		return failureMon;
	}

	/**
	 * @param failureMon the failureMon to set
	 */
	public void setFailureMon(String failureMon) {
		this.failureMon = failureMon;
	}

	/**
	 * @return the embedded
	 */
	public boolean isEmbedded() {
		return embedded;
	}

	/**
	 * @param embedded
	 *            the embedded to set
	 */
	public void setEmbedded(boolean embedded) {
		this.embedded = embedded;
	}

}
