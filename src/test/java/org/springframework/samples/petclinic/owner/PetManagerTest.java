package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
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
	private PetType cat, dog;

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

	private void setUpPetTypes() {
		cat = new PetType();
		cat.setName("cat");
		dog = new PetType();
		dog.setName("dog");
	}

	/**
	 * dummy test double was used to initialize PetManager
	 */
	@BeforeEach
	void setup() {
		spyLogger = spy(Logger.class);
		mockOwnerRepository = mock(OwnerRepository.class);
		mockPetTimedCache = mock(PetTimedCache.class);

		petManager = new PetManager(mockPetTimedCache, mockOwnerRepository, spyLogger);

		setUpOwners();
		setUpPets();
		setUpPetTypes();
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

	private void tearDownPetTypes() {
		cat = null;
		dog = null;
	}

	@AfterEach
	void tearDown() {
		spyLogger = null;
		mockOwnerRepository = null;
		mockPetTimedCache = null;
		petManager = null;

		tearDownOwners();
		tearDownPets();
		tearDownPetTypes();
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

	/**
	 * Test doubles used: mock
	 * State/Behavior: State
	 * Mockist/Classical: Classical
	 */
	@Test
	void testGetOwnerPetsReturnsCorrectPets() {
		owner1.addPet(pet1);
		owner1.addPet(pet2);
		when(mockOwnerRepository.findById(1)).thenReturn(owner1);

		List<Pet> pets = petManager.getOwnerPets(1);

		List expectedPets = new ArrayList<Pet>();
		expectedPets.add(pet1);
		expectedPets.add(pet2);
		PropertyComparator.sort(expectedPets, new MutableSortDefinition("name", true, true));

		assertArrayEquals(expectedPets.toArray(), pets.toArray());
	}

	/**
	 * Test doubles used: spy + mock
	 * State/Behavior: Behavior
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testGetOwnerLogsCorrectly() {
		when(mockOwnerRepository.findById(1)).thenReturn(owner1);
		petManager.getOwnerPets(1);

		verify(spyLogger).info("finding the owner's pets by id {}", 1);
	}

	/**
	 * Test doubles used: mock
	 * State/Behavior: State
	 * Mockist/Classical: Classical
	 */
	@Test
	void testGetOwnerPetTypesReturnsCorrectPetTypes() {
		pet1.setType(cat);
		pet2.setType(dog);
		owner1.addPet(pet1);
		owner1.addPet(pet2);

		when(mockOwnerRepository.findById(1)).thenReturn(owner1);

		Set<PetType> pets = petManager.getOwnerPetTypes(1);

		assertEquals(new HashSet<PetType>(Arrays.asList(cat, dog)), pets);
	}

	/**
	 * Test doubles used: spy + mock
	 * State/Behavior: Behavior
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testGetOwnerPetTypesLogsCorrectly() {
		when(mockOwnerRepository.findById(1)).thenReturn(owner1);
		petManager.getOwnerPetTypes(1);

		verify(spyLogger).info("finding the owner's petTypes by id {}", 1);
	}


	/**
	 * Test doubles used: mock
	 * State/Behavior: State
	 * Mockist/Classical: Classical
	 */
	@Test
	void testGetVisitsBetweenExcludesOtherDates() {
		Visit visit1 = new Visit().setDate(LocalDate.of(2018, 4, 1));
		Visit visit2 = new Visit().setDate(LocalDate.of(2019, 2, 10));
		Visit visit3 = new Visit().setDate(LocalDate.of(2021, 10, 13));
		pet2.addVisit(visit1);
		pet2.addVisit(visit2);
		pet2.addVisit(visit3);

		when(mockPetTimedCache.get(2)).thenReturn(pet2);

		List<Visit> pets = petManager.getVisitsBetween(
			2,
			LocalDate.of(2019,1,9),
			LocalDate.of(2021, 11, 1)
		);

		List<Visit> expectedPets = new ArrayList<>(Arrays.asList(visit2, visit3));


		assertArrayEquals(expectedPets.toArray(), pets.toArray());
	}

	/**
	 * Test doubles used: spy + mock
	 * State/Behavior: Behavior
	 * Mockist/Classical: Mockist
	 */
	@Test
	void testGetVisitsBetweenLogsCorrectly() {
		when(mockPetTimedCache.get(2)).thenReturn(pet2);

		int petId = 2;
		LocalDate startDate = LocalDate.of(2019,1,9);
		LocalDate endDate = LocalDate.of(2021, 11, 1);

		petManager.getVisitsBetween(petId, startDate, endDate);

		verify(spyLogger).info("get visits for pet {} from {} since {}", petId, startDate, endDate);
	}
}
