package io.github.seccoding.utilities.powerpoint;

import java.io.File;

import io.github.seccoding.utilities.powerpoint.Converter.Result;

public class Test {

	public static void main(String[] args) {
		Converter converter = new Converter();
		Result result = converter.convert(new File("D:\\Java_신교재.pptx"), "11", "jpg");
		
		System.out.println(result.getFileName());
		System.out.println(result.getOutputFolder());
		System.out.println(result.getPageSize());
		System.out.println(result.getOriginalFilePath());
	}
	
}
