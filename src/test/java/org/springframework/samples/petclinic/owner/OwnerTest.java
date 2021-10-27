package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OwnerTest {

	private Owner owner;
	private Pet newPet, oldPet;

	@BeforeEach
	void setUp() {
		owner = new Owner();

		newPet = new Pet();
		newPet.setName("new pet");

		oldPet = new Pet();
		oldPet.setName("old pet");
		oldPet.setId(1);
	}

	@AfterEach
	void tearDown() {
		owner = null;

		newPet = null;
		oldPet = null;
	}

	/**
	 * Uses state testing
	 */
	@Test
	void testAddPetAddsNewPet() {
		owner.addPet(newPet);

		List<Pet> expectedPets = new ArrayList<Pet>(Arrays.asList(newPet));

		assertArrayEquals(expectedPets.toArray(), owner.getPets().toArray());
	}

	/**
	 * Uses behavior testing
	 */
	@Test
	void testAddPetCallsAddPet() {
		Owner mockOwner = mock(Owner.class);
		Set<Pet> spyPets = (Set<Pet>)spy(Set.class);
		when(mockOwner.getPetsInternal()).thenReturn(spyPets);
		doCallRealMethod().when(mockOwner).addPet(any());

		mockOwner.addPet(newPet);

		verify(spyPets).add(newPet);
	}

	/**
	 * Uses state testing
	 */
	@Test
	void testAddPetDoesNotAddOldPet() {
		owner.addPet(oldPet);

		List<Pet> expectedPets = new ArrayList<Pet>();

		assertArrayEquals(expectedPets.toArray(), owner.getPets().toArray());
	}

	/**
	 * Uses behavior testing
	 */
	@Test
	void testAddPetDoesNotCallAddPetForOldPet() {
		Owner mockOwner = mock(Owner.class);
		Set<Pet> spyPets = (Set<Pet>)spy(Set.class);
		when(mockOwner.getPetsInternal()).thenReturn(spyPets);
		doCallRealMethod().when(mockOwner).addPet(any());

		mockOwner.addPet(oldPet);

		verify(spyPets, never()).add(oldPet);
	}

	/**
	 * Uses state testing
	 */
	@Test
	void testAddPetSetsOwnerCorrectly() {
		owner.addPet(newPet);

		assertSame(owner, newPet.getOwner());
	}

	/**
	 * Uses behavior testing
	 */
	@Test
	void testAddPetCallsSetOwner() {
		Pet spyPet = spy(Pet.class);

		owner.addPet(spyPet);

		verify(spyPet).setOwner(owner);
	}
}
