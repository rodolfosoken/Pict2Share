package testes;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import dht.DHT;

class TestDHT {

	@Test
	void testJoin() {
		DHT dht = new DHT();
		try {
			dht.join("./initxt.txt",null);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
