package kr.co.hucloud.utilities.powerpoint;

import java.io.File;

import kr.co.hucloud.utilities.powerpoint.Converter.ConvertResult;

public class Test {

	public static void main(String[] args) {
		Converter converter = new Converter();
		ConvertResult result = converter.convert(new File("D:\\Java_신교재.pptx"), "11", "jpg");
		
		System.out.println(result.getFileName());
		System.out.println(result.getOutputFolder());
		System.out.println(result.getPageSize());
		System.out.println(result.getOriginalFilePath());
	}
	
}
