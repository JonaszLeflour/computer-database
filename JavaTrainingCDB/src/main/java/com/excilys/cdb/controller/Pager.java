package com.excilys.cdb.controller;
import java.util.List;
/**
 * @author Jonasz Leflour
 * wrapper for lists to create paginized sublists
 * @param <E> element type of paged list
 */
public class Pager<E>{
	private List<E> list;
	private int pageSize = 10;
	
	/**
	 * @param list list to page
	 */
	public Pager(List<E> list){
		this.list = list;
	}
	
	/**
	 * @param newSize
	 */
	public void setPageSize(int newSize) {
		pageSize = newSize;
	}
	
	/**
	 * @return current page size
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	/**
	 * @return size of provided list
	 */
	public int getNbElements() {
		return list.size();
	}
	
	/**
	 * @return Number of pages given the current pageSize
	 */
	public int getNumberOfPages() {
		return (int) Math.ceil((list.size())/((double)pageSize));
		
	}
	
	/**
	 * @param currentPage starts at onz
	 * @param pageSize
	 * @return sublist sublist of page currentPage for pages of size pageSize 
	 */
	public List<E> getPage(int currentPage){
		int start = Math.max(Math.min(currentPage * pageSize,list.size()),0);
		int end = Math.min((currentPage + 1) * pageSize, list.size());
		return list.subList(start, end);
	}
}
