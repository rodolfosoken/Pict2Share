package test;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import dht.DHTImpl;

class TestDHT {

	@Test
	void testJoin() {
		DHTImpl dht = new DHTImpl();
		try {
			dht.join("./initxt.txt");
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
