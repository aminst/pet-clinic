package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.utility.PetTimedCache;

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
		pet1.setId(1);
		pet2 = new Pet();
		pet2.setName("faramarz");
		pet2.setId(2);
		pet3 = new Pet();
		pet3.setName("fereydoon");
		pet3.setId(3);
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
	void testFindOwnerLoggsCorrectly() {
		petManager.findOwner(1);
		verify(spyLogger).info("find owner {}", 1);
	}
}
