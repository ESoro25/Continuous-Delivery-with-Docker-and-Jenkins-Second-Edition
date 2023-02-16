package com.leszko.calculator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
/**
 * Define a calculator class to take two numbers and return the sum
*/
public class Calculator {
        final static int umlNUMBER1 = 3;
	@Cacheable("sum")
/**
 * Function that takes two integer arguments and returns the sum
*/
	public int sum(int a, int b) {
		return a + b;
	}
}
