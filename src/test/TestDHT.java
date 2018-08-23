package test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

import org.junit.jupiter.api.Test;

import dht.Node;

class TestDHT {

	@Test
	void testJoin() throws AlreadyBoundException, NotBoundException {
		Node node = new Node("hash");
			
		try {
			node.getDht().join("./initxt.txt");
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
