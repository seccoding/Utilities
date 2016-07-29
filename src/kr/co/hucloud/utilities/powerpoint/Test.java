package kr.co.hucloud.utilities.powerpoint;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		Converter converter = new Converter();
		converter.convert(new File("D:\\Java_신교재.pptx"), "11", "jpg");
	}
	
}
