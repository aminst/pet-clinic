package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PetManagerTest {

	private Logger spyLogger;
	private OwnerRepository mockOwnerRepository;
	private PetTimedCache mockPetTimedCache;

	private PetManager petManager;

	private Owner owner1, owner2, owner3;
	private Pet pet1, pet2, pet3;

	private void setUpOwners() {
		owner1 = new Owner();
		owner1.setId(1);
		owner2 = new Owner();
		owner2.setId(2);
		owner3 = new Owner();
		owner3.setId(3);
	}

	private void setUpPets() {
		pet1 = new Pet();
		pet1.setName("farshid");
		pet2 = new Pet();
		pet2.setName("faramarz");
		pet3 = new Pet();
		pet3.setName("fereydoon");
	}

	@BeforeEach
	void setup() {
		spyLogger = spy(Logger.class);
		mockOwnerRepository = mock(OwnerRepository.class);
		mockPetTimedCache = mock(PetTimedCache.class);

		petManager = new PetManager(mockPetTimedCache, mockOwnerRepository, spyLogger);

		setUpOwners();
		setUpPets();
	}

	private void tearDownOwners() {
		owner1 = null;
		owner2 = null;
		owner3 = null;
	}

	private void tearDownPets() {
		pet1 = null;
		pet2 = null;
		pet3 = null;
	}

	@AfterEach
	void tearDown() {
		spyLogger = null;
		mockOwnerRepository = null;
		mockPetTimedCache = null;
		petManager = null;

		tearDownOwners();
		tearDownPets();
	}

	/**
	 * Test doubles used: mock
	 * State/Behavior: State
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testFindOwnerReturnsExistingOwnerCorrectly() {
		when(mockOwnerRepository.findById(1)).thenReturn(owner1);

		assertSame(owner1, petManager.findOwner(1));
	}

	/**
	 * Test doubles used: mock
	 * State/Behavior: State
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testFindOwnerReturnsNullIfOwnerDoesNotExist() {
		when(mockOwnerRepository.findById(1)).thenReturn(owner1);

		assertSame(null, petManager.findOwner(4));
	}

	/**
	 * Test doubles used: spy
	 * State/Behavior: Behavior
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testFindOwnerLogsCorrectly() {
		petManager.findOwner(1);
		verify(spyLogger).info("find owner {}", 1);
	}

	/**
	 * Test doubles used: -
	 * State/Behavior: State
	 * Mockist/Classical: Classical
	 */
	@Test
	void testNewPetAddsANewPetToTheOwner() {
		owner1.addPet(pet1);
		owner1.addPet(pet2);
		List<Pet> beforePets = owner1.getPets();

		petManager.newPet(owner1);

		List<Pet> afterPets = new ArrayList<>(owner1.getPets());
		afterPets.removeAll(beforePets);
		assertEquals(1, afterPets.size());
	}

	/**
	 * Test doubles used: spy
	 * State/Behavior: Behavior
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testNewPetCallsAddPet() {
		Owner mockOwner = spy(Owner.class);

		petManager.newPet(mockOwner);

		verify(mockOwner).addPet(any());
	}

	/**
	 * Test doubles used: spy
	 * State/Behavior: Behavior
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testNewPetLogsCorrectly() {
		petManager.newPet(owner1);

		verify(spyLogger).info("add pet for owner {}", owner1.getId());
	}

	/**
	 * Test doubles used: mock
	 * State/Behavior: State
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testFindPetReturnsExistingPetCorrectly() {
		pet1.setId(1);
		when(mockPetTimedCache.get(1)).thenReturn(pet1);

		assertSame(pet1, petManager.findPet(1));
	}

	/**
	 * Test doubles used: mock
	 * State/Behavior: State
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testFindPetReturnsNullIfPetDoesNotExist() {
		pet1.setId(1);
		when(mockPetTimedCache.get(1)).thenReturn(pet1);

		assertSame(null, petManager.findPet(4));
	}

	/**
	 * Test doubles used: spy
	 * State/Behavior: Behavior
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testFindPetLogsCorrectly() {
		petManager.findPet(1);
		verify(spyLogger).info("find pet by id {}", 1);
	}

	/**
	 * Test doubles used: spy
	 * State/Behavior: Behavior
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testSavePetCallsSavePetCorrectly() {
		petManager.savePet(pet1, owner1);

		verify(mockPetTimedCache).save(pet1);
	}

	/**
	 * Test doubles used: -
	 * State/Behavior: State
	 * Mockist/Classical: Classical
	 */
	@Test
	void testSavePetAddsPetToOwner() {
		owner1.addPet(pet1);

		petManager.savePet(pet2, owner1);
		List afterPets = owner1.getPets();

		List expectedPets = new ArrayList<Pet>();
		expectedPets.add(pet1);
		expectedPets.add(pet2);
		PropertyComparator.sort(expectedPets, new MutableSortDefinition("name", true, true));

		assertArrayEquals(expectedPets.toArray(), afterPets.toArray());
	}

	/**
	 * Test doubles used: spy
	 * State/Behavior: Behavior
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testSavePetLogsCorrectly() {
		pet1.setId(1);
		petManager.savePet(pet1, owner1);

		verify(spyLogger).info("save pet {}", pet1.getId());
	}
}
