package org.springframework.samples.petclinic.owner;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.util.ArrayUtils;
import java.util.*;

import static org.junit.Assert.*;

class OwnerTest {

	private List<Pet> initialPets;
	private List<Pet> sortedPets;
	private Owner owner;

	@BeforeEach
	void setup() {
		owner = new Owner();
		owner.setAddress("Test Address");
		owner.setCity("Test City");
		owner.setTelephone("0912");

		initialPets = new ArrayList<Pet>();
		sortedPets = new ArrayList<Pet>();
		Pet pet1 = new Pet();
		pet1.setName("farshid");
		Pet pet2 = new Pet();
		pet2.setName("fereydoon");
		Pet pet3 = new Pet();
		pet3.setName("faramarz");

		initialPets.add(pet1);
		initialPets.add(pet2);
		initialPets.add(pet3);

		sortedPets.add(pet3);
		sortedPets.add(pet1);
		sortedPets.add(pet2);

		for (Pet pet : initialPets)
			owner.addPet(pet);
	}

	@After
	void tearDown() {
		initialPets = null;
		sortedPets = null;
		owner = null;
	}

	@Test
	void testGetAddressReturnsCorrectValue() {
		assertSame("getAddress returned wrong value", "Test Address", owner.getAddress());
	}

	@Test
	void testSetAddressSetsSameValue() {
		final String expected = "Set Address Test";
		owner.setAddress(expected);

		assertSame("setAddress set wrong value", expected, owner.getAddress());
	}

	@Test
	void testGetCityReturnsCorrectValue() {
		assertSame("getCity returned wrong value", "Test City", owner.getCity());
	}

	@Test
	void testSetCitySetsSameValue() {
		final String expected = "Set City Test";
		owner.setCity(expected);

		assertSame("setCity set wrong value", expected, owner.getCity());
	}

	@Test
	void testGetTelephoneReturnsCorrectValue() {
		assertSame("getTelephone returned wrong value", "0912", owner.getTelephone());
	}

	@Test
	void testSetTelephoneSetsSameValue() {
		final String expected = "Set Telephone Test";
		owner.setTelephone(expected);

		assertSame("setTelephone set wrong value", expected, owner.getTelephone());
	}

	@Test
	void testGetPetsContainsCorrectValues() {
		List<Pet> returnedPets = owner.getPets();

		for (Pet pet : initialPets)
			assertTrue(String.format("getPets did not contain pet with name %s", pet.getName()), ArrayUtils.contains(returnedPets.toArray(), pet));
	}

	@Test
	void testGetPetsReturnsCorrectlyOrderedValues() {
		List<Pet> returnedPets = owner.getPets();

		assertArrayEquals("getPets did not return the correct order", sortedPets.toArray(), returnedPets.toArray());
	}

	@Test
	void testAddPetAddsNewPet() {
		Pet newPet = new Pet();
		newPet.setName("z cat");

		owner.addPet(newPet);
		sortedPets.add(newPet);

		assertArrayEquals("addPet did not add the new pet", sortedPets.toArray(), owner.getPets().toArray());
	}

	@Test
	void testAddPetDoesNotAddNotNewPet() {
		Pet oldPet = new Pet();
		oldPet.setName("z cat");
		oldPet.setId(0);

		owner.addPet(oldPet);

		assertArrayEquals("addPet mistakenly added the old pet", sortedPets.toArray(), owner.getPets().toArray());
	}

	@Test
	void testAddPetSetsOwnerForPet() {
		Pet newPet = new Pet();
		newPet.setName("z cat");

		owner.addPet(newPet);

		assertSame("addPet did not set owner correctly", owner, newPet.getOwner());
	}

	@Test
	void testRemovePetRemovesProvidedPet() {
		Pet toBeRemovedPet = initialPets.get(0);
		owner.removePet(toBeRemovedPet);
		sortedPets.remove(toBeRemovedPet);

		assertArrayEquals("removePet did not remove the provided pet", sortedPets.toArray(), owner.getPets().toArray());
	}

	@Test
	void testGetPetFindsExistingPet() {
		Pet toBeFoundPet = initialPets.get(0);

		assertSame("getPet did not find existing pet", toBeFoundPet, owner.getPet(toBeFoundPet.getName()));
	}

	@Test
	void testGetPetReturnsNullForNonExistingPet() {
		Pet toBeFoundPet = new Pet();
		toBeFoundPet.setName("new");

		assertSame("getPet found not existing pet", null, owner.getPet(toBeFoundPet.getName()));
	}

	@Test
	void testGetPetWithIgnoreNewTrueIgnoresNewPets() {
		Pet newPet = initialPets.get(0);
		newPet.setId(null);

		assertSame("getPet did not ignore new pet", null, owner.getPet(newPet.getName(), true));
	}

	@Test
	void testGetPetsWithIgnoreNewTrueDoesNotIgnoreOldPets() {
		Pet newPet = initialPets.get(0);
		newPet.setId(1376);

		assertSame("getPet did ignored old pet", newPet, owner.getPet(newPet.getName(), true));
	}

	@Test
	void testGetPetIsCaseInsensitive() {
		Pet toBeFoundPet = initialPets.get(0);
		String upperCasedName = toBeFoundPet.getName().toUpperCase();
		assertSame("getPet is not case insensitive", toBeFoundPet, owner.getPet(upperCasedName));
	}
}
