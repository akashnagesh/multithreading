package com.me.auction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class Executor {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Class mainClass = Class.forName("com.me.auction.Simulation");
		Simulation simulation = (Simulation) mainClass.newInstance();

		Method[] methods = mainClass.getMethods();

		Method mainMethod = mainClass.getMethod("main", String[].class);

		// Method mainMethod = Arrays.asList(methods).stream().filter(m ->
		// m.getName().equals("main")).findAny()
		// .orElseThrow(() -> new IllegalStateException("No main method"));

		mainMethod.invoke(null, (Object) null);
		System.out.println("done");

		AuctionServer ac = AuctionServer.getInstance();

		Field[] declaredFields = AuctionServer.class.getDeclaredFields();

		for (Field f : declaredFields) {
			f.setAccessible(true);
			f.get(ac);
		}
	}

}
