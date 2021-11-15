package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.Assert.*;

public class CustomerDependentPriceCalculatorTest {

	Pet rareNotInfantPet, rareInfantPet, notRareNotInfantPet, notRareInfantPet;

	@Before
	public void setup() {
		rareNotInfantPet = new Pet();
		PetType rarePetType = new PetType(true);
		rareNotInfantPet.setType(rarePetType);
		rareNotInfantPet.setBirthDate(Date.from(ZonedDateTime.now().minusYears(7).toInstant()));

		rareInfantPet = new Pet();
		rareInfantPet.setType(rarePetType);
		rareInfantPet.setBirthDate(Date.from(ZonedDateTime.now().minusMonths(9).toInstant()));

		notRareNotInfantPet = new Pet();
		PetType notRarePetType = new PetType(false);
		notRareNotInfantPet.setType(notRarePetType);
		notRareNotInfantPet.setBirthDate(Date.from(ZonedDateTime.now().minusYears(5).toInstant()));

		notRareInfantPet = new Pet();
		notRareInfantPet.setType(notRarePetType);
		notRareInfantPet.setBirthDate(Date.from(ZonedDateTime.now().minusMonths(4).toInstant()));
	}

	@After
	public void tearDown() {
		rareNotInfantPet = null;
		rareInfantPet = null;
		notRareNotInfantPet = null;
		notRareInfantPet = null;
	}

	@Test
	public void testCalcPriceAppliesCoefsForRareNotInfantPets() {
		List<Pet> pets = List.of(
			rareNotInfantPet
		);

		double price = new CustomerDependentPriceCalculator().calcPrice(pets, 30, 40, UserType.SILVER);
		assertEquals(48.0, price, 0.01);
	}

	@Test
	public void testCalcPriceAppliesCoefsForRareInfantPets() {
		List<Pet> pets = List.of(
			rareInfantPet
		);

		double price = new CustomerDependentPriceCalculator().calcPrice(pets, 30, 40, UserType.SILVER);
		assertEquals(67.2, price, 0.01);
	}

	@Test
	public void testCalcPriceDoesNotApplyCoefForNotRarePets() {
		List<Pet> pets = List.of(
			notRareNotInfantPet
		);

		double price = new CustomerDependentPriceCalculator().calcPrice(pets, 30, 40, UserType.SILVER);
		assertEquals(40.0, price, 0.01);
	}

	@Test
	public void testCalcPriceAppliesCommonCoefForNotRareInfantPets() {
		List<Pet> pets = List.of(
			notRareInfantPet
		);

		double price = new CustomerDependentPriceCalculator().calcPrice(pets, 30, 40, UserType.SILVER);
		assertEquals(48.0, price, 0.01);
	}

	@Test
	public void testCalcPriceAppliesGoldDiscountForNoDiscountGoldUsers() {
		List<Pet> pets = List.of(
			rareInfantPet,
			notRareInfantPet
		);

		double price = new CustomerDependentPriceCalculator().calcPrice(pets, 30, 40, UserType.GOLD);
		assertEquals(122.16, price, 0.01);
	}

	@Test
	public void testCalcPriceAppliesDiscountForHavingDiscountNewUsers() {
		List<Pet> pets = List.of(
			notRareInfantPet,notRareInfantPet,notRareInfantPet,notRareInfantPet,notRareInfantPet,
			notRareInfantPet,notRareInfantPet,notRareInfantPet,notRareInfantPet,notRareInfantPet
		);

		double price = new CustomerDependentPriceCalculator().calcPrice(pets, 30, 40, UserType.NEW);
		assertEquals(486.0, price, 0.01);
	}

	@Test
	public void testCalcPriceAppliesDiscountForHavingDiscountSilverUsers() {
		List<Pet> pets = List.of(
			notRareInfantPet,notRareInfantPet,notRareInfantPet,notRareInfantPet,notRareInfantPet,
			notRareInfantPet,notRareInfantPet,notRareInfantPet,notRareInfantPet,notRareInfantPet
		);

		double price = new CustomerDependentPriceCalculator().calcPrice(pets, 30, 40, UserType.SILVER);
		assertEquals(459.0, price, 0.01);
	}
}
