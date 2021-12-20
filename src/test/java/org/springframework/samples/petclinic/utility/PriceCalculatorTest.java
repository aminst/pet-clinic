package org.springframework.samples.petclinic.utility;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceCalculatorTest {
	PriceCalculator priceCalculator;

	Pet infantPetWithMoreThan100DaysFromLastVisit;
	Pet infantPetWithLessThan100DaysFromLastVisit;
	Pet notInfantPetWithMoreThan100DaysFromLastVisit;
	Pet notInfantPetWithLessThan100DaysFromLastVisit;

	@Before
	public void setup()  {
		priceCalculator = new PriceCalculator();

		PetType type = new PetType();

		Visit moreThan100DaysPassedVisit = new Visit();
		moreThan100DaysPassedVisit.setDate(LocalDate.now().minusDays(120));
		Visit lessThan100DaysPassedVisit = new Visit();
		lessThan100DaysPassedVisit.setDate(LocalDate.now().minusDays(10));

		infantPetWithMoreThan100DaysFromLastVisit = new Pet();
		infantPetWithMoreThan100DaysFromLastVisit.setType(type);
		infantPetWithMoreThan100DaysFromLastVisit.setBirthDate(LocalDate.now().minusYears(1));
		infantPetWithMoreThan100DaysFromLastVisit.addVisit(moreThan100DaysPassedVisit);

		infantPetWithLessThan100DaysFromLastVisit = new Pet();
		infantPetWithLessThan100DaysFromLastVisit.setType(type);
		infantPetWithLessThan100DaysFromLastVisit.setBirthDate(LocalDate.now().minusYears(1));
		infantPetWithLessThan100DaysFromLastVisit.addVisit(lessThan100DaysPassedVisit);

		notInfantPetWithMoreThan100DaysFromLastVisit = new Pet();
		notInfantPetWithMoreThan100DaysFromLastVisit.setType(type);
		notInfantPetWithMoreThan100DaysFromLastVisit.setBirthDate(LocalDate.now().minusYears(5));
		notInfantPetWithMoreThan100DaysFromLastVisit.addVisit(moreThan100DaysPassedVisit);

		notInfantPetWithLessThan100DaysFromLastVisit = new Pet();
		notInfantPetWithLessThan100DaysFromLastVisit.setType(type);
		notInfantPetWithLessThan100DaysFromLastVisit.setBirthDate(LocalDate.now().minusYears(5));
		notInfantPetWithLessThan100DaysFromLastVisit.addVisit(lessThan100DaysPassedVisit);
	}

	@After
	public void tearDown() {
		priceCalculator = null;

		infantPetWithMoreThan100DaysFromLastVisit = null;
		infantPetWithLessThan100DaysFromLastVisit = null;
		notInfantPetWithMoreThan100DaysFromLastVisit = null;
		notInfantPetWithLessThan100DaysFromLastVisit = null;
	}

	@Test
	public void infantCoefIsAppliedForInfantPet() {
		assertEquals(
			33.6,
			priceCalculator.calcPrice(List.of(infantPetWithMoreThan100DaysFromLastVisit), 200, 20),
			0.01
		);
	}

	@Test
	public void infantCoefIsNotAppliedForNotInfantPet() {
		assertEquals(
			24,
			priceCalculator.calcPrice(List.of(notInfantPetWithLessThan100DaysFromLastVisit), 200, 20),
			0.01
		);
	}

	@Test
	public void discountPerVisitIsAppliedForLessThan100DaysFromLastVisitPetsEligibleForDiscount() {
		List<Pet> pets = List.of(
			notInfantPetWithLessThan100DaysFromLastVisit,
			notInfantPetWithLessThan100DaysFromLastVisit,
			notInfantPetWithLessThan100DaysFromLastVisit,
			notInfantPetWithLessThan100DaysFromLastVisit,
			notInfantPetWithLessThan100DaysFromLastVisit,
			notInfantPetWithLessThan100DaysFromLastVisit,
			notInfantPetWithLessThan100DaysFromLastVisit,
			notInfantPetWithLessThan100DaysFromLastVisit,
			notInfantPetWithLessThan100DaysFromLastVisit,
			notInfantPetWithLessThan100DaysFromLastVisit
		);

		assertEquals(
			656,
			priceCalculator.calcPrice(pets, 200, 20),
			0.01
		);
	}

	@Test
	public void normalDiscountIsAppliedForMoreThan100DaysFromLastVisitPetsEligibleForDiscount() {
		List<Pet> pets = List.of(
			notInfantPetWithMoreThan100DaysFromLastVisit,
			notInfantPetWithMoreThan100DaysFromLastVisit,
			notInfantPetWithMoreThan100DaysFromLastVisit,
			notInfantPetWithMoreThan100DaysFromLastVisit,
			notInfantPetWithMoreThan100DaysFromLastVisit,
			notInfantPetWithMoreThan100DaysFromLastVisit,
			notInfantPetWithMoreThan100DaysFromLastVisit,
			notInfantPetWithMoreThan100DaysFromLastVisit,
			notInfantPetWithMoreThan100DaysFromLastVisit,
			notInfantPetWithMoreThan100DaysFromLastVisit
		);

		assertEquals(
			856,
			priceCalculator.calcPrice(pets, 200, 20),
			0.01
		);
	}

	@Test
	public void discountPerVisitIsAppliedForLessThan100DaysFromLastVisitInfantPetsEligibleForDiscount() {
		List<Pet> pets = List.of(
			infantPetWithLessThan100DaysFromLastVisit,
			infantPetWithLessThan100DaysFromLastVisit,
			infantPetWithLessThan100DaysFromLastVisit,
			infantPetWithLessThan100DaysFromLastVisit,
			infantPetWithLessThan100DaysFromLastVisit
		);

		assertEquals(
			502.4,
			priceCalculator.calcPrice(pets, 200, 20),
			0.01
		);
	}

	@Test
	public void infantCoefIsAppliedFor2YearPet() {
		infantPetWithMoreThan100DaysFromLastVisit.setBirthDate(LocalDate.now().minusYears(2));
		assertEquals(
			33.6,
			priceCalculator.calcPrice(List.of(infantPetWithMoreThan100DaysFromLastVisit), 200, 20),
			0.01
		);
	}

	@Test
	public void normalDiscountIsAppliedFor100DayFromLastVisitPets() {
		Pet pet = new Pet();
		PetType type = new PetType();
		pet.setType(type);
		pet.setBirthDate(LocalDate.now().minusYears(6));
		Visit visit = new Visit();
		visit.setDate(LocalDate.now().minusDays(100));
		pet.addVisit(visit);

		List<Pet> pets = List.of(
			pet, pet, pet, pet, pet, pet, pet, pet, pet, pet
		);

		assertEquals(
			856,
			priceCalculator.calcPrice(pets, 200, 20),
			0.01
		);
	}
}
