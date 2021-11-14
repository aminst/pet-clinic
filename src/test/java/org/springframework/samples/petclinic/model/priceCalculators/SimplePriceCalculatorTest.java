package org.springframework.samples.petclinic.model.priceCalculators;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.util.*;

import static org.junit.Assert.*;

public class SimplePriceCalculatorTest {
	Pet rarePet, notRarePet;

	@Before
	public void setup() {
		rarePet = new Pet();
		PetType rarePetType = new PetType(true);
		rarePet.setType(rarePetType);

		notRarePet = new Pet();
		PetType notRarePetType = new PetType(false);
		notRarePet.setType(notRarePetType);
	}

	@After
	public void tearDown() {
		rarePet = null;
		notRarePet = null;
	}


	@Test
	public void testCalcPriceCalculatesCorrectPriceForRareNotNewPets() {
		List<Pet> pets = List.of(rarePet);

		double price = new SimplePriceCalculator().calcPrice(pets, 20, 30, UserType.GOLD);
		assertEquals(56.0, price, 0.01);
	}

	@Test
	public void testCalcPriceCalculatesCorrectPriceForRareNewPets() {
		List<Pet> pets = List.of(rarePet);

		double price = new SimplePriceCalculator().calcPrice(pets, 20, 30, UserType.NEW);
		assertEquals(53.2, price, 0.01);
	}

	@Test
	public void testCalcPriceCalculatesCorrectPriceForNotRareNotNewPets() {
		List<Pet> pets = List.of(notRarePet);

		double price = new SimplePriceCalculator().calcPrice(pets, 20, 30, UserType.GOLD);
		assertEquals(50.0, price, 0.01);
	}

	@Test
	public void testCalcPriceCalculatesCorrectPriceForNotRareNewPets() {
		List<Pet> pets = List.of(notRarePet);

		double price = new SimplePriceCalculator().calcPrice(pets, 20, 30, UserType.NEW);
		assertEquals(47.5, price, 0.01);
	}

	@Test
	public void testCalcPriceCalculatesCorrectPriceForDifferentTypePets() {
		List<Pet> pets = List.of(rarePet, notRarePet);

		double price = new SimplePriceCalculator().calcPrice(pets, 20, 30, UserType.GOLD);

		assertEquals(86.0, price, 0.01);
	}
}
