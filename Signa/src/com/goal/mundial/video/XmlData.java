package com.goal.mundial.video;

import java.util.ArrayList;

public class XmlData {

	protected ArrayList<AvatarWord> listWords = new ArrayList<AvatarWord>();

	protected InstitutionCategories institutionInfo = new InstitutionCategories();

	/**
	 * @return the listWords
	 */
	public ArrayList<AvatarWord> getListWords() {
		return listWords;
	}

	/**
	 * @param listWords the listWords to set
	 */
	public void setListWords(ArrayList<AvatarWord> listWords) {
		this.listWords = listWords;
	}

	/**
	 * @return the institutionInfo
	 */
	public InstitutionCategories getInstitutionInfo() {
		return institutionInfo;
	}

	/**
	 * @param institutionInfo the institutionInfo to set
	 */
	public void setInstitutionInfo(InstitutionCategories institutionInfo) {
		this.institutionInfo = institutionInfo;
	}
	
	

}
