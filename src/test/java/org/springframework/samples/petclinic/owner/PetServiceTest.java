package org.springframework.samples.petclinic.owner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assume.assumeTrue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class PetServiceTest {

	private PetService petService;
	private PetTimedCache mockPetTimedCache;
	static Pet pet0, pet1, pet2, pet3, pet4;

	private int id;
	private Pet expectedPet;

	public PetServiceTest(int id, Pet expectedPet) {
		this.id = id;
		this.expectedPet = expectedPet;

		mockPetTimedCache = mock(PetTimedCache.class);
		OwnerRepository mockOwnerRepository = mock(OwnerRepository.class);
		Logger mockLogger = mock(Logger.class);
		petService = new PetService(mockPetTimedCache, mockOwnerRepository, mockLogger);
	}

	@Before
	public void setup() {
		when(mockPetTimedCache.get(0)).thenReturn(pet0);
		when(mockPetTimedCache.get(1)).thenReturn(pet1);
		when(mockPetTimedCache.get(2)).thenReturn(pet2);
		when(mockPetTimedCache.get(3)).thenReturn(pet3);
		when(mockPetTimedCache.get(4)).thenReturn(pet4);
	}

	@Parameterized.Parameters
	public static Collection parameters() {
		pet0 = new Pet();
		pet0.setName("lovely cat");
		pet0.setId(0);
		pet1 = new Pet();
		pet1.setName("farshid");
		pet1.setId(1);
		pet2 = new Pet();
		pet2.setName("faramarz");
		pet2.setId(2);
		pet3 = new Pet();
		pet3.setName("fereydoon");
		pet3.setId(3);
		pet4 = new Pet();
		pet4.setName("test");
		pet4.setId(4);
		return Arrays.asList(new Object[][] {{0, pet0}, {1, pet1}, {2, pet2}, {3, pet3}, {4, pet4}, {5, null}});
	}

	@Test
	public void testFindPetFindsPetCorrectly() {
		assumeTrue(id >= 0);

		assertSame("findPet did not find the correct pet", expectedPet, petService.findPet(id));
	}
}
