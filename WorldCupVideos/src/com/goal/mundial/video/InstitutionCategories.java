
package com.goal.mundial.video;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class InstitutionCategories {

	protected String instName;
	protected String instDesc;
	protected String instLink;

	protected List<String> wordCategoriesId = new ArrayList<String>();
	protected List<String> wordCategoriesValue = new ArrayList<String>();

	/**
	 * @return the instName
	 */
	public String getInstName() {
		return instName;
	}

	/**
	 * @param instName
	 *            the instName to set
	 */
	public void setInstName(String instName) {
		this.instName = instName;
	}

	/**
	 * @return the instDesc
	 */
	public String getInstDesc() {
		return instDesc;
	}

	/**
	 * @param instDesc
	 *            the instDesc to set
	 */
	public void setInstDesc(String instDesc) {
		this.instDesc = instDesc;
	}

	/**
	 * @return the instLink
	 */
	public String getInstLink() {
		return instLink;
	}

	/**
	 * @param instLink
	 *            the instLink to set
	 */
	public void setInstLink(String instLink) {
		this.instLink = instLink;
	}

	/**
	 * @return the wordCategories
	 */
	public List<String> getWordCategoriesValue() {
		return wordCategoriesValue;
	}

	/**
	 * @param wordCategories
	 *            the wordCategories to set
	 */
	public void setWordCategoriesValue(String wordCategories) {

		if (!wordCategoriesValue.contains(wordCategories)) {
			this.wordCategoriesValue.add(wordCategories);
		}

	}

	public List<String> getWordCategoriesId() {
		return wordCategoriesId;
	}

	public void setWordCategoriesId(String wordCategoriesIds) {

		if (!wordCategoriesId.contains(wordCategoriesIds)) {
			this.wordCategoriesId.add(wordCategoriesIds);
		}
	}

}
