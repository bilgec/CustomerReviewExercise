package com.github.bilgec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class SimpleEncoding {

	public static final List<String> qwerty = Arrays.asList("1234567890qwertyuiopasdfghjkl;zxcvbnm,./".split(""));
	public static LinkedList<String> set = new LinkedList<String>();
	public static final long startTime = System.currentTimeMillis();

	public static void main(String[] args) throws IOException {
		set.addAll(qwerty);

		String transformations = new String(Files.readAllBytes(Paths.get("test_transformation.txt")));
		String input = new String(Files.readAllBytes(Paths.get("test_input.txt")));

		transformSet(transformations);
		String output = encodeInput(input);

		System.out.println(output);
	}

	public static String encodeInput(String input) {
		LinkedHashMap<Character, Character> transformer = new LinkedHashMap<Character, Character>();
		for (int i = 0; i < qwerty.size(); i++) {
			transformer.put(qwerty.get(i).charAt(0), set.get(i).charAt(0));
		}
		char[] charray = new char[input.length()];

		IntStream.range(0, input.length()).parallel().forEach(t -> {
			char ch = input.charAt(t);
			Character idx = transformer.get(ch);
			if (idx == null) {
				charray[t] = ch;
			} else {
				charray[t] = idx;
			}
		});

		return String.valueOf(charray);
	}

	public static void transformSet(String transformations) {
		for (String command : Arrays.asList(transformations.split(","))) {
			command = command.trim();
			if ("H".equals(command)) {
				horizontalFlip();
			} else if ("V".equals(command)) {
				verticalFlip();
			} else {
				try {
					Integer i = Integer.parseInt(command);
					if (i < 0)
						moveBackward(i * -1);
					else
						moveForward(i);
				} catch (Exception e) {
					System.out.println("Unknown Transformation Ignored: " + command);
				}
			}
		}
	}

	public static void verticalFlip() {
		LinkedList<String> newLine = new LinkedList<String>();
		newLine.addAll(set.subList(30, 40));
		newLine.addAll(set.subList(20, 30));
		newLine.addAll(set.subList(10, 20));
		newLine.addAll(set.subList(0, 10));
		set = newLine;
	}

	public static void horizontalFlip() {
		for (int i = 10; i <= set.size(); i = i + 10) {
			Collections.reverse(set.subList(i - 10, i));
		}
	}

	public static void moveForward(int times) {
		for (int i = 0; i < times; i++) {
			set.addFirst(set.removeLast());
		}
	}

	public static void moveBackward(int times) {
		for (int i = 0; i < times; i++) {
			set.add(set.removeFirst());
		}
	}

}
